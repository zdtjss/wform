package com.nway.wform.access.component;

public interface MultiValueComponent extends BaseComponent {

	void save(Object comp);
	
	void update(Object comp);
	
	void remove(String bizId);
	
	/**
	 * 数据库查询时关联的对象
	 * 
	 * @param fieldGroup
	 * @param mainData
	 * @return
	 */
	Object getAssociatedValue(String bizId);
	
}
