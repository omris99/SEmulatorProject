package server.servlets.execution;

import com.google.gson.reflect.TypeToken;
import clientserverdto.ErrorDTO;
import clientserverdto.RunResultsDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.SessionUtils;
import serverengine.logic.engine.EmulatorEngine;
import json.GsonFactory;
import server.utils.ServletUtils;
import serverengine.logic.exceptions.CreditBalanceTooLowException;
import serverengine.logic.exceptions.CreditBalanceTooLowForInitialChargeException;
import serverengine.logic.exceptions.InvalidArchitectureException;
import serverengine.logic.exceptions.NumberNotInRangeException;
import types.modeltypes.ArchitectureType;
import types.errortypes.ExecutionErrorType;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "runProgramServlet", urlPatterns = {"/execution/runProgram"})
public class RunProgramServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String json = req.getReader().lines().collect(Collectors.joining());
            Map<String, Object> payload = GsonFactory.getGson().fromJson(json, new TypeToken<Map<String, Object>>() {
            }.getType());

            int runDegree = ((Double) payload.get("runDegree")).intValue();
            Map<String, String> inputVariables = (Map<String, String>) payload.get("inputVariables");
            String architecture = (String) payload.get("architecture");

            resp.setContentType("text/plain");
            resp.setCharacterEncoding("UTF-8");
            String username = SessionUtils.getUsername(req);
            EmulatorEngine engine = ServletUtils.getUserEmulatorEngine(getServletContext(), username);

            Thread runProgramThread = new Thread(() -> {
                try {
                    engine.runLoadedProgramWithDebuggerWindowInput(runDegree, inputVariables, ArchitectureType.fromUserString(architecture));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            runProgramThread.setDaemon(true);
            runProgramThread.start();

//            engine.runLoadedProgramWithDebuggerWindowInput(runDegree, inputVariables, ArchitectureType.fromUserString(architecture));
//            String runResultsDtoJson = GsonFactory.getGson().toJson(runResultsDTO);
            resp.setStatus(HttpServletResponse.SC_OK);
//            resp.getWriter().write(runResultsDtoJson);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorDTO error = new ErrorDTO(ExecutionErrorType.BAD_INPUT_VARIABLES, "Error Starting Execution", "Invalid Input", "The input is invalid. Please enter integers only.");
            String errorJson = GsonFactory.getGson().toJson(error);
            resp.getWriter().write(errorJson);
        } catch (NumberNotInRangeException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorDTO error = new ErrorDTO(
                    ExecutionErrorType.BAD_INPUT_VARIABLES, "Error Starting Execution",
                    "Negative Number Submitted",
                    "You entered the number: " + e.getNumber() + " which is not positive.\n" +
                            "Please enter only Positive Numbers.");
            String errorJson = GsonFactory.getGson().toJson(error);
            resp.getWriter().write(errorJson);
        } catch (InvalidArchitectureException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorDTO error = new ErrorDTO(
                    ExecutionErrorType.UNCOMPATIBLE_ARCHITECTURE, "Error Starting Execution",
                    "Invalid Architecture Selected",
                    "Minimum architecture required for this program is: " + e.getMinimumArchitecture() + ".\n" +
                            "You selected: " + e.getSelectedArchitecture() + ".\n" +
                            "Please select a valid architecture and try again.");
            String errorJson = GsonFactory.getGson().toJson(error);
            resp.getWriter().write(errorJson);
//        } catch (CreditBalanceTooLowException e) {
//            if (e instanceof CreditBalanceTooLowForInitialChargeException) {
//                CreditBalanceTooLowForInitialChargeException ex = (CreditBalanceTooLowForInitialChargeException) e;
//                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//
//                String message = String.format(
//                        "Your credit balance is too low to run this program.%n%n" +
//                                "• Architecture cost: %d%n" +
//                                "• Average program cost: %d%n" +
//                                "• Minimum Balance Required: %d%n" +
//                                "• Your balance: %d",
//                        ex.getArchitectureCost(),
//                        ex.getAverageProgramCost(),
//                        ex.getCreditsCost(),
//                        ex.getCreditsBalance()
//                );
//                ErrorDTO error = new ErrorDTO(
//                        ExecutionErrorType.CREDIT_BALANCE_TOO_LOW, "Credit Balance Too Low",
//                        "Can't Start Program run",
//                        message);
//                String errorJson = GsonFactory.getGson().toJson(error);
//                resp.getWriter().write(errorJson);
//            } else {
//                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                ErrorDTO error = new ErrorDTO(
//                        ExecutionErrorType.CREDIT_BALANCE_TOO_LOW, "Credit Balance Too Low",
//                        "Can't Run Program",
//                        "Credit Balance Too Low. Cost of current instruction: " + e.getCreditsCost() + ",Your Balance: " + e.getCreditsBalance());
//                String errorJson = GsonFactory.getGson().toJson(error);
//                resp.getWriter().write(errorJson);
//            }
        }
    }
}
