package com.nway.platform.wform.view.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nway.platform.wform.access.FormDataAccess;
import com.nway.platform.wform.commons.SpringContextUtil;
import com.nway.platform.wform.design.entity.FormPage;
import com.nway.platform.workflow.entity.Handle;
import com.nway.platform.workflow.event.TaskCompleteEvent;
import com.nway.platform.workflow.service.WorkFlowService;

@Service
public class FormPageService {

	@Autowired
	private FormDataAccess formDataAccess;
	
	@Autowired
	private WorkFlowService workFlowService;
	
	public void createAndStartProcess(FormPage page, Handle handleInfo, Map<String, Map<String, Object>> formData) {
		
		if (handleInfo.getProcessKey() != null) {
			
			String pid = workFlowService.startProcess(handleInfo);
			
			SpringContextUtil.publishEvent(new TaskCompleteEvent(handleInfo, page, formData));

			for (Map<String, Object> groupData : formData.values()) {

				groupData.put("processInstanceId", pid);
			}
		}
		
		formDataAccess.create(page, formData);
	}
	
	public void saveAndHandle(FormPage page, Handle handleInfo, Map<String, Map<String, Object>> formData) {
		
		if (handleInfo.getProcessKey() != null) {
			
			workFlowService.handleTask(handleInfo);
			
			SpringContextUtil.publishEvent(new TaskCompleteEvent(handleInfo, page, formData));
		}
		
		if(FormPage.PAGE_TYPE_EDIT.equals(page.getPageType())) {
			
			formDataAccess.update(page, formData);
		}
	}
}
