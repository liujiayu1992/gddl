package com.zhiren.jt.het.gongzlgl;

import java.io.Serializable;

public class Liucdzbbean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long liucdzb_id;
	private long liucdzjsb_id;
	private String jues;
	private String qianqzt;
	private String houjzt;
	int xuh;
	private String caozmc;
	private String dongz;
	
	public String getCaozmc() {
		return caozmc;
	}
	public void setCaozmc(String caozmc) {
		this.caozmc = caozmc;
	}
	
	public String getDongz() {
		return dongz;
	}
	public void setDongz(String dongz) {
		this.dongz = dongz;
	}
	
	public int getXuh() {
		return xuh;
	}
	public void setXuh(int xuh) {
		this.xuh = xuh;
	}
	public Liucdzbbean(int xuh,long liucdzb_id, long liucdzjsb_id, String jues, String qianqzt, String houjzt,String caozmc,String dongz) {
		super();
		this.liucdzb_id = liucdzb_id;
		this.liucdzjsb_id = liucdzjsb_id;
		this.jues = jues;
		this.qianqzt = qianqzt;
		this.houjzt = houjzt;
		this.xuh=xuh;
		this.caozmc=caozmc;
		this.dongz=dongz;
	}
	public Liucdzbbean(int xuh,long liucdzb_id, long liucdzjsb_id) {
		super();
		this.liucdzb_id = liucdzb_id;
		this.liucdzjsb_id = liucdzjsb_id;
		this.xuh=xuh;
	}
	public String getHoujzt() {
		return houjzt;
	}
	public void setHoujzt(String houjzt) {
		this.houjzt = houjzt;
	}
	public String getJues() {
		return jues;
	}
	public void setJues(String jues) {
		this.jues = jues;
	}
	public long getLiucdzb_id() {
		return liucdzb_id;
	}
	public void setLiucdzb_id(long liucdzb_id) {
		this.liucdzb_id = liucdzb_id;
	}
	public long getLiucdzjsb_id() {
		return liucdzjsb_id;
	}
	public void setLiucdzjsb_id(long liucdzjsb_id) {
		this.liucdzjsb_id = liucdzjsb_id;
	}
	public String getQianqzt() {
		return qianqzt;
	}
	public void setQianqzt(String qianqzt) {
		this.qianqzt = qianqzt;
	}
	
}
