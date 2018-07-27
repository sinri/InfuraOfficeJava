package InfuraOffice.WebAgent.context;

import InfuraOffice.WebAgent.ExtendedHttpHandler;

import java.io.IOException;

public class HomeContext {
    public static class RootPageHandler extends ExtendedHttpHandler {
        @Override
        protected boolean validSessionRequired() {
            return false;
        }

        @Override
        protected void realHandler() throws IOException {
            redirect("page/index.html", 302);
        }
    }
}
