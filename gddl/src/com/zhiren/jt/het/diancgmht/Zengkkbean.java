package com.zhiren.jt.het.diancgmht;

import java.io.Serializable;
import java.math.BigDecimal;

public class Zengkkbean implements Serializable {
	/**
	 * 
	 */
	/*
	 * 作者：王磊 时间：2009-05-20 描述：改动扣价（KOUJ）字段为BigDecimal类型
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private int xuh;

	private String ZHIBB_ID;

	private String TIAOJB_ID;

	private double SHANGX;

	private double XIAX;

	private String DANWB_ID;

	private double JIS;

	private String JISDWID;

	private double KOUJ;

	private String KOUJDW;

	private double ZENGFJ;

	private String ZENGFJDW;

	private String XIAOSCL;

	private double JIZZKJ;

	private double JIZZB;

	private String CANZXM;

	private String CANZXMDW;

	private double CANZSX;

	private double CANZXX;

	private String JIESXXB_ID;// 结算形式表

	private String PINZB_ID; // 品种表id

	private String YUNSFSB_ID;

	private String BEIZ;

	private String shiyfw;

	public String getShiyfw() {
		return shiyfw;
	}

	public void setShiyfw(String shiyfw) {
		this.shiyfw = shiyfw;
	}

	public String getBEIZ() {
		return BEIZ;
	}

	public void setBEIZ(String beiz) {
		BEIZ = beiz;
	}

	public double getCANZSX() {
		return CANZSX;
	}

	public void setCANZSX(double canzsx) {
		CANZSX = canzsx;
	}

	public String getCANZXM() {
		return CANZXM;
	}

	public void setCANZXM(String canzxm) {
		CANZXM = canzxm;
	}

	public String getCANZXMDW() {
		return CANZXMDW;
	}

	public void setCANZXMDW(String canzxmdw) {
		CANZXMDW = canzxmdw;
	}

	public double getCANZXX() {
		return CANZXX;
	}

	public void setCANZXX(double canzxx) {
		CANZXX = canzxx;
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

	public String getJIESXXB_ID() {
		return JIESXXB_ID;
	}

	public void setJIESXXB_ID(String jiesxxb_id) {
		JIESXXB_ID = jiesxxb_id;
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

	public double getJIZZB() {
		return JIZZB;
	}

	public void setJIZZB(double jizzb) {
		JIZZB = jizzb;
	}

	public double getJIZZKJ() {
		return JIZZKJ;
	}

	public void setJIZZKJ(double jizzkj) {
		JIZZKJ = jizzkj;
	}

	public double getKOUJ() {
		// if(KOUJ==null){
		// KOUJ=new BigDecimal(0);
		// }
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

	public String getXIAOSCL() {
		return XIAOSCL;
	}

	public void setXIAOSCL(String xiaoscl) {
		XIAOSCL = xiaoscl;
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

	public String getYUNSFSB_ID() {
		return YUNSFSB_ID;
	}

	public void setYUNSFSB_ID(String yunsfsb_id) {
		YUNSFSB_ID = yunsfsb_id;
	}

	public double getZENGFJ() {
		// if(ZENGFJ==null){
		// ZENGFJ=new BigDecimal(0);
		// }
		return ZENGFJ;
	}

	public void setZENGFJ(double zengfj) {
		ZENGFJ = zengfj;
	}

	public String getZENGFJDW() {
		return ZENGFJDW;
	}

	public void setZENGFJDW(String zengfjdw) {
		ZENGFJDW = zengfjdw;
	}

	public String getZHIBB_ID() {
		return ZHIBB_ID;
	}

	public void setZHIBB_ID(String zhibb_id) {
		ZHIBB_ID = zhibb_id;
	}

	public Zengkkbean(int xuh) {
		this.xuh = xuh;
	}
	
	public String getPinzb_id() {
		return PINZB_ID;
	}

	public void setPinzb_id(String pinzb_id) {
		PINZB_ID = pinzb_id;
	}

	public Zengkkbean(long id, int xuh, String zhibb_id, String tiaojb_id,
			double shangx, double xiax, String danwb_id, double jis,
			String jisdwid, double kouj, String koujdw, double zengfj,
			String zengfjdw, String xiaoscl, double jizzkj, double jizzb,
			String canzxm, String canzxmdw, double canzsx, double canzxx,
			String jiesxxb_id, String yunsfsb_id, String beiz, String shiyfw, 
			String pinzb_id) {
		
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
		ZENGFJ = zengfj;
		ZENGFJDW = zengfjdw;
		XIAOSCL = xiaoscl;
		JIZZKJ = jizzkj;
		JIZZB = jizzb;
		CANZXM = canzxm;
		CANZXMDW = canzxmdw;
		CANZSX = canzsx;
		CANZXX = canzxx;
		JIESXXB_ID = jiesxxb_id;
		YUNSFSB_ID = yunsfsb_id;
		BEIZ = beiz;
		this.shiyfw = shiyfw;
		PINZB_ID = pinzb_id;
	}
}
