package server.servlets.execution.debug;

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

@WebServlet(name = "stepOverServlet", urlPatterns = {"/execution/debug/stepOver"})
public class StepOverServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        EmulatorEngine engine = ServletUtils.getUserEmulatorEngine(req);
        RunResultsDTO stepResult = (RunResultsDTO) engine.stepOver();
        String stepResultJson = GsonFactory.getGson().toJson(stepResult);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(stepResultJson);
    }
}
