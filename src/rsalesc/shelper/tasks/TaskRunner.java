package rsalesc.shelper.tasks;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.DefaultProgramRunner;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rsalesc.shelper.components.Configurator;
import rsalesc.shelper.tester.CppCompile;
import rsalesc.shelper.utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by rsalesc on 14/12/14.
 */
public class TaskRunner extends DefaultProgramRunner {

    public final static String RUN_CONFIGURATION_NAME = "TestRunner";
    public final static String RUNNER_PATH = "runner.cpp";
    public final static String OJ_PATH = "oj/";

    @NotNull
    @Override
    public String getRunnerId() {
        return "rsalesc.shelper.tasks.TaskRunner";
    }

    @Override
    public boolean canRun(@NotNull String s, @NotNull RunProfile runProfile) {
        return runProfile instanceof TaskConfiguration;
    }

    @Override
    public void execute(@NotNull ExecutionEnvironment env, @Nullable Callback callback) throws ExecutionException {
        Project project = env.getProject();

        TaskConfiguration taskConfig = (TaskConfiguration) env.getRunProfile();

        try {
            generateRunnerFile(project, taskConfig.task);
            generateOJFile(project, taskConfig.task);
        }catch(Exception ex){
            ex.printStackTrace();
            throw new SHelperException("Processed cpp files couldn't be created", ex);
        }

        RunnerAndConfigurationSettings setting = setupTestRunner(env);
        // execute
        // ProgramRunnerUtil.executeConfiguration(project, setting, env.getExecutor());
        VirtualFile base = project.getBaseDir();
        VirtualFile taskFile = base.findFileByRelativePath(taskConfig.task.getTaskRelativePath());
        if(taskFile == null) throw new SHelperException("Task could not be found");
        VirtualFile taskDir = taskFile.getParent();
        VirtualFile ojDir = FileUtils.findOrCreateByRelativePath(taskDir, OJ_PATH);
        VirtualFile ojFile = FileUtils.findOrCreateByRelativePath(ojDir, taskFile.getName());

        CppCompile ojCompile = new CppCompile(ojFile);
        ojCompile.compile();

        ConsoleView console = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
        console.print("CARALHOOOO", ConsoleViewContentType.NORMAL_OUTPUT);
        console.print("ERRO", ConsoleViewContentType.ERROR_OUTPUT);
        console.scrollTo(10);
    }

    public RunnerAndConfigurationSettings setupTestRunner(ExecutionEnvironment env){
        RunManager manager = RunManager.getInstance(env.getProject());
        List<RunnerAndConfigurationSettings> settings = manager.getAllSettings();
        RunnerAndConfigurationSettings output = null;
        for(RunnerAndConfigurationSettings configuration : settings){
            if(configuration.getName().equals(RUN_CONFIGURATION_NAME))
                output = configuration;
        }

        return output;
    }

    public static void generateRunnerFile(Project project, Task task) throws Exception{
        VirtualFile base = project.getBaseDir();
        VirtualFile runnerFile = FileUtils.findOrCreateByRelativePath(base, RUNNER_PATH);
        Document doc = FileDocumentManager.getInstance().getDocument(runnerFile);
        FileUtils.setDocumentText(project, doc, task.replaceTaskTemplate(TemplateUtils.getRunnerTemplate()));
    }

    public static void generateOJFile(Project project, Task task) throws Exception{
        VirtualFile base = project.getBaseDir();
        VirtualFile taskFile = base.findFileByRelativePath(task.getTaskRelativePath());
        if(taskFile == null) throw new SHelperException("Task could not be found");
        VirtualFile taskDir = taskFile.getParent();
        VirtualFile ojDir = FileUtils.findOrCreateByRelativePath(taskDir, OJ_PATH);
        VirtualFile ojFile = FileUtils.findOrCreateByRelativePath(ojDir, taskFile.getName());

        Document doc = FileDocumentManager.getInstance().getDocument(ojFile);
        String txt = task.replaceCompleteTemplate(TemplateUtils.getOJTemplate());
        FileUtils.setDocumentText(project, doc, txt);
        Configurator.State state = Utilities.getAppState();
        if(state == null)
            throw new SHelperException("Application state could not be established");
        if(state.isRemoveUnused()) removeUnusedCode(PsiManager.getInstance(project).findFile(ojFile));
    }

    public static void generateDummyRunner(Project project) throws Exception{
        VirtualFile base = project.getBaseDir();
        VirtualFile dummyFile = FileUtils.findOrCreateByRelativePath(base, RUNNER_PATH);
        Document doc = FileDocumentManager.getInstance().getDocument(dummyFile);
        FileUtils.setDocumentText(project, doc, "");
    }

    private static void removeUnusedCode(PsiFile file) {
        while (true) {
            final Collection<PsiElement> toDelete = new ArrayList<PsiElement>();
            Project project = file.getProject();
            SearchScope scope = new GlobalSearchScope.FilesScope(
                    project,
                    Collections.singletonList(file.getVirtualFile())
            );
            file.acceptChildren(new DeletionMarkingVisitor(toDelete, scope));
            if (toDelete.isEmpty()) {
                break;
            }
            new WriteCommandAction.Simple<Object>(project, file) {
                @Override
                public void run() {
                    for (PsiElement element : toDelete) {
                        element.delete();
                    }
                }
            }.execute();
        }
    }
}
