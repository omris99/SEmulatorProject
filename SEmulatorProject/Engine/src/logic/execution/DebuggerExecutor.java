package logic.execution;

import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.instruction.Instruction;
import logic.model.program.Program;

import java.util.*;

public class DebuggerExecutor implements ProgramExecutor {
    private Program program;
    private ExecutionContext context;
    private List<ExecutionContext> contextsHistory;
    private List<InstructionsQueue> queueHistory;
    private InstructionsQueue instructionsQueue;
    private final Map<Variable, Long> initialInputVariablesMap;
    private ExecutionContext initialContext;
    private int cyclesCount;
    private Instruction currentInstructionToExecute;
    private Instruction nextInstructionToExecute;
    private Instruction previousInstructionExecuted;
    private boolean isFinished = false;

    public DebuggerExecutor(Program program, Map<Variable, Long> inputVariablesMap) {
        this.contextsHistory = new LinkedList<>();
        this.initialInputVariablesMap = new LinkedHashMap<>(inputVariablesMap);
        loadProgramForDebugging(program, inputVariablesMap);
    }

    @Override
    public Map<Variable, Long> run(Map<Variable, Long> inputVariablesMap) {
        Label nextLabel;

        do {
            nextLabel = currentInstructionToExecute.execute(context);
            cyclesCount += currentInstructionToExecute.getCycles();

            if (nextLabel == FixedLabel.EMPTY) {
                currentInstructionToExecute = instructionsQueue.next();
            } else if (nextLabel != FixedLabel.EXIT) {
                instructionsQueue.setQueueBegin(nextLabel);
                currentInstructionToExecute = instructionsQueue.getFirstInQueue();
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
        isFinished = false;
    }

    private void initializeContext(Map<Variable, Long> inputVariablesMap) {
        context = new ExecutionContextImpl(inputVariablesMap,
                program.getAllInstructionsWorkVariables());
        initialContext = context;
    }

    public Map<Variable, Long> stepOver() {
        if(contextsHistory.isEmpty()) {
            context = initialContext.copy();
        }
        else {
            context = contextsHistory.getLast();
        }

        Label nextLabel = currentInstructionToExecute.execute(context);
        cyclesCount += currentInstructionToExecute.getCycles();
        if (nextLabel != FixedLabel.EXIT) {
            previousInstructionExecuted = currentInstructionToExecute;
            if (nextLabel == FixedLabel.EMPTY) {
                currentInstructionToExecute = instructionsQueue.next();
                if (currentInstructionToExecute == null) {
                    stop();
                }
            } else {
                currentInstructionToExecute = instructionsQueue.setQueueBegin(nextLabel);

            }
        } else {
            stop();
        }

        contextsHistory.add(context);
        return context.getVariablesStatus();
    }

    public Map<Variable, Long> stepBackward() {
        if(contextsHistory.isEmpty()){
            return initialContext.getVariablesStatus();
        }

        Instruction previousInstruction = instructionsQueue.prev();
        cyclesCount -= previousInstruction.getCycles();
        if(previousInstruction != null){
            currentInstructionToExecute = previousInstruction;
        }

        contextsHistory.removeLast();

        if(contextsHistory.isEmpty()){
            return initialContext.getVariablesStatus();
        }
        return context.getVariablesStatus();
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
}
