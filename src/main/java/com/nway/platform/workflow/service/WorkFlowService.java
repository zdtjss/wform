package com.nway.platform.workflow.service;

import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nway.platform.workflow.entity.HandleInfo;

@Service
public class WorkFlowService {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private HistoryService historyService;
	
	
	public String startProcess(HandleInfo handleInfo) {
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(handleInfo.getProcessKey());
		
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		
		task.setAssignee(handleInfo.getHandleUsers().get(0).getUserId());
		
		return processInstance.getId();
	}
	
	public void handleTask(HandleInfo handleInfo) {
		
		Task task = taskService.createTaskQuery().processInstanceId(handleInfo.getProcessKey()).singleResult();
		
		if(handleInfo.getParams() != null) {
			
			taskService.complete(task.getId(), handleInfo.getParams());
		}
		else {
			
			taskService.complete(task.getId());
		}
	}
	
	public void listOutcome() {
		
		// this.repositoryService.createDeploymentQuery().
	}
}
