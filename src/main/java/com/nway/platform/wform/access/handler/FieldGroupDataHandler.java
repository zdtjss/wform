package com.nway.platform.wform.access.handler;

import java.util.Map;

import com.nway.platform.wform.design.entity.FieldGroup;

public interface FieldGroupDataHandler {

	void handleParam(HandlerType HandlerType, FieldGroup fieldGroup, Map<String, Object> param);

	void handleResult(HandlerType handlerType, FieldGroup fieldGroup, Object data);
}
