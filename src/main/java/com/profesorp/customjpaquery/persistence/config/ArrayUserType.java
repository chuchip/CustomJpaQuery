package com.bankia.msc.agregadorterceros.cuentasterceros.persistence.config;


import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;
import org.hibernate.usertype.DynamicParameterizedType;

import java.util.Properties;


public class ArrayUserType 
        extends AbstractSingleColumnStandardBasicType<String[]>
        implements DynamicParameterizedType {

    public ArrayUserType() {
        super(ArraySqlTypeDescriptor.INSTANCE, StringArrayTypeDescriptor.INSTANCE);
    }

    public ArrayUserType(JavaTypeDescriptor<String[]> javaTypeDescriptor) {
        super(ArraySqlTypeDescriptor.INSTANCE, javaTypeDescriptor);
    }

    public String getName() {
        return "string-array";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public void setParameterValues(Properties parameters) {
        ((StringArrayTypeDescriptor) getJavaTypeDescriptor()).setParameterValues(parameters);
    }
}