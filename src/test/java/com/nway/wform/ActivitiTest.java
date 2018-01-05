package com.nway.wform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
		
		var.put("outcome", "yq");
		
		// 草稿
		Task firstTask = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
		//findOutComeListByTaskId(firstTask.getId())
			
		List<Task> nextTasks = handleTask(pi.getId(), firstTask.getId(), var);
		
		// 审批、核查
		for(Task task : nextTasks) {
			
			handleTask(pi.getId(), task.getId(), var);
		}
		
		// 处理
		for(Task task : nextTasks) {
			
			handleTask(pi.getId(), task.getId(), var);
		}
		
		for(Task task : nextTasks) {
			
			handleTask(pi.getId(), task.getId(), var);
		}

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(pi.getId()).singleResult();  
        
        System.out.println("流程结束时间是: " + historicProcessInstance.getEndTime());  
	}
	
	private List<Task> handleTask(String pid, String taskId,  Map<String, Object> variables) {
		
		Task task = taskService.createTaskQuery().processInstanceId(pid).taskId(taskId).singleResult();
		
		System.out.println(task.getId() + " \t " + task.getName());
		
		if(variables != null) {
			
			taskService.complete(task.getId(), variables);
		}
		else {
			
			taskService.complete(task.getId());
		}
		
		for(Task t : taskService.createTaskQuery().processInstanceId(pid).list()) {
			
			findOutComeListByTaskId(t.getId());
		}
		
		return taskService.createTaskQuery().processInstanceId(pid).list();
	}
	
	public void outcomeTest() {
		
		//List<String> outcomes = workFlowService.findOutComeListByTaskId(taskId);
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
		
		nextOutcome(activityImpl,activityImpl.getId(),"");
		
		//((TaskEntity) task).getExecution().getActivity().getOutgoingTransitions();//
        // 5.获取当前活动完成之后连线的名称
        /*List<PvmTransition> pvmList = activityImpl.getOutgoingTransitions();
        if (pvmList != null && pvmList.size() > 0) {
            for (PvmTransition pvm : pvmList) {
                String name = (String) pvm.getProperty("name");
                PvmActivity pvmActivity = pvm.getDestination();
                if (name != null) {
                    list.add(name);
                }
            }
        }*/
        return list;
    }

    private List<PvmTransition> nextOutcome(ActivityImpl activityImpl, String activityId, String elString){  
        if("userTask".equals(activityImpl.getProperty("type")) && !activityId.equals(activityImpl.getId())){  
        	return activityImpl.getOutgoingTransitions();
        }else{
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();  
            List<PvmTransition> outTransitionsTemp = null;  
            for(PvmTransition tr:outTransitions){    
                PvmActivity ac = tr.getDestination(); //获取线路的终点节点    
                if("exclusiveGateway".equals(ac.getProperty("type"))){  
                    outTransitionsTemp = ac.getOutgoingTransitions();  
                    if(outTransitionsTemp.size() == 1){  
                        return nextOutcome((ActivityImpl)outTransitionsTemp.get(0).getDestination(), activityId, elString);  
                    }else if(outTransitionsTemp.size() > 1){  
                        for(PvmTransition tr1 : outTransitionsTemp){  
                            Object s = tr1.getProperty("conditionText");  
                            if(elString.equals(s.toString().trim())){  
                                return nextOutcome((ActivityImpl)tr1.getDestination(), activityId, elString);  
                            }  
                        }  
                    }  
                }else if("userTask".equals(ac.getProperty("type"))){  
                    return ac.getOutgoingTransitions();  
                }else{  
                    System.out.println(ac.getProperty("type"));  
                }  
            }   
        return null;  
    }  
}  
}
