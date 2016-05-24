package com.nway.wform.service.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.nway.wform.entity.ComponentEntity;
import com.nway.wform.entity.FormEntity;
import com.nway.wform.entity.SQL;
import com.nway.wform.jdbc.MybatisExecutor;
import com.nway.wform.jdbc.MybatisUtils;

public class MultiValueCmpService extends AbstractCmpService
{
    private MybatisExecutor mybatisExecutor = MybatisUtils.getMybatisExecutor();
    
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
    
    public void insert(FormEntity form, ComponentEntity cmp, Map<String, String[]> params, int bid) {
        
        String statement = "insert_" + form.getName() + "_" + cmp.getName();
        
        List<Map<String,Object>> parameter = new ArrayList<>();
        
        for (String value : params.get(cmp.getName()))
        {
            Map<String,Object> param = new HashMap<>();
            
            param.put("bid", bid);
            param.put("id", (int) (Math.random() * 1000000));
            param.put("value", value);
            
            parameter.add(param);
        }
        
        mybatisExecutor.insert(statement, parameter);
    }
}
