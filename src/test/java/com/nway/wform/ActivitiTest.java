package com.nway.wform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.javax.el.ExpressionFactory;
import org.activiti.engine.impl.javax.el.ValueExpression;
import org.activiti.engine.impl.juel.ExpressionFactoryImpl;
import org.activiti.engine.impl.juel.SimpleContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.StringUtils;
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
	
	@Autowired
	private ProcessEngine processEngine;
	
	@Test
	@Deployment
	public void simpleProcessTest() {
		
		Map<String,Object> var = new HashMap<String,Object>();
		
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("designByExplorer");
		
		var.put("outcome", "dy");
		var.put("title", "check");
		
		// 草稿
		Task firstTask = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
		 
		var.put("tt", firstTask.getId());
		
		nextHandler(firstTask, "多选一", var);
		
		List<Task> nextTasks = handleTask(pi.getId(), firstTask.getId(), var);
		
		List<Task> nextTasks2 = null;
		
		// 审批、核查
		for(Task task : nextTasks) {
			
			//goBack(task.getId());
			findOutComeListByTaskId(task);
			
			nextTasks2 = taskService.createTaskQuery().processInstanceId(pi.getId()).list();//handleTask(pi.getId(), task.getId(), var);
		}		
		
		List<Task> nextTasks3 = null;
		
		// 处理
		for(Task task : nextTasks2) {
			
			nextTasks3 = handleTask(pi.getId(), task.getId(), var);
		}
		
		for(Task task : nextTasks3) {
			
			var.put("outcome", "ry2");
			
			handleTask(pi.getId(), task.getId(), var);
			
			task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
			
			goBack(task.getId());
			
			nextTasks2 = taskService.createTaskQuery().processInstanceId(pi.getId()).list();
		}
		
		for(Task task : nextTasks2) {
			
			var.put("outcome", "ry2");
			
			handleTask(pi.getId(), task.getId(), var);
		}
		
		//taskService.createTaskQuery().processInstanceId(pi.getId()).list()

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(pi.getId()).singleResult();  
        
        System.out.println("流程结束时间是: " + historicProcessInstance.getEndTime());  
	}
	
	@Test
	public void getFirstActivity() {
		
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("designByExplorer").latestVersion().singleResult();
		
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
		
		List<String> firstLevelTaskNames = new ArrayList<String>();
		
		for(FlowElement flowElement : bpmnModel.getMainProcess().getFlowElements()) {
			
			if(flowElement instanceof StartEvent) {
				
				for(SequenceFlow sequenceFlow : ((StartEvent) flowElement).getOutgoingFlows()) {
					
					sequenceFlow.getName();
					
					firstLevelTaskNames.add(sequenceFlow.getTargetRef());
				}
				
				break;
			}
		}
		
		for(FlowElement flowElement : bpmnModel.getMainProcess().getFlowElements()) {
			
			if(flowElement instanceof UserTask) {
				
				((UserTask)flowElement).getAssignee();
			}
		}
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
	
	private List<Task> handleTask(String pid, String taskId,  Map<String, Object> variables) {
		
		Task task = taskService.createTaskQuery().processInstanceId(pid).taskId(taskId).singleResult();
		
		System.out.println(task.getId() + " \t " + task.getName());
		
		findOutComeListByTaskId(task);
		
		//history(task.getProcessInstanceId(), task.getProcessDefinitionId());
		
		try {
		if(variables != null) {
			
			taskService.complete(task.getId(), variables);
		}
		else {
			
			taskService.complete(task.getId());
		}
		}catch(Exception e) {
			
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
	
	private Map<String, String> nextHandler(Task task, String outcome, Map<String, Object> var) {
		
		Map<String, String> retVal = new HashMap<String, String>();
		
		String processDefinitionId = task.getProcessDefinitionId();
		
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);

		String activityId = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult().getActivityId();
		
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
		
		List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();
		
		for(PvmTransition pvmTransition : pvmTransitions) {
			
			if(!outcome.equals(pvmTransition.getProperty("name"))) {
				
				continue;
			}
			
			PvmActivity pvmActivity = pvmTransition.getDestination();
			
			if("userTask".equals(pvmActivity.getProperty("type"))) {
				
				getTaskAssignee(pvmActivity, retVal);
				
				return retVal;
			}
			else if ("exclusiveGateway".equals(pvmActivity.getProperty("type"))) {
			
				for(PvmTransition exclusivePvmTransition : pvmActivity.getOutgoingTransitions()) {
					
					pvmActivity = exclusivePvmTransition.getDestination();
					
					String conditionText = exclusivePvmTransition.getProperty("conditionText").toString();
					
					if (isCondition(conditionText, var) && "userTask".equals(pvmActivity.getProperty("type"))) {

						getTaskAssignee(pvmActivity, retVal);
						
						return retVal;
					}
				}
			}
			else if("parallelGateway".equals(pvmActivity.getProperty("type"))) {
				
				for (PvmTransition exclusivePvmTransition : pvmActivity.getOutgoingTransitions()) {

					pvmActivity = exclusivePvmTransition.getDestination();
					
					if("userTask".equals(pvmActivity.getProperty("type"))) {
						
						getTaskAssignee(pvmActivity, retVal);
					}
				}
				
				return retVal;
			}
			else if ("inclusiveGateway".equals(pvmActivity.getProperty("type"))) {
				
				for(PvmTransition exclusivePvmTransition : pvmActivity.getOutgoingTransitions()) {
					
					pvmActivity = exclusivePvmTransition.getDestination();
					
					String conditionText = (String) exclusivePvmTransition.getProperty("conditionText");

					if (conditionText != null) {
						
						if (isCondition(conditionText, var) && "userTask".equals(pvmActivity.getProperty("type"))) {

							getTaskAssignee(pvmActivity, retVal);
						}
					}
					else if("userTask".equals(pvmActivity.getProperty("type"))) {
						
						getTaskAssignee(pvmActivity, retVal);
					}
				}
				
				return retVal;
			}
		}
		
		return retVal;
	}
	
	private void getTaskAssignee(PvmActivity pvmActivity, Map<String, String> taskAssignee) {
		
		TaskDefinition taskDefinition = ((UserTaskActivityBehavior) ((ActivityImpl) pvmActivity).getActivityBehavior()).getTaskDefinition();
		
		Expression nameExpression = taskDefinition.getNameExpression();
		
		Expression assigneeExpression = taskDefinition.getAssigneeExpression();
		
		taskAssignee.put(nameExpression != null ? nameExpression.getExpressionText() : "", assigneeExpression != null ? assigneeExpression.getExpressionText() : "");
	}

	/**
	 * 下一个任务节点信息,
	 * 
	 * 如果下一个节点为用户任务则直接返回,
	 * 
	 * 如果下一个节点为排他网关, 获取排他网关Id信息, 根据排他网关Id信息和execution获取流程实例排他网关Id为key的变量值,
	 * 根据变量值分别执行排他网关后线路中的el表达式, 并找到el表达式通过的线路后的用户任务信息
	 * 
	 * @param ActivityImpl
	 *            activityImpl 流程节点信息
	 * @param String
	 *            activityId 当前流程节点Id信息
	 * @param String
	 *            elString 排他网关顺序流线段判断条件, 例如排他网关顺序留线段判断条件为${money>1000},
	 *            若满足流程启动时设置variables中的money>1000, 则流程流向该顺序流信息
	 * @param String
	 *            processInstanceId 流程实例Id信息
	 * @return
	 */
	private TaskDefinition nextTaskDefinition(ActivityImpl activityImpl, String activityId, String elString,
			String processInstanceId) {

		PvmActivity ac = null;

		Object s = null;

		// 如果遍历节点为用户任务并且节点不是当前节点信息
		if ("userTask".equals(activityImpl.getProperty("type")) && !activityId.equals(activityImpl.getId())) {
			// 获取该节点下一个节点信息
			TaskDefinition taskDefinition = ((UserTaskActivityBehavior) activityImpl.getActivityBehavior())
					.getTaskDefinition();
			return taskDefinition;
		} else {
			// 获取节点所有流向线路信息
			List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
			List<PvmTransition> outTransitionsTemp = null;
			for (PvmTransition tr : outTransitions) {
				ac = tr.getDestination(); // 获取线路的终点节点
				// 如果流向线路为排他网关
				if ("exclusiveGateway".equals(ac.getProperty("type"))) {
					outTransitionsTemp = ac.getOutgoingTransitions();

					// 如果网关路线判断条件为空信息
					if (StringUtils.isEmpty(elString)) {
						// 获取流程启动时设置的网关判断条件信息
						elString = getGatewayCondition(ac.getId(), processInstanceId);
					}

					// 如果排他网关只有一条线路信息
					if (outTransitionsTemp.size() == 1) {
						return nextTaskDefinition((ActivityImpl) outTransitionsTemp.get(0).getDestination(), activityId,
								elString, processInstanceId);
					} else if (outTransitionsTemp.size() > 1) { // 如果排他网关有多条线路信息
						for (PvmTransition tr1 : outTransitionsTemp) {
							s = tr1.getProperty("conditionText"); // 获取排他网关线路判断条件信息
							// 判断el表达式是否成立
							/*if (isCondition(ac.getId(), StringUtils.trim(s.toString()), elString)) {
								return nextTaskDefinition((ActivityImpl) tr1.getDestination(), activityId, elString,
										processInstanceId);
							}*/
						}
					}
				} else if ("userTask".equals(ac.getProperty("type"))) {
					return ((UserTaskActivityBehavior) ((ActivityImpl) ac).getActivityBehavior()).getTaskDefinition();
				} else {
				}
			}
			return null;
		}
	}

	/**
	 * 查询流程启动时设置排他网关判断条件信息
	 * 
	 * @param String
	 *            gatewayId 排他网关Id信息, 流程启动时设置网关路线判断条件key为网关Id信息
	 * @param String
	 *            processInstanceId 流程实例Id信息
	 * @return
	 */
	public String getGatewayCondition(String gatewayId, String processInstanceId) {
		Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();
		return runtimeService.getVariable(execution.getId(), gatewayId).toString();
	}

	/**
	 * 根据key和value判断el表达式是否通过信息
	 * 
	 * @param String
	 *            key el表达式key信息
	 * @param String
	 *            el el表达式信息
	 * @param String
	 *            value el表达式传入值信息
	 * @return
	 */
	public boolean isCondition(String el, Map<String, Object> var) {
		
		ExpressionFactory factory = new ExpressionFactoryImpl();
		
		SimpleContext context = new SimpleContext();
		
		for (Entry<String, Object> entry : var.entrySet()) {
			
			context.setVariable(entry.getKey(), factory.createValueExpression(entry.getValue(), String.class));
		}
		
		ValueExpression e = factory.createValueExpression(context, el, boolean.class);
		
		return (Boolean) e.getValue(context);
	}
}
