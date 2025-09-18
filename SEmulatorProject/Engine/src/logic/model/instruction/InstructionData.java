package logic.model.instruction;

public enum InstructionData {
    NEUTRAL(InstructionType.BASIC, 0, 0),
    INCREASE(InstructionType.BASIC, 1, 0),
    DECREASE(InstructionType.BASIC, 1, 0),
    JUMP_NOT_ZERO(InstructionType.BASIC, 2, 0),
    ZERO_VARIABLE(InstructionType.SYNTHETIC, 1, 1),
    ASSIGNMENT(InstructionType.SYNTHETIC, 4, 2),
    GOTO_LABEL(InstructionType.SYNTHETIC, 1, 1),
    CONSTANT_ASSIGNMENT(InstructionType.SYNTHETIC, 2, 2),
    JUMP_ZERO(InstructionType.SYNTHETIC, 2, 2),
    JUMP_EQUAL_CONSTANT(InstructionType.SYNTHETIC, 2, 3),
    JUMP_EQUAL_VARIABLE(InstructionType.SYNTHETIC, 2, 3),
    QUOTE(InstructionType.SYNTHETIC, 5, 13),
    JUMP_EQUAL_FUNCTION(InstructionType.SYNTHETIC, 6, 13);

    private final InstructionType type;
    private final int cycles;
    private final int degree;

    InstructionData(InstructionType type, int cycles, int degree) {
        this.type = type;
        this.cycles = cycles;
        this.degree = degree;
    }

    public InstructionType getType() {
        return type;
    }

    public int getCycles() {
        return cycles;
    }

    public int getDegree() {
        return degree;
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