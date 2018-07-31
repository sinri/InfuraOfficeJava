package InfuraOffice.WebAgent.context;

import InfuraOffice.DataCenter.DataCenter;
import InfuraOffice.WebAgent.ExtendedHttpHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class EntityQueryContext {
    public static class FetchUserOptionsHandler extends ExtendedHttpHandler {

        @Override
        protected void realHandler() throws IOException {
            ArrayList<HashMap<String, String>> list = new ArrayList<>();
            DataCenter.getSharedInstance().getUserDataCenter().getEntities().forEach((key, entity) -> {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", entity.username);
                map.put("role", entity.role);
                list.add(map);
            });
            registerDataProperty("options", list);
            sayOK();
        }
    }

    public static class FetchPlatformOptionsHandler extends ExtendedHttpHandler {

        @Override
        protected void realHandler() throws IOException {
            ArrayList<HashMap<String, String>> list = new ArrayList<>();
            DataCenter.getSharedInstance().getPlatformDataCenter().getEntities().forEach((key, entity) -> {
                HashMap<String, String> map = new HashMap<>();
                map.put("platformName", entity.platformName);
                map.put("platformType", entity.platformType);
                list.add(map);
            });
            registerDataProperty("options", list);
            sayOK();
        }
    }

    public static class FetchServerGroupOptionsHandler extends ExtendedHttpHandler {

        @Override
        protected void realHandler() throws IOException {
            ArrayList<HashMap<String, Object>> list = new ArrayList<>();
            DataCenter.getSharedInstance().getServerGroupDataCenter().getEntities().forEach((key, entity) -> {
                HashMap<String, Object> map = new HashMap<>();
                map.put("groupName", entity.groupName);
                map.put("servers", entity.servers);
                list.add(map);
            });
            registerDataProperty("options", list);
            sayOK();
        }
    }

    public static class FetchServerOptionsHandler extends ExtendedHttpHandler {

        @Override
        protected void realHandler() throws IOException {
            ArrayList<HashMap<String, String>> list = new ArrayList<>();
            DataCenter.getSharedInstance().getServerDataCenter().getEntities().forEach((key, entity) -> {
                HashMap<String, String> map = new HashMap<>();
                map.put("serverName", entity.serverName);
                map.put("connectAddress", entity.connectAddress);
                map.put("platformName", entity.platformName);
                map.put("assertID", entity.assertID);
                map.put("areaCode", entity.areaCode);
                list.add(map);
            });
            registerDataProperty("options", list);
            sayOK();
        }
    }

    public static class FetchDatabaseOptionsHandler extends ExtendedHttpHandler {

        @Override
        protected void realHandler() throws IOException {
            ArrayList<HashMap<String, String>> list = new ArrayList<>();
            DataCenter.getSharedInstance().getDatabaseDataCenter().getEntities().forEach((key, entity) -> {
                HashMap<String, String> map = new HashMap<>();
                map.put("databaseName", entity.databaseName);
                map.put("type", entity.type);
                map.put("host", entity.host);
                map.put("port", entity.port + "");
                map.put("portInDothan", entity.portInDothan + "");
                map.put("assertID", entity.assertID);
                map.put("areaCode", entity.areaCode);
                list.add(map);
            });
            registerDataProperty("options", list);
            sayOK();
        }
    }
}
