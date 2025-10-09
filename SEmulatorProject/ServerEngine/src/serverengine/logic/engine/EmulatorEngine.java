package serverengine.logic.engine;

import clientserverdto.DTO;
import clientserverdto.InstructionDTO;
import clientserverdto.RunResultsDTO;
import clientserverdto.ExecutionHistoryDTO;
import serverengine.logic.exceptions.CreditBalanceTooLowForInitialChargeException;
import serverengine.logic.exceptions.InvalidArchitectureException;
import serverengine.logic.exceptions.CreditBalanceTooLowException;
import serverengine.logic.exceptions.NumberNotInRangeException;
import serverengine.logic.execution.DebuggerExecutor;
import serverengine.logic.instructiontree.InstructionsTree;
import serverengine.logic.model.argument.Argument;
import serverengine.logic.model.argument.variable.Variable;
import serverengine.logic.model.argument.variable.VariableImpl;
import serverengine.logic.model.argument.variable.VariableType;
import serverengine.logic.model.functionsrepo.ProgramsRepo;
import serverengine.logic.model.functionsrepo.UploadedProgram;
import serverengine.logic.model.instruction.ArchitectureType;
import serverengine.logic.model.instruction.Instruction;
import serverengine.logic.model.program.Function;
import serverengine.logic.model.program.Program;
import serverengine.logic.utils.Utils;

import java.util.*;

public class EmulatorEngine implements Engine {
    private Program mainProgram;
    private Program currentContextProgram;
    private Program currentOnScreenProgram;
    private DebuggerExecutor debuggerExecutor;
    private final List<ExecutionHistoryDTO> executionsHistory;
    private RunResultsDTO lastDebuggerRunResult;
    private int currentExecutionNumber;
    private long creditsUsed;
    private long creditsBalance;

    public EmulatorEngine() {
        this.executionsHistory = new LinkedList<>();
        this.currentExecutionNumber = 1;
        this.creditsUsed = 0;
    }

    public void setMainProgram(UploadedProgram uploadedProgram) {
        this.mainProgram = uploadedProgram.getProgram();
        setCurrentContextProgram(mainProgram);
    }

    public void changeLoadedProgramToFunction(String functionName) {
        if (currentContextProgram == null) {
            throw new IllegalStateException("No program loaded. Load a program first.");
        }

        if (mainProgram.getName().equals(functionName)) {
            setCurrentContextProgram(mainProgram);
        } else {
            functionName = ProgramsRepo.getInstance().getFunctionNameByUserString(functionName);
            setCurrentContextProgram(ProgramsRepo.getInstance().getFunctionByName(functionName));
        }
    }

    @Override
    public DTO getLoadedProgramDTO() {
        return currentOnScreenProgram.createDTO();
    }

    private void addExecutionToHistoryAndUpdateUploadedProgramData(RunResultsDTO runResults) {
        executionsHistory.add(new ExecutionHistoryDTO(
                currentExecutionNumber,
                runResults,
                currentOnScreenProgram instanceof Function ? "Function" : "Program",
                currentOnScreenProgram.getRepresentation(),
                ProgramsRepo.getInstance().getProgramOrFunctionByName(currentOnScreenProgram.getName()).createDTO()));
        currentExecutionNumber++;
        ProgramsRepo.getInstance().getProgramOrFunctionByName(currentOnScreenProgram.getName()).updateDataAfterExecution(runResults.getTotalCyclesCount());
    }

    public DTO runLoadedProgramWithDebuggerWindowInput(int degree, Map<String, String> guiUserInputMap, ArchitectureType architecture) throws NumberFormatException, NumberNotInRangeException, CreditBalanceTooLowException {
        RunResultsDTO runResults;
        initDebuggingSession(degree, guiUserInputMap, architecture);

        do {
            runResults = executeNextInstructionOnDebugger();
        } while (!runResults.isFinished());

        addExecutionToHistoryAndUpdateUploadedProgramData(runResults);

        return runResults;
    }

    private void chargeCredits(long creditsCost) throws CreditBalanceTooLowException {
        if (creditsCost > creditsBalance) {
            throw new CreditBalanceTooLowException(creditsCost, creditsBalance);
        }

        this.creditsUsed += creditsCost;
        this.creditsBalance -= creditsCost;
    }

    private Set<Variable> getProgramInputVariablesFromOneToN() {
        Set<Variable> inputVariables = new LinkedHashSet<>();

        int maxInputIndex = currentContextProgram.getAllInstructionsInputs().stream().map(Argument::getIndex).max(Comparator.naturalOrder()).orElse(0);
        for (int i = 1; i <= maxInputIndex; i++) {
            inputVariables.add(new VariableImpl(VariableType.INPUT, i));
        }

        return inputVariables;
    }

