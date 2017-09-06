/*
 * 日期：2016年6月23日
 * 作者：Qiuzw
 * 修改内容：增加姚孟电厂“提煤单号”字段定义
 * */

package com.zhiren.dc.jilgl.qichmz;

import java.io.Serializable;

public class Qichmzbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3527157334529076490L;

	private boolean m_selected;
	
	private long m_id;//

	private String m_cheph;// 车号

	private String m_shouhr;// 收获人

	private int m_meikdqb_id;//

	private String m_meikdqmc;// 发货单位

	private int m_meikxxb_id;//

	private String m_meikdwmc;// 煤矿单位

	private String m_pinz;// 品种

	private double m_fahl;// 发货量

	private double m_maoz;// 毛重

	private double m_piz;// 皮重

	private double m_jingz;// 净重

	private double m_koud;// 扣吨

	private double m_yuns;// 运损

	private double m_kuid;// 亏吨

	private double yingd;

	private String m_jihkj;

	private String m_chengydw;

	private String m_meigy;

	private String m_meic;

	private String m_jianjy;

	private String m_changb;

	private String m_beiz;
	
	private String m_pandmc;

	private int youxcs;
	
	private String m_timdh;//“提煤单号”，姚孟电厂专用
	
	public String getTimdh(){
		return m_timdh;
	}
	
	public void setTimdh(String s){
		m_timdh = s;
	}
	
	public boolean getSelected(){
		return m_selected;
	}
	
	public void setSelected(boolean s){
		m_selected = s;
	}

	public int getYouxcs() {
		return youxcs;
	}

	public void setYouxcs(int youxcs) {
		this.youxcs = youxcs;
	}

	public String getJihkj() {
		return m_jihkj;
	}

	public void setJihkj(String jihkj) {
		m_jihkj = jihkj;
	}

	public double getYingd() {
		return yingd;
	}

	public void setYingd(double yingd) {
		this.yingd = yingd;
	}

	public long getId() {
		return m_id;
	}

	public void setId(long id) {
		this.m_id = id;
	}

	public String getCheph() {
		return m_cheph;
	}

	public void setCheph(String cheph) {
		this.m_cheph = cheph;
	}

	public String getShouhr() {
		return m_shouhr;
	}

	public void setShouhr(String shouhr) {
		this.m_shouhr = shouhr;
	}

	public int getMeikdqb_id() {
		return m_meikdqb_id;
	}

	public void setMeikdqb_id(int meikdqb_id) {
		this.m_meikdqb_id = meikdqb_id;
	}

	public String getMeikdqmc() {
		return m_meikdqmc;
	}

	public void setMeikdqmc(String meikdqmc) {
		this.m_meikdqmc = meikdqmc;
	}

	public int getMeikxxb_id() {
		return m_meikxxb_id;
	}

	public void setMeikxxb_id(int meikxxb_id) {
		this.m_meikxxb_id = meikxxb_id;
	}

	public String getMeikdwmc() {
		return m_meikdwmc;
	}

	public void setMeikdwmc(String meikdwmc) {
		this.m_meikdwmc = meikdwmc;
	}

	public String getPinz() {
		return m_pinz;
	}

	public void setPinz(String pinz) {
		this.m_pinz = pinz;
	}

	public double getFahl() {
		return m_fahl;
	}

	public void setFahl(double fahl) {
		this.m_fahl = fahl;
	}

	public double getMaoz() {
		return m_maoz;
	}

	public void setMaoz(double maoz) {
		this.m_maoz = maoz;
	}

	public double getPiz() {
		return m_piz;
	}

	public void setPiz(double piz) {
		this.m_piz = piz;
	}

	public double getJingz() {
		return m_jingz;
	}

	public void setJingz(double jingz) {
		this.m_jingz = jingz;
	}

	public double getKoud() {
		return m_koud;
	}

	public void setKoud(double koud) {
		this.m_koud = koud;
	}

	public double getYuns() {
		return m_yuns;
	}

	public void setYuns(double yuns) {
		this.m_yuns = yuns;
	}

	public double getKuid() {
		return m_kuid;
	}

	public void setKuid(double kuid) {
		this.m_kuid = kuid;
	}

	public String getChengydw() {
		return m_chengydw;
	}

	public void setChengydw(String chengydw) {
		this.m_chengydw = chengydw;
	}

	public String getMeigy() {
		return m_meigy;
	}

	public void setMeigy(String meigy) {
		this.m_meigy = meigy;
	}

	public String getMeic() {
		return m_meic;
	}

	public void setMeic(String meic) {
		this.m_meic = meic;
	}

	public String getJianjy() {
		return m_jianjy;
	}

	public void setJianjy(String jianjy) {
		this.m_jianjy = jianjy;
	}

	public String getChangb() {
		return m_changb;
	}

	public void setChangb(String changb) {
		this.m_changb = changb;
	}

	public String getBeiz() {
		return m_beiz;
	}

	public void setBeiz(String beiz) {
		this.m_beiz = beiz;
	}
	
	public String getPandmc() {
		return m_pandmc;
	}

	public void setPandmc(String pandmc) {
		this.m_pandmc = pandmc;
	}

	public Qichmzbean() {
	}
	
	public Qichmzbean(long id,boolean selected, String cheph, String pandmc, 
			String fahdw, String meikdw,String pinz, String jihkj, double fahl, 
			double maoz, double piz, double koud, String meic, String meigy,
			String shouhr, String chengydw, String changb, String beiz){
		this.m_id = id;
		this.m_selected = selected;
		this.m_cheph = cheph;
		this.m_pandmc = pandmc;
		this.m_meikdqmc = fahdw;
		this.m_meikdwmc = meikdw;
		this.m_pinz = pinz;
		this.m_jihkj = jihkj;
		this.m_fahl = fahl;
		this.m_maoz = maoz;
		this.m_piz = piz;
		this.m_koud = koud;
		this.m_meic = meic;
		this.m_meigy = meigy;
		this.m_shouhr = shouhr;
		this.m_chengydw = chengydw;
		this.m_changb = changb;
		this.m_beiz = beiz;
	}

	public Qichmzbean(String shouhr, String meikdqmc, String meikdwmc,
			String ranlpz, String jihkj, String chengydw, String meic,
			String meigy, String changb) {
		this.m_shouhr = shouhr;
		this.m_meikdqmc = meikdqmc;
		this.m_meikdwmc = meikdwmc;
		this.m_pinz = ranlpz;
		this.m_jihkj = jihkj;
		this.m_chengydw = chengydw;
		this.m_meic = meic;
		this.m_meigy = meigy;
		this.m_changb = changb;
	}
	
	public Qichmzbean(boolean selected,String shouhr, String meikdqmc, String meikdwmc,
			String ranlpz, String jihkj, String chengydw, String meic,
			String meigy, String changb, String pandmc, double fahl, double koud) {
		this.m_selected = selected;
		this.m_shouhr = shouhr;
		this.m_meikdqmc = meikdqmc;
		this.m_meikdwmc = meikdwmc;
		this.m_pinz = ranlpz;
		this.m_jihkj = jihkj;
		this.m_chengydw = chengydw;
		this.m_meic = meic;
		this.m_meigy = meigy;
		this.m_changb = changb;
		this.m_pandmc = pandmc;
		this.m_fahl = fahl;
		this.m_koud = koud;
	}

	public Qichmzbean(long id, String cheph, String shouhr, int meikdqb_id,
			String meikdqmc, int meikxxb_id, String meikdwmc, String pinz,
			double fahl, double maoz, double piz, double jingz, double koud,
			double yuns, double kuid, String jihkj, int youxcs,
			String chengydw, String meigy, String pandmc, String meic, String changb,
			String beiz) {
		this.youxcs = youxcs;
		this.m_id = id;
		this.m_cheph = cheph;
		this.m_shouhr = shouhr;
		this.m_meikdqb_id = meikdqb_id;
		this.m_meikdqmc = meikdqmc;
		this.m_meikxxb_id = meikxxb_id;
		this.m_meikdwmc = meikdwmc;
		this.m_pinz = pinz;
		this.m_fahl = fahl;
		this.m_maoz = maoz;
		this.m_piz = piz;
		this.m_jingz = jingz;
		this.m_koud = koud;
		this.m_yuns = yuns;
		this.m_kuid = kuid;
		this.m_jihkj = jihkj;
		this.m_chengydw = chengydw;
		this.m_meigy = meigy;
		this.m_pandmc = pandmc;
		this.m_meic = meic;
		this.m_changb = changb;
		this.m_beiz = beiz;
	}

	public String toString() {

		StringBuffer buffer = new StringBuffer("");

		buffer.append(m_id);
		buffer.append(',');
		buffer.append(m_cheph);
		buffer.append(',');
		buffer.append(m_pandmc);
		buffer.append(',');
		buffer.append(m_shouhr);
		buffer.append(',');
		buffer.append(m_meikdqb_id);
		buffer.append(',');
		buffer.append(m_meikdqmc);
		buffer.append(',');
		buffer.append(m_meikxxb_id);
		buffer.append(',');
		buffer.append(m_meikdwmc);
		buffer.append(',');
		buffer.append(m_pinz);
		buffer.append(',');
		buffer.append(m_fahl);
		buffer.append(',');
		buffer.append(m_maoz);
		buffer.append(',');
		buffer.append(m_piz);
		buffer.append(',');
		buffer.append(m_jingz);
		buffer.append(',');
		buffer.append(m_koud);
		buffer.append(',');
		buffer.append(m_yuns);
		buffer.append(',');
		buffer.append(m_kuid);
		buffer.append(',');
		buffer.append(m_chengydw);
		buffer.append(',');
		buffer.append(m_meigy);
		buffer.append(',');
		buffer.append(m_meic);
		buffer.append(',');
		buffer.append(m_changb);
		buffer.append(',');
		buffer.append(m_beiz);

		return buffer.toString();
	}

}
