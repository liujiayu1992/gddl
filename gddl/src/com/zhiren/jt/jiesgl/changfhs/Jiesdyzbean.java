package com.zhiren.jt.jiesgl.changfhs;

import java.util.Date;

public class Jiesdyzbean {

	private static String Erro_Msg="";	//错误保存
	private double Jiessl=0;	//结算数量
	private double Qnetar=0;
	private double Std=0;
	private double Ad=0;
	private double Vdaf=0;
	private double Mt=0;
	private double Qgrad=0;
	private double Qbad=0;
	private double Had=0;
	private double Stad=0;
	private double Mad=0;
	private double Aar=0;
	private double Aad=0;
	private double Vad=0;
	private double T2=0;
	private long Ranlpzb_id=0;		//燃料品种表id
	private long Yunsfsb_id=0;		//运输方式表id
	private long Gongysb_id=0;		//供应商表id
	private long Hetb_id=0;			//合同表id
	private long Faz_id=0;			//发站
	private long Daoz_id=0;			//到站
	private Date Minfahrq=new Date();//发货日期	
	private String Gongs="";		//公式
	private double Hetml=0;			//合同煤量
	private String Kaihyh="";		//开户银行
	private String Zhangh="";		//帐号
	private double Hetmdj=0;		//合同煤单价
	private double Zuigmj=0;		//最高煤价
	private String Hetmdjdw="";		//合同煤基价单位
	private String Jiesfs="";		//结算方式
	private String Jiesxs="";		//结算形式
	private double Hetyj=0;			//合同运价
	private String Hetyjdw="";		//合同运价单位
	private String Hetjjfs="";		//合同计价方式
	private String SelIds="";		//结算验证数据源ID（jiesyzsjy）
	private boolean Hetjgpp_Flag=false;	//合同价格匹配（判断是否取到可用的合同价格）
	private String Yifzzb="";		//已赋值指标（从合同价格中取默认指标）
	private String User_custom_mlj_jiesgs="";		//用户自定义目录价结算公式
	private String User_custom_fmlj_jiesgs="";		//用户自定义非目录价结算公式

//	指标_begin
	//	合同_begin
		private String Shul_ht="";		//合同数量
		private String Qnetar_ht = ""; 	//合同验收低位发热量
		private String Std_ht = ""; 	//合同干燥基硫
		private String Mt_ht = ""; 		//合同验收全水份
		private String Mad_ht = ""; 	//合同一般分析煤样水分
		private String Aar_ht = ""; 	//合同收到基灰分
		private String Aad_ht = ""; 	//合同一般分析煤样灰分
		private String Ad_ht = ""; 		//合同干燥基灰分
		private String Vad_ht = ""; 	//合同一般分析煤样挥发分
		private String Vdaf_ht = ""; 	//合同干燥无灰基挥发分
		private String Stad_ht = ""; 	//合同一般分析煤样全硫
		private String Had_ht = ""; 	//合同一般分析煤样氢
		private String Qbad_ht = ""; 	//合同弹筒热值
		private String Qgrad_ht = ""; 	//合同干燥基高位热值
		private String T2_ht = ""; 		//合同灰熔点
	//	合同_end
		
	//	盈亏_begin
		private double Shul_yk=0;		//盈亏数量
		private double Qnetar_yk=0;
		private double Std_yk=0;
		private double Ad_yk=0;
		private double Vdaf_yk=0;
		private double Mt_yk=0;
		private double Qgrad_yk=0;
		private double Qbad_yk=0;
		private double Had_yk=0;
		private double Stad_yk=0;
		private double Mad_yk=0;
		private double Aar_yk=0;
		private double Aad_yk=0;
		private double Vad_yk=0;
		private double T2_yk=0;
	//  盈亏_end
		
