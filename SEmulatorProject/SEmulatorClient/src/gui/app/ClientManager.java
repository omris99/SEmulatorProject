package gui.app;

import dto.UploadedProgramDTO;
import gui.dashboard.DashBoardController;
import gui.execution.ExecutionScreenController;
import gui.login.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ClientManager {
    @FXML
    private AnchorPane mainPanel;
    private Parent loginScreen;
    private LoginController loginController;
    private Parent dashBoardScreen;
    private DashBoardController dashBoardController;
    private Parent executionScreen;
    private ExecutionScreenController executionScreenController;
    private String userName;


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
        dashBoardController.setActive();
    }

    public void switchToExecutionScreen(UploadedProgramDTO selectedProgram) {
        executionScreenController.setProgramToExecute(selectedProgram);
        setMainPanelTo(executionScreen);
//        executionScreen.setActive();
    }

    public void switchToLoginScreen() {
        setMainPanelTo(loginScreen);
//        executionScreen.setActive();
    }

    public void setUserName(String username) {
        this.userName = username;
        dashBoardController.setUserName(username);
    }

    public String getUserName() {
        if(userName == null) {
            return "";
        }
        return userName;
    }
}
