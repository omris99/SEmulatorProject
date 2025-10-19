package server.execution;

import clientserverdto.RunResultsDTO;
import java.util.concurrent.Future;

public class ExecutionStatus {
    private final String runId;
    private volatile ExecutionPhase phase;
    private RunResultsDTO result;
    private String error;
    private Future<RunResultsDTO> future;

    public ExecutionStatus(String runId, ExecutionPhase phase) {
        this.runId = runId;
        this.phase = phase;
    }

    public String getRunId() { return runId; }
    public ExecutionPhase getPhase() { return phase; }
    public void setPhase(ExecutionPhase phase) { this.phase = phase; }

    public RunResultsDTO getResult() { return result; }
    public void setResult(RunResultsDTO result) { this.result = result; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public Future<RunResultsDTO> getFuture() { return future; }
    public void setFuture(Future<RunResultsDTO> future) { this.future = future; }
}