	//	指标折单价_Begin
		private double Shul_zdj;
		private double Qnetar_zdj;
		private double Std_zdj;
		private double Ad_zdj;
		private double Vdaf_zdj;
		private double Mt_zdj;
		private double Qgrad_zdj;
		private double Qbad_zdj;
		private double Had_zdj;
		private double Stad_zdj;
		private double Mad_zdj;
		private double Aar_zdj;
		private double Aad_zdj;
		private double Vad_zdj;
		private double T2_zdj;
	//	指标折单价_end
		
	//	指标折金额_Begin
		private double Shul_zje;
		private double Qnetar_zje;
		private double Std_zje;
		private double Ad_zje;
		private double Vdaf_zje;
		private double Mt_zje;
		private double Qgrad_zje;
		private double Qbad_zje;
		private double Had_zje;
		private double Stad_zje;
		private double Mad_zje;
		private double Aar_zje;
		private double Aad_zje;
		private double Vad_zje;
		private double T2_zje;
	//	指标折金额_end
//	指标_end
		
	private double Hansmj = 0; 	// 含税煤单价
	private double Meiksl=0;	// 煤款税率
	private double Jiessl_sum=0;	//结算数量sum (单批次结算时，求单价用的，将所有列的结算数量相加)
	private double yingksl=0;		//盈亏数量
	private double Jiashj=0;		//价税合计
	private double Jiakhj=0;		//价款合计
	private double Jiaksk=0;		//价款税款
	private double Jine=0;			//金额
	private double Hej=0;			//煤款合计
	private double Shulzjbz=0;		//数量折价标准
	private double Buhsmj=0;		//不含税煤价
	private long Hetjgb_id=0;		//合同价格表id
	
	public String getUser_custom_mlj_jiesgs() {
		return User_custom_mlj_jiesgs;
	}

	public void setUser_custom_mlj_jiesgs(String user_custom_mlj_jiesgs) {
		User_custom_mlj_jiesgs = user_custom_mlj_jiesgs;
	}

	public String getUser_custom_fmlj_jiesgs() {
		return User_custom_fmlj_jiesgs;
	}

	public void setUser_custom_fmlj_jiesgs(String user_custom_fmlj_jiesgs) {
		User_custom_fmlj_jiesgs = user_custom_fmlj_jiesgs;
	}

	public String getErro_Msg() {
		return Erro_Msg;
	}

	public void setErro_Msg(String erro_Msg) {
		
		if(Erro_Msg.equals("")){
			
			Erro_Msg=erro_Msg;
		}else{
			
			Erro_Msg =Erro_Msg+","+erro_Msg;
		}
	}

	public double getJiessl() {
		return Jiessl;
	}

	public void setJiessl(double jiessl) {
		Jiessl = jiessl;
	}

	public double getQnetar() {
		return Qnetar;
	}

	public void setQnetar(double qnetar) {
		Qnetar = qnetar;
	}

	public double getStd() {
		return Std;
	}

	public void setStd(double std) {
		Std = std;
	}

	public double getAad() {
		return Aad;
	}

	public void setAad(double aad) {
		Aad = aad;
	}

	public double getAar() {
		return Aar;
	}

	public void setAar(double aar) {
		Aar = aar;
	}

	public double getAd() {
		return Ad;
	}

	public void setAd(double ad) {
		Ad = ad;
	}

	public double getHad() {
		return Had;
	}

	public void setHad(double had) {
		Had = had;
	}

	public double getMad() {
		return Mad;
	}

	public void setMad(double mad) {
		Mad = mad;
	}

	public double getMt() {
		return Mt;
	}

	public void setMt(double mt) {
		Mt = mt;
	}

	public double getQbad() {
		return Qbad;
	}

	public void setQbad(double qbad) {
		Qbad = qbad;
	}

	public double getQgrad() {
		return Qgrad;
	}

	public void setQgrad(double qgrad) {
		Qgrad = qgrad;
	}

	public double getStad() {
		return Stad;
	}

	public void setStad(double stad) {
		Stad = stad;
	}

	public double getT2() {
		return T2;
	}

