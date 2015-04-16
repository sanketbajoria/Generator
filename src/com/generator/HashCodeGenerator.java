package com.generator;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;

// TODO: Auto-generated Javadoc
/**
 * The Class HashCodeGenerator.
 */
public class HashCodeGenerator extends BaseGenerator {

    /**
     * Instantiates a new hash code generator.
     *
     * @param type
     *            the type
     * @param fields
     *            the fields
     */
    public HashCodeGenerator(IType type, IField[] fields) {
        super(type, "hashCode", fields);
    }

    /*
     * (non-Javadoc)
     * @see com.generator.BaseGenerator#createMethod(org.eclipse.jdt.core.IField[], org.eclipse.jdt.core.IType)
     */
    @Override
    protected String createMethod(IField[] fields, IType type) {
        StringBuilder buf = new StringBuilder(200);
        buf.append("\n@Override\npublic int hashCode() {\n");
        buf.append("return Objects.hash(");
        for (int i = 0; i < fields.length; ++i) {
            IField f = fields[i];
            if (i > 0) {
                buf.append(", ");
            }
            buf.append(f.getElementName());
        }
        buf.append(");\n");
        buf.append("}");
        return buf.toString();
    }

}
