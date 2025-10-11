package gui.app;

import clientserverdto.ExecutionHistoryDTO;
import clientserverdto.UploadedProgramDTO;
import clientserverdto.UserDTO;
import gui.screens.dashboard.DashBoardController;
import gui.screens.execution.ExecutionScreenController;
import gui.screens.login.LoginController;
import gui.utils.Utils;
import http.HttpClientUtil;
import http.ServerPaths;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import json.GsonFactory;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import java.io.Closeable;
import java.io.IOException;

//TODO:
// 3. TRY TO MAKE ErrorType CLASS TO HANDLE ERRORS - AND MAYBE ADD CREATE DTO FUNCTION THERE
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
            FXMLLoader dashLoader = new FXMLLoader(getClass().getResource("/gui/screens/dashboard/DashBoard.fxml"));
            dashBoardScreen = dashLoader.load();
            dashBoardController = dashLoader.getController();
            dashBoardController.setClientManager(this);

            FXMLLoader execLoader = new FXMLLoader(getClass().getResource("/gui/screens/execution/ExecutionScreen.fxml"));
            executionScreen = execLoader.load();
            executionScreenController = execLoader.getController();
            executionScreenController.setClientManager(this);

            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/gui/screens/login/Login.fxml"));
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
        dashBoardController.setInActive();
        executionScreenController.setProgramToExecute(selectedProgram);
        setMainPanelTo(executionScreen);
        updateUserInfo();
    }

    public void switchToLoginScreen() {
        setMainPanelTo(loginScreen);
    }

    public void updateUserInfo(){
        Request request = HttpClientUtil.createGetRequest(ServerPaths.GET_USER_INFO);
        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> Utils.showErrorAlert(
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
                    Platform.runLater(() -> Utils.showErrorAlert(
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
