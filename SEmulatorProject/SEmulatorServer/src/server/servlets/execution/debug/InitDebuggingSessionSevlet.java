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
import exceptions.*;
import json.GsonFactory;
import server.utils.ServletUtils;
import types.ArchitectureType;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "initDebuggingSessionSevlet", urlPatterns = {"/execution/debug/initDebuggingSession"})
public class InitDebuggingSessionSevlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Map<String, Object> payload = ServletUtils.getPayloadFromRequest(req);
            int runDegree = ((Double) payload.get("runDegree")).intValue();
            Map<String, String> inputVariables = (Map<String, String>) payload.get("inputVariables");
            String architecture = (String) payload.get("architecture");

            resp.setContentType("text/plain");
            resp.setCharacterEncoding("UTF-8");
            EmulatorEngine engine = ServletUtils.getUserEmulatorEngine(getServletContext(), SessionUtils.getUsername(req));
            ;

            RunResultsDTO initialState = (RunResultsDTO) engine.initDebuggingSession(runDegree, inputVariables, ArchitectureType.fromUserString(architecture));
            String initialStateJson = GsonFactory.getGson().toJson(initialState);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(initialStateJson);

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorAlertDTO error = new ErrorAlertDTO(ExecutionErrorType.BAD_INPUT_VARIABLES, "Error Starting Execution", "Invalid Input", "The input is invalid. Please enter integers only.");
            String errorJson = GsonFactory.getGson().toJson(error);
            resp.getWriter().write(errorJson);
        } catch (NumberNotInRangeException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorAlertDTO error = new ErrorAlertDTO(
                    ExecutionErrorType.BAD_INPUT_VARIABLES,
                    "Error Starting Execution",
                    "Negative Number Submitted",
                    "You entered the number: " + e.getNumber() + " which is not positive.\n" +
                            "Please enter only Positive Numbers.");
            String errorJson = GsonFactory.getGson().toJson(error);
            resp.getWriter().write(errorJson);
        } catch (CreditBalanceTooLowForInitialChargeException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            String message = String.format(
                    "Your credit balance is too low to start a debugging session.%n%n" +
                            "• Architecture cost: %d%n" +
                            "• Average program cost: %d%n" +
                            "• Minimum Balance Required: %d%n" +
                            "• Your balance: %d",
                    e.getArchitectureCost(),
                    e.getAverageProgramCost(),
                    e.getCreditsCost(),
                    e.getCreditsBalance()
            );

            ErrorAlertDTO error = new ErrorAlertDTO(
                    ExecutionErrorType.CREDIT_BALANCE_TOO_LOW,
                    "Credit Balance Too Low",
                    "Cannot Start Debugging Session",
                    message
            );

            String errorJson = GsonFactory.getGson().toJson(error);
            resp.getWriter().write(errorJson);
        }
    }
}
