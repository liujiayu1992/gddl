package com.zhiren.dtrlgs.shoumgl.shoumjs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import bsh.EvalError;
import bsh.Interpreter;

import com.zhiren.common.DataBassUtil;
import com.zhiren.common.DateUtil;
import com.zhiren.common.FileNameFilter;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.jt.jiesgl.changfhs.Jiesdyzbean;
import com.zhiren.main.Visit;
import com.zhiren.shihs.hesgl.Balances_variable_Shih;
import com.zhiren.common.CustomMaths;

public class Shoumjsdcz{
	
//	结算模块的所有公用方法
	
	public String SetJieszb(String Zhibmc, String Zhib_ht, String Zhib_kf, String Zhib_cf, 
			String Zhib_js, String Zhib_yk, String Zhib_zdj, String Zhib_zje, 
			String Zhib_ht_value, double Zhib_kf_value, double Zhib_cf_value, 
			double Zhib_js_value, double Zhib_yk_value, double Zhib_zdj_value, 
			double Zhib_zje_value){
		
		//初始化结算指标
		StringBuffer Stbf=new StringBuffer();
		
		Stbf.append("<tr>");
		Stbf.append("	<td class='Jsdtdborder'>"+Zhibmc+"</td>");
		Stbf.append("	<td class='Jsdtdborder'><input  readonly ='true'	class='noeditinput'  style='width:100%' name='"+Zhib_ht+"'   	id='"+Zhib_ht+"'	value="+Zhib_ht_value+"   	type='text' /></td>");
		Stbf.append("	<td class='Jsdtdborder'><input  readonly ='true'	class='noeditinput'  style='width:100%' name='"+Zhib_kf+"' 		id='"+Zhib_kf+"' 	value="+Zhib_kf_value+"		type='text' /></td>");
		Stbf.append("	<td class='Jsdtdborder'><input  readonly ='true'	class='noeditinput'  style='width:100%' name='"+Zhib_cf+"'  	id='"+Zhib_cf+"'  	value="+Zhib_cf_value+"		type='text' /></td>");
		Stbf.append("	<td class='Jsdtdborder'><input  readonly ='true'	class='noeditinput'  style='width:100%' name='"+Zhib_js+"'  	id='"+Zhib_js+"'  	value="+Zhib_js_value+"		type='text' /></td>");
		Stbf.append("	<td class='Jsdtdborder'><input  readonly ='true'	class='noeditinput'  style='width:100%' name='"+Zhib_yk+"' 		id='"+Zhib_yk+"' 	value="+Zhib_yk_value+"		type='text' /></td>");
		Stbf.append("	<td class='Jsdtdborder'><input  readonly ='true'	class='noeditinput'  style='width:100%' name='"+Zhib_zdj+"' 	id='"+Zhib_zdj+"' 	value="+Zhib_zdj_value+"	type='text' /></td>");
		Stbf.append("	<td colspan='2' class='Jsdtdborder'><input  readonly ='true' class='noeditinput' style='width:100%' name='"+Zhib_zje+"' id='"+Zhib_zje+"' value="+Zhib_zje_value+"  type='text' /></td>");
		Stbf.append("</tr>");
		
		return	Stbf.toString();    
	}
	
	public String SetHejdxh(String Shifxsckd,double chaokdl, double hej, String hejdx){
		
//		是否显示“超/亏吨量”
		StringBuffer Stbf=new StringBuffer();
		
		if(Shifxsckd.equals("")){
//			超\亏吨标识符为空，不显示
			Stbf.append("<tr>");
			Stbf.append("	<td class='Jsdtdborder'>合计大写</td>");
			Stbf.append("	<td colspan='5'  class='Jsdtdborder'><input  readonly ='true' class='noeditinput' style='width:100%' id='DAXHJ' value='"+hejdx+"' type='text' /></td>");
			Stbf.append("	<td class='Jsdtdborder'>合计小写</td>");
			Stbf.append("	<td colspan='2'  class='Jsdtdborder'><input  readonly ='true' class='noeditinput' style='width:100%' id='HEJ'	value='"+hej+"' type='text' /></td>");
			Stbf.append("</tr>");
		}else{
			
			Stbf.append("<tr>");
			Stbf.append("	<td class='Jsdtdborder'>合计大写</td>");
			Stbf.append("	<td colspan='4'  class='Jsdtdborder'><input  readonly ='true' class='noeditinput' style='width:100%' id='DAXHJ'	value='"+hejdx+"' type='text' /></td>");
			Stbf.append("	<td class='Jsdtdborder'>合计小写</td>");
			Stbf.append("	<td class='Jsdtdborder'><input  readonly ='true' class='noeditinput' style='width:100%' id='HEJ' value='"+hej+"' type='text' /></td>");
			Stbf.append("	<td class='Jsdtdborder'>超/亏吨量(吨)</td>");
			Stbf.append("	<td class='Jsdtdborder'><input  readonly ='true' class='noeditinput' style='width:100%' id='CHAOKDL' value='"+chaokdl+"' type='text' /></td>");
			Stbf.append("</tr>");
		}
		
		return	Stbf.toString();    
	}
	
	public long Feiylbb_transition(long feiylbb_id){
		
//		如果feiylbb是两票结算就在保存国铁运费的时候把它的类型转为3
		if(feiylbb_id==1){
//			两票结算
			feiylbb_id=3;
		}
		return feiylbb_id;
	}
	
