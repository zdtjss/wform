package com.nway.platform.wform.design.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import com.nway.platform.wform.commons.SpringContextUtil;
import com.nway.platform.wform.design.db.datatype.BaseDataType;
import com.nway.platform.wform.design.db.datatype.HsqldbDataType;

@Component
public class TableGenerator implements InitializingBean {

	private BaseDataType dataType;
	
	public String getType(Class<?> javaType, int capacity) {
		
		return "";
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		DataSource ds = SpringContextUtil.getBean(DataSource.class);
		
		Connection conn = DataSourceUtils.getConnection(ds);

		try {

			String databaseProductName = conn.getMetaData().getDatabaseProductName();

			if ("HSQLDB".equalsIgnoreCase(databaseProductName)) {

				dataType = new HsqldbDataType();
			}
		}
		catch (SQLException e) {

			throw e;
		}
		finally {

			DataSourceUtils.doCloseConnection(conn, ds);
		}
		
		Map<String, BaseDataType> datatTypes = SpringContextUtil.getBeansOfType(BaseDataType.class);
	}
	
}
