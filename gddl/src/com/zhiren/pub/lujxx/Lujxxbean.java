package com.zhiren.pub.lujxx;

import java.io.Serializable;

/**
 * @author Íõ¸Õ
 * 
 */
public class Lujxxbean implements Serializable {
	/*
	 * ID number xuh ÐòºÅ number MINGC Ãû³Æ varchar2 bianm ±àÂë varchar2 piny Æ´Òô
	 * varchar2 BEIZ ±¸×¢ varchar2
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = -2341050756899302682L;

	private long m_id;

	private int m_xuh;

	private String m_mingc;

	private String m_bianm;

	private String m_piny;

	private String m_beiz;

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

	public String getBianm() {
		return m_bianm;
	}

	public void setBianm(String bianm) {
		this.m_bianm = bianm;
	}

	public String getPiny() {
		return m_piny;
	}

	public void setPiny(String piny) {
		this.m_piny = piny;
	}

	public String getBeiz() {
		return m_beiz;
	}

	public void setBeiz(String beiz) {
		this.m_beiz = beiz;
	}

	public Lujxxbean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Lujxxbean(long id, int xuh, String mingc, String bianm, String piny,
			String beiz) {
		super();
		this.m_id = id;
		this.m_xuh = xuh;
		this.m_mingc = mingc;
		this.m_bianm = bianm;
		this.m_piny = piny;
		this.m_beiz = beiz;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer("");

		buffer.append(m_id);
		buffer.append(',');
		buffer.append(m_xuh);
		buffer.append(',');
		buffer.append(m_mingc);
		buffer.append(',');
		buffer.append(m_bianm);
		buffer.append(',');
		buffer.append(m_piny);
		buffer.append(',');
		buffer.append(m_beiz);

		return super.toString();
	}

}
