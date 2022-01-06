package pets.service.app.server;

import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import pets.service.app.filter.ServletFilter;
import pets.service.app.servlet.AccountServletCRUD;
import pets.service.app.servlet.AccountTypeServletR;
import pets.service.app.servlet.AppPing;
import pets.service.app.servlet.AppReset;
import pets.service.app.servlet.BankServletR;
import pets.service.app.servlet.CategoryServletR;
import pets.service.app.servlet.CategoryTypeServletR;
import pets.service.app.servlet.MerchantServletCRUD;
import pets.service.app.servlet.ReportsServletR;
import pets.service.app.servlet.TransactionServletCRUD;
import pets.service.app.servlet.TransactionTypeServletR;
import pets.service.app.servlet.UserServletCRU;

import java.util.EnumSet;

import static pets.service.app.util.Util.CONTEXT_PATH;
import static pets.service.app.util.Util.SERVER_IDLE_TIMEOUT;
import static pets.service.app.util.Util.SERVER_MAX_THREADS;
import static pets.service.app.util.Util.SERVER_MIN_THREADS;
import static pets.service.app.util.Util.SERVER_PORT;
import static pets.service.app.util.Util.getSystemEnvProperty;

public class ServerJetty {

    public void start() throws Exception {
        QueuedThreadPool threadPool = new QueuedThreadPool(SERVER_MAX_THREADS, SERVER_MIN_THREADS, SERVER_IDLE_TIMEOUT);
        Server server = new Server(threadPool);

        try (ServerConnector connector = new ServerConnector(server)) {
            String port = getSystemEnvProperty(SERVER_PORT);
            connector.setPort(port == null ? 8080 : Integer.parseInt(port));
            server.setConnectors(new Connector[]{connector});
        }

        server.setHandler(getServletHandler());
        server.start();
    }

    private ServletHandler getServletHandler() {
        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addFilterWithMapping(ServletFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

        servletHandler.addServletWithMapping(AppPing.class, CONTEXT_PATH + "/tests/ping");
        servletHandler.addServletWithMapping(AppReset.class, CONTEXT_PATH + "/actuator/clearCaches");

        // ref types
        servletHandler.addServletWithMapping(AccountTypeServletR.class, CONTEXT_PATH + "/accounttypes/*");
        servletHandler.addServletWithMapping(BankServletR.class, CONTEXT_PATH + "/banks/*");
        servletHandler.addServletWithMapping(CategoryServletR.class, CONTEXT_PATH + "/categories/*");
        servletHandler.addServletWithMapping(CategoryTypeServletR.class, CONTEXT_PATH + "/categorytypes/*");
        servletHandler.addServletWithMapping(TransactionTypeServletR.class, CONTEXT_PATH + "/transactiontypes/*");

        // users
        servletHandler.addServletWithMapping(UserServletCRU.class, CONTEXT_PATH + "/users/user/*");

        // accounts
        servletHandler.addServletWithMapping(AccountServletCRUD.class, CONTEXT_PATH + "/accounts/*");

        // merchants
        servletHandler.addServletWithMapping(MerchantServletCRUD.class, CONTEXT_PATH + "/merchants/*");

        // transactions
        servletHandler.addServletWithMapping(TransactionServletCRUD.class, CONTEXT_PATH + "/transactions/*");

        // reports
        servletHandler.addServletWithMapping(ReportsServletR.class, CONTEXT_PATH + "/reports/*");

        return servletHandler;
    }
}
