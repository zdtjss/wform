package com.nway.wform.access.event;

import java.util.Map;

import com.nway.wform.design.entity.ComponentGroup;

public class ComponentGroupEvent extends FormEvent {
	
	private ComponentGroup componentGroup;

	private Map<String, String> formData;
	
	public ComponentGroupEvent(ComponentGroup componentGroup, Map<String, String> formData, int executeType) {
		
		super(componentGroup.getName());
		
		this.formData = formData;
		this.componentGroup = componentGroup;
	}

	public Map<String, String> getFormData() {
		return formData;
	}

	public void setFormData(Map<String, String> formData) {
		this.formData = formData;
	}


	public ComponentGroup getComponentGroup() {
		return componentGroup;
	}

	public void setComponentGroup(ComponentGroup componentGroup) {
		this.componentGroup = componentGroup;
	}
}
