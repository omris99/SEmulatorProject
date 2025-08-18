package logic.model.instruction.basic;

import logic.model.argument.Argument;
import logic.execution.ExecutionContext;
import logic.model.instruction.*;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;

import java.util.HashMap;
import java.util.Map;

public class JumpNotZeroInstruction extends AbstractInstruction implements InstructionWithArguments {
    Map<InstructionArgument, Argument> arguments;
//    private final Label jnzLabel;

    public JumpNotZeroInstruction(Variable variable, Argument jnzLabel) {
        this(variable, jnzLabel, FixedLabel.EMPTY);
    }

    public JumpNotZeroInstruction(Variable variable, Argument jnzLabel, Label label) {
        super(InstructionData.JUMP_NOT_ZERO, variable, label);
        arguments = new HashMap<>();
        arguments.put(InstructionArgument.JNZ_LABEL, jnzLabel);
//        this.jnzLabel = (Label)jnzLabel;
    }

    @Override
    public Label execute(ExecutionContext context) {
        long variableValue = context.getVariableValue(getVariable());

        if (variableValue != 0) {
            return (Label) arguments.get(InstructionArgument.JNZ_LABEL);
        }

        return FixedLabel.EMPTY;
    }

    @Override
    public String getInstructionDisplayFormat() {
        String displayFormat = String.format("IF %s != 0 GOTO %s", getVariable().getRepresentation(),
                arguments.get(InstructionArgument.JNZ_LABEL).getRepresentation());

        return super.getInstructionDisplayFormat(displayFormat);
    }

    @Override
    public Map<InstructionArgument, Argument> getArguments() {
        return arguments;
    }

    @Override
    public Instruction clone() {
        JumpNotZeroInstruction copy = (JumpNotZeroInstruction) super.clone();
        copy.arguments = new HashMap<>(this.arguments);
        return copy;
    }
}
