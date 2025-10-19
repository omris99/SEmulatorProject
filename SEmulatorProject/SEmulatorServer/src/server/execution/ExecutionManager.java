package server.execution;

import clientserverdto.RunResultsDTO;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class ExecutionManager {
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    private static final ConcurrentHashMap<String, ExecutionStatus> runs = new ConcurrentHashMap<>();

    public static String submit(Callable<RunResultsDTO> task) {
        String runId = UUID.randomUUID().toString();
        ExecutionStatus status = new ExecutionStatus(runId, ExecutionPhase.PENDING);
        runs.put(runId, status);

        Future<RunResultsDTO> future = executor.submit(() -> {
            status.setPhase(ExecutionPhase.RUNNING);
            try {
                RunResultsDTO result = task.call();
                status.setResult(result);
                status.setPhase(ExecutionPhase.DONE);
            } catch (Exception e) {
                status.setPhase(ExecutionPhase.ERROR);
                status.setError(e.getMessage());
            }
            return status.getResult();
        });

        status.setFuture(future);
        return runId;
    }

    public static ExecutionStatus getStatus(String runId) {
        return runs.get(runId);
    }
}
