package com.nway.wform.access.event;

import java.util.EventObject;
import java.util.List;
import java.util.Map;

import com.nway.wform.design.entity.ComponentGroup;

public class FormDataEvent extends EventObject {

	public static final int EVENT_HANDLE_TYPE_BEFORE = 1;
	
	public static final int EVENT_HANDLE_TYPE_AFTER = 2;
	
	public static final int EVENT_TYPE_INSERT = 1;

	public static final int EVENT_TYPE_UPDATE = 2;
	
	public static final int EVENT_TYPE_DELETE = 3;
	
	public static final int EVENT_TYPE_VIEW = 4;
	
	public static final int EVENT_TYPE_LIST = 5;

	private int handleType;
	
	private int executeType;
	
	private ComponentGroup componentGroup;

	private List<Map<String, String>> formData;

	public FormDataEvent(ComponentGroup componentGroup, List<Map<String, String>> formData, int executeType, int handleType) {

		super(componentGroup.getName() + "_" + handleType);

		this.formData = formData;
		this.componentGroup = componentGroup;
	}

	public int getHandleType() {
		return handleType;
	}

	public void setHandleType(int handleType) {
		this.handleType = handleType;
	}

	public int getExecuteType() {
		return executeType;
	}

	public void setExecuteType(int executeType) {
		this.executeType = executeType;
	}

	public ComponentGroup getComponentGroup() {
		return componentGroup;
	}

	public void setComponentGroup(ComponentGroup componentGroup) {
		this.componentGroup = componentGroup;
	}

	public List<Map<String, String>> getFormData() {
		return formData;
	}

	public void setFormData(List<Map<String, String>> formData) {
		this.formData = formData;
	}

}
