package com.nway.wform.access.handler;

import com.nway.wform.design.entity.FieldGroup;

public interface FieldGroupDataRemoveHandler {

	void onBefore(FieldGroup fieldGroup, String dataId);

	void onAfter(FieldGroup fieldGroup, String dataId);
}
