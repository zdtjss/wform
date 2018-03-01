package com.nway.platform.wform.component.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nway.platform.wform.component.BaseComponent;
import com.nway.platform.wform.design.db.datatype.DataType;

@Component("switch")
public class SwitchComponent implements BaseComponent {

	@Autowired
	private DataType dataType;
	
	@Override
	public Object getValue(Object value) {
		
		return value;
	}

	@Override
	public String getDataType(int capacity) {

		return dataType.getForBoolean();
	}

}
