package logic.execution;

import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.instruction.Instruction;
import logic.model.program.Program;

import java.util.Map;

public class DebuggerExecutor implements ProgramExecutor{
    private Program program;
    private ExecutionContext context;
    private InstructionsQueue instructionsQueue;
    private int cyclesCount;
    private Instruction currentInstructionToExecute;
    private Instruction nextInstructionToExecute;
    private Instruction previousInstructionExecuted;
    private boolean isFinished = false;

    public DebuggerExecutor(Program program, Map<Variable, Long> inputVariablesMap) {
        loadProgramForDebugging(program, inputVariablesMap);
    }

    @Override
    public Map<Variable, Long> run(Map<Variable, Long> inputVariablesMap) {
        context = new ExecutionContextImpl(inputVariablesMap,
                program.getAllInstructionsWorkVariables());
        instructionsQueue = new InstructionsQueue(program.getInstructions());
        Instruction currentInstruction = instructionsQueue.getFirstInQueue();
        Label nextLabel;

        do {
            nextLabel = currentInstruction.execute(context);
            cyclesCount += currentInstruction.getCycles();

            if (nextLabel == FixedLabel.EMPTY) {
                currentInstruction = instructionsQueue.next();
            } else if (nextLabel != FixedLabel.EXIT) {
                instructionsQueue.setQueueBegin(nextLabel);
                currentInstruction = instructionsQueue.getFirstInQueue();
            }
        } while (nextLabel != FixedLabel.EXIT && currentInstruction != null);

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
    }

    public Map<Variable, Long> stepOver(){
        Label nextLabel = currentInstructionToExecute.execute(context);
        cyclesCount += currentInstructionToExecute.getCycles();
        if(nextLabel != FixedLabel.EXIT && currentInstructionToExecute != null){
            previousInstructionExecuted = currentInstructionToExecute;
            if (nextLabel == FixedLabel.EMPTY) {
                currentInstructionToExecute = instructionsQueue.next();
            } else if (nextLabel != FixedLabel.EXIT) {
                instructionsQueue.setQueueBegin(nextLabel);
                currentInstructionToExecute = instructionsQueue.getFirstInQueue();
            }
        }
        else{
            isFinished = true;
        }

        return context.getVariablesStatus();
    }

    public boolean isFinished() {
        return isFinished;
    }
}
