package com.zhiren.dc.hesgl.jiesd;

import java.util.Date;

import javax.servlet.http.HttpSession;

public class Balances_variable {

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
	
	private double _Yunfjsdj_mk=0;					//���ڼ�¼������ú��ʱ�����˷Ѳ������ʱ��ֵ�����ڽ����ı��棩
	
	private int _meikhsdjblxsw=2;					//ú�˰���۱���С��λ
	
	private int _yunfhsdjblxsw=2;					//�˷Ѻ�˰���۱���С��λ
	
	private long _Diancxxb_id=0;					//�糧��Ϣ��id
	
	private HttpSession _Session=null;				//��ǰ��session
	
	private int _Xuh=0;								//��ţ������ν�����һ���۸�һ����ţ�
	
	private long _Meikjsb_id=0;						//ú������_id
	
	private long _Yunfjsb_id=0;						//�˷ѽ����_id
	
	private long _Jihkjb_id=0;						//�ƻ��ھ���_id
	
	private String _Kuidjfyf="��";					//���־ܸ��˷�
	
	private String _Liangpjsyfbjxkd="��";			//��Ʊ�����˷Ѳ����п۶�
	
	private String _Hetbh="";						//��ͬ��
	
	private String _Mj_to_kcal_xsclfs="��������";	//�׽�ת��С������ʽ
	
	private String _Meikhsdj_qzfs="��������";		//ú�˰����ȡ����ʽ(�������롢��ȥ����λ)
	
	private StringBuffer _Meikzkksyfw = new StringBuffer();		//ú�����ۿ����÷�Χ
	
	private StringBuffer _Tmp_Sql = new StringBuffer();		///SQl��ʱ����
	
	private String _JiagpzId="";					//�۸�Ʒ��
	
	private double _Kuidjfyf_je=0;					//���پܸ��˷�
	
	private double _Kuidjfzf_je=0;					//���پܸ��ӷ�
	
	private String _ChaodOrKuid="";					//��Or���ֱ�ʶ
	
	private boolean _Danpcysyf=false;				//�����������˷�(��¼�����ν������Ƿ��Ѿ�������˷ѵı�־)
													//�ڽ����߼������ǵ����ν��㻹�Ǽ�Ȩƽ�����㣬�������ú����Եģ��˷�ʼ��ֻ����һ��
//														����ñ�־Ϊ�棬˵���Ѿ��ڼ���ú��Ĺ����м�����˷��ˡ�
	
	private String _Jieszbtscl_Items="";			//����ָ�����⴦��(��Ŀ)
	
	private String[] _Tsclzbs=null;					//��Ҫ���⴦��ָ��
													//��ָ�����ۿ������ɺ���������鱣�����ۿ��ֵ
	private boolean _Shangcjslct_Flag=false;		//�Ƿ�����ˡ��ϴν�����������������˾�true������false
	
	private double _Chengbzb=0;						//����ɱ��ã�ȡ��ͬ�۸��е�������޶�Ӧ������
	
	private String _Yunfjsdpcfztj = "";				//�˷ѽ��㵥���η���������˵�������˷���Ҫ��ĳһ���򼸸�ָ��������ʱʹ��
	
	private String _Yunsdw = "";					//���䵥λ
	
	private long _Yunsdwb_id = 0;					//���䵥λ��_id
	
	private double _Jieskdl = 0;					//����۶���
	
	private String _Jieszbsftz = "";				//����ָ���Ƿ����
	
	private double _Shangcjsl = 0;					//�ϴν�������
	
	private int _Xuh_yf=0;							//��ţ��˷ѵ����ν�����һ���۸�һ����ţ�
	
	private double _Baifbdsl = 0;					//�ٷֱȶ���������
	
	private String _Shifyrzcjs = "��";				//�Ƿ�����ֵ�����
	
	private double _Kuikjs = 0;					//��������
	
	private String _Danglgs = "";					//������ʽ
	
	private String _Danglblxsw = "2";				//��������С��λ
	
	private String _Guohxt = "";					//����ϵͳ(Ϊ�������ǵ糧����Aϵͳ��Bϵͳ����������ҵ�����Ӵ˱���)
	
	private String _Feitsclzb = "";				//��¼��ú��ͬ���ۿ���Ӱ��ú����㵥�۵ķ����⴦��ָ��
	
	private boolean _Meiksfhyf = false;         //ú���Ƿ��˷�
	
	private double _Yujsjz = 0;         //Ԥ������
	
	public String getFeitsclzb() {
		return _Feitsclzb;
	}

	public void setFeitsclzb(String meikzkkzb) {
		_Feitsclzb = meikzkkzb;
	}

	public String getGuohxt() {
		return _Guohxt;
	}

	public void setGuohxt(String guohxt) {
		_Guohxt = guohxt;
	}
	
	public String getDanglblxsw() {
		return _Danglblxsw;
	}

	public void setDanglblxsw(String danglblxsw) {
		_Danglblxsw = danglblxsw;
	}

	public String getDanglgs() {
		return _Danglgs;
	}

	public void setDanglgs(String danglgs) {
		_Danglgs = danglgs;
	}
	
	public double getKuikjs() {
		return _Kuikjs;
	}

	public void setKuikjs(double kuikjs) {
		_Kuikjs = kuikjs;
	}
	
	public String getShifyrzcjs() {
		return _Shifyrzcjs;
	}

	public void setShifyrzcjs(String shifyrzcjs) {
		_Shifyrzcjs = shifyrzcjs;
	}

	public int getXuh_yf(){
		
		return _Xuh_yf;
	}
	
	public void setXuh_yf(int value){
		
		_Xuh_yf = value;
	}
	
	public double getShangcjsl(){
		
		return _Shangcjsl;
	}
	
	public void setShangcjsl(double value){
		
		_Shangcjsl = value;
	}
	
	public String getJieszbsftz(){
		
		return _Jieszbsftz;
	}
	
	public void setJieszbsftz(String value){
		
		_Jieszbsftz = value;
	}
	
	public double getJieskdl(){
		
		return _Jieskdl;
	}
	
	public void setJieskdl(double value){
		
		_Jieskdl = value;
	}
	
	public long getYunsdwb_id(){
		
		return _Yunsdwb_id;
	}
	
	public void setYunsdwb_id(long value){
		
		_Yunsdwb_id = value;
	}
	
	public String getYunsdw(){
		
		return _Yunsdw;
	}
	
	public void setYunsdw(String value){
		
		_Yunsdw = value;
	}
	
	public String getYunfjsdpcfztj(){
		
		return _Yunfjsdpcfztj;
	}
	
	public void setYunfjsdpcfztj(String value){
		
		_Yunfjsdpcfztj = value;
	}
	
	public double getChengbzb(){
		
		return _Chengbzb;
	}
	
	public void setChengbzb(double value){
		
		_Chengbzb = value;
	}

	public boolean getShangcjslct_Flag(){
		
		return _Shangcjslct_Flag;
	}
	
	public void setShangcjslct_Flag(boolean value){
		
		_Shangcjslct_Flag = value;
	}
	
	public String[] getTsclzbs(){
		
		return _Tsclzbs;
	}
	
	public void setTsclzbs(String[] value){
		
		_Tsclzbs=value;
	}
	
	private boolean _Tsclzbzkksfxyjs=false;			//����ָ������ۿ��Ƿ���Ҫ����
													//˵��������ָ�����������ָ�꣨Tsclzbs�������У�
	public boolean getTsclzbzkksfxyjs(){			//		���⴦��ʱ����ÿ��ָ���ֵ���������ۿ�������
													//		����ָ��ֵ�������ۿ�����ʱ����ֵΪ�棬��Ҫ�������ۿ�
		return _Tsclzbzkksfxyjs;					//		����ָ��ֵ���������ۿ�����ʱ����ֵΪ�٣�����Ҫ�������ۿ�
	}
	
	public void setTsclzbzkksfxyjs(boolean value){
		
		_Tsclzbzkksfxyjs = value;
	}
	
	private String _shifykfzljs="��";				//�Ƿ��ÿ���������
	
	public String getShifykfzljs(){
		
		return _shifykfzljs;
	}
	
	public void setShifykfzljs(String value){
		
		_shifykfzljs = value;
	}
	
	public String getJieszbtscl_Items(){
		
		return _Jieszbtscl_Items;
	}
	
	public void setJieszbtscl_Items(String value){
		
		_Jieszbtscl_Items = value;
	}
	
	public void setDanpcysyf(boolean value){
		
		_Danpcysyf = value;
	}
	
	public boolean getDanpcysyf(){
		
		return _Danpcysyf;
	}
	
	public String getChaodOrKuid(){
		
		return _ChaodOrKuid;
	}
	
	public void setChaodOrKuid(String value){
		
		_ChaodOrKuid = value;
	}
	
	
	
	public double getKuidjfyf_je(){
		
		return _Kuidjfyf_je;
	}
	
	public void setKuidjfyf_je(double value){
		
		_Kuidjfyf_je = value;
	}
	
	public double getKuidjfzf_je(){
		
		return _Kuidjfzf_je;
	}
	
	public void setKuidjfzf_je(double value){
		
		_Kuidjfzf_je = value;
	}
	
	public StringBuffer getTmp_Sql(){
		
		return _Tmp_Sql;
	}
	
	public void setTmp_Sql(StringBuffer sql){
		
		this._Tmp_Sql = sql;
	}
	
	public String getJiagpzId(){
		
		return _JiagpzId;
	}
	
	public void setJiagpzId(String jiagpzid){
		
		this._JiagpzId = jiagpzid;
	}
	
	public StringBuffer getMeikzkksyfw(){
		
		return _Meikzkksyfw;
	}
	
	public void setMeikzkksyfw(StringBuffer value){
		
		this._Meikzkksyfw = value;
	}
	
	public String getMj_to_kcal_xsclfs(){
		
		return _Mj_to_kcal_xsclfs;
	}
	
	public void setMj_to_kcal_xsclfs(String value){
		
		_Mj_to_kcal_xsclfs=value;
	}
	
//	ú�˰����ȡ����ʽ
	public String getMeikhsdj_qzfs(){
		
		return _Meikhsdj_qzfs;
	}
	
