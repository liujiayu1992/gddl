package com.zhiren.common.ext;

import java.io.Serializable;

public class Window implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4167551671620686286L;
	
	private String id;
	private String render;
	private String layout = "fit";
	private int width;
	private int height;
	private String closeAction = "hide";
	private boolean closeable;
	private boolean modal;
	private String items;
	
	public Window(String id) {
		this.id = id;
		this.width = 300;
		this.height = 400;
		this.modal = true;
		this.closeable = true;
	}
	
	public Window(String id,int width,int height, boolean modal, boolean closeable,String items) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.modal = modal;
		this.closeable = closeable;
		this.items = items;
	}
	
	public String getCloseAction() {
		return closeAction;
	}

	public void setCloseAction(String closeAction) {
		this.closeAction = closeAction;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}

	public String getItems() {
		return items;
	}
	
	public void setItems(String items) {
		this.items = items;
	}

	public boolean isModal() {
		return modal;
	}

	public void setModal(boolean modal) {
		this.modal = modal;
	}

	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void addItem(String itemId) {
		if(this.items != null) 
			this.items += "," + itemId;
		else
			this.items = itemId;
	}
	
	public String getRender() {
		return render==null?"":render;
	}

	public void setRender(String render) {
		this.render = render;
	}
	
	public boolean isCloseable() {
		return closeable;
	}

	public void setCloseable(boolean closeable) {
		this.closeable = closeable;
	}
	
	public String getItemsScript() {
		return items==null?"":"items:["+items+"]";
	}
	
	public String getScript() {
		StringBuffer sb = new StringBuffer();
		if(getId()!=null && !"".equals(getId())) {
			sb.append("var ").append(id).append("_window =");
		}
		sb.append("new Ext.Window({\n");
		if(!"".equals(getRender())) {
			sb.append("\tel:'").append(getRender()).append("',\n");
		}
		if(getWidth()>0) {
			sb.append("\twidth:").append(getWidth()).append(",\n");
		}
		if(getHeight()>0) {
			sb.append("\theight:").append(getHeight()).append(",\n");
		}
		if(isCloseable()) {
			sb.append("\tcloseAction:'").append(getCloseAction()).append("',\n");
		}else {
			sb.append("\tclosable:false,\n");
		}
		if(isModal()) {
			sb.append("\tmodal:").append(isModal()).append(",\n");
		}
		if(!"".equals(getLayout())) {
			sb.append("\tlayout:'").append(getLayout()).append("',\n");
		}
		if(!"".equals(getItemsScript())) {
			sb.append(getItemsScript()).append(",\n");
		}
		sb.deleteCharAt(sb.length()-2);
		sb.append("});");
		return sb.toString();
	}

}
