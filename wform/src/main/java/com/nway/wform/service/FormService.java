package com.nway.wform.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.nway.wform.SpringContextUtil;
import com.nway.wform.entity.ComponentEntity;
import com.nway.wform.entity.FormEntity;
import com.nway.wform.service.component.ComponentService;
import com.nway.wform.service.component.MultiValueService;

@Service
public class FormService
{
    @Autowired
    private SqlSession sqlSession;
    
    public String buildSelectMapper(FormEntity form)
    {
        StringBuilder selectedColumnes = new StringBuilder("select ");
        StringBuilder selectedTables = new StringBuilder(" ");
        StringBuilder resultMap = new StringBuilder(" ");
        
        for (ComponentEntity component : form.getComponents())
        {
            selectedColumnes
                    .append(getComponentService(component.getType() + "Service").buildQuerySql(component).getColumnes())
                    .append(',');
            selectedTables
                    .append(getComponentService(component.getType() + "Service").buildQuerySql(component).getWithTable())
                    .append(' ');
            resultMap.append(getComponentService(component.getType() + "Service").buildResultMap(component));
        }
        
        selectedColumnes = selectedColumnes.length() > 7
                ? selectedColumnes.deleteCharAt(selectedColumnes.length() - 1) : selectedColumnes;
        
        return selectedColumnes.append(" from t_main ").append(selectedTables).toString()+"\n" +resultMap;
    }
    
    public FormEntity queryForm(int formId, int version)
    {
        Map<String,Object> param = new HashMap<>();
        
        param.put("formId", formId);
        param.put("version", version);
        
        return sqlSession.selectOne("selectForm", param);
    }
    
    public Map<String,Object> queryFormData(String formName, int id)
    {
        return sqlSession.selectOne("select_" + formName, id);
    }
    
    public void saveData(Map<String, String[]> params) {
        
        String formId = params.get("formId")[0];
        String requestVersion =  params.get("requestVersion")[0];
        
        FormEntity form = queryForm(Integer.parseInt(formId), Integer.parseInt(requestVersion));
        
        List<ComponentEntity> components = form.getComponents();
        
        Map<String,Object> mainData = new HashMap<>();
        
        int bid = (int) (Math.random() * 1000000);
        
        for(ComponentEntity comp : components) {
            
            ComponentService cmpService = getComponentService(comp.getType() + "Service");
            
            if(cmpService instanceof MultiValueService) {
                
                ((MultiValueService)cmpService).insert(form, comp, params, bid);
                
                mainData.put(comp.getName(), bid);
            }
            else {
                
                mainData.put(comp.getName(), params.get(comp.getName())[0]);
            }
        }
        
        sqlSession.insert("insert_" + form.getName(), mainData);
    }
    
    private ComponentService getComponentService(String componentServiceName) {
        
        return SpringContextUtil.getBean(componentServiceName, ComponentService.class);
    }
    
    private void addMapper(String mapperLocation) throws NestedIOException {
        
        Configuration configuration = sqlSession.getConfiguration();
        ClassPathResource resource = new ClassPathResource(mapperLocation);
        
        try
        {
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(resource.getInputStream(), configuration,
                    resource.toString(), configuration.getSqlFragments());
            xmlMapperBuilder.parse();
        }
        catch (Exception e)
        {
            throw new NestedIOException("Failed to parse mapping resource: '" + mapperLocation + "'", e);
        }
    }
}
