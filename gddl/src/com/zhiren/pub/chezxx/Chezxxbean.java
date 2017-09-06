package com.zhiren.pub.chezxx;

import java.io.Serializable;

/**
 * @author ����
 * 
 */

public class Chezxxbean implements Serializable {

	/*
	 * ID number XUH ��� number bianm ���� varchar2 mingc ��� varchar2 QUANC ȫ��
	 * varchar2 piny ƴ�� varchar2 LUJXXB_ID ·��id number LEIB ��𣨳�վ���ۿڣ� varchar2
	 * BEIZ ��ע varchar2
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = -7111257907086541998L;

	private long m_id;//

	private int m_xuh;// ���

	private String m_bianm;// ��վ����

	private String m_mingc;// ���

	private String m_quanc;// ȫ��

	private String m_piny;// ƴ��

	private String m_leib;// ���(��վ���ۿ�)

	private String m_lujxxb_id;// ·��

	private String m_beiz;// ��ע

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
