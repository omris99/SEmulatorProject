package clientserverdto;

public class ExecutionHistoryDTO {
    private final int executionNumber;
    private final RunResultsDTO runResults;
    private final String programType;
    private final String programName;
    private final UploadedProgramDTO uploadedProgramDTO;

    public ExecutionHistoryDTO(int executionNumber, RunResultsDTO runResults, String programType, String programName, UploadedProgramDTO uploadedProgramDTO) {
        this.executionNumber = executionNumber;
        this.runResults = runResults;
        this.programType = programType;
        this.programName = programName;
        this.uploadedProgramDTO = uploadedProgramDTO;
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

    public RunResultsDTO getRunResults() {
        return runResults;
    }

    public UploadedProgramDTO getUploadedProgramDTO() {
        return uploadedProgramDTO;
    }
}
