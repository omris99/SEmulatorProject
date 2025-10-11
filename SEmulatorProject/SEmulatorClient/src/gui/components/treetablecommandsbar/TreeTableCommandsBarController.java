package gui.components.treetablecommandsbar;

import gui.screens.execution.ExecutionScreenController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class TreeTableCommandsBarController {
    @FXML
    private ExecutionScreenController executionScreenController;

    @FXML
    private Button showTreeTableViewButton;

    @FXML
    private Button specificExpansionButton;


    @FXML
    void onShowTreeTableViewButtonClick(ActionEvent event) {
        executionScreenController.showOnScreenProgramTreeTableView();
    }

    @FXML
    void onSpecificExpansionButtonClick(ActionEvent event) {
        executionScreenController.showSpecificExpansionView();

    }
    public void setExecutionScreenController(ExecutionScreenController executionScreenController) {
        this.executionScreenController = executionScreenController;
    }

    public void disableTreeTableViewAndSpecificExpansionButton(boolean disable) {
        showTreeTableViewButton.setDisable(disable);
        specificExpansionButton.setDisable(disable);
    }
}
