package com.zhiren.common.ext.form;

import java.io.Serializable;

public class MonthField extends Field implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3129097751074638141L;

	private String format;

	public MonthField() {
		format = "Y-m";
//		setSelectOnFocus(false);
	}

	public MonthField(String id) {
		this.id = id;
		format = "Y-m";
	}
	
	public String getFormat() {
		return format==null?"":format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	
	public void  Binding(String ItemId,String formName) {
		this.listeners = "change:function(own,newValue,oldValue) {document.getElementById('"+ItemId+"').value = newValue.dateFormat('Y-m');" +
		(formName==null||"".equals(formName)?"":"document." +formName+".submit();")+"}";
	}

	public String getScript() {
		StringBuffer sb = new StringBuffer();
		
		if(!"".equals(getId())) {
			sb.append(getId()).append("=");
		}

		sb.append("new Ext.ux.MonthField({\n");
		if(isAllowBlank()) {
			sb.append("\tallowBlank:").append(isAllowBlank()).append(",\n");
		}
		if(isSelectOnFocus()) {
			sb.append("\tallowBlank:").append(isSelectOnFocus()).append(",\n");
		}
		if(!"".equals(getFormat())) {
			sb.append("\tformat:'").append(getFormat()).append("',\n");
		}
		if(!"".equals(getValue())) {
			sb.append("\tvalue:Date.parseDate('").append(getValue()).append("','Y-m'),\n");
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