    public DTO showExpandedProgramOnScreen(int degree) {
        currentOnScreenProgram = currentContextProgram.getExpandedProgram(degree);
        return currentOnScreenProgram.createDTO();
    }

    @Override
    public List<ExecutionHistoryDTO> getHistory() {
        return executionsHistory;
    }

    private void checkArchitectureCompatibilityAndChargeInitialCredits(ArchitectureType architecture) throws CreditBalanceTooLowForInitialChargeException {
        long averageExecutionCost = ProgramsRepo.getInstance().getProgramOrFunctionByName(currentOnScreenProgram.getName()).getAverageCyclesPerExecution();

        if (architecture.getNumber() < currentOnScreenProgram.getMinimalArchitectureType().getNumber()) {
            throw new InvalidArchitectureException(architecture.getUserString(), currentOnScreenProgram.getMinimalArchitectureType().getUserString());
        } else if (architecture.getExecutionCost() + averageExecutionCost > creditsBalance) {
            throw new CreditBalanceTooLowForInitialChargeException(
                    architecture.getExecutionCost() + averageExecutionCost,
                    creditsBalance,
                    architecture.getExecutionCost(),
                    averageExecutionCost);
        }

        try {
            chargeCredits(architecture.getExecutionCost());
        } catch (CreditBalanceTooLowException e){
            throw new CreditBalanceTooLowForInitialChargeException(
                    architecture.getExecutionCost() + averageExecutionCost,
                    creditsBalance,
                    architecture.getExecutionCost(),
                    averageExecutionCost);
        }
    }

    private Map<Variable, Long> convertGuiVariablesMapToDomainVariablesMap(Map<String, String> guiVariablesMap) {
        Map<Variable, Long> userInputToVariablesMapConverted = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : guiVariablesMap.entrySet()) {
            long value = Long.parseLong(entry.getValue());
            if (value < 0) {
                throw new NumberNotInRangeException(Integer.parseInt(Long.toString(value)));
            }

            userInputToVariablesMapConverted.put(new VariableImpl(entry.getKey()), value);
        }

