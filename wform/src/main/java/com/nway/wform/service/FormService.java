package com.nway.wform.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.nway.wform.entity.ComponentEntity;
import com.nway.wform.entity.FormEntity;
import com.nway.wform.jdbc.MybatisExecutor;
import com.nway.wform.jdbc.MybatisUtils;
import com.nway.wform.service.component.ComponentService;
import com.nway.wform.service.component.MultiValueService;
import com.nway.wform.service.component.SelectService;
import com.nway.wform.service.component.TextService;

@Service
public class FormService
{
    private static final Map<String, ComponentService> componentService = new HashMap<>();
    
    static
    {
        componentService.put("text", new TextService());
        componentService.put("select", new SelectService());
        componentService.put("multiValue", new MultiValueService());
        
    }
    
    private MybatisExecutor mybatisExecutor = MybatisUtils.getMybatisExecutor();
    
    public String buildSelectMapper(FormEntity form)
    {
        StringBuilder selectedColumnes = new StringBuilder("select ");
        StringBuilder selectedTables = new StringBuilder(" ");
        StringBuilder resultMap = new StringBuilder(" ");
        
        for (ComponentEntity component : form.getComponents())
        {
            selectedColumnes
                    .append(componentService.get(component.getType()).buildQuerySql(component).getColumnes())
                    .append(',');
            selectedTables
                    .append(componentService.get(component.getType()).buildQuerySql(component).getWithTable())
                    .append(' ');
            resultMap.append(componentService.get(component.getType()).buildResultMap(component));
        }
        
        selectedColumnes = selectedColumnes.length() > 7
                ? selectedColumnes.deleteCharAt(selectedColumnes.length() - 1) : selectedColumnes;
        
        return selectedColumnes.append(" from t_main ").append(selectedTables).toString()+"\n" +resultMap;
    }
    
    public FormEntity queryForm(int formId,int version)
    {
        Map<String,Object> param = new HashMap<>();
        
        param.put("formId", formId);
        param.put("version", version);
        
        return mybatisExecutor.selectOne("selectForm", param);
    }
    
    public Map<String,Object> queryFormData(int id)
    {
        return mybatisExecutor.selectOne("select_firstForm", id);
    }
    
    public void saveData(Map<String, String[]> params) {
        
        String formId = params.get("formId")[0];
        String requestVersion =  params.get("requestVersion")[0];
        
        FormEntity form = queryForm(Integer.parseInt(formId), Integer.parseInt(requestVersion));
        
        List<ComponentEntity> components = form.getComponents();
        
        Map<String,Object> mainData = new HashMap<>();
        
        int bid = (int) (Math.random() * 1000000);
        
        for(ComponentEntity comp : components) {
            
            ComponentService cmpService = componentService.get(comp.getType());
            
            if(cmpService instanceof MultiValueService) {
                
                ((MultiValueService)cmpService).insert(form, comp, params, bid);
                
                mainData.put(comp.getName(), bid);
            }
            else {
                
                mainData.put(comp.getName(), params.get(comp.getName())[0]);
            }
        }
        
        mybatisExecutor.insert("inseret_" + form.getName(), mainData);
    }
}
