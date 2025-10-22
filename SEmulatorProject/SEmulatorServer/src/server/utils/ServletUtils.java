package server.utils;

import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import serverengine.chat.ChatManager;
import serverengine.logic.engine.EmulatorEngine;
import json.GsonFactory;
import serverengine.users.UserManager;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import static server.servlets.chat.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {
    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";

    private static final String USER_ENGINE_ATTRIBUTE_NAME = "userEmulatorEngine";

    private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";

    public static EmulatorEngine getUserEmulatorEngine(ServletContext context, String username) {
        UserManager userManager = getUserManager(context);
        return userManager.getUserEmulatorEngine(username);
    }

    public static UserManager getUserManager(ServletContext servletContext) {
        synchronized (servletContext) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static Map<String,Object> getPayloadFromRequest(HttpServletRequest req) throws IOException {
        String json = req.getReader().lines().collect(Collectors.joining());
        return GsonFactory.getGson().fromJson(json, new TypeToken<Map<String, Object>>(){}.getType());
    }

    public static ChatManager getChatManager(ServletContext servletContext) {
        synchronized (servletContext) {
            if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
            }
        }
        return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
    }

    public static int getIntParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException numberFormatException) {
            }
        }
        return INT_PARAMETER_ERROR;
    }

}
