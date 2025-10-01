package gui.components.displaycommandsbar;

import gui.app.AppController;
import gui.app.Theme;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class DisplayCommandsBarController {

    @FXML
    private Button animationsButton;

    @FXML
    private ChoiceBox<Theme> themeChoiceBox;

    @FXML
    private AppController appController;

    @FXML
    private Button showTreeTableViewButton;

    @FXML
    private Button specificExpansionButton;

    private boolean animationsDisabled = true;

    @FXML
    public void initialize() {
        animationsButton.setText("OFF");

        for(Theme theme : Theme.values()) {
            themeChoiceBox.getItems().add(theme);
        }

        themeChoiceBox.getSelectionModel().select(Theme.CLASSIC);
        themeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            appController.changeTheme(newVal);
        });
    }

    @FXML
    void onAnimationsButtonClick(ActionEvent event) {
        animationsDisabled = !animationsDisabled;
        animationsButton.setText(animationsDisabled ? "OFF" : "ON");
        appController.disableAnimations(animationsDisabled);
    }

    @FXML
    void onShowTreeTableViewButtonClick(ActionEvent event) {
        appController.showOnScreenProgramTreeTableView();

    }

    @FXML
    void onSpecificExpansionButtonClick(ActionEvent event) {
        appController.showSpecificExpansionView();

    }
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void disableTreeTableViewAndSpecificExpansionButton(boolean disable) {
        showTreeTableViewButton.setDisable(disable);
        specificExpansionButton.setDisable(disable);
    }
}
