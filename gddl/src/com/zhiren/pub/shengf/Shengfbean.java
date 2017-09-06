package com.zhiren.pub.shengf;

import java.io.Serializable;

public class Shengfbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long m_id;//

	private int m_xuh;//ÐòºÅ

	private String m_mingc;//Ãû³Æ

	private String m_quanc;//È«³Æ

	private String m_piny;//Æ´Òô

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

	public Shengfbean() {
	}

	public Shengfbean(long id, int xuh, String mingc, String quanc, String piny) {

		this.m_id = id;
		this.m_xuh = xuh;
		this.m_mingc = mingc;
		this.m_quanc = quanc;
		this.m_piny = piny;
	}

	public String toString() {

		StringBuffer buffer = new StringBuffer("");

		buffer.append(m_id);
		buffer.append(',');
		buffer.append(m_xuh);
		buffer.append(',');
		buffer.append(m_mingc);
		buffer.append(',');
		buffer.append(m_quanc);
		buffer.append(',');
		buffer.append(m_piny);

		return buffer.toString();
	}

}
