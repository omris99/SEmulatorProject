package server.servlets.chat;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import json.GsonFactory;
import server.utils.ServletUtils;
import serverengine.users.UserManager;

@WebServlet(name = "usersListServlet", urlPatterns = {"/chat/userslist"})
public class UsersListServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            Set<String> usersList = userManager.getUsers().keySet();
            String json = GsonFactory.getGson().toJson(usersList);
            out.println(json);
            out.flush();
        }
    }
}
