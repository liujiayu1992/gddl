/*
 * 时间：2008-04-29
 * 作者：Qiuzw
 * 描述：
 *      增加“检皮人员”字段。
 *      平圩电厂提出打印“汽车检斤单”时检毛人员和检皮人员名字重复，实际为2人操作。
 * 修改前：
 *      无
 * 修改后：
 *      新增加
 * 影响电厂：
 *      平圩电厂
 */
package com.zhiren.dc.jilgl.qichjjddy;

import java.io.Serializable;

public class Qichjjddybean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 9100885254629770938L;

    private long	      m_id;				   //

    private String m_cheph;				// 车号

    private String m_shouhr;			       // 收获人

    private long	      m_meikdqb_id;			   //

    private String m_meikdqmc;			     // 发货单位

    private long	      m_meikxxb_id;			   //

    private String m_meikdwmc;			     // 煤矿单位

    private String m_pinz;				 // 品种

    private double	    m_fahl;				 // 发货量

    private double	    m_maoz;				 // 毛重

    private double	    m_piz;				  // 皮重

    private double	    m_jingz;				// 净重

    private double	    m_koud;				 // 扣吨

    private double	    m_yuns;				 // 运损

    private double	    m_kuid;				 // 亏吨

    private double	    yingd;

    private long	      fahb_id;

    private int	       ches;

    private long	      zhilb_id;

    private String jihkj;

    private String jianmsj;

    private String jianpsj;

    private String m_chengydw;

    private String m_meigy;

    private String m_jianjy;

    private String m_beiz;

    private String m_meic;

    private boolean	   m_xuanz;

    public boolean getXuanz() {
	return m_xuanz;
    }

    public void setXuanz(boolean xuanz) {
	this.m_xuanz = xuanz;
    }

    public String getJianmsj() {
	return jianmsj;
    }

    public void setJianmsj(String jianmsj) {
	this.jianmsj = jianmsj;
    }

    public String getJianpsj() {
	return jianpsj;
    }

    public void setJianpsj(String jianpsj) {
	this.jianpsj = jianpsj;
    }

    public String getJihkj() {
	return jihkj;
    }

    public void setJihkj(String jihkj) {
	this.jihkj = jihkj;
    }

    public long getZhilb_id() {
	return zhilb_id;
    }

    public void setZhilb_id(long zhilb_id) {
	this.zhilb_id = zhilb_id;
    }

    public int getChes() {
	return ches;
    }

    public void setChes(int ches) {
	this.ches = ches;
    }

    public long getFahb_id() {
	return fahb_id;
    }

    public void setFahb_id(long fahb_id) {
	this.fahb_id = fahb_id;
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

    public long getMeikdqb_id() {
	return m_meikdqb_id;
    }

    public void setMeikdqb_id(long meikdqb_id) {
	this.m_meikdqb_id = meikdqb_id;
    }

    public String getMeikdqmc() {
	return m_meikdqmc;
    }

    public void setMeikdqmc(String meikdqmc) {
	this.m_meikdqmc = meikdqmc;
    }

    public long getMeikxxb_id() {
	return m_meikxxb_id;
    }

    public void setMeikxxb_id(long meikxxb_id) {
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

    public String getJianjy() {
	return m_jianjy;
    }

    public void setJianjy(String jianjy) {
	this.m_jianjy = jianjy;
    }

    public String getBeiz() {
	return m_beiz;
    }

    public void setBeiz(String beiz) {
	this.m_beiz = beiz;
    }

    public String getMeic() {
	return m_meic;
    }

    public void setMeic(String meic) {
	this.m_meic = meic;
    }

    private String m_JIANPY;

    public String getJIANPY() {
	return m_JIANPY;
    }

    public void setJIANPY(String JIANPY) {
	m_JIANPY = JIANPY;
    }

    public Qichjjddybean() {
    }

    public Qichjjddybean(String shouhr) {
	this.m_shouhr = shouhr;
    }

    public Qichjjddybean(boolean xuanz, long id, String cheph, String meikdqmc,
                         String meikdwmc, String pinz, String jihkj, double fahl,
                         double maoz, double piz, double jingz, double koud, double yuns,
                         String meic, String jianmsj, String jianpsj, String chengydw,
                         String meigy, String jianjy, String shouhr, String beiz) {
	this.m_xuanz = xuanz;
	this.m_id = id;
	this.m_cheph = cheph;
	this.m_meikdqmc = meikdqmc;
	this.m_meikdwmc = meikdwmc;
	this.m_pinz = pinz;
	this.jihkj = jihkj;
	this.m_fahl = fahl;
	this.m_maoz = maoz;
	this.m_piz = piz;
	this.m_jingz = jingz;
	this.m_koud = koud;
	this.m_yuns = yuns;
	this.m_meic = meic;
	this.jianmsj = jianmsj;
	this.jianpsj = jianpsj;
	this.m_chengydw = chengydw;
	this.m_meigy = meigy;
	this.m_jianjy = jianjy;
	this.m_shouhr = shouhr;
	this.m_beiz = beiz;
    }

    public String toString() {

	StringBuffer buffer = new StringBuffer("");

	buffer.append(m_cheph);
	buffer.append(',');
	buffer.append(m_meikdqmc);
	buffer.append(',');
	buffer.append(m_meikdwmc);
	buffer.append(',');
	buffer.append(m_pinz);
	buffer.append(',');
	buffer.append(jihkj);
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
	buffer.append(m_meic);
	buffer.append(',');
	buffer.append(jianmsj);
	buffer.append(',');
	buffer.append(jianpsj);
	buffer.append(',');
	buffer.append(m_chengydw);
	buffer.append(',');
	buffer.append(m_meigy);
	buffer.append(',');
	buffer.append(m_jianjy);
	buffer.append(',');
	buffer.append(m_shouhr);
	buffer.append(',');
	buffer.append(m_beiz);

	return buffer.toString();
    }

}
