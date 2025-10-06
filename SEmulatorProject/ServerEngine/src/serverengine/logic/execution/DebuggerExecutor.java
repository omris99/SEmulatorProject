package serverengine.logic.execution;

import serverengine.logic.model.argument.label.FixedLabel;
import serverengine.logic.model.argument.label.Label;
import serverengine.logic.model.argument.variable.Variable;
import serverengine.logic.model.instruction.ArchitectureType;
import serverengine.logic.model.instruction.Instruction;
import serverengine.logic.model.program.Program;

import java.util.*;

public class DebuggerExecutor implements ProgramExecutor {
    private Program program;
    private ExecutionContext context;
    private final List<ExecutionContext> contextsHistory;
    private List<InstructionsQueue> queueHistory;
    private InstructionsQueue instructionsQueue;
    private final Map<Variable, Long> initialInputVariablesMap;
    private ExecutionContext initialContext;
    private int cyclesCount;
    private Instruction currentInstructionToExecute;
    private int creditsCost;
    private Instruction nextInstructionToExecute;
    private Instruction previousInstructionExecuted;
    private boolean isPausedAtBreakpoint;
    private boolean isFinished = false;
    private final ArchitectureType architecture;

    public DebuggerExecutor(Program program, Map<Variable, Long> inputVariablesMap, ArchitectureType architecture) {
        this.contextsHistory = new LinkedList<>();
        this.initialInputVariablesMap = new LinkedHashMap<>(inputVariablesMap);
        loadProgramForDebugging(program, inputVariablesMap);
        this.architecture = architecture;
    }

    @Override
    public Map<Variable, Long> run(Map<Variable, Long> inputVariablesMap) {
        Label nextLabel;

        do {
            contextsHistory.add(context);
            context = contextsHistory.getLast().copy();

            if (currentInstructionToExecute.getBreakpoint() && !isPausedAtBreakpoint) {
                isPausedAtBreakpoint = true;
                return context.getVariablesStatus();
            }

            isPausedAtBreakpoint = false;
            nextLabel = currentInstructionToExecute.execute(context);
            cyclesCount += currentInstructionToExecute.getCycles();
            creditsCost += currentInstructionToExecute.getArchitectureType().getExecutionCost();

            if (nextLabel == FixedLabel.EMPTY) {
                currentInstructionToExecute = instructionsQueue.next();
            } else if (nextLabel != FixedLabel.EXIT) {
                currentInstructionToExecute = instructionsQueue.jumpToLabel(nextLabel);
            }
        } while (nextLabel != FixedLabel.EXIT && currentInstructionToExecute != null);
        stop();
        return context.getVariablesStatus();
    }

    @Override
    public int getCyclesCount() {
        return cyclesCount;
    }

    private void loadProgramForDebugging(Program program, Map<Variable, Long> inputVariablesMap) {
        this.program = program;
        initializeContext(inputVariablesMap);
        instructionsQueue = new InstructionsQueue(program.getInstructions());
        currentInstructionToExecute = instructionsQueue.getFirstInQueue();
        cyclesCount = 0;
        creditsCost = 0;
        isFinished = false;
    }

    private void initializeContext(Map<Variable, Long> inputVariablesMap) {
        context = new ExecutionContextImpl(inputVariablesMap,
                program.getAllInstructionsWorkVariables());
        initialContext = context;
    }

    public Map<Variable, Long> stepOver() {
        contextsHistory.add(context);

        context = contextsHistory.getLast().copy();

        Label nextLabel = currentInstructionToExecute.execute(context);
        cyclesCount += currentInstructionToExecute.getCycles();
        creditsCost += currentInstructionToExecute.getArchitectureType().getExecutionCost();
        if (nextLabel != FixedLabel.EXIT) {
            previousInstructionExecuted = currentInstructionToExecute;
            if (nextLabel == FixedLabel.EMPTY) {
                currentInstructionToExecute = instructionsQueue.next();
                if (currentInstructionToExecute == null) {
                    stop();
                }
            } else {
                currentInstructionToExecute = instructionsQueue.jumpToLabel(nextLabel);

            }
        } else {
            stop();
        }

        return context.getVariablesStatus();
    }

    public Map<Variable, Long> stepBackward() {
        if(contextsHistory.isEmpty()){
            return initialContext.getVariablesStatus();
        }

        Instruction previousInstruction = instructionsQueue.prev();
        cyclesCount -= previousInstruction.getCycles();
        currentInstructionToExecute = previousInstruction;

        context = contextsHistory.removeLast();

        return context.getVariablesStatus();
    }

    public int getCreditsCost() {
        return creditsCost;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void stop() {
        isFinished = true;
    }

    public int getProgramDegree() {
        return program.getDegree();
    }

    public Map<Variable, Long> getInitialInputVariablesMap() {
        return initialInputVariablesMap;
    }

    public Instruction getCurrentInstructionToExecute() {
        return currentInstructionToExecute;
    }

    public ArchitectureType getArchitecture() {
        return architecture;
    }
}
