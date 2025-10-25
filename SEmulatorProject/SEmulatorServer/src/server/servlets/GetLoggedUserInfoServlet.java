package server.servlets;

import clientserverdto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.SessionUtils;
import json.GsonFactory;
import server.utils.ServletUtils;
import serverengine.users.UserManager;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetLoggedUserInfoServlet", urlPatterns = {"/users/loggedUserInfo"})
public class GetLoggedUserInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        UserDTO loggedUserInfo = userManager.getUser(SessionUtils.getUsername(req)).createDTO();
        String loggedUserInfoJson = GsonFactory.getGson().toJson(loggedUserInfo);
        resp.getWriter().write(loggedUserInfoJson);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
