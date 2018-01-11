package com.nway.platform.wform.view.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.nway.platform.wform.access.FormDataAccess;
import com.nway.platform.wform.component.Initializable;
import com.nway.platform.wform.design.entity.FormPage;
import com.nway.platform.wform.design.entity.PageFieldForm;
import com.nway.platform.wform.design.entity.PageFieldList;
import com.nway.platform.wform.design.service.FormPageAccess;
import com.nway.platform.wform.view.service.FormPageService;
import com.nway.platform.workflow.entity.Handle;
import com.nway.platform.workflow.entity.Handle.SimpleUser;

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
		
		Map<String, Object> dataModel = new HashMap<String, Object>();
		
		String basePath = request.getSession().getServletContext().getRealPath("/");
		
		String bizId = request.getParameter("pkId");
		String pageId = request.getParameter("pageId");
		String pageType = request.getParameter("pageType");
		String taskId = request.getParameter("taskId");
		
		FormPage formPage = formPageAccess.getFormPage(pageId);
		
		viewModel.put("page", formPage);
		
		makeJsp(pageType, viewModel, formPage.getModuleName(), formPage.getName(), basePath);
			
		Map<String, Map<String, String>> fieldAttr = formPageAccess.listFieldAttr(pageId);
		
		if(FormPage.PAGE_TYPE_DETAILS.equals(pageType) || FormPage.PAGE_TYPE_EDIT.equals(pageType)) {
			
			dataModel = formDataAccess.get(formPage, bizId);
		}
			
		for(PageFieldForm field : formPage.getFormFields()) {
			
			if(Initializable.class.isInstance(field.getObjType())) {
				
				dataModel.put(field.getName() + "_init", ((Initializable) field.getObjType()).init(formPage.getName()));
			}
		}
		
		view.addObject("dataModel", dataModel);
		view.addObject("fieldAttr", fieldAttr); 
		view.addObject("workflow", Collections.singletonMap("taskId", taskId)); 
		view.setViewName(formPage.getModuleName() + "/" + formPage.getName() + "_" + pageType);
		
		return view;
	}
	
	@RequestMapping("save")
	@ResponseBody
	public Map<String, Object> save(@RequestBody Map<String, Map<String, String>> jsonObj) {
		
		Map<String, String> pageParam = jsonObj.get("formPage");
		
		Map<String, String> pageData = jsonObj.get("pageData");
		
		Map<String, Object> formData = new HashMap<String, Object>();
		
		FormPage formPage = formPageAccess.getFormPage(pageParam.get("pageId"));
		
		for(PageFieldForm field : formPage.getFormFields()) {
			
			formData.put(field.getName(), field.getObjType().getValue(pageData.get(field.getName())));
		}
		
		String pageType = pageParam.get("pageType");
		
		Handle handleInfo = getHandleInfo(jsonObj);
		
		if(FormPage.PAGE_TYPE_CREATE.equals(pageType)) {
			
			formPageService.createAndStartProcess(formPage, handleInfo, formData);
		}
		else if(FormPage.PAGE_TYPE_EDIT.equals(pageType)) {
			
			formPageService.saveAndHandle(formPage, handleInfo, formData);
		}
		else {
			
			formPageService.handle(formPage, handleInfo, formData);
		}
		
		return Collections.<String, Object>singletonMap("status", 1);
	}
	
	@RequestMapping("listData")
	@ResponseBody
	public List<Map<String, Object>> listData(@RequestParam Map<String, String> pageParam, HttpServletRequest request) {
		
		String pageId = pageParam.get("pageId");
		
		FormPage formPage = formPageAccess.getFormPage(pageId);
		
		Map<String,Object> queryParam = new HashMap<String, Object>();
		
		for (PageFieldList field : formPage.getListFields()) {

			if (field.isCondition()) {

				queryParam.put(field.getName(), field.getObjType().getValue(pageParam.get(field.getName())));
			}
		}
		
		PageHelper.startPage(request);
		
		return formDataAccess.list(formPage, queryParam);
	}
	
	private void makeJsp(String type, Map<String, Object> viewModel, String moduleName, String pageName,
			String basePath) throws IOException, TemplateException {

		Template template = null;
		
		if(FormPage.PAGE_TYPE_CREATE.equals(type)) {
			
			template = freemarker.getTemplate("/default/create.ftl");
		}
		else if(FormPage.PAGE_TYPE_DETAILS.equals(type)) {
			
			template = freemarker.getTemplate("/default/details.ftl");
		}
		else if(FormPage.PAGE_TYPE_EDIT.equals(type)) {
			
			template = freemarker.getTemplate("/default/edit.ftl");
		}
		else if(FormPage.PAGE_TYPE_LIST.equals(type)) {
			
			template = freemarker.getTemplate("/default/list.ftl");
		}
		
		StringBuilder jsp = new StringBuilder();

		jsp.append(basePath).append(File.separator).append("WEB-INF/jsp/").append(moduleName).append("/")
				.append(pageName).append("_").append(type).append(".jsp");

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
				if (osw != null) {
					osw.close();
				}
				if (fos != null) {
					fos.close();
				}
			}

		}
	}
	
	private Handle getHandleInfo(Map<String, Map<String, String>> jsonObj) {		
		
		Handle handleInfo = new Handle();
		
		SimpleUser user1 = new SimpleUser();
		
		user1.setUserId("12312");
		user1.setCnName("品牌");

		try {
			
			BeanUtils.populate(handleInfo, jsonObj.get("workflow"));
		} 
		catch (IllegalAccessException e) {
			e.printStackTrace();
		} 
		catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return handleInfo;
	}
}
