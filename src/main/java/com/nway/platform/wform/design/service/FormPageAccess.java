package com.nway.platform.wform.design.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nway.platform.wform.access.component.ComponentRegister;
import com.nway.platform.wform.access.dao.FormPageMapper;
import com.nway.platform.wform.design.entity.FormPage;
import com.nway.platform.wform.design.entity.PageField;

@Component
public class FormPageAccess {

	@Autowired
	private FormPageMapper formPageMapper;
	
	@Autowired
	private ComponentRegister componentRegister;
	
	public FormPage getFormPage(String id) {
		
		FormPage page = formPageMapper.getFormPage(id);
		
 		for (PageField field : page.getFields()) {

			field.setObjType(componentRegister.getComponent(field.getType()));
		}
		
		return page;
	}
	
	public Map<String, Map<String, String>> listFieldAttr(String groupId) {		
		
		List<Map<String, String>> attrOrgin = formPageMapper.listFieldAttr(groupId);
		
		Map<String, Map<String, String>> retVal = new HashMap<String,Map<String,String>>();
		
		for(Map<String, String> row : attrOrgin) {
			
			Map<String, String> groupRow = retVal.get(row.get("fieldName"));
			
			if(groupRow != null) {
				
				groupRow.put(row.get("attrName"), row.get("attrValue"));
			}
			else {
				
				groupRow = new HashMap<String, String>();
				
				groupRow.put(row.get("attrName"), row.get("attrValue"));
				
				retVal.put(row.get("fieldName"), groupRow);
			}
		}
		
		return retVal;
	}
}
