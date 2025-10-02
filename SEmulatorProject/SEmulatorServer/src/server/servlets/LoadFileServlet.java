package server.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import logic.json.GsonFactory;
import logic.model.generated.SProgram;

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
//        String filePath = req.getParameter("filePath");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            ServletContext context = req.getServletContext();
            EmulatorEngine engine = (EmulatorEngine) context.getAttribute("emulatorEngine");
            System.out.println("[Servlet] Reached LoadFileServlet");

            if (engine == null) {
                System.out.println("[Servlet] EmulatorEngine not found, creating new one...");
                engine = new EmulatorEngine();
                context.setAttribute("emulatorEngine", engine);
                System.out.println("[Servlet] EmulatorEngine added to context");
            }

            Part fileContent = req.getPart("fileContent");

            System.out.println("[Servlet] before getinputstream ");
            System.out.println("Class exists? " + SProgram.class);
            engine.loadProgram(fileContent.getInputStream());
            System.out.println("[Servlet] after getinputstream ");

            ProgramDTO program = (ProgramDTO) engine.getLoadedProgramDTO();
            System.out.println("[Servlet] before json convert ");

            String programDtoJson = GsonFactory.getGson().toJson(program);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(programDtoJson);

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
