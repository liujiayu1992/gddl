package com.zhiren.common;
/*
 * ���ߣ�����
 * ʱ�䣺2009-08-26 10��08
 * ���������ӳ����sql(SQL_Cheb)
 */
public class SysConstant {
//	�������
	public static final int JIB_JT = 1;
	public static final int JIB_GS = 2;
	public static final int JIB_DC = 3;
//	�������
	public static final int ErrCode_unKnow = 0; 
	public static final int ErrCode_illLogin = 1; 
	public static final int ErrCode_noUser = 2; 
	public static final int ErrCode_errPwd = 3; 
	public static final int ErrCode_noPower = 4; 
	public static final int ErrCode_errdb = 5;  
	public static final int ErrCode_IeVar = 6; 
	
	public static final String FieldSpliter = "~#";
	public static final String LineSpliter = "~~";
//	�������뷽ʽ
	public static final String Caiy_GroupType_hc = "���˲������";
	public static final String Caiy_GroupType_qc = "���˲������";
//	oracle ��������
	public static final String Oracle_DataType_Number = "NUMBER";
	public static final String Oracle_DataType_Date = "DATE";
//	ext��������
	public static final String Ext_DataType_Number = "float";
	public static final String Ext_DataType_String = "string";
	public static final String Ext_DataType_Date = "date";
//	ext�󶨸�ʽ������
	public static final String Ext_Renderer_Date = "function(value){ return (value==null || value=='')?'':('object' != typeof(value)?value:value.dateFormat('Y-m-d'));}";
	public static final String Ext_Renderer_usMoney = "'usMoney'";
//	�ⵥ������
	public static final int Hengd_Auto_maoz = 0;
	public static final int Hengd_Auto_piz = 1;
	public static final int Hengd_Auto_all = 2;
	public static final int Hengd_Manual_maoz = 3;
	public static final int Hengd_Manual_piz = 4;
	public static final int Hengd_Manual_all = 5;
//	��ť�ύ����
	public static final int SaveMode_Allsubmit = 0;
	public static final int SaveMode_Selsubmit = 1;
	public static final int SaveMode_Upsubmit = 2;
	
//	���䷽ʽ
	public static final int YUNSFS_HUOY = 1;
	public static final int YUNSFS_QIY = 2;
	public static final int YUNSFS_HaiY = 3;
	public static final int YUNSFS_Pidc = 4;
//	�ƻ��ھ�
	public static final int JIHKJ_ZD = 1;
	public static final int JIHKJ_SC = 2;
	public static final int JIHKJ_QY = 3;
	public static final int JIHKJ_NONE = 4;
//	����վid
	public static final int Chez_q = 1;
	public static final int Chez_pdc = 2;
//	��Ƥ��hedbz����
	public static final int HEDBZ_LR = 0;
	public static final int HEDBZ_TJ = 1;	//�˵����ύ
	public static final int HEDBZ_YJJ = 2;	//�Ѽ��
	public static final int HEDBZ_YSH = 3;	//���������
	public static final int HEDBZ_YDP = 4;	//�ѶԻ�Ʊ
	
//	������
	public static final int CHEB_LC = 1;
	public static final int CHEB_ZB = 2;
	public static final int CHEB_QC = 3;
	public static final int CHEB_C = 4;
	public static final int CHEB_PDC = 5;
	
//	������㷽ʽ
	public static final String CountType_Yuns_dc = "����";
	public static final String CountType_Yuns_fp = "����";
	
//	�ӿ����·��(·������ʹ��ϵͳ�����е�����·��)
//	public static final String WS_errLogPath = "D:/zhiren/logs";//������־·��
	public static final String WS_errLogFileName = "stderr.log";//������־����
//	public static final String WS_infoLogPath = "D:/zhiren/logs";//��Ϣ��־·��
	public static final String WS_infoLogFileName = "stdout.log";//��Ϣ��־����
//	public static final String WS_ReceiveFilePath = "D:/zhiren/webservice/receive";//�����ļ�Ŀ¼
//	public static final String WS_ReceiveBakFilePath = "D:/zhiren/webservice/receivebak";//�����ļ�Ŀ¼
//	public static final String WS_SendFilePath = "D:/zhiren/webservice/send";//�����ļ�Ŀ¼
//	�ӿڴ�����
	public static final int WS_EC_ValiUserfail = 100;//�û�����֤ʧ��
	public static final int WS_EC_ValiPwdfail = 101;//������֤ʧ��
	public static final int WS_EC_ValiGuidfail = 200;//Guid��֤ʧ��
	public static final int WS_EC_CreateGuidfail = 300;//�����ļ���Ŀ¼ʧ��
	public static final int WS_EC_CreateFilefail = 301;//�����ļ���Ŀ¼ʧ��
	public static final int WS_EC_FileNotFound = 400; //ָ���ļ�δ�ҵ�
	public static final int WS_EC_IOExp = 500; //�ļ���дʱ��������
	public static final int WS_EC_JDOMExp = 600; //xml�ļ���ʽ����ȷ
	public static final int WS_EC_En_Decryptfail = 700; //�ļ����ܽ��ܹ��̷�������
	public static final int WS_EC_Success = 0; //�ɹ�
	
//	��ťͼƬ
	public static final String Btn_Icon_Cancel = "imgs/btnicon/cancel.gif";
	public static final String Btn_Icon_Copy = "imgs/btnicon/copy.gif";
	public static final String Btn_Icon_Count = "imgs/btnicon/count.gif";
	public static final String Btn_Icon_Create = "imgs/btnicon/create.gif";
	public static final String Btn_Icon_Delete = "imgs/btnicon/delete.gif";
	public static final String Btn_Icon_Insert = "imgs/btnicon/insert.gif";
	public static final String Btn_Icon_Print = "imgs/btnicon/print.gif";
	public static final String Btn_Icon_Refurbish = "imgs/btnicon/refurbish.gif";
	public static final String Btn_Icon_Return = "imgs/btnicon/return.gif";
	public static final String Btn_Icon_Save = "imgs/btnicon/save.gif";
	public static final String Btn_Icon_Search = "imgs/btnicon/search.gif";
	public static final String Btn_Icon_SelSubmit = "imgs/btnicon/selsubmit.gif";
	public static final String Btn_Icon_Show = "imgs/btnicon/show.gif";
	
//	�����޸ĵ�����
	public static final String Shenqxglx_shulyb = "�����±�";
	
//	�����޸ĵı�ʶ����
	public static final int Shenqxgbslx_id = 0;
	public static final int Shenqxgbslx_rq = 1;
	
//	round function name
	public static final String RoundFunction = "round";
	
//	����ĳ���ú̿��¯ǰ��������
	public static final long Rulqqtfy = 15;

//	XML��ʽ��Ϣ
		public static final String Gs_WZ_Xtsz = "��ʽ���λ��";
		//����
		public static final String Gs_JS_FilePath = "D:/zhiren/gongs";
		public static final String Gs_JS_FileName = "Jiesgs.xml";
		public static final String Gs_JS_RootName = "���㹫ʽ";
		public static final String Gs_JS_HeadName_DIANCXXB_ID = "DIANCXXB_ID";
		public static final String Gs_JS_HeadName_Mk = "ú�ʽ";
		public static final String Gs_JS_HeadName_Yf = "�˷ѹ�ʽ";
		public static final String Gs_JS_HeadName_Shih = "ʯ��ʯ��ʽ";
		public static final String Gs_JS_ChildName_Blcsh = "������ʼ��";
		public static final String Gs_JS_ChildName_Gyff = "���÷���";
		public static final String Gs_JS_ChildName_Jsgc = "�������";
		//����_End
		
//	Ʒ��������ֻ����ú�����ݵ�SQL
	public static final String SQL_Pinz_mei = "select id,mingc from pinzb where leib = 'ú' order by mingc";
	public static final String SQL_xiecfs = "select id,mingc from xiecfsb";
	public static final String SQL_Kouj = "select id,mingc from jihkjb";
	public static final String SQL_yunsfs = "select id,mingc from yunsfsb";
	public static final String SQL_Shihgys = "select id,piny||mingc from shihgysb";
	public static final String SQL_Shihpz = "select id,piny||mingc from shihpzb";
	public static final String SQL_Meic = "select id, mingc from meicb";
	public static final String SQL_Cheb = "select id,mingc from chebb";
	public 	static final String SQL_Pdcx="select id,bianm from pand_gd order by bianm desc";
	public static final String SQL_Yunsdw = " select id,mingc from yunsdwb";
	public static final String SQL_Yunsdw_mc = "select -1 id,'ȫ��' mingc from dual union all select  id,mingc from yunsdwb";
	//�е�Ͷ��ú���㷨,��ú��(ʵ����)�㷨
	public static  String LaimField="  (round_new(sum(fh.biaoz),0)+round_new(sum(fh.yingd),0)-round_new(sum(fh.yingd-fh.yingk),0)) ";
	
