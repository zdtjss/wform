package com.nway.platform.wform.design.db.datatype;

public interface DataType {

	String getForInteger(int capacity);

	String getForString(int capacity);
	
	String getForFloat(int capacity);
	
	String getForDouble(int capacity);

	String getForBoolean();

	String getForDate();

	String getForTimestamp();
}
