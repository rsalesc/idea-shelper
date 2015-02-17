package rsalesc.shelper.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.ui.components.JBScrollPane;
import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.VerticalLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rsalesc.shelper.tasks.Task;
import rsalesc.shelper.tasks.Test;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EditTestsDialog extends DialogWrapper {
    private static int HEIGHT = new JLabel("Test").getPreferredSize().height;

    private int currentTest;
    private Task task;
    private ArrayList<Test> tests;
    private DefaultListModel model;

    private final JComponent component;
    private CheckBoxList testList;
    private JTextArea input;
    private JTextArea output;

    public EditTestsDialog(@NotNull Project project, Task _task, Test[] _tests) {
        super(project);
        this.task = _task;
        this.tests = new ArrayList<Test>();

        for(Test t: _tests){
            this.tests.add(t);
        }

        setTitle("Test editing for for " + task.taskName);
        this.currentTest = -1;

        //template = new EditorTextField(EditorFactory.getInstance().createDocument(configuration.getTemplate()), project, FileTypeManager.getInstance().getFileTypeByExtension("cpp"), false, false);

        model = new DefaultListModel();
        HorizontalLayout testPanelLayout = new HorizontalLayout();
        testPanelLayout.setGap(10);
        JPanel testPanel = new JPanel(testPanelLayout);
        testList = new CheckBoxList();
        testList.setModel(model);
        testList.setFixedCellHeight((int)(HEIGHT*1.1));
        testList.setLayoutOrientation(JList.VERTICAL);
        testList.setBackground(Color.WHITE);
        testList.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        for(Test t : tests) {
            model.addElement(new CheckBoxListEntry(t, t.active));
        }

        testList.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                int index = testList.getSelectedIndex();
                if(index >= 0 && index < testList.getItemsCount()){
                    saveCurrentTest();
                    loadAnotherTest(index);
                }
            }
        });

        JPanel ioPanel = new JPanel(new VerticalLayout());
        input = new JTextArea();
        output = new JTextArea();
        input.setLineWrap(true);
        output.setLineWrap(true);
        JBScrollPane inputPane = new JBScrollPane(this.input, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JBScrollPane outputPane = new JBScrollPane(this.output, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        inputPane.setPreferredSize(new Dimension(500, 185));
        outputPane.setPreferredSize(new Dimension(500, 185));
        ioPanel.add(LabeledComponent.create(inputPane, "Input"));
        ioPanel.add(LabeledComponent.create(outputPane, "Output"));

        testPanel.add(LabeledComponent.create(testList, "Tests"));
        testPanel.add(ioPanel);

        JPanel panel = new JPanel(new VerticalLayout());
        panel.add(testPanel);

        JButton addTest = new JButton("Add Test");
        addTest.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Test newTest = new Test(tests.size(), "", "", true);
                tests.add(newTest);
                model.addElement(new CheckBoxListEntry(newTest, true));
            }
        });

        panel.add(addTest);

        component = panel;

        init();
    }

    private void initFields(){

    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return component;
    }

    private void loadAnotherTest(int index){
        Test test = tests.get(index);
        this.input.setText(test.input);
        this.output.setText(test.output);
        currentTest = index;
    }

    private void saveCurrentTest(){
        if(currentTest == -1) return;
        saveTest(currentTest);
    }

    private void saveTest(int index){
        Test oldTest = tests.get(index);
        Test newTest = new Test(oldTest.index, input.getText(), output.getText(), testList.isIndexChecked(index));
        tests.set(index, newTest);
    }

    public Test[] getTests(){
        ArrayList<Test> res = new ArrayList<Test>();
        for(Test t : this.tests){
            res.add(new Test(t.index, t.input, t.output, testList.isIndexChecked(t.index)));
        }
        return res.toArray(new Test[res.size()]);
    }

}