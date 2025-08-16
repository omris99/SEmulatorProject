package logic.model.instruction.synthetic;

import logic.model.argument.Argument;
import logic.execution.ExecutionContext;
import logic.model.instruction.AbstractInstruction;
import logic.model.instruction.InstructionArgument;
import logic.model.instruction.InstructionData;
import logic.model.instruction.InstructionWithArguments;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;

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
        String displayFormat = String.format(String.format("IF %s = 0 GOTO %s", getVariable().getRepresentation(),
                arguments.get(InstructionArgument.JZ_LABEL).getRepresentation()));

        return super.getInstructionDisplayFormat(displayFormat);
    }

    @Override
    public Map<InstructionArgument, Argument> getArguments() {
        return arguments;
    }
}
