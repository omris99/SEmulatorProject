//package gui.components.displaycommandsbar;
//
//import gui.app.ClientController;
//import gui.app.resources.themes.Theme;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.ChoiceBox;
//
//public class DisplayCommandsBarController {
//
//    @FXML
//    private Button animationsButton;
//
//    @FXML
//    private ChoiceBox<Theme> themeChoiceBox;
//
//    @FXML
//    private ClientController clientController;
//
//    @FXML
//    private Button showTreeTableViewButton;
//
//    @FXML
//    private Button specificExpansionButton;
//
//    private boolean animationsDisabled = true;
//
//    @FXML
//    public void initialize() {
//        animationsButton.setText("OFF");
//
//        for(Theme theme : Theme.values()) {
//            themeChoiceBox.getItems().add(theme);
//        }
//
//        themeChoiceBox.getSelectionModel().select(Theme.CLASSIC);
//        themeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
//            clientController.changeTheme(newVal);
//        });
//    }
//
//    @FXML
//    void onAnimationsButtonClick(ActionEvent event) {
//        animationsDisabled = !animationsDisabled;
//        animationsButton.setText(animationsDisabled ? "OFF" : "ON");
//        clientController.disableAnimations(animationsDisabled);
//    }
//
//    @FXML
//    void onShowTreeTableViewButtonClick(ActionEvent event) {
//        clientController.showOnScreenProgramTreeTableView();
//
//    }
//
//    @FXML
//    void onSpecificExpansionButtonClick(ActionEvent event) {
//        clientController.showSpecificExpansionView();
//
//    }
//    public void setCLientController(ClientController clientController) {
//        this.clientController = clientController;
//    }
//
//    public void disableTreeTableViewAndSpecificExpansionButton(boolean disable) {
//        showTreeTableViewButton.setDisable(disable);
//        specificExpansionButton.setDisable(disable);
//    }
//}
