package InfuraOffice.DataEntity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashSet;

public class UserEntity extends AnyEntity {
    public String username;
    public String role;
    public HashSet<String> privileges;
    public String lastLoginIP;
    public long lastLoginTimestamp;
    public String passwordHash;

    public UserEntity() {
        username = "";
        role = "";
        privileges = new HashSet<>();
        lastLoginIP = "";
        lastLoginTimestamp = 0;
        passwordHash = "";
    }

    @Override
    public void loadPropertiesFormJsonElement(JsonElement jsonElement) {
        JsonObject object = jsonElement.getAsJsonObject();
        username = object.get("username").getAsString();
        role = object.get("role").getAsString();
        lastLoginIP = object.get("lastLoginIP").getAsString();
        lastLoginTimestamp = object.get("lastLoginTimestamp").getAsLong();
        privileges = new HashSet<>();
        object.get("privileges").getAsJsonArray().forEach(privilege -> privileges.add(privilege.getAsString()));
        passwordHash = object.get("passwordHash").getAsString();
    }

    public boolean validatePasswordHash(String rawPassword) {
        try {
            //ThyLogger.logDebug("validatePasswordHash: "+rawPassword+" | "+passwordHash);
            return PasswordHasher.check(rawPassword, passwordHash);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
