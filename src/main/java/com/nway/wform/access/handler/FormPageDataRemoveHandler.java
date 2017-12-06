package com.nway.wform.access.handler;

import com.nway.wform.design.entity.FormPage;

public interface FormPageDataRemoveHandler {

	void onBefore(FormPage formPage, String dataId);

	void onAfter(FormPage formPage, String dataId);
}
