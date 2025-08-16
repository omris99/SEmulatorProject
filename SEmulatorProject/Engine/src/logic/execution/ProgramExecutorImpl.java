package logic.execution;

import logic.model.instruction.Instruction;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.program.Program;
import logic.model.argument.variable.Variable;


import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class ProgramExecutorImpl implements ProgramExecutor{

    private final Program program;
    private InstructionsQueue instructionsQueue;

    public ProgramExecutorImpl(Program program) {
        this.program = program;
        instructionsQueue = new InstructionsQueue(program.getInstructions());
    }

    @Override
    public long run(Long... input) {
        ExecutionContext context = new ExecutionContextImpl(program.getAllInstructionsInputs(),
                program.getAllInstructionsWorkVariables(), input);
        Instruction currentInstruction = instructionsQueue.getFirstInQueue();
        Label nextLabel;

        do {
            nextLabel = currentInstruction.execute(context);

            if (nextLabel == FixedLabel.EMPTY) {
                currentInstruction = instructionsQueue.next();
            } else if (nextLabel != FixedLabel.EXIT) {
                instructionsQueue.setQueueBegin(nextLabel);
                currentInstruction = instructionsQueue.getFirstInQueue();
            }
        } while (nextLabel != FixedLabel.EXIT && currentInstruction != null);

        return context.getVariableValue(Variable.RESULT);
    }

    @Override
    public Map<Variable, Long> variableState() {
        return Map.of();
    }
}
