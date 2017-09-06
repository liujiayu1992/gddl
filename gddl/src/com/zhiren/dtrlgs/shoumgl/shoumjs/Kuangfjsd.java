package com.zhiren.dtrlgs.shoumgl.shoumjs;

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
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Kuangfjsd extends BasePage {
	public Kuangfjsd(){
		
	}
	/**
	 * @param where
	 * @param iPageIndex
	 * @param tables
	 * @return
	 */
	public String getKuangfjsd(String where,int iPageIndex,String tables){
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
			 String strjiesyfbz_sl = ""; //运费结算数量
			 
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
			 long liucgzid=0;
			 long liucztb_id=0;
			 double dblMeik =0;
			 double dblYunf =0;
			 String strkuidjfyf="";
			 String strkuidjfzf="";
			 
			 sql="select * from "+table1+" where bianm='"+where+"'"; 
			 ResultSet rs = cn.getResultSet(sql);
			 
			 int intLeix=3;
			 long intDiancjsmkId=0;
			 long strkuangfjsmkb_id = -1;
			 boolean blnHasMeik =false;		//是否有煤款
			 
			 if(rs.next()){
				 if(rs.getString("hetb_id")!=null  && !rs.getString("hetb_id").equals("")){
				 strHetbh = Shoumjsdcz.nvlStr(MainGlobal.getTableCol("hetb", "hetbh", "id", rs.getString("hetb_id")));
				 }
//				 danjc = rs.getDouble("danjc");
				 lgdiancxxb_id=rs.getLong("diancxxb_id");
				 strbianh=rs.getString("bianm");
				 strjiesrq=FormatDate(rs.getDate("jiesrq"));
				 intLeix=rs.getInt("jieslx");//结算类型0 为两票一个供应商,1为两票两个供应商，2为运费或煤款
				 intDiancjsmkId =rs.getInt("id");//煤款id
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
				 strChaokdlx=Shoumjsdcz.nvlStr(rs.getString("chaokdlx"));	//超亏吨类型
				 liucgzid=rs.getLong("liucgzid");//流程状态表id
				 liucztb_id=rs.getLong("liucztb_id");//现在的状态
				 
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
						 
					 }else if(rs2.getString("mingc").equals(Locale.Qnetar_zhibb)){
						 
						 strhetbz_Qnetar = rs2.getString("hetbz");
						 strgongfbz_Qnetar = String.valueOf(Shoumjsdcz.getUnit_transform(rs2.getString("mingc"),Locale.qiankmqk_danw,rs2.getDouble("GONGF")));	    //供方热量
						 strchangfys_Qnetar = String.valueOf(Shoumjsdcz.getUnit_transform(rs2.getString("mingc"),Locale.qiankmqk_danw,rs2.getDouble("CHANGF")));	//验收热量
						 strjiesbz_Qnetar = String.valueOf(Shoumjsdcz.getUnit_transform(rs2.getString("mingc"),Locale.qiankmqk_danw,rs2.getDouble("jies")));		//结算热量
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
//				 stryuns =rs.getString("jiesslcy");		//运损(结算数量差异)
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
	//					 strshijfk =rs.getString("yunfhsdj");
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
						 
						 strHetbh = Shoumjsdcz.nvlStr(MainGlobal.getTableCol("hetys", "hetbh", "id", rs.getString("hetb_id")));
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
						 strbuhsdj=rs.getString("hansdj");
						 
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
			 int Ad_row=9;
			 int Vdaf_row=10;
			 int Mt_row=11;
			 int Qgrad_row=12;
			 int Qbad_row=13;
			 int Had_row=14;
			 int Stad_row=15;
			 int Mad_row=16;
			 int Aar_row=17;
			 int Aad_row=18;
			 int Vad_row=19;
			 int T2_row=20;
			 int Yunju_row=21;
			 int Star_row=22;
			 
			 //设置指标字段不显示
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
			 
			 type=MainGlobal.getXitxx_item("结算", "结算单所属单位", String.valueOf(lgdiancxxb_id), "DTRL");
			 if(type.equals("DTRL")){
//				 中国大唐
				 
				 
//				String kfsql=  "select caozy,shij from\n"+
//				  "(select dz.liucztqqid,dz.liuczthjid,gz.caozy ,gz.shij from liucgzb gz,liucdzb dz\n"+ 
//						  " where gz.liucdzb_id=dz.id  and gz.liucgzid="+liucgzid+" ) dzzt\n"+
//						 " where not exists\n"+ 
//						    "  (select * from liucgzb ngz,liucdzb ndz\n"+
//						       " where  ngz.liucdzb_id=ndz.id\n"+  
//						        "       and dzzt.liucztqqid=ndz.liuczthjid and dzzt.liuczthjid=ndz.liucztqqid and  ngz.liucgzid="+liucgzid+"\n"+
//						     " )  order by shij\n";
				
		/*		 
			String kfsql="select *  from liucgzb where id in(\n"+
				   " select max(gz.id) gz_id from liucgzb gz,liucdzb dz,liucztb zt1,liucztb zt2 ,liucztb zt3\n"+
				   " where gz.liucdzb_id=dz.id  and gz.liucgzid="+liucgzid+" and dz.liucztqqid=zt1.id and dz.liuczthjid=zt2.id and zt1.xuh<zt2.xuh \n"+
				   " and zt2.xuh<=zt3.xuh \n"+
				   "    and zt3.id="+liucztb_id+"\n"+
//				   " and gz.liucgzid="+liucgzid+"\n"+
				   "   group by gz.liucdzb_id\n"+
				  "  ) order by shij";*/
				 

				    
			String kfsql="	    select liucgzb.shij,liucgzb.caozy,diancxxb.id,liucgzb.liucdzb_id  from liucgzb ,diancxxb,renyxxb where liucgzb.id in(\n"+
				 "select max(gz.id) gz_id from liucgzb gz,liucdzb dz,liucztb zt1,liucztb zt2 ,liucztb zt3\n"+
				 "where gz.liucdzb_id=dz.id  and gz.liucgzid="+liucgzid+" and dz.liucztqqid=zt1.id and dz.liuczthjid=zt2.id and zt1.xuh<zt2.xuh\n"+
				 "and zt2.xuh<=zt3.xuh \n"+
				 "   and zt3.id="+liucztb_id+"\n"+
				 "  group by gz.liucdzb_id\n"+
				 " ) and diancxxb.id=renyxxb.diancxxb_id and liucgzb.caozy=renyxxb.quanc  order by shij\n";
				  
	    String jingxb="经销部:(盖章)";
	    String jingxbld   ="经销部领导审核:(签章)";
	    if(cn.getHasIt(kfsql)){
				  ResultSetList kfrs=cn.getResultSetList(kfsql) ;
				  String dianc_id = kfrs.getString( kfrs.getRows()-1, 2) ;
				    int i=0;
				    if(dianc_id.equals("501")){
				    	 jingxb="办事处制表人:(盖章)";
				         jingxbld="办事处领导审核:(盖章)";
				    }
				  while(kfrs.next()){
					  if(kfrs.getString("id").equals(dianc_id)){
						    
						 if(i==0){//第1公司审核级
							 strchangcwjbr=kfrs.getString("caozy");
							 strchangcwjbrq=this.FormatDate(kfrs.getDate("shij"));
						 }
						 if(i==1){//第2公司审核级
							 strzhijzxjbr=kfrs.getString("caozy");
							 strzhijzxjbrq=this.FormatDate(kfrs.getDate("shij"));
						 }
						 if(i==2){//第3公司审核级
							 strlingdqz=kfrs.getString("caozy");
							 strlingdqzrq=this.FormatDate(kfrs.getDate("shij"));
						 }
						 if(i==3){//第4公司审核级
							 strzonghcwjbr=kfrs.getString("caozy");
							 strzonghcwjbrq=this.FormatDate(kfrs.getDate("shij")); 
						 }	
						 i++;
					  }
					 }
				      i=0;
					 kfrs.close();
				 //审批人员
	 		}
				 int ArrWidth[]=new int[] {125,70,65,65,65,65,73,70,86,75,87,59};
				 
				 String ArrHeader[][]=new String[33][12];
				 ArrHeader[0]=new String[] {"供货单位:"+strfahdw,"","","发站:",strfaz,"代表车号:",strdiabch,"","收款单位:",strshoukdw,"",""};
				 ArrHeader[1]=new String[] {"发货日期:"+strfahrq,"","","地区代码:",strdiqdm,"原收货人:",stryuanshr,"","开户银行:",strkaihyh,"",""};
				 ArrHeader[2]=new String[] {"验收日期:"+stryansrq,"","","货物名称:",strhuowmc,"现收货人:",strxianshr,"","银行帐号:",stryinhzh,"",""};
//				 ArrHeader[3]=new String[] {"发运数量(吨):",strfahsl," 车数:"+strches,"验收编号:",this.nvlStr(stryansbh),"发票编号:",this.nvlStr(strfapbh),"","兑付地点:",strduifdd,"付款方式:",strfukfs};
				 ArrHeader[3]=new String[] {"发运数量(吨):",strfahsl," 车数:"+strches,"验收编号:",this.nvlStr(stryansbh),"发票编号:",this.nvlStr(strfapbh),"","合同编号:",strHetbh,"",""};
				 ArrHeader[4]=new String[] {"质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","数量验收","","",""};
				 ArrHeader[5]=new String[] {"含税价:"+strshijfk+"(元)","合同标准","供方标准","厂方验收","结算标准","相差数量","折价标准","折合金额","供方数量","验收数量","亏吨数量","折合金额"};
				 ArrHeader[6]=new String[] {""+Locale.Qnetar_zhibb+"("+Locale.qiankmqk_danw+")",strhetbz_Qnetar,strgongfbz_Qnetar,strchangfys_Qnetar,strjiesbz_Qnetar,strxiancsl_Qnetar,strzhejbz_Qnetar,strzhehje_Qnetar,"(吨)","(吨)","(吨)","(元)"};
				 ArrHeader[7]=new String[] {""+Locale.Std_zhibb+"("+Locale.baifb_danw+")",strhetbz_Std,strgongfbz_Std,strchangfys_Std,strjiesbz_Std,strxiancsl_Std,strzhejbz_Std,strzhehje_Std,strgongfbz_sl,strchangfys_sl,strxiancsl_sl,strzhehje_sl};
				 ArrHeader[8]=new String[] {""+Locale.Ad_zhibb+"("+Locale.baifb_danw+")",strhetbz_Ad,strgongfbz_Ad,strchangfys_Ad,strjiesbz_Ad,strxiancsl_Ad,strzhejbz_Ad,strzhehje_Ad,"","","",""};
				 ArrHeader[9]=new String[] {""+Locale.Vdaf_zhibb+"("+Locale.baifb_danw+")",strhetbz_Vdaf,strgongfbz_Vdaf,strchangfys_Vdaf,strjiesbz_Vdaf,strxiancsl_Vdaf,strzhejbz_Vdaf,strzhehje_Vdaf,"","","",""};
				 ArrHeader[10]=new String[] {""+Locale.Mt_zhibb+"("+Locale.baifb_danw+")",strhetbz_Mt,strgongfbz_Mt,strchangfys_Mt,strjiesbz_Mt,strxiancsl_Mt,strzhejbz_Mt,strzhehje_Mt,"","","",""};
				 ArrHeader[11]=new String[] {""+Locale.Qgrad_zhibb+"("+Locale.qiankmqk_danw+")",strhetbz_Qgrad,strgongfbz_Qgrad,strchangfys_Qgrad,strjiesbz_Qgrad,strxiancsl_Qgrad,strzhejbz_Qgrad,strzhehje_Qgrad,"","","",""};
				 ArrHeader[12]=new String[] {""+Locale.Qbad_zhibb+"("+Locale.qiankmqk_danw+")",strhetbz_Qbad,strgongfbz_Qbad,strchangfys_Qbad,strjiesbz_Qbad,strxiancsl_Qbad,strzhejbz_Qbad,strzhehje_Qbad,"","","",""};
				 ArrHeader[13]=new String[] {""+Locale.Had_zhibb+"("+Locale.baifb_danw+")",strhetbz_Had,strgongfbz_Had,strchangfys_Had,strjiesbz_Had,strxiancsl_Had,strzhejbz_Had,strzhehje_Had,"","","",""};
				 ArrHeader[14]=new String[] {""+Locale.Stad_zhibb+"("+Locale.baifb_danw+")",strhetbz_Stad,strgongfbz_Stad,strchangfys_Stad,strjiesbz_Stad,strxiancsl_Stad,strzhejbz_Stad,strzhehje_Stad,"","","",""};
				 ArrHeader[15]=new String[] {""+Locale.Mad_zhibb+"("+Locale.baifb_danw+")",strhetbz_Mad,strgongfbz_Mad,strchangfys_Mad,strjiesbz_Mad,strxiancsl_Mad,strzhejbz_Mad,strzhehje_Mad,"","","",""};
				 ArrHeader[16]=new String[] {""+Locale.Aar_zhibb+"("+Locale.baifb_danw+")",strhetbz_Aar,strgongfbz_Aar,strchangfys_Aar,strjiesbz_Aar,strxiancsl_Aar,strzhejbz_Aar,strzhehje_Aar,"","","",""};
				 ArrHeader[17]=new String[] {""+Locale.Aad_zhibb+"("+Locale.baifb_danw+")",strhetbz_Aad,strgongfbz_Aad,strchangfys_Aad,strjiesbz_Aad,strxiancsl_Aad,strzhejbz_Aad,strzhehje_Aad,"","","",""};
				 ArrHeader[18]=new String[] {""+Locale.Vad_zhibb+"("+Locale.baifb_danw+")",strhetbz_Vad,strgongfbz_Vad,strchangfys_Vad,strjiesbz_Vad,strxiancsl_Vad,strzhejbz_Vad,strzhehje_Vad,"","","",""};
				 ArrHeader[19]=new String[] {""+Locale.T2_zhibb+"("+Locale.shesd_danw+")",strhetbz_T2,strgongfbz_T2,strchangfys_T2,strjiesbz_T2,strxiancsl_T2,strzhejbz_T2,strzhehje_T2,"","","",""};
				 ArrHeader[20]=new String[] {""+Locale.Yunju_zhibb+"("+Locale.yuanmdmgl_daw+")",strhetbz_Yunju,strgongfbz_Yunju,strchangfys_Yunju,strjiesbz_Yunju,strxiancsl_Yunju,strzhejbz_Yunju,strzhehje_Yunju,"","","",""};
				 ArrHeader[21]=new String[] {""+Locale.Star_zhibb+"("+Locale.baifb_danw+")",strhetbz_Star,strgongfbz_Star,strchangfys_Star,strjiesbz_Star,strxiancsl_Star,strzhejbz_Star,strzhehje_Star,"","","",""};
				 
				 ArrHeader[22]=new String[] {"结算数量","不含税单价","金额","","补(扣)以前价款","补(扣)以前价款","价款合计","","税率","税款","价税合计","价税合计"};
				 ArrHeader[23]=new String[] {strjiesbz_sl,strbuhsdj,formatq(strjine),"",strbukouqjk," ",formatq(strjiakhj),"",strshuil_mk,formatq(strshuik_mk),formatq(strjialhj),"660165.61"};
				 ArrHeader[24]=new String[] {"煤款合计(大写):",strmeikhjdx,"","","","","","","","","",""};
				 ArrHeader[25]=new String[] {"铁路运费","铁路杂费","矿区运费","矿区杂费","补(扣)以前运杂费","补(扣)以前运杂费","不含税运费","","税率","税款","运杂费合计","运杂费合计"};
				 ArrHeader[26]=new String[] {strtielyf,strtielzf,strkuangqyf,strkuangqzf,strbukouqzf,"",formatq(strbuhsyf),"",strshuil_ys,formatq(strshuik_ys),formatq(stryunzshj),"151546.4"};
				 ArrHeader[27]=new String[] {"运杂费合计(大写):",stryunzfhjdx,"","","","","","","拒付运费",strkuidjfyf,"拒付杂费",strkuidjfzf};
				 
				 if(!strChaokdlx.equals("")){
//					 计算超\亏吨
					 ArrHeader[28]=new String[] {"合计(大写):",strhej_dx,"","","","","","","合计(小写):",strhej_xx,"超/亏吨量(吨)",strChaokdl};
				 }else{
//					 不计算超\亏吨
					 ArrHeader[28]=new String[] {"合计(大写):",strhej_dx,"","","","","","","合计(小写):",strhej_xx,"",""};
				 }
				 
				 ArrHeader[29]=new String[] {"备注:",strbeiz,"","","","","","","过衡重量(吨):",strguohzl,""+Locale.jiesslcy_title+"",stryuns};
				 ArrHeader[30]=new String[] {jingxb,jingxb,jingxb,jingxbld,jingxbld,jingxbld,"领导审核:(签章)","领导审核:(签章)","领导审核:(签章)","财务部:(签章)","财务部:(签章)","财务部:(签章)"};
				 ArrHeader[31]=new String[] {"经办人:"+strchangcwjbr,"经办人:"+strchangcwjbr,"经办人:"+strchangcwjbr,"经办人:"+strzhijzxjbr,"经办人:"+strzhijzxjbr,"经办人:"+strzhijzxjbr,"经办人:"+strlingdqz,"经办人:"+strlingdqz,"经办人:"+strlingdqz,"经办人:"+strzonghcwjbr,"经办人:"+strzonghcwjbr,"经办人:"+strzonghcwjbr};
				 ArrHeader[32]=new String[] {strchangcwjbrq,strchangcwjbrq,strchangcwjbrq,""+strzhijzxjbrq+"",strzhijzxjbrq,strzhijzxjbrq,""+strlingdqzrq+"",strlingdqzrq,strlingdqzrq,""+strzonghcwjbrq+"",strzonghcwjbrq,strzonghcwjbrq};
				 
//				 定义页Title
//				 Report rt=new Report();
				 String tit="";
				 if(this.nvlStr(strfapbh)==""){
					tit="燃 料 采 购 结 算 单"; 
				 }else{
					 tit="燃 料 采 购 入 库 单";
				 }
 				 
				 rt.setTitle(tit,ArrWidth);
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
				 
				 rt.body.mergeCell(22,3,22,4);
				 rt.body.mergeCell(22,5,22,6);
				 rt.body.mergeCell(22,7,22,8);
				 rt.body.mergeCell(22,11,22,12);
				 
				 rt.body.mergeCell(23,3,23,4);
				 rt.body.mergeCell(23,5,23,6);
				 rt.body.mergeCell(23,7,23,8);
				 rt.body.mergeCell(23,11,23,12);
				 
				 rt.body.setRowCells(24, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 
//				 设置指标对齐方式
				 rt.body.setCells(7, 1, 22, 11, Table.PER_ALIGN, Table.ALIGN_LEFT);
				 rt.body.setCells(7, 2, 22, 12, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 
				 rt.body.mergeCell(24,3,24,4);
				 rt.body.mergeCell(24,5,24,6);
				 rt.body.mergeCell(24,7,24,8);
				 rt.body.mergeCell(24,11,24,12);
				 
				 rt.body.mergeCell(25,2,25,12);					//煤款合计大写
				 rt.body.setCells(25,12,25,12,Table.PER_BORDER_RIGHT,1);
				 rt.body.setCells(25,1,25,1,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 
				 rt.body.mergeCell(26,5,26,6);
				 rt.body.mergeCell(26,7,26,8);
				 rt.body.mergeCell(26,11,26,12);
				 
				 rt.body.mergeCell(27,5,27,6);
				 rt.body.mergeCell(27,7,27,8);
				 rt.body.mergeCell(27,11,27,12);
				 
				 rt.body.mergeCell(28,2,28,8);//运费合计大写
				 rt.body.mergeCell(29,2,29,8);
				 
				 if(strChaokdlx.equals("")){
//					 不计算超亏吨
					 rt.body.mergeCell(29,10,29,12);
				 }
				 
				 rt.body.mergeCell(30,2,30,8);
				 
				 
				 rt.body.mergeCell(31,1,31,3);
				 rt.body.mergeCell(31,4,31,6);
				 rt.body.mergeCell(31,7,31,9);
//				 rt.body.mergeCell(31,8,31,9);
				 rt.body.mergeCell(31,10,31,12);
				 
				 
				 rt.body.mergeCell(32,1,32,3);
				 rt.body.mergeCell(32,4,32,6);
				 rt.body.mergeCell(32,7,32,9);
//				 rt.body.mergeCell(32,8,32,9);
				 rt.body.mergeCell(32,10,32,12);
				 
				 rt.body.mergeCell(33,1,33,3);
				 rt.body.mergeCell(33,4,33,6);
				 rt.body.mergeCell(33,7,33,9);
//				 rt.body.mergeCell(33,8,33,9);
				 rt.body.mergeCell(33,10,33,12);
				 
				 rt.body.setCells(4,1,4,1,Table.PER_BORDER_RIGHT,0);
				 rt.body.setCells(4,2,4,2,Table.PER_BORDER_RIGHT,0);
				 
				 rt.body.setCells(5, 1, 12, 12, Table.PER_ALIGN, Table.ALIGN_CENTER);
				 rt.body.setCells(6,1,11,1,Table.PER_ALIGN,Table.ALIGN_LEFT);
				 rt.body.setCells(23,1,23,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setCells(26,1,26,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setCells(27,1,27,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setRowCells(22,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 
				 
				 
				 rt.body.setRowCells(31,Table.PER_BORDER_BOTTOM,0);
				 rt.body.setRowCells(32,Table.PER_BORDER_BOTTOM,0);
				 rt.body.setRowCells(32,Table.PER_ALIGN,Table.ALIGN_CENTER);
				 rt.body.setRowCells(33,Table.PER_ALIGN,Table.ALIGN_RIGHT);
				 
//				 设置隐藏行
//				 if(Qnetar_bn){
//					 rt.body.setRowCells(Qnetar_row, Table.PER_USED, false);
//					 rt.body.setRowHeight(Qnetar_row,0);
//					 rt.body.rows[Qnetar_row].hidden=true;
//				 }

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
