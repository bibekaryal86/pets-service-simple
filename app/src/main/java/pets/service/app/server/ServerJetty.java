package pets.service.app.server;

import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import pets.service.app.filter.ServletFilter;
import pets.service.app.servlet.*;
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
        servletHandler.addServletWithMapping(AppReset.class, Util.CONTEXT_PATH + "/actuator/clearCaches");

        // ref types
        servletHandler.addServletWithMapping(AccountTypeServletR.class, Util.CONTEXT_PATH + "/accounttypes/*");
        servletHandler.addServletWithMapping(BankServletR.class, Util.CONTEXT_PATH + "/banks/*");
        servletHandler.addServletWithMapping(CategoryServletR.class, Util.CONTEXT_PATH + "/categories/*");
        servletHandler.addServletWithMapping(CategoryTypeServletR.class, Util.CONTEXT_PATH + "/categorytypes/*");
        servletHandler.addServletWithMapping(TransactionTypeServletR.class, Util.CONTEXT_PATH + "/transactiontypes/*");

        return servletHandler;
    }
}
