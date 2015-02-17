package rsalesc.shelper.tasks;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by rsalesc on 13/12/14.
 */
public class TaskConfigurationFactory extends ConfigurationFactory {
    public TaskConfigurationFactory(@NotNull ConfigurationType type) {
        super(type);
    }

    @Override
    public TaskConfiguration createTemplateConfiguration(Project project) {
        return new TaskConfiguration(project, this, Task.emptyTask(project));
    }

    @Override
    public boolean isApplicable(@NotNull Project project) {
        return false;
    }
}