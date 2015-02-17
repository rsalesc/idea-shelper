package rsalesc.shelper.generator;

import com.intellij.openapi.project.Project;
import rsalesc.shelper.tasks.Task;

/**
 * Created by rsalesc on 10/12/14.
 */
public interface ContestParser {
    public Task[] parse(Project proj) throws Exception;
}
