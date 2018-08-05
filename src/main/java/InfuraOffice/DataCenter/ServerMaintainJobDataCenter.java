package InfuraOffice.DataCenter;

import InfuraOffice.DataEntity.ServerMaintainJobEntity;
import InfuraOffice.ScheduleAgent.ScheduleAgent;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.quartz.SchedulerException;

import java.util.HashMap;

public class ServerMaintainJobDataCenter extends AnyDataCenter<ServerMaintainJobEntity> {
    @Override
    public String dataTypeOfT() {
        return "server_maintain_job";
    }

    @Override
    public HashMap<String, ServerMaintainJobEntity> readEntityMapFromFile() {

        try {
            String json = fetchDecryptedJson();

            HashMap<String, ServerMaintainJobEntity> map = new HashMap<>();

            JsonObject object = (new JsonParser()).parse(json).getAsJsonObject();
            object.keySet().forEach(key -> {
                ServerMaintainJobEntity entity = new ServerMaintainJobEntity();
                entity.loadPropertiesFormJsonElement(object.get(key));
                map.put(key, entity);
            });

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateEntityWithKey(String key, ServerMaintainJobEntity entity) {
        super.updateEntityWithKey(key, entity);
        try {
            entity.registerToScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void removeEntityWithKey(String key) {
        super.removeEntityWithKey(key);
        try {
            ScheduleAgent.removeOneCronJob("ServerMaintainJobs", key);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
