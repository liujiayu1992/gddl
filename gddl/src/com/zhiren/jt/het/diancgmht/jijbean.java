package com.zhiren.jt.het.diancgmht;

import java.io.Serializable;

public class jijbean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int xuh;
	private long id;
	private String zhibb_id;
	private String tiaojb_id;
	private double shangx;
	private double xiax;
	private String danwb_id;
	private double jij;
	private String jijdwid;
	private String jijgs;	//基价公式
	private String hetjsfsb_id;
	private String hetjjfsb_id;
	private String hetjsxsb_id;
	private double yunj;
	private String yunjdw_id;
	private String yingdkf;
	private String yunsfsb_id;
	private double zuigmj;
	private String zuigmjdw;
	private double fengsjj;
	private String fengsjjdw;
	private String jijlx;	//基价类型，（含税、不含税）
	private String pinz;
	
	public String getPinz() {
		return pinz;
	}
	public void setPinz(String pinz) {
		this.pinz = pinz;
	}
	public String getJijlx() {
		return jijlx;
	}
	public void setJijlx(String jijlx) {
		this.jijlx = jijlx;
	}
	public String getHetjjfsb_id() {
		return hetjjfsb_id;
	}
	public void setHetjjfsb_id(String hetjjfsb_id) {
		this.hetjjfsb_id = hetjjfsb_id;
	}
	public int getXuh() {
		return xuh;
	}
	public void setXuh(int xuh) {
		this.xuh = xuh;
	}
	public String getDanwb_id() {
		return danwb_id;
	}
	public void setDanwb_id(String danwb_id) {
		this.danwb_id = danwb_id;
	}
	public String getHetjsfsb_id() {
		return hetjsfsb_id;
	}
	public void setHetjsfsb_id(String hetjsfsb_id) {
		this.hetjsfsb_id = hetjsfsb_id;
	}
	public String getHetjsxsb_id() {
		return hetjsxsb_id;
	}
	public void setHetjsxsb_id(String hetjsxsb_id) {
		this.hetjsxsb_id = hetjsxsb_id;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getJij() {
		return jij;
	}
	public void setJij(double jij) {
		this.jij = jij;
	}
	public String getJijdwid() {
		return jijdwid;
	}
	public void setJijdwid(String jijdwid) {
		this.jijdwid = jijdwid;
	}
	public String getJijgs() {
		return jijgs;
	}
	public void setJijgs(String jijgs) {
		this.jijgs = jijgs;
	}
	public double getShangx() {
		return shangx;
	}
	public void setShangx(double shangx) {
		this.shangx = shangx;
	}
	public String getTiaojb_id() {
		return tiaojb_id;
	}
	public void setTiaojb_id(String tiaojb_id) {
		this.tiaojb_id = tiaojb_id;
	}
	public double getXiax() {
		return xiax;
	}
	public void setXiax(double xiax) {
		this.xiax = xiax;
	}
	public String getYingdkf() {
		return yingdkf;
	}
	public void setYingdkf(String yingdkf) {
		this.yingdkf = yingdkf;
	}
	public double getYunj() {
		return yunj;
	}
	public void setYunj(double yunj) {
		this.yunj = yunj;
	}
	public String getYunjdw_id() {
		return yunjdw_id;
	}
	public void setYunjdw_id(String yunjdw_id) {
		this.yunjdw_id = yunjdw_id;
	}
	public String getYunsfsb_id() {
		return yunsfsb_id;
	}
	public void setYunsfsb_id(String yunsfsb_id) {
		this.yunsfsb_id = yunsfsb_id;
	}
	public String getZhibb_id() {
		return zhibb_id;
	}
	public void setZhibb_id(String zhibb_id) {
		this.zhibb_id = zhibb_id;
	}
	public double getZuigmj() {
		return zuigmj;
	}
	public void setZuigmj(double zuigmj) {
		this.zuigmj = zuigmj;
	}
	public String getZuigmjdw() {
		return zuigmjdw;
	}
	public void setZuigmjdw(String zuigmjdw) {
		this.zuigmjdw = zuigmjdw;
	}
	public jijbean(int xuh){
		this.xuh=xuh;
	}
	public double getFengsjj() {
		return fengsjj;
	}
	public void setFengsjj(double fengsjj) {
		this.fengsjj = fengsjj;
	}
	public String getFengsjjdw() {
		return fengsjjdw;
	}
	public void setFengsjjdw(String fengsjjdw) {
		this.fengsjjdw = fengsjjdw;
	}
	
	public jijbean(int xuh,long id, String zhibb_id, String tiaojb_id, double shangx, 
			double xiax, String danwb_id, double jij, String jijdwid, String jijgs, String hetjsfsb_id,
			String hetjjfsb_id, String hetjsxsb_id, double yunj, 
			String yunjdw_id, String yingdkf, String yunsfsb_id, 
			double zuigmj, String zuigmjdw, double fengsjj,String fengsjjdw, String jijlx,String pinz) {
		super();
		this.id = id;
		this.zhibb_id = zhibb_id;
		this.tiaojb_id = tiaojb_id;
		this.shangx = shangx;
		this.xiax = xiax;
		this.danwb_id = danwb_id;
		this.jij = jij;
		this.jijdwid = jijdwid;
		this.jijgs = jijgs;
		this.hetjsfsb_id = hetjsfsb_id;
		this.hetjsxsb_id = hetjsxsb_id;
		this.yunj = yunj;
		this.yunjdw_id = yunjdw_id;
		this.yingdkf = yingdkf;
		this.yunsfsb_id = yunsfsb_id;
		this.zuigmj = zuigmj;
		this.zuigmjdw = zuigmjdw;
		this.xuh=xuh;
		this.hetjjfsb_id=hetjjfsb_id;
		this.fengsjj=fengsjj;
		this.fengsjjdw=fengsjjdw;
		this.jijlx = jijlx;
		this.pinz=pinz;
	}

}