	public Interpreter getYunfgsjx(long Diancxxb_id,long Faz_id,long Daoz_id,
			String Biaoz,String cheb,String SelectChepbId,String Lianpdp) {
		// TODO 自动生成方法存根
//		当用户设置了feiyxm后，从这里构造bsh类结算所需内容
		JDBCcon con=new JDBCcon();
		Interpreter bsh = new Interpreter();
		String strChes[]=SelectChepbId.split(",");
		try{
//			String tmp[][]=this.getChepb_info("distinct",2,"decode(c.chebb_id,1,'路车',2,'自备车',3,'汽',4,'船') as cheb^~c.biaoz^", SelectChepbId);
			//公式中用到的项目
//			设置了车别、标重、里程类型、里程值、车数
			
			bsh.set("车别",cheb);
			bsh.set("票重",Double.parseDouble(Biaoz));
			
			if(Lianpdp.equals("dp")){
				
				bsh.set("车数", 1);
			}else if(Lianpdp.equals("lp")){
				
				bsh.set("车数", strChes.length);
			}
			
			
			
//			1、求里程距离
			String sql="select lclx.mingc,lc.zhi from licb lc,liclxb lclx \n" 
				       + " where lc.liclxb_id=lclx.id and lc.diancxxb_id="+Diancxxb_id+" \n " 
				       + "      and lc.faz_id="+Faz_id+" and lc.daoz_id="+Daoz_id+" \n";
			
			ResultSet rs=con.getResultSet(sql);
			while(rs.next()){
				
				bsh.set(rs.getString("mingc"), rs.getInt("zhi"));
			}
			
			rs.close();
			return bsh;
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return null;
	}
	
	public Interpreter getYunfgsjx(long Diancxxb_id,long Faz_id,long Daoz_id,double Biaoz) {
		// TODO 自动生成方法存根
//		当用户设置了feiyxm后，从这里构造bsh类结算所需内容
		JDBCcon con=new JDBCcon();
		Interpreter bsh = new Interpreter();
		try{
//			该方法车别不参与计算，只能根据总票重和里程计算运费
//			String tmp[][]=this.getChepb_info("distinct",2,"decode(c.chebb_id,1,'路车',2,'自备车',3,'汽',4,'船') as cheb^~c.biaoz^", SelectChepbId);
			//公式中用到的项目
//			设置了标重、里程类型、里程值
			bsh.set("票重",Biaoz);
			
			
//			1、求里程距离
			String sql="select lclx.mingc,lc.zhi from licb lc,liclxb lclx \n" 
				       + " where lc.liclxb_id=lclx.id and lc.diancxxb_id="+Diancxxb_id+" \n " 
				       + "      and lc.faz_id="+Faz_id+" and lc.daoz_id="+Daoz_id+" \n";
			
			ResultSet rs=con.getResultSet(sql);
			while(rs.next()){
				
				bsh.set(rs.getString("mingc"), rs.getInt("zhi"));
			}
			
			rs.close();
			return bsh;
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return null;
	}
	
	public static void UpdateChepsjtzb(long Diancxxb_id,long Chepb_id,String Renymc) {
		// TODO 自动生成方法存根
		JDBCcon con=new JDBCcon();
		
		try{
			double mdb_oldbiaoz=0;
			int		mint_oldchebb_id=0;
			String	mstr_oldcheph="";
			
			String sql="select * from chepb where id="+Chepb_id;
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				mdb_oldbiaoz=rs.getDouble("biaoz");
				mint_oldchebb_id=rs.getInt("chebb_id");
				mstr_oldcheph=rs.getString("cheph");
			}
			rs.close();
			
			sql=" insert into chepsjtzb "
                + " (id, chepb_id, biaoz, chebb_id, cheph, tiaozsj, tiaozry) "
                + "    values "
                + " (getnewid("+Diancxxb_id+"), "+Chepb_id+", "+mdb_oldbiaoz+", "+mint_oldchebb_id+", '"+mstr_oldcheph+"', sysdate, '"+Renymc+"')";
			con.getInsert(sql);
		
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}
	
	public void ReCountFahbKfzl(String strChepb_id,long lngDiancxxb_id){
//		重算矿方质量
		JDBCcon con=new JDBCcon();
		long lngFahb_id=0;
		String sql="select distinct fahb_id from chepb where id in ("+strChepb_id+")";
		ResultSetList rsl=con.getResultSetList(sql);
		while(rsl.next()){
			
			lngFahb_id=rsl.getLong("fahb_id");
			ReCountFahbKfzlmx(lngFahb_id,lngDiancxxb_id);
		}
	}

	public void ReCountFahbKfzlmx(long lngFahb_id,long lngDiancxxb_id) {
		// TODO 自动生成方法存根
//		重算矿方质量明细项
		JDBCcon con=new JDBCcon();
		StringBuffer sbsql=new StringBuffer();
		ResultSetList rsl=null;
		boolean InsertKuangfzlb=false;
		long lngGongysb_id=0;
		long lngMeikxxb_id=0;
		long lngKuangfzlb_id=0;
//		判断该发货有无kuangfzl,如果没有InsertKuangfzlb=true;
		sbsql.append("select kuangfzlb_id,gongysb_id,meikxxb_id from fahb where id=").append(lngFahb_id);
		rsl=con.getResultSetList(sbsql.toString());
		if(rsl.next()){
			
			if(rsl.getLong("kuangfzlb_id")==0){
				
				lngGongysb_id=rsl.getLong("gongysb_id");
				lngMeikxxb_id=rsl.getLong("meikxxb_id");
				InsertKuangfzlb=true;
			}else{
				
				lngKuangfzlb_id=rsl.getLong("kuangfzlb_id");
			}
		}
		
		sbsql.setLength(0);
		
		sbsql.append("select 	\n");
		sbsql.append("Round(sum(decode(cp.biaoz,0,0,cp.biaoz*kzzb.qnet_ar))/sum(decode(cp.biaoz,0,1,cp.biaoz)),2) as qnetar, ").append("\n");
		sbsql.append("Round(sum(decode(cp.biaoz,0,0,cp.biaoz*kzzb.aar))/sum(decode(cp.biaoz,0,1,cp.biaoz)),2) as aar, ").append("\n");
		sbsql.append("Round(sum(decode(cp.biaoz,0,0,cp.biaoz*kzzb.ad))/sum(decode(cp.biaoz,0,1,cp.biaoz)),2) as ad, ").append("\n");
		sbsql.append("Round(sum(decode(cp.biaoz,0,0,cp.biaoz*kzzb.vdaf))/sum(decode(cp.biaoz,0,1,cp.biaoz)),2) as vdaf, ").append("\n");
		sbsql.append("Round(sum(decode(cp.biaoz,0,0,cp.biaoz*kzzb.mt))/sum(decode(cp.biaoz,0,1,cp.biaoz)),2) as mt, ").append("\n");
		sbsql.append("Round(sum(decode(cp.biaoz,0,0,cp.biaoz*kzzb.stad))/sum(decode(cp.biaoz,0,1,cp.biaoz)),2) as stad, ").append("\n");
		sbsql.append("Round(sum(decode(cp.biaoz,0,0,cp.biaoz*kzzb.aad))/sum(decode(cp.biaoz,0,1,cp.biaoz)),2) as aad, ").append("\n");
		sbsql.append("Round(sum(decode(cp.biaoz,0,0,cp.biaoz*kzzb.mad))/sum(decode(cp.biaoz,0,1,cp.biaoz)),2) as mad, ").append("\n");
		sbsql.append("Round(sum(decode(cp.biaoz,0,0,cp.biaoz*kzzb.qbad))/sum(decode(cp.biaoz,0,1,cp.biaoz)),2) as qbad, ").append("\n");
		sbsql.append("Round(sum(decode(cp.biaoz,0,0,cp.biaoz*kzzb.had))/sum(decode(cp.biaoz,0,1,cp.biaoz)),2) as had, ").append("\n");
		sbsql.append("Round(sum(decode(cp.biaoz,0,0,cp.biaoz*kzzb.vad))/sum(decode(cp.biaoz,0,1,cp.biaoz)),2) as vad, ").append("\n");
		sbsql.append("Round(sum(decode(cp.biaoz,0,0,cp.biaoz*kzzb.FCAD))/sum(decode(cp.biaoz,0,1,cp.biaoz)),2) as FCAD, ").append("\n");
		sbsql.append("Round(sum(decode(cp.biaoz,0,0,cp.biaoz*kzzb.std))/sum(decode(cp.biaoz,0,1,cp.biaoz)),2) as std, ").append("\n");
		sbsql.append("Round(sum(decode(cp.biaoz,0,0,cp.biaoz*kzzb.QGRAD))/sum(decode(cp.biaoz,0,1,cp.biaoz)),2) as qgrad, ").append("\n");
		sbsql.append("Round(sum(decode(cp.biaoz,0,0,cp.biaoz*kzzb.hdaf))/sum(decode(cp.biaoz,0,1,cp.biaoz)),2) as hdaf, ").append("\n");
		sbsql.append("Round(sum(decode(cp.biaoz,0,0,cp.biaoz*kzzb.qgrad_daf))/sum(decode(cp.biaoz,0,1,cp.biaoz)),2) as qgrad_daf, ").append("\n");
		sbsql.append("Round(sum(decode(cp.biaoz,0,0,cp.biaoz*kzzb.sdaf))/sum(decode(cp.biaoz,0,1,cp.biaoz)),2) as sdaf, ").append("\n");
		sbsql.append("Round(sum(decode(cp.biaoz,0,0,cp.biaoz*kzzb.t1))/sum(decode(cp.biaoz,0,1,cp.biaoz)),2) as t1, ").append("\n");
		sbsql.append("Round(sum(decode(cp.biaoz,0,0,cp.biaoz*kzzb.t2))/sum(decode(cp.biaoz,0,1,cp.biaoz)),2) as t2, ").append("\n");
		sbsql.append("Round(sum(decode(cp.biaoz,0,0,cp.biaoz*kzzb.t3))/sum(decode(cp.biaoz,0,1,cp.biaoz)),2) as t3, ").append("\n");
		sbsql.append("Round(sum(decode(cp.biaoz,0,0,cp.biaoz*kzzb.t4))/sum(decode(cp.biaoz,0,1,cp.biaoz)),2) as t4  ").append("\n");
		sbsql.append("from chepb cp,kuangfzlzb kzzb ").append("\n");
		sbsql.append("where cp.fahb_id=").append(lngFahb_id).append("\n"); 
		sbsql.append("and cp.kuangfzlzb_id=kzzb.id ").append("\n");
		
		rsl=con.getResultSetList(sbsql.toString());
		if(rsl.next()){
			
			sbsql.setLength(0);
			if(InsertKuangfzlb){
				
				String lngkuangfzlb_id=MainGlobal.getNewID(lngDiancxxb_id);
				sbsql.append("begin		\n");
				sbsql.append(" insert into kuangfzlb 	\n");
				sbsql.append(" 		(id, gongysb_id, meikxxb_id, qnet_ar, aar, ad, vdaf, mt, stad, aad, mad, qbad, had, vad, fcad, std, qgrad, hdaf, qgrad_daf, sdaf, var, t1, t2, t3, t4, leib, lury)		\n");
				sbsql.append("	values	\n");
				sbsql.append("	("+lngkuangfzlb_id+", "+lngGongysb_id+", "+lngMeikxxb_id+", "+rsl.getDouble("qnetar")+", "+rsl.getDouble("aar")+", "+rsl.getDouble("ad")+", "+rsl.getDouble("vdaf")+", "+
							rsl.getDouble("mt")+", "+rsl.getDouble("stad")+", "+rsl.getDouble("aad")+", "+rsl.getDouble("mad")+", "+rsl.getDouble("qbad")+", "+rsl.getDouble("had")+", "+rsl.getDouble("vad")+", "+
							rsl.getDouble("fcad")+", "+rsl.getDouble("std")+", "+rsl.getDouble("qgrad")+", "+rsl.getDouble("hdaf")+", "+rsl.getDouble("qgrad_daf")+", "+rsl.getDouble("sdaf")+", 0, "+
							rsl.getDouble("t1")+", "+rsl.getDouble("t2")+", "+rsl.getDouble("t3")+", "+rsl.getDouble("t4")+", 0, '');	\n");
				
				sbsql.append(" update fahb set kuangfzlb_id=").append(lngkuangfzlb_id).append(" where id=").append(lngFahb_id).append(";	\n");
				sbsql.append("end;");
			}else{
				
				sbsql.append(" update kuangfzlb set qnet_ar="+rsl.getDouble("qnetar")+", aar = "+rsl.getDouble("aar")+", ad = "+rsl.getDouble("ad")+", vdaf = "+rsl.getDouble("vdaf")+", mt = "+rsl.getDouble("mt")
						+",	\n stad = "+rsl.getDouble("stad")+", aad = "+rsl.getDouble("aad")+", mad = "+rsl.getDouble("mad")+", qbad = "+rsl.getDouble("qbad")+", had = "+rsl.getDouble("had")+", vad = "+rsl.getDouble("vad")
						+",	\n fcad = "+rsl.getDouble("fcad")+", std = "+rsl.getDouble("std")+", qgrad = "+rsl.getDouble("qgrad")+", hdaf = "+rsl.getDouble("hdaf")+", qgrad_daf = "+rsl.getDouble("qgrad_daf")
						+",	\n sdaf = "+rsl.getDouble("sdaf")+", t1 = "+rsl.getDouble("t1")+",   t2 = "+rsl.getDouble("t2")+",  t3 = "+rsl.getDouble("t3")+",  t4 = "+rsl.getDouble("t4")+" 	\n	where id="+lngKuangfzlb_id+"");
			}
		}
		con.getUpdate(sbsql.toString());
	}
	
	public double getMljPzbj(long ranlpzb_Id) {
		// TODO 自动生成方法存根
		JDBCcon con=new JDBCcon();
		String Pinzbj="";
		try{
			
			String sql="select pinzms from pinzb where id="+ranlpzb_Id;
			ResultSetList rsl=con.getResultSetList(sql);
			if(rsl.next()){
				
				Pinzbj=rsl.getString("pinzms");
			}
			
			if(Pinzbj.indexOf(Locale.pinzbj_bij)>-1){
				
				return Double.parseDouble(Pinzbj.substring(Pinzbj.lastIndexOf("=")+1));
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return 0;
	}
	
	public static String getTransform_Xiaoscl(int value){
			// TODO 自动生成方法存根
		String strXiaoscz="";
		switch(value){
			
			case 0:
				strXiaoscz="";
				break;
			
			case 1:
				strXiaoscz=Locale.jinw_ht_xscz;
				break;
			
			case 2:
				strXiaoscz=Locale.sheq_ht_xscz;
				break;
				
			case 3:
				strXiaoscz=Locale.siswr_ht_xscz;
				break;
				
			case 4:
				strXiaoscz=Locale.siswryw_ht_xscz;
				break;	
				
			case 5:
				strXiaoscz=Locale.siswrlw_ht_xscz;
				break;
				
			case 6:
				strXiaoscz=Locale.siswrsw_ht_xscz;
				break;	
				
			case 7:
				strXiaoscz=Locale.siswrsiw_ht_xscz;
				break;	
		}
		
		return strXiaoscz;
	}
	
	public static String getTransform_Hetjgs(String jijgs, String zhibdw, double hetj, long diancxxb_id){
		
//		函数功能：
		
//				当煤款合同用户自定义“价格公式” 后，有一些关键字是需要程序支持的，
//			由于地域不同 关键字称呼亦不同，故需要在item中增加转换功能。
//			此函数为了进行转换之用。
		
//		函数逻辑：

//				通过itemsorf 和 item 表进行关联，找到关键字组，
//			用item中的编码去替换item中的名称，对定义的公式进行转换。
//		函数形参：
		
//			jijgs 基价公式
		
		if(jijgs!=null&&!jijgs.equals("")){
			
			JDBCcon con = new JDBCcon();
			String sql = "";
			
			sql="select distinct it.bianm,it.mingc\n" +
				"       from itemsort its,item it\n" + 
				"       where its.id = it.itemsortid\n" + 
				"             and its.bianm='"
				+Locale.itemsort_HTJGGS+"'";
			
			ResultSetList rsl = con.getResultSetList(sql);
			
			while(rsl.next()){
				
				if(rsl.getString("bianm").trim().equals(Locale.Qnetar_zhibb)
						||rsl.getString("bianm").trim().equals(Locale.Qgrad_zhibb)
						||rsl.getString("bianm").trim().equals(Locale.Qbad_zhibb)
						||rsl.getString("bianm").trim().equals(Locale.jiessl_zhibb)
					){
					
					jijgs=jijgs.replaceAll(rsl.getString("mingc"),rsl.getString("bianm")+zhibdw);
					
				}else{
					
					jijgs=jijgs.replaceAll(rsl.getString("mingc"),rsl.getString("bianm"));
				}
			}
//			默认有“合同价”的公式指标
			jijgs=jijgs.replaceAll("合同价", String.valueOf(hetj));
			
			jijgs="合同价格="+jijgs+";";
			rsl.close();
			con.Close();
			
			jijgs="import com.zhiren.common.CustomMaths;\n "+getHetjgs_aide(diancxxb_id)+"\n"+jijgs;
		}
		
		return jijgs;
	}
	
	public static String getHetjgs_aide(long _Diancxxb_id) {
//		得到合同价格辅助计算公式
		JDBCcon con =new JDBCcon();
		String Gongs="";
		try {   
		   //煤款结算公式
			ResultSet rs= con.getResultSet("select id from gongsb where mingc='合同价格辅助公式' and leix='结算' and zhuangt=1 and diancxxb_id="+_Diancxxb_id);
			if (rs.next()) {
		   	
				DataBassUtil clob=new DataBassUtil();
				Gongs=clob.getClob("gongsb", "gongs", rs.getLong(1));
			}
			rs.close();
			
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally{
			
			con.Close();
		}
	   
	   return Gongs;
	}
	
	public static boolean CheckMljRz(String Zhib) {
		// TODO 自动生成方法存根
//		判断是不是目录价的热值基价
		if(Zhib.equals(Locale.Qnetar_zhibb)
				||Zhib.equals(Locale.Qgrad_zhibb)
				||Zhib.equals(Locale.Qbad_zhibb)){
			
			return true;
		}
		return false;
	}
	
	public static boolean CheckMljHff(String Zhib) {
		// TODO 自动生成方法存根
//		判断是不是目录价的挥发分
		if(Zhib.equals(Locale.Vdaf_zhibb)
				||Zhib.equals(Locale.Vad_zhibb)){
			
			return true;
		}
		return false;
	}
	
	public static boolean CheckMljLiuf(String Zhib) {
		// TODO 自动生成方法存根
//		判断是不是目录价的挥发分
		if(Zhib.equals(Locale.Std_zhibb)
				||Zhib.equals(Locale.Stad_zhibb)){
			
			return true;
		}
		return false;
	}
	
	public static boolean CheckMljHiuf(String Zhib) {
		// TODO 自动生成方法存根
//		判断是不是目录价的灰分
		if(Zhib.equals(Locale.Ad_zhibb)
				||Zhib.equals(Locale.Aad_zhibb)
				||Zhib.equals(Locale.Aar_zhibb)){
			
			return true;
		}
		return false;
	}
	
	public static String getZhibbdw(String Zhibmc,String Danw) {
		// TODO 自动生成方法存根
//		返回指标对应的单位
		String strValue="";
		if(Zhibmc.equals(Locale.jiessl_zhibb)){
			
			if(Danw.equals("")){
				
				strValue=Locale.dun_danw;
			}else{
				
				strValue=Danw;
			}
			
		}else if(Zhibmc.equals(Locale.Qnetar_zhibb)||Zhibmc.equals(Locale.Qgrad_zhibb)||Zhibmc.equals(Locale.Qbad_zhibb)){
			
//			热量
			if(Danw.equals("")){
				
				strValue=Locale.zhaojmqk_danw;
			}else{
				
				strValue=Danw;
			}
		}else if(Zhibmc.equals(Locale.T2_zhibb)){
			
//			灰熔点
			strValue="";
		}else{
			
			strValue="";
		}
		
		return strValue;
	}
	
	public static double getUnit_transform(String zhibmc,String zhibdw, double zhi) {
		// TODO 自动生成方法存根
//		单位转换方法
		if(zhibdw!=null){
			
			if(zhibmc.equals(Locale.Qnetar_zhibb)||zhibmc.equals(Locale.Qgrad_zhibb)||zhibmc.equals(Locale.Qbad_zhibb)){
				
				if(zhibdw.equals(Locale.qiankmqk_danw)){
//					如果是以上三个值，单位又是“千卡/千克”,则将兆焦/千克转化为千卡/千克
					zhi=MainGlobal.Mjkg_to_kcalkg(zhi, 0);
				}
			}else if(zhibmc.equals(Locale.jiessl_zhibb)){
//				如果是结算数量
				if(zhibdw.equals(Locale.wandun_danw)){
//					系统默认数量单位是吨，如果数量单位是万吨，则将数量除以10000
					zhi=CustomMaths.Round_new(zhi, 10);
				}
			}
		}
		return zhi;
	}
	
	public static double getUnit_transform(String zhibmc,String zhibdw, double zhi,String xiaosclfs) {
		// TODO 自动生成方法存根
//		单位转换方法
		if(zhibdw!=null){
			
			if(zhibmc.equals(Locale.Qnetar_zhibb)||zhibmc.equals(Locale.Qgrad_zhibb)||zhibmc.equals(Locale.Qbad_zhibb)){
				
				if(zhibdw.equals(Locale.qiankmqk_danw)){
//					如果是以上三个值，单位又是“千卡/千克”,则将兆焦/千克转化为千卡/千克
					zhi=MainGlobal.Mjkg_to_kcalkg(zhi, 0, xiaosclfs);
				}
			}else if(zhibmc.equals(Locale.jiessl_zhibb)){
//				如果是结算数量
				if(zhibdw.equals(Locale.wandun_danw)){
//					系统默认数量单位是吨，如果数量单位是万吨，则将数量除以10000
					zhi=CustomMaths.Round_new(zhi/10000, 10);
				}
			}
		}
		return zhi;
	}
	
	public static String getJiesszzh(String jies_Jieslqzfs, String shul,String xiaosw) {
		// TODO 自动生成方法存根
//		结算公式和数量组合
		
		String mstrExpression="";
		
		if(jies_Jieslqzfs.equals("round_new(sum())")||jies_Jieslqzfs.equals("round(sum())")||jies_Jieslqzfs.equals(Locale.xiangjhtysswr_jiesghlqzfs_xitxx)){
			
			jies_Jieslqzfs="round_new(sum())";
			mstrExpression=jies_Jieslqzfs.substring(0,jies_Jieslqzfs.indexOf(")"))+shul+"),"+xiaosw+")";
		}else if(jies_Jieslqzfs.equals("sum(round_new())")||jies_Jieslqzfs.equals("sum(round())")||jies_Jieslqzfs.equals(Locale.anlsswrhxj_jiesghlqzfs_xitxx)){
			
			jies_Jieslqzfs="sum(round_new())";
			mstrExpression=jies_Jieslqzfs.substring(0,jies_Jieslqzfs.indexOf(")"))+shul+","+xiaosw+"))";
		}else if(jies_Jieslqzfs.equals("sum()")||jies_Jieslqzfs.equals(Locale.bujxqzcz_jiesghlqzfs_xitxx)){
			
			jies_Jieslqzfs="sum()";
			mstrExpression=jies_Jieslqzfs.substring(0,jies_Jieslqzfs.indexOf("(")+1)+shul+")";
		}
		
		return mstrExpression;
	}
	
	public static double getZhib_info(Balances_variable bsv,String zhibmc, String item) {
		// TODO 自动生成方法存根
		double DblValue=0;
		
		if(item.equals("js")){
			
			if(zhibmc.equals(Locale.jiessl_zhibb)){
				
				DblValue=bsv.getJiessl();
			}else if(zhibmc.equals(Locale.Qnetar_zhibb)){
				
				DblValue=bsv.getQnetar_js();
			}else if(zhibmc.equals(Locale.Std_zhibb)){
				
				DblValue=bsv.getStd_js();
			}else if(zhibmc.equals(Locale.Ad_zhibb)){
				
				DblValue=bsv.getAd_js();
			}else if(zhibmc.equals(Locale.Vdaf_zhibb)){
				
				DblValue=bsv.getVdaf_js();
			}else if(zhibmc.equals(Locale.Mt_zhibb)){
				
				DblValue=bsv.getMt_js();
			}else if(zhibmc.equals(Locale.Qgrad_zhibb)){
				
				DblValue=bsv.getQgrad_js();
			}else if(zhibmc.equals(Locale.Qbad_zhibb)){
				
				DblValue=bsv.getQbad_js();
			}else if(zhibmc.equals(Locale.Had_zhibb)){
				
				DblValue=bsv.getHad_js();
			}else if(zhibmc.equals(Locale.Stad_zhibb)){
				
				DblValue=bsv.getStad_js();
			}else if(zhibmc.equals(Locale.Mad_zhibb)){
				
				DblValue=bsv.getMad_js();
			}else if(zhibmc.equals(Locale.Aar_zhibb)){
				
				DblValue=bsv.getAar_js();
			}else if(zhibmc.equals(Locale.Aad_zhibb)){
				
				DblValue=bsv.getAad_js();
			}else if(zhibmc.equals(Locale.Vad_zhibb)){
				
				DblValue=bsv.getVad_js();
			}else if(zhibmc.equals(Locale.T2_zhibb)){
				
				DblValue=bsv.getT2_js();
			}else if(zhibmc.equals(Locale.Yunju_zhibb)){
				
				DblValue=bsv.getYunju_js();
			}else if(zhibmc.equals(Locale.Star_zhibb)){
				
				DblValue=bsv.getStar_js();
			}
		}
		
		return DblValue;
	}
	
	public static double getZhib_info_Shih(Balances_variable_Shih bsv,String zhibmc, String item) {
		// TODO 自动生成方法存根
//		石灰石结算中取结算指标用
		double DblValue=0;
		
		if(item.equals("js")){
			
			if(zhibmc.equals(Locale.CaO_zhibb)){
				
				DblValue=bsv.getCaO_js();
			}else if(zhibmc.equals(Locale.MgO_zhibb)){
				
				DblValue=bsv.getMgO_js();
			}else if(zhibmc.equals(Locale.Xid_zhibb)){
				
				DblValue=bsv.getXid_js();
			}
		}
		
		return DblValue;
	}
	
	public static double getYanZZb_info(Jiesdyzbean yzb,String zhibmc) {
		// TODO 自动生成方法存根
		
//		得到验证指标基础信息
		double DblValue=0;
		
		if(zhibmc.equals(Locale.Qnetar_zhibb)){
			
			DblValue=yzb.getQnetar();
		}else if(zhibmc.equals(Locale.Std_zhibb)){
			
			DblValue=yzb.getStd();
		}else if(zhibmc.equals(Locale.Ad_zhibb)){
			
			DblValue=yzb.getAd();
		}else if(zhibmc.equals(Locale.Vdaf_zhibb)){
			
			DblValue=yzb.getVdaf();
		}else if(zhibmc.equals(Locale.Mt_zhibb)){
			
			DblValue=yzb.getMt();
		}else if(zhibmc.equals(Locale.Qgrad_zhibb)){
			
			DblValue=yzb.getQgrad();
		}else if(zhibmc.equals(Locale.Qbad_zhibb)){
			
			DblValue=yzb.getQbad();
		}else if(zhibmc.equals(Locale.Had_zhibb)){
			
			DblValue=yzb.getHad();
		}else if(zhibmc.equals(Locale.Stad_zhibb)){
			
			DblValue=yzb.getStad();
		}else if(zhibmc.equals(Locale.Mad_zhibb)){
			
			DblValue=yzb.getMad();
		}else if(zhibmc.equals(Locale.Aar_zhibb)){
			
			DblValue=yzb.getAar();
		}else if(zhibmc.equals(Locale.Aad_zhibb)){
			
			DblValue=yzb.getAad();
		}else if(zhibmc.equals(Locale.Vad_zhibb)){
			
			DblValue=yzb.getVad();
		}else if(zhibmc.equals(Locale.T2_zhibb)){
			
			DblValue=yzb.getT2();
		}
		return DblValue;
	}
	
    public static String FormatDate(Date _date) {
        if (_date == null) {
            return DateUtil.Formatdate("yyyy-MM-dd", new Date());
        }
        return DateUtil.Formatdate("yyyy-MM-dd", _date);
    }
    
	public static Date StringFormatdate(String format,String date) {
		java.util.Date returnday = new java.util.Date();
		try{
			 java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat(format);
			 returnday = sdf.parse(date);
		}catch (Exception e) {

		 	System.out.println("String to Date error");
		 }
		 return returnday;
	}
	
//	检查编号是否重复
	public static boolean checkbh_Shih(String jiesbh,long Meikjsb_id,long Yunfjsb_id) {
		// TODO 自动生成方法存根
		JDBCcon con = new JDBCcon();
		String sql = "";
		try {
			
			if(!jiesbh.equals("")){
				
				if(Meikjsb_id==0&&Yunfjsb_id==0){
//					insert时检查结算编号
					sql = " select bianm from ((select bianm from shihjsb) union (select bianm from shihjsyfb)) where bianm='"
						+ jiesbh + "'";
				}else{
//					update时检查验收编号
					sql=" select bianm from ((select bianm as bianm from shihjsb) union (select bianm as bianm from shihjsyfb)) where bianm='"+jiesbh+"' and (id<>"+Meikjsb_id+" and id<>"+Yunfjsb_id+")";
				}
				
			}else{
				
				return false;
			}
			
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				return false;
			}

			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return true;
	}
	
//	检查编号是否重复
	public static boolean checkbh(String jiesbh,long Meikjsb_id,long Yunfjsb_id) {
		// TODO 自动生成方法存根
		JDBCcon con = new JDBCcon();
		String sql = "";
		try {
			
			if(!jiesbh.equals("")){
				
				if(Meikjsb_id==0&&Yunfjsb_id==0){
//					insert时检查结算编号
					sql = " select bianm from ((select bianm from kuangfjsmkb) union (select bianm from kuangfjsyfb)) where bianm='"
						+ jiesbh + "'";
				}else{
//					update时检查验收编号
					sql=" select bianm from ((select bianm,id from kuangfjsmkb) union (select bianm ,id from kuangfjsyfb)) where bianm='"+jiesbh+"' and (id<>"+Meikjsb_id+" and id<>"+Yunfjsb_id+")";
				}
				
			}else{
				
				return false;
			}
			
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				return false;
			}

			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return true;
	}
	
//  结算时生成结算编号
    public static String getJiesbh(String Diancxxb_Id,String JiesType){
		
			JDBCcon con=new JDBCcon();
			String strJiesbh="";
			String strJiesbhtmp="";
			
			try{
 				java.util.Date datCur = new java.util.Date();
	            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
	            String dat =formatter.format(datCur);
 	            int intBh=0;
 	            
 	            String strJiesbhqz="";
 	            String strJiesbhgssx="";
 	            String strFadqypysx="";
 	            String strtmp="";
 	            
// 	            结算编号前缀
 	            strJiesbhqz=MainGlobal.getXitxx_item("结算","结算单编码前缀",Diancxxb_Id,"CDT-JS");
 	            strJiesbhgssx=MainGlobal.getXitxx_item("结算","区域公司拼音缩写",Diancxxb_Id,"DTRL-CG");
 	            strFadqypysx=MainGlobal.getTableCol("diancxxb","piny","id",Diancxxb_Id);
 	            
 	            if(!strJiesbhqz.equals("")&&strJiesbhqz!=null){
 	            	
 	            	strJiesbhqz=strJiesbhqz+"-";
 	            }
 	            
 	            if(!strJiesbhgssx.equals("")&&strJiesbhgssx!=null){
 	            	
 	            	strJiesbhgssx=strJiesbhgssx+"-";
 	            }
 	            
// 	            if(!strFadqypysx.equals("")&&strFadqypysx!=null){
// 	            	
// 	            	strFadqypysx=strFadqypysx+"-";
// 	            }
 	            
 	           strtmp=strJiesbhqz+strJiesbhgssx;
// 	          strtmp=strJiesbhqz+strJiesbhgssx+strFadqypysx;
 	            
				String Sql="select max(jiesbh) as jiesbh from (select bianm as jiesbh from kuangfjsmkb where diancxxb_id="+Diancxxb_Id
						+ " union select bianm as jiesbh from kuangfjsyfb where diancxxb_id="+Diancxxb_Id+")"
						+ " where jiesbh like '%"+strtmp+dat+"%' ";
				ResultSet rs=con.getResultSet(Sql);
				if(rs.next()){
					
					strJiesbh=rs.getString("jiesbh");
				}
				rs.close();
				
				if(strJiesbh==null){
					
					strJiesbh=strtmp+dat+"-"+"0000";
				}
				
				if(!JiesType.equals("")&&strJiesbh.lastIndexOf(JiesType)>strJiesbh.lastIndexOf(dat)){
					
					strJiesbh=strJiesbh.substring(0,strJiesbh.length()-(JiesType.length()+1));
					JiesType="-"+JiesType;
				}
				
				intBh=Integer.parseInt(strJiesbh.trim().substring(strJiesbh.trim().length()-4,strJiesbh.trim().length()));
	            intBh=intBh+1;
	            
	            if(intBh<10000 && intBh>=1000){
	            	strJiesbhtmp+=String.valueOf(intBh);
		        }else if(intBh<1000 && intBh>=100){        	
	            	strJiesbhtmp+="0"+String.valueOf(intBh);
	            }else if(intBh<100 && intBh>=10){
	            	strJiesbhtmp+="00"+String.valueOf(intBh);
	            }else{
	            	strJiesbhtmp+="000"+String.valueOf(intBh);
	            }
	            
			}catch(Exception e){
				
				e.printStackTrace();
			}finally{
				
				con.Close();
			}
		
		return strJiesbh.substring(0,strJiesbh.trim().length()-4)+strJiesbhtmp+JiesType;
	}
    
    public static String getJiesbh_Shih(String Diancxxb_Id) throws SQLException{
//    	得到石灰石的结算编号	就是一个排序号
    	JDBCcon con=new JDBCcon();
    	
    	String strJiesbh="";
    	String strJiesbhtmp="";
    	java.util.Date datCur = new java.util.Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String dat =formatter.format(datCur);
        int intBh=0;
    	
    	String sql="select max(bianm) as jiesbh from\n" +
    			"   	(select bianm from shihjsb where diancxxb_id="+Diancxxb_Id+" \n" + 
    			"			union\n" + 
    			"		select bianm from shihjsyfb where diancxxb_id="+Diancxxb_Id+")	\n" + 
    			"		where bianm like '%"+dat+"%'";
    	
    	ResultSet rs=con.getResultSet(sql);
    	if(rs.next()){
    		
    		strJiesbh=rs.getString("jiesbh");
    	}
    	rs.close();
    	con.Close();
    	
    	if(strJiesbh==null){
    		
    		strJiesbh=dat+"-"+"000";
    	}
    	
    	intBh=Integer.parseInt(strJiesbh.trim().substring(strJiesbh.trim().length()-3,strJiesbh.trim().length()));
        intBh=intBh+1;
    	
        if(intBh<1000 && intBh>=100){        	
        	strJiesbhtmp+=String.valueOf(intBh);
        }else if(intBh<100 && intBh>=10){
        	strJiesbhtmp+="0"+String.valueOf(intBh);
        }else{
        	strJiesbhtmp+="00"+String.valueOf(intBh);
        }
        
    	return strJiesbh.substring(0,strJiesbh.trim().length()-3)+strJiesbhtmp;
    }
    
    public static String[] getDitGongys_info(){
    	
    	JDBCcon con=new JDBCcon();
    	String[] GongysInfo=new String[3];
    	try{
    		
    		String sql="select distinct id,shoukdw,kaihyh,zhangh from ditjsb where rownum=1 order by id desc ";
    		ResultSet rs=con.getResultSet(sql);
    		if(rs.next()){
    			
    			GongysInfo[0]=rs.getString("shoukdw");
    			GongysInfo[1]=rs.getString("kaihyh");
    			GongysInfo[2]=rs.getString("zhangh");
    		}
    		rs.close();
    	}catch(Exception e){
    		
    		e.printStackTrace();
    	}finally{
    		
    		con.Close();
    	}
    	
    	return GongysInfo;
    }
    
	public static boolean InsertYunfjszlb(long Diancxxb_id, long Feiylbb_id, String Chepb_id, String Cheph, String Chebb_id, String Biaoz) {
		// TODO 自动生成方法存根
//		保存到运费结算重量表中(DIANCXXB_ID,FEIYLBB_ID,CHEPB_ID,CHEPH,CHEBB_ID,BIAOZ)
		JDBCcon con=new JDBCcon();
		long lngYunfjszlb_id=0;
		boolean blnFlag=false;
		
		StringBuffer sql = new StringBuffer("begin \n");
		StringBuffer sql2 = new StringBuffer("");
		String tableName="yunfjszlb";
		
		lngYunfjszlb_id=Check_Record_AlreadyexistsAndReturnId("yunfjszlb", "chepb_id="+Chepb_id+" and feiylbb_id="+Feiylbb_id+"");
		
		if(lngYunfjszlb_id>0){
//			如果存在记录那么更新Yunfjszlb，否则插入新记录
			
			sql.append("update ").append(tableName).append(" set ");
				
					sql.append("CHEPH").append(" = '");
					sql.append(Cheph).append("',");
					
					sql.append("CHEBB_ID").append(" = ");
					sql.append(Chebb_id).append(",");
					
					sql.append("JIFZL").append(" = ");
					sql.append(Biaoz);
					
			sql.append(" where id =").append(lngYunfjszlb_id).append(";\n");
		}else{
//			如果不存在记录那么更新Yunfjszlb，插入新记录
			
			sql.append(" insert into ").append(tableName).append("(id,diancxxb_id,chepb_id,feiylbb_id,cheph,chebb_id,jifzl");
			sql2.append(" getnewid(").append(Diancxxb_id).append("),")
					.append(Diancxxb_id).append(",")
					.append(Chepb_id).append(",")
					.append(Feiylbb_id).append(",'")
					.append(Cheph).append("',")
					.append(Chebb_id).append(",")
					.append(Biaoz);
				
			sql.append(")values(").append(sql2).append("); \n");
		}
		sql.append("end;	\n");
		
		if(con.getUpdate(sql.toString())>=0){
			
			blnFlag=true;
		}else{
			
			blnFlag=false;
		}
		con.Close();
		return blnFlag;
	}
	
	public static long Check_Record_AlreadyexistsAndReturnId(String TableName,String Where){
//		用于检查某个表中是否存在某条数据，如果存在则返回现有记录的ID
		JDBCcon con=new JDBCcon();
		
		if(!TableName.trim().equals("")&&!Where.trim().equals("")){
			
			try{
				
				String sql="select id from "+TableName+" where "+Where;
				ResultSet rs=con.getResultSet(sql);
				if(rs.next()){
					
					return rs.getLong("id");
				}
				rs.close();
			}catch(Exception e){
				
				e.printStackTrace();
			}finally{
				
				con.Close();
			}
		}
		
		return 0;
	}	
	
	//从结算设置表中取值
	public static String getJiessz_item(long diancxxb_id,long gongysb_id,long hetb_id,String bianm,String defaultValue){
		
		JDBCcon con=new JDBCcon();
		String value=defaultValue;
		String mstrzhi="";
		try{
			
			String sql="select bmb.bianm,jssz.zhi				\n"
					+ " from jiesszfahtglb faht,				\n"
					+ " jiesszfab fangab,						\n"
					+ " jiesszb jssz,							\n"
					+ " jiesszbmb bmb							\n"
					+ " where faht.jiesszfab_id=fangab.id		\n"
					+ " and fangab.shifsy=1						\n" 
					+ " and fangab.qiysj<=sysdate				\n"
					+ " and faht.hetb_id="+hetb_id+"			\n"
					+ " and fangab.gongysb_id="+gongysb_id+"	\n"
					+ " and fangab.diancxxb_id="+diancxxb_id+"	\n"
					+ " and jssz.jiesszfab_id=fangab.id			\n"
					+ " and jssz.jiesszbmb_id=bmb.id 			\n"
					+ " and bmb.bianm='"+bianm+"'";
			
			ResultSet rs=con.getResultSet(sql);
			while(rs.next()){
				
				if(mstrzhi.equals("")){
					
					mstrzhi=rs.getString("zhi");
				}else{
					
					mstrzhi+=","+rs.getString("zhi");
				}
			}
			rs.close();
			
			if(!mstrzhi.equals("")){
				
				value=mstrzhi;
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return value;
	}
	
	//取出整个结算设置表
	public static String[][] getJiessz_items(long diancxxb_id,long gongysb_id,long hetb_id){
		
		JDBCcon con=new JDBCcon();
		String Jiessz[][]=null;
		String mstrtmp="";
		int i=0;
		try{
			
			String sql=" select bmb.bianm,jssz.zhi,bmb.shifwy	\n"
					+ " from jiesszfahtglb faht,				\n"
					+ " jiesszfab fangab,						\n"
					+ " jiesszb jssz,							\n"
					+ " jiesszbmb bmb							\n"
					+ " where faht.jiesszfab_id=fangab.id		\n"
					+ " and fangab.shifsy=1						\n" 
					+ " and fangab.qiysj<=sysdate				\n"
					+ " and faht.hetb_id="+hetb_id+"			\n"
					+ " and fangab.gongysb_id="+gongysb_id+"	\n"
					+ " and fangab.diancxxb_id="+diancxxb_id+"	\n"
					+ " and jssz.jiesszfab_id=fangab.id			\n"
					+ " and jssz.jiesszbmb_id=bmb.id order by bmb.bianm			";
			
			ResultSetList rs=con.getResultSetList(sql);
			
			if(rs !=null){
				
				Jiessz=new String[rs.getRows()][2];
				while(rs.next()){
					
					if(mstrtmp.equals(rs.getString("bianm").trim())){
						
						i--;
						if(rs.getInt("shifwy")==0){
							
							
							Jiessz[i][1]=rs.getString("zhi").trim();
						}else{
							
							Jiessz[i][1]+=","+rs.getString("zhi").trim();
						}
						
					}else{
						
						Jiessz[i][0]=rs.getString("bianm").trim();
						Jiessz[i][1]=rs.getString("zhi").trim();
						mstrtmp=Jiessz[i][0];
					}
					
					i++;	
				}
			}
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return Jiessz;
	}
	
	public static String GetJiesgs(long Diancxxb_id,String Type){
		
		String strFilePath=MainGlobal.getXitsz(SysConstant.Gs_WZ_Xtsz, String.valueOf(Diancxxb_id), SysConstant.Gs_JS_FilePath);
		StringBuffer sb=new StringBuffer();
//		指向文件夹
		try {
			File Dir = new File(strFilePath);
			if (Dir.isDirectory()) {
	//			指向所有xml文档
				FileNameFilter fnf = new FileNameFilter("xml");
				File xmls[] = Dir.listFiles(fnf);
	//			遍历xml文档
				for (int i = 0; i < xmls.length; i++) {
					
					if(xmls[i].getName().equals(SysConstant.Gs_JS_FileName)){
						
						SAXBuilder builder = new SAXBuilder();
						FileInputStream fiss = new FileInputStream(xmls[i]);
						Document docw = builder.build(fiss);
						Element root = docw.getRootElement();
						List elist = root.getChildren();
						if(SysConstant.Gs_JS_RootName.equals(root.getName())) {
							
							for(int j=0;j<elist.size();j++){	
								
								Element ehead = (Element) elist.get(j);		//<gs>
								
								if(ehead.getChildTextTrim(SysConstant.Gs_JS_HeadName_DIANCXXB_ID).equals(String.valueOf(Diancxxb_id).trim())){
									
									
									if(Type.equals(SysConstant.Gs_JS_HeadName_Mk)){
										
										Element Gsehead=ehead.getChild(SysConstant.Gs_JS_HeadName_Mk);
										
										sb.append(Gsehead.getChildText(SysConstant.Gs_JS_ChildName_Blcsh)).append("\n");
										sb.append(Gsehead.getChildText(SysConstant.Gs_JS_ChildName_Gyff)).append("\n");
										sb.append(Gsehead.getChildText(SysConstant.Gs_JS_ChildName_Jsgc));
										return sb.toString();
									}else if(Type.equals(SysConstant.Gs_JS_HeadName_Yf)){
										
										Element Gsehead=ehead.getChild(SysConstant.Gs_JS_HeadName_Yf);
										
										sb.append(Gsehead.getChildText(SysConstant.Gs_JS_ChildName_Blcsh)).append("\n");
										sb.append(Gsehead.getChildText(SysConstant.Gs_JS_ChildName_Gyff)).append("\n");
										sb.append(Gsehead.getChildText(SysConstant.Gs_JS_ChildName_Jsgc));
										return sb.toString();
									}else if(Type.equals(SysConstant.Gs_JS_HeadName_Shih)){
										
										Element Gsehead=ehead.getChild(SysConstant.Gs_JS_HeadName_Shih);
										
										sb.append(Gsehead.getChildText(SysConstant.Gs_JS_ChildName_Blcsh)).append("\n");
										sb.append(Gsehead.getChildText(SysConstant.Gs_JS_ChildName_Gyff)).append("\n");
										sb.append(Gsehead.getChildText(SysConstant.Gs_JS_ChildName_Jsgc));
										return sb.toString();
									}
									
								}else if(ehead.getChildTextTrim(SysConstant.Gs_JS_HeadName_DIANCXXB_ID).equals("Default")){
//									说明已经走到xml<gs>的结尾了
									if(Type.equals(SysConstant.Gs_JS_HeadName_Mk)){
										
										Element Gsehead=ehead.getChild(SysConstant.Gs_JS_HeadName_Mk);
										
										sb.append(Gsehead.getChildText(SysConstant.Gs_JS_ChildName_Blcsh)).append("\n");
										sb.append(Gsehead.getChildText(SysConstant.Gs_JS_ChildName_Gyff)).append("\n");
										sb.append(Gsehead.getChildText(SysConstant.Gs_JS_ChildName_Jsgc));
										return sb.toString();
									}else if(Type.equals(SysConstant.Gs_JS_HeadName_Yf)){
										
										Element Gsehead=ehead.getChild(SysConstant.Gs_JS_HeadName_Yf);
										
										sb.append(Gsehead.getChildText(SysConstant.Gs_JS_ChildName_Blcsh)).append("\n");
										sb.append(Gsehead.getChildText(SysConstant.Gs_JS_ChildName_Gyff)).append("\n");
										sb.append(Gsehead.getChildText(SysConstant.Gs_JS_ChildName_Jsgc));
										return sb.toString();
									}else if(Type.equals(SysConstant.Gs_JS_HeadName_Shih)){
										
										Element Gsehead=ehead.getChild(SysConstant.Gs_JS_HeadName_Shih);
										
										sb.append(Gsehead.getChildText(SysConstant.Gs_JS_ChildName_Blcsh)).append("\n");
										sb.append(Gsehead.getChildText(SysConstant.Gs_JS_ChildName_Gyff)).append("\n");
										sb.append(Gsehead.getChildText(SysConstant.Gs_JS_ChildName_Jsgc));
										return sb.toString();
									}
								}
							}	
						}
					}
				}
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public static String FormatDate_GS(Date _date) {
        if (_date == null) {
            return "";
        }
        return DateUtil.Formatdate("yyyy-MM-dd", _date);
    }
	
	public static String InsertIntoGSKuangfjsb(JDBCcon con,String TableName,String TableID,String TableId_Diancjsb,String YF_kuangfjsmcb_id) throws Exception{
		
		StringBuffer bf=new StringBuffer();
		String sql="";
		long Diancxxb_id=0;
		String lnId="";
		ResultSet rs=null;
		if(TableName.equals("kuangfjsmkb")){
			try{
				sql=" select * from jiesb where id="+TableID;
			     rs=con.getResultSet(sql);
				
				if(rs.next()){
				Diancxxb_id=rs.getLong("diancxxb_id");
				
				lnId=MainGlobal.getNewID(Diancxxb_id);
				
				double hansdj=rs.getDouble("jiajqdj");
				double hansmk=rs.getDouble("jiajqdj")*rs.getDouble("jiessl")+rs.getDouble("bukmk");
				double shuik=hansmk/(1+rs.getDouble("shuil"));
				double buhsmk=hansmk-shuik;
				double buhsdj=hansdj/(1+rs.getDouble("shuil"));
				
				bf.append(" begin \n");
				bf.append(" insert into kuangfjsmkb \n");
				bf.append(" ( ID,DIANCXXB_ID,BIANM,GONGYSB_ID,GONGYSMC,FAZ,FAHKSRQ,FAHJZRQ,MEIZ,DAIBCH,YUANSHR,XIANSHR,\n");
				bf.append(" YANSKSRQ,YANSJZRQ,YANSBH,SHOUKDW,KAIHYH,ZHANGH,FAPBH,FUKFS,DUIFDD,CHES,JIESSL,GUOHL,\n");
				bf.append(" HANSDJ,BUKMK,HANSMK,BUHSMK,MEIKJE,SHUIK,SHUIL,BUHSDJ,JIESLX,JIESRQ,HETB_ID,LIUCZTB_ID,\n");
				bf.append(" LIUCGZID,KUANGFJSB_ID,DIANCJSMKB_ID,DANJC,BEIZ,LIUCGZB_ID,HETJ,JIESFRL,JIESLF,JIESRCRL,\n");
				bf.append(" JIESRL,JIESSLCY,JIHKJB_ID,KOUD,KUID,MEIKDWMC,MEIKXXB_ID,QIYF,RANLBMJBR,RANLBMJBRQ,\n");
				bf.append(" YINGD,YUNJ,YUNS,YUNSFSB_ID,ZHILJQ ,FENGSJJ\n");
				bf.append(" )");
				bf.append(" values(\n");
				bf.append(" "+lnId+","+Diancxxb_id+",'"+rs.getString("BIANM")+"',"+rs.getString("GONGYSB_ID")+",'"+rs.getString("GONGYSMC")+"','"+rs.getString("FAZ")+"',to_date('"+FormatDate_GS(rs.getDate("FAHKSRQ"))+"','yyyy-MM-dd'),to_date('"+FormatDate_GS(rs.getDate("FAHJZRQ"))+"','yyyy-MM-dd'),'"+rs.getString("MEIZ")+"','"+rs.getString("DAIBCH")+"','"+rs.getString("YUANSHR")+"','"+rs.getString("XIANSHR")+"',\n");
				bf.append(" to_date('"+FormatDate_GS(rs.getDate("YANSKSRQ"))+"','yyyy-MM-dd'),to_date('"+FormatDate_GS(rs.getDate("YANSJZRQ"))+"','yyyy-MM-dd'),'"+rs.getString("YANSBH")+"','"+rs.getString("SHOUKDW")+"','"+rs.getString("KAIHYH")+"','"+rs.getString("ZHANGH")+"','"+rs.getString("FAPBH")+"','"+rs.getString("FUKFS")+"','"+rs.getString("DUIFDD")+"',"+rs.getString("CHES")+","+rs.getString("JIESSL")+","+rs.getString("GUOHL")+",\n");
				bf.append(" "+hansdj+","+rs.getString("BUKMK")+","+hansmk+","+buhsmk+","+rs.getString("MEIKJE")+","+shuik+","+rs.getString("SHUIL")+",round_new("+buhsdj+",7),"+rs.getString("JIESLX")+",to_date('"+FormatDate_GS(rs.getDate("JIESRQ"))+"','yyyy-MM-dd'),"+rs.getString("HETB_ID")+","+/*rs.getString("LIUCZTB_ID")*/0+",\n");
				bf.append(" "+rs.getString("LIUCGZID")+","+0+","+TableId_Diancjsb+","+0+",'"+rs.getString("BEIZ")+"',"+/*rs.getString("LIUCGZID")*/0+","+rs.getString("HETJ")+","+rs.getString("JIESFRL")+","+rs.getString("JIESLF")+","+rs.getString("JIESRCRL")+",\n");
				bf.append(" "+rs.getString("JIESRL")+","+rs.getString("JIESSLCY")+","+rs.getString("JIHKJB_ID")+","+rs.getString("KOUD")+","+rs.getString("KUID")+",'"+rs.getString("MEIKDWMC")+"',"+rs.getString("MEIKXXB_ID")+","+rs.getString("QIYF")+",'"+rs.getString("RANLBMJBR")+"',to_date('"+FormatDate_GS(rs.getDate("RANLBMJBRQ"))+"','yyyy-MM-dd'),\n");
				bf.append(" "+rs.getString("YINGD")+","+rs.getString("YUNJ")+","+rs.getString("YUNS")+","+rs.getString("YUNSFSB_ID")+",'"+rs.getString("ZHILJQ")+"',"+rs.getString("FENGSJJ"));
				bf.append(" );\n");
				
				
				boolean Flag=false;	//区分数量折价用
				String sq=" select * from jieszbsjb t  where t.jiesdid = "+TableID;
				ResultSet rsl=con.getResultSet(sq);
				
				while(rsl.next()){
					
					if(MainGlobal.getTableCol("zhibb", "bianm", "id", rsl.getString("zhibb_id")).equals(Locale.jiessl_zhibb)){						
						Flag=true;
					}
					
					bf.append(" insert into jieszbsjb(id,jiesdid,zhibb_id,hetbz,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id)\n");
					bf.append(" values("+MainGlobal.getNewID(Diancxxb_id)+","+lnId+","+rsl.getString("zhibb_id")+",'"+rsl.getString("hetbz")+"',")
						.append(rsl.getString("gongf")+","+rsl.getString("changf")+","+rsl.getString("jies")+","+rsl.getString("yingk")+","+(Flag?hansdj:rsl.getDouble("zhejbz"))+",")
						.append((Flag?CustomMaths.Round_new(hansdj*rsl.getDouble("yingk"),2):rsl.getDouble("yingk"))+","+rsl.getString("zhuangt")+","+rsl.getString("yansbhb_id")+");");
				}
				
				bf.append(" end;");
				}
			}catch(Exception e){
				e.printStackTrace();
				throw new Exception("error");
			}
		}
		if(TableName.equals("kuangfjsyfb")){
			
			try{
				
				sql=" select * from jiesyfb where id="+TableID;
			     rs=con.getResultSet(sql);
				
				if(rs.next()){
				Diancxxb_id=rs.getLong("diancxxb_id");
				
				
				lnId=MainGlobal.getNewID(Diancxxb_id);
				
				bf.append(" begin \n");
				bf.append(" insert into kuangfjsyfb \n");
				bf.append(" ( ID,DIANCXXB_ID,BIANM,GONGYSB_ID,GONGYSMC,FAZ,FAHKSRQ,FAHJZRQ,MEIZ,DAIBCH,YUANSHR,XIANSHR,\n");
				bf.append(" YANSKSRQ,YANSJZRQ,YANSBH,SHOUKDW,KAIHYH,ZHANGH,FAPBH,FUKFS,DUIFDD,CHES,JIESSL,GUOHL,\n");
				bf.append(" GUOTYF,DITYF,JISKC,HANSDJ,BUKYF,HANSYF,BUHSYF,SHUIK,SHUIL,BUHSDJ,JIESLX,JIESRQ,HETB_ID,LIUCZTB_ID,\n");
				bf.append(" LIUCGZID,KUANGFJSB_ID,KUANGFJSMKB_ID,DIANCJSYFB_ID,DANJC,BEIZ,KUANGJSMKB_ID,LIUCGZB_ID,DIANCJSMKB_ID,\n");
				bf.append(" DITZF,GONGFSL,GUOTYFJF,GUOTZF,GUOTZFJF,JIESSLCY,KOUD,KUANGQYF,KUANGQZF,KUID,MEIKDWMC,MEIKXXB_ID,RANLBMJBR,RANLBMJBRQ,\n");
				bf.append(" YANSSL,YINGD,YINGK,YUNJ,YUNS,YUNSFSB_ID\n");
				bf.append(" )");
				bf.append(" values( \n");
				bf.append(" "+lnId+","+Diancxxb_id+",'"+rs.getString("BIANM")+"',"+rs.getString("GONGYSB_ID")+",'"+rs.getString("GONGYSMC")+"','"+rs.getString("FAZ")+"',to_date('"+FormatDate_GS(rs.getDate("FAHKSRQ"))+"','yyyy-MM-dd'),to_date('"+FormatDate_GS(rs.getDate("FAHJZRQ"))+"','yyyy-MM-dd'),'"+rs.getString("MEIZ")+"','"+rs.getString("DAIBCH")+"','"+rs.getString("YUANSHR")+"','"+rs.getString("XIANSHR")+"',\n");
				bf.append(" to_date('"+FormatDate_GS(rs.getDate("YANSKSRQ"))+"','yyyy-MM-dd'),to_date('"+FormatDate_GS(rs.getDate("YANSJZRQ"))+"','yyyy-MM-dd'),'"+rs.getString("YANSBH")+"','"+rs.getString("SHOUKDW")+"','"+rs.getString("KAIHYH")+"','"+rs.getString("ZHANGH")+"','"+rs.getString("FAPBH")+"','"+rs.getString("FUKFS")+"','"+rs.getString("DUIFDD")+"',"+rs.getString("CHES")+","+rs.getString("JIESSL")+","+rs.getString("GUOHL")+",\n");
				bf.append(" "+rs.getString("GUOTYF")+","+rs.getString("DITYF")+","+rs.getString("JISKC")+","+rs.getString("HANSDJ")+","+rs.getString("BUKYF")+","+rs.getString("HANSYF")+","+rs.getString("BUHSYF")+","+rs.getString("SHUIK")+","+rs.getString("SHUIL")+","+rs.getString("BUHSDJ")+","+rs.getString("JIESLX")+",to_date('"+FormatDate_GS(rs.getDate("JIESRQ"))+"','yyyy-MM-dd'),"+rs.getString("HETB_ID")+","+rs.getString("LIUCZTB_ID")+",\n");
				bf.append(" "+rs.getString("LIUCGZID")+","+0+","+YF_kuangfjsmcb_id+","+TableId_Diancjsb+","+0+",'"+rs.getString("BEIZ")+"',"+0+","+rs.getString("LIUCGZID")+","+0+",\n");
				bf.append(" "+rs.getString("DITZF")+","+rs.getString("GONGFSL")+","+rs.getString("GUOTYFJF")+","+rs.getString("GUOTZF")+","+rs.getString("GUOTZFJF")+","+rs.getString("JIESSLCY")+","+rs.getString("KOUD")+","+rs.getString("KUANGQYF")+","+rs.getString("KUANGQZF")+","+rs.getString("KUID")+",'"+rs.getString("MEIKDWMC")+"',"+rs.getString("MEIKXXB_ID")+",'"+rs.getString("RANLBMJBR")+"',to_date('"+FormatDate_GS(rs.getDate("RANLBMJBRQ"))+"','yyyy-MM-dd'),\n");
				bf.append(" "+rs.getString("YANSSL")+","+rs.getString("YINGD")+","+rs.getString("YINGK")+","+rs.getString("YUNJ")+","+rs.getString("YUNS")+","+rs.getString("YUNSFSB_ID"));
				bf.append(" );\n");
				

				String sq=" select * from jieszbsjb t  where t.jiesdid = "+TableID;
				ResultSet rsl=con.getResultSet(sq);
				
				while(rsl.next()){
					
					bf.append(" insert into jieszbsjb(id,jiesdid,zhibb_id,hetbz,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id)\n");
					bf.append(" values("+MainGlobal.getNewID(Diancxxb_id)+","+lnId+","+rsl.getString("zhibb_id")+",'"+rsl.getString("hetbz")+"',")
						.append(rsl.getString("gongf")+","+rsl.getString("changf")+","+rsl.getString("jies")+","+rsl.getString("yingk")+","+rsl.getString("zhejbz")+",")
						.append(rsl.getString("zhejje")+","+rsl.getString("zhuangt")+","+rsl.getString("yansbhb_id")+");");
				}
				
				
				bf.append(" end;");
				}
			}catch(Exception e){
				e.printStackTrace();
				throw new Exception("error");
			}
		}
		con.getUpdate(bf.toString());
		
	//	con.Close();
		return lnId;
	}
	public static String InsertIntoGSDiancjsb(JDBCcon con,String TableName,String TableID,String YF_diancjsmcb_id,String kuangfSql,String xiaosht) throws Exception{
		
		long Diancxxb_id=0;
		String sql="";
		String sql_jj="";
		String lnId="";
		double fengsjj = 0;
		StringBuffer sb=null;
		if(TableName.equals("jiesb")){
			
		  try{
			  if(xiaosht!=null && !xiaosht.equals("")){
				  String[] raw2=xiaosht.split(" ");
				  String hetbh = raw2[0];
				  sql_jj = " select min(jg.fengsjj) as fengsjj from hetb h,hetjgb jg where jg.hetb_id = h.id and h.leib=1 and h.hetbh = '"+hetbh+"'\n";
				  ResultSet rs_jj=con.getResultSet(sql_jj);
				  if(rs_jj.next()){
					  fengsjj = rs_jj.getDouble("fengsjj");
				  }
			  }
			  sql="select * from jiesb where id="+TableID;
				ResultSet rs=con.getResultSet(sql);
				
				if(rs.next()){
					double hansdj=CustomMaths.add(CustomMaths.sub(rs.getDouble("hansdj"), rs.getDouble("jiajqdj")),fengsjj);
					double hansmk=CustomMaths.mul(hansdj,rs.getDouble("jiessl"));
					double shuik= CustomMaths.div(hansmk, 1+rs.getDouble("shuil"), 2);
					double buhsmk=CustomMaths.sub(hansmk,shuik);
					double buhsdj=CustomMaths.div(hansdj, 1+rs.getDouble("shuil"), 2);
					Diancxxb_id=rs.getLong("diancxxb_id");
				
				sb=new StringBuffer("begin \n");
				lnId=MainGlobal.getNewID(Diancxxb_id);
				sb.append("insert into diancjsmkb		\n")
					.append(" (id, diancxxb_id, bianm, gongysb_id, gongysmc, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, hansdj, bukmk, hansmk, buhsmk, meikje, shuik, shuil, buhsdj, jieslx, jiesrq, hetb_id, liucztb_id, liucgzid, diancjsb_id, ranlbmjbr, ranlbmjbrq, beiz, liucgzbid,	\n")
					.append("JIAJQDJ,FENGSJJ,YUNSFSB_ID,YUNJ,YINGD,KUID,YUNS,KOUD,JIESSLCY,JIESFRL,JIHKJB_ID,MEIKXXB_ID,HETJ,MEIKDWMC,ZHILJQ,QIYF,JIESRL,JIESLF,JIESRCRL,RUZRY)\n")
					.append(" values		\n")
					.append(" ("+lnId+", "+Diancxxb_id+", '"+rs.getString("bianm")+"', "+rs.getLong("gongysb_id")+", '"+rs.getString("gongysmc")+"', '"+rs.getString("faz")+"', to_date('"+FormatDate_GS(rs.getDate("fahksrq"))+"','yyyy-MM-dd'), to_date('"+FormatDate_GS(rs.getDate("fahjzrq"))+"','yyyy-MM-dd'), '"+rs.getString("meiz")+"', '"+rs.getString("daibch")+"', '"+rs.getString("yuanshr")+"', '"+rs.getString("xianshr")+"', 	\n")
					.append(" to_date('"+FormatDate_GS(rs.getDate("yansksrq"))+"','yyyy-MM-dd'), to_date('"+FormatDate_GS(rs.getDate("yansjzrq"))+"','yyyy-MM-dd'), '"+rs.getString("yansbh")+"', '"+rs.getString("shoukdw")+"', '"+rs.getString("kaihyh")+"', '"+rs.getString("zhangh")+"', '"+rs.getString("fapbh")+"', '"+rs.getString("fukfs")+"', '"+rs.getString("duifdd")+"', "+rs.getInt("ches")+", "+rs.getDouble("jiessl")+", "+rs.getDouble("guohl")+", \n")
					.append(" "+rs.getDouble("hansdj")+", "+rs.getDouble("bukmk")+", "+hansmk+", "+buhsmk+", "+rs.getDouble("meikje")+", "+shuik+", "+rs.getDouble("shuil")+", "+buhsdj+", "+rs.getInt("jieslx")+", to_date('"+FormatDate_GS(rs.getDate("jiesrq"))+"','yyyy-MM-dd'), "+rs.getLong("hetb_id")+", "+/*rs.getLong("liucztb_id")*/0+", \n")
					.append(" "+rs.getLong("liucgzid")+", 0, '"+rs.getString("ranlbmjbr")+"', to_date('"+FormatDate_GS(rs.getDate("ranlbmjbrq"))+"','yyyy-MM-dd'), '"+rs.getString("beiz")+"', "+/*rs.getLong("liucgzid")*/0+",	\n")
					.append(" "+hansdj+","+rs.getString("FENGSJJ")).append(",").append( rs.getString("YUNSFSB_ID")+",'"+rs.getString("YUNJ")+"',"+rs.getString("YINGD")+","+rs.getString("KUID")+","+rs.getString("YUNS")+","+rs.getString("KOUD")+","+rs.getString("JIESSLCY")+","+rs.getString("JIESFRL")+","+rs.getString("JIHKJB_ID")+","+rs.getString("MEIKXXB_ID")+","+rs.getString("HETJ")+",'"+rs.getString("MEIKDWMC")+"','"+rs.getString("ZHILJQ")+"',"+rs.getString("QIYF")+","+rs.getString("JIESRL")+","+rs.getString("JIESLF")+","+rs.getString("JIESRCRL")+",'"+rs.getString("RUZRY")+"');");
		
				sb.append(kuangfSql);
				sb.append(" update jiesb set diancjsmkb_id="+lnId+" where id="+TableID+";\n");
				
				//插入 结算指标数据表
				
				String sq=" select * from jieszbsjb t  where t.jiesdid = "+TableID;
				ResultSet rsl=con.getResultSet(sq);
				
				while(rsl.next()){
					
					sb.append(" insert into jieszbsjb(id,jiesdid,zhibb_id,hetbz,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id)\n");
					sb.append(" values("+MainGlobal.getNewID(Diancxxb_id)+","+lnId+","+rsl.getString("zhibb_id")+",'"+rsl.getString("hetbz")+"',")
						.append(rsl.getString("gongf")+","+rsl.getString("changf")+","+rsl.getString("jies")+","+rsl.getString("yingk")+","+rsl.getString("zhejbz")+",")
						.append(rsl.getString("zhejje")+","+rsl.getString("zhuangt")+","+rsl.getString("yansbhb_id")+");");
				}
				sb.append("end;");
				}
		  }catch(Exception e){
			  e.printStackTrace();
			  throw new Exception("error");
		  }
		}
		if(TableName.equals("jiesyfb")){
			
			try{
				
				sql="select * from jiesyfb where id="+TableID;
				ResultSet rs=con.getResultSet(sql);
				if(rs.next()){
					
				Diancxxb_id=rs.getLong("diancxxb_id");
				
			    sb=new StringBuffer("begin \n");
				lnId=MainGlobal.getNewID(Diancxxb_id);
				sb.append("insert into diancjsyfb		\n")
					.append("  (id, diancxxb_id, bianm, gongysb_id, gongysmc, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, guotyf, dityf, jiskc, hansdj, bukyf, hansyf, buhsyf, shuik, shuil, buhsdj, jieslx, jiesrq, hetb_id, liucztb_id, liucgzid, beiz, diancjsb_id, diancjsmkb_id, ranlbmjbr, ranlbmjbrq, liucgzbid,	\n")
					.append("YUNSFSB_ID,YUNJ,YINGD,KUID,YUNS,KOUD,JIESSLCY,GUOTZF,KUANGQYF,KUANGQZF,GUOTYFJF,GUOTZFJF,GONGFSL,YANSSL,YINGK,DITZF,MEIKXXB_ID,MEIKDWMC)")
					.append(" values		\n")
					.append("  ("+lnId+", "+Diancxxb_id+", '"+rs.getString("bianm")+"', "+rs.getLong("gongysb_id")+", '"+rs.getString("gongysmc")+"', '"+rs.getString("faz")+"', to_date('"+FormatDate_GS(rs.getDate("fahksrq"))+"','yyyy-MM-dd'), to_date('"+FormatDate_GS(rs.getDate("fahjzrq"))+"','yyyy-MM-dd'), '"+rs.getString("meiz")+"', '"+rs.getString("daibch")+"', '"+rs.getString("yuanshr")+"', '"+rs.getString("xianshr")+"', 	\n")
					.append(" to_date('"+FormatDate_GS(rs.getDate("yansksrq"))+"','yyyy-MM-dd'), to_date('"+FormatDate(rs.getDate("yansjzrq"))+"','yyyy-MM-dd'), '"+rs.getString("yansbh")+"', '"+rs.getString("shoukdw")+"', '"+rs.getString("kaihyh")+"', '"+rs.getString("zhangh")+"', '"+rs.getString("fapbh")+"', '"+rs.getString("fukfs")+"', '"+rs.getString("duifdd")+"', "+rs.getInt("ches")+", "+rs.getDouble("jiessl")+", "+rs.getDouble("guohl")+", \n")
					.append(" "+rs.getDouble("guotyf")+", "+rs.getDouble("dityf")+", "+rs.getDouble("jiskc")+", "+rs.getDouble("hansdj")+", "+rs.getDouble("bukyf")+", "+rs.getDouble("hansyf")+","+rs.getDouble("buhsyf")+", "+rs.getDouble("shuik")+", "+rs.getDouble("shuil")+", "+rs.getDouble("buhsdj")+", "+rs.getInt("jieslx")+",to_date('"+FormatDate_GS(rs.getDate("jiesrq"))+"','yyyy-MM-dd'), "+rs.getLong("hetb_id")+", "+rs.getLong("liucztb_id")+", \n")
					.append(" "+rs.getLong("liucgzid")+",'"+rs.getString("beiz")+"',0,"+YF_diancjsmcb_id+",'"+rs.getString("ranlbmjbr")+"',to_date('"+FormatDate_GS(rs.getDate("ranlbmjbrq"))+"','yyyy-MM-dd'),"+rs.getLong("liucgzid")+",	\n")
					.append(rs.getString("YUNSFSB_ID")+",'"+rs.getString("YUNJ")+"',"+rs.getString("YINGD")+","+rs.getString("KUID")+","+rs.getString("YUNS")+","+rs.getString("KOUD")+","+rs.getString("JIESSLCY")+","+rs.getString("GUOTZF")+","+rs.getString("KUANGQYF")+","+rs.getString("KUANGQZF")+","+rs.getString("GUOTYFJF")+","+rs.getString("GUOTZFJF")+","+rs.getString("GONGFSL")+","+rs.getString("YANSSL")+","+rs.getString("YINGK")+","+rs.getString("DITZF")+","+rs.getString("MEIKXXB_ID")+",'"+rs.getString("MEIKDWMC")+"' );");
				
				sb.append(kuangfSql);
				sb.append(" update jiesyfb set diancjsyfb_id="+lnId+" where id="+TableID+";\n");
				
				String sq=" select * from jieszbsjb t  where t.jiesdid = "+TableID;
				ResultSet rsl=con.getResultSet(sq);
				
				while(rsl.next()){
					
					sb.append(" insert into jieszbsjb(id,jiesdid,zhibb_id,hetbz,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id)\n");
					sb.append(" values("+MainGlobal.getNewID(Diancxxb_id)+","+lnId+","+rsl.getString("zhibb_id")+",'"+rsl.getString("hetbz")+"',")
						.append(rsl.getString("gongf")+","+rsl.getString("changf")+","+rsl.getString("jies")+","+rsl.getString("yingk")+","+rsl.getString("zhejbz")+",")
						.append(rsl.getString("zhejje")+","+rsl.getString("zhuangt")+","+rsl.getString("yansbhb_id")+");");
				}
				
				sb.append("end;");
				}
				
			}catch(Exception e){
				e.printStackTrace();
				throw new Exception("error");
			}
		}
		con.getUpdate(sb.toString());
	//	con.Close();
		return lnId;
	}
	
	public static void SubmitGsDiancjsmkb(long Jiesb_id){
		
		JDBCcon con=new JDBCcon();
		String sql="";
		long Liucztb_id=0;
		long Diancxxb_id=0;
		String lnId="";
		boolean InsertFalg=false;
		try{
			
			sql="select * from jiesb where id="+Jiesb_id;
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				Liucztb_id=rs.getLong("liucztb_id");
				Diancxxb_id=rs.getLong("diancxxb_id");
			}
			
			sql="select lb.mingc from liucztb zt,leibztb lb		\n"
                 + " where zt.leibztb_id=lb.id and zt.id in		\n"
                 + " (select liuczthjid from liucdzb where liucztqqid="+Liucztb_id+" and mingc='提交')";
			ResultSet rec=con.getResultSet(sql);
			if(rec.next()){
				
				if(rec.getString("mingc").equals("公司审核")){
					
					InsertFalg=true;
				}
			}
			rec.close();
			
			if(InsertFalg){
				StringBuffer sb=new StringBuffer("begin \n");
				lnId=MainGlobal.getNewID(Diancxxb_id);
				sb.append("insert into diancjsmkb		\n")
					.append(" (id, diancxxb_id, bianm, gongysb_id, gongysmc, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, hansdj, bukmk, hansmk, buhsmk, meikje, shuik, shuil, buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id, liucgzid, diancjsb_id, ranlbmjbr, ranlbmjbrq, beiz, liucgzbid)	\n")
					.append(" values		\n")
					.append(" ("+lnId+", "+Diancxxb_id+", '"+rs.getString("bianm")+"', "+rs.getLong("gongysb_id")+", '"+rs.getString("gongysmc")+"', '"+rs.getString("faz")+"', to_date('"+FormatDate(rs.getDate("fahksrq"))+"','yyyy-MM-dd'), to_date('"+FormatDate(rs.getDate("fahjzrq"))+"','yyyy-MM-dd'), '"+rs.getString("meiz")+"', '"+rs.getString("daibch")+"', '"+rs.getString("yuanshr")+"', '"+rs.getString("xianshr")+"', 	\n")
					.append(" to_date('"+FormatDate(rs.getDate("yansksrq"))+"','yyyy-MM-dd'), to_date('"+FormatDate(rs.getDate("yansjzrq"))+"','yyyy-MM-dd'), '"+rs.getString("yansbh")+"', '"+rs.getString("shoukdw")+"', '"+rs.getString("kaihyh")+"', '"+rs.getString("zhangh")+"', '"+rs.getString("fapbh")+"', '"+rs.getString("fukfs")+"', '"+rs.getString("duifdd")+"', "+rs.getInt("ches")+", "+rs.getDouble("jiessl")+", "+rs.getDouble("guohl")+", \n")
					.append(" "+rs.getDouble("hansdj")+", "+rs.getDouble("bukmk")+", "+rs.getDouble("buhsmk")+", "+rs.getDouble("buhsmk")+", "+rs.getDouble("meikje")+", "+rs.getDouble("shuik")+", "+rs.getDouble("shuil")+", "+rs.getDouble("buhsdj")+", "+rs.getInt("jieslx")+", to_date('"+FormatDate(rs.getDate("jiesrq"))+"','yyyy-MM-dd'), to_date('"+FormatDate(rs.getDate("ruzrq"))+"','yyyy-MM-dd'), "+rs.getLong("hetb_id")+", "+rs.getLong("liucztb_id")+", \n")
					.append(" "+rs.getLong("liucgzid")+", 0, '"+rs.getString("ranlbmjbr")+"', to_date('"+FormatDate(rs.getDate("ranlbmjbrq"))+"','yyyy-MM-dd'), '"+rs.getString("beiz")+"', "+rs.getLong("liucgzid")+");	\n");
				
				
				sql="select * from jieszbsjb where jiesdid="+Jiesb_id;
				rs=con.getResultSet(sql);
				while(rs.next()){
					
					sb.append("insert into jieszbsjb	\n")
					.append("  (id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt, yansbhb_id)")
					.append("values		\n")
					.append("(getnewid("+Diancxxb_id+"), "+lnId+", "+rs.getLong("zhibb_id")+", "+rs.getDouble("hetbz")+", "+rs.getDouble("gongf")+", \n")
					.append(""+rs.getDouble("changf")+", "+rs.getDouble("jies")+", "+rs.getDouble("yingk")+", "+rs.getDouble("zhejbz")+", "+rs.getDouble("zhejje")+", \n")
					.append(""+rs.getInt("zhuangt")+", "+rs.getLong("yansbhb_id")+");");
				}
				
				sb.append("end;");
				con.getInsert(sb.toString());
			}
			
			rs.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	} 
	
	public static void SubmitGsDiancjsyfb(long jiesyfb_id){
		
		JDBCcon con=new JDBCcon();
		String sql="";
		long Liucztb_id=0;
		long Diancxxb_id=0;
		String lnId="";
		boolean InsertFalg=false;
		try{
			
			sql="select * from diancjsyfb where id="+jiesyfb_id;
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				Liucztb_id=rs.getLong("liucztb_id");
				Diancxxb_id=rs.getLong("diancxxb_id");
			}
			
			sql="select lb.mingc from liucztb zt,leibztb lb		\n"
                 + " where zt.leibztb_id=lb.id and zt.id in		\n"
                 + " (select liuczthjid from liucdzb where liucztqqid="+Liucztb_id+" and mingc='提交')";
			ResultSet rec=con.getResultSet(sql);
			if(rec.next()){
				
				if(rec.getString("mingc").equals("公司审核")){
					
					InsertFalg=true;
				}
			}
			rec.close();
			
			if(InsertFalg){
				StringBuffer sb=new StringBuffer("begin \n");
				lnId=MainGlobal.getNewID(Diancxxb_id);
				sb.append("insert into diancjsyfb		\n")
					.append("  (id, diancxxb_id, bianm, gongysb_id, gongysmc, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, guotyf, dityf, jiskc, hansdj, bukyf, hansyf, buhsyf, shuik, shuil, buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id, liucgzid, beiz, diancjsb_id, diancjsmkb_id, ranlbmjbr, ranlbmjbrq, liucgzbid)	\n")
					.append(" values		\n")
					.append("  ("+lnId+", "+Diancxxb_id+", '"+rs.getString("bianm")+"', "+rs.getLong("gongysb_id")+", '"+rs.getString("gongysmc")+"', '"+rs.getString("faz")+"', to_date('"+FormatDate(rs.getDate("fahksrq"))+"','yyyy-MM-dd'), to_date('"+FormatDate(rs.getDate("fahjzrq"))+"','yyyy-MM-dd'), '"+rs.getString("meiz")+"', '"+rs.getString("daibch")+"', '"+rs.getString("yuanshr")+"', '"+rs.getString("xianshr")+"', 	\n")
					.append(" to_date('"+FormatDate(rs.getDate("yansksrq"))+"','yyyy-MM-dd'), to_date('"+FormatDate(rs.getDate("yansjzrq"))+"','yyyy-MM-dd'), '"+rs.getString("yansbh")+"', '"+rs.getString("shoukdw")+"', '"+rs.getString("kaihyh")+"', '"+rs.getString("zhangh")+"', '"+rs.getString("fapbh")+"', '"+rs.getString("fukfs")+"', '"+rs.getString("duifdd")+"', "+rs.getInt("ches")+", "+rs.getDouble("jiessl")+", "+rs.getDouble("guohl")+", \n")
					.append(" "+rs.getDouble("guotyf")+", "+rs.getDouble("dityf")+", "+rs.getDouble("jiskc")+", "+rs.getDouble("hansdj")+", "+rs.getDouble("bukyf")+", "+rs.getDouble("hansyf")+", "+rs.getDouble("shuik")+", "+rs.getDouble("shuil")+", "+rs.getDouble("buhsdj")+", "+rs.getInt("jieslx")+",to_date('"+FormatDate(rs.getDate("jiesrq"))+"','yyyy-MM-dd'), to_date('"+FormatDate(rs.getDate("ruzrq"))+"','yyyy-MM-dd'), "+rs.getLong("hetb_id")+", "+rs.getLong("liucztb_id")+", \n")
					.append(" "+rs.getLong("liucgzid")+",'"+rs.getString("beiz")+"',0,0,'"+rs.getString("ranlbmjbr")+"',to_date('"+FormatDate(rs.getDate("ranlbmjbrq"))+"','yyyy-MM-dd'),"+rs.getLong("liucgzid")+");	\n");
				
				sb.append("end;");
				con.getInsert(sb.toString());
			}
			
			rs.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	} 
	
	public static boolean CheckHetshzt(long Hetb_id){
//		2008-10-17，在结算时如果合同没有审核，可以进行试结算，但不能进行结算单保存
//		这个方法是在结算时判断合同的审核状态，如果没有审核就不能保存
		JDBCcon con=new JDBCcon();
		boolean Flag=false;
		try{
			
			String sql="select id from hetb where id="+Hetb_id+" and liucztb_id=1 	\n" 
					+ " union	\n"
					+ " select id from hetys where id="+Hetb_id+" and liucztb_id=1";
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				Flag=true;
			}
			rs.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		
		return Flag;
	}
	
	public static boolean CheckHetshzt_Shih(long Hetb_id) throws SQLException{
		
		JDBCcon con=new JDBCcon();
		boolean Flag=false;
		
		String sql="select id from shihhtb where id="+Hetb_id+" and shenhzt=1";
		ResultSet rs=con.getResultSet(sql);
		if(rs.next()){
			
			Flag=true;
		}
		rs.close();
		con.Close();
		return Flag;
	}
	
	public static String getHetbh(long hetb_Id) {
		// TODO 自动生成方法存根
		JDBCcon con =new JDBCcon();
		String hetbh="";
		
		try{
			
			String sql="select hetbh from hetb where id="+hetb_Id+"		\n"
			+ " union		\n"
			+ " select hetbh from hetys where id="+hetb_Id+"";		
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				hetbh=rs.getString("hetbh");
			}
			rs.close();
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		return hetbh;
	}
	
	public static long getProperId(IPropertySelectionModel _selectModel, String value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();

		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
					value)) {
				return ((IDropDownBean) _selectModel.getOption(i)).getId();
			}
		}
		return -1;
	}
	
	public static String getJieslxAndJiesbh(String strJiesb_id) {
		// TODO 自动生成方法存根
//		得到结算单类型和结算单编号
		JDBCcon con=new JDBCcon();
		String strResult="";
		try{
			
			String sql="(select '"+Locale.liangpjs_feiylbb+"' as leix,bianm from jiesb where id="+strJiesb_id+" and jieslx="+Locale.liangpjs_feiylbb_id+")	\n"
					+ " union 	\n"
					+ "(select '"+Locale.meikjs_feiylbb+"' as leix,bianm from jiesb where id="+strJiesb_id+")	\n"
					+ "	union 	\n"
					+ "(select decode(jieslx,3,'"+Locale.guotyf_feiylbb+"',4,'"+
						Locale.dityf_feiylbb+"',5,'"+Locale.kuangqyf_feiylbb+"') as leix,bianm from jiesyfb where id="+strJiesb_id+")";
			
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				strResult=rs.getString("bianm")+"的"+rs.getString("leix");
			}
			rs.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return strResult;
	}
	
	public static String getTiaojljf(String Tiaoj,String Type,String DeafultValue){
//		返回条件链接符，处理的条件如下“大于等于、大于、小于、小于等于、区间、等于”
//		参数说明：Tiaoj为上述条件
//				Type为上限或下限
//				DeafultValue默认值
		String Lianjf="";
		Lianjf=DeafultValue;
		
		if(Type.equals("下限")){
			
			if(Tiaoj.equals("大于等于")){
				
				Lianjf=">=";
			}else if(Tiaoj.equals("大于")){
				
				Lianjf=">";
			}else if(Tiaoj.equals("小于")
					||Tiaoj.equals("小于等于")
					||Tiaoj.equals("区间")){
				
				Lianjf=">=";
			}else if(Tiaoj.equals("等于")){
				
				Lianjf="=";
			}
		}else if(Type.equals("上限")){
			
			if(Tiaoj.equals("大于等于")
					||Tiaoj.equals("大于")){
				
				Lianjf="<=";
			}else if(Tiaoj.equals("小于")){
				
				Lianjf="<";
			}else if(Tiaoj.equals("小于等于")
					||Tiaoj.equals("区间")
					||Tiaoj.equals("等于")){
					
				Lianjf="<=";
			}
		}
		
		return Lianjf;
	}
	
	public static String nvlStr(String strValue){
		if (strValue==null) {
			return "";
		}else if(strValue.equals("null")){
			return "";
		}
		
		return strValue;
	}

	public static double[] Zengkktz(Balances_variable bsv) {
		// TODO 自动生成方法存根
//		2009-5-22 用于增扣款调整用，处理的业务问题为，增扣款适用范围为超出部分的数据,
//		目前只处理结算数量指标，超出部分和全部
		StringBuffer sb=new StringBuffer();
		sb=bsv.getMeikzkksyfw();
		String Zhib[]=null;		//第一个得到所有增扣款需要调整的指标
		String Zhib_Zkkcl[]=null;	//第二个得到每个指标的增扣款适用范围	1、为超出部分
		double Zengkktzsj[]=null;
//		1、超出部分对应的折单价
//		2、超出部分折金额
		
		if(sb.length()>0){
			
			Zhib=sb.toString().split(";");
			
			for(int i=0;i<Zhib.length;i++){
				
				Zhib_Zkkcl=Zhib[i].split(",");
				
				if(Zhib_Zkkcl[1].equals("1")){	
//					1、为超出部分享受折价
//					处理逻辑：将对应指标的加价从总hansmj中减去，
					if(Zhib[0].equals(Locale.jiessl_zhibb)){
//						目前只对数量的超出部分做处理
						Zengkktzsj=new double[2];
						Zengkktzsj[0]=bsv.getShul_zdj();
						Zengkktzsj[1]=(double)CustomMaths.Round_new(bsv.getShul_yk()*bsv.getShul_zdj(),2);
					}
				}
			}
		}
		
		return Zengkktzsj;
	}
	
	public static long getGongysb_id(String selids,long diancxxb_id,
			long gongysb_id,long hetb_id,double jieskdl){
//		此函数为处理系统中取结算设置方案时，当供应商id为0时，根据用户所选发货重新得到供应商id的方法
		long m_gongysb_id=0;
		
		if(gongysb_id>0){
//			已经初始化供应商了
			m_gongysb_id=gongysb_id;
		}else{
//			没有初始化供应商
			Balances bal = new Balances();
			Balances_variable bsv = new Balances_variable();
			
			try {
				bsv=bal.getBaseInfo(selids, diancxxb_id, gongysb_id , hetb_id, "否", ""
						, jieskdl);
				
				m_gongysb_id = bsv.getGongysb_Id();
				
			} catch (NumberFormatException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			} catch (Exception e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		return m_gongysb_id;
	}
	
	public static String Sub_danw(String ZhibAndDw){
//		该方法用来去除指标单位用
		String Zhib="";
		if(ZhibAndDw.indexOf("(")>-1){
//			单位是用小括号括起来的
			
			Zhib=ZhibAndDw.substring(0,ZhibAndDw.indexOf("(")).trim();
		}
		
		return Zhib;
	}
	
	public static boolean getHet_condition(String Hetzb,String Jieszb){
//		该方法用于判定合同价格中或合同增扣款中除上、下限意外其他条件是使用，如品种等
		boolean Flag = false;
		if(Hetzb==null||Hetzb.trim().equals("")||Hetzb.trim().equals("0")){
//			如果合同指标为空，那么认为此条件返回值为真
			Flag = true;
		}else{
			
			if(Hetzb.trim().equals(Jieszb.trim())){
				
				Flag = true;
			}
		}
		
		return Flag;
	}

	public static boolean getHet_condition(String strZhibbm, double dblJieszb, 
			double dblZhibxx, double dblZhibsx) {
		// TODO 自动生成方法存根
//		用于合同条款中的参照项目时使用
		boolean Flag = false;
		if(strZhibbm.trim().equals("")){
			
			Flag = true;
		}else{
			
			if(dblJieszb>=dblZhibxx&&dblJieszb<=(dblZhibsx==0?1e308:dblZhibsx)){
				
				Flag = true;
			}
		}
		
		return Flag;
	}

	public static String getLic_id(String Lie_ids) {
		// TODO 自动生成方法存根
//		为了解决同一对发到站之间不同煤矿有不同总里程的问题
		JDBCcon con = new JDBCcon();
		String strLic_id="";
		try {
			
				String sql=
					"select distinct lc.id\n" +
					"       from fahb f,meikxxb m,licb lc\n" + 
					"            where f.diancxxb_id = lc.diancxxb_id(+)\n" + 
					"                  and f.faz_id = lc.faz_id(+)\n" + 
					"                  and f.daoz_id = lc.daoz_id(+)\n" + 
					"                  and f.meikxxb_id = m.id\n" + 
					"                  and trim(lc.beiz) = trim(m.mingc)\n" + 
					"                  and f.lie_id in ("+Lie_ids+")";
				
				ResultSet rs = con.getResultSet(sql);
		
				if(rs.next()){
					
					strLic_id = rs.getString("id");
				}
				rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return strLic_id;
	}
	
	public static String getFahb_id_FromLie_id(String SQL,String lie_id){
//		在结算中使用，根据条件和列id，选择出发货id
		JDBCcon con = new JDBCcon();
		StringBuffer fahb_id=new StringBuffer("");
		SQL=SQL.replaceAll("f.lie_id", "f.lie_id,f.id");
		SQL=SQL.substring(0,SQL.lastIndexOf(",f.id"))+SQL.substring(SQL.lastIndexOf("f.id")+4);
		SQL=" select id from (\n "+SQL+"\n) where lie_id in ("+lie_id+")";
		
		ResultSetList rsl = con.getResultSetList(SQL);
		if(rsl.getRows()>0){
			
			while(rsl.next()){
				
				fahb_id.append(rsl.getString("id")).append(",");
			}
			fahb_id.deleteCharAt(fahb_id.length()-1);
		}
		
		return fahb_id.toString();
	}
	
	public static double getZengkkxx_Value(String implementedzb,String zhib,
			double Dblimplementedzbsx,double zhibxx){
//		形参：已用过的指标，当前的指标，已用过的指标上限，当前的指标下限
		
//		函数目的：
//			在增扣款中有这样的情况，当1个指标的结算值，同时满足多个增扣款条款时，
//				（例如：vdaf 结算指标为13，有增扣款条件为小于15...，又有增扣款条件为小于20...）
//				系统要自动找到与这个结算指标最贴近的一个增扣款条款执行，为了满足此逻辑，要有一下操作
//				1、将增扣款按照 指标、条件、下限、上限作排序		
//				2、此时同一指标就要用上一个增扣款条款中的上限，作为本条增扣款中的下限使用。
		
//		函数逻辑：
//			1、判断zhibxx是否为0
//				2、判断implementedzb是否和zhib相等（即为同一指标）
//					返回Dblimplementedzbsx
//			1(false)、返回zhibxx
//				2(false)、返回zhibxx
		double RsultValue=zhibxx;
		if(zhibxx==0){
			
			if(implementedzb.equals(zhib)&&!implementedzb.equals("")){
				
				RsultValue = Dblimplementedzbsx;
			}
		}
		
		return RsultValue;
	}
	
	public static Interpreter CountYfjsfa(long meikxxb_id,long diancxxb_id,long faz_id,
				long daoz_id,long yunsjgfab_id,Interpreter bsh){
	//	计算运费方案的特殊运费
	//	逻辑：
	//		1、先找出licb中该矿对应的总里程信息，即多个里程类型的里程相加 (判断条件：diancxxb_id、faz_id、daoz_id、meikxxb_id)。
	//		2、通过得到的总里程，去查找yunsjgfab中的里程所对应的里程类型、价格（判断条件：lic、yunsjgfab_id）。
	//		3、再通过里程类型找到相应的价格×里程表中对应的里程后相加即可得到运费单价（判断条件：liclxb_id）
		JDBCcon con = new JDBCcon();
		try{
			String sql =
					"select sum(licjg * zhi) as yunfjg \n" +
					"  from (select liclxb_id, licjg\n" + 
					"          from yunsjgmxb\n" + 
					"         where yunsjgfab_id = "+yunsjgfab_id+"\n" + 
					"           and lic in (select sum(zhi) as zonglic\n" + 
					"                         from licb\n" + 
					"                        where meikxxb_id = "+meikxxb_id+"\n" + 
					"                          and diancxxb_id = "+diancxxb_id+"\n" + 
					"                          and faz_id = "+faz_id+"\n" + 
					"                          and daoz_id = "+daoz_id+")) a,\n" + 
					"\n" + 
					"       (select liclxb_id, zhi\n" + 
					"          from licb\n" + 
					"         where meikxxb_id = "+meikxxb_id+"\n" + 
					"           and diancxxb_id = "+diancxxb_id+"\n" + 
					"           and faz_id = "+faz_id+"\n" + 
					"           and daoz_id = "+daoz_id+") b\n" + 
					"\n" + 
					" where a.liclxb_id = b.liclxb_id";

			ResultSet rs = con.getResultSet(sql);
			while(rs.next()){
				
				bsh.set("合同运价", rs.getDouble("yunfjg"));
			}
			
			rs.close();
		}catch(SQLException s){
			
			s.printStackTrace();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		
		return bsh;
	}
	
	public static double reCoundYk(String hetbz,double jiesbz,double yingk){
		
//		函数作用：在合同中，通常有两个热量单位 kcal/kg,mj/kg，但这两个单位在互相转化下由于小数问题会产生误差
//			在结算的计算过程中我们为了准确通常采用与合同中单位一致的单位来计算
//			在结算显示的时候我们为了整齐划一统一采用了 kcal/kg为显示单位，此时如果按 mj/kg结算的单据会产生盈亏上的误差
//			此函数为了消除这种误差，在保存数据之前将盈亏字段进行重算，即jiessl-hetbz。如果合同标准是一个区间，我们认为盈亏是零
//		yingk=0;
		if(hetbz.indexOf("-")==-1&&!hetbz.equals("")){
//			说明合同标准中不存在“-”连接符可以重新计算盈亏
//			盈亏=结算标准-合同标准
			yingk=CustomMaths.sub(jiesbz, Double.parseDouble(hetbz));
		}
		
		return yingk;
	}
	
	public static StringBuffer InsertDanpcjsmkb(Balances_variable bsv, IPropertySelectionModel ZhibModel){
		
		StringBuffer sb = new StringBuffer("");
		
		if(!bsv.getShul_ht().equals("")){
			
			sb.append(Shoumjsdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Shoumjsdcz.getProperId(ZhibModel, Locale.jiessl_zhibb), 
						bsv.getShul_ht(), bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getYingksl(), bsv.getShul_zdj(), bsv.getShul_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl()));
			
//			sql.append("insert into danpcjsmxb (id, xuh, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, gongfsl, yanssl, jiessl, koud, kous, kouz, ches, jingz, koud_js, yuns, jiesslcy, jiesdj, jiakhj, jiaksk, jiashj, biaomdj, buhsbmdj, leib)	\n");
//			sql.append("	values(		\n");
//			sql.append("getnewid("+bsv.getDiancxxb_id()+"),"+bsv.getXuh()+","+bsv.getMeikjsb_id()+","+Shoumjsdcz.getProperId(getZhibModel(), Locale.jiessl_zhibb)+",'"+bsv.getShul_ht()+"',"+bsv.getGongfsl()+","+bsv.getYanssl()+","+bsv.getJiessl()+",")
//				.append(bsv.getYingksl()+","+bsv.getShul_zdj()+","+bsv.getShul_zje()+","+bsv.getGongfsl()+","+bsv.getYanssl()+","+bsv.getJiessl()+","+bsv.getKoud()+","+bsv.getKous()+","+bsv.getKouz()+","+bsv.getChes()+","+bsv.getJingz()+","+bsv.getKoud_js()+","+bsv.getYuns()+",")
//				.append(bsv.getJiesslcy()+","+bsv.getShulzjbz()+","+bsv.getJiakhj()+","+bsv.getJiaksk()+","+bsv.getJiashj()+",0,0,1);	\n");
		}
		
		if(!bsv.getQnetar_ht().equals("")){
			
			sb.append(Shoumjsdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Shoumjsdcz.getProperId(ZhibModel, Locale.Qnetar_zhibb), 
						MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_ht(),"-",0), MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_kf(),0,bsv.getMj_to_kcal_xsclfs()), MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_cf(),0,bsv.getMj_to_kcal_xsclfs()),
						MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_js(),0,bsv.getMj_to_kcal_xsclfs()), 
						reCoundYk(MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_ht(),"-",0),MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_js(),0,bsv.getMj_to_kcal_xsclfs()),bsv.getQnetar_yk()), bsv.getQnetar_zdj(), bsv.getQnetar_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl()));
		
//			sql.append("insert into danpcjsmxb (id, xuh, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, gongfsl, yanssl, jiessl, koud, kous, kouz, ches, jingz, koud_js, yuns, jiesslcy, jiesdj, jiakhj, jiaksk, jiashj, biaomdj, buhsbmdj, leib)	\n");
//			sql.append("	values(		\n");
//			sql.append("getnewid("+bsv.getDiancxxb_id()+"),"+bsv.getXuh()+","+bsv.getMeikjsb_id()+","+Shoumjsdcz.getProperId(getZhibModel(), Locale.Qnetar_zhibb)+",'"+bsv.getQnetar_ht()+"',"+MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_kf(), 0)+","+MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_cf(), 0)
//					+","+MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_js(), 0)+",")
//				.append(bsv.getQnetar_yk()+","+bsv.getQnetar_zdj()+","+bsv.getQnetar_zje()+","+bsv.getGongfsl()+","+bsv.getYanssl()+","+bsv.getJiessl()+","+bsv.getKoud()+","+bsv.getKous()+","+bsv.getKouz()+","+bsv.getChes()+","+bsv.getJingz()+","+bsv.getKoud_js()+","+bsv.getYuns()+",")
//				.append(bsv.getJiesslcy()+","+bsv.getShulzjbz()+","+bsv.getJiakhj()+","+bsv.getJiaksk()+","+bsv.getJiashj()+",0,0,1);	\n");
		}
		
		if(!bsv.getAd_ht().equals("")){
			
			sb.append(Shoumjsdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Shoumjsdcz.getProperId(ZhibModel, Locale.Ad_zhibb), 
					bsv.getAd_ht(), bsv.getAd_kf(), bsv.getAd_cf(), bsv.getAd_js(), 
					reCoundYk(bsv.getAd_ht(),bsv.getAd_js(),bsv.getAd_yk()), bsv.getAd_zdj(), bsv.getAd_zje(), 
					bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
					bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
					bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
					bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
					bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
					bsv.getFaz_Id(), bsv.getChaokdl()));
			
//			sql.append("insert into danpcjsmxb (id, xuh, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, gongfsl, yanssl, jiessl, koud, kous, kouz, ches, jingz, koud_js, yuns, jiesslcy, jiesdj, jiakhj, jiaksk, jiashj, biaomdj, buhsbmdj, leib)	\n");
//			sql.append("	values(		\n");
//			sql.append("getnewid("+bsv.getDiancxxb_id()+"),"+bsv.getXuh()+","+bsv.getMeikjsb_id()+","+Shoumjsdcz.getProperId(getZhibModel(), Locale.Ad_zhibb)+",'"+bsv.getAd_ht()+"',"+bsv.getAd_kf()+","+bsv.getAd_cf()+","+bsv.getAd_js()+",")
//				.append(bsv.getAd_yk()+","+bsv.getAd_zdj()+","+bsv.getAd_zje()+","+bsv.getGongfsl()+","+bsv.getYanssl()+","+bsv.getJiessl()+","+bsv.getKoud()+","+bsv.getKous()+","+bsv.getKouz()+","+bsv.getChes()+","+bsv.getJingz()+","+bsv.getKoud_js()+","+bsv.getYuns()+",")
//				.append(bsv.getJiesslcy()+","+bsv.getShulzjbz()+","+bsv.getJiakhj()+","+bsv.getJiaksk()+","+bsv.getJiashj()+",0,0,1);	\n");
		}
		
		if(!bsv.getStd_ht().equals("")){
			
			
			sb.append(Shoumjsdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Shoumjsdcz.getProperId(ZhibModel, Locale.Std_zhibb), 
					bsv.getStd_ht(), bsv.getStd_kf(), bsv.getStd_cf(), bsv.getStd_js(), 
					reCoundYk(bsv.getStd_ht(),bsv.getStd_js(),bsv.getStd_yk()), bsv.getStd_zdj(), bsv.getStd_zje(), 
					bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
					bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
					bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
					bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
					bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
					bsv.getFaz_Id(), bsv.getChaokdl()));
			
//			sql.append("insert into danpcjsmxb (id, xuh, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, gongfsl, yanssl, jiessl, koud, kous, kouz, ches, jingz, koud_js, yuns, jiesslcy, jiesdj, jiakhj, jiaksk, jiashj, biaomdj, buhsbmdj, leib)	\n");
//			sql.append("	values(		\n");
//			sql.append("getnewid("+bsv.getDiancxxb_id()+"),"+bsv.getXuh()+","+bsv.getMeikjsb_id()+","+Shoumjsdcz.getProperId(getZhibModel(), Locale.Std_zhibb)+",'"+bsv.getStd_ht()+"',"+bsv.getStd_kf()+","+bsv.getStd_cf()+","+bsv.getStd_js()+",")
//				.append(bsv.getStd_yk()+","+bsv.getStd_zdj()+","+bsv.getStd_zje()+","+bsv.getGongfsl()+","+bsv.getYanssl()+","+bsv.getJiessl()+","+bsv.getKoud()+","+bsv.getKous()+","+bsv.getKouz()+","+bsv.getChes()+","+bsv.getJingz()+","+bsv.getKoud_js()+","+bsv.getYuns()+",")
//				.append(bsv.getJiesslcy()+","+bsv.getShulzjbz()+","+bsv.getJiakhj()+","+bsv.getJiaksk()+","+bsv.getJiashj()+",0,0,1);	\n");
		}
		
		if(!bsv.getVdaf_ht().equals("")){
			
			sb.append(Shoumjsdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Shoumjsdcz.getProperId(ZhibModel, Locale.Vdaf_zhibb), 
					bsv.getVdaf_ht(), bsv.getVdaf_kf(), bsv.getVdaf_cf(), bsv.getVdaf_js(), 
					reCoundYk(bsv.getVdaf_ht(),bsv.getVdaf_js(),bsv.getVdaf_yk()), bsv.getVdaf_zdj(), bsv.getVdaf_zje(), 
					bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
					bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
					bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
					bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
					bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
					bsv.getFaz_Id(), bsv.getChaokdl()));
			
//			sql.append("insert into danpcjsmxb (id, xuh, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, gongfsl, yanssl, jiessl, koud, kous, kouz, ches, jingz, koud_js, yuns, jiesslcy, jiesdj, jiakhj, jiaksk, jiashj, biaomdj, buhsbmdj, leib)	\n");
//			sql.append("	values(		\n");
//			sql.append("getnewid("+bsv.getDiancxxb_id()+"),"+bsv.getXuh()+","+bsv.getMeikjsb_id()+","+Shoumjsdcz.getProperId(getZhibModel(), Locale.Vdaf_zhibb)+",'"+bsv.getVdaf_ht()+"',"+bsv.getVdaf_kf()+","+bsv.getVdaf_cf()+","+bsv.getVdaf_js()+",")
//				.append(bsv.getVdaf_yk()+","+bsv.getVdaf_zdj()+","+bsv.getVdaf_zje()+","+bsv.getGongfsl()+","+bsv.getYanssl()+","+bsv.getJiessl()+","+bsv.getKoud()+","+bsv.getKous()+","+bsv.getKouz()+","+bsv.getChes()+","+bsv.getJingz()+","+bsv.getKoud_js()+","+bsv.getYuns()+",")
//				.append(bsv.getJiesslcy()+","+bsv.getShulzjbz()+","+bsv.getJiakhj()+","+bsv.getJiaksk()+","+bsv.getJiashj()+",0,0,1);	\n");
		}
		
		if(!bsv.getMt_ht().equals("")){
			
			sb.append(Shoumjsdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Shoumjsdcz.getProperId(ZhibModel, Locale.Mt_zhibb), 
					bsv.getMt_ht(), bsv.getMt_kf(), bsv.getMt_cf(), bsv.getMt_js(), 
					reCoundYk(bsv.getMt_ht(),bsv.getMt_js(),bsv.getMt_yk()), bsv.getMt_zdj(), bsv.getMt_zje(), 
					bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
					bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
					bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
					bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
					bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
					bsv.getFaz_Id(), bsv.getChaokdl()));
			
			
//			sql.append("insert into danpcjsmxb (id, xuh, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, gongfsl, yanssl, jiessl, koud, kous, kouz, ches, jingz, koud_js, yuns, jiesslcy, jiesdj, jiakhj, jiaksk, jiashj, biaomdj, buhsbmdj, leib)	\n");
//			sql.append("	values(		\n");
//			sql.append("getnewid("+bsv.getDiancxxb_id()+"),"+bsv.getXuh()+","+bsv.getMeikjsb_id()+","+Shoumjsdcz.getProperId(getZhibModel(), Locale.Mt_zhibb)+",'"+bsv.getMt_ht()+"',"+bsv.getMt_kf()+","+bsv.getMt_cf()+","+bsv.getMt_js()+",")
//				.append(bsv.getMt_yk()+","+bsv.getMt_zdj()+","+bsv.getMt_zje()+","+bsv.getGongfsl()+","+bsv.getYanssl()+","+bsv.getJiessl()+","+bsv.getKoud()+","+bsv.getKous()+","+bsv.getKouz()+","+bsv.getChes()+","+bsv.getJingz()+","+bsv.getKoud_js()+","+bsv.getYuns()+",")
//				.append(bsv.getJiesslcy()+","+bsv.getShulzjbz()+","+bsv.getJiakhj()+","+bsv.getJiaksk()+","+bsv.getJiashj()+",0,0,1);	\n");
		}
		
		if(!bsv.getQgrad_ht().equals("")){
			
			sb.append(Shoumjsdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Shoumjsdcz.getProperId(ZhibModel, Locale.Qgrad_zhibb), 
					MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_ht(),"-",0), MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_kf(),0,bsv.getMj_to_kcal_xsclfs()), MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_cf(),0,bsv.getMj_to_kcal_xsclfs()), 
					MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_js(),0,bsv.getMj_to_kcal_xsclfs()), 
					reCoundYk(MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_ht(),"-",0),MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_js(),0,bsv.getMj_to_kcal_xsclfs()),bsv.getQgrad_yk()), bsv.getQgrad_zdj(), bsv.getQgrad_zje(), 
					bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
					bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
					bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
					bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
					bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
					bsv.getFaz_Id(), bsv.getChaokdl()));
			
			
