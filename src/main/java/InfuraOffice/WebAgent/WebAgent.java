package InfuraOffice.WebAgent;

import InfuraOffice.ThyLogger;
import InfuraOffice.WebAgent.context.HomeContext;
import InfuraOffice.WebAgent.context.LoginContext;
import InfuraOffice.WebAgent.context.Management.*;
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

        // PART I: SESSION

        // login with username and password @2018-07-27 00:46:12
        server.createContext("/api/session/login", new LoginContext.LoginHandler());
        // check if the token is valid @2018-07-27 00:46:12
        server.createContext("/api/session/validate", new LoginContext.SessionValidateHandler());

        // PART II: MANAGE

        // manage users, update or delete @2018-07-27 23:23:56
        server.createContext("/api/manage/user/act", new UserManageContext.UpdateUserHandler());
        // view info of one user @2018-07-27 23:30:29
        server.createContext("/api/manage/user/info", new UserManageContext.ViewUserInfoHandler());
        // user list @2018-07-27 23:32:04
        server.createContext("/api/manage/user/list", new UserManageContext.ListUserHandler());

        // platform list @2018-07-28 13:48:03
        server.createContext("/api/manage/platform/list", new PlatformManageContext.ListPlatformHandler());
        // fetch one platform info @2018-07-28 13:48:03
        server.createContext("/api/manage/platform/info", new PlatformManageContext.ViewPlatformInfoHandler());
        // manage platform, update or delete @2018-07-28 13:48:03
        server.createContext("/api/manage/platform/act", new PlatformManageContext.UpdatePlatformHandler());

        // database list
        server.createContext("/api/manage/database/list", new DatabaseManageContext.ListDatabaseHandler());
        // fetch one database info
        server.createContext("/api/manage/database/info", new DatabaseManageContext.ViewDatabaseInfoHandler());
        // manage database, update delete update_account remove_account
        server.createContext("/api/manage/database/act", new DatabaseManageContext.UpdateDatabaseHandler());

        // server list
        server.createContext("/api/manage/server/list", new ServerManageContext.ListServerHandler());
        // fetch one server info
        server.createContext("/api/manage/server/info", new ServerManageContext.ViewServerInfoHandler());
        // manage server, update delete
        server.createContext("/api/manage/server/act", new ServerManageContext.UpdateServerHandler());

        // server group list
        server.createContext("/api/manage/servergroup/list", new ServerGroupManageContext.ListServerGroupHandler());
        // fetch one server group info
        server.createContext("/api/manage/servergroup/info", new ServerGroupManageContext.ViewServerGroupInfoHandler());
        // manage server group, update delete
        server.createContext("/api/manage/servergroup/act", new ServerGroupManageContext.UpdateServerGroupHandler());
    }
}
