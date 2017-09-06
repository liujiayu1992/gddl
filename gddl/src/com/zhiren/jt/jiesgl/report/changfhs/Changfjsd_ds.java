package com.zhiren.jt.jiesgl.report.changfhs;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Date;
import org.apache.tapestry.html.BasePage;
import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.common.ResultSetList;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author 曹林
 * 2009-03-25
 * 描述：1，锦州热电使用大唐国际统一结算单。
 * 2，调整了格式，增加了手写体签名和满足锦州流程的审批。
 * */
/**
 * @author 刘雨
 * 2009-05-27
 * 描述：修改了煤价计算中产生小数位不正确的BUG
 * */

/**
 * @author 王伟
 * 2009-07-05
 * 描述：针对国电结算单的一些修改
 * 
 * 	  1 meij为增扣后的价格，如果再加上折价等于增扣了回
 * 		将每个指标的
 * 		meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Qnetar));
 * 		改成(即：合同价)
 * 		meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Qnetar));
 * 
 * 	  2 如果结算单的盈吨字段为0时付""值
 * 	  
 *    3 添加运费结算的数量 strjiesyfbz_sl，在计算运费单价时，除以strjiesyfbz_sl，替换 strjiesyfbz_sl
 *    
 *    4 判断如果运输单位是公路时不显示运杂费小计
 */
/**
 * @author 张少君
 * 2009-07-07
 * 描述：在系统中增加参数设置，已显示“超/亏吨”
 * */
/**
 * @author 张少君
 * 2009-07-17
 * 描述：在中国大唐的结算单上废除了，“对付地点，付款方式”改为了“合同编号”
 * */

/**
 * @author  王伟
 * 2009-10-31
 * 描述：  修约国电扣款小数位，使其不产生无限小数
 * 			
 */

/**
 * @author  王伟
 * 2010-01-18
 * 描述：1 根据xitxxb中的配置得到结算单(国电)中结算部门的名称
 * 		INSERT INTO xitxxb VALUES(
			getnewid(diancID),         --diancID   电厂ID
			1,
			diancID,                   --diancID   电厂ID
			'结算部门名称',
			'计划营销部',                --在结算单结算部门中显示的文字
			'',
			'结算',
			1,
			'使用'
			)	
		
 *		2 添加获取Visit的方法
 */
/*
 * 作者：夏峥
 * 时间：2013-03-02
 * 使用范围：国电电力东胜
 * 描述：按电厂要求调整结算单显示内容
 */
public class Changfjsd_ds extends BasePage{
	
	/**
	 * Visit
	 */
	private Object visit;
	
	public void setVisit(Object visit) {
		this.visit = visit;
	}
	
	public Object getVisit() {
		return visit;
	}
	
