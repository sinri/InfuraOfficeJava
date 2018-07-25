package InfuraOffice.DataEntity;

import com.google.gson.JsonElement;

abstract public class AnyEntity {
    abstract public void loadPropertiesFormJsonElement(JsonElement jsonElement);
}
