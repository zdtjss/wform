package com.nway.platform.wform.design.component;

import org.springframework.stereotype.Component;

@Component("text")
public class TextComponent implements BaseComponent {

	@Override
	public Object getValue(String value) {
		
		return value;
	}

}
