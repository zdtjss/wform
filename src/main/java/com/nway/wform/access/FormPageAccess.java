package com.nway.wform.access;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nway.wform.access.dao.FormPageMapper;
import com.nway.wform.design.entity.FieldGroup;
import com.nway.wform.design.entity.FormPage;

@Component
public class FormPageAccess {

	@Autowired
	private FormPageMapper formPageMapper;
	
	public FormPage getFormPage(String id) {
		
		FormPage page = formPageMapper.getFormPage(id);
		
		Map<String, Map<String, String>> fieldAttr = new HashMap<String, Map<String, String>>();
		
		for(FieldGroup group : page.getFielsGroups()) {
			
			Map<String, String> groupFieldAttr = fieldAttr.get(group.getName());
			
			if(groupFieldAttr == null) {
				
			}
		}
		
		return page;
	}
	
	public Map<String, Map<String, String>> listFieldAttr(String groupId) {		
		
		List<Map<String, String>>  attrOrgin = formPageMapper.listFieldAttr(groupId);
		
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