	/**
	 * @param where
	 * @param iPageIndex
	 * @param tables
	 * @return
	 */
	public String getChangfjsd(String where,int iPageIndex,String tables){
		JDBCcon cn = new JDBCcon();
		Report rt=new Report();
		  try{
			  
			 String type="";	//标志着结算单类型，目前中国大唐是"ZGDT",国电是"GD"，‘jzrd’是大唐国际锦州公司
			 String table1="";
			 String table2="";
			 
			 if(tables.indexOf(",")>-1){
				 
				 table1=tables.substring(0,tables.lastIndexOf(","));
				 table2=tables.substring(tables.lastIndexOf(",")+1);
			 }else{
				 
				 table1=table2=tables;
			 }
			  
			 String sql=""; 
			 String strjiesrq="";
			 String strfahdw="";
			 String strfaz="";
			 String strdiabch="";
			 long 	lgdiancxxb_id=0;
			 String strbianh="";
			 String strfahksrq="";
			 String strfahjzrq="";
			 String strfahrq = "";
			 String strdiqdm = "";
			 String stryuanshr = "";
			 String strshoukdw = "";
			 String strkaihyh = "";
			 String strkaisysrq="";
			 String strjiezysrq="";
			 String stryansrq = "";
			 String strhuowmc = "";
			 String strxianshr = "";
			 String stryinhzh = "";
			 String strfahsl = "";
			 String strches = "";
			 String stryansbh = "";
			 String strfapbh = "";
			 String strduifdd = "";
			 String strfukfs = "";
			 String strshijfk = "";
			 
			 String strhetbz_sl="";
			 String strgongfbz_sl = "";
			 String strchangfys_sl = "";
			 String strjiesbz_sl="";
			 String strxiancsl_sl = "";
			 String strzhejbz_sl = "";
			 String strzhehje_sl = "";
			 String strjiesyfbz_sl = "0"; //运费结算数量
			 
			 String strhetbz_Shulzb = "";		// 合同数量指标
			 String strxiancsl_Shulzb = "";		// 数量指标盈亏
			 String strzhejbz_Shulzb = "";		// 数量指标折单价
			 String strzhehje_Shulzb = "";		// 数量指标折金额
			 
			 String strhetbz_Qnetar="";
			 String strgongfbz_Qnetar = "";
			 String strchangfys_Qnetar = "";
			 String strjiesbz_Qnetar="";
			 String strxiancsl_Qnetar = "";
			 String strzhejbz_Qnetar = "";
			 String strzhehje_Qnetar = "";
			 
			 String strhetbz_Std = "";
			 String strgongfbz_Std = "";
			 String strchangfys_Std = "";
			 String strjiesbz_Std="";
			 String strxiancsl_Std = "";
			 String strzhejbz_Std = "";
			 String strzhehje_Std = "";
			 
			 String strhetbz_Star = "";
			 String strgongfbz_Star = "";
			 String strchangfys_Star = "";
			 String strjiesbz_Star="";
			 String strxiancsl_Star = "";
			 String strzhejbz_Star = "";
			 String strzhehje_Star = "";
			 
			 String strhetbz_Ad="";
			 String strgongfbz_Ad="";
			 String strchangfys_Ad="";
			 String strjiesbz_Ad="";
			 String strxiancsl_Ad="";
			 String strzhejbz_Ad="";
			 String strzhehje_Ad="";
			 
			 String strhetbz_Vdaf="";
			 String strgongfbz_Vdaf="";
			 String strchangfys_Vdaf="";
			 String strjiesbz_Vdaf="";
			 String strxiancsl_Vdaf="";
			 String strzhejbz_Vdaf="";
			 String strzhehje_Vdaf="";
			 
			 String strhetbz_Mt="";
			 String strgongfbz_Mt="";
			 String strchangfys_Mt="";
			 String strjiesbz_Mt="";
			 String strxiancsl_Mt="";
			 String strzhejbz_Mt="";
			 String strzhehje_Mt="";
			 
			 String strhetbz_Qgrad="";
			 String strgongfbz_Qgrad="";
			 String strchangfys_Qgrad="";
			 String strjiesbz_Qgrad="";
			 String strxiancsl_Qgrad="";
			 String strzhejbz_Qgrad="";
			 String strzhehje_Qgrad="";
			 
			 String strhetbz_Qbad="";
			 String strgongfbz_Qbad="";
			 String strchangfys_Qbad="";
			 String strjiesbz_Qbad="";
			 String strxiancsl_Qbad="";
			 String strzhejbz_Qbad="";
			 String strzhehje_Qbad="";
			 
			 String strhetbz_Had="";
			 String strgongfbz_Had="";
			 String strchangfys_Had="";
			 String strjiesbz_Had="";
			 String strxiancsl_Had="";
			 String strzhejbz_Had="";
			 String strzhehje_Had="";
			 
			 String strhetbz_Stad="";
			 String strgongfbz_Stad="";
			 String strchangfys_Stad="";
			 String strjiesbz_Stad="";
			 String strxiancsl_Stad="";
			 String strzhejbz_Stad="";
			 String strzhehje_Stad="";
			 
			 String strhetbz_Mad="";
			 String strgongfbz_Mad="";
			 String strchangfys_Mad="";
			 String strjiesbz_Mad="";
			 String strxiancsl_Mad="";
			 String strzhejbz_Mad="";
			 String strzhehje_Mad="";
			 
			 String strhetbz_Aar="";
			 String strgongfbz_Aar="";
			 String strchangfys_Aar="";
			 String strjiesbz_Aar="";
			 String strxiancsl_Aar="";
			 String strzhejbz_Aar="";
			 String strzhehje_Aar="";
			 
			 String strhetbz_Aad="";
			 String strgongfbz_Aad="";
			 String strchangfys_Aad="";
			 String strjiesbz_Aad="";
			 String strxiancsl_Aad="";
			 String strzhejbz_Aad="";
			 String strzhehje_Aad="";
			 
			 String strhetbz_Vad="";
			 String strgongfbz_Vad="";
			 String strchangfys_Vad="";
			 String strjiesbz_Vad="";
			 String strxiancsl_Vad="";
			 String strzhejbz_Vad="";
			 String strzhehje_Vad="";
			 
			 String strhetbz_T2="";
			 String strgongfbz_T2="";
			 String strchangfys_T2="";
			 String strjiesbz_T2="";
			 String strxiancsl_T2="";
			 String strzhejbz_T2="";
			 String strzhehje_T2="";
			 
			 String strhetbz_Yunju="";
			 String strgongfbz_Yunju="";
			 String strchangfys_Yunju="";
			 String strjiesbz_Yunju="";
			 String strxiancsl_Yunju="";
			 String strzhejbz_Yunju="";
			 String strzhehje_Yunju="";
			 
			 String strdanj = "";
			 String strbuhsdj = "";
			 String strjine = "";
			 String strbukouqjk = "";
			 String strjiakhj = "";
			 String strshuil_mk = "";
			 String strshuik_mk = "";
			 String strjialhj = "";
			 String strtielyf = "";
			 String strtielzf = "";
			 String strkuangqyf = "";
			 String strkuangqzf = "";
			 String strbukouqzf = "";
			 String strjiskc = "";
			 String strbuhsyf = "";
			 String strshuil_ys = "";
			 String strshuik_ys = "";
			 String stryunzshj = "";
			 String strhej_dx = "";
			 String strhej_xx = "";
			 String strbeiz = "";
			 String strguohzl = "";
			 String stryuns = "";
			 String strranlbmjbr=" ";
			 String strranlbmjbrq="";
			 String strchangcwjbr=" ";
			 String strchangcwjbrq="";
			 String strzhijzxjbr=" ";
			 String strzhijzxjbrq="";
			 String strlingdqz=" ";
			 String strlingdqzrq="";
			 String strzonghcwjbr=" ";
			 String strzonghcwjbrq="";
			 String strmeikhjdx="";
			 String stryunzfhjdx="";
			 String strJihkj="";
			 double danjc = 0;
			 String stryunsfs="";	//运输方式
			 String strChaokdl="";	//超亏吨量
			 String strChaokdlx="";	//超亏吨类型
			 String strHetbh="";	//合同编号
			 // 
			 double dblMeik =0;
			 double dblYunf =0;
			 String strkuidjfyf="";
			 String strkuidjfzf="";
			 String strbukmk = "";
			 sql="select * from "+table1+" where bianm='"+where+"'"; 
			 ResultSet rs = cn.getResultSet(sql);
			 
			 int intLeix=3;
			 long intDiancjsmkId=0;
			 long strkuangfjsmkb_id = -1;
			 boolean blnHasMeik =false;		//是否有煤款
			 
			 if(rs.next()){
				 
				 strHetbh = Jiesdcz.nvlStr(MainGlobal.getTableCol("hetb", "hetbh", "id", rs.getString("hetb_id")));
//				 danjc = rs.getDouble("danjc");
				 strbukmk = rs.getString("bukmk");
				 lgdiancxxb_id=rs.getLong("diancxxb_id");
				 strbianh=rs.getString("bianm");
				 strjiesrq=FormatDate(rs.getDate("jiesrq"));
				 intLeix=rs.getInt("jieslx");//结算类型0 为两票一个供应商,1为两票两个供应商，2为运费或煤款
				 intDiancjsmkId =rs.getLong("id");//煤款id
				 strfahdw=rs.getString("gongysmc");
				 strfahksrq=rs.getString("fahksrq");
				 strfahjzrq=rs.getString("fahjzrq");
				 if(strfahksrq.equals(strfahjzrq)){
					 strfahrq = FormatDate(rs.getDate("fahksrq"));//发货日期
				 }else{
					 strfahrq=FormatDate(rs.getDate("fahksrq"))+" 至 "+FormatDate(rs.getDate("fahjzrq"));
				 }
				 strfaz=rs.getString("faz");
				 strdiabch=rs.getString("daibch");
				 stryuanshr = rs.getString("yuanshr");//原收货人
				 strshoukdw = rs.getString("shoukdw");//收款单位
				 strkaihyh = rs.getString("kaihyh");//开户银行
				 strkaisysrq=rs.getString("yansksrq");
				 strjiezysrq=rs.getString("yansjzrq");
				 strJihkj=MainGlobal.getTableCol("jihkjb", "mingc", "id", rs.getString("jihkjb_id"));
				 
				 if(strkaisysrq.equals(strjiezysrq)){
					 stryansrq=FormatDate(rs.getDate("yansksrq"));
				 }else{
					 stryansrq=FormatDate(rs.getDate("yansksrq"))+" 至 "+FormatDate(rs.getDate("yansjzrq"));
				 }
				 strhuowmc = rs.getString("MEIZ");//货物名称
				 strxianshr = rs.getString("xianshr");//现收货人
				 stryinhzh = rs.getString("zhangh");//帐号
				 strches = rs.getString("ches");//车数
				 stryansbh = rs.getString("yansbh");//验收编号
				 strfapbh = rs.getString("fapbh");//发票编号
				 strduifdd = nvlStr(rs.getString("duifdd"));//兑付地点
				 strfukfs =nvlStr(rs.getString("fukfs")) ;//付款方式
				 strshijfk =rs.getString("hansdj");//实际付款
				 strbuhsdj=rs.getString("buhsdj");	//不含税单价
				 stryunsfs=MainGlobal.getTableCol("yunsfsb", "mingc", "id", rs.getString("yunsfsb_id"));
				 strChaokdl=String.valueOf(Math.abs(rs.getDouble("chaokdl")));	//超/亏吨量
				 strChaokdlx=Jiesdcz.nvlStr(rs.getString("chaokdlx"));	//超亏吨类型
				 
				  sql="select jieszbsjb.*,zhibb.bianm as mingc from jieszbsjb,"+table1+",zhibb "
					 + " where jieszbsjb.jiesdid="+table1+".id and zhibb.id=jieszbsjb.zhibb_id"
			        + " and "+table1+".bianm='"+where+"' and jieszbsjb.zhuangt=1 order by jieszbsjb.id";
				 
				 ResultSet rs2=cn.getResultSet(sql);
				 while(rs2.next()){
					 
					 if(rs2.getString("mingc").equals(Locale.jiessl_zhibb)){
						 
						 strhetbz_sl = rs2.getString("hetbz");		//合同标准
						 strgongfbz_sl = rs2.getString("gongf");	//供方数量
						 strfahsl=strgongfbz_sl;
						 strchangfys_sl = rs2.getString("CHANGF");	//验收数量
						 strjiesbz_sl = rs2.getString("JIES");		//结算数量
						 strxiancsl_sl = String.valueOf((rs2.getDouble("YINGK")>0?(-rs2.getDouble("YINGK")):0));//亏吨数量
						 strzhejbz_sl = rs2.getString("ZHEJBZ");	//折价标准
						 strzhehje_sl = rs2.getString("ZHEJJE");	//折合金额
						 
					 }else if(rs2.getString("mingc").equals(Locale.Shul_zhibb)){
					 
						 strhetbz_Shulzb = rs2.getString("hetbz");
						 strxiancsl_Shulzb = rs2.getString("YINGK");
						 strzhejbz_Shulzb = rs2.getString("ZHEJBZ");
						 strzhehje_Shulzb = rs2.getString("ZHEJJE");
					 
				 	 }else if(rs2.getString("mingc").equals(Locale.Qnetar_zhibb)){
						 
						 strhetbz_Qnetar = rs2.getString("hetbz");
						 strgongfbz_Qnetar = String.valueOf(Jiesdcz.getUnit_transform(rs2.getString("mingc"),Locale.qiankmqk_danw,rs2.getDouble("GONGF")));	    //供方热量
						 strchangfys_Qnetar = String.valueOf(Jiesdcz.getUnit_transform(rs2.getString("mingc"),Locale.qiankmqk_danw,rs2.getDouble("CHANGF")));	//验收热量
						 strjiesbz_Qnetar = String.valueOf(Jiesdcz.getUnit_transform(rs2.getString("mingc"),Locale.qiankmqk_danw,rs2.getDouble("jies")));		//结算热量
						 strxiancsl_Qnetar = rs2.getString("YINGK"); 		//相差数量热量
						 strzhejbz_Qnetar = rs2.getString("ZHEJBZ");		//折价标准热量
						 strzhehje_Qnetar = rs2.getString("ZHEJJE");	//折合金额热量
						 
					 }else if(rs2.getString("mingc").equals(Locale.Std_zhibb)){
						 
						 strhetbz_Std=rs2.getString("hetbz");
						 strgongfbz_Std = rs2.getString("GONGF");	//供方标准硫分
						 strchangfys_Std = rs2.getString("CHANGF");	//验收硫分
						 strjiesbz_Std=rs2.getString("jies");		//结算硫分
						 strxiancsl_Std = rs2.getString("YINGK");	//相差数量硫分
						 strzhejbz_Std = rs2.getString("ZHEJBZ");	//折价标准硫分
						 strzhehje_Std = rs2.getString("ZHEJJE");	//折合金额硫分
						 
					 }else if(rs2.getString("mingc").equals(Locale.Star_zhibb)){
						 
						 strhetbz_Star=rs2.getString("hetbz");
						 strgongfbz_Star = rs2.getString("GONGF");	//供方标准硫分
						 strchangfys_Star = rs2.getString("CHANGF");	//验收硫分
						 strjiesbz_Star = rs2.getString("jies");		//结算硫分
						 strxiancsl_Star = rs2.getString("YINGK");	//相差数量硫分
						 strzhejbz_Star = rs2.getString("ZHEJBZ");	//折价标准硫分
						 strzhehje_Star = rs2.getString("ZHEJJE");	//折合金额硫分
					 
					 }else if(rs2.getString("mingc").equals(Locale.Ad_zhibb)){
						 
						 strhetbz_Ad=rs2.getString("hetbz");
						 strgongfbz_Ad = rs2.getString("GONGF");//供方标准挥发分
						 strchangfys_Ad = rs2.getString("CHANGF");//验收挥发分
						 strjiesbz_Ad=rs2.getString("jies");//结算挥发分
						 strxiancsl_Ad = rs2.getString("YINGK");//相差数量挥发分
						 strzhejbz_Ad = rs2.getString("ZHEJBZ");//折价标准挥发分
						 strzhehje_Ad = rs2.getString("ZHEJJE");//折合金额挥发分
						 
					 }else if(rs2.getString("mingc").equals(Locale.Vdaf_zhibb)){
						 
						 strhetbz_Vdaf=rs2.getString("hetbz");
						 strgongfbz_Vdaf = rs2.getString("GONGF");//供方标准发分
						 strchangfys_Vdaf =rs2.getString("CHANGF");//验收发分
						 strjiesbz_Vdaf=rs2.getString("jies");//结算灰分
						 strxiancsl_Vdaf = rs2.getString("YINGK");//相差数量发分
						 strzhejbz_Vdaf =rs2.getString("ZHEJBZ");//折价标准发分
						 strzhehje_Vdaf =rs2.getString("ZHEJJE");//折合金额发分
						 
					 }else if(rs2.getString("mingc").equals(Locale.Mt_zhibb)){
						 
						 strhetbz_Mt=rs2.getString("hetbz");
						 strgongfbz_Mt = rs2.getString("GONGF");//供方标准水分
						 strchangfys_Mt = rs2.getString("CHANGF");//验收水分
						 strjiesbz_Mt=rs2.getString("jies");//结算水分
						 strxiancsl_Mt = rs2.getString("YINGK");//相差数量水分
						 strzhejbz_Mt = rs2.getString("ZHEJBZ");//折价标准水分
						 strzhehje_Mt = rs2.getString("ZHEJJE");//折合金额水分
						 
					 }else if(rs2.getString("mingc").equals(Locale.Qgrad_zhibb)){
						 
						 strhetbz_Qgrad=rs2.getString("hetbz");
						 strgongfbz_Qgrad = rs2.getString("GONGF");		//供方标准
						 strchangfys_Qgrad = rs2.getString("CHANGF");	//验收
						 strjiesbz_Qgrad=rs2.getString("jies");			//结算
						 strxiancsl_Qgrad = rs2.getString("YINGK");		//相差数量
						 strzhejbz_Qgrad = rs2.getString("ZHEJBZ");		//折价标准
						 strzhehje_Qgrad = rs2.getString("ZHEJJE");		//折合金额
						 
					 }else if(rs2.getString("mingc").equals(Locale.Qbad_zhibb)){
						 
						 strhetbz_Qbad=rs2.getString("hetbz");
						 strgongfbz_Qbad = rs2.getString("GONGF");		//供方标准
						 strchangfys_Qbad = rs2.getString("CHANGF");	//验收
						 strjiesbz_Qbad=rs2.getString("jies");			//结算
						 strxiancsl_Qbad = rs2.getString("YINGK");		//相差数量
						 strzhejbz_Qbad = rs2.getString("ZHEJBZ");		//折价标准
						 strzhehje_Qbad = rs2.getString("ZHEJJE");		//折合金额
						 
					 }else if(rs2.getString("mingc").equals(Locale.Had_zhibb)){
						 
						 strhetbz_Had=rs2.getString("hetbz");
						 strgongfbz_Had = rs2.getString("GONGF");	//供方标准
						 strchangfys_Had = rs2.getString("CHANGF");	//验收
						 strjiesbz_Had=rs2.getString("jies");		//结算
						 strxiancsl_Had = rs2.getString("YINGK");	//相差数量
						 strzhejbz_Had = rs2.getString("ZHEJBZ");	//折价标准
						 strzhehje_Had = rs2.getString("ZHEJJE");	//折合金额
						 
					 }else if(rs2.getString("mingc").equals(Locale.Stad_zhibb)){
						 
						 strhetbz_Stad=rs2.getString("hetbz");
						 strgongfbz_Stad = rs2.getString("GONGF");	//供方标准
						 strchangfys_Stad = rs2.getString("CHANGF");	//验收
						 strjiesbz_Stad = rs2.getString("jies");		//结算
						 strxiancsl_Stad = rs2.getString("YINGK");	//相差数量
						 strzhejbz_Stad = rs2.getString("ZHEJBZ");	//折价标准
						 strzhehje_Stad = rs2.getString("ZHEJJE");	//折合金额
						 
					 }else if(rs2.getString("mingc").equals(Locale.Mad_zhibb)){
						 
						 strhetbz_Mad=rs2.getString("hetbz");
						 strgongfbz_Mad = rs2.getString("GONGF");	//供方标准
						 strchangfys_Mad = rs2.getString("CHANGF");	//验收
						 strjiesbz_Mad = rs2.getString("jies");		//结算
						 strxiancsl_Mad = rs2.getString("YINGK");	//相差数量
						 strzhejbz_Mad = rs2.getString("ZHEJBZ");	//折价标准
						 strzhehje_Mad = rs2.getString("ZHEJJE");	//折合金额
						 
					 }else if(rs2.getString("mingc").equals(Locale.Aar_zhibb)){
						 
						 strhetbz_Aar=rs2.getString("hetbz");
						 strgongfbz_Aar = rs2.getString("GONGF");	//供方标准
						 strchangfys_Aar = rs2.getString("CHANGF");	//验收
						 strjiesbz_Aar = rs2.getString("jies");		//结算
						 strxiancsl_Aar = rs2.getString("YINGK");	//相差数量
						 strzhejbz_Aar = rs2.getString("ZHEJBZ");	//折价标准
						 strzhehje_Aar = rs2.getString("ZHEJJE");	//折合金额
						 
					 }else if(rs2.getString("mingc").equals(Locale.Aad_zhibb)){
						 
						 strhetbz_Aad=rs2.getString("hetbz");
						 strgongfbz_Aad = rs2.getString("GONGF");	//供方标准
						 strchangfys_Aad = rs2.getString("CHANGF");	//验收
						 strjiesbz_Aad = rs2.getString("jies");		//结算
						 strxiancsl_Aad = rs2.getString("YINGK");	//相差数量
						 strzhejbz_Aad = rs2.getString("ZHEJBZ");	//折价标准
						 strzhehje_Aad = rs2.getString("ZHEJJE");	//折合金额
						 
					 }else if(rs2.getString("mingc").equals(Locale.Vad_zhibb)){
						 
						 strhetbz_Vad=rs2.getString("hetbz");
						 strgongfbz_Vad = rs2.getString("GONGF");	//供方标准
						 strchangfys_Vad = rs2.getString("CHANGF");	//验收
						 strjiesbz_Vad = rs2.getString("jies");		//结算
						 strxiancsl_Vad = rs2.getString("YINGK");	//相差数量
						 strzhejbz_Vad = rs2.getString("ZHEJBZ");	//折价标准
						 strzhehje_Vad = rs2.getString("ZHEJJE");	//折合金额
						 
					 }else if(rs2.getString("mingc").equals(Locale.T2_zhibb)){
						 
						 strhetbz_T2=rs2.getString("hetbz");
						 strgongfbz_T2 = rs2.getString("GONGF");	//供方标准
						 strchangfys_T2 = rs2.getString("CHANGF");	//验收
						 strjiesbz_T2 = rs2.getString("jies");		//结算
						 strxiancsl_T2 = rs2.getString("YINGK");	//相差数量
						 strzhejbz_T2 = rs2.getString("ZHEJBZ");	//折价标准
						 strzhehje_T2 = rs2.getString("ZHEJJE");	//折合金额
						 
					 }else if(rs2.getString("mingc").equals(Locale.Yunju_zhibb)){
						 
						 strhetbz_Yunju=rs2.getString("hetbz");
						 strgongfbz_Yunju = rs2.getString("GONGF");		//供方标准
						 strchangfys_Yunju = rs2.getString("CHANGF");	//验收
						 strjiesbz_Yunju = rs2.getString("jies");		//结算
						 strxiancsl_Yunju = rs2.getString("YINGK");		//相差数量
						 strzhejbz_Yunju = rs2.getString("ZHEJBZ");		//折价标准
						 strzhehje_Yunju = rs2.getString("ZHEJJE");		//折合金额
						 
					 }
				 }
				 
				 rs2.close();

				 //********************其他*****************//
				 strdanj = rs.getString("hansdj");		//单价
				 strjine = rs.getString("meikje");		//金额
				 strbukouqjk = rs.getString("bukmk");	//补(扣)以前价款
				 strjiakhj = rs.getString("buhsmk");	//价款合计
				 strshuil_mk = rs.getString("shuil");	//税率(煤矿)
				 strshuik_mk = rs.getString("shuik");	//税款(煤矿)
				 strjialhj = rs.getString("hansmk");	//价税合计
				 strguohzl =rs.getString("GUOHL");		//过衡重量
				 stryuns =rs.getString("jiesslcy");		//运损(结算数量差异)
				 strbeiz = nvlStr(rs.getString("beiz"));//备注
				 dblMeik= Double.parseDouble(strjialhj);
				 strranlbmjbr=rs.getString("ranlbmjbr");
				 strranlbmjbrq=FormatDate(rs.getDate("ranlbmjbrq"));
				 blnHasMeik=true;
				 
			 }	 
//			1, 两票结算;
//			2, 煤款结算
//			3, 国铁运费
//			4, 地铁运费
			 
			 if ((blnHasMeik)&&(intLeix==1)){
				 
				 sql="select * from "+table2+" where bianm='"+where+"'";
				 
				 ResultSet rs3=cn.getResultSet(sql);
				 if (rs3.next()){
					 
					 strtielyf =rs3.getString("GUOTYF");//铁路运费
					 strtielzf = rs3.getString("guotzf");//杂费
					 strkuangqyf = rs3.getString("kuangqyf");//矿区运费
					 strkuangqzf = rs3.getString("kuangqzf");//矿区杂费
					 strbukouqzf = rs3.getString("bukyf");//补(扣)以前运杂费
					 strjiskc = rs3.getString("JISKC");//计税扣除
					 strbuhsyf =rs3.getString("buhsyf");//不含税运费
					 strshuil_ys = rs3.getString("shuil");//税率(运费)
					 strshuik_ys = rs3.getString("shuik");//税款(运费)
					 stryunzshj = rs3.getString("hansyf");//运杂费合计
					 dblYunf=rs3.getDouble("hansyf");
					 strkuidjfyf = rs3.getString("kuidjfyf");
					 strkuidjfzf = rs3.getString("kuidjfzf");
					 strjiesyfbz_sl = rs3.getString("jiessl"); //运费结算数量
				 }
				 rs3.close();
			 }else if(intLeix!=2){
					 
				 sql="select * from "+table2+"  where bianm='"+where+"'"; 
				 rs=cn.getResultSet(sql); 
				 	if(rs.next()){
//						 strshijfk =rs.getString("hansdj");		
						 lgdiancxxb_id=rs.getLong("diancxxb_id");
						 strbianh=rs.getString("bianm");
						 strjiesrq=FormatDate(rs.getDate("jiesrq"));
						 intLeix=rs.getInt("jieslx");//结算类型0 为两票一个供应商,1为两票两个供应商，2为运费或煤款
	//					 intDiancjsmkId =rs.getInt("id");//煤款id
						 strfahdw=rs.getString("gongysmc");
						 strfahksrq=rs.getString("fahksrq");
						 strfahjzrq=rs.getString("fahjzrq");
						 if(strfahksrq.equals(strfahjzrq)){
							 strfahrq = FormatDate(rs.getDate("fahksrq"));//发货日期
						 }else{
							 strfahrq=FormatDate(rs.getDate("fahksrq"))+" 至 "+FormatDate(rs.getDate("fahjzrq"));
						 }
	//					 strfahrq = rs.getString("fahrq");//发货日期
						 
						 strHetbh = Jiesdcz.nvlStr(MainGlobal.getTableCol("hetys", "hetbh", "id", rs.getString("hetb_id")));
						 strfaz=rs.getString("faz");
						 strdiabch=rs.getString("daibch");
						 stryuanshr = rs.getString("yuanshr");//原收货人
						 strshoukdw = rs.getString("shoukdw");//收款单位
						 strkaihyh = rs.getString("kaihyh");//开户银行
						 strkaisysrq=rs.getString("yansksrq");
						 strjiezysrq=rs.getString("yansjzrq");
						 
						 strgongfbz_sl=rs.getString("gongfsl");
						 strchangfys_sl=rs.getString("yanssl");
						 strjiesbz_sl=rs.getString("jiessl");
						 strxiancsl_sl=String.valueOf(-rs.getDouble("yingk"));
						 
						 
						 if(strkaisysrq.equals(strjiezysrq)){
							 stryansrq=FormatDate(rs.getDate("yansksrq"));
						 }else{
							 stryansrq=FormatDate(rs.getDate("yansksrq"))+" 至 "+FormatDate(rs.getDate("yansjzrq"));
						 }
	//					 stryansrq = rs.getString("yansrq");//验收日期
						 strhuowmc = rs.getString("MEIZ");//货物名称
						 strxianshr = rs.getString("xianshr");//现收货人
						 stryinhzh = rs.getString("zhangh");//帐号
						 strfahsl =rs.getString("gongfsl");//发运数量？？？？？？？？？？？？？？？？？？
						 strches = rs.getString("ches");//车数
						 stryansbh = rs.getString("yansbh");//验收编号
						 strfapbh = rs.getString("fapbh");//发票编号
						 strduifdd = rs.getString("duifdd");//兑付地点
						 strfukfs = rs.getString("fukfs") ;//付款方式
						 strdiqdm="";
						 strshijfk =rs.getString("hansdj");//实际付款？？？？？？？？？？？？？？？？？
						 stryunsfs=MainGlobal.getTableCol("yunsfsb", "mingc", "id", rs.getString("yunsfsb_id"));
						 
//						 2009-3-12 zsj增运费显示
						 strtielyf =rs.getString("GUOTYF");//铁路运费
						 strtielzf = rs.getString("guotzf");//杂费
						 strkuangqyf = rs.getString("kuangqyf");//矿区运费
						 strkuangqzf = rs.getString("kuangqzf");//矿区杂费
						 strbukouqzf = rs.getString("bukyf");//补(扣)以前运杂费
						 strjiskc = rs.getString("JISKC");//计税扣除
						 strbuhsyf =rs.getString("buhsyf");//不含税运费
						 strshuil_ys = rs.getString("shuil");//税率(运费)
						 strshuik_ys = rs.getString("shuik");//税款(运费)
						 stryunzshj = rs.getString("hansyf");//运杂费合计
						 dblYunf=rs.getDouble("hansyf");
						 strbuhsdj=String.valueOf(CustomMaths.mul(rs.getDouble("hansdj"),CustomMaths.sub(1,rs.getDouble("shuil"))));
						 strjiesyfbz_sl = rs.getString("jiessl"); //运费结算数量
						 
						 strhetbz_Qnetar = "";	//合同
						 strgongfbz_Qnetar = "";//供方热量
						 strchangfys_Qnetar = "";//验收热量
						 strjiesbz_Qnetar = "";//结算标准
						 strxiancsl_Qnetar= "";//相差数量热量
						 strzhejbz_Qnetar = "";//折价标准热量
						 strzhehje_Qnetar = "";//折合金额热量
						 
						 strhetbz_Std = "";		//合同
						 strgongfbz_Std = "";	//供方标准硫分
						 strchangfys_Std = "";	//验收硫分
						 strjiesbz_Std = "";	//结算标准
						 strxiancsl_Std = "";	//相差数量硫分
						 strzhejbz_Std = "";	//折价标准硫分
						 strzhehje_Std = "";	//折合金额硫分
						 
						 strhetbz_Ad = "";		//合同
						 strgongfbz_Ad = "";	//供方标准挥发分
						 strchangfys_Ad = "";	//验收挥发分
						 strjiesbz_Ad = "";		//结算标准
						 strxiancsl_Ad = "";	//相差数量挥发分
						 strzhejbz_Ad = "";		//折价标准挥发分
						 strzhehje_Ad = "";		//折合金额挥发分
	
						 strhetbz_Vdaf = "";		//供方标准发分
						 strgongfbz_Vdaf = "";		//供方标准发分
						 strchangfys_Vdaf = "";		//验收发分
						 strjiesbz_Vdaf = "";		//结算标准
						 strxiancsl_Vdaf = "";		//相差数量发分
						 strzhejbz_Vdaf = "";		//折价标准发分
						 strzhehje_Vdaf = "";		//折合金额发分
	
						 strhetbz_Mt="";
						 strgongfbz_Mt = "";		//供方标准水分
						 strchangfys_Mt = "";		//验收水分
						 strjiesbz_Mt = "";			//结算标准
						 strxiancsl_Mt = "";		//相差数量水分
						 strzhejbz_Mt = "";			//折价标准水分
						 strzhehje_Mt = "";			//折合金额水分
						 
						 strhetbz_Qgrad="";
						 strgongfbz_Qgrad = "";		//供方标准水分
						 strchangfys_Qgrad = "";	//验收水分
						 strjiesbz_Qgrad = "";		//结算标准
						 strxiancsl_Qgrad = "";		//相差数量水分
						 strzhejbz_Qgrad = "";		//折价标准水分
						 strzhehje_Qgrad = "";		//折合金额水分
						 
						 strhetbz_Qbad="";
						 strgongfbz_Qbad = "";		//供方标准水分
						 strchangfys_Qbad = "";		//验收水分
						 strjiesbz_Qbad = "";		//结算标准
						 strxiancsl_Qbad = "";		//相差数量水分
						 strzhejbz_Qbad = "";		//折价标准水分
						 strzhehje_Qbad = "";		//折合金额水分
	
						 strhetbz_sl = "";			//合同数量
//						 strgongfbz_sl = "";		//供方数量
//						 strchangfys_sl ="";		//验收数量
//						 strjiesbz_sl = "";			//结算标准
//						 strxiancsl_sl = "";		//相差数量
						 strzhejbz_sl ="";			//亏吨数量
						 strzhehje_sl = "";			//折合金额
						 
						 
	
//						 strjiessl = rs.getString("jiessl");//结算数量
//						 strjiesbz_sl = rs.getString("gongfsl");
//						 strdanj = (double)Math.round(a);//单价
						 strjine = "0";//金额
						 strbukouqjk = "0";//补(扣)以前价款
						 strjiakhj = "0";//价款合计
						 strshuil_mk = "0";//税率(煤矿)
						 strshuik_mk = "0";//税款(煤矿)
						 strjialhj = "0";//价税合计
						 strguohzl =rs.getString("GUOHL");//过衡重量
						 strbeiz = nvlStr(rs.getString("beiz"));//备注
						 dblMeik= Double.parseDouble(strjialhj);
						 blnHasMeik=true;
						 
						 strranlbmjbr=rs.getString("ranlbmjbr");
						 strranlbmjbrq=FormatDate(rs.getDate("ranlbmjbrq"));
						 
						 strkuidjfyf = rs.getString("kuidjfyf");
						 strkuidjfzf = rs.getString("kuidjfzf");
				 }
			 }
			 
			 Money money=new Money();
			 //计算合计
			 strhej_xx=format(dblYunf+dblMeik,"0.00");
			 strmeikhjdx=money.NumToRMBStr(dblMeik);
			 stryunzfhjdx=money.NumToRMBStr(dblYunf);
			 strhej_dx=money.NumToRMBStr(dblYunf+dblMeik);
			 
			 rs.close();
			 cn.Close();
//			 控制低位热、挥发分、灰分、水分隐藏显示
			 
			 boolean Qnetar_bn=false;
			 boolean Yunju_bn=false;	//运距
			 boolean Shulzb_bn=false;	//数量指标
			 boolean Ad_bn=false;
			 boolean Vdaf_bn=false;
			 boolean Mt_bn=false;
			 boolean Qgrad_bn=false;
			 boolean Qbad_bn=false;
			 boolean Had_bn=false;
			 boolean Stad_bn=false;
			 boolean Mad_bn=false;
			 boolean Aar_bn=false;
			 boolean Aad_bn=false;
			 boolean Vad_bn=false;
			 boolean T2_bn=false;
			 boolean Star_bn=false;
			 
			 //设置指标所在的行数
			 int Qnetar_row=7;
			 int Std_row=8;
			 int Shulzb_row=9;
			 int Ad_row=10;
			 int Vdaf_row=11;
			 int Mt_row=12;
			 int Qgrad_row=13;
			 int Qbad_row=14;
			 int Had_row=15;
			 int Stad_row=16;
			 int Mad_row=17;
			 int Aar_row=18;
			 int Aad_row=19;
			 int Vad_row=20;
			 int T2_row=21;
			 int Yunju_row=22;
			 int Star_row=23;
			 
			 //设置指标字段不显示
			 
			 if(strhetbz_Shulzb.equals("")||strhetbz_Shulzb.equals("0")){
				 Shulzb_bn=true;
			 }
			 if(strjiesbz_Qnetar.equals("")||strjiesbz_Qnetar.equals("0")){
				 Qnetar_bn=true;
			 }
			 if(strjiesbz_Yunju.equals("")||strjiesbz_Yunju.equals("0")){
				 Yunju_bn=true;
			 }
			 if(strjiesbz_Ad.equals("")||strjiesbz_Ad.equals("0")){
				 Ad_bn=true;
			 }
			 if(strjiesbz_Vdaf.equals("")||strjiesbz_Vdaf.equals("0")){
				 Vdaf_bn=true;
			 }
			 if(strjiesbz_Mt.equals("")||strjiesbz_Mt.equals("0")){
				 Mt_bn=true;
			 }
			 if(strjiesbz_Qgrad.equals("")||strjiesbz_Qgrad.equals("0")){
				 Qgrad_bn=true;
			 }
			 if(strjiesbz_Qbad.equals("")||strjiesbz_Qbad.equals("0")){
				 Qbad_bn=true;
			 }
			 if(strjiesbz_Had.equals("")||strjiesbz_Had.equals("0")){
				 Had_bn=true;
			 }
			 if(strjiesbz_Stad.equals("")||strjiesbz_Stad.equals("0")){
				 Stad_bn=true;
			 }
			 if(strjiesbz_Mad.equals("")||strjiesbz_Mad.equals("0")){
				 Mad_bn=true;
			 }
			 if(strjiesbz_Aar.equals("")||strjiesbz_Aar.equals("0")){
				 Aar_bn=true;
			 }
			 if(strjiesbz_Aad.equals("")||strjiesbz_Aad.equals("0")){
				 Aad_bn=true;
			 }
			 if(strjiesbz_Vad.equals("")||strjiesbz_Vad.equals("0")){
				 Vad_bn=true;
			 }
			 if(strjiesbz_T2.equals("")||strjiesbz_T2.equals("0")){
				 T2_bn=true;
			 }
			 if(strjiesbz_Star.equals("")||strjiesbz_Star.equals("0")){
				 
				 Star_bn=true;
			 }
			 
			 type=MainGlobal.getXitxx_item("结算", "结算单所属单位", String.valueOf(lgdiancxxb_id), "ZGDT");
			 if(type.equals("JZRD")){
//					1, 两票结算;
//					2, 煤款结算
//					3, 国铁运费
//					4, 地铁运费
				 String sql1="";
				 String hetbh="";
				 JDBCcon con1= new JDBCcon();
				 ResultSet rs1=null;
				 String tab="jiesb";
				  if(intLeix==3){	 //运费时合同号
					   sql1=
						  "select hetys.hetbh\n" +
						  "from jiesyfb,hetys where jiesyfb.hetb_id=hetys.id and jiesyfb.bianm='"+where+"'";
					   rs1=con1.getResultSet(sql1);
					   if(rs1.next()){
						   hetbh=rs1.getString(1);
					   }
					   tab="jiesyfb";
				  }else if(intLeix==4){//
					  hetbh="";
				  }else{//煤款的合同号
					   sql1=
						  "select hetb.hetbh\n" +
						  "from jiesb,hetb where jiesb.hetb_id=hetb.id and jiesb.bianm='"+where+"'";
					   rs1=con1.getResultSet(sql1);
					   if(rs1.next()){
						   hetbh=rs1.getString(1);
					   }
				  }
				  if(rs1!=null){
					  rs1.close();
				  }
				  con1.Close();
//				 大唐国际
					/**
					 *  
					财务部
					燃料管理部
					主管经理
					总经理
					 */

				  String sql2="select  distinct qianqztmc, caozy\n" +
				  "from (\n" + 
				  "select rownum i,t.*\n" + 
				  "from (\n" + 
				  "select *\n" + 
				  "from liucgzb\n" + 
				  "where liucgzid=(select liucgzid from  "+tab+" where bianm='"+where+"') order by id desc\n" + 
				  ")t)where i>=(\n" + 
				  "select max(i1)\n" + 
				  "from(\n" + 
				  "select  rownum i1,t1.*\n" + 
				  "from(\n" + 
				  "select liucgzb.*\n" + 
				  "from liucgzb\n" + 
				  "where liucgzid=(select liucgzid from  "+tab+" where bianm='"+where+"') order by id desc\n" + 
				  ")t1 )where liucdzbmc='提交' and houjztmc =(\n" + 
				  "select houjztmc\n" + 
				  "from(\n" + 
				  "select *\n" + 
				  "from liucgzb\n" + 
				  "where liucgzid=(select liucgzid from  "+tab+" where bianm='"+where+"') order by id desc\n" + 
				  ")t1 where rownum=1\n" + 
				  ") )and  qianqztmc in('燃料管理部','财务部','主管经理','总经理')\n" + 
				  "order by decode(qianqztmc,'燃料管理部',1,'财务部',2,'主管经理',3,4)\n" ;
				 // "--查看当前结算单所处的位置，找到提交给到当前位置的最早的前驱";
				 JDBCcon con=new JDBCcon();
				 ResultSet rs2=con.getResultSet(sql2);
				 String qianz_ranlb="";
				 String qianz_caiwb="";
				 String qianz_zhugjl="";
				 String qianz_zongjl="";
				 
				 String yij_ranlb="";
				 String yij_caiwb="";
				 String yij_zhugjl="";
				 String yij_zongjl="";
				 int i=0;
				 while(rs2.next()){ 
					 i++;
					 if(i==1){//燃料部
						 qianz_ranlb="<img name='img_qianz' mingc='"+rs2.getString(2)+"'></img>";
						 yij_ranlb="同意";
					 }else if(i==2){//财务部
						 qianz_caiwb="<img  name='img_qianz' mingc='"+rs2.getString(2)+"'></img>";
						 yij_caiwb="同意";
					 }else if(i==3){//主管经理
						 qianz_zhugjl="<img  name='img_qianz' mingc='"+rs2.getString(2)+"'></img>";
						 yij_zhugjl="同意";
					 }else{//总经理
						 qianz_zongjl="<img name='img_qianz' mingc='"+rs2.getString(2)+"'></img>";
						 yij_zongjl="同意";
					 }
				 }

				 int ArrWidth[]=new int[] {11,80,90,90,90,90,11,80,90,90,92,90,10};
				 String ArrHeader[][]=new String[23][13];
				 ArrHeader[0]=new String[] {"供货单位:"+strfahdw,"","","","发站:"+strfaz,"","代表车号:"+strdiabch,"","","收款单位:"+strshoukdw,"","","第<br>一<br>联<br>燃<br>料<br>部<br>门<br>存<br>档<br>联<br>共<br>三<br>联"};
				 ArrHeader[1]=new String[] {"发货日期:"+strfahrq,"","","","地区代码:"+strdiqdm,"","原收货人:"+stryuanshr,"","","开户银行:"+this.nvlStr(strkaihyh),"","",""};
				 ArrHeader[2]=new String[] {"验收日期:"+stryansrq,"","","","货物名称:"+strhuowmc,"","现收货人:"+strxianshr,"","","银行帐号:"+this.nvlStr(stryinhzh),"","",""};
				 ArrHeader[3]=new String[] {"发运数量:"+strfahsl+"吨"+strches+"车","","","","验收编号:"+this.nvlStr(stryansbh),"","发票编号:"+this.nvlStr(strfapbh),"","","合同编号:"+hetbh,"","",""};
				 ArrHeader[4]=new String[] {"数量、质量验收","","","","","","","","","","","",""};
				 ArrHeader[5]=new String[] {"","","合同标准","矿方","厂方","结算","相差数量","","折价标准","折合金额","","",""};
				 ArrHeader[6]=new String[] {"数量(吨)","",strgongfbz_sl,strgongfbz_sl,strchangfys_sl,strjiesbz_sl,strxiancsl_sl,"",strzhejbz_sl,strzhehje_sl,"","",""};
				 ArrHeader[7]=new String[] {"热量(kc/kg)","",strhetbz_Qnetar,strgongfbz_Qnetar,strchangfys_Qnetar,strjiesbz_Qnetar,strxiancsl_Qnetar,"",strzhejbz_Qnetar,strzhehje_Qnetar,"","",""};
				 ArrHeader[8]=new String[] {"硫分(%)","",strhetbz_Std,strgongfbz_Std,strchangfys_Std,strjiesbz_Std,strxiancsl_Std,"",strzhejbz_Std,strzhehje_Std,"","",""};
				 ArrHeader[9]=new String[] {"含税单价","","单价","金额","补(扣)价款","价款合计","","","税率","税款","价税合计","",""};
				 ArrHeader[10]=new String[] {strshijfk,"",strbuhsdj,strjine,strbukouqjk,formatq(strjiakhj),"","",strshuil_mk,formatq(strshuik_mk),formatq(strjialhj),"",""};
				 ArrHeader[11]=new String[] {"铁路运费","","杂费","补(扣)运杂费","计税扣除","不含税运费","","","税率","税款","运杂费合计","",""};
				 ArrHeader[12]=new String[] {String.valueOf(Double.parseDouble(strtielyf.equals("")?"0":strtielyf)+Double.parseDouble(strkuangqyf.equals("")?"0":strkuangqyf)),"",String.valueOf(Double.parseDouble(strtielzf.equals("")?"0":strtielzf)+Double.parseDouble(strkuangqzf.equals("")?"0":strkuangqzf)),strbukouqzf,String.valueOf(Double.parseDouble(strtielzf.equals("")?"0":strtielzf)+Double.parseDouble(strkuangqzf.equals("")?"0":strkuangqzf)),formatq(strbuhsyf),"","",strshuil_ys,formatq(strshuik_ys),formatq(stryunzshj),"",""};
				 ArrHeader[13]=new String[] {"合计(大写):"+strhej_dx,"","","","","","","","合计(小写):"+strhej_xx,"","","",""};
				 ArrHeader[14]=new String[] {"备注:"+strbeiz,"","","","","","","","过衡数量",strguohzl,"","",""};
								 
				 ArrHeader[15]=new String[] {"审<br>批<br>流<br>程","审批部门","燃料管理部","财务部","主管经理","总经理","结<br>算<br>流<br>程","会计审核:","会计主管:","出纳:","经办人:","领款人:",""};
				 ArrHeader[16]=new String[] {"","审批意见",yij_ranlb,yij_caiwb,yij_zhugjl,yij_zongjl,"","","","",strranlbmjbr,"",""};
				 ArrHeader[17]=new String[] {"","签字",qianz_ranlb,qianz_caiwb,qianz_zhugjl,qianz_zongjl,"","","","",strjiesrq,"",""};
				 ArrHeader[18]=new String[] {"","签字","","","","","","","","","","",""};
				 ArrHeader[19]=new String[] {"","签字","","","","","","","","","","",""};
					
				 ArrHeader[20]=new String[] {"以下内容由财务部填写","","","","","","","","","","","",""};
				 ArrHeader[21]=new String[] {"应付付款凭证号","应付付款凭证号","应付付款凭证号","应付付款凭证号","应付付款凭证号","应付付款凭证号","备注:","备注:","备注:","备注:","备注:","备注:",""};
				 ArrHeader[22]=new String[] {"应付发票凭证号","应付发票凭证号","应付发票凭证号","应付发票凭证号","应付发票凭证号","应付发票凭证号","备注:","备注:","备注:","备注:","备注:","备注:",""};
//				 Report rt=new Report();
				 rt.setTitle(Locale.jiesd_title,ArrWidth);
				 String tianbdw=getTianzdw(lgdiancxxb_id);//填制单位。（可根据条件来填入单位）
				 rt.setDefaultTitleLeft("填制单位："+tianbdw,4);
//				 rt.setDefaultTitle(5,3,"结算日期："+strjiesrq,Table.ALIGN_CENTER);
				 rt.setDefaultTitle(9,4,"编号:"+strbianh,Table.ALIGN_RIGHT);
			
				
//				 rt.createTitle(2,ArrWidth);
//				 rt.title.setCellValue(1,1,Locale.jiesd_title,13);
//				 rt.title.setCellAlign(1,1,Table.ALIGN_CENTER);
//				 
//				 rt.title.setCellValue(2,1,"填制单位："+tianbdw,8);
//				 rt.title.setCellAlign(2,1,Table.ALIGN_LEFT);
//				 rt.title.setCellValue(2,9,"编号:"+strbianh,5);
//				 rt.title.setCellAlign(2,9,Table.ALIGN_RIGHT);
				 rt.setBody(new Table(ArrHeader,0,0,0));
				 rt.body.setWidth(ArrWidth);	
				 
				 rt.body.mergeCell(1,1,1,4);
				 rt.body.mergeCell(1,5,1,6);
				 rt.body.mergeCell(1,7,1,9);
				 rt.body.mergeCell(1,10,1,12);
				 
				 
				 rt.body.mergeCell(2,1,2,4);
				 rt.body.mergeCell(2,5,2,6);
				 rt.body.mergeCell(2,7,2,9);
				 rt.body.mergeCell(2,10,2,12);
				 
				 
				 rt.body.mergeCell(3,1,3,4);
				 rt.body.mergeCell(3,5,3,6);
				 rt.body.mergeCell(3,7,3,9);
				 rt.body.mergeCell(3,10,3,12);
					
//				 rt.body.setCells(3,4,3,4,Table.PER_BORDER_RIGHT,0);
//				 rt.body.setCells(3,6,3,6,Table.PER_BORDER_RIGHT,0);
//				 rt.body.setCells(3,9,3,9,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.mergeCell(4,1,4,4);
				 rt.body.mergeCell(4,5,4,6);
				 rt.body.mergeCell(4,7,4,9);
				 rt.body.mergeCell(4,10,4,12);
				 
//				 rt.body.setCells(4,6,4,8,Table.PER_BORDER_RIGHT,0);
//				 rt.body.setCells(4,6,4,6,Table.PER_BORDER_RIGHT,0);
//				 rt.body.setCells(4,9,4,9,Table.PER_BORDER_RIGHT,0);
//				 rt.body.setCells(4,11,4,11,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.mergeCell(5,1,5,12);
				 
				 rt.body.mergeCell(6,1,6,2);
				 rt.body.mergeCell(6,7,6,8);
				 rt.body.mergeCell(6,10,6,12);
				 
				 rt.body.mergeCell(7,1,7,2);
				 rt.body.mergeCell(7,7,7,8);
				 rt.body.mergeCell(7,10,7,12);
				 
				 rt.body.mergeCell(8,1,8,2);
				 rt.body.mergeCell(8,7,8,8);
				 rt.body.mergeCell(8,10,8,12);
				 
				 rt.body.mergeCell(9,1,9,2);
				 rt.body.mergeCell(9,7,9,8);
				 rt.body.mergeCell(9,10,9,12);
				 
				 rt.body.mergeCell(10,1,10,2);
				 rt.body.mergeCell(10,6,10,8);
				 rt.body.mergeCell(10,11,10,12);
				 
				 rt.body.mergeCell(11,1,11,2);
				 rt.body.mergeCell(11,6,11,8);
				 rt.body.mergeCell(11,11,11,12);
				 
				 rt.body.mergeCell(12,1,12,2);
				 rt.body.mergeCell(12,6,12,8);
				 rt.body.mergeCell(12,11,12,12);
				 
				 rt.body.mergeCell(13,1,13,2);
				 rt.body.mergeCell(13,6,13,8);
				 rt.body.mergeCell(13,11,13,12);
				 
				 rt.body.mergeCell(14,1,14,8);//
				 rt.body.mergeCell(14,9,14,12);
				 
				 rt.body.mergeCell(15,1,15,8);//
				 rt.body.mergeCell(15,10,15,12);
				 
				 rt.body.mergeCell(16,1,20,1);//审批流程
				 rt.body.mergeCell(18,2,20,2);
				 rt.body.mergeCell(18,3,20,3);
				 rt.body.mergeCell(18,4,20,4);
				 rt.body.mergeCell(18,5,20,5);
				 rt.body.mergeCell(18,6,20,6);
				 rt.body.mergeCell(16,7,20,7);
				 rt.body.mergeCell(16,8,20,8);
				 rt.body.mergeCell(16,9,20,9);
				 rt.body.mergeCell(16,10,20,10);
				 
//				 rt.body.mergeCell(16,11,20,11);//经办人
				 rt.body.setCells(16,11,19,11,Table.PER_BORDER_BOTTOM,0);
				 rt.body.mergeCell(16,12,20,12);
				 
				 rt.body.mergeCell(21,1,21,12);
				 rt.body.mergeCell(22,1,22,6);
				 rt.body.mergeCell(22,7,23,12);
				 rt.body.mergeCell(23,1,23,6);
				 rt.body.mergeCell(1,13,23,13);
				 rt.body.setCells(5, 1, 12, 12, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 rt.body.setCells(5,1,13,12,Table.PER_ALIGN,Table.ALIGN_CENTER);//居中
				 rt.body.setCells(16,1,21,7,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setCells(1,13,1,13,Table.PER_ALIGN,Table.VALIGN_CENTER);
//				 rt.body.setCells(1,13,1,13,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setCells(16,8,16,12,Table.PER_VALIGN,Table.VALIGN_TOP);
				 rt.body.setCells(22,7,22,7,Table.PER_VALIGN,Table.VALIGN_TOP);
				 
				 rt.body.setCells(1,13,23,13,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(1,13,23,13,Table.PER_BORDER_BOTTOM,0);
				 rt.body.setCells(1,13,23,13,Table.PER_BORDER_TOP,0);
				 rt.body.setCells(1,13,23,13,Table.PER_BORDER_LEFT,1);
				 rt.body.setCells(1,1,1,12,Table.PER_BORDER_TOP,2);
				 rt.body.setCells(23,1,23,12,Table.PER_BORDER_BOTTOM,2);
				 rt.body.setCells(22,7,22,7,Table.PER_BORDER_BOTTOM,2);
				 rt.body.setBorder(2, 0, 0, 0);
//				 设置页数
				_CurrentPage = 1;
//					_AllPages = rt.body.getPages();
				if (_AllPages == 0) {
					_CurrentPage = 0;
				}
				// System.out.println(rt.getAllPagesHtml());
				String str="";
				rt.body.setCellValue(1, 13, "第一联燃料部门存档联<br>&nbsp&nbsp 共三联<br>");
				str=rt.getAllPagesHtml(0+iPageIndex*3);
				rt.body.setCellValue(1, 13, "第二联财务部门支付联<br>&nbsp&nbsp 共三联<br>");
				str+=rt.getAllPagesHtml(1+iPageIndex*3);
				rt.body.setCellValue(1, 13, "第三联财务部门票据联<br>&nbsp&nbsp 共三联<br>");
				str+=rt.getAllPagesHtml(2+iPageIndex*3);
				_AllPages=3;
				
//				 rt.title.rows[1].height=1;
//				 rt.title.rows[1].fontSize=1;
				rt.body.setRowHeight(10);
				return str;
			 }else if(type.equals("ZGDT")){
//				 中国大唐
				 
				 int ArrWidth[]=new int[] {125,70,65,65,65,65,73,70,86,75,87,59};
				 
				 String ArrHeader[][]=new String[34][12];
				 ArrHeader[0]=new String[] {"供货单位:"+strfahdw,"","","发站:",strfaz,"代表车号:",strdiabch,"","收款单位:",strshoukdw,"",""};
				 ArrHeader[1]=new String[] {"发货日期:"+strfahrq,"","","地区代码:",strdiqdm,"原收货人:",stryuanshr,"","开户银行:",strkaihyh,"",""};
				 ArrHeader[2]=new String[] {"验收日期:"+stryansrq,"","","货物名称:",strhuowmc,"现收货人:",strxianshr,"","银行帐号:",stryinhzh,"",""};
//				 ArrHeader[3]=new String[] {"发运数量(吨):",strfahsl," 车数:"+strches,"验收编号:",this.nvlStr(stryansbh),"发票编号:",this.nvlStr(strfapbh),"","兑付地点:",strduifdd,"付款方式:",strfukfs};
				 ArrHeader[3]=new String[] {"发运数量(吨):",strfahsl," 车数:"+strches,"验收编号:",this.nvlStr(stryansbh),"发票编号:",this.nvlStr(strfapbh),"","合同编号:",strHetbh,"",""};
				 ArrHeader[4]=new String[] {"质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","数量验收","","",""};
				 ArrHeader[5]=new String[] {"含税价:"+strshijfk+"(元)","合同标准","供方标准","厂方验收","结算标准","相差数量","折价标准","折合金额","供方数量","验收数量","亏吨数量","折合金额"};
				 ArrHeader[6]=new String[] {""+Locale.Qnetar_zhibb+"("+Locale.qiankmqk_danw+")",strhetbz_Qnetar,strgongfbz_Qnetar,strchangfys_Qnetar,strjiesbz_Qnetar,strxiancsl_Qnetar,strzhejbz_Qnetar,strzhehje_Qnetar,"(吨)","(吨)","(吨)","(元)"};
				 ArrHeader[7]=new String[] {""+Locale.Std_zhibb+"("+Locale.baifb_danw+")",strhetbz_Std,strgongfbz_Std,strchangfys_Std,strjiesbz_Std,strxiancsl_Std,strzhejbz_Std,strzhehje_Std,strgongfbz_sl,strchangfys_sl,strxiancsl_sl,strzhehje_sl};
				 ArrHeader[8]=new String[] {""+Locale.Shul_zhibb+"("+Locale.dun_danw+")",strhetbz_Shulzb,strfahsl,strchangfys_sl,strjiesbz_sl,strxiancsl_Shulzb,strzhejbz_Shulzb,strzhehje_Shulzb,"","","",""};
				 ArrHeader[9]=new String[] {""+Locale.Ad_zhibb+"("+Locale.baifb_danw+")",strhetbz_Ad,strgongfbz_Ad,strchangfys_Ad,strjiesbz_Ad,strxiancsl_Ad,strzhejbz_Ad,strzhehje_Ad,"","","",""};
				 ArrHeader[10]=new String[] {""+Locale.Vdaf_zhibb+"("+Locale.baifb_danw+")",strhetbz_Vdaf,strgongfbz_Vdaf,strchangfys_Vdaf,strjiesbz_Vdaf,strxiancsl_Vdaf,strzhejbz_Vdaf,strzhehje_Vdaf,"","","",""};
				 ArrHeader[11]=new String[] {""+Locale.Mt_zhibb+"("+Locale.baifb_danw+")",strhetbz_Mt,strgongfbz_Mt,strchangfys_Mt,strjiesbz_Mt,strxiancsl_Mt,strzhejbz_Mt,strzhehje_Mt,"","","",""};
				 ArrHeader[12]=new String[] {""+Locale.Qgrad_zhibb+"("+Locale.qiankmqk_danw+")",strhetbz_Qgrad,strgongfbz_Qgrad,strchangfys_Qgrad,strjiesbz_Qgrad,strxiancsl_Qgrad,strzhejbz_Qgrad,strzhehje_Qgrad,"","","",""};
				 ArrHeader[13]=new String[] {""+Locale.Qbad_zhibb+"("+Locale.qiankmqk_danw+")",strhetbz_Qbad,strgongfbz_Qbad,strchangfys_Qbad,strjiesbz_Qbad,strxiancsl_Qbad,strzhejbz_Qbad,strzhehje_Qbad,"","","",""};
				 ArrHeader[14]=new String[] {""+Locale.Had_zhibb+"("+Locale.baifb_danw+")",strhetbz_Had,strgongfbz_Had,strchangfys_Had,strjiesbz_Had,strxiancsl_Had,strzhejbz_Had,strzhehje_Had,"","","",""};
				 ArrHeader[15]=new String[] {""+Locale.Stad_zhibb+"("+Locale.baifb_danw+")",strhetbz_Stad,strgongfbz_Stad,strchangfys_Stad,strjiesbz_Stad,strxiancsl_Stad,strzhejbz_Stad,strzhehje_Stad,"","","",""};
				 ArrHeader[16]=new String[] {""+Locale.Mad_zhibb+"("+Locale.baifb_danw+")",strhetbz_Mad,strgongfbz_Mad,strchangfys_Mad,strjiesbz_Mad,strxiancsl_Mad,strzhejbz_Mad,strzhehje_Mad,"","","",""};
				 ArrHeader[17]=new String[] {""+Locale.Aar_zhibb+"("+Locale.baifb_danw+")",strhetbz_Aar,strgongfbz_Aar,strchangfys_Aar,strjiesbz_Aar,strxiancsl_Aar,strzhejbz_Aar,strzhehje_Aar,"","","",""};
				 ArrHeader[18]=new String[] {""+Locale.Aad_zhibb+"("+Locale.baifb_danw+")",strhetbz_Aad,strgongfbz_Aad,strchangfys_Aad,strjiesbz_Aad,strxiancsl_Aad,strzhejbz_Aad,strzhehje_Aad,"","","",""};
				 ArrHeader[19]=new String[] {""+Locale.Vad_zhibb+"("+Locale.baifb_danw+")",strhetbz_Vad,strgongfbz_Vad,strchangfys_Vad,strjiesbz_Vad,strxiancsl_Vad,strzhejbz_Vad,strzhehje_Vad,"","","",""};
				 ArrHeader[20]=new String[] {""+Locale.T2_zhibb+"("+Locale.shesd_danw+")",strhetbz_T2,strgongfbz_T2,strchangfys_T2,strjiesbz_T2,strxiancsl_T2,strzhejbz_T2,strzhehje_T2,"","","",""};
				 ArrHeader[21]=new String[] {""+Locale.Yunju_zhibb+"("+Locale.yuanmdmgl_daw+")",strhetbz_Yunju,strgongfbz_Yunju,strchangfys_Yunju,strjiesbz_Yunju,strxiancsl_Yunju,strzhejbz_Yunju,strzhehje_Yunju,"","","",""};
				 ArrHeader[22]=new String[] {""+Locale.Star_zhibb+"("+Locale.baifb_danw+")",strhetbz_Star,strgongfbz_Star,strchangfys_Star,strjiesbz_Star,strxiancsl_Star,strzhejbz_Star,strzhehje_Star,"","","",""};
				 
				 ArrHeader[23]=new String[] {"结算数量","不含税单价","金额","","补(扣)以前价款","补(扣)以前价款","价款合计","","税率","税款","价税合计","价税合计"};
				 ArrHeader[24]=new String[] {strjiesbz_sl,strbuhsdj,strjine,"",strbukouqjk," ",formatq(strjiakhj),"",strshuil_mk,formatq(strshuik_mk),formatq(strjialhj),"660165.61"};
				 ArrHeader[25]=new String[] {"煤款合计(大写):",strmeikhjdx,"","","","","","","","","",""};
				 ArrHeader[26]=new String[] {"铁路运费","铁路杂费","矿区运费","矿区杂费","补(扣)以前运杂费","补(扣)以前运杂费","不含税运费","","税率","税款","运杂费合计","运杂费合计"};
				 ArrHeader[27]=new String[] {strtielyf,strtielzf,strkuangqyf,strkuangqzf,strbukouqzf,"",formatq(strbuhsyf),"",strshuil_ys,formatq(strshuik_ys),formatq(stryunzshj),"151546.4"};
				 ArrHeader[28]=new String[] {"运杂费合计(大写):",stryunzfhjdx,"","","","","","","拒付运费",strkuidjfyf,"拒付杂费",strkuidjfzf};
				 
				 if(!strChaokdlx.equals("")){
//					 计算超\亏吨
					 ArrHeader[29]=new String[] {"合计(大写):",strhej_dx,"","","","","","","合计(小写):",strhej_xx,"超/亏吨量(吨)",strChaokdl};
				 }else{
//					 不计算超\亏吨
					 ArrHeader[29]=new String[] {"合计(大写):",strhej_dx,"","","","","","","合计(小写):",strhej_xx,"",""};
				 }
				 
				 ArrHeader[30]=new String[] {"备注:",strbeiz,"","","","","","","过衡重量(吨):",strguohzl,""+Locale.jiesslcy_title+"",stryuns};
				 ArrHeader[31]=new String[] {"电厂燃料部门:(盖章)","","电厂财务部门:(盖章)","","","质量监督处:(签章)","","领导审批:(签章)","","综合财务处:(签章)","",""};
				 ArrHeader[32]=new String[] {"经办人:"+this.nvlStr(strranlbmjbr),"","经办人:"+strchangcwjbr,"","","经办人:"+strzhijzxjbr,"",""+strlingdqz,"","经办人:"+strzonghcwjbr,"",""};
				 ArrHeader[33]=new String[] {""+strranlbmjbrq+"","",""+strchangcwjbrq+"","","",""+strzhijzxjbrq+"","",""+strlingdqzrq+"","",""+strzonghcwjbrq+"","",""};
				 
//				 定义页Title
//				 Report rt=new Report();
				 rt.setTitle(Locale.jiesd_title,ArrWidth);
				 String tianbdw=getTianzdw(lgdiancxxb_id);//填制单位。（可根据条件来填入单位）
				 rt.setDefaultTitleLeft("填制单位："+tianbdw,3);
				 rt.setDefaultTitle(5,3,"结算日期："+strjiesrq,Table.ALIGN_CENTER);
				 rt.setDefaultTitle(9,4,"编号:"+strbianh,Table.ALIGN_RIGHT);
				 rt.setBody(new Table(ArrHeader,0,0,0));
				 rt.body.setWidth(ArrWidth);	
				 
				 rt.body.mergeCell(1,1,1,3);
				 rt.body.mergeCell(1,7,1,8);
				 rt.body.mergeCell(1,10,1,12);
				 rt.body.setCells(1,4,1,4,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(1,6,1,6,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(1,9,1,9,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.mergeCell(2,1,2,3);
				 rt.body.mergeCell(2,7,2,8);
				 rt.body.mergeCell(2,10,2,12);
				 rt.body.setCells(2,4,2,4,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(2,6,2,6,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(2,9,2,9,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.mergeCell(3,1,3,3);
				 rt.body.mergeCell(3,7,3,8);
				 rt.body.mergeCell(3,10,3,12);
				 rt.body.setCells(3,4,3,4,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(3,6,3,6,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(3,9,3,9,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.mergeCell(4,7,4,8);
				 rt.body.mergeCell(4,10,4,12);//合同编号
				 rt.body.setCells(4,4,4,4,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(4,6,4,6,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(4,9,4,9,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(4,11,4,11,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.mergeCell(5,1,5,8);
				 rt.body.mergeCell(5,9,5,12);
				 
				 rt.body.mergeCell(23,3,23,4);
				 rt.body.mergeCell(23,5,23,6);
				 rt.body.mergeCell(23,7,23,8);
				 rt.body.mergeCell(23,11,23,12);
				 
				 rt.body.mergeCell(24,3,24,4);
				 rt.body.mergeCell(24,5,24,6);
				 rt.body.mergeCell(24,7,24,8);
				 rt.body.mergeCell(24,11,24,12);
				 
				 rt.body.setRowCells(25, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 
//				 设置指标对齐方式
				 rt.body.setCells(7, 1, 23, 11, Table.PER_ALIGN, Table.ALIGN_LEFT);
				 rt.body.setCells(7, 2, 23, 12, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 
				 rt.body.mergeCell(25,3,25,4);
				 rt.body.mergeCell(25,5,25,6);
				 rt.body.mergeCell(25,7,25,8);
				 rt.body.mergeCell(25,11,25,12);
				 
				 rt.body.mergeCell(26,2,26,12);					//煤款合计大写
				 rt.body.setCells(26,12,26,12,Table.PER_BORDER_RIGHT,1);
				 rt.body.setCells(26,1,26,1,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 
				 rt.body.mergeCell(27,5,27,6);
				 rt.body.mergeCell(27,7,27,8);
				 rt.body.mergeCell(27,11,27,12);
				 
				 rt.body.mergeCell(28,5,28,6);
				 rt.body.mergeCell(28,7,28,8);
				 rt.body.mergeCell(28,11,28,12);
				 
				 rt.body.mergeCell(29,2,29,8);//运费合计大写
				 rt.body.mergeCell(30,2,30,8);
				 
				 if(strChaokdlx.equals("")){
//					 不计算超亏吨
					 rt.body.mergeCell(30,10,30,12);
				 }
				 
				 rt.body.mergeCell(31,2,31,8);
				 rt.body.mergeCell(32,1,32,2);
				 rt.body.mergeCell(32,3,32,5);
				 rt.body.mergeCell(32,6,32,7);
				 rt.body.mergeCell(32,8,32,9);
				 rt.body.mergeCell(32,10,32,12);
				 
				 
				 rt.body.mergeCell(33,1,33,2);
				 rt.body.mergeCell(33,3,33,5);
				 rt.body.mergeCell(33,6,33,7);
				 rt.body.mergeCell(33,8,33,9);
				 rt.body.mergeCell(33,10,33,12);
				 
				 rt.body.mergeCell(34,1,34,2);
				 rt.body.mergeCell(34,3,34,5);
				 rt.body.mergeCell(34,6,34,7);
				 rt.body.mergeCell(34,8,34,9);
				 rt.body.mergeCell(34,10,34,12);
				 
				 rt.body.setCells(4,1,4,1,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(4,2,4,2,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.setCells(5, 1, 13, 12, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 rt.body.setCells(6,1,12,1,Table.PER_ALIGN,Table.ALIGN_LEFT);
				 rt.body.setCells(24,1,24,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setCells(27,1,27,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setCells(28,1,28,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setRowCells(23,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setRowCells(32,Table.PER_BORDER_BOTTOM,0);
				 rt.body.setRowCells(33,Table.PER_BORDER_BOTTOM,0);
				 rt.body.setRowCells(33,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setRowCells(34,Table.PER_ALIGN,Table.ALIGN_RIGHT);
				 
//				 设置隐藏行
				 if(Shulzb_bn){
					 rt.body.setRowCells(Shulzb_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Shulzb_row,0);
					 rt.body.rows[Shulzb_row].hidden=true;
				 }
				 if(Ad_bn){
					 
					 rt.body.setRowCells(Ad_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Ad_row,0);
					 rt.body.rows[Ad_row].hidden=true;
				 }
				 if(Vdaf_bn){
					 
					 rt.body.setRowCells(Vdaf_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Vdaf_row,0);
					 rt.body.rows[Vdaf_row].hidden=true;
				 }
				 if(Mt_bn){
					 
					 rt.body.setRowCells(Mt_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Mt_row,0);
					 rt.body.rows[Mt_row].hidden=true;
				 }
				 if(Qgrad_bn){
					 
					 rt.body.setRowCells(Qgrad_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Qgrad_row,0);
					 rt.body.rows[Qgrad_row].hidden=true;
				 }
				 if(Qbad_bn){
					 
					 rt.body.setRowCells(Qbad_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Qbad_row,0);
					 rt.body.rows[Qbad_row].hidden=true;
				 }
				 if(Had_bn){
					 
					 rt.body.setRowCells(Had_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Had_row,0);
					 rt.body.rows[Had_row].hidden=true;
				 }
				 if(Stad_bn){
					 
					 rt.body.setRowCells(Stad_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Stad_row,0);
					 rt.body.rows[Stad_row].hidden=true;
				 }
				 if(Star_bn){
					 
					 rt.body.setRowCells(Star_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Star_row,0);
					 rt.body.rows[Star_row].hidden=true;
				 }
				 if(Mad_bn){
					 
					 rt.body.setRowCells(Mad_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Mad_row,0);
					 rt.body.rows[Mad_row].hidden=true;
				 }
				 if(Aar_bn){
					 
					 rt.body.setRowCells(Aar_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Aar_row,0);
					 rt.body.rows[Aar_row].hidden=true;
				 }
				 if(Aad_bn){
					 
					 rt.body.setRowCells(Aad_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Aad_row,0);
					 rt.body.rows[Aad_row].hidden=true;
				 }
				 if(Vad_bn){
					 
					 rt.body.setRowCells(Vad_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Vad_row,0);
					 rt.body.rows[Vad_row].hidden=true;
				 }
				 if(T2_bn){
					 
					 rt.body.setRowCells(T2_row, Table.PER_USED, false);
					 rt.body.setRowHeight(T2_row,0);
					 rt.body.rows[T2_row].hidden=true;
				 }
				 
				 if(Yunju_bn){
				 
					 rt.body.setRowCells(Yunju_row, Table.PER_USED, false);
					 rt.body.setRowHeight(Yunju_row,0);
					 rt.body.rows[Yunju_row].hidden=true;
				 }
				 
			 }else if(type.equals("GD")){
//				 国电电力
				 if(lgdiancxxb_id==476){
//					 临时处理，青铝电厂
					 
					 String strGonghdc="";//供货地区
					 String strYunsdw="";//运输单位
					 String strHansdj="";//含税单价
					 String strBuhsdj="";//不含税单价
					 JDBCcon cn1 = new JDBCcon();
					 String sql1=
	                       "SELECT MAX(YD.MINGC) AS YUNSDW FROM CHEPB CP,YUNSDWB YD\n" +
	                       "       WHERE CP.YUNSDWB_ID = YD.ID\n" + 
	                       "             AND CP.ID IN (\n" + 
	                       "SELECT CHEPB_ID FROM DANJCPB WHERE YUNFJSB_ID = (select id from jiesyfb where bianm ='"+where+"'))";
					 ResultSetList rs1 = cn1.getResultSetList(sql1);
					 while(rs1.next()){
						 strYunsdw = rs1.getString("YUNSDW");
					 }
					 rs1.close();
					 String sql2 = 
						 "select m.mingc as mingc from jiesyfb jy,meikxxb m where jy.meikxxb_id = m.id and jy.bianm='"+where+"'";
					 ResultSetList rs2 = cn1.getResultSetList(sql2);
					 while(rs2.next()){
						 strGonghdc = rs2.getString("mingc");
					 }
					 rs2.close();
					 String sql3=
						 "select  jy.hansdj,(jy.hansdj-round_new(jy.hansdj*jy.shuil,2)) as buhsdj  from jiesyfb jy where jy.bianm='"+where+"'";
					 ResultSetList rs3 = cn1.getResultSetList(sql3);
					 while(rs3.next()){
						 strHansdj = rs3.getString("hansdj");
						 strBuhsdj = rs3.getString("buhsdj");
					 }
					 
					 String tianbdw=getTianzdw(lgdiancxxb_id);//填制单位。（可根据条件来填入单位）
					 if (strzhejbz_sl.equals("")){
						 strzhejbz_sl="0";
					 }
					 double meij=Double.parseDouble(strzhejbz_sl);//煤价
					 double mkoukxj=0;//扣款小计
					 boolean bTl_yunsfs=false;
					 boolean bGl_yunsfs=false; 
					 
					 if(!strzhejbz_Qnetar.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Qnetar));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Qnetar));
	//					 meij=meij+Double.parseDouble(strzhejbz_Qnetar);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Qnetar));
					 }
					 
					 if(!strzhejbz_Std.equals("")){
						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Std));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Std));
	//					 meij+=Double.parseDouble(strzhejbz_Std);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Std));
					 }
					 
					 if(!strzhejbz_Ad.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Ad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Ad));
	//					 meij+=Double.parseDouble(strzhejbz_Ad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Ad));
					 }
					 
					 if(!strzhejbz_Vdaf.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Vdaf));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Vdaf));
	//					 meij+=Double.parseDouble(strzhejbz_Vdaf);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Vdaf));
					 }
					 
					 if(!strzhejbz_Mt.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Mt));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Mt));
	//					 meij+=Double.parseDouble(strzhejbz_Mt);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Mt));
					 }
					 
					 if(!strzhejbz_Qgrad.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Qgrad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Qgrad));
	//					 meij+=Double.parseDouble(strzhejbz_Qgrad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Qgrad));
					 }
					 
					 if(!strzhejbz_Qbad.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Qbad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Qbad));
	//					 meij+=Double.parseDouble(strzhejbz_Qbad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Qbad));
					 }
					 
					 if(!strzhejbz_Had.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Had));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Had));
	//					 meij+=Double.parseDouble(strzhejbz_Had);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Had));
					 }
					 
					 if(!strzhejbz_Stad.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Stad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Stad));
	//					 meij+=Double.parseDouble(strzhejbz_Stad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Stad));
					 }
					 
					 if(!strzhejbz_Mad.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Mad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Mad));
	//					 meij+=Double.parseDouble(strzhejbz_Mad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Mad));
					 }
					 
					 if(!strzhejbz_Aar.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Aar));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Aar));
	//					 meij+=Double.parseDouble(strzhejbz_Aar);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Aar));
					 }
					 
					 if(!strzhejbz_Aad.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Aad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Aad));
	//					 meij+=Double.parseDouble(strzhejbz_Aad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Aad));
					 }
					 
					 if(!strzhejbz_Vad.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Vad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Vad));
	//					 meij+=Double.parseDouble(strzhejbz_Vad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Vad));
					 }
					 
					 if(!strzhejbz_T2.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_T2));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_T2));
	//					 meij+=Double.parseDouble(strzhejbz_T2);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_T2));
					 }
					 
					 if(!strzhejbz_Yunju.equals("")){
						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Yunju));
	//					 meij+=Double.parseDouble(strzhejbz_Yunju);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Yunju));
					 }
					 
					 if(!strzhejbz_Star.equals("")){
	//					 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Star));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Star));
	//					 meij+=Double.parseDouble(strzhejbz_Star);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Star));
					 }
					 
					 //格式扣款小计，避免出项多小数位
					 mkoukxj = CustomMaths.Round_new(mkoukxj, 2);
					 
					 if(stryunsfs.equals(Locale.tiel_yunsfs)){
	//					 铁路
						 bTl_yunsfs=true;
					 }else if(stryunsfs.equals(Locale.gongl_yunsfs)){
						 
						 bGl_yunsfs=true;
					 }
					 
					 
					 String Relkk="";	//热量扣款
					 String Huifkk="";	//灰分扣款
					 String Huiffkk="";	//挥发分扣款
					 String Shuifkk="";	//水分扣款
					 String liufkk="";	//硫分扣款
					 String Qitkk="";	//其他扣款
					 
					 Relkk=String.valueOf((-Double.parseDouble(strzhejbz_Qnetar.equals("")?"0":strzhejbz_Qnetar))
							 +(-Double.parseDouble(strzhejbz_Qgrad.equals("")?"0":strzhejbz_Qgrad))
							 +(-Double.parseDouble(strzhejbz_Qbad.equals("")?"0":strzhejbz_Qbad)));
					 
					 Huifkk=String.valueOf((-Double.parseDouble(strzhejbz_Ad.equals("")?"0":strzhejbz_Ad))
							 +(-Double.parseDouble(strzhejbz_Aad.equals("")?"0":strzhejbz_Aad)));
					 
					 Huiffkk=String.valueOf((-Double.parseDouble(strzhejbz_Vdaf.equals("")?"0":strzhejbz_Vdaf))
							 +(-Double.parseDouble(strzhejbz_Vad.equals("")?"0":strzhejbz_Vad)));
					 
					 Shuifkk=String.valueOf((-Double.parseDouble(strzhejbz_Mt.equals("")?"0":strzhejbz_Mt))
							 +(-Double.parseDouble(strzhejbz_Mad.equals("")?"0":strzhejbz_Mad)));
					 
					 liufkk=String.valueOf((-Double.parseDouble(strzhejbz_Std.equals("")?"0":strzhejbz_Std))
							 +(-Double.parseDouble(strzhejbz_Stad.equals("")?"0":strzhejbz_Stad))
							 +(-Double.parseDouble(strzhejbz_Star.equals("")?"0":strzhejbz_Star)) 
					 		);
					 
					 Qitkk=String.valueOf((-Double.parseDouble(strzhejbz_T2.equals("")?"0":strzhejbz_T2))
							 +(-Double.parseDouble(strzhejbz_Yunju.equals("")?"0":strzhejbz_Yunju)));
					 
					 
					 if(Relkk.equals("-0.0")){
						 
						 Relkk=""; 
					 }
					 
					 if(Huifkk.equals("-0.0")){
						 
						 Huifkk="";
					 }
					 
					 if(Huiffkk.equals("-0.0")){
						 
						 Huiffkk="";
					 }
					 
					 if(Shuifkk.equals("-0.0")){
						 
						 Shuifkk="";
					 }
					 
					 if(liufkk.equals("-0.0")){
											 
						 liufkk="";
					 }
					
					 if(Qitkk.equals("-0.0")){
						 
						 Qitkk="";
					 }
					 
					 
	//				 新增liuf增扣款累加
					 if((strjiesbz_Std.equals("")||strjiesbz_Std.equals("0"))
							 &&(strjiesbz_Star.equals("")||strjiesbz_Star.equals("0"))){
						 
						 strjiesbz_Std="";
					 }else if(!strjiesbz_Star.equals("")
							 &&!strjiesbz_Star.equals("0")){
						 
						 if(!strjiesbz_Std.equals("")){
							 
							 strjiesbz_Std=String.valueOf(Double.parseDouble(strjiesbz_Star)
							 				+Double.parseDouble(strjiesbz_Std));
						 }else{
							 
							 strjiesbz_Std=strjiesbz_Star;
						 }
					 }
					 
	//				 国电的结算单要显示除结算折价指标之外的其它指标
	//				 2010-01-19 ww
	//				 添加Aar、Star指标，国电结算单中显示收到基指标
					 String JieszbArray[] = null;
					 JieszbArray = getJieszbxx(table1,where);
					 
	//				 直取danpcjsmxb中的热值
	//				 if(strjiesbz_Qnetar.equals("")){
						 
						 strjiesbz_Qnetar = JieszbArray[0];
	//				 }
					 
					 if(strjiesbz_Ad.equals("")){
						 
						 strjiesbz_Ad = JieszbArray[8];
					 }
					 
					 if(strjiesbz_Aar.equals("")) {
						 strjiesbz_Aar = JieszbArray[9];
					 }
					 
					 if(strjiesbz_Vdaf.equals("")){
						 
						 strjiesbz_Vdaf = JieszbArray[4];
					 }
					 
					 if(strjiesbz_Mt.equals("")){
						 
						 strjiesbz_Mt = JieszbArray[5];
					 }
					 
					 if(strjiesbz_Std.equals("")){
						 
						 strjiesbz_Std = JieszbArray[1];
					 }
					 
					 if(strjiesbz_Star.equals("")) {
						 strjiesbz_Star = JieszbArray[3];
					 }
					 if (strjiesbz_Qnetar.equals("")){
						 strjiesbz_Qnetar="0";
					 }
					 if (strjiesbz_Aar.equals("")){
						 strjiesbz_Aar="0";
					 }
					 if (strjiesbz_Vdaf.equals("")){
						 strjiesbz_Vdaf="0";
					 }
					 if (strjiesbz_Mt.equals("")){
						 strjiesbz_Mt="0";
					 }
					 if (strjiesbz_Star.equals("")){
						 strjiesbz_Star="0";
					 }
					 
					 if(intLeix!=Locale.meikjs_feiylbb_id && intLeix!=Locale.liangpjs_feiylbb_id){
//						 结算类型不等于两票和煤款
						 
//						 让运费结算单上显示指标信息
						 long jiesyfb_id = 0;
						 jiesyfb_id = MainGlobal.getTableId(table2, "bianm", where);
						 
						 if("".equals(strchangfys_Yunju)){
//							 得到运距
							 strchangfys_Yunju = MainGlobal.getTableCol(table2, "nvl(yunj,0)", "id = "+jiesyfb_id);
							 
							 if(Double.parseDouble(strchangfys_Yunju)==0){
								 
								 sql1 = 
									 "SELECT nvl(max(zhi),0) AS lic FROM licb\n" +
									 "       WHERE liclxb_id = (SELECT ID FROM liclxb WHERE mingc='国铁')\n" + 
									 "             AND licb.faz_id = (SELECT id FROM chezxxb WHERE mingc='"+strfaz+"')";
								 
								 rs3 = cn1.getResultSetList(sql1);
								 if(rs3.next()){
									 
									 strchangfys_Yunju = rs3.getString("lic");
								 }
							 }
						 }
						 
						 sql1 = 
							 "SELECT\n" +
							 "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(qnet_ar,0))/SUM(jingz),2)) AS qnet_ar,\n" + 
							 "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(aar,0))/SUM(jingz),2)) AS aar,\n" + 
							 "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(vdaf,0))/SUM(jingz),2)) AS vdaf,\n" + 
							 "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(mt,0))/SUM(jingz),1)) AS mt,\n" + 
							 "       decode(SUM(nvl(jingz,0)),0,0,round_new(SUM(jingz*nvl(Star,0))/SUM(jingz),2)) AS star\n" + 
							 " FROM\n" + 
							 "--数量\n" + 
							 "(SELECT fahb_id,SUM(maoz-piz-zongkd) AS jingz FROM chepb WHERE ID IN (\n" + 
							 "  SELECT chepb_id FROM danjcpb d\n" + 
							 "         WHERE d.yunfjsb_id = "+jiesyfb_id+")\n" + 
							 "         GROUP BY fahb_id) sl,\n" + 
							 "\n" + 
							 "--质量\n" + 
							 "(SELECT fahb.id AS fahb_id,zhilb.* FROM fahb,zhilb WHERE fahb.zhilb_id = zhilb.id AND fahb.ID IN (\n" + 
							 "       SELECT fahb_id FROM chepb WHERE ID IN (\n" + 
							 "              SELECT chepb_id FROM danjcpb d\n" + 
							 "                     WHERE d.yunfjsb_id = "+jiesyfb_id+"))\n" + 
							 ") zl\n" + 
							 "WHERE sl.fahb_id = zl.fahb_id";
						 
						 rs3 = cn1.getResultSetList(sql1);
						 
						 if(rs3.next()){
							 
							 if(strjiesbz_Qnetar.equals("0")){
								 
								 strjiesbz_Qnetar = rs3.getString("qnet_ar");
							 }
							 if(strjiesbz_Aar.equals("0")){
								 
								 strjiesbz_Aar = rs3.getString("aar");
							 }
							 if(strjiesbz_Vdaf.equals("0")){
								 
								 strjiesbz_Vdaf = rs3.getString("vdaf");
							 }
							 if(strjiesbz_Mt.equals("0")){
								 
								 strjiesbz_Mt = rs3.getString("mt");
							 }
							 if(strjiesbz_Star.equals("0")){
								 
								 strjiesbz_Star = rs3.getString("star");
							 }
						 }
					 }
					 rs3.close();
					 cn1.Close();
					 
	//				 国电用Mj/kg
					 strjiesbz_Qnetar=String.valueOf(MainGlobal.kcalkg_to_Mjkg(Double.parseDouble(strjiesbz_Qnetar), 
							 ((Visit)this.getVisit()).getFarldec()));		//结算热量
					 
					 //根据xitxxb中的配置得到结算单中结算部门的名称
					 String BuM = "燃料部";
					 BuM = MainGlobal.getXitxx_item("结算", "结算部门名称", ""+lgdiancxxb_id, BuM);
					 
					 String ArrHeader[][]=new String[6][19];
					 ArrHeader[0]=new String[] {"","","","","","","","","","","","","","","","","","",""};
					 ArrHeader[1]=new String[] {"国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司"};
					 ArrHeader[2]=new String[] {"SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD."};
					 ArrHeader[3]=new String[] {"燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单"};
					 ArrHeader[4]=new String[] {"","","","","","","","","","","","","","","","","","",""};
					 ArrHeader[5]=new String[] {"单位："+tianbdw,"单位："+tianbdw,"单位："+tianbdw,"单位："+tianbdw,"日期："+stryansrq,"日期："+stryansrq,"日期："+stryansrq,"日期："+stryansrq,"单位：吨,元/吨,MJ/kg,%,元","单位：吨,元/吨,MJ/kg,%,元","单位：吨,元/吨,MJ/kg,%,元","单位：吨,元/吨,MJ/kg,%,元","单位：吨,元/吨,MJ/kg,%,元","单位：吨,元/吨,MJ/kg,%,元","单位：吨,元/吨,MJ/kg,%,元","编号："+strbianh,"编号："+strbianh,"编号："+strbianh,"编号："+strbianh};
	
					 String ArrBody[][]=new String[19][19];
					 ArrBody[0]=new String[] {"结算部门：" + BuM,"","","","供货单位："+strfahdw,"","","","供货地区："+strGonghdc,"","","","运输单位："+strYunsdw,"","","","品种："+strhuowmc,"",""};
					 ArrBody[1]=new String[] {"数量","","","","","单价","","","","","","","","","","","煤款","","税金"};
					 ArrBody[2]=new String[] {"票重","","","","","","扣款","","","","","","","单价合计","不含税价","","","",""};
					 ArrBody[3]=new String[] {"车数","数量","盈吨","亏吨","实收","煤价","热值","灰分","挥发分","水分","硫量","其他","小计","","","","","",""};
					 ArrBody[4]=new String[] {""+strches+"",""+strfahsl+"",""+("0.0".equals(strxiancsl_sl)?"":-Double.parseDouble(strxiancsl_sl)+"")+"",""+(Double.parseDouble(strxiancsl_sl)<0?"":""+strxiancsl_sl)+""+"",""+strchangfys_sl+"",""+meij+"",""+Relkk+"",""+Huifkk+"",""+Huiffkk+"",""+Shuifkk+"",""+liufkk+"",""+Qitkk+"",""+mkoukxj+"",""+strzhejbz_sl+"",""+strbuhsdj+"","",""+formatq(strjiakhj)+"","",""+formatq(strshuik_mk)+""};
					 ArrBody[5]=new String[] {"热值","灰分","挥发分","水分","硫量","应付价款","","应付税金","","其他扣款","","实付金额","","","","","","",""};
					 ArrBody[6]=new String[] {""+ (((Visit)this.getVisit()).getFarldec()==3 ? new DecimalFormat("0.000").format(Double.parseDouble(strjiesbz_Qnetar)) : strjiesbz_Qnetar)+"",""+ new DecimalFormat("0.00").format(Double.parseDouble(strjiesbz_Aar))+"",""+ new DecimalFormat("0.00").format(Double.parseDouble(strjiesbz_Vdaf))+"",""+new DecimalFormat("0.0").format(Double.parseDouble(strjiesbz_Mt))+"",""+new DecimalFormat("0.00").format(Double.parseDouble(strjiesbz_Star))+"",""+strjiakhj+"","",""+formatq(strshuik_mk)+"","","","",""+formatq(strjialhj)+"","","","","","","",""};
					 ArrBody[7]=new String[] {"运距(km)","","运费单价明细","","","","","","","","","","","","","","","","印花税"};
					 ArrBody[8]=new String[] {"国铁","地铁","国铁","地铁","矿运","专线","短运","汽运","其他运费","","","","","","运杂费(元/车)","","","",""};
					 ArrBody[9]=new String[] {"","","","","","","","","电附加","风沙","储装","道口","其它","小计","取送车","变更费","单价合计","不含税价",""};
					 ArrBody[10]=new String[] {strchangfys_Yunju,"",""+(bTl_yunsfs?String.valueOf(CustomMaths.Round_New(Double.parseDouble(strtielyf.equals("")?"0":strtielyf)/(Double.parseDouble(strjiesyfbz_sl)==0?1:Double.parseDouble(strjiesyfbz_sl)),2)):"")+"","",""+CustomMaths.Round_New(Double.parseDouble(strkuangqyf.equals("")?"0":strkuangqyf)/(Double.parseDouble(strjiesyfbz_sl)==0?1:Double.parseDouble(strjiesyfbz_sl)),2)+"","","",""+(bGl_yunsfs?(strshijfk.equals("")||strshijfk.equals("0"))?String.valueOf(CustomMaths.Round_New(Double.parseDouble(strtielyf.equals("")?"0":strtielyf)/(Double.parseDouble(strjiesyfbz_sl)==0?1:Double.parseDouble(strjiesyfbz_sl)),2)):strshijfk:"")+"","","","","","",
							 					""+(bTl_yunsfs?String.valueOf(CustomMaths.Round_New(Double.parseDouble(stryunzshj.equals("")?"0":stryunzshj)/Double.parseDouble(strches)==0?1:Double.parseDouble(strches),2)):"")+"","","",""+strHansdj+"",""+strBuhsdj+"",""};
					 ArrBody[11]=new String[] {"国铁运费","","地铁运费","","矿区运费","","专线运费","","短途运费","","汽车运费","","其他运费","","运杂费","","扣款","","实付运费金额"};
					 ArrBody[12]=new String[] {"","","","","","","","","","","","","","","","","亏吨费","其他",""};
					 ArrBody[13]=new String[] {""+(bTl_yunsfs?strtielyf:"")+"","","","",""+strkuangqyf+"","","","","","",""+(bGl_yunsfs?strtielyf:"")+"","","","","","",""+strbukouqzf+"",""+strbukmk+"",""+stryunzshj+""};
					 ArrBody[14]=new String[] {"注：根据本企业资金管理制度权限履行会审及审批程序，会审部门及审批人写明意见、签名及日期。","","","","","","","","","","","","","","","","","",""};
					 ArrBody[15]=new String[] {"","","","","","","","","","","","","","","","","","",""};
					 ArrBody[16]=new String[] {"","","","","","","","","","财务部：","","","燃料运输部：","","","","结算人：","",""};
					 ArrBody[17]=new String[] {"总经理：","","","","","主管领导：","","","","","","","","","","","","",""};
					 ArrBody[18]=new String[] {"","","","","","","","","","计划经营部：","","","生产技术部：","","","","核算人：","",""};
					 
					 int ArrWidth[]=new int[] {54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54};
	
	//				 定义页Title
					 rt.setTitle(new Table(ArrHeader,0,0,0));
					 rt.setBody(new Table(ArrBody,0,0,0));
					 rt.body.setWidth(ArrWidth);
					 rt.title.setWidth(ArrWidth);
	//				 合并单元格
	//				 表头_Begin
	//				 rt.title.merge(1, 1, 1, 19);
					 rt.title.merge(2, 1, 2, 19);
					 rt.title.merge(3, 1, 3, 19);
					 rt.title.merge(4, 1, 4, 19);
					 rt.title.merge(5, 1, 5, 19);
					 
					 rt.title.merge(6, 1, 6, 4);
					 rt.title.merge(6, 5, 6, 8);
					 rt.title.merge(6, 9, 6, 15);
					 rt.title.merge(6, 16, 6, 19);
					 
					 rt.title.setBorder(0,0,0,0);
					 rt.title.setRowCells(1,Table.PER_BORDER_BOTTOM,0);
					 rt.title.setRowCells(2,Table.PER_BORDER_BOTTOM,0);
					 rt.title.setRowCells(3,Table.PER_BORDER_BOTTOM,0);
					 rt.title.setRowCells(4,Table.PER_BORDER_BOTTOM,0);
					 rt.title.setRowCells(5,Table.PER_BORDER_BOTTOM,0);
					 rt.title.setRowCells(6,Table.PER_BORDER_BOTTOM,0);
					 
					 rt.title.setRowCells(1,Table.PER_BORDER_RIGHT,0);
					 rt.title.setRowCells(2,Table.PER_BORDER_RIGHT,0);
					 rt.title.setRowCells(3,Table.PER_BORDER_RIGHT,0);
					 rt.title.setRowCells(4,Table.PER_BORDER_RIGHT,0);
					 rt.title.setRowCells(5,Table.PER_BORDER_RIGHT,0);
					 rt.title.setRowCells(6,Table.PER_BORDER_RIGHT,0);
					 
					 rt.title.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 rt.title.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 rt.title.setRowCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 rt.title.setRowCells(4, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 rt.title.setRowCells(5, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 rt.title.setRowCells(6, Table.PER_ALIGN, Table.ALIGN_LEFT);
					 
	//				 字体
					 rt.title.setCells(2, 1, 2, 19, Table.PER_FONTNAME, "黑体");
					 rt.title.setCells(2, 1, 2, 19, Table.PER_FONTSIZE, 11);
					 rt.title.setCells(3, 1, 3, 19, Table.PER_FONTNAME, "Arial Unicode MS");
					 rt.title.setCells(3, 1, 3, 19, Table.PER_FONTSIZE, 12);
					 rt.title.setCells(4, 1, 4, 19, Table.PER_FONTNAME, "隶书");
					 rt.title.setCells(4, 1, 4, 19, Table.PER_FONTSIZE, 20);
	//				 字体				 
					 
	//				 图片
					 rt.title.setCellImage(1, 1, 110, 50, "imgs/report/GDBZ.gif");	//国电的标志（到现场要一个换上就行了）
					 rt.title.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_LEFT);
					 rt.title.setCellImage(5, 1, rt.title.getWidth()/3+30, 10, "imgs/report/GDHX.gif");
	//				 图片_End
					 
	//				 表头_End
	//				 表体_Begin
					 
					 rt.body.mergeCell(1,1,1,4);
					 rt.body.mergeCell(1,5,1,8);
					 rt.body.mergeCell(1,9,1,12);
					 rt.body.mergeCell(1,13,1,16);
					 rt.body.mergeCell(1,17,1,19);
					 rt.body.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_LEFT);
					 
					 rt.body.mergeCell(2,1,2,5);
					 rt.body.mergeCell(2,6,2,16);
					 rt.body.mergeCell(2,17,4,18);
					 rt.body.mergeCell(2,19,4,19);
					 rt.body.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(3,1,3,2);
					 rt.body.mergeCell(3,7,3,13);
					 rt.body.mergeCell(3,14,4,14);
					 rt.body.mergeCell(3,15,4,16);
					 rt.body.setRowCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.setRowCells(4, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(5,15,5,16);
					 rt.body.mergeCell(5,17,5,18);
					 rt.body.setRowCells(5, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(6,6,6,7);
					 rt.body.mergeCell(6,8,6,9);
					 rt.body.mergeCell(6,10,6,11);
					 rt.body.mergeCell(6,12,6,14);
					 rt.body.mergeCell(6,15,6,16);
					 rt.body.mergeCell(6,17,6,18);
					 rt.body.setRowCells(6, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(7,6,7,7);
					 rt.body.mergeCell(7,8,7,9);
					 rt.body.mergeCell(7,10,7,11);
					 rt.body.mergeCell(7,12,7,14);
					 rt.body.mergeCell(7,15,7,16);
					 rt.body.mergeCell(7,17,7,18);
					 rt.body.setRowCells(7, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(8,1,8,2);
					 rt.body.mergeCell(8,3,8,18);
					 rt.body.mergeCell(8,19,10,19);
					 rt.body.setRowCells(8, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(9,1,10,1);
					 rt.body.mergeCell(9,2,10,2);
					 rt.body.mergeCell(9,3,10,3);
					 rt.body.mergeCell(9,4,10,4);
					 rt.body.mergeCell(9,5,10,5);
					 rt.body.mergeCell(9,6,10,6);
					 rt.body.mergeCell(9,7,10,7);
					 rt.body.mergeCell(9,8,10,8);
					 rt.body.mergeCell(9,9,9,14);
					 rt.body.mergeCell(9,15,9,18);
					 rt.body.setRowCells(9, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.setRowCells(10, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.setRowCells(11, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(12,1,13,2);
					 rt.body.mergeCell(12,3,13,4);
					 rt.body.mergeCell(12,5,13,6);
					 rt.body.mergeCell(12,7,13,8);
					 rt.body.mergeCell(12,9,13,10);
					 rt.body.mergeCell(12,11,13,12);
					 rt.body.mergeCell(12,13,13,14);
					 rt.body.mergeCell(12,15,13,16);
					 rt.body.mergeCell(12,17,12,18);
					 rt.body.mergeCell(12,19,13,19);
					 rt.body.setRowCells(12, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.setRowCells(13, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(14,1,14,2);
					 rt.body.mergeCell(14,3,14,4);
					 rt.body.mergeCell(14,5,14,6);
					 rt.body.mergeCell(14,7,14,8);
					 rt.body.mergeCell(14,9,14,10);
					 rt.body.mergeCell(14,11,14,12);
					 rt.body.mergeCell(14,13,14,14);
					 rt.body.mergeCell(14,15,14,16);
					 rt.body.setRowCells(14, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(15,1,15,19);
					 rt.body.setRowCells(15, Table.PER_BORDER_BOTTOM, 2);
					 
	//				 “注：”(的下一行)
					 rt.body.mergeCell(16,1,16,19);
					 rt.body.setRowHeight(16,8);
					 rt.body.setRowCells(16, Table.PER_BORDER_BOTTOM, 0);
					 rt.body.setRowCells(16, Table.PER_BORDER_RIGHT, 0);
					 
					 rt.body.setBorder(0, 0, 2, 0);
					 rt.body.setCells(1, 1, 15, 1, Table.PER_BORDER_LEFT, 2);
					 rt.body.setCells(1, 19, 15, 19, Table.PER_BORDER_RIGHT, 2);
					 
					 
					 rt.body.mergeCell(17,1,17,5);
					 rt.body.mergeCell(17,6,17,9);
					 rt.body.mergeCell(17,10,17,12);
					 rt.body.mergeCell(17,13,17,16);
					 rt.body.mergeCell(17,17,17,19);
					 rt.body.setRowHeight(17,5);
					 rt.body.setRowCells(17, Table.PER_BORDER_BOTTOM, 0);
					 rt.body.setRowCells(17, Table.PER_BORDER_RIGHT, 0);
					 
					 rt.body.mergeCell(18,1,18,4);
					 rt.body.mergeCell(18, 6, 18, 19);
					// rt.body.setRowHeight(18,0);
					 rt.body.setRowCells(18, Table.PER_BORDER_BOTTOM, 0);
					 rt.body.setRowCells(18, Table.PER_BORDER_RIGHT, 0);
					 
					 rt.body.mergeCell(19,1,19,5);
					 rt.body.mergeCell(19,6,19,9);
					 rt.body.mergeCell(19,10,19,12);
					 rt.body.mergeCell(19,13,19,16);
					 rt.body.mergeCell(19,17,19,19);
					 rt.body.setRowHeight(19,5);
					 rt.body.setRowCells(19, Table.PER_BORDER_BOTTOM, 0);
					 rt.body.setRowCells(19, Table.PER_BORDER_RIGHT, 0);
					
				 }else{
//					 国电电力
					 
					 String tianbdw=getTianzdw(lgdiancxxb_id);//填制单位。（可根据条件来填入单位）
					 if (strzhejbz_sl.equals("")){
						 strzhejbz_sl="0";
					 }
					 double meij=Double.parseDouble(strzhejbz_sl);//煤价
					 double mkoukxj=0;//扣款小计
					 boolean bTl_yunsfs=false;
					 boolean bGl_yunsfs=false; 
					 
					 if(!strzhejbz_Qnetar.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Qnetar));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Qnetar));
//						 meij=meij+Double.parseDouble(strzhejbz_Qnetar);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Qnetar));
					 }
					 
					 if(!strzhejbz_Std.equals("")){
						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Std));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Std));
//						 meij+=Double.parseDouble(strzhejbz_Std);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Std));
					 }
					 
					 if(!strzhejbz_Ad.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Ad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Ad));
//						 meij+=Double.parseDouble(strzhejbz_Ad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Ad));
					 }
					 
					 if(!strzhejbz_Vdaf.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Vdaf));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Vdaf));
