package pets.service.app.server;

import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import pets.service.app.filter.ServletFilter;
import pets.service.app.servlet.AppPing;
import pets.service.app.servlet.AppReset;
import pets.service.app.util.Util;

import java.util.EnumSet;

public class ServerJetty {

    public void start() throws Exception {
        QueuedThreadPool threadPool = new QueuedThreadPool(Util.SERVER_MAX_THREADS, Util.SERVER_MIN_THREADS, Util.SERVER_IDLE_TIMEOUT);
        Server server = new Server(threadPool);

        try (ServerConnector connector = new ServerConnector(server)) {
            String port = Util.getSystemEnvProperty(Util.SERVER_PORT);
            connector.setPort(port == null ? 8080 : Integer.parseInt(port));
            server.setConnectors(new Connector[]{connector});
        }

        server.setHandler(getServletHandler());
        server.start();
    }

    private ServletHandler getServletHandler() {
        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addFilterWithMapping(ServletFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

        servletHandler.addServletWithMapping(AppPing.class, Util.CONTEXT_PATH + "/tests/ping");
        servletHandler.addServletWithMapping(AppReset.class, Util.CONTEXT_PATH + "/tests/reset");

        return servletHandler;
    }
}
