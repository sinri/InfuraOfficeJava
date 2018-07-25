package InfuraOffice.DataEntity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashSet;

public class ServerEntity extends AnyEntity {
    public String serverName;
    public String connectAddress;
    public int sshPort;
    public String sshUser;
    public String platformName;
    public String assertID;
    public String areaCode;
    public HashSet<String> slkLogPathSet;

    public ServerEntity() {
        serverName = null;
        connectAddress = null;
        sshPort = 22;
        sshUser = "admin";
        platformName = null;
        assertID = null;
        areaCode = null;
        slkLogPathSet = new HashSet<>();
    }

    @Override
    public void loadPropertiesFormJsonElement(JsonElement jsonElement) {
        JsonObject object = jsonElement.getAsJsonObject();
        serverName = object.get("serverName").getAsString();
        connectAddress = object.get("connectAddress").getAsString();
        sshPort = object.get("sshPort").getAsInt();
        sshUser = object.get("sshUser").getAsString();
        platformName = object.get("platformName").getAsString();
        assertID = object.get("assertID").getAsString();
        areaCode = object.get("areaCode").getAsString();
        slkLogPathSet = new HashSet<>();
        object.get("slkLogPathSet").getAsJsonArray().forEach(path -> slkLogPathSet.add(path.getAsString()));
    }
}
