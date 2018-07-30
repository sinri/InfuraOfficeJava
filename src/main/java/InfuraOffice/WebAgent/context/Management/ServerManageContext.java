package InfuraOffice.WebAgent.context.Management;

import InfuraOffice.DataCenter.DataCenter;
import InfuraOffice.DataEntity.ServerEntity;
import InfuraOffice.WebAgent.ExtendedHttpHandler;

import java.util.ArrayList;
import java.util.HashSet;

public class ServerManageContext {
    public static class ListServerHandler extends ExtendedHttpHandler {
        @Override
        protected HashSet<String> rolesRequired() {
            return adminRoleSetForRolesRequired();
        }

        @Override
        protected void realHandler() throws Exception {
            ArrayList<ServerEntity> list = new ArrayList<>();
            DataCenter.getSharedInstance().getServerDataCenter().getEntities().forEach((key, entity) -> {
                list.add(entity);
            });
            registerDataProperty("servers", list);
            sayOK();
        }
    }

    public static class ViewServerInfoHandler extends ExtendedHttpHandler {
        @Override
        protected HashSet<String> rolesRequired() {
            return adminRoleSetForRolesRequired();
        }

        @Override
        protected void realHandler() throws Exception {
            String serverName = seekPost("serverName");
            if (serverName == null || serverName.isEmpty()) throw new Exception("server name empty");
            ServerEntity serverEntity = DataCenter.getSharedInstance().getServerDataCenter().getEntityWithKey(serverName);
            registerDataProperty("server", serverEntity);
            sayOK();
        }
    }

    public static class UpdateServerHandler extends ExtendedHttpHandler {
        @Override
        protected HashSet<String> rolesRequired() {
            return adminRoleSetForRolesRequired();
        }

        @Override
        protected void realHandler() throws Exception {
            String serverName = seekPost("serverName");
            if (serverName == null || serverName.isEmpty()) throw new Exception("server name empty");
            ServerEntity serverEntity = DataCenter.getSharedInstance().getServerDataCenter().getEntityWithKey(serverName);
            String act = seekPost("act", "update");

            switch (act) {
                case "delete":
                    if (serverEntity == null) throw new Exception("target server does not exist");
                    DataCenter.getSharedInstance().getServerDataCenter().removeEntityWithKey(serverName);
                    break;
                case "update":
                    if (serverEntity == null) {
                        serverEntity = new ServerEntity();
                        serverEntity.serverName = serverName;
                    }
                    serverEntity.areaCode = seekPost("areaCode", "");
                    serverEntity.assertID = seekPost("assertID", "");
                    serverEntity.platformName = seekPost("platformName", "");
                    serverEntity.sshUser = seekPost("sshUser", "admin");
                    serverEntity.sshPort = Integer.parseInt(seekPost("sshPort", "22"));
                    serverEntity.connectAddress = seekPost("connectAddress", "");

                    serverEntity.slkLogPathSet = new HashSet<>();
                    String paths = seekPost("slk_paths", "");
                    String[] split = paths.split("[\r\n]+");
                    for (String path : split) {
                        if (path.trim().length() > 0) serverEntity.slkLogPathSet.add(path.trim());
                    }

                    DataCenter.getSharedInstance().getServerDataCenter().updateEntityWithKey(serverName, serverEntity);
                    break;
                default:
                    throw new Exception("Unknown act");
            }
            sayOK();
        }
    }
}
