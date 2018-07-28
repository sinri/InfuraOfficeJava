package InfuraOffice.WebAgent.context.Management;

import InfuraOffice.DataCenter.DataCenter;
import InfuraOffice.DataEntity.PlatformEntity;
import InfuraOffice.WebAgent.ExtendedHttpHandler;

import java.util.HashMap;
import java.util.HashSet;

public class PlatformManageContext {
    public static class ListPlatformHandler extends ExtendedHttpHandler {
        @Override
        protected HashSet<String> rolesRequired() {
            return adminRoleSetForRolesRequired();
        }

        @Override
        protected void realHandler() throws Exception {
            HashMap<String, PlatformEntity> platformEntityHashMap = DataCenter.getSharedInstance().getPlatformDataCenter().getEntities();
            registerDataProperty("platforms", platformEntityHashMap);
            sayOK();
        }
    }

    public static class ViewPlatformInfoHandler extends ExtendedHttpHandler {
        @Override
        protected HashSet<String> rolesRequired() {
            return adminRoleSetForRolesRequired();
        }

        @Override
        protected void realHandler() throws Exception {
            String platformName = seekPost("platformName");
            if (platformName == null || platformName.isEmpty()) throw new Exception("platform name empty");
            PlatformEntity platformEntity = DataCenter.getSharedInstance().getPlatformDataCenter().getEntityWithKey(platformName);
            registerDataProperty("platform", platformEntity);
            sayOK();
        }
    }

    public static class UpdatePlatformHandler extends ExtendedHttpHandler {
        @Override
        protected HashSet<String> rolesRequired() {
            return adminRoleSetForRolesRequired();
        }

        @Override
        protected void realHandler() throws Exception {
            String platformName = seekPost("platformName");
            if (platformName == null || platformName.isEmpty()) throw new Exception("platform name empty");
            String act = seekPost("act", "update");

            PlatformEntity platformEntity = DataCenter.getSharedInstance().getPlatformDataCenter().getEntityWithKey(platformName);

            switch (act) {
                case "delete":
                    if (platformEntity == null) throw new Exception("platform does not exist");
                    DataCenter.getSharedInstance().getPlatformDataCenter().removeEntityWithKey(platformName);
                    break;
                case "update":
                    if (platformEntity == null) {
                        platformEntity = new PlatformEntity();
                        platformEntity.platformName = platformName;
                    }
                    platformEntity.platformType = seekPost("platformType", "Unknown");
                    platformEntity.securityKey = seekPost("securityKey", "Unknown");
                    platformEntity.securitySecret = seekPost("securitySecret", "Unknown");

                    DataCenter.getSharedInstance().getPlatformDataCenter().updateEntityWithKey(platformName, platformEntity);
                    break;
                default:
                    throw new Exception("Unknown act");
            }

            sayOK();
        }
    }
}
