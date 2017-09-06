package com.zhiren.shihs.hesgl;

import java.util.Date;

import javax.servlet.http.HttpSession;

public class Balances_variable_Shih {

	private String _Tianzdw = ""; // 填制单位

	private String _Yunsfs = "";

	private String _Daibcc = "";	//代表车号
	
	private boolean _Hetjgpp_Flag=false;	//合同价格匹配（判断是否取到可用的合同价格）
	
	private String _Yifzzb="";		//已赋值指标
	
	private String _User_custom_mlj_jiesgs="";		//用户自定义目录价结算公式
	
	private String _User_custom_fmlj_jiesgs="";	//用户自定义非目录价结算公式
	
	private String _Yikj_yunfyymk="否";				//一口价（运费源于煤款）
	
	private String _Yikj_meikyyyf="否";				//一口价（煤款源于运费）
	
	private long _Hetjgb_id=0;						//合同价格表id
	
	private double _Yunfjsdj=0;						//运费结算单价
	
	private int _Hansdjblxsw=2;						//含税单价保留小数位
	
	private int _yunfhsdjblxsw=2;					//运费含税单价保留小数位
	
	private long _Diancxxb_id=0;					//电厂信息表id
	
	private HttpSession _Session=null;				//当前的session
	
	private int _Xuh=0;								//序号（单批次结算用一个价格一个序号）
	
	private long _Meikjsb_id=0;						//煤款结算表_id
	
	private long _Yunfjsb_id=0;						//运费结算表_id
	
	private long _Jihkjb_id=0;						//计划口径表_id
	
	
	public long getJihkjb_id(){
		
		return _Jihkjb_id;
	}
	
	public void setJihkjb_id(long value){
		
		_Jihkjb_id=value;
	}
	
	public int getXuh(){
		
		return _Xuh;
	}
	
	public void setXuh(int value){
		
		_Xuh=value;
	}
	
	public HttpSession getSession(){
		
		return _Session;
	}
	
	public void setSession(HttpSession value){
		
		_Session=value;
	}
	
	public int getHansdjblxsw(){
		
		return _Hansdjblxsw;
	}
	
	public void setHansdjblxsw(int value){
		
		_Hansdjblxsw=value;
	}
	
	public int getYunfhsdjblxsw(){
		
		return _yunfhsdjblxsw;
	}
	
	public void setYunfhsdjblxsw(int value){
		
		_yunfhsdjblxsw=value;
	}
	
	public double getYunfjsdj(){				
		
		return _Yunfjsdj;
	}
	
	public void setYunfjsdj(double yunfjsdj){
		
		this._Yunfjsdj=yunfjsdj;
	}
	
	public long getHetjgb_id(){
		
		return _Hetjgb_id;
	}
	
	public void setHetjgb_id(long value){
		
		this._Hetjgb_id=value;
	}
	
	public long getDiancxxb_id(){
		
		return _Diancxxb_id;
	}
	
	public void setDiancxxb_id(long value){
		
		this._Diancxxb_id=value;
	}
	
	public String getYikj_yunfyymk(){
		
		return _Yikj_yunfyymk;
	}
	
	public void setYikj_yunfyymk(String value){
		
		this._Yikj_yunfyymk=value;
	}
	
	public String getYikj_meikyyyf(){
		
		return _Yikj_meikyyyf;
	}
	
	public void setYikj_meikyyyf(String value){
		
		this._Yikj_meikyyyf=value;
	}
	
	public String getUser_custom_mlj_jiesgs(){
		
		return _User_custom_mlj_jiesgs;
	}
	
	public void setUser_custom_mlj_jiesgs(String value){
		
		this._User_custom_mlj_jiesgs=value;
	}
	
	public String getUser_custom_fmlj_jiesgs(){
		
		return _User_custom_fmlj_jiesgs;
	}
	
	public void setUser_custom_fmlj_jiesgs(String value){
		
		this._User_custom_fmlj_jiesgs=value;
	}
	
	public boolean getHetjgpp_Flag(){
		
		return _Hetjgpp_Flag;
	}
	
	public void setHetjgpp_Flag(boolean value){
		
		_Hetjgpp_Flag=value;
	}
	
