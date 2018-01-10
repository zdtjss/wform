package com.nway.platform.wform.design.component;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("checkbox")
public class CheckboxComponent implements MultiValueComponent, Initializable {

	@Override
	public Object getValue(String value) {
		
		return value == null || value.length() == 0 ? value.substring(2, value.length() - 2).split("\",\"") : value;
	}

	@Override
	public void save(String pageName, Object comp) {

	}

	@Override
	public void update(String pageName, Object comp) {

	}

	@Override
	public void remove(String pageName, String bizId) {

	}

	@Override
	public Object getAssociatedValue(String pageName, Object value) {
		
		return null;
	}

	@Override
	public Object init(String key) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("1001", "Java");
		map.put("1002", "C++");
		
		return map;
	}
}
