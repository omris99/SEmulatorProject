package logic.model.instruction;

import logic.model.Argument;
import logic.model.execution.ExecutionContext;
import logic.model.label.FixedLabel;
import logic.model.label.Label;
import logic.model.variable.Variable;

import java.util.HashMap;
import java.util.Map;

public class JumpEqualConstantInstruction extends AbstractInstruction implements InstructionWithArguments{
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
//        long variableValue = context.getVariableValue(getVariable());
//
//        if (variableValue == 0) {
//            return (Label) arguments.get(InstructionArgument.JZ_LABEL);
//        }
//
//        return FixedLabel.EMPTY;
        return null;
    }

    @Override
    public String getInstructionDisplayFormat() {
        return String.format(String.format(String.format("IF %s = %s GOTO %s",
                getVariable()), arguments.get(InstructionArgument.JE_CONSTANT_LABEL),
                arguments.get(InstructionArgument.CONSTANT_VALUE)));
    }

    @Override
    public Map<InstructionArgument, Argument> getArguments() {
        return arguments;
    }
}
