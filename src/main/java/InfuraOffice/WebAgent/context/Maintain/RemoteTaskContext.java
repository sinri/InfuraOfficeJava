package InfuraOffice.WebAgent.context.Maintain;

import InfuraOffice.RemoteAgent.RemoteAgent;
import InfuraOffice.RemoteAgent.TaskEntity;
import InfuraOffice.WebAgent.ExtendedHttpHandler;

public class RemoteTaskContext {
    public static class RequireCommandTaskOnServersHandler extends ExtendedHttpHandler {

        @Override
        protected void realHandler() throws Exception {
            String taskIndex = seekPost("taskIndex", "");
            TaskEntity taskEntity = RemoteAgent.sharedInstance().readTask(taskIndex);
            registerDataProperty("status", taskEntity.status);
            registerDataProperty("index", taskEntity.index);
            registerDataProperty("return", taskEntity.returnValue);
            registerDataProperty("output", taskEntity.output);
            registerDataProperty("done_time", taskEntity.doneTime);
            sayOK();
        }
    }
}
