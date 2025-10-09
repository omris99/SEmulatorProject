package server.servlets;

import clientserverdto.UploadedProgramDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import exceptions.AlreadyExistsProgramException;
import exceptions.InvalidArgumentException;
import exceptions.InvalidXmlFileException;
import exceptions.XmlErrorType;
import server.utils.ServletUtils;
import serverengine.logic.model.argument.label.FixedLabel;
import serverengine.logic.model.argument.label.Label;
import serverengine.logic.model.functionsrepo.ProgramsRepo;
import serverengine.logic.model.functionsrepo.UploadedProgram;
import serverengine.logic.model.generated.SProgram;
import serverengine.logic.model.mappers.ProgramMapper;
import serverengine.logic.model.program.Program;
import serverengine.users.UserManager;

import java.io.IOException;
import java.io.InputStream;

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
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String userName = (String) req.getSession().getAttribute("username");

            Part fileContent = req.getPart("fileContent");
            UploadedProgramDTO program = loadProgram(userName, fileContent.getInputStream());
            userManager.getUser(userName).addMainProgram(program.getProgram());
            resp.setStatus(HttpServletResponse.SC_OK);
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
                case AlreadyExistsProgramException aep -> content = (aep.isFunction() ?  "Function" : "Program") + " Already Exists: " + aep.getProgramName();
                default -> content = "Unexpected error: " + e.getMessage();
            }
            resp.getWriter().write(content);
        }
    }

    private synchronized UploadedProgramDTO loadProgram(String userName, InputStream inputStream) throws JAXBException, InvalidXmlFileException {
        ProgramsRepo programsRepo = ProgramsRepo.getInstance();

        JAXBContext jaxbContext = JAXBContext.newInstance(SProgram.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        SProgram sProgram = (SProgram) jaxbUnmarshaller.unmarshal(inputStream);
        if(programsRepo.getProgramOrFunctionByName(sProgram.getName()) != null){
            throw new AlreadyExistsProgramException(sProgram.getName(), false);
        }

        Program loadedProgram = ProgramMapper.toDomain(userName, sProgram);

        Label problemLabel = loadedProgram.validate();
        if (problemLabel != FixedLabel.EMPTY) {
            throw new InvalidXmlFileException("", XmlErrorType.UNKNOWN_LABEL, problemLabel.getRepresentation());
        }

        UploadedProgram uploadedProgram = new UploadedProgram(userName, loadedProgram, null);
        ProgramsRepo.getInstance().addProgram(uploadedProgram);

        return uploadedProgram.createDTO();
    }

}
