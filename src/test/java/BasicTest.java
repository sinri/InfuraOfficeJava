import InfuraOffice.DataCenter.DataCenter;
import InfuraOffice.DataCenter.UserDataCenter;
import InfuraOffice.DataEntity.UserEntity;
import InfuraOffice.InfuraOfficeConfig;
import com.google.gson.Gson;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

public class BasicTest {
    static final String configFile = "/Users/Sinri/Codes/idea/InfuraOfficeJava/runtime/config.json";

    public static void main(String[] argv) {
        // any test
        try {
            test1();
            test2();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void test1() throws IOException {
        InfuraOfficeConfig defaultConfig = InfuraOfficeConfig.getSharedInstance();
        System.out.println("json default instance: \n" + (new Gson()).toJson(defaultConfig));

        defaultConfig.runtimeDir = "/Users/Sinri/Codes/idea/InfuraOfficeJava/runtime";
        defaultConfig.minLogLevel = "DEBUG";

        System.out.println("json modified instance: \n" + (new Gson()).toJson(defaultConfig));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configFile));
        bufferedWriter.write((new Gson()).toJson(defaultConfig));
        bufferedWriter.close();

        InfuraOfficeConfig config = (new Gson()).fromJson((new FileReader(configFile)), InfuraOfficeConfig.class);
        System.out.println("instance: " + config.minLogLevel);
    }

    private static void test2() throws FileNotFoundException {
        InfuraOfficeConfig.loadConfigFile(configFile);
        HashMap<String, UserEntity> map = DataCenter.getSharedInstance().getUserDataCenter().getEntities();

        for (int i = 1; i < 5; i++) {
            UserEntity user = new UserEntity();
            user.username = "USER_" + i;
            user.role = "USER";
            user.lastLoginIP = "3.4.10." + i;
            user.lastLoginTimestamp = 0;
            user.privileges = new HashSet<>();
            user.privileges.add("P_" + (i * i + 1));
            user.privileges.add("P_" + (i * i * i + 1));
            map.put(user.username, user);
        }
        new UserDataCenter().writeEntityMapIntoFile();

        HashMap<String, UserEntity> users = new UserDataCenter().readEntityMapFromFile();
        System.out.println("read result: " + users);
        if (users == null) {
            System.out.println("USERS READ as NULL");
        } else {
            users.forEach((key, value) -> System.out.println("USER " + key + " : " + value.lastLoginIP));
        }
    }
}
