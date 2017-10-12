package com.nway.wform.design.entity;

public class ComponentEntity
{
    private String id;
    
    /**
     * 中文解释
     */
    private String display;
    
    /**
     * 用做页面元素 id 或 name
     */
    private String name;
    
    private String formId;
    
    /**
     * 组件类别
     */
    private String type;
    
    /** 行号  **/
    private int rowNum;
    
    /** 列  **/
    private int colNum;
    
    /** 跨列数  **/
    private int colSpan;
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getDisplay()
    {
        return display;
    }
    
    public void setDisplay(String display)
    {
        this.display = display;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getFormId()
    {
        return formId;
    }
    
    public void setFormId(String formId)
    {
        this.formId = formId;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public int getRowNum()
    {
        return rowNum;
    }
    
    public void setRowNum(int rowNum)
    {
        this.rowNum = rowNum;
    }

    public int getColNum()
    {
        return colNum;
    }

    public void setColNum(int colNum)
    {
        this.colNum = colNum;
    }

    public int getColSpan()
    {
        return colSpan;
    }

    public void setColSpan(int colSpan)
    {
        this.colSpan = colSpan;
    }
    
}
