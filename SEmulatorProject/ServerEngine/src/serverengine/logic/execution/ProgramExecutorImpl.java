package serverengine.logic.execution;

import serverengine.logic.model.argument.label.FixedLabel;
import serverengine.logic.model.argument.label.Label;
import serverengine.logic.model.argument.variable.Variable;
import serverengine.logic.model.instruction.Instruction;
import serverengine.logic.model.program.Program;

import java.util.Map;

public class ProgramExecutorImpl implements ProgramExecutor{

    private final Program program;
    private InstructionsQueue instructionsQueue;
    private int cyclesCount;
    private final int degree;

    public ProgramExecutorImpl(Program program) {
        this.program = program;
        this.instructionsQueue = new InstructionsQueue(program.getInstructions());
        this.cyclesCount = 0;
        this.degree = program.getDegree();
    }

    @Override
    public Map<Variable, Long> run(Map<Variable, Long> inputVariablesMap) {
        ExecutionContext context = new ExecutionContextImpl(inputVariablesMap,
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
                currentInstruction = instructionsQueue.jumpToLabel(nextLabel);
            }
        } while (nextLabel != FixedLabel.EXIT && currentInstruction != null);

        return context.getVariablesStatus();
    }

    public int getCyclesCount(){
        return cyclesCount;
    }
    public int getCurrentSessionCyclesCount(){
        return cyclesCount;
    }
}
