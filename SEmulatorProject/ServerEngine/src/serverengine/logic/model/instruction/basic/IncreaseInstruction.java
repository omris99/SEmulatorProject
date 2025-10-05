package serverengine.logic.model.instruction.basic;

import clientserverdto.InstructionDTO;
import serverengine.logic.execution.ExecutionContext;
import serverengine.logic.model.argument.label.FixedLabel;
import serverengine.logic.model.argument.label.Label;
import serverengine.logic.model.argument.variable.Variable;
import serverengine.logic.model.instruction.AbstractInstruction;
import serverengine.logic.model.instruction.InstructionData;

public class IncreaseInstruction extends AbstractInstruction {

    public IncreaseInstruction(Variable variable) {
        super(InstructionData.INCREASE, variable);
    }

    public IncreaseInstruction(Variable variable, Label label) {
        super(InstructionData.INCREASE, variable, label);
    }

    @Override
    public Label execute(ExecutionContext context) {

        long variableValue = context.getVariableValue(getVariable());
        variableValue++;
        context.updateVariable(getVariable(), variableValue);

        return FixedLabel.EMPTY;
    }

    @Override
    public InstructionDTO getInstructionDTO() {
        String displayFormat = String.format("%s <- %s + 1", getVariable().getRepresentation(), getVariable().getRepresentation());

        return super.getInstructionDTO(displayFormat);
    }

    @Override
    public String getInstructionDisplayFormat() {
        String displayFormat = String.format("%s <- %s + 1", getVariable().getRepresentation(), getVariable().getRepresentation());

        return super.getInstructionDisplayFormat(displayFormat);
    }
}
