package com.zhiren.shihs.het;

import java.io.Serializable;

public class Zengkkbean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private int xuh;
	private String ZHIBB_ID;
	private String TIAOJB_ID;
	private double SHANGX;
	private double XIAX;
	private String DANWB_ID;
	private double JIS	;
	private String JISDWID;
	private double KOUJ;
	private String KOUJDW;
	private double jicj;
	private String BEIZ;
	public String getBEIZ() {
		return BEIZ;
	}
	public void setBEIZ(String beiz) {
		BEIZ = beiz;
	}
	public String getDANWB_ID() {
		return DANWB_ID;
	}
	public void setDANWB_ID(String danwb_id) {
		DANWB_ID = danwb_id;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getJIS() {
		return JIS;
	}
	public void setJIS(double jis) {
		JIS = jis;
	}
	public String getJISDWID() {
		return JISDWID;
	}
	public void setJISDWID(String jisdwid) {
		JISDWID = jisdwid;
	}
	public double getKOUJ() {
		return KOUJ;
	}
	public void setKOUJ(double kouj) {
		KOUJ = kouj;
	}
	public String getKOUJDW() {
		return KOUJDW;
	}
	public void setKOUJDW(String koujdw) {
		KOUJDW = koujdw;
	}
	public double getSHANGX() {
		return SHANGX;
	}
	public void setSHANGX(double shangx) {
		SHANGX = shangx;
	}
	public String getTIAOJB_ID() {
		return TIAOJB_ID;
	}
	public void setTIAOJB_ID(String tiaojb_id) {
		TIAOJB_ID = tiaojb_id;
	}
	public double getXIAX() {
		return XIAX;
	}
	public void setXIAX(double xiax) {
		XIAX = xiax;
	}
	public int getXuh() {
		return xuh;
	}
	public void setXuh(int xuh) {
		this.xuh = xuh;
	}
	public String getZHIBB_ID() {
		return ZHIBB_ID;
	}
	public void setZHIBB_ID(String zhibb_id) {
		ZHIBB_ID = zhibb_id;
	}
	public double getJICJ() {
		return jicj;
	}
	public void setJICJ(double jicj) {
		this.jicj = jicj;
	}
	public Zengkkbean(int xuh){
		this.xuh=xuh;
	}
	public Zengkkbean(long id, int xuh, String zhibb_id, String tiaojb_id, double shangx, double xiax, String danwb_id, double jis, String jisdwid, double kouj, String koujdw,double jicj,String beiz) {
		super();
		this.id = id;
		this.xuh = xuh;
		ZHIBB_ID = zhibb_id;
		TIAOJB_ID = tiaojb_id;
		SHANGX = shangx;
		XIAX = xiax;
		DANWB_ID = danwb_id;
		JIS = jis;
		JISDWID = jisdwid;
		KOUJ = kouj;
		KOUJDW = koujdw;
		jicj = jicj;
		BEIZ = beiz;
	}
}