//			sql.append("insert into danpcjsmxb (id, xuh, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, gongfsl, yanssl, jiessl, koud, kous, kouz, ches, jingz, koud_js, yuns, jiesslcy, jiesdj, jiakhj, jiaksk, jiashj, biaomdj, buhsbmdj, leib)	\n");
//			sql.append("	values(		\n");
//			sql.append("getnewid("+bsv.getDiancxxb_id()+"),"+bsv.getXuh()+","+bsv.getMeikjsb_id()+","+Shoumjsdcz.getProperId(getZhibModel(), Locale.Qgrad_zhibb)+",'"+bsv.getQgrad_ht()+"',"+MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_kf(),0)+","+MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_cf(), 0)+","+MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_js(), 0)+",")
//				.append(bsv.getQgrad_yk()+","+bsv.getQgrad_zdj()+","+bsv.getQgrad_zje()+","+bsv.getGongfsl()+","+bsv.getYanssl()+","+bsv.getJiessl()+","+bsv.getKoud()+","+bsv.getKous()+","+bsv.getKouz()+","+bsv.getChes()+","+bsv.getJingz()+","+bsv.getKoud_js()+","+bsv.getYuns()+",")
//				.append(bsv.getJiesslcy()+","+bsv.getShulzjbz()+","+bsv.getJiakhj()+","+bsv.getJiaksk()+","+bsv.getJiashj()+",0,0,1);	\n");
		}
		
		if(!bsv.getQbad_ht().equals("")){
			
			sb.append(Shoumjsdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Shoumjsdcz.getProperId(ZhibModel, Locale.Qbad_zhibb), 
					MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_ht(),"-",0), MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_kf(),0,bsv.getMj_to_kcal_xsclfs()), MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_cf(),0,bsv.getMj_to_kcal_xsclfs()), 
					MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_js(),0,bsv.getMj_to_kcal_xsclfs()), 
					reCoundYk(MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_ht(),"-",0),MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_js(),0,bsv.getMj_to_kcal_xsclfs()),bsv.getQbad_yk()), bsv.getQbad_zdj(), bsv.getQbad_zje(), 
					bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
					bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
					bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
					bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
					bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
					bsv.getFaz_Id(), bsv.getChaokdl()));
			
