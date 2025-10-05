package server.servlets.execution.debug;

import clientserverdto.RunResultsDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.SessionUtils;
import serverengine.logic.engine.EmulatorEngine;
import serverengine.logic.json.GsonFactory;
import server.utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "resumeDebuggerExecutionServlet", urlPatterns = {"/execution/debug/resume"})
public class ResumeDebuggerExecutionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        EmulatorEngine engine = ServletUtils.getUserEmulatorEngine(getServletContext(), SessionUtils.getUsername(req));;
        RunResultsDTO context = (RunResultsDTO) engine.resumeDebuggingSession();
        String contextJson = GsonFactory.getGson().toJson(context);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(contextJson);
    }
}
