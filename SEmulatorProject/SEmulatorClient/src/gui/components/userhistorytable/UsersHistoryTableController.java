package gui.components.userhistorytable;

import clientserverdto.ExecutionHistoryDTO;
import clientserverdto.InstructionDTO;
import clientserverdto.UploadedProgramDTO;
import gui.components.historywindow.HistoryWindowController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class UsersHistoryTableController {

    @FXML
    private TableColumn<ExecutionHistoryDTO, String> colArchitecture;

    @FXML
    private TableColumn<ExecutionHistoryDTO, Integer> colDegree;

    @FXML
    private TableColumn<ExecutionHistoryDTO, Integer> colExecutionNumber;

    @FXML
    private TableColumn<ExecutionHistoryDTO, String> colProgramName;

    @FXML
    private TableColumn<ExecutionHistoryDTO, String> colProgramType;

    @FXML
    private TableColumn<ExecutionHistoryDTO, Integer> colTotalCycles;

    @FXML
    private TableColumn<ExecutionHistoryDTO, Integer> colYResult;

    @FXML
    private TableView<ExecutionHistoryDTO> userHistoryTable;
    private final ObservableList<ExecutionHistoryDTO> data = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        colArchitecture.setCellValueFactory(new PropertyValueFactory<>("Architecture"));
        colDegree.setCellValueFactory(new PropertyValueFactory<>("Degree"));
        colExecutionNumber.setCellValueFactory(new PropertyValueFactory<>("ExecutionNumber"));
        colProgramName.setCellValueFactory(new PropertyValueFactory<>("ProgramName"));
        colProgramType.setCellValueFactory(new PropertyValueFactory<>("ProgramType"));
        colTotalCycles.setCellValueFactory(new PropertyValueFactory<>("TotalCyclesCount"));
        colYResult.setCellValueFactory(new PropertyValueFactory<>("yValue"));

        userHistoryTable.setItems(data);

        userHistoryTable.setRowFactory(tv -> new TableRow<ExecutionHistoryDTO>() {
            @Override
            protected void updateItem(ExecutionHistoryDTO item, boolean empty) {
                super.updateItem(item, empty);
            }
        });
    }

    public void setHistory(List<ExecutionHistoryDTO> history) {
        data.setAll(history);
    }}
