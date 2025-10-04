package gui.components.availablefunctionstable;

import dto.ProgramDTO;
import dto.UserDTO;
import gui.components.availableusers.AvailabaleUsersRefresher;
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

public class AvailableFunctionsTableController {
    private Timer timer;
    private TimerTask listRefresher;
    private boolean autoUpdate;

    @FXML
    private TableColumn<ProgramDTO, String> colContextProgram;

    @FXML
    private TableColumn<ProgramDTO, Integer> colInstructionsCount;

    @FXML
    private TableColumn<ProgramDTO, Integer> colMaximalDegree;

    @FXML
    private TableColumn<ProgramDTO, Integer> colUploadedBy;

    @FXML
    private TableColumn<ProgramDTO, Integer> colName;

    @FXML
    private Button executeFunctionButton;

    @FXML
    private TableView<ProgramDTO> functionsTable;

    private final ObservableList<ProgramDTO> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUploadedBy.setCellValueFactory(new PropertyValueFactory<>("uploadedBy"));
        colContextProgram.setCellValueFactory(new PropertyValueFactory<>("contextProgram"));
        colInstructionsCount.setCellValueFactory(new PropertyValueFactory<>("instructionsCount"));
        colMaximalDegree.setCellValueFactory(new PropertyValueFactory<>("maximalDegree"));

        functionsTable.setItems(data);

        functionsTable.setRowFactory(tv -> new TableRow<ProgramDTO>() {
            @Override
            protected void updateItem(ProgramDTO item, boolean empty) {
                super.updateItem(item, empty);
            }
        });
    }



    @FXML
    void onExecuteFunctionButtonClick(ActionEvent event) {

    }



    public void updateUsersList(List<ProgramDTO> users) {
        Platform.runLater(() -> {
            ProgramDTO selectedProgram = functionsTable.getSelectionModel().getSelectedItem();
            String selectedProgramName = (selectedProgram != null) ? selectedProgram.getName() : null;

            data.setAll(users);

            if (selectedProgramName != null) {
                for (ProgramDTO function : data) {
                    if (function.getName().equals(selectedProgramName)) {
                        functionsTable.getSelectionModel().select(function);
                        break;
                    }
                }
            }
        });
    }

    public void startTableRefresher() {
        autoUpdate = true;
        listRefresher = new AvailableFunctionsTableRefresher(
                autoUpdate,
                this::updateUsersList);
        timer = new Timer();
        timer.schedule(listRefresher, 500, 500);
    }

}
