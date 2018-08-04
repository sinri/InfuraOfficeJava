package InfuraOffice.WebAgent.context.Maintain;

import InfuraOffice.DataCenter.DataCenter;
import InfuraOffice.WebAgent.ExtendedHttpHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DatabaseMaintainContext {

    public static class DatabaseProcessListHandler extends ExtendedHttpHandler {
        @Override
        protected void realHandler() throws Exception {
            String databaseName = seekPost("databaseName", "");

            ArrayList<HashMap<String, String>> list = DataCenter.getSharedInstance().getDatabaseDataCenter().getEntityWithKey(databaseName).showFullProcessList();
            registerDataProperty("list", list);
            sayOK();
        }

        @Override
        protected HashSet<String> rolesRequired() {
            return adminRoleSetForRolesRequired();
        }
    }

    public static class DatabaseKillHandler extends ExtendedHttpHandler {
        @Override
        protected void realHandler() throws Exception {
            String databaseName = seekPost("databaseName", "");
            String username = seekPost("username", "");
            String processId = seekPost("id", "");

            if (username.isEmpty() || processId.isEmpty()) throw new Exception("empty input");

            try {
                boolean killed = DataCenter.getSharedInstance().getDatabaseDataCenter().getEntityWithKey(databaseName).kill(processId, username);
                registerDataProperty("killed", killed);
            } catch (Exception e) {
                registerDataProperty("error", e.getMessage());
            }
            sayOK();
        }

        @Override
        protected HashSet<String> rolesRequired() {
            return adminRoleSetForRolesRequired();
        }
    }
}
