package com.nway.platform.wform.design.entity;

public class PageFieldList extends Field {
	
	private int rowNum;

	private int colNum;

	private int colSpan;
	
	private boolean isCondition;
	
	private boolean isMultiValue;
	
	private String formatter;
	
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

	public boolean isCondition() {
		return isCondition;
	}

	public void setCondition(boolean isCondition) {
		this.isCondition = isCondition;
	}

	public boolean isMultiValue() {
		return isMultiValue;
	}

	public void setMultiValue(boolean isMultiValue) {
		this.isMultiValue = isMultiValue;
	}

	public String getFormatter() {
		return formatter;
	}

	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}
	
}
