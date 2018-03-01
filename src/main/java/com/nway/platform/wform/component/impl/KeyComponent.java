package com.nway.platform.wform.component.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nway.platform.wform.component.BaseComponent;
import com.nway.platform.wform.design.db.datatype.DataType;

@Component("key")
public class KeyComponent implements BaseComponent {
	
	@Autowired
	private DataType dataType;

	@Override
	public Object getValue(Object value) {
		
		return value == null || value.toString().length() == 0 ? UUID.randomUUID().toString() : value;
	}

	@Override
	public String getDataType(int capacity) {

		return dataType.getForString(capacity);
	}

}
