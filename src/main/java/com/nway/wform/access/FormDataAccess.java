package com.nway.wform.access;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nway.wform.access.component.BaseComponent;
import com.nway.wform.access.component.MultiValueComponent;
import com.nway.wform.access.handler.FieldGroupDataHandler;
import com.nway.wform.access.handler.FormPageDataHandler;
import com.nway.wform.access.handler.HandlerType;
import com.nway.wform.access.mybatis.TemporaryStatementRegistry;
import com.nway.wform.commons.SpringContextUtil;
import com.nway.wform.design.entity.Field;
import com.nway.wform.design.entity.FieldGroup;
import com.nway.wform.design.entity.FormPage;

@Component
public class FormDataAccess implements InitializingBean {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	private Map<String, BaseComponent> components = new HashMap<String, BaseComponent>();
	
	/**
	 * FormPage.PAGE_TYPE_CREATE && group.isEditable()
	 * 
	 * @param formPage
	 * @param formData
	 */
	public void create(FormPage formPage, Map<String, Map<String, Object>> formData) {
		
		FormPageDataHandler formPageDataHandler = getFormPageDataHandler(formPage.getName());
		
		formPageDataHandler.onBefore(HandlerType.FORM_PAGE_DATA_CREATE, formPage, (Map) formData);
		
		for(FieldGroup group : formPage.getComponentGroups()) {
			
			Map<String, Object> groupData = formData.get(group.getName());
			
			FieldGroupDataHandler fieldGroupDataHandler = getFieldGroupDataHandler(group.getName());
						
			fieldGroupDataHandler.onBefore(HandlerType.FIELD_GROUP_DATA_CREATE, group, groupData);
			
			sqlSessionTemplate.insert(TemporaryStatementRegistry.getLastestName(group.getFullName()), groupData);
			
			// 子表操作
			for(Field field : group.getFields()) {
				
				BaseComponent cmp = components.get(field.getType());
				
				if(MultiValueComponent.class.isInstance(cmp)) {

					((MultiValueComponent) cmp).save(groupData.get(field.getName()));
				}
			}
			
			fieldGroupDataHandler.onAfter(HandlerType.FIELD_GROUP_DATA_CREATE, group, formData.get(group.getName()));
		}
		
		formPageDataHandler.onAfter(HandlerType.FORM_PAGE_DATA_CREATE, formPage, (Map) formData);
	}
	
	
	/**
	 * FormPage.PAGE_TYPE_EDIT && group.isEditable()
	 * 
	 * @param formPage
	 * @param formData
	 */
	public void update(FormPage formPage, Map<String, Map<String, Object>> formData) {
		
		FormPageDataHandler formPageDataHandler = getFormPageDataHandler(formPage.getName());

		formPageDataHandler.onBefore(HandlerType.FORM_PAGE_DATA_MODIFY, formPage, (Map) formData);
		
		for(FieldGroup group : formPage.getComponentGroups()) {
			
			Map<String, Object> groupData = formData.get(group.getName());
			
			FieldGroupDataHandler fieldGroupDataHandler = getFieldGroupDataHandler(group.getName());
						
			fieldGroupDataHandler.onBefore(HandlerType.FIELD_GROUP_DATA_MODIFY, group, groupData);
			
			sqlSessionTemplate.update(TemporaryStatementRegistry.getLastestName(group.getFullName()), groupData);
			
			// 子表操作
			for(Field field : group.getFields()) {
				
				BaseComponent cmp = components.get(field.getType());
				
				if(MultiValueComponent.class.isInstance(cmp)) {

					((MultiValueComponent) cmp).save(groupData.get(field.getName()));
				}
			}
			
			fieldGroupDataHandler.onBefore(HandlerType.FIELD_GROUP_DATA_MODIFY, group, formData.get(group.getName()));
		}
		
		formPageDataHandler.onAfter(HandlerType.FORM_PAGE_DATA_MODIFY, formPage, (Map) formData);
	}
	
	/**
	 * 对应详情页面
	 * 
	 * @param formPage
	 * @param param
	 * @return
	 */
	public Map<String, Map<String, Object>> get(FormPage formPage, String dataId) {
		
		Map<String, Map<String, Object>> pageData = new HashMap<String, Map<String, Object>>();
		
		Map<String, Object> param = Collections.<String, Object>singletonMap("id", dataId);
		
		FormPageDataHandler formPageDataHandler = getFormPageDataHandler(formPage.getName());

		formPageDataHandler.onBefore(HandlerType.FORM_PAGE_DATA_QUERY, formPage, param);
		
		for(FieldGroup group : formPage.getComponentGroups()) {
			
			FieldGroupDataHandler fieldGroupDataHandler = getFieldGroupDataHandler(group.getName());
			
			fieldGroupDataHandler.onBefore(HandlerType.FIELD_GROUP_DATA_QUERY, group, param);
			
			Map<String, Object> groupData = sqlSessionTemplate.selectOne(TemporaryStatementRegistry.getLastestName(group.getFullName()), dataId);

			// 子表操作
			for (Field field : group.getFields()) {

				BaseComponent cmp = components.get(field.getType());
				
				if(MultiValueComponent.class.isInstance(cmp)) {
					
					groupData.put(field.getName(), ((MultiValueComponent) cmp).getAssociatedValue(String.valueOf(groupData.get(field.getName()))));
				}
			}

			pageData.put(group.getName(), groupData);

			fieldGroupDataHandler.onAfter(HandlerType.FIELD_GROUP_DATA_QUERY, group, param);
		}
		
		formPageDataHandler.onAfter(HandlerType.FORM_PAGE_DATA_QUERY, formPage, param);
		
		return pageData;
	}
	
