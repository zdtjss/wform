package com.nway.platform.wform.component.impl;

import org.springframework.stereotype.Component;

import com.nway.platform.wform.component.BaseComponent;

@Component("textarea")
public class TextareaComponent implements BaseComponent {

	@Override
	public Object getValue(Object value) {
		
		return value;
	}

}
