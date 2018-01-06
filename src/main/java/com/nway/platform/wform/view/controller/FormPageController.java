package com.nway.platform.wform.view.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.nway.platform.wform.access.FormDataAccess;
import com.nway.platform.wform.design.entity.Field;
import com.nway.platform.wform.design.entity.FieldGroup;
import com.nway.platform.wform.design.entity.FormPage;
import com.nway.platform.wform.design.service.FormPageAccess;
import com.nway.platform.wform.view.service.FormPageService;
import com.nway.platform.workflow.entity.Handle;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Controller
@RequestMapping("form")
public class FormPageController {
	
	@Autowired
	private FormPageAccess formPageAccess;
	@Autowired
	private FormDataAccess formDataAccess;
	@Autowired
	private FormPageService formPageService;
	@Autowired
	private Configuration freemarker;
	
	@RequestMapping("toUI")
	public ModelAndView toUI(HttpServletRequest request, HttpServletResponse reaponse) throws Exception {
		
		ModelAndView view = new ModelAndView();
		
		Map<String, Object> viewModel = new HashMap<String, Object>();
		
		Map<String, Map<String, Object>> dataModel = Collections.emptyMap();
		
		String basePath = request.getSession().getServletContext().getRealPath("/");
		
		String bizId = request.getParameter("pkId");
		
		String pageType = request.getParameter("pageType");
		
		String formPageId = request.getParameter("pageId");
		
		FormPage formPage = formPageAccess.getFormPage(formPageId);
		
		viewModel.put("formPage", formPage);
		
		makeJsp(pageType, viewModel, formPage.getModuleName(), formPage.getName(), basePath);
		
		Map<String, Object> groupFieldAttr = new HashMap<String, Object>();
		
		for(FieldGroup group :formPage.getFielsGroups()) {
			
			groupFieldAttr.put(group.getId(), formPageAccess.listFieldAttr(group.getId()));
		}
		
		if(FormPage.PAGE_TYPE_DETAILS.equals(pageType) || FormPage.PAGE_TYPE_EDIT.equals(pageType)) {
			
			dataModel = formDataAccess.get(formPage, bizId);
		}
		
		view.addObject("dataModel", dataModel);
		view.addObject("fieldAttr", groupFieldAttr);
		view.setViewName(formPage.getName() + "_" + pageType);
		
		return view;
	}
	
	@RequestMapping("save")
	@ResponseBody
	public Map<String, Object> save(@RequestBody Map<String, Map<String, String>> jsonObj) {
		
		Map<String, String> formPageParam = jsonObj.get("formPage");
		
		FormPage formPage = formPageAccess.getFormPage(formPageParam.get("pageId"));
		
		Map<String, Map<String, Object>> formData = new HashMap<String, Map<String,Object>>();
		
		for(FieldGroup group : formPage.getFielsGroups()) {
			
			Map<String, String> groupDataOrigin = jsonObj.get(group.getName());
			
			Map<String, Object> groupData = new HashMap<String, Object>();
			
			for(Field field : group.getFields()) {
				
				groupData.put(field.getName(), field.getObjType().getValue(groupDataOrigin.get(field.getName())));
			}
			
			groupData.put("pkId", groupDataOrigin.get("pkId"));
			
			formData.put(group.getName(), groupData);
		}
		
		String pageType = formPageParam.get("pageType");
		
		Handle handleInfo = getHandleInfo(jsonObj);
		
		if(FormPage.PAGE_TYPE_CREATE.equals(pageType)) {
			
			formPageService.createAndStartProcess(formPage, handleInfo, formData);
		}
		else if(FormPage.PAGE_TYPE_EDIT.equals(pageType)) {
			
			formDataAccess.update(formPage, formData);
		}
		
		return Collections.<String, Object>singletonMap("status", 1);
	}
	
	@RequestMapping("listData")
	@ResponseBody
	public List<Map<String, Object>> listData(@RequestParam Map<String, String> pageParam, HttpServletRequest request) {
		
		String pageId = pageParam.get("pageId");
		
		FormPage formPage = formPageAccess.getFormPage(pageId);
		
		Map<String,Object> queryParam = new HashMap<String, Object>();
		
		for(FieldGroup group : formPage.getFielsGroups()) {
			
			if(group.getDisplayType() == FieldGroup.DISPLAY_TYPE_FORM) {
				
				for(Field field : group.getFields()) {
					
					queryParam.put(field.getName(), field.getObjType().getValue(pageParam.get(field.getName())));
				}
				
				break;
			}
		}
		
		PageHelper.startPage(request);
		
		return formDataAccess.list(formPage, queryParam);
	}
	
	private void makeJsp(String type, Map<String, Object> viewModel, String moduleName, String pageName,
			String basePath) throws IOException, TemplateException {

		Template template = null;
		
		if("create".equals(type)) {
			
			template = freemarker.getTemplate("/default/create.ftl");
		}
		else if("details".equals(type)) {
			
			template = freemarker.getTemplate("/default/details.ftl");
		}
		else if("update".equals(type)) {
			
			template = freemarker.getTemplate("/default/edit.ftl");
		}
		else if("list".equals(type)) {
			
			template = freemarker.getTemplate("/default/list.ftl");
		}
		
		StringBuilder jsp = new StringBuilder();

		jsp.append(basePath).append(File.separator).append("WEB-INF/jsp/").append(pageName).append("_").append(type).append(".jsp");

		File jspFile = new File(jsp.toString());
		
		File templateFile = new File(freemarker.getSharedVariable("absoluteTemplateDir").toString(), template.getName());
		
		if (templateFile.lastModified() > jspFile.lastModified()) {
			
			freemarker.removeTemplateFromCache(template.getName());

			File parentFile = jspFile.getParentFile();
			
			if(!parentFile.exists()) {
				
				parentFile.mkdirs();
			}

			if(!jspFile.exists()) {
				
				jspFile.createNewFile();
			}

			FileOutputStream fos = null;
			OutputStreamWriter osw = null;

			try {

				fos = new FileOutputStream(jspFile);

				osw = new OutputStreamWriter(fos, "utf-8");

				template.process(viewModel, osw);
			}
			catch (IOException e) {
				throw e;
			} 
			catch (TemplateException e) {
				throw e;
			} 
			finally {
				if (fos != null) {
					fos.close();
				}
				if (osw != null) {
					osw.close();
				}
			}

		}
	}
	
	private Handle getHandleInfo(Map<String, Map<String, String>> jsonObj) {		
		
		Handle handleInfo = new Handle();
		
		BeanUtils.copyProperties(jsonObj.get("workflow"), handleInfo);
		
		return handleInfo;
	}
}
