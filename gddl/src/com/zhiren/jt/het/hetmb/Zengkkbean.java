package com.zhiren.jt.het.hetmb;

import java.io.Serializable;
import java.math.BigDecimal;

public class Zengkkbean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private int xuh;

	private String ZHIBB_ID;

	private String Zhibb_bm;

	private String TIAOJB_ID;

	private double SHANGX;

	private double XIAX;

	private String DANWB_ID;

	private double JIS;

	private String JISDWID;

	private double KOUJ;
	
	private String KOUJGS;	//扣价公式

	private String KOUJDW;

	private double ZENGFJ;
	
	private String ZENGFJGS;	//增付价公式

	private String ZENGFJDW;

	private String XIAOSCL;

	private double JIZZKJ;

	private double JIZZB;

	private String CANZXM;

	private String CANZXMDW;

	private double CANZSX;

	private double CANZXX;

	private String JIESXXB_ID;// 结算形式表

	private String YUNSFSB_ID;

	private String PINZB_ID; // 品种
	
	private String SHIYFW;	//适用范围

	private String BEIZ;
	
	private String JIJLX; //基价类型（0为含税、1为不含税）

	public String getJIJLX() {
		return JIJLX;
	}

	public void setJIJLX(String jijlx) {
		JIJLX = jijlx;
	}

	public String getZhibb_bm() {
		return Zhibb_bm;
	}

	public void setZhibb_bm(String zhibb_bm) {
		Zhibb_bm = zhibb_bm;
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
		if (CANZXM == null) {
			CANZXM = "";
		}
		return CANZXM;
	}

	public void setCANZXM(String canzxm) {
		CANZXM = canzxm;
	}

	public String getCANZXMDW() {
		if (CANZXMDW == null) {
			CANZXMDW = "";
		}
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
		if (DANWB_ID == null) {
			DANWB_ID = "";
		}
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
		if (JIESXXB_ID == null) {
			JIESXXB_ID = "____";
		}
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
		if (JISDWID == null) {
			JISDWID = "____";
		}
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
		if (KOUJDW == null) {
			KOUJDW = "";
		}
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
		if (TIAOJB_ID == null) {
			TIAOJB_ID = "";
		}
		return TIAOJB_ID;
	}

	public void setTIAOJB_ID(String tiaojb_id) {
		TIAOJB_ID = tiaojb_id;
	}

	public String getXIAOSCL() {
		if (XIAOSCL == null) {
			XIAOSCL = "";
		}
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
		if (YUNSFSB_ID == null) {
			YUNSFSB_ID = "____";
		}
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
		if (ZENGFJDW == null) {
			ZENGFJDW = "";
		}
		return ZENGFJDW;
	}

	public void setZENGFJDW(String zengfjdw) {
		ZENGFJDW = zengfjdw;
	}

	public String getZHIBB_ID() {
		if (ZHIBB_ID == null) {
			ZHIBB_ID = "";
		}
		return ZHIBB_ID;
	}

	public void setZHIBB_ID(String zhibb_id) {
		ZHIBB_ID = zhibb_id;
	}

	public Zengkkbean(int xuh, String danw) {
		this.xuh = xuh;
		this.KOUJDW = danw;
		this.ZENGFJDW = danw;

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
	
	public String getShiyfw() {
		return SHIYFW;
	}

	public void setShiyfw(String shiyfw) {
		SHIYFW = shiyfw;
	}
	
	public String getKoujgs() {
		return KOUJGS;
	}

	public void setKoujgs(String koujgs) {
		KOUJGS = koujgs;
	}

	public String getZengfjgs() {
		return ZENGFJGS;
	}

	public void setZengfjgs(String zengfjgs) {
		ZENGFJGS = zengfjgs;
	}

	public Zengkkbean() {

	}

	public Zengkkbean(long id, int xuh, String zhibb_id, String tiaojb_id,
			double shangx, double xiax, String danwb_id, double jis,
			String jisdwid, double kouj, String koujgs, String koujdw, 
			double zengfj, String zengfjgs,	String zengfjdw, String xiaoscl, 
			double jizzkj, double jizzb, String canzxm, String canzxmdw, 
			double canzsx, double canzxx, String jiesxxb_id, String yunsfsb_id, 
			String pinzb_id, String shiyfw,	String beiz, String jijlx) {
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
		KOUJGS = koujgs;
		KOUJDW = koujdw;
		ZENGFJ = zengfj;
		ZENGFJGS = zengfjgs;
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
		PINZB_ID = pinzb_id;
		this.SHIYFW = shiyfw;
		BEIZ = beiz;
		JIJLX = jijlx;
	}
}
