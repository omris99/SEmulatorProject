package gui.components.availablefunctionstable;

import clientserverdto.UploadedProgramDTO;
import http.HttpClientUtil;
import http.ServerPaths;
import json.GsonFactory;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class AvailableFunctionsTableRefresher extends TimerTask {
    private final Consumer<List<UploadedProgramDTO>> functionsListConsumer;


    public AvailableFunctionsTableRefresher(Consumer<List<UploadedProgramDTO>> functionsListConsumer) {
        this.functionsListConsumer = functionsListConsumer;
    }

    @Override
    public void run() {
        Request request = HttpClientUtil.createGetRequest(ServerPaths.FUNCTIONS_LIST);
        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // Handle failure if needed
                System.out.println("Failed to fetch functions list: (failure) " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfUsersNames = response.body().string();
                if(!response.isSuccessful()){
                    System.out.println("Failed to fetch functions list. " + jsonArrayOfUsersNames);
                    return;
                }
                UploadedProgramDTO[] functions = GsonFactory.getGson().fromJson(jsonArrayOfUsersNames, UploadedProgramDTO[].class);
                functionsListConsumer.accept(Arrays.asList(functions));

                response.close();
            }
        });
    }
}
