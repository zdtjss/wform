package com.nway.platform.wform.component;

public interface MultiValueComponent extends BaseComponent {
	
	void save(String pageName, String bizId, Object comp);
	
	void update(String pageName, String bizId, Object comp);
	
	void remove(String pageName, String bizId);
	
	/**
	 * 数据库查询时关联的对象
	 * 
	 * @param pageName
	 * @param bizId 
	 * @return
	 */
	Object getAssociatedValue(String pageName, String bizId);
	
	String getDdlSql(String pageName, String refName);
	
}
