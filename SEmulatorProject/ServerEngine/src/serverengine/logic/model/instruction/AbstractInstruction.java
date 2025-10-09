package serverengine.logic.model.instruction;

import clientserverdto.InstructionDTO;
import serverengine.logic.model.argument.Argument;
import serverengine.logic.model.argument.commaseperatedarguments.CommaSeperatedArguments;
import serverengine.logic.model.argument.label.FixedLabel;
import serverengine.logic.model.argument.label.Label;
import serverengine.logic.model.argument.variable.Variable;
import types.ArchitectureType;
import types.InstructionType;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractInstruction implements Instruction, Cloneable {
    private boolean isBreakpointSet;
    private int index;
    private final InstructionData instructionData;
    private final Label label;
    private final Variable variable;
    private int degree;
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

    @Override
    public void setBreakpoint(boolean breakpointSet) {
        isBreakpointSet = breakpointSet;
    }

    @Override
    public boolean getBreakpoint() {
        return isBreakpointSet;
    }


    public InstructionDTO getInstructionDTO(String instructionDisplayFormat) {
        List<InstructionDTO> parentInstructions = new LinkedList<>();
        Instruction currentParentInstruction = parentInstruction;
        while(currentParentInstruction != null){
            parentInstructions.add(currentParentInstruction.getInstructionDTO());
            currentParentInstruction = currentParentInstruction.getParent();
        }

        return new InstructionDTO(
                index,
                getType().toString(),
                label != FixedLabel.EMPTY ? getLabel().getRepresentation() : "",
                instructionDisplayFormat, getCycles(),
                parentInstructions,
                getAssociatedArgumentsAndLabels(),
                isBreakpointSet,
                instructionData.getArchitectureType().getUserString()
        );
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
        if(degree == 0){
            degree = calculateDegree();
        }

        return degree;
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

    @Override
    public List<String> getAssociatedArgumentsAndLabels() {
        List<String> associatedArgumentsAndLabels = new LinkedList<>();
        associatedArgumentsAndLabels.add(variable.getRepresentation());
        if (label != FixedLabel.EMPTY) {
            associatedArgumentsAndLabels.add(label.getRepresentation());
        }

        if(this instanceof InstructionWithArguments instructionWithArguments){
            for(Argument argument : instructionWithArguments.getArguments().values()){
                if(argument instanceof Variable || argument instanceof Label){
                    associatedArgumentsAndLabels.add(argument.getRepresentation());
                }
                else if(argument instanceof CommaSeperatedArguments){
                    associatedArgumentsAndLabels.addAll(((CommaSeperatedArguments) argument).getAllVariables().stream().map(Variable::getRepresentation).toList());
                }
            }
        }

        return associatedArgumentsAndLabels;
    }

    private int calculateDegree() {
        if(instructionData.getType().equals(InstructionType.BASIC)){
            return 0;
        }

        ExpandableInstruction thisInstruction = (ExpandableInstruction) this;
        List<Instruction> expandedInstructions = thisInstruction.expand(1,1, FixedLabel.EMPTY);
        return expandedInstructions.stream().mapToInt(Instruction::getDegree).max().orElse(0) + 1;
    }

    @Override
    public ArchitectureType getArchitectureType() {
        return instructionData.getArchitectureType();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AbstractInstruction that = (AbstractInstruction) o;
        return isBreakpointSet == that.isBreakpointSet && index == that.index && degree == that.degree && instructionData == that.instructionData && Objects.equals(label, that.label) && Objects.equals(variable, that.variable) && Objects.equals(parentInstruction, that.parentInstruction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isBreakpointSet, index, instructionData, label, variable, degree, parentInstruction);
    }
}
