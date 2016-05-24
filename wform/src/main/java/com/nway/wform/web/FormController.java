package com.nway.wform.web;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nway.wform.Constants;
import com.nway.wform.entity.ComponentEntity;
import com.nway.wform.entity.FormEntity;
import com.nway.wform.service.FormService;

@WebServlet(name = "form", urlPatterns = { "/form/*" })
public class FormController extends BaseServlet
{
    private final Logger log = LoggerFactory.getLogger(FormController.class);
    
    private FormService formService = new FormService();
    
    public void create(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        	
        String formId = request.getParameter("formId");
        
        request.setAttribute("components", Collections.singletonList("text"));
        
		forword("template/create", request, response);
    }
    
    public void edit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        	
        String formId = request.getParameter("formId");
        String requestVersion = request.getParameter("requestVersion");
        
        FormEntity form = formService.queryForm(Integer.parseInt(formId), Integer.parseInt(requestVersion));
        
        List<ComponentEntity> components = form.getComponents();
        
        request.setAttribute("components", components);
        request.setAttribute("formName", form.getName());
        request.setAttribute("htmlRender", Constants.RENDER_TYPE_HTML);
        request.setAttribute("fileRender", Constants.RENDER_TYPE_STATICFILE);
        request.setAttribute("jsRender", Constants.RENDER_TYPE_DYNAMIC);
        
        forword("template/edit", request, response);
    }
    
    public void detail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        	
        String formId = request.getParameter("formId");
        
        forword("template/detail", request, response);
    }
    
	public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        String formId = request.getParameter("formId");
        
        forword("template/list", request, response);
    }
    
	public void componentStaticPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        
        String formId = request.getParameter("formId");
        String componentName = request.getParameter("name");
        String componentType = request.getParameter("type");
        String displayMode = request.getParameter("displayMode");
        
        request.setAttribute("componentName", componentName);
        request.setAttribute("value", "请填写内容");
        
        forword("component/" + componentType + "/" + componentType + "_" + displayMode, request, response);
    }
    
    public Map<String,Object> getData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        return formService.queryFormData(1);
    }
    
    public void saveData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        formService.saveData(request.getParameterMap());
    }
    
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
