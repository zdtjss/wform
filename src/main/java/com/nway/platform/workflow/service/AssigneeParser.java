package com.nway.platform.workflow.service;

public interface AssigneeParser {

	/**
	 * 
	 * @param taskAssignee 流程中task属性“assignee”
	 * 
	 * @return
	 */
	 <T> T parser(String taskAssignee);
}
