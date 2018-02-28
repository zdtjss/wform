package com.nway.platform.wform.design.db.datatype;

public abstract class BaseDataType {

	protected String getForInteger(int capacity) {

		return "INTEGER";
	}
	
	protected String getForString(int capacity) {
		
		return "VARCHAR(" + capacity + ")";
	}
	
	protected String getForBoolean() {
		
		return "BIT";
	}
	
	protected String getForDate() {
		
		return "TIMESTAMP";
	}
	
	protected String getForTimestamp() {
		
		return "TIMESTAMP";
	}
}