	public String getYifzzb(){
		
		return this._Yifzzb;
	}
	
	public void setYifzzb(String value){
		
		this._Yifzzb=value;
	}
	
	public String getDaibcc(){
		
		return this._Daibcc;
	}
	
	public void setDaibcc(String _value){
		
		_Daibcc=_value;
	}
	
	public String getTianzdw() {
		return _Tianzdw;
	}

	public void setTianzdw(String _value) {
		_Tianzdw = _value;
	}

	public String getYunsfs() {
		return _Yunsfs;
	}

	public void setYunsfs(String _value) {
		_Yunsfs = _value;
	}

	private long _Yunsfsb_id;

	public long getYunsfsb_id() {
		return _Yunsfsb_id;
	}

	public void setYunsfsb_id(long _value) {
		_Yunsfsb_id = _value;
	}

	private String _Jiesbh = ""; // 结算单编号

	public String getJiesbh() {
		return _Jiesbh;
	}

	public void setJiesbh(String _value) {
		_Jiesbh = _value;
	}

	private String _Fahdw = ""; // 发货单位

	public String getFahdw() {
		return _Fahdw;
	}

	public void setFahdw(String _value) {
		_Fahdw = _value;
	}

	private String _Meikdw = ""; // 煤矿单位

	public String getMeikdw() {
		return _Meikdw;
	}

	public void setMeikdw(String _value) {
		_Meikdw = _value;
	}

	private String _Fahrq = null; // 发货日期

	public String getFahrq() {
		return _Fahrq;
	}

	public void setFahrq(String _value) {
		_Fahrq = _value;
	}

	private double _yanshf = 0;

	public double getYanshf() {
		return _yanshf;
	}

	public void setYanshf(double _value) {
		_yanshf = _value;
	}

	private String _Yuanshr = ""; // 原收获人

	public String getYuanshr() {
		return _Yuanshr;
	}

	public void setYuanshr(String _value) {
		_Yuanshr = _value;
	}

	private String _Xianshr = ""; // 现收货人

	public String getXianshr() {
		return _Xianshr;
	}

	public void setXianshr(String _value) {
		_Xianshr = _value;
	}

	private String _daohrq = null; // 验收日期

	public String getDaohrq() {
		return _daohrq;
	}

	public void setDaohrq(String _value) {
		_daohrq = _value;
	}

	private String _Jiesrq = null; // 结算日期

	public String getJiesrq() {
		return _Jiesrq;
	}

	public void setJiesrq(String _value) {
		_Jiesrq = _value;
	}

	private String _Faz = ""; // 发站

	public String getFaz() {
		return _Faz;
	}

	public void setFaz(String _value) {
		_Faz = _value;
	}

	private long _Faz_Id; // 发站

	public long getFaz_Id() {
		return _Faz_Id;
	}

	public void setFaz_Id(long _value) {
		_Faz_Id = _value;
	}

	private long _Daoz_Id; // 到站id

	public long getDaoz_Id() {
		return _Daoz_Id;
	}

	public void setDaoz_Id(long _value) {
		_Daoz_Id = _value;
	}

	private Date _Fahksrq = null; // 发货开始日期

	public Date getFahksrq() {
		return _Fahksrq;
	}

	public void setFahksrq(Date _value) {
		_Fahksrq = _value;
	}

	private Date _Fahjzrq = null; // 发货截止日期

	public Date getFahjzrq() {
		return _Fahjzrq;
	}

	public void setFahjzrq(Date _value) {
		_Fahjzrq = _value;
	}

	private Date _Yansksrq = null; // 验收开始日期

	public Date getYansksrq() {
		return _Yansksrq;
	}

	public void setYansksrq(Date _value) {
		_Yansksrq = _value;
	}

	private Date _Yansjsrq = null; // 验收结束日期

	public Date getYansjsrq() {
		return _Yansjsrq;
	}

	public void setYansjsrq(Date _value) {
		_Yansjsrq = _value;
	}

	private String _Yansbh = ""; // 验收编号

	public String getYansbh() {
		return _Yansbh;
	}

	public void setYansbh(String _value) {
		_Yansbh = _value;
	}

