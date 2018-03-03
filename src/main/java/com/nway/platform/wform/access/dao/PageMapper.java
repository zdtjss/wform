package com.nway.platform.wform.access.dao;

import java.util.List;
import java.util.Map;

import com.nway.platform.wform.design.entity.FormField;
import com.nway.platform.wform.design.entity.Page;

public interface PageMapper {

	Page getFormPage(String id);

	List<Map<String, String>> listFieldAttr(String pageId);
	
	void saveFieldBase(Map<String, String> field);
	
	void saveFieldExt(Map<String, String> field);
	
	void saveFieldCustom(Map<String, String> field);
	
	List<FormField> listFields(String pageId);
}
