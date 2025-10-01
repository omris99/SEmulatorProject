package logic.engine;

import dto.DTO;
import dto.InstructionDTO;
import dto.RunResultsDTO;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import logic.exceptions.InvalidXmlFileException;
import logic.exceptions.NumberNotInRangeException;
import logic.exceptions.XmlErrorType;
import logic.execution.DebuggerExecutor;
import logic.execution.ProgramExecutor;
import logic.execution.ProgramExecutorImpl;
import logic.instructiontree.InstructionsTree;
import logic.instructiontree.InstructionsTreeNode;
import logic.model.argument.Argument;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.argument.variable.VariableImpl;
import logic.model.argument.variable.VariableType;
import logic.model.functionsrepo.FunctionsRepo;
import logic.model.generated.SProgram;
import logic.model.instruction.Instruction;
import logic.model.mappers.ProgramMapper;
import logic.model.program.Program;
import logic.utils.Utils;

import java.io.File;
import java.util.*;

public class EmulatorEngine implements Engine {
    private Program mainProgram;
    private Program currentContextProgram;
    private Program currentOnScreenProgram;
    private DebuggerExecutor debuggerExecutor;
    private final Map<String, List<RunResultsDTO>> savedHistories;

    public EmulatorEngine() {
        this.savedHistories = new HashMap<>();
    }

