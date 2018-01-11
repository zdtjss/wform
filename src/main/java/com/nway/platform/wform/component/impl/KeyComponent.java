package com.nway.platform.wform.component.impl;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.nway.platform.wform.component.BaseComponent;

@Component("key")
public class KeyComponent implements BaseComponent {

	@Override
	public Object getValue(String value) {
		
		return value == null || value.length() == 0 ? UUID.randomUUID().toString() : value;
	}

}
