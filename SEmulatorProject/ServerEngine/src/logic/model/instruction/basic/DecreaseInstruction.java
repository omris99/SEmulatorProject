package logic.model.instruction.basic;

import dto.InstructionDTO;
import logic.execution.ExecutionContext;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.instruction.AbstractInstruction;
import logic.model.instruction.InstructionData;

public class DecreaseInstruction extends AbstractInstruction {

    public DecreaseInstruction(Variable variable) {
        super(InstructionData.DECREASE, variable );
    }

    public DecreaseInstruction(Variable variable, Label label) {
        super(InstructionData.DECREASE, variable, label);
    }
    @Override
    public Label execute(ExecutionContext context) {

        long variableValue = context.getVariableValue(getVariable());
        variableValue = Math.max(0, variableValue - 1);
        context.updateVariable(getVariable(), variableValue);

        return FixedLabel.EMPTY;
    }

    @Override
    public InstructionDTO getInstructionDTO() {
        String displayFormat = String.format("%s <- %s - 1", getVariable().getRepresentation(), getVariable().getRepresentation());

        return super.getInstructionDTO(displayFormat);
    }

    @Override
    public String getInstructionDisplayFormat() {
        String displayFormat = String.format("%s <- %s - 1", getVariable().getRepresentation(), getVariable().getRepresentation());

        return super.getInstructionDisplayFormat(displayFormat);
    }
}