package gui.components.userhistorytable;

import clientserverdto.ExecutionHistoryDTO;
import gui.components.userswindow.UsersWindowController;
import gui.popups.showallvariables.ShowAllVariablesController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class UsersHistoryTableController {
    private UsersWindowController usersWindowController;

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

    public void onShowButtonClick() {
        ExecutionHistoryDTO selected = userHistoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/popups/showallvariables/ShowAllVariables.fxml"));
            Parent load = loader.load();

            ShowAllVariablesController controller = loader.getController();
            controller.setRunResults(selected.getRunResults());

            Scene scene = new Scene(load, 400, 200);
            Stage showWindow = new Stage();
            showWindow.setTitle("Show All Variables");
            showWindow.setScene(scene);
            showWindow.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onReRunButtonClick() {
        ExecutionHistoryDTO selected = userHistoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        usersWindowController.reRunSelectedHistory(selected);
    }

    public void setHistory(List<ExecutionHistoryDTO> history) {
        data.setAll(history);
    }

    public void setUsersWindowController(UsersWindowController usersWindowController) {
        this.usersWindowController = usersWindowController;
    }
}
