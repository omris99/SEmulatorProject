package logic.model.instruction;

import logic.model.Argument;
import logic.model.execution.ExecutionContext;
import logic.model.label.FixedLabel;
import logic.model.label.Label;
import logic.model.variable.Variable;

import java.util.HashMap;
import java.util.Map;

public class JumpEqualVariableInstruction extends AbstractInstruction implements InstructionWithArguments{
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
                        getVariable()), arguments.get(InstructionArgument.JE_VARIABLE_LABEL),
                arguments.get(InstructionArgument.VARIABLE_NAME)));
    }

    @Override
    public Map<InstructionArgument, Argument> getArguments() {
        return arguments;
    }
}
