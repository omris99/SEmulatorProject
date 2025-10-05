package gui.components.availableprogramstable;

import dto.ProgramDTO;
import dto.UploadedProgramDTO;
import dto.UserDTO;
import http.HttpClientUtil;
import http.ServerPaths;
import logic.json.GsonFactory;
import logic.model.functionsrepo.UploadedProgram;
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
    private final boolean shouldUpdate;


    public AvailableProgramsTableRefresher(boolean shouldUpdate, Consumer<List<UploadedProgramDTO>> programsListConsumer) {
        this.shouldUpdate = shouldUpdate;
        this.programsListConsumer = programsListConsumer;
    }

    @Override
    public void run() {

        if (!shouldUpdate) {
            return;
        }
        Request request = HttpClientUtil.createGetRequest(ServerPaths.PROGRAMS_LIST);

        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // Handle failure if needed
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfUsersNames = response.body().string();
                UploadedProgramDTO[] programs = GsonFactory.getGson().fromJson(jsonArrayOfUsersNames, UploadedProgramDTO[].class);
                programsListConsumer.accept(Arrays.asList(programs));
            }
        });
    }
}
