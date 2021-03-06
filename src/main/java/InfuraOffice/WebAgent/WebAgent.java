package InfuraOffice.WebAgent;

import InfuraOffice.ThyLogger;
import InfuraOffice.WebAgent.context.EntityQueryContext;
import InfuraOffice.WebAgent.context.HomeContext;
import InfuraOffice.WebAgent.context.Job.JobContext;
import InfuraOffice.WebAgent.context.LoginContext;
import InfuraOffice.WebAgent.context.Maintain.DatabaseMaintainContext;
import InfuraOffice.WebAgent.context.Maintain.RemoteTaskContext;
import InfuraOffice.WebAgent.context.Maintain.ServerMaintainContext;
import InfuraOffice.WebAgent.context.Management.*;
import InfuraOffice.WebAgent.context.SinriLogKeeper.SLKContext;
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

        // PART III: MAINTAIN

        // check task
        server.createContext("/api/maintain/task/info", new RemoteTaskContext.RequireCommandTaskOnServersHandler());
        // run command on server
        server.createContext("/api/maintain/server/command", new ServerMaintainContext.RequireCommandTaskOnServersHandler());
        // show full processlist on database
        server.createContext("/api/maintain/database/processes", new DatabaseMaintainContext.DatabaseProcessListHandler());
        // kill process on database
        server.createContext("/api/maintain/database/kill", new DatabaseMaintainContext.DatabaseKillHandler());

        // PART IV: FETCH OPTIONS

        server.createContext("/api/query/user", new EntityQueryContext.FetchUserOptionsHandler());
        server.createContext("/api/query/platform", new EntityQueryContext.FetchPlatformOptionsHandler());
        server.createContext("/api/query/server", new EntityQueryContext.FetchServerOptionsHandler());
        server.createContext("/api/query/servergroup", new EntityQueryContext.FetchServerGroupOptionsHandler());
        server.createContext("/api/query/database", new EntityQueryContext.FetchDatabaseOptionsHandler());

        // PART V: SinriLogKeeper

        server.createContext("/api/slk/files", new SLKContext.FetchLogFileListTaskHandler());
        server.createContext("/api/slk/filesize", new SLKContext.SyncFetchLogFileSizeTaskHandler());
        server.createContext("/api/slk/search", new SLKContext.FetchLogContentTaskHandler());

        // PART VI: JOB
        server.createContext("/api/job/servermaintainjob/list", new JobContext.ServerMaintainJobListHandler());
        server.createContext("/api/job/servermaintainjob/act", new JobContext.ServerMaintainJobUpdateHandler());
    }
}
