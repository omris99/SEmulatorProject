package gui.components.availableusers;

import clientserverdto.UserDTO;
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

public class AvailabaleUsersRefresher extends TimerTask {
    private final Consumer<List<UserDTO>> usersListConsumer;
    private final boolean shouldUpdate;


    public AvailabaleUsersRefresher(boolean shouldUpdate, Consumer<List<UserDTO>> usersListConsumer) {
        this.shouldUpdate = shouldUpdate;
        this.usersListConsumer = usersListConsumer;
    }

    @Override
    public void run() {

        if (!shouldUpdate) {
            return;
        }
        Request request = HttpClientUtil.createGetRequest(ServerPaths.USERS_LIST);

        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // Handle failure if needed
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfUsersNames = response.body().string();
                UserDTO[] usersNames = GsonFactory.getGson().fromJson(jsonArrayOfUsersNames, UserDTO[].class);
                usersListConsumer.accept(Arrays.asList(usersNames));
            }
        });
    }
}
