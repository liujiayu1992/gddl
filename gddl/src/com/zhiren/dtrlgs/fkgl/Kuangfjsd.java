package com.zhiren.dtrlgs.fkgl;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.tapestry.form.IPropertySelectionModel;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Kuangfjsd {

	public Kuangfjsd(){
		
	}
	
	public String getKuangfjsd(String where,int iPageIndex){
		
		JDBCcon cn = new JDBCcon();
		Report rt=new Report();
		  try{
			 String sql=""; 
			 String strjiesrq="";
			 String strfahdw="";
			 String strfaz="";
			 String strdiabch="";
			 long 	lgdiancxxb_id=0;
			 long 	lggongsxxb_id=0;
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
			 String strzhibr=" ";
			 String strzhibrq="";
			 String strlingdqz=" ";
			 String strlingdqzrq="";
			 String strzonghcwjbr=" ";
			 String strzonghcwjbrq="";
			 String strmeikhjdx="";
			 String stryunzfhjdx="";
			 
			 long diancjsmkb_id=-1;
			 String strdiancjsbh="";
			 
			 String strzhiljdcldqz = "";
			 String strzhiljdcldqzrq = "";
			 // 
			 double dblMeik =0;
			 double dblYunf =0;
			 sql="select * from kuangfjsmkb where bianh='"+where+"'"; 
			 ResultSet rs = cn.getResultSet(sql);
			 
			 int intLeix=2;
			 long intDiancjsmkId=0;
			 
			 boolean blnHasMeik =false;
			 
			 if(rs.next()){
				 lgdiancxxb_id=rs.getLong("diancxxb_id");
				 lggongsxxb_id=rs.getLong("gongsxxb_id");
				 
				 diancjsmkb_id=rs.getLong("diancjsmkb");
				 
				 strbianh=rs.getString("bianh");
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
				 strjiezysrq=rs.getString("yansjzrq");
				 if(strkaisysrq.equals(strjiezysrq)){
					 stryansrq=FormatDate(rs.getDate("yansrq"));
				 }else{
					 stryansrq=FormatDate(rs.getDate("yansrq"))+" 至 "+FormatDate(rs.getDate("yansjzrq"));
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
				 strshijfk =rs.getString("SHULZJBZ");//实际付款？？？？？？？？？？？？？？？？？
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

				 strgongfbz_hf = rs.getString("GONGFHF");//供方标准发分
				 strkuangfys_hf =rs.getString("HUIF");//验收发分
				 strxiancsl_hf = rs.getString("YINGKHF");//相差数量发分
				 strzhejbz_hf =rs.getString("HUIFYXDJ");//折价标准发分
				 strzhehje_hf =rs.getString("HUIFYXJE");//折合金额发分

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
				 strguohzl =rs.getString("GUOHL");//过衡重量
				 strbeiz = nvlStr(rs.getString("beiz"));//备注
				 dblMeik= Double.parseDouble(strjialhj);
				 blnHasMeik=true;
				 
//				********************人员日期*****************//
				 strzhibr=rs.getString("zhibr");
				 if(strzhibr==null){
					 strzhibr = " ";
				 }
				 strzhibrq=FormatDate(rs.getDate("zhibrq"));
				 strlingdqz=rs.getString("lingdqz");
				 if(strlingdqz==null){
					 strlingdqz = " ";
				 }
				 
				 strzhiljdcldqz=rs.getString("zhiljdcldqz");
				 if(strzhiljdcldqz==null){
					 strzhiljdcldqz = "";
				 }
				 strzhiljdcldqzrq = FormatDate(rs.getDate("zhiljdcldqzrq"));
				 
				 strlingdqzrq=FormatDate(rs.getDate("lingdqzrq"));
				 strzonghcwjbr=rs.getString("zonghcwjbr");
				 if( strzonghcwjbr==null){
					 strzonghcwjbr = " ";
				 }
				 strzonghcwjbrq=FormatDate(rs.getDate("zonghcwjbrq"));
				 
			 }
			 rs.close();
			 
			 if (blnHasMeik){
				 if(intLeix==0){
					 sql="select * from kuangfjsyf where KUANGFJSMKB_ID="+intDiancjsmkId;
				 }else{
					 sql="select * from kuangfjsyf where bianh=''";
				 }
			 }else{
				 sql="select * from kuangfjsyf where bianh='"+where+"'";
			 }
			 
			 rs = cn.getResultSet(sql);
			 if (rs.next()){
				 strtielyf =rs.getString("TIELYF");//铁路运费
				 strzaf = rs.getString("ZAF");//杂费
				 strdiqdm=rs.getString("diqbm");
				 strbukouqzf = rs.getString("BUKYQYZF");//补(扣)以前运杂费
				 strjiskc = rs.getString("JISKC");//计税扣除
				 strbuhsyf =rs.getString("BUHSYF");//不含税运费
				 strshuil_ys = rs.getString("YUNFSL");//税率(运费)
				 strshuik_ys = rs.getString("YUNFSK");//税款(运费)
				 stryunzshj = rs.getString("YUNZFHJ");//运杂费合计
				 strbeiz =strbeiz + nvlStr(rs.getString("beiz"));//备注
				 dblYunf=rs.getDouble("YUNZFHJ");
				 strshijfk =rs.getString("YUNFHSDJ");
				 double tmpshijfk=Double.parseDouble(strshijfk);
				 double tmpyunfsl=Double.parseDouble(strshuil_ys);
//				 strdanj = String.valueOf((double)Math.round((tmpshijfk-(tmpshijfk*tmpyunfsl))*100)/100);//单价
				 
				 if(intLeix==2){
					 
					 lgdiancxxb_id=rs.getLong("diancxxb_id");
					 lggongsxxb_id=rs.getLong("gongsxxb_id");
					 strbianh=rs.getString("bianh");
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
//					 strfahrq = rs.getString("fahrq");//发货日期
					 strfaz=rs.getString("faz");
					 strdiabch=rs.getString("daibch");
					 stryuanshr = rs.getString("yuanshr");//原收货人
					 strshoukdw = rs.getString("shoukdw");//收款单位
					 strkaihyh = rs.getString("kaihyh");//开户银行
					 strkaisysrq=rs.getString("yansrq");
					 strjiezysrq=rs.getString("yansjzrq");
					 if(strkaisysrq.equals(strjiezysrq)){
						 stryansrq=FormatDate(rs.getDate("yansrq"));
					 }else{
						 stryansrq=FormatDate(rs.getDate("yansrq"))+" 至 "+FormatDate(rs.getDate("yansjzrq"));
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
					 strdiqdm=rs.getString("diqbm");
//					 strshijfk =rs.getString("SHULZJBZ");//实际付款？？？？？？？？？？？？？？？？？
					 strgongfbz_rl = "";//供方热量
					 strkuangfys_rl = "";//验收热量
					 strxiancsl_rl= "";//相差数量热量
					 strzhejbz_rl = "";//折价标准热量
					 strzhehje_rl = "";//折合金额热量

					 strgongfbz_lf = "";//供方标准硫分
					 strkuangfys_lf = "";//验收硫分
					 strxiancsl_lf = "";//相差数量硫分
					 strzhejbz_lf = "";//折价标准硫分
					 strzhehje_lf = "";//折合金额硫分

					 strgongfbz_hff = "";//供方标准挥发分
					 strkuangfys_hff = "";//验收挥发分
					 strxiancsl_hff = "";//相差数量挥发分
					 strzhejbz_hff = "";//折价标准挥发分
					 strzhehje_hff = "";//折合金额挥发分

					 strgongfbz_hf = "";//供方标准发分
					 strkuangfys_hf = "";//验收发分
					 strxiancsl_hf = "";//相差数量发分
					 strzhejbz_hf = "";//折价标准发分
					 strzhehje_hf = "";//折合金额发分

					 strgongfbz_sf = "";//供方标准水分
					 strkuangfys_sf = "";//验收水分
					 strxiancsl_sf = "";//相差数量水分
					 strzhejbz_sf = "";//折价标准水分
					 strzhehje_sf = "";//折合金额水分

					 strgongfsl = "";//供方数量
					 strxianssl ="";//验收数量
					 strkuidsl ="";//亏吨数量
					 strzhehje = "";//折合金额

//					 strjiessl = rs.getString("jiessl");//结算数量
					 strjiessl = rs.getString("gongfsl");//结算数量
//					 strdanj = (double)Math.round(a);//单价
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
					 
//					 strzhibr=rs.getString("zhibr");
					 strzhibrq=FormatDate(rs.getDate("zhibrq"));
//					 strlingdqz=rs.getString("lingdqz");
					 strlingdqzrq=FormatDate(rs.getDate("lingdqzrq"));
//					 strzonghcwjbr=rs.getString("zonghcwjbr");
					 strzonghcwjbrq=FormatDate(rs.getDate("zonghcwjbrq"));
					 
				 }
				 
			 }
			 
			 
			 String dcsql="select bianh from diancjsmkb where id="+diancjsmkb_id; 
			 ResultSet dcrs = cn.getResultSet(dcsql);
			 if(dcrs.next()){
				 strdiancjsbh=dcrs.getString("bianh");
				 strdiancjsbh="("+strdiancjsbh+")";
			 }
			 dcrs.close();
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
			 ArrHeader[3]=new String[] {"发运数量:",strfahsl,"车数:"+strches,"验收编号:",stryansbh,"发票编号:",strfapbh,"兑付地点:",strduifdd,"付款方式：",strfukfs};
			 ArrHeader[4]=new String[] {"质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","数量验收","","",""};
			 ArrHeader[5]=new String[] {"含税价:"+strshijfk,"","供方标准","厂方验收","相差数量","折价标准","折合金额","供方数量","验收数量","盈亏吨数量","折合金额"};
			 ArrHeader[6]=new String[] {"热量(KCAL/KG)","",strgongfbz_rl,strkuangfys_rl,strxiancsl_rl,strzhejbz_rl,strzhehje_rl,"(吨)","(吨)","(吨)","(元)"};
			 ArrHeader[7]=new String[] {"硫分(%)","",strgongfbz_lf,strkuangfys_lf,strxiancsl_lf,strzhejbz_lf,strzhehje_lf,strgongfsl,strxianssl,strkuidsl,strzhehje};
			 ArrHeader[8]=new String[] {"挥发分(%)","","","","","","","","","",""};
			 ArrHeader[9]=new String[] {"灰分","",strgongfbz_hf,strkuangfys_hf,strxiancsl_hf,strzhejbz_hf,strzhehje_hf,"","","",""};
			 ArrHeader[10]=new String[] {"水分(%)","","","","","","","","","",""};
			 ArrHeader[11]=new String[] {"结算数量","单价","金额","","补(扣)以前价款","补(扣)以前价款","价款合计","税率","税款","价税合计","价税合计"};
			 ArrHeader[12]=new String[] {strjiessl,strdanj,strjine,"",strbukouqjk," ",formatq(strjiakhj),strshuil_mk,formatq(strshuik_mk),formatq(strjialhj),"660165.61"};
			 ArrHeader[13]=new String[] {"煤款合计(大写):",strmeikhjdx,"","","","","","","","",""};
			 ArrHeader[14]=new String[] {"运费","杂费","补(扣)以前运杂费","","计税扣除","计税扣除","不含税运费","税率","税款","运杂费合计","运杂费合计"};
			 ArrHeader[15]=new String[] {strtielyf,strzaf,strbukouqzf,"",strjiskc,"",formatq(strbuhsyf),strshuil_ys,formatq(strshuik_ys),formatq(stryunzshj),"151546.4"};
			 ArrHeader[16]=new String[] {"运杂费合计(大写):",stryunzfhjdx,"","","","","","","","",""};
			 ArrHeader[17]=new String[] {"合计(大写):",strhej_dx,"","","","","","","合计(小写):",strhej_xx,""};
			 ArrHeader[18]=new String[] {"备注:",strbeiz,"","","","","","","过衡重量:",strguohzl,""};
			 ArrHeader[19]=new String[] {"制表部门:(签章)","","质量综合处领导审核:(签章)","","","领导审核:(签章)","","","综合财务部:(签章)","",""};
			 ArrHeader[20]=new String[] {"经办人:"+strzhibr,"",""+strzhiljdcldqz+"","","",""+strlingdqz,"","","经办人:"+strzonghcwjbr,"",""};
			 ArrHeader[21]=new String[] {strzhibrq,"",strzhiljdcldqzrq,"","",strlingdqzrq,"","",strzonghcwjbrq,"",""};
			 
//			 定义页Title
			 
			 rt.setTitle("燃料采购入库单",ArrWidth);
//			 rt2.setTitle("燃料采购出库单",ArrWidth);
			 String tianbdw=getProperValue(getITianzdwModel(),lggongsxxb_id);//填制单位。（可根据条件来填入单位）
//			 String tianbdw=getTianzdw(lggongsxxb_id);//填制单位。（可根据条件来填入单位）
			 rt.setDefaultTitleLeft("填制单位："+tianbdw,2);
			 rt.setDefaultTitle(4,4,"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;结算日期："+strjiesrq,Table.ALIGN_CENTER);
			 rt.setDefaultTitle(9,3,"编号:"+strbianh+"  "+strdiancjsbh,Table.ALIGN_CENTER);
			 Table table = new Table(rs, 1, 0, 0);
			 
			 rt.setBody(table);//new Table(ArrHeader,0,true,Table.ALIGN_CENTER));
			 rt.body.setRowHeight(26);
			 rt.body.setWidth(ArrWidth);			 
			 rt.body.mergeCell(1,1,1,3);
			 rt.body.mergeCell(1,9,1,11);
			 rt.body.mergeCell(2,1,2,3);
			 rt.body.mergeCell(2,9,2,11);
			 rt.body.mergeCell(3,1,3,3);
			 rt.body.mergeCell(3,9,3,11);
			 rt.body.mergeCell(5,1,5,7);
			 rt.body.mergeCell(5,8,5,11);
			 rt.body.mergeCell(6,1,6,2);
			 rt.body.mergeCell(7,1,7,2);
			 rt.body.mergeCell(8,1,8,2);
			 rt.body.mergeCell(9,1,9,2);
			 rt.body.mergeCell(10,1,10,2);
			 rt.body.mergeCell(11,1,11,2);
			 rt.body.mergeCell(12,3,12,4);
			 rt.body.mergeCell(12,5,12,6);
			 rt.body.mergeCell(12,10,12,11);
			 rt.body.mergeCell(13,3,13,4);
			 rt.body.mergeCell(13,5,13,6);
			 rt.body.mergeCell(13,10,13,11);
			 rt.body.mergeCell(14,2,14,11);
			 rt.body.mergeCell(15,3,15,4);
			 rt.body.mergeCell(15,5,15,6);
			 rt.body.mergeCell(15,10,15,11);
			 rt.body.mergeCell(16,3,16,4);
			 rt.body.mergeCell(16,5,16,6);
			 rt.body.mergeCell(16,10,16,11);
			 rt.body.mergeCell(17,2,17,11);
			 rt.body.mergeCell(18,2,18,8);
			 rt.body.mergeCell(18,10,18,11);
			 rt.body.mergeCell(19,2,19,8);
			 rt.body.mergeCell(19,10,19,11);
			 
//			 rt.body.mergeCell(20,1,20,3);
//			 rt.body.mergeCell(20,4,20,7);
//			 rt.body.mergeCell(20,8,20,11);
//			 rt.body.mergeCell(21,1,21,3);
//			 rt.body.mergeCell(21,4,21,7);
//			 rt.body.mergeCell(21,8,21,11);
//			 rt.body.mergeCell(22,1,22,3);
//			 rt.body.mergeCell(22,4,22,7);
//			 rt.body.mergeCell(22,8,22,11);
			 
			 rt.body.mergeCell(20,1,20,2);
			 rt.body.mergeCell(20,3,20,5);
			 rt.body.mergeCell(20,6,20,8);
			 rt.body.mergeCell(20,9,20,11);
			 
			 rt.body.mergeCell(21,1,21,2);
			 rt.body.mergeCell(21,3,21,5);
			 rt.body.mergeCell(21,6,21,8);
			 rt.body.mergeCell(21,9,21,11);
			 
			 rt.body.mergeCell(22,1,22,2);
			 rt.body.mergeCell(22,3,22,5);
			 rt.body.mergeCell(22,6,22,8);
			 rt.body.mergeCell(22,9,22,11);
			 
			 
//			 rt.body.setCellBorderRight(5,1,0);
			 rt.body.setCells(4,1,4,1,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(1,4,4,4,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(1,6,4,6,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(1,8,4,8,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(4,2,4,2,Table.PER_BORDER_RIGHT,0);
			 
			 rt.body.setCells(6,1,11,1,Table.PER_ALIGN,Table.ALIGN_LEFT);
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
					 rt.body.setCellImage(21, 4, 74, 35, "http://10.66.3.193:8086/ftp/lingdqz/baijlqz.gif");
				 }else if(strlingdqz.equals("张志刚")){
					 rt.body.setCellImage(21, 4, 74, 35, "http://10.66.3.193:8086/ftp/lingdqz/zzg.gif");
				 }
			 }
				// 设置页数
				_CurrentPage = iPageIndex;
//				_AllPages = rt.body.getPages();
				
				if (_AllPages == 0) {
					_CurrentPage = 0;
				}
				return rt.getAllPagesHtml(iPageIndex);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cn.Close();
			}
			return rt.getAllPagesHtml(iPageIndex);
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

//	public String getTianzdw(long diancxxb_id) {
//		String Tianzdw="";
//		JDBCcon con=new JDBCcon();
//		try{
//			String sql="select quanc from gongsxxb where id="+diancxxb_id;
//			ResultSet rs=con.getResultSet(sql);
//			if(rs.next()){
//				
//				Tianzdw=rs.getString("quanc");
//			}
//			rs.close();
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			con.Close();
//		}
//		return Tianzdw;
//	}
	
	private String FormatDate(Date _date) {
		if (_date == null) {
//			return MainGlobal.Formatdate("yyyy年 MM月 dd日", new Date());
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	
//	格式化
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
	
	private String nvlStr(String strValue){
		if (strValue==null) {
			return "";
		}else if(strValue.equals("null")){
			return "";
		}
		
		return strValue;
	}

//	 填制单位
	private IDropDownBean _TianzdwValue;

	private boolean _Tianzdwchange = false;

	public IDropDownBean getTianzdwValue() {
		if(_TianzdwValue==null){
			_TianzdwValue=(IDropDownBean)getITianzdwModel().getOption(0);
		}
		return _TianzdwValue;
	}

	public void setTianzdwValue(IDropDownBean Value) {
		if (_TianzdwValue != Value) {

			_Tianzdwchange = true;
		}
		_TianzdwValue = Value;
	}

	private static IPropertySelectionModel _ITianzdwModel;

	public void setITianzdwModel(IPropertySelectionModel value) {
		_ITianzdwModel = value;
	}

	public IPropertySelectionModel getITianzdwModel() {
		if (_ITianzdwModel == null) {
			getITianzdwModels();
		}
		return _ITianzdwModel;
	}

	public IPropertySelectionModel getITianzdwModels() {
		String sql = "select id,quanc from "
					+" (select id,quanc,rownum+2 as xuh from diancxxb "
					+" union select id,quanc,rownum as xuh from gongsxxb) "
					+" order by xuh";
		_ITianzdwModel = new IDropDownModel(sql);
		return _ITianzdwModel;
	}

	private String getProperValue(IPropertySelectionModel _selectModel,
			long value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
				return ((IDropDownBean) _selectModel.getOption(i)).getValue();
			}
		}
		return null;
	}
	
}
