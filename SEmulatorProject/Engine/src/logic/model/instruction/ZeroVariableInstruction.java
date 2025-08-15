package logic.model.instruction;

import logic.model.execution.ExecutionContext;
import logic.model.label.FixedLabel;
import logic.model.label.Label;
import logic.model.variable.Variable;

public class ZeroVariableInstruction extends AbstractInstruction {
    public ZeroVariableInstruction(Variable variable) {
        super(InstructionData.ZERO_VARIABLE, variable, FixedLabel.EMPTY);
    }

    public ZeroVariableInstruction(Variable variable, Label label) {
        super(InstructionData.ZERO_VARIABLE, variable, label);
    }
    @Override
    public Label execute(ExecutionContext context) {
        long variableValue = context.getVariableValue(getVariable());
        variableValue = 0;
        context.updateVariable(getVariable(), variableValue);

        return FixedLabel.EMPTY;
    }

    @Override
    public String getInstructionDisplayFormat() {
        return String.format("%s <- 0", getVariable().getRepresentation());
    }
}
