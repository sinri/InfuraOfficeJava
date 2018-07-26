package InfuraOffice.DataCenter;

import InfuraOffice.DataEntity.PlatformEntity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;

public class PlatformDataCenter extends AnyDataCenter<PlatformEntity> {
    public PlatformDataCenter() {
        entities = new HashMap<>();
    }

    @Override
    public String dataTypeOfT() {
        return "platforms";
    }

    @Override
    public HashMap<String, PlatformEntity> readEntityMapFromFile() {
        try {
            String json = fetchDecryptedJson();

            HashMap<String, PlatformEntity> map = new HashMap<>();

            JsonObject object = (new JsonParser()).parse(json).getAsJsonObject();
            object.keySet().forEach(key -> {
                PlatformEntity entity = new PlatformEntity();
                entity.loadPropertiesFormJsonElement(object.get(key));
                map.put(key, entity);
            });

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