	public void setT2(double t2) {
		T2 = t2;
	}

	public double getVad() {
		return Vad;
	}

	public void setVad(double vad) {
		Vad = vad;
	}

	public double getVdaf() {
		return Vdaf;
	}

	public void setVdaf(double vdaf) {
		Vdaf = vdaf;
	}

	public long getDaoz_id() {
		return Daoz_id;
	}

	public void setDaoz_id(long daoz_id) {
		Daoz_id = daoz_id;
	}

	public long getFaz_id() {
		return Faz_id;
	}

	public void setFaz_id(long faz_id) {
		Faz_id = faz_id;
	}

	public Date getMinfahrq() {
		return Minfahrq;
	}

	public void setMinfahrq(Date minfahrq) {
		Minfahrq = minfahrq;
	}

	public long getRanlpzb_id() {
		return Ranlpzb_id;
	}

	public void setRanlpzb_id(long ranlpzb_id) {
		Ranlpzb_id = ranlpzb_id;
	}

	public long getYunsfsb_id() {
		return Yunsfsb_id;
	}

	public void setYunsfsb_id(long yunsfsb_id) {
		Yunsfsb_id = yunsfsb_id;
	}

	public String getGongs() {
		return Gongs;
	}

	public void setGongs(String gongs) {
		Gongs = gongs;
	}

	public long getGongysb_id() {
		return Gongysb_id;
	}

	public void setGongysb_id(long gongysb_id) {
		Gongysb_id = gongysb_id;
	}

	public long getHetb_id() {
		return Hetb_id;
	}

	public void setHetb_id(long hetb_id) {
		Hetb_id = hetb_id;
	}

	public double getHetml() {
		return Hetml;
	}

	public void setHetml(double hetml) {
		Hetml = hetml;
	}

	public String getKaihyh() {
		return Kaihyh;
	}

	public void setKaihyh(String kaihyh) {
		Kaihyh = kaihyh;
	}

	public String getZhangh() {
		return Zhangh;
	}

	public void setZhangh(String zhangh) {
		Zhangh = zhangh;
	}

	public String getHetyjdw() {
		return Hetyjdw;
	}

	public void setHetyjdw(String hetyjdw) {
		Hetyjdw = hetyjdw;
	}

	public String getHetjjfs() {
		return Hetjjfs;
	}

	public void setHetjjfs(String hetjjfs) {
		Hetjjfs = hetjjfs;
	}

	public String getHetmdjdw() {
		return Hetmdjdw;
	}

	public void setHetmdjdw(String hetmdjdw) {
		Hetmdjdw = hetmdjdw;
	}

	public double getHetmdj() {
		return Hetmdj;
	}

	public void setHetmdj(double hetmj) {
		Hetmdj = hetmj;
	}

	public double getHetyj() {
		return Hetyj;
	}

	public void setHetyj(double hetyj) {
		Hetyj = hetyj;
	}

	public String getJiesfs() {
		return Jiesfs;
	}

	public void setJiesfs(String jiesfs) {
		Jiesfs = jiesfs;
	}

	public String getJiesxs() {
		return Jiesxs;
	}

	public void setJiesxs(String jiesxs) {
		Jiesxs = jiesxs;
	}

	public double getZuigmj() {
		return Zuigmj;
	}

	public void setZuigmj(double zuigmj) {
		Zuigmj = zuigmj;
	}

	public String getSelIds() {
		return SelIds;
	}

	public void setSelIds(String selIds) {
		SelIds = selIds;
	}

	public boolean getHetjgpp_Flag() {
		return Hetjgpp_Flag;
	}

	public void setHetjgpp_Flag(boolean hetjgpp_Flag) {
		Hetjgpp_Flag = hetjgpp_Flag;
	}

	public String getYifzzb() {
		return Yifzzb;
	}

	public void setYifzzb(String yifzzb) {
		Yifzzb = yifzzb;
	}

