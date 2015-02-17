package rsalesc.shelper.ui;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.ui.components.JBScrollPane;
import org.jdesktop.swingx.VerticalLayout;
import org.jetbrains.annotations.Nullable;
import rsalesc.shelper.components.Configurator;
import rsalesc.shelper.utils.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigurationDialog extends DialogWrapper {
    private final JComponent component;
    private Project project;
    private JTextArea template;
    private JTextField cxxFlags;
    private TextFieldWithBrowseButton libFolder;
    private JCheckBox removeUnused;
    private JCheckBox colorOutput;

    public ConfigurationDialog(Project proj, Configurator.State configuration) {
        super(false);
        setTitle("SHelper configuration");
        this.project = proj;

        //template = new EditorTextField(EditorFactory.getInstance().createDocument(configuration.getTemplate()), project, FileTypeManager.getInstance().getFileTypeByExtension("cpp"), false, false);
        template = new JTextArea();
        cxxFlags = new JTextField();
        libFolder = new TextFieldWithBrowseButton();
        removeUnused = new JCheckBox("Remove unused code");
        colorOutput = new JCheckBox("Color console output");
        initFields(configuration);

        libFolder.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
                VirtualFile chosen = FileChooser.chooseFile(descriptor, project, VirtualFileManager.getInstance().findFileByUrl(libFolder.getText()));
                if(chosen != null) libFolder.setText(chosen.getPath());
            }
        });

        JBScrollPane scrollPane = new JBScrollPane(template, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JPanel panel = new JPanel(new VerticalLayout());
        panel.add(LabeledComponent.create(scrollPane, "Task Template"));
        panel.add(LabeledComponent.create(cxxFlags, "C++ Compiler Flags:"));
        panel.add(LabeledComponent.create(libFolder, "Include Folder:"));
        panel.add(removeUnused);
        panel.add(colorOutput);

        JButton restore = new JButton("Restore default");
        restore.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Configurator.State empty = new Configurator.State();
                Utilities.loadConfigState(empty);
                initFields(empty);
            }
        });

        panel.add(restore);

        component = panel;

        init();
    }

    public void initFields(Configurator.State configuration){
        template.setText(configuration.getTemplate());
        cxxFlags.setText(configuration.getCxxFlags());
        libFolder.setText(configuration.getLibFolder());
        removeUnused.setSelected( configuration.isRemoveUnused());
        colorOutput.setSelected(configuration.isColorOutput());
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return component;
    }

    public Configurator.State getConfiguration() {
        return new Configurator.State(
                template.getText(),
                cxxFlags.getText(),
                libFolder.getText(),
                removeUnused.isSelected(),
                colorOutput.isSelected()
        );
    }
}