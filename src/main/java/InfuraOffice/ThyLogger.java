package InfuraOffice;

import java.util.Date;

public class ThyLogger {
    public static void log(String level, String content) {
        String text = "[" + (new Date().toString()) + "] [" + level + "] " + content;
        if (!InfuraOfficeConfig.getSharedInstance().isVisibleLogLevel(level)) {
            return;
        }
        switch (level) {
            case "WARN":
            case "ERROR":
                System.err.println(text);
                break;
            case "DEBUG":
            case "INFO":
            default:
                System.out.println(text);
                break;
        }
    }

    public static void logDebug(String content) {
        log("DEBUG", content);
    }

    public static void logInfo(String content) {
        log("INFO", content);
    }

    public static void logWarn(String content) {
        log("WARN", content);
    }

    public static void logError(String content) {
        log("ERROR", content);
    }
}