	private String _Fapbh = "";

	public String getFapbh() {
		return _Fapbh;
	}

	public void setFapbh(String _value) {
		_Fapbh = _value;
	}

	private String _Shoukdw = "";

	public String getShoukdw() {
		return _Shoukdw;
	}

	public void setShoukdw(String _value) {
		_Shoukdw = _value;
	}

	private String _Kaihyh = "";

	public String getKaihyh() {
		return _Kaihyh;
	}

	public void setKaihyh(String _value) {
		_Kaihyh = _value;
	}

	private String _Zhangh = "";

	public String getZhangh() {
		return _Zhangh;
	}

	public void setZhangh(String _value) {
		_Zhangh = _value;
	}
	
	private String _Shuljs="";	// 数量结算（按矿方数量结算、还是按厂方数量结算）
	
	public String getShuljs(){
		
		return _Shuljs;
	}
	
	public void setShuljs(String _value){
		
		_Shuljs=_value;
	}
	
	private String _Zhiljs="";	// 质量结算（按矿方质量结算、还是按厂方数量结算）
	
	public String getZhiljs(){
		
		return _Zhiljs;
	}
	
	public void setZhiljs(String _value){
		
		_Zhiljs=_value;
	}

	private String _Ranlpz = ""; // 品种

	public String getRanlpz() {
		return _Ranlpz;
	}

	public void setRanlpz(String _value) {
		_Ranlpz = _value;
	}

	public String _Chec = ""; // 代表车号

	public String getChec() {
		return _Chec;
	}

	public void setChec(String _value) {
		_Chec = _value;
	}

	private String _Hejdx = ""; // 合计大写

	public String getHejdx() {
		return _Hejdx;
	}

	public void setHejdx(String _value) {
		_Hejdx = _value;
	}

	private String _Beiz = ""; // 备注

	public String getBeiz() {
		return _Beiz;
	}

	public void setBeiz(String _value) {
		_Beiz = _value;
	}

	private long _Ches = 0;

	public long getChes() {
		return _Ches;
	}

	public void setChes(long _value) {
		_Ches = _value;
	}

	private long _Hetb_Id = 0; // 合同表id

	public long getHetb_Id() {
		return _Hetb_Id;
	}

	public void setHetb_Id(long _value) {
		_Hetb_Id = _value;
	}

	public long _Gongysb_id = 0; // 供应商id

	public long getGongysb_Id() {
		return _Gongysb_id;
	}

	public void setGongysb_Id(long _value) {
		_Gongysb_id = _value;
	}

	public long _Ranlpzb_Id = 0; // 燃料品种表id

	public long getRanlpzb_Id() {
		return _Ranlpzb_Id;
	}

	public void setRanlpzb_Id(long _value) {
		_Ranlpzb_Id = _value;
	}

	public long _KuangfzlbId = 0; // 燃料品种表id

	public long getKuangfzlbId() {
		return _KuangfzlbId;
	}

	public void setKuangfzlbId(long _value) {
		_KuangfzlbId = _value;
	}

//	2008-12-9	zsj 加
	private String _Jiesslblxs="0";	//结算数量保留小数为(煤款、运费公用)
	public String getJiesslblxs(){
		
		return _Jiesslblxs;
	}
	
	public void setJiesslblxs(String value){
		
		_Jiesslblxs=value;
	}

	private double _Meiksl = 0.13; // 价款税率

	public double getMeiksl() {
		return _Meiksl;
	}

	public void setMeiksl(double _value) {
		_Meiksl = _value;
	}

	private double _Gongfsl = 0; // 供方数量

	public double getGongfsl() {
		return _Gongfsl;
	}

	public void setGongfsl(double _value) {
		_Gongfsl = _value;
	}

	private double _gongfl = 0; // 供方硫

	public double getGongfl() {
		return _gongfl;
	}

	public void setGongfl(double _value) {
		_gongfl = _value;
	}

	private double _Yanssl = 0; // 厂方验收数量

	public double getYanssl() {
		return _Yanssl;
	}

	public void setYanssl(double _value) {
		_Yanssl = _value;
	}
	
	private double _Yuns = 0; // 运损

	public double getYuns() {
		return _Yuns;
	}

