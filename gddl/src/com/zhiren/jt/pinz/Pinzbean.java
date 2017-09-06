package com.zhiren.jt.pinz;

import java.io.Serializable;

/**
 * @author Õı∏’
 * 
 */
public class Pinzbean implements Serializable {

	/*
	 * ID number xuh number bianm varchar2 MINGC varchar2 piny ∆¥“Ù varchar2
	 * zhuangt ◊¥Ã¨£®1 π”√£¨0Õ£”√£© number PINZMS √Ë ˆ varchar2
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1515153130668343233L;

	private long m_id;

	private int m_xuh;

	private String m_bianm;

	private String m_mingc;

	private String m_piny;

	private String m_zhuangt;

	private String m_pinzms;

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

	public String getPiny() {
		return m_piny;
	}

	public void setPiny(String piny) {
		this.m_piny = piny;
	}

	public String getZhuangt() {
		return m_zhuangt;
	}

	public void setZhuangt(String zhuangt) {
		this.m_zhuangt = zhuangt;
	}

	public String getPinzms() {
		return m_pinzms;
	}

	public void setPinzms(String pinzms) {
		this.m_pinzms = pinzms;
	}

	public Pinzbean() {

	}

	public Pinzbean(long id, int xuh, String bianm, String mingc, String piny,
			String zhuangt, String pinzms) {

		this.m_id = id;
		this.m_xuh = xuh;
		this.m_bianm = bianm;
		this.m_mingc = mingc;
		this.m_piny = piny;
		this.m_zhuangt = zhuangt;
		this.m_pinzms = pinzms;
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
		buffer.append(',');
		buffer.append(m_piny);
		buffer.append(',');
		buffer.append(m_zhuangt);
		buffer.append(',');
		buffer.append(m_pinzms);

		return buffer.toString();
	}

}
