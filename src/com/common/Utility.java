package com.common;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ExecutionException;
import org.eclipse.ui.handlers.IHandlerService;

public class Utility {
    private static Class[] RECOGNIZABLE_TYPES = { IType.class, ICompilationUnit.class };

    public static IType getSelectedType(IWorkbenchPage page) throws JavaModelException {
        IType type = getSelectedType(page.getSelection("org.eclipse.ui.views.ContentOutline"));
        if (type != null) {
            return type;
        }
        type = getSelectedType(page.getSelection("org.eclipse.jdt.ui.PackageExplorer"));
        if(type != null){
            return type;
        }
        type = getSelectedType(page.getSelection()); 
        return type;
    }

    public static IType getSelectedType(ISelection sel) throws JavaModelException {
        if ((sel != null) && (sel instanceof IStructuredSelection)) {
            IStructuredSelection ssel = (IStructuredSelection) sel;
            Object el = ssel.getFirstElement();

            if (el instanceof IFile) {
                el = JavaCore.create((IFile) el);
            }

            if (el instanceof IJavaElement) {
                IJavaElement top = upToTypeRecognizable((IJavaElement) el);
                if (top != null) {
                    return recognizeType(top);
                }
            }
        }

        return null;
    }

    private static boolean isTypeRecognizable(IJavaElement el) {
        Class clazz = el.getClass();
        for (int i = 0; i < RECOGNIZABLE_TYPES.length; ++i) {
            if (RECOGNIZABLE_TYPES[i].isAssignableFrom(clazz)) {
                return true;
            }
        }

        return false;
    }

    private static IJavaElement upToTypeRecognizable(IJavaElement root) {
        IJavaElement el = root;
        while ((el != null) && (!(isTypeRecognizable(el)))) {
            el = el.getParent();
        }

        return el;
    }

    private static IType recognizeType(IJavaElement el) throws JavaModelException {
        if (el instanceof IType)
            return ((IType) el);
        if (el instanceof ICompilationUnit) {
            IType[] types = ((ICompilationUnit) el).getTypes();
            return ((types.length == 0) ? null : types[0]);
        }

        throw new RuntimeException("Can't recognize IType in " + el.getClass().getName());
    }

    public static String format(String s) {
        String lineDelimiter = null;
        lineDelimiter = System.getProperty("line.separator", "\n");
        CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(JavaCore.getOptions());
        TextEdit textEdit = codeFormatter.format(4, s, 0, s.length(), 0, lineDelimiter);
        IDocument document = new Document(s);
        try {
            textEdit.apply(document);
        } catch (Exception localException) {
        }
        return document.get();
    }
    
    public static void organizeImport(IType type){
        try {
              IEditorPart editorPart = PlatformUI.getWorkbench()
                    .getActiveWorkbenchWindow().getActivePage()
                    .getActiveEditor();
            PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                    .getActivePage().activate(editorPart);


            final IHandlerService handlerService = (IHandlerService) PlatformUI
                    .getWorkbench().getService(
                            IHandlerService.class);

            IHandler handler = new AbstractHandler() {
                public Object execute(ExecutionEvent event) {
                    System.out.println("Inside execute");
                    return null;
                }
            };
            handlerService
                    .activateHandler(
                            "org.eclipse.jdt.ui.edit.text.java.organize.imports",
                            handler);

            handlerService
                    .executeCommand(
                            "org.eclipse.jdt.ui.edit.text.java.organize.imports",
                            null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
