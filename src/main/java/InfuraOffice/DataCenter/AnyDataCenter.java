package InfuraOffice.DataCenter;

import InfuraOffice.DataEntity.AnyEntity;
import InfuraOffice.InfuraOfficeConfig;
import InfuraOffice.ThyLogger;
import com.google.gson.Gson;

import java.io.*;
import java.util.HashMap;

abstract public class AnyDataCenter<T extends AnyEntity> {
    abstract public String dataTypeOfT();

    abstract public HashMap<String, T> readEntityMapFromFile();

    protected HashMap<String, T> entities;

    static String getDataFile(String x) {
        return InfuraOfficeConfig.getSharedInstance().runtimeDir + "/" + x + ".data";
    }

    protected final String fetchDecryptedJson() throws Exception {
        String file = getDataFile(dataTypeOfT());
        if (!(new File(file)).exists()) {
            throw new Exception("The data file does not exist, use empty data instead.");
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder encryptedTextBuilder = new StringBuilder();
        while (true) {
            String line = br.readLine();
            if (line == null) break;
            encryptedTextBuilder.append(line);
        }
        br.close();

        // decode
        return DataCenter.decryptText(encryptedTextBuilder.toString());
    }

    public void writeEntityMapIntoFile() {
        // this work should be done in Asynchronous Thread Pool
        DataCenter.getSharedInstance().registerDataTask(() -> {
            try {
                String file = getDataFile(dataTypeOfT());
                Gson gson = new Gson();
                String json = gson.toJson(entities);

                // encode
                String encrypted = DataCenter.encryptText(json);

                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write(encrypted);
                bw.close();
                ThyLogger.logInfo("writeEntityMapIntoFile " + dataTypeOfT() + ": over");
            } catch (IOException e) {
                e.printStackTrace();
                ThyLogger.logError(e.getMessage());
            }
        });
    }

    public void loadFromFile() {
        HashMap<String, T> entityHashMap = readEntityMapFromFile();
        if (entityHashMap != null) entities = entityHashMap;
        ThyLogger.logDebug("AnyDataCenter Type " + dataTypeOfT() + " loadFromFile -> " + entities.size());
        ThyLogger.logDebug("Keys: " + entities.keySet());
    }

    public HashMap<String, T> getEntities() {
        return entities;
    }

    public T getEntityWithKey(String key) {
        return entities.get(key);
    }
}