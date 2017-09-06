package com.zhiren.pub.shuzhlfw;

import java.io.Serializable;

public class Shuzhlfwbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long m_id;//

	private long m_diancxxb_id;//

	private String m_mingc;// 对象名称

	private double m_shangx;// 合理上限

	private double m_xiax;// 合理下限

	private String m_leib;// 类型

	private String m_beiz;// 备注

	private int m_xuh;// 序号

	public long getId() {
		return m_id;
	}

	public void setId(long id) {
		this.m_id = id;
	}

	public long getDiancxxb_id() {
		return m_diancxxb_id;
	}

	public void setDiancxxb_id(long diancxxb_id) {
		this.m_diancxxb_id = diancxxb_id;
	}

	public String getMingc() {
		return m_mingc;
	}

	public void setMingc(String mingc) {
		this.m_mingc = mingc;
	}

	public double getShangx() {
		return m_shangx;
	}

	public void setShangx(double shangx) {
		this.m_shangx = shangx;
	}

	public double getXiax() {
		return m_xiax;
	}

	public void setXiax(double xiax) {
		this.m_xiax = xiax;
	}

	public String getLeib() {
		return m_leib;
	}

	public void setLeib(String leib) {
		this.m_leib = leib;
	}

	public String getBeiz() {
		return m_beiz;
	}

	public void setBeiz(String beiz) {
		this.m_beiz = beiz;
	}

	public int getXuh() {
		return m_xuh;
	}

	public void setXuh(int xuh) {
		this.m_xuh = xuh;
	}

	public Shuzhlfwbean() {
	}

	public Shuzhlfwbean(int xuh, long id, long diancxxb_id, String mingc,
			double shangx, double xiax, String leib, String beiz) {

		this.m_xuh = xuh;
		this.m_id = id;
		this.m_diancxxb_id = diancxxb_id;
		this.m_mingc = mingc;
		this.m_shangx = shangx;
		this.m_xiax = xiax;
		this.m_leib = leib;
		this.m_beiz = beiz;
	}

	public String toString() {

		StringBuffer buffer = new StringBuffer("");
		buffer.append(m_xuh);
		buffer.append(',');
		buffer.append(m_id);
		buffer.append(',');
		buffer.append(m_diancxxb_id);
		buffer.append(',');
		buffer.append(m_mingc);
		buffer.append(',');
		buffer.append(m_shangx);
		buffer.append(',');
		buffer.append(m_xiax);
		buffer.append(',');
		buffer.append(m_leib);
		buffer.append(',');
		buffer.append(m_beiz);

		return buffer.toString();
	}

}
