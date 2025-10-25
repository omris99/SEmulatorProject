package server.utils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import server.execution.ExecutionManager;

@WebListener
public class ServerListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ExecutionManager.shutdown();
    }
}
