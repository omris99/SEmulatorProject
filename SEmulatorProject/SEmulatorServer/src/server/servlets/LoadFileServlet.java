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
import jakarta.xml.bind.JAXBException;
import logic.engine.EmulatorEngine;
import logic.exceptions.InvalidArgumentException;
import logic.exceptions.InvalidXmlFileException;
import logic.json.GsonFactory;
import logic.model.generated.SProgram;
import server.utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "LoadFileServlet", urlPatterns = {"/loadFile"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 10 * 1024 * 1024,  // 10 MB
        maxRequestSize = 20 * 1024 * 1024 // 20 MB
)
public class LoadFileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        try {
            EmulatorEngine engine = ServletUtils.getEmulatorEngine(getServletContext());

            Part fileContent = req.getPart("fileContent");
            engine.loadProgram(fileContent.getInputStream());

            ProgramDTO program = (ProgramDTO) engine.getLoadedProgramDTO();

            String programDtoJson = GsonFactory.getGson().toJson(program);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(programDtoJson);

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String content;
            switch (e) {
                case InvalidXmlFileException iv -> {
                    switch (iv.getType()) {
                        case FILE_MISSING -> content = "File not found: " + iv.getFilePath();
                        case INVALID_EXTENSION -> content = "Invalid file type: " + iv.getFilePath() + " must be .xml";
                        case UNKNOWN_LABEL ->
                                content = String.format("No instruction labeled as %s exists in the program.", iv.getElement());
                        default -> content = "Unknown InvalidXmlFileException";
                    }
                }
                case JAXBException jaxb -> content = "Can't read XML File";
                case InvalidArgumentException ia -> content = String.format("%s.  \nError %s: %s ",
                        ia.getErrorType().getUserMessage(),
                        ia.getErrorType().getArgumentType(),
                        ia.getArgumentName());
                case IllegalArgumentException iae -> content = "Invalid XML File: " + iae.getMessage();
                default -> content = "Unexpected error: " + e.getMessage();
            }
                resp.getWriter().write(content);
        }
    }
}
