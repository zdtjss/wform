package com.nway.wform.access;

import java.util.HashMap;
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
			
			for(Map<String, String> attr : group.getFieldAttr()) {
				
				
				// groupFieldAttr.put(key, value)
			}
		}
		
		return page;
	}
}
