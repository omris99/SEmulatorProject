package server.servlets;

import dto.ProgramDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.engine.EmulatorEngine;
import logic.json.GsonFactory;

import java.io.IOException;

@WebServlet(name = "changeOnScreenProgramServlet", urlPatterns = {"/changeOnScreenProgram"})
public class ChangeOnScreenProgramServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String functionName = req.getParameter("functionName");
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        EmulatorEngine engine = (EmulatorEngine) getServletContext().getAttribute("emulatorEngine");
        if (engine == null) {
            System.out.println("Engine is null");
            engine = new EmulatorEngine();
            getServletContext().setAttribute("emulatorEngine", engine);
        }

        engine.changeLoadedProgramToFunction(functionName);
        ProgramDTO programDTO = (ProgramDTO) engine.getLoadedProgramDTO();
        String programDtoJson = GsonFactory.getGson().toJson(programDTO);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(programDtoJson);
    }
}
