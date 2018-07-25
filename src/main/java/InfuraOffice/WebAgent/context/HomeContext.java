package InfuraOffice.WebAgent.context;

import InfuraOffice.WebAgent.ExtendedHttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class HomeContext {
    public static class RootPageHandler extends ExtendedHttpHandler {
        public void handle(HttpExchange httpExchange) throws IOException {
            super.handle(httpExchange);
            redirect("page/index.html", 302);
        }
    }
}
