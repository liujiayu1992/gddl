package com.zhiren.common;
/*
 * 作者：王磊
 * 时间：2009-08-26 10：08
 * 描述：增加车别的sql(SQL_Cheb)
 */
public class SysConstant {
//	级别代码
	public static final int JIB_JT = 1;
	public static final int JIB_GS = 2;
	public static final int JIB_DC = 3;
//	错误代码
	public static final int ErrCode_unKnow = 0; 
	public static final int ErrCode_illLogin = 1; 
	public static final int ErrCode_noUser = 2; 
	public static final int ErrCode_errPwd = 3; 
	public static final int ErrCode_noPower = 4; 
	public static final int ErrCode_errdb = 5;  
	public static final int ErrCode_IeVar = 6; 
	
	public static final String FieldSpliter = "~#";
	public static final String LineSpliter = "~~";
//	采样编码方式
	public static final String Caiy_GroupType_hc = "火运采样编号";
	public static final String Caiy_GroupType_qc = "汽运采样编号";
//	oracle 数据类型
	public static final String Oracle_DataType_Number = "NUMBER";
	public static final String Oracle_DataType_Date = "DATE";
//	ext数据类型
	public static final String Ext_DataType_Number = "float";
	public static final String Ext_DataType_String = "string";
	public static final String Ext_DataType_Date = "date";
//	ext绑定格式化类型
	public static final String Ext_Renderer_Date = "function(value){ return (value==null || value=='')?'':('object' != typeof(value)?value:value.dateFormat('Y-m-d'));}";
	public static final String Ext_Renderer_usMoney = "'usMoney'";
//	衡单的类型
	public static final int Hengd_Auto_maoz = 0;
	public static final int Hengd_Auto_piz = 1;
	public static final int Hengd_Auto_all = 2;
	public static final int Hengd_Manual_maoz = 3;
	public static final int Hengd_Manual_piz = 4;
	public static final int Hengd_Manual_all = 5;
//	按钮提交类型
	public static final int SaveMode_Allsubmit = 0;
	public static final int SaveMode_Selsubmit = 1;
	public static final int SaveMode_Upsubmit = 2;
	
//	运输方式
	public static final int YUNSFS_HUOY = 1;
	public static final int YUNSFS_QIY = 2;
	public static final int YUNSFS_HaiY = 3;
	public static final int YUNSFS_Pidc = 4;
//	计划口径
	public static final int JIHKJ_ZD = 1;
	public static final int JIHKJ_SC = 2;
	public static final int JIHKJ_QY = 3;
	public static final int JIHKJ_NONE = 4;
//	汽车站id
	public static final int Chez_q = 1;
	public static final int Chez_pdc = 2;
//	车皮表hedbz类型
	public static final int HEDBZ_LR = 0;
	public static final int HEDBZ_TJ = 1;	//运单已提交
	public static final int HEDBZ_YJJ = 2;	//已检斤
	public static final int HEDBZ_YSH = 3;	//数量已审核
	public static final int HEDBZ_YDP = 4;	//已对货票
	
//	车别常量
	public static final int CHEB_LC = 1;
	public static final int CHEB_ZB = 2;
	public static final int CHEB_QC = 3;
	public static final int CHEB_C = 4;
	public static final int CHEB_PDC = 5;
	
//	运损计算方式
	public static final String CountType_Yuns_dc = "单车";
	public static final String CountType_Yuns_fp = "分批";
	
//	接口相关路径(路径现在使用系统设置中的设置路径)
//	public static final String WS_errLogPath = "D:/zhiren/logs";//错误日志路径
	public static final String WS_errLogFileName = "stderr.log";//错误日志名称
//	public static final String WS_infoLogPath = "D:/zhiren/logs";//信息日志路径
	public static final String WS_infoLogFileName = "stdout.log";//信息日志名称
//	public static final String WS_ReceiveFilePath = "D:/zhiren/webservice/receive";//接收文件目录
//	public static final String WS_ReceiveBakFilePath = "D:/zhiren/webservice/receivebak";//接收文件目录
//	public static final String WS_SendFilePath = "D:/zhiren/webservice/send";//发送文件目录
//	接口错误码
	public static final int WS_EC_ValiUserfail = 100;//用户名验证失败
	public static final int WS_EC_ValiPwdfail = 101;//密码验证失败
	public static final int WS_EC_ValiGuidfail = 200;//Guid验证失败
	public static final int WS_EC_CreateGuidfail = 300;//创建文件或目录失败
	public static final int WS_EC_CreateFilefail = 301;//创建文件或目录失败
	public static final int WS_EC_FileNotFound = 400; //指定文件未找到
	public static final int WS_EC_IOExp = 500; //文件读写时发生错误
	public static final int WS_EC_JDOMExp = 600; //xml文件格式不正确
	public static final int WS_EC_En_Decryptfail = 700; //文件加密解密过程发生错误
	public static final int WS_EC_Success = 0; //成功
	
//	按钮图片
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
	
//	申请修改的类型
	public static final String Shenqxglx_shulyb = "数量月报";
	
//	申请修改的标识类型
	public static final int Shenqxgbslx_id = 0;
	public static final int Shenqxgbslx_rq = 1;
	
//	round function name
	public static final String RoundFunction = "round";
	
//	估算的厂内煤炭入炉前其它费用
	public static final long Rulqqtfy = 15;

//	XML公式信息
		public static final String Gs_WZ_Xtsz = "公式存放位置";
		//结算
		public static final String Gs_JS_FilePath = "D:/zhiren/gongs";
		public static final String Gs_JS_FileName = "Jiesgs.xml";
		public static final String Gs_JS_RootName = "结算公式";
		public static final String Gs_JS_HeadName_DIANCXXB_ID = "DIANCXXB_ID";
		public static final String Gs_JS_HeadName_Mk = "煤款公式";
		public static final String Gs_JS_HeadName_Yf = "运费公式";
		public static final String Gs_JS_HeadName_Shih = "石灰石公式";
		public static final String Gs_JS_ChildName_Blcsh = "变量初始化";
		public static final String Gs_JS_ChildName_Gyff = "共用方法";
		public static final String Gs_JS_ChildName_Jsgc = "计算过程";
		//结算_End
		
//	品种下拉框只包含煤的数据的SQL
	public static final String SQL_Pinz_mei = "select id,mingc from pinzb where leib = '煤' order by mingc";
	public static final String SQL_xiecfs = "select id,mingc from xiecfsb";
	public static final String SQL_Kouj = "select id,mingc from jihkjb";
	public static final String SQL_yunsfs = "select id,mingc from yunsfsb";
	public static final String SQL_Shihgys = "select id,piny||mingc from shihgysb";
	public static final String SQL_Shihpz = "select id,piny||mingc from shihpzb";
	public static final String SQL_Meic = "select id, mingc from meicb";
	public static final String SQL_Cheb = "select id,mingc from chebb";
	public 	static final String SQL_Pdcx="select id,bianm from pand_gd order by bianm desc";
	public static final String SQL_Yunsdw = " select id,mingc from yunsdwb";
	public static final String SQL_Yunsdw_mc = "select -1 id,'全部' mingc from dual union all select  id,mingc from yunsdwb";
	//中电投来煤量算法,来煤量(实收量)算法
	public static  String LaimField="  (round_new(sum(fh.biaoz),0)+round_new(sum(fh.yingd),0)-round_new(sum(fh.yingd-fh.yingk),0)) ";
	
