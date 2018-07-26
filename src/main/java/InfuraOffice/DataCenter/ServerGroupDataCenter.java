package InfuraOffice.DataCenter;

import InfuraOffice.DataEntity.ServerGroupEntity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;

public class ServerGroupDataCenter extends AnyDataCenter<ServerGroupEntity> {
    public ServerGroupDataCenter() {
        entities = new HashMap<>();
    }
    @Override
    public String dataTypeOfT() {
        return "server_groups";
    }

    @Override
    public HashMap<String, ServerGroupEntity> readEntityMapFromFile() {
        try {
            String json = fetchDecryptedJson();

            HashMap<String, ServerGroupEntity> map = new HashMap<>();

            JsonObject object = (new JsonParser()).parse(json).getAsJsonObject();
            object.keySet().forEach(key -> {
                ServerGroupEntity entity = new ServerGroupEntity();
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
