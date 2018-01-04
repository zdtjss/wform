package com.nway.platform.workflow.entity;

import java.util.List;
import java.util.Map;

public class HandleInfo {

	private String processKey;
	
	private Map<String, Object> params;
	
	private List<SimpleUser> handleUsers;

	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public List<SimpleUser> getHandleUsers() {
		return handleUsers;
	}

	public void setHandleUsers(List<SimpleUser> handleUsers) {
		this.handleUsers = handleUsers;
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
