package rsalesc.shelper.components;

import com.intellij.openapi.components.*;
import com.intellij.util.containers.HashMap;
import org.jetbrains.annotations.NotNull;
import rsalesc.shelper.tasks.Test;

import java.util.ArrayList;
import java.util.Map;

@com.intellij.openapi.components.State(
        name = "ProjectConfigurator",
        storages = {
                @Storage(id = "default", file = StoragePathMacros.PROJECT_FILE, scheme = StorageScheme.DEFAULT),
                @Storage(id = "dir",
                        file = StoragePathMacros.PROJECT_CONFIG_DIR + "/SHelper.xml",
                        scheme = StorageScheme.DIRECTORY_BASED)
        }
)

/**
 * Created by rsalesc on 21/01/15.
 */
public class ProjectConfigurator implements ProjectComponent, PersistentStateComponent<ProjectConfigurator.State> {

    private ProjectConfigurator.State state;

    public ProjectConfigurator(){
        state = new ProjectConfigurator.State();
    }

    public ProjectConfigurator.State getState(){
        return state;
    }

    public void loadState(ProjectConfigurator.State newState){
        state = newState;
    }

    @Override
    public void projectOpened() {
        state.clearEmptyTasks();
    }

    @Override
    public void projectClosed() {

    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "ProjectConfigurator";
    }

    public static class State{
        private HashMap<String, Test[]> testMap;

        public State(){
            setTestMap(new HashMap<String, Test[]>());
        }

        public HashMap<String, Test[]> getTestMap() {
            return testMap;
        }

        public void setTestMap(HashMap<String, Test[]> testMap) {
            this.testMap = testMap;
        }

        public Test[] getTaskTests(String taskName){
            if(!testMap.containsKey(taskName)) return new Test[0];
            Test[] tests = testMap.get(taskName);
            return tests;
        }

        public void setTaskTests(String taskName, Test[] tests){
            if(testMap.containsKey(taskName)) testMap.remove(taskName);
            testMap.put(taskName, tests);
        }

        public void clearEmptyTasks(){
            ArrayList<String> toDelete = new ArrayList<String>();
            for(Map.Entry<String, Test[]> entry : testMap.entrySet()){
                if(entry.getValue().length == 0)
                    toDelete.add(entry.getKey());
            }

            for(String key : toDelete)
                testMap.remove(key);
        }
    }
}
