package com.nway.platform.wform.access.dao;

import java.util.List;
import java.util.Map;

import com.nway.platform.wform.design.entity.FormPage;

public interface FormPageMapper {

	FormPage getFormPage(String id);

	List<Map<String, String>> listFieldAttr(String pageId);
}
