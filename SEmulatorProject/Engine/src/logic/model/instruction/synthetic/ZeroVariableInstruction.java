package logic.model.instruction.synthetic;

import dto.InstructionDTO;
import logic.execution.ExecutionContext;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.label.LabelImpl;
import logic.model.argument.variable.Variable;
import logic.model.instruction.AbstractInstruction;
import logic.model.instruction.ExpandableInstruction;
import logic.model.instruction.Instruction;
import logic.model.instruction.InstructionData;
import logic.model.instruction.basic.DecreaseInstruction;
import logic.model.instruction.basic.JumpNotZeroInstruction;
import logic.model.instruction.basic.NeutralInstruction;
import logic.model.program.Program;
import logic.utils.Utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
    public List<Instruction> expand(Program program, Label instructionLabel){
        int maxLabelIndex = Utils.getMaxLabelIndex(program.getAllInstructionsLabels());
        int maxWorkVariableIndex = Utils.getMaxGeneralVariableIndex(program.getAllInstructionsWorkVariables());

        List<Instruction> expandedInstructions = new LinkedList<>();
        Label freeLabel = new LabelImpl(maxLabelIndex + 1);

        expandedInstructions.add(new NeutralInstruction(Variable.RESULT, instructionLabel));
        expandedInstructions.add(new DecreaseInstruction(getVariable(), freeLabel));
        expandedInstructions.add(new JumpNotZeroInstruction(getVariable(), freeLabel));

        return expandedInstructions;
    }
}