	public String getAad_ht() {
		return Aad_ht;
	}

	public void setAad_ht(String aad_ht) {
		Aad_ht = aad_ht;
	}

	public double getAad_yk() {
		return Aad_yk;
	}

	public void setAad_yk(double aad_yk) {
		Aad_yk = aad_yk;
	}

	public double getAad_zdj() {
		return Aad_zdj;
	}

	public void setAad_zdj(double aad_zdj) {
		Aad_zdj = aad_zdj;
	}

	public String getAar_ht() {
		return Aar_ht;
	}

	public void setAar_ht(String aar_ht) {
		Aar_ht = aar_ht;
	}

	public double getAar_yk() {
		return Aar_yk;
	}

	public void setAar_yk(double aar_yk) {
		Aar_yk = aar_yk;
	}

	public double getAar_zdj() {
		return Aar_zdj;
	}

	public void setAar_zdj(double aar_zdj) {
		Aar_zdj = aar_zdj;
	}

	public String getAd_ht() {
		return Ad_ht;
	}

	public void setAd_ht(String ad_ht) {
		Ad_ht = ad_ht;
	}

	public double getAd_yk() {
		return Ad_yk;
	}

	public void setAd_yk(double ad_yk) {
		Ad_yk = ad_yk;
	}

	public double getAd_zdj() {
		return Ad_zdj;
	}

	public void setAd_zdj(double ad_zdj) {
		Ad_zdj = ad_zdj;
	}

	public String getHad_ht() {
		return Had_ht;
	}

	public void setHad_ht(String had_ht) {
		Had_ht = had_ht;
	}

	public double getHad_yk() {
		return Had_yk;
	}

	public void setHad_yk(double had_yk) {
		Had_yk = had_yk;
	}

	public double getHad_zdj() {
		return Had_zdj;
	}

	public void setHad_zdj(double had_zdj) {
		Had_zdj = had_zdj;
	}

	public String getMad_ht() {
		return Mad_ht;
	}

	public void setMad_ht(String mad_ht) {
		Mad_ht = mad_ht;
	}

	public double getMad_yk() {
		return Mad_yk;
	}

	public void setMad_yk(double mad_yk) {
		Mad_yk = mad_yk;
	}

	public double getMad_zdj() {
		return Mad_zdj;
	}

	public void setMad_zdj(double mad_zdj) {
		Mad_zdj = mad_zdj;
	}

	public String getMt_ht() {
		return Mt_ht;
	}

	public void setMt_ht(String mt_ht) {
		Mt_ht = mt_ht;
	}

	public double getMt_yk() {
		return Mt_yk;
	}

	public void setMt_yk(double mt_yk) {
		Mt_yk = mt_yk;
	}

	public double getMt_zdj() {
		return Mt_zdj;
	}

	public void setMt_zdj(double mt_zdj) {
		Mt_zdj = mt_zdj;
	}

	public String getQbad_ht() {
		return Qbad_ht;
	}

	public void setQbad_ht(String qbad_ht) {
		Qbad_ht = qbad_ht;
	}

	public double getQbad_yk() {
		return Qbad_yk;
	}

	public void setQbad_yk(double qbad_yk) {
		Qbad_yk = qbad_yk;
	}

	public double getQbad_zdj() {
		return Qbad_zdj;
	}

	public void setQbad_zdj(double qbad_zdj) {
		Qbad_zdj = qbad_zdj;
	}

	public String getQgrad_ht() {
		return Qgrad_ht;
	}

	public void setQgrad_ht(String qgrad_ht) {
		Qgrad_ht = qgrad_ht;
	}

	public double getQgrad_yk() {
		return Qgrad_yk;
	}

	public void setQgrad_yk(double qgrad_yk) {
		Qgrad_yk = qgrad_yk;
	}

	public double getQgrad_zdj() {
		return Qgrad_zdj;
	}

	public void setQgrad_zdj(double qgrad_zdj) {
		Qgrad_zdj = qgrad_zdj;
	}

