package serverengine.logic.model.instruction;

public enum ArchitectureType {
    ONE("I", 5),
    TWO("II", 100),
    THREE("III", 500),
    FOUR("IV", 1000);

    private final String userString;
    private final int executionCost;

    ArchitectureType(String userString, int executionCost) {
        this.userString = userString;
        this.executionCost = executionCost;
    }
}
