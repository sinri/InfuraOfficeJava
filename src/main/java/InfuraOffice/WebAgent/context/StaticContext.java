package InfuraOffice.WebAgent.context;

import InfuraOffice.WebAgent.ExtendedHttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class StaticContext {
    public static class ResourceHandler extends ExtendedHttpHandler {
        public void handle(HttpExchange httpExchange) throws IOException {
            super.handle(httpExchange);
            showFrontendResourceFile(httpExchange.getRequestURI().getPath());
        }
    }

    public static class PageHandler extends ExtendedHttpHandler {
        public void handle(HttpExchange httpExchange) throws IOException {
            super.handle(httpExchange);
            showFrontendPage(httpExchange.getRequestURI().getPath());
        }
    }
}