//						 meij+=Double.parseDouble(strzhejbz_Vdaf);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Vdaf));
					 }
					 
					 if(!strzhejbz_Mt.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Mt));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Mt));
//						 meij+=Double.parseDouble(strzhejbz_Mt);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Mt));
					 }
					 
					 if(!strzhejbz_Qgrad.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Qgrad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Qgrad));
//						 meij+=Double.parseDouble(strzhejbz_Qgrad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Qgrad));
					 }
					 
					 if(!strzhejbz_Qbad.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Qbad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Qbad));
//						 meij+=Double.parseDouble(strzhejbz_Qbad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Qbad));
					 }
					 
					 if(!strzhejbz_Had.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Had));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Had));
//						 meij+=Double.parseDouble(strzhejbz_Had);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Had));
					 }
					 
					 if(!strzhejbz_Stad.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Stad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Stad));
//						 meij+=Double.parseDouble(strzhejbz_Stad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Stad));
					 }
					 
					 if(!strzhejbz_Mad.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Mad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Mad));
//						 meij+=Double.parseDouble(strzhejbz_Mad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Mad));
					 }
					 
					 if(!strzhejbz_Aar.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Aar));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Aar));
//						 meij+=Double.parseDouble(strzhejbz_Aar);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Aar));
					 }
					 
					 if(!strzhejbz_Aad.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Aad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Aad));
