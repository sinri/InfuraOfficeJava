package InfuraOffice.WebAgent.context.Maintain;

import InfuraOffice.DataCenter.DataCenter;
import InfuraOffice.DataEntity.ServerEntity;
import InfuraOffice.DataEntity.ServerGroupEntity;
import InfuraOffice.WebAgent.ExtendedHttpHandler;

import java.util.HashMap;
import java.util.HashSet;

public class ServerMaintainContext {
    public static class RequireCommandTaskOnServersHandler extends ExtendedHttpHandler {
        @Override
        protected void realHandler() throws Exception {
            String serverNames = seekPost("serverNames", "");
            String serverGroupNames = seekPost("serverGroupNames", "");

            String command = seekPost("command", "");

            HashMap<String, ServerEntity> servers = new HashMap<>();
            for (String serverName : serverNames.split(",")) {
                ServerEntity entity = DataCenter.getSharedInstance().getServerDataCenter().getEntityWithKey(serverName.trim());
                if (entity != null) servers.put(entity.serverName, entity);
            }
            for (String serverGroupName : serverGroupNames.split(",")) {
                ServerGroupEntity entity = DataCenter.getSharedInstance().getServerGroupDataCenter().getEntityWithKey(serverGroupName.trim());
                entity.servers.forEach(serverName -> {
                    ServerEntity innerEntity = DataCenter.getSharedInstance().getServerDataCenter().getEntityWithKey(serverName.trim());
                    if (innerEntity != null) servers.put(innerEntity.serverName, innerEntity);
                });
            }

            HashMap<String, String> result = new HashMap<>();
            servers.forEach((key, server) -> {
                String taskIndex = server.raiseRemoteCommandExecutionTask(command);
                result.put(server.serverName, taskIndex);
            });

            registerDataProperty("tasks", result);
            sayOK();
        }

        @Override
        protected HashSet<String> rolesRequired() {
            return adminRoleSetForRolesRequired();
        }


    }
}
