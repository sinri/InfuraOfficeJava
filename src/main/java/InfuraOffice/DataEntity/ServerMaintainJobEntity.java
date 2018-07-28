package InfuraOffice.DataEntity;

import InfuraOffice.ScheduleAgent.CronJob.ServerMaintainJob;
import InfuraOffice.ScheduleAgent.ScheduleAgent;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.quartz.SchedulerException;

import java.util.HashMap;
import java.util.HashSet;

public class ServerMaintainJobEntity extends AnyEntity {
    public String cronJobName;
    public HashSet<String> serverGroupNames;
    public HashSet<String> serverNames;
    public String cronExpression;
    public String type;
    public String command;

    public ServerMaintainJobEntity() {
        cronJobName = "";
        serverGroupNames = new HashSet<>();
        serverNames = new HashSet<>();
        cronExpression = "";
        type = "";
        command = "";
    }

    @Override
    public void loadPropertiesFormJsonElement(JsonElement jsonElement) {
        JsonObject object = jsonElement.getAsJsonObject();
        cronJobName = object.get("cronJobName").getAsString();
        cronExpression = object.get("cronExpression").getAsString();
        type = object.get("type").getAsString();
        command = object.get("command").getAsString();
        serverGroupNames = new HashSet<>();
        object.get("serverGroupNames").getAsJsonArray().forEach(serverGroup -> serverGroupNames.add(serverGroup.getAsString()));
        serverNames = new HashSet<>();
        object.get("serverNames").getAsJsonArray().forEach(server -> serverNames.add(server.getAsString()));
    }

    public void registerToScheduler() throws SchedulerException {
        HashMap<String, String> parameters = new HashMap<>();

//        StringBuilder groups=new StringBuilder();
//        serverGroupNames.forEach(groups::append);
//        StringBuilder servers=new StringBuilder();
//        serverNames.forEach(servers::append);

        parameters.put("cronJobName", cronJobName);
        //parameters.put("serverGroupNames",groups.toString());
        //parameters.put("serverNames",servers.toString());
        //parameters.put("type",type);
        //parameters.put("command",command);

        ScheduleAgent.scheduleOneCronJob("ServerMaintainJobs", cronJobName, cronExpression, ServerMaintainJob.class, parameters);
    }

    public void execute() {
        // TODO remote job
    }
}
