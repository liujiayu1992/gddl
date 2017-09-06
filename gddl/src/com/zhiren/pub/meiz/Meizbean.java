package com.zhiren.pub.meiz;

import java.io.Serializable;

public class Meizbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long m_id;//
	private int m_xuh;//ÐòºÅ
	private String m_bianm;//±àÂë
	private String m_mingc;//Ãû³Æ

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

	public Meizbean() {
	}

	public Meizbean(long id, int xuh, String bianm, String mingc) {

		this.m_id = id;
		this.m_xuh = xuh;
		this.m_bianm = bianm;
		this.m_mingc = mingc;
	}

	public String toString() {

		StringBuffer buffer = new StringBuffer("");

		buffer.append(m_id);
		buffer.append(',');
		buffer.append(m_xuh);
		buffer.append(',');
		buffer.append(m_bianm);
		buffer.append(',');
		buffer.append(m_mingc);

		return buffer.toString();
	}

}
