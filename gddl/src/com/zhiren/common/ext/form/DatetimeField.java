package com.zhiren.common.ext.form;

import java.io.Serializable;

public class DatetimeField extends Field implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7770417893645941356L;

	private String format;
	
	public DatetimeField() {
		format = "Y-m-d H:i:s";
//		setSelectOnFocus(false);
	}
	
	public DatetimeField(String id) {
		this.id = id;
		format = "Y-m-d H:i:s";
	}
	
	private String menu;
	
	public String getMenu() {
		return menu==null?"":menu;
	}
	public void setMenu(String menu) {
		this.menu = menu;
	}
	
	public String getFormat() {
		return format==null?"":format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	
	public void  Binding(String ItemId,String formName) {
		this.listeners = "change:function(own,newValue,oldValue) {document.getElementById('"+ItemId+"').value = newValue.dateFormat('Y-m-d H:i:s');" +
		(formName==null||"".equals(formName)?"":"document." +formName+".submit();")+"}";
	}

	public String getScript() {
		StringBuffer sb = new StringBuffer();
		
		if(!"".equals(getId())) {
			sb.append(getId()).append("=");
		}

		sb.append("new Ext.form.DateTimeField({\n");
		if(isAllowBlank()) {
			sb.append("\tallowBlank:").append(isAllowBlank()).append(",\n");
		}
		if(isSelectOnFocus()) {
			sb.append("\tallowBlank:").append(isSelectOnFocus()).append(",\n");
		}
		if(!"".equals(getFormat())) {
			sb.append("\tformat:'").append(getFormat()).append("',\n");
		}
		if(!"".equals(getMenu())) {
			sb.append("\tmenu:").append(getMenu()).append(",\n");
		}
		if(!"".equals(getValue())) {
			sb.append("\tvalue:Date.parseDate('").append(getValue()).append("','Y-m-d H:i:s'),\n");
		}
		if(!"".equals(getMinValue())) {
			sb.append("\tminValue:'").append(getMinValue()).append("',\n");
		}
		if(!"".equals(getMaxValue())) {
			sb.append("\tmaxValue:'").append(getMaxValue()).append("',\n");
		}
		if(!"".equals(getListeners())) {
			sb.append("\tlisteners:{").append(getListeners()).append("},\n");
		}
		
		sb.deleteCharAt(sb.length()-2);
		sb.append("})");
		return sb.toString();
	}

}