	public static  String LaimField1="  (round_new(sum(biaoz),0)+round_new(sum(yingd),0)-round_new(sum(yingd-yingk),0)) ";
	
//	�������ɲ�������� �������������ֵ
	public static String BiascCodeSequenceName = "XL_BiascCode";
	
	public static String RuLJQL="��¯����ú��";

	
//	���̶������ж�Ӧ�Ĳ�����������
	public static String Liucdz_ShenhOrHuitBtxyj="��˻���˲���д���";	//��������ᵯ������������_�����д�����棬�û�������д��������
	
//	�Զ��屨���ԱȲ�ѯ�еĴ��붨��
	public static String CustomAttribute_DataSource = "DataSrc";
	public static String CustomAttribute_DataSourceCol = "DataSrcCol";
	public static String CustomAttribute_SourceParamCol = "ParamCol";
	public static String CustomAttribute_ParamPointBegin = "ParamPointBegin";
	public static String CustomAttribute_ParamPointEnd = "ParamPointEnd";
	public static String CustomAttribute_ColWord = "ColWord";
	public static String CustomAttribute_ColHead = "ColHead";
	public static String CustomAttribute_ColSubHead = "ColSubHead";
	public static String CustomAttribute_ColWidth = "ColWidth";
	public static String CustomAttribute_ColFormat = "ColFormat";
	public static String CustomAttribute_ColOperational = "ColOperational";
	public static String CustomAttribute_ColWeighted = "ColWeighted";
	public static String CustomAttribute_ColAlign = "ColAlign";
	public static String CustomAttribute_ColFormula = "ColFormula";
	
