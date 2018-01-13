package com.nway.platform.workflow.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
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
	
	@RequestMapping("getNextTaskAssignee")
	@ResponseBody
	public Object getNextTaskAssignee(String taskId, String outcome) {
		
		String assignee = workFlowService.getNextTaskAssignee(taskId, outcome);
		
		return SpringContextUtil.getBean(AssigneeParser.class).parser(assignee);
	}
	
	@RequestMapping("getDiagram")
	public void getDiagram(String taskId, HttpServletResponse response) throws IOException {
		
		IOUtils.copy(workFlowService.getDiagram(taskId), response.getOutputStream()) ;
	}
	
	@RequestMapping("historicHandle")
	@ResponseBody
	public Map<String, Map<String, Object>> historicHandle(String taskId) {

		return workFlowService.historicHandle(taskId);
	}
}
