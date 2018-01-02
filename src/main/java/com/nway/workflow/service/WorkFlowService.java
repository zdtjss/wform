package com.nway.workflow.service;

import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public void handleTask(String pid) {
		
		handleTask(pid, null);
	}
	
	public void handleTask(String pid,  Map<String, Object> variables) {
		
		Task task = taskService.createTaskQuery().processInstanceId(pid).active().singleResult();
		
		if(variables != null) {
			
			taskService.complete(task.getId(), variables);
		}
		else {
			
			taskService.complete(task.getId());
		}
	}
}
