package com.nway.platform.wform.access.handler;

import java.util.Map;

import com.nway.platform.wform.design.entity.Page;

public interface PageDataHandler {

	void handleParam(HandlerType handlerType, Page page, Map<String, Object> param);

	void handleResult(HandlerType handlerType, Page page, Map<String, Object> data);
}
