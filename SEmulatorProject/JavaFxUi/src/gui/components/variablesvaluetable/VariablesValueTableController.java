package gui.components.variablesvaluetable;

import dto.RunResultsDTO;
import gui.app.AnimationsManager;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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

    private final ObservableList<VariableValueRow> data = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        colVariable.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getVariableName()));

        colVariableValue.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getValue()));

        variablesDisplayTable.setItems(data);

        variablesDisplayTable.setRowFactory(tv -> new TableRow<VariableValueRow>() {
            @Override
            protected void updateItem(VariableValueRow item, boolean empty) {
                super.updateItem(item, empty);

                getStyleClass().removeAll("value-changed");

                if (item == null || empty) {
                    return;
                }

                if (item.isHighlighted()) {
                    getStyleClass().add("value-changed");
                    AnimationsManager.playFadeIn(tv, 200);
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

        public Long getValue() {
            return value;
        }

        public void setValue(Long newValue) {
            this.previousValue = this.value;
            this.value = newValue;
        }

        public boolean isHighlighted() {
            return previousValue != null && !(previousValue.equals(value));
        }

    }

    private void updateOrAddRow(String name, Long value) {
        VariableValueRow row = data.stream()
                .filter(r -> r.getVariableName().equals(name))
                .findFirst()
                .orElse(null);

        if (row != null) {
            row.setValue(value);
        } else {
            data.add(new VariableValueRow(name, value));
        }
    }

    public void updateTable(RunResultsDTO runResultsDTO) {
        updateOrAddRow("Y-RESULT", runResultsDTO.getYValue());

        runResultsDTO.getInputVariablesValueResult().forEach(this::updateOrAddRow);
        runResultsDTO.getWorkVariablesValues().forEach(this::updateOrAddRow);

        variablesDisplayTable.refresh();

        Platform.runLater(() -> {
            data.stream()
                    .filter(VariableValueRow::isHighlighted)
                    .findFirst()
                    .ifPresent(instruction -> variablesDisplayTable.scrollTo(instruction));
        });
    }


    public void reset() {
        variablesDisplayTable.getItems().clear();
    }

    public void unmarkHighlightedLines() {
        for (VariableValueRow row : variablesDisplayTable.getItems()) {
            row.previousValue = null;
        }

        variablesDisplayTable.refresh();
    }
}
