package logic.model.instruction.basic;

import logic.execution.ExecutionContext;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.instruction.AbstractInstruction;
import logic.model.instruction.InstructionData;

public class NeutralInstruction extends AbstractInstruction {

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
        String displayFormat = String.format("%s <- %s", getVariable().getRepresentation(), getVariable().getRepresentation());

        return super.getInstructionDisplayFormat(displayFormat);
    }
}