	public void setMeikhsdj_qzfs(String value){
		
		_Meikhsdj_qzfs=value;
	}
//	ú��˰����ȡ����ʽ_end
	
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
	
	public int getMeikhsdjblxsw(){
		
		return _meikhsdjblxsw;
	}
	
	public void setMeikhsdjblxsw(int value){
		
		_meikhsdjblxsw=value;
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
	
	public boolean getMeiksfhyf() {
		return _Meiksfhyf;
	}

	public void setMeiksfhyf(boolean Meiksfhyf) {
		_Meiksfhyf = Meiksfhyf;
	}
	
	public double getYujsjz() {
		return _Yujsjz;
	}

	public void setYujsjz(double Yujsjz) {
		_Yujsjz = Yujsjz;
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

	private String _ZhangH = "";

	public String getZhangH() {
		return _ZhangH;
	}

	public void setZhangH(String _value) {
		_ZhangH = _value;
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

	public long _Meikxxb_Id = 0; // ú����Ϣ��id

	public long getMeikxxb_Id() {
		return _Meikxxb_Id;
	}

	public void setMeikxxb_Id(long _value) {
		_Meikxxb_Id = _value;
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

	private double _Yunfsl = 0.07; // �˷�˰��

	public double getYunfsl() {
		return _Yunfsl;
	}

	public void setYunfsl(double _value) {
		_Yunfsl = _value;
	}
	
	private double _Yunfjsl = 0; // �˷ѽ�����
	
	public double getYunfjsl() {
		return _Yunfjsl;
	}

	public void setYunfjsl(double _value) {
		_Yunfjsl = _value;
	}
	
	private double _Yunfjsl_mk = 0; //���ڼ�¼������ú��ʱ�����˷Ѳ������ʱ��ֵ�����ڽ����ı���
	
	private double _Chaokdl = 0; // ��������

	public double getChaokdl() {
		return _Chaokdl;
	}

	public void setChaokdl(double _value) {
		_Chaokdl = _value;
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
	
	private int _Meikzkkblxsw=2;	//	ú�����ۿ��С��λ
	
	public int getMeikzkkblxsw(){
		
		return _Meikzkkblxsw;
	}
	
	public void setMeikzkkblxsw(int value){
		
		_Meikzkkblxsw = value;
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
	
	private double _Yingd = 0; // ӯ��

	public double getYingd() {
		return _Yingd;
	}

	public void setYingd(double _value) {
		_Yingd = _value;
	}
	
	private double _Kuid = 0; // ����

	public double getKuid() {
		return _Kuid;
	}

	public void setKuid(double _value) {
		_Kuid = _value;
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

	private double _Kous = 0; // ��ˮ

	public double getKous() {
		return _Kous;
	}

	public void setKous(double _value) {
		_Kous = _value;
	}

	private double _Kouz = 0; // ����

	public double getKouz() {
		return _Kouz;
	}

	public void setKouz(double _value) {
		_Kouz = _value;
	}

	private double _Jingz = 0; // ����

	public double getJingz() {
		return _Jingz;
	}

	public void setJingz(double _value) {
		_Jingz = _value;
	}

	// ��ָͬ��_Begin

	private String _Hetml = "0";

	public String getHetml() {
		return _Hetml;
	}

	public void setHetml(String _value) {
		_Hetml = _value;
	}

	private double _Hetmdj = 0; // ��ͬú����

	public double getHetmdj() {
		return _Hetmdj;
	}

	public void setHetmdj(double _value) {
		_Hetmdj = _value;
	}
	
	private String _Hetmdjgs = ""; // ��ͬú���۹�ʽ
	
	public String getHetmdjgs(){
		
		return _Hetmdjgs;
	}
	
	public void setHetmdjgs(String hetmdjgs){
		
		_Hetmdjgs = hetmdjgs;
	}
	
//	��������
	private int _jijlx = 0; // ��˰����

	public int getJijlx() {
		return _jijlx;
	}

	public void setJijlx(int _value) {
		_jijlx = _value;
	}
//	��������
	
	private double _Tielzf = 0; // ��·�ӷ�(��·��Ʊ�ϲ���˰�ķ���)

	public double getTielzf() {
		return _Tielzf;
	}

	public void setTielzf(double _value) {
		_Tielzf = _value;
	}
	
	private double _Tielyfsk = 0; // ��·�˷�˰��(��·��Ʊ�Ͽɵ�˰�ķ��ã���Ӧ��˰��)
	
	public double getTielyfsk() {
		return _Tielyfsk;
	}

	public void setTielyfsk(double _value) {
		_Tielyfsk = _value;
	}
	
	private double _Tielyfjk = 0; // ��·�˷Ѽۿ�(��·��Ʊ�Ͽɵ�˰�ķ��ã���Ӧ��˰��)
	
	public double getTielyfjk() {
		return _Tielyfjk;
	}

	public void setTielyfjk(double _value) {
		_Tielyfjk = _value;
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
	
	private double _Fengsjj = 0; // �ֹ�˾�Ӽ�
	
	public double getFengsjj(){
		return _Fengsjj;
	}
	
	public void setFengsjj(double _value){
		_Fengsjj=_value;
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

	private String _Hetjjfs = ""; // ��ͬ�Ƽ۷�ʽ(Ŀ¼�ۡ���ֵ����(��)����ֵ����(��)�������۸���)

	public String getHetjjfs() {
		return _Hetjjfs;
	}

	public void setHetjjfs(String _value) {
		_Hetjjfs = _value;
	}

	private String _Jiesfs = ""; // ���㷽ʽ�������ۡ�����ۣ�

	public String getJiesfs() {
		return _Jiesfs;
	}

	public void setJiesfs(String _value) {
		_Jiesfs = _value;
	}

	private String _Jiesxs = ""; // ������ʽ�������Ρ���Ȩƽ����

	public String getJiesxs() {
		return _Jiesxs;
	}

	public void setJiesxs(String _value) {
		_Jiesxs = _value;
	}
	
//	��ͬ����ָ��_Begin

	private String _Shul_ht = ""; 	// ��ͬ��ú�����������۵�����

	public String getShul_ht() {
		return _Shul_ht;
	}

	public void setShul_ht(String _value) {
		_Shul_ht = _value;
	}
	
	private String _Qnetar_ht = ""; // ��ͬ���յ�λ������

	public String getQnetar_ht() {
		return _Qnetar_ht;
	}

	public void setQnetar_ht(String _value) {
		_Qnetar_ht = _value;
	}

	private String _Std_ht = ""; // ��ͬ�������

	public String getStd_ht() {
		return _Std_ht;
	}

	public void setStd_ht(String _value) {
		_Std_ht = _value;
	}
	
//	�յ���ȫ��
	private String _Star_ht = ""; // ��ͬ�յ���ȫ��

	public String getStar_ht() {
		return _Star_ht;
	}

	public void setStar_ht(String _value) {
		_Star_ht = _value;
	}
//	�յ���ȫ��

	private String _Mt_ht = ""; // ��ͬ����ȫˮ��

	public String getMt_ht() {
		return _Mt_ht;
	}

	public void setMt_ht(String _value) {
		_Mt_ht = _value;
	}

	private String _Mad_ht = ""; // ��ͬһ�����ú��ˮ��

	public String getMad_ht() {
		return _Mad_ht;
	}

	public void setMad_ht(String _value) {
		_Mad_ht = _value;
	}

	private String _Aar_ht = ""; // ��ͬ�յ����ҷ�

	public String getAar_ht() {
		return _Aar_ht;
	}

	public void setAar_ht(String _value) {
		_Aar_ht = _value;
	}

	private String _Aad_ht = ""; // ��ͬһ�����ú���ҷ�

	public String getAad_ht() {
		return _Aad_ht;
	}

	public void setAad_ht(String _value) {
		_Aad_ht = _value;
	}

	private String _Ad_ht = ""; // ��ͬ������ҷ�

	public String getAd_ht() {
		return _Ad_ht;
	}

	public void setAd_ht(String _value) {
		_Ad_ht = _value;
	}

	private String _Vad_ht = ""; // ��ͬһ�����ú���ӷ���

	public String getVad_ht() {
		return _Vad_ht;
	}

	public void setVad_ht(String _value) {
		_Vad_ht = _value;
	}

	private String _Vdaf_ht = ""; // ��ͬ�����޻һ��ӷ���

	public String getVdaf_ht() {
		return _Vdaf_ht;
	}

	public void setVdaf_ht(String _value) {
		_Vdaf_ht = _value;
	}

	private String _Stad_ht = ""; // ��ͬһ�����ú��ȫ��

	public String getStad_ht() {
		return _Stad_ht;
	}

	public void setStad_ht(String _value) {
		_Stad_ht = _value;
	}

	private String _Had_ht = ""; // ��ͬһ�����ú����

	public String getHad_ht() {
		return _Had_ht;
	}

	public void setHad_ht(String _value) {
		_Had_ht = _value;
	}

	private String _Qbad_ht = ""; // ��ͬ��Ͳ��ֵ

	public String getQbad_ht() {
		return _Qbad_ht;
	}

	public void setQbad_ht(String _value) {
		_Qbad_ht = _value;
	}

	private String _Qgrad_ht = ""; // ��ͬ�������λ��ֵ

	public String getQgrad_ht() {
		return _Qgrad_ht;
	}

	public void setQgrad_ht(String _value) {
		_Qgrad_ht = _value;
	}
	
	private String _T2_ht = ""; 	// ��ͬ���۵�

	public String getT2_ht() {
		return _T2_ht;
	}

	public void setT2_ht(String _value) {
		_T2_ht = _value;
	}
	
	private String _Yunju_ht = ""; 	// ��ͬ�˾�

	public String getYunju_ht() {
		return _Yunju_ht;
	}

	public void setYunju_ht(String _value) {
		_Yunju_ht = _value;
	}
	
	// ��ͬ����ָ��_end

	// ����ָ��_Begin
	private double _Qnetar_cf = 0; // �������յ�λ������

	public double getQnetar_cf() {
		return _Qnetar_cf;
	}

	public void setQnetar_cf(double _value) {
		_Qnetar_cf = _value;
	}

	private double _Std_cf = 0; // �����������

	public double getStd_cf() {
		return _Std_cf;
	}

	public void setStd_cf(double _value) {
		_Std_cf = _value;
	}
	
//	�յ���ȫ��
	private double _Star_cf = 0; // 

	public double getStar_cf() {
		return _Star_cf;
	}

	public void setStar_cf(double _value) {
		_Star_cf = _value;
	}
//	�յ���ȫ��

	private double _Mt_cf = 0; // ��������ȫˮ��

	public double getMt_cf() {
		return _Mt_cf;
	}

	public void setMt_cf(double _value) {
		_Mt_cf = _value;
	}

	private double _Mad_cf = 0; // ����һ�����ú��ˮ��

	public double getMad_cf() {
		return _Mad_cf;
	}

	public void setMad_cf(double _value) {
		_Mad_cf = _value;
	}

	private double _Aar_cf = 0; // �����յ����ҷ�

	public double getAar_cf() {
		return _Aar_cf;
	}

	public void setAar_cf(double _value) {
		_Aar_cf = _value;
	}

	private double _Aad_cf = 0; // ����һ�����ú���ҷ�

	public double getAad_cf() {
		return _Aad_cf;
	}

	public void setAad_cf(double _value) {
		_Aad_cf = _value;
	}

	private double _Ad_cf = 0; // ����������ҷ�

	public double getAd_cf() {
		return _Ad_cf;
	}

	public void setAd_cf(double _value) {
		_Ad_cf = _value;
	}

	private double _Vad_cf = 0; // ����һ�����ú���ӷ���

	public double getVad_cf() {
		return _Vad_cf;
	}

	public void setVad_cf(double _value) {
		_Vad_cf = _value;
	}

	private double _Vdaf_cf = 0; // ���������޻һ��ӷ���

	public double getVdaf_cf() {
		return _Vdaf_cf;
	}

	public void setVdaf_cf(double _value) {
		_Vdaf_cf = _value;
	}

	private double _Stad_cf = 0; // ����һ�����ú��ȫ��

	public double getStad_cf() {
		return _Stad_cf;
	}

	public void setStad_cf(double _value) {
		_Stad_cf = _value;
	}

	private double _Had_cf = 0; // ����һ�����ú����

	public double getHad_cf() {
		return _Had_cf;
	}

	public void setHad_cf(double _value) {
		_Had_cf = _value;
	}

	private double _Qbad_cf = 0; // ������Ͳ��ֵ

	public double getQbad_cf() {
		return _Qbad_cf;
	}

	public void setQbad_cf(double _value) {
		_Qbad_cf = _value;
	}

	private double _Qgrad_cf = 0; // �����������λ��ֵ

	public double getQgrad_cf() {
		return _Qgrad_cf;
	}

	public void setQgrad_cf(double _value) {
		_Qgrad_cf = _value;
	}

	private double _T2_cf = 0; // �������۵�

	public double getT2_cf() {
		return _T2_cf;
	}

	public void setT2_cf(double _value) {
		_T2_cf = _value;
	}
	
	private double _Yunju_cf = 0; // �����˾�

	public double getYunju_cf() {
		return _Yunju_cf;
	}

	public void setYunju_cf(double _value) {
		_Yunju_cf = _value;
	}

	
	// ��������ָ��_end

	// ��ָ��_Begin
	private double _Qnetar_kf = 0; // �����յ�λ������

	public double getQnetar_kf() {
		return _Qnetar_kf;
	}

	public void setQnetar_kf(double _value) {
		_Qnetar_kf = _value;
	}

	private double _Std_kf = 0; // �󷽸������

	public double getStd_kf() {
		return _Std_kf;
	}

	public void setStd_kf(double _value) {
		_Std_kf = _value;
	}
	
//	�յ���ȫ��
	private double _Star_kf = 0; // ���յ���ȫ��

	public double getStar_kf() {
		return _Star_kf;
	}

	public void setStar_kf(double _value) {
		_Star_kf = _value;
	}
//	�յ���ȫ��

	private double _Mt_kf = 0; // ������ȫˮ��

	public double getMt_kf() {
		return _Mt_kf;
	}

	public void setMt_kf(double _value) {
		_Mt_kf = _value;
	}

	private double _Mad_kf = 0; // ��һ�����ú��ˮ��

	public double getMad_kf() {
		return _Mad_kf;
	}

	public void setMad_kf(double _value) {
		_Mad_kf = _value;
	}

	private double _Aar_kf = 0; // ���յ����ҷ�

	public double getAar_kf() {
		return _Aar_kf;
	}

	public void setAar_kf(double _value) {
		_Aar_kf = _value;
	}

	private double _Aad_kf = 0; // ��һ�����ú���ҷ�

	public double getAad_kf() {
		return _Aad_kf;
	}

	public void setAad_kf(double _value) {
		_Aad_kf = _value;
	}

	private double _Ad_kf = 0; // �󷽸�����ҷ�

	public double getAd_kf() {
		return _Ad_kf;
	}

	public void setAd_kf(double _value) {
		_Ad_kf = _value;
	}

	private double _Vad_kf = 0; // ��һ�����ú���ӷ���

	public double getVad_kf() {
		return _Vad_kf;
	}

	public void setVad_kf(double _value) {
		_Vad_kf = _value;
	}

	private double _Vdaf_kf = 0; // �󷽸����޻һ��ӷ���

	public double getVdaf_kf() {
		return _Vdaf_kf;
	}

	public void setVdaf_kf(double _value) {
		_Vdaf_kf = _value;
	}

	private double _Stad_kf = 0; // ��һ�����ú��ȫ��

	public double getStad_kf() {
		return _Stad_kf;
	}

	public void setStad_kf(double _value) {
		_Stad_kf = _value;
	}

	private double _Had_kf = 0; // ��һ�����ú����

	public double getHad_kf() {
		return _Had_kf;
	}

	public void setHad_kf(double _value) {
		_Had_kf = _value;
	}

	private double _Qbad_kf = 0; // �󷽵�Ͳ��ֵ

	public double getQbad_kf() {
		return _Qbad_kf;
	}

	public void setQbad_kf(double _value) {
		_Qbad_kf = _value;
	}

	private double _Qgrad_kf = 0; // �󷽸������λ��ֵ

	public double getQgrad_kf() {
		return _Qgrad_kf;
	}

	public void setQgrad_kf(double _value) {
		_Qgrad_kf = _value;
	}
	
	private double _T2_kf = 0; // �󷽸������λ��ֵ

	public double getT2_kf() {
		return _T2_kf;
	}

	public void setT2_kf(double _value) {
		_T2_kf = _value;
	}
	
	private double _Yunju_kf = 0; // ���˾�

	public double getYunju_kf() {
		return _Yunju_kf;
	}

	public void setYunju_kf(double _value) {
		_Yunju_kf = _value;
	}

	// �󷽽���ָ��_end

	// ����ָ��_Begin
	private double _Qnetar_js = 0; // �������յ�λ������

	public double getQnetar_js() {
		return _Qnetar_js;
	}

	public void setQnetar_js(double _value) {
		_Qnetar_js = _value;
	}

	private double _Std_js = 0; // ����������

	public double getStd_js() {
		return _Std_js;
	}

	public void setStd_js(double _value) {
		_Std_js = _value;
	}
	
//	�յ���ȫ��
	private double _Star_js = 0; // �����յ���ȫ��

	public double getStar_js() {
		return _Star_js;
	}

	public void setStar_js(double _value) {
		_Star_js = _value;
	}
//	�յ���ȫ��

	private double _Mt_js = 0; // ��������ȫˮ��

	public double getMt_js() {
		return _Mt_js;
	}

	public void setMt_js(double _value) {
		_Mt_js = _value;
	}

	private double _Mad_js = 0; // ����һ�����ú��ˮ��

	public double getMad_js() {
		return _Mad_js;
	}

	public void setMad_js(double _value) {
		_Mad_js = _value;
	}

	private double _Aar_js = 0; // �����յ����ҷ�

	public double getAar_js() {
		return _Aar_js;
	}

	public void setAar_js(double _value) {
		_Aar_js = _value;
	}

	private double _Aad_js = 0; // ����һ�����ú���ҷ�

	public double getAad_js() {
		return _Aad_js;
	}

	public void setAad_js(double _value) {
		_Aad_js = _value;
	}

	private double _Ad_js = 0; // ���������ҷ�

	public double getAd_js() {
		return _Ad_js;
	}

	public void setAd_js(double _value) {
		_Ad_js = _value;
	}

	private double _Vad_js = 0; // ����һ�����ú���ӷ���

	public double getVad_js() {
		return _Vad_js;
	}

	public void setVad_js(double _value) {
		_Vad_js = _value;
	}
	
	private double _T2_js = 0; 	// ������ڵ�

	public double getT2_js() {
		return _T2_js;
	}

	public void setT2_js(double _value) {
		_T2_js = _value;
	}

	private double _Vdaf_js = 0; // ��������޻һ��ӷ���

	public double getVdaf_js() {
		return _Vdaf_js;
	}

	public void setVdaf_js(double _value) {
		_Vdaf_js = _value;
	}

	private double _Stad_js = 0; // ����һ�����ú��ȫ��

	public double getStad_js() {
		return _Stad_js;
	}

	public void setStad_js(double _value) {
		_Stad_js = _value;
	}

	private double _Had_js = 0; // ����һ�����ú����

	public double getHad_js() {
		return _Had_js;
	}

	public void setHad_js(double _value) {
		_Had_js = _value;
	}

	private double _Qbad_js = 0; // ���㵯Ͳ��ֵ

	public double getQbad_js() {
		return _Qbad_js;
	}

	public void setQbad_js(double _value) {
		_Qbad_js = _value;
	}

	private double _Qgrad_js = 0; // ����������λ��ֵ

	public double getQgrad_js() {
		return _Qgrad_js;
	}

	public void setQgrad_js(double _value) {
		_Qgrad_js = _value;
	}
	
	private double _Yunju_js = 0; // �����˾�

	public double getYunju_js() {
		return _Yunju_js;
	}

	public void setYunju_js(double _value) {
		_Yunju_js = _value;
	}
	
	private String _Yunju_jsbz ="";	// �˾ࣨ��Ӧ���е�yunj��varchar2���ֶΣ�
	
	public String getYunju_jsbz(){
		
		return _Yunju_jsbz;
	}
	
	public void setYunju_jsbz(String value){
		
		_Yunju_jsbz = value;
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
	
//	�Ӽ�ǰ����
	private double _jiajqdj = 0; 	// �Ӽ�ǰ���ۣ��ȿ����Ǻ�˰��Ҳ�����ǲ���˰�ۣ�

	public double getJiajqdj() {
		return _jiajqdj;
	}

	public void setJiajqdj(double _value) {
		_jiajqdj = _value;
	}
//	�Ӽ�ǰ����_End

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
	
	private double _Buhsyf_mk = 0; //���ڼ�¼������ú��ʱ�����˷Ѳ������ʱ��ֵ�����ڽ����ı���

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
	
	private double _Yunzfhj_mk = 0;	//���ڼ�¼������ú��ʱ�����˷Ѳ������ʱ��ֵ�����ڽ����ı���

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

	public String _Gongs_Mk = ""; // ��ʽ

	public String getGongs_Mk() {
		return _Gongs_Mk;
	}

	public void setGongs_Mk(String _value) {
		_Gongs_Mk = _value;
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
	
	private double Shulzb_yk;	//�����������ۼ�
	
	private double Qnetar_yk;
	
	private double Std_yk;
	
	private double Star_yk;
	
	private double Ad_yk;
	
	private double Vdaf_yk;
	
	private double Mt_yk;
	
	private double Qgrad_yk;
	
	private double Qbad_yk;
	
	private double Had_yk;
	
	private double Stad_yk;
	
	private double Mad_yk;
	
	private double Aar_yk;
	
	private double Aad_yk;
	
	private double Vad_yk;
	
	private double T2_yk;
	
	private double Yunju_yk;	//�˾�

	
	public double getShulzb_yk() {
		
		return Shulzb_yk;
	}
	
	public void setShulzb_yk(double shulzb_yk) {
		
		Shulzb_yk=shulzb_yk;
	}
	
	/**
	 * @return aad_yk
	 */
	public double getAad_yk() {
		return Aad_yk;
	}

	/**
	 * @param aad_yk Ҫ���õ� aad_yk
	 */
	public void setAad_yk(double aad_yk) {
		Aad_yk = aad_yk;
	}

	/**
	 * @return aar_yk
	 */
	public double getAar_yk() {
		return Aar_yk;
	}

	/**
	 * @param aar_yk Ҫ���õ� aar_yk
	 */
	public void setAar_yk(double aar_yk) {
		Aar_yk = aar_yk;
	}

	/**
	 * @return ad_yk
	 */
	public double getAd_yk() {
		return Ad_yk;
	}

	/**
	 * @param ad_yk Ҫ���õ� ad_yk
	 */
	public void setAd_yk(double ad_yk) {
		Ad_yk = ad_yk;
	}

	/**
	 * @return had_yk
	 */
	public double getHad_yk() {
		return Had_yk;
	}

	/**
	 * @param had_yk Ҫ���õ� had_yk
	 */
	public void setHad_yk(double had_yk) {
		Had_yk = had_yk;
	}

	/**
	 * @return mad_yk
	 */
	public double getMad_yk() {
		return Mad_yk;
	}

	/**
	 * @param mad_yk Ҫ���õ� mad_yk
	 */
	public void setMad_yk(double mad_yk) {
		Mad_yk = mad_yk;
	}

	/**
	 * @return mt_yk
	 */
	public double getMt_yk() {
		return Mt_yk;
	}

	/**
	 * @param mt_yk Ҫ���õ� mt_yk
	 */
	public void setMt_yk(double mt_yk) {
		Mt_yk = mt_yk;
	}

	/**
	 * @return qbad_yk
	 */
	public double getQbad_yk() {
		return Qbad_yk;
	}

	/**
	 * @param qbad_yk Ҫ���õ� qbad_yk
	 */
	public void setQbad_yk(double qbad_yk) {
		Qbad_yk = qbad_yk;
	}

	/**
	 * @return qgrad_yk
	 */
	public double getQgrad_yk() {
		return Qgrad_yk;
	}

	/**
	 * @param qgrad_yk Ҫ���õ� qgrad_yk
	 */
	public void setQgrad_yk(double qgrad_yk) {
		Qgrad_yk = qgrad_yk;
	}

	/**
	 * @return qnetar_yk
	 */
	public double getQnetar_yk() {
		return Qnetar_yk;
	}

	/**
	 * @param qnetar_yk Ҫ���õ� qnetar_yk
	 */
	public void setQnetar_yk(double qnetar_yk) {
		Qnetar_yk = qnetar_yk;
	}

	/**
	 * @return T2_yk
	 */
	public double getT2_yk() {
		return T2_yk;
	}

	/**
	 * @param T2_yk Ҫ���õ� T2_yk
	 */
	public void setT2_yk(double t2_yk) {
		T2_yk = t2_yk;
	}
	
	public double getYunju_yk() {
		return Yunju_yk;
	}

	/**
	 * @param Yunju_yk Ҫ���õ� Yunju_yk
	 */
	public void setYunju_yk(double yunju_yk) {
		Yunju_yk = yunju_yk;
	}

	/**
	 * @return stad_yk
	 */
	public double getStad_yk() {
		return Stad_yk;
	}

	/**
	 * @param stad_yk Ҫ���õ� stad_yk
	 */
	public void setStad_yk(double stad_yk) {
		Stad_yk = stad_yk;
	}

	/**
	 * @return std_yk
	 */
	public double getStd_yk() {
		return Std_yk;
	}

	/**
	 * @param std_yk Ҫ���õ� std_yk
	 */
	public void setStd_yk(double std_yk) {
		Std_yk = std_yk;
	}

	/**
	 * @return vad_yk
	 */
	public double getVad_yk() {
		return Vad_yk;
	}

	/**
	 * @param vad_yk Ҫ���õ� vad_yk
	 */
	public void setVad_yk(double vad_yk) {
		Vad_yk = vad_yk;
	}

	/**
	 * @return vdaf_yk
	 */
	public double getVdaf_yk() {
		return Vdaf_yk;
	}

	/**
	 * @param vdaf_yk Ҫ���õ� vdaf_yk
	 */
	public void setVdaf_yk(double vdaf_yk) {
		Vdaf_yk = vdaf_yk;
	}
	
	//ָ��ӯ��_End
	
//	ָ���۵���_Begin
	
	private double Shul_zdj;
	
	private double Shulzb_zdj;
	
	private double Qnetar_zdj;
	
	private double Std_zdj;
	
	private double Star_zdj;
	
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
	
	private double Yunju_zdj;	//�˾�
	
	/**
	 * @return aad_zdj
	 */
	public double getAad_zdj() {
		return Aad_zdj;
	}

	/**
	 * @param aad_zdj Ҫ���õ� aad_zdj
	 */
	public void setAad_zdj(double aad_zdj) {
		Aad_zdj = aad_zdj;
	}

	/**
	 * @return aar_zdj
	 */
	public double getAar_zdj() {
		return Aar_zdj;
	}

	/**
	 * @param aar_zdj Ҫ���õ� aar_zdj
	 */
	public void setAar_zdj(double aar_zdj) {
		Aar_zdj = aar_zdj;
	}

	/**
	 * @return ad_zdj
	 */
	public double getAd_zdj() {
		return Ad_zdj;
	}

	/**
	 * @param ad_zdj Ҫ���õ� ad_zdj
	 */
	public void setAd_zdj(double ad_zdj) {
		Ad_zdj = ad_zdj;
	}

	/**
	 * @return had_zdj
	 */
	public double getHad_zdj() {
		return Had_zdj;
	}

	/**
	 * @param had_zdj Ҫ���õ� had_zdj
	 */
	public void setHad_zdj(double had_zdj) {
		Had_zdj = had_zdj;
	}

	/**
	 * @return mad_zdj
	 */
	public double getMad_zdj() {
		return Mad_zdj;
	}

	/**
	 * @param mad_zdj Ҫ���õ� mad_zdj
	 */
	public void setMad_zdj(double mad_zdj) {
		Mad_zdj = mad_zdj;
	}

	/**
	 * @return mt_zdj
	 */
	public double getMt_zdj() {
		return Mt_zdj;
	}

	/**
	 * @param mt_zdj Ҫ���õ� mt_zdj
	 */
	public void setMt_zdj(double mt_zdj) {
		Mt_zdj = mt_zdj;
	}

	/**
	 * @return qbad_zdj
	 */
	public double getQbad_zdj() {
		return Qbad_zdj;
	}

	/**
	 * @param qbad_zdj Ҫ���õ� qbad_zdj
	 */
	public void setQbad_zdj(double qbad_zdj) {
		Qbad_zdj = qbad_zdj;
	}

	/**
	 * @return qgrad_zdj
	 */
	public double getQgrad_zdj() {
		return Qgrad_zdj;
	}

	/**
	 * @param qgrad_zdj Ҫ���õ� qgrad_zdj
	 */
	public void setQgrad_zdj(double qgrad_zdj) {
		Qgrad_zdj = qgrad_zdj;
	}

	/**
	 * @return qnetar_zdj
	 */
	public double getQnetar_zdj() {
		return Qnetar_zdj;
	}

	/**
	 * @param qnetar_zdj Ҫ���õ� qnetar_zdj
	 */
	public void setQnetar_zdj(double qnetar_zdj) {
		Qnetar_zdj = qnetar_zdj;
	}

	/**
	 * @return T2_zdj
	 */
	public double getT2_zdj() {
		return T2_zdj;
	}

	/**
	 * @param T2_zdj Ҫ���õ� T2_zdj
	 */
	public void setT2_zdj(double t2_zdj) {
		T2_zdj = t2_zdj;
	}
	
	public double getYunju_zdj() {
		return Yunju_zdj;
	}

	/**
	 * @param yunju_zdj Ҫ���õ� yunju_zdj
	 */
	public void setYunju_zdj(double yunju_zdj) {
		Yunju_zdj = yunju_zdj;
	}

	/**
	 * @return stad_zdj
	 */
	public double getStad_zdj() {
		return Stad_zdj;
	}

	/**
	 * @param stad_zdj Ҫ���õ� stad_zdj
	 */
	public void setStad_zdj(double stad_zdj) {
		Stad_zdj = stad_zdj;
	}

	/**
	 * @return std_zdj
	 */
	public double getStd_zdj() {
		return Std_zdj;
	}

	/**
	 * @param std_zdj Ҫ���õ� std_zdj
	 */
	public void setStd_zdj(double std_zdj) {
		Std_zdj = std_zdj;
	}

	/**
	 * @return vad_zdj
	 */
	public double getVad_zdj() {
		return Vad_zdj;
	}

	/**
	 * @param vad_zdj Ҫ���õ� vad_zdj
	 */
	public void setVad_zdj(double vad_zdj) {
		Vad_zdj = vad_zdj;
	}

	/**
	 * @return vdaf_zdj
	 */
	public double getVdaf_zdj() {
		return Vdaf_zdj;
	}

	/**
	 * @param vdaf_zdj Ҫ���õ� vdaf_zdj
	 */
	public void setVdaf_zdj(double vdaf_zdj) {
		Vdaf_zdj = vdaf_zdj;
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
	
//	����ָ���۵���_Begin
	/**
	 * @return shul_zdj
	 */
	public double getShulzb_zdj() {
		return Shulzb_zdj;
	}

	/**
	 * @param shul_zdj Ҫ���õ� shul_zdj
	 */
	public void setShulzb_zdj(double shulzb_zdj) {
		Shulzb_zdj = shulzb_zdj;
	}
//	����ָ���۵���_End
	
	
//	ָ���۵���_End
	
//	ָ���۽��_Begin
	
	private double Shul_zje;
	
	private double Shulzb_zje;
	
	private double Qnetar_zje;
	
	private double Std_zje;
	
	private double Star_zje;
	
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
	
	private double Yunju_zje;

	/**
	 * @return aad_zje
	 */
	public double getAad_zje() {
		return Aad_zje;
	}

	/**
	 * @param aad_zje Ҫ���õ� aad_zje
	 */
	public void setAad_zje(double aad_zje) {
		Aad_zje = aad_zje;
	}

	/**
	 * @return aar_zje
	 */
	public double getAar_zje() {
		return Aar_zje;
	}

	/**
	 * @param aar_zje Ҫ���õ� aar_zje
	 */
	public void setAar_zje(double aar_zje) {
		Aar_zje = aar_zje;
	}

	/**
	 * @return ad_zje
	 */
	public double getAd_zje() {
		return Ad_zje;
	}

	/**
	 * @param ad_zje Ҫ���õ� ad_zje
	 */
	public void setAd_zje(double ad_zje) {
		Ad_zje = ad_zje;
	}

	/**
	 * @return had_zje
	 */
	public double getHad_zje() {
		return Had_zje;
	}

	/**
	 * @param had_zje Ҫ���õ� had_zje
	 */
	public void setHad_zje(double had_zje) {
		Had_zje = had_zje;
	}

	/**
	 * @return mad_zje
	 */
	public double getMad_zje() {
		return Mad_zje;
	}

	/**
	 * @param mad_zje Ҫ���õ� mad_zje
	 */
	public void setMad_zje(double mad_zje) {
		Mad_zje = mad_zje;
	}

	/**
	 * @return mt_zje
	 */
	public double getMt_zje() {
		return Mt_zje;
	}

	/**
	 * @param mt_zje Ҫ���õ� mt_zje
	 */
	public void setMt_zje(double mt_zje) {
		Mt_zje = mt_zje;
	}

	/**
	 * @return qbad_zje
	 */
	public double getQbad_zje() {
		return Qbad_zje;
	}

	/**
	 * @param qbad_zje Ҫ���õ� qbad_zje
	 */
	public void setQbad_zje(double qbad_zje) {
		Qbad_zje = qbad_zje;
	}

	/**
	 * @return qgrad_zje
	 */
	public double getQgrad_zje() {
		return Qgrad_zje;
	}

	/**
	 * @param qgrad_zje Ҫ���õ� qgrad_zje
	 */
	public void setQgrad_zje(double qgrad_zje) {
		Qgrad_zje = qgrad_zje;
	}

	/**
	 * @return qnetar_zje
	 */
	public double getQnetar_zje() {
		return Qnetar_zje;
	}

	/**
	 * @param qnetar_zje Ҫ���õ� qnetar_zje
	 */
	public void setQnetar_zje(double qnetar_zje) {
		Qnetar_zje = qnetar_zje;
	}

	/**
	 * @return shul_zje
	 */
	public double getShul_zje() {
		return Shul_zje;
	}

	/**
	 * @param shul_zje Ҫ���õ� shul_zje
	 */
	public void setShul_zje(double shul_zje) {
		Shul_zje = shul_zje;
	}
	
//	����ָ���۽��_Begin
	
	/**
	 * @return shulzb_zje
	 */
	public double getShulzb_zje() {
		return Shulzb_zje;
	}

	/**
	 * @param shulzb_zje Ҫ���õ� shulzb_zje
	 */
	public void setShulzb_zje(double shulzb_zje) {
		Shulzb_zje = shulzb_zje;
	}
	
//	����ָ���۽��_End

	/**
	 * @return T2_zje
	 */
	public double getT2_zje() {
		return T2_zje;
	}

	/**
	 * @param T2_zje Ҫ���õ� T2_zje
	 */
	public void setT2_zje(double t2_zje) {
		T2_zje = t2_zje;
	}
	
	public double getYunju_zje() {
		return Yunju_zje;
	}

	/**
	 * @param Yunju_zje Ҫ���õ� Yunju_zje
	 */
	public void setYunju_zje(double yunju_zje) {
		Yunju_zje = yunju_zje;
	}

	/**
	 * @return stad_zje
	 */
	public double getStad_zje() {
		return Stad_zje;
	}

	/**
	 * @param stad_zje Ҫ���õ� stad_zje
	 */
	public void setStad_zje(double stad_zje) {
		Stad_zje = stad_zje;
	}

	/**
	 * @return std_zje
	 */
	public double getStd_zje() {
		return Std_zje;
	}

	/**
	 * @param std_zje Ҫ���õ� std_zje
	 */
	public void setStd_zje(double std_zje) {
		Std_zje = std_zje;
	}

	/**
	 * @return vad_zje
	 */
	public double getVad_zje() {
		return Vad_zje;
	}

	/**
	 * @param vad_zje Ҫ���õ� vad_zje
	 */
	public void setVad_zje(double vad_zje) {
		Vad_zje = vad_zje;
	}

	/**
	 * @return vdaf_zje
	 */
	public double getVdaf_zje() {
		return Vdaf_zje;
	}

	/**
	 * @param vdaf_zje Ҫ���õ� vdaf_zje
	 */
	public void setVdaf_zje(double vdaf_zje) {
		Vdaf_zje = vdaf_zje;
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

	public String getKuidjfyf() {
		return _Kuidjfyf;
	}

	public void setKuidjfyf(String kuidjfyf) {
		_Kuidjfyf = kuidjfyf;
	}
	
	public void setLiangpjsyfbjxkd(String value){
		
		_Liangpjsyfbjxkd = value;
	}
	
	public String getLiangpjsyfbjxkd(){
		
		return _Liangpjsyfbjxkd;
	}
	
	public String getHetbh(){
		
		return _Hetbh;
	}
	
	public void setHetbh(String hetbh){
		
		_Hetbh=hetbh;
	}

	public double getStar_yk() {
		return Star_yk;
	}

	public void setStar_yk(double star_yk) {
		Star_yk = star_yk;
	}

	public double getStar_zje() {
		return Star_zje;
	}

	public void setStar_zje(double star_zje) {
		Star_zje = star_zje;
	}

	public double getStar_zdj() {
		return Star_zdj;
	}

	public void setStar_zdj(double star_zdj) {
		Star_zdj = star_zdj;
	}
	
//	����ָ�����⴦���۽��
	private double Shul_zje_tscl;
	
	private double Qnetar_zje_tscl;
	
	private double Std_zje_tscl;
	
	private double Star_zje_tscl;
	
	private double Ad_zje_tscl;
	
	private double Vdaf_zje_tscl;
	
	private double Mt_zje_tscl;
	
	private double Qgrad_zje_tscl;
	
	private double Qbad_zje_tscl;
	
	private double Had_zje_tscl;
	
	private double Stad_zje_tscl;
	
	private double Mad_zje_tscl;
	
	private double Aar_zje_tscl;
	
	private double Aad_zje_tscl;
	
	private double Vad_zje_tscl;
	
	private double T2_zje_tscl;
	
	private double Yunju_zje_tscl;

	public double getAad_zje_tscl() {
		return Aad_zje_tscl;
	}

	public void setAad_zje_tscl(double aad_zje_tscl) {
		Aad_zje_tscl = aad_zje_tscl;
	}

	public double getAar_zje_tscl() {
		return Aar_zje_tscl;
	}

	public void setAar_zje_tscl(double aar_zje_tscl) {
		Aar_zje_tscl = aar_zje_tscl;
	}

	public double getAd_zje_tscl() {
		return Ad_zje_tscl;
	}

	public void setAd_zje_tscl(double ad_zje_tscl) {
		Ad_zje_tscl = ad_zje_tscl;
	}

	public double getHad_zje_tscl() {
		return Had_zje_tscl;
	}

	public void setHad_zje_tscl(double had_zje_tscl) {
		Had_zje_tscl = had_zje_tscl;
	}

	public double getMad_zje_tscl() {
		return Mad_zje_tscl;
	}

	public void setMad_zje_tscl(double mad_zje_tscl) {
		Mad_zje_tscl = mad_zje_tscl;
	}

	public double getMt_zje_tscl() {
		return Mt_zje_tscl;
	}

	public void setMt_zje_tscl(double mt_zje_tscl) {
		Mt_zje_tscl = mt_zje_tscl;
	}

	public double getQbad_zje_tscl() {
		return Qbad_zje_tscl;
	}

	public void setQbad_zje_tscl(double qbad_zje_tscl) {
		Qbad_zje_tscl = qbad_zje_tscl;
	}

	public double getQgrad_zje_tscl() {
		return Qgrad_zje_tscl;
	}

	public void setQgrad_zje_tscl(double qgrad_zje_tscl) {
		Qgrad_zje_tscl = qgrad_zje_tscl;
	}

	public double getQnetar_zje_tscl() {
		return Qnetar_zje_tscl;
	}

	public void setQnetar_zje_tscl(double qnetar_zje_tscl) {
		Qnetar_zje_tscl = qnetar_zje_tscl;
	}

	public double getShul_zje_tscl() {
		return Shul_zje_tscl;
	}

	public void setShul_zje_tscl(double shul_zje_tscl) {
		Shul_zje_tscl = shul_zje_tscl;
	}

	public double getStad_zje_tscl() {
		return Stad_zje_tscl;
	}

	public void setStad_zje_tscl(double stad_zje_tscl) {
		Stad_zje_tscl = stad_zje_tscl;
	}

	public double getStar_zje_tscl() {
		return Star_zje_tscl;
	}

	public void setStar_zje_tscl(double star_zje_tscl) {
		Star_zje_tscl = star_zje_tscl;
	}

	public double getStd_zje_tscl() {
		return Std_zje_tscl;
	}

	public void setStd_zje_tscl(double std_zje_tscl) {
		Std_zje_tscl = std_zje_tscl;
	}

	public double getT2_zje_tscl() {
		return T2_zje_tscl;
	}

	public void setT2_zje_tscl(double t2_zje_tscl) {
		T2_zje_tscl = t2_zje_tscl;
	}

	public double getVad_zje_tscl() {
		return Vad_zje_tscl;
	}

	public void setVad_zje_tscl(double vad_zje_tscl) {
		Vad_zje_tscl = vad_zje_tscl;
	}

	public double getVdaf_zje_tscl() {
		return Vdaf_zje_tscl;
	}

	public void setVdaf_zje_tscl(double vdaf_zje_tscl) {
		Vdaf_zje_tscl = vdaf_zje_tscl;
	}

	public double getYunju_zje_tscl() {
		return Yunju_zje_tscl;
	}

	public void setYunju_zje_tscl(double yunju_zje_tscl) {
		Yunju_zje_tscl = yunju_zje_tscl;
	}
	
//	����ָ�����⴦���۽��_end
	
//	����ָ�����⴦���۵���_begin
	private double Shul_zdj_tscl;
	
	private double Qnetar_zdj_tscl;
	
	private double Std_zdj_tscl;
	
	private double Star_zdj_tscl;
	
	private double Ad_zdj_tscl;
	
	private double Vdaf_zdj_tscl;
	
	private double Mt_zdj_tscl;
	
	private double Qgrad_zdj_tscl;
	
	private double Qbad_zdj_tscl;
	
	private double Had_zdj_tscl;
	
	private double Stad_zdj_tscl;
	
	private double Mad_zdj_tscl;
	
	private double Aar_zdj_tscl;
	
	private double Aad_zdj_tscl;
	
	private double Vad_zdj_tscl;
	
	private double T2_zdj_tscl;
	
	private double Yunju_zdj_tscl;	//�˾�

	public double getAad_zdj_tscl() {
		return Aad_zdj_tscl;
	}

	public void setAad_zdj_tscl(double aad_zdj_tscl) {
		Aad_zdj_tscl = aad_zdj_tscl;
	}

	public double getAar_zdj_tscl() {
		return Aar_zdj_tscl;
	}

	public void setAar_zdj_tscl(double aar_zdj_tscl) {
		Aar_zdj_tscl = aar_zdj_tscl;
	}

	public double getAd_zdj_tscl() {
		return Ad_zdj_tscl;
	}

	public void setAd_zdj_tscl(double ad_zdj_tscl) {
		Ad_zdj_tscl = ad_zdj_tscl;
	}

	public double getHad_zdj_tscl() {
		return Had_zdj_tscl;
	}

	public void setHad_zdj_tscl(double had_zdj_tscl) {
		Had_zdj_tscl = had_zdj_tscl;
	}

	public double getMad_zdj_tscl() {
		return Mad_zdj_tscl;
	}

	public void setMad_zdj_tscl(double mad_zdj_tscl) {
		Mad_zdj_tscl = mad_zdj_tscl;
	}

	public double getMt_zdj_tscl() {
		return Mt_zdj_tscl;
	}

	public void setMt_zdj_tscl(double mt_zdj_tscl) {
		Mt_zdj_tscl = mt_zdj_tscl;
	}

	public double getQbad_zdj_tscl() {
		return Qbad_zdj_tscl;
	}

	public void setQbad_zdj_tscl(double qbad_zdj_tscl) {
		Qbad_zdj_tscl = qbad_zdj_tscl;
	}

	public double getQgrad_zdj_tscl() {
		return Qgrad_zdj_tscl;
	}

	public void setQgrad_zdj_tscl(double qgrad_zdj_tscl) {
		Qgrad_zdj_tscl = qgrad_zdj_tscl;
	}

	public double getQnetar_zdj_tscl() {
		return Qnetar_zdj_tscl;
	}

	public void setQnetar_zdj_tscl(double qnetar_zdj_tscl) {
		Qnetar_zdj_tscl = qnetar_zdj_tscl;
	}

	public double getShul_zdj_tscl() {
		return Shul_zdj_tscl;
	}

	public void setShul_zdj_tscl(double shul_zdj_tscl) {
		Shul_zdj_tscl = shul_zdj_tscl;
	}

	public double getStad_zdj_tscl() {
		return Stad_zdj_tscl;
	}

	public void setStad_zdj_tscl(double stad_zdj_tscl) {
		Stad_zdj_tscl = stad_zdj_tscl;
	}

	public double getStar_zdj_tscl() {
		return Star_zdj_tscl;
	}

	public void setStar_zdj_tscl(double star_zdj_tscl) {
		Star_zdj_tscl = star_zdj_tscl;
	}

	public double getStd_zdj_tscl() {
		return Std_zdj_tscl;
	}

	public void setStd_zdj_tscl(double std_zdj_tscl) {
		Std_zdj_tscl = std_zdj_tscl;
	}

	public double getT2_zdj_tscl() {
		return T2_zdj_tscl;
	}

	public void setT2_zdj_tscl(double t2_zdj_tscl) {
		T2_zdj_tscl = t2_zdj_tscl;
	}

	public double getVad_zdj_tscl() {
		return Vad_zdj_tscl;
	}

	public void setVad_zdj_tscl(double vad_zdj_tscl) {
		Vad_zdj_tscl = vad_zdj_tscl;
	}

	public double getVdaf_zdj_tscl() {
		return Vdaf_zdj_tscl;
	}

	public void setVdaf_zdj_tscl(double vdaf_zdj_tscl) {
		Vdaf_zdj_tscl = vdaf_zdj_tscl;
	}

	public double getYunju_zdj_tscl() {
		return Yunju_zdj_tscl;
	}

	public void setYunju_zdj_tscl(double yunju_zdj_tscl) {
		Yunju_zdj_tscl = yunju_zdj_tscl;
	}

//	����ָ�����⴦���۵���_end
	
//	��Ҫ�浽jiesb�е��˷ѽ�����Ϣ
	public double getBuhsyf_mk() {
		return _Buhsyf_mk;
	}

	public void setBuhsyf_mk(double buhsyf_mk) {
		_Buhsyf_mk = buhsyf_mk;
	}

	public double getYunfjsdj_mk() {
		return _Yunfjsdj_mk;
	}

	public void setYunfjsdj_mk(double yunfjsdj_mk) {
		_Yunfjsdj_mk = yunfjsdj_mk;
	}

	public double getYunfjsl_mk() {
		return _Yunfjsl_mk;
	}

	public void setYunfjsl_mk(double yunfjsl_mk) {
		_Yunfjsl_mk = yunfjsl_mk;
	}

	public double getYunzfhj_mk() {
		return _Yunzfhj_mk;
	}

	public void setYunzfhj_mk(double yunzfhj_mk) {
		_Yunzfhj_mk = yunzfhj_mk;
	}
//	��Ҫ�浽jiesb�е��˷ѽ�����Ϣ_end

//	�������ָ������������_begin
	private double Shul_zsl;
	
	private double Shulzb_zsl;
	
	private double Qnetar_zsl;
	
	private double Std_zsl;
	
	private double Star_zsl;
	
	private double Ad_zsl;
	
	private double Vdaf_zsl;
	
	private double Mt_zsl;
	
	private double Qgrad_zsl;
	
	private double Qbad_zsl;
	
	private double Had_zsl;
	
	private double Stad_zsl;
	
	private double Mad_zsl;
	
	private double Aar_zsl;
	
	private double Aad_zsl;
	
	private double Vad_zsl;
	
	private double T2_zsl;
	
	private double Yunju_zsl;	//�˾� 
	
//	��������λ
	
	private String Shul_zsldw;
	
	private String Shulzb_zsldw;
	
	private String Qnetar_zsldw;
	
	private String Std_zsldw;
	
	private String Star_zsldw;
	
	private String Ad_zsldw;
	
	private String Vdaf_zsldw;
	
	private String Mt_zsldw;
	
	private String Qgrad_zsldw;
	
	private String Qbad_zsldw;
	
	private String Had_zsldw;
	
	private String Stad_zsldw;
	
	private String Mad_zsldw;
	
	private String Aar_zsldw;
	
	private String Aad_zsldw;
	
	private String Vad_zsldw;
	
	private String T2_zsldw;
	
	private String Yunju_zsldw;	//�˾� 

	public double getAad_zsl() {
		return Aad_zsl;
	}

	public void setAad_zsl(double aad_zsl) {
		Aad_zsl = aad_zsl;
	}

	public double getAar_zsl() {
		return Aar_zsl;
	}

	public void setAar_zsl(double aar_zsl) {
		Aar_zsl = aar_zsl;
	}

	public double getAd_zsl() {
		return Ad_zsl;
	}

	public void setAd_zsl(double ad_zsl) {
		Ad_zsl = ad_zsl;
	}

	public double getHad_zsl() {
		return Had_zsl;
	}

	public void setHad_zsl(double had_zsl) {
		Had_zsl = had_zsl;
	}

	public double getMad_zsl() {
		return Mad_zsl;
	}

	public void setMad_zsl(double mad_zsl) {
		Mad_zsl = mad_zsl;
	}

	public double getMt_zsl() {
		return Mt_zsl;
	}

	public void setMt_zsl(double mt_zsl) {
		Mt_zsl = mt_zsl;
	}

	public double getQbad_zsl() {
		return Qbad_zsl;
	}

	public void setQbad_zsl(double qbad_zsl) {
		Qbad_zsl = qbad_zsl;
	}

	public double getQgrad_zsl() {
		return Qgrad_zsl;
	}

	public void setQgrad_zsl(double qgrad_zsl) {
		Qgrad_zsl = qgrad_zsl;
	}

	public double getQnetar_zsl() {
		return Qnetar_zsl;
	}

	public void setQnetar_zsl(double qnetar_zsl) {
		Qnetar_zsl = qnetar_zsl;
	}

	public double getShul_zsl() {
		return Shul_zsl;
	}

	public void setShul_zsl(double shul_zsl) {
		Shul_zsl = shul_zsl;
	}

	public double getShulzb_zsl() {
		return Shulzb_zsl;
	}

	public void setShulzb_zsl(double shulzb_zsl) {
		Shulzb_zsl = shulzb_zsl;
	}

	public double getStad_zsl() {
		return Stad_zsl;
	}

	public void setStad_zsl(double stad_zsl) {
		Stad_zsl = stad_zsl;
	}

	public double getStar_zsl() {
		return Star_zsl;
	}

	public void setStar_zsl(double star_zsl) {
		Star_zsl = star_zsl;
	}

	public double getStd_zsl() {
		return Std_zsl;
	}

	public void setStd_zsl(double std_zsl) {
		Std_zsl = std_zsl;
	}

	public double getT2_zsl() {
		return T2_zsl;
	}

	public void setT2_zsl(double t2_zsl) {
		T2_zsl = t2_zsl;
	}

	public double getVad_zsl() {
		return Vad_zsl;
	}

	public void setVad_zsl(double vad_zsl) {
		Vad_zsl = vad_zsl;
	}

	public double getVdaf_zsl() {
		return Vdaf_zsl;
	}

	public void setVdaf_zsl(double vdaf_zsl) {
		Vdaf_zsl = vdaf_zsl;
	}

	public double getYunju_zsl() {
		return Yunju_zsl;
	}

	public void setYunju_zsl(double yunju_zsl) {
		Yunju_zsl = yunju_zsl;
	}

	public String getAad_zsldw() {
		return Aad_zsldw;
	}

	public void setAad_zsldw(String aad_zsldw) {
		Aad_zsldw = aad_zsldw;
	}

	public String getAar_zsldw() {
		return Aar_zsldw;
	}

	public void setAar_zsldw(String aar_zsldw) {
		Aar_zsldw = aar_zsldw;
	}

	public String getAd_zsldw() {
		return Ad_zsldw;
	}

	public void setAd_zsldw(String ad_zsldw) {
		Ad_zsldw = ad_zsldw;
	}

	public String getHad_zsldw() {
		return Had_zsldw;
	}

	public void setHad_zsldw(String had_zsldw) {
		Had_zsldw = had_zsldw;
	}

	public String getMad_zsldw() {
		return Mad_zsldw;
	}

	public void setMad_zsldw(String mad_zsldw) {
		Mad_zsldw = mad_zsldw;
	}

	public String getMt_zsldw() {
		return Mt_zsldw;
	}

	public void setMt_zsldw(String mt_zsldw) {
		Mt_zsldw = mt_zsldw;
	}

	public String getQbad_zsldw() {
		return Qbad_zsldw;
	}

	public void setQbad_zsldw(String qbad_zsldw) {
		Qbad_zsldw = qbad_zsldw;
	}

	public String getQgrad_zsldw() {
		return Qgrad_zsldw;
	}

	public void setQgrad_zsldw(String qgrad_zsldw) {
		Qgrad_zsldw = qgrad_zsldw;
	}

	public String getQnetar_zsldw() {
		return Qnetar_zsldw;
	}

	public void setQnetar_zsldw(String qnetar_zsldw) {
		Qnetar_zsldw = qnetar_zsldw;
	}

	public String getShul_zsldw() {
		return Shul_zsldw;
	}

	public void setShul_zsldw(String shul_zsldw) {
		Shul_zsldw = shul_zsldw;
	}

	public String getShulzb_zsldw() {
		return Shulzb_zsldw;
	}

	public void setShulzb_zsldw(String shulzb_zsldw) {
		Shulzb_zsldw = shulzb_zsldw;
	}

	public String getStad_zsldw() {
		return Stad_zsldw;
	}

	public void setStad_zsldw(String stad_zsldw) {
		Stad_zsldw = stad_zsldw;
	}

	public String getStar_zsldw() {
		return Star_zsldw;
	}

	public void setStar_zsldw(String star_zsldw) {
		Star_zsldw = star_zsldw;
	}

	public String getStd_zsldw() {
		return Std_zsldw;
	}

	public void setStd_zsldw(String std_zsldw) {
		Std_zsldw = std_zsldw;
	}

	public String getT2_zsldw() {
		return T2_zsldw;
	}

	public void setT2_zsldw(String t2_zsldw) {
		T2_zsldw = t2_zsldw;
	}

	public String getVad_zsldw() {
		return Vad_zsldw;
	}

	public void setVad_zsldw(String vad_zsldw) {
		Vad_zsldw = vad_zsldw;
	}

	public String getVdaf_zsldw() {
		return Vdaf_zsldw;
	}

	public void setVdaf_zsldw(String vdaf_zsldw) {
		Vdaf_zsldw = vdaf_zsldw;
	}

	public String getYunju_zsldw() {
		return Yunju_zsldw;
	}

	public void setYunju_zsldw(String yunju_zsldw) {
		Yunju_zsldw = yunju_zsldw;
	}
//	�������ָ������������_end
	
//	�������ν����Ǽ�Ȩƽ��ָ�������_begin
	
//	1������ָ���static_status �����ָ��Ϊ�����ν����еļ�Ȩƽ�������ָ���static_status Ϊtrue,����Ϊ false
	
	private boolean Shul_static_status = false;
	
	private boolean Shulzb_static_status = false;
	
	private boolean Qnetar_static_status = false;
	
	private boolean Std_static_status = false;
	
	private boolean Star_static_status = false;
	
	private boolean Ad_static_status = false;
	
	private boolean Vdaf_static_status = false;
	
	private boolean Mt_static_status = false;
	
	private boolean Qgrad_static_status = false;
	
	private boolean Qbad_static_status = false;
	
	private boolean Had_static_status = false;
	
	private boolean Stad_static_status = false;
	
	private boolean Mad_static_status = false;
	
	private boolean Aar_static_status = false;
	
	private boolean Aad_static_status = false;
	
	private boolean Vad_static_status = false;
	
	private boolean T2_static_status = false;
	
	private boolean Yunju_static_status = false;	//�˾� 

	public boolean isAad_static_status() {
		return Aad_static_status;
	}

	public void setAad_static_status(boolean aad_static_status) {
		Aad_static_status = aad_static_status;
	}

	public boolean isAar_static_status() {
		return Aar_static_status;
	}

	public void setAar_static_status(boolean aar_static_status) {
		Aar_static_status = aar_static_status;
	}

	public boolean isAd_static_status() {
		return Ad_static_status;
	}

	public void setAd_static_status(boolean ad_static_status) {
		Ad_static_status = ad_static_status;
	}

	public boolean isHad_static_status() {
		return Had_static_status;
	}

	public void setHad_static_status(boolean had_static_status) {
		Had_static_status = had_static_status;
	}

	public boolean isMad_static_status() {
		return Mad_static_status;
	}

	public void setMad_static_status(boolean mad_static_status) {
		Mad_static_status = mad_static_status;
	}

	public boolean isMt_static_status() {
		return Mt_static_status;
	}

	public void setMt_static_status(boolean mt_static_status) {
		Mt_static_status = mt_static_status;
	}

	public boolean isQbad_static_status() {
		return Qbad_static_status;
	}

	public void setQbad_static_status(boolean qbad_static_status) {
		Qbad_static_status = qbad_static_status;
	}

	public boolean isQgrad_static_status() {
		return Qgrad_static_status;
	}

	public void setQgrad_static_status(boolean qgrad_static_status) {
		Qgrad_static_status = qgrad_static_status;
	}

	public boolean isQnetar_static_status() {
		return Qnetar_static_status;
	}

	public void setQnetar_static_status(boolean qnetar_static_status) {
		Qnetar_static_status = qnetar_static_status;
	}

	public boolean isShul_static_status() {
		return Shul_static_status;
	}

	public void setShul_static_status(boolean shul_static_status) {
		Shul_static_status = shul_static_status;
	}

	public boolean isShulzb_static_status() {
		return Shulzb_static_status;
	}

	public void setShulzb_static_status(boolean shulzb_static_status) {
		Shulzb_static_status = shulzb_static_status;
	}

	public boolean isStad_static_status() {
		return Stad_static_status;
	}

	public void setStad_static_status(boolean stad_static_status) {
		Stad_static_status = stad_static_status;
	}

	public boolean isStar_static_status() {
		return Star_static_status;
	}

	public void setStar_static_status(boolean star_static_status) {
		Star_static_status = star_static_status;
	}

	public boolean isStd_static_status() {
		return Std_static_status;
	}

	public void setStd_static_status(boolean std_static_status) {
		Std_static_status = std_static_status;
	}

	public boolean isT2_static_status() {
		return T2_static_status;
	}

	public void setT2_static_status(boolean t2_static_status) {
		T2_static_status = t2_static_status;
	}

	public boolean isVad_static_status() {
		return Vad_static_status;
	}

	public void setVad_static_status(boolean vad_static_status) {
		Vad_static_status = vad_static_status;
	}

	public boolean isVdaf_static_status() {
		return Vdaf_static_status;
	}

	public void setVdaf_static_status(boolean vdaf_static_status) {
		Vdaf_static_status = vdaf_static_status;
	}

	public boolean isYunju_static_status() {
		return Yunju_static_status;
	}

	public void setYunju_static_status(boolean yunju_static_status) {
		Yunju_static_status = yunju_static_status;
	}
	
//	2������ָ���static_value �����ָ��Ϊ�����ν����еļ�Ȩƽ�������ָ���static_value Ϊ��ָ���Ȩƽ����ֵ
	
	private double Shul_static_value = 0;
	
	private double Shulzb_static_value = 0;
	
	private double Qnetar_static_value = 0;
	
	private double Std_static_value = 0;
	
	private double Star_static_value = 0;
	
	private double Ad_static_value = 0;
	
	private double Vdaf_static_value = 0;
	
	private double Mt_static_value = 0;
	
	private double Qgrad_static_value = 0;
	
	private double Qbad_static_value = 0;
	
	private double Had_static_value = 0;
	
	private double Stad_static_value = 0;
	
	private double Mad_static_value = 0;
	
	private double Aar_static_value = 0;
	
	private double Aad_static_value = 0;
	
	private double Vad_static_value = 0;
	
	private double T2_static_value = 0;
	
	private double Yunju_static_value = 0;	//�˾� 

	public double getAad_static_value() {
		return Aad_static_value;
	}

	public void setAad_static_value(double aad_static_value) {
		Aad_static_value = aad_static_value;
	}

	public double getAar_static_value() {
		return Aar_static_value;
	}

	public void setAar_static_value(double aar_static_value) {
		Aar_static_value = aar_static_value;
	}

	public double getAd_static_value() {
		return Ad_static_value;
	}

	public void setAd_static_value(double ad_static_value) {
		Ad_static_value = ad_static_value;
	}

	public double getHad_static_value() {
		return Had_static_value;
	}

	public void setHad_static_value(double had_static_value) {
		Had_static_value = had_static_value;
	}

	public double getMad_static_value() {
		return Mad_static_value;
	}

	public void setMad_static_value(double mad_static_value) {
		Mad_static_value = mad_static_value;
	}

	public double getMt_static_value() {
		return Mt_static_value;
	}

	public void setMt_static_value(double mt_static_value) {
		Mt_static_value = mt_static_value;
	}

	public double getQbad_static_value() {
		return Qbad_static_value;
	}

	public void setQbad_static_value(double qbad_static_value) {
		Qbad_static_value = qbad_static_value;
	}

	public double getQgrad_static_value() {
		return Qgrad_static_value;
	}

	public void setQgrad_static_value(double qgrad_static_value) {
		Qgrad_static_value = qgrad_static_value;
	}

	public double getQnetar_static_value() {
		return Qnetar_static_value;
	}

	public void setQnetar_static_value(double qnetar_static_value) {
		Qnetar_static_value = qnetar_static_value;
	}

	public double getShul_static_value() {
		return Shul_static_value;
	}

	public void setShul_static_value(double shul_static_value) {
		Shul_static_value = shul_static_value;
	}

	public double getShulzb_static_value() {
		return Shulzb_static_value;
	}

	public void setShulzb_static_value(double shulzb_static_value) {
		Shulzb_static_value = shulzb_static_value;
	}

	public double getStad_static_value() {
		return Stad_static_value;
	}

	public void setStad_static_value(double stad_static_value) {
		Stad_static_value = stad_static_value;
	}

	public double getStar_static_value() {
		return Star_static_value;
	}

	public void setStar_static_value(double star_static_value) {
		Star_static_value = star_static_value;
	}

	public double getStd_static_value() {
		return Std_static_value;
	}

	public void setStd_static_value(double std_static_value) {
		Std_static_value = std_static_value;
	}

	public double getT2_static_value() {
		return T2_static_value;
	}

	public void setT2_static_value(double t2_static_value) {
		T2_static_value = t2_static_value;
	}

	public double getVad_static_value() {
		return Vad_static_value;
	}

	public void setVad_static_value(double vad_static_value) {
		Vad_static_value = vad_static_value;
	}

	public double getVdaf_static_value() {
		return Vdaf_static_value;
	}

	public void setVdaf_static_value(double vdaf_static_value) {
		Vdaf_static_value = vdaf_static_value;
	}

	public double getYunju_static_value() {
		return Yunju_static_value;
	}

	public void setYunju_static_value(double yunju_static_value) {
		Yunju_static_value = yunju_static_value;
	}
	
//	�������ν����Ǽ�Ȩƽ��ָ�������_end
	
//	���������ʽΪ����Ȩ���С��Ľ�������_begin
//	����������Ϊ�˼�¼"��Ȩ����"�㷨�е�ͳһ���ۿ���Ŀ
	
	private String[][] Jiaqfl_zhibsz=null;	//��Ȩ����ָ������
	
	public String[][] getJiaqfl_zhibsz(){
		
		return Jiaqfl_zhibsz;
	}
	
	public void setJiaqfl_zhibsz(String[][] value){
		
		Jiaqfl_zhibsz = value;
	}

	public double getBaifbdsl() {
		return _Baifbdsl;
	}

	public void setBaifbdsl(double Baifbdsl) {
		this._Baifbdsl = Baifbdsl;
	}
//	���������ʽΪ����Ȩ���С��Ľ�������_end
	
private double _Kouk_js = 0;
	
	public double getKouk_js(){
		return _Kouk_js;
	}
	public void setKouk_js(double value){
		_Kouk_js = value;
	}
	//���ֿۿ�
	private boolean Shul_xb_kou_static_status = false;
	public boolean isShul_xb_kou_static_status() {
		return Shul_xb_kou_static_status;
	}
	public void setShul_xb_kou_static_status(boolean shul_xb_kou_static_status) {
		Shul_xb_kou_static_status = shul_xb_kou_static_status;
	}
	
	private double Shul_xb_kou_static_value = 0;
	public double getShul_xb_kou_static_value() {
		return Shul_xb_kou_static_value;
	}
	public void setShul_xb_kou_static_value(double shul_xb_kou_static_value) {
		Shul_xb_kou_static_value = shul_xb_kou_static_value;
	}
	//���ֽ���
	private boolean Shul_xb_jiang_static_status = false;
	public boolean isShul_xb_jiang_static_status() {
		return Shul_xb_jiang_static_status;
	}
	public void setShul_xb_jiang_static_status(boolean shul_xb_jiang_static_status) {
		Shul_xb_jiang_static_status = shul_xb_jiang_static_status;
	}
	
	private double Shul_xb_jiang_static_value = 0;
	public double getShul_xb_jiang_static_value() {
		return Shul_xb_jiang_static_value;
	}
	public void setShul_xb_jiang_static_value(double shul_xb_jiang_static_value) {
		Shul_xb_jiang_static_value = shul_xb_jiang_static_value;
	}
	
	//��̬����
	public double getShul_xb_dongt_static_value() {
		return Shul_xb_jiang_static_value;
	}
	public void setShul_xb_dongt_static_value(double shul_xb_jiang_static_value) {
		Shul_xb_jiang_static_value = shul_xb_jiang_static_value;
	}
	
//	�������Ƿ�ʹ�ü�Ȩֵ
	private boolean Shul_rz_szs_jiang_static_status = false;
	public boolean isShul_rz_szs_jiang_static_status() {
		return Shul_rz_szs_jiang_static_status;
	}
	public void setShul_rz_szs_jiang_static_status(boolean shul_xb_jiang_static_status) {
		Shul_rz_szs_jiang_static_status = shul_xb_jiang_static_status;
	}
	
	private double Shul_rz_szs_jiang_static_value = 0;
	public double getShul_rz_szs_jiang_static_value() {
		return Shul_rz_szs_jiang_static_value;
	}
	public void setShul_rz_szs_jiang_static_value(double _Shul_rz_szs_jiang_static_value) {
		Shul_rz_szs_jiang_static_value = _Shul_rz_szs_jiang_static_value;
	}
	
//	��%��ʱ������ϵ��Ϊ��������
	private boolean koud_shulxs_yanssl = false;
	public void setKoud_shulxs_yanssl(boolean koud_shulxs_yanssl){
		this.koud_shulxs_yanssl = koud_shulxs_yanssl;
	}
	public boolean getKoud_shulxs_yanssl(){
		return koud_shulxs_yanssl;
	}
//	��%��ʱ��һ��ú��ָ��۶֣����ձ���λ��
	private int koud_plm_ws = 5;
	public void setKoud_plm_ws(int koud_plm_ws){
		this.koud_plm_ws = koud_plm_ws;
	}
	public int getKoud_plm_ws(){
		return koud_plm_ws;
	}
//	������ȡ����ʽ
	private String jiesjeqzfs = "Round_new"; 
	public void setJiesjeqzfs(String jiesjeqzfs){
		this.jiesjeqzfs = jiesjeqzfs;
	}
	public String getJiesjeqzfs(){
		return jiesjeqzfs;
	}
	
//	����۶������Ƿ���㵥��
	private boolean koudcxjsdj = true; 
	public void setKoudcxjsdj(boolean koudcxjsdj){
		this.koudcxjsdj = koudcxjsdj;
	}
	public boolean getKoudcxjsdj(){
		return koudcxjsdj;
	}
}
