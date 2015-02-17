package rsalesc.shelper.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import org.jdesktop.swingx.VerticalLayout;
import rsalesc.shelper.tasks.Task;

import javax.swing.*;

/**
 * Panel for task configuration.
 */
public class TaskSettingsComponent extends JPanel {
    private JTextField taskName = null;
    private JTextField problemName = null;
    private JTextField contestName = null;
    private JTextField taskPath = null;
    private JButton manageTestsBtn = null;
    private Task task = null;

    private Project project;

    public TaskSettingsComponent(Project project){
        this(project, Task.emptyTask(project));
    }

    public TaskSettingsComponent(Project project, Task task) {
        super(new VerticalLayout());
        this.project = project;
        setTask(task);
    }

    public Task getTask() {
        Task t = new Task(project, taskName.getText(), problemName.getText(), contestName.getText(), taskPath.getText());

        return t;
    }

    public void setTask(Task task) {
        removeAll();
        taskName = new JTextField(task.taskName);
        problemName = new JTextField(task.problemName);
        contestName = new JTextField(task.contestName);
        taskPath = new JTextField(task.taskPath);
        manageTestsBtn = new JButton("Manage Tests");

        this.task = task;

        add(LabeledComponent.create(taskName, "Task name"));
        add(LabeledComponent.create(problemName, "Problem name"));
        add(LabeledComponent.create(contestName, "Contest name"));
        add(LabeledComponent.create(taskPath, "Task path"));
        add(manageTestsBtn);
    }


}
