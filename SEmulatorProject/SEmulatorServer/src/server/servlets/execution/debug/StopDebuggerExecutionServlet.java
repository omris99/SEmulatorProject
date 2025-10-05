package server.servlets.execution.debug;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import serverengine.logic.engine.EmulatorEngine;
import server.utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "stopDebuggerExecutionServlet", urlPatterns = {"/execution/debug/stop"})
public class StopDebuggerExecutionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        EmulatorEngine engine = ServletUtils.getUserEmulatorEngine(req);
        engine.stopDebuggingSession();
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
