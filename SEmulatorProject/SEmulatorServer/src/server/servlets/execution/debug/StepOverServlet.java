package server.servlets.execution.debug;

import clientserverdto.ErrorDTO;
import clientserverdto.RunResultsDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.SessionUtils;
import serverengine.logic.engine.EmulatorEngine;
import serverengine.logic.exceptions.CreditBalanceTooLowException;
import serverengine.logic.utils.ErrorMapper;
import types.errortypes.ExecutionErrorType;
import json.GsonFactory;
import server.utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "stepOverServlet", urlPatterns = {"/execution/debug/stepOver"})
public class StepOverServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RunResultsDTO stepResult;
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        EmulatorEngine engine = ServletUtils.getUserEmulatorEngine(getServletContext(), SessionUtils.getUsername(req));
        try {
            stepResult = (RunResultsDTO) engine.stepOver();
        } catch (Exception e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorDTO error = ErrorMapper.fromException(e);
            String errorJson = GsonFactory.getGson().toJson(error);
            resp.getWriter().write(errorJson);
            return;
        }

        String stepResultJson = GsonFactory.getGson().toJson(stepResult);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(stepResultJson);
    }
}
