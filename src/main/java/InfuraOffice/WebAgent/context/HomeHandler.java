package InfuraOffice.WebAgent.context;

import InfuraOffice.WebAgent.ExtendedHttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class HomeHandler extends ExtendedHttpHandler {
    public void handle(HttpExchange httpExchange) throws IOException {
        super.handle(httpExchange);
//        try {
//            String taskIndex = this.seekPost("task_index", "");
//            HashMap<String, Object> map = new HashMap<>();
//            sayOK(map);
//        } catch (Exception e) {
//            sayFail(e.getMessage());
//        }

        output("Welcome to InfuraOffice Java Version!", 200);
    }
}
