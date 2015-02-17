package rsalesc.shelper.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.jetbrains.objc.psi.OCImportDirective;
import com.jetbrains.objc.psi.OCPragma;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by rsalesc on 19/01/15.
 */
public class IncludeProcessor {
    private Set<PsiFile> processedFiles = new HashSet<PsiFile>();
    @SuppressWarnings("StringBufferField")
    private StringBuilder result = new StringBuilder();
    private Project project;
    private VirtualFile libFolder;

    private IncludeProcessor(Project project) {
        libFolder = VirtualFileManager.getInstance().findFileByUrl(Utilities.getAppState().getLibFolder());
        this.project = project;
    }

    private void processFile(PsiFile file) {
        if (processedFiles.contains(file)) {
            return;
        }
        processedFiles.add(file);
        for (PsiElement element : file.getChildren()) {
            if (element instanceof OCImportDirective) {
                OCImportDirective include = (OCImportDirective) element;
                if (!include.isAngleBrackets()) {
                    if(include.getImportedFile() == null) throw new SHelperException("Invalid include");
                    processFile(include.getImportedFile());
                    continue;

                }
            }
            if (element instanceof OCPragma) {
                OCPragma pragma = (OCPragma) element;
                if (pragma.getContent().trim().equals("once")) {
                    continue;
                }
            }
            result.append(element.getText());
        }
    }

    /*private void processAngleBracketsInclude(OCImportDirective include) {
        PsiFile file = include.getImportedFile();
        if (processedFiles.contains(file)) {
            return;
        }
        processedFiles.add(file);
        result.append(include.getText());
    }*/

    public static String process(Project project, PsiFile file) {
        IncludeProcessor processor = new IncludeProcessor(project);
        processor.processFile(file);
        return processor.result.toString();
    }
}