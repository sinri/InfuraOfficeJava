package InfuraOffice.DataCenter;

import InfuraOffice.DataEntity.UserEntity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;

public class UserDataCenter extends AnyDataCenter<UserEntity> {

    @Override
    public String dataTypeOfT() {
        return "users";
    }

    @Override
    public HashMap<String, UserEntity> readEntityMapFromFile() {
        try {
            String json = fetchDecryptedJson();

            HashMap<String, UserEntity> map = new HashMap<>();

            JsonObject object = (new JsonParser()).parse(json).getAsJsonObject();
            object.keySet().forEach(key -> {
                UserEntity entity = new UserEntity();
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