package com.zhiren.dtrlgs.faygl.fayjs;

import com.zhiren.common.ext.form.Field;

public class NumberField extends Field {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5588581981489576452L;
	
	private long decimalPrecision;
	public NumberField() {
		this.decimalPrecision = 2;
	}
	
	public long getDecimalPrecision() {
		return this.decimalPrecision;
	}
	public void setDecimalPrecision(long decimalPrecision) {
		this.decimalPrecision = decimalPrecision;
	}
	public String getScript() {
		StringBuffer sb = new StringBuffer();

		sb.append("new Ext.form.NumberField({\n");
		if(!"".equals(getId())) {
			sb.append("\tid:'").append(getId()).append("',\n");
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
		if(!"".equals(getValidator())) {
			sb.append("\tvalidator:").append(getValidator()).append(",\n");
		}
		if(getDecimalPrecision()>=0) {
			sb.append("\tdecimalPrecision:").append(getDecimalPrecision()).append(",\n");
		}
		if(getWidth()!=null) {
			sb.append("\twidth:").append(getWidth()).append(",\n");
		}
		if(!"".equals(getMinValue())) {
			sb.append("\tminValue:").append(getMinValue()).append(",\n");
		}
		if(!"".equals(getMaxValue())) {
			sb.append("\tmaxValue:").append(getMaxValue()).append(",\n");
		}
		if(isSelectOnFocus()) {
			sb.append("\tselectOnFocus:").append(isSelectOnFocus()).append(",\n");
		}
		if(!"".equals(getListeners())) {
			sb.append("\tlisteners:{").append(getListeners()).append("},\n");
		}
		if(!"".equals(getValue())) {
			sb.append("\tvalue: ").append(getValue()).append(",\n");
		}
		
		sb.deleteCharAt(sb.length()-2);
		sb.append("})");
//		sb.append(id==null?"":id + " = ");
//		sb.append(fieldLabel==null?"":"\tfieldLabel: '"+fieldLabel+"',\n");
//		sb.append(inputType==null?"":"\tinputType: '"+inputType+"',\n");
//		sb.append(validator==null?"":"\tvalidator:"+validator+",\n");
//		sb.append("\tdecimalPrecision:").append(decimalPrecision).append(",\n");
//		sb.append(width == 0?"":"\twidth:"+width+",\n");
//		sb.append(minValue==null?"":"\tminValue: "+minValue+",\n");
//		sb.append(maxValue==null?"":"\tmaxValue: "+maxValue+",\n");
//		sb.append("\tselectOnFocus:").append(selectOnFocus).append(",\n");
//		sb.append(this.listeners==null?"":"\tlisteners:{"+listeners+"},\n");
//		sb.append("\tvalue: ").append(value).append(",\n");
//		sb.append("\tallowBlank:").append(allowBlank).append("})\n");
		return sb.toString();
	}
}
