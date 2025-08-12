package model;

public enum InstructionDetails {
    NEUTRAL("basic", 0),
    INCREASE("basic", 1),
    DECREASE("basic", 1),
    JUMP_NOT_ZERO("basic", 2),
    ZERO_VARIABLE("synthetic", 1),
    ASSIGNMENT("synthetic", 4),
    GOTO_LABEL("synthetic", 1),
    CONSTANT_ASSIGNMENT("synthetic", 2),
    JUMP_ZERO("synthetic", 2),
    JUMP_EQUAL_CONSTANT("synthetic", 2),
    JUMP_EQUAL_VARIABLE("synthetic", 2);

    private final String type;
    private final int cycles;

    InstructionDetails(String type, int cycles) {
        this.type = type;
        this.cycles = cycles;
    }

    public String getType() {
        return type;
    }

    public int getCycles() {
        return cycles;
    }

    public boolean isBasic() {
        return "basic".equals(type);
    }

    public boolean isSynthetic() {
        return "synthetic".equals(type);
    }

    public static InstructionDetails fromNameAndType(String name, String type) {
        // אם רוצים לוודא התאמה מלאה
        for (InstructionDetails detail : values()) {
            if (detail.name().equals(name) && detail.type.equals(type)) {
                return detail;
            }
        }
        throw new IllegalArgumentException("Unknown instruction: name=" + name + ", type=" + type);
    }
}