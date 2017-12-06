package com.nway.wform.access.handler;

import java.util.Map;

import com.nway.wform.design.entity.FieldGroup;

public interface FieldGroupDataModifyHandler {

	void onBefore(FieldGroup fieldGroup, Map<String, Object> groupData);
	
	void onAfter(FieldGroup fieldGroup, Map<String, Object> groupData);
}
