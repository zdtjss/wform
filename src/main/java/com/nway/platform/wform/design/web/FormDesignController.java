package com.nway.platform.wform.design.web;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
	public void saveFields(@RequestBody List<Map<String, Map<String, String>>> fields) {

		System.out.println(fields);
		
		formPageAccess.saveFields(fields);
	}
	
	@RequestMapping("toDesignUI")
	public ModelAndView toDesignUI(String pageId) {

		ModelAndView mv = new ModelAndView();
		
		mv.addObject("pageId", pageId);

		mv.addObject("fields", formPageAccess.listFields(pageId));
		
		mv.setViewName("formDesign/pageDesign");

		return mv;
	}
	
	@RequestMapping("showCustomAttr")
	public void showCustomAttr(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String type = request.getParameter("type");
		String jsp = "/WEB-INF/wform/component/" + type + "/" + type + "_attr.jsp";
		String jspPath = request.getSession().getServletContext().getRealPath(jsp);
		
		if( jsp != null && new File(jspPath).exists()) {
			
			request.getRequestDispatcher(jsp).forward(request, response);
		}
		else {
			
			response.getWriter().write("");
		}
	}
}
