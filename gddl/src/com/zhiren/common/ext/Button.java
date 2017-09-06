package com.zhiren.common.ext;

import java.io.Serializable;

public abstract class Button implements Serializable {
	
	public String id;
	public String text;
	public String handler;
	public String icon;
	
	public Button() {
//		text = "°´Å¥";
	}
	public Button(String text,String hander) {
		this.text = text;
		this.handler = hander;
	}
	
	public Button(String text,String hander,String icon) {
		this.text = text;
		this.handler = hander;
		this.icon=icon;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setHandler(String hander) {
		this.handler = hander;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getScript() {
		StringBuffer buttonjs = new StringBuffer();
		buttonjs.append("new Ext.Toolbar.Button({\n");
		buttonjs.append("text:'").append(text).append("',");
		if(id !=null) {
			buttonjs.append("id:'").append(id).append("',");
		}
		if(icon != null) {
			buttonjs.append("icon:'").append(icon).append("',");
			buttonjs.append("cls:'x-btn-text-icon',");
		}
		if(handler == null || "".equals(handler)) {
			buttonjs.deleteCharAt(buttonjs.length()-1);
		}else {
			buttonjs.append("handler:").append(handler);
		}
		buttonjs.append("});\n");
		return buttonjs.toString();
	}

}
