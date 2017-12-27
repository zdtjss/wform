package com.nway.wform.access.component;

import org.springframework.stereotype.Component;

@Component("checkbox")
public class CheckboxComponent implements MultiValueComponent {

	@Override
	public String getPath() {
		return null;
	}

	@Override
	public Object getValue(String value) {
		
		return value != null && value.length() > 0 ? value.substring(2, value.length() - 2).split("\",\"") : null;
	}

	@Override
	public void save(Object comp) {

	}

	@Override
	public void update(Object comp) {

	}

	@Override
	public void remove(String bizId) {

	}

	@Override
	public Object getAssociatedValue(String bizId) {
		
		return null;
	}

}
