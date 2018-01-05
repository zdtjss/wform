package com.nway.platform.workflow.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
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
		
		Map<String, Object> variables = new HashMap<String, Object>();
		
		variables.put("outcome", handleInfo.getOutcome());
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(handleInfo.getProcessKey(), variables);
		
		return processInstance.getId();
	}
	
	public void handleTask(HandleInfo handleInfo) {
		
		Task task = taskService.createTaskQuery().taskId(handleInfo.getTaskId()).singleResult();
		
		taskService.complete(task.getId(), Collections.<String, Object>singletonMap("outcome", handleInfo.getOutcome()));
	}
	
	// 一直任务ID，查询processDefinitionEntity对象，从而获取
    // 当前任务完成之后的连线名称，并设置到List<String>对象中
    public List<String> findOutComeListByTaskId(String taskId) {
        // 返回连线的名称集合
        List<String> list = new ArrayList<String>();

        // 1.使用任务ID查询任务对象
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 2.获取流程定义ID
        String processDefinitionId = task.getProcessDefinitionId();
		// 3.查询流程定义（ProcessDefinitionEntity）的实体对象
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(processDefinitionId);
        // 使用任务对象Task获取流程实例ID
        String processInstanceId = task.getProcessInstanceId();
        // 使用历程实例ID，查询正在执行的对象表，返回流程实例对象
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

        // 4.获取当前的活动
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(pi.getActivityId());

        // 5.获取当前活动完成之后连线的名称
        List<PvmTransition> pvmList = activityImpl.getOutgoingTransitions();
        if (pvmList != null && pvmList.size() > 0) {
            for (PvmTransition pvm : pvmList) {
                String name = (String) pvm.getProperty("name");
                if (name != null) {
                    list.add(name);
                }
            }
        }
        return list;
    }
}
