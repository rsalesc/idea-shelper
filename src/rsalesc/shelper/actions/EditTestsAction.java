package rsalesc.shelper.actions;

import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import rsalesc.shelper.components.ProjectConfigurator;
import rsalesc.shelper.tasks.Task;
import rsalesc.shelper.tasks.TaskConfiguration;
import rsalesc.shelper.tasks.Test;
import rsalesc.shelper.ui.EditTestsDialog;
import rsalesc.shelper.utils.SHelperException;

/**
 * Created by rsalesc on 20/01/15.
 */
public class EditTestsAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            throw new SHelperException("No project found");
        }

        /*ProjectConfigurator.State states = Utilities.getProjectState(project);

        for(Map.Entry<String, Test[]> entry : states.getTestMap().entrySet()){
            //Utilities.log(entry.getKey());
            System.out.println(entry.getKey());
            System.out.println(entry.getValue().length);
        }*/

        RunnerAndConfigurationSettings selectedConfiguration =
                RunManagerImpl.getInstanceImpl(project).getSelectedConfiguration();
        if (selectedConfiguration == null)
            return;
        RunConfiguration configuration = selectedConfiguration.getConfiguration();
        if (configuration instanceof TaskConfiguration) {
            TaskConfiguration taskConfiguration = (TaskConfiguration) configuration;
            Task task = taskConfiguration.task;

            ProjectConfigurator configurator = project.getComponent(ProjectConfigurator.class);
            ProjectConfigurator.State state = configurator.getState();

            if(state == null) throw new SHelperException("Project state could not be established");

            Test[] tests = state.getTaskTests(task.taskName);

            EditTestsDialog dialog = new EditTestsDialog(project, task, tests);
            dialog.show();
            if(dialog.isOK()){
                Test[] newTests = dialog.getTests();
                state.setTaskTests(task.taskName, newTests);
                configurator.loadState(state);
            }
        }
    }
}
