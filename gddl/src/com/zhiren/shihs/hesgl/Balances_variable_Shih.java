package com.zhiren.shihs.hesgl;

import java.util.Date;

import javax.servlet.http.HttpSession;

public class Balances_variable_Shih {

	private String _Tianzdw = ""; // ���Ƶ�λ

	private String _Yunsfs = "";

	private String _Daibcc = "";	//������
	
	private boolean _Hetjgpp_Flag=false;	//��ͬ�۸�ƥ�䣨�ж��Ƿ�ȡ�����õĺ�ͬ�۸�
	
	private String _Yifzzb="";		//�Ѹ�ֵָ��
	
	private String _User_custom_mlj_jiesgs="";		//�û��Զ���Ŀ¼�۽��㹫ʽ
	
	private String _User_custom_fmlj_jiesgs="";	//�û��Զ����Ŀ¼�۽��㹫ʽ
	
	private String _Yikj_yunfyymk="��";				//һ�ڼۣ��˷�Դ��ú�
	
	private String _Yikj_meikyyyf="��";				//һ�ڼۣ�ú��Դ���˷ѣ�
	
	private long _Hetjgb_id=0;						//��ͬ�۸��id
	
	private double _Yunfjsdj=0;						//�˷ѽ��㵥��
	
	private int _Hansdjblxsw=2;						//��˰���۱���С��λ
	
	private int _yunfhsdjblxsw=2;					//�˷Ѻ�˰���۱���С��λ
	
	private long _Diancxxb_id=0;					//�糧��Ϣ��id
	
	private HttpSession _Session=null;				//��ǰ��session
	
	private int _Xuh=0;								//��ţ������ν�����һ���۸�һ����ţ�
	
	private long _Meikjsb_id=0;						//ú������_id
	
	private long _Yunfjsb_id=0;						//�˷ѽ����_id
	
	private long _Jihkjb_id=0;						//�ƻ��ھ���_id
	
	
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

	private String _Jiesbh = ""; // ���㵥���

	public String getJiesbh() {
		return _Jiesbh;
	}

	public void setJiesbh(String _value) {
		_Jiesbh = _value;
	}

	private String _Fahdw = ""; // ������λ

	public String getFahdw() {
		return _Fahdw;
	}

	public void setFahdw(String _value) {
		_Fahdw = _value;
	}

	private String _Meikdw = ""; // ú��λ

	public String getMeikdw() {
		return _Meikdw;
	}

	public void setMeikdw(String _value) {
		_Meikdw = _value;
	}

	private String _Fahrq = null; // ��������

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

	private String _Yuanshr = ""; // ԭ�ջ���

	public String getYuanshr() {
		return _Yuanshr;
	}

	public void setYuanshr(String _value) {
		_Yuanshr = _value;
	}

	private String _Xianshr = ""; // ���ջ���

	public String getXianshr() {
		return _Xianshr;
	}

	public void setXianshr(String _value) {
		_Xianshr = _value;
	}

	private String _daohrq = null; // ��������

	public String getDaohrq() {
		return _daohrq;
	}

	public void setDaohrq(String _value) {
		_daohrq = _value;
	}

	private String _Jiesrq = null; // ��������

	public String getJiesrq() {
		return _Jiesrq;
	}

	public void setJiesrq(String _value) {
		_Jiesrq = _value;
	}

	private String _Faz = ""; // ��վ

	public String getFaz() {
		return _Faz;
	}

	public void setFaz(String _value) {
		_Faz = _value;
	}

	private long _Faz_Id; // ��վ

	public long getFaz_Id() {
		return _Faz_Id;
	}

	public void setFaz_Id(long _value) {
		_Faz_Id = _value;
	}

	private long _Daoz_Id; // ��վid

	public long getDaoz_Id() {
		return _Daoz_Id;
	}

	public void setDaoz_Id(long _value) {
		_Daoz_Id = _value;
	}

	private Date _Fahksrq = null; // ������ʼ����

	public Date getFahksrq() {
		return _Fahksrq;
	}

	public void setFahksrq(Date _value) {
		_Fahksrq = _value;
	}

	private Date _Fahjzrq = null; // ������ֹ����

	public Date getFahjzrq() {
		return _Fahjzrq;
	}

	public void setFahjzrq(Date _value) {
		_Fahjzrq = _value;
	}

	private Date _Yansksrq = null; // ���տ�ʼ����

	public Date getYansksrq() {
		return _Yansksrq;
	}

