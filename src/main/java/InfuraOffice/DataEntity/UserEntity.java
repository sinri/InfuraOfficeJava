package InfuraOffice.DataEntity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashSet;

public class UserEntity extends AnyEntity {
    public String username;
    public String role;
    public HashSet<String> privileges;
    public String lastLoginIP;
    public int lastLoginTimestamp;

    public UserEntity() {
        username = null;
        role = null;
        privileges = new HashSet<>();
        lastLoginIP = null;
        lastLoginTimestamp = 0;
    }

    @Override
    public void loadPropertiesFormJsonElement(JsonElement jsonElement) {
        JsonObject object = jsonElement.getAsJsonObject();
        username = object.get("username").getAsString();
        role = object.get("role").getAsString();
        lastLoginIP = object.get("lastLoginIP").getAsString();
        lastLoginTimestamp = object.get("lastLoginTimestamp").getAsInt();
        privileges = new HashSet<>();
        object.get("privileges").getAsJsonArray().forEach(privilege -> privileges.add(privilege.getAsString()));
    }
}
