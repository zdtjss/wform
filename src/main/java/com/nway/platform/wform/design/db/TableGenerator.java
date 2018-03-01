package com.nway.platform.wform.design.db;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nway.platform.wform.component.BaseComponent;
import com.nway.platform.wform.design.db.datatype.DataType;
import com.nway.platform.wform.design.entity.PageForm;

@Component
public class TableGenerator {

	@Autowired
	private DataType dataType;
	
	public void createTable(List<PageForm> formFields) {
		
		System.out.println(dataType);
	}
	
	private void makeTableCreateSql(List<PageForm> formFields) {
		
		for(PageForm field : formFields) {
			
			BaseComponent component = field.getObjType();
			
			getType(String.class, field.getSize());
		}
	}
	
	private String getType(Class<?> javaType, int capacity) {
		
		return "";
	}
	
}
