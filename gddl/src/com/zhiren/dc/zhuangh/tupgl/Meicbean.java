package com.zhiren.dc.zhuangh.tupgl;

import java.io.Serializable;

import org.apache.tapestry.request.IUploadFile;

public class Meicbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long m_id;//id

	private long m_xuh;//diancxxb_id

	private String m_mingc;//mingc

	private String m_piny;//piny

	private String m_kuc;//quanc

	private String m_changd;//bum

	private String m_kuand;//xingb

	private String m_mianj;

	private String m_gaod;

	private String m_tij;

	private String m_meict;//tupmc

	private String m_diandml;

	private String m_beiz;//beiz

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

	public Meicbean(long id, long xuh, String mingc, String piny, String kuc,
			String changd, String kuand, String mianj, String gaod,
			String tij, String meict, String diandml, String beiz) {
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

	public String getChangd() {
		return m_changd;
	}

	public void setChangd(String changd) {
		this.m_changd = changd;
	}

	public String getDiandml() {
		return m_diandml;
	}

	public void setDiandml(String diandml) {
		this.m_diandml = diandml;
	}

	public String getGaod() {
		return m_gaod;
	}

	public void setGaod(String gaod) {
		this.m_gaod = gaod;
	}

	public String getKuand() {
		return m_kuand;
	}

	public void setKuand(String kuand) {
		this.m_kuand = kuand;
	}

	public String getKuc() {
		return m_kuc;
	}

	public void setKuc(String kuc) {
		this.m_kuc = kuc;
	}

	public String getMeict() {
		return m_meict;
	}

	public void setMeict(String meict) {
		this.m_meict = meict;
	}

	public String getMianj() {
		return m_mianj;
	}

	public void setMianj(String mianj) {
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

	public String getTij() {
		return m_tij;
	}

	public void setTij(String tij) {
		this.m_tij = tij;
	}

}
