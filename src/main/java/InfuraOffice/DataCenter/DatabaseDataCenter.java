package InfuraOffice.DataCenter;

import InfuraOffice.DataEntity.DatabaseEntity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;

public class DatabaseDataCenter extends AnyDataCenter<DatabaseEntity> {

    public DatabaseDataCenter() {
        entities = new HashMap<>();
    }

    @Override
    public String dataTypeOfT() {
        return "databases";
    }

    @Override
    public HashMap<String, DatabaseEntity> readEntityMapFromFile() {
        try {
            String json = fetchDecryptedJson();

            HashMap<String, DatabaseEntity> map = new HashMap<>();

            JsonObject object = (new JsonParser()).parse(json).getAsJsonObject();
            object.keySet().forEach(key -> {
                DatabaseEntity entity = new DatabaseEntity();
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
