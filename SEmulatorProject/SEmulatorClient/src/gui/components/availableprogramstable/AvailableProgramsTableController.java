package gui.components.availableprogramstable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AvailableProgramsTableController {

    @FXML
    private TableColumn<?, ?> colCreditsCost;

    @FXML
    private TableColumn<?, ?> colInstructionsCount;

    @FXML
    private TableColumn<?, ?> colMaximalDegree;

    @FXML
    private TableColumn<?, ?> colTotalExecutions;

    @FXML
    private TableColumn<?, ?> colUploadedBy;

    @FXML
    private TableColumn<?, ?> colUserName;

    @FXML
    private Button onExecuteProgramButtonClick;

    @FXML
    private TableView<?> usersTable;

    @FXML
    void onExecuteProgramButtonClick(ActionEvent event) {

    }

}
