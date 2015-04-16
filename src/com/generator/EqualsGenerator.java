package com.generator;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;

// TODO: Auto-generated Javadoc
/**
 * The Class EqualsGenerator.
 */
public class EqualsGenerator extends BaseGenerator {

    /**
     * Instantiates a new equals generator.
     *
     * @param type
     *            the type
     * @param fields
     *            the fields
     */
    public EqualsGenerator(IType type, IField[] fields) {
        super(type, "equals", fields);
    }

    /*
     * (non-Javadoc)
     * @see com.generator.BaseGenerator#createMethod(org.eclipse.jdt.core.IField[], org.eclipse.jdt.core.IType)
     */
    @Override
    protected String createMethod(IField[] fields, IType type) {
        StringBuilder buf = new StringBuilder(200);
        String typeName = type.getElementName();
        buf.append("\n@Override\npublic boolean equals(Object obj)  {\n");
        buf.append("if (obj == null || getClass() != obj.getClass()) {\n return false; \n}\n");
        if (fields.length > 0) {
            buf.append("final ").append(typeName).append(" other = (").append(typeName).append(") obj;\n");
            buf.append("return ");
            for (int i = 0; i < fields.length; ++i) {
                IField f = fields[i];
                if (i > 0) {
                    buf.append("&& ");
                }
                String elementName = f.getElementName();
                buf.append("Objects.equals(this.").append(elementName).append(", other.").append(elementName)
                        .append(")");
            }
            buf.append(";\n");
        } else {
            buf.append("return true;");
        }

        buf.append("}");
        return buf.toString();
    }

}