	/**
	 * 对应 FormPage.PAGE_TYPE_LIST
	 * 
	 * 对于点击列表在本页显示详情的情况，使用详情页中加list组件解决
	 * 
	 * @param formPage
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> list(FormPage formPage, Map<String, Object> param) {
		
		List<Map<String, Object>> pageData = new ArrayList<Map<String, Object>>();
		
		FormPageDataHandler formPageDataHandler = getFormPageDataHandler(formPage.getName());
		
		formPageDataHandler.onBefore(HandlerType.FORM_PAGE_DATA_LIST, formPage, param);
		
		// 这里可能是条件组  有待调整
		FieldGroup group = formPage.getComponentGroups().get(0);

		FieldGroupDataHandler fieldGroupDataHandler = getFieldGroupDataHandler(group.getName());

		fieldGroupDataHandler.onBefore(HandlerType.FIELD_GROUP_DATA_LIST, group, param);

		pageData = sqlSessionTemplate.selectList(TemporaryStatementRegistry.getLastestName(group.getFullName()), param);

		// 子表操作
		for (Field field : group.getFields()) {

			BaseComponent cmp = components.get(field.getType());

			if (MultiValueComponent.class.isInstance(cmp)) {
				
				for(Map<String, Object> row : pageData) {

					row.put(field.getName(), ((MultiValueComponent) cmp).getAssociatedValue(String.valueOf(row.get(field.getName()))));
				}
			}
		}

		fieldGroupDataHandler.onAfter(HandlerType.FIELD_GROUP_DATA_LIST, group, param);
	
		formPageDataHandler.onAfter(HandlerType.FORM_PAGE_DATA_LIST, formPage, param);
		
		return pageData;
	}
	
	public void remove(FormPage formPage, String dataId) {
		
		FormPageDataHandler formPageDataHandler = getFormPageDataHandler(formPage.getName());
		
		Map<String, Object> param = Collections.<String, Object>singletonMap("id", dataId);

		formPageDataHandler.onBefore(HandlerType.FORM_PAGE_DATA_REMOVE, formPage, param);
		
		for(FieldGroup group : formPage.getComponentGroups()) {

			FieldGroupDataHandler fieldGroupDataHandler = getFieldGroupDataHandler(formPage.getName());

			fieldGroupDataHandler.onBefore(HandlerType.FIELD_GROUP_DATA_REMOVE, group, param);

			int effectCount = sqlSessionTemplate.delete(TemporaryStatementRegistry.getLastestName(group.getFullName()), dataId);
			
			if (effectCount > 0) {

				// 子表操作
				for (Field field : group.getFields()) {
					
					BaseComponent cmp = components.get(field.getType());
					
					if(MultiValueComponent.class.isInstance(cmp)) {
						
						((MultiValueComponent) cmp).remove(dataId);
					}
				}
			}
			fieldGroupDataHandler.onAfter(HandlerType.FIELD_GROUP_DATA_REMOVE, group, param);
		}
		
		formPageDataHandler.onAfter(HandlerType.FORM_PAGE_DATA_REMOVE, formPage, param);
	}
	
	private static final FormPageDataHandler defaultFormPageDataHandler = new DefaultFormPageDataHandler();
	
	private static final class DefaultFormPageDataHandler implements FormPageDataHandler {

		@Override
		public void onBefore(HandlerType handlerType, FormPage formPage, Map<String, Object> param) {
			
		}

		@Override
		public void onAfter(HandlerType handlerType, FormPage formPage, Map<String, Object> param) {
			
		}

	}

	private static final DefaultFieldGroupDataHandler defaultFieldGroupDataHandler = new DefaultFieldGroupDataHandler();
	
	private static final class DefaultFieldGroupDataHandler implements FieldGroupDataHandler {

		@Override
		public void onBefore(HandlerType HandlerType, FieldGroup fieldGroup, Map<String, Object> param) {
			
		}

		@Override
		public void onAfter(HandlerType handlerType, FieldGroup fieldGroup, Map<String, Object> param) {
			
		}
	}
	
	private FieldGroupDataHandler getFieldGroupDataHandler(String groupName) {

		return SpringContextUtil.getBean(groupName + "DataHandler", FieldGroupDataHandler.class, defaultFieldGroupDataHandler);
	}

	private FormPageDataHandler getFormPageDataHandler(String pageName) {

		return SpringContextUtil.getBean(pageName + "DataHandler", FormPageDataHandler.class, defaultFormPageDataHandler);
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		
		
	}
}