//						 meij+=Double.parseDouble(strzhejbz_Aad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Aad));
					 }
					 
					 if(!strzhejbz_Vad.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Vad));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Vad));
//						 meij+=Double.parseDouble(strzhejbz_Vad);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Vad));
					 }
					 
					 if(!strzhejbz_T2.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_T2));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_T2));
//						 meij+=Double.parseDouble(strzhejbz_T2);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_T2));
					 }
					 
					 if(!strzhejbz_Yunju.equals("")){
						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Yunju));
//						 meij+=Double.parseDouble(strzhejbz_Yunju);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Yunju));
					 }
					 
					 if(!strzhejbz_Star.equals("")){
//						 meij = CustomMaths.add(meij, Double.parseDouble(strzhejbz_Star));
						 meij = CustomMaths.add(meij, -Double.parseDouble(strzhejbz_Star));
//						 meij+=Double.parseDouble(strzhejbz_Star);
						 mkoukxj+=(-Double.parseDouble(strzhejbz_Star));
					 }
					 
					 //格式扣款小计，避免出项多小数位
					 mkoukxj = CustomMaths.Round_new(mkoukxj, 2);
					 
					 if(stryunsfs.equals(Locale.tiel_yunsfs)){
//						 铁路
						 bTl_yunsfs=true;
					 }else if(stryunsfs.equals(Locale.gongl_yunsfs)){
						 
						 bGl_yunsfs=true;
					 }
					 
					 
					 String Relkk="";	//热量扣款
					 String Huifkk="";	//灰分扣款
					 String Huiffkk="";	//挥发分扣款
					 String Shuifkk="";	//水分扣款
					 String liufkk="";	//硫分扣款
					 String Qitkk="";	//其他扣款
					 
					 Relkk=String.valueOf((-Double.parseDouble(strzhejbz_Qnetar.equals("")?"0":strzhejbz_Qnetar))
							 +(-Double.parseDouble(strzhejbz_Qgrad.equals("")?"0":strzhejbz_Qgrad))
							 +(-Double.parseDouble(strzhejbz_Qbad.equals("")?"0":strzhejbz_Qbad)));
					 
					 Huifkk=String.valueOf((-Double.parseDouble(strzhejbz_Ad.equals("")?"0":strzhejbz_Ad))
							 +(-Double.parseDouble(strzhejbz_Aad.equals("")?"0":strzhejbz_Aad)));
					 
					 Huiffkk=String.valueOf((-Double.parseDouble(strzhejbz_Vdaf.equals("")?"0":strzhejbz_Vdaf))
							 +(-Double.parseDouble(strzhejbz_Vad.equals("")?"0":strzhejbz_Vad)));
					 
					 Shuifkk=String.valueOf((-Double.parseDouble(strzhejbz_Mt.equals("")?"0":strzhejbz_Mt))
							 +(-Double.parseDouble(strzhejbz_Mad.equals("")?"0":strzhejbz_Mad)));
					 
					 liufkk=String.valueOf((-Double.parseDouble(strzhejbz_Std.equals("")?"0":strzhejbz_Std))
							 +(-Double.parseDouble(strzhejbz_Stad.equals("")?"0":strzhejbz_Stad))
							 +(-Double.parseDouble(strzhejbz_Star.equals("")?"0":strzhejbz_Star)) 
					 		);
					 
					 Qitkk=String.valueOf((-Double.parseDouble(strzhejbz_T2.equals("")?"0":strzhejbz_T2))
							 +(-Double.parseDouble(strzhejbz_Yunju.equals("")?"0":strzhejbz_Yunju)));
					 
					 
					 if(Relkk.equals("-0.0")){
						 
						 Relkk=""; 
					 }
					 
					 if(Huifkk.equals("-0.0")){
						 
						 Huifkk="";
					 }
					 
					 if(Huiffkk.equals("-0.0")){
						 
						 Huiffkk="";
					 }
					 
					 if(Shuifkk.equals("-0.0")){
						 
						 Shuifkk="";
					 }
					 
					 if(liufkk.equals("-0.0")){
											 
						 liufkk="";
					 }
					
					 if(Qitkk.equals("-0.0")){
						 
						 Qitkk="";
					 }
					 
					 