	public void setYansksrq(Date _value) {
		_Yansksrq = _value;
	}

	private Date _Yansjsrq = null; // ���ս�������

	public Date getYansjsrq() {
		return _Yansjsrq;
	}

	public void setYansjsrq(Date _value) {
		_Yansjsrq = _value;
	}

	private String _Yansbh = ""; // ���ձ��

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
	
	private String _Shuljs="";	// �������㣨�����������㡢���ǰ������������㣩
	
	public String getShuljs(){
		
		return _Shuljs;
	}
	
	public void setShuljs(String _value){
		
		_Shuljs=_value;
	}
	
	private String _Zhiljs="";	// �������㣨�����������㡢���ǰ������������㣩
	
	public String getZhiljs(){
		
		return _Zhiljs;
	}
	
	public void setZhiljs(String _value){
		
		_Zhiljs=_value;
	}

	private String _Ranlpz = ""; // Ʒ��

	public String getRanlpz() {
		return _Ranlpz;
	}

	public void setRanlpz(String _value) {
		_Ranlpz = _value;
	}

	public String _Chec = ""; // ������

	public String getChec() {
		return _Chec;
	}

	public void setChec(String _value) {
		_Chec = _value;
	}

	private String _Hejdx = ""; // �ϼƴ�д

	public String getHejdx() {
		return _Hejdx;
	}

	public void setHejdx(String _value) {
		_Hejdx = _value;
	}

	private String _Beiz = ""; // ��ע

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

	private long _Hetb_Id = 0; // ��ͬ��id

	public long getHetb_Id() {
		return _Hetb_Id;
	}

	public void setHetb_Id(long _value) {
		_Hetb_Id = _value;
	}

	public long _Gongysb_id = 0; // ��Ӧ��id

	public long getGongysb_Id() {
		return _Gongysb_id;
	}

	public void setGongysb_Id(long _value) {
		_Gongysb_id = _value;
	}

	public long _Ranlpzb_Id = 0; // ȼ��Ʒ�ֱ�id

	public long getRanlpzb_Id() {
		return _Ranlpzb_Id;
	}

	public void setRanlpzb_Id(long _value) {
		_Ranlpzb_Id = _value;
	}

	public long _KuangfzlbId = 0; // ȼ��Ʒ�ֱ�id

	public long getKuangfzlbId() {
		return _KuangfzlbId;
	}

	public void setKuangfzlbId(long _value) {
		_KuangfzlbId = _value;
	}

//	2008-12-9	zsj ��
	private String _Jiesslblxs="0";	//������������С��Ϊ(ú��˷ѹ���)
	public String getJiesslblxs(){
		
		return _Jiesslblxs;
	}
	
	public void setJiesslblxs(String value){
		
		_Jiesslblxs=value;
	}

	private double _Meiksl = 0.13; // �ۿ�˰��

	public double getMeiksl() {
		return _Meiksl;
	}

	public void setMeiksl(double _value) {
		_Meiksl = _value;
	}

	private double _Gongfsl = 0; // ��������

	public double getGongfsl() {
		return _Gongfsl;
	}

	public void setGongfsl(double _value) {
		_Gongfsl = _value;
	}

	private double _gongfl = 0; // ������

	public double getGongfl() {
		return _gongfl;
	}

	public void setGongfl(double _value) {
		_gongfl = _value;
	}

	private double _Yanssl = 0; // ������������

	public double getYanssl() {
		return _Yanssl;
	}

	public void setYanssl(double _value) {
		_Yanssl = _value;
	}
	
	private double _Yuns = 0; // ����

	public double getYuns() {
		return _Yuns;
	}

	public void setYuns(double _value) {
		_Yuns = _value;
	}
	
	private double _Jiesslcy = 0; // ������������

	public double getJiesslcy() {
		return _Jiesslcy;
	}

	public void setJiesslcy(double _value) {
		_Jiesslcy = _value;
	}

	private double _Jiessl = 0; // ��������

	public double getJiessl() {
		return _Jiessl;
	}

	public void setJiessl(double _value) {
		_Jiessl = _value;
	}
	
	private double _Yingksl = 0; // ӯ������

	public double getYingksl() {
		return _Yingksl;
	}

	public void setYingksl(double _value) {
		_Yingksl = _value;
	}
	
	private double _Yunssl = 0; // ����

	public double getYunssl() {
		return _Yunssl;
	}

	public void setYunssl(double _value) {
		_Yunssl = _value;
	}
	
	private double _Koud_js = 0; // ����۶�

