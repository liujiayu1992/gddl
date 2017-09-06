package com.zhiren.common.ext;

import java.io.Serializable;

public class PagingToolbar extends Toolbar implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6652020760151558096L;

	private boolean displayInfo;
	private String displayMsg;
	private String emptyMsg;
	private String id;
	private int pageSize;
	private String store;
	
	public PagingToolbar(String id) {
		super(id);
		setDisplayInfo(true);
		setDisplayMsg("显示第 {0} 条到 {1} 条记录，一共 {2} 条");
		setEmptyMsg("没有记录");
	}

	public boolean isDisplayInfo() {
		return displayInfo;
	}
	public void setDisplayInfo(boolean displayInfo) {
		this.displayInfo = displayInfo;
	}

	public String getDisplayMsg() {
		return displayMsg;
	}
	public void setDisplayMsg(String displayMsg) {
		this.displayMsg = displayMsg;
	}

	public String getEmptyMsg() {
		return emptyMsg;
	}
	public void setEmptyMsg(String emptyMsg) {
		this.emptyMsg = emptyMsg;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}

	public String getScript() {
		// TODO 自动生成方法存根
		StringBuffer sb = new StringBuffer();
		if(getId() != null) {
			sb.append("var ").append(getId()).append(" =");
		}
		sb.append("new Ext.PagingToolbar({ \n");
		if(isDisplayInfo()) {
			sb.append("displayInfo: true,\n");
		}
		if(getDisplayMsg() != null) {
			sb.append("displayMsg: ").append(getDisplayMsg()).append(",\n");
		}
		if(getEmptyMsg() != null) {
			sb.append("emptyMsg: ").append(getEmptyMsg()).append(",\n");
		}
		if(getPageSize()==0) {
			sb.append("pageSize: ").append(getPageSize()).append(",\n");
		}
		if(getStore() != null) {
			sb.append("store: ").append(getStore()).append(",\n");
		}
		sb.deleteCharAt(sb.length() - 2);
		sb.append("});\n");
		return sb.toString();
	}
}
