package com.nway.platform.wform.component.impl;

import org.springframework.stereotype.Component;

import com.nway.platform.wform.component.BaseComponent;

@Component("switch")
public class SwitchComponent implements BaseComponent {

	@Override
	public Object getValue(Object value) {
		
		return value;
	}

}