//					 新增liuf增扣款累加
					 if((strjiesbz_Std.equals("")||strjiesbz_Std.equals("0"))
							 &&(strjiesbz_Star.equals("")||strjiesbz_Star.equals("0"))){
						 
						 strjiesbz_Std="";
					 }else if(!strjiesbz_Star.equals("")
							 &&!strjiesbz_Star.equals("0")){
						 
						 if(!strjiesbz_Std.equals("")){
							 
							 strjiesbz_Std=String.valueOf(Double.parseDouble(strjiesbz_Star)
							 				+Double.parseDouble(strjiesbz_Std));
						 }else{
							 
							 strjiesbz_Std=strjiesbz_Star;
						 }
					 }
					 
//					 国电的结算单要显示除结算折价指标之外的其它指标
//					 2010-01-19 ww
//					 添加Aar、Star指标，国电结算单中显示收到基指标
					 String JieszbArray[] = null;
					 JieszbArray = getJieszbxx(table1,where);
					 
//					 直取danpcjsmxb中的热值
//					 if(strjiesbz_Qnetar.equals("")){
						 
						 strjiesbz_Qnetar = JieszbArray[0];
//					 }
					 
					 if(strjiesbz_Ad.equals("")){
						 
						 strjiesbz_Ad = JieszbArray[8];
					 }
					 
					 if(strjiesbz_Aar.equals("")) {
						 strjiesbz_Aar = JieszbArray[9];
					 }
					 
					 if(strjiesbz_Vdaf.equals("")){
						 
						 strjiesbz_Vdaf = JieszbArray[4];
					 }
					 
					 if(strjiesbz_Mt.equals("")){
						 
						 strjiesbz_Mt = JieszbArray[5];
					 }
					 
					 if(strjiesbz_Std.equals("")){
						 
						 strjiesbz_Std = JieszbArray[1];
					 }
					 
					 if(strjiesbz_Star.equals("")) {
						 strjiesbz_Star = JieszbArray[3];
					 }
					 if (strjiesbz_Qnetar.equals("")){
						 strjiesbz_Qnetar="0";
					 }
					 if (strjiesbz_Aar.equals("")){
						 strjiesbz_Aar="0";
					 }
					 if (strjiesbz_Vdaf.equals("")){
						 strjiesbz_Vdaf="0";
					 }
					 if (strjiesbz_Mt.equals("")){
						 strjiesbz_Mt="0";
					 }
					 if (strjiesbz_Star.equals("")){
						 strjiesbz_Star="0";
					 }
					 
					 
