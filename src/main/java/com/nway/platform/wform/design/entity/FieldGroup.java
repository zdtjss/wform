package com.nway.platform.wform.design.entity;

import java.util.List;
import java.util.Map;

/**
 * 组件组
 */
public class FieldGroup {
	
	public static final int DISPLAY_TYPE_FORM = 1;
	
	public static final int DISPLAY_TYPE_LIST = 2;

	private String id;

	private String name;
	
	private String formPageId;
	
	private boolean isManual;
	
	private String tableName;

	// form：表单样式；list：列表样式
	private int displayType;
	
	private boolean editable;

	private int maxColumnNum;

	private List<Field> fields;
	
	private List<Map<String, String>> fieldAttr;
	
	private int order;

	// VO
	private String updateFieldNames;

	private String viewFieldNames;
	
	private int effectCount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isManual() {
		return isManual;
	}

	public void setManual(boolean isManual) {
		this.isManual = isManual;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getDisplayType() {
		return displayType;
	}

	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public int getMaxColumnNum() {
		return maxColumnNum;
	}

	public void setMaxColumnNum(int maxColumnNum) {
		this.maxColumnNum = maxColumnNum;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public List<Map<String, String>> getFieldAttr() {
		return fieldAttr;
	}

	public void setFieldAttr(List<Map<String, String>> fieldAttr) {
		this.fieldAttr = fieldAttr;
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

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getFormPageId() {
		return formPageId;
	}

	public void setFormPageId(String formPageId) {
		this.formPageId = formPageId;
	}

	public int getEffectCount() {
		return effectCount;
	}

	public void setEffectCount(int effectCount) {
		this.effectCount = effectCount;
	}

}
