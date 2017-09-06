package com.zhiren.jt.het.yunsht.yunshtcx;

import java.io.Serializable;

public class Yunshtcxbean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String hetbh;
	private String xuf;
	private String gongf;
	private String hetl;
	private String qiandrq;
	private String jihkj;
	private String weiz;
	private String leix;
	private String rel;
	private String jiag;
	
	public String getJiag() {
		return jiag;
	}
	public void setJiag(String jiag) {
		this.jiag = jiag;
	}
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	public String getGongf() {
		return gongf;
	}
	public void setGongf(String gongf) {
		this.gongf = gongf;
	}
	public String getHetbh() {
		return hetbh;
	}
	public void setHetbh(String hetbh) {
		this.hetbh = hetbh;
	}
	public String getHetl() {
		return hetl;
	}
	public void setHetl(String hetl) {
		this.hetl = hetl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getJihkj() {
		return jihkj;
	}
	public void setJihkj(String jihkj) {
		this.jihkj = jihkj;
	}
	public String getLeix() {
		return leix;
	}
	public void setLeix(String leix) {
		this.leix = leix;
	}
	public String getQiandrq() {
		return qiandrq;
	}
	public void setQiandrq(String qiandrq) {
		this.qiandrq = qiandrq;
	}
	public String getWeiz() {
		return weiz;
	}
	public void setWeiz(String weiz) {
		this.weiz = weiz;
	}
	public String getXuf() {
		return xuf;
	}
	public void setXuf(String xuf) {
		this.xuf = xuf;
	}
	public Yunshtcxbean(String id, String hetbh, String xuf, String gongf, String qiandrq, String weiz,String jiag) {
		super();
		this.id = id;
		this.hetbh = hetbh;
		this.xuf = xuf;
		this.gongf = gongf;
		this.qiandrq = qiandrq;
		this.weiz = weiz;
		this.jiag=jiag;
	}
	
}
