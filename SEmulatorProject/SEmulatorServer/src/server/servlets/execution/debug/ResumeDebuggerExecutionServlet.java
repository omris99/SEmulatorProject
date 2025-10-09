package server.servlets.execution.debug;

import clientserverdto.ErrorAlertDTO;
import clientserverdto.RunResultsDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.SessionUtils;
import serverengine.logic.engine.EmulatorEngine;
import exceptions.CreditBalanceTooLowException;
import exceptions.ExecutionErrorType;
import json.GsonFactory;
import server.utils.ServletUtils;
import serverengine.logic.model.functionsrepo.ProgramsRepo;

import java.io.IOException;

@WebServlet(name = "resumeDebuggerExecutionServlet", urlPatterns = {"/execution/debug/resume"})
public class ResumeDebuggerExecutionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        EmulatorEngine engine = ServletUtils.getUserEmulatorEngine(getServletContext(), SessionUtils.getUsername(req));
        RunResultsDTO context;
        try {
            context = (RunResultsDTO) engine.resume();
        } catch (CreditBalanceTooLowException e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorAlertDTO error = new ErrorAlertDTO(ExecutionErrorType.CREDIT_BALANCE_TOO_LOW, "Credit Balance Too Low",
                    "Can't Resume Debug",
                    "Credit Balance Too Low. Cost of current instruction: " + e.getCreditsCost() + ",Your Balance: " + e.getCreditsBalance());
            String errorJson = GsonFactory.getGson().toJson(error);
            resp.getWriter().write(errorJson);
            return;
        }
        String contextJson = GsonFactory.getGson().toJson(context);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(contextJson);
    }
}
