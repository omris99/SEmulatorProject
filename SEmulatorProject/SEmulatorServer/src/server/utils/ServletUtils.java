package server.utils;

import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import logic.engine.EmulatorEngine;
import logic.json.GsonFactory;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class ServletUtils {
    private static final String EMULATOR_ENGINE_ATTRIBUTE_NAME = "emulatorEngine";

    public static EmulatorEngine getEmulatorEngine(ServletContext servletContext) {
        synchronized (servletContext) {
            if (servletContext.getAttribute(EMULATOR_ENGINE_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(EMULATOR_ENGINE_ATTRIBUTE_NAME, new EmulatorEngine());
            }
        }
        return (EmulatorEngine) servletContext.getAttribute(EMULATOR_ENGINE_ATTRIBUTE_NAME);
    }

    public static Map<String,Object> getPayloadFromRequest(HttpServletRequest req) throws IOException {
        String json = req.getReader().lines().collect(Collectors.joining());
        return GsonFactory.getGson().fromJson(json, new TypeToken<Map<String, Object>>(){}.getType());
    }
}
