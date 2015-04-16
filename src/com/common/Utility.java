package com.common;

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
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

// TODO: Auto-generated Javadoc
/**
 * Utility Class.
 *
 * @author sanketbajoria
 */
public class Utility {

    /** The recognizable types. */
    private static Class[] RECOGNIZABLE_TYPES = { IType.class, ICompilationUnit.class };

    /**
     * Get Selected Type from WorkbenchPart.
     *
     * @param targetPart
     *            the target part
     * @return the selected type
     * @throws JavaModelException
     *             the java model exception
     */
    public static IType getSelectedType(IWorkbenchPart targetPart) throws JavaModelException {
        IType type = null;
        type = getSelectedType(targetPart.getSite().getPage());
        if (type == null) {
            type = getSelectedType(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage());
        }
        return type;
    }

    /**
     * Gets the selected type.
     *
     * @param page
     *            the page
     * @return the selected type
     * @throws JavaModelException
     *             the java model exception
     */
    public static IType getSelectedType(IWorkbenchPage page) throws JavaModelException {
        IType type = null;
        if (page != null) {
            type = getSelectedType(page.getSelection("org.eclipse.ui.views.ContentOutline"));
            if (type != null) {
                System.out.println("Found here -- " + "org.eclipse.ui.views.ContentOutline");
                return type;
            }
            type = getSelectedType(page.getSelection("org.eclipse.jdt.ui.PackageExplorer"));
            if (type != null) {
                System.out.println("Found here -- " + "org.eclipse.jdt.ui.PackageExplorer");
                return type;
            }
            type = getSelectedType(page.getSelection());
            if (type != null) {
                System.out.println("Found here -- " + "Direct selection");
                return type;
            }
            IEditorPart part = page.getActiveEditor();
            if (part != null) {
                IEditorInput input = part.getEditorInput();
                IJavaElement elem = ((IJavaElement) input.getAdapter(IJavaElement.class));
                if (elem != null) {
                    System.out.println("Found here -- " + "Editor part");
                    type = recognizeType(elem);
                }
            }
        }
        return type;
    }

    /**
     * Gets the selected type.
     *
     * @param sel
     *            the sel
     * @return the selected type
     * @throws JavaModelException
     *             the java model exception
     */
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

    /**
     * Checks if is type recognizable.
     *
     * @param el
     *            the el
     * @return true, if is type recognizable
     */
    private static boolean isTypeRecognizable(IJavaElement el) {
        Class clazz = el.getClass();
        for (int i = 0; i < RECOGNIZABLE_TYPES.length; ++i) {
            if (RECOGNIZABLE_TYPES[i].isAssignableFrom(clazz)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Up to type recognizable.
     *
     * @param root
     *            the root
     * @return the i java element
     */
    private static IJavaElement upToTypeRecognizable(IJavaElement root) {
        IJavaElement el = root;
        while ((el != null) && (!(isTypeRecognizable(el)))) {
            el = el.getParent();
        }

        return el;
    }

    /**
     * Recognize type.
     *
     * @param el
     *            the el
     * @return the i type
     * @throws JavaModelException
     *             the java model exception
     */
    private static IType recognizeType(IJavaElement el) throws JavaModelException {
        if (el instanceof IType) {
            return ((IType) el);
        }
        if (el instanceof ICompilationUnit) {
            IType[] types = ((ICompilationUnit) el).getTypes();
            return ((types.length == 0) ? null : types[0]);
        }
        throw new RuntimeException("Can't recognize IType in " + el.getClass().getName());
    }

    /**
     * Format.
     *
     * @param s
     *            the s
     * @return the string
     */
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

    /**
     * Organize import.
     *
     * @param type
     *            the type
     */
    public static void organizeImport(IType type) {
        try {
            IEditorPart editorPart =
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().activate(editorPart);

            final IHandlerService handlerService =
                    (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);

            IHandler handler = new AbstractHandler() {
                @Override
                public Object execute(ExecutionEvent event) {
                    System.out.println("Inside execute");
                    return null;
                }
            };
            handlerService.activateHandler("org.eclipse.jdt.ui.edit.text.java.organize.imports", handler);

            handlerService.executeCommand("org.eclipse.jdt.ui.edit.text.java.organize.imports", null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
