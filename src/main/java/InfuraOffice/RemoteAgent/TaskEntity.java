package InfuraOffice.RemoteAgent;

import InfuraOffice.ThyLogger;

import java.util.Date;
import java.util.Map;

public class TaskEntity {
    public static final String STATUS_NOT_EXIST = "NOT_EXIST";
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_EXECUTING = "EXECUTING";
    public static final String STATUS_FINISHED = "FINISHED";
    public static final String STATUS_FETCHED = "FETCHED";

    public String status;
    public String index;

    public String output;
    public int returnValue;

    public long doneTime;

    Runnable handler;

    private TaskEntity() {
        this.status = TaskEntity.STATUS_NOT_EXIST;
        this.index = "";
        this.doneTime = -1;
        this.output = "";
    }

    public static TaskEntity createTask(String type, Map<String, String> parameter) {
        try {
            ThyLogger.logInfo("TaskEntity::createTask for type: " + type);
            TaskEntity task = new TaskEntity();
            switch (type) {
                case "command":
                    String ip = parameter.get("ip");
                    String user = parameter.get("user");
                    int port = Integer.parseInt(parameter.getOrDefault("port", "22"));
                    String command = parameter.get("command");
                    if (ip == null) {
                        throw new Exception("ip empty");
                    }
                    if (user == null) {
                        throw new Exception("user empty");
                    }
                    if (command == null) {
                        throw new Exception("command empty");
                    }

                    task.index = RemoteAgent.sharedInstance().createTaskId();
                    if (task.index == null) {
                        throw new Exception("Cannot create task id");
                    }
                    task.handler = () -> {
                        try {
                            ThyLogger.logInfo("TASK [" + task.index + "] STARTS");
                            // run ssh
                            task.status = TaskEntity.STATUS_EXECUTING;
                            RemoteAgent.sharedInstance().taskMap.put(task.index, task);

                            String output;
                            task.returnValue = -1;
                            try {
                                SSHAgent agent = new SSHAgent();
                                task.returnValue = agent.executeCommandOnRemote(ip, port, user, command);
                                output = agent.output;
                            } catch (Exception e) {
                                e.printStackTrace();
                                output = e.getMessage();
                            }

                            task.output = output;
                            task.status = TaskEntity.STATUS_FINISHED;

                            RemoteAgent.sharedInstance().taskMap.put(task.index, task);
                            ThyLogger.logInfo("PoolAgent.taskMap counts " + RemoteAgent.sharedInstance().taskMap.size());

                            ThyLogger.logInfo("TASK [" + task.index + "] ENDS");
                        } catch (Exception e) {
                            e.printStackTrace();
                            ThyLogger.logError("TASK [" + task.index + "] ERROR: " + e.getMessage());
                        }
                    };
                    task.status = TaskEntity.STATUS_PENDING;
                    task.doneTime = (new Date()).getTime();
                    RemoteAgent.sharedInstance().taskMap.put(task.index, task);
                    break;
                default:
                    throw new Exception("unknown type");
            }

            RemoteAgent.sharedInstance().newTask(task.handler);

            ThyLogger.logInfo("Task created: " + task);

            return task;
        } catch (Exception e) {
            e.printStackTrace();
            ThyLogger.logError("TaskEntity::createTask Exception: " + e.getMessage());
            return null;
        }
    }


}
