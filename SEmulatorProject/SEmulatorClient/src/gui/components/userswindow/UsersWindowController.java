package gui.components.userswindow;

import clientserverdto.ExecutionHistoryDTO;
import clientserverdto.InstructionDTO;
import clientserverdto.UserDTO;
import gui.components.availableusers.AvailableUsersTableController;
import gui.components.userhistorytable.UsersHistoryTableController;
import http.HttpClientUtil;
import http.ServerPaths;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import serverengine.logic.json.GsonFactory;
import serverengine.logic.utils.Utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UsersWindowController {
    @FXML
    private AvailableUsersTableController availableUsersTableController;

    @FXML
    private UsersHistoryTableController usersHistoryTableController;

    public void startAvailableUsersTableRefresher() {
        availableUsersTableController.startTableRefresher();
    }

    private void setHistory(String userName) {
        Request request;
        if(userName == null) {
            request = HttpClientUtil.createGetRequest(ServerPaths.GET_HISTORY);
        }
        else {
            request = new Request.Builder()
                    .url(HttpUrl.parse(ServerPaths.GET_HISTORY)
                            .newBuilder()
                            .addQueryParameter("username", userName)
                            .build()
                            .toString())
                    .build();
        }
        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // In case of failure, set an empty history
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String historyJson = response.body().string();
                    ExecutionHistoryDTO[] history = GsonFactory.getGson().fromJson(historyJson, ExecutionHistoryDTO[].class);
                    List<ExecutionHistoryDTO> userHistory = Arrays.asList(history);
                    Platform.runLater(() -> {
                        if (userHistory != null) {
                            usersHistoryTableController.setHistory(userHistory);
                        } else {
                            usersHistoryTableController.setHistory(List.of());
                        }
                    });
                } else {
                    Platform.runLater(() -> usersHistoryTableController.setHistory(List.of()));
                }
            }
        });
    }

    public void setHistory() {
        setHistory(null);
    }

    @FXML
    public void initialize() {
        availableUsersTableController.getTable().getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<UserDTO>() {
                    @Override
                    public void changed(ObservableValue<? extends UserDTO> obs, UserDTO oldSel, UserDTO newSel) {
                        if (availableUsersTableController.isRefreshing()) {
                            return;
                        }

                        if (oldSel != null && newSel != null &&
                                Objects.equals(oldSel.getUserName(), newSel.getUserName())) {
                            return;
                        }

                        if (newSel != null) {
                            String userName = newSel.getUserName();
                            setHistory(userName);
                        }
                        else {
                            setHistory(null);
                        }
                    }
                }
        );
    }
}
