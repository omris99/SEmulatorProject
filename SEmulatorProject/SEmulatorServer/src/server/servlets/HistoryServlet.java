package server.servlets;

import clientserverdto.ExecutionHistoryDTO;
import clientserverdto.RunResultsDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.SessionUtils;
import serverengine.logic.engine.EmulatorEngine;
import json.GsonFactory;
import server.utils.ServletUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "historyServlet", urlPatterns = {"/history"})
public class HistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        System.out.println(username);
        EmulatorEngine engine;
        if(username != null) {
            engine = ServletUtils.getUserEmulatorEngine(getServletContext(), username);
        }
        else {
            engine = ServletUtils.getUserEmulatorEngine(getServletContext(), SessionUtils.getUsername(req));
            System.out.println("No username provided, using session username: " + SessionUtils.getUsername(req));
        }

        List<ExecutionHistoryDTO> history = engine.getHistory();

        String historyDtoJson = GsonFactory.getGson().toJson(history);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(historyDtoJson);
    }
}
