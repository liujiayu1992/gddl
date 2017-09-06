package com.zhiren.gs.bjdt.jiesgl;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.asset.ExternalAsset;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
//import com.zhiren.rmis.report.diancpmt.DiancpmtBean;

public class Changfjsd extends BasePage{
	
	public Changfjsd(){
		
	}
	
	public String getChangfjsd(String where,int iPageIndex,Visit visit){
		
		JDBCcon cn = new JDBCcon();
		ResultSet yfrs=null;
		Report rt=new Report();
		  try{
			 String sql=""; 
			 int intjiesdscfs = -1;
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
			 String strgongfbz_rl = "";
			 String strkuangfys_rl = "";
			 String strxiancsl_rl = "";
			 String strzhejbz_rl = "";
			 String strzhehje_rl = "";
			 String strgongfbz_lf = "";
			 String strkuangfys_lf = "";
			 String strxiancsl_lf = "";
			 String strzhejbz_lf = "";
			 String strzhehje_lf = "";
			 String strgongfsl = "";
			 String strxianssl = "";
			 String strkuidsl = "";
			 String strzhehje = "";
			 String strjiessl = "";
			 String strdanj = "";
			 String strjine = "";
			 String strbukouqjk = "";
			 String strjiakhj = "";
			 String strshuil_mk = "";
			 String strshuik_mk = "";
			 String strjialhj = "";
			 String strtielyf = "";
			 String strzaf = "";
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
			 String strgongfbz_hff="";
			 String strkuangfys_hff="";
			 String strxiancsl_hff="";
			 String strgongfbz_hf="";
			 String strkuangfys_hf="";
			 String strxiancsl_hf="";
			 String strzhejbz_hf="";
			 String strzhejbz_hff="";
			 String strzhehje_hff="";
			 String strgongfbz_sf="";
			 String strkuangfys_sf="";
			 String strxiancsl_sf="";
			 String strzhejbz_sf="";
			 String strzhehje_sf="";
			 String strzhehje_hf="";
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
			 String strkuangfjsdbh = "";
			 
			 String strzhiljdcldqz = "";
			 String strzhiljdcldqzrq = "";
			 String liucztb_id="";
			 double danjc = 0;
			 // 
			 double dblMeik =0;
			 double dblYunf =0;
			 sql="select djs.hetbh hetbh, nvl(rel.changf,0) changffrl,nvl(quanl.changf,0) changflf,nvl(hf.changf,0)  changfhf ,0 danjc,\n"+
                  "djs.diancxxb_id diancxxb_id,djs.bianm bianh,djs.jiesrq jiesrq,djs.jieslx jieslx,djs.id id,djs.gongysmc fahdw,\n"+
                  "djs.fahksrq fahrq,djs.fahjzrq jiezfhrq,djs.faz faz,djs.daibch daibch,djs.yuanshr yuanshr,djs.shoukdw shoukdw,\n"+
                  "djs.kaihyh kaihyh,djs.yansksrq yansrq,djs.yansjzrq jiezysrq,djs.meiz meiz,djs.xianshr xianshr,djs.zhangh zhangh,\n"+
                  "nvl(sl.gongf,0) gongfsl,djs.ches ches,djs.yansbh yansbh,djs.fapbh fapbh,djs.duifdd duifdd,djs.fukfs fukfs,go.bianm diqbm,\n"+
                  "djs.hansdj SHULZJBZ,\n"+
                  "nvl(rel.hetbz,0) GONGFRL,nvl(rel.jies,0) YANSRL,nvl(rel.jies,0)-to_number(substr(rel.hetbz,0,4))  YINGKRL,nvl(rel.zhejbz,0) RELZJBZ,nvl(rel.zhejje,0) RELZJJE,\n"+
                   "nvl(quanl.hetbz,0) LIUBZ,nvl(quanl.jies,0) LIUF,nvl(quanl.jies,0)-nvl(quanl.hetbz,0)  LIUYK,nvl(quanl.zhejbz,0) LIUYXDJ,nvl(quanl.zhejje,0) LIUYXJE,\n"+
                   "nvl(hff.gongf,0) GONGFHF,nvl(hff.jies,0) HUIFF,nvl(hff.yingk,0)  YINGKHF,nvl(hff.zhejbz,0) HUIFFYXDJ,nvl(hff.zhejje,0) HUIFFYXJE,\n"+
                   "nvl(shuif.gongf,0) GONGFSF,nvl(shuif.jies,0) SHUIF,nvl(shuif.yingk,0)  YINGKSF,nvl(shuif.zhejbz,0) SHUIFZJBZ,nvl(shuif.zhejje,0) SHUIFZJJE,\n"+
                   "nvl(sl.gongf,0) gongfsl,nvl(sl.jies,0)  YANSSL,nvl(sl.yingk,0) YINGK,nvl(sl.zhejje,0) SHULZJJE,\n"+
                   "djs.jiessl jiessl , djs.buhsdj danj,djs.buhsmk jiakje,djs.bukmk bukyqjk,djs.shuik JIAKHJ,djs.shuil jiaksl,\n"+
                   "djs.meikje jiaksk,djs.shuik+djs.meikje JIASJE,djs.guohl guohl,djs.beiz beiz,djs.ranlbmjbr ranlbmjbr,djs.ranlbmjbrq ranlbmjbrq,\n"+
                   "djs.ranlbmjbrq zhijzxjbrq,djs.liucgzid liucgzid,djs.ranlbmjbr,djs.ranlbmjbrq,djs.liucztb_id\n"+
                   "from diancjsmkb  djs\n"+
                   "left join\n"+
                   "--热量\n"+
                   "(select * from  jieszbsjb ji  left join zhibb zhi on(ji.zhibb_id=zhi.id) where zhi.mingc='收到基低位热值') rel\n"+
                   "on(djs.id=rel.jiesdid)\n"+
                   "--全硫\n"+
                   " left join\n"+
                   "(select * from  jieszbsjb ji left join zhibb zhi on(ji.zhibb_id=zhi.id) where zhi.mingc='干燥基全硫') quanl\n"+
                   "on (djs.id=quanl.jiesdid)\n"+
                   "--灰分\n"+
                   "left join\n"+ 
                   "(select * from  jieszbsjb ji left join zhibb zhi on(ji.zhibb_id=zhi.id) where zhi.mingc='干燥基灰分') hf\n"+
                   "on (hf.jiesdid=djs.id)\n"+
                    "--数量\n"+
                    "left join\n"+
                    "(select * from  jieszbsjb ji left join zhibb zhi on(ji.zhibb_id=zhi.id) where zhi.mingc='数量' ) sl\n"+
                    "on(sl.jiesdid=djs.id)\n"+
                    "--挥发份\n"+
                    "left join\n"+
                    "(select * from  jieszbsjb ji left join zhibb zhi on(ji.zhibb_id=zhi.id) where zhi.mingc='干燥无灰基挥发分') hff\n"+
                    "on(hff.jiesdid=djs.id)\n"+
                    "--水分\n"+
                    "left join\n"+
                    "(select * from  jieszbsjb ji left join zhibb zhi on(ji.zhibb_id=zhi.id) where zhi.mingc='全水分' ) shuif\n"+
                    "on(hff.jiesdid=djs.id)\n"+
                    "left join\n"+
                    "gongysb go\n"+
                    "on(djs.gongysb_id=go.id)\n"+
                    " where djs.bianm='"+where+"'\n"; 
			 ResultSet rs = cn.getResultSet(sql);
			
			 int intLeix=2;
			 long intDiancjsmkId=0;
			 long strkuangfjsmkb_id = -1;
			 boolean blnHasMeik =false;
			 
			 
			 String strhetbh = "";
			 String strchangffrl = "";
			 String strchangflf = "";
			 String strchangfhf = "";
			 
			 if(rs.next()){
				 
				 strhetbh = rs.getString("hetbh");//合同编号
				 strchangffrl = rs.getString("changffrl");//厂方热量
				 strchangflf = rs.getString("changflf");//厂方硫分
				 strchangfhf = rs.getString("changfhf"); //厂方灰分
				 
				 danjc = rs.getDouble("danjc");//价差
				 lgdiancxxb_id=rs.getLong("diancxxb_id");
				// strkuangfjsmkb_id = rs.getLong("kuangfjsmkb_id");
				 
				 strbianh=rs.getString("bianh");//编号
				 strjiesrq=FormatDate(rs.getDate("jiesrq"));
				 intLeix=rs.getInt("jieslx");//结算类型0 为两票一个供应商,1为两票两个供应商，2为运费或煤款
				 intDiancjsmkId =rs.getInt("id");//煤款id
				 strfahdw=rs.getString("fahdw");
				 
				 strfahksrq=rs.getString("fahrq");
				 strfahjzrq=rs.getString("jiezfhrq");
				 if(strfahksrq.equals(strfahjzrq)){
					 strfahrq = FormatDate(rs.getDate("fahrq"));//发货日期
				 }else{
					 strfahrq=FormatDate(rs.getDate("fahrq"))+" 至 "+FormatDate(rs.getDate("jiezfhrq"));
				 }
//				 strfahrq = rs.getString("fahrq");//发货日期
				 strfaz=rs.getString("faz");
				 strdiabch=rs.getString("daibch");
				 stryuanshr = rs.getString("yuanshr");//原收货人
				 strshoukdw = rs.getString("shoukdw");//收款单位
				 strkaihyh = rs.getString("kaihyh");//开户银行
				 strkaisysrq=rs.getString("yansrq");
				 strjiezysrq=rs.getString("jiezysrq");
				 if(strkaisysrq.equals(strjiezysrq)){
					 stryansrq=FormatDate(rs.getDate("yansrq"));
				 }else{
					 stryansrq=FormatDate(rs.getDate("yansrq"))+" 至 "+FormatDate(rs.getDate("jiezysrq"));
				 }
//				 stryansrq = rs.getString("yansrq");//验收日期
				 strhuowmc = rs.getString("MEIZ");//货物名称
				 strxianshr = rs.getString("xianshr");//现收货人
				 stryinhzh = rs.getString("zhangh");//帐号
				 strfahsl =rs.getString("gongfsl");//发运数量？？？？？？？？？？？？？？？？？？
				 strches = rs.getString("ches");//车数
				 stryansbh = rs.getString("yansbh");//验收编号
				 strfapbh = rs.getString("fapbh");//发票编号
				 strduifdd = rs.getString("duifdd");//兑付地点
				 strfukfs =rs.getString("fukfs") ;//付款方式
				 strdiqdm=rs.getString("diqbm");
				 strshijfk =rs.getString("SHULZJBZ");//含税单价？？？？？？？？？？？？？？？？？
				 strgongfbz_rl = rs.getString("GONGFRL");//供方热量
				 strkuangfys_rl = rs.getString("YANSRL");//验收热量
				 strxiancsl_rl=rs.getString("YINGKRL");//相差数量热量
				 strzhejbz_rl =rs.getString("RELZJBZ");//折价标准热量
				 strzhehje_rl = rs.getString("RELZJJE");//折合金额热量

				 strgongfbz_lf = rs.getString("LIUBZ");//供方标准硫分
				 strkuangfys_lf = rs.getString("LIUF");//验收硫分
				 strxiancsl_lf = rs.getString("LIUYK");//相差数量硫分
				 strzhejbz_lf = rs.getString("LIUYXDJ");//折价标准硫分
				 strzhehje_lf = rs.getString("LIUYXJE");//折合金额硫分

				 strgongfbz_hff = rs.getString("GONGFHF");//供方标准挥发分
				 strkuangfys_hff = rs.getString("HUIFF");//验收挥发分
				 strxiancsl_hff = rs.getString("YINGKHF");//相差数量挥发分
				 strzhejbz_hff = rs.getString("HUIFFYXDJ");//折价标准挥发分
				 strzhehje_hff = rs.getString("HUIFFYXJE");//折合金额挥发分


				 strgongfbz_sf = rs.getString("GONGFSF");//供方标准水分
				 strkuangfys_sf = rs.getString("SHUIF");//验收水分
				 strxiancsl_sf = rs.getString("YINGKSF");//相差数量水分
				 strzhejbz_sf = rs.getString("SHUIFZJBZ");//折价标准水分
				 strzhehje_sf = rs.getString("SHUIFZJJE");//折合金额水分

				 //*****************数量验收*****************//
				 strgongfsl = rs.getString("gongfsl");//供方数量
				 strxianssl =rs.getString("YANSSL");//验收数量
				 strkuidsl =rs.getString("YINGK");//亏吨数量
				 strzhehje = rs.getString("SHULZJJE");//折合金额

				 //********************其他*****************//
				 strjiessl = rs.getString("jiessl");//结算数量
				 strdanj = rs.getString("danj");//单价
				 strjine = rs.getString("JIAKJE");//金额
				 strbukouqjk = rs.getString("BUKYQJK");//补(扣)以前价款
				 strjiakhj = rs.getString("JIAKHJ");//价款合计
				 
				 strshuil_mk = rs.getString("JIAKSL");//税率(煤矿)
				 strshuik_mk = rs.getString("JIAKSK");//税款(煤矿)
				 
				 strjialhj = rs.getString("JIASJE");//价税合计
				 liucztb_id=rs.getString("liucztb_id");
				 
				 strguohzl =rs.getString("GUOHL");//过衡重量
				 strbeiz = nvlStr(rs.getString("beiz"));//备注
				
				 dblMeik= Double.parseDouble(strjialhj);
				 blnHasMeik=true;
				 
//				 intjiesdscfs = rs.getInt("jiesdscfs");
//				********************人员日期*****************//
				 strranlbmjbr=rs.getString("ranlbmjbr");//厂内录入人
				 strranlbmjbrq=FormatDate(rs.getDate("ranlbmjbrq"));//录入时间
				 if(rs.getDate("ranlbmjbrq").getYear()+1900<2009){//硬性规定09年前税率为0.13；之后的为0.17
					 strshuil_mk="0.13";
				 }else{
					 strshuil_mk="0.17";
				 }
				 /*//				 strchangcwjbr=rs.getString("changcwjbr");
				 strchangcwjbrq=FormatDate(rs.getDate("changcwjbrq"));
//				 strzhijzxjbr=rs.getString("zhijzxjbr");
				 strzhijzxjbr=rs.getString("gongsshr");
				 if(strzhijzxjbr==null){
					 strzhijzxjbr = " ";
				 }
				 strzhijzxjbrq=FormatDate(rs.getDate("zhijzxjbrq"));
				 
				 strzhiljdcldqz=rs.getString("zhiljdcldqz");
				 if(strzhiljdcldqz==null){
					 strzhiljdcldqz = "";
				 }
				 strzhiljdcldqzrq = FormatDate(rs.getDate("zhiljdcldqzrq"));
				 
				 strlingdqz=rs.getString("lingdqz");
				 if(strlingdqz==null){
					 strlingdqz = " ";
				 }
				 strlingdqzrq=FormatDate(rs.getDate("lingdqzrq"));
				 strzonghcwjbr=rs.getString("zonghcwjbr");
				 if(strzonghcwjbr==null){
					 strzonghcwjbr = " ";
				 }
				 strzonghcwjbrq=FormatDate(rs.getDate("zonghcwjbrq"));*/
				 
				 long liucgzid = rs.getLong("liucgzid");//流程跟踪外键
				 //查找每一级的操作员
				 String kfsql="select caozy,shij from liucgzb where  liucgzid ="+liucgzid+" order by id"; 
					 kfsql="select *  from liucgzb where id in(\n"+
					   " select max(gz.id) gz_id from liucgzb gz,liucdzb dz,liucztb zt1,liucztb zt2 ,liucztb zt3\n"+
					   " where gz.liucdzb_id=dz.id  and gz.liucgzid="+liucgzid+" and dz.liucztqqid=zt1.id and dz.liuczthjid=zt2.id and zt1.xuh<zt2.xuh \n"+
					   " and zt2.xuh<=zt3.xuh \n"+
					   "    and zt3.id="+liucztb_id+"\n"+
					   "   group by gz.liucdzb_id\n"+
					  "  ) order by shij";
					    
//				System.out.println(kfsql);
				 ResultSet kfrs = cn.getResultSet(kfsql);
				 List liuChengeXunXu =new ArrayList();
				 while(kfrs.next()){
					/* 
					 if(kfrs.getRow()==1){
						 strchangcwjbr=kfrs.getString("caozy");
						 strchangcwjbrq=this.FormatDate(kfrs.getDate("shij"));
					 }*/
					 if(kfrs.getRow()==1){//第1公司审核级
						 strzhijzxjbr=kfrs.getString("caozy");
						 strzhijzxjbrq=this.FormatDate(kfrs.getDate("shij"));
					 }
					 if(kfrs.getRow()==2){//第2公司审核级
						 strzhiljdcldqz=kfrs.getString("caozy");
						 strzhiljdcldqzrq=this.FormatDate(kfrs.getDate("shij"));
					 }
					 if(kfrs.getRow()==3){//第3公司审核级
						 strlingdqz=kfrs.getString("caozy");
						 strlingdqzrq=this.FormatDate(kfrs.getDate("shij"));
					 }
					 if(kfrs.getRow()==4){//第4公司审核级
						 strzonghcwjbr=kfrs.getString("caozy");
						 strzonghcwjbrq=this.FormatDate(kfrs.getDate("shij")); 
					 }	 
				 }
				 kfrs.close();
				 
//	根据条件在diancjsyf中取出数据			 
				 if(intjiesdscfs==1){
					 if ((blnHasMeik)&&(intLeix==0)){
						 sql="select * from diancjsyf where diancjsmkb_id="+intDiancjsmkId;
					 }else{
						 sql="select * from diancjsyf where bianh='"+where+"'";
					 }
				 	 yfrs = cn.getResultSet(sql);
				  }
				}else{
					sql="select * from diancjsyf where bianh='"+where+"'";
					yfrs = cn.getResultSet(sql);
				}
			 
			 if(intjiesdscfs==1){
			 	 if ((blnHasMeik)&&(intLeix==0)){
					 sql="select * from diancjsyf where diancjsmkb_id="+intDiancjsmkId;
				 }else{
					 sql="select * from diancjsyf where bianh='"+where+"'";
				 }
				 rs = cn.getResultSet(sql);
			 }
			 
			 if(yfrs!=null){
				 if (yfrs.next()){
					 strtielyf =yfrs.getString("TIELYF");//铁路运费
					 strzaf = yfrs.getString("ZAF");//杂费
					 strdiqdm=yfrs.getString("diqbm");
					 strbukouqzf = yfrs.getString("BUKYQYZF");//补(扣)以前运杂费
					 strjiskc = yfrs.getString("JISKC");//计税扣除
					 strbuhsyf =yfrs.getString("BUHSYF");//不含税运费
					 strshuil_ys = yfrs.getString("YUNFSL");//税率(运费)
					 strshuik_ys = yfrs.getString("YUNFSK");//税款(运费)
					 stryunzshj = yfrs.getString("YUNZFHJ");//运杂费合计
	//				 strbeiz =strbeiz + nvlStr(rs.getString("beiz"));//备注
					 dblYunf=yfrs.getDouble("YUNZFHJ");
	//				 double tmpshijfk=Double.parseDouble(strshijfk);
	//				 double tmpyunfsl=Double.parseDouble(strshuil_ys);
	//				 strdanj = String.valueOf((double)Math.round((tmpshijfk-(tmpshijfk*tmpyunfsl))*100)/100);//单价
					 
					 if(intLeix==2){
						 strshijfk =yfrs.getString("yunfhsdj");
						 lgdiancxxb_id=yfrs.getLong("diancxxb_id");
						 strbianh=yfrs.getString("bianh");
						 strjiesrq=FormatDate(yfrs.getDate("jiesrq"));
						 intLeix=yfrs.getInt("jieslx");//结算类型0 为两票一个供应商,1为两票两个供应商，2为运费或煤款
						 intDiancjsmkId =yfrs.getInt("id");//煤款id
						 strfahdw=yfrs.getString("fahdw");
						 strfahksrq=yfrs.getString("fahrq");
						 strfahjzrq=yfrs.getString("jiezfhrq");
						 if(strfahksrq.equals(strfahjzrq)){
							 strfahrq = FormatDate(yfrs.getDate("fahrq"));//发货日期
						 }else{
							 strfahrq=FormatDate(yfrs.getDate("fahrq"))+" 至 "+FormatDate(yfrs.getDate("jiezfhrq"));
						 }
	//					 strfahrq = yfrs.getString("fahrq");//发货日期
						 strfaz=yfrs.getString("faz");
						 strdiabch=yfrs.getString("daibch");
						 stryuanshr = yfrs.getString("yuanshr");//原收货人
						 strshoukdw = yfrs.getString("shoukdw");//收款单位
						 strkaihyh = yfrs.getString("kaihyh");//开户银行
						 strkaisysrq=yfrs.getString("yansrq");
						 strjiezysrq=yfrs.getString("yansjzrq");
						 if(strkaisysrq.equals(strjiezysrq)){
							 stryansrq=FormatDate(yfrs.getDate("yansrq"));
						 }else{
							 stryansrq=FormatDate(yfrs.getDate("yansrq"))+" 至 "+FormatDate(yfrs.getDate("yansjzrq"));
						 }
	//					 stryansrq = rs.getString("yansrq");//验收日期
						 strhuowmc = yfrs.getString("MEIZ");//货物名称
						 strxianshr = yfrs.getString("xianshr");//现收货人
						 stryinhzh = yfrs.getString("zhangh");//帐号
						 strfahsl =yfrs.getString("gongfsl");//发运数量
						 strches = yfrs.getString("ches");//车数
						 stryansbh = yfrs.getString("yansbh");//验收编号
						 strfapbh = yfrs.getString("fapbh");//发票编号
						 strduifdd = yfrs.getString("duifdd");//兑付地点
						 strfukfs = yfrs.getString("fukfs") ;//付款方式
						 strdiqdm=yfrs.getString("diqbm");
						 strjiessl = yfrs.getString("gongfsl");
						 strjine = "0";//金额
						 strbukouqjk = "0";//补(扣)以前价款
						 strjiakhj = "0";//价款合计
						 strshuil_mk = "0";//税率(煤矿)
						 strshuik_mk = "0";//税款(煤矿)
						 strjialhj = "0";//价税合计
						 strguohzl =yfrs.getString("GUOHL");//过衡重量
						 strbeiz = nvlStr(yfrs.getString("beiz"));//备注
						 dblMeik= Double.parseDouble(strjialhj);
						 blnHasMeik=true;
						 
						 strranlbmjbr=yfrs.getString("ranlbmjbr");
						 strranlbmjbrq=FormatDate(yfrs.getDate("ranlbmjbrq"));
						 strchangcwjbrq=FormatDate(yfrs.getDate("changcwjbrq"));
						 strzhijzxjbrq=FormatDate(yfrs.getDate("zhijzxjbrq"));
						 strlingdqzrq=FormatDate(yfrs.getDate("lingdqzrq"));
						 strzonghcwjbrq=FormatDate(yfrs.getDate("zonghcwjbrq"));
						 
					 }
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
			 
			 int ArrWidth[]=new int[] {128,78,76,76,76,76,136,76,80,76,76};
			 
			 String ArrHeader[][]=new String[22][11];
			 ArrHeader[0]=new String[] {"供货单位:"+strfahdw,"","","发站:",strfaz,"代表车号:",strdiabch,"收款单位:",strshoukdw,"",""};
			 ArrHeader[1]=new String[] {"发货日期:"+strfahrq,"","","地区代码:",strdiqdm,"原收货人:",stryuanshr,"开户银行:",strkaihyh,"",""};
			 ArrHeader[2]=new String[] {"验收日期:"+stryansrq,"","","货物名称:",strhuowmc,"现收货人:",strxianshr,"银行帐号:",stryinhzh,"",""};
			 ArrHeader[3]=new String[] {"发运数量:",strfahsl,"车数:"+strches,"验收编号:",stryansbh,"发票编号:",strfapbh,"合同编号:",strhetbh,"",""};
			 ArrHeader[4]=new String[] {"质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","数量验收","","",""};
			 ArrHeader[5]=new String[] {"含税价:"+strshijfk,"供方标准","厂方验收","结算质量","相差数量","折价标准","折合金额","供方数量","验收数量","盈亏吨数量","折合金额"};
			 ArrHeader[6]=new String[] {"热量Qnet,ar(KCAL/KG)",strgongfbz_rl,strchangffrl,strkuangfys_rl,strxiancsl_rl,strzhejbz_rl,strzhehje_rl,"(吨)","(吨)","(吨)","(元)"};
			 ArrHeader[7]=new String[] {"硫分St,ad(%)",strgongfbz_lf,strchangflf,strkuangfys_lf,strxiancsl_lf,strzhejbz_lf,strzhehje_lf,strgongfsl,strxianssl,strkuidsl,strzhehje};
			 ArrHeader[8]=new String[] {"挥发分vdaf(%)","","","","","","","","","",""};
			 ArrHeader[9]=new String[] {"灰分aad",strgongfbz_hf,strchangfhf,strkuangfys_hf,strxiancsl_hf,strzhejbz_hf,strzhehje_hf,"","","",""};
			 ArrHeader[10]=new String[] {"水分Mt(%)","","","","","","","","","",""};
			 ArrHeader[11]=new String[] {"结算数量","单价","金额","","补(扣)以前价款","补(扣)以前价款","价款合计","税率","税款","价税合计","价税合计"};
			 ArrHeader[12]=new String[] {strjiessl,strdanj,strjine,"",strbukouqjk," ",formatq(strjiakhj),strshuil_mk,formatq(strshuik_mk),formatq(strjialhj),"660165.61"};
			 ArrHeader[13]=new String[] {"煤款合计(大写):",strmeikhjdx,"","","","","","","","",""};
			 ArrHeader[14]=new String[] {"运费","杂费","补(扣)以前运杂费","","计税扣除","计税扣除","不含税运费","税率","税款","运杂费合计","运杂费合计"};
			 ArrHeader[15]=new String[] {strtielyf,strzaf,strbukouqzf,"",strjiskc,"",formatq(strbuhsyf),strshuil_ys,formatq(strshuik_ys),formatq(stryunzshj),"151546.4"};
			 ArrHeader[16]=new String[] {"运杂费合计(大写):",stryunzfhjdx,"","","","","","","","",""};
			 ArrHeader[17]=new String[] {"合计(大写):",strhej_dx,"","","","","","","合计(小写):",strhej_xx,""};
			 ArrHeader[18]=new String[] {"备注:",strbeiz,"","","","","","","过衡重量:",strguohzl,""};
			 ArrHeader[19]=new String[] {"电厂燃料部门:(盖章)","电厂财务部门:(盖章)","","质量监督处:(签章)","","质量监督处领导:(签章)","","领导审批:(签章)","","综合财务处:(签章)",""};
			 ArrHeader[20]=new String[] {"经办人:"+strranlbmjbr,"经办人:"+strchangcwjbr,"","经办人:"+strzhijzxjbr,"","经办人:"+strzhiljdcldqz,"",""+strlingdqz,"","经办人:"+strzonghcwjbr,""};
			 ArrHeader[21]=new String[] {""+strranlbmjbrq+"",""+strchangcwjbrq+"","",""+strzhijzxjbrq+"","",""+strzhiljdcldqzrq+"","",""+strlingdqzrq+"","",""+strzonghcwjbrq+"",""};
			 
//			 定义页Title
			 rt.setTitle("燃料采购结算通知单",ArrWidth);
//			 Visit visit =(Visit)this.getPage().getVisit();
			 String tianbdw=getTianzdw(lgdiancxxb_id);//填制单位。（可根据条件来填入单位）
			 rt.setDefaultTitleLeft("填制单位："+tianbdw,3);
			 rt.setDefaultTitle(4,4,"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;结算日期："+strjiesrq,Table.ALIGN_CENTER);
			 rt.setDefaultTitle(9,3,"编号:"+strbianh+"  "+strkuangfjsdbh,Table.ALIGN_RIGHT);
			 rt.setBody(new Table(ArrHeader,0,0,0));
//			 rt.body.setHeaderData(ArrHeader);
			 rt.body.setRowHeight(26);
			 rt.body.setWidth(ArrWidth);			 
			 rt.body.mergeCell(1,1,1,3);
			 rt.body.mergeCell(1,9,1,11);
			 rt.body.mergeCell(2,1,2,3);
			 rt.body.mergeCell(2,9,2,11);
			 rt.body.mergeCell(3,1,3,3);
			 rt.body.mergeCell(3,9,3,11);
			 rt.body.mergeCell(4,9,4,11);
			 rt.body.mergeCell(5,1,5,7);
			 rt.body.mergeCell(5,8,5,11);
			 rt.body.mergeCell(6,5,6,5);
			 rt.body.mergeCell(6,6,6,6);
			 rt.body.mergeCell(6,10,6,10);
			 rt.body.mergeCell(7,5,7,5);
			 rt.body.mergeCell(7,6,7,6);
			 rt.body.mergeCell(7,10,7,10);
			 rt.body.mergeCell(8,5,8,5);
			 rt.body.mergeCell(8,6,8,6);
			 rt.body.mergeCell(8,10,8,10);
			 rt.body.mergeCell(12,1,12,1);
			 rt.body.mergeCell(12,2,12,2);
			 rt.body.mergeCell(12,3,12,4);
			 rt.body.mergeCell(12,5,12,6);
			 rt.body.mergeCell(12,10,12,11);
			 rt.body.mergeCell(13,1,13,1);
			 rt.body.mergeCell(13,3,13,4);
			 rt.body.mergeCell(13,5,13,6);
			 rt.body.mergeCell(13,10,13,11);
			 rt.body.mergeCell(14,2,14,11);
			 rt.body.mergeCell(15,1,15,1);
			 rt.body.mergeCell(15,2,15,2);
			 rt.body.mergeCell(15,3,15,4);
			 rt.body.mergeCell(15,5,15,6);
			 rt.body.mergeCell(15,10,15,11);
			 rt.body.mergeCell(16,1,16,1);
			 rt.body.mergeCell(16,2,16,2);
			 rt.body.mergeCell(16,3,16,4);
			 rt.body.mergeCell(16,5,16,6);
			 rt.body.mergeCell(16,10,16,11);
			 rt.body.mergeCell(17,2,17,11);
			 rt.body.mergeCell(18,2,18,8);
			 rt.body.mergeCell(18,10,18,11);
			 rt.body.mergeCell(19,2,19,8);
			 rt.body.mergeCell(19,10,19,11);
			 
			 rt.body.mergeCell(20,2,20,3);
			 rt.body.mergeCell(20,4,20,5);
			 rt.body.mergeCell(20,6,20,7);
			 rt.body.mergeCell(20,8,20,9);
			 rt.body.mergeCell(20,10,20,11);
			 rt.body.mergeCell(21,2,21,3);
			 rt.body.mergeCell(21,4,21,5);
			 rt.body.mergeCell(21,6,21,7);
			 rt.body.mergeCell(21,8,21,9);
			 rt.body.mergeCell(21,10,21,11);
			 rt.body.mergeCell(22,2,22,3);
			 rt.body.mergeCell(22,4,22,5);
			 rt.body.mergeCell(22,6,22,7);
			 rt.body.mergeCell(22,8,22,9);
			 rt.body.mergeCell(22,10,22,11);
			 
			 rt.body.setCells(4,1,4,1,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(1,4,4,4,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(1,6,4,6,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(1,8,4,8,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(4,2,4,2,Table.PER_BORDER_RIGHT,0);
			 
			 rt.body.setCells(5,1,19,11,Table.PER_ALIGN,Table.ALIGN_CENTER);
			 rt.body.setCells(21,1,21,11,Table.PER_ALIGN,Table.ALIGN_CENTER);
			 rt.body.setCells(1,1,4,1,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setCells(1,4,4,4,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setCells(1,6,4,6,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setCells(1,8,4,8,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 
			 
			 rt.body.setCells(17,2,17,11,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setCells(14,2,14,11,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setCells(18,2,18,8,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setRowCells(20,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setRowCells(20,Table.PER_BORDER_BOTTOM,0);
			 rt.body.setRowCells(21,Table.PER_BORDER_BOTTOM,0);
			 rt.body.setRowCells(22,Table.PER_ALIGN,Table.ALIGN_RIGHT);
			 rt.body.setRowCells(1,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setRowCells(2,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setRowCells(3,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setRowCells(4,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 if(strlingdqz!=null){
				 if(strlingdqz.equals("白福贵")){
					 rt.body.setCellImage(21, 8, 74, 35, "http://10.66.3.193:8086/ftp/lingdqz/baijlqz.gif");
				 }else if(strlingdqz.equals("张志刚")){
					 rt.body.setCellImage(21, 8, 74, 35, "http://10.66.3.193:8086/ftp/lingdqz/zzg.gif");
				 }
			 }
			 
				// 设置页数
				_CurrentPage = iPageIndex;
//				_AllPages = rt.body.getPages();
				if (_AllPages == 0) {
					_CurrentPage = 0;
				}
				// System.out.println(rt.getAllPagesHtml());
				return rt.getAllPagesHtml(iPageIndex);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cn.Close();
			}
			return rt.getAllPagesHtml(iPageIndex);
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
	
//	 填制单位
//	private IDropDownBean _TianzdwValue;
//
//	private boolean _Tianzdwchange = false;
//
//	public IDropDownBean getTianzdwValue() {
//		if(_TianzdwValue==null){
//			_TianzdwValue=(IDropDownBean)getITianzdwModel().getOption(0);
//		}
//		return _TianzdwValue;
//	}
//
//	public void setTianzdwValue(IDropDownBean Value) {
//		if (_TianzdwValue != Value) {
//
//			_Tianzdwchange = true;
//		}
//		_TianzdwValue = Value;
//	}

//	private static IPropertySelectionModel _ITianzdwModel;
//
//	public void setITianzdwModel(IPropertySelectionModel value) {
//		_ITianzdwModel = value;
//	}
//
//	public IPropertySelectionModel getITianzdwModel() {
//		if (_ITianzdwModel == null) {
//			getITianzdwModels();
//		}
//		return _ITianzdwModel;
//	}
//
//	public IPropertySelectionModel getITianzdwModels() {
//		String sql = "select id,quanc from "
//					+" (select id,quanc,rownum+2 as xuh from diancxxb "
//					+" union select id,quanc,rownum as xuh from gongsxxb) "
//					+" order by xuh";
//		_ITianzdwModel = new IDropDownSelectionModel(sql);
//		return _ITianzdwModel;
//	}

	
}
