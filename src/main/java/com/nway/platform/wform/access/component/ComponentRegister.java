package com.nway.platform.wform.access.component;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ComponentRegister implements InitializingBean, ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	private Map<String, BaseComponent> components = new HashMap<String, BaseComponent>();
	
	public BaseComponent getComponent(String name) {
		
		return components.get(name);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {

		this.components = applicationContext.getBeansOfType(BaseComponent.class);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		this.applicationContext = applicationContext;
	}

}
