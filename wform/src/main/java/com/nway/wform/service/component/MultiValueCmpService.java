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
        
        querySql.setColumnes(tableAlias + "." + fieldName + " " + fieldName + "_value");
        querySql.setWithTable(" inner join " + tableName + " " + tableAlias + " on " + tableAlias + ".b_id"
                + " in m." + cmp.getName());
        
        return querySql;
    }
    
    @Override
    public String buildResultMap(ComponentEntity cmp)
    {
        StringBuilder resultMap = new StringBuilder();
        
        resultMap.append("<collection property=\"")
            .append(cmp.getName())
            .append("\" javaType=\"java.util.ArrayList\" ofType=\"java.lang.Object\" columnPrefix=\"").append(cmp.getName())
            .append("\"><result column=\"").append("_value\" />")
            .append("</collection>");
        
        return resultMap.toString();
    }
    
    public String buildInsertSql(ComponentEntity cmp)
    {
        StringBuilder insertSql = new StringBuilder();
        
        insertSql.append("insert into ").append(getTableName(cmp)).append(" ( id, b_id, value) values (#{id}, #{bid}, #{value})");
        
        return insertSql.toString();
    }
    
    public String buildDeleteSql(ComponentEntity cmp)
    {
        StringBuilder insertSql = new StringBuilder();
        
        insertSql.append("delete from ").append(getTableName(cmp)).append(" where b_id = #{bid}");
        
        return insertSql.toString();
    }
}
