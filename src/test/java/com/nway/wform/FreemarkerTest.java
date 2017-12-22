package com.nway.wform;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import freemarker.template.Configuration;
import freemarker.template.Template;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/spring.xml" })
public class FreemarkerTest {

	@Autowired
	@Qualifier("freemarkerConfiguration")
	private Configuration freemarker;
	
	@Test
	public void out() throws Exception {
		
		Template template = freemarker.getTemplate("/test.ftl");
		
		Map<String, Object> dataModel = new HashMap<String, Object>();
		
		dataModel.put("name", "哈哈");
		
		Writer out = new PrintWriter(System.out);
		
		template.process(dataModel, out);
	}
}
