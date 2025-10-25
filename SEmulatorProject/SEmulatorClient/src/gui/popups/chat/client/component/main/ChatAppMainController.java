package gui.popups.chat.client.component.main;

import gui.popups.chat.client.component.api.HttpStatusUpdate;
import gui.popups.chat.client.component.chatroom.ChatRoomMainController;
import gui.popups.chat.client.component.status.StatusController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;

import static gui.popups.chat.client.util.Constants.CHAT_ROOM_FXML_RESOURCE_LOCATION;
import static gui.popups.chat.client.util.Constants.JHON_DOE;


public class ChatAppMainController implements Closeable, HttpStatusUpdate {

    @FXML private Parent httpStatusComponent;
    @FXML private StatusController httpStatusComponentController;

    private Parent chatRoomComponent;
    private ChatRoomMainController chatRoomComponentController;

    @FXML private Label userGreetingLabel;
    @FXML private AnchorPane mainPanel;

    private final StringProperty currentUserName;

    public ChatAppMainController() {
        currentUserName = new SimpleStringProperty(JHON_DOE);
    }

    @FXML
    public void initialize() {
        userGreetingLabel.textProperty().bind(Bindings.concat("Hello ", currentUserName));
        loadChatRoomPage();
    }

    public void updateUserName(String userName) {
        currentUserName.set(userName);
    }
    
    private void setMainPanelTo(Parent pane) {
        mainPanel.getChildren().clear();
        mainPanel.getChildren().add(pane);
        AnchorPane.setBottomAnchor(pane, 1.0);
        AnchorPane.setTopAnchor(pane, 1.0);
        AnchorPane.setLeftAnchor(pane, 1.0);
        AnchorPane.setRightAnchor(pane, 1.0);
    }

    @Override
    public void close() throws IOException {
        chatRoomComponentController.close();
    }

    private void loadChatRoomPage() {
        URL loginPageUrl = getClass().getResource(CHAT_ROOM_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            chatRoomComponent = fxmlLoader.load();
            chatRoomComponentController = fxmlLoader.getController();
            chatRoomComponentController.setChatAppMainController(this);
            setMainPanelTo(chatRoomComponent);
            chatRoomComponentController.setActive();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateHttpLine(String line) {
        httpStatusComponentController.addHttpStatusLine(line);
    }

    public void switchToChatRoom() {
        setMainPanelTo(chatRoomComponent);
        chatRoomComponentController.setActive();
    }
}
