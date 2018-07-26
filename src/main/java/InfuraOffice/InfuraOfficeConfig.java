package InfuraOffice;

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;

public class InfuraOfficeConfig {
    public static final String LOG_LEVEL_DEBUG = "DEBUG";
    public static final String LOG_LEVEL_INFO = "INFO";
    public static final String LOG_LEVEL_WARN = "WARN";
    public static final String LOG_LEVEL_ERROR = "ERROR";

    private static ArrayList<String> logLevelArray;

    private static InfuraOfficeConfig instance;

    static {
        logLevelArray = new ArrayList<>();
        logLevelArray.add("DEBUG");
        logLevelArray.add("INFO");
        logLevelArray.add("WARN");
        logLevelArray.add("ERROR");

        instance = new InfuraOfficeConfig();
    }

    public static InfuraOfficeConfig getSharedInstance() {
        return instance;
    }

    public static void loadDefaultConfig() {
        // load the default
        instance = new InfuraOfficeConfig();
    }

    public static void loadConfigFile(String configFile) throws FileNotFoundException {
        // read the instance file
        instance = (new Gson()).fromJson((new FileReader(configFile)), InfuraOfficeConfig.class);
    }

    public Boolean isVisibleLogLevel(String level) {
        int levelOrder = logLevelArray.indexOf(level);
        if (levelOrder < 0) levelOrder = 1;

        int minOrder = logLevelArray.indexOf(minLogLevel);
        if (minOrder < 0) minOrder = 1;

        return minOrder <= levelOrder;
    }

    // as GSON class

    public String minLogLevel;
    public String privateKeyPath;
    public int httpListenPort;
    public int remoteAgentMaxWorker;
    public String runtimeDir;

    protected InfuraOfficeConfig() {
        httpListenPort = 8080;
        minLogLevel = "INFO";
        privateKeyPath = "~/.ssh/id_rsa";
        remoteAgentMaxWorker = 3;
        runtimeDir = "/Users/Sinri/Codes/idea/InfuraOfficeJava/runtime";// TODO it should be changed
    }

    public void writeIntoConfigFile(String configFile) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configFile));
        bufferedWriter.write((new Gson()).toJson(this));
        bufferedWriter.close();
    }
}
