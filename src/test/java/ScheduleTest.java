import InfuraOffice.ScheduleAgent.CronJob.ScheduleBasicJob;
import InfuraOffice.ScheduleAgent.ScheduleAgent;
import InfuraOffice.ThyLogger;
import org.quartz.*;

public class ScheduleTest {
    public static void main(String[] argv) {
        try {
            ScheduleAgent.initializeScheduler();

            /*
             * 秒 分 时 日 月 星期 年（可选）
             * Day and Weekday should set one and another to be ?
             */

            JobDetail jobDetail = JobBuilder.newJob(ScheduleTest.TestScheduleJob.class)
                    .withIdentity("testJob_1", "group_1")
                    .build();
            jobDetail.getJobDataMap().put("k1", "v1");
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
                    .build();

            //用于描叙Job实现类及其他的一些静态信息，构建一个作业实例
            //JobDetail jobDetail = JobBuilder.newJob(ScheduleBasicJob.class)
            //        .withIdentity("testJob_1", "group_1")
            //        .build();
            //ScheduleAgent.scheduler().scheduleJob(jobDetail, trigger);

            ScheduleAgent.scheduler().scheduleJob(jobDetail, trigger);

            //启动调度器
            ScheduleAgent.scheduler().start();

            Thread.sleep(20 * 1000);

            ScheduleAgent.scheduler().getPausedTriggerGroups().forEach(group -> {
                ThyLogger.logInfo("group: " + group);
            });
            ScheduleAgent.scheduler().getCurrentlyExecutingJobs().forEach(x -> {
                ThyLogger.logInfo("context: " + x);
            });

            Thread.sleep(20 * 1000);

            ThyLogger.logInfo("Reschedule to per 3s");
            //ScheduleAgent.scheduler().pauseTrigger(trigger.getKey());
            //ScheduleAgent.scheduler().unscheduleJob(trigger.getKey());

            CronTrigger triggerNew = TriggerBuilder.newTrigger()
                    .withSchedule(CronScheduleBuilder.cronSchedule("0/3 * * * * ?"))
                    .forJob("testJob_1", "group_1")
                    .build();

            ScheduleAgent.scheduler().rescheduleJob(trigger.getKey(), triggerNew);
            //ScheduleAgent.scheduler().resumeTrigger(trigger.getKey());
            //ScheduleAgent.scheduler().start();

        } catch (SchedulerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * It is very strange that this class of Job is not available as Job
     */
    static class TestScheduleJob extends ScheduleBasicJob {

        TestScheduleJob() {
            ThyLogger.logInfo("TestScheduleJob constructor");
        }

        @Override
        protected void realExecute(JobExecutionContext jobExecutionContext) {
            ThyLogger.logInfo("Work for " + readData("k1"));
        }
    }
}
