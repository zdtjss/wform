package com.nway.platform.workflow.service;

import java.util.List;

import com.nway.platform.workflow.entity.Handle;

public interface AssigneeParser {

	/**
	 * 
	 * @param taskAssignee 流程中task属性“assignee”
	 * 
	 * @return
	 */
	 List<Handle.SimpleUser> parser(String taskAssignee);
}
