package server.servlets.execution.debug;

import clientserverdto.InstructionDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import serverengine.logic.engine.EmulatorEngine;
import serverengine.logic.json.GsonFactory;
import server.utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "getNextInstructionToExecute", urlPatterns = {"/execution/debug/nextInstructionToExecute"})
public class GetNextInstructionToExecuteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        EmulatorEngine engine = ServletUtils.getUserEmulatorEngine(req);
        InstructionDTO nextInstructionToExecute = (InstructionDTO) engine.getNextInstructionToExecute();
        String nextInstructionToExecuteJson = GsonFactory.getGson().toJson(nextInstructionToExecute);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(nextInstructionToExecuteJson);
    }
}
