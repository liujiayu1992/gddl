package com.zhiren.jt.jiesgl.changfhs;

import java.io.Serializable;

import java.util.Date;

public class Balancebean implements Serializable {

	private long m_id;// 煤款_id

	private long m_yid;// 运费_id

	private String m_Tianzdw;

	private String m_Fahdw;

	private String m_Faz;

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

	private String m_hetrl;//合同热量
	
	private String m_hetlf;//合同硫分
	
	private String m_hethff;//合同挥发分
	
	private String m_hethf;//合同灰分
	
	private String m_hetsf;//合同水分
	
	private double m_gongfrl;//

	private double m_gongfhf;//

	private double m_gongfhff;//

	private double m_gongfsf;//
	
	private double m_gongflf;//

	private double m_yansrl;//

	private double m_yanshf;//

	private double m_yanshff;//

	private double m_yanssf;//

	private double m_yanslf;//
	//结算字段
	private double m_jiesrl;//

	private double m_jieshf;//

	private double m_jieshff;//

	private double m_jiessf;//

	private double m_jieslf;//
	//
	private double m_yingkrl;//

	private double m_yingkhf;//

	private double m_yingkhff;//

	private double m_yingksf;//

	private double m_relzjbz;//

	private double m_huifzjbz;//

	private double m_huiffzjbz;//

	private double m_shuifzjbz;//

	private double m_relzjje;//

	private double m_huifzjje;//

	private double m_huiffzjje;//

	private double m_shuifzjje;//

	private double m_shulzjbz;//

	private double m_shulzjje;//

	private double m_liuzjbz;//

	private double m_liuzjje;//

	private double m_huirdzjbz;//

	private double m_huirdzjje;//

	private double m_huirdbz;//

	private double m_gongfhrd;//

	private double m_yanshrd;//
	
	private String m_hetsl;//

	private double m_gongfsl;//

	private double m_yanssl;//

	private double m_yingk;//

	private double m_jiessl;//

	private double m_buhsdj;//

	private double m_jiakje;//

	private double m_bukyqjk;//

	private double m_jiakhj;//

	private double m_jiaksl;//

	private double m_jiaksk;//

	private double m_jiasje;//

	private double m_tielyf;//

	private double m_zaf;//

	private double m_qitzf;//

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

	private double m_relsx;//

	private double m_relxx;//

	private double m_liusx;//

	private double m_liuxx;//

	private String m_beiz;//

	private Date m_Fahrq;

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

	private int m_butgys;

	private String m_yunsfs;

	private String m_meikjsdbh;
	
	private String m_diancjsbs;

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

	public Date getFahrq() {
		return m_Fahrq;
	}

