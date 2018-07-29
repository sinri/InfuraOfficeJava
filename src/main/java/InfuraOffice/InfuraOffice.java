package InfuraOffice;

import InfuraOffice.DataCenter.DataCenter;
import InfuraOffice.DataEntity.PasswordHasher;
import InfuraOffice.DataEntity.UserEntity;
import InfuraOffice.RemoteAgent.RemoteAgent;
import InfuraOffice.ScheduleAgent.ScheduleAgent;
import InfuraOffice.WebAgent.WebAgent;

import java.util.HashMap;
import java.util.HashSet;

public class InfuraOffice {

    /**
     * @return InfuraOfficeConfig
     * @deprecated use InfuraOfficeConfig.getSharedInstance() instead
     */
    public static InfuraOfficeConfig getConfig() {
        return InfuraOfficeConfig.getSharedInstance();
    }

    public static void main(String[] argv) {
        try {
            if (argv.length > 0) {
                // load instance
                InfuraOfficeConfig.loadConfigFile(argv[0]);
            } else {
                InfuraOfficeConfig.loadDefaultConfig();
            }
            ThyLogger.logDebug("Here goes config log level " + InfuraOfficeConfig.getSharedInstance().minLogLevel);

            // load DataCenter
            DataCenter.getSharedInstance().loadFromFile();

            // set up daemon
            RemoteAgent.initializeSharedInstance(InfuraOfficeConfig.getSharedInstance().remoteAgentMaxWorker);
            // set up schedule
            ScheduleAgent.initializeScheduler();

            // ensureUserAvailable
            ensureUserAvailable();

            // set up HTTP service
            WebAgent.listen(InfuraOfficeConfig.getSharedInstance().httpListenPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void ensureUserAvailable() throws Exception {
        // for user
        HashMap<String, UserEntity> users = DataCenter.getSharedInstance().getUserDataCenter().getEntities();
        if (users.size() == 0) {
            UserEntity userEntity = new UserEntity();
            userEntity.username = "admin";
            userEntity.role = UserEntity.ROLE_ADMIN;
            userEntity.privileges = new HashSet<>();
            userEntity.privileges.add(UserEntity.PRIVILEGE_VAIN);
            userEntity.passwordHash = PasswordHasher.getSaltedHash("InGodWeTrust");

            // check
            if (!PasswordHasher.check("InGodWeTrust", userEntity.passwordHash)) {
                throw new Exception("ERROR! PW CANNOT BE VALIDATED");
            }

            users.put(userEntity.username, userEntity);
            DataCenter.getSharedInstance().getUserDataCenter().writeEntityMapIntoFile();
        }
    }
}