//					 国电用Mj/kg
					 strjiesbz_Qnetar=String.valueOf(MainGlobal.kcalkg_to_Mjkg(Double.parseDouble(strjiesbz_Qnetar), 
							 ((Visit)this.getVisit()).getFarldec()));		//结算热量
					 
					 //根据xitxxb中的配置得到结算单中结算部门的名称
					 String BuM = "燃料部";
					 BuM = MainGlobal.getXitxx_item("结算", "结算部门名称", ""+lgdiancxxb_id, BuM);
					 
					 String ArrHeader[][]=new String[6][19];
					 ArrHeader[0]=new String[] {"","","","","","","","","","","","","","","","","","",""};
					 ArrHeader[1]=new String[] {"国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司","国 电 电 力 发 展 股 份 有 限 公 司"};
					 ArrHeader[2]=new String[] {"SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD.","SP POWER DEVELOPMENT CO.,LTD."};
					 ArrHeader[3]=new String[] {"燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单","燃  料  （ 煤 炭 ）  结  算  单"};
					 ArrHeader[4]=new String[] {"","","","","","","","","","","","","","","","","","",""};
					 ArrHeader[5]=new String[] {"单位："+tianbdw,"单位："+tianbdw,"单位："+tianbdw,"单位："+tianbdw,"日期："+stryansrq,"日期："+stryansrq,"日期："+stryansrq,"日期："+stryansrq,"日期："+stryansrq,"合同号："+strHetbh,"合同号："+strHetbh,"合同号："+strHetbh,"单位：吨,元/吨,MJ/kg,%,元","单位：吨,元/吨,MJ/kg,%,元","单位：吨,元/吨,MJ/kg,%,元","单位：吨,元/吨,MJ/kg,%,元","编号："+strbianh,"编号："+strbianh,"编号："+strbianh};

