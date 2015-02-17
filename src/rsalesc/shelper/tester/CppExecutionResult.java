package rsalesc.shelper.tester;

/**
 * Created by rsalesc on 16/02/15.
 */
public class CppExecutionResult {
    public int exitCode;
    public String stdout;
    public String stderr;

    public CppExecutionResult(int exitCode){
        this.exitCode = exitCode;
    }

    public CppExecutionResult(){
        this(0);
    }
}
