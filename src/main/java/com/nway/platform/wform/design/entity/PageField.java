package com.nway.platform.wform.design.entity;

public class PageField extends Field {
	
	public static final String PRIMARY_KEY_NAME = "PK_ID";
	
	private String id;
	
	private String fieldId;

	private String name;
	
	private boolean editable;
	
	private boolean showInWorkItem;
	
	private boolean isCondition;

	// VO
	private String updateFieldNames;

	private String viewFieldNames;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isShowInWorkItem() {
		return showInWorkItem;
	}

	public void setShowInWorkItem(boolean showInWorkItem) {
		this.showInWorkItem = showInWorkItem;
	}

	public boolean isCondition() {
		return isCondition;
	}

	public void setCondition(boolean isCondition) {
		this.isCondition = isCondition;
	}

	public String getUpdateFieldNames() {
		return updateFieldNames;
	}

	public void setUpdateFieldNames(String updateFieldNames) {
		this.updateFieldNames = updateFieldNames;
	}

	public String getViewFieldNames() {
		return viewFieldNames;
	}

	public void setViewFieldNames(String viewFieldNames) {
		this.viewFieldNames = viewFieldNames;
	}
	
}
