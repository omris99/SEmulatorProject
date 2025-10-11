package gui.components.userswindow;

import clientserverdto.ExecutionHistoryDTO;
import clientserverdto.UserDTO;
import gui.components.availableusers.AvailableUsersTableController;
import gui.components.userhistorytable.UsersHistoryTableController;
import gui.screens.dashboard.DashBoardController;
import http.HttpClientUtil;
import http.ServerPaths;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import json.GsonFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static gui.app.ClientController.showErrorAlert;

public class UsersWindowController implements Closeable {
    @FXML
    private AvailableUsersTableController availableUsersTableController;

    @FXML
    private UsersHistoryTableController usersHistoryTableController;

    private DashBoardController dashBoardController;

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
                Platform.runLater(() -> showErrorAlert(
                        "Error to Fetch History",
                        "Failed to fetch execution history from server",
                        e.getMessage()));
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
                    Platform.runLater(() -> showErrorAlert(
                            ("HTTP " + response.code() + " Error"),
                            ("Failed to fetch history from server"),
                            null));
                }

                response.close();
            }
        });
    }

    public void setHistory() {
        setHistory(null);
    }

    public void reRunSelectedHistory(ExecutionHistoryDTO selectedRun) {
        dashBoardController.reRunSelectedHistory(selectedRun);
    }


    @FXML
    public void initialize() {
        usersHistoryTableController.setUsersWindowController(this);
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

    public void setDashboardController(DashBoardController dashboardController) {
        this.dashBoardController = dashboardController;
    }

    @Override
    public void close() throws IOException {
        availableUsersTableController.close();
    }
}
