package gui.components.historywindow;

import dto.RunResultsDTO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class HistoryWindowController {

    @FXML
    private TableColumn<RunResultsDTO, Integer> colDegree;

    @FXML
    private TableColumn<RunResultsDTO, Integer> colExecution;

    @FXML
    private TableColumn<RunResultsDTO, Integer> colTotalCycles;

    @FXML
    private TableColumn<RunResultsDTO, Long> colYResult;

    @FXML
    private TableView<RunResultsDTO> historyTable;

    @FXML
    private Button reRunButton;

    @FXML
    private Button showButton;

    @FXML
    public void initialize() {
        // degree
        colDegree.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getDegree()).asObject());

        // execution index
        colExecution.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(historyTable.getItems().indexOf(cellData.getValue()) + 1).asObject());

        // total cycles
        colTotalCycles.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getTotalCyclesCount()).asObject());

        // y result
        colYResult.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getYValue()));
    }

    public void updateHistoryTable(List<RunResultsDTO> history){
        historyTable.getItems().clear();
        historyTable.getItems().addAll(history);
    }
}
