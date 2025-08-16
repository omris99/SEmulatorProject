package logic.model.instruction.synthetic;

import logic.model.argument.Argument;
import logic.execution.ExecutionContext;
import logic.model.argument.constant.Constant;
import logic.model.instruction.AbstractInstruction;
import logic.model.instruction.InstructionArgument;
import logic.model.instruction.InstructionData;
import logic.model.instruction.InstructionWithArguments;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;

import java.util.HashMap;
import java.util.Map;

public class JumpEqualConstantInstruction extends AbstractInstruction implements InstructionWithArguments {
    Map<InstructionArgument, Argument> arguments;

    public JumpEqualConstantInstruction(Variable variable, Argument jeConstantLabel, Argument constantValue) {
        this(variable, jeConstantLabel, constantValue, FixedLabel.EMPTY);
    }

    public JumpEqualConstantInstruction(Variable variable, Argument jeConstantLabel, Argument constantValue, Label label) {
        super(InstructionData.JUMP_EQUAL_CONSTANT, variable, label);
        arguments = new HashMap<>();
        arguments.put(InstructionArgument.JE_CONSTANT_LABEL, jeConstantLabel);
        arguments.put(InstructionArgument.CONSTANT_VALUE, constantValue);
    }

    @Override
    public Label execute(ExecutionContext context) {
        long variableValue = context.getVariableValue(getVariable());

        if (variableValue == ((Constant)arguments.get(InstructionArgument.CONSTANT_VALUE)).getValue()) {
            return (Label) arguments.get(InstructionArgument.JE_CONSTANT_LABEL);
        }

        return FixedLabel.EMPTY;
    }

    @Override
    public String getInstructionDisplayFormat() {
        String displayFormat = String.format("IF %s = %s GOTO %s",
                getVariable().getRepresentation(), (arguments.get(InstructionArgument.CONSTANT_VALUE).getRepresentation()),
                (arguments.get(InstructionArgument.JE_CONSTANT_LABEL).getRepresentation()));

        return super.getInstructionDisplayFormat(displayFormat);
    }

    @Override
    public Map<InstructionArgument, Argument> getArguments() {
        return arguments;
    }
}
