package gui.app;

import dto.ErrorAlertDTO;
import dto.InstructionDTO;
import dto.ProgramDTO;
import dto.RunResultsDTO;
import gui.components.debuggerwindow.DebuggerWindowController;
import gui.components.displaycommandsbar.DisplayCommandsBarController;
import gui.components.historywindow.HistoryWindowController;
import gui.components.instructionstreetable.InstructionsTreeTableController;
import gui.components.instructionswindow.InstructionsWindowController;
import gui.components.loadfilebar.LoadFileBarController;
import gui.dashboard.DashBoardController;
import gui.execution.ExecutionScreenController;
import http.HttpClientUtil;
import http.ServerPaths;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logic.instructiontree.InstructionsTree;
import logic.json.GsonFactory;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientManager {
    @FXML
    private AnchorPane mainPanel;

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

            FXMLLoader execLoader = new FXMLLoader(getClass().getResource("/gui/execution/ExecutionScreen.fxml"));
            executionScreen = execLoader.load();
            executionScreenController = execLoader.getController();
            switchToDashBoard();

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
//        dashBoardController.setActive();
    }

    public void switchToExecutionScreen() {
        setMainPanelTo(executionScreen);
//        executionScreen.setActive();
    }


}
