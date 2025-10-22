package gui.screens.dashboard;

import clientserverdto.ErrorDTO;
import clientserverdto.ExecutionHistoryDTO;
import clientserverdto.UploadedProgramDTO;
import clientserverdto.UserDTO;
import gui.app.ClientController;
import gui.components.creditswindow.CreditsWindowController;
import gui.components.loadfilebar.LoadFileBarController;
import gui.components.programswindow.ProgramsWindowController;
import gui.components.userInfoBanner.UserInfoBannerController;
import gui.components.userswindow.UsersWindowController;
import gui.popups.chat.client.component.main.ChatAppMainController;
import gui.utils.Utils;
import http.HttpClientUtil;
import http.ServerPaths;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import json.GsonFactory;
import okhttp3.*;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public class DashBoardController implements Closeable {
    @FXML
    private LoadFileBarController loadFileBarController;

    @FXML
    private UserInfoBannerController userInfoBannerController;

    @FXML
    private ClientController clientController;

    @FXML
    private UsersWindowController usersWindowController;

    @FXML
    private ProgramsWindowController programsWindowController;

    @FXML
    private CreditsWindowController creditsWindowController;

    @FXML
    private Button chatButton;

    @FXML
    private void initialize() {
        loadFileBarController.setDashBoardController(this);
        programsWindowController.setDashBoardController(this);
        creditsWindowController.setDashBoardController(this);
        usersWindowController.setDashboardController(this);
    }

    public void loadProgramWithProgress(File selectedFile) {
        loadFileBarController.removeProgressBarErrorStyle();
        Task<Void> loadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                updateProgress(15, 100);
                updateMessage(selectedFile.getAbsolutePath());
                Request request = HttpClientUtil.buildUploadFileRequest(selectedFile);
                HttpClientUtil.runAsync(request, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Platform.runLater(() -> {
                            loadFileBarController.setProgressBarLoadErrorStyle();
                            Utils.showErrorAlert(
                                    "Upload Failed",
                                    "Failed to upload file to server",
                                    e.getMessage()
                            );
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseBodyString = response.body().string();
                        if(!response.isSuccessful()){
                            Platform.runLater(() -> {
                                loadFileBarController.setProgressBarLoadErrorStyle();
                                Utils.showErrorAlert(
                                        ("HTTP " + response.code() + " Error"),
                                        ("Failed to load XML file"),
                                        responseBodyString);
                            });
                        }
                        else{
                            Platform.runLater(() -> {
                                Utils.showInfoAlert("File Uploaded Successfully",
                                        "The program has been uploaded successfully.",
                                        "You can now find it in the programs list.");
                            });
                        }

                        response.close();
                    }
                });

                updateProgress(100, 100);
                return null;
            }
        };

        loadFileBarController.bindTaskToUI(loadTask);

        new Thread(loadTask).start();
    }

    public void setClientManager(ClientController clientController) {
        this.clientController = clientController;
    }

    public void setActive() {
        usersWindowController.startAvailableUsersTableRefresher();
        usersWindowController.setHistory();
        programsWindowController.startAvailableFunctionsTableRefresher();
        programsWindowController.startAvailableProgramsTableRefresher();
    }

    public void setInActive() {
        try {
            close();
        } catch (Exception ignored) {}
    }

    public void executeProgramButtonClicked(UploadedProgramDTO selectedProgram) {
        clientController.switchToExecutionScreen(selectedProgram);
    }

    public void chargeCredits(String credits) {
        RequestBody formBody = new FormBody.Builder()
                .add("creditsAmount", credits)
                .build();

        Request request = new Request.Builder()
                .url(ServerPaths.CHARGE_CREDITS)
                .post(formBody)
                .build();

        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> Utils.showErrorAlert(
                        "Credits Charge Failed",
                        "Failed to charge credits",
                        e.getMessage()
                ));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyString = response.body().string();
                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        creditsWindowController.resetCreditsInput();
                    });
                    clientController.updateUserInfo();
                } else {
                    ErrorDTO error = GsonFactory.getGson().fromJson(responseBodyString, ErrorDTO.class);

                            Platform.runLater(() -> Utils.showErrorAlert(error.getTitle(), error.getHeader(), error.getContent()));
                }

                response.close();
            }
        });
    }

    public void reRunSelectedHistory(ExecutionHistoryDTO selectedRun) {
        clientController.reRunSelectedHistory(selectedRun);
    }

    public void setUserInfo(UserDTO userDTO) {
        userInfoBannerController.updateUserInfo(userDTO);
    }

    @Override
    public void close() throws IOException {
        usersWindowController.close();
        programsWindowController.close();
    }

    public void chatButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/popups/chat/client/component/main/chat-app-main.fxml"));
            loader.load();
            ChatAppMainController controller = loader.getController();
            controller.updateUserName(userInfoBannerController.getUserName());
            Parent root = loader.getRoot();
            Stage primaryStage = new Stage();
            primaryStage.setTitle("S-Emulator Chat");
            primaryStage.setMinHeight(600);
            primaryStage.setMinWidth(600);
            primaryStage.setScene(new Scene(root, 700, 600));
            primaryStage.setOnCloseRequest(event -> {
                try {
                    controller.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
