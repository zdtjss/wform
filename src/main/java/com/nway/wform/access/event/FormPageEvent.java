package com.nway.wform.access.event;

import java.util.Map;

import com.nway.wform.design.entity.FormPage;

public class FormPageEvent extends FormEvent {
	
	private FormPage formPage;
	
	private Map<String, Map<String, Object>> requestParam;

	public FormPageEvent(FormPage formPage, Map<String, Map<String, Object>> requestParam, int executeType) {

		super(formPage.getName());

		this.requestParam = requestParam;
	}

	public FormPage getFormPage() {
		return formPage;
	}

	public void setFormPage(FormPage formPage) {
		this.formPage = formPage;
	}

	public Map<String, Map<String, Object>> getRequestParam() {
		return requestParam;
	}

	public void setRequestParam(Map<String, Map<String, Object>> requestParam) {
		this.requestParam = requestParam;
	}

}
