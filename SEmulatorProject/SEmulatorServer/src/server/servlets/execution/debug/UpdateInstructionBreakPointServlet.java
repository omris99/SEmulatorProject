package server.servlets.execution.debug;

import clientserverdto.InstructionDTO;
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
import java.util.Map;

@WebServlet(name = "updateInstructionBreakPointServlet", urlPatterns = {"/execution/debug/updateInstructionBreakPoint"})
public class UpdateInstructionBreakPointServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        Map<String, Object> payLoad = ServletUtils.getPayloadFromRequest(req);
        int index = ((Double) payLoad.get("index")).intValue();
        boolean isSet = (boolean) payLoad.get("isSet");

        EmulatorEngine engine = ServletUtils.getUserEmulatorEngine(getServletContext(), SessionUtils.getUsername(req));
        InstructionDTO instruction = engine.updateInstructionBreakpoint(index, isSet);
        String instructionJson = GsonFactory.getGson().toJson(instruction);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(instructionJson);
    }
}
