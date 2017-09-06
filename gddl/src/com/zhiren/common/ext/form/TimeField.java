package com.zhiren.common.ext.form;

import java.io.Serializable;

public class TimeField extends Field implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3129097751074638141L;

	//private String format;
	
	public TimeField() {
		super.allowBlank=true;
		//format = "Y-m-d";
	}
	
	public TimeField(String id) {
		this.id = id;
		//format = "Y-m-d";
	}
	
//	public String getFormat() {
//		return format==null?"":format;
//	}
//	public void setFormat(String format) {
//		this.format = format;
//	}
	
//	public void  Binding(String ItemId,String formName) {
//		this.listeners = "change:function(own,newValue,oldValue) {document.getElementById('"+ItemId+"').value = newValue.dateFormat('Y-m-d');" +
//		(formName==null||"".equals(formName)?"":"document." +formName+".submit();")+"}";
//	}

	public String getScript() {
		StringBuffer sb = new StringBuffer();
		
		if(!"".equals(getId())) {
			sb.append(getId()).append("=");
		}

		sb.append("new Ext.form.TimeField({\n");
		if(isAllowBlank()) {
			sb.append("\tallowBlank:").append(isAllowBlank()).append(",\n");
		}
//		if(!"".equals(getFormat())) {
//			sb.append("\tformat:'").append(getFormat()).append("',\n");
//		}
		if(!"".equals(getValue())) {
			sb.append("\tvalue:'").append(getValue()).append("',\n");
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
