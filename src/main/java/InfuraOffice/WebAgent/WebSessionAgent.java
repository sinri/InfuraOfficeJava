package InfuraOffice.WebAgent;

import InfuraOffice.DataCenter.DataCenter;
import InfuraOffice.DataEntity.PasswordHasher;
import InfuraOffice.DataEntity.UserEntity;

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

    public static class WebSessionEntity {
        public String username;
        public long expireTimestamp;
        public String token;


        public UserEntity getCurrentUser() {
            return DataCenter.getSharedInstance().getUserDataCenter().getEntityWithKey(username);
        }
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

    public WebSessionEntity createSession(UserEntity userEntity) throws Exception {
        WebSessionEntity webSessionEntity = new WebSessionEntity();
        webSessionEntity.token = PasswordHasher.md5(userEntity.username + "_" + (new Date()).getTime()) + (int) Math.rint(Math.random() * 1000);
        webSessionEntity.username = userEntity.username;
        webSessionEntity.expireTimestamp = (new Date().getTime()) + 3600 * 8;

        sessions.put(webSessionEntity.token, webSessionEntity);

        return webSessionEntity;
    }
}
