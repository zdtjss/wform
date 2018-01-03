package com.nway.platform.wform.design.entity;

import java.util.Map;

import com.nway.platform.wform.access.component.BaseComponent;

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

	/** 所属模块  **/
	private String moduleId;

	/**
	 * 组件类别
	 */
	private String type;
	
	private Map<String, String> attributes;
	
	// VO
	private boolean editable;
	
	/** 行号 **/
	private int rowNum;

	/** 列 **/
	private int colNum;

	/** 跨列数 **/
	private int colSpan;
	
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

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public int getColNum() {
		return colNum;
	}

	public void setColNum(int colNum) {
		this.colNum = colNum;
	}

	public int getColSpan() {
		return colSpan;
	}

	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public BaseComponent getObjType() {
		return objType;
	}

	public void setObjType(BaseComponent objType) {
		this.objType = objType;
	}
	
}
