package com.zhiren.common.ext.grid;

import java.io.Serializable;

import com.zhiren.common.ext.form.Field;

public class CheckColumn implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6884991291649908404L;

	private String align;
	private String dataIndex;
	private String datatype;
	private Field editor;
	private boolean fixed;
	private String header;
	private boolean hidden;
	private String id;
	private String listeners;
	private String renderer;
	private boolean resizable;
	private boolean sortable;
	private String width;
	
	public CheckColumn() {
		setWidth("'auto'");
		// TODO 自动生成构造函数存根
	}
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	
	public String getDataIndex() {
		return dataIndex;
	}
	public void setDataIndex(String dataIndex) {
		this.dataIndex = dataIndex;
	}
	
	public String getDataType() {
		return datatype;
	}
	public void setDataType(String datatype) {
		this.datatype = datatype;
	}
	
	public Field getEditor() {
		return editor;
	}
	public void setEditor(Field editor) {
		this.editor = editor;
	}
	
	public boolean isFixed() {
		return fixed;
	}
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}
	
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getListeners() {
		return listeners;
	}
	public void setListeners(String listeners) {
		this.listeners = listeners;
	}
	
	public String getRenderer() {
		return renderer;
	}
	public void setRenderer(String renderer) {
		this.renderer = renderer;
	}
	
	public boolean isResizable() {
		return resizable;
	}
	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}
	
	public boolean isSortable() {
		return sortable ;
	}
	public void setSortable(boolean sortable) {
		this.sortable = sortable; 
	}
	
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public void setWidth(int width) {
		this.width = String.valueOf(width);
	}

	public String getScript() {
		StringBuffer sb = new StringBuffer();
		sb.append("var ").append(getId()).append(" = new Ext.grid.CheckColumn({");
		sb.append("header:'").append(getHeader()).append("',");
		sb.append("dataIndex:'").append(getDataIndex()).append("',");
		sb.append(getWidth()==null?"":"width:"+width);
		sb.append("});\n");
		return sb.toString();
	}
}
