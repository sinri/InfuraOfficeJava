import InfuraOffice.DataCenter.DataCenter;
import InfuraOffice.DataEntity.PasswordHasher;
import InfuraOffice.DataEntity.UserEntity;
import InfuraOffice.InfuraOfficeConfig;

import java.util.HashMap;
import java.util.HashSet;

public class InitRuntimeData {
    static final String configFile = "/Users/Sinri/Codes/idea/InfuraOfficeJava/runtime/config.json";

    public static void main(String[] argv) {
        try {
            // for config
            InfuraOfficeConfig defaultConfig = InfuraOfficeConfig.getSharedInstance();
            defaultConfig.minLogLevel = "DEBUG";
            defaultConfig.runtimeDir = "/Users/Sinri/Codes/idea/InfuraOfficeJava/runtime";
            defaultConfig.writeIntoConfigFile(configFile);
            // for user
            HashMap<String, UserEntity> users = DataCenter.getSharedInstance().getUserDataCenter().getEntities();
            UserEntity userEntity = new UserEntity();
            userEntity.username = "admin";
            userEntity.role = "ADMIN";
            userEntity.privileges = new HashSet<>();
            userEntity.privileges.add("ADMIN");
            userEntity.passwordHash = PasswordHasher.getSaltedHash("InGodWeTrust");

            // check
            if (!PasswordHasher.check("InGodWeTrust", userEntity.passwordHash)) {
                throw new Exception("ERROR! PW CANNOT BE VALIDATED");
            }

            users.put(userEntity.username, userEntity);
            DataCenter.getSharedInstance().getUserDataCenter().writeEntityMapIntoFile();
            // others
            DataCenter.getSharedInstance().getPlatformDataCenter().writeEntityMapIntoFile();
            DataCenter.getSharedInstance().getDatabaseDataCenter().writeEntityMapIntoFile();
            DataCenter.getSharedInstance().getServerGroupDataCenter().writeEntityMapIntoFile();
            DataCenter.getSharedInstance().getServerDataCenter().writeEntityMapIntoFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
