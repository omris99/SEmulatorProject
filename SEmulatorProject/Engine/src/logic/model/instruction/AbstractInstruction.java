package logic.model.instruction;

import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;

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

    public void setParentInstruction(Instruction parentInstruction) {
        this.parentInstruction = parentInstruction;
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
}