	public String getQnetar_ht() {
		return Qnetar_ht;
	}

	public void setQnetar_ht(String qnetar_ht) {
		Qnetar_ht = qnetar_ht;
	}

	public double getQnetar_yk() {
		return Qnetar_yk;
	}

	public void setQnetar_yk(double qnetar_yk) {
		Qnetar_yk = qnetar_yk;
	}

	public double getQnetar_zdj() {
		return Qnetar_zdj;
	}

	public void setQnetar_zdj(double qnetar_zdj) {
		Qnetar_zdj = qnetar_zdj;
	}

	public String getShul_ht() {
		return Shul_ht;
	}

	public void setShul_ht(String shul_ht) {
		Shul_ht = shul_ht;
	}

	public double getShul_yk() {
		return Shul_yk;
	}

	public void setShul_yk(double shul_yk) {
		Shul_yk = shul_yk;
	}

	public double getShul_zdj() {
		return Shul_zdj;
	}

	public void setShul_zdj(double shul_zdj) {
		Shul_zdj = shul_zdj;
	}

	public String getStad_ht() {
		return Stad_ht;
	}

	public void setStad_ht(String stad_ht) {
		Stad_ht = stad_ht;
	}

	public double getStad_yk() {
		return Stad_yk;
	}

	public void setStad_yk(double stad_yk) {
		Stad_yk = stad_yk;
	}

	public double getStad_zdj() {
		return Stad_zdj;
	}

	public void setStad_zdj(double stad_zdj) {
		Stad_zdj = stad_zdj;
	}

	public String getStd_ht() {
		return Std_ht;
	}

	public void setStd_ht(String std_ht) {
		Std_ht = std_ht;
	}

	public double getStd_yk() {
		return Std_yk;
	}

	public void setStd_yk(double std_yk) {
		Std_yk = std_yk;
	}

	public double getStd_zdj() {
		return Std_zdj;
	}

	public void setStd_zdj(double std_zdj) {
		Std_zdj = std_zdj;
	}

	public String getT2_ht() {
		return T2_ht;
	}

	public void setT2_ht(String t2_ht) {
		T2_ht = t2_ht;
	}

	public double getT2_yk() {
		return T2_yk;
	}

	public void setT2_yk(double t2_yk) {
		T2_yk = t2_yk;
	}

	public double getT2_zdj() {
		return T2_zdj;
	}

	public void setT2_zdj(double t2_zdj) {
		T2_zdj = t2_zdj;
	}

	public String getVad_ht() {
		return Vad_ht;
	}

	public void setVad_ht(String vad_ht) {
		Vad_ht = vad_ht;
	}

	public double getVad_yk() {
		return Vad_yk;
	}

	public void setVad_yk(double vad_yk) {
		Vad_yk = vad_yk;
	}

	public double getVad_zdj() {
		return Vad_zdj;
	}

	public void setVad_zdj(double vad_zdj) {
		Vad_zdj = vad_zdj;
	}

	public String getVdaf_ht() {
		return Vdaf_ht;
	}

	public void setVdaf_ht(String vdaf_ht) {
		Vdaf_ht = vdaf_ht;
	}

	public double getVdaf_yk() {
		return Vdaf_yk;
	}

	public void setVdaf_yk(double vdaf_yk) {
		Vdaf_yk = vdaf_yk;
	}

	public double getVdaf_zdj() {
		return Vdaf_zdj;
	}

	public void setVdaf_zdj(double vdaf_zdj) {
		Vdaf_zdj = vdaf_zdj;
	}

	public double getHansmj() {
		return Hansmj;
	}

	public void setHansmj(double hansmj) {
		Hansmj = hansmj;
	}

	public double getMeiksl() {
		return Meiksl;
	}

	public void setMeiksl(double meiksl) {
		Meiksl = meiksl;
	}

	public double getJiessl_sum() {
		return Jiessl_sum;
	}

