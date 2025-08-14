package logic.model.instruction;

import logic.model.Argument;
import logic.model.execution.ExecutionContext;
import logic.model.label.FixedLabel;
import logic.model.label.Label;
import logic.model.variable.Variable;

public class JumpNotZeroInstruction extends AbstractInstruction {

    private final Label jnzLabel;

    public JumpNotZeroInstruction(Variable variable, Argument jnzLabel) {
        this(variable, jnzLabel, FixedLabel.EMPTY);
    }

    public JumpNotZeroInstruction(Variable variable, Argument jnzLabel, Label label) {
        super(InstructionData.JUMP_NOT_ZERO, variable, label);
        this.jnzLabel = (Label)jnzLabel;
    }

    @Override
    public Label execute(ExecutionContext context) {
        long variableValue = context.getVariableValue(getVariable());

        if (variableValue != 0) {
            return jnzLabel;
        }
        return FixedLabel.EMPTY;
    }

    @Override
    public String getInstructionDisplayFormat() {
        return String.format("IF %s != 0 GOTO %s", getVariable().getRepresentation(), jnzLabel.getLabelRepresentation());
    }
}
