package com.nway.platform.wform.design.db;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.nway.platform.wform.component.BaseComponent;
import com.nway.platform.wform.component.MultiValueComponent;
import com.nway.platform.wform.design.entity.Page;
import com.nway.platform.wform.design.entity.FormPage;

@Component
public class TableGenerator {

	@Autowired
	private DataSource dataSource;
	
	public void createTable(Page page) {
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		String mainTableSql = makeMainTableCreateSql(page);
		
		List<String> subTableSql = makeSubTableCreateSql(page);
		
		jdbcTemplate.execute(mainTableSql);
		
		for(String sql : subTableSql) {
			
			jdbcTemplate.execute(sql);
		}
		
	}
	
	private String makeMainTableCreateSql(Page page) {
		
		StringBuilder mainTableSql = new StringBuilder("create table ");
		
		mainTableSql.append(page.getTableName()).append(" ( ");
		
		List<FormPage> form = page.getSimpleValueFormFields();
		
		for(FormPage field : form) {
			
			BaseComponent component = field.getObjType();
			
			mainTableSql.append(field.getName()).append(' ').append(component.getDataType(field.getSize())).append(",");
		}
		
		return form.size() > 0 ? mainTableSql.deleteCharAt(mainTableSql.length() - 1).append(')').toString() : "";
	}
	
	private List<String> makeSubTableCreateSql(Page page) {
		
		List<String> sql = new ArrayList<String>();
		
		for(FormPage field : page.getMultiValueFormFields()) {
			
			MultiValueComponent component = (MultiValueComponent) field.getObjType();
			
			sql.add(component.getDdlSql(page.getName(), field.getName()));
		}
		
		return sql;
	}
}
