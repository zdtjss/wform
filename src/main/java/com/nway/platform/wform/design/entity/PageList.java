package com.nway.platform.wform.design.entity;

public class PageList extends Field {

	private int index;

	private String formatter;

	private boolean isMultiValue;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getFormatter() {
		return formatter;
	}

	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}

	public boolean isMultiValue() {
		return isMultiValue;
	}

	public void setMultiValue(boolean isMultiValue) {
		this.isMultiValue = isMultiValue;
	}

}
