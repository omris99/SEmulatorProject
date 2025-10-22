package server.servlets.execution;

import com.google.gson.reflect.TypeToken;
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
import java.util.stream.Collectors;

@WebServlet(name = "runProgramServlet", urlPatterns = {"/execution/runProgram"})
public class RunProgramServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = req.getReader().lines().collect(Collectors.joining());
        Map<String, Object> payload = GsonFactory.getGson().fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());

        int runDegree = ((Double) payload.get("runDegree")).intValue();
        Map<String, String> inputVariables = (Map<String, String>) payload.get("inputVariables");
        String architecture = (String) payload.get("architecture");

        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        String username = SessionUtils.getUsername(req);
        EmulatorEngine engine = ServletUtils.getUserEmulatorEngine(getServletContext(), username);

        Thread runProgramThread = new Thread(() -> {
            try {
                engine.runLoadedProgramWithDebuggerWindowInput(runDegree, inputVariables, ArchitectureType.fromUserString(architecture));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        runProgramThread.setDaemon(true);
        runProgramThread.start();
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
