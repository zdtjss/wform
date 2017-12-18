package com.nway.wform.view.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.nway.wform.access.FormPageAccess;

@Controller
@RequestMapping("form")
public class FormPageController {
	
	@Autowired
	private FormPageAccess formPageAccess;

	public Object getPageData(String formPageId) {
		
		return null;
	}
	
	@RequestMapping("create")
	public ModelAndView create(String formPageId) {
		
		ModelAndView mv = new ModelAndView("template/create");
		
		mv.addObject("formPage", formPageAccess.getFormPage(formPageId));
		
		System.out.println(JSONObject.toJSONString(formPageAccess.getFormPage(formPageId)));
		
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
