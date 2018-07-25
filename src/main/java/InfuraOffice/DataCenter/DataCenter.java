package InfuraOffice.DataCenter;

import InfuraOffice.DataEntity.*;

import java.util.HashMap;

public class DataCenter {
    private static DataCenter instance;

    public static DataCenter getSharedInstance() {
        return instance;
    }

    static {
        instance = new DataCenter();
    }

    HashMap<String, UserEntity> users;
    HashMap<String, ServerEntity> servers;
    HashMap<String, ServerGroupEntity> serverGroups;
    HashMap<String, PlatformEntity> platforms;
    HashMap<String, DatabaseEntity> databases;

    protected DataCenter() {
        users = new HashMap<>();
        servers = new HashMap<>();
        serverGroups = new HashMap<>();
        platforms = new HashMap<>();
        databases = new HashMap<>();
    }

    public void loadFromFile() {
        HashMap<String, UserEntity> userEntityHashMap = (new UserDataCenter()).readEntityMapFromFile();
        if (userEntityHashMap != null) users = userEntityHashMap;

        HashMap<String, PlatformEntity> platformEntityHashMap = (new PlatformDataCenter()).readEntityMapFromFile();
        if (platformEntityHashMap != null) platforms = platformEntityHashMap;

        HashMap<String, DatabaseEntity> databaseEntityHashMap = (new DatabaseDataCenter()).readEntityMapFromFile();
        if (databaseEntityHashMap != null) databases = databaseEntityHashMap;

        HashMap<String, ServerEntity> serverEntityHashMap = (new ServerDataCenter()).readEntityMapFromFile();
        if (serverEntityHashMap != null) servers = serverEntityHashMap;

        HashMap<String, ServerGroupEntity> serverGroupEntityHashMap = (new ServerGroupDataCenter()).readEntityMapFromFile();
        if (serverGroupEntityHashMap != null) serverGroups = serverGroupEntityHashMap;
    }

    public void writeIntoFile() {
        (new UserDataCenter()).writeEntityMapIntoFile(users);
        (new PlatformDataCenter()).writeEntityMapIntoFile(platforms);
        (new DatabaseDataCenter()).writeEntityMapIntoFile(databases);
        (new ServerDataCenter()).writeEntityMapIntoFile(servers);
        (new ServerGroupDataCenter()).writeEntityMapIntoFile(serverGroups);
    }


    public static String encryptText(String raw) {
        // TODO encrypt
        return raw;
    }

    public static String decryptText(String encrypted) {
        // TODO decrypt
        return encrypted;
    }
}