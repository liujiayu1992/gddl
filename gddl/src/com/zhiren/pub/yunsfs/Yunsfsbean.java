package com.zhiren.pub.yunsfs;

import java.io.Serializable;

/**
 * @author ����
 *
 */
public class Yunsfsbean implements Serializable {

	/*
	 * ID number MINGC ���ƣ���·����·��ˮ�ˣ� varchar2 piny ƴ�� varchar2 BEIZ ��ע varchar2
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = -585080986293830421L;

	private long m_id;

	private String m_mingc;// ����(��·����·��ˮ��)

	private String m_piny;// ƴ��

	private String m_beiz;// ��ע

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
