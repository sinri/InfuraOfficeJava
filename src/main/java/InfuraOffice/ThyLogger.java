package InfuraOffice;

import java.util.Date;

public class ThyLogger {
    public static void log(String level, String content) {
        String text = "[" + (new Date().toString()) + "] [" + level + "] " + content;
        if (!InfuraOfficeConfig.getSharedInstance().isVisibleLogLevel(level)) {
            return;
        }
        switch (level) {
            case InfuraOfficeConfig.LOG_LEVEL_WARN:
            case InfuraOfficeConfig.LOG_LEVEL_ERROR:
                System.err.println(text);
                break;
            case InfuraOfficeConfig.LOG_LEVEL_DEBUG:
            case InfuraOfficeConfig.LOG_LEVEL_INFO:
            default:
                System.out.println(text);
                break;
        }
    }

    public static void logDebug(String content) {
        log(InfuraOfficeConfig.LOG_LEVEL_DEBUG, content);
    }

    public static void logInfo(String content) {
        log(InfuraOfficeConfig.LOG_LEVEL_INFO, content);
    }

    public static void logWarn(String content) {
        log(InfuraOfficeConfig.LOG_LEVEL_WARN, content);
    }

    public static void logError(String content) {
        log(InfuraOfficeConfig.LOG_LEVEL_ERROR, content);
    }
}
