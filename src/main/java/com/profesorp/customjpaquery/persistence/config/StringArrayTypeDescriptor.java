package com.bankia.msc.agregadorterceros.cuentasterceros.persistence.config;

public class StringArrayTypeDescriptor extends	AbstractArrayTypeDescriptor<String[]> {
	
	private static final long serialVersionUID = -4133709243888879239L;
	public static final StringArrayTypeDescriptor INSTANCE = new StringArrayTypeDescriptor();

	public StringArrayTypeDescriptor() {
		super(String[].class);
	}

	@Override
	public String getSqlArrayType() {
		return "text";
	}
}