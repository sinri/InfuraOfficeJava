package InfuraOffice.WebAgent.context;

import InfuraOffice.WebAgent.ExtendedHttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class LoginContext {
    public static class LoginHandler extends ExtendedHttpHandler {
        public void handle(HttpExchange httpExchange) throws IOException {
            super.handle(httpExchange);

            String username = seekPost("username");
            String password = seekPost("password");


        }
    }
}