	public void setFahrq(Date _Fahrq) {
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

	// public long getDiancxxb_id() {
	// return m_diancxxb_id;
	// }
	//
	// public void setDiancxxb_id(long diancxxb_id) {
	// this.m_diancxxb_id = diancxxb_id;
	// }
	//
	// public long getMeikxxb_id() {
	// return m_meikxxb_id;
	// }
	//
	// public void setMeikxxb_id(long meikxxb_id) {
	// this.m_meikxxb_id = meikxxb_id;
	// }

	// public long getFahdwb_id() {
	// return m_fahdwb_id;
	// }
	//
	// public void setFahdwb_id(long fahdwb_id) {
	// this.m_fahdwb_id = fahdwb_id;
	// }

	// public long getChezxxb_id() {
	// return m_chezxxb_id;
	// }
	//
	// public void setChezxxb_id(long chezxxb_id) {
	// this.m_chezxxb_id = chezxxb_id;
	// }
	//
	// public long getMeijb_id() {
	// return m_meijb_id;
	// }
	//
	// public void setMeijb_id(long meijb_id) {
	// this.m_meijb_id = meijb_id;
	// }

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

	public double getGongfrl() {
		return m_gongfrl;
	}

	public void setGongfrl(double gongfrl) {
		this.m_gongfrl = gongfrl;
	}

	//
	public double getGongfhf() {
		return m_gongfhf;
	}

	public void setGongfhf(double gongfhf) {
		this.m_gongfhf = gongfhf;
	}

	//	
	public double getGongfhff() {
		return m_gongfhff;
	}

	public void setGongfhff(double gongfhff) {
		this.m_gongfhff = gongfhff;
	}

	public double getGongfsf() {
		return m_gongfsf;
	}

	public void setGongfsf(double gongfsf) {
		this.m_gongfsf = gongfsf;
	}

	public double getYansrl() {
		return m_yansrl;
	}

	public void setYansrl(double yansrl) {
		this.m_yansrl = yansrl;
	}

	//	
	public double getYanshf() {
		return m_yanshf;
	}

	public void setYanshf(double yanshf) {
		this.m_yanshf = yanshf;
	}

	public double getYanshff() {
		return m_yanshff;
	}

	public void setYanshff(double yanshff) {
		this.m_yanshff = yanshff;
	}

	//	
	public double getYanssf() {
		return m_yanssf;
	}

	public void setYanssf(double yanssf) {
		this.m_yanssf = yanssf;
	}

	public double getGongflf() {
		return m_gongflf;
	}

	public void setGongflf(double gongflf) {
		this.m_gongflf = gongflf;
	}

	public double getYanslf() {
		return m_yanslf;
	}

	public void setYanslf(double yanslf) {
		this.m_yanslf = yanslf;
	}

	public double getYingkrl() {
		return m_yingkrl;
	}

	public void setYingkrl(double yingkrl) {
		this.m_yingkrl = yingkrl;
	}

	//
	public double getYingkhf() {
		return m_yingkhf;
	}

	public void setYingkhf(double yingkhf) {
		this.m_yingkhf = yingkhf;
	}

	public double getYingkhff() {
		return m_yingkhff;
	}

	public void setYingkhff(double yingkhff) {
		this.m_yingkhff = yingkhff;
	}

	//
	public double getYingksf() {
		return m_yingksf;
	}

	public void setYingksf(double yingksf) {
		this.m_yingksf = yingksf;
	}

	public double getRelzjbz() {
		return m_relzjbz;
	}

	public void setRelzjbz(double relzjbz) {
		this.m_relzjbz = relzjbz;
	}

	//
	public double getHuifzjbz() {
		return m_huifzjbz;
	}

	public void setHuifzjbz(double huifzjbz) {
		this.m_huifzjbz = huifzjbz;
	}

	public double getHuiffzjbz() {
		return m_huiffzjbz;
	}

	public void setHuiffzjbz(double huiffzjbz) {
		this.m_huiffzjbz = huiffzjbz;
	}

	public double getShuifzjbz() {
		return m_shuifzjbz;
	}

	public void setShuifzjbz(double shuifzjbz) {
		this.m_shuifzjbz = shuifzjbz;
	}

	//	

	public double getRelzjje() {
		return m_relzjje;
	}

	public void setRelzjje(double relzjje) {
		this.m_relzjje = relzjje;
	}

	//
	public double getHuifzjje() {
		return m_huifzjje;
	}

	public void setHuifzjje(double huifzjje) {
		this.m_huifzjje = huifzjje;
	}

	public double getHuiffzjje() {
		return m_huiffzjje;
	}

	public void setHuiffzjje(double huiffzjje) {
		this.m_huiffzjje = huiffzjje;
	}

	//	
	public double getShuifzjje() {
		return m_shuifzjje;
	}

	public void setShuifzjje(double shuifzjje) {
		this.m_shuifzjje = shuifzjje;
	}

	public double getShulzjbz() {
		return m_shulzjbz;
	}

	public void setShulzjbz(double shulzjbz) {
		this.m_shulzjbz = shulzjbz;
	}

	public double getShulzjje() {
		return m_shulzjje;
	}

	public void setShulzjje(double shulzjje) {
		this.m_shulzjje = shulzjje;
	}

	public double getLiuzjbz() {
		return m_liuzjbz;
	}

	public void setLiuzjbz(double liuzjbz) {
		this.m_liuzjbz = liuzjbz;
	}

	public double getLiuzjje() {
		return m_liuzjje;
	}

	public void setLiuzjje(double liuzjje) {
		this.m_liuzjje = liuzjje;
	}

	public double getHuirdzjbz() {
		return m_huirdzjbz;
	}

	public void setHuirdzjbz(double huirdzjbz) {
		this.m_huirdzjbz = huirdzjbz;
	}

	public double getHuirdzjje() {
		return m_huirdzjje;
	}

	public void setHuirdzjje(double huirdzjje) {
		this.m_huirdzjje = huirdzjje;
	}

	public double getHuirdbz() {
		return m_huirdbz;
	}

	public void setHuirdbz(double huirdbz) {
		this.m_huirdbz = huirdbz;
	}

	public double getGongfhrd() {
		return m_gongfhrd;
	}

	public void setGongfhrd(double gongfhrd) {
		this.m_gongfhrd = gongfhrd;
	}

	public double getYanshrd() {
		return m_yanshrd;
	}

	public void setYanshrd(double yanshrd) {
		this.m_yanshrd = yanshrd;
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

	public double getJiessl() {
		return m_jiessl;
	}

	public void setJiessl(double jiessl) {
		this.m_jiessl = jiessl;
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

	public double getZaf() {
		return m_zaf;
	}

	public void setZaf(double zaf) {
		this.m_zaf = zaf;
	}

	public double getQitzf() {
		return m_qitzf;
	}

	public void setQitzf(double qitzf) {
		this.m_qitzf = qitzf;
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

	public double getRelsx() {
		return m_relsx;
	}

	public void setRelsx(double relsx) {
		this.m_relsx = relsx;
	}

	public double getRelxx() {
		return m_relxx;
	}

	public void setRelxx(double relxx) {
		this.m_relxx = relxx;
	}

	public double getLiusx() {
		return m_liusx;
	}

	public void setLiusx(double liusx) {
		this.m_liusx = liusx;
	}

	public double getLiuxx() {
		return m_liuxx;
	}

	public void setLiuxx(double liuxx) {
		this.m_liuxx = liuxx;
	}

	public String getBeiz() {
		return m_beiz;
	}

	public void setBeiz(String beiz) {
		this.m_beiz = beiz;
	}
	
	public String getHetsl(){
		
		return m_hetsl;
	}
	
	public void setHetsl(String hetsl){
		
		this.m_hetsl=hetsl;
	}
	
	public String getHetrl(){
		
		return m_hetrl;
	}
	
	public void setHetrl(String hetrl){
		
		this.m_hetrl=hetrl;
	}
	
	public String getHetlf(){
		
		return m_hetlf;
	}
	
	public void setHetlf(String hetlf){
		
		this.m_hetlf=hetlf;
	}
	
	public String getHethff(){
		
		return m_hethff;
	}
	
	public void setHethff(String hethff){
		
		this.m_hethff=hethff;
	}

	public String getHethf(){
		
		return m_hethf;
	}
	
	public void setHethf(String hethf){
		
		this.m_hethf=hethf;
	}
	
	public String getHetsf(){
		
		return m_hetsf;
	}
	
	public void setHetsf(String hetsf){
		
		this.m_hetsf=hetsf;
	}
	
	public double getJiesrl(){
		
		return m_jiesrl;
	}
	
	public void setJiesrl(double jiesrl){
		
		this.m_jiesrl=jiesrl;
	}
	
	public double getJieslf(){
		
		return m_jieslf;
	}
	
	public void setJieslf(double jieslf){
		
		this.m_jieslf=jieslf;
	}
	
	public double getJieshf(){
		
		return m_jieshf;
	}
	
	public void setJieshf(double jieshf){
		
		this.m_jieshf=jieshf;
	}
	
	public double getJieshff(){
		
		return m_jieshff;
	}
	
	public void setJieshff(double jieshff){
		
		this.m_jieshff=jieshff;
	}
	
	public double getJiessf(){
		
		return m_jiessf;
	}
	
	public void setJiessf(double jiessf){
		
		this.m_jiessf=jiessf;
	}

	public Balancebean() {
	}

	public Balancebean(long id, long yid, String tianzdw, String jiesbh,
			String fahdw, String faz, String shoukdw, Date fahksrq,
			Date fahjzrq, Date yansksrq, Date yansjzrq, String kaihyh,
			String pinz, String yuanshr, String zhangh,String hetsl,double gongfsl,
			long ches, String xianshr, String fapbh, String daibcc,
			String yansbh, String duifdd, String fukfs, double shulzjbz,
			double yanssl, double yingksl, double shulzjje,String hetrl,double gongfrl,
			double yansrl,double jiesrl,double yingkrl, double relzjbz, double relzjje,String hethff,
			double gongfhff, double yanshff, double jieshff,double yingkhff, double huiffzjbz,
			double huiffzjje,String hethf,double gongfhf, double yanshf,double jieshf,double yingkhf,
			double huifzjbz, double huifzjje,String hetsf,double gongfsf, double yanssf,double jiessf,
			double yingksf, double shuifzjbz, double shuifzjje,String hetlf,double gongflf,
			double yanslf, double jieslf,double liuyk, double liubz, double liuzjbz,
			double liuzjje, double jiessl, double buhsdj, double jiakje,
			double bukyqjk, double jiakhj, double jiaksl, double jiaksk,
			double jiasje, double tielyf, double zaf, double bukyqyzf,
			double jiskc, double buhsyf, double yunfsl, double yunfsk,
			double yunzfhj, double hej, String meikhjdx, String yunzfhjdx,
			String daxhj, String beiz, String ranlbmjbr,
			Date ranlbmjbrq, double kuidjf, double jingz, Date jiesrq,
			Date fahrq, double huirdzjbz, double huirdzjje, double huirdbz,
			double gongfhrd, double yanshrd, double qitzf, String changcwjbr,
			Date changcwjbrq, Date ruzrq, String jieszxjbr, Date jieszxjbrq,
			String gongsrlbjbr, Date gongsrlbjbrq, double hetjg, long jieslx,
			double relsx, double relxx, double liusx, double liuxx,
			double yuns, String yunsfs,String diancjsbs) {
		// this.m_daohrq=daohrq;
		this.m_id = id;
		this.m_yid = yid;
		if (tianzdw == null) {
			this.m_Tianzdw = "";
		} else {
			this.m_Tianzdw = tianzdw;
		}
		this.m_jiesbh = jiesbh;
		this.m_Fahdw = fahdw;
		this.m_Faz = faz;
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
		this.m_duifdd = duifdd;
		this.m_fukfs = fukfs;
		this.m_shulzjbz = shulzjbz;
		this.m_yanssl = yanssl;
		this.m_yingk = yingksl;
		this.m_shulzjje = shulzjje;
		
		this.m_hetsl=hetsl;
		
		this.m_hetrl = hetrl;
		this.m_jiesrl=jiesrl;
		
		this.m_hetlf=hetlf;
		this.m_jieslf=jieslf;
		
		this.m_hethff=hethff;
		this.m_jieshff=jieshff;;
		
		this.m_hethf=hethf;
		this.m_jieshf=jieshf;
		
		this.m_hetsf=hetsf;
		this.m_jiessf=jiessf;
		
		this.m_gongfrl = gongfrl;
		this.m_yansrl = yansrl;
		this.m_yingkrl = yingkrl;
		this.m_relzjbz = relzjbz;
		this.m_relzjje = relzjje;
		this.m_gongfhff = gongfhff;
		this.m_yanshff = yanshff;
		this.m_yingkhff = yingkhff;
		this.m_huiffzjbz = huiffzjbz;
		this.m_huiffzjje = huiffzjje;
		this.m_gongfhf = gongfhf;
		this.m_yanshf = yanshf;
		this.m_yingkhf = yingkhf;
		this.m_huifzjbz = huifzjbz;
		this.m_huifzjje = huifzjje;
		this.m_gongfsf = gongfsf;
		this.m_yanssf = yanssf;
		this.m_yingksf = yingksf;
		this.m_shuifzjbz = shuifzjbz;
		this.m_shuifzjje = shuifzjje;
		this.m_gongflf = gongflf;
		this.m_yanslf = yanslf;
		this.m_liuyk = liuyk;
		this.m_liubz = liubz;
		this.m_liuzjbz = liuzjbz;
		this.m_liuzjje = liuzjje;
		this.m_jiessl = jiessl;
		this.m_buhsdj = buhsdj;
		this.m_jiakje = jiakje;
		this.m_bukyqjk = bukyqjk;
		this.m_jiakhj = jiakhj;
		this.m_jiaksl = jiaksl;
		this.m_jiaksk = jiaksk;
		this.m_jiasje = jiasje;
		this.m_tielyf = tielyf;
		this.m_zaf = zaf;
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
		this.m_huirdzjbz = huirdzjbz;
		this.m_huirdzjje = huirdzjje;
		this.m_huirdbz = huirdbz;
		this.m_gongfhrd = gongfhrd;
		this.m_yanshrd = yanshrd;
		this.m_qitzf = qitzf;
		this.m_changcwjbr = changcwjbr;
		this.m_changcwjbrq = changcwjbrq;
		this.m_ruzrq = ruzrq;
		this.m_jieszxjbr = jieszxjbr;
		this.m_jieszxjbrq = jieszxjbrq;
		this.m_gongsrlbjbr = gongsrlbjbr;
		this.m_gongsrlbjbrq = gongsrlbjbrq;
		this.m_hetjg = hetjg;
		this.m_jieslx = jieslx;
		this.m_relsx = relsx;
		this.m_relxx = relxx;
		this.m_liusx = liusx;
		this.m_liuxx = liuxx;
		this.m_yuns = yuns;
		this.m_yunsfs = yunsfs;
		this.m_diancjsbs=diancjsbs;
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
		buffer.append(m_gongfrl);
		buffer.append(',');
		buffer.append(m_yansrl);
		buffer.append(',');
		buffer.append(m_gongflf);
		buffer.append(',');
		buffer.append(m_yanslf);
		buffer.append(',');
		buffer.append(m_yingkrl);
		buffer.append(',');
		buffer.append(m_relzjbz);
		buffer.append(',');
		buffer.append(m_relzjje);
		buffer.append(',');
		buffer.append(m_shulzjbz);
		buffer.append(',');
		buffer.append(m_shulzjje);
		buffer.append(',');
		buffer.append(m_liuzjbz);
		buffer.append(',');
		buffer.append(m_liuzjje);
		buffer.append(',');
		buffer.append(m_huirdzjbz);
		buffer.append(',');
		buffer.append(m_huirdzjje);
		buffer.append(',');
		buffer.append(m_huirdbz);
		buffer.append(',');
		buffer.append(m_gongfhrd);
		buffer.append(',');
		buffer.append(m_yanshrd);
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
		buffer.append(m_zaf);
		buffer.append(',');
		buffer.append(m_qitzf);
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
		buffer.append(m_relsx);
		buffer.append(',');
		buffer.append(m_relxx);
		buffer.append(',');
		buffer.append(m_liusx);
		buffer.append(',');
		buffer.append(m_liuxx);
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

		return buffer.toString();
	}

}