	public double getKoud_js() {
		return _Koud_js;
	}

	public void setKoud_js(double _value) {
		_Koud_js = _value;
	}
	
	private double _Koud = 0; // �۶�

	public double getKoud() {
		return _Koud;
	}

	public void setKoud(double _value) {
		_Koud = _value;
	}

	private double _Jingz = 0; // ����

	public double getJingz() {
		return _Jingz;
	}

	public void setJingz(double _value) {
		_Jingz = _value;
	}

	// ��ָͬ��_Begin

	private double _Hetml = 0;

	public double getHetml() {
		return _Hetml;
	}

	public void setHetml(double _value) {
		_Hetml = _value;
	}

	private double _Hetmdj = 0; // ��ͬú����

	public double getHetmdj() {
		return _Hetmdj;
	}

	public void setHetmdj(double _value) {
		_Hetmdj = _value;
	}
	
	private double _Tielzf = 0; // ��·�ӷ�(��·��Ʊ�ϲ���˰�ķ���)

	public double getTielzf() {
		return _Tielzf;
	}

	public void setTielzf(double _value) {
		_Tielzf = _value;
	}
	
	private double _Kuangqsk = 0; // ����˰��(�󷽴�Ʊ�ϲ���˰�ķ���)

	public double getKuangqsk() {
		return _Kuangqsk;
	}

	public void setKuangqsk(double _value) {
		_Kuangqsk = _value;
	}
	
	private double _Kuangqjk = 0; // �����ۿ�(�󷽴�Ʊ�ϲ���˰�ķ���)

	public double getKuangqjk() {
		return _Kuangqjk;
	}

	public void setKuangqjk(double _value) {
		_Kuangqjk = _value;
	}
	
	private double _Kuangqzf = 0; // �����ӷ�(�󷽴�Ʊ�ϲ���˰�ķ���)

	public double getKuangqzf() {
		return _Kuangqzf;
	}

	public void setKuangqzf(double _value) {
		_Kuangqzf = _value;
	}
	
	private double _Tielyf = 0; // �����˷�(��·��Ʊ�ϵĿɵ�˰����)

	public double getTielyf() {
		return _Tielyf;
	}

	public void setTielyf(double _value) {
		_Tielyf = _value;
	}
	
	private double _Kuangqyf = 0; // �����˷�(��·��Ʊ�ϵĿɵ�˰����)

	public double getKuangqyf() {
		return _Kuangqyf;
	}

	public void setKuangqyf(double _value) {
		_Kuangqyf = _value;
	}

	private double _Zuigmj = 0; // ���ú��

	public double getZuigmj() {
		return _Zuigmj;
	}

	public void setZuigmj(double _value) {
		_Zuigmj = _value;
	}
	
	private double _Zuidmj = 0;	//���ú��
	
	public double getZuidmj(){
		return _Zuidmj;
	}
	
	public void setZuidmj(double _value){
		_Zuidmj=_value;
	}

	private String _Hetmdjdw = ""; // ��ͬú���۵�λ

	public String getHetmdjdw() {
		return _Hetmdjdw;
	}

	public void setHetmdjdw(String _value) {
		_Hetmdjdw = _value;
	}

	private double _Hetyj = 0; // ��ͬ�˼۵��ۣ�һ������úʹ�ã�

	public double getHetyj() {
		return _Hetyj;
	}

	public void setHetyj(double _value) {
		_Hetyj = _value;
	}

	private String _Hetyjdw = ""; // ��ͬ�˼۵�λ

	public String getHetyjdw() {
		return _Hetyjdw;
	}

	public void setHetyjdw(String _value) {
		_Hetyjdw = _value;
	}

//	��ͬ����ָ��_Begin

	private String _Shul_ht = ""; 	// ��ͬ��ú�����������۵�����

	public String getShul_ht() {
		return _Shul_ht;
	}

	public void setShul_ht(String _value) {
		_Shul_ht = _value;
	}
	
	private String _CaO_ht = ""; // ��ͬCaO

	public String getCaO_ht() {
		return _CaO_ht;
	}

	public void setCaO_ht(String _value) {
		_CaO_ht = _value;
	}

	private String _MgO_ht = ""; // ��ͬMgO

	public String getMgO_ht() {
		return _MgO_ht;
	}

	public void setMgO_ht(String _value) {
		_MgO_ht = _value;
	}

	private String _Xid_ht = ""; // ��ͬϸ��

	public String getXid_ht() {
		return _Xid_ht;
	}