//					 新增东胜签名内容
					 String ZHIBR="";
					 String JIESR="";
					 String SHULSHR="";
					 String ZHILSHR="";
					 String JIHYXB="";
					 String CWSHRY="";
					 String LDQM="";
					 String ZKJSQM="";
					 
						String SQL="SELECT TO_CHAR(RZB.SHIJ, 'yyyy\"年\"mm\"月\"dd\"日\"') SHIJ,\n" +
						"       (SELECT MAX(MINGC) FROM RENYXXB WHERE QUANC = RZB.CAOZY) CAORY\n" + 
						"  FROM (SELECT MAX(RZB.ID) ID\n" + 
						"          FROM JIESB, LIUCGZB RZB\n" + 
						"         WHERE JIESB.LIUCGZID = RZB.LIUCGZID\n" + 
						"           AND JIESB.BIANM = '"+where+"'\n" +
						"			AND rzb.id>=GETLIUCGZBID('"+where+"')\n" + 
						"         GROUP BY QIANQZTMC) RZ,\n" + 
						"       LIUCGZB RZB\n" + 
						" WHERE RZ.ID = RZB.ID\n" + 
						" ORDER BY RZB.ID";
							
						ResultSetList Qm_rsl = cn.getResultSetList(SQL);
						
						String qianm[][]=new String[Qm_rsl.getRows()][2];
						for(int i=0;i<Qm_rsl.getRows();i++){
							Qm_rsl.next();
							qianm[i][0]=Qm_rsl.getString(0);
							qianm[i][1]=Qm_rsl.getString(1);
						}
						
						if(qianm.length>=1){
							ZHIBR="<image src='imgs/dsqm/"+qianm[0][1]+".gif' width=\"80\" height=\"40\" align=\"left\"/>";
							JIESR="<image src='imgs/dsqm/"+qianm[0][1]+".gif' width=\"80\" height=\"40\" align=\"left\"/>";
						}
						if(qianm.length>=2){
							SHULSHR="<image src='imgs/dsqm/"+qianm[1][1]+".gif' width=\"80\" height=\"40\" align=\"left\"/>";
						}
						if(qianm.length>=3){
							ZHILSHR="<image src='imgs/dsqm/"+qianm[2][1]+".gif' width=\"80\" height=\"40\" align=\"left\"/>";
						}
						if(qianm.length>=4){
							JIHYXB="<image src='imgs/dsqm/"+qianm[3][1]+".gif' width=\"80\" height=\"40\" align=\"left\"/>";
						}
						if(qianm.length>=5){
							CWSHRY="<image src='imgs/dsqm/"+qianm[4][1]+".gif' width=\"80\" height=\"40\" align=\"left\"/>";
						}
						if(qianm.length>=6){
							LDQM="<image src='imgs/dsqm/"+qianm[5][1]+".gif' width=\"80\" height=\"40\" align=\"left\"/>";
						}
						if(qianm.length>=7){
							ZKJSQM="<image src='imgs/dsqm/"+qianm[6][1]+".gif' width=\"80\" height=\"40\" align=\"left\"/>";
						}
					 
					 String ArrBody[][]=new String[19][19];
					 ArrBody[0]=new String[] {"结算部门：" + BuM,"","","","供货单位："+strfahdw,"","","","供货地区：","","","","计划渠道："+strJihkj,"","","","品种："+strhuowmc,"",""};
					 ArrBody[1]=new String[] {"数量","","","","","单价","","","","","","","","","","","煤款","","税金"};
					 ArrBody[2]=new String[] {"票重","","","","","","扣款","","","","","","","单价合计","不含税价","","","",""};
					 ArrBody[3]=new String[] {"车数","数量","盈吨","亏吨","实收","煤价","热值","灰分","挥发分","水分","硫量","其他","小计","","","","","",""};
					 ArrBody[4]=new String[] {""+strches+"",""+strfahsl+"",""+("0.0".equals(strxiancsl_sl)?"":-Double.parseDouble(strxiancsl_sl)+"")+"",""+(Double.parseDouble(strxiancsl_sl)<0?"":""+strxiancsl_sl)+""+"",""+strchangfys_sl+"",""+meij+"",""+Relkk+"",""+Huifkk+"",""+Huiffkk+"",""+Shuifkk+"",""+liufkk+"",""+Qitkk+"",""+mkoukxj+"",""+strzhejbz_sl+"",""+strbuhsdj+"","",""+formatq(strjiakhj)+"","",""+formatq(strshuik_mk)+""};
					 ArrBody[5]=new String[] {"热值","灰分","挥发分","水分","硫量","应付价款","","应付税金","","其他扣款","","实付金额","","","","","","",""};
					 ArrBody[6]=new String[] {""+ (((Visit)this.getVisit()).getFarldec()==3 ? new DecimalFormat("0.000").format(Double.parseDouble(strjiesbz_Qnetar)) : strjiesbz_Qnetar)+"",""+ new DecimalFormat("0.00").format(Double.parseDouble(strjiesbz_Aar))+"",""+ new DecimalFormat("0.00").format(Double.parseDouble(strjiesbz_Vdaf))+"",""+new DecimalFormat("0.0").format(Double.parseDouble(strjiesbz_Mt))+"",""+new DecimalFormat("0.00").format(Double.parseDouble(strjiesbz_Star))+"",""+strjiakhj+"","",""+formatq(strshuik_mk)+"","","","",""+formatq(strjialhj)+"","","","","","","",""};
					 ArrBody[7]=new String[] {"运距(km)","","运费单价明细","","","","","","","","","","","","","","","","印花税"};
					 ArrBody[8]=new String[] {"国铁","地铁","国铁","地铁","矿运","专线","短运","汽运","其他运费","","","","","","运杂费(元/车)","","","",""};
					 ArrBody[9]=new String[] {"","","","","","","","","电附加","风沙","储装","道口","其它","小计","取送车","变更费","其它","小计",""};
					 ArrBody[10]=new String[] {"","",""+(bTl_yunsfs?String.valueOf(CustomMaths.Round_New(Double.parseDouble(strtielyf.equals("")?"0":strtielyf)/(Double.parseDouble(strjiesyfbz_sl)==0?1:Double.parseDouble(strjiesyfbz_sl)),2)):"")+"","",""+CustomMaths.Round_New(Double.parseDouble(strkuangqyf.equals("")?"0":strkuangqyf)/(Double.parseDouble(strjiesyfbz_sl)==0?1:Double.parseDouble(strjiesyfbz_sl)),2)+"","","",""+(bGl_yunsfs?String.valueOf(CustomMaths.Round_New(Double.parseDouble(strtielyf.equals("")?"0":strtielyf)/(Double.parseDouble(strjiesyfbz_sl)==0?1:Double.parseDouble(strjiesyfbz_sl)),2)):"")+"","","","","","",
							 					""+(bTl_yunsfs?String.valueOf(CustomMaths.Round_New(Double.parseDouble(stryunzshj.equals("")?"0":stryunzshj)/Double.parseDouble(strches)==0?1:Double.parseDouble(strches),2)):"")+"","","","","",""};
					 ArrBody[11]=new String[] {"国铁运费","","地铁运费","","矿区运费","","专线运费","","短途运费","","汽车运费","","其他运费","","运杂费","","扣款","","实付运费金额"};
					 ArrBody[12]=new String[] {"","","","","","","","","","","","","","","","","复检费","其他",""};
					 ArrBody[13]=new String[] {""+(bTl_yunsfs?strtielyf:"")+"","","","",""+strkuangqyf+"","","","","","",""+(bGl_yunsfs?strtielyf:"")+"","","","","","",""+strbukouqzf+"","",""+stryunzshj+""};
					 ArrBody[14]=new String[] {"注：根据本企业资金管理制度权限履行会审及审批程序，会审部门及审批人写明意见、签名及日期。","","","","","","","","","","","","","","","","","",""};
					 ArrBody[15]=new String[] {"","","","","","","","","","","","","","","","","","",""};
					 ArrBody[16]=new String[] {"公司(厂)分管领导：","",LDQM,"",BuM+"负责人：","",JIHYXB,"","质价审核：","",ZHILSHR,"","数量审核：","",SHULSHR,"","制表：",ZHIBR,""};
					 ArrBody[17]=new String[] {"","","","","","","","","","","","","","","","","","",""};
					 ArrBody[18]=new String[] {"总会计师：","",ZKJSQM,"","会计机构负责人：","",CWSHRY,"","财务审核：","","","","","付款：","","","结算人：",JIESR,""};

					 int ArrWidth[]=new int[] {55,65,50,50,65,58,50,50,54,52,52,52,50,50,50,50,55,50,67};