	public void setJiessl_sum(double jiessl_sum) {
		Jiessl_sum = jiessl_sum;
	}

	public double getYingksl() {
		return yingksl;
	}

	public void setYingksl(double yingksl) {
		this.yingksl = yingksl;
	}

	public double getJiakhj() {
		return Jiakhj;
	}

	public void setJiakhj(double jiakhj) {
		Jiakhj = jiakhj;
	}

	public double getJiaksk() {
		return Jiaksk;
	}

	public void setJiaksk(double jiaksk) {
		Jiaksk = jiaksk;
	}

	public double getJiashj() {
		return Jiashj;
	}

	public void setJiashj(double jiashj) {
		Jiashj = jiashj;
	}

	public double getHej() {
		return Hej;
	}

	public void setHej(double hej) {
		Hej = hej;
	}

	public double getJine() {
		return Jine;
	}

	public void setJine(double jine) {
		Jine = jine;
	}

	public double getAad_zje() {
		return Aad_zje;
	}

	public void setAad_zje(double aad_zje) {
		Aad_zje = aad_zje;
	}

	public double getAar_zje() {
		return Aar_zje;
	}

	public void setAar_zje(double aar_zje) {
		Aar_zje = aar_zje;
	}

	public double getAd_zje() {
		return Ad_zje;
	}

	public void setAd_zje(double ad_zje) {
		Ad_zje = ad_zje;
	}

	public double getHad_zje() {
		return Had_zje;
	}

	public void setHad_zje(double had_zje) {
		Had_zje = had_zje;
	}

	public double getMad_zje() {
		return Mad_zje;
	}

	public void setMad_zje(double mad_zje) {
		Mad_zje = mad_zje;
	}

	public double getMt_zje() {
		return Mt_zje;
	}

	public void setMt_zje(double mt_zje) {
		Mt_zje = mt_zje;
	}

	public double getQbad_zje() {
		return Qbad_zje;
	}

	public void setQbad_zje(double qbad_zje) {
		Qbad_zje = qbad_zje;
	}

	public double getQgrad_zje() {
		return Qgrad_zje;
	}

	public void setQgrad_zje(double qgrad_zje) {
		Qgrad_zje = qgrad_zje;
	}

	public double getQnetar_zje() {
		return Qnetar_zje;
	}

	public void setQnetar_zje(double qnetar_zje) {
		Qnetar_zje = qnetar_zje;
	}

	public double getShul_zje() {
		return Shul_zje;
	}

	public void setShul_zje(double shul_zje) {
		Shul_zje = shul_zje;
	}

	public double getStad_zje() {
		return Stad_zje;
	}

	public void setStad_zje(double stad_zje) {
		Stad_zje = stad_zje;
	}

	public double getStd_zje() {
		return Std_zje;
	}

	public void setStd_zje(double std_zje) {
		Std_zje = std_zje;
	}

	public double getT2_zje() {
		return T2_zje;
	}

	public void setT2_zje(double t2_zje) {
		T2_zje = t2_zje;
	}

	public double getVad_zje() {
		return Vad_zje;
	}

	public void setVad_zje(double vad_zje) {
		Vad_zje = vad_zje;
	}

	public double getVdaf_zje() {
		return Vdaf_zje;
	}

	public void setVdaf_zje(double vdaf_zje) {
		Vdaf_zje = vdaf_zje;
	}

	public double getBuhsmj() {
		return Buhsmj;
	}

	public void setBuhsmj(double buhsmj) {
		Buhsmj = buhsmj;
	}

	public double getShulzjbz() {
		return Shulzjbz;
	}

	public void setShulzjbz(double shulzjbz) {
		Shulzjbz = shulzjbz;
	}

	public long getHetjgb_id() {
		return Hetjgb_id;
	}

	public void setHetjgb_id(long hetjgb_id) {
		Hetjgb_id = hetjgb_id;
	}
	
}
