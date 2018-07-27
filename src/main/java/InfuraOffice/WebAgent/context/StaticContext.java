package InfuraOffice.WebAgent.context;

import InfuraOffice.WebAgent.ExtendedHttpHandler;

public class StaticContext {
    public static class ResourceHandler extends ExtendedHttpHandler {
        @Override
        protected boolean validSessionRequired() {
            return false;
        }

        @Override
        protected void realHandler() throws Exception {
            showFrontendResourceFile(httpExchange.getRequestURI().getPath());
        }
    }

    public static class PageHandler extends ExtendedHttpHandler {
        @Override
        protected boolean validSessionRequired() {
            return false;
        }

        @Override
        protected void realHandler() throws Exception {
            showFrontendPage(httpExchange.getRequestURI().getPath());
        }
    }
}
