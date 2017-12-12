package com.nway.wform.design.entity;

import java.util.List;

public class FormPage {

	public static final int PAGE_CREATE_WAY_DESIGN = 1;
	
	public static final int PAGE_CREATE_WAY_PROCESS = 2;
	
	public static final int PAGE_TYPE_CREATE = 1;

	public static final int PAGE_TYPE_EDIT = 2;

	public static final int PAGE_TYPE_DETAILS = 3;

	public static final int PAGE_TYPE_LIST = 4;

	private String id;

	private String name;
	
	private String title;

	// 增、改、查、列
	private int pageType;

	private int status;
	// 简介
	private String summary;

	private String moduleId;

	private List<FieldGroup> componentGroups;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPageType() {
		return pageType;
	}

	public void setPageType(int pageType) {
		this.pageType = pageType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public List<FieldGroup> getComponentGroups() {
		return componentGroups;
	}

	public void setComponentGroups(List<FieldGroup> componentGroups) {
		this.componentGroups = componentGroups;
	}

}
