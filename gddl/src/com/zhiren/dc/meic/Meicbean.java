package com.zhiren.dc.meic;

import java.io.Serializable;

import org.apache.tapestry.request.IUploadFile;

public class Meicbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long m_id;

	private long m_xuh;

	private String m_mingc;

	private String m_piny;

	private double m_kuc;

	private double m_changd;

	private double m_kuand;

	private double m_mianj;

	private double m_gaod;

	private double m_tij;

	private String m_meict;

	private double m_diandml;

	private String m_beiz;

	private IUploadFile Filepath;

	public IUploadFile getFilepath() {
		return Filepath;
	}

	public void setFilepath(IUploadFile filepath) {
		Filepath = filepath;
	}

	public long getId() {
		return m_id;
	}

	public void setId(long id) {
		this.m_id = id;
	}

	public long getXuh() {
		return m_xuh;
	}

	public void setXuh(long xuh) {
		this.m_xuh = xuh;
	}

	public String getBeiz() {
		return m_beiz;
	}

	public void setBeiz(String beiz) {
		this.m_beiz = beiz;
	}

	public Meicbean() {
	}
	
	public Meicbean(long xuh) {
		this.m_xuh = xuh;
	}

	public Meicbean(long id, long xuh, String mingc, String piny, double kuc,
			double changd, double kuand, double mianj, double gaod,
			double tij, String meict, double diandml, String beiz) {
		super();
		// TODO 自动生成构造函数存根
		// this.MEICSYTX_old=meicsytx_old;
		this.m_id = id;
		this.m_xuh = xuh;
		this.m_mingc = mingc;
		this.m_piny = piny;
		this.m_kuc = kuc;
		this.m_changd = changd;
		this.m_kuand = kuand;
		this.m_mianj = mianj;
		this.m_gaod = gaod;
		this.m_tij = tij;
		this.m_meict = meict;
		this.m_diandml = diandml;
		this.m_beiz = beiz;
	}

	public String toString() {

		StringBuffer buffer = new StringBuffer("");

		buffer.append(m_id);
		buffer.append(',');
		buffer.append(m_xuh);
		buffer.append(',');
		buffer.append(m_mingc);
		buffer.append(',');
		buffer.append(m_piny);
		buffer.append(',');
		buffer.append(m_kuc);
		buffer.append(',');
		buffer.append(m_changd);
		buffer.append(',');
		buffer.append(m_kuand);
		buffer.append(',');
		buffer.append(m_mianj);
		buffer.append(',');
		buffer.append(m_gaod);
		buffer.append(',');
		buffer.append(m_tij);
		buffer.append(',');
		buffer.append(m_meict);
		buffer.append(',');
		buffer.append(m_diandml);
		buffer.append(',');
		buffer.append(m_beiz);

		return buffer.toString();
	}

	public double getChangd() {
		return m_changd;
	}

	public void setChangd(double changd) {
		this.m_changd = changd;
	}

	public double getDiandml() {
		return m_diandml;
	}

	public void setDiandml(double diandml) {
		this.m_diandml = diandml;
	}

	public double getGaod() {
		return m_gaod;
	}

	public void setGaod(double gaod) {
		this.m_gaod = gaod;
	}

	public double getKuand() {
		return m_kuand;
	}

	public void setKuand(double kuand) {
		this.m_kuand = kuand;
	}

	public double getKuc() {
		return m_kuc;
	}

	public void setKuc(double kuc) {
		this.m_kuc = kuc;
	}

	public String getMeict() {
		return m_meict;
	}

	public void setMeict(String meict) {
		this.m_meict = meict;
	}

	public double getMianj() {
		return m_mianj;
	}

	public void setMianj(double mianj) {
		this.m_mianj = mianj;
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

	public double getTij() {
		return m_tij;
	}

	public void setTij(double tij) {
		this.m_tij = tij;
	}

}
