package InfuraOffice.WebAgent.context.Management;

import InfuraOffice.DataCenter.DataCenter;
import InfuraOffice.DataEntity.ServerGroupEntity;
import InfuraOffice.WebAgent.ExtendedHttpHandler;

import java.util.HashMap;
import java.util.HashSet;

public class ServerGroupManageContext {
    public static class ListServerGroupHandler extends ExtendedHttpHandler {
        @Override
        protected HashSet<String> rolesRequired() {
            return adminRoleSetForRolesRequired();
        }

        @Override
        protected void realHandler() throws Exception {
            HashMap<String, ServerGroupEntity> entityHashMap = DataCenter.getSharedInstance().getServerGroupDataCenter().getEntities();
            registerDataProperty("serverGroups", entityHashMap);
            sayOK();
        }
    }

    public static class ViewServerGroupInfoHandler extends ExtendedHttpHandler {
        @Override
        protected HashSet<String> rolesRequired() {
            return adminRoleSetForRolesRequired();
        }

        @Override
        protected void realHandler() throws Exception {
            String serverGroupName = seekPost("serverGroupName");
            if (serverGroupName == null || serverGroupName.isEmpty()) throw new Exception("server group name empty");
            ServerGroupEntity serverGroupEntity = DataCenter.getSharedInstance().getServerGroupDataCenter().getEntityWithKey(serverGroupName);
            registerDataProperty("serverGroup", serverGroupEntity);
            sayOK();
        }
    }

    public static class UpdateServerGroupHandler extends ExtendedHttpHandler {
        @Override
        protected HashSet<String> rolesRequired() {
            return adminRoleSetForRolesRequired();
        }

        @Override
        protected void realHandler() throws Exception {
            String serverGroupName = seekPost("serverGroupName");
            if (serverGroupName == null || serverGroupName.isEmpty()) throw new Exception("server group name empty");
            ServerGroupEntity serverGroupEntity = DataCenter.getSharedInstance().getServerGroupDataCenter().getEntityWithKey(serverGroupName);
            String act = seekPost("act", "update");

            switch (act) {
                case "delete":
                    if (serverGroupEntity == null) throw new Exception("server group does not exist");
                    DataCenter.getSharedInstance().getServerGroupDataCenter().removeEntityWithKey(serverGroupName);
                    break;
                case "update":
                    if (serverGroupEntity == null) {
                        serverGroupEntity = new ServerGroupEntity();
                        serverGroupEntity.groupName = serverGroupName;
                    }
                    serverGroupEntity.servers = new HashSet<>();
                    String serverNames = seekPost("serverNames", "");
                    String[] split = serverNames.split(",");
                    for (String serverName : split) {
                        if (serverName.trim().length() > 0) serverGroupEntity.servers.add(serverName.trim());
                    }

                    DataCenter.getSharedInstance().getServerGroupDataCenter().updateEntityWithKey(serverGroupName, serverGroupEntity);
                    break;
                default:
                    throw new Exception("Unknown act");
            }

            sayOK();
        }
    }
}
