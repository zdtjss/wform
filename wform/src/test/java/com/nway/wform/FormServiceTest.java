package com.nway.wform;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.nway.wform.entity.ComponentEntity;
import com.nway.wform.entity.FormEntity;
import com.nway.wform.service.FormService;

public class FormServiceTest
{
    @Test
    public void testBuildSelectMapper()
    {
        
        FormService formService = new FormService();
        
        FormEntity form = new FormEntity();
        
        form.setComponents(getComponents());
        
        String mapper = formService.buildSelectMapper(form);
        
        System.out.println(mapper);
    }
    
    private List<ComponentEntity> getComponents()
    {
        
        List<ComponentEntity> components = new ArrayList<>();
        
        ComponentEntity text = new ComponentEntity();
        
        text.setId(100);
        text.setFormId("100001");
        text.setName("bz");
        text.setType("text");
        text.setRenderType(Constants.RENDER_TYPE_DYNAMIC);
        text.setEditable(true);
        
        ComponentEntity select = new ComponentEntity();
        
        select.setId(1001);
        select.setFormId("100001");
        select.setName("lx");
        select.setType("select");
        select.setRenderType(Constants.RENDER_TYPE_STATICFILE);
        select.setEditable(true);
        
        components.add(text);
        components.add(select);
        
        return components;
    }
}
