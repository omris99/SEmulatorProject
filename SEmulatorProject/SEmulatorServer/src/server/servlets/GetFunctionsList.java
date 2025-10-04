package server.servlets;

import dto.ProgramDTO;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.json.GsonFactory;
import logic.model.functionsrepo.FunctionsRepo;
import server.utils.ServletUtils;
import users.UserManager;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetFunctionsList", urlPatterns = {"/functions"})
public class GetFunctionsList extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        List<ProgramDTO> programsList = FunctionsRepo.getInstance().getAllFunctions();
        String programsListJson = GsonFactory.getGson().toJson(programsList);
        resp.getWriter().write(programsListJson);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
