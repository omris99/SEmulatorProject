package server.servlets.execution;

import clientserverdto.ProgramDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.ServletUtils;
import server.utils.SessionUtils;
import serverengine.logic.engine.EmulatorEngine;
import json.GsonFactory;
import serverengine.programs.repo.ProgramsRepo;

import java.io.IOException;

@WebServlet(name = "setProgramToExecuteServlet", urlPatterns = {"/execution/setProgramToExecute"})
public class SetProgramToExecuteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String programUserString = req.getParameter("programName");
        String programName = ProgramsRepo.getInstance().getFunctionNameByUserString(programUserString);
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        EmulatorEngine engine = ServletUtils.getUserEmulatorEngine(getServletContext(), SessionUtils.getUsername(req));
        engine.setMainProgram(ProgramsRepo.getInstance().getProgramOrFunctionByName(programName));
        ProgramDTO programDTO = (ProgramDTO) engine.getLoadedProgramDTO();
        String programDtoJson = GsonFactory.getGson().toJson(programDTO);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(programDtoJson);
    }
}
