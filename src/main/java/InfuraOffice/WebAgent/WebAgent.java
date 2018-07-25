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


    private static void loadContext(HttpServer server) {
        // TODO add contexts such as
        //server.createContext("/newTask", new ContextHandlerForNewTask());
        //server.createContext("/checkTask", new ContextHandlerForCheckTask());


        server.createContext("/", new HomeContext.RootPageHandler());
        server.createContext("/static/", new StaticContext.ResourceHandler());
        server.createContext("/page/", new StaticContext.ResourceHandler());

        server.createContext("/api/login", new LoginContext.LoginHandler());
    }
}