	public void setYuns(double _value) {
		_Yuns = _value;
	}
	
	private double _Jiesslcy = 0; // 结算数量差异

	public double getJiesslcy() {
		return _Jiesslcy;
	}

	public void setJiesslcy(double _value) {
		_Jiesslcy = _value;
	}

	private double _Jiessl = 0; // 结算数量

	public double getJiessl() {
		return _Jiessl;
	}

	public void setJiessl(double _value) {
		_Jiessl = _value;
	}
	
	private double _Yingksl = 0; // 盈亏数量

	public double getYingksl() {
		return _Yingksl;
	}

	public void setYingksl(double _value) {
		_Yingksl = _value;
	}
	
	private double _Yunssl = 0; // 运损

	public double getYunssl() {
		return _Yunssl;
	}

	public void setYunssl(double _value) {
		_Yunssl = _value;
	}
	
	private double _Koud_js = 0; // 结算扣吨

	public double getKoud_js() {
		return _Koud_js;
	}

	public void setKoud_js(double _value) {
		_Koud_js = _value;
	}
	
	private double _Koud = 0; // 扣吨

	public double getKoud() {
		return _Koud;
	}

	public void setKoud(double _value) {
		_Koud = _value;
	}

	private double _Jingz = 0; // 净重

	public double getJingz() {
		return _Jingz;
	}

	public void setJingz(double _value) {
		_Jingz = _value;
	}

	// 合同指标_Begin

	private double _Hetml = 0;

	public double getHetml() {
		return _Hetml;
	}

	public void setHetml(double _value) {
		_Hetml = _value;
	}

	private double _Hetmdj = 0; // 合同煤单价

	public double getHetmdj() {
		return _Hetmdj;
	}

	public void setHetmdj(double _value) {
		_Hetmdj = _value;
	}
	
	private double _Tielzf = 0; // 铁路杂费(铁路大票上不抵税的费用)

	public double getTielzf() {
		return _Tielzf;
	}

	public void setTielzf(double _value) {
		_Tielzf = _value;
	}
	
	private double _Kuangqsk = 0; // 矿区税款(矿方大票上不抵税的费用)

	public double getKuangqsk() {
		return _Kuangqsk;
	}

	public void setKuangqsk(double _value) {
		_Kuangqsk = _value;
	}
	
	private double _Kuangqjk = 0; // 矿区价款(矿方大票上不抵税的费用)

	public double getKuangqjk() {
		return _Kuangqjk;
	}

	public void setKuangqjk(double _value) {
		_Kuangqjk = _value;
	}
	
	private double _Kuangqzf = 0; // 矿区杂费(矿方大票上不抵税的费用)

	public double getKuangqzf() {
		return _Kuangqzf;
	}

	public void setKuangqzf(double _value) {
		_Kuangqzf = _value;
	}
	
	private double _Tielyf = 0; // 结算运费(铁路大票上的可抵税费用)

	public double getTielyf() {
		return _Tielyf;
	}

	public void setTielyf(double _value) {
		_Tielyf = _value;
	}
	
	private double _Kuangqyf = 0; // 矿区运费(铁路大票上的可抵税费用)

	public double getKuangqyf() {
		return _Kuangqyf;
	}

	public void setKuangqyf(double _value) {
		_Kuangqyf = _value;
	}

	private double _Zuigmj = 0; // 最高煤价

	public double getZuigmj() {
		return _Zuigmj;
	}

	public void setZuigmj(double _value) {
		_Zuigmj = _value;
	}
	
	private double _Zuidmj = 0;	//最低煤价
	
	public double getZuidmj(){
		return _Zuidmj;
	}
	
	public void setZuidmj(double _value){
		_Zuidmj=_value;
	}

	private String _Hetmdjdw = ""; // 合同煤单价单位

	public String getHetmdjdw() {
		return _Hetmdjdw;
	}

	public void setHetmdjdw(String _value) {
		_Hetmdjdw = _value;
	}

	private double _Hetyj = 0; // 合同运价单价（一般汽车煤使用）

	public double getHetyj() {
		return _Hetyj;
	}

	public void setHetyj(double _value) {
		_Hetyj = _value;
	}

