package com.nway.workflow.entity;

import java.util.List;
import java.util.Map;

public class HandleInfo {

	private String pid;
	
	private Map<String, String> params;
	
	private List<SimpleUser> handleUsers;

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
