package com.zhiren.common.ext.grid;

import java.io.Serializable;

public class RowSelectionModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6072072248574118969L;
	
	private String id;
	private String listeners;
	private boolean singleSelect;
	
	public RowSelectionModel() {
		
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
	
	public boolean isSingleSelect() {
		return singleSelect;
	}
	public void setSingleSelect(boolean singleSelect) {
		this.singleSelect = singleSelect;
	}
	
	public String getScript() {
		StringBuffer sb = new StringBuffer();
		if(getId() != null) {
			sb.append(getId()).append("=");
		}
		sb.append("new Ext.grid.RowSelectionModel({");
		if(getListeners() != null) {
			sb.append("listeners:{").append(getListeners()).append("},");
		}
		if(isSingleSelect()) {
			sb.append("singleSelect:true,");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("});\n");
		return sb.toString();
	}
}
