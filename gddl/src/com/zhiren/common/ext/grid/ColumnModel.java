package com.zhiren.common.ext.grid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ColumnModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6884991291649908404L;

	private String id;
	private List columns;
	
	public ColumnModel() {
		// TODO 自动生成构造函数存根
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void addColumn(Column c) {
		getColumns().add(c);
	}
	public Column getColumn(int index) {
		return (Column)getColumns().get(index);
	}
	public Column getColumn(String dataindex) {
		for(int i=0; i < getColumns().size() ; i++) {
			if(dataindex.toUpperCase().equals(getColumn(i).getDataIndex().toUpperCase())) {
				return getColumn(i);
			}
		}
		return null;
	}
	public List getColumns() {
		if(columns == null) {
			columns = new ArrayList();
		}
		return columns;
	}
	public void setColumns(List c) {
		columns = c;
	}
	
	public int Size() {
		return getColumns().size();
	}
	
	public Object get(int i) {
		return getColumns().get(i);
	}

	public String getScript() {
		if(getColumns().isEmpty()) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		if(getId()!=null) {
			sb.append("var ").append(getId()).append(" = ");
		}
		sb.append("new Ext.grid.ColumnModel([");
		for(int i = 0 ; i< getColumns().size() ;i++) {
			Column c = (Column)getColumns().get(i);
			sb.append("{");
			if(c.getAlign() != null) {
				sb.append("align:'").append(c.getAlign()).append("',");
			}
			if(c.getDataIndex() != null) {
				sb.append("dataIndex:'").append(c.getDataIndex()).append("',");
			}
			if(c.getEditor() != null) {
				sb.append("editor:").append(c.getEditor().getScript()).append(",");
			}
			if(c.isFixed()) {
				sb.append("fixed:true,");
			}
			if(c.getHeader() != null) {
				sb.append("header:'").append(c.getHeader()).append("',");
			}
			if(c.isHidden()) {
				sb.append("hidden:true");
			}
			if(c.getRenderer() != null) {
				sb.append("renderer:").append(c.getRenderer()).append(",");
			}
			if(!c.isResizable()) {
				sb.append("resizable:false,");
			}
			if(c.isSortable()) {
				sb.append("sortable:true,");
			}
			if(c.getWidth() > 0) {
				sb.append("width:").append(c.getWidth()).append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("},\n");
		}
		sb.deleteCharAt(sb.length()-2);
		sb.append("]);\n");
		return sb.toString();
	}
}
