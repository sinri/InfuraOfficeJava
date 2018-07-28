package InfuraOffice.WebAgent.context.Management;

import InfuraOffice.DataCenter.DataCenter;
import InfuraOffice.DataEntity.PasswordHasher;
import InfuraOffice.DataEntity.UserEntity;
import InfuraOffice.WebAgent.ExtendedHttpHandler;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class UserManageContext {
    public static class ListUserHandler extends ExtendedHttpHandler {
        @Override
        protected void realHandler() throws Exception {
            HashMap<String, UserEntity> entities = DataCenter.getSharedInstance().getUserDataCenter().getEntities();
            ArrayList<JsonElement> list = new ArrayList<>();
            entities.forEach((key, userEntity) -> {
                JsonElement jsonElement = (new JsonParser()).parse((new Gson()).toJson(userEntity));
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                jsonObject.remove("passwordHash");
                list.add(jsonObject);
            });
            registerDataProperty("users", list);
            sayOK();
        }

        @Override
        protected HashSet<String> rolesRequired() {
            return adminRoleSetForRolesRequired();
        }
    }

    public static class ViewUserInfoHandler extends ExtendedHttpHandler {
        @Override
        protected void realHandler() throws Exception {
            String username = seekPost("username");
            if (username == null || username.isEmpty()) throw new Exception("username empty");
            UserEntity userEntity = DataCenter.getSharedInstance().getUserDataCenter().getEntityWithKey(username);
            if (userEntity == null) throw new Exception("target user does not exist");

            JsonElement jsonElement = (new JsonParser()).parse((new Gson()).toJson(userEntity));
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            jsonObject.remove("passwordHash");
            sayOK(jsonElement);
        }

        @Override
        protected HashSet<String> rolesRequired() {
            return adminRoleSetForRolesRequired();
        }
    }

    public static class UpdateUserHandler extends ExtendedHttpHandler {
        @Override
        protected void realHandler() throws Exception {
            String username = seekPost("username");
            if (username == null || username.isEmpty()) throw new Exception("username empty");
            String act = seekPost("act", "update");
            String password = seekPost("password");

            UserEntity userEntity = DataCenter.getSharedInstance().getUserDataCenter().getEntityWithKey(username);

            switch (act) {
                case "delete":
                    if (userEntity == null) throw new Exception("target user does not exist");
                    DataCenter.getSharedInstance().getUserDataCenter().removeEntityWithKey(username);
                    break;
                case "update":
                    if (userEntity == null) {
                        // create new
                        userEntity = new UserEntity();
                        userEntity.username = username;
                        if (password == null || password.isEmpty()) throw new Exception("password empty");

                    }
                    if (password != null && !password.isEmpty())
                        userEntity.passwordHash = PasswordHasher.getSaltedHash(password);
                    userEntity.role = seekPost("role", UserEntity.ROLE_USER);
                    String privileges = seekPost("privileges");
                    if (privileges != null && !privileges.isEmpty()) {
                        Collections.addAll(userEntity.privileges, privileges.split(","));
                    }
                    DataCenter.getSharedInstance().getUserDataCenter().updateEntityWithKey(userEntity.username, userEntity);
                    break;
                default:
                    throw new Exception("Unknown act");
            }
            sayOK();
        }

        @Override
        protected HashSet<String> rolesRequired() {
            return adminRoleSetForRolesRequired();
        }
    }
}