	private String _Hetyjdw = ""; // 合同运价单位

	public String getHetyjdw() {
		return _Hetyjdw;
	}

	public void setHetyjdw(String _value) {
		_Hetyjdw = _value;
	}

//	合同结算指标_Begin

	private String _Shul_ht = ""; 	// 合同供煤数量，数量折单价用

	public String getShul_ht() {
		return _Shul_ht;
	}

	public void setShul_ht(String _value) {
		_Shul_ht = _value;
	}
	
	private String _CaO_ht = ""; // 合同CaO

	public String getCaO_ht() {
		return _CaO_ht;
	}

	public void setCaO_ht(String _value) {
		_CaO_ht = _value;
	}

	private String _MgO_ht = ""; // 合同MgO

	public String getMgO_ht() {
		return _MgO_ht;
	}

	public void setMgO_ht(String _value) {
		_MgO_ht = _value;
	}

	private String _Xid_ht = ""; // 合同细度

	public String getXid_ht() {
		return _Xid_ht;
	}

	public void setXid_ht(String _value) {
		_Xid_ht = _value;
	}
	
	// 合同结算指标_end

	// 厂方指标_Begin
	private double _CaO_cf = 0; 	// 厂方CaO

	public double getCaO_cf() {
		return _CaO_cf;
	}

	public void setCaO_cf(double _value) {
		_CaO_cf = _value;
	}

	private double _MgO_cf = 0; 	// 厂方MgO

	public double getMgO_cf() {
		return _MgO_cf;
	}

	public void setMgO_cf(double _value) {
		_MgO_cf = _value;
	}

	private double _Xid_cf = 0; 	// 厂方细度

	public double getXid_cf() {
		return _Xid_cf;
	}

	public void setXid_cf(double _value) {
		_Xid_cf = _value;
	}
	
	// 厂方结算指标_end

	// 矿方指标_Begin
	private double _CaO_kf = 0; 		// 矿方CaO

	public double getCaO_kf() {
		return _CaO_kf;
	}

	public void setCaO_kf(double _value) {
		_CaO_kf = _value;
	}

	private double _MgO_kf = 0; 		// 矿方MgO

	public double getMgO_kf() {
		return _MgO_kf;
	}

	public void setMgO_kf(double _value) {
		_MgO_kf = _value;
	}

	private double _Xid_kf = 0; 		// 矿方细度

	public double getXid_kf() {
		return _Xid_kf;
	}

	public void setXid_kf(double _value) {
		_Xid_kf = _value;
	}

	// 矿方结算指标_end

	// 结算指标_Begin
	private double _CaO_js = 0; 		// 结算CaO

	public double getCaO_js() {
		return _CaO_js;
	}

	public void setCaO_js(double _value) {
		_CaO_js = _value;
	}

	private double _MgO_js = 0; 		// 结算MgO

	public double getMgO_js() {
		return _MgO_js;
	}

	public void setMgO_js(double _value) {
		_MgO_js = _value;
	}

	private double _Xid_js = 0; 		// 结算细度

	public double getXid_js() {
		return _Xid_js;
	}

	public void setXid_js(double _value) {
		_Xid_js = _value;
	}

	// 结算结算指标_end


	private double _Pinzbj = 0; 			// 燃料品种比价

	public double getPinzbj() {
		return _Pinzbj;
	}

	public void setPinzbj(double _value) {
		_Pinzbj = _value;
	}

	private double _Zhengcxjj = 0; 		// 政策性加价

	public double getZhengcxjj() {
		return _Zhengcxjj;
	}

	public void setZhengcxjj(double _value) {
		_Zhengcxjj = _value;
	}

	private double _Buhsmj = 0; 	// 不含税煤价

	public double getBuhsmj() {
		return _Buhsmj;
	}

	public void setBuhsmj(double _value) {
		_Buhsmj = _value;
	}

	private double _Hansmj = 0; 	// 含税煤单价

	public double getHansmj() {
		return _Hansmj;
	}

	public void setHansmj(double _value) {
		_Hansmj = _value;
	}

	private double _Shulzjbz = 0; // 数量折价标准

	public double getShulzjbz() {
		return _Shulzjbz;
	}

