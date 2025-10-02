package server.servlets;

import dto.ProgramDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.engine.EmulatorEngine;
import logic.json.GsonFactory;
import server.utils.ServletUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "getProgramInputsNamesServlet", urlPatterns = {"/programInputsNames"})
public class GetProgramInputsNamesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        EmulatorEngine engine = ServletUtils.getEmulatorEngine(getServletContext());
        List<String> inputNames = ((ProgramDTO) engine.getLoadedProgramDTO()).getInputNames();
        String inputNamesJson = GsonFactory.getGson().toJson(inputNames);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(inputNamesJson);
    }
}
