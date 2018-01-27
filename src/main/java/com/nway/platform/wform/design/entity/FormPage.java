package com.nway.platform.wform.design.entity;

import java.util.Date;
import java.util.List;

public class FormPage {

	public static final String PAGE_TYPE_CREATE = "create";
	public static final String PAGE_TYPE_EDIT = "update";
	public static final String PAGE_TYPE_DETAILS = "details";
	public static final String PAGE_TYPE_LIST = "list";
	
	public static final int PAGE_RENDER_TYPE_PC = 1;
	public static final int PAGE_RENDER_TYPE_ANDROID_PHONE = 2;
	public static final int PAGE_RENDER_TYPE_ANDROID_PAD = 3;
	public static final int PAGE_RENDER_TYPE_IOS_PHONE = 4;
	public static final int PAGE_RENDER_TYPE_IOS_PAD = 5;

	private String id;
	
	private String formId;

	private String name;
	
	private String title;
	
	// 增、改、查、列
	private String type;
	
	private int maxColumnNum;
	
	private boolean isMunual;
	
	private String tableName;
	
	private String moduleId;
	
	private String moduleName;
	// 简介
	private String summary;
	
	private Date createTime;
	
	private int status;

	private List<PageForm> formFields;
	
	private List<PageList> listFields;
	
	private List<PageListCondition> listCondition;
	
	private String keyName;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getMaxColumnNum() {
		return maxColumnNum;
	}

	public void setMaxColumnNum(int maxColumnNum) {
		this.maxColumnNum = maxColumnNum;
	}

	public boolean isMunual() {
		return isMunual;
	}

	public void setMunual(boolean isMunual) {
		this.isMunual = isMunual;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public List<PageForm> getFormFields() {
		return formFields;
	}

	public void setFormFields(List<PageForm> formFields) {
		this.formFields = formFields;
	}

	public List<PageList> getListFields() {
		return listFields;
	}

	public void setListFields(List<PageList> listFields) {
		this.listFields = listFields;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public List<PageListCondition> getListCondition() {
		return listCondition;
	}

	public void setListCondition(List<PageListCondition> listCondition) {
		this.listCondition = listCondition;
	}

}
