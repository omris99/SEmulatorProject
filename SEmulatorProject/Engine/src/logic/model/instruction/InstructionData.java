package logic.model.instruction;

public enum InstructionData {
    NEUTRAL(InstructionType.BASIC, 0),
    INCREASE(InstructionType.BASIC, 1),
    DECREASE(InstructionType.BASIC, 1),
    JUMP_NOT_ZERO(InstructionType.BASIC, 2),
    ZERO_VARIABLE(InstructionType.SYNTHETIC, 1),
    ASSIGNMENT(InstructionType.SYNTHETIC, 4),
    GOTO_LABEL(InstructionType.SYNTHETIC, 1),
    CONSTANT_ASSIGNMENT(InstructionType.SYNTHETIC, 2),
    JUMP_ZERO(InstructionType.SYNTHETIC, 2),
    JUMP_EQUAL_CONSTANT(InstructionType.SYNTHETIC, 2),
    JUMP_EQUAL_VARIABLE(InstructionType.SYNTHETIC, 2),
    QUOTE(InstructionType.SYNTHETIC, 5),
    JUMP_EQUAL_FUNCTION(InstructionType.SYNTHETIC, 6);

    private final InstructionType type;
    private final int cycles;

    InstructionData(InstructionType type, int cycles) {
        this.type = type;
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
}