	public void setShulzjbz(double _value) {
		_Shulzjbz = _value;
	}

	private double _Shulzjje = 0; // 数量折价金额=含税煤价*盈亏数量

	public double getShulzjje() {
		return _Shulzjje;
	}

	public void setShulzjje(double _value) {
		_Shulzjje = _value;
	}

	private double _Bukjk = 0; // 补扣价款

	public double getBukjk() {
		return _Bukjk;
	}

	public void setBukjk(double _value) {
		_Bukjk = _value;
	}

	private double _Jiashj = 0; // 价税合计

	public double getJiashj() {
		return _Jiashj;
	}

	public void setJiashj(double _value) {
		_Jiashj = _value;
	}

	private double _Jiaksk = 0; // 价款税款

	public double getJiaksk() {
		return _Jiaksk;
	}

	public void setJiaksk(double _value) {
		_Jiaksk = _value;
	}

	private double _Jiakhj = 0; // 价款合计

	public double getJiakhj() {
		return _Jiakhj;
	}

	public void setJiakhj(double _value) {
		_Jiakhj = _value;
	}

	private double _Jine = 0; // 金额

	/**
	 * 返回 金额
	 */
	public double getJine() {
		return _Jine;
	}

	/**
	 * 设置 金额
	 */
	public void setJine(double _value) {
		_Jine = _value;
	}

	private double _Yunf_kds = 0; // 大票运费可抵税 (运费中能抵税的费用)

	public double getYunf_kds() {
		return _Yunf_kds;
	}

	public void setYunf_kds(double _value) {
		_Yunf_kds = _value;
	}

	private double _Yunf_bkds = 0; // 大票运费中不可抵税的部分（运费中不能抵税的费用）

	public double getYunf_bkds() {
		return _Yunf_bkds;
	}

	public void setYunf_bkds(double _value) {
		_Yunf_bkds = _value;
	}

	private double _Zaf = 0; // 到站前杂费（显示在结算单左下角的杂费=到货前杂费）

	public double getZaf() {
		return _Zaf;
	}

	public void setZaf(double _value) {
		_Zaf = _value;
	}

	private double _Bukyzf = 0; // 补扣运杂费

	public double getBukyzf() {
		return _Bukyzf;
	}

	public void setBukyzf(double _value) {
		_Bukyzf = _value;
	}

	private double _Jiskc = 0; // 计税扣除(不能抵税的费用合计)

	public double getJiskc() {
		return _Jiskc;
	}

	public void setJiskc(double _value) {
		_Jiskc = _value;
	}

	private double _Buhsyf = 0; // 不含税运费

	public double getBuhsyf() {
		return _Buhsyf;
	}

	public void setBuhsyf(double _value) {
		_Buhsyf = _value;
	}

	private double _Yunfsk = 0; // 运费税款

	public double getYunfsk() {
		return _Yunfsk;
	}

	public void setYunfsk(double _value) {
		_Yunfsk = _value;
	}

	private double _Yunzfhj = 0; // 运杂费合计

	public double getYunzfhj() {
		return _Yunzfhj;
	}

	public void setYunzfhj(double _value) {
		_Yunzfhj = _value;
	}

	private double _Hej = 0; // 合计=运杂费合计＋价税合计

	public double getHej() {
		return _Hej;
	}

	public void setHej(double _value) {
		_Hej = _value;
	}

	private double _Kuidyf = 0; // 亏吨运费

	public double getKuidyf() {
		return _Kuidyf;
	}

	public void setKuidyf(double _value) {
		_Kuidyf = _value;
	}

	private double _Kuidzf = 0; // 亏吨杂费

	public double getKuidzf() {
		return _Kuidzf;
	}

	public void setKuidzf(double _value) {
		_Kuidzf = _value;
	}

	private String mselIds = ""; // 要结算的发货标识

	public String getSelIds() {
		return mselIds;
	}

	public void setSelIds(String _value) {
		mselIds = _value;
	}

	public String _Gongs_Shih = ""; // 石灰石公式

	public String getGongs_Shih() {
		return _Gongs_Shih;
	}

	public void setGongs_Shih(String _value) {
		_Gongs_Shih = _value;
	}
	
