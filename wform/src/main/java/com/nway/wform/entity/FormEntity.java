package com.nway.wform.entity;

import java.util.List;

public class FormEntity
{
    private int id;
    
    private String name;
    
    private String title;
    
    private String tableName;
    
    private List<ComponentEntity> components;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
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
    
    public List<ComponentEntity> getComponents()
    {
        return components;
    }
    
    public void setComponents(List<ComponentEntity> components)
    {
        this.components = components;
    }
    
}
