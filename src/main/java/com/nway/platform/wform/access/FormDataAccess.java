package com.nway.platform.wform.access;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nway.platform.wform.access.component.MultiValueComponent;
import com.nway.platform.wform.access.handler.FieldGroupDataHandler;
import com.nway.platform.wform.access.handler.FormPageDataHandler;
import com.nway.platform.wform.access.handler.HandlerType;
import com.nway.platform.wform.access.mybatis.TemporaryStatementRegistry;
import com.nway.platform.wform.commons.SpringContextUtil;
import com.nway.platform.wform.design.entity.Field;
import com.nway.platform.wform.design.entity.FieldGroup;
import com.nway.platform.wform.design.entity.FormPage;

@Component
public class FormDataAccess {

	private static final String ACCESS_TYPE_CREATE = "save";
	
	private static final String ACCESS_TYPE_UPDATE = "update";
	
	private static final String ACCESS_TYPE_DETAILS = "details";
	
	private static final String ACCESS_TYPE_LIST = "list";
	
	private static final String ACCESS_TYPE_REMOVE = "remove";
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	/**
	 * FormPage.PAGE_TYPE_CREATE && group.isEditable()
	 * 
	 * @param page
	 * @param formData
	 */
	public void create(FormPage page, Map<String, Map<String, Object>> formData) {
		
		FormPageDataHandler formPageDataHandler = getFormPageDataHandler(page.getName());
		
		formPageDataHandler.handleParam(HandlerType.FORM_PAGE_DATA_CREATE, page, (Map) formData);
		
		for(FieldGroup group : page.getFielsGroups()) {
			
			Map<String, Object> groupData = formData.get(group.getName());
			
			groupData.put("pkId", UUID.randomUUID().toString());
			
			FieldGroupDataHandler fieldGroupDataHandler = getFieldGroupDataHandler(page.getName(), group.getName());
						
			fieldGroupDataHandler.handleParam(HandlerType.FIELD_GROUP_DATA_CREATE, group, groupData);
			
			int effectCount = sqlSessionTemplate.insert(
					TemporaryStatementRegistry.getLastestName(page.getName(), group.getName(), ACCESS_TYPE_CREATE), groupData);
			
			group.setEffectCount(effectCount);
			
			// 子表操作
			for(Field field : group.getFields()) {
				
				if(effectCount > 0 && MultiValueComponent.class.isInstance(field.getObjType())) {

					((MultiValueComponent) field.getObjType()).save(groupData.get(field.getName()));
				}
			}
			
			fieldGroupDataHandler.handleResult(HandlerType.FIELD_GROUP_DATA_CREATE, group, formData.get(group.getName()));
		}
		
		formPageDataHandler.handleResult(HandlerType.FORM_PAGE_DATA_CREATE, page, (Map) formData);
	}
	
	/**
	 * FormPage.PAGE_TYPE_EDIT && group.isEditable()
	 * 
	 * @param page
	 * @param formData
	 */
	public void update(FormPage page, Map<String, Map<String, Object>> formData) {
		
		FormPageDataHandler formPageDataHandler = getFormPageDataHandler(page.getName());

		formPageDataHandler.handleParam(HandlerType.FORM_PAGE_DATA_MODIFY, page, (Map) formData);
		
		for(FieldGroup group : page.getFielsGroups()) {
			
			Map<String, Object> groupData = formData.get(group.getName());
			
			FieldGroupDataHandler fieldGroupDataHandler = getFieldGroupDataHandler(page.getName(), group.getName());
			
			fieldGroupDataHandler.handleParam(HandlerType.FIELD_GROUP_DATA_MODIFY, group, groupData);
			
			int effectCount = sqlSessionTemplate.update(TemporaryStatementRegistry.getLastestName(page.getName(), group.getName(), ACCESS_TYPE_UPDATE), groupData);
			
			group.setEffectCount(effectCount);
			
			// 子表操作
			for(Field field : group.getFields()) {
				
				if(effectCount > 0 && MultiValueComponent.class.isInstance(field.getObjType())) {

					((MultiValueComponent) field.getObjType()).save(groupData.get(field.getName()));
				}
			}
			
			fieldGroupDataHandler.handleResult(HandlerType.FIELD_GROUP_DATA_MODIFY, group, formData.get(group.getName()));
		}
		
		formPageDataHandler.handleResult(HandlerType.FORM_PAGE_DATA_MODIFY, page, (Map) formData);
	}
	
	/**
	 * 对应详情页面
	 * 
	 * @param page
	 * @param param
	 * @return
	 */
	public Map<String, Map<String, Object>> get(FormPage page, String dataId) {
		
		Map<String, Map<String, Object>> pageData = new HashMap<String, Map<String, Object>>();
		
		Map<String, Object> param = Collections.<String, Object>singletonMap("id", dataId);
		
		FormPageDataHandler formPageDataHandler = getFormPageDataHandler(page.getName());

		formPageDataHandler.handleParam(HandlerType.FORM_PAGE_DATA_QUERY, page, param);
		
		for(FieldGroup group : page.getFielsGroups()) {
			
			FieldGroupDataHandler fieldGroupDataHandler = getFieldGroupDataHandler(page.getName(), group.getName());
			
			fieldGroupDataHandler.handleParam(HandlerType.FIELD_GROUP_DATA_QUERY, group, param);
			
			Map<String, Object> groupData = sqlSessionTemplate.selectOne(TemporaryStatementRegistry.getLastestName(page.getName(), group.getName(), ACCESS_TYPE_DETAILS), dataId);

			// 子表操作
			for (Field field : group.getFields()) {

				if(MultiValueComponent.class.isInstance(field.getObjType())) {
					
					groupData.put(field.getName(), ((MultiValueComponent) field.getObjType()).getAssociatedValue(String.valueOf(groupData.get(field.getName()))));
				}
			}

			pageData.put(group.getId(), groupData);

			fieldGroupDataHandler.handleResult(HandlerType.FIELD_GROUP_DATA_QUERY, group, groupData);
		}
		
		formPageDataHandler.handleResult(HandlerType.FORM_PAGE_DATA_QUERY, page, pageData);
		
		return pageData;
	}
	
