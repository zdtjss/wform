package com.nway.platform.wform.access.dao;

import java.util.List;
import java.util.Map;

import com.nway.platform.wform.design.entity.Field;
import com.nway.platform.wform.design.entity.FormPage;

public interface FormPageMapper {

	FormPage getFormPage(String id);

	List<Map<String, String>> listFieldAttr(String pageId);
	
	void saveFieldBase(Map<String, String> field);
	
	void saveFieldExt(Map<String, String> field);
	
	void saveFieldCustom(Map<String, String> field);
	
	List<Field> listFields(String pageId);
}
