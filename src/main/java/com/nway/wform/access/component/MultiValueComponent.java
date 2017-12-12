package com.nway.wform.access.component;

import java.util.List;
import java.util.Map;

public interface MultiValueComponent<T> extends BaseComponent<T> {

	void save(Map<String, String[]> pageData);
	
	void update(Map<String, String[]> pageData);
	
	/**
	 * 根据页面数据解析
	 * 
	 * @param fieldGroup
	 * @param pageData
	 * @return
	 */
	T getValue(Map<String, String[]> pageData);
	
	/**
	 * 数据库查询时关联的对象
	 * 
	 * @param fieldGroup
	 * @param mainData
	 * @return
	 */
	List<T> getAssociatedValue(Map<String, String[]> mainData);
	
	void remove(String mainDataId);
}
