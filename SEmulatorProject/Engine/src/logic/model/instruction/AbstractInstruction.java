package logic.model.instruction;

import logic.model.label.FixedLabel;
import logic.model.label.Label;
import logic.model.variable.Variable;

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

}
