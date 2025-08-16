package logic.model.instruction;

import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;

public abstract class AbstractInstruction implements Instruction {
    private final InstructionData instructionData;
    private final Label label;
    private final Variable variable;

    public AbstractInstruction(InstructionData instructionData, Variable variable) {
        this(instructionData, variable, FixedLabel.EMPTY);
    }

    public AbstractInstruction(InstructionData instructionData, Variable variable, Label label) {
        this.instructionData = instructionData;
        this.label = label;
        this.variable = variable;
    }

    public String getType(){
        return instructionData.getType();
    }

    public String getInstructionDisplayFormat(String instructionDisplayFormat) {
        return String.format("(%s) [ %-4s] %s (%d)",
                getType().equals("basic") ? "B" : "S",
                label != FixedLabel.EMPTY ? getLabel().getRepresentation() : "",
                instructionDisplayFormat,
                getCycles());
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
    public Label getLabel() {
        return label;
    }

    @Override
    public Variable getVariable() {
        return variable;
    }

    public int getDegree() {
        return 0;
    }

}
