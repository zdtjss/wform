package com.nway.wform.access.handler;

import java.util.Map;

import com.nway.wform.design.entity.FieldGroup;

public interface FieldGroupDataHandler {

	void handleParam(HandlerType HandlerType, FieldGroup fieldGroup, Map<String, Object> param);

	void handleData(HandlerType handlerType, FieldGroup fieldGroup, Object data);
}
