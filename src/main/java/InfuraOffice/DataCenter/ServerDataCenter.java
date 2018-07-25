package InfuraOffice.DataCenter;

import InfuraOffice.DataEntity.ServerEntity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;

public class ServerDataCenter extends AnyDataCenter<ServerEntity> {
    @Override
    public String dataTypeOfT() {
        return "servers";
    }

    @Override
    public HashMap<String, ServerEntity> readEntityMapFromFile() {
        try {
            String json = fetchDecryptedJson();

            HashMap<String, ServerEntity> map = new HashMap<>();

            JsonObject object = (new JsonParser()).parse(json).getAsJsonObject();
            object.keySet().forEach(key -> {
                ServerEntity entity = new ServerEntity();
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
