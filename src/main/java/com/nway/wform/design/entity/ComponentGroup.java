package com.nway.wform.design.entity;

import java.util.List;

/**
 * 组件组
 */
public class ComponentGroup {

	private String id;

	private String name;
	
	private boolean isManual;

	private int displayType;
	
	private boolean isModify;

	private int maxRowNum;

	private int maxColumnNum;

	private List<Component> components;

	// VO
	private String fullName;
	
	private List<String> updateComponentNames;

	private List<String> viewComponentNames;

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

	public int getDisplayType() {
		return displayType;
	}

	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}

	public boolean isModify() {
		return isModify;
	}

	public void setModify(boolean isModify) {
		this.isModify = isModify;
	}

	public int getMaxRowNum() {
		return maxRowNum;
	}

	public void setMaxRowNum(int maxRowNum) {
		this.maxRowNum = maxRowNum;
	}

	public int getMaxColumnNum() {
		return maxColumnNum;
	}

	public void setMaxColumnNum(int maxColumnNum) {
		this.maxColumnNum = maxColumnNum;
	}

	public List<Component> getComponents() {
		return components;
	}

	public void setComponents(List<Component> components) {
		this.components = components;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public List<String> getUpdateComponentNames() {
		return updateComponentNames;
	}

	public void setUpdateComponentNames(List<String> updateComponentNames) {
		this.updateComponentNames = updateComponentNames;
	}

	public List<String> getViewComponentNames() {
		return viewComponentNames;
	}

	public void setViewComponentNames(List<String> viewComponentNames) {
		this.viewComponentNames = viewComponentNames;
	}

}
