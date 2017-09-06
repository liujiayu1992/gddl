package com.zhiren.jt.jiesgl.report.kuangfhs;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Date;

import com.zhiren.common.DateUtil;
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
			 
			 String strhetbz_rl="";
			 String strgongfbz_rl = "";
			 String strkuangfys_rl = "";
			 String strjiessl_rl="";
			 String strxiancsl_rl = "";
			 String strzhejbz_rl = "";
			 String strzhehje_rl = "";
			 
			 String strhetbz_lf="";
			 String strgongfbz_lf = "";
			 String strkuangfys_lf = "";
			 String strjiessl_lf="";
			 String strxiancsl_lf = "";
			 String strzhejbz_lf = "";
			 String strzhehje_lf = "";
				 
			 String strhetbz_hff=""; 	 
			 String strgongfbz_hff="";
			 String strkuangfys_hff="";
			 String strjiessl_hff="";
			 String strxiancsl_hff="";
			 String strzhejbz_hff="";
			 String strzhehje_hff=""; 
			 
			 String strhetbz_hf="";
			 String strgongfbz_hf="";
			 String strkuangfys_hf="";
			 String strjiessl_hf="";
			 String strxiancsl_hf="";
			 String strzhejbz_hf="";
			 String strzhehje_hf="";
				 
			 String strhetbz_sf="";
			 String strgongfbz_sf="";
			 String strkuangfys_sf="";
			 String strjiessl_sf="";
			 String strxiancsl_sf="";
			 String strzhejbz_sf="";
			 String strzhehje_sf="";
			 
				 ;
			 String strhetsl = "";
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
			 // 
			 double dblMeik =0;
			 double dblYunf =0;
			 sql="select * from kuangfjsmkb where bianm='"+where+"'"; 
			 ResultSet rs = cn.getResultSet(sql);
			 
			 int intLeix=2;
			 long intDiancjsmkId=0;
			 
			 boolean blnHasMeik =false;
			 
			 if(rs.next()){
				 lgdiancxxb_id=rs.getLong("diancxxb_id");
				 diancjsmkb_id=rs.getLong("diancjsmkb_id");
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
				 strduifdd = rs.getString("duifdd");//兑付地点
				 strfukfs =rs.getString("fukfs") ;//付款方式
				 
				 
				 sql = "select jieszbsjb.*,zhibb.mingc from jieszbsjb,kuangfjsmkb,zhibb "
						+ " where jieszbsjb.jiesdid=kuangfjsmkb.id and zhibb.id=jieszbsjb.zhibb_id"
						+ " and kuangfjsmkb.bianm='"
						+ where
						+ "' and jieszbsjb.zhuangt=1 ";
				 ResultSet rec = cn.getResultSet(sql);
				 while(rec.next()){
					 
					 if(rec.getString("mingc").equals("数量")){
						 
						 //*****************数量验收*****************//
						 
						 strhetsl=rec.getString("hetbz");
						 strgongfsl = rec.getString("gongf");//供方数量
						 strfahsl=strgongfsl;
						 strxianssl =rec.getString("changf");//验收数量
						 strkuidsl =String.valueOf(-rec.getDouble("yingk"));//亏吨数量
						 strshijfk =rec.getString("zhejbz");//数量折价标准
						 strzhehje = rec.getString("zhejje");//折合金额
					 
					 }else if(rec.getString("mingc").equals("收到基低位热值")){
						 
//						*****************热量验收*****************//
						 strhetbz_rl=rec.getString("hetbz");
						 strgongfbz_rl = rec.getString("GONGF");//供方热量
						 strkuangfys_rl = rec.getString("changf");//验收热量
						 strjiessl_rl=rec.getString("jies");
						 strxiancsl_rl=rec.getString("yingk");//相差数量热量
						 strzhejbz_rl =rec.getString("zhejbz");//折价标准热量
						 strzhehje_rl = rec.getString("zhejje");//折合金额热量
					 
					 }else if(rec.getString("mingc").equals("干燥基全硫")){
						 
						 strhetbz_lf=rec.getString("hetbz");
						 strgongfbz_lf = rec.getString("GONGF");//供方标准硫分
						 strkuangfys_lf = rec.getString("changf");//验收硫分
						 strjiessl_lf=rec.getString("jies");
						 strxiancsl_lf = rec.getString("yingk");//相差数量硫分
						 strzhejbz_lf = rec.getString("zhejbz");//折价标准硫分
						 strzhehje_lf = rec.getString("zhejje");//折合金额硫分
					 
					 }else if(rec.getString("mingc").equals("干燥无灰基挥发分")){
						 
						 strhetbz_hff=rec.getString("hetbz");//合同标准
						 strgongfbz_hff = rec.getString("GONGF");//供方标准挥发分
						 strkuangfys_hff = rec.getString("changf");//验收挥发分
						 strjiessl_hff=rec.getString("jies");
						 strxiancsl_hff = rec.getString("yingk");//相差数量挥发分
						 strzhejbz_hff = rec.getString("zhejbz");//折价标准挥发分
						 strzhehje_hff = rec.getString("zhejje");//折合金额挥发分
						 
					 }else if(rec.getString("mingc").equals("干燥基灰分")){
						 
						 strhetbz_hf=rec.getString("hetbz");
						 strgongfbz_hf = rec.getString("GONGF");//供方标准发分
						 strkuangfys_hf =rec.getString("changf");//验收发分
						 strjiessl_hf=rec.getString("jies");
						 strxiancsl_hf = rec.getString("yingk");//相差数量发分
						 strzhejbz_hf =rec.getString("zhejbz");//折价标准发分
						 strzhehje_hf =rec.getString("zhejje");//折合金额发分
						 
					 }else if(rec.getString("mingc").equals("全水分")){
						 
						 strhetbz_sf=rec.getString("hetbz");
						 strgongfbz_sf = rec.getString("GONGF");//供方标准水分
						 strkuangfys_sf = rec.getString("changf");//验收水分
						 strjiessl_sf=rec.getString("jies");
						 strxiancsl_sf = rec.getString("YINGK");//相差数量水分
						 strzhejbz_sf = rec.getString("zhejbz");//折价标准水分
						 strzhehje_sf = rec.getString("zhejje");//折合金额水分
					 }
				 }
				 
				 rec.close();

				 //********************其他*****************//
				 strjiessl = rs.getString("jiessl");//结算数量
				 strdanj = rs.getString("hansdj");//单价
				 strjine = rs.getString("meikje");//金额
				 strbukouqjk = rs.getString("bukmk");//补(扣)以前价款
				 strjiakhj = rs.getString("buhsmk");//价款合计
				 strshuil_mk = rs.getString("shuil");//税率(煤矿)
				 strshuik_mk = rs.getString("shuik");//税款(煤矿)
				 strjialhj = rs.getString("hansmk");//价税合计
				 strguohzl =rs.getString("GUOHL");//过衡重量
				 strbeiz = nvlStr(rs.getString("beiz"));//备注
				 dblMeik= Double.parseDouble(strjialhj);
				 blnHasMeik=true;
			 }
				 
			 	if ((blnHasMeik)&&(intLeix==1)){
					 
					 sql="select * from kuangfjsyfb where bianm='"+where+"'";
					 
					 rs=cn.getResultSet(sql);
					 if (rs.next()){
						 
						 strtielyf =rs.getString("GUOTYF");//铁路运费
						 strzaf = rs.getString("dityf");//杂费
						 strbukouqzf = rs.getString("bukyf");//补(扣)以前运杂费
						 strjiskc = rs.getString("JISKC");//计税扣除
						 strbuhsyf =rs.getString("buhsyf");//不含税运费
						 strshuil_ys = rs.getString("shuil");//税率(运费)
						 strshuik_ys = rs.getString("shuik");//税款(运费)
						 stryunzshj = rs.getString("hansyf");//运杂费合计
						 dblYunf=rs.getDouble("hansyf");
					 }
				 }else if(intLeix!=0){
					 
					 sql="select * from kuangfjsyfb where bianm='"+where+"'";
					 
					 rs=cn.getResultSet(sql);
					 if(rs.next()){
						 
						 lgdiancxxb_id=rs.getLong("diancxxb_id");
						 strbianh=rs.getString("bianm");
						 strjiesrq=FormatDate(rs.getDate("jiesrq"));
						 intLeix=rs.getInt("jieslx");//结算类型0 为两票一个供应商,1为两票两个供应商，2为运费或煤款
						 strfahdw=rs.getString("gongysmc");
						 strfahksrq=rs.getString("fahksrq");
						 strfahjzrq=rs.getString("fahjzrq");
						 if(strfahksrq.equals(strfahjzrq)){
							 strfahrq = FormatDate(rs.getDate("fahksrq"));//发货日期
						 }else{
							 strfahrq=FormatDate(rs.getDate("fahksrq"))+" 至 "+FormatDate(rs.getDate("fahjzrq"));
						 }
	//					 strfahrq = rs.getString("fahrq");//发货日期
						 strfaz=rs.getString("faz");
						 strdiabch=rs.getString("daibch");
						 stryuanshr = rs.getString("yuanshr");//原收货人
						 strshoukdw = rs.getString("shoukdw");//收款单位
						 strkaihyh = rs.getString("kaihyh");//开户银行
						 strkaisysrq=rs.getString("yansksrq");
						 strjiezysrq=rs.getString("yansjzrq");
						 if(strkaisysrq.equals(strjiezysrq)){
							 stryansrq=FormatDate(rs.getDate("yansksrq"));
						 }else{
							 stryansrq=FormatDate(rs.getDate("yansksrq"))+" 至 "+FormatDate(rs.getDate("yansjzrq"));
						 }
						 strhuowmc = rs.getString("MEIZ");//货物名称
						 strxianshr = rs.getString("xianshr");//现收货人
						 stryinhzh = rs.getString("zhangh");//帐号
						 strfahsl =getGongfsl(rs.getLong("diancjsb_id"));//发运数量？？？？？？？？？？？？？？？？？？
						 strches = rs.getString("ches");//车数
						 stryansbh = rs.getString("yansbh");//验收编号
						 strfapbh = rs.getString("fapbh");//发票编号
						 strduifdd = rs.getString("duifdd");//兑付地点
						 strfukfs = rs.getString("fukfs") ;//付款方式
						 strshijfk =" ";//实际付款？？？？？？？？？？？？？？？？？
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
	
						 strjiessl = rs.getString("gongfsl");
						 strjine = "0";//金额
						 strbukouqjk = "0";//补(扣)以前价款
						 strjiakhj = "0";//价款合计
						 strshuil_mk = "0";//税率(煤矿)
						 strshuik_mk = "0";//税款(煤矿)
						 strjialhj = "0";//价税合计
						 strguohzl =rs.getString("GUOHL");//过衡重量
						 strbeiz = nvlStr(rs.getString("beiz"));//备注
						 dblMeik= Double.parseDouble(strjialhj);
				 }
			 }
			 rs.close();
			 
			 String dcsql="select bianm from diancjsmkb where id="+diancjsmkb_id; 
			 ResultSet dcrs = cn.getResultSet(dcsql);
			 if(dcrs.next()){
				 strdiancjsbh=dcrs.getString("bianm");
				 strdiancjsbh="("+strdiancjsbh+")";
			 }
			 dcrs.close();
			 Money money=new Money();
			 //计算合计
			 strhej_xx=format(dblYunf+dblMeik,"0.00");
			 strmeikhjdx=money.NumToRMBStr(dblMeik);
			 stryunzfhjdx=money.NumToRMBStr(dblYunf);
			 strhej_dx=money.NumToRMBStr(dblYunf+dblMeik);
			 cn.Close();
