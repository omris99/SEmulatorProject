package gui.components.variablesvaluetable;

import dto.RunResultsDTO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class VariablesValueTableController {
    @FXML
    private TableColumn<VariableValueRow, String> colVariable;

    @FXML
    private TableColumn<VariableValueRow, Long> colVariableValue;

    @FXML
    private TableView<VariableValueRow> variablesDisplayTable;

    @FXML
    public void initialize() {
        colVariable.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getVariableName()));

        colVariableValue.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getValue()));
    }

    public static class VariableValueRow {
        private final String variableName;
        private final Long value;

        public VariableValueRow(String variableName, Long value) {
            this.variableName = variableName;
            this.value = value;
        }

        private String getVariableName() {
            return variableName;
        }

        private Long getValue() {
            return value;
        }
    }

    public void updateTable(RunResultsDTO runResultsDTO) {
        ObservableList<VariableValueRow> rows = javafx.collections.FXCollections.observableArrayList();

        rows.add(new VariableValueRow("Y-RESULT", runResultsDTO.getYValue()));

        runResultsDTO.getInputVariablesValueResult().forEach(
                (name, value) -> rows.add(new VariableValueRow(name, value))
        );

        runResultsDTO.getWorkVariablesValues().forEach(
                (name, value) -> rows.add(new VariableValueRow(name, value))
        );

        variablesDisplayTable.setItems(rows);
    }

    public void reset() {
        variablesDisplayTable.getItems().clear();
    }

}
