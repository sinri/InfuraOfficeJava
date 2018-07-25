package InfuraOffice;

import InfuraOffice.DataCenter.DataCenter;
import InfuraOffice.RemoteAgent.RemoteAgent;
import InfuraOffice.WebAgent.WebAgent;

import java.io.IOException;

public class InfuraOffice {

    /**
     * @return InfuraOfficeConfig
     * @deprecated
     */
    public static InfuraOfficeConfig getConfig() {
        return InfuraOfficeConfig.getSharedInstance();
    }

    public static void main(String[] argv) {
        try {
            if (argv.length > 0) {
                // load instance
                InfuraOfficeConfig.loadConfigFile(argv[0]);
            } else {
                InfuraOfficeConfig.loadDefaultConfig();
            }

            // load DataCenter
            DataCenter.getSharedInstance().loadFromFile();

            // set up daemon
            RemoteAgent.initializeSharedInstance(InfuraOfficeConfig.getSharedInstance().remoteAgentMaxWorker);
            // TODO set up schedule
            // set up HTTP service
            WebAgent.listen(InfuraOfficeConfig.getSharedInstance().httpListenPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