	/**
	 * 对应 FormPage.PAGE_TYPE_LIST
	 * 
	 * 对于点击列表在本页显示详情的情况，使用详情页中加list组件解决
	 * 
	 * @param page
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> list(FormPage page, Map<String, Object> param) {
		
		List<Map<String, Object>> pageData = new ArrayList<Map<String, Object>>();
		
		FormPageDataHandler formPageDataHandler = getFormPageDataHandler(page.getName());
		
		formPageDataHandler.handleParam(HandlerType.FORM_PAGE_DATA_LIST, page, param);
		
		// 这里可能是条件组  有待调整
		for (FieldGroup group : page.getFielsGroups()) {

			if (group.getDisplayType() == FieldGroup.DISPLAY_TYPE_FORM) {

				getFieldGroupDataHandler(page.getName(), group.getName()).handleParam(HandlerType.FIELD_GROUP_DATA_LIST, group, param);
				
				break;
			}
		}
		
		for (FieldGroup group : page.getFielsGroups()) {

			if (group.getDisplayType() == FieldGroup.DISPLAY_TYPE_LIST) {
				
				pageData = sqlSessionTemplate.selectList(TemporaryStatementRegistry.getLastestName(page.getName(), group.getName(), ACCESS_TYPE_LIST), param);

				// 子表操作
				for (Field field : group.getFields()) {

					if (MultiValueComponent.class.isInstance(field.getObjType())) {

						for (Map<String, Object> row : pageData) {

							row.put(field.getName(), ((MultiValueComponent) field.getObjType())
									.getAssociatedValue(String.valueOf(row.get(field.getName()))));
						}
					}
				}
				
				getFieldGroupDataHandler(page.getName(), group.getName()).handleResult(HandlerType.FIELD_GROUP_DATA_LIST, group, pageData);
			}
		}

		formPageDataHandler.handleResult(HandlerType.FORM_PAGE_DATA_LIST, page, pageData);
		
		return pageData;
	}
	
	public void remove(FormPage page, String dataId) {
		
		FormPageDataHandler formPageDataHandler = getFormPageDataHandler(page.getName());
		
		Map<String, Object> param = Collections.<String, Object>singletonMap("id", dataId);

		formPageDataHandler.handleParam(HandlerType.FORM_PAGE_DATA_REMOVE, page, param);
		
		for(FieldGroup group : page.getFielsGroups()) {

			FieldGroupDataHandler fieldGroupDataHandler = getFieldGroupDataHandler(page.getName(), group.getName());

			fieldGroupDataHandler.handleParam(HandlerType.FIELD_GROUP_DATA_REMOVE, group, param);

			int effectCount = sqlSessionTemplate.delete(TemporaryStatementRegistry.getLastestName(page.getName(), group.getName(), ACCESS_TYPE_REMOVE), dataId);
			
			group.setEffectCount(effectCount);
			
			if (effectCount > 0) {

				// 子表操作
				for (Field field : group.getFields()) {
					
					if(MultiValueComponent.class.isInstance(field.getObjType())) {
						
						((MultiValueComponent) field.getObjType()).remove(dataId);
					}
				}
			}
			fieldGroupDataHandler.handleResult(HandlerType.FIELD_GROUP_DATA_REMOVE, group, param);
		}
		
		formPageDataHandler.handleResult(HandlerType.FORM_PAGE_DATA_REMOVE, page, param);
	}
	
	private static final FormPageDataHandler defaultFormPageDataHandler = new DefaultFormPageDataHandler();
	
	private static final class DefaultFormPageDataHandler implements FormPageDataHandler {

		@Override
		public void handleParam(HandlerType handlerType, FormPage formPage, Map<String, Object> param) {
			
		}

		@Override
		public void handleResult(HandlerType handlerType, FormPage formPage, Object param) {
			
		}

	}

	private static final DefaultFieldGroupDataHandler defaultFieldGroupDataHandler = new DefaultFieldGroupDataHandler();
	
	private static final class DefaultFieldGroupDataHandler implements FieldGroupDataHandler {

		@Override
		public void handleParam(HandlerType HandlerType, FieldGroup fieldGroup, Map<String, Object> param) {
			
		}

		@Override
		public void handleResult(HandlerType handlerType, FieldGroup fieldGroup, Object param) {
			
		}
	}
	
	private FieldGroupDataHandler getFieldGroupDataHandler(String pageName, String groupName) {

		String baseName = pageName + String.valueOf(groupName.charAt(0)).toUpperCase() + groupName.substring(1);
		
		return SpringContextUtil.getBean(baseName + "DataHandler", FieldGroupDataHandler.class, defaultFieldGroupDataHandler);
	}

	private FormPageDataHandler getFormPageDataHandler(String pageName) {

		return SpringContextUtil.getBean(pageName + "DataHandler", FormPageDataHandler.class, defaultFormPageDataHandler);
	}

}
