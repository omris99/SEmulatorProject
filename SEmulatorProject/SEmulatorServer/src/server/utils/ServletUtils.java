package server.utils;

import jakarta.servlet.ServletContext;
import logic.engine.EmulatorEngine;

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
}
