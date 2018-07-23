package InfuraOffice.RemoteAgent;

import InfuraOffice.ThyLogger;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RemoteAgent {
    private ExecutorService threadPool;
    private Random random;
    public HashMap<String, TaskEntity> taskMap;

    private RemoteAgent(int ThreadLimit) {
        this.random = new Random(new Date().getTime());
        threadPool = Executors.newFixedThreadPool(ThreadLimit);
        taskMap = new HashMap<>();
    }

    void newTask(Runnable taskHandler) {
        ThyLogger.logInfo("PoolAgent::newTask " + taskHandler);
        threadPool.execute(taskHandler);
    }

    String createTaskId() {
        String taskIndex = null;
        for (int i = 0; i < 5 && taskIndex == null; i++) {
            taskIndex = (new Date()).getTime() + "." + random.nextInt(10000);
            if (taskMap.containsKey(taskIndex)) {
                taskIndex = null;
            }
        }
        return taskIndex;
    }

    public void arisePoolRecycleJob() {
        newTask(() -> {
            ThyLogger.logInfo("POOL TASK MAP CLEAN STARTS, size: " + taskMap.size());
            Set<Map.Entry<String, TaskEntity>> entries = taskMap.entrySet();
            for (Map.Entry<String, TaskEntity> entry : entries) {
                if (
                        entry.getValue().status.equals(TaskEntity.STATUS_FETCHED)
                                ||
                                (
                                        entry.getValue().status.equals(TaskEntity.STATUS_FINISHED)
                                                && entry.getValue().doneTime < (new Date()).getTime() - 60 * 10
                                )
                                ||
                                entry.getValue().doneTime < (new Date()).getTime() - 60 * 60
                        ) {
                    taskMap.remove(entry.getKey());
                }
            }
            ThyLogger.logInfo("POOL TASK MAP CLEAN ENDS, size: " + taskMap.size());
        });
    }

    private static RemoteAgent instance;

    public static void initializeSharedInstance(int ThreadLimit) {
        if (null == instance) {
            instance = new RemoteAgent(ThreadLimit);
        }
    }

    public static RemoteAgent sharedInstance() {
        return instance;
    }
}
