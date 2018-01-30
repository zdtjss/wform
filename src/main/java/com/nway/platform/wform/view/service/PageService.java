package com.nway.platform.wform.view.service;

import java.util.ArrayList;
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
import com.nway.platform.wform.design.entity.Page;
import com.nway.platform.workflow.dao.WorkFlowDaoMapper;
import com.nway.platform.workflow.entity.Handle;
import com.nway.platform.workflow.event.TaskCompleteEvent;
import com.nway.platform.workflow.service.WorkFlowService;

@Service
public class PageService {

	@Autowired
	private FormDataAccess formDataAccess;
	
	@Autowired
	private WorkFlowService workFlowService;
	
	@Autowired
	private WorkFlowDaoMapper workFlowDaoMapper;
	
	public String startProcess(String pageId) {
		
		String pid = "";
		
		String pdKey = workFlowDaoMapper.getPdKey(pageId);
		
		if (pdKey != null) {
			
			pid = workFlowService.startProcess(pdKey);
		}
		
		return pid;
	}
	
	public void saveAndHandle(Page page, String pageType, Handle handleInfo, Map<String, Object> formData) {
		
		if (handleInfo.getTaskId().length() != 0) {
			
			String pid = workFlowService.getTask(handleInfo.getTaskId()).getProcessInstanceId();
			
			workFlowService.handleTask(handleInfo);
			
			formData.put("processInstanceId", pid);
			
			SpringContextUtil.publishEvent(new TaskCompleteEvent(handleInfo, page, formData));
		}
		
		if(Page.PAGE_TYPE_CREATE.equals(pageType)) {
			
			formDataAccess.create(page, formData);
		}
		else if(Page.PAGE_TYPE_EDIT.equals(pageType)) {
			
			formDataAccess.update(page, formData);
		}
		
	}

	public List<String> getTaskIdByPid(String pid) {

		List<String> names = new ArrayList<String>();
		
		for(Task task : workFlowService.getTaskByPid(pid)) {
			
			names.add(task.getId());
		}

		return names;
	}
	
	public Set<String> findOutcomeNameListByTaskId(String taskId, Map<String, Object> param) {
		
		List<PvmTransition> pvmTransitionList = workFlowService.findOutcomeByTaskId(taskId, param);
		
		Set<String> transitionNames = new HashSet<String>(pvmTransitionList.size());
		
		for (PvmTransition transition : pvmTransitionList) {
			
			transitionNames.add((String) transition.getProperty("name"));
		}
		
		return transitionNames;
	}
}
