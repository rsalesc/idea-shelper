package rsalesc.shelper.tester;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by rsalesc on 16/02/15.
 */
public class CppExecution {
    private String cmd;

    public CppExecution(@NotNull String cmd){
        this.cmd = cmd;
    }

    public CppExecutionResult execute(String input) throws IOException, InterruptedException {
        // ProcessBuilder pb = new ProcessBuilder(cmd);
        Process proc = Runtime.getRuntime().exec(cmd);

        BufferedReader stdout = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        // BufferedReader stderr = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

        if(input != null) {
            OutputStream stdin = proc.getOutputStream();
            stdin.write(input.getBytes());
            stdin.flush();
        }

        StringBuilder output = new StringBuilder();
        String line;
        while((line = stdout.readLine()) != null) {
            output.append(line);
        }

        proc.waitFor();

        CppExecutionResult res = new CppExecutionResult();
        res.exitCode = proc.exitValue();
        res.stdout = output.toString();
        return res;
    }

    public static CppExecutionResult createAndExecute(String cmd, String input) throws IOException, InterruptedException {
        CppExecution exec = new CppExecution(cmd);
        return exec.execute(input);
    }
}
