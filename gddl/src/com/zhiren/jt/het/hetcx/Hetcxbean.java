package com.zhiren.jt.het.hetcx;

import java.io.Serializable;

public class Hetcxbean implements Serializable{

	/**
	 * /*
 * 作者：赵胜男
 * 时间：2012-07-24
 * 描述：，增加结算方式列，重写构造方法。
 * 		
 */
	 
	private static final long serialVersionUID = 1L;
	private String id;
	private String hetbh;
	private String xuf;
	private String gongf;
	private String hetl;
	private String qiandrq;
	private String qisrq;
	private String guoqrq;
	private String jihkj;
	private String weiz;
	private String leix;
	private String rel;
	private String meij;
	private String yunj;
	private String fuid;
	private String fbh;
	private String jiesfs;
	
	public String getFbh() {
		return fbh;
	}
	public void setFbh(String fbh) {
		this.fbh = fbh;
	}
	public String getFuid() {
		return fuid;
	}
	public void setFuid(String fuid) {
		this.fuid = fuid;
	}
//	/煤价
	public String getMeij() {
		return meij;
	}
	public void setMeij(String meij) {
		this.meij = meij;
	}
	///运价
	public String getYunj() {
		return yunj;
	}
	public void setYunj(String yunj) {
		this.yunj = yunj;
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
	//增加起始日期
	public String getQisrq() {
		return qisrq;
	}
	public void setQisrq(String qisrq) {
		this.qisrq = qisrq;
	}
    //增加截至日期
	public String getGuoqrq() {
		return guoqrq;
	}
	public void setGuoqrq(String guoqrq) {
		this.guoqrq = guoqrq;
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
	public String getJiesfs() {
		return jiesfs;
	}
	public void setJiesfs(String jiesfs) {
		this.jiesfs = jiesfs;
	}
	public Hetcxbean(String id, String xuf, String gongf, String hetbh, String hetl, 
			String qiandrq, String qisrq,String guoqrq,String jihkj, String weiz, String leix,String rel,
			String meij,String yunj,String fuid,String bh) {
		super();
		this.id = id;
		this.hetbh = hetbh;
		this.xuf = xuf;
		this.gongf = gongf;
		this.hetl = hetl;
		this.qiandrq = qiandrq;
		this.qisrq = qisrq;
		this.guoqrq = guoqrq;
		this.jihkj = jihkj;
		this.weiz = weiz;
		this.leix = leix;
		this.rel=rel;
		this.meij=meij;
		this.yunj=yunj;
		this.fuid=fuid;
		this.fbh=bh;
	}
	public Hetcxbean(String id, String xuf, String gongf, String hetbh, String hetl, 
			String qiandrq, String qisrq,String guoqrq,String jihkj, String weiz, String leix,String rel,
			String meij,String yunj,String fuid,String bh,String jiesfs) {
		super();
		this.id = id;
		this.hetbh = hetbh;
		this.xuf = xuf;
		this.gongf = gongf;
		this.hetl = hetl;
		this.qiandrq = qiandrq;
		this.qisrq = qisrq;
		this.guoqrq = guoqrq;
		this.jihkj = jihkj;
		this.weiz = weiz;
		this.leix = leix;
		this.rel=rel;
		this.meij=meij;
		this.yunj=yunj;
		this.fuid=fuid;
		this.fbh=bh;
		this.jiesfs=jiesfs;
	}

	
}