//			sql.append("insert into danpcjsmxb (id, xuh, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, gongfsl, yanssl, jiessl, koud, kous, kouz, ches, jingz, koud_js, yuns, jiesslcy, jiesdj, jiakhj, jiaksk, jiashj, biaomdj, buhsbmdj, leib)	\n");
//			sql.append("	values(		\n");
//			sql.append("getnewid("+bsv.getDiancxxb_id()+"),"+bsv.getXuh()+","+bsv.getMeikjsb_id()+","+Shoumjsdcz.getProperId(getZhibModel(), Locale.Qbad_zhibb)+",'"+bsv.getQbad_ht()+"',"+MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_kf(), 0)+","+MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_cf(), 0)
//					+","+MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_js(), 0)+",")
//				.append(bsv.getQbad_yk()+","+bsv.getQbad_zdj()+","+bsv.getQbad_zje()+","+bsv.getGongfsl()+","+bsv.getYanssl()+","+bsv.getJiessl()+","+bsv.getKoud()+","+bsv.getKous()+","+bsv.getKouz()+","+bsv.getChes()+","+bsv.getJingz()+","+bsv.getKoud_js()+","+bsv.getYuns()+",")
//				.append(bsv.getJiesslcy()+","+bsv.getShulzjbz()+","+bsv.getJiakhj()+","+bsv.getJiaksk()+","+bsv.getJiashj()+",0,0,1);	\n");
		}
		
		if(!bsv.getHad_ht().equals("")){
			
			sb.append(Shoumjsdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Shoumjsdcz.getProperId(ZhibModel, Locale.Had_zhibb), 
					bsv.getHad_ht(), bsv.getHad_kf(), bsv.getHad_cf(), bsv.getHad_js(), 
					reCoundYk(bsv.getHad_ht(),bsv.getHad_js(),bsv.getHad_yk()), bsv.getHad_zdj(), bsv.getHad_zje(), 
					bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
					bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
					bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
					bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
					bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
					bsv.getFaz_Id(), bsv.getChaokdl()));
			
