package com.nway.wform.access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nway.wform.access.component.MultiValueComponent;
import com.nway.wform.access.handler.FieldGroupDataCreateHandler;
import com.nway.wform.access.handler.FieldGroupDataListHandler;
import com.nway.wform.access.handler.FieldGroupDataModifyHandler;
import com.nway.wform.access.handler.FieldGroupDataQueryHandler;
import com.nway.wform.access.handler.FieldGroupDataRemoveHandler;
import com.nway.wform.access.handler.FormPageDataCreateHandler;
import com.nway.wform.access.handler.FormPageDataListHandler;
import com.nway.wform.access.handler.FormPageDataModifyHandler;
import com.nway.wform.access.handler.FormPageDataQueryHandler;
import com.nway.wform.access.handler.FormPageDataRemoveHandler;
import com.nway.wform.access.mybatis.TemporaryStatementRegistry;
import com.nway.wform.commons.SpringContextUtil;
import com.nway.wform.design.entity.Field;
import com.nway.wform.design.entity.FieldGroup;
import com.nway.wform.design.entity.FormPage;

@Component
public class FormDataAccess {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	/**
	 * FormPage.PAGE_TYPE_CREATE && group.isEditable()
	 * 
	 * @param formPage
	 * @param formData
	 */
	public void create(FormPage formPage, Map<String, Map<String, Object>> formData) {
		
		FormPageDataCreateHandler formPageDataCreateHandler = SpringContextUtil.getBean(
				formPage.getName() + "DataCreateHandler", FormPageDataCreateHandler.class, defaultFormPageDataCreateHandler);
		
		formPageDataCreateHandler.onBefore(formPage, formData);
		
		for(FieldGroup group : formPage.getComponentGroups()) {
			
			FieldGroupDataCreateHandler fieldGroupDataModifyHandler = SpringContextUtil.getBean(
					group.getName() + "DataModifyHandler", FieldGroupDataCreateHandler.class, defaultFieldGroupDataCreateHandler);
						
			fieldGroupDataModifyHandler.onBefore(group, formData.get(group.getName()));
			
			sqlSessionTemplate.insert(TemporaryStatementRegistry.getLastestName(group.getFullName()), formData);
			
			// 子表操作
			for(Field field : group.getFields()) {
				
				if(MultiValueComponent.class.isInstance(field)) {

				}
			}
			
			fieldGroupDataModifyHandler.onAfter(group, formData.get(group.getName()));
		}
		
		formPageDataCreateHandler.onAfter(formPage, formData);
	}
	
	
	/**
	 * FormPage.PAGE_TYPE_EDIT && group.isEditable()
	 * 
	 * @param formPage
	 * @param formData
	 */
	public void update(FormPage formPage, Map<String, Map<String, Object>> formData) {
		
		FormPageDataModifyHandler formPageDataModifyHandler = SpringContextUtil.getBean(
				formPage.getName() + "DataModifyHandler", FormPageDataModifyHandler.class, defaultFormPageDataModifyHandler);
		
		formPageDataModifyHandler.onBefore(formPage, formData);
		
		for(FieldGroup group : formPage.getComponentGroups()) {
			
			FieldGroupDataModifyHandler fieldGroupDataModifyHandler = SpringContextUtil.getBean(
					formPage.getName() + "DataModifyHandler", FieldGroupDataModifyHandler.class, defaultFieldGroupDataModifyHandler);
						
			fieldGroupDataModifyHandler.onBefore(group, formData.get(group.getName()));
			
			sqlSessionTemplate.update(TemporaryStatementRegistry.getLastestName(group.getFullName()), formData);
			
			// 子表操作
			for(Field field : group.getFields()) {
				
				if(MultiValueComponent.class.isInstance(field)) {

				}
			}
			
			fieldGroupDataModifyHandler.onAfter(group, formData.get(group.getName()));
		}
		
		formPageDataModifyHandler.onAfter(formPage, formData);
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
		
		FormPageDataQueryHandler formPageDataQueryHandler = SpringContextUtil.getBean(
				formPage.getName() + "DataQueryHandler", FormPageDataQueryHandler.class, defaultFormPageDataQueryHandler);
		
		formPageDataQueryHandler.onBefore(formPage, dataId);
		
		for(FieldGroup group : formPage.getComponentGroups()) {
			
			FieldGroupDataQueryHandler fieldGroupDataQueryHandler = SpringContextUtil.getBean(
					group.getFullName() + "DataQueryHandler", FieldGroupDataQueryHandler.class, defaultFieldGroupDataQueryHandler);
			
			fieldGroupDataQueryHandler.onBefore(group, dataId);
			
			Map<String, Object> groupData = sqlSessionTemplate.selectOne(TemporaryStatementRegistry.getLastestName(group.getFullName()), dataId);

			// 子表操作
			for (Field field : group.getFields()) {

				if (MultiValueComponent.class.isInstance(field)) {

				}
			}

			pageData.put(group.getName(), groupData);

			fieldGroupDataQueryHandler.onAfter(group, groupData);
		}
		
		formPageDataQueryHandler.onAfter(formPage, dataId);
		
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
	public List<Map<String, Object>> list(FormPage formPage, Map<String, Map<String, Object>> param) {
		
		List<Map<String, Object>> pageData = new ArrayList<Map<String, Object>>();
		
		FormPageDataListHandler formPageDataListHandler = SpringContextUtil.getBean(
				formPage.getName() + "DataListHandler", FormPageDataListHandler.class, defaultFormPageDataListHandler);
		
		formPageDataListHandler.onBefore(formPage, param);
		
		for(FieldGroup group : formPage.getComponentGroups()) {
			
			FieldGroupDataListHandler fieldGroupDataListHandler = SpringContextUtil.getBean(
					group.getFullName() + "DataListHandler", FieldGroupDataListHandler.class, defaultFieldGroupDataListHandler);
			
			fieldGroupDataListHandler.onBefore(group, param.get(group.getName()));
			
			pageData = sqlSessionTemplate.selectList(TemporaryStatementRegistry.getLastestName(group.getFullName()), param);
				
			// 子表操作
			for(Field field : group.getFields()) {
					
				if(MultiValueComponent.class.isInstance(field)) {
					
					
				}
			}
			
			fieldGroupDataListHandler.onAfter(group, param.get(group.getName()));	
		}
	
		formPageDataListHandler.onAfter(formPage, param);
		
		return pageData;
	}
	
	public void remove(FormPage formPage, String dataId) {
		
		FormPageDataRemoveHandler formPageDataRemoveHandler = SpringContextUtil.getBean(
				formPage.getName() + "DataRemoveHandler", FormPageDataRemoveHandler.class, defaultFormPageDataRemoveHandler);
		
		formPageDataRemoveHandler.onBefore(formPage, dataId);
		
		for(FieldGroup group : formPage.getComponentGroups()) {

			FieldGroupDataRemoveHandler fieldGroupDataRemoveHandler = SpringContextUtil.getBean(
					group.getFullName() + "DataRemoveHandler", FieldGroupDataRemoveHandler.class, defaultFieldGroupDataRemoveHandler);
			
			fieldGroupDataRemoveHandler.onBefore(group, dataId);
			
			int effectCount = sqlSessionTemplate.delete(TemporaryStatementRegistry.getLastestName(group.getFullName()), dataId);
			
			if (effectCount > 0) {

				// 子表操作
				for (Field field : group.getFields()) {
					
					if (MultiValueComponent.class.isInstance(field)) {

						
					}
				}
			}
			fieldGroupDataRemoveHandler.onAfter(group, dataId);
		}
		
		formPageDataRemoveHandler.onAfter(formPage, dataId);
	}
	
	private static final FormPageDataRemoveHandler defaultFormPageDataRemoveHandler = new DefaultFormPageDataRemoveHandler();
	
	private static final class DefaultFormPageDataRemoveHandler implements FormPageDataRemoveHandler {

		@Override
		public void onBefore(FormPage formPage, String dataId) {
			
		}

		@Override
		public void onAfter(FormPage formPage, String dataId) {
			
		}
		
	}

	private static final DefaultFieldGroupDataRemoveHandler defaultFieldGroupDataRemoveHandler = new DefaultFieldGroupDataRemoveHandler();
	
	private static final class DefaultFieldGroupDataRemoveHandler implements FieldGroupDataRemoveHandler {

		@Override
		public void onBefore(FieldGroup fieldGroup, String dataId) {

		}

		@Override
		public void onAfter(FieldGroup fieldGroup, String dataId) {

		}
		
	}
	
	private static final DefaultFieldGroupDataModifyHandler defaultFieldGroupDataModifyHandler = new DefaultFieldGroupDataModifyHandler();
	
	private static final class DefaultFieldGroupDataModifyHandler implements FieldGroupDataModifyHandler {

		@Override
		public void onBefore(FieldGroup fieldGroup, Map<String, Object> groupData) {
			
		}

		@Override
		public void onAfter(FieldGroup fieldGroup, Map<String, Object> groupData) {
			
		}
		
	}
	
	private static final DefaultFieldGroupDataQueryHandler defaultFieldGroupDataQueryHandler = new DefaultFieldGroupDataQueryHandler();
	
	private static final class DefaultFieldGroupDataQueryHandler implements FieldGroupDataQueryHandler {

		@Override
		public void onBefore(FieldGroup fieldGroup, String dataId) {
			
		}

		@Override
		public void onAfter(FieldGroup fieldGroup, Map<String, Object> groupData) {
			
		}
		
	}
	
	private static final DefaultFormPageDataModifyHandler defaultFormPageDataModifyHandler = new DefaultFormPageDataModifyHandler();
	
	private static final class DefaultFormPageDataModifyHandler implements FormPageDataModifyHandler {

		@Override
		public void onBefore(FormPage formPage, Map<String, Map<String, Object>> requestParam) {
			
		}

		@Override
		public void onAfter(FormPage formPage, Map<String, Map<String, Object>> requestParam) {
			
		}

	}
	
	private static final DefaultFormPageDataQueryHandler defaultFormPageDataQueryHandler = new DefaultFormPageDataQueryHandler();
	
	private static final class DefaultFormPageDataQueryHandler implements FormPageDataQueryHandler {

		@Override
		public void onBefore(FormPage formPage, String dataId) {
			
		}

		@Override
		public void onAfter(FormPage formPage, String dataId) {
			
		}
		
	}
	
	private static final DefaultFormPageDataListHandler defaultFormPageDataListHandler = new DefaultFormPageDataListHandler();
	
	private static final class DefaultFormPageDataListHandler implements FormPageDataListHandler {

		@Override
		public void onBefore(FormPage formPage, Map<String, Map<String, Object>> requestParam) {
			
		}

		@Override
		public void onAfter(FormPage formPage, Map<String, Map<String, Object>> requestParam) {
			
		}
		
	}
	
	private static final DefaultFieldGroupDataListHandler defaultFieldGroupDataListHandler = new DefaultFieldGroupDataListHandler();
	
	private static final class DefaultFieldGroupDataListHandler implements FieldGroupDataListHandler {

		@Override
		public void onBefore(FieldGroup fieldGroup, Map<String, Object> requestData) {
			
		}

		@Override
		public void onAfter(FieldGroup fieldGroup, Map<String, Object> requestData) {
			
		}
		
	}
	
	private static final DefaultFieldGroupDataCreateHandler defaultFieldGroupDataCreateHandler = new DefaultFieldGroupDataCreateHandler();
	
	private static final class DefaultFieldGroupDataCreateHandler implements FieldGroupDataCreateHandler {

		@Override
		public void onBefore(FieldGroup fieldGroup, Map<String, Object> groupData) {
			
		}

		@Override
		public void onAfter(FieldGroup fieldGroup, Map<String, Object> groupData) {
			
		}
		
	}
	
	private static final DefaultFormPageDataCreateHandler defaultFormPageDataCreateHandler = new DefaultFormPageDataCreateHandler();
	
	private static final class DefaultFormPageDataCreateHandler implements FormPageDataCreateHandler {

		@Override
		public void onBefore(FormPage formPage, Map<String, Map<String, Object>> requestParam) {
			
		}

		@Override
		public void onAfter(FormPage formPage, Map<String, Map<String, Object>> requestParam) {
			
		}
		
	}
}
