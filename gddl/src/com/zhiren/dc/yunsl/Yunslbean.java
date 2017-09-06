package com.zhiren.dc.yunsl;

import java.io.Serializable;

public class Yunslbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 725479376632668328L;

	/***************************************************************************
	 * ID number 15 0 diancxxb_id 电厂信息表id number 15 0 pinzb_id 品种表_id number 15
	 * 0 yunsfsb_id 运输方式_id number 15 0 meikxxb_id 煤矿信息_id number 15 0 yunsl 运损率
	 * number 5 3
	 **************************************************************************/

	private long m_id;//

	private int m_xuh;// 序号

	private String m_diancxxb_id;//

	private String m_pinzb_id;// 品种

	private String m_yunsfsb_id;// 运输方式

	private String m_meikxxb_id;// 煤矿

	private double m_yunsl;// 运损率

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

	public String getPingzb_id() {
		return m_pinzb_id;
	}

	public void setPinzb_id(String pinzb_id) {
		this.m_pinzb_id = pinzb_id;
	}

	public String getYunsfsb_id() {
		return m_yunsfsb_id;
	}

	public void setYunsfsb_id(String yunsfsb_id) {
		this.m_yunsfsb_id = yunsfsb_id;
	}

	public String getMeikxxb_id() {
		return m_meikxxb_id;
	}

	public void setMeikxxb_id(String meikxxb_id) {
		this.m_meikxxb_id = meikxxb_id;
	}

	public double getYunsl() {
		return m_yunsl;
	}

	public void setYunsl(double yunsl) {
		this.m_yunsl = yunsl;
	}

	private boolean m_sel;

	public boolean getSel() {
		return m_sel;
	}

	public void setSel(boolean sel) {
		this.m_sel = sel;
	}

	public Yunslbean() {
	}

	public Yunslbean(boolean sel, long id, int xuh, String diancxxb_id,
			String pinzb_id, String yunsfsb_id, String meikxxb_id, double yunsl) {

		this.m_sel = sel;
		this.m_id = id;
		this.m_xuh = xuh;
		this.m_diancxxb_id = diancxxb_id;
		this.m_pinzb_id = pinzb_id;
		this.m_yunsfsb_id = yunsfsb_id;
		this.m_meikxxb_id = meikxxb_id;
		this.m_yunsl = yunsl;
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
		buffer.append(m_pinzb_id);
		buffer.append(',');
		buffer.append(m_yunsfsb_id);
		buffer.append(',');
		buffer.append(m_meikxxb_id);
		buffer.append(',');
		buffer.append(m_yunsl);

		return buffer.toString();
	}

}
