package com.nway.wform;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.nway.wform.entity.ComponentEntity;
import com.nway.wform.entity.FormEntity;
import com.nway.wform.service.FormService;
import com.nway.wform.web.BaseServlet;

@Controller
@RequestMapping("form")
public class FormController extends BaseServlet
{
    private final Logger log = LoggerFactory.getLogger(FormController.class);
    
    @Autowired
    private FormService formService;
    
	public void create(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        String formId = request.getParameter("formId");
        
        request.setAttribute("components", Collections.singletonList("text"));
        
		forword("template/create", request, response);
    }
    
	public void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        String formId = request.getParameter("formId");
        
        FormEntity form = formService.queryForm(1001, 1);
        
        List<ComponentEntity> components = form.getComponents();
        
        request.setAttribute("components", components);
        request.setAttribute("formId", 100001);
        request.setAttribute("formVersion", 1);
        request.setAttribute("formName", "testForm");
        
        forword("template/edit", request, response);
    }
    
	public void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        String formId = request.getParameter("formId");
        
        forword("template/detail", request, response);
    }
    
	public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        String formId = request.getParameter("formId");
        
        forword("template/list", request, response);
    }
    
	public void staticPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        
        String formId = request.getParameter("formId");
        String componentName = request.getParameter("name");
        String componentType = request.getParameter("type");
        String displayMode = request.getParameter("displayMode");
        
        Map<String, Object> param = new HashMap<>();
        
        param.put("name", "abc");
        param.put("value", "试试看");
        
        System.out.println(formId);
        
        forword("component/" + componentType + "/" + componentType + "_" + displayMode, request, response);
    }
    
    @RequestMapping("getData")
    public void getData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/json;charset=UTF-8");
        response.getWriter().print("{\"bz\":\"备注\"}");
    }
    
    @RequestMapping("release")
    public void release(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        StringBuffer requestUrl = request.getRequestURL();
        String templateName = request.getParameter("templateName");
        
        String releasePage = requestUrl.delete(requestUrl.length() - 7, requestUrl.length()) + templateName;
        
        log.info(releasePage);
        
        URL pageUrl = new URL(releasePage);
        
        HttpURLConnection urlConnection = (HttpURLConnection) pageUrl.openConnection();
        
        InputStream is = urlConnection.getInputStream();
        
        FileOutputStream fos = new FileOutputStream("C:\\page.html");
        
        int length = -1;
        byte[] b = new byte[4096];
        
        while ((length = is.read(b)) != -1)
        {
            fos.write(b, 0, length);
        }
        
        fos.close();
        is.close();
        urlConnection.disconnect();
    }
}
