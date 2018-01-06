package com.nway.wform;

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
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class ActivitiTest {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private HistoryService historyService;
	
	@Test
	@Deployment
	public void simpleProcessTest() {
		
		Map<String,Object> var = new HashMap<String,Object>();
		
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("designByExplorer");
		
		var.put("outcome", "dy");
		var.put("title", "check");
		
		// 草稿
		Task firstTask = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
		 
		List<Task> nextTasks = handleTask(pi.getId(), firstTask.getId(), var);
		
		List<Task> nextTasks2 = null;
		
		// 审批、核查
		for(Task task : nextTasks) {
			
			nextTasks2 = handleTask(pi.getId(), task.getId(), var);
		}
		
		List<Task> nextTasks3 = null;
		
		// 处理
		for(Task task : nextTasks2) {
			
			nextTasks3 = handleTask(pi.getId(), task.getId(), var);
		}
		
		for(Task task : nextTasks3) {
			
			handleTask(pi.getId(), task.getId(), var);
		}

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(pi.getId()).singleResult();  
        
        System.out.println("流程结束时间是: " + historicProcessInstance.getEndTime());  
	}
	
	public Map<String, Object> history(String pid, String pdId) {
		
		Map<String, Object> position = new HashMap<String, Object>();
		
		List<HistoricActivityInstance> his = historyService.createHistoricActivityInstanceQuery().processInstanceId(pid).list();
		
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(pdId);
		
		for(HistoricActivityInstance h : his) {
			
			if(!"userTask".equals(h.getActivityType())) {
				continue;
			}
			
			ActivityImpl activityImpl = processDefinitionEntity.findActivity(h.getActivityId());
			
			position.put("taskId", h.getTaskId());
			position.put("x", activityImpl.getX());
			position.put("y", activityImpl.getY());
			position.put("width", activityImpl.getWidth());
			position.put("height", activityImpl.getHeight());

		}
		
		System.out.println(JSONObject.toJSONString(position));
		
		return position;
	}
	
	private List<Task> handleTask(String pid, String taskId,  Map<String, Object> variables) {
		
		Task task = taskService.createTaskQuery().processInstanceId(pid).taskId(taskId).singleResult();
		
		System.out.println(task.getId() + " \t " + task.getName());
		
		findOutComeListByTaskId(task);
		
		history(task.getProcessInstanceId(), task.getProcessDefinitionId());
		
		if(variables != null) {
			
			taskService.complete(task.getId(), variables);
		}
		else {
			
			taskService.complete(task.getId());
		}
		
		return taskService.createTaskQuery().processInstanceId(pid).list();
	}
	
	public void outcomeTest() {
		
		//List<String> outcomes = workFlowService.findOutComeListByTaskId(taskId);
	}
	
	// 一直任务ID，查询processDefinitionEntity对象，从而获取
    // 当前任务完成之后的连线名称，并设置到List<String>对象中
    public List<String> findOutComeListByTaskId(Task task) {
    	
        // 返回连线的名称集合
        List<String> list = new ArrayList<String>();

        // 2.获取流程定义ID
        String processDefinitionId = task.getProcessDefinitionId();
		// 3.查询流程定义（ProcessDefinitionEntity）的实体对象
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
        // 使用任务对象Task获取流程实例ID
        String processInstanceId = task.getProcessInstanceId();
        // 使用历程实例ID，查询正在执行的对象表，返回流程实例对象
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

        // 4.获取当前的活动
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(pi.getActivityId());
		
		String activityId = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult().getActivityId();
		
		activityImpl = processDefinitionEntity.findActivity(activityId);
		
		List<PvmTransition> transitions = nextOutcome(activityImpl);

		for(PvmTransition trans:transitions) {
			
			System.out.println(trans.getProperty("name"));
		}
		
		return list;
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
					
					return ac.getOutgoingTransitions();
				}
				else {
					
					System.out.println(ac.getProperty("type"));
				}
			}
		}

		return outcome;
	}
}
