package com.zhiren.jt.diaoygl.shouhcrb.shouhcrb;

import java.io.Serializable;

public class Shouhcrbbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8655311071276367018L;

	private long id;//

	private long xuh;// 序号

	private String riq;// 日期

	private long dianchangid;

	private String dianchangmc;// 收货单位

	private long gongm;// 供煤

	private long haoy;// 耗用

	private long kuc;// 库存
	
	private long shangykc;// 昨日库存
	
	private long leijgm;//累计供煤
	
	private long leijhm;//累计耗煤
	private long tiaozl;//调整量
	
	public Shouhcrbbean(){
		
	}
	
	public Shouhcrbbean(long id, long xuh, String riq, long dianchangid, String dianchangmc, long gongm, long haoy, long kuc,long shangykc,long leijgm,long leijhm,long tiaozl) {
		super();
		this.id = id;
		this.xuh = xuh;
		this.riq = riq;
		this.dianchangid = dianchangid;
		this.dianchangmc = dianchangmc;
		this.gongm = gongm;
		this.haoy = haoy;
		this.kuc = kuc;
		this.shangykc = shangykc;
		this.leijgm = leijgm;
		this.leijhm = leijhm;
		this.tiaozl=tiaozl;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public long getDianchangid() {
		return dianchangid;
	}

	public void setDianchangid(long dianchangid) {
		this.dianchangid = dianchangid;
	}

	public String getDianchangmc() {
		return dianchangmc;
	}

	public void setDianchangmc(String dianchangmc) {
		this.dianchangmc = dianchangmc;
	}

	public long getGongm() {
		return gongm;
	}

	public void setGongm(long gongm) {
		this.gongm = gongm;
	}

	public long getHaoy() {
		return haoy;
	}

	public void setHaoy(long haoy) {
		this.haoy = haoy;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getKuc() {
		return kuc;
	}

	public void setKuc(long kuc) {
		this.kuc = kuc;
	}
	
	public long getShangykc() {
		return shangykc;
	}

	public void setShangykc(long shangykc) {
		this.shangykc = shangykc;
	}
	
	public long getLeijgm() {
		return leijgm;
	}

	public void setLeijgm(long leijgm) {
		this.leijgm = leijgm;
	}
	
	public long getLeijhm() {
		return leijhm;
	}

	public void setLeijhm(long leijhm) {
		this.leijhm = leijhm;
	}

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}

	public long getXuh() {
		return xuh;
	}

	public void setXuh(long xuh) {
		this.xuh = xuh;
	}

	public long getTiaozl() {
		return tiaozl;
	}

	public void setTiaozl(long tiaozl) {
		this.tiaozl = tiaozl;
	}
	


	

}