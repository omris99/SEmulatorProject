package clientserverdto;

public class ExecutionStatusDTO {
    private ExecutionStatus status;
    private RunResultsDTO lastRunResult;
    private ErrorDTO error;

    public ExecutionStatusDTO() {
        this.status = ExecutionStatus.IDLE;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    public RunResultsDTO getLastRunResult() {
        return lastRunResult;
    }

    public void setLastRunResult(RunResultsDTO lastRunResult) {
        this.lastRunResult = lastRunResult;
    }

    public ErrorDTO getError() {
        return error;
    }

    public void setError(ErrorDTO error) {
        this.error = error;
    }
}
