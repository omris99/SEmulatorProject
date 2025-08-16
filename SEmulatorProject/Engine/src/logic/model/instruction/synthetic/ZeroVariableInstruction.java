package logic.model.instruction.synthetic;

import logic.execution.ExecutionContext;
import logic.model.instruction.AbstractInstruction;
import logic.model.instruction.InstructionData;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;

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
        String displayFormat = String.format("%s <- 0", getVariable().getRepresentation());

        return super.getInstructionDisplayFormat(displayFormat);
    }
}
