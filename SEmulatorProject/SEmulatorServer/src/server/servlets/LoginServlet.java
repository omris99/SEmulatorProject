package server.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.ServletUtils;
import server.utils.SessionUtils;
import users.UserManager;

import java.io.IOException;

@WebServlet(name="LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(req);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if (usernameFromSession == null) {
            String usernameFromParameter = req.getParameter("username");
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                usernameFromParameter = usernameFromParameter.trim();
                synchronized (this) {
                    if (userManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";
                        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        resp.getWriter().write(errorMessage);
                    } else {
                        userManager.addUser(usernameFromParameter);
                        req.getSession(true).setAttribute("username", usernameFromParameter);
                        resp.setStatus(HttpServletResponse.SC_OK);
                        resp.getWriter().write("Login successful");
                    }
                }
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Already logged in as " + usernameFromSession);
        }
    }
}
