package server.servlets.execution.debug;


import clientserverdto.ErrorDTO;
import clientserverdto.ExecutionStatus;
import clientserverdto.ExecutionStatusDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import json.GsonFactory;
import server.utils.ServletUtils;
import server.utils.SessionUtils;
import serverengine.logic.engine.EmulatorEngine;
import types.modeltypes.ArchitectureType;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "initDebuggingSessionSevlet", urlPatterns = {"/execution/debug/initDebuggingSession"})
public class InitDebuggingSessionSevlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            Map<String, Object> payload = ServletUtils.getPayloadFromRequest(req);
            int runDegree = ((Double) payload.get("runDegree")).intValue();
            Map<String, String> inputVariables = (Map<String, String>) payload.get("inputVariables");
            String architecture = (String) payload.get("architecture");

            resp.setContentType("text/plain");
            resp.setCharacterEncoding("UTF-8");
            EmulatorEngine engine = ServletUtils.getUserEmulatorEngine(getServletContext(), SessionUtils.getUsername(req));

            engine.initDebuggingSession(runDegree, inputVariables, ArchitectureType.fromUserString(architecture));
            ExecutionStatusDTO initialState = engine.getExecutionStatus();
            if(initialState.getStatus() == ExecutionStatus.ERROR){
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ErrorDTO error = initialState.getError();
                String errorJson = GsonFactory.getGson().toJson(error);
                resp.getWriter().write(errorJson);
                return;
            }
            String initialStateJson = GsonFactory.getGson().toJson(initialState.getLastRunResult());
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(initialStateJson);
    }
}
