/*
 * 创建日期 2005-5-7
 * 
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.zhiren.common;

import java.io.Serializable;

/**
 * @author Administrator
 * 
 * TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class IDropDownBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4850693996958599624L;

    private String Id;

    private String Value;

    public IDropDownBean() {
    }

    public IDropDownBean(long id, String name) {
        this.Id = String.valueOf(id);
        this.Value = name;
    }
    
    public IDropDownBean(String id, String name) {
        this.Id = id;
        this.Value = name;
    }

    public IDropDownBean(long id) {
        this.Id = String.valueOf(id);
    }

    public IDropDownBean(String name) {
        this.Value = name;
    }
    
    public String getStrId() {
    	return Id;
    }

    public long getId() {
        return Long.parseLong(Id);
    }

    public void setId(long id) {
        this.Id = String.valueOf(id);
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        this.Value = value;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer("");
        buffer.append(Id);
        return buffer.toString();
    }
}
