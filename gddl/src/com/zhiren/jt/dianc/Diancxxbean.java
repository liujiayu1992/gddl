package com.zhiren.jt.dianc;

import java.io.Serializable;

/**
 * @author 王刚
 * 
 */
public class Diancxxbean implements Serializable {
	/*
	 * ID number XUH 序号 number bianm 编码 varchar2 mingc 简称 varchar2 QUANC 全称
	 * varchar2 piny 拼音 varchar2 Shengfb_id 省 number fuid 父id number DIZ 地址
	 * varchar2 YOUZBM 邮政编码 varchar2 zongj 总机 varchar2 RANLCDH 燃料处电话 varchar2
	 * ZHUANGJRL 装机容量(MW) number ZUIDKC 最大库存(吨) number ZHENGCCB 正常储备(吨) number
	 * XIANFHKC 限负荷库存(吨) number RIJHM 日均耗煤(吨) number JINGJCMSX 经济储煤上限(吨) number
	 * JINGJCMXX 经济储煤下限(吨) number dongcmzb 冬储煤指标(吨) number FADDBR 法定代表人 varchar2
	 * WEITDLR 委托代理人 varchar2 KAIHYH 开户银行 varchar2 ZHANGH 帐号 varchar2 DIANH 电话
	 * varchar2 SHUIH 税号 varchar2 JIEXFS 接卸方式 varchar2 JIEXX 接卸线 number JIEXNL
	 * 接卸能力(车) number CAIYFS 采样方式（人工，机械） varchar2 JILFS 计量方式（过衡、检尺） varchar2
	 * RANLFZR 燃料负责人 varchar2 LIANXDZ 联系地址 varchar2 JINGJCML 警戒存煤（吨） number jib
	 * 级别(集团1，分公司2，电厂3) number BEIZ 备注 varchar2
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = -3134707903385557624L;

	private long m_id;

	private int m_xuh;

	private String m_bianm;

	private String m_mingc;

	private String m_quanc;

	private String m_piny;

	private String m_shengfb_id;

	private String m_fuid;

	private String m_diz;

	private String m_youzbm;

	private String m_zongj;

	private String m_ranlcdh;

	private int m_zhuangjrl;

	private int m_zuidkc;

	private int m_zhengccb;

	private int m_xianfhkc;

	private int m_rijhm;

	private int m_jingjcmsx;

	private int m_jingjcmxx;

	private int m_dongcmzb;

	private String m_faddbr;

	private String m_weitdlr;

	private String m_kaihyh;

	private String m_zhangh;

	private String m_dianh;

	private String m_shuih;

	private String m_jiexfs;

	private int m_jiexx;

	private int m_jiexnl;

	private String m_caiyfs;

	private String m_jilfs;

	private String m_ranlfzr;

	private String m_lianxdz;

	private int m_jingjcml;

	private String m_jib;

	private String m_beiz;
	
	private String m_dianclbb_id;
	
	private String m_daoz;
	
	private String m_daog;

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

	public String getQuanc() {
		return m_quanc;
	}

	public void setQuanc(String quanc) {
		this.m_quanc = quanc;
	}

	public String getPiny() {
		return m_piny;
	}

	public void setPiny(String piny) {
		this.m_piny = piny;
	}

	public String getShengfb_id() {
		return m_shengfb_id;
	}

	public void setShengfb_id(String shengfb_id) {
		this.m_shengfb_id = shengfb_id;
	}

	public String getFuid() {
		return m_fuid;
	}

	public void setFuid(String fuid) {
		this.m_fuid = fuid;
	}

	public String getDiz() {
		return m_diz;
	}

	public void setDiz(String diz) {
		this.m_diz = diz;
	}

	public String getYouzbm() {
		return m_youzbm;
	}

	public void setYouzbm(String youzbm) {
		this.m_youzbm = youzbm;
	}

	public String getZongj() {
		return m_zongj;
	}

	public void setZongj(String zongj) {
		this.m_zongj = zongj;
	}

	public String getRanlcdh() {
		return m_ranlcdh;
	}

	public void setRanlcdh(String ranlcdh) {
		this.m_ranlcdh = ranlcdh;
	}

	public int getZhuangjrl() {
		return m_zhuangjrl;
	}

	public void setZhuangjrl(int zhuangjrl) {
		this.m_zhuangjrl = zhuangjrl;
	}

	public int getZuidkc() {
		return m_zuidkc;
	}

	public void setZuidkc(int zuidkc) {
		this.m_zuidkc = zuidkc;
	}

	public int getZhengccb() {
		return m_zhengccb;
	}

	public void setZhengccb(int zhengccb) {
		this.m_zhengccb = zhengccb;
	}

	public int getXianfhkc() {
		return m_xianfhkc;
	}

	public void setXianfhkc(int xianfhkc) {
		this.m_xianfhkc = xianfhkc;
	}

	public int getRijhm() {
		return m_rijhm;
	}

	public void setRijhm(int rijhm) {
		this.m_rijhm = rijhm;
	}

	public int getJingjcmsx() {
		return m_jingjcmsx;
	}

	public void setJingjcmsx(int jingjcmsx) {
		this.m_jingjcmsx = jingjcmsx;
	}

	public int getJingjcmxx() {
		return m_jingjcmxx;
	}

	public void setJingjcmxx(int jingjcmxx) {
		this.m_jingjcmxx = jingjcmxx;
	}

	public int getDongcmzb() {
		return m_dongcmzb;
	}

	public void setDongcmzb(int dongcmzb) {
		this.m_dongcmzb = dongcmzb;
	}

	public String getFaddbr() {
		return m_faddbr;
	}

	public void setFaddbr(String faddbr) {
		this.m_faddbr = faddbr;
	}

	public String getWeitdlr() {
		return m_weitdlr;
	}

	public void setWeitdlr(String weitdlr) {
		this.m_weitdlr = weitdlr;
	}

	public String getKaihyh() {
		return m_kaihyh;
	}

	public void setKaihyh(String kaihyh) {
		this.m_kaihyh = kaihyh;
	}

	public String getZhangh() {
		return m_zhangh;
	}

	public void setZhangh(String zhangh) {
		this.m_zhangh = zhangh;
	}

	public String getDianh() {
		return m_dianh;
	}

	public void setDianh(String dianh) {
		this.m_dianh = dianh;
	}

	public String getShuih() {
		return m_shuih;
	}

	public void setShuih(String shuih) {
		this.m_shuih = shuih;
	}

	public String getJiexfs() {
		return m_jiexfs;
	}

	public void setJiexfs(String jiexfs) {
		this.m_jiexfs = jiexfs;
	}

	public int getJiexx() {
		return m_jiexx;
	}

	public void setJiexx(int jiexx) {
		this.m_jiexx = jiexx;
	}

	public int getJiexnl() {
		return m_jiexnl;
	}

	public void setJiexnl(int jiexnl) {
		this.m_jiexnl = jiexnl;
	}

	public String getCaiyfs() {
		return m_caiyfs;
	}

	public void setCaiyfs(String caiyfs) {
		this.m_caiyfs = caiyfs;
	}

	public String getJilfs() {
		return m_jilfs;
	}

	public void setJilfs(String jilfs) {
		this.m_jilfs = jilfs;
	}

	public String getRanlfzr() {
		return m_ranlfzr;
	}

	public void setRanlfzr(String ranlfzr) {
		this.m_ranlfzr = ranlfzr;
	}

	public String getLianxdz() {
		return m_lianxdz;
	}

	public void setLianxdz(String lianxdz) {
		this.m_lianxdz = lianxdz;
	}

	public int getJingjcml() {
		return m_jingjcml;
	}

	public void setJingjcml(int jingjcml) {
		this.m_jingjcml = jingjcml;
	}

	public String getJib() {
		return m_jib;
	}

	public void setJib(String jib) {
		this.m_jib = jib;
	}

	public String getBeiz() {
		return m_beiz;
	}

	public void setBeiz(String beiz) {
		this.m_beiz = beiz;
	}

	public Diancxxbean() {
		super();
	}

	public Diancxxbean(long id, int xuh, String bianm, String mingc,
			String quanc, String piny, String shengfb_id, String fuid, String diz,
			String youzbm, String zongj, String ranlcdh, int zhuangjrl,
			int zuidkc, int zhengccb, int xianfhkc, int rijhm, int jingjcmsx,
			int jingjcmxx, int dongcmzb, String faddbr, String weitdlr,
			String kaihyh, String zhangh, String dianh, String shuih,
			String jiexfs, int jiexx, int jiexnl, String caiyfs, String jilfs,
			String ranlfzr, String lianxdz, int jingjcml, String jib, String beiz,String dianclbb_id,String daoz,String daog) {
		super();
		this.m_id = id;
		this.m_xuh = xuh;
		this.m_bianm = bianm;
		this.m_mingc = mingc;
		this.m_quanc = quanc;
		this.m_piny = piny;
		this.m_shengfb_id = shengfb_id;
		this.m_fuid = fuid;
		this.m_diz = diz;
		this.m_youzbm = youzbm;
		this.m_zongj = zongj;
		this.m_ranlcdh = ranlcdh;
		this.m_zhuangjrl = zhuangjrl;
		this.m_zuidkc = zuidkc;
		this.m_zhengccb = zhengccb;
		this.m_xianfhkc = xianfhkc;
		this.m_rijhm = rijhm;
		this.m_jingjcmsx = jingjcmsx;
		this.m_jingjcmxx = jingjcmxx;
		this.m_dongcmzb = dongcmzb;
		this.m_faddbr = faddbr;
		this.m_weitdlr = weitdlr;
		this.m_kaihyh = kaihyh;
		this.m_zhangh = zhangh;
		this.m_dianh = dianh;
		this.m_shuih = shuih;
		this.m_jiexfs = jiexfs;
		this.m_jiexx = jiexx;
		this.m_jiexnl = jiexnl;
		this.m_caiyfs = caiyfs;
		this.m_jilfs = jilfs;
		this.m_ranlfzr = ranlfzr;
		this.m_lianxdz = lianxdz;
		this.m_jingjcml = jingjcml;
		this.m_jib = jib;
		this.m_beiz = beiz;
		this.m_dianclbb_id = dianclbb_id;
		this.m_daoz = daoz;
		this.m_daog = daog;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer("");

		buffer.append(m_id);
		buffer.append(',');
		buffer.append(m_xuh);
		buffer.append(',');
		buffer.append(m_bianm);
		buffer.append(',');
		buffer.append(m_mingc);
		buffer.append(',');
		buffer.append(m_quanc);
		buffer.append(',');
		buffer.append(m_piny);
		buffer.append(',');
		buffer.append(m_shengfb_id);
		buffer.append(',');
		buffer.append(m_fuid);
		buffer.append(',');
		buffer.append(m_diz);
		buffer.append(',');
		buffer.append(m_youzbm);
		buffer.append(',');
		buffer.append(m_zongj);
		buffer.append(',');
		buffer.append(m_ranlcdh);
		buffer.append(',');
		buffer.append(m_zhuangjrl);
		buffer.append(',');
		buffer.append(m_zuidkc);
		buffer.append(',');
		buffer.append(m_zhengccb);
		buffer.append(',');
		buffer.append(m_xianfhkc);
		buffer.append(',');
		buffer.append(m_rijhm);
		buffer.append(',');
		buffer.append(m_jingjcmsx);
		buffer.append(',');
		buffer.append(m_jingjcmxx);
		buffer.append(',');
		buffer.append(m_dongcmzb);
		buffer.append(',');
		buffer.append(m_faddbr);
		buffer.append(',');
		buffer.append(m_weitdlr);
		buffer.append(',');
		buffer.append(m_kaihyh);
		buffer.append(',');
		buffer.append(m_zhangh);
		buffer.append(',');
		buffer.append(m_dianh);
		buffer.append(',');
		buffer.append(m_shuih);
		buffer.append(',');
		buffer.append(m_jiexfs);
		buffer.append(',');
		buffer.append(m_jiexx);
		buffer.append(',');
		buffer.append(m_jiexnl);
		buffer.append(',');
		buffer.append(m_caiyfs);
		buffer.append(',');
		buffer.append(m_jilfs);
		buffer.append(',');
		buffer.append(m_ranlfzr);
		buffer.append(',');
		buffer.append(m_lianxdz);
		buffer.append(',');
		buffer.append(m_jingjcml);
		buffer.append(',');
		buffer.append(m_jib);
		buffer.append(',');
		buffer.append(m_beiz);
		buffer.append(',');
		buffer.append(m_dianclbb_id);
		buffer.append(',');
		buffer.append(m_daoz);
		buffer.append(',');
		buffer.append(m_daog);

		return buffer.toString();
	}

	public String getDianclbb_id() {
		return m_dianclbb_id;
	}

	public void setDianclbb_id(String dianclbb_id) {
		this.m_dianclbb_id = dianclbb_id;
	}

	public String getDaoz() {
		return m_daoz;
	}

	public void setDaoz(String daoz) {
		this.m_daoz = daoz;
	}

	public String getDaog() {
		return m_daog;
	}

	public void setDaog(String daog) {
		this.m_daog = daog;
	}

}
