package rsalesc.shelper.utils;

/**
 * Created by rsalesc on 11/12/14.
 */
public class SHelperException extends RuntimeException {
    public SHelperException(String message){
        super("SHelper error: " + message);
    }

    public SHelperException(String message, Exception e){
        super(message, e.getCause());
        this.setStackTrace(e.getStackTrace());
    }
}
