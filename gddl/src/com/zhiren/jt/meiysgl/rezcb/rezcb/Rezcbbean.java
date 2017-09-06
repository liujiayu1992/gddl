package com.zhiren.jt.meiysgl.rezcb.rezcb;

import java.io.Serializable;

public class Rezcbbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5311770101892291152L;

	private long id;

	private long dianchangid;

	private String dianchangmc;// 收货单位

	private String riq;
	
	private long rucsl;// 入厂数量

	private double rucrl;// 入厂热量

	private double rucsf;// 入厂水份

	private long rulsl;// 入炉数量

	private double rulrl;// 入炉热量

	private double rulsf;// 入炉水份

	private double rezctzq;// mj/kg

	private long rezctzqdk;// 大卡/公斤

	private double rezctzh;// mj/kg

	private long rezctzhdk;// 大卡/公斤

	private String beiz;// 备注

	public Rezcbbean() {

	}
	private boolean m_flag;
	 public boolean getFlag(){
	     return m_flag;
	    }
	    public void setFlag(boolean flag){
	     m_flag = flag;
	    }
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getBeiz() {
		return beiz;
	}

	public void setBeiz(String beiz) {
		this.beiz = beiz;
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
	
	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getRezctzh() {
		return rezctzh;
	}

	public void setRezctzh(double rezctzh) {
		this.rezctzh = rezctzh;
	}

	public long getRezctzhdk() {
		return rezctzhdk;
	}

	public void setRezctzhdk(long rezctzhdk) {
		this.rezctzhdk = rezctzhdk;
	}

	public double getRezctzq() {
		return rezctzq;
	}

	public void setRezctzq(double rezctzq) {
		this.rezctzq = rezctzq;
	}

	public long getRezctzqdk() {
		return rezctzqdk;
	}

	public void setRezctzqdk(long rezctzqdk) {
		this.rezctzqdk = rezctzqdk;
	}

	public double getRucrl() {
		return rucrl;
	}

	public void setRucrl(double rucrl) {
		this.rucrl = rucrl;
	}

	public double getRucsf() {
		return rucsf;
	}

	public void setRucsf(double rucsf) {
		this.rucsf = rucsf;
	}

	public long getRucsl() {
		return rucsl;
	}

	public void setRucsl(long rucsl) {
		this.rucsl = rucsl;
	}

	public double getRulrl() {
		return rulrl;
	}

	public void setRulrl(double rulrl) {
		this.rulrl = rulrl;
	}

	public double getRulsf() {
		return rulsf;
	}

	public void setRulsf(double rulsf) {
		this.rulsf = rulsf;
	}

	public long getRulsl() {
		return rulsl;
	}

	public void setRulsl(long rulsl) {
		this.rulsl = rulsl;
	}

	public Rezcbbean(long id, long dianchangid, String dianchangmc,String riq, long rucsl,
			double rucrl, double rucsf, long rulsl, double rulrl, double rulsf,
			double rezctzq, long rezctzqdk, double rezctzh, long rezctzhdk,
			String beiz) {
		super();
		this.id = id;
		this.dianchangid = dianchangid;
		this.dianchangmc = dianchangmc;
		this.riq = riq;
		this.rucsl = rucsl;
		this.rucrl = rucrl;
		this.rucsf = rucsf;
		this.rulsl = rulsl;
		this.rulrl = rulrl;
		this.rulsf = rulsf;
		this.rezctzq = rezctzq;
		this.rezctzqdk = rezctzqdk;
		this.rezctzh = rezctzh;
		this.rezctzhdk = rezctzhdk;
		this.beiz = beiz;
	}

}