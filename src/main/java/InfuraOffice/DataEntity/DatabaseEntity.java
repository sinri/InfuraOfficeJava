package InfuraOffice.DataEntity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;

public class DatabaseEntity extends AnyEntity {
    public String databaseName;
    public String type;
    public String host;
    public int port;
    public int portInDothan;
    public String platformName;
    public String assertID;
    public String areaCode;
    public HashMap<String, String> accounts;

    public DatabaseEntity() {
        databaseName = null;
        type = null;
        host = null;
        port = 3306;
        portInDothan = 0;
        platformName = null;
        assertID = null;
        areaCode = null;
        accounts = new HashMap<>();
    }

    @Override
    public void loadPropertiesFormJsonElement(JsonElement jsonElement) {
        JsonObject object = jsonElement.getAsJsonObject();
        databaseName = object.get("databaseName").getAsString();
        type = object.get("type").getAsString();
        host = object.get("host").getAsString();
        port = object.get("port").getAsInt();
        portInDothan = object.get("portInDothan").getAsInt();
        platformName = object.get("platformName").getAsString();
        assertID = object.get("assertID").getAsString();
        areaCode = object.get("areaCode").getAsString();
        accounts = new HashMap<>();
        JsonObject accountsJsonObject = object.get("accounts").getAsJsonObject();
        accountsJsonObject.keySet().forEach(un -> accountsJsonObject.get(un).getAsString());
    }
}
