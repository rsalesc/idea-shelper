package rsalesc.shelper.tasks;

/**
 * Created by rsalesc on 10/12/14.
 */
public class Test
{
    public final static String TEST_SEPARATOR = "%%T%%";
    public final static String IO_SEPARATOR = "%%IO%%";
    public int index;
    public String input;
    public String output;
    public boolean active;

    public Test(){} // avoid runtime exception in state configurator

    public Test(int index, String input, String output, boolean active){
        this.index = index;
        this.input = input;
        this.output = output;
        this.active = active;
    }

    public Test(int index, String input, boolean active){
        this(index, input, "", active);
    }

    /*public String toFileString(){
        String content = input;
        if(output != null) content += Test.IO_SEPARATOR + output;
        return content;
    }

    public static Test fromFileString(int index, String input){
        String[] arr  = input.split(IO_SEPARATOR);
        if(arr.length > 1)
            return new Test(index, arr[0], arr[1]);
        else
            return new Test(index, arr[0], "");
    }*/

    public String toRunnerStr(){
        return "SHelper::Test(" + index + ", string(\"" + input.replace("\n", "\\n") + "\"), string(\"" + output.replace("\n", "\\n") + "\"))";
    }

    @Override
    public String toString(){
        return "Test #" + index;
    }

}
