package server.servlets.execution;

import clientserverdto.ErrorDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.utils.ServletUtils;
import server.utils.SessionUtils;
import serverengine.logic.engine.EmulatorEngine;
import types.errortypes.ExecutionErrorType;
import serverengine.logic.exceptions.NumberNotInRangeException;
import json.GsonFactory;

import java.io.IOException;

@WebServlet(name = "chargeCreditsServlet", urlPatterns = {"/chargeCredits"})
public class ChargeCreditsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = SessionUtils.getUsername(req);
        String creditsStr = req.getParameter("creditsAmount");
        try {
            EmulatorEngine engine = ServletUtils.getUserEmulatorEngine(getServletContext(), username);
            engine.chargeCredits(creditsStr);
        } catch (NumberFormatException | NumberNotInRangeException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorDTO error = new ErrorDTO(ExecutionErrorType.INVALID_CREDITS_AMOUNT, "Invalid credits amount",
                    "Can't charge " + creditsStr + " credits.",
                    "The credits amount must be a positive integer.");
            String errorJson = GsonFactory.getGson().toJson(error);
            resp.getWriter().write(errorJson);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("Successfully charged " + creditsStr + " credits to user: " + username);
    }
}
