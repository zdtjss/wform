package com.nway.platform.wform.design.entity;

public class FormPage extends FormField {
	
	/** 行号 **/
	private int rowNum;

	/** 列 **/
	private int colNum;

	/** 跨列数 **/
	private int colSpan;
	
	private boolean isReadonly;
	
	private boolean isHidden;
	
	// 需要在待办表现实，值为待办表中使用的名字
	private String forWorkItem;
	
	// 可作为流程变量，值为流程变量中使用的名字
	private String forWorkflow;
	
	private int size;

	// VO
	private String updateFieldNames;

	private String viewFieldNames;
	
	private boolean isMultiValue;
	
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

	public boolean isReadonly() {
		return isReadonly;
	}

	public void setReadonly(boolean isReadonly) {
		this.isReadonly = isReadonly;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public String getForWorkItem() {
		return forWorkItem;
	}

	public void setForWorkItem(String forWorkItem) {
		this.forWorkItem = forWorkItem;
	}

	public String getForWorkflow() {
		return forWorkflow;
	}

	public void setForWorkflow(String forWorkflow) {
		this.forWorkflow = forWorkflow;
	}

	public boolean isMultiValue() {
		return isMultiValue;
	}

	public void setMultiValue(boolean isMultiValue) {
		this.isMultiValue = isMultiValue;
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

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
}
