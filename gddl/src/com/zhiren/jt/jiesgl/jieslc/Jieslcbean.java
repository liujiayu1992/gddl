package com.zhiren.jt.jiesgl.jieslc;

import java.io.Serializable;
import java.util.Date;

public class Jieslcbean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long m_id;

	private String m_jiesbh;

	private String m_fahdw;

	private String m_xianshr;

	private double m_jiessl;

	private Date m_jiesrq;

	private String m_zhuangt;

	private String m_leib;// 两票结算、煤款结算、运费结算

	private int m_shendzt;

	private long m_liucztb_id;

	private long m_liucb_id;
	
	private String m_zhongl;

	public long getId() {
		return m_id;
	}

	public void setid(long id) {
		this.m_id = id;
	}

	public String getJiesbh() {
		return m_jiesbh;
	}

	public void setJiesbh(String jiesbh) {
		this.m_jiesbh = jiesbh;
	}

	public String getFahdw() {
		return m_fahdw;
	}

	public void setFahdw(String fahdw) {
		this.m_fahdw = fahdw;
	}

	public String getXianshr() {
		return m_xianshr;
	}

	public void setXianshr(String xianshr) {
		this.m_xianshr = xianshr;
	}

	public double getJiessl() {
		return m_jiessl;
	}

	public void setJiessl(double jiessl) {
		this.m_jiessl = jiessl;
	}

	public Date getJiesrq() {
		return m_jiesrq;
	}

	public void setJiesrq(Date jiesrq) {
		this.m_jiesrq = jiesrq;
	}

	public String getZhuangt() {
		return m_zhuangt;
	}

	public void setZhuangt(String zhuangt) {
		this.m_zhuangt = zhuangt;
	}

	public String getLeib() {
		return m_leib;
	}

	public void setLeib(String leib) {
		this.m_leib = leib;
	}

	public int getShendzt() {
		return m_shendzt;
	}

	public void setShendzt(int shendzt) {
		this.m_shendzt = shendzt;
	}

	public long getLiucztb_id() {
		return m_liucztb_id;
	}

	public void setLiucztb_id(long liucztb_id) {
		this.m_liucztb_id = liucztb_id;
	}

	public long getLiucb_id() {
		return m_liucb_id;
	}

	public void setLiucb_id(long liucb_id) {
		this.m_liucb_id = liucb_id;
	}
	
	public String getZhongl() {
		return m_zhongl;
	}

	public void setZhongl(String zhongl) {
		this.m_zhongl = zhongl;
	}

	// 代审定状态的
	public Jieslcbean(long id, String jiesbh, String fahdw, String xianshr,
			double jiessl, Date jiesrq, String zhuangt, String leib,
			int shendzt, long liucztb_id, long liucb_id,String zhongl) {
		super();
		this.m_id = id;
		this.m_jiesbh = jiesbh;
		this.m_fahdw = fahdw;
		this.m_xianshr = xianshr;
		this.m_jiessl = jiessl;
		this.m_jiesrq = jiesrq;
		this.m_zhuangt = zhuangt;
		this.m_leib = leib;
		this.m_shendzt = shendzt;
		this.m_liucztb_id = liucztb_id;
		this.m_liucb_id = liucb_id;
		this.m_zhongl=zhongl;
	}
}
