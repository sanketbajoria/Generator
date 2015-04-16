package com.popup.actions;

import java.util.Arrays;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.ListSelectionDialog;

import com.common.Utility;
import com.generator.EqualsGenerator;
import com.generator.HashCodeGenerator;

// TODO: Auto-generated Javadoc
/**
 * The Class GenerateAction.
 */
public class GenerateAction implements IObjectActionDelegate {

    /** The shell. */
    private Shell shell;

    /** The target part. */
    private IWorkbenchPart targetPart;

    /**
     * Gets the target part.
     *
     * @return the target part
     */
    public IWorkbenchPart getTargetPart() {
        return targetPart;
    }

    /**
     * Sets the target part.
     *
     * @param targetPart
     *            the new target part
     */
    public void setTargetPart(IWorkbenchPart targetPart) {
        this.targetPart = targetPart;
    }

    /**
     * Constructor for Action1.
     */
    public GenerateAction() {
        super();
    }

    /**
     * Sets the active part.
     *
     * @param action
     *            the action
     * @param targetPart
     *            the target part
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    @Override
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        shell = targetPart.getSite().getShell();
        this.targetPart = targetPart;
    }

    /**
     * Run.
     *
     * @param action
     *            the action
     * @see IActionDelegate#run(IAction)
     */
    @Override
    public void run(IAction action) {

        try {
            IType type = Utility.getSelectedType(targetPart);
            if (type != null) {
                if (type.isClass()) {
                    ListSelectionDialog dlg =
                            new ListSelectionDialog(shell, type.getFields(), new ArrayContentProvider(),
                                    new FieldLabelProvider(), "Select variables, you want in your method:");
                    dlg.setTitle("Selec the variables");
                    if (Window.OK == dlg.open()) {
                        IField[] fields = Arrays.copyOf(dlg.getResult(), dlg.getResult().length, IField[].class);
                        HashCodeGenerator hashCodeGenerator = new HashCodeGenerator(type, fields);
                        hashCodeGenerator.generateMethod(true);
                        EqualsGenerator equalsGenerator = new EqualsGenerator(type, fields);
                        equalsGenerator.generateMethod(true);
                        Utility.organizeImport(type);
                    }
                } else {
                    Utility.showInfo(shell, "This operation is not applicable to this type");
                }
            } else {
                Utility.showInfo(shell, "Error while selecting item, please, try from package explorer or content view");
            }

        } catch (JavaModelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Selection changed.
     *
     * @param action
     *            the action
     * @param selection
     *            the selection
     * @see IActionDelegate#selectionChanged(IAction, ISelection)
     */
    @Override
    public void selectionChanged(IAction action, ISelection selection) {
    }

}

class FieldLabelProvider extends LabelProvider {
    @Override
    public String getText(Object element) {
        String ret = "";
        try {
            ret =
                    element == null ? "" : ((IField) element).getElementName() + " - "
                            + Signature.toString(((IField) element).getTypeSignature());
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JavaModelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }
}
