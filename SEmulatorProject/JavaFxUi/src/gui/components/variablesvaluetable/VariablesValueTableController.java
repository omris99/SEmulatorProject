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

import java.util.function.BiConsumer;

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
                item.isHighlighted = isChanged;

                if(item.isHighlighted) {
                    getStyleClass().add("value-changed");
                }
            }
        });

    }

    public static class VariableValueRow {
        private final String variableName;
        private Long value;
        private Long previousValue;
        private boolean isHighlighted;

        public VariableValueRow(String variableName, Long value) {
            this.variableName = variableName;
            this.value = value;
        }

        private String getVariableName() {
            return variableName;
        }

        public Long getValue() { return value; }
        public void setValue(Long newValue) {
            this.previousValue = this.value;
            this.value = newValue;
        }
        public Long getPreviousValue() { return previousValue; }

    }

    public void updateTable(RunResultsDTO runResultsDTO) {
        ObservableList<VariableValueRow> items = variablesDisplayTable.getItems();

        BiConsumer<String, Long> addOrUpdate = (name, value) -> {
            VariableValueRow row = items.stream()
                    .filter(r -> r.getVariableName().equals(name))
                    .findFirst()
                    .orElse(null);

            if (row != null) {
                row.setValue(value);
            } else {
                items.add(new VariableValueRow(name, value));
            }
        };

        addOrUpdate.accept("Y-RESULT", runResultsDTO.getYValue());

        runResultsDTO.getInputVariablesValueResult()
                .forEach(addOrUpdate);

        runResultsDTO.getWorkVariablesValues()
                .forEach(addOrUpdate);

        variablesDisplayTable.refresh();
    }

    public void reset() {
        variablesDisplayTable.getItems().clear();
    }

    public void unmarkHighlightedLines() {
        for (VariableValueRow row : variablesDisplayTable.getItems()) {
            row.isHighlighted = false;
            row.previousValue = null;
        }

        variablesDisplayTable.refresh();
    }

}
