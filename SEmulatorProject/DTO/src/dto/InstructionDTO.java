package dto;

public class InstructionDTO implements DTO{
    private final int index;
    private final String instructionType;
    private final String label;
    private final String displayFormat;
    private final int cycles;

    public InstructionDTO(int index, String instructionType, String label, String displayFormat, int cycles) {
        this.index = index;
        this.instructionType = instructionType;
        this.label = label;
        this.displayFormat = displayFormat;
        this.cycles = cycles;
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
}
