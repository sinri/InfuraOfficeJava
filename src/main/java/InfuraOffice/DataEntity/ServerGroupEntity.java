package InfuraOffice.DataEntity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashSet;

public class ServerGroupEntity extends AnyEntity {
    public String groupName;
    public HashSet<String> servers;

    public ServerGroupEntity() {
        groupName = "";
        servers = new HashSet<>();
    }

    @Override
    public void loadPropertiesFormJsonElement(JsonElement jsonElement) {
        JsonObject object = jsonElement.getAsJsonObject();
        groupName = object.get("groupName").getAsString();
        servers = new HashSet<>();
        object.get("servers").getAsJsonArray().forEach(server -> servers.add(server.getAsString()));
    }
}
