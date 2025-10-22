package gui.popups.chat.client.component.chatarea;

import com.google.gson.Gson;
import gui.popups.chat.client.component.chatarea.model.ChatLinesWithVersion;
import gui.popups.chat.client.util.Constants;
import gui.utils.Utils;
import http.HttpClientUtil;
import http.ServerPaths;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import json.GsonFactory;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;


public class ChatAreaRefresher extends TimerTask {

    private final Consumer<ChatLinesWithVersion> chatlinesConsumer;
    private final IntegerProperty chatVersion;
    private final BooleanProperty shouldUpdate;
    private int requestNumber;

    public ChatAreaRefresher(IntegerProperty chatVersion, BooleanProperty shouldUpdate, Consumer<String> httpRequestLoggerConsumer, Consumer<ChatLinesWithVersion> chatlinesConsumer) {
        this.chatlinesConsumer = chatlinesConsumer;
        this.chatVersion = chatVersion;
        this.shouldUpdate = shouldUpdate;
        requestNumber = 0;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }

        final int finalRequestNumber = ++requestNumber;

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(ServerPaths.CHAT_LINES_LIST)
                .newBuilder()
                .addQueryParameter("chatversion", String.valueOf(chatVersion.get()))
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Utils.showErrorAlert("Chat Error", "Failure", "Something went wrong with Chat Request # " + finalRequestNumber);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String rawBody = response.body().string();
                    ChatLinesWithVersion chatLinesWithVersion = GsonFactory.getGson().fromJson(rawBody, ChatLinesWithVersion.class);
                    chatlinesConsumer.accept(chatLinesWithVersion);
                } else {
                    Utils.showErrorAlert("Chat Error", "Error", "Something went wrong with Request");
                }
            }
        });

    }

}
