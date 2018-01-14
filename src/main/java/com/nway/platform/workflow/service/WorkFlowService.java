package com.nway.platform.workflow.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
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
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nway.platform.workflow.entity.Handle;

@Service
public class WorkFlowService {

	@Autowired
	private ProcessEngine processEngine;
	
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private HistoryService historyService;
	
	public String startProcess(String pdKey) {
		
		return runtimeService.startProcessInstanceByKey(pdKey).getId();
	}
	
	public void handleTask(Handle handleInfo) {
		
		Task task = taskService.createTaskQuery().taskId(handleInfo.getTaskId()).singleResult();
		
		// 退回的连线条件${outcome == 'back'}
		if(Handle.Action.FORWARD.equals(handleInfo.getAction())) {
			
			task.setDescription("同意");
			
			taskService.complete(task.getId(), handleInfo.getVariables());
		}
		else if(Handle.Action.BACK.equals(handleInfo.getAction())) {
			
			task.setDescription("退回");
			
			handleInfo.getVariables().put("outcome", "back");
			
			goBack(handleInfo.getTaskId());
		}
		
	}
	
	public List<PvmTransition> findOutcomeByTaskId(String taskId, Map<String, Object> param) {
		
		List<PvmTransition> retVal = new ArrayList<PvmTransition>();
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		ActivityImpl activityImpl = getActivityImplByTask(task);
		
		for (PvmTransition pvmTransition : activityImpl.getOutgoingTransitions()) {

			String conditionText = (String) pvmTransition.getProperty("conditionText");

			if (param.isEmpty() || conditionText == null || isCondition(conditionText, param)) {

				retVal.add(pvmTransition);
			}
		}
		
		return retVal;
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
	
	public List<Task> getTaskByPid(String processInstanceId) {
		
		return taskService.createTaskQuery().processInstanceId(processInstanceId).list();
	}
	
	public List<Task> getCurentTasks(String pid) {
		
		return taskService.createTaskQuery().processInstanceId(pid).list();
	}
	
	public InputStream getDiagram(String taskId) {
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
		
		ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
		
		processEngineConfiguration.setActivityFontName("宋体");
		processEngineConfiguration.setAnnotationFontName("宋体");
		processEngineConfiguration.setLabelFontName("宋体");
		processEngineConfiguration.setCreateDiagramOnDeploy(true);
		//processEngineConfiguration.setProcessDiagramGenerator(new CustomProcessDiagramGenerator());
		
        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);

        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(task.getProcessDefinitionId());

        List<HistoricActivityInstance> highLightedActivitList =  historyService.createHistoricActivityInstanceQuery().processInstanceId(task.getProcessInstanceId()).list();
        //高亮环节id集合
        List<String> highLightedActivitis = new ArrayList<String>();
        //高亮线路id集合
        List<String> highLightedFlows = getHighLightedFlows(definitionEntity,highLightedActivitList);

        for(HistoricActivityInstance tempActivity : highLightedActivitList){
            String activityId = tempActivity.getActivityId();
            highLightedActivitis.add(activityId);
        }

        //中文显示的是口口口，设置字体就好了
        return diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivitis, highLightedFlows,"宋体","宋体","宋体", getClass().getClassLoader(), 1.0);

		/*ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();

		String diagramResourceName = processDefinition.getDiagramResourceName();

		return repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), diagramResourceName);*/
		
		/*Command<InputStream> cmd = new ProcessDefinitionDiagramCmd(task.getProcessDefinitionId());

        return processEngine.getManagementService().executeCommand(cmd);*/
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

	public Map<String, String> nextAssignee(String taskId, String outcome, Map<String, Object> var) {
		
		Map<String, String> retVal = new HashMap<String, String>();
		
		Task task = getTask(taskId);
		
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

	private boolean isCondition(String el, Map<String, Object> var) {
		
		ExpressionFactory factory = new ExpressionFactoryImpl();
		
		SimpleContext context = new SimpleContext();
		
		for (Entry<String, Object> entry : var.entrySet()) {
			
			context.setVariable(entry.getKey(), factory.createValueExpression(entry.getValue(), String.class));
		}
		
		ValueExpression e = factory.createValueExpression(context, el, boolean.class);
		
		return (Boolean) e.getValue(context);
	}
	
	private void getTaskAssignee(PvmActivity pvmActivity, Map<String, String> taskAssignee) {
		
		TaskDefinition taskDefinition = ((UserTaskActivityBehavior) ((ActivityImpl) pvmActivity).getActivityBehavior()).getTaskDefinition();
		
		Expression nameExpression = taskDefinition.getNameExpression();
		
		Expression assigneeExpression = taskDefinition.getAssigneeExpression();
		
		taskAssignee.put(nameExpression != null ? nameExpression.getExpressionText() : "", assigneeExpression != null ? assigneeExpression.getExpressionText() : "");
	}
	
	/**
     * 获取需要高亮的线
     * @param processDefinitionEntity
     * @param historicActivityInstances
     * @return
     */
	private List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity,
			List<HistoricActivityInstance> historicActivityInstances) {
		List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId
		for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历
			ActivityImpl activityImpl = processDefinitionEntity
					.findActivity(historicActivityInstances.get(i).getActivityId());// 得到节点定义的详细信息
			List<ActivityImpl> sameStartTimeNodes = new ArrayList<ActivityImpl>();// 用以保存后需开始时间相同的节点
			ActivityImpl sameActivityImpl1 = processDefinitionEntity
					.findActivity(historicActivityInstances.get(i + 1).getActivityId());
			// 将后面第一个节点放在时间相同节点的集合里
			sameStartTimeNodes.add(sameActivityImpl1);
			for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
				HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
				HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点
				if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) {
					// 如果第一个节点和第二个节点开始时间相同保存
					ActivityImpl sameActivityImpl2 = processDefinitionEntity
							.findActivity(activityImpl2.getActivityId());
					sameStartTimeNodes.add(sameActivityImpl2);
				} else {
					// 有不相同跳出循环
					break;
				}
			}
			List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();// 取出节点的所有出去的线
			for (PvmTransition pvmTransition : pvmTransitions) {
				// 对所有的线进行遍历
				ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();
				// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
				if (sameStartTimeNodes.contains(pvmActivityImpl)) {
					highFlows.add(pvmTransition.getId());
				}
			}
		}
		return highFlows;
	}
}
