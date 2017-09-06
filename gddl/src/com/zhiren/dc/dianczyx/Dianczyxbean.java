package com.zhiren.dc.dianczyx;

import java.io.Serializable;

public class Dianczyxbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6639638173064611902L;

	private long m_id;//

	private long m_diancxxb_id;//

	private int m_xuh;//

	private String m_bianm;// 专用线编码

	private String m_mingc;// 专用线名称

	private double m_lic = 0.0;// 专用线里程

	private String m_beiz;

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

	public double getLic() {
		return m_lic;
	}

	public void setLic(double lic) {
		m_lic = lic;
	}

	public String getBeiz() {
		return m_beiz;
	}

	public void setBeiz(String beiz) {
		m_beiz = beiz;
	}

	public Dianczyxbean() {
	}

	public Dianczyxbean(int xuh, long id, long diancxxb_id, String bianm,
			String mingc, double lic, String beiz) {

		this.m_xuh = xuh;
		this.m_id = id;
		this.m_diancxxb_id = diancxxb_id;
		this.m_bianm = bianm;
		this.m_mingc = mingc;
		this.m_lic = lic;
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
		buffer.append(m_bianm);
		buffer.append(',');
		buffer.append(m_mingc);
		buffer.append(',');
		buffer.append(m_beiz);

		return buffer.toString();
	}

}
