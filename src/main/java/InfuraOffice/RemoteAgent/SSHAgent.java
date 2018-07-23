package InfuraOffice.RemoteAgent;

import InfuraOffice.InfuraOffice;
import InfuraOffice.ThyLogger;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class SSHAgent {
    String output;

    int executeCommandOnRemote(String host, int port, String user, String command) throws JSchException, IOException {
        String connectTag = "[" + user + "@" + host + ":" + port + "]";

        JSch jsch = new JSch();
        jsch.addIdentity(InfuraOffice.getConfig().privateKeyPath);

        Session session = jsch.getSession(user, host, port);
        session.setConfig("StrictHostKeyChecking", "no");

        session.connect(5000);

        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        InputStream in = channelExec.getInputStream();
        channelExec.setCommand(command);
        channelExec.setErrStream(System.err);
        channelExec.connect();

        ThyLogger.logDebug("Connected to " + connectTag);

        output = IOUtils.toString(in, "UTF-8");

        ThyLogger.logDebug("Fetched Output for Command [" + command + "] from " + connectTag + ":\n" + output);

        while (!channelExec.isClosed()) {
            try {
                Thread.sleep(1000);
                ThyLogger.logInfo("Waiting for channel of " + connectTag + " to close...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int returnValue = channelExec.getExitStatus();

        ThyLogger.logDebug("Fetched Exit Code for Command [" + command + "] from " + connectTag + ": " + returnValue);

        channelExec.disconnect();
        session.disconnect();

        return returnValue;
    }

}
