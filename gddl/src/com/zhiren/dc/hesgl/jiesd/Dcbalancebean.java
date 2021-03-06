package com.zhiren.dc.hesgl.jiesd;

import java.io.Serializable;

import java.util.Date;

/*
 * 作者：夏峥
 * 时间：2012-05-30
 * 描述：增加gongysb_id,meikxxb_id字段，并重新构造初始化方法
 */

public class Dcbalancebean implements Serializable {

	private long m_id;// 煤款_id

	private long m_yid;// 运费_id
	
	private long m_gongysb_id;
	
	private long m_meikxxb_id;

	private String m_Tianzdw;

	private String m_Fahdw;

	private String m_Meikdw;

	private String m_Faz;

	private long m_yunsfsb_id;

	private String m_jiesbh;//

	private Date m_fahksrq;//

	private Date m_fahjzrq;//

	private String m_shoukdw;//

	private String m_yuanshr;//

	private String m_xianshr;//

	private Date m_jiesrq;//

	private Date m_yansksrq;//

	private Date m_yansjzrq;//

	private String m_kaihyh;//

	private String m_pinz;//

	private double m_ches;//

	private String m_daibcc;//

	private String m_fapbh;//

	private String m_yansbh;//

	private String m_zhangh;//

	private String m_duifdd;//

	private String m_fukfs;//

	private double m_shulzjbz;//

	private String m_hetsl;//

	private double m_gongfsl;//

	private double m_yanssl;//

	private double m_yingk;//

	private double m_yingd;//

	private double m_kuid;//

	private double m_shulzjje;//

	private double m_jiessl;//

	private double m_jiesslcy;//

	private double m_yunfjsl;

	private double m_buhsdj;//

	private double m_jiakje;//

	private double m_bukyqjk;//

	private double m_jiakhj;//

	private double m_jiaksl;//

	private double m_jiaksk;//

	private double m_jiasje;//

	private double m_tielyf;//

	private double m_tielzf;//

	private double m_kuangqyf;//

	private double m_kuangqzf;//

	private double m_kuangqsk;//

	private double m_kuangqjk;//

	private double m_bukyqyzf;//

	private double m_jiskc;//

	private double m_buhsyf;//

	private double m_yunfsl;//

	private double m_yunfsk;//

	private double m_yunzfhj;//

	private double m_hej;//

	private double m_qiyfdj;//

	private String m_ranlbmjbr;//

	private Date m_ranlbmjbrq;//

	private String m_changcwjbr;//

	private Date m_changcwjbrq;//

	private Date m_ruzrq;//

	private String m_jieszxjbr;//

	private Date m_jieszxjbrq;//

	private String m_gongsrlbjbr;//

	private Date m_gongsrlbjbrq;//

	private double m_hetjg;//

	private double m_jieslx;//

	private double m_kuidjf;//

	private String m_beiz;//

	private String m_Fahrq;

	private String m_daohrq;

	private String m_daxhj;

	private String m_meikhjdx;

	private String m_yunzfhjdx;

	private double m_liubz;

	private double m_liuyk;

	private double m_qiyfhj;

	private double m_qiyfsk;

	private double m_qiyfjk;

	private double m_jingz;

	private double m_yuns;

	private double m_koud_js;

	private int m_butgys;

	private String m_yunsfs;

	private String m_meikjsdbh;

	private String m_diancjsbs;

	private long m_hetb_id;

	private String m_yunju; // 运距

	private long m_Meikjsb_id; // 煤款结算表id

	private long m_Yunfjsb_id; // 运费结算表id

	private long m_jihkjb_id; // 计划口径表id

	private double m_fengsjj; // 分公司加价

	private double m_jiajqdj; // 加价前单价

	private int m_jijlx; // 基价类型
	
	private String m_Mjtokcalxsclfs;//兆焦转大卡小数位处理
	
	private double m_kuidjfyf_je;	//亏吨拒付运费金额
	
	private double m_kuidjfzf_je;	//亏顿拒付杂费金额
	
	private double m_chaokdl;		//超/亏吨量
	
	private String m_ChaodOrKuid;	//记录是超吨还是亏吨
	
	private double m_yunfhsdj;		//运费结算单价
	
	private double m_Yunfjsdj_mk;	//运费结算单价(jiesb)
	
	private double m_Yunzfhj_mk;	//运杂费合计（jiesb）
	
	private double m_Buhsyf_mk;		//不含税运费（jiesb）
	
	private double m_Yunfjsl_mk;	//运费结算数量(jiesb)	
	
	private double m_Kouk_js;	//结算时扣款
	
	private long m_Chongdjsb_id;//冲抵预结算表id;
	
	private double m_weicdje;//未冲抵金额;
	
	private int m_jieslx_dt;//大同结算类型;
	
	public long getGongysb_id() {
		return m_gongysb_id;
	}

	public void setGongysb_id(long mGongysbId) {
		m_gongysb_id = mGongysbId;
	}

	public long getMeikxxb_id() {
		return m_meikxxb_id;
	}

	public void setMeikxxb_id(long mMeikxxbId) {
		m_meikxxb_id = mMeikxxbId;
	}
	
	public double getBuhsyf_mk() {
		return m_Buhsyf_mk;
	}

