package com.nway.wform.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.nway.wform.Constants;
import com.nway.wform.entity.ComponentEntity;
import com.nway.wform.entity.FormEntity;
import com.nway.wform.service.FormService;

@Controller
@RequestMapping("/form")
public class FormController
{
    private final Logger log = LoggerFactory.getLogger(FormController.class);
    
    @Autowired
    private FormService formService;
    
    @RequestMapping("create")
    public String create(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        	
        String formId = request.getParameter("formId");
        
        request.setAttribute("components", Collections.singletonList("text"));
        
		return "template/create";
    }
    
    @RequestMapping("edit")
    public String edit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        	
        String formId = request.getParameter("formId");
        String formVersion = request.getParameter("formVersion");
        String bid = request.getParameter("bid");
        
        FormEntity form = formService.queryForm(Integer.parseInt(formId), Integer.parseInt(formVersion));
        
        List<ComponentEntity> components = form.getComponents();
        
        request.setAttribute("formId", formId);
        request.setAttribute("formVersion", formVersion);
        request.setAttribute("bid", bid);
        request.setAttribute("components", components);
        request.setAttribute("formName", form.getName());
        request.setAttribute("htmlRender", Constants.RENDER_TYPE_HTML);
        request.setAttribute("fileRender", Constants.RENDER_TYPE_STATICFILE);
        request.setAttribute("jsRender", Constants.RENDER_TYPE_DYNAMIC);
        
        return "template/edit";
    }
    
    @RequestMapping("detail")
    public String detail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        	
        String formId = request.getParameter("formId");
        
       return "template/detail";
    }
    
    @RequestMapping("list")
	public String list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        String formId = request.getParameter("formId");
        
        return "template/list";
    }
    
    @RequestMapping("component/staticPage")
	public String componentStaticPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        
        String formId = request.getParameter("formId");
        String componentName = request.getParameter("name");
        String componentType = request.getParameter("type");
        String displayMode = request.getParameter("displayMode");
        
        request.setAttribute("componentName", componentName);
        request.setAttribute("value", "请填写内容");
        
        return "component/" + componentType + "/" + componentType + "_" + displayMode;
    }
    
    @ResponseBody
    @RequestMapping("getData")
    public Map<String,Object> getData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        return formService.queryFormData("firstForm", Integer.parseInt(request.getParameter("bid")));
    }
    
    @RequestMapping("saveData")
    public void saveData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        formService.saveData(request.getParameterMap());
    }
    
    @RequestMapping("release")
    public void release(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        StringBuffer requestUrl = request.getRequestURL();
        String templateName = request.getParameter("templateName");
        String basePath = request.getSession().getServletContext().getRealPath("customform");
        
        String releasePage = requestUrl.delete(requestUrl.length() - 7, requestUrl.length()) + templateName +"?formId=1001&requestVersion=1";
        
        log.info(releasePage);
        
        URL pageUrl = new URL(releasePage);
        
        HttpURLConnection urlConnection = (HttpURLConnection) pageUrl.openConnection();
        
        InputStream is = urlConnection.getInputStream();
        
        File releaseFile = new File(basePath+File.separator+"firstForm"+File.separator+"v_"+1);
        
        if(!releaseFile.exists()) {
            
            releaseFile.mkdirs();
        }
        
        FileOutputStream fos = new FileOutputStream(releaseFile + File.separator + "firstForm.html");
        
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
