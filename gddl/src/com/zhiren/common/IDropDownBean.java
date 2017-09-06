/*
 * �������� 2005-5-7
 * 
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.zhiren.common;

import java.io.Serializable;

/**
 * @author Administrator
 * 
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת�� ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
