package com.nway.platform.wform.component;

public interface Initializable {

	/**
	 * 
	 * t_compName_pageName_fieldName
	 * 
	 * @param pageName
	 * @param fieldName
	 * @return
	 */
	Object init(String pageName, String fieldName);
}
