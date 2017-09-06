package com.zhiren.common.ext.data;

import java.io.Serializable;

public class Store implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6780581962693409337L;

	private String id;
	private String data;
	private String proxy;
	private boolean pruneMR;
	private String reader;

	public Store() {
		// TODO 自动生成构造函数存根
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	public String getProxy() {
		return proxy;
	}
	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
	
	public boolean isPruneMR() {
		return pruneMR;
	}
	public void setPruneMR(boolean pruneMR) {
		this.pruneMR = pruneMR;
	}
	
	public String getReader() {
		return reader;
	}
	public void setReader(String reader) {
		this.reader = reader;
	}

	public String getScript() {
		StringBuffer sb = new StringBuffer();
		if(getId() != null) {
			sb.append("var ").append(getId()).append(" = ");
		}
		sb.append("new Ext.data.Store({\n");
		if(getProxy() != null && getData() != null) {
			sb.append("proxy:new Ext.zr.data.PagingMemoryProxy(").append(getData()).append("),\n");
		}
		if(isPruneMR()) {
			sb.append("pruneModifiedRecords:true,\n");
		}
		if(getReader() != null) {
			sb.append("reader:").append(getReader()).append(",\n");
		}
		sb.deleteCharAt(sb.length()-2);
		sb.append("});\n");
		return sb.toString();
	}
}
