package com.nway.wform.access.handler;

import java.util.Map;

import com.nway.wform.design.entity.FormPage;

public interface FormPageDataHandler {

	void onBefore(HandlerType handlerType, FormPage formPage, Map<String, Object> param);

	void onAfter(HandlerType handlerType, FormPage formPage, Map<String, Object> param);
}