//			sql.append("insert into danpcjsmxb (id, xuh, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, gongfsl, yanssl, jiessl, koud, kous, kouz, ches, jingz, koud_js, yuns, jiesslcy, jiesdj, jiakhj, jiaksk, jiashj, biaomdj, buhsbmdj, leib)	\n");
//			sql.append("	values(		\n");
//			sql.append("getnewid("+bsv.getDiancxxb_id()+"),"+bsv.getXuh()+","+bsv.getMeikjsb_id()+","+Shoumjsdcz.getProperId(getZhibModel(), Locale.Had_zhibb)+",'"+bsv.getHad_ht()+"',"+bsv.getHad_kf()+","+bsv.getHad_cf()+","+bsv.getHad_js()+",")
//				.append(bsv.getHad_yk()+","+bsv.getHad_zdj()+","+bsv.getHad_zje()+","+bsv.getGongfsl()+","+bsv.getYanssl()+","+bsv.getJiessl()+","+bsv.getKoud()+","+bsv.getKous()+","+bsv.getKouz()+","+bsv.getChes()+","+bsv.getJingz()+","+bsv.getKoud_js()+","+bsv.getYuns()+",")
//				.append(bsv.getJiesslcy()+","+bsv.getShulzjbz()+","+bsv.getJiakhj()+","+bsv.getJiaksk()+","+bsv.getJiashj()+",0,0,1);	\n");
		}
		
		if(!bsv.getStad_ht().equals("")){
			
			sb.append(Shoumjsdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Shoumjsdcz.getProperId(ZhibModel, Locale.Stad_zhibb), 
					bsv.getStad_ht(), bsv.getStad_kf(), bsv.getStad_cf(), bsv.getStad_js(), 
					reCoundYk(bsv.getStad_ht(),bsv.getStad_js(),bsv.getStad_yk()), bsv.getStad_zdj(), bsv.getStad_zje(), 
					bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
					bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
					bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
					bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
					bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
					bsv.getFaz_Id(), bsv.getChaokdl()));
			
//			sql.append("insert into danpcjsmxb (id, xuh, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, gongfsl, yanssl, jiessl, koud, kous, kouz, ches, jingz, koud_js, yuns, jiesslcy, jiesdj, jiakhj, jiaksk, jiashj, biaomdj, buhsbmdj, leib)	\n");
//			sql.append("	values(		\n");
//			sql.append("getnewid("+bsv.getDiancxxb_id()+"),"+bsv.getXuh()+","+bsv.getMeikjsb_id()+","+Shoumjsdcz.getProperId(getZhibModel(), Locale.Stad_zhibb)+",'"+bsv.getStad_ht()+"',"+bsv.getStad_kf()+","+bsv.getStad_cf()+","+bsv.getStad_js()+",")
//				.append(bsv.getStad_yk()+","+bsv.getStad_zdj()+","+bsv.getStad_zje()+","+bsv.getGongfsl()+","+bsv.getYanssl()+","+bsv.getJiessl()+","+bsv.getKoud()+","+bsv.getKous()+","+bsv.getKouz()+","+bsv.getChes()+","+bsv.getJingz()+","+bsv.getKoud_js()+","+bsv.getYuns()+",")
//				.append(bsv.getJiesslcy()+","+bsv.getShulzjbz()+","+bsv.getJiakhj()+","+bsv.getJiaksk()+","+bsv.getJiashj()+",0,0,1);	\n");
		}
		
		if(!bsv.getMad_ht().equals("")){
			
			sb.append(Shoumjsdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Shoumjsdcz.getProperId(ZhibModel, Locale.Mad_zhibb), 
					bsv.getMad_ht(), bsv.getMad_kf(), bsv.getMad_cf(), bsv.getMad_js(), 
					reCoundYk(bsv.getMad_ht(),bsv.getMad_js(),bsv.getMad_yk()), bsv.getMad_zdj(), bsv.getMad_zje(), 
					bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
					bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
					bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
					bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
					bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
					bsv.getFaz_Id(), bsv.getChaokdl()));
			
//			sql.append("insert into danpcjsmxb (id, xuh, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, gongfsl, yanssl, jiessl, koud, kous, kouz, ches, jingz, koud_js, yuns, jiesslcy, jiesdj, jiakhj, jiaksk, jiashj, biaomdj, buhsbmdj, leib)	\n");
//			sql.append("	values(		\n");
//			sql.append("getnewid("+bsv.getDiancxxb_id()+"),"+bsv.getXuh()+","+bsv.getMeikjsb_id()+","+Shoumjsdcz.getProperId(getZhibModel(), Locale.Mad_zhibb)+",'"+bsv.getMad_ht()+"',"+bsv.getMad_kf()+","+bsv.getMad_cf()+","+bsv.getMad_js()+",")
//				.append(bsv.getMad_yk()+","+bsv.getMad_zdj()+","+bsv.getMad_zje()+","+bsv.getGongfsl()+","+bsv.getYanssl()+","+bsv.getJiessl()+","+bsv.getKoud()+","+bsv.getKous()+","+bsv.getKouz()+","+bsv.getChes()+","+bsv.getJingz()+","+bsv.getKoud_js()+","+bsv.getYuns()+",")
//				.append(bsv.getJiesslcy()+","+bsv.getShulzjbz()+","+bsv.getJiakhj()+","+bsv.getJiaksk()+","+bsv.getJiashj()+",0,0,1);	\n");
		}
		
		if(!bsv.getAar_ht().equals("")){
			
			sb.append(Shoumjsdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Shoumjsdcz.getProperId(ZhibModel, Locale.Aar_zhibb), 
					bsv.getAar_ht(), bsv.getAar_kf(), bsv.getAar_cf(), bsv.getAar_js(), 
					reCoundYk(bsv.getAar_ht(),bsv.getAar_js(),bsv.getAar_yk()), bsv.getAar_zdj(), bsv.getAar_zje(), 
					bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
					bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
					bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
					bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
					bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
					bsv.getFaz_Id(), bsv.getChaokdl()));
			
