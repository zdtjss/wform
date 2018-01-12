package com.nway.platform.workflow.controller;

import java.io.InputStream;

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
