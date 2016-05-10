package com.nway.wform;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.nway.wform.entity.ComponentEntity;

@Controller
@RequestMapping("form")
public class FormController
{
    private final Logger log = LoggerFactory.getLogger(FormController.class);
    
    @RequestMapping("create")
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response)
    {
        String formId = request.getParameter("formId");
        
        request.setAttribute("components", Collections.singletonList("text"));
        
        return new ModelAndView("template/create");
    }
    
    @RequestMapping("edit")
    public ModelAndView edit(HttpServletRequest request, HttpServletResponse response)
    {
        String formId = request.getParameter("formId");
        
        List<ComponentEntity> components = new ArrayList<>();
        
        ComponentEntity label = new ComponentEntity();
        
        label.setComponentName("label");
        label.setComponentType("edit");
        
        ComponentEntity text = new ComponentEntity();
        
        text.setComponentName("text");
        text.setComponentType("edit");
        
        components.add(text);
        components.add(label);
        
        request.setAttribute("components", components);
        
        return new ModelAndView("template/edit");
    }
    
    @RequestMapping("detail")
    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response)
    {
        String formId = request.getParameter("formId");
        
        return new ModelAndView("template/detail");
    }
    
    @RequestMapping("list")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
    {
        String formId = request.getParameter("formId");
        
        return new ModelAndView("template/list");
    }
    
    @RequestMapping("component/page")
    public ModelAndView componentPage(HttpServletRequest request, HttpServletResponse response)
    {
        
        String componentName = request.getParameter("componentName");
        String componentType = request.getParameter("componentType");
        
        Map<String, Object> param = new HashMap<>();
        
        param.put("name", "abc");
        param.put("value", "试试看");
        
        return new ModelAndView("component/" + componentName + "/" + componentName + "_" + componentType,
                param);
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
