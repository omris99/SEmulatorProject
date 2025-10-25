package server.servlets;

import clientserverdto.ProgramDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.SessionUtils;
import serverengine.logic.engine.EmulatorEngine;
import json.GsonFactory;
import server.utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "changeOnScreenProgramServlet", urlPatterns = {"/changeOnScreenProgram"})
public class ChangeOnScreenProgramServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String functionName = req.getParameter("functionName");
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        EmulatorEngine engine = ServletUtils.getUserEmulatorEngine(getServletContext(), SessionUtils.getUsername(req));

        engine.changeLoadedProgramToFunction(functionName);
        ProgramDTO programDTO = (ProgramDTO) engine.getLoadedProgramDTO();
        String programDtoJson = GsonFactory.getGson().toJson(programDTO);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(programDtoJson);
    }
}
