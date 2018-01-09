package com.nway.platform.wform.access.handler;

import java.util.Map;

import com.nway.platform.wform.design.entity.PageField;

public interface FieldGroupDataHandler {

	void handleParam(HandlerType HandlerType, PageField fieldGroup, Map<String, Object> param);

	void handleResult(HandlerType handlerType, PageField fieldGroup, Object data);
}
