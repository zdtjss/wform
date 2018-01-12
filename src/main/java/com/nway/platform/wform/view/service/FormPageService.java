package com.nway.platform.wform.view.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.task.Task;
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
	
	public void createAndStartProcess(FormPage page, Handle handleInfo, Map<String, Object> formData) {
		
		if (handleInfo.getProcessKey() != null) {
			
			String pid = workFlowService.startProcess(handleInfo);
			
			formData.put("processInstanceId", pid);
			
			SpringContextUtil.publishEvent(new TaskCompleteEvent(handleInfo, page, formData));
		}
		
		formDataAccess.create(page, formData);
	}
	
	public void handle(FormPage page, Handle handleInfo, Map<String, Object> formData) {
		
		if (handleInfo.getTaskId() != null) {
			
			String pid = workFlowService.getTask(handleInfo.getTaskId()).getProcessInstanceId();
			
			workFlowService.handleTask(handleInfo);
			
			formData.put("processInstanceId", pid);
			
			SpringContextUtil.publishEvent(new TaskCompleteEvent(handleInfo, page, formData));
		}
		
	}
	
	public void saveAndHandle(FormPage page, Handle handleInfo, Map<String, Object> formData) {
		
		handle(page, handleInfo, formData);
		
		if(FormPage.PAGE_TYPE_EDIT.equals(page.getType())) {
			
			formDataAccess.update(page, formData);
		}
	}

	public Set<String> findOutcomeNameListByTaskId(String taskId) {
		
		List<PvmTransition> pvmTransitionList = workFlowService.findOutcomeByTaskId(taskId);
		
		Set<String> transitionNames = new HashSet<String>(pvmTransitionList.size());
		
		for (PvmTransition transition : pvmTransitionList) {
			
			transitionNames.add((String) transition.getProperty("name"));
		}
		
		return transitionNames;
	}
}
