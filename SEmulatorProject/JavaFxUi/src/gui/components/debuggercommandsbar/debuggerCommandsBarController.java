package gui.components.debuggercommandsbar;

import gui.app.AnimationsManager;
import gui.components.debuggerwindow.DebuggerWindowController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class debuggerCommandsBarController {
    private DebuggerWindowController debuggerWindowController;

    @FXML
    private Button debugButton;

    @FXML
    private Button newRunButton;

    @FXML
    private Button resumeButton;

    @FXML
    private Button startButton;

    @FXML
    private Button stepBackwardButton;

    @FXML
    private Button stepOverButton;

    @FXML
    private Button stopButton;

    @FXML
    void debugButtonAction(ActionEvent event) {
        debuggerWindowController.onDebugButtonClick();
    }

    @FXML
    void newRunButtonAction(ActionEvent event) {
        debuggerWindowController.onNewRunClick();
    }

    @FXML
    void resumeButtonAction(ActionEvent event) {
        debuggerWindowController.onResumeClick();

    }

    @FXML
    void startButtonAction(ActionEvent event) {
        startButton.setDisable(true);
        AnimationsManager.playBigger(startButton, 400);
        debuggerWindowController.onStartClick();
        startButton.setDisable(false);
    }

    @FXML
    void stepBackwardButtonAction(ActionEvent event) {
        AnimationsManager.playSmaller(stepBackwardButton, 300);
        debuggerWindowController.onStepBackwardClick();
    }

    @FXML
    void stepOverButtonAction(ActionEvent event) {
        AnimationsManager.playBigger(stepOverButton, 300);
        debuggerWindowController.onStepOverClick();
    }

    @FXML
    void stopButtonAction(ActionEvent event) {
        debuggerWindowController.onStopButtonClick();
    }

    public void setDebuggerWindowController(DebuggerWindowController debuggerWindowController) {
        this.debuggerWindowController = debuggerWindowController;
    }

    public void disableNewRunButton(boolean disable) {
        newRunButton.setDisable(disable);
    }

    public void enableExecutionButtons(){
        startButton.setDisable(false);
        debugButton.setDisable(false);
    }

    public void reset(){
        startButton.setDisable(true);
        debugButton.setDisable(true);
        resumeButton.setDisable(true);
        stepOverButton.setDisable(true);
        stepBackwardButton.setDisable(true);
        stopButton.setDisable(true);
        newRunButton.setDisable(true);
    }

    public void disableDebuggerControlButtons(boolean disable){
        startButton.setDisable(!disable);
        debugButton.setDisable(!disable);
        resumeButton.setDisable(disable);
        stepOverButton.setDisable(disable);
        stepBackwardButton.setDisable(disable);
        stopButton.setDisable(disable);
    }


}
