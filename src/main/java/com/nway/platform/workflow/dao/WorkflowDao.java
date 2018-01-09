package com.nway.platform.workflow.dao;

import java.util.Map;

public interface WorkFlowDao {

	String getPdKey(String pageId);
	
	void createWorkItem(Map<String,Object> param);
}
