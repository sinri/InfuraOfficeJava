package InfuraOffice.WebAgent.context.Management;

import InfuraOffice.DataCenter.DataCenter;
import InfuraOffice.DataEntity.DatabaseEntity;
import InfuraOffice.WebAgent.ExtendedHttpHandler;

import java.util.HashMap;
import java.util.HashSet;

public class DatabaseManageContext {
    public static class ListDatabaseHandler extends ExtendedHttpHandler {
        @Override
        protected HashSet<String> rolesRequired() {
            return adminRoleSetForRolesRequired();
        }

        @Override
        protected void realHandler() throws Exception {
            HashMap<String, DatabaseEntity> entityHashMap = DataCenter.getSharedInstance().getDatabaseDataCenter().getEntities();
            registerDataProperty("databases", entityHashMap);
            sayOK();
        }
    }

    public static class ViewDatabaseInfoHandler extends ExtendedHttpHandler {
        @Override
        protected HashSet<String> rolesRequired() {
            return adminRoleSetForRolesRequired();
        }

        @Override
        protected void realHandler() throws Exception {
            String databaseName = seekPost("databaseName");
            if (databaseName == null || databaseName.isEmpty()) throw new Exception("database name empty");
            DatabaseEntity platformEntity = DataCenter.getSharedInstance().getDatabaseDataCenter().getEntityWithKey(databaseName);
            registerDataProperty("database", platformEntity);
            sayOK();
        }
    }

    public static class UpdateDatabaseHandler extends ExtendedHttpHandler {
        @Override
        protected void realHandler() throws Exception {
            String databaseName = seekPost("databaseName");
            if (databaseName == null || databaseName.isEmpty()) throw new Exception("database name empty");
            String act = seekPost("act", "update");

            DatabaseEntity databaseEntity = DataCenter.getSharedInstance().getDatabaseDataCenter().getEntityWithKey(databaseName);

            switch (act) {
                case "delete":
                    if (databaseEntity == null) throw new Exception("database does not exist");
                    DataCenter.getSharedInstance().getDatabaseDataCenter().removeEntityWithKey(databaseName);
                    break;
                case "update":
                    if (databaseEntity == null) {
                        databaseEntity = new DatabaseEntity();
                        databaseEntity.databaseName = databaseName;
                    }
                    databaseEntity.platformName = seekPost("platformName", "");
                    databaseEntity.areaCode = seekPost("areaCode", "");
                    databaseEntity.assertID = seekPost("assertID", "");
                    databaseEntity.portInDothan = Integer.parseInt(seekPost("portInDothan", "-1"));
                    databaseEntity.port = Integer.parseInt(seekPost("port", "-1"));
                    databaseEntity.host = seekPost("host", "");
                    databaseEntity.type = seekPost("type", "");
                    break;
                case "update_account":
                    if (databaseEntity == null) throw new Exception("database does not exist");
                    String username = seekPost("username", "");
                    String password = seekPost("password", "");
                    if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                        throw new Exception("database account info error");
                    }
                    databaseEntity.accounts.put(username, password);
                    break;
                case "remove_account":
                    if (databaseEntity == null) throw new Exception("database does not exist");
                    String usernameToRemove = seekPost("username", "");
                    if (usernameToRemove == null || usernameToRemove.isEmpty() || !databaseEntity.accounts.containsKey(usernameToRemove)) {
                        throw new Exception("Username Empty or Deleted");
                    }
                    databaseEntity.accounts.remove(usernameToRemove);
                    break;
                default:
                    throw new Exception("Unknown act");
            }

            sayOK();
        }

        @Override
        protected HashSet<String> rolesRequired() {
            return adminRoleSetForRolesRequired();
        }
    }
}
