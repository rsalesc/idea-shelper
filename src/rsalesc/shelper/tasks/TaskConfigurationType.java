package rsalesc.shelper.tasks;

import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.openapi.util.IconLoader;

/**
 * Created by rsalesc on 13/12/14.
 */
public class TaskConfigurationType extends ConfigurationTypeBase {

    public static final String ID = "rsalesc.shelper.tasks.TaskConfigurationType";

    public TaskConfigurationType() {
        super(
                TaskConfigurationType.ID,
                "SHelper Task",
                "Task for SHelper",
                IconLoader.getIcon("/rsalesc/shelper/data/icons/taskIcon.png")
        );
        //noinspection ThisEscapedInObjectConstruction
        addFactory(new TaskConfigurationFactory(this));
    }

}
