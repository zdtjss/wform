package com.nway.platform.wform.design.component;

import org.springframework.stereotype.Component;

@Component("textarea")
public class TextareaComponent implements BaseComponent {

	@Override
	public Object getValue(String value) {
		
		return value;
	}

}
