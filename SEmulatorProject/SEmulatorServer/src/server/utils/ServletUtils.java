package server.utils;

import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import serverengine.logic.engine.EmulatorEngine;
import json.GsonFactory;
import serverengine.users.UserManager;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class ServletUtils {
    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";

    private static final String USER_ENGINE_ATTRIBUTE_NAME = "userEmulatorEngine";

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
}
