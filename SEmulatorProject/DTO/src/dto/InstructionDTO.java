package dto;

import logic.model.instruction.Instruction;

import java.util.List;
import java.util.Objects;

public class InstructionDTO implements DTO{
    private final int index;
    private final String instructionType;
    private final String label;
    private final String displayFormat;
    private final int cycles;
    private final List<InstructionDTO> parentInstructions;
    private final List<String> associatedArgumentsAndLabels;
    private boolean isBreakpointSet;

    public InstructionDTO(int index, String instructionType, String label,
                          String displayFormat, int cycles, List<InstructionDTO> parentInstructions,
                          List<String> associatedArgumentsAndLabels, boolean isBreakpointSet) {
        this.index = index;
        this.instructionType = instructionType;
        this.label = label;
        this.displayFormat = displayFormat;
        this.cycles = cycles;
        this.parentInstructions = parentInstructions;
        this.associatedArgumentsAndLabels = associatedArgumentsAndLabels;
        this.isBreakpointSet = isBreakpointSet;
    }

    public int getIndex() {
        return index;
    }

    public String getInstructionType() {
        return instructionType;
    }

    public String getLabel() {
        return label;
    }

    public String getDisplayFormat() {
        return displayFormat;
    }

    public int getCycles() {
        return cycles;
    }

    public List<InstructionDTO> getParentInstructions() {
        return parentInstructions;
    }
    public List<String> getAssociatedArgumentsAndLabels(){
        return associatedArgumentsAndLabels;
    }
    public boolean getIsBreakpointSet() {
        return isBreakpointSet;
    }


    public void getIsBreakpointSet(boolean isBreakpointSet) {
        this.isBreakpointSet = isBreakpointSet;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InstructionDTO that = (InstructionDTO) o;
        return index == that.index && cycles == that.cycles && isBreakpointSet == that.isBreakpointSet && Objects.equals(instructionType, that.instructionType) && Objects.equals(label, that.label) && Objects.equals(displayFormat, that.displayFormat) && Objects.equals(parentInstructions, that.parentInstructions) && Objects.equals(associatedArgumentsAndLabels, that.associatedArgumentsAndLabels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, instructionType, label, displayFormat, cycles, parentInstructions, associatedArgumentsAndLabels, isBreakpointSet);
    }
}
