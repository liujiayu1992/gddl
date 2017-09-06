package com.zhiren.common.ext;

import java.io.Serializable;

public class ToolbarButton extends ToolbarItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 115403334557041065L;

	private String id;
	private String cls;
	private String icon;
	private String handler;
	private int minWidth;
	private boolean disabled;
//	private String icon;
	private String text;
	
	public ToolbarButton(String id,String text,String handler) {
		this.id = id;
		this.text = text;
		this.handler = handler;
		this.minWidth = 75;
	}

	public String getCls() {
		return cls==null?"":cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}

	public String getHandler() {
		return handler==null?"":handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getIcon() {
		return icon==null?"":icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
		setCls("x-btn-text-icon");
	}

	public String getId() {
		return id==null?"":id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getMinWidth() {
		return minWidth;
	}

	public void setMinWidth(int minWidth) {
		this.minWidth = minWidth;
	}

	public String getText() {
		return text==null?"":text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public boolean getDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	public String getScript() {
		StringBuffer sb = new StringBuffer();
//		if(id!= null && !"".equals(id)) {
//			sb.append("var ").append(id).append("_btn = ");
//		}
		sb.append(" new Ext.Toolbar.Button({\n");
		if(!"".equals(getId())) {
			sb.append("\tid:'").append(getId()).append("',\n");
		}
		if(getDisabled()) {
			sb.append("\tdisabled:").append(getDisabled()).append(",\n");
		}
		if(!"".equals(getText())) {
			sb.append("\ttext:'").append(getText()).append("',\n");
		}
		if(!"".equals(getCls())) {
			sb.append("\tcls:'").append(getCls()).append("',\n");
		}
		if(!"".equals(getIcon())) {
			sb.append("\ticon:'").append(getIcon()).append("',\n");
		}
		if(!"".equals(getHandler())) {
			sb.append("\thandler:").append(getHandler()).append(",\n");
		}
		if(getMinWidth()>0) {
			sb.append("\tminWidth:").append(getMinWidth()).append(",\n");
		}
		sb.deleteCharAt(sb.length()-2);
		sb.append("})\n");
		return sb.toString();
	}

}
