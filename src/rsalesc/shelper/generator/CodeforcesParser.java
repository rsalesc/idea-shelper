package rsalesc.shelper.generator;

import com.intellij.openapi.project.Project;
import jodd.jerry.Jerry;
import jodd.jerry.JerryFunction;
import rsalesc.shelper.tasks.Task;
import rsalesc.shelper.tasks.Test;
import rsalesc.shelper.utils.Utilities;

import java.util.ArrayList;

/**
 * Created by rsalesc on 10/12/14.
 */
public class CodeforcesParser implements ContestParser {
    public final static String URI_PREFIX = "http://codeforces.com/contest/";
    private int contestId;
    private String relativeBaseDir;
    public static int COUNTER = 0;
    public CodeforcesParser(int contestId, String relativeBaseDir){
        this.contestId = contestId;
        this.relativeBaseDir = relativeBaseDir;
    }

    public String getUrl(){
        return URI_PREFIX + contestId + "/";
    }

    public String getProblemSetUrl(){
        return this.getUrl() + "problems"; // may be wrong
    }

    public Task[] parse(final Project proj)  throws Exception{
        String html = Utilities.getPageHtml(getProblemSetUrl());
        Jerry doc = Jerry.jerry(html);
        final ArrayList<Task> tasks = new ArrayList<Task>();

        //VirtualFile f = proj.getBaseDir().createChildData(null, "set.html");
        //FileDocumentManager.getInstance().getDocument(f).setText(html);

        final String relativeBase = this.relativeBaseDir;
        final int contestId = this.contestId;

        doc.$("[problemindex]").each(new JerryFunction(){
            public boolean onNode(Jerry e, int i){
                // Utilities.log("PROBREM FAUND");
                String problemIdx = e.attr("problemindex");
                String problemName = e.find(".title").first().text();
                final Task task = Task.create(proj, "Task"+problemIdx+"_"+contestId, problemName, "", relativeBase+"Task"+problemIdx+".cpp");
                CodeforcesParser.COUNTER = 0;

                e.find(".sample-test").each(new JerryFunction(){
                    public boolean onNode(Jerry e2, int i2){
                        e2.find(".input").each(new JerryFunction(){
                            public boolean onNode(Jerry e3, int i3){
                                Test t = new Test(CodeforcesParser.COUNTER++,
                                    Utilities.brToNl(e3.find("pre").first().html()),
                                    Utilities.brToNl(e3.next().find("pre").first().html()),
                                    true
                                );
                                task.addTest(t);
                                return true;
                            }
                        });
                        return true;
                    }
                });
                tasks.add(task);
                return true;
            }
        });

        return tasks.toArray(new Task[tasks.size()]);
    }
}
