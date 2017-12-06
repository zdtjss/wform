package com.nway.wform.access.handler;

import java.util.Map;

import com.nway.wform.design.entity.FieldGroup;

public interface FieldGroupDataQueryHandler {

	void onBefore(FieldGroup fieldGroup, String dataId);
	
	void onAfter(FieldGroup fieldGroup, Map<String, Object> groupData);
}
