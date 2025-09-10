package gui.components.debuggerwindow;

import dto.ProgramDTO;
import gui.app.AppController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import gui.components.debuggercommandsbar.debuggerCommandsBarController ;

import java.util.List;

public class DebuggerWindowController {
    private AppController appController;

    @FXML
    private debuggerCommandsBarController debuggerCommandsBarController;

    @FXML
    private TableView<?> variablesDisplayTable; // תוכל למלא בהמשך

    @FXML
    private TableColumn<?, ?> colVariable;

    @FXML
    private TableColumn<?, ?> colVariableValue;

    @FXML
    private TableView<Row> inputVariablesTable;

    @FXML
    private TableColumn<Row, String> colInputVariable;

    @FXML
    private TableColumn<Row, String> colInputVariableValue;

    private final ObservableList<Row> inputRows = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        debuggerCommandsBarController.setDebuggerWindowController(this);

        // עמודת שם המשתנה (לא ניתנת לעריכה)
        colInputVariable.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        // עמודת Value – editable עם TextFieldTableCell
        colInputVariableValue.setCellValueFactory(cellData -> cellData.getValue().valueProperty());

        // שימוש ב-TextFieldTableCell עם StringConverter
        colInputVariableValue.setCellFactory(TextFieldTableCell.forTableColumn());

        // מיישר את הערכים לימין
        colInputVariableValue.setStyle("-fx-alignment: CENTER-RIGHT;");

        // מאזין לעריכה – מעדכן את המודל
        colInputVariableValue.setOnEditCommit(event -> {
            Row row = event.getRowValue();
            row.setValue(event.getNewValue());
        });

        // הפיכת הטבלה לעריכה
        inputVariablesTable.setEditable(true);

        // אתחול הנתונים
        inputVariablesTable.setItems(inputRows);
    }



    /**
     * טוען לתוך הטבלה את שמות המשתנים מתוך ProgramDTO
     */
    public void loadProgram(ProgramDTO programDTO) {
        setInputVariables(programDTO.getInputNames());
    }

    /**
     * ממלא את השמות בשורות
     */
    public void setInputVariables(List<String> names) {
        inputRows.clear();
        for (String name : names) {
            inputRows.add(new Row(name, "0"));
        }
    }

    public void clearInputVariablesTable() {
        inputRows.clear();
    }

    /**
     * מחזיר את הערכים שהמשתמש הקליד
     */
    public List<Row> getInputValues() {
        return inputRows;
    }
    public void onNewRunClick() {
        appController.newRunState();
    }

    public void setProgramInputVariablesInTable(ProgramDTO programDTO) {
        setInputVariables(programDTO.getInputNames());
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    // מודל פנימי ל"שורה" של הטבלה
    public static class Row {
        private final StringProperty name;
        private final StringProperty value;

        public Row(String name, String value) {
            this.name = new SimpleStringProperty(name);
            this.value = new SimpleStringProperty(value);
        }

        public String getName() { return name.get(); }
        public void setName(String n) { name.set(n); }
        public StringProperty nameProperty() { return name; }

        public String getValue() { return value.get(); }
        public void setValue(String v) { value.set(v); }
        public StringProperty valueProperty() { return value; }
    }
}