	public void setBuhsyf_mk(double buhsyf_mk) {
		m_Buhsyf_mk = buhsyf_mk;
	}

	public double getYunfjsdj_mk() {
		return m_Yunfjsdj_mk;
	}

	public void setYunfjsdj_mk(double yunfjsdj_mk) {
		m_Yunfjsdj_mk = yunfjsdj_mk;
	}

	public double getYunfjsl_mk() {
		return m_Yunfjsl_mk;
	}

	public void setYunfjsl_mk(double yunfjsl_mk) {
		m_Yunfjsl_mk = yunfjsl_mk;
	}

	public double getYunzfhj_mk() {
		return m_Yunzfhj_mk;
	}

	public void setYunzfhj_mk(double yunzfhj_mk) {
		m_Yunzfhj_mk = yunzfhj_mk;
	}

	public double getYunfhsdj(){
		
		return m_yunfhsdj;
	}
	
	public void setYunfhsdj(double Yunfhsdj){
		
		m_yunfhsdj = Yunfhsdj;
	}
	
	public double getChaokdl(){
		
		return m_chaokdl;
	}
	
	public void setChaokdl(double value){
		
		m_chaokdl = value;
	}
	
	public String getChaodOrKuid(){
		
		return m_ChaodOrKuid;
	}
	
	public void setChaodOrKuid(String value){
		
		m_ChaodOrKuid=value;
	}
	
	public double getKuidjfyf(){
		
		return m_kuidjfyf_je;
	}
	
	public void setKuidjfyf(double value){
		
		m_kuidjfyf_je = value;
	}
	
	public double getKuidjfzf(){
		
		return m_kuidjfzf_je;
	}
	
	public void setKuidjfzf(double value){
		
		m_kuidjfzf_je = value;
	}

	public String getMjtokcalxsclfs(){
		
		return m_Mjtokcalxsclfs;
	}
	
	public void setMjtokcalxsclfs(String value){
		
		m_Mjtokcalxsclfs=value;
	}
	
	public int getJijlx() {
		return m_jijlx;
	}

	public void setJijlx(int m_jijlx) {
		this.m_jijlx = m_jijlx;
	}

	public double getJiajqdj() {
		return m_jiajqdj;
	}

	public void setJiajqdj(double m_jiajqdj) {
		this.m_jiajqdj = m_jiajqdj;
	}

	public long getJihkjb_id() {

		return m_jihkjb_id;
	}

	public void setJihkjb_id(long value) {

		this.m_jihkjb_id = value;
	}

	public long getMeikjsb_id() {

		return m_Meikjsb_id;
	}

	public void setMeikjsb_id(long value) {

		this.m_Meikjsb_id = value;
	}

	public long getYunfjsb_id() {

		return m_Yunfjsb_id;
	}

	public void setYunfjsb_id(long value) {

		m_Yunfjsb_id = value;
	}

	public double getLiubz() {
		return m_liubz;
	}

	public void setLiubz(double liubz) {
		this.m_liubz = liubz;
	}

	public int getButgys() {
		return m_butgys;
	}

	public void setButgys(int butgys) {
		this.m_butgys = butgys;
	}

	public double getLiuyk() {
		return m_liuyk;
	}

	public void setLiuyk(double liuyk) {
		this.m_liuyk = liuyk;
	}

	public double getQiyfhj() {
		return m_qiyfhj;
	}

	public void setQiyfhj(double qiyfhj) {
		this.m_qiyfhj = qiyfhj;
	}

	public double getQiyfsk() {
		return m_qiyfsk;
	}

	public void setQiyfsk(double qiyfsk) {
		this.m_qiyfsk = qiyfsk;
	}

	public double getQiyfjk() {
		return m_qiyfjk;
	}

	public void setQiyfjk(double qiyfjk) {
		this.m_qiyfjk = qiyfjk;
	}

	public double getJingz() {
		return m_jingz;
	}

	public void setJingz(double jingz) {
		this.m_jingz = jingz;
	}

	public double getYuns() {
		return m_yuns;
	}

	public void setYuns(double yuns) {
		this.m_yuns = yuns;
	}

	public double getKoud_js() {
		return m_koud_js;
	}

	public void setKoud_js(double koud_js) {
		this.m_koud_js = koud_js;
	}

	public String getMeikjsdbh() {
		return m_meikjsdbh;
	}

	public void setMeikjsdbh(String meikjsdbh) {
		this.m_meikjsdbh = meikjsdbh;
	}

	public String getDiancjsbs() {
		return m_diancjsbs;
	}

	public void setDiancjsbs(String diancjsbs) {
		this.m_diancjsbs = diancjsbs;
	}

	public long getHetb_id() {
		return m_hetb_id;
	}

	public void setHetb_id(long hetb_id) {
		this.m_hetb_id = hetb_id;
	}

	public String getYunju() {
		return m_yunju;
	}

	public void setYunj(String yunju) {
		this.m_yunju = yunju;
	}

	public long getId() {
		return m_id;
	}

	public void setId(long id) {
		this.m_id = id;
	}

	public long getYid() {
		return m_yid;
	}

	public void setYid(long id) {
		this.m_yid = id;
	}

	public String getDaxhj() {
		return m_daxhj;
	}

	public void setDaxhj(String _daxhj) {
		this.m_daxhj = _daxhj;
	}

