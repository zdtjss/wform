package com.nway.platform.wform.access.component;

public interface BaseComponent {

	String BASE_PATH = "";
	
	/**
	 * 组件对应的jsp路径
	 * 
	 * @return
	 */
	String getPath();
	
	/**
	 * 根据组件所需的实际类型，转换页面参数值
	 * 
	 * @return
	 */
	Object getValue(String value);
	
}
