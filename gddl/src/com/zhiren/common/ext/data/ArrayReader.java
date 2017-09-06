package com.zhiren.common.ext.data;

import java.io.Serializable;

import com.zhiren.common.ext.grid.Column;
import com.zhiren.common.ext.grid.ColumnModel;

public class ArrayReader implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1132187575695319861L;
	
	private String id;
	
	private ColumnModel cm;
	
	public ArrayReader() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public ColumnModel getCM() {
		return cm;
	}
	public void setCM(ColumnModel cm) {
		this.cm = cm;
	}
	
	public String getScript() {
		if(getCM() == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		if(getId() != null) {
			sb.append("var ").append(getId()).append(" = ");
		}
		sb.append("new Ext.data.ArrayReader({},[");
		for(int i=0; i<getCM().Size();i++) {
			Column c = (Column)getCM().get(i);
			sb.append("{name:'").append(c.getDataIndex()).append("'},");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("])");
		return sb.toString();
	}
	
}
