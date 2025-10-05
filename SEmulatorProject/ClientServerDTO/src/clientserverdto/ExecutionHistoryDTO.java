package clientserverdto;

public class ExecutionHistoryDTO {
    private int executionNumber;
    private RunResultsDTO runResults;
    private String programType;
    private String programName;
    private String Architecture;

    public ExecutionHistoryDTO(int executionNumber, RunResultsDTO runResults, String programType, String programName, String architecture) {
        this.executionNumber = executionNumber;
        this.runResults = runResults;
        this.programType = programType;
        this.programName = programName;
        this.Architecture = architecture;
    }

    public int getExecutionNumber() {
        return executionNumber;
    }

    public String getProgramType() {
        return programType;
    }

    public String getProgramName() {
        return programName;
    }

    public String getArchitecture() {
        return Architecture;
    }

    public int getDegree() {
        return runResults.getDegree();
    }

    public int getTotalCyclesCount() {
        return runResults.getTotalCyclesCount();
    }

    public Long getYValue() {
        return runResults.getYValue();
    }
}
