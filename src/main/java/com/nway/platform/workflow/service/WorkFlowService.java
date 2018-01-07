package com.nway.platform.workflow.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nway.platform.workflow.entity.Handle;

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
	
	public String startProcess(Handle handleInfo) {
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(handleInfo.getProcessKey(), handleInfo.getVariables());
		
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		
		task.setAssignee(handleInfo.getCurrentUser().getCnName());
		
		taskService.complete(task.getId());
		
		return processInstance.getId();
	}
	
	public void handleTask(Handle handleInfo) {
		
		Task task = taskService.createTaskQuery().taskId(handleInfo.getTaskId()).singleResult();
		
		// 退回的连线条件${outcome == 'back'}
		if(Handle.Action.FORWARD.equals(handleInfo.getAction())) {
			
			task.setDescription("同意");
		}
		else if(Handle.Action.BACK.equals(handleInfo.getAction())) {
			
			task.setDescription("退回");
			
			handleInfo.getVariables().put("outcome", "back");
		}
		
		task.setAssignee(handleInfo.getCurrentUser().getCnName());
		
		taskService.complete(task.getId(), handleInfo.getVariables());
	}
	
	public List<PvmTransition> findOutComeListByTaskId(String taskId) {
    	
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		ActivityImpl activityImpl = getActivityImplByTask(task);
		
		return nextOutcome(activityImpl);
    }
	
	public void goBack(String taskId) {

		Map<String, Object> variables;
		
		// 取得当前任务
		HistoricTaskInstance currTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		// 取得流程实例
		ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(currTask.getProcessInstanceId()).singleResult();
		
		if (instance == null) {
			// 流程已经结束
		}
		
		variables = instance.getProcessVariables();
		
		// 取得流程定义
		ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(currTask.getProcessDefinitionId());
		
		if (definition == null) {
			// 流程定义未找到
		}
		
		// 取得上一步活动
		ActivityImpl currActivity = ((ProcessDefinitionImpl) definition).findActivity(currTask.getTaskDefinitionKey());
		
		List<PvmTransition> nextTransitionList = currActivity.getIncomingTransitions();
		
		// 清除当前活动的出口
		List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
		List<PvmTransition> pvmTransitionList = currActivity.getOutgoingTransitions();
		
		for (PvmTransition pvmTransition : pvmTransitionList) {
			
			oriPvmTransitionList.add(pvmTransition);
		}
		
		pvmTransitionList.clear();

		// 建立新出口
		List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
		
		for (PvmTransition nextTransition : nextTransitionList) {
			
			PvmActivity nextActivity = nextTransition.getSource();
			
			ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition).findActivity(nextActivity.getId());
			
			TransitionImpl newTransition = currActivity.createOutgoingTransition();
			
			newTransition.setDestination(nextActivityImpl);
			newTransitions.add(newTransition);
		}
		
		// 完成任务
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(instance.getId()).taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
		
		for (Task task : tasks) {
			
			taskService.complete(task.getId(), variables);
			
			historyService.deleteHistoricTaskInstance(task.getId());
		}
		
		// 恢复方向
		for (TransitionImpl transitionImpl : newTransitions) {
			
			currActivity.getOutgoingTransitions().remove(transitionImpl);
		}
		
		for (PvmTransition pvmTransition : oriPvmTransitionList) {
			
			pvmTransitionList.add(pvmTransition);
		}

	}
	
	private ActivityImpl getActivityImplByTask(Task task) {

		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(task.getProcessDefinitionId());

		String activityId = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult().getActivityId();

		return processDefinitionEntity.findActivity(activityId);
	}
	
	public Task getTask(String taskId) {
		
		return taskService.createTaskQuery().taskId(taskId).singleResult();
	}
	
	public List<Task> getCurentTasks(String pid) {
		
		return taskService.createTaskQuery().processInstanceId(pid).list();
	}
	
	public String getTaskAssignee(String taskId) {
		
		return getTask(taskId).getAssignee();
	}
	
	public InputStream getDiagram(String taskId) {
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
		
		return repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName());
	}
	
	public Map<String, Map<String, Object>> historicHandle(String taskId) {
		
		Map<String, Map<String, Object>> positions = new HashMap<String, Map<String, Object>>();
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		ActivityImpl activityImpl = getActivityImplByTask(task);
		
		Map<String, Object> position1 = getShape(activityImpl);

		position1.put("taskId", task.getId());

		positions.put("current", position1);
		
		List<HistoricActivityInstance> his = historyService.createHistoricActivityInstanceQuery().processInstanceId(task.getProcessInstanceId()).list();
		
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(task.getProcessDefinitionId());
		
		for(HistoricActivityInstance h : his) {
			
			if(!"userTask".equals(h.getActivityType())) {
				continue;
			}
			
			ActivityImpl histActivityImpl = processDefinitionEntity.findActivity(h.getActivityId());
			
			Map<String, Object> position2 = getShape(histActivityImpl);

			position2.put("taskId", h.getTaskId());
			
			positions.put("historic", position2);
		}
		
		return positions;
	}
	
	private Map<String, Object> getShape(ActivityImpl histActivityImpl) {
		
		Map<String, Object> position = new HashMap<String, Object>();
		
		position.put("x", histActivityImpl.getX());
		position.put("y", histActivityImpl.getY());
		position.put("width", histActivityImpl.getWidth());
		position.put("height", histActivityImpl.getHeight());
		
		return position;
	}

	private List<PvmTransition> nextOutcome(ActivityImpl activityImpl) {

		List<PvmTransition> outcome = Collections.emptyList();

		if ("userTask".equals(activityImpl.getProperty("type"))) {

			outcome = activityImpl.getOutgoingTransitions();
		}
		else {
			
			List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
			
			for (PvmTransition tr : outTransitions) {
				
				PvmActivity ac = tr.getDestination();
				
				if ("exclusiveGateway".equals(ac.getProperty("type"))) {
					
					for (PvmTransition tr1 : ac.getOutgoingTransitions()) {
						
						outcome = nextOutcome((ActivityImpl) tr1.getDestination());
					}
				}
				else if ("userTask".equals(ac.getProperty("type"))) {
					
					outcome = ac.getOutgoingTransitions();
				}
				else {
					
					System.out.println(ac.getProperty("type"));
				}
			}
		}

		return outcome;
	}
}
