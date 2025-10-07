package gui.login;

import gui.app.ClientManager;
import http.HttpClientUtil;
import http.ServerPaths;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button loginButton;

    @FXML
    private TextField userNameTextField;

    @FXML
    public Label errorMessageLabel;

    private final StringProperty errorMessageProperty = new SimpleStringProperty();

    @FXML
    private ClientManager clientManager;

    @FXML
    public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
    }

    public void setClientManager(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @FXML
    void onLoginButton(ActionEvent event) {
        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        String finalUrl = HttpUrl
                .parse(ServerPaths.LOGIN)
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .get()
                .build();

        HttpClientUtil.runAsync(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        clientManager.switchToDashBoard();
                    });
                }
            }
        });
    }

}
