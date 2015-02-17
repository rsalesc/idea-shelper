package rsalesc.shelper.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import org.jdesktop.swingx.VerticalLayout;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by rsalesc on 22/01/15.
 */
public class CodeforcesParserDialog extends DialogWrapper {

    private final JComponent component;
    private JTextField contestId;
    private JTextField contestBaseDir;

    public CodeforcesParserDialog(Project project){
        super(project);
        setTitle("Parsing Codeforces contest");
        JPanel panel = new JPanel(new VerticalLayout());

        contestId = new JTextField();
        contestBaseDir = new JTextField();

        panel.add(LabeledComponent.create(contestId, "Contest identifier (numeric)"));
        panel.add(LabeledComponent.create(contestBaseDir, "Contest base directory"));

        component = panel;

        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return component;
    }

    public int getContestId(){
        return Integer.parseInt(contestId.getText());
    }

    public String getContestBaseDir(){
        return contestBaseDir.getText();
    }

}
