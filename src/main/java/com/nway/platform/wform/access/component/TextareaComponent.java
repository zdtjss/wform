package com.nway.platform.wform.access.component;

import org.springframework.stereotype.Component;

@Component("textarea")
public class TextareaComponent implements BaseComponent {

	@Override
	public String getPath() {
		return null;
	}

	@Override
	public Object getValue(String value) {
		
		return value;
	}

}
