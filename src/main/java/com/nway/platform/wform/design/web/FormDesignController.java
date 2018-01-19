package com.nway.platform.wform.design.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.nway.platform.wform.design.entity.Field;
import com.nway.platform.wform.design.service.FormPageAccess;

@Controller
@RequestMapping("formDesign")
public class FormDesignController {

	@Autowired
	private FormPageAccess formPageAccess;
	
	@RequestMapping("toFieldCreateUI")
	public String toFieldCreateUI(HttpServletRequest request) {

		request.setAttribute("pageId", request.getParameter("pageId"));
		
		return "formDesign/createField";
	}

	@RequestMapping("saveFields")
	public void saveFields(@RequestBody List<Map<String, String>> fields) {

		System.out.println(fields);
		
		formPageAccess.saveFields(fields);
	}
	
	@RequestMapping("toDesignUI")
	public ModelAndView toDesignUI(String pageId) {

		ModelAndView mv = new ModelAndView();

		mv.addObject("fields", formPageAccess.listFields(pageId));
		
		mv.setViewName("formDesign/pageDesign");

		return mv;
	}
}
