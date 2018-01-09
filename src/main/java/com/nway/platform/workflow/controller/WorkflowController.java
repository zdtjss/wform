package com.nway.platform.workflow.controller;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activiti.engine.impl.pvm.PvmTransition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nway.platform.wform.commons.SpringContextUtil;
import com.nway.platform.workflow.service.AssigneeParser;
import com.nway.platform.workflow.service.WorkFlowService;

@Controller
@RequestMapping("workflow")
public class WorkflowController {

	@Autowired
	private WorkFlowService workFlowService;
	
	public void startFlow() {
		
		
	}
	
	public void handleTask() {
		
		
	}
	
	@RequestMapping("nextOutcome")
	public Set<String> nextOutcome(String taskId) {
		
		List<PvmTransition> pvmTransitionList = workFlowService.findOutComeListByTaskId(taskId);

		Set<String> transitionNames = new HashSet<String>(pvmTransitionList.size());
		
		for (PvmTransition transition : pvmTransitionList) {
			
			transitionNames.add((String) transition.getProperty("name"));
		}
		
		return transitionNames;
	}
	
	@RequestMapping("getTaskAssignee")
	@ResponseBody
	public Object getTaskAssignee(String taskId) {
		
		String assignee = workFlowService.getTaskAssignee(taskId);
		
		return SpringContextUtil.getBean(AssigneeParser.class).parser(assignee);
	}
	
	@RequestMapping("getDiagram")
	public InputStream getDiagram(String taskId) {
		
		return workFlowService.getDiagram(taskId);
	}
}
