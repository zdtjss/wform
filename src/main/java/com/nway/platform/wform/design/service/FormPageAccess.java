package com.nway.platform.wform.design.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nway.platform.wform.access.dao.FormPageMapper;
import com.nway.platform.wform.component.ComponentRegister;
import com.nway.platform.wform.component.MultiValueComponent;
import com.nway.platform.wform.component.impl.KeyComponent;
import com.nway.platform.wform.design.entity.Field;
import com.nway.platform.wform.design.entity.FormPage;
import com.nway.platform.wform.design.entity.PageFieldForm;
import com.nway.platform.wform.design.entity.PageFieldList;

@Component
public class FormPageAccess {

	@Autowired
	private FormPageMapper formPageMapper;
	
	@Autowired
	private ComponentRegister componentRegister;
	
	public FormPage getFormPage(String id) {
		
		FormPage page = formPageMapper.getFormPage(id);
		
 		for (PageFieldForm field : page.getFormFields()) {

			field.setObjType(componentRegister.getComponent(field.getType()));
			
			if(KeyComponent.class.isInstance(field.getObjType())) {
				
				page.setKeyName(field.getName());
			}
			
			if(MultiValueComponent.class.isInstance(field.getObjType())) {
				
				field.setMultiValue(true);
			}
		}
 		
 		for (PageFieldList field : page.getListFields()) {
 			
 			field.setObjType(componentRegister.getComponent(field.getType()));
 			
 			if(MultiValueComponent.class.isInstance(field.getObjType())) {
				
				field.setMultiValue(true);
			}
 		}
 		
		
		return page;
	}
	
	public Map<String, Map<String, String>> listFieldAttr(String pageId) {		
		
		List<Map<String, String>> attrOrgin = formPageMapper.listFieldAttr(pageId);
		
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
	
	public void saveFields(List<Map<String, String>> fields) {

		for (Map<String, String> field : fields) {

			field.put("fieldId", UUID.randomUUID().toString());
			
			formPageMapper.saveField(field);
		}
	}
	
	public List<Field> listFields(String pageId) {
		
		return formPageMapper.listFields(pageId);
	}
}
