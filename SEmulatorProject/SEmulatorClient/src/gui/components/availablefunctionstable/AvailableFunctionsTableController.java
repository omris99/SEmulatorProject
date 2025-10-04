package gui.components.availablefunctionstable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AvailableFunctionsTableController {

    @FXML
    private TableColumn<?, ?> colContextProgram;

    @FXML
    private TableColumn<?, ?> colInstructionsCount;

    @FXML
    private TableColumn<?, ?> colMaximalDegree;

    @FXML
    private TableColumn<?, ?> colUploadedBy;

    @FXML
    private TableColumn<?, ?> colUserName;

    @FXML
    private Button executeFunctionButton;

    @FXML
    private TableView<?> usersTable;

    @FXML
    void onExecuteFunctionButtonClick(ActionEvent event) {

    }

}