	public static String Fenx_Beny = "����";
	public static String Fenx_Leij = "�ۼ�";
	
//	���������˵�ģ�����Ƶĳ���
	public static String Diaor16b = "����16-1��";
	public static String Diaor01b = "��ȼ01��";
	public static String Diaor03b = "��ȼ03��";
	public static String Diaor04b = "��ȼ04��";
	public static String Diaor08b = "��ȼ08��";
	
	public static String RizOpType_DEL = "ɾ��";
	public static String RizOpType_UP = "����";
	
	public static String RizOpMokm_Shulxg = "������Ϣ�޸�";
	public static String RizOpMokm_Fahxg = "�����޸�";
	public static String RizOpMokm_Shulsh = "�������";
	public static String RizOpMokm_Jianjxg = "����޸�";
	public static String RizOpMokm_Yundxg = "�˵��޸�";
	public static String RizOpMokm_Caiyxxwh = "������Ϣά���޸�";
	public static String RizOpMokm_Caizhbm = "���ƻ������޸�";
	public static String RizOpMokm_Huayzlu = "����ֵ¼���޸�";
	public static String RizOpMokm_Rulhy = "��¯����";
	public static String RizOpMokm_Meihy = "ú����";
	public static String RizOpMokm_Tielyb = "��·Ԥ��";
	public static String RizOpMokm_Kuangfyb = "��Ԥ��";
	public static String RizOpMokm_Riscsj = "����������";
	public static String RizOpMokm_Jiaojjl = "���Ӽ�¼";
	public static String RizOpMokm_Quzpk = "ȡ���ſ�";
	public static String RizOpMokm_Jizyxzt = "��������״̬";
	public static String RizOpMokm_Jiexsbzt = "��ж�豸״̬";
	public static String RizOpMokm_Pand = "�̵�";
	public static String RizOpMokm_Meictj = "ú�����";
	public static String RizOpMokm_Meicmd = "ú���ܶ�";
	public static String RizOpMokm_Qitcm = "������ú";
	public static String RizOpMokm_Yougpd = "�͹��̵�";
	public static String RizOpMokm_Renyzz = "��Աְ��";
	public static String RizOpMokm_Zhangmm = "����ú";
	public static String RizOpMokm_Zhangmy = "������";
	
	public static String RizOpMokm_Rlgs_Shujqr = "ȼ�Ϲ�˾�ϴ�����ȷ��";
	public static String RizOpMokm_Rlgs_Shujwh = "ȼ�Ϲ�˾¼������ά��";
	
}