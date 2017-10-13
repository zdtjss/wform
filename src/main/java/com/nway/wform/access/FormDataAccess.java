package com.nway.wform.access;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.nway.wform.access.event.EventPublisher;
import com.nway.wform.access.event.FormDataEvent;
import com.nway.wform.design.entity.ComponentGroup;
import com.nway.wform.design.entity.FormPage;

@Component
public class FormDataAccess {

	public void save(FormPage formPage, List<Map<String, String>> formData) {
		
		for(ComponentGroup group : formPage.getComponentGroups()) {
			
			EventPublisher.publishFormDataEvent(new FormDataEvent(group, formData, FormDataEvent.EVENT_HANDLE_TYPE_BEFORE,
					FormDataEvent.EVENT_TYPE_INSERT));
			
			// 手动控制数据库操作
			if(group.isManual()) {
				
				
			}
			else {
				
				
			}
			
			EventPublisher.publishFormDataEvent(new FormDataEvent(group, formData,
					FormDataEvent.EVENT_HANDLE_TYPE_AFTER, FormDataEvent.EVENT_TYPE_INSERT));
		}
	}
}
