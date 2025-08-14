package logic.model.instruction;

import logic.model.execution.ExecutionContext;
import logic.model.label.FixedLabel;
import logic.model.label.Label;
import logic.model.variable.Variable;

public class NeutralInstruction extends AbstractInstruction{

    public NeutralInstruction(Variable variable) {
        super(InstructionData.NEUTRAL, variable);
    }

    public NeutralInstruction(Variable variable, Label label) {
        super(InstructionData.NEUTRAL, variable, label);
    }

    @Override
    public Label execute(ExecutionContext context) {
        return FixedLabel.EMPTY;

    }

    @Override
    public String getInstructionDisplayFormat() {
        return String.format("%s <- %s", getVariable().getRepresentation(), getVariable().getRepresentation());
    }
}
