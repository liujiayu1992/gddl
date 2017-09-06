package com.zhiren.pub.chengs;

import java.io.Serializable;

/**
 * 2010-5-21
 * @author Àîè¡»ù
 *
 */
public class Chengsbean implements Serializable {

	private static final long serialVersionUID = 1L;

	private long m_id;

	private int m_shengfb_id;//Ê¡·Ý±íID

	private int m_xuh;//ÐòºÅ

	private String m_bianm;//±àÂë

	private String m_mingc;//¼ò³Æ

	private String m_quanc;//È«³Æ

	private String m_piny;//Æ´Òô
	
	private String m_beiz;//±¸×¢

	public long getId() {
		return this.m_id;
	}

	public void setId(long id) {
		this.m_id = id;
	}

	public int getShengfb_id() {
		return this.m_shengfb_id;
	}

	public void setShengfb_id(int shengfb_id) {
		this.m_shengfb_id = shengfb_id;
	}

	public int getXuh() {
		return this.m_xuh;
	}

	public void setXuh(int xuh) {
		this.m_xuh = xuh;
	}

	public String getBianm() {
		return this.m_bianm;
	}

	public void setBianm(String bianm) {
		this.m_bianm = bianm;
	}

	public String getMingc() {
		return this.m_mingc;
	}

	public void setMingc(String mingc) {
		this.m_mingc = mingc;
	}

	public String getQuanc() {
		return this.m_quanc;
	}

	public void setQuanc(String quanc) {
		this.m_quanc = quanc;
	}

	public String getPiny() {
		return this.m_piny;
	}

	public void setPiny(String piny) {
		this.m_piny = piny;
	}

	public String getBeiz(){
		return this.m_beiz;
	}
	public void setBeiz(String beiz){
		this.m_beiz=beiz;
	}
	public Chengsbean() {
	}

	public Chengsbean(long id, int shengfb_id, int xuh, String mingc,
			String quanc, String bianm, String piny,String beiz) {
		this.m_bianm = bianm;
		this.m_id = id;
		this.m_mingc = mingc;
		this.m_piny = piny;
		this.m_quanc = quanc;
		this.m_shengfb_id = shengfb_id;
		this.m_xuh = xuh;
		this.m_beiz=beiz;
	}

	public String toString() {

		StringBuffer buffer = new StringBuffer("");

		buffer.append(m_id);
		buffer.append(',');
		buffer.append(m_shengfb_id);
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
		buffer.append(m_beiz);

		return buffer.toString();
	}
	/*
	 * private long m_id;//
	 * 
	 * private int m_xuh;//ÐòºÅ
	 * 
	 * private String m_mingc;//Ãû³Æ
	 * 
	 * private String m_quanc;//È«³Æ
	 * 
	 * private String m_piny;//Æ´Òô
	 * 
	 * public long getId() { return m_id; }
	 * 
	 * public void setId(long id) { this.m_id = id; }
	 * 
	 * public int getXuh() { return m_xuh; }
	 * 
	 * public void setXuh(int xuh) { this.m_xuh = xuh; }
	 * 
	 * public String getMingc() { return m_mingc; }
	 * 
	 * public void setMingc(String mingc) { this.m_mingc = mingc; }
	 * 
	 * public String getQuanc() { return m_quanc; }
	 * 
	 * public void setQuanc(String quanc) { this.m_quanc = quanc; }
	 * 
	 * public String getPiny() { return m_piny; }
	 * 
	 * public void setPiny(String piny) { this.m_piny = piny; }
	 * 
	 * public Chengsbean() { }
	 * 
	 * public Chengsbean(long id, int xuh, String mingc, String quanc, String
	 * piny) {
	 * 
	 * this.m_id = id; this.m_xuh = xuh; this.m_mingc = mingc; this.m_quanc =
	 * quanc; this.m_piny = piny; }
	 * 
	 * public String toString() {
	 * 
	 * StringBuffer buffer = new StringBuffer("");
	 * 
	 * buffer.append(m_id); buffer.append(','); buffer.append(m_xuh);
	 * buffer.append(','); buffer.append(m_mingc); buffer.append(',');
	 * buffer.append(m_quanc); buffer.append(','); buffer.append(m_piny);
	 * 
	 * return buffer.toString(); }
	 */

}
