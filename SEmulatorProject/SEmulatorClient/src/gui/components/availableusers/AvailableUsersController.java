package gui.components.availableusers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AvailableUsersController {

    @FXML
    private TableColumn<?, ?> colCreditBalance;

    @FXML
    private TableColumn<?, ?> colCreditsUsed;

    @FXML
    private TableColumn<?, ?> colExecutionsPerformed;

    @FXML
    private TableColumn<?, ?> colFunctionsContributed;

    @FXML
    private TableColumn<?, ?> colMainProgramsUploaded;

    @FXML
    private TableColumn<?, ?> colUserName;

    @FXML
    private Button unselectUserButton;

    @FXML
    private TableView<?> usersTable;

    @FXML
    void onUnselectButtonClick(ActionEvent event) {

    }

}
