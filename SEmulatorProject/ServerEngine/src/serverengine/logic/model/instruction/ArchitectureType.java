package serverengine.logic.model.instruction;

public enum ArchitectureType {
    ONE("I", 5, 1),
    TWO("II", 100, 2),
    THREE("III", 500, 3),
    FOUR("IV", 1000, 4);

    private final String userString;
    private final int executionCost;
    private final int number;

    ArchitectureType(String userString, int executionCost, int number) {
        this.userString = userString;
        this.executionCost = executionCost;
        this.number = number;
    }

    public String getUserString() {
        return userString;
    }

    public int getExecutionCost() {
        return executionCost;
    }

    public int getNumber() {
        return number;
    }

    public static ArchitectureType fromUserString(String userString) {
        for (ArchitectureType type : values()) {
            if (type.getUserString().equalsIgnoreCase(userString)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown architecture: " + userString);
    }
}
