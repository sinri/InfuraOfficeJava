package InfuraOffice.WebAgent.context;

import InfuraOffice.DataCenter.DataCenter;
import InfuraOffice.DataEntity.UserEntity;
import InfuraOffice.WebAgent.ExtendedHttpHandler;
import InfuraOffice.WebAgent.WebSessionAgent;

public class LoginContext {
    public static class LoginHandler extends ExtendedHttpHandler {
        @Override
        protected boolean validSessionRequired() {
            return false;
        }

        @Override
        protected void realHandler() throws Exception {
            String username = seekPost("username");
            String password = seekPost("password");

            UserEntity userEntity = DataCenter.getSharedInstance().getUserDataCenter().getEntityWithKey(username);
            if (userEntity == null) {
                throw new Exception("User [" + username + "] does not exist");
            }
            if (!userEntity.validatePasswordHash(password)) {
                throw new Exception("Password Incorrect!");
            }
            // ok
            WebSessionAgent.WebSessionEntity session = WebSessionAgent.getSharedInstance().createSession(userEntity);

            // update ip
            String ip = httpExchange.getRemoteAddress().getAddress().getHostAddress();
            DataCenter.getSharedInstance().getUserDataCenter().recordUserLastLogin(username, ip);

            registerDataProperty("token", session.token);
            registerDataProperty("expireTimestamp", session.expireTimestamp);
            sayOK();
        }
    }

    public static class SessionValidateHandler extends ExtendedHttpHandler {
        @Override
        protected void realHandler() throws Exception {
            registerDataProperty("token", session.token);
            registerDataProperty("username", session.getCurrentUser().username);
            registerDataProperty("role", session.getCurrentUser().role);
            registerDataProperty("privileges", session.getCurrentUser().privileges);
            registerDataProperty("expire", session.expireTimestamp);
            sayOK();
        }
    }
}
