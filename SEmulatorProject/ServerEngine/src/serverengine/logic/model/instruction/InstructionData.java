package serverengine.logic.model.instruction;

public enum InstructionData {
    NEUTRAL(InstructionType.BASIC, ArchitectureType.ONE ,0),
    INCREASE(InstructionType.BASIC, ArchitectureType.ONE, 1),
    DECREASE(InstructionType.BASIC, ArchitectureType.ONE, 1),
    JUMP_NOT_ZERO(InstructionType.BASIC, ArchitectureType.ONE, 2),
    ZERO_VARIABLE(InstructionType.SYNTHETIC, ArchitectureType.TWO, 1),
    ASSIGNMENT(InstructionType.SYNTHETIC, ArchitectureType.THREE, 4),
    GOTO_LABEL(InstructionType.SYNTHETIC, ArchitectureType.TWO,1),
    CONSTANT_ASSIGNMENT(InstructionType.SYNTHETIC, ArchitectureType.TWO, 2),
    JUMP_ZERO(InstructionType.SYNTHETIC, ArchitectureType.THREE, 2),
    JUMP_EQUAL_CONSTANT(InstructionType.SYNTHETIC, ArchitectureType.THREE, 2),
    JUMP_EQUAL_VARIABLE(InstructionType.SYNTHETIC, ArchitectureType.THREE, 2),
    QUOTE(InstructionType.SYNTHETIC, ArchitectureType.FOUR, 5),
    JUMP_EQUAL_FUNCTION(InstructionType.SYNTHETIC, ArchitectureType.FOUR, 6);

    private final InstructionType type;
    private final ArchitectureType architectureType;
    private final int cycles;

    InstructionData(InstructionType type, ArchitectureType architectureType, int cycles) {
        this.type = type;
        this.architectureType = architectureType;
        this.cycles = cycles;
    }

    public InstructionType getType() {
        return type;
    }

    public int getCycles() {
        return cycles;
    }

    public boolean isBasic() {
        return type == InstructionType.BASIC;
    }

    public boolean isSynthetic() {
        return type == InstructionType.SYNTHETIC;
    }

    public static InstructionData fromNameAndType(String name, String type) {
        for (InstructionData detail : values()) {
            if (detail.name().equals(name) && (type.equals(detail.type.toString().toLowerCase()))) {
                return detail;
            }
        }

        throw new IllegalArgumentException("Unknown instruction: name=" + name + ", type=" + type);
    }

    public ArchitectureType getArchitectureType() {
        return architectureType;
    }
}