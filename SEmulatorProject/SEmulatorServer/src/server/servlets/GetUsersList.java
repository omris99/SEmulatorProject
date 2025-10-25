package server.servlets;

import clientserverdto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import json.GsonFactory;
import server.utils.ServletUtils;
import serverengine.users.UserManager;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetUsersList", urlPatterns = {"/users/list"})
public class GetUsersList extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        List<UserDTO> usersList = userManager.getUserDTOs();
        String usersListJson = GsonFactory.getGson().toJson(usersList);
        resp.getWriter().write(usersListJson);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
