package com.zhiren.dc.lic;

import java.io.Serializable;

public class Licbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8365708025912844169L;

	private int m_xuh;

	private long m_id;//

	private long m_diancxxb_id;//

	private String m_faz_id;// 发站

	private String m_daoz_id;// 到站

	private String m_liclxb_id;// 类型

	private double m_zhi;// 值

	private String m_beiz;// 备注

	public int getXuh() {
		return m_xuh;
	}

	public void setXuh(int xuh) {
		this.m_xuh = xuh;
	}

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

	public String getFaz_id() {
		return m_faz_id;
	}

	public void setFaz_id(String faz_id) {
		this.m_faz_id = faz_id;
	}

	public String getDaoz_id() {
		return m_daoz_id;
	}

	public void setDaoz_id(String daoz_id) {
		this.m_daoz_id = daoz_id;
	}

	public String getLiclxb_id() {
		return m_liclxb_id;
	}

	public void setLiclxb_id(String liclxb_id) {
		this.m_liclxb_id = liclxb_id;
	}

	public double getZhi() {
		return m_zhi;
	}

	public void setZhi(double zhi) {
		this.m_zhi = zhi;
	}

	public String getBeiz() {
		return m_beiz;
	}

	public void setBeiz(String beiz) {
		this.m_beiz = beiz;
	}

	public Licbean() {
	}

	public Licbean(int xuh, long id, long diancxxb_id, String faz_id,
			String daoz_id, String liclxb_id, double zhi, String beiz) {

		this.m_xuh = xuh;
		this.m_id = id;
		this.m_diancxxb_id = diancxxb_id;
		this.m_faz_id = faz_id;
		this.m_daoz_id = daoz_id;
		this.m_liclxb_id = liclxb_id;
		this.m_zhi = zhi;
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
		buffer.append(m_faz_id);
		buffer.append(',');
		buffer.append(m_daoz_id);
		buffer.append(',');
		buffer.append(m_liclxb_id);
		buffer.append(',');
		buffer.append(m_zhi);
		buffer.append(',');
		buffer.append(m_beiz);

		return buffer.toString();
	}

}
