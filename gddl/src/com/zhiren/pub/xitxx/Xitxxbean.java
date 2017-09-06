package com.zhiren.pub.xitxx;

import java.io.Serializable;

public class Xitxxbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4501683968594365628L;

	private long m_id;//

	private int m_xuh;// 序号

	private String m_diancxxb_id;//

	private String m_mingc;// 名称

	private String m_zhi;// 值

	private String m_danw;// 单位

	private String m_leib;// 类别

	private String m_zhuangt;// 状态

	private String m_beiz;// 备注

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

	public String getDiancxxb_id() {
		return m_diancxxb_id;
	}

	public void setDiancxxb_id(String diancxxb_id) {
		this.m_diancxxb_id = diancxxb_id;
	}

	public String getMingc() {
		return m_mingc;
	}

	public void setMingc(String mingc) {
		this.m_mingc = mingc;
	}

	public String getZhi() {
		return m_zhi;
	}

	public void setZhi(String zhi) {
		this.m_zhi = zhi;
	}

	public String getDanw() {
		return m_danw;
	}

	public void setDanw(String danw) {
		this.m_danw = danw;
	}

	public String getLeib() {
		return m_leib;
	}

	public void setLeib(String leib) {
		this.m_leib = leib;
	}

	public String getZhuangt() {
		return m_zhuangt;
	}

	public void setZhuangt(String zhuangt) {
		this.m_zhuangt = zhuangt;
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

	public Xitxxbean() {
	}

	public Xitxxbean(boolean sel,long id, int xuh, String diancxxb_id, String mingc,
			String zhi, String danw, String leib, String zhuangt, String beiz) {

		this.m_sel = sel;
		this.m_id = id;
		this.m_xuh = xuh;
		this.m_diancxxb_id = diancxxb_id;
		this.m_mingc = mingc;
		this.m_zhi = zhi;
		this.m_danw = danw;
		this.m_leib = leib;
		this.m_zhuangt = zhuangt;
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
		buffer.append(m_diancxxb_id);
		buffer.append(',');
		buffer.append(m_mingc);
		buffer.append(',');
		buffer.append(m_zhi);
		buffer.append(',');
		buffer.append(m_danw);
		buffer.append(',');
		buffer.append(m_leib);
		buffer.append(',');
		buffer.append(m_zhuangt);
		buffer.append(',');
		buffer.append(m_beiz);

		
		return buffer.toString();
	}

}
