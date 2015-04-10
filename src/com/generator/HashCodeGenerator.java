package com.generator;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;

public class HashCodeGenerator extends BaseGenerator{
    
    public HashCodeGenerator(IType type, IField[] fields){
        super(type, "hashCode", fields);
    }

    @Override
    protected String createMethod(IField[] fields, IType type) {
        StringBuilder buf = new StringBuilder();
        buf.append("\n@Override\npublic int hashCode() {\n");
        buf.append("return Objects.hash(");
        for (int i = 0; i < fields.length; ++i) {
            IField f = fields[i];
            if(i>0){
                buf.append(", ");
            }
            buf.append(f.getElementName());
        }
        buf.append(");\n");
        buf.append("}");
        return buf.toString();
    }
    
    
}
