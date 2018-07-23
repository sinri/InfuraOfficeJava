package InfuraOffice;

import java.util.ArrayList;

public class InfuraOfficeConfig {
    public static final String LOG_LEVEL_DEBUG = "DEBUG";
    public static final String LOG_LEVEL_INFO = "INFO";
    public static final String LOG_LEVEL_WARN = "WARN";
    public static final String LOG_LEVEL_ERROR = "ERROR";

    private static ArrayList<String> logLevelArray;

    static {
        logLevelArray = new ArrayList<>();
        logLevelArray.add("DEBUG");
        logLevelArray.add("INFO");
        logLevelArray.add("WARN");
        logLevelArray.add("ERROR");
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

    public InfuraOfficeConfig() {
        httpListenPort = 8080;
        minLogLevel = "INFO";
        privateKeyPath = "~/.ssh/id_rsa";
        remoteAgentMaxWorker = 3;
    }
}
