package com.nway.wform.access;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nway.wform.access.component.MultiValueComponent;
import com.nway.wform.access.event.EventPublisher;
import com.nway.wform.access.event.FieldGroupEvent;
import com.nway.wform.access.event.FormEvent;
import com.nway.wform.access.event.FormPageEvent;
import com.nway.wform.access.mybatis.TemporaryStatementRegistry;
import com.nway.wform.design.entity.Field;
import com.nway.wform.design.entity.FieldGroup;
import com.nway.wform.design.entity.FormPage;

@Component
public class ModuleDataAccess {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	public void update(FormPage formPage, Map<String, Map<String, Object>> formData) {
		
		EventPublisher.publishFormDataEvent(new FormPageEvent(formPage, formData, FormEvent.EVENT_HANDLE_TYPE_BEFORE));

		for(FieldGroup group : formPage.getComponentGroups()) {
			
			EventPublisher.publishFormDataEvent(new FieldGroupEvent(group, formData.get(group.getName()), FormEvent.EVENT_HANDLE_TYPE_BEFORE));
			
			if(formPage.getPageType() == FormPage.PAGE_TYPE_EDIT && group.isEditable()) {
				
				sqlSessionTemplate.update(TemporaryStatementRegistry.getLastestName(group.getFullName()), formData);
			}
			else if(formPage.getPageType() == FormPage.PAGE_TYPE_CREATE && group.isEditable()) {
				
				sqlSessionTemplate.insert(TemporaryStatementRegistry.getLastestName(group.getFullName()), formData);
			}
			
			// 子表操作
			for(Field field : group.getFields()) {
				
				if(MultiValueComponent.class.isInstance(field)) {
					if(formPage.getPageType() == FormPage.PAGE_TYPE_CREATE && field.isEditable()) {
						
					}
					else if(formPage.getPageType() == FormPage.PAGE_TYPE_EDIT && field.isEditable()) {
						
					}
				}
			}
			
			EventPublisher.publishFormDataEvent(new FieldGroupEvent(group, formData.get(group.getName()), FormEvent.EVENT_HANDLE_TYPE_AFTER));
		}
		
		EventPublisher.publishFormDataEvent(new FormPageEvent(formPage, formData, FormEvent.EVENT_HANDLE_TYPE_AFTER));
	}
	
	public Map<String,Object> get(FormPage formPage, Map<String, Map<String, Object>> param) {
		
		Map<String,Object> pageData = new HashMap<String,Object>();
		
		EventPublisher.publishFormDataEvent(new FormPageEvent(formPage, param, FormEvent.EVENT_HANDLE_TYPE_BEFORE));

		for(FieldGroup group : formPage.getComponentGroups()) {
			
			EventPublisher.publishFormDataEvent(new FieldGroupEvent(group, param.get(group.getName()), FormEvent.EVENT_HANDLE_TYPE_BEFORE));
			
			if(formPage.getPageType() == FormPage.PAGE_TYPE_DETAILS) {
			
				Map<String, Object> groupData = sqlSessionTemplate.selectOne(TemporaryStatementRegistry.getLastestName(group.getFullName()), param);
				
				// 子表操作
				for(Field field : group.getFields()) {
					
					if(MultiValueComponent.class.isInstance(field)) {
						
					}
				}
				
				pageData.put(group.getName(), groupData);
			}
			else if(formPage.getPageType() == FormPage.PAGE_TYPE_LIST) {
			
				List<Map<String, Object>> groupData = sqlSessionTemplate.selectList(TemporaryStatementRegistry.getLastestName(group.getFullName()), param);
				
				// 子表操作
				for(Field field : group.getFields()) {
					
					if(MultiValueComponent.class.isInstance(field)) {
						
						
					}
				}
				
				pageData.put(group.getName(), groupData);
			}
			
			EventPublisher.publishFormDataEvent(new FieldGroupEvent(group, param.get(group.getName()), FormEvent.EVENT_HANDLE_TYPE_AFTER));
		}
		
		EventPublisher.publishFormDataEvent(new FormPageEvent(formPage, param, FormEvent.EVENT_HANDLE_TYPE_AFTER));
		
		return pageData;
	}
	
}
