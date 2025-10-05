package gui.components.availableprogramstable;

import clientserverdto.UploadedProgramDTO;
import gui.components.programswindow.ProgramsWindowController;
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

public class AvailableProgramsTableController {
    private Timer timer;
    private TimerTask listRefresher;
    private boolean autoUpdate;

    @FXML
    private ProgramsWindowController programsWindowController;

    @FXML
    private TableColumn<UploadedProgramDTO, String> colTotalExecutions;

    @FXML
    private TableColumn<UploadedProgramDTO, String> colCreditsCost;

    @FXML
    private TableColumn<UploadedProgramDTO, Integer> colInstructionsCount;

    @FXML
    private TableColumn<UploadedProgramDTO, Integer> colMaximalDegree;

    @FXML
    private TableColumn<UploadedProgramDTO, Integer> colUploadedBy;

    @FXML
    private TableColumn<UploadedProgramDTO, Integer> colName;

    @FXML
    private Button executeProgramButton;

    @FXML
    private TableView<UploadedProgramDTO> programsTable;

    private final ObservableList<UploadedProgramDTO> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUploadedBy.setCellValueFactory(new PropertyValueFactory<>("uploadedBy"));
        colTotalExecutions.setCellValueFactory(new PropertyValueFactory<>("totalExecutions"));
        colCreditsCost.setCellValueFactory(new PropertyValueFactory<>("creditsCost"));
        colInstructionsCount.setCellValueFactory(new PropertyValueFactory<>("instructionsCount"));
        colMaximalDegree.setCellValueFactory(new PropertyValueFactory<>("maximalDegree"));

        programsTable.setItems(data);

        programsTable.setRowFactory(tv -> new TableRow<UploadedProgramDTO>() {
            @Override
            protected void updateItem(UploadedProgramDTO item, boolean empty) {
                super.updateItem(item, empty);
            }
        });
    }



    @FXML
    void onExecuteProgramButtonClick(ActionEvent event) {
        UploadedProgramDTO selectedProgram = programsTable.getSelectionModel().getSelectedItem();
        programsWindowController.executeProgramButtonClicked(selectedProgram);
    }



    public void updateProgramsTable(List<UploadedProgramDTO> users) {
        Platform.runLater(() -> {
            UploadedProgramDTO selectedProgram = programsTable.getSelectionModel().getSelectedItem();
            String selectedProgramName = (selectedProgram != null) ? selectedProgram.getProgram().getName() : null;

            data.setAll(users);

            if (selectedProgramName != null) {
                for (UploadedProgramDTO function : data) {
                    if (function.getProgram().getName().equals(selectedProgramName)) {
                        programsTable.getSelectionModel().select(function);
                        break;
                    }
                }
            }
        });
    }

    public void startTableRefresher() {
        autoUpdate = true;
        listRefresher = new AvailableProgramsTableRefresher(
                autoUpdate,
                this::updateProgramsTable);
        timer = new Timer();
        timer.schedule(listRefresher, 500, 500);
    }

    public void setProgramsWindowController(ProgramsWindowController programsWindowController) {
        this.programsWindowController = programsWindowController;
    }
}