//			sql.append("insert into danpcjsmxb (id, xuh, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, gongfsl, yanssl, jiessl, koud, kous, kouz, ches, jingz, koud_js, yuns, jiesslcy, jiesdj, jiakhj, jiaksk, jiashj, biaomdj, buhsbmdj, leib)	\n");
//			sql.append("	values(		\n");
//			sql.append("getnewid("+bsv.getDiancxxb_id()+"),"+bsv.getXuh()+","+bsv.getMeikjsb_id()+","+Shoumjsdcz.getProperId(getZhibModel(), Locale.Aar_zhibb)+",'"+bsv.getAar_ht()+"',"+bsv.getAar_kf()+","+bsv.getAar_cf()+","+bsv.getAar_js()+",")
//				.append(bsv.getAar_yk()+","+bsv.getAar_zdj()+","+bsv.getAar_zje()+","+bsv.getGongfsl()+","+bsv.getYanssl()+","+bsv.getJiessl()+","+bsv.getKoud()+","+bsv.getKous()+","+bsv.getKouz()+","+bsv.getChes()+","+bsv.getJingz()+","+bsv.getKoud_js()+","+bsv.getYuns()+",")
//				.append(bsv.getJiesslcy()+","+bsv.getShulzjbz()+","+bsv.getJiakhj()+","+bsv.getJiaksk()+","+bsv.getJiashj()+",0,0,1);	\n");
		}
		
		if(!bsv.getAad_ht().equals("")){
			
			sb.append(Shoumjsdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Shoumjsdcz.getProperId(ZhibModel, Locale.Aad_zhibb), 
					bsv.getAad_ht(), bsv.getAad_kf(), bsv.getAad_cf(), bsv.getAad_js(), 
					reCoundYk(bsv.getAad_ht(),bsv.getAad_js(),bsv.getAad_yk()), bsv.getAad_zdj(), bsv.getAad_zje(), 
					bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
					bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
					bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
					bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
					bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
					bsv.getFaz_Id(), bsv.getChaokdl()));
			
			
//			sql.append("insert into danpcjsmxb (id, xuh, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, gongfsl, yanssl, jiessl, koud, kous, kouz, ches, jingz, koud_js, yuns, jiesslcy, jiesdj, jiakhj, jiaksk, jiashj, biaomdj, buhsbmdj, leib)	\n");
//			sql.append("	values(		\n");
//			sql.append("getnewid("+bsv.getDiancxxb_id()+"),"+bsv.getXuh()+","+bsv.getMeikjsb_id()+","+Shoumjsdcz.getProperId(getZhibModel(), Locale.Aad_zhibb)+",'"+bsv.getAad_ht()+"',"+bsv.getAad_kf()+","+bsv.getAad_cf()+","+bsv.getAad_js()+",")
//				.append(bsv.getAad_yk()+","+bsv.getAad_zdj()+","+bsv.getAad_zje()+","+bsv.getGongfsl()+","+bsv.getYanssl()+","+bsv.getJiessl()+","+bsv.getKoud()+","+bsv.getKous()+","+bsv.getKouz()+","+bsv.getChes()+","+bsv.getJingz()+","+bsv.getKoud_js()+","+bsv.getYuns()+",")
//				.append(bsv.getJiesslcy()+","+bsv.getShulzjbz()+","+bsv.getJiakhj()+","+bsv.getJiaksk()+","+bsv.getJiashj()+",0,0,1);	\n");
		}
		
		if(!bsv.getVad_ht().equals("")){
			
			sb.append(Shoumjsdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Shoumjsdcz.getProperId(ZhibModel, Locale.Vad_zhibb), 
					bsv.getVad_ht(), bsv.getVad_kf(), bsv.getVad_cf(), bsv.getVad_js(), 
					reCoundYk(bsv.getVad_ht(),bsv.getVad_js(),bsv.getVad_yk()), bsv.getVad_zdj(), bsv.getVad_zje(), 
					bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
					bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
					bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
					bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
					bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
					bsv.getFaz_Id(), bsv.getChaokdl()));
			
//			sql.append("insert into danpcjsmxb (id, xuh, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, gongfsl, yanssl, jiessl, koud, kous, kouz, ches, jingz, koud_js, yuns, jiesslcy, jiesdj, jiakhj, jiaksk, jiashj, biaomdj, buhsbmdj, leib)	\n");
//			sql.append("	values(		\n");
//			sql.append("getnewid("+bsv.getDiancxxb_id()+"),"+bsv.getXuh()+","+bsv.getMeikjsb_id()+","+Shoumjsdcz.getProperId(getZhibModel(), Locale.Vad_zhibb)+",'"+bsv.getVad_ht()+"',"+bsv.getVad_kf()+","+bsv.getVad_cf()+","+bsv.getVad_js()+",")
//				.append(bsv.getVad_yk()+","+bsv.getVad_zdj()+","+bsv.getVad_zje()+","+bsv.getGongfsl()+","+bsv.getYanssl()+","+bsv.getJiessl()+","+bsv.getKoud()+","+bsv.getKous()+","+bsv.getKouz()+","+bsv.getChes()+","+bsv.getJingz()+","+bsv.getKoud_js()+","+bsv.getYuns()+",")
//				.append(bsv.getJiesslcy()+","+bsv.getShulzjbz()+","+bsv.getJiakhj()+","+bsv.getJiaksk()+","+bsv.getJiashj()+",0,0,1);	\n");
		}
		
		if(!bsv.getT2_ht().equals("")){
			
			sb.append(Shoumjsdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Shoumjsdcz.getProperId(ZhibModel, Locale.T2_zhibb), 
					bsv.getT2_ht(), bsv.getT2_kf(), bsv.getT2_cf(), bsv.getT2_js(), 
					reCoundYk(bsv.getT2_ht(),bsv.getT2_js(),bsv.getT2_yk()), bsv.getT2_zdj(), bsv.getT2_zje(), 
					bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
					bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
					bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
					bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
					bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
					bsv.getFaz_Id(), bsv.getChaokdl()));
			
//			sql.append("insert into danpcjsmxb (id, xuh, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, gongfsl, yanssl, jiessl, koud, kous, kouz, ches, jingz, koud_js, yuns, jiesslcy, jiesdj, jiakhj, jiaksk, jiashj, biaomdj, buhsbmdj, leib)	\n");
//			sql.append("	values(		\n");
//			sql.append("getnewid("+bsv.getDiancxxb_id()+"),"+bsv.getXuh()+","+bsv.getMeikjsb_id()+","+Shoumjsdcz.getProperId(getZhibModel(), Locale.T2_zhibb)+",'"+bsv.getT2_ht()+"',"+bsv.getT2_kf()+","+bsv.getT2_cf()+","+bsv.getT2_js()+",")
//				.append(bsv.getT2_yk()+","+bsv.getT2_zdj()+","+bsv.getT2_zje()+","+bsv.getGongfsl()+","+bsv.getYanssl()+","+bsv.getJiessl()+","+bsv.getKoud()+","+bsv.getKous()+","+bsv.getKouz()+","+bsv.getChes()+","+bsv.getJingz()+","+bsv.getKoud_js()+","+bsv.getYuns()+",")
//				.append(bsv.getJiesslcy()+","+bsv.getShulzjbz()+","+bsv.getJiakhj()+","+bsv.getJiaksk()+","+bsv.getJiashj()+",0,0,1);	\n");
		}
		
		if(!bsv.getYunju_ht().equals("")){
			
			sb.append(Shoumjsdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Shoumjsdcz.getProperId(ZhibModel, Locale.Yunju_zhibb), 
					bsv.getYunju_ht(), bsv.getYunju_kf(), bsv.getYunju_cf(), bsv.getYunju_js(), 
					reCoundYk(bsv.getYunju_ht(),bsv.getYunju_js(),bsv.getYunju_yk()), bsv.getYunju_zdj(), bsv.getYunju_zje(), 
					bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
					bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
					bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
					bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
					bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
					bsv.getFaz_Id(), bsv.getChaokdl()));
			
//			sql.append("insert into danpcjsmxb (id, xuh, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, gongfsl, yanssl, jiessl, koud, kous, kouz, ches, jingz, koud_js, yuns, jiesslcy, jiesdj, jiakhj, jiaksk, jiashj, biaomdj, buhsbmdj, leib)	\n");
//			sql.append("	values(		\n");
//			sql.append("getnewid("+bsv.getDiancxxb_id()+"),"+bsv.getXuh()+","+bsv.getMeikjsb_id()+","+Shoumjsdcz.getProperId(getZhibModel(), Locale.Yunju_zhibb)+",'"+bsv.getYunju_ht()+"',"+bsv.getYunju_kf()+","+bsv.getYunju_cf()+","+bsv.getYunju_js()+",")
//				.append(bsv.getYunju_yk()+","+bsv.getYunju_zdj()+","+bsv.getYunju_zje()+","+bsv.getGongfsl()+","+bsv.getYanssl()+","+bsv.getJiessl()+","+bsv.getKoud()+","+bsv.getKous()+","+bsv.getKouz()+","+bsv.getChes()+","+bsv.getJingz()+","+bsv.getKoud_js()+","+bsv.getYuns()+",")
//				.append(bsv.getJiesslcy()+","+bsv.getShulzjbz()+","+bsv.getJiakhj()+","+bsv.getJiaksk()+","+bsv.getJiashj()+",0,0,1);	\n");
		}
		
		if(!bsv.getStar_ht().equals("")){
			
			sb.append(Shoumjsdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Shoumjsdcz.getProperId(ZhibModel, Locale.Star_zhibb), 
					bsv.getStar_ht(), bsv.getStar_kf(), bsv.getStar_cf(), bsv.getStar_js(), 
					reCoundYk(bsv.getStar_ht(),bsv.getStar_js(),bsv.getStar_yk()), bsv.getStar_zdj(), bsv.getStar_zje(), 
					bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
					bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
					bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
					bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
					bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
					bsv.getFaz_Id(), bsv.getChaokdl()));
			
