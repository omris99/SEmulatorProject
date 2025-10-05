package server.servlets;

import clientserverdto.ProgramDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import serverengine.logic.engine.EmulatorEngine;
import serverengine.logic.json.GsonFactory;
import server.utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "getExpandedProgramServlet", urlPatterns = {"/expandedProgram"})
public class GetExpandedProgramServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int degreeParam = Integer.parseInt(req.getParameter("degree"));
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        EmulatorEngine engine = ServletUtils.getEmulatorEngine(getServletContext());

        ProgramDTO program = (ProgramDTO) engine.showExpandedProgramOnScreen(degreeParam);

        String programDtoJson = GsonFactory.getGson().toJson(program);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(programDtoJson);
    }
}
