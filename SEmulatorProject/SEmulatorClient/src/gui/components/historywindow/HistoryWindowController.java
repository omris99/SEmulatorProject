//package gui.components.historywindow;
//
//import clientserverdto.RunResultsDTO;
//import gui.app.ClientController;
//import gui.popups.showallvariables.ShowAllVariablesController;
//import javafx.application.Platform;
//import javafx.beans.property.SimpleIntegerProperty;
//import javafx.beans.property.SimpleObjectProperty;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.stage.Stage;
//
//import java.util.List;
//
//public class HistoryWindowController {
//    private ClientController clientController;
//
//    @FXML
//    private TableColumn<RunResultsDTO, Integer> colDegree;
//
//    @FXML
//    private TableColumn<RunResultsDTO, Integer> colExecution;
//
//    @FXML
//    private TableColumn<RunResultsDTO, Integer> colTotalCycles;
//
//    @FXML
//    private TableColumn<RunResultsDTO, Long> colYResult;
//
//    @FXML
//    private TableView<RunResultsDTO> historyTable;
//
//    @FXML
//    private Button reRunButton;
//
//    @FXML
//    private Button showButton;
//
//    @FXML
//    public void initialize() {
//        colDegree.setCellValueFactory(cellData ->
//                new SimpleIntegerProperty(cellData.getValue().getDegree()).asObject());
//
//        colExecution.setCellValueFactory(cellData ->
//                new SimpleIntegerProperty(historyTable.getItems().indexOf(cellData.getValue()) + 1).asObject());
//
//        colTotalCycles.setCellValueFactory(cellData ->
//                new SimpleIntegerProperty(cellData.getValue().getTotalCyclesCount()).asObject());
//
//        colYResult.setCellValueFactory(cellData ->
//                new SimpleObjectProperty<>(cellData.getValue().getYValue()));
//    }
//
//    public void updateHistoryTable(List<RunResultsDTO> history){
//        historyTable.getItems().clear();
//        historyTable.getItems().addAll(history);
//    }
//
//    public void onShowButtonClick() {
//        RunResultsDTO selected = historyTable.getSelectionModel().getSelectedItem();
//        if (selected == null) {
//            return;
//        }
//
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/popups/showallvariables/ShowAllVariables.fxml"));
//            Parent load = loader.load();
//
//            ShowAllVariablesController controller = loader.getController();
//            controller.setRunResults(selected);
//
//            Scene scene = new Scene(load, 400, 200);
//            Stage showWindow = new Stage();
//            showWindow.setTitle("Show All Variables");
//            showWindow.setScene(scene);
//            showWindow.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void onReRunButtonClick() {
//        RunResultsDTO selected = historyTable.getSelectionModel().getSelectedItem();
//        if (selected == null) {
//            return;
//        }
//
//        clientController.reRunSelectedHistory(selected);
//    }
//
//    public void setClientController(ClientController clientController) {
//        this.clientController = clientController;
//    }
//
//    public void reset(){
//            historyTable.getItems().clear();
//    }
//
//    public void disableReRunButton(boolean disable){
//        Platform.runLater(() -> reRunButton.setDisable(disable));
//    }
//}
