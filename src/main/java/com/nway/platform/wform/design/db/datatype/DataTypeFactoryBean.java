package com.nway.platform.wform.design.db.datatype;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nway.platform.wform.design.db.DbMetaData;

@Component
public class DataTypeFactoryBean implements FactoryBean<DataType> {

	@Autowired
	private DbMetaData dbMetaData;
	
	@Override
	public DataType getObject() throws Exception {
		
		String dataTypeName = DataType.class.getName();
		
		return (DataType) getClass().getClassLoader().loadClass(
				dataTypeName.substring(0, dataTypeName.lastIndexOf('.') + 1) + dbMetaData.getDatabaseProductName() + "DataType")
				.newInstance();
	}

	@Override
	public Class<?> getObjectType() {
		
		return DataType.class;
	}

	@Override
	public boolean isSingleton() {
		
		return true;
	}

}
