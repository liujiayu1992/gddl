package com.zhiren.common.ext.form;

import java.io.Serializable;

import com.zhiren.common.ext.Component;

public abstract class Field extends Component implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 92173881498404943L;
	public String id;
	public String fieldLabel;
	public String inputType;
	public boolean allowBlank;
	public long maxLength;
	public boolean selectOnFocus;
	public boolean readOnly;
	public String width;
	public String validator;
	public String value;
	public String maxValue;
	public String minValue;
	public String listeners;
	public String vtype;
	
	public String emptyText = "«Î—°‘Ò..."; 
	
	
	public Field() {
		allowBlank = false;
		selectOnFocus = true;
	}
	
//	field
	public String getId() {
		return id==null?"":id;
	}
	public void setId(String id) {
		this.id  = id;
	}
	
	public String getFieldLabel() {
		return fieldLabel==null?"":fieldLabel;
	}
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}
	
	public String getInputType() {
		return inputType==null?"":inputType;
	}
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
	
	public boolean isAllowBlank() {
		return allowBlank;
	}
	public void setAllowBlank(boolean allowBlank) {
		this.allowBlank = allowBlank;
	}
	
	public long getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(long maxLength) {
		this.maxLength = maxLength;
	}
	
	public boolean isSelectOnFocus() {
		return selectOnFocus;
	}
	public void setSelectOnFocus(boolean selectOnFocus) {
		this.selectOnFocus = selectOnFocus;
	}
	
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	public String getWidth() {
		return width;
	}
	public void setWidth(String width){
		this.width = width;
	}
	public void setWidth(int width) {
		this.width = String.valueOf(width);
	}
	
	public String getValidator() {
		return validator==null?"":validator;
	}
	public void setValidator(String validator) {
		this.validator = validator;
	}
	
	public String getValue() {
		return value==null?"":value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getMinValue() {
		return minValue==null?"":minValue;
	}
	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}
	
	public String getMaxValue() {
		return maxValue==null?"":maxValue;
	}
	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}
	
	public String getListeners() {
		return listeners==null?"":listeners;
	}
	public void setListeners(String listeners) {
		this.listeners = listeners;
	}
	
	public String getVtype() {
		return vtype==null?"":vtype;
	}
	/**
	 * @param vtype ”–alphanum °¢ alpha °¢ email °¢ url
	 */
	public void setVtype(String vtype) {
		this.vtype = vtype;
	}
	
	public String getEmptyText() {
		return emptyText==null?"":emptyText;
	}
	public void setEmptyText(String emptyText) {
		this.emptyText = emptyText;
	}
	
	public abstract String getScript() ;

}
