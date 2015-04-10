package com.popup.actions;

import java.util.Arrays;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListSelectionDialog;

import com.common.Utility;
import com.generator.EqualsGenerator;
import com.generator.HashCodeGenerator;

public class GenerateAction implements IObjectActionDelegate {

	private Shell shell;
	
	private IWorkbenchPart targetPart;
	
	public IWorkbenchPart getTargetPart() {
        return targetPart;
    }

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
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
		this.targetPart = targetPart;
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		
		try {
            IType type = Utility.getSelectedType(targetPart.getSite().getPage());
            if(type == null){
                type = Utility.getSelectedType(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage());
            }
            if(type != null){
                ListSelectionDialog dlg = new ListSelectionDialog(shell, type.getFields(), new ArrayContentProvider(), new FieldLabelProvider(), "Select the Variable:");
                dlg.setTitle("Edit variables");
                dlg.open();
                IField[] fields = Arrays.copyOf(dlg.getResult(), dlg.getResult().length, IField[].class);
                HashCodeGenerator hashCodeGenerator = new HashCodeGenerator(type,fields);
                hashCodeGenerator.generateMethod(true);
                EqualsGenerator equalsGenerator = new EqualsGenerator(type,fields);
                equalsGenerator.generateMethod(true);
                Utility.organizeImport(type); 
            }else{
                MessageDialog dialog = new MessageDialog(shell, "Oops", null,
                        "Error while selecting item, please, try again", MessageDialog.INFORMATION, new String[] { "OK"}, 0);
                    int result = dialog.open();
            }
            
        } catch (JavaModelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}
	
	

}

class FieldLabelProvider extends LabelProvider{
    @Override
    public String getText(Object element) {
        String ret = "";
        try {
            ret = element == null ? "" : ((IField)element).getElementName() + " - " + Signature.toString(((IField)element).getTypeSignature());
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
