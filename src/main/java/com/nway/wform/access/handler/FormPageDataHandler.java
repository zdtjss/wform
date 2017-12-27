package com.nway.wform.access.handler;

import java.util.Map;

import com.nway.wform.design.entity.FormPage;

public interface FormPageDataHandler {

	void handleParam(HandlerType handlerType, FormPage formPage, Map<String, Object> param);

	void handleResult(HandlerType handlerType, FormPage formPage, Object data);
}
