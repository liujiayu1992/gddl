package com.zhiren.jt.het.gongzlgl;

import java.io.Serializable;

public class Liucjsbbean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String mingc;
	private String beiz;
	private int xuh;
	
	public int getXuh() {
		return xuh;
	}
	public void setXuh(int xuh) {
		this.xuh = xuh;
	}
	public String getBeiz() {
		return beiz;
	}
	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMingc() {
		return mingc;
	}
	public void setMingc(String mingc) {
		this.mingc = mingc;
	}
	public Liucjsbbean(long id, String mingc, String beiz,int xuh) {
		super();
		this.id = id;
		this.mingc = mingc;
		this.beiz = beiz;
		this.xuh=xuh;
	}
	public Liucjsbbean(int xuh) {
		super();
		this.xuh=xuh;
	}
	
}