        return userInputToVariablesMapConverted;
    }

    public DTO initDebuggingSession(int degree, Map<String, String> guiUserInputMap, ArchitectureType architecture) throws NumberFormatException, NumberNotInRangeException, CreditBalanceTooLowForInitialChargeException {
        checkArchitectureCompatibilityAndChargeInitialCredits(architecture);
        Map<Variable, Long> userInputToVariablesMapConverted = convertGuiVariablesMapToDomainVariablesMap(guiUserInputMap);
        Set<Variable> programActualInputVariables = getProgramInputVariablesFromOneToN();

        Map<Variable, Long> programInitialInputVariablesMap = new LinkedHashMap<>();
        for (Variable inputVariable : programActualInputVariables) {
            programInitialInputVariablesMap.put(
                    inputVariable, userInputToVariablesMapConverted.getOrDefault(inputVariable, 0L));
        }
        programInitialInputVariablesMap.put(Variable.RESULT, 0L);

        debuggerExecutor = new DebuggerExecutor(currentOnScreenProgram, new LinkedHashMap<>(programInitialInputVariablesMap), architecture);

        lastDebuggerRunResult = new RunResultsDTO(
                degree,
                programInitialInputVariablesMap.get(Variable.RESULT),
                userInputToVariablesMapConverted,
                userInputToVariablesMapConverted,
                Utils.extractVariablesTypesFromMap(programInitialInputVariablesMap, VariableType.WORK),
                debuggerExecutor.getCyclesCount(),
                debuggerExecutor.getArchitecture().getUserString(),
                debuggerExecutor.isFinished());

        return lastDebuggerRunResult;
    }

    public DTO stepOver() throws CreditBalanceTooLowException {
        if (debuggerExecutor == null) {
            throw new IllegalStateException("Debugging session not initialized. Call initDebuggingSession first.");
        }

        RunResultsDTO debugResults = executeNextInstructionOnDebugger();

        if (debuggerExecutor.isFinished()) {
            addExecutionToHistoryAndUpdateUploadedProgramData(debugResults);
        }

        return debugResults;
    }

    private void chargeCreditsForCurrentInstruction() throws CreditBalanceTooLowException {
        try {
            chargeCredits(debuggerExecutor.getCurrentInstructionToExecute().getCycles());
        } catch (CreditBalanceTooLowException e) {
            stopDebuggingSession();
            throw e;
        }
    }

    public DTO stepBackward() {
        if (debuggerExecutor == null) {
            throw new IllegalStateException("Debugging session not initialized. Call initDebuggingSession first.");
        }

        Map<Variable, Long> finalVariablesResult = debuggerExecutor.stepBackward();

        return new RunResultsDTO(
                debuggerExecutor.getProgramDegree(),
                finalVariablesResult.get(Variable.RESULT),
                debuggerExecutor.getInitialInputVariablesMap(),
                Utils.extractVariablesTypesFromMap(finalVariablesResult, VariableType.INPUT),
                Utils.extractVariablesTypesFromMap(finalVariablesResult, VariableType.WORK),
                debuggerExecutor.getCyclesCount(),
                debuggerExecutor.getArchitecture().getUserString(),
                debuggerExecutor.isFinished()
        );
    }

    public void stopDebuggingSession() {
        debuggerExecutor.stop();
    }

    public DTO resume() throws CreditBalanceTooLowException {
        if (debuggerExecutor == null) {
            throw new IllegalStateException("Debugging session not initialized. Call initDebuggingSession first.");
        }

        RunResultsDTO debugResults = lastDebuggerRunResult;
        boolean isExitedLoopBecauseBreakpoint = false;

        do {
            debugResults = executeNextInstructionOnDebugger();

            if (debuggerExecutor.isPausedAtBreakpoint()) {
                isExitedLoopBecauseBreakpoint = true;
                break;
            }
        } while (!debugResults.isFinished());

        if (!isExitedLoopBecauseBreakpoint) {
            addExecutionToHistoryAndUpdateUploadedProgramData(debugResults);
        }

        return debugResults;
    }

    private RunResultsDTO executeNextInstructionOnDebugger() throws CreditBalanceTooLowException {
        try {
            chargeCreditsForCurrentInstruction();
        } catch (CreditBalanceTooLowException e) {
            addExecutionToHistoryAndUpdateUploadedProgramData(lastDebuggerRunResult);
            throw e;
        }

        Map<Variable, Long> finalVariablesResult = debuggerExecutor.stepOver();

        lastDebuggerRunResult = new RunResultsDTO(
                debuggerExecutor.getProgramDegree(),
                finalVariablesResult.get(Variable.RESULT),
                debuggerExecutor.getInitialInputVariablesMap(),
                Utils.extractVariablesTypesFromMap(finalVariablesResult, VariableType.INPUT),
                Utils.extractVariablesTypesFromMap(finalVariablesResult, VariableType.WORK),
                debuggerExecutor.getCyclesCount(),
                debuggerExecutor.getArchitecture().getUserString(),
                debuggerExecutor.isFinished());

        return lastDebuggerRunResult;
    }

    public DTO getNextInstructionToExecute() {
        return debuggerExecutor.getCurrentInstructionToExecute().getInstructionDTO();
    }

    public InstructionDTO updateInstructionBreakpoint(int index, boolean isSet) {
        Instruction instruction = currentOnScreenProgram.getInstructions().stream().filter(instr -> instr.getIndex() == index).findFirst().orElse(null);
        if (instruction != null) {
            instruction.setBreakpoint(isSet);
        }

        return instruction.getInstructionDTO();
    }

    private void setCurrentContextProgram(Program program) {
        currentContextProgram = program;
        currentOnScreenProgram = program;
    }

    public InstructionsTree getOnScreenProgramInstructionsTree() {
        return currentOnScreenProgram.getInstructionsTree();
    }

    public InstructionsTree getSpecificExpansionInstructionsTree() {
        Program fullExpandedProgram = currentContextProgram.getExpandedProgram(currentContextProgram.getMaximalDegree());
        return fullExpandedProgram.getInstructionsTree();
    }

    public int getExecutionsPerformed() {
        return executionsHistory.size();
    }

    public String getLoadedProgramName() {
        return currentOnScreenProgram.getName();
    }

    public long getCreditsUsed() {
        return creditsUsed;
    }

    public long getCreditsBalance() {
        return creditsBalance;
    }

    public void chargeCredits(String amount) {
        try {
            int creditsToCharge = Integer.parseInt(amount);
            if (creditsToCharge < 0) {
                throw new NumberNotInRangeException(creditsToCharge);
            }

            this.creditsBalance += creditsToCharge;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid number format for credits: " + amount);
        }
    }
}
