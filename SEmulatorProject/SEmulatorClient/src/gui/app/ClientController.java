package gui.app;

import clientserverdto.*;
import gui.dashboard.DashBoardController;
import gui.execution.ExecutionScreenController;
import gui.login.LoginController;
import http.HttpClientUtil;
import http.ServerPaths;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import okhttp3.*;
import json.GsonFactory;

import java.io.Closeable;
import java.io.IOException;

//TODO:
// 1. CHANGE VALIDATION OF PROGRAM'S FUNCITON BY EXERCISE 3 - NEEDS TO CHECK FUNCTIONS IN COMMA SEPERATED ARGUMENTS TOO !!
// 3. TRY TO MAKE ErrorType CLASS TO HANDLE ERRORS - AND MAYBE ADD CREATE DTO FUNCTION THERE
// 4. IF PROGRAM NOT RUN BECAUSE OF INITIAL CHARGING - MAKE THE ALERT CLEARER
// 6. IMPLEMENT SHOW ERROR ALERT AS STATIC UTIL AND USE IT Refreshers!!
// 7. MAKE GENERIC RESPONSE FOR FAILURE WITH SERVER CONNECTION - LIKE "FAILED TO CONNECT TO SERVER" - AND USE IT IN ALL CALLS

public class ClientController implements Closeable {
    @FXML
    private AnchorPane mainPanel;
    private Parent loginScreen;
    private LoginController loginController;

    private Parent dashBoardScreen;
    private DashBoardController dashBoardController;

    private Parent executionScreen;
    private ExecutionScreenController executionScreenController;


    @FXML
    public void initialize() {
        try {
            FXMLLoader dashLoader = new FXMLLoader(getClass().getResource("/gui/dashboard/DashBoard.fxml"));
            dashBoardScreen = dashLoader.load();
            dashBoardController = dashLoader.getController();
            dashBoardController.setClientManager(this);

            FXMLLoader execLoader = new FXMLLoader(getClass().getResource("/gui/execution/ExecutionScreen.fxml"));
            executionScreen = execLoader.load();
            executionScreenController = execLoader.getController();
            executionScreenController.setClientManager(this);

            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/gui/login/Login.fxml"));
            loginScreen = loginLoader.load();
            loginController = loginLoader.getController();
            loginController.setClientManager(this);
            switchToLoginScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void setMainPanelTo(Parent pane) {
        mainPanel.getChildren().setAll(pane);
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
    }

    public void switchToDashBoard() {
        setMainPanelTo(dashBoardScreen);
        AnimationsManager.playFadeIn(dashBoardScreen, 1500);
        updateUserInfo();
        dashBoardController.setActive();
    }

    public void switchToExecutionScreen(UploadedProgramDTO selectedProgram) {
        executionScreenController.setProgramToExecute(selectedProgram);
        setMainPanelTo(executionScreen);
        updateUserInfo();
        executionScreenController.setActive();
    }

    public void switchToLoginScreen() {
        setMainPanelTo(loginScreen);
//        executionScreen.setActive();
    }

    public static void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void updateUserInfo(){
        Request request = HttpClientUtil.createGetRequest(ServerPaths.GET_USER_INFO);
        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> showErrorAlert(
                        "User Info Fetch Failed",
                        "Failed to fetch user info from server",
                        e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyString = response.body().string();
                if (response.isSuccessful()) {
                    UserDTO userDTO = GsonFactory.getGson().fromJson(responseBodyString, UserDTO.class);
                    Platform.runLater(() -> {
                        dashBoardController.setUserInfo(userDTO);
                        executionScreenController.setUserInfo(userDTO);
                    });
                }
                else {
                    Platform.runLater(() -> showErrorAlert(
                            ("HTTP " + response.code() + " Error"),
                            ("Failed to fetch user info from server"),
                            null));
                }

                response.close();
            }
        });
    }

    public void reRunSelectedHistory(ExecutionHistoryDTO selectedRun) {
        switchToExecutionScreen(selectedRun.getUploadedProgramDTO());
        executionScreenController.reRunSelectedHistory(selectedRun.getRunResults());
    }

    @Override
    public void close() throws IOException {
        dashBoardController.close();
    }
}
