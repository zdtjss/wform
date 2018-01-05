package com.nway.platform.workflow.entity;

import java.util.Map;

public class HandleInfo {

	// 当前办理流程定义key或者流程实例id
	private String processKey;
	
	// 当前办理的任务id
	private String taskId;
	
	private String outcome;
	
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

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public Map<String, SimpleUser[]> getHandlerTaskMap() {
		return handlerTaskMap;
	}

	public void setHandlerTaskMap(Map<String, SimpleUser[]> handlerTaskMap) {
		this.handlerTaskMap = handlerTaskMap;
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
}
