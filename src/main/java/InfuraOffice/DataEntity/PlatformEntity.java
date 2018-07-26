package InfuraOffice.DataEntity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PlatformEntity extends AnyEntity {
    public String platformType;
    public String platformName;
    public String securityKey;
    public String securitySecret;

    public PlatformEntity() {
        platformName = "";
        platformType = "";
        securityKey = "";
        securitySecret = "";
    }

    @Override
    public void loadPropertiesFormJsonElement(JsonElement jsonElement) {
        JsonObject object = jsonElement.getAsJsonObject();
        platformName = object.get("platformName").getAsString();
        platformType = object.get("platformType").getAsString();
        securityKey = object.get("securityKey").getAsString();
        securitySecret = object.get("securitySecret").getAsString();
    }
}
