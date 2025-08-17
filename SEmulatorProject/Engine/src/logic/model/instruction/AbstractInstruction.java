package logic.model.instruction;

import logic.model.argument.label.FixedLabel;
import logic.model.argument.label.Label;
import logic.model.argument.variable.Variable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractInstruction implements Instruction {
    private final InstructionData instructionData;
    private final Label label;
    private final Variable variable;
    private Instruction parentInstruction;
//    private final int degree;
//    private final List<Instruction> expandedInstructions;

    public AbstractInstruction(InstructionData instructionData, Variable variable) {
        this(instructionData, variable, FixedLabel.EMPTY);
    }

    public AbstractInstruction(InstructionData instructionData, Variable variable, Label label) {
        this.instructionData = instructionData;
        this.label = label;
        this.variable = variable;
        parentInstruction = this;
//        this.degree = computeDegree();
//        expandedInstructions = new ArrayList<>();
    }

    public String getInstructionDisplayFormat(String instructionDisplayFormat) {
        String instructionFormatted = String.format("(%s) [ %-4s] %s (%d)",
                getType().equals("basic") ? "B" : "S",
                label != FixedLabel.EMPTY ? getLabel().getRepresentation() : "",
                instructionDisplayFormat,
                getCycles());

        if(parentInstruction != this){
            instructionFormatted = instructionFormatted.concat(" <<< " + parentInstruction.getInstructionDisplayFormat());
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
    public String getType() {
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
    public int computeDegree() {
//        int maxChildInstuctionDegree = 0;
//        int currentChildInstuctionDegree;
//
//        for(Instruction instruction : expandedInstructions) {
//            if(instruction.getType().equals("basic")){
//                return 1;
//            }
//            else{
//                currentChildInstuctionDegree = instruction.computeDegree();
//                if(currentChildInstuctionDegree > maxChildInstuctionDegree){
//                    maxChildInstuctionDegree = currentChildInstuctionDegree;
//                }
//            }
//        }
//
//        return maxChildInstuctionDegree;
        return 0;
    }

    @Override
    public int getDegree() {
        return instructionData.getDegree();
    }

}
