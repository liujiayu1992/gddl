package com.zhiren.common.ext.form;

public class TextField extends Field {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5716404083326427344L;

	public TextField() {
		setWidth("'auto'");
	}
	
	public String getScript() {
		StringBuffer sb = new StringBuffer();
		if(!"".equals(getId())) {
			sb.append(getId()).append("=");
		}
		sb.append("new Ext.form.TextField({\n");
		if(!"".equals(getVtype())) {
			sb.append("\tvtype:'").append(getVtype()).append("',\n");
		}
		if(isAllowBlank()) {
			sb.append("\tallowBlank:").append(isAllowBlank()).append(",\n");
		}
		if(isReadOnly()) {
			sb.append("\treadOnly:").append(isReadOnly()).append(",\n");
		}
		if(!"".equals(getFieldLabel())) {
			sb.append("\tfieldLabel: '").append(getFieldLabel()).append("',\n");
		}
		if(!"".equals(getInputType())) {
			sb.append("\tinputType: '").append(getInputType()).append("',\n");
		}
		if(getMaxLength()>0) {
			sb.append("\tmaxLength:").append(getMaxLength()).append(",\n");
		}
		if(!"".equals(getValidator())) {
			sb.append("\tvalidator:").append(getValidator()).append(",\n");
		}
		if(getWidth()!=null) {
			sb.append("\twidth:").append(getWidth()).append(",\n");
		}
		if(isSelectOnFocus()) {
			sb.append("\tselectOnFocus:").append(isSelectOnFocus()).append(",\n");
		}
		if(!"".equals(getListeners())) {
			sb.append("\tlisteners:{").append(getListeners()).append("},\n");
		}
		if(!"".equals(getValue())) {
			sb.append("\tvalue: '").append(getValue()).append("',\n");
		}
		
		sb.deleteCharAt(sb.length()-2);
		sb.append("})");
//		sb.append(vtype==null?"":"\tvtype:'"+vtype+"',\n");
//		sb.append(fieldLabel==null?"":"\tfieldLabel: '"+fieldLabel+"',\n");
//		sb.append(inputType==null?"":"\tinputType: '"+inputType+"',\n");
//		sb.append(maxLength==0?"":"\tmaxLength:"+maxLength+",\n");
//		sb.append(validator==null?"":"\tvalidator:"+validator+",\n");
//		sb.append(width == 0?"":"\twidth:"+width+",\n");
//		sb.append("\tselectOnFocus:").append(selectOnFocus).append(",\n");
//		sb.append(this.listeners==null?"":"\tlisteners:{"+listeners+"},\n");
//		sb.append(value==null?"":"\tvalue: '"+value+"',\n");
//		sb.append("\tallowBlank:").append(allowBlank).append("})\n");
		
		return sb.toString();
	}
}
