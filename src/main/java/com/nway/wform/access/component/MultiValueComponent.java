package com.nway.wform.access.component;

import java.util.Map;

import com.nway.wform.design.entity.FieldGroup;

public abstract class MultiValueComponent<T> extends BaseComponent<T> {

	/**
	 * 根据页面数据解析
	 * 
	 * @param fieldGroup
	 * @param pageData
	 * @return
	 */
	protected abstract T getValue(FieldGroup fieldGroup, Map<String, Map<String, String>> pageData);
	
	/**
	 * 数据库查询时关联的对象
	 * 
	 * @param fieldGroup
	 * @param mainData
	 * @return
	 */
	protected abstract T getAssociatedValue(FieldGroup fieldGroup, Map<String, Object> mainData);
}
