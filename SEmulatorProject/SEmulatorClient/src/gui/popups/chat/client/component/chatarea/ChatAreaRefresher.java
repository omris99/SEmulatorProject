package gui.popups.chat.client.component.chatarea;

import com.google.gson.Gson;
import gui.popups.chat.client.component.chatarea.model.ChatLinesWithVersion;
import gui.popups.chat.client.util.Constants;
import http.HttpClientUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static gui.popups.chat.client.util.Constants.GSON_INSTANCE;

public class ChatAreaRefresher extends TimerTask {

    private final Consumer<String> httpRequestLoggerConsumer;
    private final Consumer<ChatLinesWithVersion> chatlinesConsumer;
    private final IntegerProperty chatVersion;
    private final BooleanProperty shouldUpdate;
    private int requestNumber;

    public ChatAreaRefresher(IntegerProperty chatVersion, BooleanProperty shouldUpdate, Consumer<String> httpRequestLoggerConsumer, Consumer<ChatLinesWithVersion> chatlinesConsumer) {
        this.httpRequestLoggerConsumer = httpRequestLoggerConsumer;
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
                .parse(Constants.CHAT_LINES_LIST)
                .newBuilder()
                .addQueryParameter("chatversion", String.valueOf(chatVersion.get()))
                .build()
                .toString();

        httpRequestLoggerConsumer.accept("About to invoke: " + finalUrl + " | Chat Request # " + finalRequestNumber);
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                httpRequestLoggerConsumer.accept("Something went wrong with Chat Request # " + finalRequestNumber);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String rawBody = response.body().string();
                    httpRequestLoggerConsumer.accept("Response of Chat Request # " + finalRequestNumber + ": " + rawBody);
                    ChatLinesWithVersion chatLinesWithVersion = GSON_INSTANCE.fromJson(rawBody, ChatLinesWithVersion.class);
                    chatlinesConsumer.accept(chatLinesWithVersion);
                } else {
                    httpRequestLoggerConsumer.accept("Something went wrong with Request # " + finalRequestNumber + ". Code is " + response.code());
                }
            }
        });

    }

}
