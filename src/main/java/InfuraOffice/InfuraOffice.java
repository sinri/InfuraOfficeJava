package InfuraOffice;

import InfuraOffice.RemoteAgent.RemoteAgent;
import InfuraOffice.WebAgent.WebAgent;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InfuraOffice {

    protected static InfuraOfficeConfig config;

    public static InfuraOfficeConfig getConfig() {
        return config;
    }

    public static void main(String[] argv) {
        try {
            if (argv.length > 0) {
                // load config
                loadConfigFile(argv[0]);
            } else {
                loadDefaultConfig();
            }

            // set up daemon
            RemoteAgent.initializeSharedInstance(config.remoteAgentMaxWorker);
            // TODO set up schedule
            // set up HTTP service
            WebAgent.listen(config.httpListenPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadDefaultConfig() {
        // load the default
        config = new InfuraOfficeConfig();
    }

    private static void loadConfigFile(String configFile) throws FileNotFoundException {
        // read the config file
        config = (new Gson()).fromJson((new FileReader(configFile)), InfuraOfficeConfig.class);
    }
}
