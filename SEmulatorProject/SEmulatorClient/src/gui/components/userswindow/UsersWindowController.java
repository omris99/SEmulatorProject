package gui.components.userswindow;

import gui.components.availableusers.AvailableUsersTableController;
import javafx.fxml.FXML;

public class UsersWindowController {
    @FXML
    AvailableUsersTableController availableUsersTableController;

    public void startAvailableUsersTableRefresher() {
        availableUsersTableController.startTableRefresher();
    }

}
