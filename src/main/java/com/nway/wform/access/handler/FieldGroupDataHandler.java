package com.nway.wform.access.handler;

import java.util.Map;

import com.nway.wform.design.entity.FieldGroup;

public interface FieldGroupDataHandler {

	void onBefore(HandlerType HandlerType, FieldGroup fieldGroup, Map<String, Object> param);

	void onAfter(HandlerType handlerType, FieldGroup fieldGroup, Map<String, Object> param);
}
