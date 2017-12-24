package com.nway.wform.design.entity;

import java.util.Date;
import java.util.List;

public class FormPage {

	public static final int PAGE_CREATE_WAY_DESIGN = 1;
	public static final int PAGE_CREATE_WAY_PROCESS = 2;
	
	public static final int PAGE_TYPE_CREATE = 1;
	public static final int PAGE_TYPE_EDIT = 2;
	public static final int PAGE_TYPE_DETAILS = 3;
	public static final int PAGE_TYPE_LIST = 4;
	
	public static final int PAGE_RENDER_TYPE_PC = 1;
	public static final int PAGE_RENDER_TYPE_ANDROID_PHONE = 2;
	public static final int PAGE_RENDER_TYPE_ANDROID_PAD = 3;
	public static final int PAGE_RENDER_TYPE_IOS_PHONE = 4;
	public static final int PAGE_RENDER_TYPE_IOS_PAD = 5;

	private String id;

	private String name;
	
	private String title;

	// 增、改、查、列
	private int pageType;

	private int status;
	// 简介
	private String summary;

	private String moduleId;
	
	private String moduleName;
	
	private Date createTime;

	private List<FieldGroup> fielsGroups;

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

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public List<FieldGroup> getFielsGroups() {
		return fielsGroups;
	}

	public void setFielsGroups(List<FieldGroup> fielsGroups) {
		this.fielsGroups = fielsGroups;
	}

}
