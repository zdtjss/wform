package com.nway.platform.wform.component.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.nway.platform.wform.component.BaseComponent;
import com.nway.platform.wform.component.Initializable;

@Component("singleSelect")
public class SingleSelectComponent implements BaseComponent, Initializable {

	public Object getValue(Object value) {

		return value;
	}

	@Override
	public Object init(String pageName, String fieldName) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		if (pageName.equals("formPageCreater") && fieldName.equals("PAGE_TYPE")) {
			
			map.put("1001", "个人电脑");
			map.put("1002", "安卓手机");
			map.put("1003", "安卓PAD");
			map.put("1004", "苹果手机");
			map.put("1005", "苹果PAD");
		}
		else if (pageName.equals("formPageCreater") && fieldName.equals("MODULE_ID")) {

			map.put("100101", "测试");
			map.put("100102", "表单设计");
		}
		
		return map;
	}

}
