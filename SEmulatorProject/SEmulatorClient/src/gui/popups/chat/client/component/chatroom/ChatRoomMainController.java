package gui.popups.chat.client.component.chatroom;

import gui.popups.chat.client.component.api.ChatCommands;
import gui.popups.chat.client.component.api.HttpStatusUpdate;
import gui.popups.chat.client.component.chatarea.ChatAreaController;
import gui.popups.chat.client.component.commands.CommandsController;
import gui.popups.chat.client.component.main.ChatAppMainController;
import gui.popups.chat.client.component.users.UsersListController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.Closeable;
import java.io.IOException;

public class ChatRoomMainController implements Closeable, HttpStatusUpdate, ChatCommands {

    @FXML private VBox usersListComponent;
    @FXML private UsersListController usersListComponentController;
    @FXML private VBox actionCommandsComponent;
    @FXML private CommandsController actionCommandsComponentController;
    @FXML private GridPane chatAreaComponent;
    @FXML private ChatAreaController chatAreaComponentController;

    private ChatAppMainController chatAppMainController;

    @FXML
    public void initialize() {
        usersListComponentController.setHttpStatusUpdate(this);
        actionCommandsComponentController.setChatCommands(this);
        chatAreaComponentController.setHttpStatusUpdate(this);

        chatAreaComponentController.autoUpdatesProperty().bind(actionCommandsComponentController.autoUpdatesProperty());
        usersListComponentController.autoUpdatesProperty().bind(actionCommandsComponentController.autoUpdatesProperty());
    }

    @Override
    public void updateHttpLine(String line) {
        chatAppMainController.updateHttpLine(line);
    }

    @Override
    public void close() throws IOException {
        usersListComponentController.close();
        chatAreaComponentController.close();
    }

    public void setActive() {
        usersListComponentController.startListRefresher();
        chatAreaComponentController.startListRefresher();
    }

    public void setInActive() {
        try {
            usersListComponentController.close();
            chatAreaComponentController.close();
        } catch (Exception ignored) {}
    }

    public void setChatAppMainController(ChatAppMainController chatAppMainController) {
        this.chatAppMainController = chatAppMainController;
    }

    @Override
    public void logout() {
        chatAppMainController.switchToLogin();
    }
}
