package rsalesc.shelper.tasks;

import com.intellij.openapi.project.Project;

/**
 * Created by rsalesc on 10/12/14.
 */
public interface Persistent {
    public void save(Project project);
    public void load(Project project);
}
