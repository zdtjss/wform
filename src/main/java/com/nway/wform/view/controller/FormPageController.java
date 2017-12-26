package com.nway.wform.view.controller;

import java.io.File;
import java.io.FileOutputStream;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.nway.wform.access.FormPageAccess;
import com.nway.wform.design.entity.FieldGroup;
import com.nway.wform.design.entity.FormPage;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
@RequestMapping("form")
public class FormPageController {
	
	@Autowired
	private FormPageAccess formPageAccess;
	
	@Autowired
	private Configuration freemarker;

	public Object getPageData(String formPageId) {
		
		return null;
	}
	
	@RequestMapping("create")
	public ModelAndView create(HttpServletRequest request, HttpServletResponse reaponse) throws Exception {
		
		ModelAndView mv = new ModelAndView();
		
		String formPageId = request.getParameter("formPageId");
		
		FormPage formPage = formPageAccess.getFormPage(formPageId);
		
		mv.addObject("formPage", formPage);
		
		System.out.println(new Gson().toJson((formPage)));
		
		Template template = freemarker.getTemplate("/default/create.ftl");
		
		Map<String, Object> viewModel = new HashMap<String, Object>();
		
		viewModel.put("formPage", formPage);
		
		File jspFile = new File(request.getSession().getServletContext().getRealPath("/") + File.separator
				+ "WEB-INF/jsp/" + formPage.getName() + ".jsp");
		
		jspFile.getParentFile().mkdirs();
		
		jspFile.createNewFile();
		
		FileOutputStream fos = new FileOutputStream(jspFile);
		
		OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
		
		template.process(viewModel, osw);
		
		fos.close();
		osw.close();
		
		template.getConfiguration().clearTemplateCache();
		
		Map<String, Object> dataModel = new HashMap<String, Object>();
		
		mv.setViewName(formPage.getName());
		
		dataModel.put("title", "标题行不行");
		
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
		
		for(FieldGroup group : formPageAccess.getFormPage(pageId).getFielsGroups()) {
			
			System.out.println(json.get(group.getName()));
		}
		
		return Collections.<String, Object>singletonMap("status", 1);
	}
	
	@RequestMapping("details")
	public ModelAndView details(String formPageId) {
		
		ModelAndView mv = new ModelAndView("template/details");
		
		mv.addObject("formPage", formPageAccess.getFormPage(formPageId));
		
		return mv;
	}
	
}
