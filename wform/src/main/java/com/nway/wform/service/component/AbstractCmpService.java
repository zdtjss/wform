package com.nway.wform.service.component;

import com.nway.wform.entity.ComponentEntity;
import com.nway.wform.entity.SQL;

public abstract class AbstractCmpService
{
    public SQL buildQuerySql(ComponentEntity cmp)
    {
        
        String fieldName = cmp.getName();
        //String tableName = getTableName(cmp);
        
        SQL querySql = new SQL();
        
        querySql.setColumnes(fieldName);
        //querySql.setWithTable(tableName);
        
        return querySql;
    }
    
    public String buildResultMap(ComponentEntity cmp)
    {
        StringBuilder resultMap = new StringBuilder();
        
        resultMap.append("<result property=\"")
                .append(cmp.getName())
                .append("\" column=\"")
                .append(cmp.getName())
                .append("\" />");
        
        return resultMap.toString();
    }
    
    protected String getTableName(ComponentEntity cmp)
    {
        return cmp.getTableName() == null ? "t_"+cmp.getFormId() + "_" + cmp.getId() : cmp.getTableName();
    }
}
