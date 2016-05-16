package com.nway.wform.service.component;

import com.nway.wform.entity.ComponentEntity;
import com.nway.wform.entity.SQL;

public class MultiValueCmpService extends AbstractCmpService
{
    @Override
    public SQL buildQuerySql(ComponentEntity cmp)
    {
        String fieldName = cmp.getName();
        String tableName = getTableName(cmp);
        
        SQL querySql = new SQL();
        
        String tableAlias = "ta_" + tableName;
        
        querySql.setColumnes(tableAlias + "." + fieldName + " " + tableName + "_value");
        querySql.setWithTable(" left join " + tableName + " "+tableAlias + " on m." + cmp.getName()
                + " = " + tableAlias + "." + cmp.getName());
        
        return querySql;
    }
    
    @Override
    public String buildResultMap(ComponentEntity cmp)
    {
        StringBuilder resultMap = new StringBuilder();
        
        resultMap.append("<collection property=\"")
            .append(cmp.getName())
            .append("\" javaType=\"java.util.ArrayList\" ofType=\"java.lang.Object\" columnPrefix=\"").append(getTableName(cmp))
            .append("\"><result column=\"").append("_value\" />")
            .append("</collection>");
        
        return resultMap.toString();
    }
}
