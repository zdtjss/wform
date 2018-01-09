package com.nway.platform.workflow.event;

import java.util.Map;

import org.springframework.context.ApplicationEvent;

import com.nway.platform.wform.design.entity.FormPage;
import com.nway.platform.workflow.entity.Handle;

public class TaskCompleteEvent extends ApplicationEvent {
	
	private Handle handleInfo;
	private FormPage page;
	private Map<String, Object> formData;
	
	public TaskCompleteEvent(Handle handleInfo, FormPage page, Map<String, Object> formData) {
		super(handleInfo);
		this.handleInfo = handleInfo;
		this.page = page;
		this.formData = formData;
	}

	public Handle getHandleInfo() {
		return handleInfo;
	}

	public void setHandleInfo(Handle handleInfo) {
		this.handleInfo = handleInfo;
	}

	public FormPage getPage() {
		return page;
	}

	public void setPage(FormPage page) {
		this.page = page;
	}

	public Map<String, Object> getFormData() {
		return formData;
	}

	public void setFormData(Map<String, Object> formData) {
		this.formData = formData;
	}
	
}
