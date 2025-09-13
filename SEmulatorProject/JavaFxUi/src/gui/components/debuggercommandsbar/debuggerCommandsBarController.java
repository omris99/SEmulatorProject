package gui.components.debuggercommandsbar;

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

    }

    @FXML
    void newRunButtonAction(ActionEvent event) {
        debuggerWindowController.onNewRunClick();
    }

    @FXML
    void resumeButtonAction(ActionEvent event) {

    }

    @FXML
    void startButtonAction(ActionEvent event) {
        startButton.setDisable(true);
        debuggerWindowController.onStartClick();
        startButton.setDisable(false);
    }

    @FXML
    void stepBackwardButtonAction(ActionEvent event) {

    }

    @FXML
    void stepOverButtonAction(ActionEvent event) {

    }

    @FXML
    void stopButtonAction(ActionEvent event) {

    }

    public void setDebuggerWindowController(DebuggerWindowController debuggerWindowController) {
        this.debuggerWindowController = debuggerWindowController;
    }

    public void enableNewRunButton(){
        newRunButton.setDisable(false);
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

}
