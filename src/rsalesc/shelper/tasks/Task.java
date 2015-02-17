package rsalesc.shelper.tasks;

import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import rsalesc.shelper.components.Configurator;
import rsalesc.shelper.components.ProjectConfigurator;
import rsalesc.shelper.utils.FileUtils;
import rsalesc.shelper.utils.IncludeProcessor;
import rsalesc.shelper.utils.SHelperException;
import rsalesc.shelper.utils.Utilities;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by rsalesc on 10/12/14.
 */
public class Task implements Persistent{
    private final static String TASKNAME_WC = "%%taskname%%";
    private final static String TASKPATH_WC = "%%taskpath%%";
    private final static String TASKCLASS_WC = "%%taskclass%%";
    private final static String TASKFILE_WC = "%%taskfile%%";
    private final static String TASKCODE_WC = "%%taskcode%%";
    private final static String PAUSE_WC = "%%pause%%";
    private final static String TESTS_WC = "%%tests%%";
    private final static String COLOR_WC = "%%color%%";

    public Project project;
    public String taskName;
    public String contestName;
    public String problemName;
    public String taskPath;

    public Task(Project project, String taskName, String problemName, String contestName, String taskPath){
        this.project = project;
        this.taskName = taskName;
        this.problemName = problemName;
        this.contestName = contestName;
        this.taskPath = taskPath;
    }

    public Task(Project project, String taskName, String problemName, String taskPath){
        this(project, taskName, problemName, "", taskPath);
    }

    public static Task emptyTask(Project project){
        return new Task(project, "", "", "");
    }

    public static Task create(Project project, String taskName, String problemName, String contestName, String taskPath){
        Task res = new Task(project, taskName, problemName, contestName, taskPath);
        res.clearTests();
        return res;
    }

    public String getCode(){
        VirtualFile base = project.getBaseDir();
        VirtualFile vf = null;
        try{
            vf = base.findFileByRelativePath(getTaskRelativePath());
        }catch(Exception ex){
            ex.printStackTrace();
            throw new SHelperException("Task couldn't be found");
        }
        return FileDocumentManager.getInstance().getDocument(vf).getText();
    }

    public String getProcessedCode() {
        VirtualFile base = project.getBaseDir();
        VirtualFile vf = null;
        try{
            vf = base.findFileByRelativePath(getTaskRelativePath());
        }catch(Exception ex) {
            ex.printStackTrace();
            throw new SHelperException("Task couldn't be found");
        }
        return IncludeProcessor.process(project, PsiManager.getInstance(project).findFile(vf));
    }

    public String replaceTaskTemplate(String input){
        Configurator.State state = Utilities.getAppState();
        if(state == null)
            throw new SHelperException("Application state could not be established");
        String output = input.replace(TASKNAME_WC, taskName).replace(TASKPATH_WC, getTaskRelativePath()).replace(PAUSE_WC,"1").replace(TESTS_WC, testString()).replace(COLOR_WC, state.isColorOutput() ? "1" : "0");
        return output;
    }

    public String replaceCompleteTemplate(String input){
        String output = replaceTaskTemplate(input);
        return output.replace(TASKCODE_WC, getProcessedCode());
    }

    public String testString(){
        String output = "";
        int i = 0;
        for(Test t : getTests()){
            if(t.active) {
                if (i > 0) output += ", ";
                output += t.toRunnerStr();
                i++;
            }
        }
        return output;
    }

    public void save(Project proj){
        // debug
        // Utilities.log(testString());
        // first check if task cpp file is created
        VirtualFile baseDir = proj.getBaseDir();
        VirtualFile taskFile;
        try{
            taskFile = FileUtils.findOrCreateByRelativePath(baseDir, getTaskRelativePath());
            FileUtils.setDocumentText(proj,   FileDocumentManager.getInstance().getDocument(taskFile),
                    replaceTaskTemplate(Utilities.getConfigState().getTemplate()));
        }catch(Exception ex){
            SHelperException exx = new SHelperException("Task file couldn't be created");
            Utilities.logException(exx);
            throw exx;
        }

        // now save all the tests (clear them first)
        /*VirtualFile testsDir;
        try{
            testsDir = FileUtils.findOrCreateByRelativePath(baseDir, "tests/");
        }catch(Exception ex){
            SHelperException exx = new SHelperException("Tests directory couldn't be created");
            Utilities.logException(exx);
            throw exx;
        }

        String content = "";
        int i = 0;
        for(Test test : tests){
            if(i > 0)
                content += Test.TEST_SEPARATOR;
            content += test.toFileString();
            i++;
        }

        VirtualFile testFile;
        try{
            testFile = FileUtils.findOrCreateByRelativePath(testsDir, getTestFileName());
        }catch(Exception ex){
            SHelperException exx = new SHelperException("Test file couldn't be created");
            Utilities.logException(exx);
            throw exx;
        }

        Document testDoc = FileDocumentManager.getInstance().getDocument(testFile);
        FileUtils.setDocumentText(testDoc, content);*/

        createConfiguration(proj);
    }

    public String getTaskRelativePath(){
        return taskPath;
    }

    public String getOJRelativePath(){
        VirtualFile base = project.getBaseDir();
        VirtualFile taskFile = base.findFileByRelativePath(getTaskRelativePath());
        if(taskFile == null) throw new SHelperException("Task could not be found");
        VirtualFile taskDir = taskFile.getParent();
        VirtualFile ojDir = FileUtils.findOrCreateByRelativePath(taskDir, TaskRunner.OJ_PATH);
        VirtualFile ojFile = FileUtils.findOrCreateByRelativePath(ojDir, taskFile.getName());
        return ojFile.getPath();
    }

    public void load(Project project){
        // to implement
    }

    public void createConfiguration(Project project){
        TaskConfigurationType configType = new TaskConfigurationType();
        ConfigurationFactory configFactory = configType.getConfigurationFactories()[0];

        RunManager manager = RunManager.getInstance(project);
        RunnerAndConfigurationSettings config = manager.createConfiguration(
                new TaskConfiguration(
                        project,
                        configFactory,
                        this
                ), configFactory
        );
        manager.addConfiguration(config, true);
        manager.setSelectedConfiguration(config);
    }

    /*public String testsToString(){
        String content = "";
        int i = 0;
        for(Test t : tests){
            if(i++ > 0) content += Test.TEST_SEPARATOR;
            content += t.toFileString();
        }
        return content;
    }

    public void testsFromString(String input){
        if(input.isEmpty()) return;
        String[] str = input.split(Test.TEST_SEPARATOR);
        tests.clear();
        int i = 0;
        for(String ts : str){
            tests.add(Test.fromFileString(i++, ts));
        }
    }*/

    public Test[] getTests(){
        ProjectConfigurator.State state = Utilities.getProjectState(project);
        if(state == null) return new Test[0];
        return state.getTaskTests(this.taskName);
    }

    public void addTest(Test t){
        ProjectConfigurator config = Utilities.getProjectConfigurator(project);
        ProjectConfigurator.State state = config.getState();
        if(state == null) throw new SHelperException("Project state could not be established");
        ArrayList<Test> tmp = new ArrayList<Test>(Arrays.asList(state.getTaskTests(this.taskName)));
        tmp.add(t);
        state.setTaskTests(this.taskName, tmp.toArray(new Test[tmp.size()]));
        config.loadState(state);
    }

    public void clearTests(){
       ProjectConfigurator.State state = Utilities.getProjectState(project);
        if(state == null) throw new SHelperException("Project state could not be established");
        state.setTaskTests(this.taskName, new Test[0]);
    }
}
