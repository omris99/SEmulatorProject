package logic.model.instruction.synthetic;

import logic.execution.ExecutionContext;
import logic.model.argument.label.LabelImpl;
import logic.model.instruction.*;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;
import logic.model.instruction.basic.DecreaseInstruction;
import logic.model.instruction.basic.JumpNotZeroInstruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ZeroVariableInstruction extends AbstractInstruction implements ExpandableInstruction {
    public ZeroVariableInstruction(Variable variable) {
        super(InstructionData.ZERO_VARIABLE, variable, FixedLabel.EMPTY);
    }

    public ZeroVariableInstruction(Variable variable, Label label) {
        super(InstructionData.ZERO_VARIABLE, variable, label);
    }
    @Override
    public Label execute(ExecutionContext context) {
        context.updateVariable(getVariable(), 0);

        return FixedLabel.EMPTY;
    }

    @Override
    public String getInstructionDisplayFormat() {
        String displayFormat = String.format("%s <- 0", getVariable().getRepresentation());

        return super.getInstructionDisplayFormat(displayFormat);
    }

    @Override
    public List<Instruction> expand(int maxLabelIndex, int maxWorkVariableIndex, Label instructionLabel) {
        List<Instruction> expandedInstructions = new LinkedList<>();
        Label freeLabel = instructionLabel.equals(FixedLabel.EMPTY) ? new LabelImpl(maxLabelIndex + 1) : instructionLabel;

        expandedInstructions.add(new DecreaseInstruction(getVariable(), freeLabel));
        expandedInstructions.add(new JumpNotZeroInstruction(getVariable(), freeLabel));

        return expandedInstructions;
    }
}
