package gui.components.dynamicexecutiondatawindow;

import clientserverdto.ExecutionStatusDTO;
import clientserverdto.RunResultsDTO;
import http.HttpClientUtil;
import http.ServerPaths;
import json.GsonFactory;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class DynamicExecutionDataWindowRefresher extends TimerTask {
    private final Consumer<ExecutionStatusDTO> ExecutionStatusConsumer;


    public DynamicExecutionDataWindowRefresher(Consumer<ExecutionStatusDTO> ExecutionStatusConsumer) {
        this.ExecutionStatusConsumer = ExecutionStatusConsumer;
    }

    @Override
    public void run() {
        Request request = HttpClientUtil.createGetRequest(ServerPaths.GET_EXECUTION_STATUS);
        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // Handle failure if needed
                System.out.println("Failed to fetch execution Status: (failure) " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String executionStatusString = response.body().string();
                if (!response.isSuccessful()) {
                    System.out.println("Failed to fetch execution Status. " + executionStatusString);
                    return;
                }
                ExecutionStatusDTO executionStatus = GsonFactory.getGson().fromJson(executionStatusString, ExecutionStatusDTO.class);
                ExecutionStatusConsumer.accept(executionStatus);

                response.close();
            }
        });
    }
}