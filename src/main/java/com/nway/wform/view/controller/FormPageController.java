package com.nway.wform.view.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
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
	public ModelAndView create(String formPageId, HttpServletRequest request, HttpServletResponse reaponse) throws Exception {
		
		ModelAndView mv = new ModelAndView();
		
		FormPage formPage = formPageAccess.getFormPage(formPageId);
		
		mv.addObject("formPage", formPage);
		
		System.out.println(JSONObject.toJSONString(formPage));
		
		Template template = freemarker.getTemplate("/default/create.ftl");
		
		Map<String, Object> viewModel = new HashMap<String, Object>();
		
		viewModel.put("formPage", formPage);
		
		File jspFile = new File(request.getSession().getServletContext().getRealPath("/") + File.separator
				+ "WEB-INF/jsp/" + formPage.getName()+".jsp");
		
		jspFile.getParentFile().mkdirs();
		
		jspFile.createNewFile();
		
		FileOutputStream fos = new FileOutputStream(jspFile);
		
		OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
		
		template.process(viewModel, osw);
		
		fos.close();
		osw.close();
		
		Map<String, Object> dataModel = new HashMap<String, Object>();
		
		dataModel.put("title", "这是标题啊");
		
		mv.setViewName(formPage.getName());
		
		mv.addObject("dataModel", dataModel);
		
		for(FieldGroup group :formPage.getFielsGroups()) {
			
			mv.addObject("fieldAttr", Collections.singletonMap(group.getId(), formPageAccess.listFieldAttr(group.getId())));
		}
		
		return mv;
	}
	
	@RequestMapping("details")
	public ModelAndView details(String formPageId) {
		
		ModelAndView mv = new ModelAndView("template/details");
		
		mv.addObject("formPage", formPageAccess.getFormPage(formPageId));
		
		return mv;
	}
	
	public static void main(String[] args) {
		
		System.out.println(java.util.UUID.randomUUID().toString().replace("-", ""));
	}
}
