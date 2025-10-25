package server.servlets.execution;

import clientserverdto.ExecutionStatusDTO;
import clientserverdto.RunResultsDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import json.GsonFactory;
import server.utils.ServletUtils;
import server.utils.SessionUtils;
import serverengine.logic.engine.EmulatorEngine;

import java.io.IOException;

@WebServlet(name = "getExecutionLatestData", urlPatterns = {"/execution/status"})
public class GetExecutionStatus extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        EmulatorEngine engine = ServletUtils.getUserEmulatorEngine(getServletContext(), SessionUtils.getUsername(req));
        ExecutionStatusDTO executionLatestData = engine.getExecutionStatus();
        String executionDtoJson = GsonFactory.getGson().toJson(executionLatestData);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(executionDtoJson);
    }
}
