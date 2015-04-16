package com.generator;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import com.common.Utility;

// TODO: Auto-generated Javadoc
/**
 * The Class BaseGenerator.
 */
public abstract class BaseGenerator {
    
    /** The type. */
    private IType type;
    
    /** The method. */
    private String method;
    
    /** The fields. */
    private IField[] fields;

    /**
     * Gets the fields.
     *
     * @return the fields
     */
    protected IField[] getFields() {
        return fields;
    }

    /**
     * Sets the fields.
     *
     * @param fields the new fields
     */
    protected void setFields(IField[] fields) {
        this.fields = fields;
    }

    /**
     * Instantiates a new base generator.
     *
     * @param type the type
     * @param method the method
     * @param fields the fields
     */
    public BaseGenerator(IType type, String method, IField[] fields) {
        this.type = type;
        this.method = method;
        this.fields = fields;
    }

    /**
     * Instantiates a new base generator.
     *
     * @param type the type
     * @param method the method
     * @throws JavaModelException the java model exception
     */
    public BaseGenerator(IType type, String method) throws JavaModelException {
        this(type, method, type.getFields());
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    protected IType getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the new type
     */
    protected void setType(IType type) {
        this.type = type;
    }

    /**
     * Removes the existing.
     *
     * @throws JavaModelException the java model exception
     */
    public void removeExisting() throws JavaModelException {
        IMethod[] methods = this.type.getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getElementName().equalsIgnoreCase(method)) {
                methods[i].delete(true, null);
                break;
            }
        }
    }

    /**
     * Generate method.
     *
     * @param overwrite the overwrite
     * @throws JavaModelException the java model exception
     */
    public void generateMethod(boolean overwrite) throws JavaModelException {
        if (overwrite) {
            removeExisting();
        }
        String src = createMethod(fields, this.type);
        src = Utility.format(src) + "\n";
        this.type.createMethod(src, null, false, null);
    }

    /**
     * Creates the method.
     *
     * @param fields the fields
     * @param type the type
     * @return the string
     */
    protected abstract String createMethod(IField[] fields, IType type);

    /**
     * Gets the method.
     *
     * @return the method
     */
    protected String getMethod() {
        return method;
    }

    /**
     * Sets the method.
     *
     * @param method the new method
     */
    protected void setMethod(String method) {
        this.method = method;
    }
}
