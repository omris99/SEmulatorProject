package model;

public enum InstructionDetails {
    NEUTRAL("basic"),
    INCREASE("basic"),
    DECREASE("basic"),
    JUMP_NOT_ZERO("basic"),
    ZERO_VARIABLE("synthetic"),
    ASSIGNMENT("synthetic"),
    GOTO_LABEL("synthetic"),
    CONSTANT_ASSIGNMENT("synthetic"),
    JUMP_ZERO("synthetic"),
    JUMP_EQUAL_CONSTANT("synthetic"),
    JUMP_EQUAL_VARIABLE("synthetic");

    private final String type;

    InstructionDetails(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
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