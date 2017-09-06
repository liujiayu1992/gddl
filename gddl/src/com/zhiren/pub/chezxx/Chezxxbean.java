package com.zhiren.pub.chezxx;

import java.io.Serializable;

/**
 * @author 王刚
 * 
 */

public class Chezxxbean implements Serializable {

	/*
	 * ID number XUH 序号 number bianm 编码 varchar2 mingc 简称 varchar2 QUANC 全称
	 * varchar2 piny 拼音 varchar2 LUJXXB_ID 路局id number LEIB 类别（车站、港口） varchar2
	 * BEIZ 备注 varchar2
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = -7111257907086541998L;

	private long m_id;//

	private int m_xuh;// 序号

	private String m_bianm;// 车站编码

	private String m_mingc;// 简称

	private String m_quanc;// 全称

	private String m_piny;// 拼音

	private String m_leib;// 类别(车站、港口)

	private String m_lujxxb_id;// 路局

	private String m_beiz;// 备注

	public long getId() {
		return m_id;
	}

	public void setId(long id) {
		this.m_id = id;
	}

	public int getXuh() {
		return m_xuh;
	}

	public void setXuh(int xuh) {
		this.m_xuh = xuh;
	}

	public String getBianm() {
		return m_bianm;
	}

	public void setBianm(String bianm) {
		this.m_bianm = bianm;
	}

	public String getMingc() {
		return m_mingc;
	}

	public void setMingc(String mingc) {
		this.m_mingc = mingc;
	}

	public String getQuanc() {
		return m_quanc;
	}

	public void setQuanc(String quanc) {
		this.m_quanc = quanc;
	}

	public String getPiny() {
		return m_piny;
	}

	public void setPiny(String piny) {
		this.m_piny = piny;
	}

	public String getLeib() {
		return m_leib;
	}

	public void setLeib(String leib) {
		this.m_leib = leib;
	}

	public String getLujxxb_id() {
		return m_lujxxb_id;
	}

	public void setLujxxb_id(String lujxxb_id) {
		this.m_lujxxb_id = lujxxb_id;
	}

	public String getBeiz() {
		return m_beiz;
	}

	public void setBeiz(String beiz) {
		this.m_beiz = beiz;
	}

	private boolean m_sel;

	public boolean getSel() {
		return m_sel;
	}

	public void setSel(boolean sel) {
		this.m_sel = sel;
	}

	public Chezxxbean() {
		super();
	}

	public Chezxxbean(boolean sel, long id, int xuh, String bianm,
			String mingc, String quanc, String piny, String lujxxb_id,
			String leib, String beiz) {
		super();
		this.m_sel = sel;
		this.m_id = id;
		this.m_xuh = xuh;
		this.m_bianm = bianm;
		this.m_mingc = mingc;
		this.m_quanc = quanc;
		this.m_piny = piny;
		this.m_lujxxb_id = lujxxb_id;
		this.m_leib = leib;
		this.m_beiz = beiz;
	}

	public String toString() {

		StringBuffer buffer = new StringBuffer("");

		buffer.append(m_sel);
		buffer.append(',');
		buffer.append(m_id);
		buffer.append(',');
		buffer.append(m_xuh);
		buffer.append(',');
		buffer.append(m_bianm);
		buffer.append(',');
		buffer.append(m_mingc);
		buffer.append(',');
		buffer.append(m_quanc);
		buffer.append(',');
		buffer.append(m_piny);
		buffer.append(',');
		buffer.append(m_lujxxb_id);
		buffer.append(',');
		buffer.append(m_leib);
		buffer.append(',');
		buffer.append(m_beiz);

		return buffer.toString();
	}
}