//			sql.append("insert into danpcjsmxb (id, xuh, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, gongfsl, yanssl, jiessl, koud, kous, kouz, ches, jingz, koud_js, yuns, jiesslcy, jiesdj, jiakhj, jiaksk, jiashj, biaomdj, buhsbmdj, leib)	\n");
//			sql.append("	values(		\n");
//			sql.append("getnewid("+bsv.getDiancxxb_id()+"),"+bsv.getXuh()+","+bsv.getMeikjsb_id()+","+Shoumjsdcz.getProperId(getZhibModel(), Locale.Yunju_zhibb)+",'"+bsv.getStar_ht()+"',"+bsv.getStar_kf()+","+bsv.getStar_cf()+","+bsv.getStar_js()+",")
//				.append(bsv.getStar_yk()+","+bsv.getStar_zdj()+","+bsv.getStar_zje()+","+bsv.getGongfsl()+","+bsv.getYanssl()+","+bsv.getJiessl()+","+bsv.getKoud()+","+bsv.getKous()+","+bsv.getKouz()+","+bsv.getChes()+","+bsv.getJingz()+","+bsv.getKoud_js()+","+bsv.getYuns()+",")
//				.append(bsv.getJiesslcy()+","+bsv.getShulzjbz()+","+bsv.getJiakhj()+","+bsv.getJiaksk()+","+bsv.getJiashj()+",0,0,1);	\n");
		}
		
		return sb;
	}
	
	public static StringBuffer getDanpcjsmxb_InsertSql(long diancxxb_id, int xuh, long meikjsb_id, long zhibb_id,
			String hetbz, double kuangf, double changf, double jies, double yingk, double zhejbz, double zhejje,
			double gongfsl, double yanssl, double jiessl, double koud, double kous, double kouz, long ches,
			double jingz, double koud_js, double yuns, double jiesslcy, double jiajqdj, double jiesdj, double jiakhj, double jiaksk,
			double jiashj, double hetj, double qnetar, double std, double stad, double star, double vdaf, double mt,
			double mad, double aad, double ad, double aar, double vad, double zongje, long meikxxb_id, long faz_id, 
			double chaokdl){
		
		StringBuffer sb = new StringBuffer("");
		
		sb.append("insert into danpcjsmxb (id, xuh, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, gongfsl, yanssl, jiessl, koud, kous, kouz, \n")
			.append(" ches, jingz, koud_js, yuns, jiesslcy, jiajqdj, jiesdj, jiakhj, jiaksk, jiashj, biaomdj, buhsbmdj, leib, hetj, qnetar, std, stad, star, vdaf, mt, mad, aad, \n")
			.append(" ad, aar, vad, zongje, meikxxb_id, faz_id, chaokdl) \n")
			.append("	values(		\n")
			.append("getnewid("+diancxxb_id+"),"+xuh+","+meikjsb_id+","+zhibb_id+",'"+hetbz+"',"+kuangf+","+changf+","+jies+",")
			.append(yingk+","+zhejbz+","+zhejje+","+gongfsl+","+yanssl+","+jiessl+","+koud+","+kous+","+kouz+","+ches+","+jingz+","+koud_js+","+yuns+",")
			.append(jiesslcy+","+jiajqdj+","+jiesdj+","+jiakhj+","+jiaksk+","+jiashj+",0,0,1,"+hetj+","+qnetar+","+std+","+stad+","+star+","+vdaf+","+mt+","+mad+","+aad+",")
			.append(ad+","+aar+","+vad+","+zongje+","+meikxxb_id+","+faz_id+","+chaokdl+");\n");
		
		return sb;
	}
	
	public static String getJieszbtscl(String Zhibbm,String SelId){
		
//		函数名称：结算指标特殊处理
//		功能：在正常指标计算完后（该单批次单批次、该加权平均加权平均），
//			算出基础结算价格后，如果有需要特殊处理的指标，在此进行特殊处理。
//		参数：指标编码，要结算的lie_id
		String zhibs="";
		String sql="";
		JDBCcon con = new JDBCcon();
		ResultSetList rsl=null;
		int i=0;
			
		if(Zhibbm.equals(Locale.Yunju_zhibb)){
//				运距处理，目前只支持单位为“Km”的运距指标
			sql=
				"select meikxxb_id,zhi from meiksxglb,danwb\n" +
				"       where shuxbm='"+Locale.Yunju_zhibb+"'\n" + 
				"         and meiksxglb.danwb_id=danwb.id\n" + 
				"         and danwb.bianm='"+Locale.qianm_daw+"'\n" + 
				"         and meikxxb_id in\n" + 
				"         (select meikxxb_id from fahb\n" + 
				"                where lie_id in ("+SelId+"))";
			rsl = con.getResultSetList(sql);
			
			if(rsl.getRows()>1){
//				如果种类大于1单独计算才有意义，否则指标应在计算数量质量时得到
				while(rsl.next()){
//					结构：id,指标编码,指标分组依据,分组依据的值,分组指标的值,标志（用来标识是否已特殊处理过了，0为未处理过、1为已处理过）
					zhibs+=i+","+Locale.Yunju_zhibb+",meikxxb_id,"+rsl.getString("meikxxb_id")+","+rsl.getString("zhi")+",0;";
					i++;
				}
			}
		}else{
//			暂定为除了“运距”的其它指标用
			sql=
				"select f.lie_id,"+getZhibb_zhilb_transform(Zhibbm)+" from fahb f,zhilb z\n" +
				"       where f.zhilb_id = z.id\n" + 
				"             and f.lie_id in ("+SelId+")";

			rsl = con.getResultSetList(sql);
			
			if(rsl.getRows()>1){
//				如果种类大于1单独计算才有意义，否则指标应在计算数量质量时得到
				while(rsl.next()){
//					结构：id,指标编码,指标分组依据,分组依据的值,分组指标的值,标志（用来标识是否已特殊处理过了，0为未处理过、1为已处理过）
					zhibs+=i+","+Zhibbm+",lie_id,"+rsl.getString(0)+","+rsl.getString(1)+",0;";
					i++;
				}
			}

		}
		
		if(rsl!=null){
			
			rsl.close();
		}
		con.Close();
		
		return zhibs;
	}
	
	public static String getZhibb_zhilb_transform(String zhibbm){
//		函数名称：指标_质量单位转换
//		功能：
//			将指标表中指标编码转化为质量表中的字段名称。
//			如指标表中“Qnetar”对应的就是质量表中的“qnet_ar”
//		参数：指标编码，要结算的lie_id
		
		String result=zhibbm;
		
		if(zhibbm.equals(Locale.Qnetar_zhibb)){
			
			result="qnet_ar";
		}
		
		return result;
	}
	
	public static boolean getJieszbtscl_Zkk(String jiagjsxs,String zkkjsxs){
		
//		函数功能：
//			如果某一个指标是需要特殊处理的，那么在计算增扣款时应忽略该指标
//		函数逻辑：
//				如果价格条款中煤款是加权平均计算的，才考虑renturn false 
//			否则，是单批次结算没有特殊指标处理的情况
//		函数形参：
//			Tsclzbs:需要特殊调整的指标数值；zhibbm：要进行增扣款的指标编码
		
		boolean Flag=true;
		
		if(jiagjsxs.equals(Locale.jiaqpj_jiesxs)){
//			如果价格条款中煤款是加权平均计算的，才考虑renturn false 
//				否则，是单批次结算没有特殊指标处理的情况
			if(zkkjsxs==null
					||zkkjsxs.equals(Locale.jiaqpj_jiesxs)
					||zkkjsxs.equals("")){
				
				Flag=true;
			}else if(zkkjsxs.equals(Locale.danpc_jiesxs)){
				
				Flag=false;
			}
		}
		
//		if(Tsclzbs!=null){
//			
//			for(int i=0;i<Tsclzbs.length;i++){
//				
//				if(Tsclzbs[i].indexOf(zhibbm)>-1){
//
//					Flag=false;
//					break;
//				}
//			}
//		}
		
		return Flag;
	}
	
	public static void Mark_Tsclzbs_bz(String[] Tsclzbs,String Joint_primary_key){
		
//		函数功能：
//				需要特殊处理的指标都保存在Tsclzbs数组中，如果该指标已经重新计算过增扣款了，
//			就在Tsclzbs数组的标志位上打上标识1.
//		函数逻辑：
//			如果Tsclzbs里包含Joint_primary_key，就将Tsclzbs里面的标识位打上标识
//		函数形参：
//			Tsclzbs:需要特殊调整的指标数值；Joint_primary_key：Tsclzbs里的联合组件
		
		for(int i=0;i<Tsclzbs.length;i++){
			
			if(Tsclzbs[i].indexOf(Joint_primary_key)>-1){
				
				Tsclzbs[i] = Tsclzbs[i].substring(0,Tsclzbs[i].lastIndexOf(",")+1)+"1;";
			}
		}
	}
	
	public static void setJieszb_Tszbcl(Interpreter bsh,Balances_variable bsv,String zhibbm){
//		
//		函数功能：
//			将经特殊处理过的指标赋值给该指标的显示模块。有下面两种情况：
//				1、当该指标即是加权平均计算又是单批次计算时，显示模块只显示加权平均结算的指标
//				2、当该指标是单批次计算时(即:在此方法被调用前加权平均赋值过的值为空)，
//					则显示模块只显示单批次结算的指标（且只显示该指标第一次单批次结算的信息）
//		函数逻辑：
//			如果Tsclzbs里包含Joint_primary_key，就将Tsclzbs里面的标识位打上标识
//		函数形参：
//			Tsclzbs:需要特殊调整的指标数值；Joint_primary_key：Tsclzbs里的联合组件
		try {
	
			if(!bsh.get("合同标准_"+Locale.jiessl_zhibb).toString().equals("")
					&&bsv.getShul_ht().equals("")){
				
//				数量增扣款取值
				bsv.setShul_ht(bsh.get("合同标准_结算数量").toString());
				bsv.setShul_yk(Double.parseDouble(bsh.get("盈亏_结算数量").toString()));
				bsv.setShul_zdj(Double.parseDouble(bsh.get("折单价_结算数量").toString()));
			}
			
			if(!bsh.get("合同标准_"+Locale.Qnetar_zhibb).toString().equals("")
					&&bsv.getQnetar_ht().equals("")){
				
//				Qnetar
				bsv.setQnetar_ht(bsh.get("合同标准_Qnetar").toString());
				bsv.setQnetar_yk(Double.parseDouble(bsh.get("盈亏_Qnetar").toString()));
				bsv.setQnetar_zdj(Double.parseDouble(bsh.get("折单价_Qnetar").toString()));
			}
			
			if(!bsh.get("合同标准_"+Locale.Std_zhibb).toString().equals("")
					&&bsv.getStd_ht().equals("")){
				
//				Std
				bsv.setStd_ht(bsh.get("合同标准_Std").toString());
				bsv.setStd_yk(Double.parseDouble(bsh.get("盈亏_Std").toString()));
				bsv.setStd_zdj(Double.parseDouble(bsh.get("折单价_Std").toString()));
			}
			
			if(!bsh.get("合同标准_"+Locale.Ad_zhibb).toString().equals("")
					&&bsv.getAd_ht().equals("")){
				
//				Ad
				bsv.setAd_ht(bsh.get("合同标准_Ad").toString());
				bsv.setAd_yk(Double.parseDouble(bsh.get("盈亏_Ad").toString()));
				bsv.setAd_zdj(Double.parseDouble(bsh.get("折单价_Ad").toString()));
			}

			if(!bsh.get("合同标准_"+Locale.Vdaf_zhibb).toString().equals("")
					&&bsv.getVdaf_ht().equals("")){
				
//				Vdaf
				bsv.setVdaf_ht(bsh.get("合同标准_Vdaf").toString());
				bsv.setVdaf_yk(Double.parseDouble(bsh.get("盈亏_Vdaf").toString()));
				bsv.setVdaf_zdj(Double.parseDouble(bsh.get("折单价_Vdaf").toString()));
			}

			if(!bsh.get("合同标准_"+Locale.Mt_zhibb).toString().equals("")
					&&bsv.getMt_ht().equals("")){
				
//				Mt
				bsv.setMt_ht(bsh.get("合同标准_Mt").toString());
				bsv.setMt_yk(Double.parseDouble(bsh.get("盈亏_Mt").toString()));
				bsv.setMt_zdj(Double.parseDouble(bsh.get("折单价_Mt").toString()));
			}

			if(!bsh.get("合同标准_"+Locale.Qgrad_zhibb).toString().equals("")
					&&bsv.getQgrad_ht().equals("")){
				
//				Qgrad
				bsv.setQgrad_ht(bsh.get("合同标准_Qgrad").toString());
				bsv.setQgrad_yk(Double.parseDouble(bsh.get("盈亏_Qgrad").toString()));
				bsv.setQgrad_zdj(Double.parseDouble(bsh.get("折单价_Qgrad").toString()));
			}

			if(!bsh.get("合同标准_"+Locale.Qbad_zhibb).toString().equals("")
					&&bsv.getQbad_ht().equals("")){
				
//				Qbad
				bsv.setQbad_ht(bsh.get("合同标准_Qbad").toString());
				bsv.setQbad_yk(Double.parseDouble(bsh.get("盈亏_Qbad").toString()));
				bsv.setQbad_zdj(Double.parseDouble(bsh.get("折单价_Qbad").toString()));
			}

			if(!bsh.get("合同标准_"+Locale.Had_zhibb).toString().equals("")
					&&bsv.getHad_ht().equals("")){
				
//				Had
				bsv.setHad_ht(bsh.get("合同标准_Had").toString());
				bsv.setHad_yk(Double.parseDouble(bsh.get("盈亏_Had").toString()));
				bsv.setHad_zdj(Double.parseDouble(bsh.get("折单价_Had").toString()));
			}

			if(!bsh.get("合同标准_"+Locale.Stad_zhibb).toString().equals("")
					&&bsv.getStad_ht().equals("")){
				
//				Stad
				bsv.setStad_ht(bsh.get("合同标准_Stad").toString());
				bsv.setStad_yk(Double.parseDouble(bsh.get("盈亏_Stad").toString()));
				bsv.setStad_zdj(Double.parseDouble(bsh.get("折单价_Stad").toString()));
			}

			if(!bsh.get("合同标准_"+Locale.Star_zhibb).toString().equals("")
					&&bsv.getStar_ht().equals("")){
				
//				Star
				bsv.setStar_ht(bsh.get("合同标准_Star").toString());
				bsv.setStar_yk(Double.parseDouble(bsh.get("盈亏_Star").toString()));
				bsv.setStar_zdj(Double.parseDouble(bsh.get("折单价_Star").toString()));
			}

			if(!bsh.get("合同标准_"+Locale.Mad_zhibb).toString().equals("")
					&&bsv.getMad_ht().equals("")){
				
//				Mad
				bsv.setMad_ht(bsh.get("合同标准_Mad").toString());
				bsv.setMad_yk(Double.parseDouble(bsh.get("盈亏_Mad").toString()));
				bsv.setMad_zdj(Double.parseDouble(bsh.get("折单价_Mad").toString()));
			}

			if(!bsh.get("合同标准_"+Locale.Aar_zhibb).toString().equals("")
					&&bsv.getAar_ht().equals("")){
				
//				Aar
				bsv.setAar_ht(bsh.get("合同标准_Aar").toString());
				bsv.setAar_yk(Double.parseDouble(bsh.get("盈亏_Aar").toString()));
				bsv.setAar_zdj(Double.parseDouble(bsh.get("折单价_Aar").toString()));
			}

			if(!bsh.get("合同标准_"+Locale.Aad_zhibb).toString().equals("")
					&&bsv.getAad_ht().equals("")){
				
//				Aad
				bsv.setAad_ht(bsh.get("合同标准_Aad").toString());
				bsv.setAad_yk(Double.parseDouble(bsh.get("盈亏_Aad").toString()));
				bsv.setAad_zdj(Double.parseDouble(bsh.get("折单价_Aad").toString()));
			}

			if(!bsh.get("合同标准_"+Locale.Vad_zhibb).toString().equals("")
					&&bsv.getVad_ht().equals("")){
				
//				Vad
				bsv.setVad_ht(bsh.get("合同标准_Vad").toString());
				bsv.setVad_yk(Double.parseDouble(bsh.get("盈亏_Vad").toString()));
				bsv.setVad_zdj(Double.parseDouble(bsh.get("折单价_Vad").toString()));
			}

			if(!bsh.get("合同标准_"+Locale.T2_zhibb).toString().equals("")
					&&bsv.getT2_ht().equals("")){
				
//				T2
				bsv.setT2_ht(bsh.get("合同标准_T2").toString());
				bsv.setT2_yk(Double.parseDouble(bsh.get("盈亏_T2").toString()));
				bsv.setT2_zdj(Double.parseDouble(bsh.get("折单价_T2").toString()));
			}

			if(!bsh.get("合同标准_"+Locale.Yunju_zhibb).toString().equals("")
					&&bsv.getYunju_ht().equals("")){
				
//				运距
				bsv.setYunju_ht(bsh.get("合同标准_运距").toString());
				bsv.setYunju_yk(Double.parseDouble(bsh.get("盈亏_运距").toString()));
				bsv.setYunju_zdj(Double.parseDouble(bsh.get("折单价_运距").toString()));
			}
			
		} catch (EvalError e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}
	
	public static String getShezzh(String Shezvalue){
//		函数功能：(设置转换)
//			在结算系统设置的模块里，关于数量取整设置的描述都是通过文字来完成的，
//			如：按列四舍五入后相加、相加后统一四舍五入、不进行取整操作；
//			但在后台的处理中都要转化成符号才行，故通过此函数进行转换（目前只针对系统信息中的设置）
//		函数逻辑：
//			通过全字匹配转换赋值
//		函数形参：
//			Shezvalue:需要转化的设置的值
		if(Shezvalue.equals(Locale.anlsswrhxj_jiesghlqzfs_xitxx)){
//			过衡量取整方式:按列四舍五入后相加
			Shezvalue="sum(round_new())";
		}else if(Shezvalue.equals(Locale.xiangjhtysswr_jiesghlqzfs_xitxx)){
//			过衡量取整方式:相加后统一四舍五入
			Shezvalue="round_new(sum())";
		}else if(Shezvalue.equals(Locale.bujxqzcz_jiesghlqzfs_xitxx)){
//			过衡量取整方式:不进行取整操作
			Shezvalue="sum()";
		}else{
//			过衡量取整方式:如果都没得到默认为“按列四舍五入后相加”
			Shezvalue="sum(round_new())";
		}
		
		return Shezvalue;
	}
	
	public static StringBuffer getJiesszl_Sql(Balances_variable bsv,String Jieszbsftz,String SelIds,long Diancxxb_id,
			long Gongysb_id,long Hetb_id,double Jieskdl,long Yunsdwb_id,long Jieslx,double Shangcjsl,
			String Tsclzb_where){
		
		String jies_Jqsl="jingz";								//结算加权数量
		String jies_Qnetarblxs="2";
		String jies_Stdblxs="2";
		String jies_Mtblxs="1";
		String jies_Madblxs="2";
		String jies_Aarblxs="2";
		String jies_Aadblxs="2";
		String jies_Adblxs="2";
		String jies_Vadblxs="2";
		String jies_Vdafblxs="2";
		String jies_Stadblxs="2";
		String jies_Starblxs="2";
		String jies_Hadblxs="2";
		String jies_Qbadblxs="2";
		String jies_Qgradblxs="2";
		String jies_T2blxs="2";
		String jies_shifykfzljs="否";
		String jies_Jieslqzfs="sum(round_new())";				//结算数量取整方式
		String jies_Jsslblxs="0";								//结算数量保留小数位
		String jies_Kdkskzqzfs="round_new(sum())";				//扣吨、扣水、扣杂取整方式
		String jies_Jssl="biaoz+yingk-koud-kous-kouz";			//结算数量
		String jies_yunfjssl="jingz";							//运费结算数量
		boolean blnDandszyfjssl=false;	//是否单独设置了运费结算数量，如果是则yunfjssl=设置数量，如果不是且两票结算yunfjssl=gongfsl
		String jiscdkd="否";										//是否计算超吨、扣吨
		String ChaodOrKuid="";									//计算超吨还是亏吨
		
		String jies_Guohlqzfs=Locale.anlsswrhxj_jiesghlqzfs_xitxx;	//系统信息中过衡量取整方式，默认为：按列四舍五入后相加
		String jies_Guohlblxsw="0";									//系统信息中过衡量保留小数位，默认为：2
		String yunsdw="";	//运输单位的条件
		if(Yunsdwb_id>-1){
//			选择了运输单位
			yunsdw=" and cp.yunsdwb_id="+Yunsdwb_id;
		}
		
		
		
//		jies_Jqsl=MainGlobal.getXitxx_item("结算", Locale.jiaqsl_xitxx, 
//    			String.valueOf(Diancxxb_id),jies_Jqsl);
		
//		从系统信息表中取结算设置的信息
		String XitxxArrar[][]=null;	
		XitxxArrar=MainGlobal.getXitxx_items("结算",	"select mingc from xitxxb where leib='结算'"
				,String.valueOf(Diancxxb_id));
		
//		分析取得的值，然后对变量进行赋值
		if(XitxxArrar!=null){
			
			for(int i=0;i<XitxxArrar.length;i++){
				
				if(XitxxArrar[i][0]!=null){
					
					if(XitxxArrar[i][0].trim().equals(Locale.jiaqsl_xitxx)){
//						加权数量
						jies_Jqsl=XitxxArrar[i][1].trim();
					}else if(XitxxArrar[i][0].trim().equals(Locale.jiesghlqzfs_xitxx)){
//						过衡量取整方式
						jies_Guohlqzfs=getShezzh(XitxxArrar[i][1].trim());
					}else if(XitxxArrar[i][0].trim().equals(Locale.jiesghlblxsw_xitxx)){
//						结算过衡量保留小数位
						jies_Guohlblxsw=XitxxArrar[i][1].trim();
					}else if(XitxxArrar[i][0].trim().equals(Locale.meiksl_xitxx)){
//						煤款税率
						bsv.setMeiksl(Double.parseDouble(XitxxArrar[i][1].trim()));
					}else if(XitxxArrar[i][0].trim().equals(Locale.yunfsl_xitxx)){
//						运费税率
						bsv.setYunfsl(Double.parseDouble(XitxxArrar[i][1].trim()));
					}else if(XitxxArrar[i][0].trim().equals(Locale.jiscdkd_xitxx)){
//						计算超吨扣吨
						jiscdkd=XitxxArrar[i][1].trim();
					}else if(XitxxArrar[i][0].trim().equals(Locale.Meikzkkblxsw_xitxx)){
//						煤款增扣款保留小数位
						bsv.setMeikzkkblxsw(Integer.parseInt(XitxxArrar[i][1].trim()));
					}else if(XitxxArrar[i][0].trim().equals(Locale.jiesslqzfs_jies)){
//						结算数量取整方式
						jies_Jieslqzfs = getShezzh(XitxxArrar[i][1].trim());
					}else if(XitxxArrar[i][0].trim().equals(Locale.jiesslblxsw_jies)){
//						结算数量保留小数位
						jies_Jsslblxs = XitxxArrar[i][1].trim();
					}else if(XitxxArrar[i][0].trim().equals(Locale.jiesslzcfs_jies)){
//						结算数量组成方式
						jies_Jssl = XitxxArrar[i][1].trim();
					}else if(XitxxArrar[i][0].trim().equals(Locale.kuidjfyf_jies)){
//						亏吨拒付运费
						bsv.setKuidjfyf(XitxxArrar[i][1].trim());
					}
//					else if(XitxxArrar[i][0].trim().equals(Locale.yunjdpcjs)){
////						运距单批次处理，需要单独处理
//						if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)
//								&&XitxxArrar[i][1].trim().equals("是")
//								&&bsv.getTsclzbs()==null						
//								){
////							前提是结算形式必须为“加权平均”,系统信息中设置的值为“是”
////							因为前提是“加权平均”结算，故SelIds为全部结算lie_id
////							特殊指标数组只能赋值一次
//							bsv.setJieszbtscl_Items(bsv.getJieszbtscl_Items()+Shoumjsdcz.getJieszbtscl(Locale.Yunju_zhibb, SelIds));
//						}
//					}
				}
			}
		}
		
		if(jiscdkd.equals("是")){
//			已在系统信息表中设定了超吨或亏吨的计算
			if(bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)){
//				到厂价计算“超吨”
				ChaodOrKuid="CD";
			}else if(bsv.getJiesfs().equals(Locale.chukjg_ht_jsfs)){
//				出矿价计算“亏吨”
				ChaodOrKuid="KD";
			}
		}
//		记录超吨Or亏吨
		bsv.setChaodOrKuid(ChaodOrKuid);
		
		if(Shoumjsdcz.getJiessz_items(Diancxxb_id,Gongysb_id,Hetb_id)!=null){

        	String JiesszArray[][]=null;
		
        	JiesszArray=Shoumjsdcz.getJiessz_items(Diancxxb_id,Gongysb_id,Hetb_id);
	
			for(int i=0;i<JiesszArray.length;i++){
				
				if(JiesszArray[i][0]!=null){
					
					if(JiesszArray[i][0].equals(Locale.jiesjqsl_jies)){
						
						jies_Jqsl=JiesszArray[i][1];
					}else if(JiesszArray[i][0].equals(Locale.mtblxsw_jies)){
						
						jies_Mtblxs=JiesszArray[i][1];
					}else if(JiesszArray[i][0].equals(Locale.madblxsw_jies)){
						
						jies_Madblxs=JiesszArray[i][1];
					}else if(JiesszArray[i][0].equals(Locale.aarblxsw_jies)){
						
						jies_Aarblxs=JiesszArray[i][1];
					}else if(JiesszArray[i][0].equals(Locale.aadblxsw_jies)){
						
						jies_Aadblxs=JiesszArray[i][1];
					}else if(JiesszArray[i][0].equals(Locale.adblxsw_jies)){
						
						jies_Adblxs=JiesszArray[i][1];
					}else if(JiesszArray[i][0].equals(Locale.vadblxsw_jies)){
						
						jies_Vadblxs=JiesszArray[i][1];
					}else if(JiesszArray[i][0].equals(Locale.vdafblxsw_jies)){
						
						jies_Vdafblxs=JiesszArray[i][1];
					}else if(JiesszArray[i][0].equals(Locale.stadblxsw_jies)){
						
						jies_Stadblxs=JiesszArray[i][1];
					}else if(JiesszArray[i][0].equals(Locale.starblxsw_jies)){
					
						jies_Starblxs=JiesszArray[i][1];
					}else if(JiesszArray[i][0].equals(Locale.stdblxsw_jies)){
						
						jies_Stdblxs=JiesszArray[i][1];
					}else if(JiesszArray[i][0].equals(Locale.hadblxsw_jies)){
						
						jies_Hadblxs=JiesszArray[i][1];
					}else if(JiesszArray[i][0].equals(Locale.Qnetarblxsw_jies)){
						
						jies_Qnetarblxs=JiesszArray[i][1];
					}else if(JiesszArray[i][0].equals(Locale.Qbadblxsw_jies)){
						
						jies_Qbadblxs=JiesszArray[i][1];
					}else if(JiesszArray[i][0].equals(Locale.Qgradblxsw_jies)){
						
						jies_Qgradblxs=JiesszArray[i][1];
					}else if(JiesszArray[i][0].equals(Locale.shifykfzljs_jies)){
						
//						jies_shifykfzljs=JiesszArray[i][1];
						bsv.setShifykfzljs(JiesszArray[i][1]);
					}else if(JiesszArray[i][0].equals(Locale.meiksl_jies)){
						
						bsv.setMeiksl(Double.parseDouble(JiesszArray[i][1]));
					}else if(JiesszArray[i][0].equals(Locale.yunfsl_jies)){
						
						bsv.setYunfsl(Double.parseDouble(JiesszArray[i][1]));
					}else if(JiesszArray[i][0].equals(Locale.jiesslqzfs_jies)){
						
						jies_Jieslqzfs=JiesszArray[i][1];
					}else if(JiesszArray[i][0].equals(Locale.jiesslblxsw_jies)){
						
						jies_Jsslblxs=JiesszArray[i][1];
						bsv.setJiesslblxs(jies_Jsslblxs);
					}else if(JiesszArray[i][0].equals(Locale.jiesslzcfs_jies)){
						
						jies_Jssl=JiesszArray[i][1];
					}else if(JiesszArray[i][0].equals(Locale.yunfjsslzcfs_jies)){
						
						jies_yunfjssl=JiesszArray[i][1];
						blnDandszyfjssl=true;
					}else if(JiesszArray[i][0].equals(Locale.user_custom_mlj_jiesgs)){
						
						bsv.setUser_custom_mlj_jiesgs(JiesszArray[i][1]);
					}else if(JiesszArray[i][0].equals(Locale.user_custom_fmlj_jiesgs)){
						
						bsv.setUser_custom_fmlj_jiesgs(JiesszArray[i][1]);
					}else if(JiesszArray[i][0].equals(Locale.yikj_yunfyymk_jies)){
						
						bsv.setYikj_yunfyymk(JiesszArray[i][1]);
					}else if(JiesszArray[i][0].equals(Locale.yikj_meikyyyf_jies)){
						
						bsv.setYikj_meikyyyf(JiesszArray[i][1]);
					}else if(JiesszArray[i][0].equals(Locale.meikhsdjblxsw_jies)){
						
//						煤款含税单价保留小数位
						bsv.setMeikhsdjblxsw(Integer.parseInt(JiesszArray[i][1]));
					}else if(JiesszArray[i][0].equals(Locale.yunfhsdjblxsw_jies)){
						
//						运费含税单价保留小数位
						bsv.setYunfhsdjblxsw(Integer.parseInt(JiesszArray[i][1]));
					}else if(JiesszArray[i][0].equals(Locale.kuidjfyf_jies)){
						
//						亏吨拒付运费
						bsv.setKuidjfyf(JiesszArray[i][1]);
					}else if(JiesszArray[i][0].equals(Locale.Mj_to_kcal_xsclfs_jies)){
						
//						兆焦转大卡
						bsv.setMj_to_kcal_xsclfs(JiesszArray[i][1]);
					}else if(JiesszArray[i][0].equals(Locale.meikhsdjqzfs_jies)){
						
//						含税单价取整方式
						bsv.setMeikhsdj_qzfs(JiesszArray[i][1]);
					}
//					else if(JiesszArray[i][0].trim().equals(Locale.yunjdpcjs)){
////						运距单批次处理，需要单独处理
//						
//						if(bsv.getTsclzbs()==null){
////							特殊指标数组只能赋值一次
//							if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)
//									&&JiesszArray[i][1].trim().equals("是")){
////								前提是结算形式必须为“加权平均”,结算设置的值为“是”
////								因为前提是“加权平均”结算，故SelIds为全部结算lie_id
//								
//								if(bsv.getJieszbtscl_Items().indexOf(Locale.Yunju_zhibb)>-1){
////									说明该指标已经在系统信息设置中取到了，这里不再单独做处理
//									
//								}else{
////									说明该指标没在系统信息设置中取到，这里单独做处理
//									bsv.setJieszbtscl_Items(bsv.getJieszbtscl_Items()+Shoumjsdcz.getJieszbtscl(Locale.Yunju_zhibb, SelIds));
//								}
//							}else{
//								
//								if(bsv.getJieszbtscl_Items().indexOf(Locale.Yunju_zhibb)>-1){
////									说明该指标已经在系统信息设置中取到了，但这里又设置成否，说明这个供应商特殊，要去掉这个特殊处理的指标
//									String tmp_begin="";
//									String tmp_md="";
//									String tmp_end="";
//									tmp_begin=bsv.getJieszbtscl_Items().substring(0,bsv.getJieszbtscl_Items().indexOf(Locale.Yunju_zhibb));
//									tmp_md=bsv.getJieszbtscl_Items().substring(bsv.getJieszbtscl_Items().lastIndexOf(Locale.Yunju_zhibb));
//									tmp_end=tmp_md.substring(tmp_md.indexOf(";")+1);
//									bsv.setJieszbtscl_Items(tmp_begin+tmp_end);
//								}
//							}
//						}
//					}
				}
			}
        }
		
//		if(!bsv.getJieszbtscl_Items().equals("")
//				&&bsv.getTsclzbs()==null){
////			说明有要特殊处理的指标
//			String ArrayTsclzbs[]=null;
//			ArrayTsclzbs=bsv.getJieszbtscl_Items().split(";");
//			bsv.setTsclzbs(ArrayTsclzbs);
////			0,运距,meikxxb_id,100,10,0;
////			1,运距,meikxxb_id,101,15,0;
////			2,运距,meikxxb_id,102,20,0;
////			3,Std,meikxxb_id,100,1.0,0;
//		}
		
		try {
			
			bsv.setMeikxxb_Id(Long.parseLong(MainGlobal.getTableCol("fahb", "Meikxxb_id", "lie_id in ("+SelIds+")")));
			bsv.setFaz_Id(Long.parseLong(MainGlobal.getTableCol("fahb", "Faz_id", "lie_id in ("+SelIds+")")));
		} catch (NumberFormatException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		
//		增加两个条件项，如果结算类型是煤款，没事。如果结算类型是国铁、或是两票或地铁结算 加danjcpb,yunfdjb其中费用类别国铁，yunfjsb_id=0 or null
		String contion_table="";//
		String contion_where="";
		long Yunf_Jieslx=Jieslx;	//为了处理两票结算为了得到运费单据表中的费用类型是“国铁”的数据，在此要进行类型转换
		if(Jieslx==Locale.guotyf_feiylbb_id||Jieslx==Locale.liangpjs_feiylbb_id||Jieslx==Locale.daozdt_feiylbb_leib){
//			运费结算和两票结算
			
			if(Jieslx==Locale.liangpjs_feiylbb_id){
				
				Yunf_Jieslx=Locale.guotyf_feiylbb_id;
			}
			
//			contion_table=",(select c.id as chepb_id,yfzl.jifzl from chepb c,yunfjszlb yfzl \n"+	
//                "       		where c.id=yfzl.chepb_id \n" +														
//                "       			and yfzl.feiylbb_id="+Yunf_Jieslx+") yfzl,\n" +
//                "		(select distinct cp.id,dj.yunfjsb_id\n" +
//                "                  from chepb cp,yunfdjb yd,danjcpb dj,fahb f,zhilb z,yansbhb ys,\n" +
//                "						gongysb g,meikxxb m\n" + 
//                "                  where yd.id=dj.yunfdjb_id\n" + 
//                "                        and dj.chepb_id=cp.id\n" + 
//                "                        and f.id=cp.fahb_id\n" + 
//                "						 and f.gongysb_id=g.id\n" +
//                "						 and f.meikxxb_id=m.id\n" +
//                "                        and yd.feiylbb_id="+Yunf_Jieslx+"\n" +
//                "						 and f.lie_id in ("+SelIds+") \n" + 
//                "     ) djcp";
//			
//			contion_where=
//				"       and cp.id=djcp.id(+)\n" + 
//				"       and (djcp.yunfjsb_id is null or djcp.yunfjsb_id=0) \n" + 
//				"		and cp.id=yfzl.chepb_id(+) ";
		}
		
		StringBuffer sql=new StringBuffer("");
		
		if(Jieszbsftz.equals("否")){
			
			sql.append(" select nvl(Qnetar_cf,0) as Qnetar_cf,nvl(Qnetar_kf,0) as Qnetar_kf,nvl(Std_cf,0) as Std_cf,nvl(Std_kf,0) as Std_kf,nvl(Mt_cf,0) as Mt_cf,nvl(Mt_kf,0) as Mt_kf,nvl(Mad_cf,0) as Mad_cf,nvl(Mad_kf,0) as Mad_kf,nvl(Aar_cf,0) as Aar_cf,nvl(Aar_kf,0) as Aar_kf,nvl(Aad_cf,0) as Aad_cf,nvl(Aad_kf,0) as Aad_kf,nvl(Ad_cf,0) as Ad_cf,				\n");
			sql.append(" 		nvl(Ad_kf,0) as Ad_kf,nvl(Vad_cf,0) as Vad_cf,nvl(Vad_kf,0) as Vad_kf,nvl(Vdaf_cf,0) as Vdaf_cf,nvl(Vdaf_kf,0) as Vdaf_kf,nvl(Stad_cf,0) as Stad_cf,nvl(Stad_kf,0) as Stad_kf,/*nvl(star_cf,0) as star_cf,nvl(star_kf,0) as star_kf,*/nvl(Had_cf,0) as Had_cf,nvl(Had_kf,0) as Had_kf,nvl(Qbad_cf,0) as Qbad_cf,nvl(Qbad_kf,0) as Qbad_kf,nvl(Qgrad_cf,0) as Qgrad_cf,nvl(Qgrad_kf,0) as Qgrad_kf,nvl(T2_cf,0) as T2_cf,nvl(T2_kf,0) as T2_kf,	\n");
			sql.append(" 		yuns,jingz,koud,kous,kouz,ches,biaoz,yingk,jiessl,(jiessl-jingz) as jieslcy,yunfjssl,chaokdl,yingd,yingd-yingk as kuid from (select "+Shoumjsdcz.getJiesszzh(jies_Jieslqzfs, "yuns", jies_Jsslblxs)+" as yuns,"+Shoumjsdcz.getJiesszzh(jies_Guohlqzfs,"jingz",jies_Guohlblxsw)+" as jingz,						\n");
			sql.append(			Shoumjsdcz.getJiesszzh(jies_Kdkskzqzfs, "koud", jies_Jsslblxs)+" as koud,"+Shoumjsdcz.getJiesszzh(jies_Kdkskzqzfs, "kous", jies_Jsslblxs)+" as kous,"+Shoumjsdcz.getJiesszzh(jies_Kdkskzqzfs, "kouz", jies_Jsslblxs)+" as kouz,	\n");
			sql.append(" 		sum(ches) as ches, "+Shoumjsdcz.getJiesszzh(jies_Jieslqzfs, "biaoz", jies_Jsslblxs)+" as biaoz,"+Shoumjsdcz.getJiesszzh(jies_Jieslqzfs, "jingz+yuns-biaoz", jies_Jsslblxs)+" as yingk,"+Shoumjsdcz.getJiesszzh(jies_Jieslqzfs, "yingd", jies_Jsslblxs)+" as yingd, 	\n");
			sql.append(         Shoumjsdcz.getJiesszzh(jies_Jieslqzfs, jies_Jssl, jies_Jsslblxs)).append(" as jiessl,   \n");
			sql.append(         Shoumjsdcz.getJiesszzh(jies_Jieslqzfs, jies_yunfjssl, jies_Jsslblxs)).append(" as yunfjssl,sum(chaokdl) as chaokdl,   \n");
			sql.append("	 --厂方验收   																															\n");
		//round_new(sum(jingz*round(z.qnet_ar*1000/4.1816,0))/sum((jingz)),0))
			sql.append("	 decode(sum(jingz),0,0,round_new(sum("+jies_Jqsl+"*round(z.qnet_ar*1000/4.1816,0))/sum(("+jies_Jqsl+")),0)) as Qnetar_cf,    		\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.std)/sum(("+jies_Jqsl+")),"+jies_Stdblxs+")) as Std_cf,   		   		\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.mt)/sum(("+jies_Jqsl+")),"+jies_Mtblxs+")) as Mt_cf,		  			 	\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.mad)/sum(("+jies_Jqsl+")),"+jies_Madblxs+")) as Mad_cf,	  			 	\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.aar)/sum(("+jies_Jqsl+")),"+jies_Aarblxs+")) as Aar_cf,	  			 	\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.aad)/sum(("+jies_Jqsl+")),"+jies_Aadblxs+")) as Aad_cf,	  			 	\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.ad)/sum(("+jies_Jqsl+")),"+jies_Adblxs+")) as Ad_cf,		  			 	\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.vad)/sum(("+jies_Jqsl+")),"+jies_Vadblxs+")) as Vad_cf,	  			 	\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.vdaf)/sum(("+jies_Jqsl+")),"+jies_Vdafblxs+")) as Vdaf_cf,  			 	\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.stad)/sum(("+jies_Jqsl+")),"+jies_Stadblxs+")) as Stad_cf, 			 	\n");
//			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.star)/sum(("+jies_Jqsl+")),"+jies_Starblxs+")) as Star_cf, 			 	\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.had)/sum(("+jies_Jqsl+")),"+jies_Hadblxs+")) as Had_cf,	 			 	\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.qbad)/sum(("+jies_Jqsl+")),"+jies_Qbadblxs+")) as Qbad_cf, 			 	\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.qgrad)/sum(("+jies_Jqsl+")),"+jies_Qgradblxs+")) as Qgrad_cf,		 	\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.t2)/sum(("+jies_Jqsl+")),"+jies_T2blxs+")) as T2_cf,				 		\n");
			sql.append("	 --矿方验收   \n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.qnet_ar)/sum(("+jies_Jqsl+")),"+jies_Qnetarblxs+")) as Qnetar_kf,  		\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.std)/sum(("+jies_Jqsl+")),"+jies_Stdblxs+")) as Std_kf,   		   		\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.mt)/sum(("+jies_Jqsl+")),"+jies_Mtblxs+")) as Mt_kf,	  			 	\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.mad)/sum(("+jies_Jqsl+")),"+jies_Madblxs+")) as Mad_kf,	  			 	\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.aar)/sum(("+jies_Jqsl+")),"+jies_Aarblxs+")) as Aar_kf,	  			 	\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.aad)/sum(("+jies_Jqsl+")),"+jies_Aadblxs+")) as Aad_kf,	  			 	\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.ad)/sum(("+jies_Jqsl+")),"+jies_Adblxs+")) as Ad_kf,	  			 	\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.vad)/sum(("+jies_Jqsl+")),"+jies_Vadblxs+")) as Vad_kf,	  			 	\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.vdaf)/sum(("+jies_Jqsl+")),"+jies_Vdafblxs+")) as Vdaf_kf,  			\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.stad)/sum(("+jies_Jqsl+")),"+jies_Stadblxs+")) as Stad_kf, 			 	\n");
//			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.star)/sum(("+jies_Jqsl+")),"+jies_Starblxs+")) as Star_kf, 			 	\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.had)/sum(("+jies_Jqsl+")),"+jies_Hadblxs+")) as Had_kf,	 			 	\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.qbad)/sum(("+jies_Jqsl+")),"+jies_Qbadblxs+")) as Qbad_kf, 			 	\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.qgrad)/sum(("+jies_Jqsl+")),"+jies_Qgradblxs+")) as Qgrad_kf,		 	\n");
			sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.t2)/sum(("+jies_Jqsl+")),"+jies_T2blxs+")) as T2_kf					 	\n");
			sql.append("	 	from (select distinct f.id,f.yuanid,f.diancxxb_id,f.gongysb_id,f.meikxxb_id,f.pinzb_id,f.faz_id,									\n");
			sql.append("				f.daoz_id,f.jihkjb_id,f.fahrq,f.daohrq,f.hetb_id,f.zhilb_id,f.jiesb_id,f.yunsfsb_id,f.chec,									\n");
			sql.append("       			f.maoz as maoz,f.piz as piz,f.jingz as jingz,												\n");
			