	public void setXid_ht(String _value) {
		_Xid_ht = _value;
	}
	
	// ��ͬ����ָ��_end

	// ����ָ��_Begin
	private double _CaO_cf = 0; 	// ����CaO

	public double getCaO_cf() {
		return _CaO_cf;
	}

	public void setCaO_cf(double _value) {
		_CaO_cf = _value;
	}

	private double _MgO_cf = 0; 	// ����MgO

	public double getMgO_cf() {
		return _MgO_cf;
	}

	public void setMgO_cf(double _value) {
		_MgO_cf = _value;
	}

	private double _Xid_cf = 0; 	// ����ϸ��

	public double getXid_cf() {
		return _Xid_cf;
	}

	public void setXid_cf(double _value) {
		_Xid_cf = _value;
	}
	
	// ��������ָ��_end

	// ��ָ��_Begin
	private double _CaO_kf = 0; 		// ��CaO

	public double getCaO_kf() {
		return _CaO_kf;
	}

	public void setCaO_kf(double _value) {
		_CaO_kf = _value;
	}

	private double _MgO_kf = 0; 		// ��MgO

	public double getMgO_kf() {
		return _MgO_kf;
	}

	public void setMgO_kf(double _value) {
		_MgO_kf = _value;
	}

	private double _Xid_kf = 0; 		// ��ϸ��

	public double getXid_kf() {
		return _Xid_kf;
	}

	public void setXid_kf(double _value) {
		_Xid_kf = _value;
	}

	// �󷽽���ָ��_end

	// ����ָ��_Begin
	private double _CaO_js = 0; 		// ����CaO

	public double getCaO_js() {
		return _CaO_js;
	}

	public void setCaO_js(double _value) {
		_CaO_js = _value;
	}

	private double _MgO_js = 0; 		// ����MgO

	public double getMgO_js() {
		return _MgO_js;
	}

	public void setMgO_js(double _value) {
		_MgO_js = _value;
	}

	private double _Xid_js = 0; 		// ����ϸ��

	public double getXid_js() {
		return _Xid_js;
	}

	public void setXid_js(double _value) {
		_Xid_js = _value;
	}

	// �������ָ��_end


	private double _Pinzbj = 0; 			// ȼ��Ʒ�ֱȼ�

	public double getPinzbj() {
		return _Pinzbj;
	}

	public void setPinzbj(double _value) {
		_Pinzbj = _value;
	}

	private double _Zhengcxjj = 0; 		// �����ԼӼ�

	public double getZhengcxjj() {
		return _Zhengcxjj;
	}

	public void setZhengcxjj(double _value) {
		_Zhengcxjj = _value;
	}

	private double _Buhsmj = 0; 	// ����˰ú��

	public double getBuhsmj() {
		return _Buhsmj;
	}

	public void setBuhsmj(double _value) {
		_Buhsmj = _value;
	}

	private double _Hansmj = 0; 	// ��˰ú����

	public double getHansmj() {
		return _Hansmj;
	}

	public void setHansmj(double _value) {
		_Hansmj = _value;
	}

	private double _Shulzjbz = 0; // �����ۼ۱�׼

	public double getShulzjbz() {
		return _Shulzjbz;
	}

	public void setShulzjbz(double _value) {
		_Shulzjbz = _value;
	}

	private double _Shulzjje = 0; // �����ۼ۽��=��˰ú��*ӯ������

	public double getShulzjje() {
		return _Shulzjje;
	}

	public void setShulzjje(double _value) {
		_Shulzjje = _value;
	}

	private double _Bukjk = 0; // ���ۼۿ�

	public double getBukjk() {
		return _Bukjk;
	}

	public void setBukjk(double _value) {
		_Bukjk = _value;
	}

	private double _Jiashj = 0; // ��˰�ϼ�

	public double getJiashj() {
		return _Jiashj;
	}

	public void setJiashj(double _value) {
		_Jiashj = _value;
	}

	private double _Jiaksk = 0; // �ۿ�˰��

	public double getJiaksk() {
		return _Jiaksk;
	}

	public void setJiaksk(double _value) {
		_Jiaksk = _value;
	}

	private double _Jiakhj = 0; // �ۿ�ϼ�

	public double getJiakhj() {
		return _Jiakhj;
	}

	public void setJiakhj(double _value) {
		_Jiakhj = _value;
	}

	private double _Jine = 0; // ���

	/**
	 * ���� ���
	 */
	public double getJine() {
		return _Jine;
	}

