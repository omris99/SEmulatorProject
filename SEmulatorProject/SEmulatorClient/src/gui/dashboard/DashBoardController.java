package gui.dashboard;

import clientserverdto.ProgramDTO;
import clientserverdto.UploadedProgramDTO;
import gui.app.ClientManager;
import gui.components.loadfilebar.LoadFileBarController;
import gui.components.programswindow.ProgramsWindowController;
import gui.components.userswindow.UsersWindowController;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import serverengine.logic.json.GsonFactory;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;

public class DashBoardController {
    @FXML
    private LoadFileBarController loadFileBarController;

    @FXML
    private ClientManager clientManager;

    @FXML
    private Label userNameLabel;

    @FXML
    private UsersWindowController usersWindowController;

    @FXML
    private ProgramsWindowController programsWindowController;

    @FXML
    private void initialize() {
        loadFileBarController.setDashBoardController(this);
        programsWindowController.setDashBoardController(this);
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
                            showErrorAlert(
                                    "Upload Failed",
                                    "Failed to upload file to server",
                                    e.getMessage()
                            );
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseBodyString = response.body().string();
                        if (response.isSuccessful()) {
                            ProgramDTO programDTO = GsonFactory.getGson().fromJson(responseBodyString, ProgramDTO.class);
                            Platform.runLater(() -> {
//                                instructionWindowController.onProgramLoaded(programDTO);
//                                resetComponents();
                            });
                        } else {
                            Platform.runLater(() -> {
                                loadFileBarController.setProgressBarLoadErrorStyle();
                                showErrorAlert(
                                        ("HTTP " + response.code() + " Error"),
                                        ("Failed to load XML file"),
                                        responseBodyString);
                                System.out.println("HTTP " + response.code() + " Error: " + responseBodyString);
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

    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setClientManager(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    public void setActive() {
        userNameLabel.setText(clientManager.getUserName());
        usersWindowController.startAvailableUsersTableRefresher();
        usersWindowController.setHistory();
        programsWindowController.startAvailableFunctionsTableRefresher();
        programsWindowController.startAvailableProgramsTableRefresher();
    }

    public void executeProgramButtonClicked(UploadedProgramDTO selectedProgram) {
        clientManager.switchToExecutionScreen(selectedProgram);
    }
}
