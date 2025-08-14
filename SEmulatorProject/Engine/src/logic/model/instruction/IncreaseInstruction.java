package logic.model.instruction;

import logic.model.execution.ExecutionContext;
import logic.model.label.FixedLabel;
import logic.model.label.Label;
import logic.model.variable.Variable;

public class IncreaseInstruction extends AbstractInstruction{

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
    public String getInstructionDisplayFormat() {
        return String.format("%s <- %s + 1", getVariable().getRepresentation(), getVariable().getRepresentation());
    }
}
