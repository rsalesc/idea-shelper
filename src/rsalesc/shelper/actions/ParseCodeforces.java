package rsalesc.shelper.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import rsalesc.shelper.generator.CodeforcesParser;
import rsalesc.shelper.tasks.Task;
import rsalesc.shelper.tasks.TaskRunner;
import rsalesc.shelper.ui.CodeforcesParserDialog;
import rsalesc.shelper.utils.CMakeUtils;
import rsalesc.shelper.utils.SHelperException;
import rsalesc.shelper.utils.Utilities;

/**
 * Created by rsalesc on 11/12/14.
 */
public class ParseCodeforces extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Project project = e.getProject();
        if(project == null)
            throw new SHelperException("Project not found");

        CodeforcesParserDialog dialog = new CodeforcesParserDialog(project);
        dialog.show();
        if(dialog.isOK()) {
            Task[] tasks;
            int contestCode = dialog.getContestId();
            try {
                tasks = new CodeforcesParser(contestCode, Utilities.normalizePath(dialog.getContestBaseDir())).parse(e.getProject());
                for (Task task : tasks) {
                    Utilities.log("Saving " + task.problemName);
                    task.save(e.getData(PlatformDataKeys.PROJECT));
                    TaskRunner.generateOJFile(e.getProject(), task);
                }
                TaskRunner.generateDummyRunner(e.getProject());
                try {
                    CMakeUtils.update(project);
                    CMakeUtils.reload(project);
                } catch (Exception ex) {
                    SHelperException exx = new SHelperException("CMakeLists could not be updated correctly", ex);
                    Utilities.logException(exx);
                    throw exx;
                }
            } catch (Exception ex) {
                SHelperException exx = new SHelperException("The contest could not be parsed", ex);
                Utilities.logException(exx);
                throw exx;
            }
        }
    }
}
