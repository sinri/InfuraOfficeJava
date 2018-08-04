package InfuraOffice.RemoteAgent;

import InfuraOffice.DataEntity.ServerEntity;
import InfuraOffice.ThyLogger;
import com.jcraft.jsch.JSchException;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
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

    public static TaskEntity createTaskToExecuteCommandOnOneServer(ServerEntity serverEntity, String command) {
        try {
            ThyLogger.logInfo("TaskEntity::createTaskToExecuteCommandOnOneServer for " + serverEntity.serverName);
            TaskEntity task = new TaskEntity();

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
                        task.returnValue = agent.executeCommandOnRemote(serverEntity.connectAddress, serverEntity.sshPort, serverEntity.sshUser, command);
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
            RemoteAgent.sharedInstance().newTask(task.handler);

            ThyLogger.logInfo("Task created: " + task);

            return task;
        } catch (Exception e) {
            e.printStackTrace();
            ThyLogger.logError("TaskEntity::createTaskToExecuteCommandOnOneServer Exception: " + e.getMessage());
            return null;
        }
    }

    public static TaskEntity createTaskToReadLogContentOnOneServer(ServerEntity serverEntity, String logFilePath, int aroundLines, long tailLines, String keyword, boolean isCaseSensitive) {
        try {
            ThyLogger.logInfo("TaskEntity::createTaskToReadLogContentOnOneServer for " + serverEntity.serverName);
            TaskEntity task = new TaskEntity();

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

                    SSHAgent agent = new SSHAgent();

                    String command = "sudo ";
                    if (tailLines > 0) {
                        command += "tail -n " + tailLines + " \"" + StringEscapeUtils.escapeJava(logFilePath) + "\" | ";
                        command += "grep -n " + (isCaseSensitive ? "-i" : "") + " -m 2000 -C " + aroundLines + " \"" + StringEscapeUtils.escapeJava(keyword) + "\"";
                    } else {
                        command += "grep -n " + (isCaseSensitive ? "-i" : "") + " -m 2000 -C " + aroundLines + " \"" + StringEscapeUtils.escapeJava(keyword) + "\" \"" + StringEscapeUtils.escapeJava(logFilePath) + "\"";
                    }

                    task.returnValue = agent.executeCommandOnRemote(serverEntity.connectAddress, serverEntity.sshPort, serverEntity.sshUser, command);
                    task.output = agent.getOutput();
                    task.status = TaskEntity.STATUS_FINISHED;

                    RemoteAgent.sharedInstance().taskMap.put(task.index, task);
                    ThyLogger.logInfo("PoolAgent.taskMap counts " + RemoteAgent.sharedInstance().taskMap.size());

                    ThyLogger.logInfo("TASK [" + task.index + "] ENDS");
                } catch (JSchException | IOException e) {
                    e.printStackTrace();
                }
            };
            task.status = TaskEntity.STATUS_PENDING;
            task.doneTime = (new Date()).getTime();
            RemoteAgent.sharedInstance().taskMap.put(task.index, task);
            RemoteAgent.sharedInstance().newTask(task.handler);

            ThyLogger.logInfo("Task created: " + task);

            return task;
        } catch (Exception e) {
            e.printStackTrace();
            ThyLogger.logError("TaskEntity::createTaskToReadLogContentOnOneServer Exception: " + e.getMessage());
            return null;
        }
    }

    /**
     * @param type
     * @param parameter
     * @return
     * @deprecated
     */
    public static TaskEntity createTask(String type, Map<String, String> parameter) {
        try {
            ThyLogger.logInfo("TaskEntity::createTask for type: " + type);
            TaskEntity task = new TaskEntity();
            switch (type) {
                case "command": {
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
                }
                break;
                case "read_partial_file_content": {
                    String ip = parameter.get("ip");
                    String user = parameter.get("user");
                    int port = Integer.parseInt(parameter.getOrDefault("port", "22"));

                    String filePath = parameter.get("filePath");
                    String rangeStart = parameter.get("rangeStart");
                    String rangeEnd = parameter.get("rangeEnd");
                    String aroundLines = parameter.get("aroundLines");
                    String isCaseSensitive = parameter.get("isCaseSensitive");
                    String keyword = parameter.get("keyword");

                    if (ip == null) {
                        throw new Exception("ip empty");
                    }
                    if (user == null) {
                        throw new Exception("user empty");
                    }
                    if (filePath == null) {
                        throw new Exception("filePath empty");
                    }
                    if (rangeStart == null) {
                        rangeStart = "0";
                    }
                    if (rangeEnd == null) {
                        rangeEnd = "0";
                    }
                    if (aroundLines == null) {
                        aroundLines = "0";
                    }
                    if (isCaseSensitive == null) {
                        isCaseSensitive = "NO";
                    }
                    if (keyword == null) {
                        keyword = "";
                    }

                    task.index = RemoteAgent.sharedInstance().createTaskId();
                    if (task.index == null) {
                        throw new Exception("Cannot create task id");
                    }
                    String finalRangeStart = rangeStart;
                    String finalRangeEnd = rangeEnd;
                    String finalAroundLines = aroundLines;
                    String finalIsCaseSensitive = isCaseSensitive;
                    String finalKeyword = keyword;
                    task.handler = () -> {
                        try {
                            ThyLogger.logInfo("TASK [" + task.index + "] STARTS");
                            // run ssh
                            task.status = TaskEntity.STATUS_EXECUTING;
                            RemoteAgent.sharedInstance().taskMap.put(task.index, task);

                            SSHAgent agent = new SSHAgent();

                            // 1. fetch total lines

                            String commandForLines = "lines=`sudo wc -l \"" + StringEscapeUtils.escapeJava(filePath) + "\" | awk '{print $1}'`;";
                            agent.executeCommandOnRemote(ip, port, user, commandForLines);
                            int total_lines = Integer.parseInt(agent.output);
                            ThyLogger.logInfo("FILE [" + filePath + "] contains " + total_lines + " lines");

                            // 2. grep

                            int range_start = Integer.parseInt(finalRangeStart);
                            if (range_start < 0) {
                                range_start = total_lines + range_start;
                            }
                            int range_end = Integer.parseInt(finalRangeEnd);
                            if (range_end < 0) {
                                range_end = total_lines + range_end;
                            }
                            int around_lines = Integer.parseInt(finalAroundLines);

                            String command = "sudo cat -n '" + StringEscapeUtils.escapeJava(filePath)
                                    + "' | awk '{if($i>=" + range_start + " && $i<=" + range_end + ") print $0}'"
                                    + " | grep -C " + around_lines + (finalIsCaseSensitive.equals("YES") ? " -i " : " ") + " -m 2000 '" + StringEscapeUtils.escapeJava(finalKeyword) + "'";
                            task.returnValue = agent.executeCommandOnRemote(ip, port, user, command);
                            task.output = agent.getOutput();
                            task.status = TaskEntity.STATUS_FINISHED;

                            RemoteAgent.sharedInstance().taskMap.put(task.index, task);
                            ThyLogger.logInfo("PoolAgent.taskMap counts " + RemoteAgent.sharedInstance().taskMap.size());

                            ThyLogger.logInfo("TASK [" + task.index + "] ENDS");
                        } catch (JSchException | IOException e) {
                            e.printStackTrace();
                        }
                    };
                    task.status = TaskEntity.STATUS_PENDING;
                    task.doneTime = (new Date()).getTime();
                    RemoteAgent.sharedInstance().taskMap.put(task.index, task);
                }
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
