package InfuraOffice.ScheduleAgent.CronJob;

import InfuraOffice.DataCenter.DataCenter;
import InfuraOffice.DataEntity.ServerMaintainJobEntity;
import InfuraOffice.ThyLogger;
import org.quartz.JobExecutionContext;

public class ServerMaintainJob extends ScheduleBasicJob {
    @Override
    protected void realExecute(JobExecutionContext jobExecutionContext) {
        String cronJobName = readData("cronJobName");
        ThyLogger.logInfo("cronJobName: " + cronJobName);
        // fetch cronJobEntity
        ServerMaintainJobEntity serverMaintainJobEntity = DataCenter.getSharedInstance().getServerMaintainJobDataCenter().getEntityWithKey(cronJobName);
        // and run
        if (serverMaintainJobEntity == null) {
            ThyLogger.logError("Missing Job Entity: " + cronJobName);
            return;
        }
        serverMaintainJobEntity.execute();
    }
}
