package com.nway.platform.wform.component.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.nway.platform.wform.component.BaseComponent;
import com.nway.platform.wform.component.Initializable;
import com.nway.platform.wform.design.service.FormPageAccess;

@Component("singleSelect")
public class SingleSelectComponent implements BaseComponent, Initializable {

	@Autowired
	private FormPageAccess formPageAccess;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Object getValue(Object value) {

		return value;
	}

	@Override
	public Object init(String pageId, String fieldName) {

		Map<String, Object> map = new HashMap<String, Object>();
		
		Map<String, Map<String, String>> fieldAttrsMap = formPageAccess.listFieldAttr(pageId);
		
		if (fieldAttrsMap != null) {
			
			Map<String, String> attrMap = fieldAttrsMap.get(fieldName);
			
			String sql = attrMap.get("sql");

			if (sql != null) {

				map = reform(jdbcTemplate.queryForList(sql));
			}

			String dic = attrMap.get("dic");

			if (dic != null) {

				map = reform(jdbcTemplate.queryForList("SELECT CODE key,VALUE FROM T_DICTIONARY WHERE PARENT_CODE = ?", dic));
			}
		}
		
		return map;
	}

	
	private Map<String, Object> reform(List<Map<String, Object>> orgin) {
		
		Map<String, Object> kvMap = new HashMap<String, Object>();
		
		
		for(Map<String, Object> per : orgin) {
			
			kvMap.put(per.get("KEY").toString(), per.get("VALUE"));
		}
		
		return kvMap;
	}
}
