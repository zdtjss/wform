package com.nway.wform.access.dao;

import java.util.List;
import java.util.Map;

import com.nway.wform.design.entity.FormPage;

public interface FormPageMapper {

	FormPage getFormPage(String id);

	List<Map<String, String>> listFieldAttr(String groupId);
}
