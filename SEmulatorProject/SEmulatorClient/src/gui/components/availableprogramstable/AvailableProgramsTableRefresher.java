package gui.components.availableprogramstable;

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

public class AvailableProgramsTableRefresher extends TimerTask {
    private final Consumer<List<UploadedProgramDTO>> programsListConsumer;

    public AvailableProgramsTableRefresher(Consumer<List<UploadedProgramDTO>> programsListConsumer) {
        this.programsListConsumer = programsListConsumer;
    }

    @Override
    public void run() {
        Request request = HttpClientUtil.createGetRequest(ServerPaths.PROGRAMS_LIST);

        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Failed to fetch programs list: (failure) " + e.getMessage());

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfUsersNames = response.body().string();
                UploadedProgramDTO[] programs = GsonFactory.getGson().fromJson(jsonArrayOfUsersNames, UploadedProgramDTO[].class);
                programsListConsumer.accept(Arrays.asList(programs));

                response.close();
            }
        });
    }
}
