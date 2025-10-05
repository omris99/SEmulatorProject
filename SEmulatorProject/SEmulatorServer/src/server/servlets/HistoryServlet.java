package server.servlets;

import clientserverdto.RunResultsDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import serverengine.logic.engine.EmulatorEngine;
import serverengine.logic.json.GsonFactory;
import server.utils.ServletUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "historyServlet", urlPatterns = {"/history"})
public class HistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        EmulatorEngine engine = ServletUtils.getUserEmulatorEngine(req);

        List<RunResultsDTO> history = engine.getHistory();

        String historyDtoJson = GsonFactory.getGson().toJson(history);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(historyDtoJson);
    }
}
