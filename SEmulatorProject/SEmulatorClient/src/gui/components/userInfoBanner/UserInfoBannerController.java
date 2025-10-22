package gui.components.userInfoBanner;

import clientserverdto.UserDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserInfoBannerController {

    @FXML
    private Label availableCreditsLabel;

    @FXML
    private Label userNameLabel;

    public void updateUserInfo(UserDTO userDTO) {
        userNameLabel.setText(userDTO.getUserName());
        availableCreditsLabel.setText(String.valueOf(userDTO.getCreditBalance()));
    }

    public String getUserName() {
        return userNameLabel.getText();
    }
}
