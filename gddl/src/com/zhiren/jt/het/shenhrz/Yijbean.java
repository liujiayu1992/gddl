package com.zhiren.jt.het.shenhrz;

import java.io.Serializable;

public class Yijbean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3020866821060188952L;
	private String xitts;
	private String yij;
	public String getXitts() {
		return xitts;
	}
	public void setXitts(String xitts) {
		this.xitts = xitts;
	}
	public String getYij() {
		return yij;
	}
	public void setYij(String yij) {
		this.yij = yij;
	}
	public Yijbean(String xitts, String yij) {
		super();
		this.xitts = xitts;
		this.yij = yij;
	}
	public Yijbean() {
		super();
	}
	
}
