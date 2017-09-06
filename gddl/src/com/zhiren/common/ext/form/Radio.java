package com.zhiren.common.ext.form;

public class Radio extends Field {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4603790434772681420L;
	private String boxLabel;
	private String name;
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

	public String getName() {
		return name==null?"":name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBoxLabel () {
		return boxLabel ==null?"":boxLabel ;
	}

	public void setBoxLabel (String boxLabel ) {
		this.boxLabel  = boxLabel ;
	} 
	public Radio(String name) {
		super();
		this.name = name;
	}
	
	public String getScript() {
		// TODO 自动生成方法存根
		StringBuffer sb = new StringBuffer();
		if(!"".equals(getId())) {
			sb.append(getId()).append("=");
		}
		sb.append("new Ext.form.Radio({\n");
		if(!"".equals(getBoxLabel())) {
			sb.append("\tboxLabel:'").append(getBoxLabel()).append("',\n");
		}
		if(isChecked()) {
			sb.append("\tchecked:").append(isChecked()).append(",\n");
		}
		if(!"".equals(getName())) {
			sb.append("\tname:'").append(getName()).append("',\n");
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
		if(!"".equals(getListeners())) {
			sb.append("\tlisteners:{").append(getListeners()).append("},\n");
		}
		sb.deleteCharAt(sb.length()-2);
		sb.append("})");
		return sb.toString();
	}
}
