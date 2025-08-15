package logic.model.instruction;

import logic.model.Argument;
import logic.model.execution.ExecutionContext;
import logic.model.label.FixedLabel;
import logic.model.label.Label;
import logic.model.variable.Variable;

import java.util.HashMap;
import java.util.Map;

public class JumpZeroInstruction extends AbstractInstruction implements InstructionWithArguments {
    Map<InstructionArgument, Argument> arguments;

    public JumpZeroInstruction(Variable variable, Argument jzLabel) {
        this(variable, jzLabel, FixedLabel.EMPTY);
    }

    public JumpZeroInstruction(Variable variable, Argument jzLabel, Label label) {
        super(InstructionData.JUMP_ZERO, variable, label);
        arguments = new HashMap<>();
        arguments.put(InstructionArgument.JZ_LABEL, jzLabel);
    }

    @Override
    public Label execute(ExecutionContext context) {
        long variableValue = context.getVariableValue(getVariable());

        if (variableValue == 0) {
            return (Label) arguments.get(InstructionArgument.JZ_LABEL);
        }

        return FixedLabel.EMPTY;
    }

    @Override
    public String getInstructionDisplayFormat() {
        return String.format(String.format("IF %s = 0 GOTO %s", getVariable().getRepresentation(),
                arguments.get(InstructionArgument.JZ_LABEL).getArgumentString()));
    }

    @Override
    public Map<InstructionArgument, Argument> getArguments() {
        return arguments;
    }
}
