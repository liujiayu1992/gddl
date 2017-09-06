package com.zhiren.common.ext;

public class ToolbarText extends ToolbarItem {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8111382114059713114L;

	private String id;
	private String text;
	
	public ToolbarText(String text) {
		this.text = text;
	}
	public String getId() {
		return id;
	}
	public String getScript() {
		// TODO 自动生成方法存根
		return "'"+text+"'";
	}

}
