package InfuraOffice.ScheduleAgent;

import InfuraOffice.DataCenter.DataCenter;
import InfuraOffice.ScheduleAgent.CronJob.ScheduleBasicJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Map;

public class ScheduleAgent {

    public static Scheduler scheduler() throws SchedulerException {
        // Grab the Scheduler instance from the Factory
        return StdSchedulerFactory.getDefaultScheduler();
    }

    public static void initializeScheduler() throws SchedulerException {
        // fetch and register cronjob s
        DataCenter.getSharedInstance().getServerMaintainJobDataCenter().getEntities().forEach((key, jobEntity) -> {
            try {
                jobEntity.registerToScheduler();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        });
        // and start it off
        scheduler().start();
    }

    public static void scheduleOneCronJob(String jobGroup, String jobName, String cronExpression, Class<? extends ScheduleBasicJob> jobClass, Map<String, String> parameters) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, jobGroup)
                .build();
        parameters.forEach((k, v) -> jobDetail.getJobDataMap().put(k, v));
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)) // like "0/5 * * * * ?"
                .build();

        if (scheduler().checkExists(jobKey)) {
            scheduler().getTriggersOfJob(jobKey).forEach(oldTrigger -> {
                try {
                    scheduler().rescheduleJob(oldTrigger.getKey(), trigger);
                } catch (SchedulerException e) {
                    e.printStackTrace();
                }
            });
        } else {
            scheduler().scheduleJob(jobDetail, trigger);
        }
    }

}
