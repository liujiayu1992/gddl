package com.zhiren.pub.wangjxx;

import java.io.Serializable;

/**
 * @author ����
 * 
 */

public class Wangjxxbean implements Serializable {

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

	private String m_wangjdm;// ���ִ���

	private String m_wangjmc;// ��������

	

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

	public String getWangjdm() {
		return m_wangjdm;
	}

	public void setWangjdm(String wangjdm) {
		this.m_wangjdm = wangjdm;
	}

	public String getWangjmc() {
		return m_wangjmc;
	}

	public void setWangjmc(String wangjmc) {
		this.m_wangjmc = wangjmc;
	}

	
	private boolean m_sel;

	public boolean getSel() {
		return m_sel;
	}

	public void setSel(boolean sel) {
		this.m_sel = sel;
	}

	public Wangjxxbean() {
		super();
	}

	public Wangjxxbean(boolean sel, long id, int xuh, String wangjdm,
			String wangjmc) {
		super();
		this.m_sel = sel;
		this.m_id = id;
		this.m_xuh = xuh;
		this.m_wangjdm = wangjdm;
		this.m_wangjmc = wangjmc;
		
	}

	public String toString() {

		StringBuffer buffer = new StringBuffer("");

		buffer.append(m_sel);
		buffer.append(',');
		buffer.append(m_id);
		buffer.append(',');
		buffer.append(m_xuh);
		buffer.append(',');
		buffer.append(m_wangjdm);
		buffer.append(',');
		buffer.append(m_wangjdm);
		buffer.append(',');
		

		return buffer.toString();
	}
}
