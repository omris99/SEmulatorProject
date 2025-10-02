package server.servlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.ErrorAlertDTO;
import dto.RunResultsDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.engine.EmulatorEngine;
import logic.exceptions.NumberNotInRangeException;
import logic.json.GsonFactory;
import server.utils.ServletUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "runProgramServlet", urlPatterns = {"/runProgram"})
public class RunProgramServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            String json = req.getReader().lines().collect(Collectors.joining());
            Map<String, Object> payload = GsonFactory.getGson().fromJson(json, new TypeToken<Map<String, Object>>(){}.getType());

            int runDegree = ((Double) payload.get("runDegree")).intValue();
            Map<String, String> inputVariables = (Map<String, String>) payload.get("inputVariables");

            resp.setContentType("text/plain");
            resp.setCharacterEncoding("UTF-8");
            EmulatorEngine engine = ServletUtils.getEmulatorEngine(getServletContext());

            RunResultsDTO runResultsDTO = (RunResultsDTO) engine.runLoadedProgramWithDebuggerWindowInput(runDegree, inputVariables);
            String runResultsDtoJson = GsonFactory.getGson().toJson(runResultsDTO);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(runResultsDtoJson);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorAlertDTO error = new ErrorAlertDTO("Error Starting Execution", "Invalid Input", "The input is invalid. Please enter integers only.");
            String errorJson = GsonFactory.getGson().toJson(error);
            resp.getWriter().write(errorJson);
        } catch (NumberNotInRangeException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorAlertDTO error = new ErrorAlertDTO(
                    "Error Starting Execution",
                    "Negative Number Submitted",
                    "You entered the number: " + e.getNumber() + " which is not positive.\n" +
                    "Please enter only Positive Numbers.");
            String errorJson = GsonFactory.getGson().toJson(error);
            resp.getWriter().write(errorJson);
        }
    }
}
