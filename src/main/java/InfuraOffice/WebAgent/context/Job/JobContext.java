package InfuraOffice.WebAgent.context.Job;

import InfuraOffice.DataCenter.DataCenter;
import InfuraOffice.DataEntity.ServerMaintainJobEntity;
import InfuraOffice.WebAgent.ExtendedHttpHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class JobContext {
    public static class ServerMaintainJobListHandler extends ExtendedHttpHandler {

        @Override
        protected HashSet<String> rolesRequired() {
            return adminRoleSetForRolesRequired();
        }

        @Override
        protected void realHandler() throws Exception {
            HashMap<String, ServerMaintainJobEntity> jobs = DataCenter.getSharedInstance().getServerMaintainJobDataCenter().getEntities();
            ArrayList<ServerMaintainJobEntity> list = new ArrayList<>();
            jobs.forEach((jobName, job) -> list.add(job));
            registerDataProperty("list", list);
            sayOK();
        }
    }

    public static class ServerMaintainJobUpdateHandler extends ExtendedHttpHandler {

        @Override
        protected HashSet<String> rolesRequired() {
            return adminRoleSetForRolesRequired();
        }

        @Override
        protected void realHandler() throws Exception {
            String cronJobName = seekPost("cronJobName");
            String act = seekPost("act");

            ServerMaintainJobEntity serverMaintainJobEntity = DataCenter.getSharedInstance().getServerMaintainJobDataCenter().getEntityWithKey(cronJobName);

            switch (act) {
                case "update": {
                    if (serverMaintainJobEntity == null) {
                        serverMaintainJobEntity = new ServerMaintainJobEntity();
                        serverMaintainJobEntity.cronJobName = cronJobName;
                    }

                    String serverGroupNames = seekPost("serverGroupNames");
                    String serverNames = seekPost("serverNames");
                    String cronExpression = seekPost("cronExpression");
                    String type = seekPost("type", "ServerMaintainJob");
                    String command = seekPost("command");
                    String active = seekPost("active");

                    String[] serverGroupNameArray = serverGroupNames.split("[\r\n]+");
                    serverMaintainJobEntity.serverGroupNames = new HashSet<>();
                    serverMaintainJobEntity.serverGroupNames.addAll(Arrays.asList(serverGroupNameArray));
                    String[] serverNameArray = serverNames.split("[\r\n]+");
                    serverMaintainJobEntity.serverNames = new HashSet<>();
                    serverMaintainJobEntity.serverNames.addAll(Arrays.asList(serverNameArray));
                    serverMaintainJobEntity.cronExpression = cronExpression;
                    serverMaintainJobEntity.type = type;
                    serverMaintainJobEntity.command = command;
                    serverMaintainJobEntity.active = (active.equals("YES"));

                    DataCenter.getSharedInstance().getServerMaintainJobDataCenter().updateEntityWithKey(cronJobName, serverMaintainJobEntity);
                }
                break;
                case "delete": {
                    if (serverMaintainJobEntity == null) throw new Exception("No such cronjob");
                    DataCenter.getSharedInstance().getServerMaintainJobDataCenter().removeEntityWithKey(cronJobName);
                }
                break;
                default:
                    throw new Exception("Unsupported action");
            }
            sayOK();
        }
    }
}
