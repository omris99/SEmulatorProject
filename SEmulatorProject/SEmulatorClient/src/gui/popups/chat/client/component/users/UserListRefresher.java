package gui.popups.chat.client.component.users;


import com.google.gson.Gson;
import gui.utils.Utils;
import http.HttpClientUtil;
import http.ServerPaths;
import javafx.beans.property.BooleanProperty;
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

public class UserListRefresher extends TimerTask {

    private final Consumer<List<String>> usersListConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;


    public UserListRefresher(BooleanProperty shouldUpdate, Consumer<String> httpRequestLoggerConsumer, Consumer<List<String>> usersListConsumer) {
        this.shouldUpdate = shouldUpdate;
        this.usersListConsumer = usersListConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }

        final int finalRequestNumber = ++requestNumber;
        Request request = new Request.Builder()
                .url(ServerPaths.CHAT_USERS_LIST)
                .build();

        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Utils.showErrorAlert("Chat Error", "Failure", "Something went wrong with Request");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfUsersNames = response.body().string();
                String[] usersNames = GsonFactory.getGson().fromJson(jsonArrayOfUsersNames, String[].class);
                usersListConsumer.accept(Arrays.asList(usersNames));
            }
        });
    }
}
