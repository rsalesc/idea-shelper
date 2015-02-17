package rsalesc.shelper.utils;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import rsalesc.shelper.components.Configurator;
import rsalesc.shelper.components.ProjectConfigurator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by rsalesc on 10/12/14.
 */
public class Utilities {
    public static String getPageHtml(String url) throws IOException {
        URL page = new URL(url);
        BufferedReader in = new BufferedReader(new InputStreamReader(page.openStream()));

        StringBuilder builder = new StringBuilder();
        String inLine;
        while((inLine = in.readLine()) != null) {
            builder.append(inLine + "\n");
        }
        in.close();
        return builder.toString();
    }

    public static void logException(Exception e){
        Notification notification = new Notification("SHelper", "SHelper error", e.getMessage(), NotificationType.ERROR);
        Notifications.Bus.notify(notification);
        e.printStackTrace();
        // throw e;
    }

    public static void log(String msg){
        Notification notification = new Notification("SHelper", "SHelper log", msg, NotificationType.INFORMATION);
        Notifications.Bus.notify(notification);
    }

    public static String brToNl(String html) {
        if (html == null)
            return "";
        html = html.replaceAll("<(br|BR)/?>", "\n");
        return html;
    }

    public static Configurator.State getConfigState(){
        return ApplicationManager.getApplication().getComponent(Configurator.class).getState();
    }
    public static void loadConfigState(Configurator.State state){
        ApplicationManager.getApplication().getComponent(Configurator.class).loadState(state);
    }

    public static ProjectConfigurator getProjectConfigurator(Project project){
        return project.getComponent(ProjectConfigurator.class);
    }

    public static ProjectConfigurator.State getProjectState(Project project){
        ProjectConfigurator config = getProjectConfigurator(project);
        return config.getState();
    }

    public static Configurator getAppConfigurator(){
        return ApplicationManager.getApplication().getComponent(Configurator.class);
    }

    public static Configurator.State getAppState(){
        return getAppConfigurator().getState();
    }

    public static String normalizePath(String path){
        path = path.trim();
        if(path.lastIndexOf('/') != path.length()-1)
            path += "/";
        return path;
    }
}
