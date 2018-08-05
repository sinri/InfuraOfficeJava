package InfuraOffice.DataEntity;

import InfuraOffice.DataCenter.DataCenter;
import InfuraOffice.FileLogger;
import InfuraOffice.InfuraOfficeConfig;
import InfuraOffice.RemoteAgent.SSHAgent;
import InfuraOffice.ScheduleAgent.CronJob.ServerMaintainJob;
import InfuraOffice.ScheduleAgent.ScheduleAgent;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.quartz.SchedulerException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class ServerMaintainJobEntity extends AnyEntity {
    public String cronJobName;
    public HashSet<String> serverGroupNames;
    public HashSet<String> serverNames;
    public String cronExpression;
    public String type;
    public String command;
    public boolean active;

    public ServerMaintainJobEntity() {
        cronJobName = "";
        serverGroupNames = new HashSet<>();
        serverNames = new HashSet<>();
        cronExpression = "";
        type = "";
        command = "";
        active = false;
    }

    @Override
    public void loadPropertiesFormJsonElement(JsonElement jsonElement) {
        JsonObject object = jsonElement.getAsJsonObject();
        cronJobName = object.get("cronJobName").getAsString();
        cronExpression = object.get("cronExpression").getAsString();
        active = object.get("active").getAsBoolean();
        type = object.get("type").getAsString();
        command = object.get("command").getAsString();
        serverGroupNames = new HashSet<>();
        object.get("serverGroupNames").getAsJsonArray().forEach(serverGroup -> serverGroupNames.add(serverGroup.getAsString()));
        serverNames = new HashSet<>();
        object.get("serverNames").getAsJsonArray().forEach(server -> serverNames.add(server.getAsString()));
    }

    public void registerToScheduler() throws SchedulerException {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("cronJobName", cronJobName);
        ScheduleAgent.scheduleOneCronJob("ServerMaintainJobs", cronJobName, cronExpression, ServerMaintainJob.class, parameters);
    }

    public void execute() {
        try {
            FileLogger fileLogger = new FileLogger(InfuraOfficeConfig.getSharedInstance().jobLogDir + "/" + this.cronJobName + "_" + (new SimpleDateFormat("yyyy-MM-dd").format(new Date())) + ".log");

            HashSet<String> involvedServerNames = new HashSet<>();
            serverGroupNames.forEach(serverGroupName -> {
                try {
                    ServerGroupEntity serverGroupEntity = DataCenter.getSharedInstance().getServerGroupDataCenter().getEntityWithKey(serverGroupName);
                    involvedServerNames.addAll(serverGroupEntity.servers);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            involvedServerNames.addAll(serverNames);

            involvedServerNames.forEach(serverName -> {
                try {
                    fileLogger.logInfo("ServerMaintainJob " + cronJobName + " Executed on " + serverName);

                    if (!active) throw new Exception("This job is not active, stop here.");

                    ServerEntity serverEntity = DataCenter.getSharedInstance().getServerDataCenter().getEntityWithKey(serverName);
                    SSHAgent agent = new SSHAgent();
                    int returnValue = agent.executeCommandOnRemote(serverEntity.connectAddress, serverEntity.sshPort, serverEntity.sshUser, command);

                    fileLogger.logInfo("Remote execution return value is " + returnValue);

                    fileLogger.writeTextBlock("--- OUTPUT BEGIN ---");
                    fileLogger.writeTextBlock(agent.getOutput());
                    fileLogger.writeTextBlock("--- OUTPUT END ---");
                } catch (Exception e) {
                    //e.printStackTrace();
                    fileLogger.logError(e.getMessage());
                }
            });
            fileLogger.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*
cronExpression in Quartz Framework
字段名                 允许的值                        允许的特殊字符
秒                         0-59                               , - * /
分                         0-59                               , - * /
小时                     0-23                               , - * /
日                         1-31                               , - * ? / L W C
月                         1-12 or JAN-DEC         , - * /
周几                     1-7 or SUN-SAT           , - * ? / L C #
年 (可选字段)     empty, 1970-2099      , - * /
 */
