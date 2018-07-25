package InfuraOffice.WebAgent;

import java.util.Date;
import java.util.HashMap;

public class WebSessionAgent {
    private static WebSessionAgent instance;

    static {
        instance = new WebSessionAgent();
    }

    public static WebSessionAgent getSharedInstance() {
        return instance;
    }

    static class WebSessionEntity {
        public String username;
        public long expireTimestamp;
        public String token;
    }

    private HashMap<String, WebSessionEntity> sessions;// token -> WebSessionEntity

    protected WebSessionAgent() {
        sessions = new HashMap<>();
    }

    public WebSessionEntity validateSession(String token) {
        if (!sessions.containsKey(token)) return null;
        WebSessionEntity webSessionEntity = sessions.get(token);
        if (webSessionEntity.expireTimestamp < (new Date()).getTime()) {
            sessions.remove(token);
            return null;
        }
        return webSessionEntity;
    }
}
