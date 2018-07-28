package InfuraOffice.DataCenter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataCenter {
    private static DataCenter instance;

    static {
        instance = new DataCenter();
    }

//    HashMap<String, UserEntity> users;
//    HashMap<String, ServerEntity> servers;
//    HashMap<String, ServerGroupEntity> serverGroups;
//    HashMap<String, PlatformEntity> platforms;
//    HashMap<String, DatabaseEntity> databases;

    UserDataCenter userDataCenter;
    ServerDataCenter serverDataCenter;
    ServerGroupDataCenter serverGroupDataCenter;
    PlatformDataCenter platformDataCenter;
    DatabaseDataCenter databaseDataCenter;

    ServerMaintainJobDataCenter serverMaintainJobDataCenter;

    ExecutorService dataExecutorService;

    protected DataCenter() {
        dataExecutorService = Executors.newSingleThreadExecutor();

        userDataCenter = new UserDataCenter();
        serverDataCenter = new ServerDataCenter();
        serverGroupDataCenter = new ServerGroupDataCenter();
        platformDataCenter = new PlatformDataCenter();
        databaseDataCenter = new DatabaseDataCenter();

        serverMaintainJobDataCenter = new ServerMaintainJobDataCenter();
    }

    public static DataCenter getSharedInstance() {
        return instance;
    }

    public static String encryptText(String raw) {
        // encrypt would be added later
        return raw;
    }

    public static String decryptText(String encrypted) {
        // decrypt would be added later
        return encrypted;
    }

    public ServerMaintainJobDataCenter getServerMaintainJobDataCenter() {
        return serverMaintainJobDataCenter;
    }

    public void registerDataTask(Runnable task) {
        dataExecutorService.execute(task);
    }

    public UserDataCenter getUserDataCenter() {
        return userDataCenter;
    }

    public ServerDataCenter getServerDataCenter() {
        return serverDataCenter;
    }

    public ServerGroupDataCenter getServerGroupDataCenter() {
        return serverGroupDataCenter;
    }

    public PlatformDataCenter getPlatformDataCenter() {
        return platformDataCenter;
    }

    public DatabaseDataCenter getDatabaseDataCenter() {
        return databaseDataCenter;
    }

//    public HashMap<String, UserEntity> getUsers() {
//        return userDataCenter.getEntities();
//    }
//
//    public HashMap<String, ServerEntity> getServers() {
//        return serverDataCenter.getEntities();
//    }
//
//    public HashMap<String, ServerGroupEntity> getServerGroups() {
//        return serverGroupDataCenter.getEntities();
//    }
//
//    public HashMap<String, PlatformEntity> getPlatforms() {
//        return platformDataCenter.getEntities();
//    }
//
//    public HashMap<String, DatabaseEntity> getDatabases() {
//        return databaseDataCenter.getEntities();
//    }

    public void loadFromFile() {
//        HashMap<String, UserEntity> userEntityHashMap = (new UserDataCenter()).readEntityMapFromFile();
//        if (userEntityHashMap != null) users = userEntityHashMap;
//
//        HashMap<String, PlatformEntity> platformEntityHashMap = (new PlatformDataCenter()).readEntityMapFromFile();
//        if (platformEntityHashMap != null) platforms = platformEntityHashMap;
//
//        HashMap<String, DatabaseEntity> databaseEntityHashMap = (new DatabaseDataCenter()).readEntityMapFromFile();
//        if (databaseEntityHashMap != null) databases = databaseEntityHashMap;
//
//        HashMap<String, ServerEntity> serverEntityHashMap = (new ServerDataCenter()).readEntityMapFromFile();
//        if (serverEntityHashMap != null) servers = serverEntityHashMap;
//
//        HashMap<String, ServerGroupEntity> serverGroupEntityHashMap = (new ServerGroupDataCenter()).readEntityMapFromFile();
//        if (serverGroupEntityHashMap != null) serverGroups = serverGroupEntityHashMap;
        userDataCenter.loadFromFile();
        platformDataCenter.loadFromFile();
        databaseDataCenter.loadFromFile();
        serverDataCenter.loadFromFile();
        serverGroupDataCenter.loadFromFile();
        serverMaintainJobDataCenter.loadFromFile();
    }

    public void writeIntoFile() {
        userDataCenter.writeEntityMapIntoFile();
        platformDataCenter.writeEntityMapIntoFile();
        databaseDataCenter.writeEntityMapIntoFile();
        serverDataCenter.writeEntityMapIntoFile();
        serverGroupDataCenter.writeEntityMapIntoFile();
        serverMaintainJobDataCenter.writeEntityMapIntoFile();
    }
}