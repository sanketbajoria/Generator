package com.generator;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import com.common.Utility;

public abstract class BaseGenerator {
    private IType type;
    private String method;
    private IField[] fields;

    protected IField[] getFields() {
        return fields;
    }

    protected void setFields(IField[] fields) {
        this.fields = fields;
    }

    public BaseGenerator(IType type, String method, IField[] fields) {
        this.type = type;
        this.method = method;
        this.fields = fields;
    }
    
    public BaseGenerator(IType type, String method) throws JavaModelException {
        this(type,method,type.getFields());
    }

    protected IType getType() {
        return type;
    }

    protected void setType(IType type) {
        this.type = type;
    }

    public void removeExisting() throws JavaModelException {
        IMethod[] methods = this.type.getMethods();
        for(int i=0;i<methods.length;i++){
            if(methods[i].getElementName().equalsIgnoreCase(method)){
                methods[i].delete(true, null);
                break;
            }
        }
    }

    public void generateMethod(boolean overwrite) throws JavaModelException {
        if (overwrite) {
            removeExisting();
        }
        String src = createMethod(fields, this.type);
        src = Utility.format(src) + "\n";
        this.type.createMethod(src, null, false, null);
    }

    protected abstract String createMethod(IField[] fields, IType type);

    protected String getMethod() {
        return method;
    }

    protected void setMethod(String method) {
        this.method = method;
    }
}
