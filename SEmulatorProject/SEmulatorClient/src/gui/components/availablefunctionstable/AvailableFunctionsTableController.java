package gui.components.availablefunctionstable;

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

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AvailableFunctionsTableController implements Closeable {
    private Timer timer;
    private TimerTask listRefresher;

    @FXML
    private ProgramsWindowController programsWindowController;

    @FXML
    private TableColumn<UploadedProgramDTO, String> colContextProgram;

    @FXML
    private TableColumn<UploadedProgramDTO, Integer> colInstructionsCount;

    @FXML
    private TableColumn<UploadedProgramDTO, Integer> colMaximalDegree;

    @FXML
    private TableColumn<UploadedProgramDTO, Integer> colUploadedBy;

    @FXML
    private TableColumn<UploadedProgramDTO, Integer> colName;

    @FXML
    private Button executeFunctionButton;

    @FXML
    private TableView<UploadedProgramDTO> functionsTable;

    private final ObservableList<UploadedProgramDTO> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUploadedBy.setCellValueFactory(new PropertyValueFactory<>("uploadedBy"));
        colContextProgram.setCellValueFactory(new PropertyValueFactory<>("contextProgram"));
        colInstructionsCount.setCellValueFactory(new PropertyValueFactory<>("instructionsCount"));
        colMaximalDegree.setCellValueFactory(new PropertyValueFactory<>("maximalDegree"));

        functionsTable.setItems(data);

        functionsTable.setRowFactory(tv -> new TableRow<UploadedProgramDTO>() {
            @Override
            protected void updateItem(UploadedProgramDTO item, boolean empty) {
                super.updateItem(item, empty);
            }
        });
    }



    @FXML
    public void onExecuteFunctionButtonClick(ActionEvent event) {
        UploadedProgramDTO selectedProgram = functionsTable.getSelectionModel().getSelectedItem();
        if (selectedProgram != null) {
            programsWindowController.executeProgramButtonClicked(selectedProgram);
        }
    }



    public void updateFunctionsTable(List<UploadedProgramDTO> users) {
        Platform.runLater(() -> {
            UploadedProgramDTO selectedProgram = functionsTable.getSelectionModel().getSelectedItem();
            String selectedProgramName = (selectedProgram != null) ? selectedProgram.getProgram().getName() : null;

            data.setAll(users);

            if (selectedProgramName != null) {
                for (UploadedProgramDTO function : data) {
                    if (function.getProgram().getName().equals(selectedProgramName)) {
                        functionsTable.getSelectionModel().select(function);
                        break;
                    }
                }
            }
        });
    }

    public void startTableRefresher() {
        listRefresher = new AvailableFunctionsTableRefresher(
                this::updateFunctionsTable);
        timer = new Timer();
        timer.schedule(listRefresher, 500, 500);
    }

    public void setProgramsWindowController(ProgramsWindowController programsWindowController) {
        this.programsWindowController = programsWindowController;
    }

    @Override
    public void close() throws IOException {
        if(listRefresher != null && timer != null) {
            System.out.println("AvailableFunctionsTableController: Closing and cancelling refresher.");
            listRefresher.cancel();
            timer.cancel();
            timer.purge();
            System.out.println("AvailableFunctionsTableController:  CLOSED .");
        }
    }
}
