package com.nway.wform.access.handler;

import java.util.Map;

import com.nway.wform.design.entity.FieldGroup;

public interface FieldGroupDataListHandler {

	void onBefore(FieldGroup fieldGroup, Map<String, Object> requestData);

	void onAfter(FieldGroup fieldGroup, Map<String, Object> requestData);
}
