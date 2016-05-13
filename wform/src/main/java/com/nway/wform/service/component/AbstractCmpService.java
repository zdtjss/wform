package com.nway.wform.service.component;

import com.nway.wform.entity.ComponentEntity;

public abstract class AbstractCmpService
{
    abstract String buildQuerySql(ComponentEntity cmp);
}
