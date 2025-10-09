package server.servlets;

import clientserverdto.UploadedProgramDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import json.GsonFactory;
import serverengine.logic.model.functionsrepo.ProgramsRepo;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetProgramsListServlet", urlPatterns = {"/programs"})
public class GetProgramsListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        List<UploadedProgramDTO> programsList = ProgramsRepo.getInstance().getAllPrograms();

        String programsListJson = GsonFactory.getGson().toJson(programsList);
        resp.getWriter().write(programsListJson);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
