package InfuraOffice.ScheduleAgent.CronJob;

import InfuraOffice.ThyLogger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

abstract public class ScheduleBasicJob implements org.quartz.Job {

    JobExecutionContext jobExecutionContext;

    protected abstract void realExecute(JobExecutionContext jobExecutionContext);

    protected final String readData(String key) {
        return this.jobExecutionContext.getMergedJobDataMap().getString(key);
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        this.jobExecutionContext = jobExecutionContext;
        ThyLogger.logInfo(this.getClass().getName() + " [" + jobExecutionContext.getJobDetail().getKey() + "] executed now on " + jobExecutionContext.getFireTime());
//        jobExecutionContext.getMergedJobDataMap().forEach((key,value)->{
//            ThyLogger.logInfo("data ["+key+"] = "+value);
//        });
        realExecute(jobExecutionContext);
    }
}