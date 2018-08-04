package InfuraOffice.DataEntity;

import InfuraOffice.RemoteAgent.SSHAgent;
import InfuraOffice.RemoteAgent.TaskEntity;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jcraft.jsch.JSchException;

import java.io.IOException;
import java.util.HashSet;

public class ServerEntity extends AnyEntity {
    public String serverName;
    public String connectAddress;
    public int sshPort;
    public String sshUser;
    public String platformName;
    public String assertID;
    public String areaCode;
    public HashSet<String> slkLogPathSet;

    public ServerEntity() {
        serverName = "";
        connectAddress = "";
        sshPort = 22;
        sshUser = "admin";
        platformName = "";
        assertID = "";
        areaCode = "";
        slkLogPathSet = new HashSet<>();
    }

    @Override
    public void loadPropertiesFormJsonElement(JsonElement jsonElement) {
        JsonObject object = jsonElement.getAsJsonObject();
        serverName = object.get("serverName").getAsString();
        connectAddress = object.get("connectAddress").getAsString();
        sshPort = object.get("sshPort").getAsInt();
        sshUser = object.get("sshUser").getAsString();
        platformName = object.get("platformName").getAsString();
        assertID = object.get("assertID").getAsString();
        areaCode = object.get("areaCode").getAsString();
        slkLogPathSet = new HashSet<>();
        object.get("slkLogPathSet").getAsJsonArray().forEach(path -> slkLogPathSet.add(path.getAsString()));
    }

    public String raiseRemoteCommandExecutionTask(String command) {
//        HashMap<String, String> map = new HashMap<>();
//        map.put("ip", connectAddress);
//        map.put("user", sshUser);
//        map.put("command", command);
//        map.put("port", "" + sshPort);
        TaskEntity taskEntity = TaskEntity.createTaskToExecuteCommandOnOneServer(this, command);
        //TaskEntity.createTask("command", map);
        if (taskEntity == null) return "";
        return taskEntity.index;
    }

    public String instantRemoteCommandExecution(String command) throws IOException, JSchException {
        SSHAgent sshAgent = new SSHAgent();
        sshAgent.executeCommandOnRemote(connectAddress, sshPort, sshUser, command);
        return sshAgent.getOutput();
    }

    public String raiseReadPartialFileContentTask(String filePath, long tailLines, int aroundLines, boolean isCaseSensitive, String keyword) {
//        HashMap<String, String> map = new HashMap<>();
//        map.put("ip", connectAddress);
//        map.put("user", sshUser);
//        map.put("port", "" + sshPort);
//
//        map.put("filePath",filePath);
//        map.put("rangeStart",rangeStart+"");
//        map.put("rangeEnd",rangeEnd+"");
//        map.put("aroundLines",aroundLines+"");
//        map.put("isCaseSensitive",isCaseSensitive?"YES":"NO");
//        map.put("keyword",keyword);

        TaskEntity taskEntity = TaskEntity.createTaskToReadLogContentOnOneServer(this, filePath, aroundLines, tailLines, keyword, isCaseSensitive);
        //TaskEntity.createTask("read_partial_file_content", map);
        if (taskEntity == null) return "";
        return taskEntity.index;
    }
}
