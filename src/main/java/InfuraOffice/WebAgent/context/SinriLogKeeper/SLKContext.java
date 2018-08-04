package InfuraOffice.WebAgent.context.SinriLogKeeper;

import InfuraOffice.DataCenter.DataCenter;
import InfuraOffice.DataEntity.ServerEntity;
import InfuraOffice.WebAgent.ExtendedHttpHandler;
import org.apache.commons.text.StringEscapeUtils;

public class SLKContext {
    public static class FetchLogFileListTaskHandler extends ExtendedHttpHandler {

        @Override
        protected void realHandler() throws Exception {
            String serverName = seekPost("serverName", "");
            ServerEntity serverEntity = DataCenter.getSharedInstance().getServerDataCenter().getEntityWithKey(serverName);
            if (serverEntity == null) {
                throw new Exception("server not found");
            }

            StringBuilder commands = new StringBuilder();
            serverEntity.slkLogPathSet.forEach(path -> {
                if (path.trim().isEmpty()) return;
                //String command = "echo -e 'import glob\\nlist=glob.glob(\""+ StringEscapeUtils.escapeJava(path.trim())+"\")\\nfor item in list:\\n\\tprint(item)'|python -;";
                // perl -e "@files = glob('/var/log/*');foreach $files(@files){print $files;print \"\n\";}"
                String command = "perl -e \"@files = glob('" + StringEscapeUtils.escapeJava(path) + "');foreach $files(@files){if( -d $files ){}else{print $files;print \\\"\\n\\\";}}\";";
                commands.append(command);
            });

            String logs = serverEntity.instantRemoteCommandExecution(commands.toString());
            registerDataProperty("logs", logs.split("[\r\n]+"));

            //String taskIndex = serverEntity.raiseRemoteCommandExecutionTask(commands.toString());
            //registerDataProperty("taskIndex",taskIndex);
            sayOK();
        }
    }

    public static class AsyncFetchLogFileInfoTaskHandler extends ExtendedHttpHandler {

        @Override
        protected void realHandler() throws Exception {
            String serverName = seekPost("serverName", "");
            String filePath = seekPost("filePath", "");

            ServerEntity serverEntity = DataCenter.getSharedInstance().getServerDataCenter().getEntityWithKey(serverName);
            if (serverEntity == null) {
                throw new Exception("server not found");
            }
            if (filePath.isEmpty()) throw new Exception("filepath is empty");

            String commandForSize = "sudo ls -alh \"" + StringEscapeUtils.escapeJava(filePath) + "\" | awk '{print $5}';";
            String commandForLines = "sudo wc -l \"" + StringEscapeUtils.escapeJava(filePath) + "\" | awk '{print $1}';";

            String taskIndexForSize = serverEntity.raiseRemoteCommandExecutionTask(commandForSize);
            String taskIndexForLines = serverEntity.raiseRemoteCommandExecutionTask(commandForLines);

            registerDataProperty("taskIndexForSize", taskIndexForSize);
            registerDataProperty("taskIndexForLines", taskIndexForLines);
            sayOK();
        }
    }

    public static class SyncFetchLogFileSizeTaskHandler extends ExtendedHttpHandler {

        @Override
        protected void realHandler() throws Exception {
            String serverName = seekPost("serverName", "");
            String filePath = seekPost("filePath", "");

            ServerEntity serverEntity = DataCenter.getSharedInstance().getServerDataCenter().getEntityWithKey(serverName);
            if (serverEntity == null) {
                throw new Exception("server not found");
            }
            if (filePath.isEmpty()) throw new Exception("filepath is empty");

            String commandForSize = "sudo ls -alh \"" + StringEscapeUtils.escapeJava(filePath) + "\" | awk '{print $5}';";
            String lines = serverEntity.instantRemoteCommandExecution(commandForSize);
            if (lines == null) lines = "-1";
            registerDataProperty("fileSize", lines.trim());
            sayOK();
        }
    }

    public static class FetchLogContentTaskHandler extends ExtendedHttpHandler {

        @Override
        protected void realHandler() throws Exception {
            String serverName = seekPost("serverName", "");
            String filePath = seekPost("filePath", "");
            //String rangeStart=seekPost("rangeStart","");
            //String rangeEnd=seekPost("rangeEnd","");
            String tailLines = seekPost("tailLines", "10000");
            String aroundLines = seekPost("aroundLines", "10");
            String isCaseSensitive = seekPost("isCaseSensitive", "NO");
            String keyword = seekPost("keyword", "");

            ServerEntity serverEntity = DataCenter.getSharedInstance().getServerDataCenter().getEntityWithKey(serverName);
            if (serverEntity == null) {
                throw new Exception("server not found");
            }
            if (filePath.isEmpty()) throw new Exception("filepath is empty");

            String taskIndex = serverEntity.raiseReadPartialFileContentTask(filePath, Integer.parseInt(tailLines), Integer.parseInt(aroundLines), isCaseSensitive.equals("YES"), keyword);
            registerDataProperty("taskIndex", taskIndex);
            sayOK();
        }
    }
}
