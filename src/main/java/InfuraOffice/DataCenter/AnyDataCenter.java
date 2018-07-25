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

    public void writeEntityMapIntoFile(HashMap<String, T> map) {
        try {
            String file = getDataFile(dataTypeOfT());
            Gson gson = new Gson();
            String json = gson.toJson(map);

            // encode
            String encrypted = DataCenter.encryptText(json);

            BufferedWriter br = new BufferedWriter(new FileWriter(file));
            br.write(encrypted);
            br.close();
            ThyLogger.logInfo("writeEntityMapIntoFile " + dataTypeOfT() + ": over");
        } catch (IOException e) {
            e.printStackTrace();
            ThyLogger.logError(e.getMessage());
        }
    }

}