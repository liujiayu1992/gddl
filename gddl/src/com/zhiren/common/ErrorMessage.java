package com.zhiren.common;

public class ErrorMessage {
	public static final String SaveSuccessMessage = "保存成功";
	/*
	 * 化验错误提示
	 */
	//转码类别未设置
	public static final String Sign_HNotFound = "入厂氢值化验符号设置错误！";
	public static final String Sign_SNotFound = "入厂硫值化验符号设置错误！";
	public static final String ZhuanmlbNotFound = "转码类别中未包含化验编码的设置！";
	public static final String getYuansfsFailed = "元素分析设置错误！";
	/*
	 * 月基础表处理
	 */
	public static final String InsertYuetjkjFailed = "月统计口径生成时发生异常！";
	public static final String DeleteYueslbFailed = "删除月数量表时发生异常！";
	public static final String InsertYueslbFailed = "生成月数量表时发生异常！";
	public static final String UpdateYueslbFailed = "更新月数量表时发生异常！";
	public static final String DeleteYuezlbFailed = "删除月质量表时发生异常！";
	public static final String InsertYuezlbFailed = "生成月质量表时发生异常！";
	public static final String UpdateYuezlbFailed = "更新月质量表时发生异常！";
	/*
	 * 火车错误信息
	 */
	//运单录入页面错误信息
	public static final String NullResult = "数据库连接失败！请检查服务器数据库连接状态！";
	public static final String InsertDatabaseFail = "写入表失败！";
	public static final String UpdateDatabaseFail = "更新表失败！";
	public static final String DeleteDatabaseFail = "删除表失败！";
	public static final String UnknowError = "未知错误！";
	
	public static final String NullSql = "更新数据为空！";
	public static final String InesrtCheplsbFailed = "写入车皮临时表时发生数据异常！";
	public static final String UpdatezlidFailed = "采样编号生成时发生异常！"; 
	public static final String INSorUpfahbFailed = "计算生成发货批次时发生异常！"; 
	public static final String InsChepbFailed = "写入单车数据时发生异常！"; 
	public static final String CountChepbYunsFailed = "计算单车运损时发生异常！";
	public static final String updateFahbFailed = "更新批次数据时发生异常！"; 
	public static final String updateLieidFailed = "更新批次计算列数据时发生异常！"; 
	public static final String CountFahbYunsFailed = "计算批次运损时发生异常！";
	public static final String ShujdrBjFailed = "导入过程中更新标记发生异常！";
	public static final String CaiyjBmFailed = "采样设备编码发生异常！"; 
	
