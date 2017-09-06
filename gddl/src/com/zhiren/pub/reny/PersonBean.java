package com.zhiren.pub.reny;

import java.io.Serializable;

import org.apache.tapestry.request.IUploadFile;

public class PersonBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7215130306035460153L;
	private boolean Selected;
	private long ID;
	private String Mingc;
	private String Mim; 
	private String Piny;
	private String Quanc;
	private long DiancxxbId;
	private String Bum;
	private String Zhiw;
	private String Xingb;
	private String Lianxdz;
	private String Youzbm;
	private String Chuanz;
	private String Yiddh;
	private String Guddh;
	private String Email;
	private String Zhuangt;
	private String Beiz;
	private IUploadFile Qianm;
	public String getBeiz() {
		return Beiz;
	}
	public void setBeiz(String beiz) {
		Beiz = beiz;
	}
	public String getBum() {
		return Bum;
	}
	public void setBum(String bum) {
		Bum = bum;
	}
	public String getChuanz() {
		return Chuanz;
	}
	public void setChuanz(String chuanz) {
		Chuanz = chuanz;
	}
	public long getDiancxxbId() {
		return DiancxxbId;
	}
	public void setDiancxxbId(long diancxxbId) {
		DiancxxbId = diancxxbId;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getGuddh() {
		return Guddh;
	}
	public void setGuddh(String guddh) {
		Guddh = guddh;
	}
	public long getID() {
		return ID;
	}
	public void setID(long id) {
		ID = id;
	}
	public String getLianxdz() {
		return Lianxdz;
	}
	public void setLianxdz(String lianxdz) {
		Lianxdz = lianxdz;
	}
	public String getMim() {
		return Mim;
	}
	public void setMim(String mim) {
		Mim = mim;
	}
	public String getMingc() {
		return Mingc;
	}
	public void setMingc(String mingc) {
		Mingc = mingc;
	}
	public String getPiny() {
		return Piny;
	}
	public void setPiny(String piny) {
		Piny = piny;
	}
	public IUploadFile getQianm() {
		return Qianm;
	}
	public void setQianm(IUploadFile qianm) {
		Qianm = qianm;
	}
	public String getQuanc() {
		return Quanc;
	}
	public void setQuanc(String quanc) {
		Quanc = quanc;
	}
	public boolean getSelected() {
		return Selected;
	}
	public void setSelected(boolean selected) {
		Selected = selected;
	}
	public String getXingb() {
		return Xingb;
	}
	public void setXingb(String xingb) {
		Xingb = xingb;
	}
	public String getYiddh() {
		return Yiddh;
	}
	public void setYiddh(String yiddh) {
		Yiddh = yiddh;
	}
	public String getYouzbm() {
		return Youzbm;
	}
	public void setYouzbm(String youzbm) {
		Youzbm = youzbm;
	}
	public String getZhiw() {
		return Zhiw;
	}
	public void setZhiw(String zhiw) {
		Zhiw = zhiw;
	}
	public String getZhuangt() {
		return Zhuangt;
	}
	public void setZhuangt(String zhuangt) {
		Zhuangt = zhuangt;
	}
	/**
	 * @param selected
	 * @param id
	 * @param mingc
	 * @param mim
	 * @param piny
	 * @param quanc
	 * @param diancxxbId
	 * @param bum
	 * @param zhiw
	 * @param xingb
	 * @param lianxdz
	 * @param youzbm
	 * @param chuanz
	 * @param yiddh
	 * @param guddh
	 * @param email
	 * @param zhuangt
	 * @param beiz
	 * @param qianm
	 */
	public PersonBean(boolean selected, long id, String mingc, String mim, String piny, String quanc, long diancxxbId, String bum, String zhiw, String xingb, String lianxdz, String youzbm, String chuanz, String yiddh, String guddh, String email, String zhuangt, String beiz, IUploadFile qianm) {
		super();
		Selected = selected;
		ID = id;
		Mingc = mingc;
		Mim = mim;
		Piny = piny;
		Quanc = quanc;
		DiancxxbId = diancxxbId;
		Bum = bum;
		Zhiw = zhiw;
		Xingb = xingb;
		Lianxdz = lianxdz;
		Youzbm = youzbm;
		Chuanz = chuanz;
		Yiddh = yiddh;
		Guddh = guddh;
		Email = email;
		Zhuangt = zhuangt;
		Beiz = beiz;
		Qianm = qianm;
	}
	/**
	 * @param selected
	 * @param id
	 * @param mingc
	 * @param piny
	 * @param quanc
	 * @param diancxxbId
	 * @param xingb
	 * @param zhuangt
	 */
	public PersonBean(boolean selected, long id, String mingc, String piny, String quanc, long diancxxbId, String xingb, String zhuangt) {
		super();
		Selected = selected;
		ID = id;
		Mingc = mingc;
		Piny = piny;
		Quanc = quanc;
		DiancxxbId = diancxxbId;
		Xingb = xingb;
		Zhuangt = zhuangt;
	}
	
}
