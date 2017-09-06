package com.zhiren.jt.het.shihsht;

import java.io.Serializable;

public class Zhilyqbean  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int xuh;
	private long id;
	private String mingc;
	private String tiaoj;
	private double shangx;
	private double xiax;
	private String danw;
	
	public int getXuh() {
		return xuh;
	}
	public void setXuh(int xuh) {
		this.xuh = xuh;
	}
	public String getDanw() {
		return danw;
	}
	public void setDanw(String danw) {
		this.danw = danw;
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
	public double getShangx() {
		return shangx;
	}
	public void setShangx(double shangx) {
		this.shangx = shangx;
	}
	public String getTiaoj() {
		return tiaoj;
	}
	public void setTiaoj(String tiaoj) {
		this.tiaoj = tiaoj;
	}
	public double getXiax() {
		return xiax;
	}
	public void setXiax(double xiax) {
		this.xiax = xiax;
	}
	public Zhilyqbean(int xuh){
		this.xuh=xuh;
	}
	
	public Zhilyqbean(int xuh,long id, String mingc, String tiaoj, double shangx, double xiax, String danw) {
		super();
		this.id = id;
		this.mingc = mingc;
		this.tiaoj = tiaoj;
		this.shangx = shangx;
		this.xiax = xiax;
		this.danw = danw;
		this.xuh=xuh;
	}
}
