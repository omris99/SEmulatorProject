package exceptions;

public class InvalidArchitectureException extends RuntimeException {
    private final String selectedArchitecture;
    private final String minimumArchitecture;

    public InvalidArchitectureException(String architecture, String minimumArchitecture) {
        super("Invalid architecture: " + architecture);
        this.selectedArchitecture = architecture;
        this.minimumArchitecture = minimumArchitecture;
    }

    public String getSelectedArchitecture() {
        return selectedArchitecture;
    }
    public String getMinimumArchitecture() {
        return minimumArchitecture;
    }
}
