package serverengine.logic.model.instruction.synthetic;

import clientserverdto.InstructionDTO;
import serverengine.logic.execution.ExecutionContext;
import serverengine.logic.model.argument.label.FixedLabel;
import serverengine.logic.model.argument.label.Label;
import serverengine.logic.model.argument.label.LabelImpl;
import serverengine.logic.model.argument.variable.Variable;
import serverengine.logic.model.instruction.AbstractInstruction;
import serverengine.logic.model.instruction.ExpandableInstruction;
import serverengine.logic.model.instruction.Instruction;
import serverengine.logic.model.instruction.InstructionData;
import serverengine.logic.model.instruction.basic.DecreaseInstruction;
import serverengine.logic.model.instruction.basic.JumpNotZeroInstruction;
import serverengine.logic.model.instruction.basic.NeutralInstruction;

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
    public InstructionDTO getInstructionDTO() {
        String displayFormat = String.format("%s <- 0", getVariable().getRepresentation());

        return super.getInstructionDTO(displayFormat);
    }

    @Override
    public String getInstructionDisplayFormat() {
        String displayFormat = String.format("%s <- 0", getVariable().getRepresentation());

        return super.getInstructionDisplayFormat(displayFormat);
    }

    @Override
    public List<Instruction> expand(int maxLabelIndex, int maxWorkVariableIndex, Label instructionLabel){
        List<Instruction> expandedInstructions = new LinkedList<>();
        Label freeLabel = new LabelImpl(maxLabelIndex + 1);

        expandedInstructions.add(new NeutralInstruction(Variable.RESULT, instructionLabel));
        expandedInstructions.add(new DecreaseInstruction(getVariable(), freeLabel));
        expandedInstructions.add(new JumpNotZeroInstruction(getVariable(), freeLabel));

        return expandedInstructions;
    }
}
