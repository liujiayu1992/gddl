package com.zhiren.jt.gongys;

import java.io.Serializable;

public class GongysChakbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5128127251695960784L;

	private long m_id;//

	private int m_xuh;

	private String m_piny;

	private String m_bianm;// 供应商编码

	private String m_quanc;// 供应商名称

	private String m_mingc;// 供应商简称

	private String m_shengfb_id;// 所属省

	private String m_leib;// 供应商类别

	private String m_leix;

	private String m_jihkjb_id;

	private String m_beiz;

	private String m_danwdz;

	public long getId() {
		return m_id;
	}

	public void setId(long id) {
		this.m_id = id;
	}

	public String getBianm() {
		return m_bianm;
	}

	public void setBianm(String bianm) {
		this.m_bianm = bianm;
	}

	public String getQuanc() {
		return m_quanc;
	}

	public void setQuanc(String quanc) {
		this.m_quanc = quanc;
	}

	public String getMingc() {
		return m_mingc;
	}

	public void setMingc(String mingc) {
		this.m_mingc = mingc;
	}

	public String getShengfb_id() {
		return m_shengfb_id;
	}

	public void setShengfb_id(String shengfb_id) {
		this.m_shengfb_id = shengfb_id;
	}

	public String getLeib() {
		return m_leib;
	}

	public void setLeib(String leib) {
		this.m_leib = leib;
	}

	public String getBeiz() {
		return m_beiz;
	}

	public void setBeiz(String beiz) {
		this.m_beiz = beiz;
	}

	public String getDanwdz() {
		return m_danwdz;
	}

	public void setDanwdz(String danwdz) {
		this.m_danwdz = danwdz;
	}

	public String getJihkjb_id() {
		return m_jihkjb_id;
	}

	public void setJihkjb_id(String jihkjb_id) {
		this.m_jihkjb_id = jihkjb_id;
	}

	public String getLeix() {
		return m_leix;
	}

	public void setLeix(String leix) {
		this.m_leix = leix;
	}

	public String getPiny() {
		return m_piny;
	}

	public void setPiny(String piny) {
		this.m_piny = piny;
	}

	public int getXuh() {
		return m_xuh;
	}

	public void setXuh(int xuh) {
		this.m_xuh = xuh;
	}

	public GongysChakbean() {
	}

	public GongysChakbean(long id, int xuh, String piny, String bianm,
			String quanc, String mingc, String shengfb_id, String leib,
			String leix, String jihkjb_id, String beiz, String danwdz) {

		this.m_id = id;
		this.m_xuh = xuh;
		this.m_piny = piny;
		this.m_bianm = bianm;
		this.m_quanc = quanc;
		this.m_mingc = mingc;
		this.m_shengfb_id = shengfb_id;
		this.m_leib = leib;
		this.m_leix = leix;
		this.m_jihkjb_id = jihkjb_id;
		this.m_beiz = beiz;
		this.m_danwdz = danwdz;
	}

	public String toString() {

		StringBuffer buffer = new StringBuffer("");

		buffer.append(m_id);
		buffer.append(',');
		buffer.append(m_xuh);
		buffer.append(',');
		buffer.append(m_piny);
		buffer.append(',');
		buffer.append(m_bianm);
		buffer.append(',');
		buffer.append(m_quanc);
		buffer.append(',');
		buffer.append(m_mingc);
		buffer.append(',');
		buffer.append(m_shengfb_id);
		buffer.append(',');
		buffer.append(m_leib);
		buffer.append(',');
		buffer.append(m_leix);
		buffer.append(',');
		buffer.append(m_jihkjb_id);
		buffer.append(',');
		buffer.append(m_beiz);
		buffer.append(',');
		buffer.append(m_danwdz);
		return buffer.toString();
	}

}
