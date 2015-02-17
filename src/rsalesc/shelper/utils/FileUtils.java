package rsalesc.shelper.utils;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;

/**
 * Created by rsalesc on 11/12/14.
 */
public class FileUtils {
    public static VirtualFile findOrCreateByRelativePath(final VirtualFile file, final String localPath) throws SHelperException{
        return ApplicationManager.getApplication().runWriteAction(
                new Computable<VirtualFile>() {
                    @Override
                    public VirtualFile compute() {
                        String path = localPath;
                        if (path.isEmpty()) {
                            return file;
                        }
                        path = StringUtil.trimStart(path, "/");
                        int index = path.indexOf('/');
                        if (index < 0) {
                            index = path.length();
                        }
                        String name = path.substring(0, index);

                        @Nullable VirtualFile child;
                        if (name.equals(".")) {
                            child = file;
                        }
                        else if (name.equals("..")) {
                            child = file.getParent();
                        }
                        else {
                            child = file.findChild(name);
                            if (child == null) {
                                try {
                                    if (index == path.length()) {
                                        child = file.createChildData(this, name);
                                    }
                                    else {
                                        child = file.createChildDirectory(this, name);
                                    }
                                }
                                catch (IOException e) {
                                    throw new SHelperException("Couldn't create directory: " + file.getPath() + '/' + name, e);
                                }
                            }
                        }

                        assert child != null;

                        if (index < path.length()) {
                            return findOrCreateByRelativePath(child, path.substring(index + 1));
                        }

                        return child;
                    }
                }
        );
    }

    public static void setDocumentText(final Project project, final Document doc, final String text){
        ApplicationManager.getApplication().runWriteAction(new Runnable(){
            @Override
            public void run(){
                try {
                    // Utilities.log(text);
                    doc.setText(text);
                    FileDocumentManager.getInstance().saveDocument(doc);
                    PsiDocumentManager.getInstance(project).commitDocument(doc);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    public static String getResourceAsString(String path) throws Exception{
        URL base = FileUtils.class.getResource(path);
        return Resources.toString(base, Charsets.UTF_8);
    }
}
