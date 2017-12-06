package com.nway.wform.access.component;

import java.util.List;
import java.util.Map;

import com.nway.wform.design.entity.FieldGroup;

public interface MultiValueComponent<T> extends BaseComponent<T> {

	void create(FieldGroup fieldGroup, Map<String, Map<String, String>> pageData);
	
	void update(FieldGroup fieldGroup, Map<String, Map<String, String>> pageData);
	
	/**
	 * 根据页面数据解析
	 * 
	 * @param fieldGroup
	 * @param pageData
	 * @return
	 */
	T getValue(FieldGroup fieldGroup, Map<String, Map<String, String>> pageData);
	
	/**
	 * 数据库查询时关联的对象
	 * 
	 * @param fieldGroup
	 * @param mainData
	 * @return
	 */
	List<T> getAssociatedValue(FieldGroup fieldGroup, Map<String, Object> mainData);
	
	void remove(FieldGroup fieldGroup, String mainDataId);
}