	public static final String Yundlr001 = "保存过程中出现错误！错误代码 YDLR-001";
	public static final String Yundlr002 = "保存过程中出现错误！错误代码 YDLR-002";
	public static final String Yundlr003 = "保存过程中出现错误！错误代码 YDLR-003";
	public static final String Yundlr004 = "保存过程中出现错误！错误代码 YDLR-004";
	public static final String Yundlr005 = "保存过程中出现错误！错误代码 YDLR-005";
	public static final String Yundlr006 = "保存过程中出现错误！错误代码 YDLR-006";
	//运单修改页面错误信息
	public static final String Yundxg001 = "保存过程中出现错误！错误代码 YDXG-001";
	public static final String Yundxg002 = "保存过程中出现错误！错误代码 YDXG-002";
	public static final String Yundxg003 = "保存过程中出现错误！错误代码 YDXG-003";
	public static final String Yundxg004 = "保存过程中出现错误！错误代码 YDXG-004";
	public static final String Yundxg005 = "保存过程中出现错误！错误代码 YDXG-005";
	public static final String Yundxg006 = "保存过程中出现错误！错误代码 YDXG-006";
	public static final String Yundxg007 = "保存过程中出现错误！错误代码 YDXG-007";
	public static final String Yundxg008 = "保存过程中出现错误！错误代码 YDXG-008";
	public static final String Yundxg009 = "保存过程中出现错误！错误代码 YDXG-009";
	public static final String Yundxg010 = "保存过程中出现错误！错误代码 YDXG-010";
	//车号核对页面错误信息
	public static final String Chehhd001 = "文件为空！错误代码 CHHD-001";
	public static final String Chehhd002 = "没有符合规则的过衡文件！错误代码 CHHD-002";
	public static final String Chehhd003 = "该文件未找到！错误代码 CHHD-003";
	public static final String Chehhd004 = "文件名错误！错误代码 CHHD-004";
	public static final String Chehhd005 = "文件处理过程发生错误！错误代码 CHHD-005";
	public static final String Chehhd006 = "更新数据失败！错误代码 CHHD-006";
	public static final String Chehhd007 = "更新数据失败！错误代码 CHHD-007";
	public static final String Chehhd008 = "更新数据失败！错误代码 CHHD-008";
	public static final String Chehhd009 = "更新数据失败！错误代码 CHHD-009";
	public static final String Chehhd010 = "更新数据失败！错误代码 CHHD-010";
	public static final String Chehhd011 = "更新数据失败！错误代码 CHHD-011";
	public static final String Chehhd012 = "更新数据失败！错误代码 CHHD-012";
	public static final String Chehhd013 = "更新数据失败！错误代码 CHHD-013";
	public static final String Chehhd014 = "更新数据失败！错误代码 CHHD-014";
	public static final String Chehhd015 = "更新数据失败！错误代码 CHHD-015";
	
	
	//衡单修改页面错误信息
	public static final String Hengdxg001 = "原文件删除失败! 错误代码 HDXY-001";
	public static final String Hengdxg002 = "写入文件失败! 错误代码  HDXY-002";
	public static final String Hengdxg003 = "原文件删除失败！错误代码 HDXG-003";
	public static final String Hengdxg004 = "该文件未找到！错误代码 HDXG-004";
	public static final String Hengdxg005 = "文件名错误！错误代码 HDXG-005";
	public static final String Hengdxg006 = "文件处理过程发生错误！错误代码 HDXG-006";
	public static final String Hengdxg007 = "文件为空！错误代码 HDXG-007";
	//车皮核对页面错误信息
	public static final String Chepsh001 = "保存过程中出现错误！错误代码 CPSH-001";
	public static final String Chepsh002 = "保存过程中出现错误！错误代码 CPSH-002";
	public static final String Chepsh003 = "保存过程中出现错误！错误代码 CPSH-003";
	public static final String Chepsh004 = "保存过程中出现错误！错误代码 CPSH-004";
	public static final String Chepsh005 = "保存过程中出现错误！错误代码 CPSH-005";
	public static final String Chepsh006 = "保存过程中出现错误！错误代码 CPSH-006";
	public static final String Chepsh007 = "保存过程中出现错误！错误代码 CPSH-007";
	//数据审核页面错误信息
	public static final String ShujshH001 = "更新数据失败！错误代码 SJSHH-001";
	public static final String ShujshH002 = "更新数据失败！错误代码 SJSHH-002";
	public static final String Shujsh001 = "更新数据失败！错误代码 SJSH-001";
	public static final String Shujsh002 = "更新数据失败！错误代码 SJSH-002";
	/*
	 * 汽车错误信息
	 */
	//检斤修改页面错误信息
	public static final String Jianjxg001 = "保存过程中出现错误！错误代码 JJXG-001";
	public static final String Jianjxg002 = "保存过程中出现错误！错误代码 JJXG-002";
	public static final String Jianjxg003 = "保存过程中出现错误！错误代码 JJXG-003";
	public static final String Jianjxg004 = "保存过程中出现错误！错误代码 JJXG-004";
	public static final String Jianjxg005 = "保存过程中出现错误！错误代码 JJXG-005";
	public static final String Jianjxg006 = "保存过程中出现错误！错误代码 JJXG-006";
	public static final String Jianjxg007 = "保存过程中出现错误！错误代码 JJXG-007";
	public static final String Jianjxg008 = "保存过程中出现错误！错误代码 JJXG-008";
	//空车检斤页面错误信息
	public static final String Kongcjj001 = "保存过程中出现错误！错误代码 KCJJ-001";
	public static final String Kongcjj002 = "保存过程中出现错误！错误代码 KCJJ-002";
	public static final String Kongcjj003 = "保存过程中出现错误！错误代码 KCJJ-003";
	public static final String Kongcjj004 = "保存过程中出现错误！错误代码 KCJJ-004";
	//空车检斤录入页面错误信息
	public static final String Kongcjjlr001 = "保存过程中出现错误！错误代码 KCJJLR-001";
	public static final String Kongcjjlr002 = "保存过程中出现错误！错误代码 KCJJLR-002";
	public static final String Kongcjjlr003 = "保存过程中出现错误！错误代码 KCJJLR-003";
	public static final String Kongcjjlr004 = "保存过程中出现错误！错误代码 KCJJLR-004";
	public static final String Kongcjjlr005 = "保存过程中出现错误！错误代码 KCJJLR-005";
	public static final String Kongcjjlr006 = "保存过程中出现错误！错误代码 KCJJLR-006";
	public static final String Kongcjjlr007 = "保存过程中出现错误！错误代码 KCJJLR-007";
	public static final String Kongcjjlr008 = "保存过程中出现错误！错误代码 KCJJLR-008";
	//汽车检斤页面错误信息
	public static final String Qicjj001 = "保存过程中出现错误！错误代码 QCJJ-001";
	public static final String Qicjj002 = "保存过程中出现错误！错误代码 QCJJ-002";
	public static final String Qicjj003 = "保存过程中出现错误！错误代码 QCJJ-003";
	public static final String Qicjj004 = "保存过程中出现错误！错误代码 QCJJ-004";
	public static final String Qicjj005 = "保存过程中出现错误！错误代码 QCJJ-005";
	public static final String Qicjj006 = "保存过程中出现错误！错误代码 QCJJ-006";
	public static final String Qicjj007 = "保存过程中出现错误！错误代码 QCJJ-007";
	public static final String Qicjj008 = "保存过程中出现错误！错误代码 QCJJ-008";
	//汽车检斤录入页面错误信息
	public static final String Qicjjlr001 = "SQL构成错误！错误代码 QCJJLR-001";
	public static final String Qicjjlr002 = "保存过程中出现错误！错误代码 QCJJLR-002";
	public static final String Qicjjlr003 = "保存过程中出现错误！错误代码 QCJJLR-003";
	public static final String Qicjjlr004 = "保存过程中出现错误！错误代码 QCJJLR-004";
	public static final String Qicjjlr005 = "保存过程中出现错误！错误代码 QCJJLR-005";
	public static final String Qicjjlr006 = "保存过程中出现错误！错误代码 QCJJLR-006";
	public static final String Qicjjlr007 = "保存过程中出现错误！错误代码 QCJJLR-007";
	public static final String Qicjjlr008 = "保存皮重过程中出现错误！错误代码 QCJJLR-008";
	public static final String Qicjjlr009 = "保存皮重过程中出现错误！错误代码 QCJJLR-009";
	public static final String Qicjjlr010 = "保存皮重过程中出现错误！错误代码 QCJJLR-010";
	public static final String Qicjjlr011 = "保存皮重过程中出现错误！错误代码 QCJJLR-011";
	//重车检斤页面错误信息
	public static final String Zhongcjj001 = "保存过程中出现错误！错误代码 ZCJJ-001";
	public static final String Zhongcjj002 = "保存过程中出现错误！错误代码 ZCJJ-002";
	public static final String Zhongcjj003 = "保存过程中出现错误！错误代码 ZCJJ-003";
	public static final String Zhongcjj004 = "保存过程中出现错误！错误代码 ZCJJ-004";
	///重车检斤录入页面错误信息
	public static final String Zhongcjjlr001 = "保存过程中出现错误！错误代码 ZCJJLR-001";
	public static final String Zhongcjjlr002 = "保存过程中出现错误！错误代码 ZCJJLR-002";
	public static final String Zhongcjjlr003 = "保存过程中出现错误！错误代码 ZCJJLR-003";
	public static final String Zhongcjjlr004 = "保存过程中出现错误！错误代码 ZCJJLR-004";
	public static final String Zhongcjjlr005 = "保存过程中出现错误！错误代码 ZCJJLR-005";
	public static final String Zhongcjjlr006 = "保存过程中出现错误！错误代码 ZCJJLR-006";
	//车皮审核（汽）页面错误信息
	public static final String ChepshQ001 = "保存过程中出现错误！错误代码 CPSHQ-001";
	public static final String ChepshQ002 = "保存过程中出现错误！错误代码 CPSHQ-002";
	public static final String ChepshQ003 = "保存过程中出现错误！错误代码 CPSHQ-003";
	public static final String ChepshQ004 = "保存过程中出现错误！错误代码 CPSHQ-004";
	public static final String ChepshQ005 = "保存过程中出现错误！错误代码 CPSHQ-005";
	public static final String ChepshQ006 = "保存过程中出现错误！错误代码 CPSHQ-006";
	public static final String ChepshQ007 = "保存过程中出现错误！错误代码 CPSHQ-007";
	//数据审核（汽）页面错误信息
	public static final String ShujshQ001 = "保存过程中出现错误！错误代码 SJSHQ-001";
	public static final String ShujshQ002 = "保存过程中出现错误！错误代码 SJSHQ-002";
	public static final String ShujshQ003 = "保存过程中出现错误！错误代码 SJSHQ-003";
	public static final String ShujshQ004 = "保存过程中出现错误！错误代码 SJSHQ-004";
	//信息录入页面错误信息
	public static final String Xinxlr001 = "保存过程中出现错误！错误代码 XXLU-001";
	public static final String Xinxlr002 = "保存过程中出现错误！错误代码 XXLU-002";
	public static final String Xinxlr003 = "保存过程中出现错误！错误代码 XXLU-003";
	public static final String Xinxlr004 = "保存过程中出现错误！错误代码 XXLU-004";
	public static final String Xinxlr005 = "保存过程中出现错误！错误代码 XXLU-005";
	public static final String Xinxlr006 = "保存过程中出现错误！错误代码 XXLU-006";
	//信息修改页面错误信息
	public static final String Xinxxg001 = "保存过程中出现错误！错误代码 XXXG-001";
	public static final String Xinxxg002 = "保存过程中出现错误！错误代码 XXXG-002";
	public static final String Xinxxg003 = "保存过程中出现错误！错误代码 XXXG-003";
	public static final String Xinxxg004 = "保存过程中出现错误！错误代码 XXXG-004";
	public static final String Xinxxg005 = "保存过程中出现错误！错误代码 XXXG-005";
	public static final String Xinxxg006 = "保存过程中出现错误！错误代码 XXXG-006";
	public static final String Xinxxg007 = "保存过程中出现错误！错误代码 XXXG-007";
	public static final String Xinxxg008 = "保存过程中出现错误！错误代码 XXXG-008";
	//月数量表的约束提示	
	public static final String DataLocked_Yueslb = "该数据已经被锁定，请确认月收耗存合计是否已经删除！";
	public static final String DataLocked_Yueshchjb = "该数据已经被锁定，请确认月耗存数据是否已经删除！";
	public static final String DataLocked_Yueqfmkb = "该数据已经被上报锁定，不能进行操作！";
	/*
	 * 周报表处理
	 */
	public static final String InsertZhoubFailed = "本周内没有周报数据，无法生成周报！";
	public static final String DeleteZhoubFailed = "删除周报表时发生异常！";
//	周报表的约束提示	
	public static final String DataLocked_Zhoub = "该数据已经被锁定，请确认周报是否已经删除！";

//	计划数据导入错误信息 
	public static final String Jhdr001 = "保存过程中出现错误！错误代码JHSJDR-001！";
	public static final String Jhdr002 = "保存过程中出现错误！错误代码JHSJDR-002！";
	public static final String Jhdr003 = "保存过程中出现错误！错误代码JHSJDR-003！";
	public static final String Jhdr004 = "保存过程中出现错误！错误代码JHSJDR-004！";
	public static final String Jhdr005 = "保存过程中出现错误！错误代码JHSJDR-005！";
	public static final String Jhdr006 = "保存过程中出现错误！错误代码JHSJDR-006！";
	public static final String Jhdr007 = "保存过程中出现错误！错误代码JHSJDR-007！";
	public static final String Jhdr008 = "保存过程中出现错误！错误代码JHSJDR-008！";
	public static final String Jhdr009 = "保存过程中出现错误！错误代码JHSJDR-009！";
	public static final String Jhdr010 = "保存过程中出现错误！错误代码JHSJDR-010！";
	public static final String Jhdr011 = "保存过程中出现错误！错误代码JHSJDR-011！";
	public static final String Jhdr012 = "保存过程中出现错误！错误代码JHSJDR-012！";
}
