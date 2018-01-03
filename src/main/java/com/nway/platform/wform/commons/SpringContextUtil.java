package com.nway.platform.wform.commons;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取spring容器，以访问容器中定义的其他bean
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
	
	// Spring应用上下文环境
	private static ApplicationContext applicationContext;

	/**
	 * 实现ApplicationContextAware接口的回调方法，设置上下文环境
	 */
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextUtil.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取对象 这里重写了bean方法，起主要作用
	 * 
	 * @param name
	 * @return Object 一个以所给名字注册的bean的实例
	 * @throws BeansException
	 */
	public static Object getBean(String name) {
		
		return applicationContext.getBean(name);
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		
		return applicationContext.getBean(name, requiredType);
	}
	
	public static <T> T getBean(String name, Class<T> requiredType, T defaultObj) {

		T bean = null;

		if (applicationContext.containsBean(name)) {

			bean = applicationContext.getBean(name, requiredType);
		}
		else {

			bean = defaultObj;
		}

		return bean;
	}
	
	public static <T> Map<String, T> getBeansOfType(Class<T> type) {

		return applicationContext.getBeansOfType(type);
	}
}