package com.nway.wform;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nway.platform.wform.design.db.datatype.DataType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class SpringTest {
	
	@Autowired
	private DataType dataType;
	
	@Autowired
	private DataSource dataSource;

	@Test
	public void beanTest() {

		System.out.println(dataType.getClass());
	}
	
	@Test
	public void mataTest() {
		
		try {
			ResultSet rs = DataSourceUtils.getConnection(dataSource).getMetaData().getTypeInfo();
			while(rs.next()) {
				System.out.println("TYPE_NAME = " +rs.getString("TYPE_NAME")+"\t DATA_TYPE = " +rs.getInt("DATA_TYPE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
