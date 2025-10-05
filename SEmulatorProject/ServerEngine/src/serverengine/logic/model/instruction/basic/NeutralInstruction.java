package serverengine.logic.model.instruction.basic;

import clientserverdto.InstructionDTO;
import serverengine.logic.execution.ExecutionContext;
import serverengine.logic.model.argument.label.FixedLabel;
import serverengine.logic.model.argument.label.Label;
import serverengine.logic.model.argument.variable.Variable;
import serverengine.logic.model.instruction.AbstractInstruction;
import serverengine.logic.model.instruction.InstructionData;

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
    public InstructionDTO getInstructionDTO() {
        String displayFormat = String.format("%s <- %s", getVariable().getRepresentation(), getVariable().getRepresentation());

        return super.getInstructionDTO(displayFormat);
    }

    @Override
    public String getInstructionDisplayFormat() {
        String displayFormat = String.format("%s <- %s", getVariable().getRepresentation(), getVariable().getRepresentation());

        return super.getInstructionDisplayFormat(displayFormat);
    }
}