    @Override
    public void loadProgram(String xmlPath) throws JAXBException, InvalidXmlFileException {
        if (!xmlPath.endsWith(".xml") || !(xmlPath.length() > 4)) {
            throw new InvalidXmlFileException(xmlPath, XmlErrorType.INVALID_EXTENSION);
        }

        File xmlFile = new File(xmlPath);
        if (!xmlFile.exists()) {
            throw new InvalidXmlFileException(xmlPath, XmlErrorType.FILE_MISSING);
        }

        JAXBContext jaxbContext = JAXBContext.newInstance(SProgram.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        SProgram sProgram = (SProgram) jaxbUnmarshaller.unmarshal(xmlFile);

        Program loadedProgram = ProgramMapper.toDomain(sProgram);
        Label problemLabel = loadedProgram.validate();
        if (problemLabel != FixedLabel.EMPTY) {
            throw new InvalidXmlFileException(xmlPath, XmlErrorType.UNKNOWN_LABEL, problemLabel.getRepresentation());
        }

        this.mainProgram = loadedProgram;
        setCurrentContextProgram(mainProgram);

        savedHistories.clear();
    }

    public void changeLoadedProgramToFunction(String functionName) {
        if (currentContextProgram == null) {
            throw new IllegalStateException("No program loaded. Load a program first.");
        }

        if (mainProgram.getName().equals(functionName)) {
            setCurrentContextProgram(mainProgram);
        } else {
            functionName = FunctionsRepo.getInstance().getFunctionNameByUserString(functionName);
            setCurrentContextProgram(FunctionsRepo.getInstance().getFunctionByName(functionName));
        }
    }

    @Override
    public DTO getLoadedProgramDTO() {
        return currentOnScreenProgram.createDTO();
    }

    @Override
    public DTO runLoadedProgramWithCommaSeperatedInput(int degree, String input) {
        ProgramExecutor executor = new ProgramExecutorImpl(currentContextProgram.getExpandedProgram(degree));
        Long[] inputs = Arrays.stream(input.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .toArray(Long[]::new);
        for (Long number : inputs) {
            if (number < 0) {
                throw new NumberNotInRangeException(Integer.parseInt(number.toString()));
            }
        }

        Map<Variable, Long> userInputToVariablesMap = mapUserInputToVariables(inputs);
        Map<Variable, Long> programUseInitialInputVariablesMap = createProgramUseInitialVariablesMap(inputs);

        Map<Variable, Long> finalVariablesResult = executor.run(new LinkedHashMap<>(programUseInitialInputVariablesMap));

        RunResultsDTO runResults = new RunResultsDTO(
                degree,
                finalVariablesResult.get(Variable.RESULT),
                userInputToVariablesMap,
                Utils.extractVariablesTypesFromMap(finalVariablesResult, VariableType.INPUT),
                Utils.extractVariablesTypesFromMap(finalVariablesResult, VariableType.WORK),
                executor.getCyclesCount());
        savedHistories.computeIfAbsent(currentOnScreenProgram.getName(), name -> new LinkedList<>()).add(runResults);

        return runResults;
    }

    public DTO runLoadedProgramWithDebuggerWindowInput(int degree, Map<String, String> guiUserInputMap) throws NumberFormatException, NumberNotInRangeException {
        Map<Variable, Long> userInputToVariablesMapConverted = convertGuiVariablesMapToDomainVariablesMap(guiUserInputMap);
        ProgramExecutor executor = new ProgramExecutorImpl(currentOnScreenProgram);

        Set<Variable> programActualInputVariables = getProgramInputVariablesFromOneToN();

        Map<Variable, Long> programInitialInputVariablesMap = new LinkedHashMap<>();
        for (Variable inputVariable : programActualInputVariables) {
            programInitialInputVariablesMap.put(
                    inputVariable, userInputToVariablesMapConverted.getOrDefault(inputVariable, 0L));
        }

        Map<Variable, Long> finalVariablesResult = executor.run(new LinkedHashMap<>(programInitialInputVariablesMap));

        RunResultsDTO runResults = new RunResultsDTO(
                degree,
                finalVariablesResult.get(Variable.RESULT),
                programInitialInputVariablesMap,
                Utils.extractVariablesTypesFromMap(finalVariablesResult, VariableType.INPUT),
                Utils.extractVariablesTypesFromMap(finalVariablesResult, VariableType.WORK),
                executor.getCyclesCount());
        savedHistories.computeIfAbsent(currentOnScreenProgram.getName(), name -> new LinkedList<>()).add(runResults);

        return runResults;
    }

    private Set<Variable> getProgramInputVariablesFromOneToN() {
        Set<Variable> inputVariables = new LinkedHashSet<>();

        int maxInputIndex = currentContextProgram.getAllInstructionsInputs().stream().map(Argument::getIndex).max(Comparator.naturalOrder()).orElse(0);
        for (int i = 1; i <= maxInputIndex; i++) {
            inputVariables.add(new VariableImpl(VariableType.INPUT, i));
        }

        return inputVariables;
    }

    private Map<Variable, Long> mapUserInputToVariables(Long... input) {
        int i = 1;
        Map<Variable, Long> userInputToVariablesMap = new LinkedHashMap<>();

        for (long value : input) {
            userInputToVariablesMap.put(new VariableImpl(VariableType.INPUT, i), value);
            i++;
        }

        return userInputToVariablesMap;
    }

    private Map<Variable, Long> createProgramUseInitialVariablesMap(Long... inputs) {
        Set<Variable> programActualInputVariables = getProgramInputVariablesFromOneToN();

        return Utils.createInputVariablesMap(programActualInputVariables, inputs);
    }

    public DTO showExpandedProgramOnScreen(int degree) {
        currentOnScreenProgram = currentContextProgram.getExpandedProgram(degree);
        return currentOnScreenProgram.createDTO();
    }

    @Override
    public List<RunResultsDTO> getHistory() {
        return savedHistories.getOrDefault(currentOnScreenProgram.getName(), new LinkedList<>());
    }

    public int getMaximalDegree() {
        return currentContextProgram.getMaximalDegree();
    }

    public boolean isProgramLoaded() {
        return !(mainProgram == null);
    }


    @Override
    public void quit() {
        System.exit(0);
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

    public DTO initDebuggingSession(int degree, Map<String, String> guiUserInputMap) throws NumberFormatException, NumberNotInRangeException {
        Map<Variable, Long> userInputToVariablesMapConverted = convertGuiVariablesMapToDomainVariablesMap(guiUserInputMap);
        Set<Variable> programActualInputVariables = getProgramInputVariablesFromOneToN();

        Map<Variable, Long> programInitialInputVariablesMap = new LinkedHashMap<>();
        for (Variable inputVariable : programActualInputVariables) {
            programInitialInputVariablesMap.put(
                    inputVariable, userInputToVariablesMapConverted.getOrDefault(inputVariable, 0L));
        }
        programInitialInputVariablesMap.put(Variable.RESULT, 0L);

        debuggerExecutor = new DebuggerExecutor(currentOnScreenProgram, new LinkedHashMap<>(programInitialInputVariablesMap));


        return new RunResultsDTO(
                degree,
                programInitialInputVariablesMap.get(Variable.RESULT),
                userInputToVariablesMapConverted,
                userInputToVariablesMapConverted,
                Utils.extractVariablesTypesFromMap(programInitialInputVariablesMap, VariableType.WORK),
                debuggerExecutor.getCyclesCount(),
                debuggerExecutor.isFinished());
    }

    public DTO stepOver() {
        if (debuggerExecutor == null) {
            throw new IllegalStateException("Debugging session not initialized. Call initDebuggingSession first.");
        }

        Map<Variable, Long> finalVariablesResult = debuggerExecutor.stepOver();

        RunResultsDTO debugResults = new RunResultsDTO(
                debuggerExecutor.getProgramDegree(),
                finalVariablesResult.get(Variable.RESULT),
                debuggerExecutor.getInitialInputVariablesMap(),
                Utils.extractVariablesTypesFromMap(finalVariablesResult, VariableType.INPUT),
                Utils.extractVariablesTypesFromMap(finalVariablesResult, VariableType.WORK),
                debuggerExecutor.getCyclesCount(),
                debuggerExecutor.isFinished()
        );
        if (debuggerExecutor.isFinished()) {
            savedHistories.computeIfAbsent(currentOnScreenProgram.getName(), name -> new LinkedList<>()).add(debugResults);
        }

        return debugResults;
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
                debuggerExecutor.isFinished()
        );
    }

    public void stopDebuggingSession() {
        debuggerExecutor.stop();
    }

    public DTO resumeDebuggingSession() {
        if (debuggerExecutor == null) {
            throw new IllegalStateException("Debugging session not initialized. Call initDebuggingSession first.");
        }

        Map<Variable, Long> finalVariablesResult = debuggerExecutor.run(new LinkedHashMap<>());

        RunResultsDTO debugResults = new RunResultsDTO(
                debuggerExecutor.getProgramDegree(),
                finalVariablesResult.get(Variable.RESULT),
                debuggerExecutor.getInitialInputVariablesMap(),
                Utils.extractVariablesTypesFromMap(finalVariablesResult, VariableType.INPUT),
                Utils.extractVariablesTypesFromMap(finalVariablesResult, VariableType.WORK),
                debuggerExecutor.getCyclesCount(),
                debuggerExecutor.isFinished());
        if (debuggerExecutor.isFinished()) {
            savedHistories.computeIfAbsent(currentOnScreenProgram.getName(), name -> new LinkedList<>()).add(debugResults);
        }

        return debugResults;
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

    public InstructionsTree getSpecificExpansionInstructionsTree(){
        Program fullExpandedProgram = currentContextProgram.getExpandedProgram(currentContextProgram.getMaximalDegree());
        return fullExpandedProgram.getInstructionsTree();
    }
}
