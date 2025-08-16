package logic.execution;

import logic.model.instruction.Instruction;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.program.Program;
import logic.model.argument.variable.Variable;


import java.util.Map;

public class ProgramExecutorImpl implements ProgramExecutor{

    private final Program program;

    public ProgramExecutorImpl(Program program) {
        this.program = program;
    }

    @Override
    public long run(Long... input) {

        ExecutionContext context = null; // create the context with inputs.

        Instruction currentInstruction = program.getInstructions().get(0);
        Label nextLabel;
        do {
            nextLabel = currentInstruction.execute(context);

            if (nextLabel == FixedLabel.EMPTY) {
                // set currentInstruction to the next instruction in line
            } else if (nextLabel != FixedLabel.EXIT) {
                // need to find the instruction at 'nextLabel' and set current instruction to it
            }
        } while (nextLabel != FixedLabel.EXIT);

        return context.getVariableValue(Variable.RESULT);
    }

    @Override
    public Map<Variable, Long> variableState() {
        return Map.of();
    }
}
