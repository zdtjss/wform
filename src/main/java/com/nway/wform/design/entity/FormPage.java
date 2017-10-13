package com.nway.wform.design.entity;

import java.util.List;
import java.util.Map;

public class FormPage {

	public static final int PAGE_TYPE_CREATE = 1;

	public static final int PAGE_TYPE_EDIT = 2;

	public static final int PAGE_TYPE_DETAILS = 3;

	public static final int PAGE_TYPE_LIST = 4;

	private String id;

	private String name;

	// 增、改、查、列
	private int pageType;

	private List<ComponentGroup> componentGroups;

	private Map<String, Map<String, String>> componentAttributes;

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

	public int getPageType() {
		return pageType;
	}

	public void setPageType(int pageType) {
		this.pageType = pageType;
	}

	public List<ComponentGroup> getComponentGroups() {
		return componentGroups;
	}

	public void setComponentGroups(List<ComponentGroup> componentGroups) {
		this.componentGroups = componentGroups;
	}

	public Map<String, Map<String, String>> getComponentAttributes() {
		return componentAttributes;
	}

	public void setComponentAttributes(Map<String, Map<String, String>> componentAttributes) {
		this.componentAttributes = componentAttributes;
	}

}
