package com.nway.platform.wform.component.impl;

import org.springframework.stereotype.Component;

import com.nway.platform.wform.component.BaseComponent;
import com.nway.platform.wform.design.db.datatype.DataType;

@Component("text")
public class TextComponent implements BaseComponent {

	private DataType dataType;
	
	@Override
	public Object getValue(Object value) {
		
		return value;
	}

	public Object getDataType(int capacity) {
		
		return dataType.getForString(capacity);
	}

}
