package com.nway.platform.wform.access.handler;

import java.util.Map;

import com.nway.platform.wform.design.entity.FormPage;

public interface PageDataHandler {

	void handleParam(HandlerType handlerType, FormPage formPage, Map<String, Object> param);

	void handleResult(HandlerType handlerType, FormPage formPage, Map<String, Object> data);
}
