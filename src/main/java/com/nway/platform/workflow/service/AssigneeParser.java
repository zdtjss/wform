package com.nway.platform.workflow.service;

import java.util.Map;

public interface AssigneeParser {

	/**
	 * 
	 * @param taskAssigneeMap key:节点名称；value：节点 assignee
	 * 
	 * @return
	 */
	 <T> T parser(Map<String, String> taskAssigneeMap);
}
