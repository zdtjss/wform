package com.nway.wform.access.component;

import java.util.Map;

public interface MultiValueComponent extends BaseComponent {

	void save(Object comp);
	
	void update(Object comp);
	
	void remove(String bizId);
	
	/**
	 * 根据页面数据解析
	 * 
	 * @param fieldGroup
	 * @param pageData
	 * @return
	 */
	Object getValue(Map<String, String[]> pageData);
	
	/**
	 * 数据库查询时关联的对象
	 * 
	 * @param fieldGroup
	 * @param mainData
	 * @return
	 */
	Object getAssociatedValue(String bizId);
	
}
