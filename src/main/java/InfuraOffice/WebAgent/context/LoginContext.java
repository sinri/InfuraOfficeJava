package InfuraOffice.WebAgent.context;

import InfuraOffice.DataCenter.DataCenter;
import InfuraOffice.DataEntity.UserEntity;
import InfuraOffice.WebAgent.ExtendedHttpHandler;
import InfuraOffice.WebAgent.WebSessionAgent;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class LoginContext {
    public static class LoginHandler extends ExtendedHttpHandler {
        public void handle(HttpExchange httpExchange) throws IOException {
            super.handle(httpExchange);
            try {
                String username = seekPost("username");
                String password = seekPost("password");

                UserEntity userEntity = DataCenter.getSharedInstance().getUserDataCenter().getEntityWithKey(username);
                if (userEntity == null) {
                    throw new Exception("User [" + username + "] does not exist");
                }
                if (!userEntity.validatePasswordHash(password)) {
                    throw new Exception("Password Incorrect: " + password);
                }
                // ok
                WebSessionAgent.WebSessionEntity session = WebSessionAgent.getSharedInstance().createSession(userEntity);

                // update ip
                String ip = httpExchange.getRemoteAddress().getAddress().getHostAddress();
                DataCenter.getSharedInstance().getUserDataCenter().recordUserLastLogin(username, ip);

                registerDataProperty("token", session.token);
                registerDataProperty("expireTimestamp", session.expireTimestamp);
                sayOK();
            } catch (Exception e) {
                e.printStackTrace();
                sayFail(e.getMessage());
            }
        }
    }

    public static class SessionValidateHandler extends ExtendedHttpHandler {
        public void handle(HttpExchange httpExchange) throws IOException {
            super.handle(httpExchange);
            try {
                WebSessionAgent.WebSessionEntity webSessionEntity = this.validateUserSession();

                registerDataProperty("token", webSessionEntity.token);
                registerDataProperty("username", webSessionEntity.getCurrentUser().username);
                registerDataProperty("expire", webSessionEntity.expireTimestamp);
                sayOK();
            } catch (Exception e) {
                e.printStackTrace();
                sayFail(e.getMessage());
            }
        }
    }
}
