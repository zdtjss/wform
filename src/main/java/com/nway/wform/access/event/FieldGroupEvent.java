package com.nway.wform.access.event;

import java.util.Map;

import com.nway.wform.design.entity.FieldGroup;

public class FieldGroupEvent extends FormEvent {
	
	private FieldGroup fieldGroup;

	private Map<String, Object> formData;
	
	public FieldGroupEvent(FieldGroup fieldGroup, Map<String, Object> formData, int executeType) {
		
		super(fieldGroup.getName());
		
		this.formData = formData;
		this.fieldGroup = fieldGroup;
	}

	public Map<String, Object> getFormData() {
		return formData;
	}

	public void setFormData(Map<String, Object> formData) {
		this.formData = formData;
	}

	public FieldGroup getFieldGroup() {
		return fieldGroup;
	}

	public void setFieldGroup(FieldGroup fieldGroup) {
		this.fieldGroup = fieldGroup;
	}

}
