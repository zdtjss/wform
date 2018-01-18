package com.nway.platform.wform.component.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nway.platform.wform.component.Initializable;
import com.nway.platform.wform.component.MultiValueComponent;
import com.nway.platform.wform.component.impl.dao.MultiSelectMapper;

@Component("multiSelect")
public class MultiSelectComponent implements MultiValueComponent, Initializable {

	@Autowired
	private MultiSelectMapper multiSelectMapper;
	
	@Override
	public Object getValue(Object value) {
		
		if(value instanceof String) {
			
			return Arrays.asList(value);
		}
		
		return value;
	}

	@Override
	public void save(String pageName, String bizId, Object compValue) {

		multiSelectMapper.clear(pageName, bizId);
		
		if(compValue != null) {
			
			multiSelectMapper.save(pageName, bizId, compValue);
		}
	}

	@Override
	public void update(String pageName, String bizId, Object compValue) {

		save(pageName, bizId, compValue);
	}

	@Override
	public void remove(String pageName, String bizId) {

		multiSelectMapper.clear(pageName, bizId);
	}

	@Override
	public Object getAssociatedValue(String pageName, String bizId) {
		
		List<String> retVal = new ArrayList<String>();
		
		List<Map<String, String>> values = multiSelectMapper.getValues(pageName, bizId);
		
		for(Map<String, String> value : values) {
			
			retVal.addAll(value.values());
		}
		
		return retVal;
	}

	@Override
	public Object init(String pageId, String fieldName) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("1001", "Java");
		map.put("1002", "C++");
		
		return map;
	}
}
