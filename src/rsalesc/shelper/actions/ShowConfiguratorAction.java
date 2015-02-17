package rsalesc.shelper.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import rsalesc.shelper.components.Configurator;
import rsalesc.shelper.ui.ConfigurationDialog;
import rsalesc.shelper.utils.Utilities;

/**
 * Created by rsalesc on 20/01/15.
 */
public class ShowConfiguratorAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {

        Configurator configurator = Utilities.getAppConfigurator();
        Configurator.State state = configurator.getState();

        ConfigurationDialog dialog = new ConfigurationDialog(e.getProject(), state); // project can be null in this case
        dialog.show();
        if(dialog.isOK()){
            configurator.loadState(dialog.getConfiguration());
        }
    }
}
