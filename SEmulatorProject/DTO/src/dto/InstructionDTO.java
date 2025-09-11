package dto;

import logic.model.instruction.Instruction;

import java.util.List;

public class InstructionDTO implements DTO{
    private final int index;
    private final String instructionType;
    private final String label;
    private final String displayFormat;
    private final int cycles;
    private final List<InstructionDTO> parentInstructions;

    public InstructionDTO(int index, String instructionType, String label, String displayFormat, int cycles, List<InstructionDTO> parentInstructions) {
        this.index = index;
        this.instructionType = instructionType;
        this.label = label;
        this.displayFormat = displayFormat;
        this.cycles = cycles;
        this.parentInstructions = parentInstructions;
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
}
