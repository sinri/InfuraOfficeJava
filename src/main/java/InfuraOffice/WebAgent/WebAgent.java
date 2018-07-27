package InfuraOffice.WebAgent;

import InfuraOffice.ThyLogger;
import InfuraOffice.WebAgent.context.HomeContext;
import InfuraOffice.WebAgent.context.LoginContext;
import InfuraOffice.WebAgent.context.StaticContext;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebAgent {

    public static void listen(int port) throws IOException {
        InetSocketAddress socketAddress = new InetSocketAddress(port);
        HttpServer server = HttpServer.create(socketAddress, 0);

        loadContext(server);

        ThyLogger.logInfo("Context Loaded, ready to listen on port " + port);

        server.start();
    }

    /**
     * Note:
     * The api might need a header 'Content-Type: application/x-www-form-urlencoded'
     *
     * @param server HttpServer
     */
    private static void loadContext(HttpServer server) {
        // PAGE

        // the web root of the web site
        server.createContext("/", new HomeContext.RootPageHandler());
        // the entrance of resources
        server.createContext("/static/", new StaticContext.ResourceHandler());
        // the entrance of the html pages
        server.createContext("/page/", new StaticContext.ResourceHandler());

        // API

        // login with username and password @2018-07-27 00:46:12
        server.createContext("/api/session/login", new LoginContext.LoginHandler());
        // check if the token is valid @2018-07-27 00:46:12
        server.createContext("/api/session/validate", new LoginContext.SessionValidateHandler());
    }
}
