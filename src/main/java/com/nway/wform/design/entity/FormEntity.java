package com.nway.wform.design.entity;

import java.util.List;

public class FormEntity
{
    private String id;
    
    private String name;
    
    private String title;
    
    private String tableName;
    
    private int maxColumnNum;
    
    private List<ComponentGroupEntity> componentGroups;
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public String getTableName()
    {
        return tableName;
    }
    
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
    
    public List<ComponentGroupEntity> getComponents()
    {
        return componentGroups;
    }
    
    public void setComponents(List<ComponentGroupEntity> components)
    {
        this.componentGroups = components;
    }
    
}
