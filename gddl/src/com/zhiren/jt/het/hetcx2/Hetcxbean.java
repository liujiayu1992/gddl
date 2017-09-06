package com.zhiren.jt.het.hetcx2;

import java.io.Serializable;

public class Hetcxbean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String danw;
	private String jihkjmc;
	private String hetsm;
	private String hetl;
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getDanw() {
		return danw;
	}
	public void setDanw(String danw) {
		this.danw = danw;
	}
	public String getHetl() {
		return hetl;
	}
	public void setHetl(String hetl) {
		this.hetl = hetl;
	}
	public String getHetsm() {
		return hetsm;
	}
	public void setHetsm(String hetsm) {
		this.hetsm = hetsm;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getJihkjmc() {
		return jihkjmc;
	}
	public void setJihkjmc(String jihkjmc) {
		this.jihkjmc = jihkjmc;
	}
	public Hetcxbean(String id, String danw, String jihkjmc, String hetsm, String hetl) {
		super();
		this.id = id;
		this.danw = danw;
		this.jihkjmc = jihkjmc;
		this.hetsm = hetsm;
		this.hetl = hetl;
	}
}