//					 定义页Title
					 rt.setTitle(new Table(ArrHeader,0,0,0));
					 rt.setBody(new Table(ArrBody,0,0,0));
					 rt.body.setWidth(ArrWidth);
					 rt.title.setWidth(ArrWidth);
//					 合并单元格
//					 表头_Begin
//					 rt.title.merge(1, 1, 1, 19);
					 rt.title.merge(2, 1, 2, 19);
					 rt.title.merge(3, 1, 3, 19);
					 rt.title.merge(4, 1, 4, 19);
					 rt.title.merge(5, 1, 5, 19);
					 
					 rt.title.merge(6, 1, 6, 4);
					 rt.title.merge(6, 5, 6, 9);
					 rt.title.merge(6, 10, 6, 12);
					 rt.title.merge(6, 13, 6, 16);
					 rt.title.merge(6, 17, 6, 19);
					 
					 rt.title.setBorder(0,0,0,0);
					 rt.title.setRowCells(1,Table.PER_BORDER_BOTTOM,0);
					 rt.title.setRowCells(2,Table.PER_BORDER_BOTTOM,0);
					 rt.title.setRowCells(3,Table.PER_BORDER_BOTTOM,0);
					 rt.title.setRowCells(4,Table.PER_BORDER_BOTTOM,0);
					 rt.title.setRowCells(5,Table.PER_BORDER_BOTTOM,0);
					 rt.title.setRowCells(6,Table.PER_BORDER_BOTTOM,0);
					 
					 rt.title.setRowCells(1,Table.PER_BORDER_RIGHT,0);
					 rt.title.setRowCells(2,Table.PER_BORDER_RIGHT,0);
					 rt.title.setRowCells(3,Table.PER_BORDER_RIGHT,0);
					 rt.title.setRowCells(4,Table.PER_BORDER_RIGHT,0);
					 rt.title.setRowCells(5,Table.PER_BORDER_RIGHT,0);
					 rt.title.setRowCells(6,Table.PER_BORDER_RIGHT,0);
					 
					 rt.title.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 rt.title.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 rt.title.setRowCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 rt.title.setRowCells(4, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 rt.title.setRowCells(5, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 rt.title.setRowCells(6, Table.PER_ALIGN, Table.ALIGN_LEFT);
					 
//					 字体
					 rt.title.setCells(2, 1, 2, 19, Table.PER_FONTNAME, "黑体");
					 rt.title.setCells(2, 1, 2, 19, Table.PER_FONTSIZE, 11);
					 rt.title.setCells(3, 1, 3, 19, Table.PER_FONTNAME, "Arial Unicode MS");
					 rt.title.setCells(3, 1, 3, 19, Table.PER_FONTSIZE, 12);
					 rt.title.setCells(4, 1, 4, 19, Table.PER_FONTNAME, "隶书");
					 rt.title.setCells(4, 1, 4, 19, Table.PER_FONTSIZE, 20);
//					 字体				 
					 
//					 图片
					 rt.title.setCellImage(1, 1, 110, 50, "imgs/report/GDBZ.gif");	//国电的标志（到现场要一个换上就行了）
					 rt.title.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_LEFT);
					 rt.title.setCellImage(5, 1, rt.title.getWidth()/3+30, 10, "imgs/report/GDHX.gif");
//					 图片_End
					 
//					 表头_End
//					 表体_Begin
					 
					 rt.body.mergeCell(1,1,1,4);
					 rt.body.mergeCell(1,5,1,8);
					 rt.body.mergeCell(1,9,1,12);
					 rt.body.mergeCell(1,13,1,16);
					 rt.body.mergeCell(1,17,1,19);
					 rt.body.setRowCells(1, Table.PER_ALIGN, Table.ALIGN_LEFT);
					 
					 rt.body.mergeCell(2,1,2,5);
					 rt.body.mergeCell(2,6,2,16);
					 rt.body.mergeCell(2,17,4,18);
					 rt.body.mergeCell(2,19,4,19);
					 rt.body.setRowCells(2, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(3,1,3,2);
					 rt.body.mergeCell(3,7,3,13);
					 rt.body.mergeCell(3,14,4,14);
					 rt.body.mergeCell(3,15,4,16);
					 rt.body.setRowCells(3, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.setRowCells(4, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(5,15,5,16);
					 rt.body.mergeCell(5,17,5,18);
					 rt.body.setRowCells(5, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(6,6,6,7);
					 rt.body.mergeCell(6,8,6,9);
					 rt.body.mergeCell(6,10,6,11);
					 rt.body.mergeCell(6,12,6,14);
					 rt.body.mergeCell(6,15,6,16);
					 rt.body.mergeCell(6,17,6,18);
					 rt.body.setRowCells(6, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(7,6,7,7);
					 rt.body.mergeCell(7,8,7,9);
					 rt.body.mergeCell(7,10,7,11);
					 rt.body.mergeCell(7,12,7,14);
					 rt.body.mergeCell(7,15,7,16);
					 rt.body.mergeCell(7,17,7,18);
					 rt.body.setRowCells(7, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(8,1,8,2);
					 rt.body.mergeCell(8,3,8,18);
					 rt.body.mergeCell(8,19,10,19);
					 rt.body.setRowCells(8, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(9,1,10,1);
					 rt.body.mergeCell(9,2,10,2);
					 rt.body.mergeCell(9,3,10,3);
					 rt.body.mergeCell(9,4,10,4);
					 rt.body.mergeCell(9,5,10,5);
					 rt.body.mergeCell(9,6,10,6);
					 rt.body.mergeCell(9,7,10,7);
					 rt.body.mergeCell(9,8,10,8);
					 rt.body.mergeCell(9,9,9,14);
					 rt.body.mergeCell(9,15,9,18);
					 rt.body.setRowCells(9, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.setRowCells(10, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.setRowCells(11, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(12,1,13,2);
					 rt.body.mergeCell(12,3,13,4);
					 rt.body.mergeCell(12,5,13,6);
					 rt.body.mergeCell(12,7,13,8);
					 rt.body.mergeCell(12,9,13,10);
					 rt.body.mergeCell(12,11,13,12);
					 rt.body.mergeCell(12,13,13,14);
					 rt.body.mergeCell(12,15,13,16);
					 rt.body.mergeCell(12,17,12,18);
					 rt.body.mergeCell(12,19,13,19);
					 rt.body.setRowCells(12, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.setRowCells(13, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(14,1,14,2);
					 rt.body.mergeCell(14,3,14,4);
					 rt.body.mergeCell(14,5,14,6);
					 rt.body.mergeCell(14,7,14,8);
					 rt.body.mergeCell(14,9,14,10);
					 rt.body.mergeCell(14,11,14,12);
					 rt.body.mergeCell(14,13,14,14);
					 rt.body.mergeCell(14,15,14,16);
					 rt.body.setRowCells(14, Table.PER_ALIGN, Table.ALIGN_CENTER);
					 
					 rt.body.mergeCell(15,1,15,19);
					 rt.body.setRowCells(15, Table.PER_BORDER_BOTTOM, 2);
					 
//					 “注：”(的下一行)
					 rt.body.mergeCell(16,1,16,19);
					 rt.body.setRowHeight(16,8);
					 rt.body.setRowCells(16, Table.PER_BORDER_BOTTOM, 0);
					 rt.body.setRowCells(16, Table.PER_BORDER_RIGHT, 0);
					 
					 rt.body.setBorder(0, 0, 2, 0);
					 rt.body.setCells(1, 1, 15, 1, Table.PER_BORDER_LEFT, 2);
					 rt.body.setCells(1, 19, 15, 19, Table.PER_BORDER_RIGHT, 2);
					 
					 
					 rt.body.mergeCell(17,1,17,2);
					 rt.body.mergeCell(17,3,17,4);
					 rt.body.mergeCell(17,5,17,6);
					 rt.body.setCellAlign(17, 5, Table.ALIGN_RIGHT);
					 rt.body.mergeCell(17,7,17,8);
					 rt.body.mergeCell(17,9,17,10);
					 rt.body.setCellAlign(17, 9, Table.ALIGN_RIGHT);
					 
					 rt.body.mergeCell(17,11,17,12);
					 rt.body.mergeCell(17,13,17,14);
					 rt.body.mergeCell(17,15,17,16);
					 rt.body.setCellAlign(17, 13, Table.ALIGN_RIGHT);
					 rt.body.mergeCell(17,18,17,19);
					 rt.body.setRowCells(17, Table.PER_BORDER_BOTTOM, 0);
					 rt.body.setRowCells(17, Table.PER_BORDER_RIGHT, 0);
					 
					 rt.body.mergeCell(18,1,18,19);
					 rt.body.setRowHeight(18,0);
					 rt.body.setRowCells(18, Table.PER_BORDER_BOTTOM, 0);
					 rt.body.setRowCells(18, Table.PER_BORDER_RIGHT, 0);
					 
					 rt.body.mergeCell(19,1,19,2);
					 rt.body.mergeCell(19,3,19,4);
					 rt.body.mergeCell(19,5,19,6);
					 rt.body.setCellAlign(19, 5, Table.ALIGN_RIGHT);
					 rt.body.mergeCell(19,7,19,8);
					 rt.body.mergeCell(19,9,19,10);
					 rt.body.setCellAlign(19, 9, Table.ALIGN_RIGHT);
					 
					 rt.body.mergeCell(19,11,19,12);
					 rt.body.setCellAlign(19, 14, Table.ALIGN_RIGHT);
					 rt.body.mergeCell(19,18,19,19);
					 rt.body.setRowCells(19, Table.PER_BORDER_BOTTOM, 0);
					 rt.body.setRowCells(19, Table.PER_BORDER_RIGHT, 0);
					 
				 } 
			 }
			 
				// 设置页数
				_CurrentPage = 1;
				_AllPages=1;
//				_AllPages = rt.body.getPages();
				if (_AllPages == 0) {
					_CurrentPage = 0;
				}
				// System.out.println(rt.getAllPagesHtml());
				return rt.getAllPagesHtml(iPageIndex);
			}catch(Exception e) {
		// TODO 自动生成方法存根
				e.printStackTrace();
			}finally{
				cn.Close();
			}
			return rt.getAllPagesHtml(iPageIndex);
	}
	
	private String getGongfsl(long jiesbid,String tables) {
		// TODO 自动生成方法存根
		JDBCcon con=new JDBCcon();
		String gongfsl="";
		try{
			
			String sql=" select gongf from jieszbsjb,"+tables+",zhibb "
			        + " where diancjsmkb.diancjsb_id= "+jiesbid+""
			        + " and diancjsmkb.id=jieszbsjb.jiesdid" 
			        + " and jieszbsjb.zhibb_id=zhibb.id and zhibb.bianm='数量'";
			
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				gongfsl=rs.getString("gongf");
				return gongfsl;
			}
			rs.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return null;
	}

	public String getTianzdw(long diancxxb_id) {
		String Tianzdw="";
		JDBCcon con=new JDBCcon();
		try{
			String sql="select quanc from diancxxb where id="+diancxxb_id;
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				Tianzdw=rs.getString("quanc");
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return Tianzdw;
	}
	
	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}
	
	private String FormatDate(Date _date) {
		if (_date == null) {
//			return MainGlobal.Formatdate("yyyy年 MM月 dd日", new Date());
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	
	private String nvlStr(String strValue){
		if (strValue==null) {
			return "";
		}else if(strValue.equals("null")){
			return "";
		}
		
		return strValue;
	}
	
	public String format(double dblValue,String strFormat){
		 DecimalFormat df = new DecimalFormat(strFormat);
		 return formatq(df.format(dblValue));
		 
	}
	
	public String[] getJieszbxx(String Table,String Jiesbh){
		
//	函数功能：
		
//		得到某一张结算单的所有指标
//	函数逻辑：
		
//		从单批次结算明细表中查到该结算单对应的值
//	函数形参：
		
//		结算单编号
		
		String sql = "";
		String Jieszb[] = new String[11];
		long TalbeId = 0;
		
		try {
			TalbeId = MainGlobal.getTableId(Table, "bianm", Jiesbh);
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}

		sql = 
			"select\n" +
			"   round_new(sum(decode(jiessl,0,0,jiessl*qnetar))/sum(decode(jiessl,0,1,jiessl))," + ((Visit)this.getVisit()).getFarldec() + ") as qnetar,\n" + 
			"   round_new(sum(decode(jiessl,0,0,jiessl*std))/sum(decode(jiessl,0,1,jiessl)),2) as std,\n" + 
			"   round_new(sum(decode(jiessl,0,0,jiessl*stad))/sum(decode(jiessl,0,1,jiessl)),2) as stad,\n" + 
			"   round_new(sum(decode(jiessl,0,0,jiessl*star))/sum(decode(jiessl,0,1,jiessl)),2) as star,\n" + 
			"   round_new(sum(decode(jiessl,0,0,jiessl*vdaf))/sum(decode(jiessl,0,1,jiessl)),2) as vdaf,\n" + 
			"   round_new(sum(decode(jiessl,0,0,jiessl*mt))/sum(decode(jiessl,0,1,jiessl)),1) as mt,\n" + 
			"   round_new(sum(decode(jiessl,0,0,jiessl*mad))/sum(decode(jiessl,0,1,jiessl)),2) as mad,\n" + 
			"   round_new(sum(decode(jiessl,0,0,jiessl*aad))/sum(decode(jiessl,0,1,jiessl)),2) as aad,\n" + 
			"   round_new(sum(decode(jiessl,0,0,jiessl*ad))/sum(decode(jiessl,0,1,jiessl)),2) as ad,\n" + 
			"   round_new(sum(decode(jiessl,0,0,jiessl*aar))/sum(decode(jiessl,0,1,jiessl)),2) as aar,\n" + 
			"   round_new(sum(decode(jiessl,0,0,jiessl*vad))/sum(decode(jiessl,0,1,jiessl)),2) as vad\n" + 
			" from\n" + 
			"   (select xuh,\n" + 
			"     max(mx.jiessl) as jiessl,\n" + 
			"     max(mx.qnetar)  as qnetar,\n" + 
			"     max(mx.std)  as std,\n" + 
			"     max(mx.stad)  as stad,\n" + 
			"     max(mx.star)  as star,\n" + 
			"     max(mx.vdaf)  as vdaf,\n" + 
			"     max(mx.mt)  as mt,\n" + 
			"     max(mx.mad) as mad,\n" + 
			"     max(mx.aad) as aad,\n" + 
			"     max(mx.ad) as ad,\n" + 
			"     max(mx.aar) as aar,\n" + 
			"     max(mx.vad) as vad\n" + 
			"   from danpcjsmxb mx\n" + 
			"        where leib=1 and jiesdid="+TalbeId+"\n" + 
			"        group by xuh)";
		
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			
			for(int i=0;i<rsl.getColumnCount();i++){
				
				Jieszb[i] = rsl.getString(i);
			}
		}
		rsl.close();
		con.Close();
		
		return Jieszb;
	}
	
	
	public String formatq(String strValue){//加千位分隔符
		String strtmp="",xiaostmp="",tmp="";
		int i=3;
		if(strValue.lastIndexOf(".")==-1){
			
			strtmp=strValue;
			if(strValue.equals("")){
				
				xiaostmp="";
			}else{
				
				xiaostmp=".00";
			}
			
		}else {
			
			strtmp=strValue.substring(0,strValue.lastIndexOf("."));
			
			if(strValue.substring(strValue.lastIndexOf(".")).length()==2){
				
				xiaostmp=strValue.substring(strValue.lastIndexOf("."))+"0";
			}else{
				
				xiaostmp=strValue.substring(strValue.lastIndexOf("."));
			}
			
		}
		tmp=strtmp;
		
		while(i<tmp.length()){
			strtmp=strtmp.substring(0,strtmp.length()-(i+(i-3)/3))+","+strtmp.substring(strtmp.length()-(i+(i-3)/3),strtmp.length());
			i=i+3;
		}
		
		return strtmp+xiaostmp;
	}
	
}