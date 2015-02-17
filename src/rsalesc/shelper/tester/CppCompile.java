package rsalesc.shelper.tester;

import com.intellij.execution.process.CommandLineArgumentsProvider;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created by rsalesc on 16/02/15.
 */
public class CppCompile {
    private boolean v11;
    private String file;

    public CppCompile(String file, boolean v11){
        this.file = file;
        this.v11 = v11;
    }

    public CppCompile(String file){
        this(file, false);
    }

    public CppCompile(VirtualFile file){
        this(file.getPath());
    }

    public CppExecutionResult compile(){
        String cmd =  CommandLineArgumentsProvider.toCommandLine("g++", "-std=c++11", this.file);
        try {
            CppExecutionResult res = CppExecution.createAndExecute(cmd, null);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return new CppExecutionResult(1);
        }
    }
}