	public static  String LaimField1="  (round_new(sum(biaoz),0)+round_new(sum(yingd),0)-round_new(sum(yingd-yingk),0)) ";
	
//	用来生成采样编码的 基础编码的序列值
	public static String BiascCodeSequenceName = "XL_BiascCode";
	
	public static String RuLJQL="入炉化验煤量";

	
//	流程动作表中对应的操作动作常量
	public static String Liucdz_ShenhOrHuitBtxyj="审核或回退并填写意见";	//这个动作会弹出“结算流程_意见填写”界面，用户可以填写审核意见。
	
//	自定义报表及对比查询中的代码定义
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
	
	public static String Fenx_Beny = "本月";
	public static String Fenx_Leij = "累计";
	
//	设计数据审核的模块名称的常量
	public static String Diaor16b = "电生16-1表";
	public static String Diaor01b = "调燃01表";
	public static String Diaor03b = "调燃03表";
	public static String Diaor04b = "调燃04表";
	public static String Diaor08b = "调燃08表";
	
	public static String RizOpType_DEL = "删除";
	public static String RizOpType_UP = "更新";
	
	public static String RizOpMokm_Shulxg = "数量信息修改";
	public static String RizOpMokm_Fahxg = "发货修改";
	public static String RizOpMokm_Shulsh = "数量审核";
	public static String RizOpMokm_Jianjxg = "检斤修改";
	public static String RizOpMokm_Yundxg = "运单修改";
	public static String RizOpMokm_Caiyxxwh = "采样信息维护修改";
	public static String RizOpMokm_Caizhbm = "采制化编码修改";
	public static String RizOpMokm_Huayzlu = "化验值录入修改";
	public static String RizOpMokm_Rulhy = "入炉化验";
	public static String RizOpMokm_Meihy = "煤耗用";
	public static String RizOpMokm_Tielyb = "铁路预报";
	public static String RizOpMokm_Kuangfyb = "矿方预报";
	public static String RizOpMokm_Riscsj = "日生产数据";
	public static String RizOpMokm_Jiaojjl = "交接记录";
	public static String RizOpMokm_Quzpk = "取重排空";
	public static String RizOpMokm_Jizyxzt = "机组运行状态";
	public static String RizOpMokm_Jiexsbzt = "接卸设备状态";
	public static String RizOpMokm_Pand = "盘点";
	public static String RizOpMokm_Meictj = "煤场体积";
	public static String RizOpMokm_Meicmd = "煤场密度";
	public static String RizOpMokm_Qitcm = "其他存煤";
	public static String RizOpMokm_Yougpd = "油罐盘点";
	public static String RizOpMokm_Renyzz = "人员职责";
	public static String RizOpMokm_Zhangmm = "账面煤";
	public static String RizOpMokm_Zhangmy = "账面油";
	
	public static String RizOpMokm_Rlgs_Shujqr = "燃料公司上传数量确认";
	public static String RizOpMokm_Rlgs_Shujwh = "燃料公司录入数量维护";
	
}