	public String getMeikhjdx() {
		return m_meikhjdx;
	}

	public void setMeikhjdx(String _meikhjdx) {
		this.m_meikhjdx = _meikhjdx;
	}

	public String getYunzfhjdx() {
		return m_yunzfhjdx;
	}

	public void setYunzfhjdx(String _yunzfhjdx) {
		this.m_yunzfhjdx = _yunzfhjdx;
	}

	public String getYunsfs() {
		return m_yunsfs;
	}

	public void setYunsfs(String yunsfs) {
		this.m_yunsfs = yunsfs;
	}

	public String getFaz() {
		return m_Faz;
	}

	public void setFaz(String _Faz) {
		this.m_Faz = _Faz;
	}

	public long getYunsfsb_id() {
		return m_yunsfsb_id;
	}

	public void setYunsfsb_id(long _yunsfsb_id) {
		this.m_yunsfsb_id = _yunsfsb_id;
	}

	public String getFahrq() {
		return m_Fahrq;
	}

	public void setFahrq(String _Fahrq) {
		this.m_Fahrq = _Fahrq;
	}

	public String getDaohrq() {
		return m_daohrq;
	}

	public void setDaohrq(String _daohrq) {
		this.m_daohrq = _daohrq;
	}

	public String getFahdw() {
		return m_Fahdw;
	}

	public void setFahdw(String _Fahdw) {
		this.m_Fahdw = _Fahdw;
	}

	public String getMeikdw() {
		return m_Meikdw;
	}

	public void setMeikdw(String _Meikdw) {
		this.m_Meikdw = _Meikdw;
	}

	public String getTianzdw() {
		return m_Tianzdw;
	}

	public void setTianzdw(String _Tianzdw) {
		this.m_Tianzdw = _Tianzdw;
	}

	public String getJiesbh() {
		return m_jiesbh;
	}

	public void setJiesbh(String jiesbh) {
		this.m_jiesbh = jiesbh;
	}

	public Date getJiesrq() {
		return m_jiesrq;
	}

	public void setJiesrq(Date jiesrq) {
		this.m_jiesrq = jiesrq;
	}

	public Date getFahksrq() {
		return m_fahksrq;
	}

	public void setFahksrq(Date fahksrq) {
		this.m_fahksrq = fahksrq;
	}

	public Date getFahjzrq() {
		return m_fahjzrq;
	}

	public void setFahjzrq(Date fahjzrq) {
		this.m_fahjzrq = fahjzrq;
	}

	public String getPinz() {
		return m_pinz;
	}

	public void setPinz(String pinz) {
		this.m_pinz = pinz;
	}

	public double getChes() {
		return m_ches;
	}

	public void setChes(double ches) {
		this.m_ches = ches;
	}

	public String getDaibcc() {
		return m_daibcc;
	}

	public void setDaibcc(String daibcc) {
		this.m_daibcc = daibcc;
	}

	public String getFapbh() {
		return m_fapbh;
	}

	public void setFapbh(String fapbh) {
		this.m_fapbh = fapbh;
	}

	public String getYuanshr() {
		return m_yuanshr;
	}

	public void setYuanshr(String yuanshr) {
		this.m_yuanshr = yuanshr;
	}

	public String getXianshr() {
		return m_xianshr;
	}

	public void setXianshr(String xianshr) {
		this.m_xianshr = xianshr;
	}

	public Date getYansksrq() {
		return m_yansksrq;
	}

	public void setYansksrq(Date yansksrq) {
		this.m_yansksrq = yansksrq;
	}

	public Date getYansjzrq() {
		return m_yansjzrq;
	}

	public void setYansjzrq(Date yansjzrq) {
		this.m_yansjzrq = yansjzrq;
	}

	public String getYansbh() {
		return m_yansbh;
	}

	public void setYansbh(String yansbh) {
		this.m_yansbh = yansbh;
	}

	public String getShoukdw() {
		return m_shoukdw;
	}

