package gui.components.variablesvaluetable;

import dto.InstructionDTO;
import dto.RunResultsDTO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
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

        variablesDisplayTable.setRowFactory(tv -> new TableRow<VariableValueRow>() {
            @Override
            protected void updateItem(VariableValueRow item, boolean empty) {
                super.updateItem(item, empty);

                getStyleClass().removeAll("value-changed");

                if (item == null || empty) {
                    return;
                }

                boolean isChanged = item.getPreviousValue() != null && !item.getPreviousValue().equals(item.getValue());
                if (isChanged) {
                    getStyleClass().add("value-changed");
                }
            }
        });

    }

    public static class VariableValueRow {
        private final String variableName;
        private Long value;
        private Long previousValue;

        public VariableValueRow(String variableName, Long value) {
            this.variableName = variableName;
            this.value = value;
        }

        private String getVariableName() {
            return variableName;
        }

        public Long getValue() { return value; }
        public void setValue(Long newValue) {
            this.previousValue = this.value; // קודם נשמור את הקודם
            this.value = newValue;
        }
        public Long getPreviousValue() { return previousValue; }

    }

    public void updateTable(RunResultsDTO runResultsDTO) {
        ObservableList<VariableValueRow> items = variablesDisplayTable.getItems();

        if (items.isEmpty()) {
            items.add(new VariableValueRow("Y-RESULT", runResultsDTO.getYValue()));
            runResultsDTO.getInputVariablesValueResult().forEach(
                    (name, value) -> items.add(new VariableValueRow(name, value))
            );
            runResultsDTO.getWorkVariablesValues().forEach(
                    (name, value) -> items.add(new VariableValueRow(name, value))
            );
        } else {
            for (VariableValueRow row : items) {
                Long newValue = null;

                if (row.getVariableName().equals("Y-RESULT")) {
                    newValue = runResultsDTO.getYValue();
                } else if (runResultsDTO.getInputVariablesValueResult().containsKey(row.getVariableName())) {
                    newValue = runResultsDTO.getInputVariablesValueResult().get(row.getVariableName());
                } else if (runResultsDTO.getWorkVariablesValues().containsKey(row.getVariableName())) {
                    newValue = runResultsDTO.getWorkVariablesValues().get(row.getVariableName());
                }

                if (newValue != null) {
                    row.setValue(newValue);
                }
            }
        }

        variablesDisplayTable.refresh();
    }

    public void reset() {
        variablesDisplayTable.getItems().clear();
    }

}
