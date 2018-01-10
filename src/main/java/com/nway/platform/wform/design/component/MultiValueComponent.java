package com.nway.platform.wform.design.component;

public interface MultiValueComponent extends BaseComponent {
	
	void save(String pageName, Object comp);
	
	void update(String pageName, Object comp);
	
	void remove(String pageName, String bizId);
	
	/**
	 * 数据库查询时关联的对象
	 * 
	 * @param pageName
	 * @param origin {@link #getValue(String)}
	 * @return
	 */
	Object getAssociatedValue(String pageName, Object origin);
	
}
