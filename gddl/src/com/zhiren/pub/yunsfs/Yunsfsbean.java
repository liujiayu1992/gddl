package com.zhiren.pub.yunsfs;

import java.io.Serializable;

/**
 * @author 王刚
 *
 */
public class Yunsfsbean implements Serializable {

	/*
	 * ID number MINGC 名称（铁路、公路、水运） varchar2 piny 拼音 varchar2 BEIZ 备注 varchar2
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = -585080986293830421L;

	private long m_id;

	private String m_mingc;// 名称(铁路、公路、水运)

	private String m_piny;// 拼音

	private String m_beiz;// 备注

	public long getId() {
		return m_id;
	}

	public void setId(long id) {
		this.m_id = id;
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

	public String getBeiz() {
		return m_beiz;
	}

	public void setBeiz(String beiz) {
		this.m_beiz = beiz;
	}

	public Yunsfsbean() {

	}

	public Yunsfsbean(long id, String mingc, String piny, String beiz) {

		this.m_id = id;

		this.m_mingc = mingc;

		this.m_piny = piny;

		this.m_beiz = beiz;

	}

	public String toString() {

		StringBuffer buffer = new StringBuffer("");

		buffer.append(m_id);
		buffer.append(',');
		buffer.append(m_mingc);
		buffer.append(',');
		buffer.append(m_piny);
		buffer.append(',');
		buffer.append(m_beiz);

		return buffer.toString();
	}
}