//			 A4	110*790
//			 now 901
//			 控制挥发分、灰分、水分隐藏显示
			 boolean hff_bn=false;
			 boolean hf_bn=false;
			 boolean sf_bn=false;
			 
			 int hff_row=9;
			 int hf_row=10;
			 int sf_row=11;
			 
			 if(strjiessl_hff.equals("")||strjiessl_hff.equals("0")){
				 hff_bn=true;
			 }
			 if(strjiessl_hf.equals("")||strjiessl_hf.equals("0")){
				 hf_bn=true;
			 }
			 if(strjiessl_sf.equals("")||strjiessl_sf.equals("0")){
				 sf_bn=true;
			 }
//			 
			 int ArrWidth[]=new int[] {110,85,65,65,65,65,75,70,76,85,70,70};
			 String ArrHeader[][]=new String[22][12];
			 ArrHeader[0]=new String[] {"供货单位:"+strfahdw,"","","发站:",strfaz,"代表车号:",strdiabch,"","收款单位:",strshoukdw,"",""};
			 ArrHeader[1]=new String[] {"发货日期:"+strfahrq,"","","地区代码:",strdiqdm,"原收货人:",stryuanshr,"","开户银行:",strkaihyh,"",""};
			 ArrHeader[2]=new String[] {"验收日期:"+stryansrq+"-"+stryansrq,"","","货物名称:",strhuowmc,"现收货人:",strxianshr,"","银行帐号:",stryinhzh,"",""};
			 ArrHeader[3]=new String[] {"发运数量:",strfahsl,"车数:"+strches,"验收编号:",stryansbh,"发票编号:",strfapbh,"","兑付地点:",strduifdd,"付款方式:",strfukfs};
			 ArrHeader[4]=new String[] {"质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","数量验收","","",""};
			 ArrHeader[5]=new String[] {"含税价:"+strshijfk+"(元)","合同标准","供方标准","厂方验收","结算标准","相差数量","折价标准","折合金额","供方数量","验收数量","亏吨数量","折合金额"};
			 ArrHeader[6]=new String[] {"热量(KCAL/KG)",strhetbz_rl,strgongfbz_rl,strkuangfys_rl,strjiessl_rl,strxiancsl_rl,strzhejbz_rl,strzhehje_rl,"(吨)","(吨)","(吨)","(元)"};
			 ArrHeader[7]=new String[] {"硫分(%)",strhetbz_lf,strgongfbz_lf,strkuangfys_lf,strjiessl_lf,strxiancsl_lf,strzhejbz_lf,strzhehje_lf,strgongfsl,strxianssl,strkuidsl,strzhehje};
			 ArrHeader[8]=new String[] {"挥发分(%)",strhetbz_hff,strgongfbz_hff,strkuangfys_hff,strjiessl_hff,strxiancsl_hff,strzhejbz_hff,strzhehje_hff,"","","",""};
			 ArrHeader[9]=new String[] {"灰分(%)",strhetbz_hf,strgongfbz_hf,strkuangfys_hf,strjiessl_hf,strxiancsl_hf,strzhejbz_hf,strzhehje_hf,"","","",""};
			 ArrHeader[10]=new String[] {"水分(%)",strhetbz_sf,strgongfbz_sf,strkuangfys_sf,strjiessl_sf,strxiancsl_sf,strzhejbz_sf,strzhehje_sf,"","","",""};
			 ArrHeader[11]=new String[] {"结算数量","单价","金额","","补(扣)以前价款","补(扣)以前价款","价款合计","","税率","税款","价税合计","价税合计"};
			 ArrHeader[12]=new String[] {strjiessl,strdanj,strjine,"",strbukouqjk," ",formatq(strjiakhj),"",strshuil_mk,formatq(strshuik_mk),formatq(strjialhj),"660165.61"};
			 ArrHeader[13]=new String[] {"煤款合计(大写):",strmeikhjdx,"","","","","","","","","",""};
			 ArrHeader[14]=new String[] {"铁路运费","杂费","补(扣)以前运杂费","","计税扣除","计税扣除","不含税运费","","税率","税款","运杂费合计","运杂费合计"};
			 ArrHeader[15]=new String[] {strtielyf,strzaf,strbukouqzf,"",strjiskc,"",formatq(strbuhsyf),"",strshuil_ys,formatq(strshuik_ys),formatq(stryunzshj),"151546.4"};
			 ArrHeader[16]=new String[] {"运杂费合计(大写):",stryunzfhjdx,"","","","","","","","","",""};
			 ArrHeader[17]=new String[] {"合计(大写):",strhej_dx,"","","","","","","合计(小写):",strhej_xx,"",""};
			 ArrHeader[18]=new String[] {"备注:",strbeiz,"","","","","","","过衡重量:",strguohzl,"",""};
			 ArrHeader[19]=new String[] {"制表部门:(签章)","","","领导审核:(签章)","","","","综合财务部:(签章)","","","",""};
			 ArrHeader[20]=new String[] {"经办人:"+strzhibr,"","",""+strlingdqz,"","","","经办人:"+strzonghcwjbr,"","","",""};
			 ArrHeader[21]=new String[] {strzhibrq,"","",strlingdqzrq,"","","",strzonghcwjbrq,"","","",""};
			 
