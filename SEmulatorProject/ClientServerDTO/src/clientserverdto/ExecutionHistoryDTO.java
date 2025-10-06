package clientserverdto;

public class ExecutionHistoryDTO {
    private int executionNumber;
    private RunResultsDTO runResults;
    private String programType;
    private String programName;

    public ExecutionHistoryDTO(int executionNumber, RunResultsDTO runResults, String programType, String programName) {
        this.executionNumber = executionNumber;
        this.runResults = runResults;
        this.programType = programType;
        this.programName = programName;
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
        return runResults.getArchitecture();
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
