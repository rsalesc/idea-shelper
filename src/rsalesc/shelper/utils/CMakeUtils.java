package rsalesc.shelper.utils;

import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.jetbrains.cidr.cpp.cmake.workspace.CMakeWorkspace;
import rsalesc.shelper.tasks.Task;
import rsalesc.shelper.tasks.TaskConfiguration;

import java.util.ArrayList;

/**
 * Created by rsalesc on 20/01/15.
 */
public class CMakeUtils {
    public final static String PROJECT_WC = "%%PROJECT_NAME%%";
    public final static String HIGHLIGHT_WC = "%%TASKS_FILES%%";
    public final static String OJ_HIGHLIGHT_WC = "%%OJ_TASKS_FILES%%";
    public final static String CXX_FLAGS_WC = "%%CXX_FLAGS%%";

    public static void update(Project project) throws Exception {
        String highlight = "";
        String oj_highlight = "";
        ArrayList<Task> tasks = new ArrayList<Task>();
        // loop over runconfigurations and get tasks
        RunManager runManager = RunManagerImpl.getInstance(project);
        for(RunConfiguration runConfig : runManager.getAllConfigurationsList()){
            if(runConfig instanceof TaskConfiguration){
                TaskConfiguration taskConfig = (TaskConfiguration)runConfig;
                tasks.add(taskConfig.task);
            }
        }
        int i = 0;
        for(Task t : tasks){
            if(i++ > 0){
                highlight += " ";
                oj_highlight += " ";
            }
            highlight += t.getTaskRelativePath();
            oj_highlight += t.getOJRelativePath();
        }
        String temp = TemplateUtils.getCMakeTemplate();
        VirtualFile baseDir = project.getBaseDir();
        VirtualFile list = FileUtils.findOrCreateByRelativePath(baseDir, "CMakeLists.txt");
        Document doc = FileDocumentManager.getInstance().getDocument(list);
        String res = temp.replace(CXX_FLAGS_WC, "").replace(HIGHLIGHT_WC, highlight).replace(OJ_HIGHLIGHT_WC, oj_highlight);
        res = res.replace(PROJECT_WC, project.getName());


        FileUtils.setDocumentText(project, doc, res);
    }
    public static void reload(Project project){
        CMakeWorkspace.getInstance(project).scheduleReload(true);
    }
}
