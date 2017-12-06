package com.nway.wform.access.handler;

import java.util.Map;

import com.nway.wform.design.entity.FormPage;

public interface FormPageDataCreateHandler {

	void onBefore(FormPage formPage, Map<String, Map<String, Object>> requestParam);
	
	void onAfter(FormPage formPage, Map<String, Map<String, Object>> requestParam);
}
