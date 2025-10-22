package server.execution;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutionManager {
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    public static ExecutorService getExecutor() {
        return executor;
    }

    public static void submit(Runnable runnable) {
        executor.submit(runnable);
    }

    public static void shutdown() {
        executor.shutdown();
    }
}
