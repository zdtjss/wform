package com.nway.wform.view.controller;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.nway.wform.access.FormDataAccess;
import com.nway.wform.access.FormPageAccess;
import com.nway.wform.design.entity.Field;
import com.nway.wform.design.entity.FieldGroup;
import com.nway.wform.design.entity.FormPage;

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
	private Configuration freemarker;

	@RequestMapping("create")
	public ModelAndView create(HttpServletRequest request, HttpServletResponse reaponse) throws Exception {
		
		ModelAndView mv = new ModelAndView();
		
		String formPageId = request.getParameter("formPageId");
		
		FormPage formPage = formPageAccess.getFormPage(formPageId);
		
		Map<String, Object> viewModel = new HashMap<String, Object>();
		
		viewModel.put("formPage", formPage);
		
		makeJsp("/default/create.ftl", viewModel, formPage.getName(), request);
		
		Map<String, Object> dataModel = new HashMap<String, Object>();
		
		mv.setViewName(formPage.getName());
		
		mv.addObject("dataModel", dataModel);
		
		Map<String, Object> groupFieldAttr = new HashMap<String, Object>();
		
		for(FieldGroup group :formPage.getFielsGroups()) {
			
			groupFieldAttr.put(group.getId(), formPageAccess.listFieldAttr(group.getId()));
		}
		
		mv.addObject("fieldAttr", groupFieldAttr);
		
		return mv;
	}
	
	@RequestMapping("list")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse reaponse) throws Exception {
		
		ModelAndView mv = new ModelAndView();
		
		String formPageId = request.getParameter("pageId");
		
		FormPage formPage = formPageAccess.getFormPage(formPageId);
		
		mv.addObject("formPage", formPage);
		
		Map<String, Object> viewModel = new HashMap<String, Object>();
		
		viewModel.put("formPage", formPage);
		
		makeJsp("/default/list.ftl", viewModel, formPage.getName(), request);
		
		Map<String, Object> dataModel = new HashMap<String, Object>();
		
		mv.setViewName(formPage.getName());
		
		mv.addObject("dataModel", dataModel);
		
		Map<String, Object> groupFieldAttr = new HashMap<String, Object>();
		
		for(FieldGroup group :formPage.getFielsGroups()) {
			
			groupFieldAttr.put(group.getId(), formPageAccess.listFieldAttr(group.getId()));
		}
		
		mv.addObject("fieldAttr", groupFieldAttr);
		
		return mv;
	}
	
	@RequestMapping("save")
	@ResponseBody
	public Map<String, Object> save(@RequestBody Map<String, Map<String, String>> json) {
		
		String pageId = json.get("formPage").get("pageId");
		
		FormPage formPage = formPageAccess.getFormPage(pageId);
		
		Map<String, Map<String, Object>> formData = new HashMap<String, Map<String,Object>>();
		
		for(FieldGroup group : formPage.getFielsGroups()) {
			
			Map<String, String> groupDataOrigin = json.get(group.getName());
			
			Map<String, Object> groupData = new HashMap<String, Object>();
			
			for(Field field : group.getFields()) {
				
				groupData.put(field.getName(), field.getObjType().getValue(groupDataOrigin.get(field.getName())));
			}
			
			formData.put(group.getName(), groupData);
		}
		
		formDataAccess.create(formPage, formData);
		
		return Collections.<String, Object>singletonMap("status", 1);
	}
	
	@RequestMapping("details")
	public ModelAndView details(String formPageId) {
		
		ModelAndView mv = new ModelAndView("template/details");
		
		mv.addObject("formPage", formPageAccess.getFormPage(formPageId));
		
		return mv;
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
	

	private void makeJsp(String templateName, Map<String, Object> viewModel, String pageName,
			HttpServletRequest request) throws IOException, TemplateException {

		Template template = freemarker.getTemplate(templateName);

		File jspFile = new File(request.getSession().getServletContext().getRealPath("/") + File.separator
				+ "WEB-INF/jsp/" + pageName + ".jsp");

		jspFile.getParentFile().mkdirs();

		jspFile.createNewFile();
		
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
			if(fos != null) {
				fos.close();
			}
			if(osw != null) {
				osw.close();
			}
		}

		template.getConfiguration().clearTemplateCache();
	}
}
