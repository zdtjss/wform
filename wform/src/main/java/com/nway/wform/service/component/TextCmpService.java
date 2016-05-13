package com.nway.wform.service.component;

import com.nway.wform.entity.ComponentEntity;

public class TextCmpService extends AbstractCmpService
{
    /**
     * 返回本组件在这个表单中对应的sql查询语句,最终组合为
     * select * from 表单主数据表 m left join (组件sql1) cmp1.getName() on m.cmp1.getName() = cmp1.getName().cmp1.getName() left join (组件sql2) cmp2.getName() on m.cmp2.getName() = cmp2.getName().cmp2.getName()
     * 
     */
    public String buildQuerySql(ComponentEntity cmp)
    {
        return "";
    }
}
