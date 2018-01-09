package com.nway.platform.workflow.dao;

import java.util.Map;

public interface WorkflowDao {

	void createWorkItem(Map<String, Object> workItem);
}
