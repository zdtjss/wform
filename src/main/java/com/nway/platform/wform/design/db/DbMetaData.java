package com.nway.platform.wform.design.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import com.nway.platform.wform.commons.SpringContextUtil;

@Component
public class DbMetaData implements InitializingBean {

	private String databaseProductName;
	
	public String getDatabaseProductName() {
		
		return databaseProductName;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {

		DataSource ds = SpringContextUtil.getBean(DataSource.class);
		
		Connection conn = DataSourceUtils.getConnection(ds);

		try {

			String simpleName = null;
			
			String databaseProductName = conn.getMetaData().getDatabaseProductName();

			Matcher matcher = Pattern.compile("(\\w+)").matcher(databaseProductName);
			
			if(matcher.find()) {
				
				simpleName = matcher.group(1).toLowerCase();
			}

			this.databaseProductName = Character.toUpperCase(simpleName.charAt(0)) + simpleName.substring(1);
		}
		catch(BeansException e) {
			
			throw e;
		}
		catch(SQLException e) {
			
			throw e;
		}
		finally {

			DataSourceUtils.doCloseConnection(conn, ds);
		}
	}

	
}
