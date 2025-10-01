package server.servlets;

import dto.ProgramDTO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import logic.engine.EmulatorEngine;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Scanner;

@WebServlet(name = "LoadFileServlet", urlPatterns = {"/loadFile"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 10 * 1024 * 1024,  // 10 MB
        maxRequestSize = 20 * 1024 * 1024 // 20 MB
)
public class LoadFileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filePath = req.getParameter("filePath");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            ServletContext context = getServletContext();
            EmulatorEngine engine = (EmulatorEngine) context.getAttribute("emulatorEngine");
            System.out.println("i a m here");

            if (engine == null) {
//                System.out.println("fileContent part: " + req.getPart("fileContent"));

//                engine = new EmulatorEngine();
                context.setAttribute("emulatorEngine", new EmulatorEngine());
                System.out.println("engine added");
            }

//            Collection<Part> parts = req.getParts();
//            for (Part part : parts) {
//                fileContent.append(part.getInputStream());
//            }
            Part fileContent = req.getPart("fileContent");
            System.out.println("fileContent part: " + req.getPart("fileContent"));

            engine.loadProgram(fileContent.getInputStream());
            ProgramDTO program = (ProgramDTO) engine.getLoadedProgramDTO();

            // ממירים את ה־DTO ל־JSON (באמצעות Jackson)
//            ObjectMapper mapper = new ObjectMapper();
//            String json = mapper.writeValueAsString(program);

            resp.setStatus(HttpServletResponse.SC_OK);
//            resp.getWriter().write(json);

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
