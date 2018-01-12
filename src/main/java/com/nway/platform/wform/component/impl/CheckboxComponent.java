package com.nway.platform.wform.component.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nway.platform.wform.component.Initializable;
import com.nway.platform.wform.component.MultiValueComponent;
import com.nway.platform.wform.component.impl.dao.CheckboxMapper;

@Component("checkbox")
public class CheckboxComponent implements MultiValueComponent, Initializable {

	@Autowired
	private CheckboxMapper checkboxMapper;
	
	@Override
	public Object getValue(Object value) {
		
		if(value instanceof String) {
			
			return Arrays.asList(value);
		}
		
		return value;
	}

	@Override
	public void save(String pageName, String bizId, Object compValue) {

		checkboxMapper.clear(pageName, bizId);
		
		checkboxMapper.save(pageName, bizId, compValue);
	}

	@Override
	public void update(String pageName, String bizId, Object compValue) {

		save(pageName, bizId, compValue);
	}

	@Override
	public void remove(String pageName, String bizId) {

		checkboxMapper.clear(pageName, bizId);
	}

	@Override
	public Object getAssociatedValue(String pageName, String bizId) {
		
		return checkboxMapper.getValues(pageName, bizId);
	}

	@Override
	public Object init(String key) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("1001", "Java");
		map.put("1002", "C++");
		
		return map;
	}
}
