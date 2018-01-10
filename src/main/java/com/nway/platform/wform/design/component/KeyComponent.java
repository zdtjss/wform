package com.nway.platform.wform.design.component;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component("key")
public class KeyComponent implements BaseComponent {

	@Override
	public Object getValue(String value) {
		
		return value == null || value.length() == 0 ? UUID.randomUUID().toString() : value;
	}

}
