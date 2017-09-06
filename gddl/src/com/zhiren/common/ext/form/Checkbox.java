package com.zhiren.common.ext.form;

public class Checkbox extends Field {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4603790434772681420L;
	private String text;
	private String boxLabel;
	private boolean checked; 
	private String checkHandler;
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getCheckHandler() {
		return checkHandler==null?"":checkHandler;
	}

	public void setCheckHandler(String checkHandler) {
		this.checkHandler = checkHandler;
	}

	public String getText() {
		return text==null?"":text;
	}

	public void setText(String text) {
		this.text = text;
	} 
	
	public String getBoxLabel () {
		return boxLabel ==null?"":boxLabel ;
	}

	public void setBoxLabel (String boxLabel ) {
		this.boxLabel  = boxLabel ;
	} 
	
	public Checkbox() {
		super();
		setWidth("'auto'");
	}
	
	public String getScript() {
		// TODO 自动生成方法存根
		StringBuffer sb = new StringBuffer();
		if(!"".equals(getId())) {
			sb.append(getId()).append("=");
		}
		sb.append("new Ext.form.Checkbox({\n");
		if(!"".equals(getBoxLabel())) {
			sb.append("\tboxLabel:'").append(getBoxLabel()).append("',\n");
		}
		if(!"".equals(getText())) {
			sb.append("\ttext:'").append(getText()).append("',\n");
		}
		if(isChecked()) {
			sb.append("\tchecked:").append(isChecked()).append(",\n");
		}
		if(!"".equals(getCheckHandler())) {
			sb.append("\tcheckHandler:").append(getCheckHandler()).append(",\n");
		}
		if(!"".equals(getFieldLabel())) {
			sb.append("\tfieldLabel: '").append(getFieldLabel()).append("',\n");
		}
		if(!"".equals(getInputType())) {
			sb.append("\tinputType: '").append(getInputType()).append("',\n");
		}
		if(getWidth()!=null) {
			sb.append("\twidth:").append(getWidth()).append(",\n");
		}
//		if(isSelectOnFocus()) {
//			sb.append("\tselectOnFocus:").append(isSelectOnFocus()).append(",\n");
//		}
		if(!"".equals(getListeners())) {
			sb.append("\tlisteners:{").append(getListeners()).append("},\n");
		}
		sb.deleteCharAt(sb.length()-2);
		sb.append("})");
		return sb.toString();
	} 
}
