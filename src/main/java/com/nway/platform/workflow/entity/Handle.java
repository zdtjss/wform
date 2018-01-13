package com.nway.platform.workflow.entity;

import java.util.Map;

public class Handle {

	// 当前办理流程定义key或者流程实例id
	private String processKey;
	
	// 当前办理的任务id
	private String taskId;
	
	private Action action;
	
	private String workItemId;
	
	private SimpleUser currentUser;
	
	private Map<String, Object> variables;
	
	// <下一步任务名：对应的办理人>
	private Map<String, SimpleUser[]> handlerTaskMap;

	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public String getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(String workItemId) {
		this.workItemId = workItemId;
	}

	public SimpleUser getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(SimpleUser currentUser) {
		this.currentUser = currentUser;
	}

	public Map<String, SimpleUser[]> getHandlerTaskMap() {
		return handlerTaskMap;
	}

	public void setHandlerTaskMap(Map<String, SimpleUser[]> handlerTaskMap) {
		this.handlerTaskMap = handlerTaskMap;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}

	public static class SimpleUser {
		
		private String userId;
		
		private String cnName;

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getCnName() {
			return cnName;
		}

		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}
	
	public static enum Action {
		
		FORWARD("forward"), BACK("back"), TO_START("toStart");
		
		private String name;
		
		private Action(String name) {
			
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
}