	public String _Gongs_Yf = ""; // 运费

	public String getGongs_Yf() {
		return _Gongs_Yf;
	}

	public void setGongs_Yf(String _value) {
		_Gongs_Yf = _value;
	}

	public long _Jieslx = 0; // 结算类型

	public long getJieslx() {
		return _Jieslx;
	}

	public void setJieslx(long _value) {
		_Jieslx = _value;
	}

	private String _Jingbr = ""; // 经办人

	public String getJingbr() {
		return _Jingbr;
	}

	public void setJingbr(String _value) {
		_Jingbr = _value;
	}

	private String ErroInfo = ""; // 是否又错误

	public String getErroInfo() {
		return ErroInfo;
	}

	public void setErroInfo(String _value) {
		ErroInfo = _value;
	}

	private boolean IsError = true;

	public boolean getIsError() {
		return IsError;
	}

	public void setIsError(boolean _value) {
		IsError = _value;
	}

	private int _leix = 0; // 结算类型

	public int getLeix() {
		return _leix;
	}

	public void setLeix(int _value) {
		_leix = _value;
	}
	
	
	//指标盈亏_Begin
	
	private double Shul_yk;
	
	private double CaO_yk;
	
	private double MgO_yk;
	
	private double Xid_yk;

	
	public double getCaO_yk() {
		return CaO_yk;
	}

	public void setCaO_yk(double value) {
		CaO_yk = value;
	}

	public double getMgO_yk() {
		return MgO_yk;
	}

	public void setMgO_yk(double value) {
		MgO_yk = value;
	}

	public double getXid_yk() {
		return Xid_yk;
	}

	public void setXid_yk(double value) {
		Xid_yk = value;
	}
	
	//指标盈亏_End
	
//	指标折单价_Begin
	
	private double Shul_zdj;
	
	private double CaO_zdj;
	
	private double MgO_zdj;
	
	private double Xid_zdj;

	
	public double getCaO_zdj() {
		return CaO_zdj;
	}

	public void setCaO_zdj(double value) {
		CaO_zdj = value;
	}

	public double getMgO_zdj() {
		return MgO_zdj;
	}

	public void setMgO_zdj(double value) {
		MgO_zdj = value;
	}

	public double getXid_zdj() {
		return Xid_zdj;
	}

	public void setXid_zdj(double value) {
		Xid_zdj = value;
	}

	/**
	 * @return shul_yk
	 */
	public double getShul_yk() {
		return Shul_yk;
	}

	/**
	 * @param shul_yk 要设置的 shul_yk
	 */
	public void setShul_yk(double shul_yk) {
		Shul_yk = shul_yk;
	}

	/**
	 * @return shul_zdj
	 */
	public double getShul_zdj() {
		return Shul_zdj;
	}

	/**
	 * @param shul_zdj 要设置的 shul_zdj
	 */
	public void setShul_zdj(double shul_zdj) {
		Shul_zdj = shul_zdj;
	}
	
	
//	指标折单价_End
	
//	指标折金额_Begin
	
	private double Shul_zje;
	
	private double CaO_zje;
	
	private double MgO_zje;
	
	private double Xid_zje;

	
	public double getShul_zje(){
		return Shul_zje;
	}
	
	public void setShul_zje(double value){
		Shul_zje=value;
	}
	
	public double getCaO_zje() {
		return CaO_zje;
	}

	public void setCaO_zje(double value) {
		CaO_zje = value;
	}

	public double getMgO_zje() {
		return MgO_zje;
	}

	public void setMgO_zje(double value) {
		MgO_zje = value;
	}

	public double getXid_zje() {
		return Xid_zje;
	}

	public void setXid_zje(double value) {
		Xid_zje = value;
	}
	

	public long getMeikjsb_id() {
		return _Meikjsb_id;
	}

	public void setMeikjsb_id(long meikjsb_id) {
		_Meikjsb_id = meikjsb_id;
	}

	public long getYunfjsb_id() {
		return _Yunfjsb_id;
	}

	public void setYunfjsb_id(long yunfjsb_id) {
		_Yunfjsb_id = yunfjsb_id;
	}
}