	public void setShoukdw(String shoukdw) {
		this.m_shoukdw = shoukdw;
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

	public String getDuifdd() {
		return m_duifdd;
	}

	public void setDuifdd(String duifdd) {
		this.m_duifdd = duifdd;
	}

	public String getFukfs() {
		return m_fukfs;
	}

	public void setFukfs(String fukfs) {
		this.m_fukfs = fukfs;
	}

	public double getShulzjbz() {
		return m_shulzjbz;
	}

	public void setShulzjbz(double shulzjbz) {
		this.m_shulzjbz = shulzjbz;
	}

	public double getGongfsl() {
		return m_gongfsl;
	}

	public void setGongfsl(double gongfsl) {
		this.m_gongfsl = gongfsl;
	}

	public double getYanssl() {
		return m_yanssl;
	}

	public void setYanssl(double yanssl) {
		this.m_yanssl = yanssl;
	}

	public double getYingksl() {
		return m_yingk;
	}

	public void setYingksl(double yingk) {
		this.m_yingk = yingk;
	}

	public double getYingd() {
		return m_yingd;
	}

	public void setYingd(double yingd) {
		this.m_yingd = yingd;
	}

	public double getKuid() {
		return m_kuid;
	}

	public void setKuid(double kuid) {
		this.m_kuid = kuid;
	}

	public double getShulzjje() {
		return m_shulzjje;
	}

	public void setShulzjje(double shulzjje) {
		this.m_shulzjje = shulzjje;
	}

	public double getJiessl() {
		return m_jiessl;
	}

	public void setJiessl(double jiessl) {
		this.m_jiessl = jiessl;
	}

	public double getJiesslcy() {
		return m_jiesslcy;
	}

	public void setJiesslcy(double jiesslcy) {
		this.m_jiesslcy = jiesslcy;
	}

	public double getYunfjsl() {
		return m_yunfjsl;
	}

	public void setYunfjsl(double yunfjsl) {
		this.m_yunfjsl = yunfjsl;
	}

	public double getBuhsdj() {
		return m_buhsdj;
	}

	public void setBuhsdj(double buhsdj) {
		this.m_buhsdj = buhsdj;
	}

	public double getJiakje() {
		return m_jiakje;
	}

	public void setJiakje(double jiakje) {
		this.m_jiakje = jiakje;
	}

	public double getBukyqjk() {
		return m_bukyqjk;
	}

	public void setBukyqjk(double bukyqjk) {
		this.m_bukyqjk = bukyqjk;
	}

	public double getJiakhj() {
		return m_jiakhj;
	}

	public void setJiakhj(double jiakhj) {
		this.m_jiakhj = jiakhj;
	}

	public double getJiaksl() {
		return m_jiaksl;
	}

	public void setJiaksl(double jiaksl) {
		this.m_jiaksl = jiaksl;
	}

	public double getJiaksk() {
		return m_jiaksk;
	}

	public void setJiaksk(double jiaksk) {
		this.m_jiaksk = jiaksk;
	}

	public double getJiasje() {
		return m_jiasje;
	}

	public void setJiasje(double jiasje) {
		this.m_jiasje = jiasje;
	}

	public double getTielyf() {
		return m_tielyf;
	}

	public void setTielyf(double tielyf) {
		this.m_tielyf = tielyf;
	}

	public double getTielzf() {
		return m_tielzf;
	}

	public void setTielzf(double tielzf) {
		this.m_tielzf = tielzf;
	}

	public double getKuangqyf() {
		return m_kuangqyf;
	}

	public void setKuangqyf(double kuangqyf) {
		this.m_kuangqyf = kuangqyf;
	}

	public double getKuangqzf() {
		return m_kuangqzf;
	}

	public void setKuangqzf(double kuangqzf) {
		this.m_kuangqzf = kuangqzf;
	}

	public double getKuangqsk() {
		return m_kuangqsk;
	}

	public void setKuangqsk(double kuangqsk) {
		this.m_kuangqsk = kuangqsk;
	}

	public double getKuangqjk() {
		return m_kuangqjk;
	}

	public void setKuangqjk(double kuangqjk) {
		this.m_kuangqjk = kuangqjk;
	}

	public double getBukyqyzf() {
		return m_bukyqyzf;
	}

	public void setBukyqyzf(double bukyqyzf) {
		this.m_bukyqyzf = bukyqyzf;
	}

	public double getJiskc() {
		return m_jiskc;
	}

	public void setJiskc(double jiskc) {
		this.m_jiskc = jiskc;
	}

	public double getBuhsyf() {
		return m_buhsyf;
	}

	public void setBuhsyf(double buhsyf) {
		this.m_buhsyf = buhsyf;
	}

	public double getYunfsl() {
		return m_yunfsl;
	}

	public void setYunfsl(double yunfsl) {
		this.m_yunfsl = yunfsl;
	}

	public double getYunfsk() {
		return m_yunfsk;
	}

	public void setYunfsk(double yunfsk) {
		this.m_yunfsk = yunfsk;
	}

	public double getYunzfhj() {
		return m_yunzfhj;
	}

	public void setYunzfhj(double yunzfhj) {
		this.m_yunzfhj = yunzfhj;
	}

	public double getHej() {
		return m_hej;
	}

	public void setHej(double hej) {
		this.m_hej = hej;
	}

	public double getQiyfdj() {
		return m_qiyfdj;
	}

	public void setQiyfdj(double qiyfdj) {
		this.m_qiyfdj = qiyfdj;
	}

	public String getRanlbmjbr() {
		return m_ranlbmjbr;
	}

	public void setRanlbmjbr(String ranlbmjbr) {
		this.m_ranlbmjbr = ranlbmjbr;
	}

	public Date getRanlbmjbrq() {
		return m_ranlbmjbrq;
	}

	public void setRanlbmjbrq(Date ranlbmjbrq) {
		this.m_ranlbmjbrq = ranlbmjbrq;
	}

	public String getChangcwjbr() {
		return m_changcwjbr;
	}

	public void setChangcwjbr(String changcwjbr) {
		this.m_changcwjbr = changcwjbr;
	}

	public Date getChangcwjbrq() {
		return m_changcwjbrq;
	}

	public void setChangcwjbrq(Date changcwjbrq) {
		this.m_changcwjbrq = changcwjbrq;
	}

	public Date getRuzrq() {
		return m_ruzrq;
	}

	public void setRuzrq(Date ruzrq) {
		this.m_ruzrq = ruzrq;
	}

	public String getJieszxjbr() {
		return m_jieszxjbr;
	}

	public void setJieszxjbr(String jieszxjbr) {
		this.m_jieszxjbr = jieszxjbr;
	}

	public Date getJieszxjbrq() {
		return m_jieszxjbrq;
	}

	public void setJieszxjbrq(Date jieszxjbrq) {
		this.m_jieszxjbrq = jieszxjbrq;
	}

	public String getGongsrlbjbr() {
		return m_gongsrlbjbr;
	}

	public void setGongsrlbjbr(String gongsrlbjbr) {
		this.m_gongsrlbjbr = gongsrlbjbr;
	}

	public Date getGongsrlbjbrq() {
		return m_gongsrlbjbrq;
	}

	public void setGongsrlbjbrq(Date gongsrlbjbrq) {
		this.m_gongsrlbjbrq = gongsrlbjbrq;
	}

	public double getHetjg() {
		return m_hetjg;
	}

	public void setHetjg(double hetjg) {
		this.m_hetjg = hetjg;
	}

	public double getJieslx() {
		return m_jieslx;
	}

	public void setJieslx(double jieslx) {
		this.m_jieslx = jieslx;
	}

	public double getKuidjf() {
		return m_kuidjf;
	}

	public void setKuidjf(double kuidjf) {
		this.m_kuidjf = kuidjf;
	}

	public String getBeiz() {
		return m_beiz;
	}

	public void setBeiz(String beiz) {
		this.m_beiz = beiz;
	}

	public String getHetsl() {

		return m_hetsl;
	}

	public void setHetsl(String hetsl) {

		this.m_hetsl = hetsl;
	}

	public double getFengsjj() {
		return m_fengsjj;
	}

	public void setFengsjj(double m_fengsjj) {
		this.m_fengsjj = m_fengsjj;
	}
	
	public double getKouk_js() {
		return m_Kouk_js;
	}

	public void setkouk_js(double value) {
		this.m_Kouk_js = value;
	}
	
	public long getChongdjsb_id() {
		return m_Chongdjsb_id;
	}

	public void setChongdjsb_id(long chongdjsb_id) {
		this.m_Chongdjsb_id = chongdjsb_id;
	}
	
	public double getweicdje() {
		return m_weicdje;
	}

	public void setweicdje(double m_weicdje) {
		this.m_weicdje = m_weicdje;
	}
	
	public int getjieslx_dt() {
		return m_jieslx_dt;
	}

	public void setjieslx_dt(int m_jieslx_dt) {
		this.m_jieslx_dt = m_jieslx_dt;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	

	public Dcbalancebean() {
	}

	public Dcbalancebean(long id, long yid, String tianzdw, String jiesbh,
			String fahdw, String meikdw, String faz, long yunsfsb_id,
			String shoukdw, Date fahksrq, Date fahjzrq, Date yansksrq,
			Date yansjzrq, String kaihyh, String pinz, String yuanshr,
			String zhangh, String hetsl, double gongfsl, long ches,
			String xianshr, String fapbh, String daibcc, String yansbh,
			String duifdd, String fukfs, double shulzjbz, double yanssl,
			double yingksl, double yingd, double kuid, double shulzjje,
			double jiessl, double jiesslcy, double yunfjsl, double buhsdj,
			double jiakje, double bukyqjk, double jiakhj, double jiaksl,
			double jiaksk, double jiasje, double tielyf, double tielzf,
			double kuangqyf, double kuangqzf, double kuangqsk, double kuangqjk,
			double bukyqyzf, double jiskc, double buhsyf, double yunfsl,
			double yunfsk, double yunzfhj, double hej, String meikhjdx,
			String yunzfhjdx, String daxhj, String beiz, String ranlbmjbr,
			Date ranlbmjbrq, double kuidjf, double jingz, Date jiesrq,
			String fahrq, String changcwjbr, Date changcwjbrq, Date ruzrq,
			String jieszxjbr, Date jieszxjbrq, String gongsrlbjbr,
			Date gongsrlbjbrq, double hetjg, long jieslx, double yuns,
			double koud_js, String yunsfs, String diancjsbs, long hetb_id,
			String yunju, long meikjsb_id, long yunfjsb_id, long jihkjb_id,
			double fengsjj, double jiajqdj, int jijlx,String Mjtokcalxsclfs,
			double kuidjfyf_je,double kuidjfzf_je,double chaokdl,String ChaodOrKuid,
			double yunfhsdj,double Yunfjsdj_mk,double Yunzfhj_mk,double Buhsyf_mk,
			double Yunfjsl_mk,long Chongdjsb_id,double weicdje,int jieslx_dt
			) {

		this.m_id = id;
		this.m_yid = yid;
		if (tianzdw == null) {
			this.m_Tianzdw = "";
		} else {
			this.m_Tianzdw = tianzdw;
		}
		this.m_jiesbh = jiesbh;
		this.m_Fahdw = fahdw;
		this.m_Meikdw = meikdw;
		this.m_Faz = faz;
		this.m_yunsfsb_id = yunsfsb_id;
		this.m_shoukdw = shoukdw;
		this.m_fahksrq = fahksrq;
		this.m_fahjzrq = fahjzrq;
		this.m_yansksrq = yansksrq;
		this.m_yansjzrq = yansjzrq;
		this.m_kaihyh = kaihyh;
		this.m_pinz = pinz;
		this.m_yuanshr = yuanshr;
		this.m_zhangh = zhangh;
		this.m_gongfsl = gongfsl;
		this.m_ches = ches;
		this.m_xianshr = xianshr;
		this.m_daibcc = daibcc;
		this.m_fapbh = fapbh;
		this.m_yansbh = yansbh;
/*
*huochaoyuan
*2009-10-22对字段duifdd进行初始化
*/		
		if (duifdd==null) {
			this.m_duifdd = "";
		} else {
			this.m_duifdd = duifdd;
		}
//		end
		this.m_fukfs = fukfs;
		this.m_shulzjbz = shulzjbz;
		this.m_yanssl = yanssl;
		this.m_yingk = yingksl;
		this.m_yingd = yingd;
		this.m_kuid = kuid;
		this.m_shulzjje = shulzjje;
		this.m_hetsl = hetsl;
		this.m_jiessl = jiessl;
		this.m_jiesslcy = jiesslcy;
		this.m_yunfjsl = yunfjsl;
		this.m_buhsdj = buhsdj;
		this.m_jiakje = jiakje;
		this.m_bukyqjk = bukyqjk;
		this.m_jiakhj = jiakhj;
		this.m_jiaksl = jiaksl;
		this.m_jiaksk = jiaksk;
		this.m_jiasje = jiasje;
		this.m_tielyf = tielyf;
		this.m_tielzf = tielzf;
		this.m_kuangqyf = kuangqyf;
		this.m_kuangqzf = kuangqzf;
		this.m_kuangqsk = kuangqsk;
		this.m_kuangqjk = kuangqjk;
		this.m_bukyqyzf = bukyqyzf;
		this.m_jiskc = jiskc;
		this.m_buhsyf = buhsyf;
		this.m_yunfsl = yunfsl;
		this.m_yunfsk = yunfsk;
		this.m_yunzfhj = yunzfhj;
		this.m_hej = hej;
		this.m_meikhjdx = meikhjdx;
		this.m_yunzfhjdx = yunzfhjdx;
		this.m_daxhj = daxhj;
		this.m_beiz = beiz;
		this.m_ranlbmjbr = ranlbmjbr;
		this.m_ranlbmjbrq = ranlbmjbrq;
		this.m_kuidjf = kuidjf;
		this.m_jingz = jingz;
		this.m_jiesrq = jiesrq;
		this.m_Fahrq = fahrq;
		this.m_changcwjbr = changcwjbr;
		this.m_changcwjbrq = changcwjbrq;
		this.m_ruzrq = ruzrq;
		this.m_jieszxjbr = jieszxjbr;
		this.m_jieszxjbrq = jieszxjbrq;
		this.m_gongsrlbjbr = gongsrlbjbr;
		this.m_gongsrlbjbrq = gongsrlbjbrq;
		this.m_hetjg = hetjg;
		this.m_jieslx = jieslx;
		this.m_yuns = yuns;
		this.m_koud_js = koud_js;
		this.m_yunsfs = yunsfs;
		this.m_diancjsbs = diancjsbs;
		this.m_hetb_id = hetb_id;
		this.m_yunju = yunju;
		this.m_Meikjsb_id = meikjsb_id;
		this.m_Yunfjsb_id = yunfjsb_id;
		this.m_jihkjb_id = jihkjb_id;
		this.m_fengsjj = fengsjj;
		this.m_jiajqdj = jiajqdj;
		this.m_jijlx = jijlx;
		this.m_Mjtokcalxsclfs = Mjtokcalxsclfs;
		this.m_kuidjfyf_je = kuidjfyf_je;
		this.m_kuidjfzf_je = kuidjfzf_je;
		this.m_chaokdl = chaokdl;
		this.m_ChaodOrKuid = ChaodOrKuid;
		this.m_yunfhsdj = yunfhsdj;
		this.m_Yunfjsdj_mk = Yunfjsdj_mk;
		this.m_Yunzfhj_mk = Yunzfhj_mk;
		this.m_Buhsyf_mk = Buhsyf_mk;
		this.m_Yunfjsl_mk = Yunfjsl_mk;
		this.m_Chongdjsb_id=Chongdjsb_id;
		this.m_weicdje=weicdje;
		this.m_jieslx_dt=jieslx_dt;
	}
	
	public Dcbalancebean(long id, long yid, String tianzdw, String jiesbh,
			String fahdw, String meikdw, String faz, long yunsfsb_id,
			String shoukdw, Date fahksrq, Date fahjzrq, Date yansksrq,
			Date yansjzrq, String kaihyh, String pinz, String yuanshr,
			String zhangh, String hetsl, double gongfsl, long ches,
			String xianshr, String fapbh, String daibcc, String yansbh,
			String duifdd, String fukfs, double shulzjbz, double yanssl,
			double yingksl, double yingd, double kuid, double shulzjje,
			double jiessl, double jiesslcy, double yunfjsl, double buhsdj,
			double jiakje, double bukyqjk, double jiakhj, double jiaksl,
			double jiaksk, double jiasje, double tielyf, double tielzf,
			double kuangqyf, double kuangqzf, double kuangqsk, double kuangqjk,
			double bukyqyzf, double jiskc, double buhsyf, double yunfsl,
			double yunfsk, double yunzfhj, double hej, String meikhjdx,
			String yunzfhjdx, String daxhj, String beiz, String ranlbmjbr,
			Date ranlbmjbrq, double kuidjf, double jingz, Date jiesrq,
			String fahrq, String changcwjbr, Date changcwjbrq, Date ruzrq,
			String jieszxjbr, Date jieszxjbrq, String gongsrlbjbr,
			Date gongsrlbjbrq, double hetjg, long jieslx, double yuns,
			double koud_js, String yunsfs, String diancjsbs, long hetb_id,
			String yunju, long meikjsb_id, long yunfjsb_id, long jihkjb_id,
			double fengsjj, double jiajqdj, int jijlx,String Mjtokcalxsclfs,
			double kuidjfyf_je,double kuidjfzf_je,double chaokdl,String ChaodOrKuid,
			double yunfhsdj,double Yunfjsdj_mk,double Yunzfhj_mk,double Buhsyf_mk,
			double Yunfjsl_mk,double Kouk_js, long meikxxb_id,long gongysb_id,long Chongdjsb_id,
			double weicdje,int jieslx_dt) {

		this.m_id = id;
		this.m_yid = yid;
		if (tianzdw == null) {
			this.m_Tianzdw = "";
		} else {
			this.m_Tianzdw = tianzdw;
		}
		this.m_gongysb_id=gongysb_id;
		this.m_meikxxb_id=meikxxb_id;
		this.m_jiesbh = jiesbh;
		this.m_Fahdw = fahdw;
		this.m_Meikdw = meikdw;
		this.m_Faz = faz;
		this.m_yunsfsb_id = yunsfsb_id;
		this.m_shoukdw = shoukdw;
		this.m_fahksrq = fahksrq;
		this.m_fahjzrq = fahjzrq;
		this.m_yansksrq = yansksrq;
		this.m_yansjzrq = yansjzrq;
		this.m_kaihyh = kaihyh;
		this.m_pinz = pinz;
		this.m_yuanshr = yuanshr;
		this.m_zhangh = zhangh;
		this.m_gongfsl = gongfsl;
		this.m_ches = ches;
		this.m_xianshr = xianshr;
		this.m_daibcc = daibcc;
		this.m_fapbh = fapbh;
		this.m_yansbh = yansbh;
/*
*huochaoyuan
*2009-10-22对字段duifdd进行初始化
*/		
		if (duifdd==null) {
			this.m_duifdd = "";
		} else {
			this.m_duifdd = duifdd;
		}
//		end
		this.m_fukfs = fukfs;
		this.m_shulzjbz = shulzjbz;
		this.m_yanssl = yanssl;
		this.m_yingk = yingksl;
		this.m_yingd = yingd;
		this.m_kuid = kuid;
		this.m_shulzjje = shulzjje;
		this.m_hetsl = hetsl;
		this.m_jiessl = jiessl;
		this.m_jiesslcy = jiesslcy;
		this.m_yunfjsl = yunfjsl;
		this.m_buhsdj = buhsdj;
		this.m_jiakje = jiakje;
		this.m_bukyqjk = bukyqjk;
		this.m_jiakhj = jiakhj;
		this.m_jiaksl = jiaksl;
		this.m_jiaksk = jiaksk;
		this.m_jiasje = jiasje;
		this.m_tielyf = tielyf;
		this.m_tielzf = tielzf;
		this.m_kuangqyf = kuangqyf;
		this.m_kuangqzf = kuangqzf;
		this.m_kuangqsk = kuangqsk;
		this.m_kuangqjk = kuangqjk;
		this.m_bukyqyzf = bukyqyzf;
		this.m_jiskc = jiskc;
		this.m_buhsyf = buhsyf;
		this.m_yunfsl = yunfsl;
		this.m_yunfsk = yunfsk;
		this.m_yunzfhj = yunzfhj;
		this.m_hej = hej;
		this.m_meikhjdx = meikhjdx;
		this.m_yunzfhjdx = yunzfhjdx;
		this.m_daxhj = daxhj;
		this.m_beiz = beiz;
		this.m_ranlbmjbr = ranlbmjbr;
		this.m_ranlbmjbrq = ranlbmjbrq;
		this.m_kuidjf = kuidjf;
		this.m_jingz = jingz;
		this.m_jiesrq = jiesrq;
		this.m_Fahrq = fahrq;
		this.m_changcwjbr = changcwjbr;
		this.m_changcwjbrq = changcwjbrq;
		this.m_ruzrq = ruzrq;
		this.m_jieszxjbr = jieszxjbr;
		this.m_jieszxjbrq = jieszxjbrq;
		this.m_gongsrlbjbr = gongsrlbjbr;
		this.m_gongsrlbjbrq = gongsrlbjbrq;
		this.m_hetjg = hetjg;
		this.m_jieslx = jieslx;
		this.m_yuns = yuns;
		this.m_koud_js = koud_js;
		this.m_yunsfs = yunsfs;
		this.m_diancjsbs = diancjsbs;
		this.m_hetb_id = hetb_id;
		this.m_yunju = yunju;
		this.m_Meikjsb_id = meikjsb_id;
		this.m_Yunfjsb_id = yunfjsb_id;
		this.m_jihkjb_id = jihkjb_id;
		this.m_fengsjj = fengsjj;
		this.m_jiajqdj = jiajqdj;
		this.m_jijlx = jijlx;
		this.m_Mjtokcalxsclfs = Mjtokcalxsclfs;
		this.m_kuidjfyf_je = kuidjfyf_je;
		this.m_kuidjfzf_je = kuidjfzf_je;
		this.m_chaokdl = chaokdl;
		this.m_ChaodOrKuid = ChaodOrKuid;
		this.m_yunfhsdj = yunfhsdj;
		this.m_Yunfjsdj_mk = Yunfjsdj_mk;
		this.m_Yunzfhj_mk = Yunzfhj_mk;
		this.m_Buhsyf_mk = Buhsyf_mk;
		this.m_Yunfjsl_mk = Yunfjsl_mk;
		this.m_Kouk_js = Kouk_js;
		this.m_Chongdjsb_id=Chongdjsb_id;
		this.m_weicdje=weicdje;
		this.m_jieslx_dt=jieslx_dt;
	}

	public String toString() {

		StringBuffer buffer = new StringBuffer("");

		buffer.append(m_id);
		buffer.append(',');
		buffer.append(m_jiesbh);
		buffer.append(',');
		buffer.append(m_jiesrq);
		buffer.append(',');
		buffer.append(m_fahksrq);
		buffer.append(',');
		buffer.append(m_fahjzrq);
		buffer.append(',');
		buffer.append(m_pinz);
		buffer.append(',');
		buffer.append(m_ches);
		buffer.append(',');
		buffer.append(m_daibcc);
		buffer.append(',');
		buffer.append(m_fapbh);
		buffer.append(',');
		buffer.append(m_yuanshr);
		buffer.append(',');
		buffer.append(m_yansksrq);
		buffer.append(',');
		buffer.append(m_yansjzrq);
		buffer.append(',');
		buffer.append(m_yansbh);
		buffer.append(',');
		buffer.append(m_shoukdw);
		buffer.append(',');
		buffer.append(m_kaihyh);
		buffer.append(',');
		buffer.append(m_zhangh);
		buffer.append(',');
		buffer.append(m_duifdd);
		buffer.append(',');
		buffer.append(m_fukfs);
		buffer.append(',');
		buffer.append(m_gongfsl);
		buffer.append(',');
		buffer.append(m_yanssl);
		buffer.append(',');
		buffer.append(m_yingk);
		buffer.append(',');
		buffer.append(m_jiessl);
		buffer.append(',');
		buffer.append(m_buhsdj);
		buffer.append(',');
		buffer.append(m_jiakje);
		buffer.append(',');
		buffer.append(m_bukyqjk);
		buffer.append(',');
		buffer.append(m_jiakhj);
		buffer.append(',');
		buffer.append(m_jiaksl);
		buffer.append(',');
		buffer.append(m_jiaksk);
		buffer.append(',');
		buffer.append(m_jiasje);
		buffer.append(',');
		buffer.append(m_tielyf);
		buffer.append(',');
		buffer.append(m_kuangqzf);
		buffer.append(',');
		buffer.append(m_bukyqyzf);
		buffer.append(',');
		buffer.append(m_jiskc);
		buffer.append(',');
		buffer.append(m_buhsyf);
		buffer.append(',');
		buffer.append(m_yunfsl);
		buffer.append(',');
		buffer.append(m_yunfsk);
		buffer.append(',');
		buffer.append(m_yunzfhj);
		buffer.append(',');
		buffer.append(m_hej);
		buffer.append(',');
		buffer.append(m_ranlbmjbr);
		buffer.append(',');
		buffer.append(m_ranlbmjbrq);
		buffer.append(',');
		buffer.append(m_changcwjbr);
		buffer.append(',');
		buffer.append(m_changcwjbrq);
		buffer.append(',');
		buffer.append(m_ruzrq);
		buffer.append(',');
		buffer.append(m_jieszxjbr);
		buffer.append(',');
		buffer.append(m_jieszxjbrq);
		buffer.append(',');
		buffer.append(m_gongsrlbjbr);
		buffer.append(',');
		buffer.append(m_gongsrlbjbrq);
		buffer.append(',');
		buffer.append(m_hetjg);
		buffer.append(',');
		buffer.append(m_jieslx);
		buffer.append(',');
		buffer.append(m_kuidjf);
		buffer.append(',');
		buffer.append(m_beiz);
		buffer.append(',');
		buffer.append(m_liubz);
		buffer.append(',');
		buffer.append(m_liuyk);
		buffer.append(',');
		buffer.append(m_qiyfhj);
		buffer.append(',');
		buffer.append(m_qiyfsk);
		buffer.append(',');
		buffer.append(m_qiyfjk);
		buffer.append(',');
		buffer.append(m_xianshr);
		buffer.append(',');
		buffer.append(m_jingz);
		buffer.append(',');
		buffer.append(m_yuns);
		buffer.append(',');
		buffer.append(m_yunsfs);
		buffer.append(',');
		buffer.append(m_Chongdjsb_id);
		buffer.append(',');
		buffer.append(m_weicdje);
		buffer.append(',');
		buffer.append(m_jieslx_dt);

		return buffer.toString();
	}

	



	
}
