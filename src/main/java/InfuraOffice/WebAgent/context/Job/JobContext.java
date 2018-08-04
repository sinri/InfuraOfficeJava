package InfuraOffice.WebAgent.context.Job;

import InfuraOffice.DataCenter.DataCenter;
import InfuraOffice.DataEntity.ServerMaintainJobEntity;
import InfuraOffice.WebAgent.ExtendedHttpHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class JobContext {
    public static class ServerMainJobListHandler extends ExtendedHttpHandler {

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
        }
    }
}
