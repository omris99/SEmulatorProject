package gui.components.availableusers;

import clientserverdto.ExecutionHistoryDTO;
import clientserverdto.UserDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AvailableUsersTableController {
    private Timer timer;
    private TimerTask listRefresher;
    private boolean autoUpdate;
    private boolean isRefreshing;


    @FXML
    private TableColumn<UserDTO, Integer> colCreditBalance;

    @FXML
    private TableColumn<UserDTO, Integer> colCreditsUsed;

    @FXML
    private TableColumn<UserDTO, Integer> colExecutionsPerformed;

    @FXML
    private TableColumn<UserDTO, Integer> colFunctionsContributed;

    @FXML
    private TableColumn<UserDTO, Integer> colMainProgramsUploaded;

    @FXML
    private TableColumn<UserDTO, String> colUserName;

    @FXML
    private Button unselectUserButton;

    @FXML
    private TableView<UserDTO> usersTable;

    private final ObservableList<UserDTO> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colMainProgramsUploaded.setCellValueFactory(new PropertyValueFactory<>("mainProgramsUploaded"));
        colFunctionsContributed.setCellValueFactory(new PropertyValueFactory<>("functionsContributed"));
        colCreditsUsed.setCellValueFactory(new PropertyValueFactory<>("creditsUsed"));
        colCreditBalance.setCellValueFactory(new PropertyValueFactory<>("creditBalance"));
        colExecutionsPerformed.setCellValueFactory(new PropertyValueFactory<>("executionsPerformed"));


        usersTable.setItems(data);

        usersTable.setRowFactory(tv -> new TableRow<UserDTO>() {
            @Override
            protected void updateItem(UserDTO item, boolean empty) {
                super.updateItem(item, empty);
            }
        });
    }


    @FXML
    void onUnselectButtonClick(ActionEvent event) {
        usersTable.getSelectionModel().clearSelection();
    }

    public void updateUsersList(List<UserDTO> users) {
        isRefreshing = true;
        Platform.runLater(() -> {
            try {
                UserDTO selectedUser = usersTable.getSelectionModel().getSelectedItem();
                String selectedUserName = (selectedUser != null) ? selectedUser.getUserName() : null;

                data.setAll(users);

                if (selectedUserName != null) {
                    for (UserDTO user : data) {
                        if (user.getUserName().equals(selectedUserName)) {
                            usersTable.getSelectionModel().select(user);
                            break;
                        }
                    }
                }
            } finally {
                isRefreshing = false;
            }
        });
    }

    public void startTableRefresher() {
        autoUpdate = true;
        listRefresher = new AvailabaleUsersRefresher(
                autoUpdate,
                this::updateUsersList);
        timer = new Timer();
        timer.schedule(listRefresher, 500, 500);
    }

    public TableView<UserDTO> getTable() {
        return usersTable;
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }
}