	/**
	 * ���� ���
	 */
	public void setJine(double _value) {
		_Jine = _value;
	}

	private double _Yunf_kds = 0; // ��Ʊ�˷ѿɵ�˰ (�˷����ܵ�˰�ķ���)

	public double getYunf_kds() {
		return _Yunf_kds;
	}

	public void setYunf_kds(double _value) {
		_Yunf_kds = _value;
	}

	private double _Yunf_bkds = 0; // ��Ʊ�˷��в��ɵ�˰�Ĳ��֣��˷��в��ܵ�˰�ķ��ã�

	public double getYunf_bkds() {
		return _Yunf_bkds;
	}

	public void setYunf_bkds(double _value) {
		_Yunf_bkds = _value;
	}

	private double _Zaf = 0; // ��վǰ�ӷѣ���ʾ�ڽ��㵥���½ǵ��ӷ�=����ǰ�ӷѣ�

	public double getZaf() {
		return _Zaf;
	}

	public void setZaf(double _value) {
		_Zaf = _value;
	}

	private double _Bukyzf = 0; // �������ӷ�

	public double getBukyzf() {
		return _Bukyzf;
	}

	public void setBukyzf(double _value) {
		_Bukyzf = _value;
	}

	private double _Jiskc = 0; // ��˰�۳�(���ܵ�˰�ķ��úϼ�)

	public double getJiskc() {
		return _Jiskc;
	}

	public void setJiskc(double _value) {
		_Jiskc = _value;
	}

	private double _Buhsyf = 0; // ����˰�˷�

	public double getBuhsyf() {
		return _Buhsyf;
	}

	public void setBuhsyf(double _value) {
		_Buhsyf = _value;
	}

	private double _Yunfsk = 0; // �˷�˰��

	public double getYunfsk() {
		return _Yunfsk;
	}

	public void setYunfsk(double _value) {
		_Yunfsk = _value;
	}

	private double _Yunzfhj = 0; // ���ӷѺϼ�

	public double getYunzfhj() {
		return _Yunzfhj;
	}

	public void setYunzfhj(double _value) {
		_Yunzfhj = _value;
	}

	private double _Hej = 0; // �ϼ�=���ӷѺϼƣ���˰�ϼ�

	public double getHej() {
		return _Hej;
	}

	public void setHej(double _value) {
		_Hej = _value;
	}

	private double _Kuidyf = 0; // �����˷�

	public double getKuidyf() {
		return _Kuidyf;
	}

	public void setKuidyf(double _value) {
		_Kuidyf = _value;
	}

	private double _Kuidzf = 0; // �����ӷ�

	public double getKuidzf() {
		return _Kuidzf;
	}

	public void setKuidzf(double _value) {
		_Kuidzf = _value;
	}

	private String mselIds = ""; // Ҫ����ķ�����ʶ

	public String getSelIds() {
		return mselIds;
	}

	public void setSelIds(String _value) {
		mselIds = _value;
	}

	public String _Gongs_Shih = ""; // ʯ��ʯ��ʽ

	public String getGongs_Shih() {
		return _Gongs_Shih;
	}

	public void setGongs_Shih(String _value) {
		_Gongs_Shih = _value;
	}
	
	public String _Gongs_Yf = ""; // �˷�

	public String getGongs_Yf() {
		return _Gongs_Yf;
	}

	public void setGongs_Yf(String _value) {
		_Gongs_Yf = _value;
	}

	public long _Jieslx = 0; // ��������

	public long getJieslx() {
		return _Jieslx;
	}

	public void setJieslx(long _value) {
		_Jieslx = _value;
	}

	private String _Jingbr = ""; // ������

	public String getJingbr() {
		return _Jingbr;
	}

	public void setJingbr(String _value) {
		_Jingbr = _value;
	}

	private String ErroInfo = ""; // �Ƿ��ִ���

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

	private int _leix = 0; // ��������

	public int getLeix() {
		return _leix;
	}

	public void setLeix(int _value) {
		_leix = _value;
	}
	
	
	//ָ��ӯ��_Begin
	
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
	
	//ָ��ӯ��_End
	
//	ָ���۵���_Begin
	
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
	 * @param shul_yk Ҫ���õ� shul_yk
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
	 * @param shul_zdj Ҫ���õ� shul_zdj
	 */
	public void setShul_zdj(double shul_zdj) {
		Shul_zdj = shul_zdj;
	}
	
	
//	ָ���۵���_End
	
//	ָ���۽��_Begin
	
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
