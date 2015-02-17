package rsalesc.shelper.components;

import com.intellij.openapi.components.*;
import org.jetbrains.annotations.NotNull;
import rsalesc.shelper.utils.TemplateUtils;

/**
 * Created by rsalesc on 20/01/15.
 */
@State(
    name = "Configurator",
    storages = {
        @Storage(id = "other", file = StoragePathMacros.APP_CONFIG + "/SHelper.xml")
    }
)

public class Configurator implements ApplicationComponent, PersistentStateComponent<Configurator.State> {

    private Configurator.State state;

    public Configurator(){
        state = new Configurator.State();
    }

    public Configurator.State getState(){
        return state;
    }

    public void loadState(Configurator.State newState){
        state = newState;
    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "Configurator";
    }

    public static class State{
        private String template;
        private String cxxFlags;
        private String libFolder;
        private boolean removeUnused;
        private boolean colorOutput;

        public State(String a, String b, String c, boolean d, boolean e){
            init(a,b,c,d, e);
        }

        public State(){
            String tmp = "";
            try {
                tmp = TemplateUtils.getTaskTemplate();
            } catch (Exception e) {
                //e.printStackTrace();
            }
            init(tmp, "-std=c++11", "", true, false);
        }

        private void init(String a, String b, String c, boolean d, boolean e){
            this.setTemplate(a);
            this.setCxxFlags(b);
            this.setLibFolder(c);
            this.setRemoveUnused(d);
            this.setColorOutput(e);
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }

        public String getCxxFlags() {
            return cxxFlags;
        }

        public void setCxxFlags(String cxxFlags) {
            this.cxxFlags = cxxFlags;
        }

        public boolean isRemoveUnused() {
            return removeUnused;
        }

        public void setRemoveUnused(boolean removeUnused) {
            this.removeUnused = removeUnused;
        }

        public boolean isColorOutput() {
            return colorOutput;
        }

        public void setColorOutput(boolean colorOutput) {
            this.colorOutput = colorOutput;
        }

        public String getLibFolder() {
            return libFolder;
        }

        public void setLibFolder(String libFolder) {
            this.libFolder = libFolder;
        }
    }
}
