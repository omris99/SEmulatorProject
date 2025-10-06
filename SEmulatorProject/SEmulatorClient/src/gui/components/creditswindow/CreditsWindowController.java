package gui.components.creditswindow;

import gui.dashboard.DashBoardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import okhttp3.Request;

public class CreditsWindowController {

    @FXML
    private Label availableCreditsLabel;

    @FXML
    private Button chargeCreditButton;

    @FXML
    private TextField creditsTextField;

    @FXML
    private DashBoardController dashBoardController;

    @FXML
    public void onChargeCreditButtonClicked(ActionEvent event) {
        dashBoardController.chargeCredits(creditsTextField.getText());

    }

    public void resetCreditsInput(){
        creditsTextField.setText("0");
    }
    public void setDashBoardController(DashBoardController dashBoardController) {
        this.dashBoardController = dashBoardController;
    }
}
