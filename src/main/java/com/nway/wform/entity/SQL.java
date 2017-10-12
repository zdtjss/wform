package com.nway.wform.entity;

public class SQL
{
    private String columnes;
    
    private String withTable = " ";
    
    private String condition = " ";
    
    public String getColumnes()
    {
        return columnes;
    }
    
    public void setColumnes(String columnes)
    {
        this.columnes = columnes;
    }
    
    public String getWithTable()
    {
        return withTable;
    }
    
    public void setWithTable(String withTable)
    {
        this.withTable = withTable;
    }
    
    public String getCondition()
    {
        return condition;
    }
    
    public void setCondition(String condition)
    {
        this.condition = condition;
    }
    
}
