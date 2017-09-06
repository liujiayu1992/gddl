package com.zhiren.zidy;

import java.io.Serializable;

public class ZidyCss implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5549370524567670121L;
	
	private String Id;
	private String Name;
	private String Text;
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getText() {
		return Text;
	}
	public void setText(String text) {
		Text = text;
	}
	
	public ZidyCss(String id, String name, String text) {
		setId(id);
		setName(name);
		setText(text);
	}
}
