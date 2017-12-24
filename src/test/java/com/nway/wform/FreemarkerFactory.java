package com.nway.wform;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ObjectFactory;

import freemarker.template.Configuration;
import freemarker.template.SimpleScalar;

public class FreemarkerFactory implements FactoryBean<Configuration>, ObjectFactory<Configuration> {

	private String basePackagePath;

	@Override
	public Configuration getObject() throws BeansException {

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);

		cfg.setDefaultEncoding("UTF-8");
		cfg.setSharedVariable("exists", new FileExistsDirective());
		cfg.setClassForTemplateLoading(FreemarkerFactory.class, basePackagePath);
		cfg.setSharedVariable("baseWebPath", new SimpleScalar("E:\\workspace\\wform\\src\\test\\resources"));

		return cfg;
	}

	public void setBasePackagePath(String basePackagePath) {
		this.basePackagePath = basePackagePath;
	}

	@Override
	public Class<?> getObjectType() {

		return Configuration.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
