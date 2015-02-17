package rsalesc.shelper.tasks;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rsalesc.shelper.ui.TaskSettingsComponent;
import rsalesc.shelper.utils.SHelperException;

import javax.swing.*;

/**
 * Created by rsalesc on 13/12/14.
 */
public class TaskConfiguration extends RunConfigurationBase {
    public Task task;
    public Project project;

    public TaskConfiguration(Project project, ConfigurationFactory factory, Task task){
        super(project, factory, task.taskName);
        this.task = task;
        this.project = project;
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new SettingsEditor<TaskConfiguration>(){
            private TaskSettingsComponent component = new TaskSettingsComponent(getProject());

            @Override protected void resetEditorFrom(TaskConfiguration s){
                component.setTask(s.task);
            }

            @Override
            protected void applyEditorTo(TaskConfiguration s){
                s.task = component.getTask();
                setName(s.task.taskName);
            }

            @Override
            protected JComponent createEditor() { return component;}
        };
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {

    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
        throw new SHelperException("This method isn't supposed to be used");
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        super.readExternal(element);
        task = new Task(project,
                element.getAttribute("task_name").getValue(),
                element.getAttribute("problem_name").getValue(),
                element.getAttribute("contest_name").getValue(),
                element.getAttribute("task_path").getValue()
        );
        //task.testsFromString(element.getAttribute("tests").getValue());
        setName(task.taskName);
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        element.setAttribute("task_name", task.taskName);
        element.setAttribute("problem_name", task.problemName);
        element.setAttribute("contest_name", task.contestName);
        element.setAttribute("task_path", task.taskPath);
        //element.setAttribute("tests", task.testsToString());
        super.writeExternal(element);
    }
}