//			if(!(Jieslx==Locale.guotyf_feiylbb_id||Jieslx==Locale.liangpjs_feiylbb_id||Jieslx==Locale.daozdt_feiylbb_leib)){
////				如果不是结算煤款或两票结算则取原票重
				sql.append("			f.biaoz as biaoz,	\n");
//			}else{
////				如果是则取调整后的票重
//				sql.append("			decode(yfzl.jifzl,null,f.biaoz,yfzl.jifzl) as biaoz,	\n");
//			}
			
			sql.append("       			f.yingd as yingd,f.yingk as yingk,f.yuns as yuns,f.koud as koud,f.kous as kous,				\n");
			sql.append("       			f.kouz as kouz,f.koum as koum,f.zongkd as zongkd,f.sanfsl as sanfsl,f.ches as ches,			\n");
			sql.append("       			0 as chaokdl, 						\n");
			sql.append("				f.tiaozbz,f.yansbhb_id,f.lie_id,f.yuandz_id,f.yuanshdwb_id,f.kuangfzlb_id,f.liucb_id,f.liucztb_id,f.hedbz,f.beiz,			\n");
			sql.append("       			f.ruccbb_id,f.ditjsbz,f.ditjsb_id,f.laimsl,f.laimzl,f.laimkc																\n");
			sql.append(" 			from fahb f"+contion_table+" where  "+contion_where+"  f.lie_id in("+SelIds+")"+yunsdw+" 													\n"); 
//			sql.append(" 			group by f.id,f.yuanid,f.diancxxb_id,f.gongysb_id,f.meikxxb_id,f.pinzb_id,f.faz_id,												\n");
//			sql.append("       				f.daoz_id,f.jihkjb_id,f.fahrq,f.daohrq,f.hetb_id,f.zhilb_id,f.jiesb_id,f.yunsfsb_id,f.chec,								\n");
//			sql.append("       				f.tiaozbz,f.yansbhb_id,f.lie_id,f.yuandz_id,f.yuanshdwb_id,f.kuangfzlb_id,f.liucb_id,f.liucztb_id,f.hedbz,f.beiz,		\n");
//			sql.append("       				f.ruccbb_id,f.ditjsbz,f.ditjsb_id,f.laimsl,f.laimzl,f.laimkc															\n");
			sql.append("			) f,zhilb z,gongysb g,meikxxb m,chezxxb cz,vwyuanshdw vwydw,pinzb pz,yunsfsb ysfs,chezxxb dz,  									\n");
			sql.append("	 	(select distinct f.id as fahb_id,k.* from fahb f,kuangfzlb k where f.kuangfzlb_id=k.id "+yunsdw+" and f.lie_id in ("+SelIds+")) kz			\n");
			sql.append("			where f.zhilb_id=z.id and f.id=kz.fahb_id(+) and f.faz_id=cz.id and f.pinzb_id=pz.id   								\n");
			sql.append("				and f.gongysb_id=g.id and f.meikxxb_id=m.id and f.yuanshdwb_id=vwydw.id(+) and f.yunsfsb_id=ysfs.id and f.daoz_id=dz.id   	\n");
			sql.append("				and f.liucztb_id=0 and z.liucztb_id=1 and f.lie_id in("+SelIds+")) 	\n");
		
		}else if(Jieszbsftz.equals("是")){
			
			sql.append(" select nvl(Qnetar_cf,0) as Qnetar_cf,nvl(Qnetar_kf,0) as Qnetar_kf,nvl(Qnetar_js,0) as Qnetar_js,nvl(Std_cf,0) as Std_cf,nvl(Std_kf,0) as Std_kf,nvl(Std_js,0) as Std_js,\n ");
			sql.append("		nvl(Mt_cf,0) as Mt_cf,nvl(Mt_kf,0) as Mt_kf,nvl(Mt_js,0) as Mt_js,nvl(Mad_cf,0) as Mad_cf,nvl(Mad_kf,0) as Mad_kf,nvl(Mad_js,0) as Mad_js,nvl(Aar_cf,0) as Aar_cf,\n ");
			sql.append("		nvl(Aar_kf,0) as Aar_kf,nvl(Aar_js,0) as Aar_js,nvl(Aad_cf,0) as Aad_cf,nvl(Aad_kf,0) as Aad_kf,nvl(Aad_js,0) as Aad_js,nvl(Ad_cf,0) as Ad_cf,nvl(Ad_kf,0) as Ad_kf,nvl(Ad_js,0) as Ad_js,	\n");
			sql.append(" 		nvl(Vad_cf,0) as Vad_cf,nvl(Vad_kf,0) as Vad_kf,nvl(Vad_js,0) as Vad_js,nvl(Vdaf_cf,0) as Vdaf_cf,nvl(Vdaf_kf,0) as Vdaf_kf,nvl(Vdaf_js,0) as Vdaf_js,nvl(Stad_cf,0) as Stad_cf, \n ");
			sql.append("		nvl(Stad_kf,0) as Stad_kf,nvl(Stad_js,0) as Stad_js,nvl(star_cf,0) as star_cf,nvl(star_kf,0) as star_kf,nvl(star_js,0) as star_js,nvl(Had_cf,0) as Had_cf,nvl(Had_kf,0) as Had_kf, \n ");
			sql.append("		nvl(Had_js,0) as Had_js,nvl(Qbad_cf,0) as Qbad_cf,nvl(Qbad_kf,0) as Qbad_kf,nvl(Qbad_js,0) as Qbad_js,nvl(Qgrad_cf,0) as Qgrad_cf,nvl(Qgrad_kf,0) as Qgrad_kf,nvl(Qgrad_js,0) as Qgrad_js, \n ");
			sql.append("		nvl(T2_cf,0) as T2_cf,nvl(T2_kf,0) as T2_kf,nvl(T2_js,0) as T2_js,	\n");
			sql.append(" 		yuns,jingz,koud,kous,kouz,ches,Jiessl_kf as biaoz,(jingz+yuns-Jiessl_kf) as yingk,(jingz+yuns) as yanssl,Jiessl_js as jiessl,(Jiessl_js-jingz) as jieslcy,yunfjssl,chaokdl,yingd,yingd-(jingz+yuns-Jiessl_kf) as kuid from (select "+Shoumjsdcz.getJiesszzh(jies_Jieslqzfs, "yuns", jies_Jsslblxs)+" as yuns,"+Shoumjsdcz.getJiesszzh(jies_Guohlqzfs,"jingz",jies_Guohlblxsw)+" as jingz,						\n");
			sql.append(			Shoumjsdcz.getJiesszzh(jies_Kdkskzqzfs, "koud", jies_Jsslblxs)+" as koud,"+Shoumjsdcz.getJiesszzh(jies_Kdkskzqzfs, "kous", jies_Jsslblxs)+" as kous,"+Shoumjsdcz.getJiesszzh(jies_Kdkskzqzfs, "kouz", jies_Jsslblxs)+" as kouz,	\n");
			sql.append(" 		sum(ches) as ches,"+Shoumjsdcz.getJiesszzh(jies_Jieslqzfs, "yingd", jies_Jsslblxs)+" as yingd, 	\n");
			sql.append(         Shoumjsdcz.getJiesszzh(jies_Jieslqzfs, jies_yunfjssl, jies_Jsslblxs)).append(" as yunfjssl,sum(chaokdl) as chaokdl,   \n");
			sql.append("	 --厂方验收   																															\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Qnetar_zhibb+"','changf') as Qnetar_cf,   		\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Std_zhibb+"','changf') as Std_cf,   		   		\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Mt_zhibb+"','changf') as Mt_cf,		  			\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Mad_zhibb+"','changf') as Mad_cf,	  			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Aar_zhibb+"','changf') as Aar_cf,	  			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Aad_zhibb+"','changf') as Aad_cf,	  			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Ad_zhibb+"','changf') as Ad_cf,		  			\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Vad_zhibb+"','changf') as Vad_cf,	  			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Vdaf_zhibb+"','changf') as Vdaf_cf,  			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Stad_zhibb+"','changf') as Stad_cf, 			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Star_zhibb+"','changf') as Star_cf, 			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Had_zhibb+"','changf') as Had_cf,	 			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Qbad_zhibb+"','changf') as Qbad_cf, 			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Qgrad_zhibb+"','changf') as Qgrad_cf,		 		\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.T2_zhibb+"','changf') as T2_cf,				 	\n");
			sql.append("	 --矿方验收   \n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.jiessl_zhibb+"','gongf') as Jiessl_kf,   			\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Qnetar_zhibb+"','gongf') as Qnetar_kf,   			\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Std_zhibb+"','gongf') as Std_kf,   		   		\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Mt_zhibb+"','gongf') as Mt_kf,		  			\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Mad_zhibb+"','gongf') as Mad_kf,	  			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Aar_zhibb+"','gongf') as Aar_kf,	  			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Aad_zhibb+"','gongf') as Aad_kf,	  			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Ad_zhibb+"','gongf') as Ad_kf,		  			\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Vad_zhibb+"','gongf') as Vad_kf,	  			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Vdaf_zhibb+"','gongf') as Vdaf_kf,  			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Stad_zhibb+"','gongf') as Stad_kf, 			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Star_zhibb+"','gongf') as Star_kf, 			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Had_zhibb+"','gongf') as Had_kf,	 			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Qbad_zhibb+"','gongf') as Qbad_kf, 			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Qgrad_zhibb+"','gongf') as Qgrad_kf,		 		\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.T2_zhibb+"','gongf') as T2_kf,				 	\n");
			sql.append("	 --结算指标   \n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.jiessl_zhibb+"','jies') as Jiessl_js,   			\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Qnetar_zhibb+"','jies') as Qnetar_js,   			\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Std_zhibb+"','jies') as Std_js,   		   		\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Mt_zhibb+"','jies') as Mt_js,		  				\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Mad_zhibb+"','jies') as Mad_js,	  			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Aar_zhibb+"','jies') as Aar_js,	  			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Aad_zhibb+"','jies') as Aad_js,	  			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Ad_zhibb+"','jies') as Ad_js,		  				\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Vad_zhibb+"','jies') as Vad_js,	  			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Vdaf_zhibb+"','jies') as Vdaf_js,  			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Stad_zhibb+"','jies') as Stad_js, 			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Star_zhibb+"','jies') as Star_js, 			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Had_zhibb+"','jies') as Had_js,	 			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Qbad_zhibb+"','jies') as Qbad_js, 			 	\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.Qgrad_zhibb+"','jies') as Qgrad_js,		 		\n");
			sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.T2_zhibb+"','jies') as T2_js				 		\n");
			
			sql.append("	 	from (select distinct f.id,f.yuanid,f.diancxxb_id,f.gongysb_id,f.meikxxb_id,f.pinzb_id,f.faz_id,									\n");
			sql.append("				f.daoz_id,f.jihkjb_id,f.fahrq,f.daohrq,f.hetb_id,f.zhilb_id,f.jiesb_id,f.yunsfsb_id,f.chec,									\n");
			sql.append("       			sum(cp.maoz) as maoz,sum(cp.piz) as piz,sum(cp.maoz-cp.piz-cp.zongkd) as jingz,												\n");
			
			if(contion_table.equals("")){
//				如果不是结算煤款或两票结算则取原票重
				sql.append("			sum(cp.biaoz) as biaoz,	\n");
			}else{
//				如果是则取调整后的票重
				sql.append("			sum(decode(yfzl.jifzl,null,cp.biaoz,yfzl.jifzl)) as biaoz,	\n");
			}
			
			sql.append("       			sum(cp.yingd) as yingd,sum(cp.yingk) as yingk,sum(cp.yuns) as yuns,sum(cp.koud) as koud,sum(cp.kous) as kous,				\n");
			sql.append("       			sum(cp.kouz) as kouz,sum(cp.koum) as koum,sum(cp.zongkd) as zongkd,sum(cp.sanfsl) as sanfsl,count(cp.id) as ches,			\n");
			sql.append("       			sum(nvl(getChaodkd(cp.id,'biaoz','"+jies_Jssl+"','"+Locale.sheq_ht_xscz+"','"+ChaodOrKuid+"'),0)) as chaokdl, 				\n");
			sql.append("       			f.tiaozbz,f.yansbhb_id,f.lie_id,f.yuandz_id,f.yuanshdwb_id,f.kuangfzlb_id,f.liucb_id,f.liucztb_id,f.hedbz,f.beiz,			\n");
			sql.append("       			f.ruccbb_id,f.ditjsbz,f.ditjsb_id,f.laimsl,f.laimzl,f.laimkc																\n");
			sql.append(" 			from fahb f,chepb cp"+contion_table+" where f.id=cp.fahb_id "+contion_where+" and f.lie_id in("+SelIds+")"+yunsdw+" 			\n"); 
			sql.append(" 			group by f.id,f.yuanid,f.diancxxb_id,f.gongysb_id,f.meikxxb_id,f.pinzb_id,f.faz_id,												\n");
			sql.append("       				f.daoz_id,f.jihkjb_id,f.fahrq,f.daohrq,f.hetb_id,f.zhilb_id,f.jiesb_id,f.yunsfsb_id,f.chec,								\n");
			sql.append("       				f.tiaozbz,f.yansbhb_id,f.lie_id,f.yuandz_id,f.yuanshdwb_id,f.kuangfzlb_id,f.liucb_id,f.liucztb_id,f.hedbz,f.beiz,		\n");
			sql.append("       				f.ruccbb_id,f.ditjsbz,f.ditjsb_id,f.laimsl,f.laimzl,f.laimkc															\n");
			sql.append("			) f,yansbhb y,gongysb g,meikxxb m,chezxxb cz,vwyuanshdw vwydw,pinzb pz,yunsfsb ysfs,chezxxb dz  								\n");
			sql.append("			where f.faz_id=cz.id and f.pinzb_id=pz.id and f.yansbhb_id=y.id \n");
			sql.append("				and f.gongysb_id=g.id and f.meikxxb_id=m.id and f.yuanshdwb_id=vwydw.id(+) and f.yunsfsb_id=ysfs.id and f.daoz_id=dz.id   	\n");
			sql.append("				and f.liucztb_id=1").append(Tsclzb_where);
			sql.append("				and f.lie_id in("+SelIds+")) 	\n");
		}
		
		return sql;
	}
	
	public static void setJieszbzdj_Tszb(String zhibbm,Balances_variable bsv,double zhedj,double zhibjsbz,double zhibzje){
//		函数功能：
//			1、特殊指标经过特殊处理计算后，得到该指标的折单价信息，折金额信息，将此值赋值给bsv
//			2、将特殊指标的值当作是结算值付给该相应变量
//		函数逻辑：
//			如果“zhibbm”等于某一个指标时，就将折单价赋值给该指标的折单价
//		函数形参：
//			zhibbm要赋值的指标编码，bsv全局类，zhedj指标折单价，zhibjsbz指标结算值，zhibzje指标折金额
		
		if(zhibbm.equals(Locale.Qnetar_zhibb)){
			
			bsv.setQnetar_zdj_tscl(zhedj);
			bsv.setQnetar_zje_tscl(zhibzje);
			
		}else if(zhibbm.equals(Locale.Std_zhibb)){
			
			bsv.setStd_zdj_tscl(zhedj);
			bsv.setStd_zje_tscl(zhibzje);
			
		}else if(zhibbm.equals(Locale.Ad_zhibb)){
			
			bsv.setAd_zdj_tscl(zhedj);
			bsv.setAd_zje_tscl(zhibzje);

		}else if(zhibbm.equals(Locale.Vdaf_zhibb)){
			
			bsv.setVdaf_zdj_tscl(zhedj);
			bsv.setVdaf_zje_tscl(zhibzje);

		}else if(zhibbm.equals(Locale.Mt_zhibb)){
			
			bsv.setMt_zdj_tscl(zhedj);
			bsv.setMt_zje_tscl(zhibzje);

		}else if(zhibbm.equals(Locale.Qgrad_zhibb)){
			
			bsv.setQgrad_zdj_tscl(zhedj);
			bsv.setQgrad_zje_tscl(zhibzje);
			
		}else if(zhibbm.equals(Locale.Qbad_zhibb)){
			
			bsv.setQbad_zdj_tscl(zhedj);
			bsv.setQbad_zje_tscl(zhibzje);

		}else if(zhibbm.equals(Locale.Had_zhibb)){
			
			bsv.setHad_zdj_tscl(zhedj);
			bsv.setHad_zje_tscl(zhibzje);

		}else if(zhibbm.equals(Locale.Stad_zhibb)){
			
			bsv.setStad_zdj_tscl(zhedj);
			bsv.setStad_zje_tscl(zhibzje);
			
		}else if(zhibbm.equals(Locale.Mad_zhibb)){
			
			bsv.setMad_zdj_tscl(zhedj);
			bsv.setMad_zje_tscl(zhibzje);
			
		}else if(zhibbm.equals(Locale.Aar_zhibb)){
			
			bsv.setAar_zdj_tscl(zhedj);
			bsv.setAar_zje_tscl(zhibzje);
			
		}else if(zhibbm.equals(Locale.Aad_zhibb)){
			
			bsv.setAad_zdj_tscl(zhedj);
			bsv.setAad_zje_tscl(zhibzje);

		}else if(zhibbm.equals(Locale.Vad_zhibb)){
			
			bsv.setVad_zdj_tscl(zhedj);
			bsv.setVad_zje_tscl(zhibzje);
			
		}else if(zhibbm.equals(Locale.T2_zhibb)){
			
			bsv.setT2_zdj_tscl(zhedj);
			bsv.setT2_zje_tscl(zhibzje);
			
		}else if(zhibbm.equals(Locale.jiessl_zhibb)){
			
			bsv.setShul_zdj_tscl(zhedj);
			bsv.setShul_zje_tscl(zhibzje);
			
		}else if(zhibbm.equals(Locale.Yunju_zhibb)){
//			运距折单价
			bsv.setYunju_zdj_tscl(zhedj);
			bsv.setYunju_zje_tscl(zhibzje);
			
			if(bsv.getShifykfzljs().equals("是")){
				
				bsv.setYunju_kf(zhibjsbz);
			}else{
				
				bsv.setYunju_cf(zhibjsbz);
			}
			
			bsv.setYunju_js(zhibjsbz);
			
			bsv.setYunju_yk(reCoundYk(bsv.getYunju_ht(),bsv.getYunju_js(),bsv.getYunju_yk()));
			
		}else if(zhibbm.equals(Locale.Star_zhibb)){
//			Star折单价
			bsv.setStar_zdj_tscl(zhedj);
			bsv.setStar_zje_tscl(zhibzje);
		}
	}
	
	public static void UpdateDanpcjsmkb(long jiesdid,double hansdj,double jiakhj,
			double jiaksk,double jiashj,double jiajqdj){
//		函数功能：
//			当单结算煤款时，如果煤价为到厂价时说明里面含有运费，要在最后的recount方法中
//				用含税总煤款-运费合计=含税总煤款。但这时已经在Danpcjsmkb存入了含运费的煤款信息了，
//				所以要在减去运费后更新Danpcjsmkb里的内容。
//		函数逻辑：
//			根据jiesdid的值更新Danpcjsmkb中的记录
//		函数形参：
//			
		JDBCcon con = new JDBCcon();
		String sql="update danpcjsmxb set jiesdj="+hansdj+", jiakhj="+jiakhj+", jiaksk="+jiaksk+", jiashj="
				+jiashj+", zongje="+jiashj+", jiajqdj="+jiajqdj+" where jiesdid="+jiesdid;
		
		con.getUpdate(sql);
		
		con.Close();
	}
}