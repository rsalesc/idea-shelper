package rsalesc.shelper.utils;

/**
 * Created by rsalesc on 13/12/14.
 */
public class TemplateUtils {
    public static String getTaskTemplate() throws Exception{
        return FileUtils.getResourceAsString("/rsalesc/shelper/data/task.template");
    }

    public static String getRunnerTemplate() throws Exception{
        return FileUtils.getResourceAsString("/rsalesc/shelper/data/runner.template");
    }

    public static String getOJTemplate() throws Exception{
        return FileUtils.getResourceAsString("/rsalesc/shelper/data/oj.template");
    }

    public static String getCMakeTemplate() throws Exception{
        return FileUtils.getResourceAsString("/rsalesc/shelper/data/cmake.template");
    }
}
