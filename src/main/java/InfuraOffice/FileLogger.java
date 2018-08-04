package InfuraOffice;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class FileLogger {
    private BufferedWriter bufferedWriter;

    public FileLogger(String file) throws IOException {
        bufferedWriter = new BufferedWriter(new FileWriter(file, true));
    }

    public void writeTextBlock(String text) {
        try {
            bufferedWriter.write(text);
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String level, String content) {
        String text = "[" + (new Date().toString()) + "] [" + level + "] " + content;
        if (!InfuraOfficeConfig.getSharedInstance().isVisibleLogLevel(level)) {
            return;
        }
        writeTextBlock(text);
    }

    public void logDebug(String content) {
        log(InfuraOfficeConfig.LOG_LEVEL_DEBUG, content);
    }

    public void logInfo(String content) {
        log(InfuraOfficeConfig.LOG_LEVEL_INFO, content);
    }

    public void logWarn(String content) {
        log(InfuraOfficeConfig.LOG_LEVEL_WARN, content);
    }

    public void logError(String content) {
        log(InfuraOfficeConfig.LOG_LEVEL_ERROR, content);
    }
}
