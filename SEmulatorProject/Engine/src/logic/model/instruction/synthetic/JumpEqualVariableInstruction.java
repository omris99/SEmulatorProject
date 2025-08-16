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

public class JumpEqualVariableInstruction extends AbstractInstruction implements InstructionWithArguments {
    Map<InstructionArgument, Argument> arguments;

    public JumpEqualVariableInstruction(Variable variable, Argument jeVariableLabel, Argument variableName) {
        this(variable, jeVariableLabel, variableName, FixedLabel.EMPTY);
    }

    public JumpEqualVariableInstruction(Variable variable, Argument jeVariableLabel, Argument variableName, Label label) {
        super(InstructionData.JUMP_EQUAL_VARIABLE, variable, label);
        arguments = new HashMap<>();
        arguments.put(InstructionArgument.JE_VARIABLE_LABEL, jeVariableLabel);
        arguments.put(InstructionArgument.VARIABLE_NAME, variableName);
    }

    @Override
    public Label execute(ExecutionContext context) {
        long variableValue = context.getVariableValue(getVariable());

        if (variableValue == context.getVariableValue((Variable)arguments.get(InstructionArgument.VARIABLE_NAME))) {
            return (Label) arguments.get(InstructionArgument.JE_VARIABLE_LABEL);
        }

        return FixedLabel.EMPTY;
    }

    @Override
    public String getInstructionDisplayFormat() {
        String displayFormat = String.format("IF %s = %s GOTO %s",
                getVariable().getRepresentation(), arguments.get(InstructionArgument.VARIABLE_NAME).getRepresentation(),
                arguments.get(InstructionArgument.JE_VARIABLE_LABEL).getRepresentation());

        return getInstructionDisplayFormat(displayFormat);
    }

    @Override
    public Map<InstructionArgument, Argument> getArguments() {
        return arguments;
    }
}
