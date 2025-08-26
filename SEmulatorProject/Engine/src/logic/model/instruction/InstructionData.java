package logic.model.instruction;

public enum InstructionData {
    NEUTRAL("basic", 0, 0),
    INCREASE("basic", 1, 0),
    DECREASE("basic", 1, 0),
    JUMP_NOT_ZERO("basic", 2, 0),
    ZERO_VARIABLE("synthetic", 1, 1),
    ASSIGNMENT("synthetic", 4, 2),
    GOTO_LABEL("synthetic", 1, 1),
    CONSTANT_ASSIGNMENT("synthetic", 2, 2),
    JUMP_ZERO("synthetic", 2, 2),
    JUMP_EQUAL_CONSTANT("synthetic", 2, 3),
    JUMP_EQUAL_VARIABLE("synthetic", 2, 3);

    private final String type;
    private final int cycles;
    private final int degree;

    InstructionData(String type, int cycles, int degree) {
        this.type = type;
        this.cycles = cycles;
        this.degree = degree;
    }

    public String getType() {
        return type;
    }

    public int getCycles() {
        return cycles;
    }

    public int getDegree() {
        return degree;
    }

    public boolean isBasic() {
        return "basic".equals(type);
    }

    public boolean isSynthetic() {
        return "synthetic".equals(type);
    }

    public static InstructionData fromNameAndType(String name, String type) {
        for (InstructionData detail : values()) {
            if (detail.name().equals(name) && detail.type.equals(type)) {
                return detail;
            }
        }
        throw new IllegalArgumentException("Unknown instruction: name=" + name + ", type=" + type);
    }
}