package com.nway.platform.wform.design.entity;

import com.nway.platform.wform.component.BaseComponent;

public class Field {
	
	private String id;

	/**
	 * 用做页面元素 id 或 name
	 */
	private String name;

	/**
	 * 中文解释
	 */
	private String display;

	private String pageId;

	/**
	 * 组件类别
	 */
	private String type;
	
	// VO
	private BaseComponent objType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BaseComponent getObjType() {
		return objType;
	}

	public void setObjType(BaseComponent objType) {
		this.objType = objType;
	}
	
}
