package com.nway.platform.wform.design.db.datatype;

public abstract class BaseDataType implements DataType {
	
	@Override
	public String getForInteger(int capacity) {

		return "INTEGER";
	}
	
	@Override
	public String getForString(int capacity) {

		return "VARCHAR(" + capacity + ")";
	}
	
	@Override
	public String getForFloat(int capacity) {
		
		return null;
	}

	@Override
	public String getForDouble(int capacity) {
		
		return null;
	}

	@Override
	public String getForBoolean() {

		return "BIT";
	}

	@Override
	public String getForDate() {

		return "TIMESTAMP";
	}

	@Override
	public String getForTimestamp() {

		return "TIMESTAMP";
	}
}
