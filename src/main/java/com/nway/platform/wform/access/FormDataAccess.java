package com.nway.platform.wform.access;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nway.platform.wform.access.handler.HandlerType;
import com.nway.platform.wform.access.handler.PageDataHandler;
import com.nway.platform.wform.access.mybatis.TemporaryStatementRegistry;
import com.nway.platform.wform.commons.SpringContextUtil;
import com.nway.platform.wform.component.MultiValueComponent;
import com.nway.platform.wform.design.entity.Page;
import com.nway.platform.wform.design.entity.PageForm;

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
	public void create(Page page, Map<String, Object> formData) {
		
		PageDataHandler formPageDataHandler = getPageDataHandler(page.getName());
		
		formPageDataHandler.handleParam(HandlerType.DATA_CREATE, page, formData);

		int effectCount = sqlSessionTemplate.insert(
				TemporaryStatementRegistry.getLastestName(page.getModuleName(), page.getName(), ACCESS_TYPE_CREATE), formData);
		
		if (effectCount > 0) {
			
			for (PageForm field : page.getFormFields()) {

				if (field.isMultiValue()) {

					((MultiValueComponent) field.getObjType()).save(page.getName(),
							formData.get(page.getKeyName()).toString(), formData.get(field.getName()));
				}
			}
		}
		
		formPageDataHandler.handleResult(HandlerType.DATA_CREATE, page, formData);
	}
	
	/**
	 * FormPage.PAGE_TYPE_EDIT && group.isEditable()
	 * 
	 * @param page
	 * @param formData
	 */
	public void update(Page page, Map<String, Object> formData) {
		
		PageDataHandler pageDataHandler = getPageDataHandler(page.getName());

		pageDataHandler.handleParam(HandlerType.DATA_MODIFY, page, formData);
		
		int effectCount = sqlSessionTemplate.update(TemporaryStatementRegistry.getLastestName(page.getModuleName(), page.getName(), ACCESS_TYPE_UPDATE), formData);
		
		if (effectCount > 0) {
			
			for (PageForm field : page.getFormFields()) {

				// 子表操作
				if (field.isMultiValue()) {

					((MultiValueComponent) field.getObjType()).update(page.getName(),
							formData.get(page.getKeyName()).toString(), formData.get(field.getName()));
				}
			}
		}
		
		pageDataHandler.handleResult(HandlerType.DATA_MODIFY, page, formData);
	}
	
	/**
	 * 对应详情页面
	 * 
	 * @param page
	 * @param param
	 * @return
	 */
	public Map<String, Object> get(Page page, String dataId) {
		
		Map<String, Object> pageData = Collections.emptyMap();
		
		Map<String, Object> param = Collections.<String, Object>singletonMap("pkId", dataId);
		
		PageDataHandler pageDataHandler = getPageDataHandler(page.getName());

		pageDataHandler.handleParam(HandlerType.DATA_QUERY, page, param);
		
		pageData = sqlSessionTemplate
				.selectOne(TemporaryStatementRegistry.getLastestName(page.getModuleName(), page.getName(), ACCESS_TYPE_DETAILS), dataId);
		
		for(PageForm field : page.getFormFields()) {
			
			// 子表操作
			if(field.isMultiValue()) {
				
				pageData.put(field.getName(), ((MultiValueComponent) field.getObjType())
						.getAssociatedValue(page.getName(), pageData.get(page.getKeyName()).toString()));
			}
		}
		
		pageDataHandler.handleResult(HandlerType.DATA_QUERY, page, pageData);
		
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
	public List<Map<String, Object>> list(Page page, Map<String, Object> param) {
		
		List<Map<String, Object>> pageData = Collections.emptyList();
		
		PageDataHandler pageDataHandler = getPageDataHandler(page.getName());
		
		pageDataHandler.handleParam(HandlerType.DATA_LIST, page, param);

		pageData = sqlSessionTemplate
				.selectList(TemporaryStatementRegistry.getLastestName(page.getModuleName(), page.getName(), ACCESS_TYPE_LIST), param);

		for (PageForm field : page.getFormFields()) {
			
			if (field.isMultiValue()) {

				for (Map<String, Object> row : pageData) {

					row.put(field.getName(), ((MultiValueComponent) field.getObjType())
							.getAssociatedValue(page.getName(), row.get(page.getKeyName()).toString()));
				}
			}
		}

		for(Map<String, Object> data : pageData) {
			
			pageDataHandler.handleResult(HandlerType.DATA_LIST, page, data);
		}
		
		return pageData;
	}
	
	public void remove(Page page, String dataId) {
		
		PageDataHandler pageDataHandler = getPageDataHandler(page.getName());
		
		Map<String, Object> param = Collections.<String, Object>singletonMap("pkId", dataId);

		pageDataHandler.handleParam(HandlerType.DATA_REMOVE, page, param);
		
		int effectCount = sqlSessionTemplate
				.delete(TemporaryStatementRegistry.getLastestName(page.getModuleName(), page.getName(), ACCESS_TYPE_REMOVE), dataId);
		
		for(PageForm field : page.getFormFields()) {

			if (effectCount > 0 && MultiValueComponent.class.isInstance(field.getObjType())) {

				((MultiValueComponent) field.getObjType()).remove(page.getName(), dataId);
			}
			
		}
		
		pageDataHandler.handleResult(HandlerType.DATA_REMOVE, page, param);
	}
	
	private static final PageDataHandler defaultFormPageDataHandler = new DefaultPageDataHandler();
	
	private static final class DefaultPageDataHandler implements PageDataHandler {

		@Override
		public void handleParam(HandlerType handlerType, Page formPage, Map<String, Object> param) {
			
		}

		@Override
		public void handleResult(HandlerType handlerType, Page formPage, Map<String, Object> param) {
			
		}

	}

	private PageDataHandler getPageDataHandler(String pageName) {

		return SpringContextUtil.getBean(pageName + "DataHandler", PageDataHandler.class, defaultFormPageDataHandler);
	}
}
