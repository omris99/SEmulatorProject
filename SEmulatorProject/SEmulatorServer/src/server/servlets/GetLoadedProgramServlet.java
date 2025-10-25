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

@WebServlet(name = "getLoadedProgramServlet", urlPatterns = {"/loadedProgram"})
public class GetLoadedProgramServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        EmulatorEngine engine = ServletUtils.getUserEmulatorEngine(getServletContext(), SessionUtils.getUsername(req));

        ProgramDTO program = (ProgramDTO) engine.getLoadedProgramDTO();

        String programDtoJson = GsonFactory.getGson().toJson(program);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(programDtoJson);
    }
}
