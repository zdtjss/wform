package com.nway.wform.access;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nway.wform.access.event.ComponentGroupEvent;
import com.nway.wform.access.event.EventPublisher;
import com.nway.wform.access.event.FormEvent;
import com.nway.wform.access.event.FormPageEvent;
import com.nway.wform.access.mybatis.TemporaryStatementRegistry;
import com.nway.wform.design.entity.ComponentGroup;
import com.nway.wform.design.entity.FormPage;

@Component
public class ModuleDataAccess {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	public void update(FormPage formPage, Map<String, String> formData) {
		
		EventPublisher.publishFormDataEvent(new FormPageEvent(formPage, formData, FormEvent.EVENT_HANDLE_TYPE_BEFORE));

		for(ComponentGroup group : formPage.getComponentGroups()) {
			
			EventPublisher.publishFormDataEvent(new ComponentGroupEvent(group, formData, FormEvent.EVENT_HANDLE_TYPE_BEFORE));
			
			sqlSessionTemplate.update(TemporaryStatementRegistry.getLastestName(formPage.getName(), group.getName()), formData);

			EventPublisher.publishFormDataEvent(new ComponentGroupEvent(group, formData, FormEvent.EVENT_HANDLE_TYPE_AFTER));
		}
		
		EventPublisher.publishFormDataEvent(new FormPageEvent(formPage, formData, FormEvent.EVENT_HANDLE_TYPE_AFTER));
	}
	
	public Map<String,Object> get(FormPage formPage, Map<String, String> param) {
		
		Map<String,Object> pageData = new HashMap<String,Object>();
		
		EventPublisher.publishFormDataEvent(new FormPageEvent(formPage, param, FormEvent.EVENT_HANDLE_TYPE_BEFORE));

		for(ComponentGroup group : formPage.getComponentGroups()) {
			
			EventPublisher.publishFormDataEvent(new ComponentGroupEvent(group, param, FormEvent.EVENT_HANDLE_TYPE_BEFORE));
			
			if(!group.isModify()) {
			
				Map<String,Object> groupData = sqlSessionTemplate.selectOne(TemporaryStatementRegistry.getLastestName(formPage.getName(), group.getName()), param);
				
				pageData.put(group.getName(), groupData);
			}

			EventPublisher.publishFormDataEvent(new ComponentGroupEvent(group, param, FormEvent.EVENT_HANDLE_TYPE_AFTER));
		}
		
		EventPublisher.publishFormDataEvent(new FormPageEvent(formPage, param, FormEvent.EVENT_HANDLE_TYPE_AFTER));
		
		return pageData;
	}
	
}