//			 定义页Title
			 
			 rt.setTitle("燃料采购入库单",ArrWidth);
			 String tianbdw=getTianzdw(lgdiancxxb_id);//填制单位。（可根据条件来填入单位）
			 rt.setDefaultTitleLeft("填制单位："+tianbdw,3);
			 rt.setDefaultTitle(4,5,"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;结算日期："+strjiesrq,Table.ALIGN_CENTER);
			 rt.setDefaultTitle(9,3,"编号:"+strbianh+"  "+strdiancjsbh,Table.ALIGN_CENTER);
			 rt.setBody(new Table(ArrHeader,0,0,0));
			 rt.body.setRowHeight(24);
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
			 rt.body.setCells(4,4,4,4,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(4,6,4,6,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(4,9,4,9,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(4,11,4,11,Table.PER_BORDER_RIGHT,0);
			 
			 rt.body.mergeCell(5,1,5,8);
			 rt.body.mergeCell(5,9,5,12);
			 
			 rt.body.mergeCell(12,3,12,4);
			 rt.body.mergeCell(12,5,12,6);
			 rt.body.mergeCell(12,7,12,8);
			 rt.body.mergeCell(12,11,12,12);
			 
			 rt.body.mergeCell(13,3,13,4);
			 rt.body.mergeCell(13,5,13,6);
			 rt.body.mergeCell(13,7,13,8);
			 rt.body.mergeCell(13,11,13,12);
			 
			 rt.body.mergeCell(14,2,14,12);//煤款合计大写
			 rt.body.setCells(14,12,14,12,Table.PER_BORDER_RIGHT,1);
			 
			 rt.body.mergeCell(15,3,15,4);
			 rt.body.mergeCell(15,5,15,6);
			 rt.body.mergeCell(15,7,15,8);
			 rt.body.mergeCell(15,11,15,12);
			 
			 rt.body.mergeCell(16,3,16,4);
			 rt.body.mergeCell(16,5,16,6);
			 rt.body.mergeCell(16,7,16,8);
			 rt.body.mergeCell(16,11,16,12);
			 
			 rt.body.mergeCell(17,2,17,12);//运费合计大写
			 
			 rt.body.mergeCell(18,2,18,8);
			 rt.body.mergeCell(18,10,18,12);//合计大写
			 
			 rt.body.mergeCell(19,2,19,8);
			 rt.body.mergeCell(19,10,19,12);//备注
			 
			 rt.body.mergeCell(20,1,20,3);
			 rt.body.mergeCell(20,4,20,7);
			 rt.body.mergeCell(20,8,20,12);
			 
			 rt.body.mergeCell(21,1,21,3);
			 rt.body.mergeCell(21,4,21,7);
			 rt.body.mergeCell(21,8,21,12);
			 rt.body.mergeCell(22,1,22,3);
			 rt.body.mergeCell(22,4,22,7);
			 rt.body.mergeCell(22,8,22,12);
			 
			 rt.body.setCells(4,1,4,1,Table.PER_BORDER_RIGHT,0);
			 rt.body.setCells(4,2,4,2,Table.PER_BORDER_RIGHT,0);
			 
			 
			 rt.body.setCells(5, 1, 12, 12, Table.PER_ALIGN, Table.ALIGN_CENTER);
			 rt.body.setCells(6,1,11,1,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setCells(13,1,13,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
			 rt.body.setCells(15,1,15,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
			 rt.body.setCells(16,1,16,12,Table.PER_ALIGN,Table.ALIGN_CENTER);
			 rt.body.setRowCells(20,Table.PER_BORDER_BOTTOM,0);
			 rt.body.setRowCells(21,Table.PER_BORDER_BOTTOM,0);
			 
//			 设置隐藏行
			 if(hff_bn){
				 rt.body.setRowCells(hff_row, Table.PER_USED, false);
				 rt.body.setRowHeight(hff_row,0);
			 }
			 if(hf_bn){
				 
				 rt.body.setRowCells(hf_row, Table.PER_USED, false);
				 rt.body.setRowHeight(hf_row,0);
			 }
			 if(sf_bn){
				 
				 rt.body.setRowCells(sf_row, Table.PER_USED, false);
				 rt.body.setRowHeight(sf_row,0);
			 }
			 
			 if(strlingdqz!=null){
				 if(strlingdqz.equals("白福贵")){
					 rt.body.setCellImage(21, 4, 74, 35, "http://10.66.2.222:8086/ftp/lingdqz/baijlqz.gif");
				 }else if(strlingdqz.equals("张志刚")){
					 rt.body.setCellImage(21, 4, 74, 35, "http://10.66.2.222:8086/ftp/lingdqz/zzg.gif");
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

	private String getGongfsl(long jiesbid) {
		// TODO 自动生成方法存根
		JDBCcon con=new JDBCcon();
		String gongfsl="";
		try{
			
			String sql=" select gongf from jieszbsjb,diancjsmkb,zhibb "
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

	
}
