package logic.model.instruction;

import dto.InstructionDTO;
import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractInstruction implements Instruction, Cloneable {
    private int index;
    private final InstructionData instructionData;
    private final Label label;
    private final Variable variable;
    private Instruction parentInstruction;

    @Override
    public Instruction clone() {
        try {
            AbstractInstruction copy = (AbstractInstruction) super.clone();
            copy.parentInstruction = this.parentInstruction;
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public AbstractInstruction(InstructionData instructionData, Variable variable) {
        this(instructionData, variable, FixedLabel.EMPTY);
    }

    public AbstractInstruction(InstructionData instructionData, Variable variable, Label label) {
        this.instructionData = instructionData;
        this.label = label;
        this.variable = variable;
        parentInstruction = null;
    }

    public InstructionDTO getInstructionDTO(String instructionDisplayFormat) {
        List<InstructionDTO> parentInstructions = new LinkedList<>();
        Instruction currentParentInstruction = parentInstruction;
        while(currentParentInstruction != null){
            parentInstructions.add(currentParentInstruction.getInstructionDTO());
            currentParentInstruction = currentParentInstruction.getParent();
        }

        return new InstructionDTO(index, getType().toString(), label != FixedLabel.EMPTY ? getLabel().getRepresentation() : "", instructionDisplayFormat, getCycles(), parentInstructions);
    }

    public String getInstructionDisplayFormat(String instructionDisplayFormat) {
        String instructionFormatted = String.format("#%d (%s) [ %-4s] %s (%d)",
                index, getType().equals(InstructionType.BASIC) ? "B" : "S",
                label != FixedLabel.EMPTY ? getLabel().getRepresentation() : "",
                instructionDisplayFormat,
                getCycles());

        if(parentInstruction != null){
            instructionFormatted = instructionFormatted.concat(" >>> " + parentInstruction.getInstructionDisplayFormat());
        }

        return instructionFormatted;
    }

    @Override
    public String getName() {
        return instructionData.name();
    }

    @Override
    public int getCycles() {
        return instructionData.getCycles();
    }

    @Override
    public InstructionType getType() {
        return instructionData.getType();
    }

    @Override
    public Label getLabel() {
        return label;
    }

    @Override
    public Variable getVariable() {
        return variable;
    }

    @Override
    public int getDegree() {
        return instructionData.getDegree();
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setParent(Instruction parent) {
        this.parentInstruction = parent;
    }

    @Override
    public Instruction getParent() {
        return parentInstruction;
    }
}
