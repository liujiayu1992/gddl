package com.zhiren.dc.hesgl.jiesd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import bsh.EvalError;
import bsh.Interpreter;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DataBassUtil;
import com.zhiren.common.DateUtil;
import com.zhiren.common.FileNameFilter;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Liuc;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.jt.jiesgl.changfhs.Jiesdyzbean;
import com.zhiren.main.Visit;
import com.zhiren.shihs.hesgl.Balances_variable_Shih;

/*
 * ���ߣ����پ�
 * ʱ�䣺2012-05-22
 * ���������Ӷ�visit.getString19Ϊnull���ж�
 */

/**
 * ���ߣ�������
 * ʱ�䣺2012-08-14
 * �������������ƹ��������Ƿ����ȫˮ���ˣ����п�ˮ��
 */
/**
 * ���ߣ����
 * ʱ�䣺2012-12-01
 * �������������ƹ��������ȵ��Ƿ�����������á�
 * 		MainGlobal.getXitxx_item("����", "�����������", "0", "")
 */
/**
 * ���ߣ����
 * ʱ�䣺2014-02-18
 * ������ʹ�ò������ƽ�����ֵ��ת�׽�ʱ��С��λ����λ����Ĭ��ֵΪ0
 * 		MainGlobal.getXitxx_item("����", "������ֵ�󿨱���С��λ", "0", "0");
 */

public class Jiesdcz extends BasePage{
	
//	����ģ������й��÷���
	
	public String SetJieszb(String Zhibmc, String Zhib_ht, String Zhib_kf, String Zhib_cf, 
			String Zhib_js, String Zhib_yk, String Zhib_zdj, String Zhib_zje, 
			String Zhib_ht_value, double Zhib_kf_value, double Zhib_cf_value, 
			double Zhib_js_value, double Zhib_yk_value, double Zhib_zdj_value, 
			double Zhib_zje_value){

		//��ʼ������ָ��
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
	
	public static String Regular_Ht(String value){
//		�������ܣ�
//			�淶���ֵ���ʾ��ʽ�����Դ����ָ������������£�
//			1�����㵥��ʾʱ������ͬ�涨ָ�����ĳһ��ֵʱ����û�й涨���޵�����£���ֵͬ����ʾ������ 5000-0
//		�����߼���
//			���1��
//				�ж��Ƿ�������ӷ�����������ж��Ҷ˵����Ƿ�Ϊ0�����Ϊ0����ȥ��������
//		�����βΣ�
//			���1��
//				value��Ҫ����ĺ�ֵͬ
//		
//		���1��
		if(value.indexOf("-")>-1){
			
			if(Double.parseDouble(value.substring(value.indexOf("-")+1))==0){
//				������ִ����м����ӷ������Ҳ������ֵΪ0������ȥ���ӷ�������
				value = value.substring(0,value.indexOf("-"));
			}
		}
		
		return value;
	}
	
	public String SetHejdxh(String Shifxsckd,double chaokdl, double hej, String hejdx){
		
//		�Ƿ���ʾ����/��������
		StringBuffer Stbf=new StringBuffer();
		
		if(Shifxsckd.equals("")){
//			��\���ֱ�ʶ��Ϊ�գ�����ʾ
			Stbf.append("<tr>");
			Stbf.append("	<td class='Jsdtdborder'>�ϼƴ�д</td>");
			Stbf.append("	<td colspan='5'  class='Jsdtdborder'><input  readonly ='true' class='noeditinput' style='width:100%' id='DAXHJ' value='"+hejdx+"' type='text' /></td>");
			Stbf.append("	<td class='Jsdtdborder'>�ϼ�Сд</td>");
			Stbf.append("	<td colspan='2'  class='Jsdtdborder'><input  readonly ='true' class='noeditinput' style='width:100%' id='HEJ'	value='"+hej+"' type='text' /></td>");
			Stbf.append("</tr>");
		}else{
			
			Stbf.append("<tr>");
			Stbf.append("	<td class='Jsdtdborder'>�ϼƴ�д</td>");
			Stbf.append("	<td colspan='4'  class='Jsdtdborder'><input  readonly ='true' class='noeditinput' style='width:100%' id='DAXHJ'	value='"+hejdx+"' type='text' /></td>");
			Stbf.append("	<td class='Jsdtdborder'>�ϼ�Сд</td>");
			Stbf.append("	<td class='Jsdtdborder'><input  readonly ='true' class='noeditinput' style='width:100%' id='HEJ' value='"+hej+"' type='text' /></td>");
			Stbf.append("	<td class='Jsdtdborder'>��/������(��)</td>");
			Stbf.append("	<td class='Jsdtdborder'><input  readonly ='true' class='noeditinput' style='width:100%' id='CHAOKDL' value='"+chaokdl+"' type='text' /></td>");
			Stbf.append("</tr>");
		}
		
		return	Stbf.toString();    
	}
	
	public long Feiylbb_transition(long feiylbb_id){
		
//		���feiylbb����Ʊ������ڱ�������˷ѵ�ʱ�����������תΪ3
		if(feiylbb_id==1){
//			��Ʊ����
			feiylbb_id=3;
		} else if (feiylbb_id==7) { // ���feiylbb�Ǻ����˷Ѿ��ڱ�������˷ѵ�ʱ�����������תΪ3
			feiylbb_id=3;
		}
		return feiylbb_id;
	}
	
	public static boolean isFengsj(String Diancxxb_id){
//	�������ܣ�
//		�ж�ĳһ��diancxxb_id�Ƿ�Ϊ�ֹ�˾��id
//	�����߼���
//		1���ж��ββ�Ϊ�գ����β�������
//		2�����Ҹ�id�ļ������С�ڵ���2˵���Ƿֹ�˾��ֱ�ӷ���true
//		3�������id�ļ���Ϊ3���¼��Ϊ�գ�ֱ�ӷ���false
//	�����βΣ�
//		Diancxxb_id Ҫ�жϵ�diancxxb_id
		
		boolean Flag = false;
		
		if(!Diancxxb_id.equals("")&&Diancxxb_id.matches("\\d+")){
			
			JDBCcon con = new JDBCcon();
			try{
				
				String sql = "select jib from diancxxb where id="+Diancxxb_id;
				ResultSet rs = con.getResultSet(sql);
				if(rs.next()){
					
					if(rs.getInt("jib")<=2){
						
						Flag = true;
					}
				}
				rs.close();
			}catch(Exception e){
				
				e.printStackTrace();
			}finally{
				
				con.Close();
			}
		}
		
		return Flag;
	}
	
	public static boolean isDiancj(String Diancxxb_id){
//	�������ܣ�
//		�ж�ĳһ��diancxxb_id�Ƿ�Ϊ�ֹ�˾��id
//	�����߼���
//		1���ж��ββ�Ϊ�գ����β�������
//		2�����Ҹ�id�ļ������С�ڵ���2˵���Ƿֹ�˾��ֱ�ӷ���true
//		3�������id�ļ���Ϊ3���¼��Ϊ�գ�ֱ�ӷ���false
//	�����βΣ�
//		Diancxxb_id Ҫ�жϵ�diancxxb_id
		
		boolean Flag = false;
		if(!Diancxxb_id.equals("")&&Diancxxb_id.matches("\\d+")){
		
			JDBCcon con = new JDBCcon();
			try{
				
				String sql = "select jib from diancxxb where id="+Diancxxb_id;
				ResultSet rs = con.getResultSet(sql);
				if(rs.next()){
					
					if(rs.getInt("jib")==3){
						
						Flag = true;
					}
				}
				rs.close();
			}catch(Exception e){
				
				e.printStackTrace();
			}finally{
				
				con.Close();
			}
		}
		
		return Flag;
	}
	
	public Interpreter getYunfgsjx(long Diancxxb_id,long Faz_id,long Daoz_id,
			String Biaoz,String cheb,String SelectChepbId,String Lianpdp) {
		// TODO �Զ����ɷ������
//		���û�������feiyxm�󣬴����ﹹ��bsh�������������
		JDBCcon con=new JDBCcon();
		Interpreter bsh = new Interpreter();
		String strChes[]=SelectChepbId.split(",");
		try{
//			String tmp[][]=this.getChepb_info("distinct",2,"decode(c.chebb_id,1,'·��',2,'�Ա���',3,'��',4,'��') as cheb^~c.biaoz^", SelectChepbId);
			//��ʽ���õ�����Ŀ
//			�����˳��𡢱��ء�������͡����ֵ������
			
			bsh.set("����",cheb);
			bsh.set("Ʊ��",Double.parseDouble(Biaoz));
			
			if(Lianpdp.equals("dp")){
				
				bsh.set("����", 1);
			}else if(Lianpdp.equals("lp")){
				
				bsh.set("����", strChes.length);
			}
			
			
			
//			1������̾���
			String sql="select lclx.mingc,lc.zhi from licb lc,liclxb lclx \n" 
				       + " where lc.liclxb_id=lclx.id and (lc.diancxxb_id="+Diancxxb_id
				       + " 		or lc.diancxxb_id in (select id from diancxxb where jib=3 and id in (select fuid from diancxxb where id = "+Diancxxb_id+"))) \n " 
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
		// TODO �Զ����ɷ������
//		���û�������feiyxm�󣬴����ﹹ��bsh�������������
		JDBCcon con=new JDBCcon();
		Interpreter bsh = new Interpreter();
		try{
//			�÷������𲻲�����㣬ֻ�ܸ�����Ʊ�غ���̼����˷�
//			String tmp[][]=this.getChepb_info("distinct",2,"decode(c.chebb_id,1,'·��',2,'�Ա���',3,'��',4,'��') as cheb^~c.biaoz^", SelectChepbId);
			//��ʽ���õ�����Ŀ
//			�����˱��ء�������͡����ֵ
			bsh.set("Ʊ��",Biaoz);
			
			
//			1������̾���
			String sql="select lclx.mingc,lc.zhi from licb lc,liclxb lclx \n" 
				       + " where lc.liclxb_id=lclx.id and (lc.diancxxb_id="+Diancxxb_id
				       + " 		or lc.diancxxb_id in (select id from diancxxb where jib=3 and id in (select fuid from diancxxb where id = "+Diancxxb_id+"))) \n " 
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
		// TODO �Զ����ɷ������
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
//		���������
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
		// TODO �Զ����ɷ������
//		�����������ϸ��
		JDBCcon con=new JDBCcon();
		StringBuffer sbsql=new StringBuffer();
		ResultSetList rsl=null;
		boolean InsertKuangfzlb=false;
		long lngGongysb_id=0;
		long lngMeikxxb_id=0;
		long lngKuangfzlb_id=0;
//		�жϸ÷�������kuangfzl,���û��InsertKuangfzlb=true;
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
		// TODO �Զ����ɷ������
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
			// TODO �Զ����ɷ������
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
		
//		�������ܣ�
		
//				��ú���ͬ�û��Զ��塰�۸�ʽ�� ����һЩ�ؼ�������Ҫ����֧�ֵģ�
//			���ڵ���ͬ �ؼ��ֳƺ��಻ͬ������Ҫ��item������ת�����ܡ�
//			�˺���Ϊ�˽���ת��֮�á�
		
//		�����߼���

//				ͨ��itemsorf �� item ����й������ҵ��ؼ����飬
//			��item�еı���ȥ�滻item�е����ƣ��Զ���Ĺ�ʽ����ת����
//		�����βΣ�
		
//			jijgs ���۹�ʽ
		
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
//			Ĭ���С���ͬ�ۡ��Ĺ�ʽָ��
			jijgs=jijgs.replaceAll("��ͬ��", String.valueOf(hetj));
			
			jijgs="��ͬ�۸�="+jijgs+";";
			rsl.close();
			con.Close();
			
			jijgs="import com.zhiren.common.CustomMaths;\n "+getHetjgs_aide(diancxxb_id)+"\n"+jijgs;
		}
		
		return jijgs;
	}
	
	public static String getHetjgs_aide(long _Diancxxb_id) {
//		�õ���ͬ�۸������㹫ʽ
		JDBCcon con =new JDBCcon();
		String Gongs="";
		try {   
		   //ú����㹫ʽ
			ResultSet rs= con.getResultSet("select id from gongsb where mingc='��ͬ�۸�����ʽ' and leix='����' and zhuangt=1 and diancxxb_id="+_Diancxxb_id);
			if (rs.next()) {
		   	
				DataBassUtil clob=new DataBassUtil();
				Gongs=clob.getClob("gongsb", "gongs", rs.getLong(1));
			}
			rs.close();
			
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} catch (IOException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally{
			
			con.Close();
		}
	   
	   return Gongs;
	}
	
	public static boolean CheckMljRz(String Zhib) {
		// TODO �Զ����ɷ������
//		�ж��ǲ���Ŀ¼�۵���ֵ����
		if(Zhib.equals(Locale.Qnetar_zhibb)
				||Zhib.equals(Locale.Qgrad_zhibb)
				||Zhib.equals(Locale.Qbad_zhibb)){
			
			return true;
		}
		return false;
	}
	
	public static boolean CheckMljHff(String Zhib) {
		// TODO �Զ����ɷ������
//		�ж��ǲ���Ŀ¼�۵Ļӷ���
		if(Zhib.equals(Locale.Vdaf_zhibb)
				||Zhib.equals(Locale.Vad_zhibb)){
			
			return true;
		}
		return false;
	}
	
	public static boolean CheckMljLiuf(String Zhib) {
		// TODO �Զ����ɷ������
//		�ж��ǲ���Ŀ¼�۵Ļӷ���
		if(Zhib.equals(Locale.Std_zhibb)
				||Zhib.equals(Locale.Stad_zhibb)){
			
			return true;
		}
		return false;
	}
	
	public static boolean CheckMljHiuf(String Zhib) {
		// TODO �Զ����ɷ������
//		�ж��ǲ���Ŀ¼�۵Ļҷ�
		if(Zhib.equals(Locale.Ad_zhibb)
				||Zhib.equals(Locale.Aad_zhibb)
				||Zhib.equals(Locale.Aar_zhibb)){
			
			return true;
		}
		return false;
	}
	
	public static String getZhibbdw(String Zhibmc,String Danw) {
		// TODO �Զ����ɷ������
//		����ָ���Ӧ�ĵ�λ
		String strValue="";
		if(Zhibmc.equals(Locale.jiessl_zhibb)){
			
			if(Danw.equals("")){
				
				strValue=Locale.dun_danw;
			}else{
				
				strValue=Danw;
			}
			
		}else if(Zhibmc.equals(Locale.Qnetar_zhibb)||Zhibmc.equals(Locale.Qgrad_zhibb)||Zhibmc.equals(Locale.Qbad_zhibb)){
			
//			����
			if(Danw.equals("")){
				
				strValue=Locale.zhaojmqk_danw;
			}else{
				
				strValue=Danw;
			}
		}else if(Zhibmc.equals(Locale.T2_zhibb)){
			
//			���۵�
			strValue="";
		}else{
			
			strValue="";
		}
		
		return strValue;
	}
	
	public static double getUnit_transform(String zhibmc,String zhibdw, double zhi) {
		// TODO �Զ����ɷ������
//		��λת������
		if(zhibdw!=null){
			
			if(zhibmc.equals(Locale.Qnetar_zhibb)||zhibmc.equals(Locale.Qgrad_zhibb)||zhibmc.equals(Locale.Qbad_zhibb)){
				
				if(zhibdw.equals(Locale.qiankmqk_danw)){
//					�������������ֵ����λ���ǡ�ǧ��/ǧ�ˡ�,���׽�/ǧ��ת��Ϊǧ��/ǧ��
					String xsw=MainGlobal.getXitxx_item("����", "������ֵ�󿨱���С��λ", "0", "0");
					int xws=Integer.parseInt(xsw);
					zhi=MainGlobal.Mjkg_to_kcalkg(zhi, xws);
				}
			}else if(zhibmc.equals(Locale.jiessl_zhibb)){
//				����ǽ�������
				if(zhibdw.equals(Locale.wandun_danw)){
//					ϵͳĬ��������λ�Ƕ֣����������λ����֣�����������10000
					zhi=CustomMaths.Round_new(zhi, 10);
				}
			}
		}
		return zhi;
	}
	
	public static double getUnit_transform(String zhibmc,String zhibdw, double zhi,String xiaosclfs) {
		// TODO �Զ����ɷ������
//		��λת������
		if(zhibdw!=null){
			
			if(zhibmc.equals(Locale.Qnetar_zhibb)||zhibmc.equals(Locale.Qgrad_zhibb)||zhibmc.equals(Locale.Qbad_zhibb)){
				
				if(zhibdw.equals(Locale.qiankmqk_danw)){
//					�������������ֵ����λ���ǡ�ǧ��/ǧ�ˡ�,���׽�/ǧ��ת��Ϊǧ��/ǧ��
					String xsw=MainGlobal.getXitxx_item("����", "������ֵ�󿨱���С��λ", "0", "0");
					int xws=Integer.parseInt(xsw);
					zhi=MainGlobal.Mjkg_to_kcalkg(zhi, xws, xiaosclfs);
				}
			}else if(zhibmc.equals(Locale.jiessl_zhibb)){
//				����ǽ�������
				if(zhibdw.equals(Locale.wandun_danw)){
//					ϵͳĬ��������λ�Ƕ֣����������λ����֣�����������10000
					zhi=CustomMaths.Round_new(zhi/10000, 10);
				}
			}
		}
		return zhi;
	}
	
	public static String getJiesszzh(String jies_Jieslqzfs, String shul,String xiaosw) {
		// TODO �Զ����ɷ������
//		���㹫ʽ���������
		
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
		// TODO �Զ����ɷ������
//		2009-11-25 �� zsj �ģ�
//			Ϊ�������ĵ����ν���ʱ����ҪĳһЩָ���Ȩƽ����Ϊ���Ľ���ֵ��
//		����	item Ϊ "js" ʱ,���Ӷ� static_status ״̬���жϣ����ֵΪtrue,˵����ָ��������������
//		Ӧ���� static_value ��ֵ��
		
		double DblValue=0;
		
		if(item.equals("js")){
			
			if(zhibmc.equals(Locale.jiessl_zhibb)){
				
				if(bsv.isShul_static_status()){
					
					DblValue=bsv.getShul_static_value();
					bsv.setJiessl(DblValue);
				}else{
					
					DblValue=bsv.getJiessl();
				}
				
			}else if(zhibmc.equals(Locale.Qnetar_zhibb)){
				
				if(bsv.isQnetar_static_status()){
					
					DblValue=bsv.getQnetar_static_value();
					bsv.setQnetar_js(DblValue);
				}else{
					
					DblValue=bsv.getQnetar_js();
				}
				
				if (bsv.getShifyrzcjs().equals("��") && bsv.getJieslx() == Locale.guotyf_feiylbb_id) {
					
					if (bsv.getQnetar_kf() > 0 && bsv.getQnetar_kf() > DblValue) {
						DblValue = CustomMaths.Round_new(bsv.getQnetar_kf() - DblValue, 3);
					} else if (bsv.getQnetar_kf() == 0) {
						bsv.setErroInfo("û�п���ֵ���޷����������");
					}
					
				} else if (MainGlobal.getXitxx_item("����", "�Ƿ�����ֵ�����", String.valueOf(bsv.getDiancxxb_id()), "��").equals("��")
						&& bsv.getJieslx() == Locale.guotyf_feiylbb_id) {
					
					if (bsv.getQnetar_kf() > 0 && bsv.getQnetar_kf() > DblValue) {
						DblValue = CustomMaths.Round_new(bsv.getQnetar_kf() - DblValue, 3);
					} else if (bsv.getQnetar_kf() == 0) {
						bsv.setErroInfo("û�п���ֵ���޷����������");
					}
					
				}
				
			}else if(zhibmc.equals(Locale.Std_zhibb)){
				
				if(bsv.isStd_static_status()){
					
					DblValue=bsv.getStd_static_value();
					bsv.setStd_js(DblValue);
				}else{
					
					DblValue=bsv.getStd_js();
				}
				
			}else if(zhibmc.equals(Locale.Ad_zhibb)){
				
				if(bsv.isAd_static_status()){
					
					DblValue=bsv.getAd_static_value();
					bsv.setAd_js(DblValue);
				}else{
					
					DblValue=bsv.getAd_js();
				}
				
			}else if(zhibmc.equals(Locale.Vdaf_zhibb)){
				
				if(bsv.isVdaf_static_status()){
					
					DblValue=bsv.getVdaf_static_value();
					bsv.setVdaf_js(DblValue);
				}else{
					
					DblValue=bsv.getVdaf_js();
				}
				
			}else if(zhibmc.equals(Locale.Mt_zhibb)){
				
				if(bsv.isMt_static_status()){
					
					DblValue=bsv.getMt_static_value();
					bsv.setMt_js(DblValue);
				}else{
					
					DblValue=bsv.getMt_js();
				}
				
			}else if(zhibmc.equals(Locale.Qgrad_zhibb)){
				
				if(bsv.isQgrad_static_status()){
					
					DblValue=bsv.getQgrad_static_value();
					bsv.setQgrad_js(DblValue);
				}else{
					
					DblValue=bsv.getQgrad_js();
				}

			}else if(zhibmc.equals(Locale.Qbad_zhibb)){
				
				if(bsv.isQbad_static_status()){
					
					DblValue=bsv.getQbad_static_value();
					bsv.setQbad_js(DblValue);
				}else{
					
					DblValue=bsv.getQbad_js();
				}
				
			}else if(zhibmc.equals(Locale.Had_zhibb)){
				
				if(bsv.isHad_static_status()){
					
					DblValue=bsv.getHad_static_value();
					bsv.setHad_js(DblValue);
				}else{
					
					DblValue=bsv.getHad_js();
				}
				
			}else if(zhibmc.equals(Locale.Stad_zhibb)){
				
				if(bsv.isStad_static_status()){
					
					DblValue=bsv.getStad_static_value();
					bsv.setStad_js(DblValue);
				}else{
					
					DblValue=bsv.getStad_js();
				}

			}else if(zhibmc.equals(Locale.Mad_zhibb)){
				
				if(bsv.isMad_static_status()){
					
					DblValue=bsv.getMad_static_value();
					bsv.setMad_js(DblValue);
				}else{
					
					DblValue=bsv.getMad_js();
				}
				
			}else if(zhibmc.equals(Locale.Aar_zhibb)){
				
				if(bsv.isAar_static_status()){
					
					DblValue=bsv.getAar_static_value();
					bsv.setAar_js(DblValue);
				}else{
					
					DblValue=bsv.getAar_js();
				}
				
			}else if(zhibmc.equals(Locale.Aad_zhibb)){
				
				if(bsv.isAad_static_status()){
					
					DblValue=bsv.getAad_static_value();
					bsv.setAad_js(DblValue);
				}else{
					
					DblValue=bsv.getAad_js();
				}
				
			}else if(zhibmc.equals(Locale.Vad_zhibb)){
				
				if(bsv.isVad_static_status()){
					
					DblValue=bsv.getVad_static_value();
					bsv.setVad_js(DblValue);
				}else{
					
					DblValue=bsv.getVad_js();
				}

			}else if(zhibmc.equals(Locale.T2_zhibb)){
				
				if(bsv.isT2_static_status()){
					
					DblValue=bsv.getT2_static_value();
					bsv.setT2_js(DblValue);
				}else{
					
					DblValue=bsv.getT2_js();
				}

			}else if(zhibmc.equals(Locale.Yunju_zhibb)){
				
				if(bsv.isYunju_static_status()){
					
					DblValue=bsv.getYunju_static_value();
					bsv.setYunju_js(DblValue);
				}else{
					
					DblValue=bsv.getYunju_js();
				}

			}else if(zhibmc.equals(Locale.Star_zhibb)){
				
				if(bsv.isStar_static_status()){
					
					DblValue=bsv.getStar_static_value();
					bsv.setStar_js(DblValue);
				}else{
					
					DblValue=bsv.getStar_js();
				}
			}
		}
		
		return DblValue;
	}
	
	public static double getZhib_info_Shih(Balances_variable_Shih bsv,String zhibmc, String item) {
		// TODO �Զ����ɷ������
//		ʯ��ʯ������ȡ����ָ����
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
		// TODO �Զ����ɷ������
		
//		�õ���ָ֤�������Ϣ
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
	
//	������Ƿ��ظ�
	public static boolean checkbh_Shih(String jiesbh,long Meikjsb_id,long Yunfjsb_id) {
		// TODO �Զ����ɷ������
		JDBCcon con = new JDBCcon();
		String sql = "";
		try {
			
			if(!jiesbh.equals("")){
				
				if(Meikjsb_id==0&&Yunfjsb_id==0){
//					insertʱ��������
					sql = " select bianm from ((select bianm from shihjsb) union (select bianm from shihjsyfb)) where bianm='"
						+ jiesbh + "'";
				}else{
//					updateʱ������ձ��
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
	
//	������Ƿ��ظ�
	public static boolean checkbh(String Table1, String Table2, String jiesbh, long Meikjsb_id, long Yunfjsb_id) {
		// TODO �Զ����ɷ������
		JDBCcon con = new JDBCcon();
		String sql = "";
		try {
			
			if(!jiesbh.equals("")){
				
				if(Meikjsb_id==0&&Yunfjsb_id==0){
//					insertʱ��������
					sql = " select bianm from ((select bianm from "+Table1+") union (select bianm from "+Table2+")) where bianm='"
						+ jiesbh + "'";
				}else{
//					updateʱ������ձ��
					sql=" select bianm from ((select bianm,id from "+Table1+") union (select bianm ,id from "+Table2+")) where bianm='"+jiesbh+"' and (id<>"+Meikjsb_id+" and id<>"+Yunfjsb_id+")";
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
	
//  ����ʱ���ɽ�����
    public static String getJiesbh(String Diancxxb_Id,String JiesType){
		
			JDBCcon con=new JDBCcon();
			String strJiesbh="";
			String strJiesbhtmp="";
			String strjiesxz="";	//�������ʣ��ֹ�˾�ɹ�������ǵ糧�ɹ����㣩
			try{
 				java.util.Date datCur = new java.util.Date();
	            SimpleDateFormat formatter = new SimpleDateFormat("yyMM");
	            String dat =formatter.format(datCur);
 	            int intBh=0;
 	            
 	            String strJiesbhqz="";
 	            String strJiesbhgssx="";
 	            String strFadqypysx="";
 	            String strtmp="";
 	            
 	            String strtable_mk="jiesb";
 	            String strtable_yf="jiesyfb";
 	            
 	            String strfgs_id="";
 	            
 	            if(Diancxxb_Id.indexOf(",")>-1){
 	            	
 	            	strfgs_id = Diancxxb_Id.substring(0,Diancxxb_Id.indexOf(","));
 	            	Diancxxb_Id = Diancxxb_Id.substring(Diancxxb_Id.indexOf(",")+1);
 	            }
 	            
 	            
 	            if(isFengsj(strfgs_id)){
// 	            	�ֹ�˾�ɹ�����
 	            	strtable_mk = "kuangfjsmkb";
 	            	strtable_yf = "kuangfjsyfb";
 	            	strjiesxz = "-C";
 	            }
// 	           CDT-JS-GANS-ef-1102-006
// 	             ������ǰ׺
 	            strJiesbhqz=MainGlobal.getXitxx_item("����","���㵥����ǰ׺",strfgs_id.equals("")?Diancxxb_Id:strfgs_id,"CDT-JS");
 	            strJiesbhgssx=MainGlobal.getXitxx_item("����","����˾ƴ����д",strfgs_id.equals("")?Diancxxb_Id:strfgs_id,"");
 	            strFadqypysx=MainGlobal.getTableCol("diancxxb","piny","id",strfgs_id.equals("")?Diancxxb_Id:strfgs_id);
 	            
 	            if(!strJiesbhqz.equals("")&&strJiesbhqz!=null){
 	            	
 	            	strJiesbhqz=strJiesbhqz+"-";
 	            }
 	            
 	            if(!strJiesbhgssx.equals("")&&strJiesbhgssx!=null){
 	            	
 	            	strJiesbhgssx=strJiesbhgssx+"-";
 	            }
 	            
 	            if(!strFadqypysx.equals("")&&strFadqypysx!=null){
 	            	
 	            	strFadqypysx=strFadqypysx+"-";
 	            }
 	            
 	           strtmp=strJiesbhqz+strJiesbhgssx+strFadqypysx;
 	            
				String Sql="select max(jiesbh) as jiesbh from (select bianm as jiesbh from "+strtable_mk+" where diancxxb_id="+Diancxxb_Id
						+ " union select bianm as jiesbh from "+strtable_yf+" where diancxxb_id="+Diancxxb_Id+")"
						+ " where jiesbh like '%"+strtmp+dat+"%' ";
				ResultSet rs=con.getResultSet(Sql);
				if(rs.next()){
					
					strJiesbh=rs.getString("jiesbh");
				}
				rs.close();
				
				if(strJiesbh==null){
					
					strJiesbh=strtmp+dat+"-"+"000";
				}else if(!strfgs_id.equals("")){
					
					strJiesbh=strJiesbh.trim().substring(0,strJiesbh.trim().length()-2);
				}
				
				if(!JiesType.equals("")&&strJiesbh.lastIndexOf(JiesType)>strJiesbh.lastIndexOf(dat)){
					
					strJiesbh=strJiesbh.substring(0,strJiesbh.length()-(JiesType.length()+1));
					JiesType="-"+JiesType;
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
	            
	            String strJiesbh_zdy = MainGlobal.getXitxx_item("����","�ͻ��Զ�����",strfgs_id.equals("")?Diancxxb_Id:strfgs_id,"");
	            if(!strJiesbh_zdy.equals("")){
	            	if(strJiesbh_zdy.indexOf("��ˮ��")!=-1){
	            		Sql="select count(0) lsh from jiesb"; //where to_char(jiesrq,'yyyy')=" + DateUtil.getYear(new Date());
						rs=con.getResultSet(Sql);
						if(rs.next()){
							strJiesbh_zdy = strJiesbh_zdy.replaceAll("��ˮ��", rs.getString("lsh"));
						}else{
							strJiesbh_zdy = strJiesbh_zdy.replaceAll("��ˮ��", "1");
						}
	            	}
	            	return strJiesbh_zdy;
	            }
	            
			}catch(Exception e){
				
				e.printStackTrace();
			}finally{
				
				con.Close();
			}
		
		return strJiesbh.substring(0,strJiesbh.trim().length()-3)+strJiesbhtmp+JiesType+strjiesxz;
	}
    
    public static String getJiesbh_Shih(String Diancxxb_Id) throws SQLException{
//    	�õ�ʯ��ʯ�Ľ�����	����һ�������
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
		// TODO �Զ����ɷ������
//		���浽�˷ѽ�����������(DIANCXXB_ID,FEIYLBB_ID,CHEPB_ID,CHEPH,CHEBB_ID,BIAOZ)
		JDBCcon con=new JDBCcon();
		long lngYunfjszlb_id=0;
		boolean blnFlag=false;
		
		StringBuffer sql = new StringBuffer("begin \n");
		StringBuffer sql2 = new StringBuffer("");
		String tableName="yunfjszlb";
		
		lngYunfjszlb_id=Check_Record_AlreadyexistsAndReturnId("yunfjszlb", "chepb_id="+Chepb_id+" and feiylbb_id="+Feiylbb_id+"");
		
		if(lngYunfjszlb_id>0){
//			������ڼ�¼��ô����Yunfjszlb����������¼�¼
			
			sql.append("update ").append(tableName).append(" set ");
				
					sql.append("CHEPH").append(" = '");
					sql.append(Cheph).append("',");
					
					sql.append("CHEBB_ID").append(" = ");
					sql.append(Chebb_id).append(",");
					
					sql.append("JIFZL").append(" = ");
					sql.append(Biaoz);
					
			sql.append(" where id =").append(lngYunfjszlb_id).append(";\n");
		}else{
//			��������ڼ�¼��ô����Yunfjszlb�������¼�¼
			
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
//		���ڼ��ĳ�������Ƿ����ĳ�����ݣ���������򷵻����м�¼��ID
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
	
	//�ӽ������ñ���ȡֵ
	public static String getJiessz_item(long diancxxb_id,long gongysb_id,long hetb_id,String bianm,String defaultValue){
		
		JDBCcon con=new JDBCcon();
		String value=defaultValue;
		String mstrzhi="";
		String sql="";
		ResultSetList rs=null;
		try{
			
			sql="select distinct bmb.bianm,jssz.zhi			\n"
					+ " from jiesszfahtglb faht,				\n"
					+ " jiesszfab fangab,						\n"
					+ " jiesszb jssz,							\n"
					+ " jiesszbmb bmb							\n"
					+ " where faht.jiesszfab_id=fangab.id		\n"
					+ " and fangab.shifsy=1						\n" 
					+ " and fangab.qiysj<=sysdate				\n"
					+ " and faht.hetb_id="+hetb_id+"			\n"
					+ " and fangab.gongysb_id="+gongysb_id+"	\n"
					+ " and fangab.diancxxb_id="+diancxxb_id+"  \n"
					+ " and jssz.jiesszfab_id=fangab.id			\n"
					+ " and jssz.jiesszbmb_id=bmb.id 			\n"
					+ " and bmb.bianm='"+bianm+"'";
			
			rs=con.getResultSetList(sql);
			
			if(rs.getRows()>0){
//				�ӳ��м�¼
				while(rs.next()){
					
					if(mstrzhi.equals("")){
						
						mstrzhi=rs.getString("zhi");
					}else{
						
						mstrzhi+=","+rs.getString("zhi");
					}
				}
			}else{
//				�ӳ�û�м�¼���鿴�ܳ���¼
				sql="select distinct bmb.bianm,jssz.zhi			\n"
					+ " from jiesszfahtglb faht,				\n"
					+ " jiesszfab fangab,						\n"
					+ " jiesszb jssz,							\n"
					+ " jiesszbmb bmb							\n"
					+ " where faht.jiesszfab_id=fangab.id		\n"
					+ " and fangab.shifsy=1						\n" 
					+ " and fangab.qiysj<=sysdate				\n"
					+ " and faht.hetb_id="+hetb_id+"			\n"
					+ " and fangab.gongysb_id="+gongysb_id+"	\n"
					+ " and fangab.diancxxb_id="+getDiancFuid(SysConstant.JIB_DC,String.valueOf(diancxxb_id))+"  \n"
					+ " and jssz.jiesszfab_id=fangab.id			\n"
					+ " and jssz.jiesszbmb_id=bmb.id 			\n"
					+ " and bmb.bianm='"+bianm+"'";
				
				rs=con.getResultSetList(sql);
				
				while(rs.next()){
					
					if(mstrzhi.equals("")){
						
						mstrzhi=rs.getString("zhi");
					}else{
						
						mstrzhi+=","+rs.getString("zhi");
					}
				}
			}
			
			if(rs!=null){
				
				rs.close();
			}
			
			
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
	
	public static String getDiancFuid(int jib,String diancxxb_id){

//	�������ܣ�
//			�õ��糧��fuid
//	�����߼���
//			�õ糧id�ҵ��ϼ��糧id
//	�����βΣ�
//			�ϼ��糧�ļ��𣬱����糧��id	
		
		JDBCcon con =  new JDBCcon();
		try{
			
			String sql="select id from diancxxb where jib="+jib+" and id in\n" +
					"(select fuid from diancxxb where id="+diancxxb_id+")";
			
			ResultSet rs = con.getResultSet(sql);
			if(rs.next()){
				
				diancxxb_id = rs.getString("id");
			}
			rs.close();
			
		}catch(SQLException e){
			
			e.printStackTrace();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		
		return diancxxb_id;
	}
	
	//ȡ�������������ñ�
	public static String[][] getJiessz_items(long diancxxb_id,long gongysb_id,long hetb_id){
		
		JDBCcon con=new JDBCcon();
		String Jiessz[][]=null;
		String mstrtmp="";
		int i=0;
		String sql="";
		ResultSetList rs=null;
		try{
			
			sql=" select distinct bmb.bianm,jssz.zhi,bmb.shifwy	\n"
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
			
			rs=con.getResultSetList(sql);
			
			if(rs !=null){
				
				if(rs.getRows()>0){
//					�иõ糧�����ü�¼
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
				}else{
					
					sql=" select distinct bmb.bianm,jssz.zhi,bmb.shifwy	\n"
						+ " from jiesszfahtglb faht,				\n"
						+ " jiesszfab fangab,						\n"
						+ " jiesszb jssz,							\n"
						+ " jiesszbmb bmb							\n"
						+ " where faht.jiesszfab_id=fangab.id		\n"
						+ " and fangab.shifsy=1						\n" 
						+ " and fangab.qiysj<=sysdate				\n"
						+ " and faht.hetb_id="+hetb_id+"			\n"
						+ " and fangab.gongysb_id="+gongysb_id+"	\n"
						+ " and fangab.diancxxb_id="+getDiancFuid(SysConstant.JIB_DC
								,String.valueOf(diancxxb_id))+"	\n"
						+ " and jssz.jiesszfab_id=fangab.id			\n"
						+ " and jssz.jiesszbmb_id=bmb.id order by bmb.bianm	";
					rs=con.getResultSetList(sql);
					
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
			}
			
			if(rs!=null){
				
				rs.close();
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
//		ָ���ļ���
		try {
			File Dir = new File(strFilePath);
			if (Dir.isDirectory()) {
	//			ָ������xml�ĵ�
				FileNameFilter fnf = new FileNameFilter("xml");
				File xmls[] = Dir.listFiles(fnf);
	//			����xml�ĵ�
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
//									˵���Ѿ��ߵ�xml<gs>�Ľ�β��
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
	
	public static String InsertIntoGSKuangfjsb(JDBCcon con,String TableName,String TableID,String TableId_Diancjsb,
			String YF_kuangfjsmcb_id) throws Exception{
		
		StringBuffer bf=new StringBuffer("begin	\n");
		String sql="";
		long Diancxxb_id=0;
		String lnId="";
		String Fuid="";	//��id
		ResultSet rs=null;
		if(TableName.equals("kuangfjsmkb")){
			
			try{
				
				double hansdj=0;
				double meikje=0;
				double hansmk=0;
				double buhsmk=0;
				double shuik=0;
				double buhsdj=0;
//				�ж�ú���Ƿ����ͬ���й���
				String SQL="select ht.id, ht.gongysb_id, ht.gongfdwmc, ht.gongfkhyh, ht.gongfzh\n" +
                   "  from hetmkfzglb h, jiesb j, meikfzmxb m, hetb ht\n" + 
                   " where m.meikfzb_id = h.meikfzb_id\n" + 
                   "   and m.meikxxb_id = j.meikxxb_id\n" + 
                   "   and j.diancxxb_id = h.diancxxb_id\n" + 
                   "   and h.hetb_id = ht.id\n" + 
                   "   and j.fahksrq >= h.qisrq\n" + 
                   "   and j.fahjzrq <= h.jiezrq \n"+
                   "   and j.id="+TableID;	
				 ResultSet rsl=con.getResultSet(SQL);
				if(rsl.next()){					
//				1�������ܽ��㵥
					sql=" select * from jiesb where id="+TableID;
					rs=con.getResultSet(sql);
					
					if(rs.next()){
						
						Diancxxb_id=rs.getLong("diancxxb_id");
						lnId=MainGlobal.getNewID(Diancxxb_id);
						Fuid=lnId;
						
						hansdj=CustomMaths.sub(rs.getDouble("hansdj"), rs.getDouble("fengsjj"));
						meikje=CustomMaths.div(CustomMaths.Round_new(CustomMaths.mul(hansdj,rs.getDouble("jiessl")),2),1+rs.getDouble("shuil"),2);
						hansmk=CustomMaths.add(CustomMaths.Round_new(CustomMaths.mul(hansdj,rs.getDouble("jiessl")),2),rs.getDouble("bukmk"));
						buhsmk=CustomMaths.div(hansmk,(1+rs.getDouble("shuil")),2);
						shuik=CustomMaths.sub(hansmk,buhsmk);
						buhsdj=CustomMaths.div(hansdj,(1+rs.getDouble("shuil")),7);
						
						bf.append(" insert into kuangfjsmkb \n");
						bf.append(" ( ID,DIANCXXB_ID,BIANM,GONGYSB_ID,GONGYSMC,FAZ,FAHKSRQ,FAHJZRQ,MEIZ,DAIBCH,YUANSHR,XIANSHR,\n");
						bf.append(" YANSKSRQ,YANSJZRQ,YANSBH,SHOUKDW,KAIHYH,ZHANGH,FAPBH,FUKFS,DUIFDD,CHES,JIESSL,GUOHL,\n");
						bf.append(" HANSDJ,BUKMK,HANSMK,BUHSMK,MEIKJE,SHUIK,SHUIL,BUHSDJ,JIESLX,JIESRQ,HETB_ID,LIUCZTB_ID,\n");
						bf.append(" LIUCGZID,KUANGFJSB_ID,DIANCJSMKB_ID,DANJC,BEIZ,LIUCGZB_ID,HETJ,JIESFRL,JIESLF,JIESRCRL,\n");
						bf.append(" JIESRL,JIESSLCY,JIHKJB_ID,KOUD,KUID,MEIKDWMC,MEIKXXB_ID,QIYF,RANLBMJBR,RANLBMJBRQ,\n");
						bf.append(" YINGD,YUNJ,YUNS,YUNSFSB_ID,ZHILJQ,FUID,FENGSJJ,JIAJQDJ,JIJLX,KUIDJFYF,KUIDJFZF,CHAOKDL,\n");
						bf.append(" CHAOKDLX,YUFKJE,Yunfhsdj,hansyf,buhsyf,yunfjsl,zhuangt \n");
						bf.append(" )");
						bf.append(" values(\n");
						bf.append(" "+lnId+","+Diancxxb_id+",'"+rs.getString("BIANM")+"-CG',"+rsl.getString("GONGYSB_ID")+",'"+rsl.getString("gongfdwmc")+"','"+rs.getString("FAZ")+"',to_date('"+FormatDate_GS(rs.getDate("FAHKSRQ"))+"','yyyy-MM-dd'),to_date('"+FormatDate_GS(rs.getDate("FAHJZRQ"))+"','yyyy-MM-dd'),'"+rs.getString("MEIZ")+"','"+rs.getString("DAIBCH")+"','"+rs.getString("YUANSHR")+"','"+rs.getString("XIANSHR")+"',\n");
						bf.append(" to_date('"+FormatDate_GS(rs.getDate("YANSKSRQ"))+"','yyyy-MM-dd'),to_date('"+FormatDate_GS(rs.getDate("YANSJZRQ"))+"','yyyy-MM-dd'),'"+rs.getString("YANSBH")+"','"+rsl.getString("gongfdwmc")+"','"+rsl.getString("gongfkhyh")+"','"+rsl.getString("gongfzh")+"','"+rs.getString("FAPBH")+"','"+rs.getString("FUKFS")+"','"+rs.getString("DUIFDD")+"',"+rs.getString("CHES")+","+rs.getString("JIESSL")+","+rs.getString("GUOHL")+",\n");
						bf.append(" "+hansdj+","+rs.getString("BUKMK")+","+hansmk+","+buhsmk+","+meikje+","+shuik+","+rs.getString("SHUIL")+","+buhsdj+","+rs.getString("JIESLX")+",to_date('"+FormatDate_GS(rs.getDate("JIESRQ"))+"','yyyy-MM-dd'),"+rsl.getString("ID")+","+/*rs.getString("LIUCZTB_ID")*/0+",\n");
						bf.append(" "+rs.getString("LIUCGZID")+","+0+","+rs.getLong("diancjsmkb_id")+","+0+",'"+rs.getString("BEIZ")+"',"+/*rs.getString("LIUCGZID")*/0+","+rs.getString("HETJ")+","+rs.getString("JIESFRL")+","+rs.getString("JIESLF")+","+rs.getString("JIESRCRL")+",\n");
						bf.append(" "+rs.getString("JIESRL")+","+rs.getString("JIESSLCY")+","+rs.getString("JIHKJB_ID")+","+rs.getString("KOUD")+","+rs.getString("KUID")+",'"+rs.getString("MEIKDWMC")+"',"+rs.getString("MEIKXXB_ID")+","+rs.getString("QIYF")+",'"+rs.getString("RANLBMJBR")+"',to_date('"+FormatDate_GS(rs.getDate("RANLBMJBRQ"))+"','yyyy-MM-dd'),\n");
						bf.append(" "+rs.getString("YINGD")+","+rs.getString("YUNJ")+","+rs.getString("YUNS")+","+rs.getString("YUNSFSB_ID")+",'"+rs.getString("ZHILJQ")+"',"+rs.getString("FUID")+",0,"+hansdj+","+rs.getString("JIJLX")+","+rs.getString("KUIDJFYF")+","+rs.getString("KUIDJFZF")+","+rs.getString("CHAOKDL")+","+rs.getString("CHAOKDLX")+","+rs.getString("YUFKJE")+",	\n");
						bf.append(" "+rs.getString("Yunfhsdj")+","+rs.getString("hansyf")+","+rs.getString("buhsyf")+","+rs.getString("yunfjsl")+",1");
						bf.append(" );\n");
						
						
						sql=" select * from jieszbsjb t  where t.jiesdid = "+TableID;
						rs=con.getResultSet(sql);
						
						while(rs.next()){
							
							bf.append(" insert into jieszbsjb(id,jiesdid,zhibb_id,hetbz,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id)\n");
							bf.append(" values("+MainGlobal.getNewID(Diancxxb_id)+","+lnId+","+rs.getString("zhibb_id")+",'"+rs.getString("hetbz")+"',")
							.append(rs.getString("gongf")+","+rs.getString("changf")+","+rs.getString("jies")+","+rs.getString("yingk")+","+(rs.getLong("zhibb_id")== 1? hansdj:rs.getDouble("zhejbz"))+",")
							.append((rs.getLong("zhibb_id")== 1? CustomMaths.Round_new(CustomMaths.mul(hansdj,rs.getDouble("yingk")),2):rs.getDouble("zhejje"))+","+rs.getString("zhuangt")+","+rs.getString("yansbhb_id")+");\n");
						}
					}
					
//				2�������ӽ���
					sql=" select * from jiesb where fuid="+TableID;
					rs=con.getResultSet(sql);
					
					while(rs.next()){
						
						Diancxxb_id=rs.getLong("diancxxb_id");
						lnId=MainGlobal.getNewID(Diancxxb_id);
						
						hansdj=CustomMaths.sub(rs.getDouble("hansdj"), rs.getDouble("fengsjj"));
						meikje=CustomMaths.div(CustomMaths.Round_new(CustomMaths.mul(hansdj,rs.getDouble("jiessl")),2),1+rs.getDouble("shuil"),2);
						hansmk=CustomMaths.add(CustomMaths.Round_new(CustomMaths.mul(hansdj,rs.getDouble("jiessl")),2),rs.getDouble("bukmk"));
						buhsmk=CustomMaths.div(hansmk,(1+rs.getDouble("shuil")),2);
						shuik=CustomMaths.sub(hansmk,buhsmk);
						buhsdj=CustomMaths.div(hansdj,(1+rs.getDouble("shuil")),7);
						
						bf.append(" insert into kuangfjsmkb \n");
						bf.append(" ( ID,DIANCXXB_ID,BIANM,GONGYSB_ID,GONGYSMC,FAZ,FAHKSRQ,FAHJZRQ,MEIZ,DAIBCH,YUANSHR,XIANSHR,\n");
						bf.append(" YANSKSRQ,YANSJZRQ,YANSBH,SHOUKDW,KAIHYH,ZHANGH,FAPBH,FUKFS,DUIFDD,CHES,JIESSL,GUOHL,\n");
						bf.append(" HANSDJ,BUKMK,HANSMK,BUHSMK,MEIKJE,SHUIK,SHUIL,BUHSDJ,JIESLX,JIESRQ,HETB_ID,LIUCZTB_ID,\n");
						bf.append(" LIUCGZID,KUANGFJSB_ID,DIANCJSMKB_ID,DANJC,BEIZ,LIUCGZB_ID,HETJ,JIESFRL,JIESLF,JIESRCRL,\n");
						bf.append(" JIESRL,JIESSLCY,JIHKJB_ID,KOUD,KUID,MEIKDWMC,MEIKXXB_ID,QIYF,RANLBMJBR,RANLBMJBRQ,\n");
						bf.append(" YINGD,YUNJ,YUNS,YUNSFSB_ID,ZHILJQ,FUID,FENGSJJ,JIAJQDJ,JIJLX,KUIDJFYF,KUIDJFZF,CHAOKDL,\n");
						bf.append(" CHAOKDLX,YUFKJE,Yunfhsdj,hansyf,buhsyf,yunfjsl");
						bf.append(" )");
						bf.append(" values(\n");
						bf.append(" "+lnId+","+Diancxxb_id+",'"+rs.getString("BIANM")+"-CG',"+rsl.getString("GONGYSB_ID")+",'"+rsl.getString("gongfdwmc")+"','"+rs.getString("FAZ")+"',to_date('"+FormatDate_GS(rs.getDate("FAHKSRQ"))+"','yyyy-MM-dd'),to_date('"+FormatDate_GS(rs.getDate("FAHJZRQ"))+"','yyyy-MM-dd'),'"+rs.getString("MEIZ")+"','"+rs.getString("DAIBCH")+"','"+rs.getString("YUANSHR")+"','"+rs.getString("XIANSHR")+"',\n");
						bf.append(" to_date('"+FormatDate_GS(rs.getDate("YANSKSRQ"))+"','yyyy-MM-dd'),to_date('"+FormatDate_GS(rs.getDate("YANSJZRQ"))+"','yyyy-MM-dd'),'"+rs.getString("YANSBH")+"','"+rsl.getString("gongfdwmc")+"','"+rsl.getString("gongfkhyh")+"','"+rsl.getString("gongfzh")+"','"+rs.getString("FAPBH")+"','"+rs.getString("FUKFS")+"','"+rs.getString("DUIFDD")+"',"+rs.getString("CHES")+","+rs.getString("JIESSL")+","+rs.getString("GUOHL")+",\n");
						bf.append(" "+hansdj+","+rs.getString("BUKMK")+","+hansmk+","+buhsmk+","+meikje+","+shuik+","+rs.getString("SHUIL")+","+buhsdj+","+rs.getString("JIESLX")+",to_date('"+FormatDate_GS(rs.getDate("JIESRQ"))+"','yyyy-MM-dd'),"+rsl.getString("ID")+","+/*rs.getString("LIUCZTB_ID")*/0+",\n");
						bf.append(" "+rs.getString("LIUCGZID")+","+0+","+rs.getLong("diancjsmkb_id")+","+0+",'"+rs.getString("BEIZ")+"',"+/*rs.getString("LIUCGZID")*/0+","+rs.getString("HETJ")+","+rs.getString("JIESFRL")+","+rs.getString("JIESLF")+","+rs.getString("JIESRCRL")+",\n");
						bf.append(" "+rs.getString("JIESRL")+","+rs.getString("JIESSLCY")+","+rs.getString("JIHKJB_ID")+","+rs.getString("KOUD")+","+rs.getString("KUID")+",'"+rs.getString("MEIKDWMC")+"',"+rs.getString("MEIKXXB_ID")+","+rs.getString("QIYF")+",'"+rs.getString("RANLBMJBR")+"',to_date('"+FormatDate_GS(rs.getDate("RANLBMJBRQ"))+"','yyyy-MM-dd'),\n");
						bf.append(" "+rs.getString("YINGD")+","+rs.getString("YUNJ")+","+rs.getString("YUNS")+","+rs.getString("YUNSFSB_ID")+",'"+rs.getString("ZHILJQ")+"',"+Fuid+",0,"+hansdj+","+rs.getString("JIJLX")+","+rs.getString("KUIDJFYF")+","+rs.getString("KUIDJFZF")+","+rs.getString("CHAOKDL")+","+rs.getString("CHAOKDLX")+","+rs.getString("YUFKJE")+",\n");
						bf.append(" "+rs.getString("Yunfhsdj")+","+rs.getString("hansyf")+","+rs.getString("buhsyf")+","+rs.getString("yunfjsl"));
						bf.append(" );\n");
						
						
						sql=" select * from jieszbsjb t  where t.jiesdid = "+rs.getString("id");
						ResultSet rec=con.getResultSet(sql);
						
						while(rec.next()){
							
							bf.append(" insert into jieszbsjb(id,jiesdid,zhibb_id,hetbz,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id)\n");
							bf.append(" values("+MainGlobal.getNewID(Diancxxb_id)+","+lnId+","+rec.getString("zhibb_id")+",'"+rec.getString("hetbz")+"',")
							.append(rec.getString("gongf")+","+rec.getString("changf")+","+rec.getString("jies")+","+rec.getString("yingk")+","+(rec.getLong("zhibb_id")== 1? hansdj:rec.getDouble("zhejbz"))+",")
							.append((rec.getLong("zhibb_id")== 1? CustomMaths.Round_new(CustomMaths.mul(hansdj,rec.getDouble("yingk")),2):rec.getDouble("zhejje"))+","+rec.getString("zhuangt")+","+rec.getString("yansbhb_id")+");\n");
						}
						rec.close();
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				throw new Exception("error");
			}
		}
		
		if(TableName.equals("kuangfjsyfb")){
			
			try{
//				1�������ܽ��㵥
				sql=" select * from jiesyfb where id="+TableID;
			    rs=con.getResultSet(sql);
				
				if(rs.next()){
					
					Diancxxb_id=rs.getLong("diancxxb_id");
					lnId=MainGlobal.getNewID(Diancxxb_id);
					Fuid=lnId;
				
					bf.append(" insert into kuangfjsyfb \n");
					bf.append(" ( ID,DIANCXXB_ID,BIANM,GONGYSB_ID,GONGYSMC,FAZ,FAHKSRQ,FAHJZRQ,MEIZ,DAIBCH,YUANSHR,XIANSHR,\n");
					bf.append(" YANSKSRQ,YANSJZRQ,YANSBH,SHOUKDW,KAIHYH,ZHANGH,FAPBH,FUKFS,DUIFDD,CHES,JIESSL,GUOHL,\n");
					bf.append(" GUOTYF,DITYF,JISKC,HANSDJ,BUKYF,HANSYF,BUHSYF,SHUIK,SHUIL,BUHSDJ,JIESLX,JIESRQ,HETB_ID,LIUCZTB_ID,\n");
					bf.append(" LIUCGZID,KUANGFJSB_ID,KUANGFJSMKB_ID,DIANCJSYFB_ID,DANJC,BEIZ,KUANGJSMKB_ID,LIUCGZB_ID,DIANCJSMKB_ID,\n");
					bf.append(" DITZF,GONGFSL,GUOTYFJF,GUOTZF,GUOTZFJF,JIESSLCY,KOUD,KUANGQYF,KUANGQZF,KUID,MEIKDWMC,MEIKXXB_ID,RANLBMJBR,RANLBMJBRQ,\n");
					bf.append(" YANSSL,YINGD,YINGK,YUNJ,YUNS,YUNSFSB_ID,FUID,KUIDJFYF,KUIDJFZF,YUFKJE,zhuangt\n");
					bf.append(" )");
					bf.append(" values( \n");
					bf.append(" "+lnId+","+Diancxxb_id+",'"+rs.getString("BIANM")+"-CG',"+rs.getString("GONGYSB_ID")+",'"+rs.getString("GONGYSMC")+"','"+rs.getString("FAZ")+"',to_date('"+FormatDate_GS(rs.getDate("FAHKSRQ"))+"','yyyy-MM-dd'),to_date('"+FormatDate_GS(rs.getDate("FAHJZRQ"))+"','yyyy-MM-dd'),'"+rs.getString("MEIZ")+"','"+rs.getString("DAIBCH")+"','"+rs.getString("YUANSHR")+"','"+rs.getString("XIANSHR")+"',\n");
					bf.append(" to_date('"+FormatDate_GS(rs.getDate("YANSKSRQ"))+"','yyyy-MM-dd'),to_date('"+FormatDate_GS(rs.getDate("YANSJZRQ"))+"','yyyy-MM-dd'),'"+rs.getString("YANSBH")+"','"+rs.getString("SHOUKDW")+"','"+rs.getString("KAIHYH")+"','"+rs.getString("ZHANGH")+"','"+rs.getString("FAPBH")+"','"+rs.getString("FUKFS")+"','"+rs.getString("DUIFDD")+"',"+rs.getString("CHES")+","+rs.getString("JIESSL")+","+rs.getString("GUOHL")+",\n");
					bf.append(" "+rs.getString("GUOTYF")+","+rs.getString("DITYF")+","+rs.getString("JISKC")+","+rs.getString("HANSDJ")+","+rs.getString("BUKYF")+","+rs.getString("HANSYF")+","+rs.getString("BUHSYF")+","+rs.getString("SHUIK")+","+rs.getString("SHUIL")+","+rs.getString("BUHSDJ")+","+rs.getString("JIESLX")+",to_date('"+FormatDate_GS(rs.getDate("JIESRQ"))+"','yyyy-MM-dd'),"+rs.getString("HETB_ID")+","+rs.getString("LIUCZTB_ID")+",\n");
					bf.append(" "+rs.getString("LIUCGZID")+","+0+","+YF_kuangfjsmcb_id+","+rs.getLong("diancjsmkb_id")+","+0+",'"+rs.getString("BEIZ")+"',"+0+","+rs.getString("LIUCGZID")+","+0+",\n");
					bf.append(" "+rs.getString("DITZF")+","+rs.getString("GONGFSL")+","+rs.getString("GUOTYFJF")+","+rs.getString("GUOTZF")+","+rs.getString("GUOTZFJF")+","+rs.getString("JIESSLCY")+","+rs.getString("KOUD")+","+rs.getString("KUANGQYF")+","+rs.getString("KUANGQZF")+","+rs.getString("KUID")+",'"+rs.getString("MEIKDWMC")+"',"+rs.getString("MEIKXXB_ID")+",'"+rs.getString("RANLBMJBR")+"',to_date('"+FormatDate_GS(rs.getDate("RANLBMJBRQ"))+"','yyyy-MM-dd'),\n");
					bf.append(" "+rs.getString("YANSSL")+","+rs.getString("YINGD")+","+rs.getString("YINGK")+","+rs.getString("YUNJ")+","+rs.getString("YUNS")+","+rs.getString("YUNSFSB_ID")+","+rs.getString("FUID")+","+rs.getString("KUIDJFYF")+","+rs.getString("KUIDJFZF")+","+rs.getString("YUFKJE")+",1");
					bf.append(" );\n");
					
	
					sql=" select * from jieszbsjb t  where t.jiesdid = "+TableID;
					rs=con.getResultSet(sql);
					
					while(rs.next()){
						
						bf.append(" insert into jieszbsjb(id,jiesdid,zhibb_id,hetbz,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id)\n");
						bf.append(" values("+MainGlobal.getNewID(Diancxxb_id)+","+lnId+","+rs.getString("zhibb_id")+",'"+rs.getString("hetbz")+"',")
							.append(rs.getString("gongf")+","+rs.getString("changf")+","+rs.getString("jies")+","+rs.getString("yingk")+","+rs.getString("zhejbz")+",")
							.append(rs.getString("zhejje")+","+rs.getString("zhuangt")+","+rs.getString("yansbhb_id")+");\n");
					}
				}
				
//				2�������ӽ��㵥
				sql=" select * from jiesyfb where fuid="+TableID;
			    rs=con.getResultSet(sql);
				
				while(rs.next()){
					
					Diancxxb_id=rs.getLong("diancxxb_id");
					lnId=MainGlobal.getNewID(Diancxxb_id);
				
					bf.append(" insert into kuangfjsyfb \n");
					bf.append(" ( ID,DIANCXXB_ID,BIANM,GONGYSB_ID,GONGYSMC,FAZ,FAHKSRQ,FAHJZRQ,MEIZ,DAIBCH,YUANSHR,XIANSHR,\n");
					bf.append(" YANSKSRQ,YANSJZRQ,YANSBH,SHOUKDW,KAIHYH,ZHANGH,FAPBH,FUKFS,DUIFDD,CHES,JIESSL,GUOHL,\n");
					bf.append(" GUOTYF,DITYF,JISKC,HANSDJ,BUKYF,HANSYF,BUHSYF,SHUIK,SHUIL,BUHSDJ,JIESLX,JIESRQ,HETB_ID,LIUCZTB_ID,\n");
					bf.append(" LIUCGZID,KUANGFJSB_ID,KUANGFJSMKB_ID,DIANCJSYFB_ID,DANJC,BEIZ,KUANGJSMKB_ID,LIUCGZB_ID,DIANCJSMKB_ID,\n");
					bf.append(" DITZF,GONGFSL,GUOTYFJF,GUOTZF,GUOTZFJF,JIESSLCY,KOUD,KUANGQYF,KUANGQZF,KUID,MEIKDWMC,MEIKXXB_ID,RANLBMJBR,RANLBMJBRQ,\n");
					bf.append(" YANSSL,YINGD,YINGK,YUNJ,YUNS,YUNSFSB_ID,FUID,KUIDJFYF,KUIDJFZF,YUFKJE\n");
					bf.append(" )");
					bf.append(" values( \n");
					bf.append(" "+lnId+","+Diancxxb_id+",'"+rs.getString("BIANM")+"-CG',"+rs.getString("GONGYSB_ID")+",'"+rs.getString("GONGYSMC")+"','"+rs.getString("FAZ")+"',to_date('"+FormatDate_GS(rs.getDate("FAHKSRQ"))+"','yyyy-MM-dd'),to_date('"+FormatDate_GS(rs.getDate("FAHJZRQ"))+"','yyyy-MM-dd'),'"+rs.getString("MEIZ")+"','"+rs.getString("DAIBCH")+"','"+rs.getString("YUANSHR")+"','"+rs.getString("XIANSHR")+"',\n");
					bf.append(" to_date('"+FormatDate_GS(rs.getDate("YANSKSRQ"))+"','yyyy-MM-dd'),to_date('"+FormatDate_GS(rs.getDate("YANSJZRQ"))+"','yyyy-MM-dd'),'"+rs.getString("YANSBH")+"','"+rs.getString("SHOUKDW")+"','"+rs.getString("KAIHYH")+"','"+rs.getString("ZHANGH")+"','"+rs.getString("FAPBH")+"','"+rs.getString("FUKFS")+"','"+rs.getString("DUIFDD")+"',"+rs.getString("CHES")+","+rs.getString("JIESSL")+","+rs.getString("GUOHL")+",\n");
					bf.append(" "+rs.getString("GUOTYF")+","+rs.getString("DITYF")+","+rs.getString("JISKC")+","+rs.getString("HANSDJ")+","+rs.getString("BUKYF")+","+rs.getString("HANSYF")+","+rs.getString("BUHSYF")+","+rs.getString("SHUIK")+","+rs.getString("SHUIL")+","+rs.getString("BUHSDJ")+","+rs.getString("JIESLX")+",to_date('"+FormatDate_GS(rs.getDate("JIESRQ"))+"','yyyy-MM-dd'),"+rs.getString("HETB_ID")+","+rs.getString("LIUCZTB_ID")+",\n");
					bf.append(" "+rs.getString("LIUCGZID")+","+0+","+YF_kuangfjsmcb_id+","+rs.getLong("diancjsmkb_id")+","+0+",'"+rs.getString("BEIZ")+"',"+0+","+rs.getString("LIUCGZID")+","+0+",\n");
					bf.append(" "+rs.getString("DITZF")+","+rs.getString("GONGFSL")+","+rs.getString("GUOTYFJF")+","+rs.getString("GUOTZF")+","+rs.getString("GUOTZFJF")+","+rs.getString("JIESSLCY")+","+rs.getString("KOUD")+","+rs.getString("KUANGQYF")+","+rs.getString("KUANGQZF")+","+rs.getString("KUID")+",'"+rs.getString("MEIKDWMC")+"',"+rs.getString("MEIKXXB_ID")+",'"+rs.getString("RANLBMJBR")+"',to_date('"+FormatDate_GS(rs.getDate("RANLBMJBRQ"))+"','yyyy-MM-dd'),\n");
					bf.append(" "+rs.getString("YANSSL")+","+rs.getString("YINGD")+","+rs.getString("YINGK")+","+rs.getString("YUNJ")+","+rs.getString("YUNS")+","+rs.getString("YUNSFSB_ID")+","+Fuid+","+rs.getString("KUIDJFYF")+","+rs.getString("KUIDJFZF")+","+rs.getString("YUFKJE"));
					bf.append(" );\n");
					
	
					sql=" select * from jieszbsjb t  where t.jiesdid = "+rs.getString("id");
					ResultSet rec=con.getResultSet(sql);
					
					while(rec.next()){
						
						bf.append(" insert into jieszbsjb(id,jiesdid,zhibb_id,hetbz,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id)\n");
						bf.append(" values("+MainGlobal.getNewID(Diancxxb_id)+","+lnId+","+rec.getString("zhibb_id")+",'"+rec.getString("hetbz")+"',")
							.append(rec.getString("gongf")+","+rec.getString("changf")+","+rec.getString("jies")+","+rec.getString("yingk")+","+rec.getString("zhejbz")+",")
							.append(rec.getString("zhejje")+","+rec.getString("zhuangt")+","+rec.getString("yansbhb_id")+");\n");
					}
					rec.close();
				}
				
			}catch(Exception e){
				e.printStackTrace();
				throw new Exception("error");
			}
		}
		
		bf.append("end;");
		
		if(bf.length()>13){
			
			con.getUpdate(bf.toString());
		}
		
		if(rs!=null){
			
			rs.close();
		}
		bf=null;
		return lnId;
	}
	
	public static String InsertIntoGSDiancjsb(JDBCcon con,String TableName,String TableID,
			String YF_diancjsmcb_id,String kuangfSql,String xiaosht,long diancxxb_id) throws Exception{
		
		long Diancxxb_id=0;
		String sql="";
		String sql_jj="";
		String lnId="";
		String Fuid="";	//�����㵥id
		double fengsjj = 0;
		StringBuffer sb=new StringBuffer("begin \n");
		ResultSet rs = null;
		boolean Flag = false;//����Ƿֹ�˾ֱ�Ӻ͵糧ǩ��ĺ�ͬ��ֵΪfalse;����Ƿֹ�˾�͵�����ǩ��ĺ�ͬ��ֵΪtrue��
		
		if(TableName.equals("jiesb")){
			
			double hansdj=0;
			double jiajqdj=0;
			double hansmk=0;
			double buhsmk=0;
			double meikje=0;
			double shuik=0;
			double buhsdj=0;
			
		  try{
//			  ���xiaosht��Ϊ�գ��ú�ͬһ���ǵ糧��ֹ�˾(��������ֹ�˾)�ĺ�ͬ��һ���Ƿֹ�˾�����ۺ�ͬ��
//			  1���糧�ͷֹ�˾�ĺ�ͬ
//			  	�����в��������糧��ֱֵ�Ӵ���diancjsmkb
//			  2���������ͷֹ�˾�ĺ�ͬ
//			  	�ҵ���ͬ�мӼۣ��üӼ�Ϊ�ֹ�˾���۸�������ʱ�ڿ���еļӼ�
//			  	�Ƚ��糧�Ľ��㵥�ϵĺ�˰����-�������ļӼ�+�ֹ�˾���۸�������ʱ�ڿ���еļӼۼ�Ϊ�ֹ�˾�����۵�
			  
			  	if(xiaosht!=null && !xiaosht.equals("") && Checkdsfht(xiaosht,diancxxb_id)){
//				  	�ǵ������ĺ�ͬ
					  String[] raw2=xiaosht.split(" ");
					  String hetbh = raw2[0];
					  sql_jj = " select min(jg.fengsjj) as fengsjj from hetb h,hetjgb jg where jg.hetb_id = h.id and h.leib=1 and h.hetbh = '"+hetbh+"'\n";
					  ResultSet rs_jj=con.getResultSet(sql_jj);
					  if(rs_jj.next()){
						  fengsjj = rs_jj.getDouble("fengsjj");
					  }
					  rs_jj.close();
					  Flag = true;
			  	}
			  	
//			  	1�������ܽ��㵥
			    sql="select * from jiesb where id="+TableID;
				rs=con.getResultSet(sql);
				
				if(rs.next()){
					
					if(Flag){
//						�ǵ������ĺ�ͬ
//						1����˰����=��˰����-�������ķֹ�˾�Ӽ�+��������ķֹ�˾���۵ķֹ�˾�Ӽ�
//						2����˰ú��=��˰���ۡ���������+������ǰ�ۿ�
//						3������˰ú��=��˰ú��/(1+˰��)
//						4��˰��=��˰ú��-����˰ú��
//						5�����
//						6������˰����
						hansdj = CustomMaths.add(rs.getDouble("jiajqdj"),fengsjj);
						hansmk = CustomMaths.Round_new(CustomMaths.add(CustomMaths.Round_new(CustomMaths.mul(hansdj, rs.getDouble("jiessl")),2),rs.getDouble("bukmk")),2);
						buhsmk = CustomMaths.Round_new(CustomMaths.div(hansmk,(1+rs.getDouble("shuil"))),2);
						shuik = CustomMaths.Round_new(CustomMaths.sub(hansmk, buhsmk),2);
						meikje = CustomMaths.Round_new(CustomMaths.div(CustomMaths.Round_new(CustomMaths.mul(hansdj, rs.getDouble("jiessl")),2),(1+rs.getDouble("shuil"))),2);
						buhsdj = CustomMaths.div(hansdj, (1+rs.getDouble("shuil")), 7);
						
					}else{
//						�ֹ�˾��ͬ
						hansmk = rs.getDouble("hansmk");
						meikje = rs.getDouble("meikje");
						buhsmk = rs.getDouble("buhsmk");
						shuik = rs.getDouble("shuik");
						hansdj = rs.getDouble("hansdj");
						buhsdj = rs.getDouble("buhsdj");
//						fengsjj =rs.getDouble("fengsjj");
//						jiajqdj =rs.getDouble("jiajqdj");
					}
					
					Diancxxb_id=rs.getLong("diancxxb_id");
				
					lnId=MainGlobal.getNewID(Diancxxb_id);
					Fuid=lnId;	//��id
					sb.append("insert into diancjsmkb		\n")
						.append(" (id, diancxxb_id, bianm, gongysb_id, gongysmc, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, hansdj, bukmk, hansmk, buhsmk, meikje, shuik, shuil, buhsdj, jieslx, jiesrq, hetb_id, liucztb_id, liucgzid, diancjsb_id, ranlbmjbr, ranlbmjbrq, beiz, liucgzbid,	\n")
						.append(" JIAJQDJ,FENGSJJ,YUNSFSB_ID,YUNJ,YINGD,KUID,YUNS,KOUD,JIESSLCY,JIESFRL,JIHKJB_ID,MEIKXXB_ID,HETJ,MEIKDWMC,ZHILJQ,QIYF,JIESRL,JIESLF,JIESRCRL,RUZRY,FUID,JIJLX,KUIDJFYF,KUIDJFZF,CHAOKDL,CHAOKDLX,YUFKJE,Yunfhsdj,hansyf,buhsyf,yunfjsl)\n")
						.append(" values		\n")
						.append(" ("+lnId+", "+Diancxxb_id+", '"+rs.getString("bianm")+"-XS', "+rs.getLong("gongysb_id")+", '"+rs.getString("gongysmc")+"', '"+rs.getString("faz")+"', to_date('"+FormatDate_GS(rs.getDate("fahksrq"))+"','yyyy-MM-dd'), to_date('"+FormatDate_GS(rs.getDate("fahjzrq"))+"','yyyy-MM-dd'), '"+rs.getString("meiz")+"', '"+rs.getString("daibch")+"', '"+rs.getString("yuanshr")+"', '"+rs.getString("xianshr")+"', 	\n")
						.append(" to_date('"+FormatDate_GS(rs.getDate("yansksrq"))+"','yyyy-MM-dd'), to_date('"+FormatDate_GS(rs.getDate("yansjzrq"))+"','yyyy-MM-dd'), '"+rs.getString("yansbh")+"', '"+rs.getString("shoukdw")+"', '"+rs.getString("kaihyh")+"', '"+rs.getString("zhangh")+"', '"+rs.getString("fapbh")+"', '"+rs.getString("fukfs")+"', '"+rs.getString("duifdd")+"', "+rs.getInt("ches")+", "+rs.getDouble("jiessl")+", "+rs.getDouble("guohl")+", \n")
						.append(" "+hansdj+", "+rs.getDouble("bukmk")+", "+hansmk+", "+buhsmk+", "+meikje+", "+shuik+", "+rs.getDouble("shuil")+", "+buhsdj+", "+rs.getInt("jieslx")+", to_date('"+FormatDate_GS(rs.getDate("jiesrq"))+"','yyyy-MM-dd'), "+rs.getLong("hetb_id")+", "+/*rs.getLong("liucztb_id")*/0+", \n")
						.append(" "+rs.getLong("liucgzid")+", 0, '"+rs.getString("ranlbmjbr")+"', to_date('"+FormatDate_GS(rs.getDate("ranlbmjbrq"))+"','yyyy-MM-dd'), '"+rs.getString("beiz")+"', "+/*rs.getLong("liucgzid")*/0+",	\n")
						.append(" "+jiajqdj+","+fengsjj).append(",")
						.append( rs.getString("YUNSFSB_ID")+",'"+rs.getString("YUNJ")+"',"+rs.getString("YINGD")+","+rs.getString("KUID")+","+rs.getString("YUNS")+","+rs.getString("KOUD")+","+rs.getString("JIESSLCY")+","+rs.getString("JIESFRL")+","+rs.getString("JIHKJB_ID")+","+rs.getString("MEIKXXB_ID")+","+rs.getString("HETJ")+", \n'")
						.append( rs.getString("MEIKDWMC")+"','"+rs.getString("ZHILJQ")+"',"+rs.getString("QIYF")+","+rs.getString("JIESRL")+","+rs.getString("JIESLF")+","+rs.getString("JIESRCRL")+",'"+rs.getString("RUZRY")+"',"+rs.getString("FUID")+","+rs.getString("JIJLX")).append(",")
						.append( rs.getString("KUIDJFYF")).append(",").append(rs.getString("KUIDJFZF")).append(",").append(rs.getString("CHAOKDL")).append(",").append((rs.getString("CHAOKDLX")==null||rs.getString("CHAOKDLX").equals(""))?"''":rs.getString("CHAOKDLX")).append(",").append(rs.getString("YUFKJE")).append(",")
						.append( rs.getString("Yunfhsdj")).append(",").append(rs.getString("hansyf")).append(",").append(rs.getString("buhsyf")).append(",").append(rs.getString("yunfjsl"))
						.append(");");		
					
					sb.append(kuangfSql);
					sb.append(" update jiesb set diancjsmkb_id="+lnId+" where id="+TableID+";\n");
					
					//���� ����ָ�����ݱ�
					sql=" select * from jieszbsjb t  where t.jiesdid = "+TableID;
					rs=con.getResultSet(sql);
					
					while(rs.next()){
						
						sb.append(" insert into jieszbsjb(id,jiesdid,zhibb_id,hetbz,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id)\n");
						sb.append(" values("+MainGlobal.getNewID(Diancxxb_id)+","+lnId+","+rs.getString("zhibb_id")+",'"+rs.getString("hetbz")+"',")
							.append(rs.getString("gongf")+","+rs.getString("changf")+","+rs.getString("jies")+","+rs.getString("yingk")+","+rs.getString("zhejbz")+",")
							.append(rs.getString("zhejje")+","+rs.getString("zhuangt")+","+rs.getString("yansbhb_id")+");");
					}
				}
			  	
			  	
//			  	2�������ӽ��㵥
			  	sql="select * from jiesb where fuid="+TableID;
			  	rs=con.getResultSet(sql);
			  	while(rs.next()){
			  		
			  		if(Flag){
			  			
//						�ǵ������ĺ�ͬ
//			  			1����˰����=��˰����-�������ķֹ�˾�Ӽ�+��������ķֹ�˾���۵ķֹ�˾�Ӽ�
//						2����˰ú��=��˰���ۡ���������+������ǰ�ۿ�
//						3������˰ú��=��˰ú��/(1+˰��)
//						4��˰��=��˰ú��-����˰ú��
//						5�����
//						6������˰����
						hansdj = CustomMaths.add(rs.getDouble("jiajqdj"),fengsjj);
						hansmk = CustomMaths.Round_new(CustomMaths.add(CustomMaths.Round_new(CustomMaths.mul(hansdj, rs.getDouble("jiessl")),2),rs.getDouble("bukmk")),2);
						buhsmk = CustomMaths.Round_new(CustomMaths.div(hansmk,(1+rs.getDouble("shuil"))),2);
						shuik = CustomMaths.Round_new(CustomMaths.sub(hansmk, buhsmk),2);
						meikje = CustomMaths.Round_new(CustomMaths.div(CustomMaths.Round_new(CustomMaths.mul(hansdj, rs.getDouble("jiessl")),2),(1+rs.getDouble("shuil"))),2);
						buhsdj = CustomMaths.div(hansdj, (1+rs.getDouble("shuil")), 7);
					}else{
						
//						�ֹ�˾��ͬ
						hansmk = rs.getDouble("hansmk");
						meikje = rs.getDouble("meikje");
						buhsmk = rs.getDouble("buhsmk");
						shuik = rs.getDouble("shuik");
						hansdj = rs.getDouble("hansdj");
						buhsdj = rs.getDouble("buhsdj");
					}
			  		
			  		meikje=CustomMaths.div(CustomMaths.Round_new(CustomMaths.mul(hansdj,rs.getDouble("jiessl")),2),1+rs.getDouble("shuil"),2);
					hansmk=CustomMaths.add(CustomMaths.Round_new(CustomMaths.mul(hansdj,rs.getDouble("jiessl")),2),rs.getDouble("bukmk"));
					buhsmk=CustomMaths.div(hansmk, 1+rs.getDouble("shuil") ,2);
					shuik=CustomMaths.sub(hansmk,buhsmk);
					buhsdj=CustomMaths.div(hansdj, 1+rs.getDouble("shuil"), 7);
					Diancxxb_id=rs.getLong("diancxxb_id");
					
					lnId=MainGlobal.getNewID(Diancxxb_id);
					sb.append("insert into diancjsmkb		\n")
						.append(" (id, diancxxb_id, bianm, gongysb_id, gongysmc, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, hansdj, bukmk, hansmk, buhsmk, meikje, shuik, shuil, buhsdj, jieslx, jiesrq, hetb_id, liucztb_id, liucgzid, diancjsb_id, ranlbmjbr, ranlbmjbrq, beiz, liucgzbid,	\n")
						.append(" JIAJQDJ,FENGSJJ,YUNSFSB_ID,YUNJ,YINGD,KUID,YUNS,KOUD,JIESSLCY,JIESFRL,JIHKJB_ID,MEIKXXB_ID,HETJ,MEIKDWMC,ZHILJQ,QIYF,JIESRL,JIESLF,JIESRCRL,RUZRY,FUID,JIJLX,KUIDJFYF,KUIDJFZF,CHAOKDL,CHAOKDLX,YUFKJE,Yunfhsdj,hansyf,buhsyf,yunfjsl)\n")
						.append(" values		\n")
						.append(" ("+lnId+", "+Diancxxb_id+", '"+rs.getString("bianm")+"-XS', "+rs.getLong("gongysb_id")+", '"+rs.getString("gongysmc")+"', '"+rs.getString("faz")+"', to_date('"+FormatDate_GS(rs.getDate("fahksrq"))+"','yyyy-MM-dd'), to_date('"+FormatDate_GS(rs.getDate("fahjzrq"))+"','yyyy-MM-dd'), '"+rs.getString("meiz")+"', '"+rs.getString("daibch")+"', '"+rs.getString("yuanshr")+"', '"+rs.getString("xianshr")+"', 	\n")
						.append(" to_date('"+FormatDate_GS(rs.getDate("yansksrq"))+"','yyyy-MM-dd'), to_date('"+FormatDate_GS(rs.getDate("yansjzrq"))+"','yyyy-MM-dd'), '"+rs.getString("yansbh")+"', '"+rs.getString("shoukdw")+"', '"+rs.getString("kaihyh")+"', '"+rs.getString("zhangh")+"', '"+rs.getString("fapbh")+"', '"+rs.getString("fukfs")+"', '"+rs.getString("duifdd")+"', "+rs.getInt("ches")+", "+rs.getDouble("jiessl")+", "+rs.getDouble("guohl")+", \n")
						.append(" "+hansdj+", "+rs.getDouble("bukmk")+", "+hansmk+", "+buhsmk+", "+meikje+", "+shuik+", "+rs.getDouble("shuil")+", "+buhsdj+", "+rs.getInt("jieslx")+", to_date('"+FormatDate_GS(rs.getDate("jiesrq"))+"','yyyy-MM-dd'), "+rs.getLong("hetb_id")+", "+/*rs.getLong("liucztb_id")*/0+", \n")
						.append(" "+rs.getLong("liucgzid")+", 0, '"+rs.getString("ranlbmjbr")+"', to_date('"+FormatDate_GS(rs.getDate("ranlbmjbrq"))+"','yyyy-MM-dd'), '"+rs.getString("beiz")+"', "+/*rs.getLong("liucgzid")*/0+",	\n")
						.append(" "+jiajqdj+","+fengsjj).append(",")
						.append(rs.getString("YUNSFSB_ID")+",'"+rs.getString("YUNJ")+"',"+rs.getString("YINGD")+","+rs.getString("KUID")+","+rs.getString("YUNS")+","+rs.getString("KOUD")+","+rs.getString("JIESSLCY")+","+rs.getString("JIESFRL")+","+rs.getString("JIHKJB_ID")+","+rs.getString("MEIKXXB_ID")+","+rs.getString("HETJ")+", \n'")
						.append(rs.getString("MEIKDWMC")+"','"+rs.getString("ZHILJQ")+"',"+rs.getString("QIYF")+","+rs.getString("JIESRL")+","+rs.getString("JIESLF")+","+rs.getString("JIESRCRL")+",'"+rs.getString("RUZRY")+"',"+Fuid+","+rs.getString("JIJLX")).append(",")
						.append(rs.getString("KUIDJFYF")).append(",").append(rs.getString("KUIDJFZF")).append(",").append(rs.getString("CHAOKDL")).append(",").append((rs.getString("CHAOKDLX")==null||rs.getString("CHAOKDLX").equals(""))?"''":rs.getString("CHAOKDLX")).append(",").append(rs.getString("YUFKJE")).append(",")
						.append(rs.getString("Yunfhsdj")).append(",").append(rs.getString("hansyf")).append(",").append(rs.getString("buhsyf")).append(",").append(rs.getString("yunfjsl"))
						.append(");	\n");		
					
					sb.append(kuangfSql);
					sb.append(" update jiesb set diancjsmkb_id="+lnId+" where id="+rs.getString("id")+";\n");
					
					//���� ����ָ�����ݱ�
					sql=" select * from jieszbsjb t  where t.jiesdid = "+rs.getString("id");
					ResultSet rec=con.getResultSet(sql);
					
					while(rec.next()){
						
						sb.append(" insert into jieszbsjb(id,jiesdid,zhibb_id,hetbz,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id)\n");
						sb.append(" values("+MainGlobal.getNewID(Diancxxb_id)+","+lnId+","+rec.getString("zhibb_id")+",'"+rec.getString("hetbz")+"',")
							.append(rec.getString("gongf")+","+rec.getString("changf")+","+rec.getString("jies")+","+rec.getString("yingk")+","+rec.getString("zhejbz")+",")
							.append(rec.getString("zhejje")+","+rec.getString("zhuangt")+","+rec.getString("yansbhb_id")+");\n");
					}
					rec.close();
			  	}
			  	
		  }catch(Exception e){
			  e.printStackTrace();
			  throw new Exception("error");
		  }
		}
		
		if(TableName.equals("jiesyfb")){
			
			try{
//				1���ȴ����ܽ��㵥
				sql="select * from jiesyfb where id="+TableID;
				rs=con.getResultSet(sql);
				if(rs.next()){
						
					Diancxxb_id=rs.getLong("diancxxb_id");
					lnId=MainGlobal.getNewID(Diancxxb_id);
					Fuid=lnId;
					sb.append("insert into diancjsyfb		\n")
						.append("  (id, diancxxb_id, bianm, gongysb_id, gongysmc, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, guotyf, dityf, jiskc, hansdj, bukyf, hansyf, buhsyf, shuik, shuil, buhsdj, jieslx, jiesrq, hetb_id, liucztb_id, liucgzid, beiz, diancjsb_id, diancjsmkb_id, ranlbmjbr, ranlbmjbrq, liucgzbid,	\n")
						.append("	YUNSFSB_ID,YUNJ,YINGD,KUID,YUNS,KOUD,JIESSLCY,GUOTZF,KUANGQYF,KUANGQZF,GUOTYFJF,GUOTZFJF,GONGFSL,YANSSL,YINGK,DITZF,MEIKXXB_ID,MEIKDWMC,FUID,KUIDJFYF,KUIDJFZF,YUFKJE)")
						.append(" values		\n")
						.append("  ("+lnId+", "+Diancxxb_id+", '"+rs.getString("bianm")+"-XS', "+rs.getLong("gongysb_id")+", '"+rs.getString("gongysmc")+"', '"+rs.getString("faz")+"', to_date('"+FormatDate_GS(rs.getDate("fahksrq"))+"','yyyy-MM-dd'), to_date('"+FormatDate_GS(rs.getDate("fahjzrq"))+"','yyyy-MM-dd'), '"+rs.getString("meiz")+"', '"+rs.getString("daibch")+"', '"+rs.getString("yuanshr")+"', '"+rs.getString("xianshr")+"', 	\n")
						.append(" to_date('"+FormatDate_GS(rs.getDate("yansksrq"))+"','yyyy-MM-dd'), to_date('"+FormatDate(rs.getDate("yansjzrq"))+"','yyyy-MM-dd'), '"+rs.getString("yansbh")+"', '"+rs.getString("shoukdw")+"', '"+rs.getString("kaihyh")+"', '"+rs.getString("zhangh")+"', '"+rs.getString("fapbh")+"', '"+rs.getString("fukfs")+"', '"+rs.getString("duifdd")+"', "+rs.getInt("ches")+", "+rs.getDouble("jiessl")+", "+rs.getDouble("guohl")+", \n")
						.append(" "+rs.getDouble("guotyf")+", "+rs.getDouble("dityf")+", "+rs.getDouble("jiskc")+", "+rs.getDouble("hansdj")+", "+rs.getDouble("bukyf")+", "+rs.getDouble("hansyf")+","+rs.getDouble("buhsyf")+", "+rs.getDouble("shuik")+", "+rs.getDouble("shuil")+", "+rs.getDouble("buhsdj")+", "+rs.getInt("jieslx")+",to_date('"+FormatDate_GS(rs.getDate("jiesrq"))+"','yyyy-MM-dd'), "+rs.getLong("hetb_id")+", "+rs.getLong("liucztb_id")+", \n")
						.append(" "+rs.getLong("liucgzid")+",'"+rs.getString("beiz")+"',0,"+YF_diancjsmcb_id+",'"+rs.getString("ranlbmjbr")+"',to_date('"+FormatDate_GS(rs.getDate("ranlbmjbrq"))+"','yyyy-MM-dd'),"+rs.getLong("liucgzid")+",	\n")
						.append(rs.getString("YUNSFSB_ID")+",'"+rs.getString("YUNJ")+"',"+rs.getString("YINGD")+","+rs.getString("KUID")+","+rs.getString("YUNS")+","+rs.getString("KOUD")+","+rs.getString("JIESSLCY")+","+rs.getString("GUOTZF")+",\n")
						.append(rs.getString("KUANGQYF")+","+rs.getString("KUANGQZF")+","+rs.getString("GUOTYFJF")+","+rs.getString("GUOTZFJF")+","+rs.getString("GONGFSL")+","+rs.getString("YANSSL")+","+rs.getString("YINGK")+","+rs.getString("DITZF")+",\n")
						.append(rs.getString("MEIKXXB_ID")+",'"+rs.getString("MEIKDWMC")+"',"+rs.getString("FUID")+","+rs.getString("KUIDJFYF")+","+rs.getString("KUIDJFZF")+","+rs.getString("YUFKJE")+");\n");
					
					sb.append(kuangfSql);
					sb.append(" update jiesyfb set diancjsyfb_id="+lnId+" where id="+TableID+";\n");
					
					sql=" select * from jieszbsjb t  where t.jiesdid = "+TableID;
					rs=con.getResultSet(sql);
					
					while(rs.next()){
						
						sb.append(" insert into jieszbsjb(id,jiesdid,zhibb_id,hetbz,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id)\n");
						sb.append(" values("+MainGlobal.getNewID(Diancxxb_id)+","+lnId+","+rs.getString("zhibb_id")+",'"+rs.getString("hetbz")+"',")
							.append(rs.getString("gongf")+","+rs.getString("changf")+","+rs.getString("jies")+","+rs.getString("yingk")+","+rs.getString("zhejbz")+",")
							.append(rs.getString("zhejje")+","+rs.getString("zhuangt")+","+rs.getString("yansbhb_id")+");\n");
					}
				}
				
//				2�������ӽ��㵥
				sql="select * from jiesyfb where fuid="+TableID;
				rs=con.getResultSet(sql);
				while(rs.next()){
						
					Diancxxb_id=rs.getLong("diancxxb_id");
					lnId=MainGlobal.getNewID(Diancxxb_id);
					sb.append("insert into diancjsyfb		\n")
						.append("  (id, diancxxb_id, bianm, gongysb_id, gongysmc, faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, guotyf, dityf, jiskc, hansdj, bukyf, hansyf, buhsyf, shuik, shuil, buhsdj, jieslx, jiesrq, hetb_id, liucztb_id, liucgzid, beiz, diancjsb_id, diancjsmkb_id, ranlbmjbr, ranlbmjbrq, liucgzbid,	\n")
						.append("	YUNSFSB_ID,YUNJ,YINGD,KUID,YUNS,KOUD,JIESSLCY,GUOTZF,KUANGQYF,KUANGQZF,GUOTYFJF,GUOTZFJF,GONGFSL,YANSSL,YINGK,DITZF,MEIKXXB_ID,MEIKDWMC,FUID,KUIDJFYF,KUIDJFZF,YUFKJE)")
						.append(" values		\n")
						.append("  ("+lnId+", "+Diancxxb_id+", '"+rs.getString("bianm")+"-XS', "+rs.getLong("gongysb_id")+", '"+rs.getString("gongysmc")+"', '"+rs.getString("faz")+"', to_date('"+FormatDate_GS(rs.getDate("fahksrq"))+"','yyyy-MM-dd'), to_date('"+FormatDate_GS(rs.getDate("fahjzrq"))+"','yyyy-MM-dd'), '"+rs.getString("meiz")+"', '"+rs.getString("daibch")+"', '"+rs.getString("yuanshr")+"', '"+rs.getString("xianshr")+"', 	\n")
						.append(" to_date('"+FormatDate_GS(rs.getDate("yansksrq"))+"','yyyy-MM-dd'), to_date('"+FormatDate(rs.getDate("yansjzrq"))+"','yyyy-MM-dd'), '"+rs.getString("yansbh")+"', '"+rs.getString("shoukdw")+"', '"+rs.getString("kaihyh")+"', '"+rs.getString("zhangh")+"', '"+rs.getString("fapbh")+"', '"+rs.getString("fukfs")+"', '"+rs.getString("duifdd")+"', "+rs.getInt("ches")+", "+rs.getDouble("jiessl")+", "+rs.getDouble("guohl")+", \n")
						.append(" "+rs.getDouble("guotyf")+", "+rs.getDouble("dityf")+", "+rs.getDouble("jiskc")+", "+rs.getDouble("hansdj")+", "+rs.getDouble("bukyf")+", "+rs.getDouble("hansyf")+","+rs.getDouble("buhsyf")+", "+rs.getDouble("shuik")+", "+rs.getDouble("shuil")+", "+rs.getDouble("buhsdj")+", "+rs.getInt("jieslx")+",to_date('"+FormatDate_GS(rs.getDate("jiesrq"))+"','yyyy-MM-dd'), "+rs.getLong("hetb_id")+", "+rs.getLong("liucztb_id")+", \n")
						.append(" "+rs.getLong("liucgzid")+",'"+rs.getString("beiz")+"',0,"+YF_diancjsmcb_id+",'"+rs.getString("ranlbmjbr")+"',to_date('"+FormatDate_GS(rs.getDate("ranlbmjbrq"))+"','yyyy-MM-dd'),"+rs.getLong("liucgzid")+",	\n")
						.append(rs.getString("YUNSFSB_ID")+",'"+rs.getString("YUNJ")+"',"+rs.getString("YINGD")+","+rs.getString("KUID")+","+rs.getString("YUNS")+","+rs.getString("KOUD")+","+rs.getString("JIESSLCY")+","+rs.getString("GUOTZF")+",\n")
						.append(rs.getString("KUANGQYF")+","+rs.getString("KUANGQZF")+","+rs.getString("GUOTYFJF")+","+rs.getString("GUOTZFJF")+","+rs.getString("GONGFSL")+","+rs.getString("YANSSL")+","+rs.getString("YINGK")+","+rs.getString("DITZF")+",\n")
						.append(rs.getString("MEIKXXB_ID")+",'"+rs.getString("MEIKDWMC")+"',"+Fuid+","+rs.getString("KUIDJFYF")+","+rs.getString("KUIDJFZF")+","+rs.getString("YUFKJE")+");\n");
					
					sb.append(kuangfSql);
					sb.append(" update jiesyfb set diancjsyfb_id="+lnId+" where id="+rs.getString("id")+";\n");
					
					sql=" select * from jieszbsjb t  where t.jiesdid = "+rs.getString("id");
					ResultSet rec=con.getResultSet(sql);
					
					while(rec.next()){
						
						sb.append(" insert into jieszbsjb(id,jiesdid,zhibb_id,hetbz,gongf,changf,jies,yingk,zhejbz,zhejje,zhuangt,yansbhb_id)\n");
						sb.append(" values("+MainGlobal.getNewID(Diancxxb_id)+","+lnId+","+rec.getString("zhibb_id")+",'"+rec.getString("hetbz")+"',")
							.append(rec.getString("gongf")+","+rec.getString("changf")+","+rec.getString("jies")+","+rec.getString("yingk")+","+rec.getString("zhejbz")+",")
							.append(rec.getString("zhejje")+","+rec.getString("zhuangt")+","+rec.getString("yansbhb_id")+");\n");
					}
					
					rec.close();
				}
				
			}catch(Exception e){
				e.printStackTrace();
				throw new Exception("error");
			}
		}
		
		sb.append("end;");
		
		if(rs!=null){
			
			rs.close();
		}
		
		if(sb.length()>13){
			
			con.getUpdate(sb.toString());
		}
		
		sb=null;

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
                 + " (select liuczthjid from liucdzb where liucztqqid="+Liucztb_id+" and mingc='�ύ')";
			ResultSet rec=con.getResultSet(sql);
			if(rec.next()){
				
				if(rec.getString("mingc").equals("��˾���")){
					
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
                 + " (select liuczthjid from liucdzb where liucztqqid="+Liucztb_id+" and mingc='�ύ')";
			ResultSet rec=con.getResultSet(sql);
			if(rec.next()){
				
				if(rec.getString("mingc").equals("��˾���")){
					
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
	
	public static boolean CheckHetshzt(long Hetb_id,long Diancxxb_id){
//		2008-10-17���ڽ���ʱ�����ͬû����ˣ����Խ����Խ��㣬�����ܽ��н��㵥����
//		����������ڽ���ʱ�жϺ�ͬ�����״̬�����û����˾Ͳ��ܱ���
//		2009-12-25,�ں�ͬ״̬�ж�ʱ�������ûѡ��ͬ���������㡢���㵥������Ͳ��жϸú�ͬ�����״̬
		JDBCcon con=new JDBCcon();
		boolean Flag=false;
		try{
			
			String strliucshzt=" and liucztb_id = 1 ";
			
			String strXitszzt="��";	//ϵͳ����״̬
			
			strXitszzt = MainGlobal.getXitxx_item("����", Locale.hetxyzzsh_xitxx, String.valueOf(Diancxxb_id), strXitszzt);
			
			if(!strXitszzt.equals("��")){
				
				strliucshzt = "";
			}
			
			if(Hetb_id>0){
				
				String sql="select id from hetb where id="+Hetb_id+strliucshzt+" \n" 
					+ " union	\n"
					+ " select id from hetys where id="+Hetb_id+strliucshzt+"";
				ResultSet rs=con.getResultSet(sql);
				if(rs.next()){
					
					Flag=true;
				}
				rs.close();
			}else{
				
				Flag = true;
			}
			
			
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
		// TODO �Զ����ɷ������
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
		// TODO �Զ����ɷ������
//		�õ����㵥���ͺͽ��㵥���
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
				
				strResult=rs.getString("bianm")+"��"+rs.getString("leix");
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
//		�����������ӷ���������������¡����ڵ��ڡ����ڡ�С�ڡ�С�ڵ��ڡ����䡢���ڡ�
//		����˵����TiaojΪ��������
//				TypeΪ���޻�����
//				DeafultValueĬ��ֵ
		String Lianjf="";
		Lianjf=DeafultValue;
		
		if(Type.equals("����")){
			
			if(Tiaoj.equals("���ڵ���")){
				
				Lianjf=">=";
			}else if(Tiaoj.equals("����")){
				
				Lianjf=">";
			}else if(Tiaoj.equals("С��")
					||Tiaoj.equals("С�ڵ���")
					||Tiaoj.equals("����")){
				
				Lianjf=">=";
			}else if(Tiaoj.equals("����")){
				
				Lianjf="=";
			}
		}else if(Type.equals("����")){
			
			if(Tiaoj.equals("���ڵ���")
					||Tiaoj.equals("����")){
				
				Lianjf="<=";
			}else if(Tiaoj.equals("С��")){
				
				Lianjf="<";
			}else if(Tiaoj.equals("С�ڵ���")
					||Tiaoj.equals("����")
					||Tiaoj.equals("����")){
					
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
		// TODO �Զ����ɷ������
//		2009-5-22 �������ۿ�����ã������ҵ������Ϊ�����ۿ����÷�ΧΪ�������ֵ�����,
//		Ŀǰֻ�����������ָ�꣬�������ֺ�ȫ��
		StringBuffer sb=new StringBuffer();
		sb=bsv.getMeikzkksyfw();
		String Zhib[]=null;		//��һ���õ��������ۿ���Ҫ������ָ��
		String Zhib_Zkkcl[]=null;	//�ڶ����õ�ÿ��ָ������ۿ����÷�Χ	1��Ϊ��������
		double Zengkktzsj[]=null;
//		1���������ֶ�Ӧ���۵���
//		2�����������۽��
		
		if(sb.length()>0){
			
			Zhib=sb.toString().split(";");
			
			for(int i=0;i<Zhib.length;i++){
				
				Zhib_Zkkcl=Zhib[i].split(",");
				
				if(Zhib_Zkkcl[1].equals("1")){	
//					1��Ϊ�������������ۼ�
//					�����߼�������Ӧָ��ļӼ۴���hansmj�м�ȥ��
					if(Zhib[0].equals(Locale.jiessl_zhibb)){
//						Ŀǰֻ�������ĳ�������������
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
//		�˺���Ϊ����ϵͳ��ȡ�������÷���ʱ������Ӧ��idΪ0ʱ�������û���ѡ�������µõ���Ӧ��id�ķ���
		long m_gongysb_id=0;
		
		if(gongysb_id>0){
//			�Ѿ���ʼ����Ӧ����
			m_gongysb_id=gongysb_id;
		}else{
//			û�г�ʼ����Ӧ��
			Balances bal = new Balances();
			Balances_variable bsv = new Balances_variable();
			
			try {
				bsv=bal.getBaseInfo(selids, diancxxb_id, gongysb_id , hetb_id, "��", ""
						, jieskdl);
				
				m_gongysb_id = bsv.getGongysb_Id();
				
			} catch (NumberFormatException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			} catch (Exception e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}
		}
		
		return m_gongysb_id;
	}
	
	public static String Sub_danw(String ZhibAndDw){
//		�÷�������ȥ��ָ�굥λ��
		String Zhib="";
		if(ZhibAndDw.indexOf("(")>-1){
//			��λ����С������������
			
			Zhib=ZhibAndDw.substring(0,ZhibAndDw.indexOf("(")).trim();
		}
		
		return Zhib;
	}
	
	public static boolean getHet_condition(String Hetzb,String Jieszb){
//		�÷��������ж���ͬ�۸��л��ͬ���ۿ��г��ϡ�������������������ʹ�ã���Ʒ�ֵ�
		boolean Flag = false;
		if(Hetzb==null||Hetzb.trim().equals("")||Hetzb.trim().equals("0")){
//			�����ָͬ��Ϊ�գ���ô��Ϊ����������ֵΪ��
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
		// TODO �Զ����ɷ������
//		���ں�ͬ�����еĲ�����Ŀʱʹ��
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
		// TODO �Զ����ɷ������
//		Ϊ�˽��ͬһ�Է���վ֮�䲻ͬú���в�ͬ����̵�����
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
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} catch (Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return strLic_id;
	}
	
	public static String getFahb_id_FromLie_id(String SQL,String lie_id){
//		�ڽ�����ʹ�ã�������������id��ѡ�������id
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
//		�βΣ����ù���ָ�꣬��ǰ��ָ�꣬���ù���ָ�����ޣ���ǰ��ָ������
		
//		����Ŀ�ģ�
//			�����ۿ������������������1��ָ��Ľ���ֵ��ͬʱ���������ۿ�����ʱ��
//				�����磺vdaf ����ָ��Ϊ13�������ۿ�����ΪС��15...���������ۿ�����ΪС��20...��
//				ϵͳҪ�Զ��ҵ����������ָ����������һ�����ۿ�����ִ�У�Ϊ��������߼���Ҫ��һ�²���
//				1�������ۿ�� ָ�ꡢ���������ޡ�����������		
//				2����ʱͬһָ���Ҫ����һ�����ۿ������е����ޣ���Ϊ�������ۿ��е�����ʹ�á�
		
//		�����߼���
//			1���ж�zhibxx�Ƿ�Ϊ0
//				2���ж�implementedzb�Ƿ��zhib��ȣ���Ϊͬһָ�꣩
//					����Dblimplementedzbsx
//			1(false)������zhibxx
//				2(false)������zhibxx
		double RsultValue=zhibxx;
		if(zhibxx==0){
			
			if(implementedzb.equals(zhib)&&!implementedzb.equals("")){
				
				RsultValue = Dblimplementedzbsx;
			}
		}
		
		return RsultValue;
	}
	
	public static Interpreter CountYfjsfa(Balances_variable bsv,long meikxxb_id,long diancxxb_id,
			long faz_id,long daoz_id,long yunsjgfab_id,Interpreter bsh){
	//	�����˷ѷ����������˷�
	//	�߼���
	//		1�����ҳ�licb�иÿ��Ӧ���������Ϣ�������������͵������� (�ж�������diancxxb_id��faz_id��daoz_id��meikxxb_id)��
	//		2��ͨ���õ�������̣�ȥ����yunsjgfab�е��������Ӧ��������͡��۸��ж�������lic��yunsjgfab_id����
	//		3����ͨ����������ҵ���Ӧ�ļ۸����̱��ж�Ӧ����̺���Ӽ��ɵõ��˷ѵ��ۣ��ж�������liclxb_id��
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
					"           and daoz_id = "+daoz_id+") b \n" + 
					"\n" + 
					" where a.liclxb_id = b.liclxb_id";

			ResultSet rs = con.getResultSet(sql);
			while(rs.next()){
				
				bsh.set("��ͬ�˼�", rs.getDouble("yunfjg"));
			}
			
			String yunju="";
			sql="select liclxb.mingc, licb.zhi\n" + 
				"	from licb,liclxb\n" + 
				"		where meikxxb_id = "+meikxxb_id+"\n" + 
				"			and licb.liclxb_id = liclxb.id\n" +
				"			and diancxxb_id = "+diancxxb_id+"\n" + 
				"           and faz_id = "+faz_id+"\n" + 
				"           and daoz_id = "+daoz_id+"";
			
			rs = con.getResultSet(sql);
			while(rs.next()){
				
				yunju+=rs.getString("mingc")+":"+rs.getString("zhi")+",";
			}
			if(yunju.lastIndexOf(",")>-1){
				
				yunju=yunju.substring(0,yunju.lastIndexOf(","));
			}
			bsv.setYunju_jsbz(yunju);
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
		
//		�������ã��ں�ͬ�У�ͨ��������������λ kcal/kg,mj/kg������������λ�ڻ���ת��������С�������������
//			�ڽ���ļ������������Ϊ��׼ȷͨ���������ͬ�е�λһ�µĵ�λ������
//			�ڽ�����ʾ��ʱ������Ϊ�����뻮һͳһ������ kcal/kgΪ��ʾ��λ����ʱ����� mj/kg����ĵ��ݻ����ӯ���ϵ����
//			�˺���Ϊ�������������ڱ�������֮ǰ��ӯ���ֶν������㣬��jiessl-hetbz�������ͬ��׼��һ�����䣬������Ϊӯ������
//		yingk=0;
		if(hetbz.indexOf("-")==-1&&!hetbz.equals("")){
//			˵����ͬ��׼�в����ڡ�-�����ӷ��������¼���ӯ��
//			ӯ��=�����׼-��ͬ��׼
			yingk=CustomMaths.sub(jiesbz, Double.parseDouble(hetbz));
		}
		
		return yingk;
	}
	
	public static StringBuffer InsertDanpcjsmkb(Balances_variable bsv, IPropertySelectionModel ZhibModel, String Type, String lie_id){
		
//		�������ã��õ�ú����˷��� danpcjsmxb �в�������
//		�βΣ� Type ��������ͣ�"MK"��"YF"��
//		2010-05-14	zsj�ģ���������������danpcjsmxb�ı��淽��		��ָ��+zhesl�ж�
//			��danpcjsmxb������ "�ۿ۷�ʽ" �ֶ� number 1 ��0���ۼۣ�1���۶֣�
		
		StringBuffer sb = new StringBuffer("");
		
		if((!bsv.getShul_ht().equals(""))&&bsv.getShul_zsl()==0){
//			�ۼ���Ϣ
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Shul_zhibb), 
						bsv.getShul_ht().trim(), bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getShul_yk(), bsv.getShulzb_zdj(), bsv.getShulzb_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(CustomMaths.mul(bsv.getHetmdj(),(1+bsv.getMeiksl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
			
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Shul_zhibb), 
						bsv.getShul_ht().trim(), bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getShul_yk(), bsv.getShulzb_zdj(), bsv.getShulzb_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
				
			}
		}
		
		if(bsv.getShul_zsl()!=0){
//			��������Ϣ
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Shul_zhibb), 
						bsv.getShul_ht().trim(), bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getShul_yk(), bsv.getShulzb_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(CustomMaths.mul(bsv.getHetmdj(),(1+bsv.getMeiksl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
			
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Shul_zhibb), 
						bsv.getShul_ht().trim(), bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getShul_yk(), bsv.getShulzb_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
				
			}
		}
		
		if((!bsv.getQnetar_ht().equals(""))&&bsv.getQnetar_zsl()==0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Qnetar_zhibb), 
						MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_ht().trim(),"-",0), MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_kf(),0,bsv.getMj_to_kcal_xsclfs()), MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_cf(),0,bsv.getMj_to_kcal_xsclfs()),
						MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_js(),0,bsv.getMj_to_kcal_xsclfs()), 
						reCoundYk(MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_ht().trim(),"-",0),MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_js(),0,bsv.getMj_to_kcal_xsclfs()),bsv.getQnetar_yk()), bsv.getQnetar_zdj(), bsv.getQnetar_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
			
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Qnetar_zhibb), 
						MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_ht().trim(),"-",0), MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_kf(),0,bsv.getMj_to_kcal_xsclfs()), MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_cf(),0,bsv.getMj_to_kcal_xsclfs()),
						MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_js(),0,bsv.getMj_to_kcal_xsclfs()), 
						reCoundYk(MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_ht().trim(),"-",0),MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_js(),0,bsv.getMj_to_kcal_xsclfs()),bsv.getQnetar_yk()), bsv.getQnetar_zdj(), bsv.getQnetar_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
			}
		}
		
		if(bsv.getQnetar_zsl()!=0){
//			������������Ϣ
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Qnetar_zhibb), 
						MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_ht().trim(),"-",0), MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_kf(),0,bsv.getMj_to_kcal_xsclfs()), MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_cf(),0,bsv.getMj_to_kcal_xsclfs()),
						MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_js(),0,bsv.getMj_to_kcal_xsclfs()), 
						reCoundYk(MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_ht().trim(),"-",0),MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_js(),0,bsv.getMj_to_kcal_xsclfs()),bsv.getQnetar_yk()), bsv.getQnetar_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
			
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Qnetar_zhibb), 
						MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_ht().trim(),"-",0), MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_kf(),0,bsv.getMj_to_kcal_xsclfs()), MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_cf(),0,bsv.getMj_to_kcal_xsclfs()),
						MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_js(),0,bsv.getMj_to_kcal_xsclfs()), 
						reCoundYk(MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_ht().trim(),"-",0),MainGlobal.Mjkg_to_kcalkg(bsv.getQnetar_js(),0,bsv.getMj_to_kcal_xsclfs()),bsv.getQnetar_yk()), bsv.getQnetar_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
			}
		}
		
		if((!bsv.getAd_ht().equals(""))&&bsv.getAd_zsl()==0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Ad_zhibb), 
						bsv.getAd_ht().trim(), bsv.getAd_kf(), bsv.getAd_cf(), bsv.getAd_js(), 
						reCoundYk(bsv.getAd_ht().trim(),bsv.getAd_js(),bsv.getAd_yk()), bsv.getAd_zdj(), bsv.getAd_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Ad_zhibb), 
						bsv.getAd_ht().trim(), bsv.getAd_kf(), bsv.getAd_cf(), bsv.getAd_js(), 
						reCoundYk(bsv.getAd_ht().trim(),bsv.getAd_js(),bsv.getAd_yk()), bsv.getAd_zdj(), bsv.getAd_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
			}
		}
		
		if(bsv.getAd_zsl()!=0){
//			������������Ϣ
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Ad_zhibb), 
						bsv.getAd_ht().trim(), bsv.getAd_kf(), bsv.getAd_cf(), bsv.getAd_js(), 
						reCoundYk(bsv.getAd_ht().trim(),bsv.getAd_js(),bsv.getAd_yk()), bsv.getAd_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Ad_zhibb), 
						bsv.getAd_ht().trim(), bsv.getAd_kf(), bsv.getAd_cf(), bsv.getAd_js(), 
						reCoundYk(bsv.getAd_ht().trim(),bsv.getAd_js(),bsv.getAd_yk()), bsv.getAd_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
			}
		}
		
		
		if((!bsv.getStd_ht().equals(""))&&bsv.getStd_zsl()==0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Std_zhibb), 
						bsv.getStd_ht().trim(), bsv.getStd_kf(), bsv.getStd_cf(), bsv.getStd_js(), 
						reCoundYk(bsv.getStd_ht().trim(),bsv.getStd_js(),bsv.getStd_yk()), bsv.getStd_zdj(), bsv.getStd_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Std_zhibb), 
						bsv.getStd_ht().trim(), bsv.getStd_kf(), bsv.getStd_cf(), bsv.getStd_js(), 
						reCoundYk(bsv.getStd_ht().trim(),bsv.getStd_js(),bsv.getStd_yk()), bsv.getStd_zdj(), bsv.getStd_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
			}
		}
		
		if(bsv.getStd_zsl()!=0){
//			������������Ϣ
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Std_zhibb), 
						bsv.getStd_ht().trim(), bsv.getStd_kf(), bsv.getStd_cf(), bsv.getStd_js(), 
						reCoundYk(bsv.getStd_ht().trim(),bsv.getStd_js(),bsv.getStd_yk()), bsv.getStd_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Std_zhibb), 
						bsv.getStd_ht().trim(), bsv.getStd_kf(), bsv.getStd_cf(), bsv.getStd_js(), 
						reCoundYk(bsv.getStd_ht().trim(),bsv.getStd_js(),bsv.getStd_yk()), bsv.getStd_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
			}
		}
		
		if((!bsv.getVdaf_ht().equals(""))&&bsv.getVdaf_zsl()==0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Vdaf_zhibb), 
						bsv.getVdaf_ht().trim(), bsv.getVdaf_kf(), bsv.getVdaf_cf(), bsv.getVdaf_js(), 
						reCoundYk(bsv.getVdaf_ht().trim(),bsv.getVdaf_js(),bsv.getVdaf_yk()), bsv.getVdaf_zdj(), bsv.getVdaf_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Vdaf_zhibb), 
						bsv.getVdaf_ht().trim(), bsv.getVdaf_kf(), bsv.getVdaf_cf(), bsv.getVdaf_js(), 
						reCoundYk(bsv.getVdaf_ht().trim(),bsv.getVdaf_js(),bsv.getVdaf_yk()), bsv.getVdaf_zdj(), bsv.getVdaf_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
			}
		}
		
		if(bsv.getVdaf_zsl()!=0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Vdaf_zhibb), 
						bsv.getVdaf_ht().trim(), bsv.getVdaf_kf(), bsv.getVdaf_cf(), bsv.getVdaf_js(), 
						reCoundYk(bsv.getVdaf_ht().trim(),bsv.getVdaf_js(),bsv.getVdaf_yk()), bsv.getVdaf_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Vdaf_zhibb), 
						bsv.getVdaf_ht().trim(), bsv.getVdaf_kf(), bsv.getVdaf_cf(), bsv.getVdaf_js(), 
						reCoundYk(bsv.getVdaf_ht().trim(),bsv.getVdaf_js(),bsv.getVdaf_yk()), bsv.getVdaf_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
			}
		}
		
		if((!bsv.getMt_ht().equals(""))&&bsv.getMt_zsl()==0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Mt_zhibb), 
						bsv.getMt_ht().trim(), bsv.getMt_kf(), bsv.getMt_cf(), bsv.getMt_js(), 
						reCoundYk(bsv.getMt_ht().trim(),bsv.getMt_js(),bsv.getMt_yk()), bsv.getMt_zdj(), bsv.getMt_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Mt_zhibb), 
						bsv.getMt_ht().trim(), bsv.getMt_kf(), bsv.getMt_cf(), bsv.getMt_js(), 
						reCoundYk(bsv.getMt_ht().trim(),bsv.getMt_js(),bsv.getMt_yk()), bsv.getMt_zdj(), bsv.getMt_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(),  
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
			}
		}
		
		if(bsv.getMt_zsl()!=0){
			
//			��������
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Mt_zhibb), 
						bsv.getMt_ht().trim(), bsv.getMt_kf(), bsv.getMt_cf(), bsv.getMt_js(), 
						reCoundYk(bsv.getMt_ht().trim(),bsv.getMt_js(),bsv.getMt_yk()), bsv.getMt_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Mt_zhibb), 
						bsv.getMt_ht().trim(), bsv.getMt_kf(), bsv.getMt_cf(), bsv.getMt_js(), 
						reCoundYk(bsv.getMt_ht().trim(),bsv.getMt_js(),bsv.getMt_yk()), bsv.getMt_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(),  
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
			}
		}
		
		if((!bsv.getQgrad_ht().equals(""))&&bsv.getQgrad_zsl()==0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Qgrad_zhibb), 
						MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_ht().trim(),"-",0), MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_kf(),0,bsv.getMj_to_kcal_xsclfs()), MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_cf(),0,bsv.getMj_to_kcal_xsclfs()), 
						MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_js(),0,bsv.getMj_to_kcal_xsclfs()), 
						reCoundYk(MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_ht().trim(),"-",0),MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_js(),0,bsv.getMj_to_kcal_xsclfs()),bsv.getQgrad_yk()), bsv.getQgrad_zdj(), bsv.getQgrad_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Qgrad_zhibb), 
						MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_ht().trim(),"-",0), MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_kf(),0,bsv.getMj_to_kcal_xsclfs()), MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_cf(),0,bsv.getMj_to_kcal_xsclfs()), 
						MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_js(),0,bsv.getMj_to_kcal_xsclfs()), 
						reCoundYk(MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_ht().trim(),"-",0),MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_js(),0,bsv.getMj_to_kcal_xsclfs()),bsv.getQgrad_yk()), bsv.getQgrad_zdj(), bsv.getQgrad_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
			}
		}
		
		if(bsv.getQgrad_zsl()!=0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Qgrad_zhibb), 
						MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_ht().trim(),"-",0), MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_kf(),0,bsv.getMj_to_kcal_xsclfs()), MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_cf(),0,bsv.getMj_to_kcal_xsclfs()), 
						MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_js(),0,bsv.getMj_to_kcal_xsclfs()), 
						reCoundYk(MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_ht().trim(),"-",0),MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_js(),0,bsv.getMj_to_kcal_xsclfs()),bsv.getQgrad_yk()), bsv.getQgrad_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Qgrad_zhibb), 
						MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_ht().trim(),"-",0), MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_kf(),0,bsv.getMj_to_kcal_xsclfs()), MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_cf(),0,bsv.getMj_to_kcal_xsclfs()), 
						MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_js(),0,bsv.getMj_to_kcal_xsclfs()), 
						reCoundYk(MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_ht().trim(),"-",0),MainGlobal.Mjkg_to_kcalkg(bsv.getQgrad_js(),0,bsv.getMj_to_kcal_xsclfs()),bsv.getQgrad_yk()), bsv.getQgrad_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
			}
		}
		
		if((!bsv.getQbad_ht().equals(""))&&bsv.getQbad_zsl()==0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Qbad_zhibb), 
						MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_ht().trim(),"-",0), MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_kf(),0,bsv.getMj_to_kcal_xsclfs()), MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_cf(),0,bsv.getMj_to_kcal_xsclfs()), 
						MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_js(),0,bsv.getMj_to_kcal_xsclfs()), 
						reCoundYk(MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_ht().trim(),"-",0),MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_js(),0,bsv.getMj_to_kcal_xsclfs()),bsv.getQbad_yk()), bsv.getQbad_zdj(), bsv.getQbad_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
				
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Qbad_zhibb), 
						MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_ht().trim(),"-",0), MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_kf(),0,bsv.getMj_to_kcal_xsclfs()), MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_cf(),0,bsv.getMj_to_kcal_xsclfs()), 
						MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_js(),0,bsv.getMj_to_kcal_xsclfs()), 
						reCoundYk(MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_ht().trim(),"-",0),MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_js(),0,bsv.getMj_to_kcal_xsclfs()),bsv.getQbad_yk()), bsv.getQbad_zdj(), bsv.getQbad_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(),  bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
			}
		}
		
		if(bsv.getQbad_zsl()!=0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Qbad_zhibb), 
						MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_ht().trim(),"-",0), MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_kf(),0,bsv.getMj_to_kcal_xsclfs()), MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_cf(),0,bsv.getMj_to_kcal_xsclfs()), 
						MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_js(),0,bsv.getMj_to_kcal_xsclfs()), 
						reCoundYk(MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_ht().trim(),"-",0),MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_js(),0,bsv.getMj_to_kcal_xsclfs()),bsv.getQbad_yk()), bsv.getQbad_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
				
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Qbad_zhibb), 
						MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_ht().trim(),"-",0), MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_kf(),0,bsv.getMj_to_kcal_xsclfs()), MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_cf(),0,bsv.getMj_to_kcal_xsclfs()), 
						MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_js(),0,bsv.getMj_to_kcal_xsclfs()), 
						reCoundYk(MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_ht().trim(),"-",0),MainGlobal.Mjkg_to_kcalkg(bsv.getQbad_js(),0,bsv.getMj_to_kcal_xsclfs()),bsv.getQbad_yk()), bsv.getQbad_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(),  bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
			}
		}
		
		if((!bsv.getHad_ht().equals(""))&&bsv.getHad_zsl()==0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Had_zhibb), 
						bsv.getHad_ht().trim(), bsv.getHad_kf(), bsv.getHad_cf(), bsv.getHad_js(), 
						reCoundYk(bsv.getHad_ht().trim(),bsv.getHad_js(),bsv.getHad_yk()), bsv.getHad_zdj(), bsv.getHad_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Had_zhibb), 
						bsv.getHad_ht().trim(), bsv.getHad_kf(), bsv.getHad_cf(), bsv.getHad_js(), 
						reCoundYk(bsv.getHad_ht().trim(),bsv.getHad_js(),bsv.getHad_yk()), bsv.getHad_zdj(), bsv.getHad_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
				
			}
		}
		
		if(bsv.getHad_zsl()!=0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Had_zhibb), 
						bsv.getHad_ht().trim(), bsv.getHad_kf(), bsv.getHad_cf(), bsv.getHad_js(), 
						reCoundYk(bsv.getHad_ht().trim(),bsv.getHad_js(),bsv.getHad_yk()), bsv.getHad_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Had_zhibb), 
						bsv.getHad_ht().trim(), bsv.getHad_kf(), bsv.getHad_cf(), bsv.getHad_js(), 
						reCoundYk(bsv.getHad_ht().trim(),bsv.getHad_js(),bsv.getHad_yk()), bsv.getHad_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
				
			}
		}
		
		if((!bsv.getStad_ht().equals(""))&&bsv.getStad_zsl()==0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Stad_zhibb), 
						bsv.getStad_ht().trim(), bsv.getStad_kf(), bsv.getStad_cf(), bsv.getStad_js(), 
						reCoundYk(bsv.getStad_ht().trim(),bsv.getStad_js(),bsv.getStad_yk()), bsv.getStad_zdj(), bsv.getStad_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Stad_zhibb), 
						bsv.getStad_ht().trim(), bsv.getStad_kf(), bsv.getStad_cf(), bsv.getStad_js(), 
						reCoundYk(bsv.getStad_ht().trim(),bsv.getStad_js(),bsv.getStad_yk()), bsv.getStad_zdj(), bsv.getStad_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
				
			}
		}
		
		if(bsv.getStad_zsl()!=0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Stad_zhibb), 
						bsv.getStad_ht().trim(), bsv.getStad_kf(), bsv.getStad_cf(), bsv.getStad_js(), 
						reCoundYk(bsv.getStad_ht().trim(),bsv.getStad_js(),bsv.getStad_yk()), bsv.getStad_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Stad_zhibb), 
						bsv.getStad_ht().trim(), bsv.getStad_kf(), bsv.getStad_cf(), bsv.getStad_js(), 
						reCoundYk(bsv.getStad_ht().trim(),bsv.getStad_js(),bsv.getStad_yk()), bsv.getStad_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
				
			}
		}
		
		if((!bsv.getMad_ht().equals(""))&&bsv.getMad_zsl()==0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Mad_zhibb), 
						bsv.getMad_ht().trim(), bsv.getMad_kf(), bsv.getMad_cf(), bsv.getMad_js(), 
						reCoundYk(bsv.getMad_ht().trim(),bsv.getMad_js(),bsv.getMad_yk()), bsv.getMad_zdj(), bsv.getMad_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Mad_zhibb), 
						bsv.getMad_ht().trim(), bsv.getMad_kf(), bsv.getMad_cf(), bsv.getMad_js(), 
						reCoundYk(bsv.getMad_ht().trim(),bsv.getMad_js(),bsv.getMad_yk()), bsv.getMad_zdj(), bsv.getMad_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
				
			}
		}
		
		if(bsv.getMad_zsl()!=0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Mad_zhibb), 
						bsv.getMad_ht().trim(), bsv.getMad_kf(), bsv.getMad_cf(), bsv.getMad_js(), 
						reCoundYk(bsv.getMad_ht().trim(),bsv.getMad_js(),bsv.getMad_yk()), bsv.getMad_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Mad_zhibb), 
						bsv.getMad_ht().trim(), bsv.getMad_kf(), bsv.getMad_cf(), bsv.getMad_js(), 
						reCoundYk(bsv.getMad_ht().trim(),bsv.getMad_js(),bsv.getMad_yk()), bsv.getMad_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
				
			}
		}
		
		if((!bsv.getAar_ht().equals(""))&&bsv.getAar_zsl()==0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Aar_zhibb), 
						bsv.getAar_ht().trim(), bsv.getAar_kf(), bsv.getAar_cf(), bsv.getAar_js(), 
						reCoundYk(bsv.getAar_ht().trim(),bsv.getAar_js(),bsv.getAar_yk()), bsv.getAar_zdj(), bsv.getAar_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Aar_zhibb), 
						bsv.getAar_ht().trim(), bsv.getAar_kf(), bsv.getAar_cf(), bsv.getAar_js(), 
						reCoundYk(bsv.getAar_ht().trim(),bsv.getAar_js(),bsv.getAar_yk()), bsv.getAar_zdj(), bsv.getAar_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
				
			}
		}
		
		if(bsv.getAar_zsl()!=0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Aar_zhibb), 
						bsv.getAar_ht().trim(), bsv.getAar_kf(), bsv.getAar_cf(), bsv.getAar_js(), 
						reCoundYk(bsv.getAar_ht().trim(),bsv.getAar_js(),bsv.getAar_yk()), bsv.getAar_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Aar_zhibb), 
						bsv.getAar_ht().trim(), bsv.getAar_kf(), bsv.getAar_cf(), bsv.getAar_js(), 
						reCoundYk(bsv.getAar_ht().trim(),bsv.getAar_js(),bsv.getAar_yk()), bsv.getAar_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
				
			}
		}
		
		if((!bsv.getAad_ht().equals(""))&&bsv.getAad_zsl()==0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Aad_zhibb), 
						bsv.getAad_ht().trim(), bsv.getAad_kf(), bsv.getAad_cf(), bsv.getAad_js(), 
						reCoundYk(bsv.getAad_ht().trim(),bsv.getAad_js(),bsv.getAad_yk()), bsv.getAad_zdj(), bsv.getAad_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Aad_zhibb), 
						bsv.getAad_ht().trim(), bsv.getAad_kf(), bsv.getAad_cf(), bsv.getAad_js(), 
						reCoundYk(bsv.getAad_ht().trim(),bsv.getAad_js(),bsv.getAad_yk()), bsv.getAad_zdj(), bsv.getAad_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
			}
		}
		
		if(bsv.getAad_zsl()!=0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Aad_zhibb), 
						bsv.getAad_ht().trim(), bsv.getAad_kf(), bsv.getAad_cf(), bsv.getAad_js(), 
						reCoundYk(bsv.getAad_ht().trim(),bsv.getAad_js(),bsv.getAad_yk()), bsv.getAad_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Aad_zhibb), 
						bsv.getAad_ht().trim(), bsv.getAad_kf(), bsv.getAad_cf(), bsv.getAad_js(), 
						reCoundYk(bsv.getAad_ht().trim(),bsv.getAad_js(),bsv.getAad_yk()), bsv.getAad_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
			}
		}
		
		if((!bsv.getVad_ht().equals(""))&&bsv.getVad_zsl()==0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Vad_zhibb), 
						bsv.getVad_ht().trim(), bsv.getVad_kf(), bsv.getVad_cf(), bsv.getVad_js(), 
						reCoundYk(bsv.getVad_ht().trim(),bsv.getVad_js(),bsv.getVad_yk()), bsv.getVad_zdj(), bsv.getVad_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Vad_zhibb), 
						bsv.getVad_ht().trim(), bsv.getVad_kf(), bsv.getVad_cf(), bsv.getVad_js(), 
						reCoundYk(bsv.getVad_ht().trim(),bsv.getVad_js(),bsv.getVad_yk()), bsv.getVad_zdj(), bsv.getVad_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
			}
		}
		
		if(bsv.getVad_zsl()!=0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Vad_zhibb), 
						bsv.getVad_ht().trim(), bsv.getVad_kf(), bsv.getVad_cf(), bsv.getVad_js(), 
						reCoundYk(bsv.getVad_ht().trim(),bsv.getVad_js(),bsv.getVad_yk()), bsv.getVad_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Vad_zhibb), 
						bsv.getVad_ht().trim(), bsv.getVad_kf(), bsv.getVad_cf(), bsv.getVad_js(), 
						reCoundYk(bsv.getVad_ht().trim(),bsv.getVad_js(),bsv.getVad_yk()), bsv.getVad_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
			}
		}
		
		if((!bsv.getT2_ht().equals(""))&&bsv.getT2_zsl()==0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.T2_zhibb), 
						bsv.getT2_ht().trim(), bsv.getT2_kf(), bsv.getT2_cf(), bsv.getT2_js(), 
						reCoundYk(bsv.getT2_ht().trim(),bsv.getT2_js(),bsv.getT2_yk()), bsv.getT2_zdj(), bsv.getT2_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.T2_zhibb), 
						bsv.getT2_ht().trim(), bsv.getT2_kf(), bsv.getT2_cf(), bsv.getT2_js(), 
						reCoundYk(bsv.getT2_ht().trim(),bsv.getT2_js(),bsv.getT2_yk()), bsv.getT2_zdj(), bsv.getT2_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
				
			}
		}
		
		if(bsv.getT2_zsl()!=0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.T2_zhibb), 
						bsv.getT2_ht().trim(), bsv.getT2_kf(), bsv.getT2_cf(), bsv.getT2_js(), 
						reCoundYk(bsv.getT2_ht().trim(),bsv.getT2_js(),bsv.getT2_yk()), bsv.getT2_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.T2_zhibb), 
						bsv.getT2_ht().trim(), bsv.getT2_kf(), bsv.getT2_cf(), bsv.getT2_js(), 
						reCoundYk(bsv.getT2_ht().trim(),bsv.getT2_js(),bsv.getT2_yk()), bsv.getT2_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
				
			}
		}
		
		if((!bsv.getYunju_ht().equals(""))&&bsv.getYunju_zsl()==0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Yunju_zhibb), 
						bsv.getYunju_ht().trim(), bsv.getYunju_kf(), bsv.getYunju_cf(), bsv.getYunju_js(), 
						reCoundYk(bsv.getYunju_ht().trim(),bsv.getYunju_js(),bsv.getYunju_yk()), bsv.getYunju_zdj(), bsv.getYunju_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Yunju_zhibb), 
						bsv.getYunju_ht().trim(), bsv.getYunju_kf(), bsv.getYunju_cf(), bsv.getYunju_js(), 
						reCoundYk(bsv.getYunju_ht().trim(),bsv.getYunju_js(),bsv.getYunju_yk()), bsv.getYunju_zdj(), bsv.getYunju_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
			}
		}
		
		if(bsv.getYunju_zsl()!=0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Yunju_zhibb), 
						bsv.getYunju_ht().trim(), bsv.getYunju_kf(), bsv.getYunju_cf(), bsv.getYunju_js(), 
						reCoundYk(bsv.getYunju_ht().trim(),bsv.getYunju_js(),bsv.getYunju_yk()), bsv.getYunju_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Yunju_zhibb), 
						bsv.getYunju_ht().trim(), bsv.getYunju_kf(), bsv.getYunju_cf(), bsv.getYunju_js(), 
						reCoundYk(bsv.getYunju_ht().trim(),bsv.getYunju_js(),bsv.getYunju_yk()), bsv.getYunju_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
			}
		}
		
		if((!bsv.getStar_ht().equals(""))&&bsv.getStar_zsl()==0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Star_zhibb), 
						bsv.getStar_ht().trim(), bsv.getStar_kf(), bsv.getStar_cf(), bsv.getStar_js(), 
						reCoundYk(bsv.getStar_ht().trim(),bsv.getStar_js(),bsv.getStar_yk()), bsv.getStar_zdj(), bsv.getStar_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Star_zhibb), 
						bsv.getStar_ht().trim(), bsv.getStar_kf(), bsv.getStar_cf(), bsv.getStar_js(), 
						reCoundYk(bsv.getStar_ht().trim(),bsv.getStar_js(),bsv.getStar_yk()), bsv.getStar_zdj(), bsv.getStar_zje(), 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhej, bsv.getYingksl()));
			}
		}
		
		if(bsv.getStar_zsl()!=0){
			
			if(Type.equals("MK")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh(), bsv.getMeikjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Star_zhibb), 
						bsv.getStar_ht().trim(), bsv.getStar_kf(), bsv.getStar_cf(), bsv.getStar_js(), 
						reCoundYk(bsv.getStar_ht().trim(),bsv.getStar_js(),bsv.getStar_yk()), bsv.getStar_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getJiajqdj(), bsv.getShulzjbz(), bsv.getJiakhj(), bsv.getJiaksk(), bsv.getJiashj(), 
						bsv.getJijlx()==0?bsv.getHetmdj():CustomMaths.Round_new(bsv.getHetmdj()*(1+bsv.getMeiksl()), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getJiashj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
				
			}else if(Type.equals("YF")){
				
				sb.append(Jiesdcz.getDanpcjsmxb_InsertSql(bsv.getDiancxxb_id(), bsv.getXuh_yf(), bsv.getYunfjsb_id(), Jiesdcz.getProperId(ZhibModel, Locale.Star_zhibb), 
						bsv.getStar_ht().trim(), bsv.getStar_kf(), bsv.getStar_cf(), bsv.getStar_js(), 
						reCoundYk(bsv.getStar_ht().trim(),bsv.getStar_js(),bsv.getStar_yk()), bsv.getStar_zsl(), 0, 
						bsv.getGongfsl(), bsv.getYanssl(), bsv.getJiessl(), bsv.getKoud(), bsv.getKous(), bsv.getKouz(), bsv.getChes(), bsv.getJingz(), 
						bsv.getKoud_js(), bsv.getYuns(), bsv.getJiesslcy(), bsv.getYunfjsdj(), bsv.getYunfjsdj(), bsv.getBuhsyf(), bsv.getYunfsk(), bsv.getYunzfhj(), 
						bsv.getJijlx()==0?bsv.getHetyj():CustomMaths.Round_new(CustomMaths.div(bsv.getHetyj(),(1-bsv.getYunfsl())), 2), bsv.getQnetar_js(), bsv.getStd_js(), 
						bsv.getStad_js(), bsv.getStar_js(), bsv.getVdaf_js(), bsv.getMt_js(), 
						bsv.getMad_js(), bsv.getAad_js(), bsv.getAd_js(), bsv.getAar_js(), bsv.getVad_js(), bsv.getYunzfhj(), bsv.getMeikxxb_Id(), 
						bsv.getFaz_Id(), bsv.getChaokdl(), lie_id, Locale.zengkfs_zhed, bsv.getYingksl()));
			}
		}
		
		return sb;
	}
	
	public static StringBuffer getDanpcjsmxb_InsertSql(long diancxxb_id, int xuh, long meikjsb_id, long zhibb_id,
			String hetbz, double kuangf, double changf, double jies, double yingk, double zhejbz, double zhejje,
			double gongfsl, double yanssl, double jiessl, double koud, double kous, double kouz, long ches,
			double jingz, double koud_js, double yuns, double jiesslcy, double jiajqdj, double jiesdj, double jiakhj, double jiaksk,
			double jiashj, double hetj, double qnetar, double std, double stad, double star, double vdaf, double mt,
			double mad, double aad, double ad, double aar, double vad, double zongje, long meikxxb_id, long faz_id, 
			double chaokdl, String lie_id, int zhekfs, double shulzbyk){
		
		StringBuffer sb = new StringBuffer("");
		
		sb.append("insert into danpcjsmxb (id, xuh, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, gongfsl, yanssl, jiessl, koud, kous, kouz, \n")
			.append(" ches, jingz, koud_js, yuns, jiesslcy, jiajqdj, jiesdj, jiakhj, jiaksk, jiashj, biaomdj, buhsbmdj, leib, hetj, qnetar, std, stad, star, vdaf, mt, mad, aad, \n")
			.append(" ad, aar, vad, zongje, meikxxb_id, faz_id, chaokdl, lie_id, zhekfs, shulzbyk) \n")
			.append("	values(		\n")
			.append("getnewid("+diancxxb_id+"),"+xuh+","+meikjsb_id+","+zhibb_id+",'"+hetbz+"',"+kuangf+","+changf+","+jies+",")
			.append(yingk+","+zhejbz+","+zhejje+","+gongfsl+","+yanssl+","+jiessl+","+koud+","+kous+","+kouz+","+ches+","+jingz+","+koud_js+","+yuns+",")
			.append(jiesslcy+","+jiajqdj+","+jiesdj+","+jiakhj+","+jiaksk+","+jiashj+",0,0,1,"+hetj+","+qnetar+","+std+","+stad+","+star+","+vdaf+","+mt+","+mad+","+aad+",")
			.append(ad+","+aar+","+vad+","+zongje+","+meikxxb_id+","+faz_id+","+chaokdl+",'"+lie_id+"',"+zhekfs+","+ shulzbyk +");\n");
		
		return sb;
	}
	
	public static String getJieszbtscl(String Zhibbm,String SelId,int Shiyfw){
		
//		�������ƣ�����ָ�����⴦��
//		���ܣ�������ָ�������󣨸õ����ε����Ρ��ü�Ȩƽ����Ȩƽ������
//			�����������۸���������Ҫ���⴦���ָ�꣬�ڴ˽������⴦��
//		������ָ����룬Ҫ�����lie_id
		String zhibs="";
		String sql="";
		JDBCcon con = new JDBCcon();
		ResultSetList rsl=null;
		int i=0;
			
		if(Zhibbm.equals(Locale.Yunju_zhibb)&&Shiyfw==0){
//				�˾ദ��Ŀǰֻ֧�ֵ�λΪ��Km�����˾�ָ��
			sql=
				"select meikxxb_id,zhi from meiksxglb,danwb\n" +
				"       where shuxbm='"+Locale.Yunju_zhibb+"'\n" + 
				"         and meiksxglb.danwb_id=danwb.id\n" + 
				"         and danwb.bianm='"+Locale.qianm_daw+"'\n" + 
				"         and meikxxb_id in\n" + 
				"         (select meikxxb_id from fahb\n" + 
				"                where lie_id in ("+SelId+"))";
			rsl = con.getResultSetList(sql);
			
			if(rsl.getRows()>=1){
//				����������1��������������壬����ָ��Ӧ�ڼ�����������ʱ�õ�
				while(rsl.next()){
//					�ṹ��id,ָ�����,ָ���������,�������ݵ�ֵ,����ָ���ֵ,��־��������ʶ�Ƿ������⴦����ˣ�0Ϊδ�������1Ϊ�Ѵ������
					zhibs+=i+","+Locale.Yunju_zhibb+",meikxxb_id,"+rsl.getString("meikxxb_id")+","+rsl.getString("zhi")+",0;";
					i++;
				}
			}
		}else if(Shiyfw==0){
//			�ݶ�Ϊ���ˡ��˾ࡱ������ָ����
			sql=
				"select f.lie_id,"+getZhibb_zhilb_transform(Zhibbm)+" from fahb f,zhilb z\n" +
				"       where f.zhilb_id = z.id\n" + 
				"             and f.lie_id in ("+SelId+")";

			rsl = con.getResultSetList(sql);
			
			if(rsl.getRows()>=1){
//				����������1��������������壬����ָ��Ӧ�ڼ�����������ʱ�õ�,
//					����һ����������⴦��ĺͲ���Ҫ���⴦����õ���һ�ݺ�ͬ��
//					����Ҫ������Ե�ָ���������һʱ��
				while(rsl.next()){
//					�ṹ��id,ָ�����,ָ���������,�������ݵ�ֵ,����ָ���ֵ,��־��������ʶ�Ƿ������⴦����ˣ�0Ϊδ�������1Ϊ�Ѵ������
					zhibs+=i+","+Zhibbm+",lie_id,"+rsl.getString(0)+","+rsl.getString(1)+",0;";
					i++;
				}
			}
		}else if(Shiyfw==1&&Zhibbm.equals(Locale.jiessl_zhibb)){
//			���������ͬ���ۿ������÷�ΧΪ���������֡���ָ��ʱʹ��,Ŀǰֻ���������2009-11-8��
//			�ַ����е�'C'���������֣��Ǹ����ͱ�ʶ
			zhibs+=i+","+Zhibbm+",'jiessl','jiessl','C',0;";
			i++;
		}
		
		if(rsl!=null){
			
			rsl.close();
		}
		con.Close();
		
		return zhibs;
	}
	
	public static String getZhibb_zhilb_transform(String zhibbm){
//		�������ƣ�ָ��_������λת��
//		���ܣ�
//			��ָ�����ָ�����ת��Ϊ�������е��ֶ����ơ�
//			��ָ����С�Qnetar����Ӧ�ľ����������еġ�qnet_ar��
//		������ָ����룬Ҫ�����lie_id
		
		String result=zhibbm;
		
		if(zhibbm.equals(Locale.Qnetar_zhibb)){
			
			result="qnet_ar";
		}
		
		return result;
	}
	
	public static boolean getJieszbtscl_Zkk(String jiagjsxs,String zkkjsxs,int shiyfw,String zhibbm){
		
//		�������ܣ�
//			���ĳһ��ָ������Ҫ���⴦��ģ���ô�ڼ������ۿ�ʱӦ���Ը�ָ��
//		�����߼���
//				����۸�������ú���Ǽ�Ȩƽ������ģ��ſ���renturn false 
//			�����ǵ����ν���û������ָ�괦������
//		�����βΣ�
//			Tsclzbs:��Ҫ���������ָ����ֵ��zhibbm��Ҫ�������ۿ��ָ�����
		
		boolean Flag=true;
		
		if(jiagjsxs.equals(Locale.jiaqpj_jiesxs)){
//			����۸�������ú���Ǽ�Ȩƽ������ģ������ۿ��еĽ�����ʽ������
//				�� ���÷�ΧΪ�������ֵ� �ſ���renturn false 
//				�����ǵ����ν���û������ָ�괦������
			if((zkkjsxs==null
					||zkkjsxs.equals(Locale.jiaqpj_jiesxs)
					||zkkjsxs.equals(""))
					&&shiyfw!=1){
				
				Flag=true;
			}else if(zkkjsxs.equals(Locale.danpc_jiesxs)
					||shiyfw==1){
				
				Flag=false;
			}
		}else if(jiagjsxs.equals(Locale.danpc_jiesxs)){
//			����۸�������ú���ǵ����μ���ģ������ۿ��еĽ�����ʽΪ��Ȩƽ��
//			�� ���÷�ΧΪ�������ֵ� �ſ���renturn false 
			
			if((zkkjsxs==null
					||zkkjsxs.equals(Locale.danpc_jiesxs)
					||zkkjsxs.equals(""))
					&&shiyfw!=1){
				
				Flag=true;
			}else if((zkkjsxs.equals(Locale.jiaqpj_jiesxs)&&zhibbm.equals(Locale.jiessl_zhibb))
					||shiyfw==1){
				
				Flag=false;
			}else if(zkkjsxs.equals(Locale.jiaqpj_jiesxs)){
				
				Flag=true;
			}
		}
		
		return Flag;
	}

	public static boolean getJieszbtscl_Zkk_dw(String zengfjdw, String koufjdw){
		
//		�������ܣ�
//			���ĳһ��ָ������Ҫ���⴦��ģ��������ۿλ�ǿ۶ֵ����
//		�����߼���
//				���������λ��۸���λΪ"%��"�ǣ��򷵻�false�����������ۿ��¼
//		�����βΣ�
//			Tsclzbs:��Ҫ���������ָ����ֵ��zhibbm��Ҫ�������ۿ��ָ�����
		
		boolean Flag=true;
		
		if(zengfjdw!=null&&zengfjdw.trim().equals("")){
			
			if(zengfjdw.equals(Locale.baifbd_danw)){
//				���������λ��۸���λΪ"%��"�ǣ��򷵻�false�����������ۿ��¼
				
				Flag = false;
			}
		}
		
		if(koufjdw!=null&&koufjdw.trim().equals("")){
			
			if(koufjdw.equals(Locale.baifbd_danw)){
//				���������λ��۸���λΪ"%��"�ǣ��򷵻�false�����������ۿ��¼
				
				Flag = false;
			}
		}
		
		return Flag;
	}
	
	public static void Mark_Tsclzbs_bz(String[] Tsclzbs,String Joint_primary_key){
		
//		�������ܣ�
//				��Ҫ���⴦���ָ�궼������Tsclzbs�����У������ָ���Ѿ����¼�������ۿ��ˣ�
//			����Tsclzbs����ı�־λ�ϴ��ϱ�ʶ1.
//		�����߼���
//			���Tsclzbs�����Joint_primary_key���ͽ�Tsclzbs����ı�ʶλ���ϱ�ʶ
//		�����βΣ�
//			Tsclzbs:��Ҫ���������ָ����ֵ��Joint_primary_key��Tsclzbs����������
		
		for(int i=0;i<Tsclzbs.length;i++){
			
			if(Tsclzbs[i].substring(0,Joint_primary_key.length()).equals(Joint_primary_key)){
				
				Tsclzbs[i] = Tsclzbs[i].substring(0,Tsclzbs[i].lastIndexOf(",")+1)+"1;";
			}
		}
	}
	
	public static void setJieszb_Tszbcl(Interpreter bsh,Balances_variable bsv,String zhibbm){
//		
//		�������ܣ�
//			�������⴦�����ָ�긳ֵ����ָ�����ʾģ�顣���������������
//				1������ָ�꼴�Ǽ�Ȩƽ���������ǵ����μ���ʱ����ʾģ��ֻ��ʾ��Ȩƽ�������ָ��
//				2������ָ���ǵ����μ���ʱ(��:�ڴ˷���������ǰ��Ȩƽ����ֵ����ֵΪ��)��
//					����ʾģ��ֻ��ʾ�����ν����ָ�꣨��ֻ��ʾ��ָ���һ�ε����ν������Ϣ��
//		�����߼���
//			���Tsclzbs�����Joint_primary_key���ͽ�Tsclzbs����ı�ʶλ���ϱ�ʶ
//		�����βΣ�
//			Tsclzbs:��Ҫ���������ָ����ֵ��Joint_primary_key��Tsclzbs����������
		try {
	
//			if(!bsh.get("��ͬ��׼_"+Locale.jiessl_zhibb).toString().equals("")
//					&&bsv.getShul_ht().equals("")){
				
//				�������ۿ�ȡֵ
				bsv.setShul_ht(Regular_Ht(bsh.get("��ͬ��׼_��������").toString()));
				bsv.setShul_yk(Double.parseDouble(bsh.get("ӯ��_��������").toString()));
//				bsv.setShul_zdj(Double.parseDouble(bsh.get("�۵���_��������").toString()));
//				bsv.setShul_zsl(Double.parseDouble(bsh.get("������_��������").toString()));
//				bsv.setShul_zsldw(bsh.get("��������λ_��������").toString());
//			}
			
//			if(!bsh.get("��ͬ��׼_"+Locale.Qnetar_zhibb).toString().equals("")
//					&&bsv.getQnetar_ht().equals("")){
				
//				Qnetar
				bsv.setQnetar_ht(Regular_Ht(bsh.get("��ͬ��׼_Qnetar").toString()));
				bsv.setQnetar_yk(Double.parseDouble(bsh.get("ӯ��_Qnetar").toString()));
//				bsv.setQnetar_zdj(Double.parseDouble(bsh.get("�۵���_Qnetar").toString()));
//				bsv.setQnetar_zsl(Double.parseDouble(bsh.get("������_Qnetar").toString()));
//				bsv.setQnetar_zsldw(bsh.get("��������λ_Qnetar").toString());
//			}
			
//			if(!bsh.get("��ͬ��׼_"+Locale.Std_zhibb).toString().equals("")
//					&&bsv.getStd_ht().equals("")){
				
//				Std
				bsv.setStd_ht(Regular_Ht(bsh.get("��ͬ��׼_Std").toString()));
				bsv.setStd_yk(Double.parseDouble(bsh.get("ӯ��_Std").toString()));
//				bsv.setStd_zdj(Double.parseDouble(bsh.get("�۵���_Std").toString()));
//				bsv.setStd_zsl(Double.parseDouble(bsh.get("������_Std").toString()));
//				bsv.setStd_zsldw(bsh.get("��������λ_Std").toString());
//			}
			
//			if(!bsh.get("��ͬ��׼_"+Locale.Ad_zhibb).toString().equals("")
//					&&bsv.getAd_ht().equals("")){
				
//				Ad
				bsv.setAd_ht(Regular_Ht(bsh.get("��ͬ��׼_Ad").toString()));
				bsv.setAd_yk(Double.parseDouble(bsh.get("ӯ��_Ad").toString()));
//				bsv.setAd_zdj(Double.parseDouble(bsh.get("�۵���_Ad").toString()));
//				bsv.setAd_zsl(Double.parseDouble(bsh.get("������_Ad").toString()));
//				bsv.setAd_zsldw(bsh.get("��������λ_Ad").toString());
//			}

//			if(!bsh.get("��ͬ��׼_"+Locale.Vdaf_zhibb).toString().equals("")
//					&&bsv.getVdaf_ht().equals("")){
				
//				Vdaf
				bsv.setVdaf_ht(Regular_Ht(bsh.get("��ͬ��׼_Vdaf").toString()));
				bsv.setVdaf_yk(Double.parseDouble(bsh.get("ӯ��_Vdaf").toString()));
//				bsv.setVdaf_zdj(Double.parseDouble(bsh.get("�۵���_Vdaf").toString()));
//				bsv.setVdaf_zsl(Double.parseDouble(bsh.get("������_Vdaf").toString()));
//				bsv.setVdaf_zsldw(bsh.get("��������λ_Vdaf").toString());
//			}

//			if(!bsh.get("��ͬ��׼_"+Locale.Mt_zhibb).toString().equals("")
//					&&bsv.getMt_ht().equals("")){
				
//				Mt
				bsv.setMt_ht(Regular_Ht(bsh.get("��ͬ��׼_Mt").toString()));
				bsv.setMt_yk(Double.parseDouble(bsh.get("ӯ��_Mt").toString()));
//				bsv.setMt_zdj(Double.parseDouble(bsh.get("�۵���_Mt").toString()));
//				bsv.setMt_zsl(Double.parseDouble(bsh.get("������_Mt").toString()));
//				bsv.setMt_zsldw(bsh.get("��������λ_Mt").toString());
//			}

//			if(!bsh.get("��ͬ��׼_"+Locale.Qgrad_zhibb).toString().equals("")
//					&&bsv.getQgrad_ht().equals("")){
				
//				Qgrad
				bsv.setQgrad_ht(Regular_Ht(bsh.get("��ͬ��׼_Qgrad").toString()));
				bsv.setQgrad_yk(Double.parseDouble(bsh.get("ӯ��_Qgrad").toString()));
//				bsv.setQgrad_zdj(Double.parseDouble(bsh.get("�۵���_Qgrad").toString()));
//				bsv.setQgrad_zsl(Double.parseDouble(bsh.get("������_Qgrad").toString()));
//				bsv.setQgrad_zsldw(bsh.get("��������λ_Qgrad").toString());
//			}

//			if(!bsh.get("��ͬ��׼_"+Locale.Qbad_zhibb).toString().equals("")
//					&&bsv.getQbad_ht().equals("")){
				
//				Qbad
				bsv.setQbad_ht(Regular_Ht(bsh.get("��ͬ��׼_Qbad").toString()));
				bsv.setQbad_yk(Double.parseDouble(bsh.get("ӯ��_Qbad").toString()));
//				bsv.setQbad_zdj(Double.parseDouble(bsh.get("�۵���_Qbad").toString()));
//				bsv.setQbad_zsl(Double.parseDouble(bsh.get("������_Qbad").toString()));
//				bsv.setQbad_zsldw(bsh.get("��������λ_Qbad").toString());
//			}

//			if(!bsh.get("��ͬ��׼_"+Locale.Had_zhibb).toString().equals("")
//					&&bsv.getHad_ht().equals("")){
				
//				Had
				bsv.setHad_ht(Regular_Ht(bsh.get("��ͬ��׼_Had").toString()));
				bsv.setHad_yk(Double.parseDouble(bsh.get("ӯ��_Had").toString()));
//				bsv.setHad_zdj(Double.parseDouble(bsh.get("�۵���_Had").toString()));
//				bsv.setHad_zsl(Double.parseDouble(bsh.get("������_Had").toString()));
//				bsv.setHad_zsldw(bsh.get("��������λ_Had").toString());
//			}

//			if(!bsh.get("��ͬ��׼_"+Locale.Stad_zhibb).toString().equals("")
//					&&bsv.getStad_ht().equals("")){
				
//				Stad
				bsv.setStad_ht(Regular_Ht(bsh.get("��ͬ��׼_Stad").toString()));
				bsv.setStad_yk(Double.parseDouble(bsh.get("ӯ��_Stad").toString()));
//				bsv.setStad_zdj(Double.parseDouble(bsh.get("�۵���_Stad").toString()));
//				bsv.setStad_zsl(Double.parseDouble(bsh.get("������_Stad").toString()));
//				bsv.setStad_zsldw(bsh.get("��������λ_Stad").toString());
//			}

//			if(!bsh.get("��ͬ��׼_"+Locale.Star_zhibb).toString().equals("")
//					&&bsv.getStar_ht().equals("")){
				
//				Star
				bsv.setStar_ht(Regular_Ht(bsh.get("��ͬ��׼_Star").toString()));
				bsv.setStar_yk(Double.parseDouble(bsh.get("ӯ��_Star").toString()));
//				bsv.setStar_zdj(Double.parseDouble(bsh.get("�۵���_Star").toString()));
//				bsv.setStar_zsl(Double.parseDouble(bsh.get("������_Star").toString()));
//				bsv.setStar_zsldw(bsh.get("��������λ_Star").toString());
//			}

//			if(!bsh.get("��ͬ��׼_"+Locale.Mad_zhibb).toString().equals("")
//					&&bsv.getMad_ht().equals("")){
				
//				Mad
				bsv.setMad_ht(Regular_Ht(bsh.get("��ͬ��׼_Mad").toString()));
				bsv.setMad_yk(Double.parseDouble(bsh.get("ӯ��_Mad").toString()));
//				bsv.setMad_zdj(Double.parseDouble(bsh.get("�۵���_Mad").toString()));
//				bsv.setMad_zsl(Double.parseDouble(bsh.get("������_Mad").toString()));
//				bsv.setMad_zsldw(bsh.get("��������λ_Mad").toString());
//			}

//			if(!bsh.get("��ͬ��׼_"+Locale.Aar_zhibb).toString().equals("")
//					&&bsv.getAar_ht().equals("")){
				
//				Aar
				bsv.setAar_ht(Regular_Ht(bsh.get("��ͬ��׼_Aar").toString()));
				bsv.setAar_yk(Double.parseDouble(bsh.get("ӯ��_Aar").toString()));
//				bsv.setAar_zdj(Double.parseDouble(bsh.get("�۵���_Aar").toString()));
//				bsv.setAar_zsl(Double.parseDouble(bsh.get("������_Aar").toString()));
//				bsv.setAar_zsldw(bsh.get("��������λ_Aar").toString());
//			}

//			if(!bsh.get("��ͬ��׼_"+Locale.Aad_zhibb).toString().equals("")
//					&&bsv.getAad_ht().equals("")){
				
//				Aad
				bsv.setAad_ht(Regular_Ht(bsh.get("��ͬ��׼_Aad").toString()));
				bsv.setAad_yk(Double.parseDouble(bsh.get("ӯ��_Aad").toString()));
//				bsv.setAad_zdj(Double.parseDouble(bsh.get("�۵���_Aad").toString()));
//				bsv.setAad_zsl(Double.parseDouble(bsh.get("������_Aad").toString()));
//				bsv.setAad_zsldw(bsh.get("��������λ_Aad").toString());
				
//			}

//			if(!bsh.get("��ͬ��׼_"+Locale.Vad_zhibb).toString().equals("")
//					&&bsv.getVad_ht().equals("")){
				
//				Vad
				bsv.setVad_ht(Regular_Ht(bsh.get("��ͬ��׼_Vad").toString()));
				bsv.setVad_yk(Double.parseDouble(bsh.get("ӯ��_Vad").toString()));
//				bsv.setVad_zdj(Double.parseDouble(bsh.get("�۵���_Vad").toString()));
//				bsv.setVad_zsl(Double.parseDouble(bsh.get("������_Vad").toString()));
//				bsv.setVad_zsldw(bsh.get("��������λ_Vad").toString());
//			}

//			if(!bsh.get("��ͬ��׼_"+Locale.T2_zhibb).toString().equals("")
//					&&bsv.getT2_ht().equals("")){
				
//				T2
				bsv.setT2_ht(Regular_Ht(bsh.get("��ͬ��׼_T2").toString()));
				bsv.setT2_yk(Double.parseDouble(bsh.get("ӯ��_T2").toString()));
//				bsv.setT2_zdj(Double.parseDouble(bsh.get("�۵���_T2").toString()));
//				bsv.setT2_zsl(Double.parseDouble(bsh.get("������_T2").toString()));
//				bsv.setT2_zsldw(bsh.get("��������λ_T2").toString());
//			}

//			if(!bsh.get("��ͬ��׼_"+Locale.Yunju_zhibb).toString().equals("")
//					&&bsv.getYunju_ht().equals("")){
				
//				�˾�
				bsv.setYunju_ht(Regular_Ht(bsh.get("��ͬ��׼_�˾�").toString()));
				bsv.setYunju_yk(Double.parseDouble(bsh.get("ӯ��_�˾�").toString()));
//				bsv.setYunju_zdj(Double.parseDouble(bsh.get("�۵���_�˾�").toString()));
//				bsv.setYunju_zsl(Double.parseDouble(bsh.get("������_�˾�").toString()));
//				bsv.setYunju_zsldw(bsh.get("��������λ_�˾�").toString());
//			}
			
		} catch (EvalError e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
	}
	
	public static String getShezzh(String Shezvalue){
//		�������ܣ�(����ת��)
//			�ڽ���ϵͳ���õ�ģ�����������ȡ�����õ���������ͨ����������ɵģ�
//			�磺���������������ӡ���Ӻ�ͳһ�������롢������ȡ��������
//			���ں�̨�Ĵ����ж�Ҫת���ɷ��Ų��У���ͨ���˺�������ת����Ŀǰֻ���ϵͳ��Ϣ�е����ã�
//		�����߼���
//			ͨ��ȫ��ƥ��ת����ֵ
//		�����βΣ�
//			Shezvalue:��Ҫת�������õ�ֵ
		if(Shezvalue.equals(Locale.anlsswrhxj_jiesghlqzfs_xitxx)){
//			������ȡ����ʽ:����������������
			Shezvalue="sum(round_new())";
		}else if(Shezvalue.equals(Locale.xiangjhtysswr_jiesghlqzfs_xitxx)){
//			������ȡ����ʽ:��Ӻ�ͳһ��������
			Shezvalue="round_new(sum())";
		}else if(Shezvalue.equals(Locale.bujxqzcz_jiesghlqzfs_xitxx)){
//			������ȡ����ʽ:������ȡ������
			Shezvalue="sum()";
		}else{
//			������ȡ����ʽ:�����û�õ�Ĭ��Ϊ�����������������ӡ�
			Shezvalue="sum(round_new())";
		}
		
		return Shezvalue;
	}
	
	public static StringBuffer getJiesszl_Sql(Balances_variable bsv,Visit visit, String Jieszbsftz,String SelIds,long Diancxxb_id,
			long Gongysb_id,long Hetb_id,double Jieskdl,long Yunsdwb_id,long Jieslx,double Shangcjsl,
			String Tsclzb_where){
		
//		˵����Tsclzb_where �β����ڴ���ĳָ�굥���ν��㡢���ۿ�ֻ�Գ������������á��˷ѵ����ν�����ʹ�ã�
//			��Ϊ��ȷ����������������Ӧ�Ľ������������������ӵ�
		JDBCcon con = new JDBCcon();
		
		String jies_Jqsl="jingz";								//�����Ȩ����
		String jies_Qnetarblxs=String.valueOf(visit.getFarldec());
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
		String jies_Qbadblxs=String.valueOf(visit.getFarldec());;
		String jies_Qgradblxs=String.valueOf(visit.getFarldec());;
		String jies_T2blxs="2";
		String jies_shifykfzljs="��";
		String jies_Jieslqzfs="sum(round_new())";				//��������ȡ����ʽ
		String jies_Jsslblxs="0";								//������������С��λ
		String jies_Kdkskzqzfs="round_new(sum())";				//�۶֡���ˮ������ȡ����ʽ
		String jies_Jssl="biaoz+yingk-koud-kous-kouz";			//��������
		String jies_yunfjssl="jingz";							//�˷ѽ�������
		boolean blnDandszyfjssl=false;	//�Ƿ񵥶��������˷ѽ����������������yunfjssl=���������������������Ʊ����yunfjssl=gongfsl
		String jiscdkd="��";										//�Ƿ���㳬�֡��۶�
		String ChaodOrKuid="";									//���㳬�ֻ��ǿ���
		
		String jies_Guohlqzfs=Locale.anlsswrhxj_jiesghlqzfs_xitxx;	//ϵͳ��Ϣ�й�����ȡ����ʽ��Ĭ��Ϊ������������������
		String jies_Guohlblxsw="0";									//ϵͳ��Ϣ�й���������С��λ��Ĭ��Ϊ��2
		String yunsdw="";	//���䵥λ������
		String ranlpz_where="";	//ȼ��Ʒ�ֵ�����
		if(Yunsdwb_id>-1){
//			ѡ�������䵥λ
			
			if(Jieslx==Locale.daozyf_feiylbb_id){
//				��װ�˷ѵ�ʱ��õ����������䵥λ
				yunsdw=" and cp.yunsdw='"+bsv.getYunsdw()+"'";
			}else{
				
				yunsdw=" and cp.yunsdwb_id="+Yunsdwb_id;
			}
			
		}
		
		if(visit.getString19()!=null&&!visit.getString19().equals("") && !visit.getString19().equals("0")){
//			ѡ����ȼ��Ʒ��
			ranlpz_where = "and f.pinzb_id = "+visit.getString19();
		}
		
		String sqll ="select * from xitxxb where mingc='���������㵥��ʽ' and zhuangt=1";
		 ResultSetList rb = con.getResultSetList(sqll);
		 boolean handjsd = false;
		 if(rb.next()){
			 handjsd = true;
		 }
		 
		 String sqll2 ="select * from xitxxb where mingc='�����ͬר��' and zhuangt=1";
		 ResultSetList rb2 = con.getResultSetList(sqll2);
		 boolean datjsd = false;
		 if(rb2.next()){
			 datjsd = true;
		 }
		 rb2.close();
		 //���������糧ʱ��������������Ȩ������ɷ�ʽ������
		 boolean xuanwjsd = MainGlobal.getXitxx_item("����", "���������������Ƿ����ˮ�ֿ��˵���", String.valueOf(visit.getDiancxxb_id()), "��").equals("��");
		 if(xuanwjsd){
			 jies_Jqsl = "jingz - SHUIFTZL ";
			 jies_Jssl = "jingz - SHUIFTZL ";
		 }
		 
		 
		 
//		jies_Jqsl=MainGlobal.getXitxx_item("����", Locale.jiaqsl_xitxx, 
//    			String.valueOf(Diancxxb_id),jies_Jqsl);
		
//		��ϵͳ��Ϣ����ȡ�������õ���Ϣ
//		��Yansmxreport.java�������Ƶķ�������һͬ�޸�
		String XitxxArrar[][]=null;	
		XitxxArrar=MainGlobal.getXitxx_items("����",	"select mingc from xitxxb where leib='����'"
				,String.valueOf(Diancxxb_id));
		
//		����ȡ�õ�ֵ��Ȼ��Ա������и�ֵ
		if(XitxxArrar!=null){
			
			for(int i=0;i<XitxxArrar.length;i++){
				
				if(XitxxArrar[i][0]!=null){
					
					if(XitxxArrar[i][0].trim().equals(Locale.jiaqsl_xitxx)){
//						��Ȩ����
						jies_Jqsl=XitxxArrar[i][1].trim();
					}else if(XitxxArrar[i][0].trim().equals(Locale.jiesghlqzfs_xitxx)){
//						������ȡ����ʽ
						jies_Guohlqzfs=getShezzh(XitxxArrar[i][1].trim());
					}else if(XitxxArrar[i][0].trim().equals(Locale.jiesghlblxsw_xitxx)){
//						�������������С��λ
						jies_Guohlblxsw=XitxxArrar[i][1].trim();
					}else if(XitxxArrar[i][0].trim().equals(Locale.meiksl_xitxx)){
//						ú��˰��
						bsv.setMeiksl(Double.parseDouble(XitxxArrar[i][1].trim()));
					}else if(XitxxArrar[i][0].trim().equals(Locale.yunfsl_xitxx)){
//						�˷�˰��
						bsv.setYunfsl(Double.parseDouble(XitxxArrar[i][1].trim()));
					}else if(XitxxArrar[i][0].trim().equals(Locale.jiscdkd_xitxx)){
//						���㳬�ֿ۶�
						jiscdkd=XitxxArrar[i][1].trim();
					}else if(XitxxArrar[i][0].trim().equals(Locale.Meikzkkblxsw_xitxx)){
//						ú�����ۿ��С��λ
						bsv.setMeikzkkblxsw(Integer.parseInt(XitxxArrar[i][1].trim()));
					}else if(XitxxArrar[i][0].trim().equals(Locale.jiesslqzfs_jies)){
//						��������ȡ����ʽ
						jies_Jieslqzfs = getShezzh(XitxxArrar[i][1].trim());
					}else if(XitxxArrar[i][0].trim().equals(Locale.jiesslblxsw_jies)){
//						������������С��λ
						jies_Jsslblxs = XitxxArrar[i][1].trim();
						bsv.setJiesslblxs(XitxxArrar[i][1].trim());
					}else if(XitxxArrar[i][0].trim().equals(Locale.jiesslzcfs_jies)){
//						����������ɷ�ʽ
						jies_Jssl = XitxxArrar[i][1].trim();
					}else if(XitxxArrar[i][0].trim().equals(Locale.kuidjfyf_jies)){
//						���־ܸ��˷�
						bsv.setKuidjfyf(XitxxArrar[i][1].trim());
					}else if(XitxxArrar[i][0].trim().equals(Locale.Qnetarblxsw_jies)){
						
						jies_Qnetarblxs = XitxxArrar[i][1].trim();
					}else if(XitxxArrar[i][0].trim().equals(Locale.liangpjsyfbjxkd_jies)){
//						��Ʊ�����˷Ѳ����п۶�
						bsv.setLiangpjsyfbjxkd(XitxxArrar[i][1].trim());
					}else if(XitxxArrar[i][0].equals(Locale.yunfjsdpcfztj_jies)){
//						�˷ѽ��㵥���η�������
						bsv.setYunfjsdpcfztj(XitxxArrar[i][1].trim());
					}else if(XitxxArrar[i][0].trim().equals(Locale.yunfjsslzcfs_jies)){
//						�˷ѽ���������ɷ�ʽ
						jies_yunfjssl=XitxxArrar[i][1].trim();
						blnDandszyfjssl=true;
					}else if(XitxxArrar[i][0].trim().equals(Locale.duojgdpcjqhyzqzzcfs_jies)){
//						��۸����μ�Ȩ����ֵ��Ȩֵ��ɷ�ʽ
						jies_Jqsl=XitxxArrar[i][1].trim();
					}else if(XitxxArrar[i][0].trim().equals(Locale.jieskoudcxjsdj_jies)){
//						����۶����¼��㵥��
						if("��".equals(XitxxArrar[i][1].trim())){
							bsv.setKoudcxjsdj(false);
						}
					}
					
					
//					else if(XitxxArrar[i][0].trim().equals(Locale.yunjdpcjs)){
////						�˾൥���δ�����Ҫ��������
//						if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)
//								&&XitxxArrar[i][1].trim().equals("��")
//								&&bsv.getTsclzbs()==null						
//								){
////							ǰ���ǽ�����ʽ����Ϊ����Ȩƽ����,ϵͳ��Ϣ�����õ�ֵΪ���ǡ�
////							��Ϊǰ���ǡ���Ȩƽ�������㣬��SelIdsΪȫ������lie_id
////							����ָ������ֻ�ܸ�ֵһ��
//							bsv.setJieszbtscl_Items(bsv.getJieszbtscl_Items()+Jiesdcz.getJieszbtscl(Locale.Yunju_zhibb, SelIds));
//						}
//					}
				}
			}
		}
		
		if(jiscdkd.equals("��")){
//			����ϵͳ��Ϣ�����趨�˳��ֻ���ֵļ���
			if(bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)){
//				�����ۼ��㡰���֡�
				ChaodOrKuid="CD";
			}else if(bsv.getJiesfs().equals(Locale.chukjg_ht_jsfs)){
//				����ۼ��㡰���֡�
				ChaodOrKuid="KD";
			}
		}
//		��¼����Or����
		bsv.setChaodOrKuid(ChaodOrKuid);
//		��Yansmxreport.java �����Ƶķ�������һͬ�޸ġ�
		if(Jiesdcz.getJiessz_items(Diancxxb_id,Gongysb_id,Hetb_id)!=null){

        	String JiesszArray[][]=null;
		
        	JiesszArray=Jiesdcz.getJiessz_items(Diancxxb_id,Gongysb_id,Hetb_id);
	
			for(int i=0;i<JiesszArray.length;i++){
				
				if(JiesszArray[i][0]!=null){
					
					if(JiesszArray[i][0].trim().equals(Locale.jiesjqsl_jies)){
						
						jies_Jqsl=JiesszArray[i][1].trim();
					}else if(JiesszArray[i][0].trim().equals(Locale.mtblxsw_jies)){
						
						jies_Mtblxs=JiesszArray[i][1].trim();
					}else if(JiesszArray[i][0].trim().equals(Locale.madblxsw_jies)){
						
						jies_Madblxs=JiesszArray[i][1].trim();
					}else if(JiesszArray[i][0].trim().equals(Locale.aarblxsw_jies)){
						
						jies_Aarblxs=JiesszArray[i][1].trim();
					}else if(JiesszArray[i][0].trim().equals(Locale.aadblxsw_jies)){
						
						jies_Aadblxs=JiesszArray[i][1].trim();
					}else if(JiesszArray[i][0].trim().equals(Locale.adblxsw_jies)){
						
						jies_Adblxs=JiesszArray[i][1].trim();
					}else if(JiesszArray[i][0].trim().equals(Locale.vadblxsw_jies)){
						
						jies_Vadblxs=JiesszArray[i][1].trim();
					}else if(JiesszArray[i][0].trim().equals(Locale.vdafblxsw_jies)){
						
						jies_Vdafblxs=JiesszArray[i][1].trim();
					}else if(JiesszArray[i][0].trim().equals(Locale.stadblxsw_jies)){
						
						jies_Stadblxs=JiesszArray[i][1].trim();
					}else if(JiesszArray[i][0].trim().equals(Locale.starblxsw_jies)){
					
						jies_Starblxs=JiesszArray[i][1].trim();
					}else if(JiesszArray[i][0].trim().equals(Locale.stdblxsw_jies)){
						
						jies_Stdblxs=JiesszArray[i][1].trim();
					}else if(JiesszArray[i][0].trim().equals(Locale.hadblxsw_jies)){
						
						jies_Hadblxs=JiesszArray[i][1].trim();
					}else if(JiesszArray[i][0].trim().equals(Locale.Qnetarblxsw_jies)){
						
						jies_Qnetarblxs=JiesszArray[i][1].trim();
					}else if(JiesszArray[i][0].trim().equals(Locale.Qbadblxsw_jies)){
						
						jies_Qbadblxs=JiesszArray[i][1].trim();
					}else if(JiesszArray[i][0].trim().equals(Locale.Qgradblxsw_jies)){
						
						jies_Qgradblxs=JiesszArray[i][1].trim();
					}else if(JiesszArray[i][0].trim().equals(Locale.shifykfzljs_jies)){
						
//						jies_shifykfzljs=JiesszArray[i][1];
						bsv.setShifykfzljs(JiesszArray[i][1].trim());
					}else if(JiesszArray[i][0].trim().equals(Locale.meiksl_jies)){
						
						bsv.setMeiksl(Double.parseDouble(JiesszArray[i][1].trim()));
					}else if(JiesszArray[i][0].trim().equals(Locale.yunfsl_jies)){
						
						bsv.setYunfsl(Double.parseDouble(JiesszArray[i][1].trim()));
					}else if(JiesszArray[i][0].trim().equals(Locale.jiesslqzfs_jies)){
						
						jies_Jieslqzfs=JiesszArray[i][1].trim();
					}else if(JiesszArray[i][0].trim().equals(Locale.jiesslblxsw_jies)){
						
						jies_Jsslblxs=JiesszArray[i][1].trim();
						bsv.setJiesslblxs(jies_Jsslblxs);
					}else if(JiesszArray[i][0].trim().equals(Locale.jiesslzcfs_jies)){
						
						jies_Jssl=JiesszArray[i][1].trim();
					}else if(JiesszArray[i][0].trim().equals(Locale.yunfjsslzcfs_jies)){
						
						jies_yunfjssl=JiesszArray[i][1].trim();
						blnDandszyfjssl=true;
					}else if(JiesszArray[i][0].trim().equals(Locale.user_custom_mlj_jiesgs)){
						
						bsv.setUser_custom_mlj_jiesgs(JiesszArray[i][1].trim());
					}else if(JiesszArray[i][0].trim().equals(Locale.user_custom_fmlj_jiesgs)){
						
						bsv.setUser_custom_fmlj_jiesgs(JiesszArray[i][1].trim());
					}else if(JiesszArray[i][0].trim().equals(Locale.yikj_yunfyymk_jies)){
						
						bsv.setYikj_yunfyymk(JiesszArray[i][1].trim());
					}else if(JiesszArray[i][0].trim().equals(Locale.yikj_meikyyyf_jies)){
						
						bsv.setYikj_meikyyyf(JiesszArray[i][1].trim());
					}else if(JiesszArray[i][0].trim().equals(Locale.meikhsdjblxsw_jies)){
						
//						ú�˰���۱���С��λ
						bsv.setMeikhsdjblxsw(Integer.parseInt(JiesszArray[i][1].trim()));
					}else if(JiesszArray[i][0].trim().equals(Locale.yunfhsdjblxsw_jies)){
						
//						�˷Ѻ�˰���۱���С��λ
						bsv.setYunfhsdjblxsw(Integer.parseInt(JiesszArray[i][1].trim()));
					}else if(JiesszArray[i][0].trim().equals(Locale.kuidjfyf_jies)){
						
//						���־ܸ��˷�
						bsv.setKuidjfyf(JiesszArray[i][1].trim());
					}else if(JiesszArray[i][0].trim().equals(Locale.Mj_to_kcal_xsclfs_jies)){
						
//						�׽�ת��
						bsv.setMj_to_kcal_xsclfs(JiesszArray[i][1].trim());
					}else if(JiesszArray[i][0].trim().equals(Locale.meikhsdjqzfs_jies)){
						
//						��˰����ȡ����ʽ
						bsv.setMeikhsdj_qzfs(JiesszArray[i][1].trim());
					}else if(JiesszArray[i][0].trim().equals(Locale.yunfjsdpcfztj_jies)){
//						�˷ѽ��㵥���η�������
						bsv.setYunfjsdpcfztj(JiesszArray[i][1].trim());
					}else if(JiesszArray[i][0].trim().equals(Locale.liangpjsyfbjxkd_jies)){
//						��Ʊ�����˷Ѳ����п۶�
						bsv.setLiangpjsyfbjxkd(JiesszArray[i][1].trim());
					}else if(JiesszArray[i][0].trim().equals(Locale.shifyrzcjs_jies)) {
//						�Ƿ�����ֵ�����
						bsv.setShifyrzcjs(JiesszArray[i][1].trim());
					}else if(JiesszArray[i][0].trim().equals(Locale.kuikjs_jies)) {
//						��������
						bsv.setKuikjs(Double.parseDouble(JiesszArray[i][1].trim()));
					}else if(JiesszArray[i][0].trim().equals(Locale.danglgs_jies)) {
//						������ʽ
						bsv.setDanglgs(JiesszArray[i][1].trim());
					}else if(JiesszArray[i][0].trim().equals(Locale.danglblxsw_jies)) {
//						��������С��λ
						bsv.setDanglblxsw(JiesszArray[i][1].trim());
					}else if(JiesszArray[i][0].trim().equals(Locale.meiksfhyf_jies)) {
//						ú���Ƿ��˷�
						if("��".equals(JiesszArray[i][1].trim())){
							bsv.setMeiksfhyf(true);
						}
					}
//					else if(JiesszArray[i][0].trim().equals(Locale.yunjdpcjs)){
////						�˾൥���δ�����Ҫ��������
//						
//						if(bsv.getTsclzbs()==null){
////							����ָ������ֻ�ܸ�ֵһ��
//							if(bsv.getJiesxs().equals(Locale.jiaqpj_jiesxs)
//									&&JiesszArray[i][1].trim().equals("��")){
////								ǰ���ǽ�����ʽ����Ϊ����Ȩƽ����,�������õ�ֵΪ���ǡ�
////								��Ϊǰ���ǡ���Ȩƽ�������㣬��SelIdsΪȫ������lie_id
//								
//								if(bsv.getJieszbtscl_Items().indexOf(Locale.Yunju_zhibb)>-1){
////									˵����ָ���Ѿ���ϵͳ��Ϣ������ȡ���ˣ����ﲻ�ٵ���������
//									
//								}else{
////									˵����ָ��û��ϵͳ��Ϣ������ȡ�������ﵥ��������
//									bsv.setJieszbtscl_Items(bsv.getJieszbtscl_Items()+Jiesdcz.getJieszbtscl(Locale.Yunju_zhibb, SelIds));
//								}
//							}else{
//								
//								if(bsv.getJieszbtscl_Items().indexOf(Locale.Yunju_zhibb)>-1){
////									˵����ָ���Ѿ���ϵͳ��Ϣ������ȡ���ˣ������������óɷ�˵�������Ӧ�����⣬Ҫȥ��������⴦���ָ��
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
////			˵����Ҫ���⴦���ָ��
//			String ArrayTsclzbs[]=null;
//			ArrayTsclzbs=bsv.getJieszbtscl_Items().split(";");
//			bsv.setTsclzbs(ArrayTsclzbs);
////			0,�˾�,meikxxb_id,100,10,0;
////			1,�˾�,meikxxb_id,101,15,0;
////			2,�˾�,meikxxb_id,102,20,0;
////			3,Std,meikxxb_id,100,1.0,0;
//		}
		
		try {
			
			bsv.setMeikxxb_Id(Long.parseLong(MainGlobal.getTableCol("fahb", "Meikxxb_id", "lie_id in ("+SelIds+")")));
			bsv.setFaz_Id(Long.parseLong(MainGlobal.getTableCol("fahb", "Faz_id", "lie_id in ("+SelIds+")")));
		} catch (NumberFormatException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		
//		��������������������������ú�û�¡�������������ǹ�����������Ʊ��������� ��danjcpb,yunfdjb���з�����������yunfjsb_id=0 or null
		String contion_table="";//
		String contion_where="";
		long Yunf_Jieslx=Jieslx;	//Ϊ�˴�����Ʊ����Ϊ�˵õ��˷ѵ��ݱ��еķ��������ǡ������������ݣ��ڴ�Ҫ��������ת��
		String strJieslx=String.valueOf(Jieslx);  	//����ׯ�ӵ糧�С������˷ѡ����ֽ������ͣ�������Ʊ����ʱ���˷ѿ����ǡ������˷ѡ����ǡ������˷ѡ���
        											//�������߶��У���ô�ڴ˽���Ʊ�����е��˷�ת���ɡ������˷ѡ��͡������˷ѡ���
													//Ŀǰ��Ʊ����ʱ���˷�����ʱ��ô����
		
//		���ɽ�����ǵ糧����������Aϵͳ��Bϵͳ����ú�����ֿ������˷ѽ���
        String zhi = "��";
        String guohxt = "";
		if (Jieslx == Locale.guotyf_feiylbb_id || Jieslx == Locale.dityf_feiylbb_id) {
			zhi = MainGlobal.getXitxx_item("����", "�Ƿ���ʾ����ϵͳ������", String.valueOf(visit.getDiancxxb_id()), "��");
			if (zhi.equals("��")) {
				guohxt = "and cp.zhongchh = '"+ visit.getString19() +"'\n";
			}
		}
		
		if(Jieslx==Locale.guotyf_feiylbb_id||Jieslx==Locale.liangpjs_feiylbb_id||Jieslx==Locale.dityf_feiylbb_id||Jieslx==Locale.haiyyf_feiylbb_id){
//			�˷ѽ������Ʊ����
			
			if(Jieslx==Locale.liangpjs_feiylbb_id){
				
				Yunf_Jieslx=Locale.guotyf_feiylbb_id;
				strJieslx=Locale.guotyf_feiylbb_id +","+ Locale.haiyyf_feiylbb_id;
			}
			
			contion_table=",(select cp.id as chepb_id,yfzl.jifzl from chepb cp,yunfjszlb yfzl \n"+	
                "       		where cp.id=yfzl.chepb_id \n" +	guohxt + 													
                "       			and yfzl.feiylbb_id in ("+strJieslx+")) yfzl,\n" +
                "		(select distinct cp.id,dj.yunfjsb_id\n" +
                "                  from chepb cp,yunfdjb yd,danjcpb dj,fahb f,zhilb z,yansbhb ys,\n" +
                "						gongysb g,meikxxb m\n" + 
                "                  where yd.id=dj.yunfdjb_id\n" + 
                "                        and dj.chepb_id=cp.id\n" + 
                "                        and f.id=cp.fahb_id\n" + 
                "						 and f.gongysb_id=g.id\n" +
                "						 and f.meikxxb_id=m.id\n" +
                "						 AND f.zhilb_id = z.id\n" +
                "						 AND f.yansbhb_id = ys.id(+)\n" +
                "                        and yd.feiylbb_id in ("+strJieslx+")\n" + guohxt +
                "						 and f.lie_id in ("+SelIds+") \n" + 
                "     ) djcp";
			
			contion_where=
				"       and cp.id=djcp.id(+)\n" + 
				"     --  and (djcp.yunfjsb_id is null or djcp.yunfjsb_id=0) \n" +
				"		and cp.id=yfzl.chepb_id(+) ";
		}
		
		StringBuffer sql=new StringBuffer("");
//		������װ�˷Ѵ����ܣ�������װ�˷ѡ�������������������������ԭ������������������ڣ�
//		1����Ƥ��ͬ��ԭ���õ��ǡ�chepb������װ�˷��õ��ǡ�daozcpb��
//		2���ж��Ƿ���������� daozcpb��jiesb_id�Ƿ�Ϊ0
		
		
		String chehh=",cp.zhongchh,cp.qingchh";
		if(xuanwjsd){
			//������������㵥���򲻸��ݳ���ŷ���
			chehh="";
		}
		
		
		if(Jieszbsftz.equals("��")){
//			������ָ�����
			if(Jieslx==Locale.daozyf_feiylbb_id){
//				��װ�˷�
				sql.append(" select nvl(Qnetar_cf,0) as Qnetar_cf,nvl(Qnetar_kf,0) as Qnetar_kf,nvl(Std_cf,0) as Std_cf,nvl(Std_kf,0) as Std_kf,nvl(Mt_cf,0) as Mt_cf,nvl(Mt_kf,0) as Mt_kf,nvl(Mad_cf,0) as Mad_cf,nvl(Mad_kf,0) as Mad_kf,nvl(Aar_cf,0) as Aar_cf,nvl(Aar_kf,0) as Aar_kf,nvl(Aad_cf,0) as Aad_cf,nvl(Aad_kf,0) as Aad_kf,nvl(Ad_cf,0) as Ad_cf,				\n");
				sql.append(" 		nvl(Ad_kf,0) as Ad_kf,nvl(Vad_cf,0) as Vad_cf,nvl(Vad_kf,0) as Vad_kf,nvl(Vdaf_cf,0) as Vdaf_cf,nvl(Vdaf_kf,0) as Vdaf_kf,nvl(Stad_cf,0) as Stad_cf,nvl(Stad_kf,0) as Stad_kf,nvl(star_cf,0) as star_cf,nvl(star_kf,0) as star_kf,nvl(Had_cf,0) as Had_cf,nvl(Had_kf,0) as Had_kf,nvl(Qbad_cf,0) as Qbad_cf,nvl(Qbad_kf,0) as Qbad_kf,		\n");
				sql.append("		nvl(Qgrad_cf,0) as Qgrad_cf,nvl(Qgrad_kf,0) as Qgrad_kf,nvl(T2_cf,0) as T2_cf,nvl(T2_kf,0) as T2_kf,	\n");
				if (!bsv.getDanglgs().equals("")) { // �жϽ������÷������Ƿ����á������������������㵱��
					sql.append("round_new((").append(bsv.getDanglgs()).append("), ").append(bsv.getDanglblxsw()).append(") as dangl,");
				}
				 if(handjsd){//�����糧Ҫ��ʵ����Ϊ����
					 sql.append(" 		yuns,jingz,koud,kous,kouz,ches,biaoz,yingk,jingz2 as yanssl,jiessl,(jiessl-jingz) as jieslcy,yunfjssl,chaokdl,yingd,yingd-yingk as kuid from (select "+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "yuns", jies_Jsslblxs)+" as yuns,"+Jiesdcz.getJiesszzh(jies_Guohlqzfs,"jingz",jies_Guohlblxsw)+" as jingz,"+Jiesdcz.getJiesszzh(jies_Jieslqzfs,"jingz",jies_Jsslblxs)+" as jingz2,					\n");
				 }else{
					 sql.append(" 		yuns,jingz,koud,kous,kouz,ches,biaoz,yingk,(jingz2+yuns) as yanssl,jiessl,(jiessl-jingz) as jieslcy,yunfjssl,chaokdl,yingd,yingd-yingk as kuid from (select "+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "yuns", jies_Jsslblxs)+" as yuns,"+Jiesdcz.getJiesszzh(jies_Guohlqzfs,"jingz",jies_Guohlblxsw)+" as jingz,"+Jiesdcz.getJiesszzh(jies_Jieslqzfs,"jingz",jies_Jsslblxs)+" as jingz2,					\n");
				 }
				sql.append(			Jiesdcz.getJiesszzh(jies_Kdkskzqzfs, "koud", jies_Jsslblxs)+" as koud,"+Jiesdcz.getJiesszzh(jies_Kdkskzqzfs, "kous", jies_Jsslblxs)+" as kous,"+Jiesdcz.getJiesszzh(jies_Kdkskzqzfs, "kouz", jies_Jsslblxs)+" as kouz,	\n");
				sql.append(" 		sum(ches) as ches, "+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "biaoz", jies_Jsslblxs)+" as biaoz,"+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "jingz+yuns-biaoz", jies_Jsslblxs)+" as yingk,"+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "yingd", jies_Jsslblxs)+" as yingd, 	\n");
				sql.append(         Jiesdcz.getJiesszzh(jies_Jieslqzfs, jies_Jssl, jies_Jsslblxs)).append(" as jiessl,   \n");
				sql.append(         Jiesdcz.getJiesszzh(jies_Jieslqzfs, jies_yunfjssl, jies_Jsslblxs)).append(" as yunfjssl,sum(chaokdl) as chaokdl,   \n");
				sql.append("	 --��������   																															\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*round_new(z.qnet_ar,"+jies_Qnetarblxs+"))/sum(("+jies_Jqsl+")),"+jies_Qnetarblxs+")) as Qnetar_cf,   		\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.std)/sum(("+jies_Jqsl+")),"+jies_Stdblxs+")) as Std_cf,   		   		\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.mt)/sum(("+jies_Jqsl+")),"+jies_Mtblxs+")) as Mt_cf,		  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.mad)/sum(("+jies_Jqsl+")),"+jies_Madblxs+")) as Mad_cf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.aar)/sum(("+jies_Jqsl+")),"+jies_Aarblxs+")) as Aar_cf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.aad)/sum(("+jies_Jqsl+")),"+jies_Aadblxs+")) as Aad_cf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.ad)/sum(("+jies_Jqsl+")),"+jies_Adblxs+")) as Ad_cf,		  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.vad)/sum(("+jies_Jqsl+")),"+jies_Vadblxs+")) as Vad_cf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.vdaf)/sum(("+jies_Jqsl+")),"+jies_Vdafblxs+")) as Vdaf_cf,  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.stad)/sum(("+jies_Jqsl+")),"+jies_Stadblxs+")) as Stad_cf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.star)/sum(("+jies_Jqsl+")),"+jies_Starblxs+")) as Star_cf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.had)/sum(("+jies_Jqsl+")),"+jies_Hadblxs+")) as Had_cf,	 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*round_new(z.qbad,"+jies_Qbadblxs+"))/sum(("+jies_Jqsl+")),"+jies_Qbadblxs+")) as Qbad_cf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*round_new(z.qgrad,"+jies_Qgradblxs+"))/sum(("+jies_Jqsl+")),"+jies_Qgradblxs+")) as Qgrad_cf,		 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.t2)/sum(("+jies_Jqsl+")),"+jies_T2blxs+")) as T2_cf,				 		\n");
				sql.append("	 --������   \n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*round_new(kz.qnet_ar,"+jies_Qnetarblxs+"))/sum(("+jies_Jqsl+")),"+jies_Qnetarblxs+")) as Qnetar_kf,  		\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.std)/sum(("+jies_Jqsl+")),"+jies_Stdblxs+")) as Std_kf,   		   		\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.mt)/sum(("+jies_Jqsl+")),"+jies_Mtblxs+")) as Mt_kf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.mad)/sum(("+jies_Jqsl+")),"+jies_Madblxs+")) as Mad_kf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.aar)/sum(("+jies_Jqsl+")),"+jies_Aarblxs+")) as Aar_kf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.aad)/sum(("+jies_Jqsl+")),"+jies_Aadblxs+")) as Aad_kf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.ad)/sum(("+jies_Jqsl+")),"+jies_Adblxs+")) as Ad_kf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.vad)/sum(("+jies_Jqsl+")),"+jies_Vadblxs+")) as Vad_kf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.vdaf)/sum(("+jies_Jqsl+")),"+jies_Vdafblxs+")) as Vdaf_kf,  			\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.stad)/sum(("+jies_Jqsl+")),"+jies_Stadblxs+")) as Stad_kf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.star)/sum(("+jies_Jqsl+")),"+jies_Starblxs+")) as Star_kf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.had)/sum(("+jies_Jqsl+")),"+jies_Hadblxs+")) as Had_kf,	 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*round_new(kz.qbad,"+jies_Qbadblxs+"))/sum(("+jies_Jqsl+")),"+jies_Qbadblxs+")) as Qbad_kf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*round_new(kz.qgrad,"+jies_Qgradblxs+"))/sum(("+jies_Jqsl+")),"+jies_Qgradblxs+")) as Qgrad_kf,		 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.t2)/sum(("+jies_Jqsl+")),"+jies_T2blxs+")) as T2_kf					 	\n");
				sql.append("	 	from (select distinct f.id,f.yuanid,f.diancxxb_id,f.gongysb_id,f.meikxxb_id,f.pinzb_id,f.faz_id,									\n");
				sql.append("				f.daoz_id,f.jihkjb_id,f.fahrq,f.daohrq,f.hetb_id,f.zhilb_id,f.jiesb_id,f.yunsfsb_id,f.chec,									\n");
				sql.append("       			sum(cp.maoz) as maoz,sum(cp.piz) as piz,sum(cp.maoz-cp.piz-cp.zongkd) as jingz,												\n");
				
				if(xuanwjsd){
					sql.append("       				max(f.SHUIFTZL) shuiftzl ,										\n");
				}
				
				if(contion_table.equals("")){
//					������ǽ���ú�����Ʊ������ȡԭƱ��
					sql.append("			sum(cp.biaoz) as biaoz,	\n");
				}else{
//					�������ȡ�������Ʊ��
					sql.append("			sum(decode(yfzl.jifzl,null,cp.biaoz,yfzl.jifzl)) as biaoz,	\n");
				}
				
				
				sql.append("       			sum(cp.yingd) as yingd,sum(cp.yingk) as yingk,sum(cp.yuns) as yuns,sum(cp.koud) as koud,sum(cp.kous) as kous,				\n");
				sql.append("       			sum(cp.kouz) as kouz,sum(cp.koum) as koum,sum(cp.zongkd) as zongkd,sum(cp.sanfsl) as sanfsl,count(cp.id) as ches,			\n");
				sql.append("       			sum(nvl(getChaodkd(cp.id,'biaoz','"+jies_Jssl+"','"+Locale.sheq_ht_xscz+"','"+ChaodOrKuid+"'),0)) as chaokdl, 						\n");
				sql.append("				f.tiaozbz,f.yansbhb_id,f.lie_id,f.yuandz_id,f.yuanshdwb_id,f.kuangfzlb_id,f.liucb_id,f.liucztb_id,f.hedbz,f.beiz,			\n");
				sql.append("       			f.ruccbb_id,f.ditjsbz,f.ditjsb_id,f.laimsl,f.laimzl,f.laimkc "+chehh+"																\n");
				sql.append(" 			from fahb f,daozcpb cp"+contion_table+" where f.id=cp.fahb_id "+contion_where+" and cp.jiesb_id=0 and f.lie_id in("+SelIds+")"+yunsdw+" 													\n"); 
				sql.append(" 			group by f.id,f.yuanid,f.diancxxb_id,f.gongysb_id,f.meikxxb_id,f.pinzb_id,f.faz_id,												\n");
				sql.append("       				f.daoz_id,f.jihkjb_id,f.fahrq,f.daohrq,f.hetb_id,f.zhilb_id,f.jiesb_id,f.yunsfsb_id,f.chec,								\n");
				sql.append("       				f.tiaozbz,f.yansbhb_id,f.lie_id,f.yuandz_id,f.yuanshdwb_id,f.kuangfzlb_id,f.liucb_id,f.liucztb_id,f.hedbz,f.beiz,		\n");
				sql.append("       				f.ruccbb_id,f.ditjsbz,f.ditjsb_id,f.laimsl,f.laimzl,f.laimkc "+chehh+"															\n");
				sql.append("			) f,zhilb z,gongysb g,meikxxb m,chezxxb cz,vwyuanshdw vwydw,pinzb pz,yunsfsb ysfs,chezxxb dz,  									\n");
				sql.append("	 	(select distinct f.id as fahb_id,\n" +
								"       decode(SUM(cp.biaoz),0,0,round_new(sum((cp.biaoz)*round_new(k.qnet_ar, " + jies_Qnetarblxs + "))/sum((cp.biaoz)), " + jies_Qnetarblxs + ")) as qnet_ar,\n" + 
							"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.std)/sum((cp.biaoz)),2)) as Std,\n" + 
							"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.mt)/sum((cp.biaoz)),2)) as Mt,\n" + 
							"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.mad)/sum((cp.biaoz)),2)) as Mad,\n" + 
							"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.aar)/sum((cp.biaoz)),2)) as Aar,\n" + 
							"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.aad)/sum((cp.biaoz)),2)) as Aad,\n" + 
							"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.ad)/sum((cp.biaoz)),2)) as Ad,\n" + 
							"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.vad)/sum((cp.biaoz)),2)) as Vad,\n" + 
							"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.vdaf)/sum((cp.biaoz)),2)) as Vdaf,\n" + 
							"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.stad)/sum((cp.biaoz)),2)) as Stad,\n" + 
							"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.star)/sum((cp.biaoz)),2)) as Star,\n" + 
							"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.had)/sum((cp.biaoz)),2)) as Had,\n" + 
							"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*round_new(k.qbad,2))/sum((cp.biaoz)),2)) as Qbad,\n" + 
							"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*round_new(k.qgrad,2))/sum((cp.biaoz)),2)) as Qgrad,\n" + 
							"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.t2)/sum((cp.biaoz)),2)) as T2\n" + 
							"           from fahb f,chepb cp,kuangfzlzb k\n" + 
							"           where f.id=cp.fahb_id\n" + 
							"             and cp.kuangfzlzb_id=k.id\n" + 
							 			  yunsdw + guohxt +
							"             and f.lie_id in ("+SelIds+")\n" + 
							"             GROUP BY f.id) kz			\n");
				sql.append("			where f.zhilb_id=z.id and f.id=kz.fahb_id(+) and f.faz_id=cz.id and f.pinzb_id=pz.id   								\n");
				sql.append("				and f.gongysb_id=g.id and f.meikxxb_id=m.id and f.yuanshdwb_id=vwydw.id(+) and f.yunsfsb_id=ysfs.id and f.daoz_id=dz.id   	\n");
				sql.append("				and f.liucztb_id=1 and z.liucztb_id=1").append(Tsclzb_where).append(ranlpz_where);
				sql.append("				and f.lie_id in("+SelIds+")) 	\n");
				
			}else{
//				����ָ�����
//				���˵�װ�˷��ⶼִ����������
				
				sql.append(" select nvl(Qnetar_cf,0) as Qnetar_cf,nvl(Qnetar_kf,0) as Qnetar_kf,nvl(Std_cf,0) as Std_cf,nvl(Std_kf,0) as Std_kf,nvl(Mt_cf,0) as Mt_cf,nvl(Mt_kf,0) as Mt_kf,nvl(Mad_cf,0) as Mad_cf,nvl(Mad_kf,0) as Mad_kf,nvl(Aar_cf,0) as Aar_cf,nvl(Aar_kf,0) as Aar_kf,nvl(Aad_cf,0) as Aad_cf,nvl(Aad_kf,0) as Aad_kf,nvl(Ad_cf,0) as Ad_cf,				\n");
				sql.append(" 		nvl(Ad_kf,0) as Ad_kf,nvl(Vad_cf,0) as Vad_cf,nvl(Vad_kf,0) as Vad_kf,nvl(Vdaf_cf,0) as Vdaf_cf,nvl(Vdaf_kf,0) as Vdaf_kf,nvl(Stad_cf,0) as Stad_cf,nvl(Stad_kf,0) as Stad_kf,nvl(star_cf,0) as star_cf,nvl(star_kf,0) as star_kf,nvl(Had_cf,0) as Had_cf,nvl(Had_kf,0) as Had_kf,nvl(Qbad_cf,0) as Qbad_cf,nvl(Qbad_kf,0) as Qbad_kf,		\n");
				sql.append("		nvl(Qgrad_cf,0) as Qgrad_cf,nvl(Qgrad_kf,0) as Qgrad_kf,nvl(T2_cf,0) as T2_cf,nvl(T2_kf,0) as T2_kf,	\n");
				if (!bsv.getDanglgs().equals("")) { // �жϽ������÷������Ƿ����á������������������㵱��
					sql.append("round_new((").append(bsv.getDanglgs()).append("), ").append(bsv.getDanglblxsw()).append(") as dangl,");
				}
				if(handjsd){//�����糧Ҫ��ʵ����Ϊ����
					 sql.append(" 		yuns,jingz,koud,kous,kouz,ches,biaoz,yingk,jingz2 as yanssl,jiessl,(jiessl-jingz) as jieslcy,yunfjssl,chaokdl,yingd,yingd-yingk as kuid from (select "+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "yuns", jies_Jsslblxs)+" as yuns,"+Jiesdcz.getJiesszzh(jies_Guohlqzfs,"jingz",jies_Guohlblxsw)+" as jingz,"+Jiesdcz.getJiesszzh(jies_Jieslqzfs,"jingz",jies_Jsslblxs)+" as jingz2,					\n");
				 }else{
					 sql.append(" 		yuns,jingz,koud,kous,kouz,ches,biaoz,yingk,(jingz2+yuns) as yanssl,jiessl,(jiessl-jingz) as jieslcy,yunfjssl,chaokdl,yingd,yingd-yingk as kuid from (select "+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "yuns", jies_Jsslblxs)+" as yuns,"+Jiesdcz.getJiesszzh(jies_Guohlqzfs,"jingz",jies_Guohlblxsw)+" as jingz,"+Jiesdcz.getJiesszzh(jies_Jieslqzfs,"jingz",jies_Jsslblxs)+" as jingz2,					\n");
				 }
				sql.append(			Jiesdcz.getJiesszzh(jies_Kdkskzqzfs, "koud", jies_Jsslblxs)+" as koud,"+Jiesdcz.getJiesszzh(jies_Kdkskzqzfs, "kous", jies_Jsslblxs)+" as kous,"+Jiesdcz.getJiesszzh(jies_Kdkskzqzfs, "kouz", jies_Jsslblxs)+" as kouz,	\n");
				sql.append(" 		sum(ches) as ches, "+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "biaoz", jies_Jsslblxs)+" as biaoz,"+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "jingz+yuns-biaoz", jies_Jsslblxs)+" as yingk,"+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "yingd", jies_Jsslblxs)+" as yingd, 	\n");
				sql.append(         Jiesdcz.getJiesszzh(jies_Jieslqzfs, jies_Jssl, jies_Jsslblxs)).append(" as jiessl,   \n");
				sql.append(         Jiesdcz.getJiesszzh(jies_Jieslqzfs, jies_yunfjssl, jies_Jsslblxs)).append(" as yunfjssl,sum(chaokdl) as chaokdl,   \n");
				sql.append("	 --��������   																															\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*round_new(z.qnet_ar,"+jies_Qnetarblxs+"))/sum(("+jies_Jqsl+")),"+jies_Qnetarblxs+")) as Qnetar_cf,   		\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.std)/sum(("+jies_Jqsl+")),"+jies_Stdblxs+")) as Std_cf,   		   		\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.mt)/sum(("+jies_Jqsl+")),"+jies_Mtblxs+")) as Mt_cf,		  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.mad)/sum(("+jies_Jqsl+")),"+jies_Madblxs+")) as Mad_cf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.aar)/sum(("+jies_Jqsl+")),"+jies_Aarblxs+")) as Aar_cf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.aad)/sum(("+jies_Jqsl+")),"+jies_Aadblxs+")) as Aad_cf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.ad)/sum(("+jies_Jqsl+")),"+jies_Adblxs+")) as Ad_cf,		  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.vad)/sum(("+jies_Jqsl+")),"+jies_Vadblxs+")) as Vad_cf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.vdaf)/sum(("+jies_Jqsl+")),"+jies_Vdafblxs+")) as Vdaf_cf,  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.stad)/sum(("+jies_Jqsl+")),"+jies_Stadblxs+")) as Stad_cf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.star)/sum(("+jies_Jqsl+")),"+jies_Starblxs+")) as Star_cf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.had)/sum(("+jies_Jqsl+")),"+jies_Hadblxs+")) as Had_cf,	 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*round_new(z.qbad,"+jies_Qbadblxs+"))/sum(("+jies_Jqsl+")),"+jies_Qbadblxs+")) as Qbad_cf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*round_new(z.qgrad,"+jies_Qgradblxs+"))/sum(("+jies_Jqsl+")),"+jies_Qgradblxs+")) as Qgrad_cf,		 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*z.t2)/sum(("+jies_Jqsl+")),"+jies_T2blxs+")) as T2_cf,				 		\n");
				sql.append("	 --������   \n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*round_new(kz.qnet_ar,"+jies_Qnetarblxs+"))/sum(("+jies_Jqsl+")),"+jies_Qnetarblxs+")) as Qnetar_kf,  		\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.std)/sum(("+jies_Jqsl+")),"+jies_Stdblxs+")) as Std_kf,   		   		\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.mt)/sum(("+jies_Jqsl+")),"+jies_Mtblxs+")) as Mt_kf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.mad)/sum(("+jies_Jqsl+")),"+jies_Madblxs+")) as Mad_kf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.aar)/sum(("+jies_Jqsl+")),"+jies_Aarblxs+")) as Aar_kf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.aad)/sum(("+jies_Jqsl+")),"+jies_Aadblxs+")) as Aad_kf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.ad)/sum(("+jies_Jqsl+")),"+jies_Adblxs+")) as Ad_kf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.vad)/sum(("+jies_Jqsl+")),"+jies_Vadblxs+")) as Vad_kf,	  			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.vdaf)/sum(("+jies_Jqsl+")),"+jies_Vdafblxs+")) as Vdaf_kf,  			\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.stad)/sum(("+jies_Jqsl+")),"+jies_Stadblxs+")) as Stad_kf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.star)/sum(("+jies_Jqsl+")),"+jies_Starblxs+")) as Star_kf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.had)/sum(("+jies_Jqsl+")),"+jies_Hadblxs+")) as Had_kf,	 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*round_new(kz.qbad,"+jies_Qbadblxs+"))/sum(("+jies_Jqsl+")),"+jies_Qbadblxs+")) as Qbad_kf, 			 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*round_new(kz.qgrad,"+jies_Qgradblxs+"))/sum(("+jies_Jqsl+")),"+jies_Qgradblxs+")) as Qgrad_kf,		 	\n");
				sql.append("	 decode(sum("+jies_Jqsl+"),0,0,round_new(sum(("+jies_Jqsl+")*kz.t2)/sum(("+jies_Jqsl+")),"+jies_T2blxs+")) as T2_kf					 	\n");
				sql.append("	 	from (select distinct f.id,f.yuanid,f.diancxxb_id,f.gongysb_id,f.meikxxb_id,f.pinzb_id,f.faz_id,									\n");
				sql.append("				f.daoz_id,f.jihkjb_id,f.fahrq,f.daohrq,f.hetb_id,f.zhilb_id,f.jiesb_id,f.yunsfsb_id,f.chec,									\n");
				sql.append("       			sum(cp.maoz) as maoz,sum(cp.piz) as piz,sum(cp.maoz-cp.piz) as jingz,									\n");
				
				if(xuanwjsd){
					sql.append("       				max(f.SHUIFTZL) shuiftzl ,										\n");
				}
				if(contion_table.equals("")){
//					������ǽ���ú�����Ʊ������ȡԭƱ��
					sql.append("			sum(cp.biaoz) as biaoz,	\n");
				}else{
//					�������ȡ�������Ʊ��
					sql.append("			sum(decode(yfzl.jifzl,null,cp.biaoz,yfzl.jifzl)) as biaoz,	\n");
				}
				
				sql.append("       			sum(cp.yingd) as yingd,sum(cp.yingk) as yingk,sum(cp.yuns) as yuns,sum(cp.koud) as koud,sum(cp.kous) as kous,				\n");
				sql.append("       			sum(cp.kouz) as kouz,sum(cp.koum) as koum,sum(cp.zongkd) as zongkd,sum(cp.sanfsl) as sanfsl,count(cp.id) as ches,			\n");
				sql.append("       			sum(nvl(getChaodkd(cp.id,'biaoz','"+jies_Jssl+"','"+Locale.sheq_ht_xscz+"','"+ChaodOrKuid+"'),0)) as chaokdl, 						\n");
				sql.append("				f.tiaozbz,f.yansbhb_id,f.lie_id,f.yuandz_id,f.yuanshdwb_id,f.kuangfzlb_id,f.liucb_id,f.liucztb_id,f.hedbz,f.beiz,			\n");
				sql.append("       			f.ruccbb_id,f.ditjsbz,f.ditjsb_id,f.laimsl,f.laimzl,f.laimkc "+chehh+"																\n");
				sql.append(" 			from fahb f,chepb cp"+contion_table+" where f.id=cp.fahb_id "+contion_where+ guohxt +" and f.lie_id in("+SelIds+")"+yunsdw+" 													\n"); 
				sql.append(" 			group by f.id,f.yuanid,f.diancxxb_id,f.gongysb_id,f.meikxxb_id,f.pinzb_id,f.faz_id,												\n");
				sql.append("       				f.daoz_id,f.jihkjb_id,f.fahrq,f.daohrq,f.hetb_id,f.zhilb_id,f.jiesb_id,f.yunsfsb_id,f.chec,								\n");
				sql.append("       				f.tiaozbz,f.yansbhb_id,f.lie_id,f.yuandz_id,f.yuanshdwb_id,f.kuangfzlb_id,f.liucb_id,f.liucztb_id,f.hedbz,f.beiz,		\n");
				sql.append("       				f.ruccbb_id,f.ditjsbz,f.ditjsb_id,f.laimsl,f.laimzl,f.laimkc "+chehh+"															\n");
				sql.append("			) f,zhilb z,gongysb g,meikxxb m,chezxxb cz,vwyuanshdw vwydw,pinzb pz,yunsfsb ysfs,chezxxb dz,  									\n");
				
				sql.append("	 	(select distinct f.id as fahb_id,\n" +
						"       decode(SUM(cp.biaoz),0,0,round_new(sum((cp.biaoz)*round_new(k.qnet_ar, " + jies_Qnetarblxs + "))/sum((cp.biaoz)), " + jies_Qnetarblxs + ")) as qnet_ar,\n" + 
					"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.std)/sum((cp.biaoz)),2)) as Std,\n" + 
					"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.mt)/sum((cp.biaoz)),2)) as Mt,\n" + 
					"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.mad)/sum((cp.biaoz)),2)) as Mad,\n" + 
					"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.aar)/sum((cp.biaoz)),2)) as Aar,\n" + 
					"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.aad)/sum((cp.biaoz)),2)) as Aad,\n" + 
					"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.ad)/sum((cp.biaoz)),2)) as Ad,\n" + 
					"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.vad)/sum((cp.biaoz)),2)) as Vad,\n" + 
					"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.vdaf)/sum((cp.biaoz)),2)) as Vdaf,\n" + 
					"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.stad)/sum((cp.biaoz)),2)) as Stad,\n" + 
					"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.star)/sum((cp.biaoz)),2)) as Star,\n" + 
					"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.had)/sum((cp.biaoz)),2)) as Had,\n" + 
					"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*round_new(k.qbad,2))/sum((cp.biaoz)),2)) as Qbad,\n" + 
					"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*round_new(k.qgrad,2))/sum((cp.biaoz)),2)) as Qgrad,\n" + 
					"           decode(sum(cp.biaoz),0,0,round_new(sum((cp.biaoz)*k.t2)/sum((cp.biaoz)),2)) as T2\n" + 
					"           from fahb f,chepb cp,kuangfzlzb k\n" + 
					"           where f.id=cp.fahb_id\n" + 
					"             and cp.kuangfzlzb_id=k.id\n" + 
					 			  yunsdw + guohxt +
					"             and f.lie_id in ("+SelIds+")\n" + 
					"             GROUP BY f.id) kz			\n");
				sql.append("			where f.zhilb_id=z.id and f.id=kz.fahb_id(+) and f.faz_id=cz.id and f.pinzb_id=pz.id   								\n");
				sql.append("				and f.gongysb_id=g.id and f.meikxxb_id=m.id and f.yuanshdwb_id=vwydw.id(+) and f.yunsfsb_id=ysfs.id and f.daoz_id=dz.id   	\n");
				sql.append("				and f.liucztb_id=1 and z.liucztb_id=1").append(Tsclzb_where).append(ranlpz_where);
				sql.append("				and f.lie_id in("+SelIds+")) 	\n");
			}
			
			
		
		}else if(Jieszbsftz.equals("��")){
			
//			��Ҫָ�����
			if(Jieslx==Locale.daozyf_feiylbb_id){
//				��װ�˷�
				sql.append(" select nvl(Qnetar_cf,0) as Qnetar_cf,nvl(Qnetar_kf,0) as Qnetar_kf,nvl(Qnetar_js,0) as Qnetar_js,nvl(Std_cf,0) as Std_cf,nvl(Std_kf,0) as Std_kf,nvl(Std_js,0) as Std_js,\n ");
				sql.append("		nvl(Mt_cf,0) as Mt_cf,nvl(Mt_kf,0) as Mt_kf,nvl(Mt_js,0) as Mt_js,nvl(Mad_cf,0) as Mad_cf,nvl(Mad_kf,0) as Mad_kf,nvl(Mad_js,0) as Mad_js,nvl(Aar_cf,0) as Aar_cf,\n ");
				sql.append("		nvl(Aar_kf,0) as Aar_kf,nvl(Aar_js,0) as Aar_js,nvl(Aad_cf,0) as Aad_cf,nvl(Aad_kf,0) as Aad_kf,nvl(Aad_js,0) as Aad_js,nvl(Ad_cf,0) as Ad_cf,nvl(Ad_kf,0) as Ad_kf,nvl(Ad_js,0) as Ad_js,	\n");
				sql.append(" 		nvl(Vad_cf,0) as Vad_cf,nvl(Vad_kf,0) as Vad_kf,nvl(Vad_js,0) as Vad_js,nvl(Vdaf_cf,0) as Vdaf_cf,nvl(Vdaf_kf,0) as Vdaf_kf,nvl(Vdaf_js,0) as Vdaf_js,nvl(Stad_cf,0) as Stad_cf, \n ");
				sql.append("		nvl(Stad_kf,0) as Stad_kf,nvl(Stad_js,0) as Stad_js,nvl(star_cf,0) as star_cf,nvl(star_kf,0) as star_kf,nvl(star_js,0) as star_js,nvl(Had_cf,0) as Had_cf,nvl(Had_kf,0) as Had_kf, \n ");
				sql.append("		nvl(Had_js,0) as Had_js,nvl(Qbad_cf,0) as Qbad_cf,nvl(Qbad_kf,0) as Qbad_kf,nvl(Qbad_js,0) as Qbad_js,nvl(Qgrad_cf,0) as Qgrad_cf,nvl(Qgrad_kf,0) as Qgrad_kf,nvl(Qgrad_js,0) as Qgrad_js, \n ");
				sql.append("		nvl(T2_cf,0) as T2_cf,nvl(T2_kf,0) as T2_kf,nvl(T2_js,0) as T2_js,	\n");
				if (!bsv.getDanglgs().equals("")) { // �жϽ������÷������Ƿ����á������������������㵱��
					sql.append("round_new((").append(bsv.getDanglgs()).append("), ").append(bsv.getDanglblxsw()).append(") as dangl,");
				}
				if(handjsd){//�����糧Ҫ��ʵ����Ϊ����
					sql.append(" 		yuns,jingz,koud,kous,kouz,ches,Jiessl_kf as biaoz,(jingz2+yuns-Jiessl_kf) as yingk,jingz2 as yanssl,Jiessl_js as jiessl,(Jiessl_js-jingz) as jieslcy,yunfjssl,chaokdl,yingd,yingd-( +yuns-Jiessl_kf) as kuid from (select "+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "yuns", jies_Jsslblxs)+" as yuns,"+Jiesdcz.getJiesszzh(jies_Guohlqzfs,"jingz",jies_Guohlblxsw)+" as jingz,	"+Jiesdcz.getJiesszzh(jies_Jieslqzfs,"jingz",jies_Jsslblxs)+" as jingz2,					\n");
				 }else{
					 sql.append(" 		yuns,jingz,koud,kous,kouz,ches,Jiessl_kf as biaoz,(jingz2+yuns-Jiessl_kf) as yingk,(jingz2+yuns) as yanssl,Jiessl_js as jiessl,(Jiessl_js-jingz) as jieslcy,yunfjssl,chaokdl,yingd,yingd-( +yuns-Jiessl_kf) as kuid from (select "+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "yuns", jies_Jsslblxs)+" as yuns,"+Jiesdcz.getJiesszzh(jies_Guohlqzfs,"jingz",jies_Guohlblxsw)+" as jingz,	"+Jiesdcz.getJiesszzh(jies_Jieslqzfs,"jingz",jies_Jsslblxs)+" as jingz2,					\n");
				 }
				sql.append(			Jiesdcz.getJiesszzh(jies_Kdkskzqzfs, "koud", jies_Jsslblxs)+" as koud,"+Jiesdcz.getJiesszzh(jies_Kdkskzqzfs, "kous", jies_Jsslblxs)+" as kous,"+Jiesdcz.getJiesszzh(jies_Kdkskzqzfs, "kouz", jies_Jsslblxs)+" as kouz,	\n");
				sql.append(" 		sum(ches) as ches,"+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "yingd", jies_Jsslblxs)+" as yingd, 	\n");
				sql.append(         Jiesdcz.getJiesszzh(jies_Jieslqzfs, jies_yunfjssl, jies_Jsslblxs)).append(" as yunfjssl,sum(chaokdl) as chaokdl,   \n");
				sql.append("	 --��������   																															\n");
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
				sql.append("	 --������   \n");
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
				sql.append("	 --����ָ��   \n");
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
				
				if(xuanwjsd){
					sql.append("       				max(f.SHUIFTZL) shuiftzl ,										\n");
				}
				
				if(contion_table.equals("")){
//					������ǽ���ú�����Ʊ������ȡԭƱ��
					sql.append("			sum(cp.biaoz) as biaoz,	\n");
				}else{
//					�������ȡ�������Ʊ��
					sql.append("			sum(decode(yfzl.jifzl,null,cp.biaoz,yfzl.jifzl)) as biaoz,	\n");
				}
				
				sql.append("       			sum(cp.yingd) as yingd,sum(cp.yingk) as yingk,sum(cp.yuns) as yuns,sum(cp.koud) as koud,sum(cp.kous) as kous,				\n");
				sql.append("       			sum(cp.kouz) as kouz,sum(cp.koum) as koum,sum(cp.zongkd) as zongkd,sum(cp.sanfsl) as sanfsl,count(cp.id) as ches,			\n");
				sql.append("       			sum(nvl(getChaodkd(cp.id,'biaoz','"+jies_Jssl+"','"+Locale.sheq_ht_xscz+"','"+ChaodOrKuid+"'),0)) as chaokdl, 				\n");
				sql.append("       			f.tiaozbz,f.yansbhb_id,f.lie_id,f.yuandz_id,f.yuanshdwb_id,f.kuangfzlb_id,f.liucb_id,f.liucztb_id,f.hedbz,f.beiz,			\n");
				sql.append("       			f.ruccbb_id,f.ditjsbz,f.ditjsb_id,f.laimsl,f.laimzl,f.laimkc "+chehh+"															\n");
				sql.append(" 			from fahb f,daozcpb cp"+contion_table+" where f.id=cp.fahb_id "+contion_where+" and cp.jiesb_id=0 and f.lie_id in("+SelIds+")"+yunsdw+" 			\n"); 
				sql.append(" 			group by f.id,f.yuanid,f.diancxxb_id,f.gongysb_id,f.meikxxb_id,f.pinzb_id,f.faz_id,												\n");
				sql.append("       				f.daoz_id,f.jihkjb_id,f.fahrq,f.daohrq,f.hetb_id,f.zhilb_id,f.jiesb_id,f.yunsfsb_id,f.chec,								\n");
				sql.append("       				f.tiaozbz,f.yansbhb_id,f.lie_id,f.yuandz_id,f.yuanshdwb_id,f.kuangfzlb_id,f.liucb_id,f.liucztb_id,f.hedbz,f.beiz,		\n");
				sql.append("       				f.ruccbb_id,f.ditjsbz,f.ditjsb_id,f.laimsl,f.laimzl,f.laimkc "+chehh+"														\n");
				sql.append("			) f,yansbhb y,gongysb g,meikxxb m,chezxxb cz,vwyuanshdw vwydw,pinzb pz,yunsfsb ysfs,chezxxb dz  								\n");
				sql.append("			where f.faz_id=cz.id and f.pinzb_id=pz.id and f.yansbhb_id=y.id \n");
				sql.append("				and f.gongysb_id=g.id and f.meikxxb_id=m.id and f.yuanshdwb_id=vwydw.id(+) and f.yunsfsb_id=ysfs.id and f.daoz_id=dz.id   	\n");
				sql.append("				and f.liucztb_id=1").append(Tsclzb_where);
				sql.append("				and f.lie_id in("+SelIds+")) 	\n");
				
			}else{
//				��Ҫָ�����
//				���˵�װ�˷���ʹ���������
				
				sql.append(" select nvl(Qnetar_cf,0) as Qnetar_cf,nvl(Qnetar_kf,0) as Qnetar_kf,nvl(Qnetar_js,0) as Qnetar_js,nvl(Std_cf,0) as Std_cf,nvl(Std_kf,0) as Std_kf,nvl(Std_js,0) as Std_js,\n ");
				sql.append("		nvl(Mt_cf,0) as Mt_cf,nvl(Mt_kf,0) as Mt_kf,nvl(Mt_js,0) as Mt_js,nvl(Mad_cf,0) as Mad_cf,nvl(Mad_kf,0) as Mad_kf,nvl(Mad_js,0) as Mad_js,nvl(Aar_cf,0) as Aar_cf,\n ");
				sql.append("		nvl(Aar_kf,0) as Aar_kf,nvl(Aar_js,0) as Aar_js,nvl(Aad_cf,0) as Aad_cf,nvl(Aad_kf,0) as Aad_kf,nvl(Aad_js,0) as Aad_js,nvl(Ad_cf,0) as Ad_cf,nvl(Ad_kf,0) as Ad_kf,nvl(Ad_js,0) as Ad_js,	\n");
				sql.append(" 		nvl(Vad_cf,0) as Vad_cf,nvl(Vad_kf,0) as Vad_kf,nvl(Vad_js,0) as Vad_js,nvl(Vdaf_cf,0) as Vdaf_cf,nvl(Vdaf_kf,0) as Vdaf_kf,nvl(Vdaf_js,0) as Vdaf_js,nvl(Stad_cf,0) as Stad_cf, \n ");
				sql.append("		nvl(Stad_kf,0) as Stad_kf,nvl(Stad_js,0) as Stad_js,nvl(star_cf,0) as star_cf,nvl(star_kf,0) as star_kf,nvl(star_js,0) as star_js,nvl(Had_cf,0) as Had_cf,nvl(Had_kf,0) as Had_kf, \n ");
				sql.append("		nvl(Had_js,0) as Had_js,nvl(Qbad_cf,0) as Qbad_cf,nvl(Qbad_kf,0) as Qbad_kf,nvl(Qbad_js,0) as Qbad_js,nvl(Qgrad_cf,0) as Qgrad_cf,nvl(Qgrad_kf,0) as Qgrad_kf,nvl(Qgrad_js,0) as Qgrad_js, \n ");
				sql.append("		nvl(T2_cf,0) as T2_cf,nvl(T2_kf,0) as T2_kf,nvl(T2_js,0) as T2_js,	\n");
				if (!bsv.getDanglgs().equals("")) { // �жϽ������÷������Ƿ����á������������������㵱��
					sql.append("round_new((").append(bsv.getDanglgs()).append("), ").append(bsv.getDanglblxsw()).append(") as dangl,");
				}
				if(handjsd){//�����糧Ҫ��ʵ����Ϊ����
					sql.append(" 		yuns,jingz,koud,kous,kouz,ches,Jiessl_kf as biaoz,(jingz2+yuns-Jiessl_kf) as yingk,jingz2 as yanssl,Jiessl_js as jiessl,(Jiessl_js-jingz) as jieslcy,yunfjssl,chaokdl,yingd,yingd-(jingz2+yuns-Jiessl_kf) as kuid from (select "+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "yuns", jies_Jsslblxs)+" as yuns,"+Jiesdcz.getJiesszzh(jies_Guohlqzfs,"jingz",jies_Guohlblxsw)+" as jingz, "+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "jingz", jies_Jsslblxs)+" as jingz2,						\n");
				 }else if(datjsd){//��ͬ�糧���㵥
					 sql.append(" 		yuns,jingz,koud,kous,kouz,ches,Jiessl_kf as biaoz,(Jiessl_js-Jiessl_kf) as yingk,jiessl_cf as yanssl,Jiessl_js as jiessl,(Jiessl_js-jiessl_cf) as jieslcy,yunfjssl,chaokdl,yingd,(Jiessl_js-Jiessl_kf) as kuid from (select "+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "yuns", jies_Jsslblxs)+" as yuns,"+Jiesdcz.getJiesszzh(jies_Guohlqzfs,"jingz",jies_Guohlblxsw)+" as jingz, "+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "jingz", jies_Jsslblxs)+" as jingz2,						\n");	
				 }else{
					 sql.append(" 		yuns,jingz,koud,kous,kouz,ches,Jiessl_kf as biaoz,(jingz2+yuns-Jiessl_kf) as yingk,(jingz2+yuns) as yanssl,Jiessl_js as jiessl,(Jiessl_js-jingz) as jieslcy,yunfjssl,chaokdl,yingd,yingd-(jingz2+yuns-Jiessl_kf) as kuid from (select "+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "yuns", jies_Jsslblxs)+" as yuns,"+Jiesdcz.getJiesszzh(jies_Guohlqzfs,"jingz",jies_Guohlblxsw)+" as jingz, "+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "jingz", jies_Jsslblxs)+" as jingz2,						\n");
				 }
				sql.append(			Jiesdcz.getJiesszzh(jies_Kdkskzqzfs, "koud", jies_Jsslblxs)+" as koud,"+Jiesdcz.getJiesszzh(jies_Kdkskzqzfs, "kous", jies_Jsslblxs)+" as kous,"+Jiesdcz.getJiesszzh(jies_Kdkskzqzfs, "kouz", jies_Jsslblxs)+" as kouz,	\n");
				sql.append(" 		sum(ches) as ches,"+Jiesdcz.getJiesszzh(jies_Jieslqzfs, "yingd", jies_Jsslblxs)+" as yingd, 	\n");
				sql.append(         Jiesdcz.getJiesszzh(jies_Jieslqzfs, jies_yunfjssl, jies_Jsslblxs)).append(" as yunfjssl,sum(chaokdl) as chaokdl,   \n");
				sql.append("	 --��������   																															\n");
				sql.append("	 getJiesdzb_tz(max(y.id),'"+Locale.jiessl_zhibb+"','changf') as jiessl_cf,   		\n");
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
				sql.append("	 --������   \n");
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
				sql.append("	 --����ָ��   \n");
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
				
				if(xuanwjsd){
					sql.append("       				max(f.SHUIFTZL) shuiftzl ,										\n");
				}
				
				if(contion_table.equals("")){
//					������ǽ���ú�����Ʊ������ȡԭƱ��
					sql.append("			sum(cp.biaoz) as biaoz,	\n");
				}else{
//					�������ȡ�������Ʊ��
					sql.append("			sum(decode(yfzl.jifzl,null,cp.biaoz,yfzl.jifzl)) as biaoz,	\n");
				}
				
				sql.append("       			sum(cp.yingd) as yingd,sum(cp.yingk) as yingk,sum(cp.yuns) as yuns,sum(cp.koud) as koud,sum(cp.kous) as kous,				\n");
				sql.append("       			sum(cp.kouz) as kouz,sum(cp.koum) as koum,sum(cp.zongkd) as zongkd,sum(cp.sanfsl) as sanfsl,count(cp.id) as ches,			\n");
				sql.append("       			sum(nvl(getChaodkd(cp.id,'biaoz','"+jies_Jssl+"','"+Locale.sheq_ht_xscz+"','"+ChaodOrKuid+"'),0)) as chaokdl, 				\n");
				sql.append("       			f.tiaozbz,f.yansbhb_id,f.lie_id,f.yuandz_id,f.yuanshdwb_id,f.kuangfzlb_id,f.liucb_id,f.liucztb_id,f.hedbz,f.beiz,			\n");
				sql.append("       			f.ruccbb_id,f.ditjsbz,f.ditjsb_id,f.laimsl,f.laimzl,f.laimkc "+chehh+"																\n");
				sql.append(" 			from fahb f,chepb cp"+contion_table+" where f.id=cp.fahb_id "+contion_where+" and f.lie_id in("+SelIds+")"+yunsdw+" 			\n"); 
				sql.append(" 			group by f.id,f.yuanid,f.diancxxb_id,f.gongysb_id,f.meikxxb_id,f.pinzb_id,f.faz_id,												\n");
				sql.append("       				f.daoz_id,f.jihkjb_id,f.fahrq,f.daohrq,f.hetb_id,f.zhilb_id,f.jiesb_id,f.yunsfsb_id,f.chec,								\n");
				sql.append("       				f.tiaozbz,f.yansbhb_id,f.lie_id,f.yuandz_id,f.yuanshdwb_id,f.kuangfzlb_id,f.liucb_id,f.liucztb_id,f.hedbz,f.beiz,		\n");
				sql.append("       				f.ruccbb_id,f.ditjsbz,f.ditjsb_id,f.laimsl,f.laimzl,f.laimkc "+chehh+"														\n");
				sql.append("			) f,yansbhb y,gongysb g,meikxxb m,chezxxb cz,vwyuanshdw vwydw,pinzb pz,yunsfsb ysfs,chezxxb dz  								\n");
				sql.append("			where f.faz_id=cz.id and f.pinzb_id=pz.id and f.yansbhb_id=y.id \n");
				sql.append("				and f.gongysb_id=g.id and f.meikxxb_id=m.id and f.yuanshdwb_id=vwydw.id(+) and f.yunsfsb_id=ysfs.id and f.daoz_id=dz.id   	\n");
				sql.append("				and f.liucztb_id=1").append(Tsclzb_where);
				sql.append("				and f.lie_id in("+SelIds+")) 	\n");
			}
		}
		return sql;
	}
	
	public static void setJieszbzdj_Tszb(String zhibbm,Balances_variable bsv,double zhedj,double zhibjsbz,double zhibzje){
//		�������ܣ�
//			1������ָ�꾭�����⴦�����󣬵õ���ָ����۵�����Ϣ���۽����Ϣ������ֵ��ֵ��bsv
//			2��������ָ���ֵ�����ǽ���ֵ��������Ӧ����
//		�����߼���
//			�����zhibbm������ĳһ��ָ��ʱ���ͽ��۵��۸�ֵ����ָ����۵���
//		�����βΣ�
//			zhibbmҪ��ֵ��ָ����룬bsvȫ���࣬zhedjָ���۵��ۣ�zhibjsbzָ�����ֵ��zhibzjeָ���۽��
//		2010-01-09	zsj �޸� ���۽�ֵȥ��
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
//			�˾��۵���
			bsv.setYunju_zdj_tscl(zhedj);
			bsv.setYunju_zje_tscl(zhibzje);
			
			if(bsv.getShifykfzljs().equals("��")){
				
				bsv.setYunju_kf(zhibjsbz);
			}else{
				
				bsv.setYunju_cf(zhibjsbz);
			}
			
			bsv.setYunju_js(zhibjsbz);
			
			bsv.setYunju_yk(reCoundYk(bsv.getYunju_ht().trim(),bsv.getYunju_js(),bsv.getYunju_yk()));
			
		}else if(zhibbm.equals(Locale.Star_zhibb)){
//			Star�۵���
			bsv.setStar_zdj_tscl(zhedj);
			bsv.setStar_zje_tscl(zhibzje);
		}
	}
	
	public static void UpdateDanpcjsmkb(long jiesdid,double hansdj,double jiakhj,
			double jiaksk,double jiashj,double jiajqdj){
//		�������ܣ�
//			��������ú��ʱ�����ú��Ϊ������ʱ˵�����溬���˷ѣ�Ҫ������recount������
//				�ú�˰��ú��-�˷Ѻϼ�=��˰��ú�����ʱ�Ѿ���Danpcjsmkb�����˺��˷ѵ�ú����Ϣ�ˣ�
//				����Ҫ�ڼ�ȥ�˷Ѻ����Danpcjsmkb������ݡ�
//		�����߼���
//			����jiesdid��ֵ����Danpcjsmkb�еļ�¼
//		�����βΣ�
//			
		JDBCcon con = new JDBCcon();
		String sql="update danpcjsmxb set jiesdj="+hansdj+", jiakhj="+jiakhj+", jiaksk="+jiaksk+", jiashj="
				+jiashj+", zongje="+jiashj+", jiajqdj="+jiajqdj+" where jiesdid="+jiesdid;
		
		con.getUpdate(sql);
		
		con.Close();
	}
	
	public static boolean Checkdsfht(String hetbh,long diancxxb_id){
//		�������ܣ�
//			�жϺ�ͬ�ġ���ͬ�������Ƿ�Ϊ�ֹ�˾���۸��������ĺ�ͬ������Ƿ���true��������Ƿ���false��
//		�����߼���
//				�ú�ͬ���е�hetgysbid��gongysb������ȡ��ȫ�ƣ��ͷֹ�˾�ĵ糧��Ϣ���ȫ����Ƚϣ�
//			�����ͬ��Ϊ�ֹ�˾�ĺ�ͬ�������ͬ����Ϊ��������ͬ��
//		�����βΣ�
//			hetbh:���ۺ�ͬ�ַ�����diancxxb_id �糧��Ϣ��id
		
		boolean Flag = false;
		JDBCcon con = new JDBCcon();
		
		try{
			
			String[] raw2=hetbh.split(" ");
			hetbh = raw2[0];
			
			String sql = "select decode(ht.quanc,dc.quanc,1,0) as disfht\n" +
						"from\n" + 
						"  (select g.quanc from hetb h,gongysb g\n" + 
						"         where hetbh='"+hetbh+"'\n" + 
						"               and h.hetgysbid = g.id) ht,\n" + 
						"  (select d.quanc from diancxxb d where d.id = "+diancxxb_id+") dc";
			
			ResultSet rs = con.getResultSet(sql);
			if(rs.next()){
				
				if(rs.getString("disfht").equals("0")){
					
					Flag = true;
				}
			}
			rs.close();
		} catch(SQLException e){
			
			e.printStackTrace();
		} catch(Exception e){
			
			e.printStackTrace();
		} finally{
			
			con.Close();
		}
		
		return Flag;
	}
	
	public static void Zijsdlccl(String TalbeName,long Id,long renyxxb_id,String xiaox,
				long liuc_id,String Type){
//		�ӽ��㵥���̴���
		JDBCcon con = new JDBCcon();
		try{
			
			String sql ="select id from "+TalbeName+" where fuid="+Id;
			ResultSet rs = con.getResultSet(sql);
			
			while(rs.next()){
				
				if(Type.equals("TJ")){
//					�ύ
					if(liuc_id<=0){
						
						Liuc.tij(TalbeName, rs.getLong("id"), renyxxb_id, xiaox);
					}else{
						
						Liuc.tij(TalbeName, rs.getLong("id"), renyxxb_id, xiaox, liuc_id);
					}
				}else if(Type.equals("HT")){
//					����
						
					Liuc.huit(TalbeName, rs.getLong("id"), renyxxb_id, xiaox);
				}
			}
			rs.close();
		}catch(SQLException e){
			
			e.printStackTrace();
		}catch(Exception e) {
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}
	
	public static String[][] getYunfjsdpcsz(String fenztj,String lie_ids,String guohxt){
//		�������ܣ�
//			�������õ��˷ѽ������������Ҫ����ķ��������õ��������з��飬������������ת��Ϊ������ʽ��
//		�����߼���
//				����ѡ����id�ķ�����¼����jiesxz�е��������˷ѵ����ν�������������з��飬�õ�����������Ӧ��ֵ��
//			�ڶ�ά��ֵΪ[0]meikxxb_id��[1]�����������[2]�����ֵ��[3]��������
//				����ļ����߼�Ҫ���ݷ�������,�Կ���Ӱ���˷Ѽ۸�����ؽ������¸�ֵ����Ŀǰ��֪���˾࣬ú��
//			���˾����Ǹ���ú��ȡ�õ�,�����ڹ�����������ʱҪ��meikxxb_id��Ϊ�ر�������
		
//				2010-05-19 zsj ��
		
//					Ϊ�����������������⵽վ�����⣺�� �糧�����������⣬�������������ľ��벻ͬ��
//				�˷ѽ���ʱҪ��ÿ������ú�󡢵�վ����������з��顣Ҫ��item �ı���Ҫ��chepb�е�zhongchh��ͬ
		
//		        2010-10-11 yinjm ��		
//		            ����guohxt����
		
//		�����βΣ�
//			fenztj: ��������(�������������Դ�����), lie_ids��Ҫ�������id��guohxt��Ҫ����Ĺ���ϵͳ��������ǵ糧��������ϵͳ(Aϵͳ��Bϵͳ)���˷ѽ������⡣
		JDBCcon con = new JDBCcon();
		String Yunfjsdpcsz[][] = null;
		ResultSetList rsl = null;
		int i = 0;
		String sql ="";
		String strFinalArray[]=null;
		String where_lieid_continue="";
		strFinalArray = Jiesdcz.getFenzzfc(lie_ids, ",", 1000);	//�õ������ַ���
		
		if(strFinalArray!=null){
			
			where_lieid_continue="";
//			˵���з���
			for(int j=0;j<strFinalArray.length;j++){
				
				if(j==0){
					
					where_lieid_continue=" lie_id in ("+strFinalArray[j]+")";
				}else{
					
					where_lieid_continue=" or lie_id in ("+strFinalArray[j]+")";
				}
			}
		}
//		���û��1000 strFinalArray Ϊ��
		if(where_lieid_continue.equals("")){
			
			where_lieid_continue = " lie_id in ("+lie_ids+")";
		}
		
//		�������
		
		if(fenztj.equals(Locale.Yunju_zhibb)){
//			���ǵ糧,��������Ϊ"�˾�"�����صķ�������Ϊ"meikxxb_id"
			
			String where = "";
			if (!guohxt.equals("")) {
				where = "	and i.bianm = '"+ guohxt +"'\n";
			}
			
			sql= 
				"select meikxxb_id, ''''||i.bianm||'''' as bianm	\n" +
				"  from meiksxglb, item i\n" + 
				" where meiksxglb.shuxmc = i.id\n" + 
				"	and meiksxglb.shuxbm = '"+Locale.Yunju_zhibb+"' \n" + where +
				"   and meikxxb_id in (select distinct meikxxb_id\n" + 
				"                        from fahb\n" + 
				"                       where "+where_lieid_continue+")";
			
			rsl = con.getResultSetList(sql);
			if(rsl.getRows()>0){
				
				Yunfjsdpcsz = new String[rsl.getRows()][3];
				while(rsl.next()){
					
					Yunfjsdpcsz[i][0] = rsl.getString("meikxxb_id")+","+rsl.getString("bianm");	//������Ŀֵ
					Yunfjsdpcsz[i][1] = "meikxxb_id,zhongchh"; 
					Yunfjsdpcsz[i][2] =	rsl.getColumnCount()>1?getArrayValue(rsl,0):rsl.getString(1);
					i++;
				}
			}
//				MEIKXXB_ID	BIANM	ZHI
//				264293156	Bϵͳ	71
//				264293156	Aϵͳ	66

		}else{
//			����糧�����ھ��Ǽ�������������Ϊ"meikxxb_id"
			sql = "select distinct max(meikxxb_id) as meikxxb_id,"+fenztj+" from fahb \n"
				+ " where "+where_lieid_continue+"	\n"
				+ "	group by "+fenztj+"";
			rsl = con.getResultSetList(sql);
			if(rsl.getRows()>0){
				
				Yunfjsdpcsz = new String[rsl.getRows()][3];
				while(rsl.next()){
					
					Yunfjsdpcsz[i][0] = rsl.getString("meikxxb_id");
					Yunfjsdpcsz[i][1] = fenztj; 
					Yunfjsdpcsz[i][2] =	rsl.getColumnCount()>2?getArrayValue(rsl,1):rsl.getString(1);
					i++;
				}
			}
		}
		
//		���ۣ�Yunfjsdpcsz�У���[0]��Ԫ�صĵ�һ��ֵΪ meikxxb_id��ֵ
		if(rsl!=null){
			
			rsl.close();
		}
		con.Close();
		return Yunfjsdpcsz;
	}
	
	public static String getArrayValue(ResultSetList rsl,int StartColumn){
//		�������ܣ�
//			��ResultSetList��¼����ȡ��һ�еļ�¼�������������е�ֵ���һ���ַ���
//		�����߼���
//			�õ���¼�����жԵ������У�ѭ������������ַ���
//		�����βΣ�
//			rsl����¼��Դ
		StringBuffer value = new StringBuffer("");
		if(rsl!=null){

			for(int i=StartColumn;i<rsl.getColumnCount();i++){
				
				value.append(rsl.getString(i)).append(",");
			}
			value.deleteCharAt(value.length()-1);
		}
		
		return value.toString();
	}
	
	public static String getSql_Where_assemble(String A,String B){
//		�������ܣ�
//			�������ַ����Զ��ŷָ��ĸ�����ֵ��ϳ�sql��where ���
//		�����߼���
//			��A��B�����ַ����Զ���Ϊ��������������������飬����where ����ƴ��
//		�����βΣ�
//			A���Ⱥ�ǰ����Ŀ���ϡ�B���Ⱥź����Ŀ����
		String strSql_Where = "";
		if(!A.equals("")&&!B.equals("")){
			
			String tmpA[]=null;
			String tmpB[]=null;
			
			tmpA = A.split(",");
			tmpB = B.split(",");
			
			if(tmpA.length==tmpB.length){
				
				for(int i=0;i<tmpA.length;i++){
					
					strSql_Where+= " and "+tmpA[i]+"="+tmpB[i]+" \n";
				}
			}
			
			tmpA=null;
			tmpB=null;
		}
		
		return strSql_Where;
	}
	
	public static double getReCountJiessl(Balances_variable bsv, double jiessl, int Type){
//		�������ܣ�
//			Ϊ�˴������ۿ����������������������㹫ʽ����������������Ҫ��jiessl�����ӻ����
//		�����߼���
//				������ָ������ۿ���������Ӻ��jiessl���ӷ����������jiessl��Ϊ0Ҫ�ø�����ָ���������������ӣ�
//			�����Ҫ��bsv.getJiessl() �� ָ�������������
//		�����βΣ�
//			bsv Balances_variable�ͱ��� , jiessl Ҫ��Ϊ����, Type:0		ú����㣻1	�˷ѽ���
		
//		���¼����������
		double db_tmp = 0;
		
		if(jiessl==0){
			
			if(Type==0){

//				˵������������Դ�� bsv.getJiessl(),����ۼ۵�λΪ�ٷֱȣ�����bsv.getJiessl() ֱ�Ӽ���
				jiessl = bsv.getJiessl();
				
				db_tmp = CountZkkkdl(jiessl,bsv);
				
				bsv.setJiessl(CustomMaths.Round_new(CustomMaths.add(bsv.getJiessl(), db_tmp),Integer.parseInt(bsv.getJiesslblxs())));
				
//				������ʯ��ɽ�˷ѽ��������¼���
				String flag = MainGlobal.getXitxx_item("����", "����ʯ��ɽ�˷ѽ��������¼���", String.valueOf(bsv.getDiancxxb_id()), "��");
				if("��".equals(flag)){
					if(bsv.getJieslx()==Locale.meikjs_feiylbb_id
							&&bsv.getJiesfs().equals(Locale.daocjg_ht_jsfs)){
						
						bsv.setYunfjsl(CustomMaths.Round_new(CustomMaths.add(bsv.getYunfjsl(), db_tmp),Integer.parseInt(bsv.getJiesslblxs())));
					}
				}
			}else if(Type==1){
				
//				˵������������Դ�� bsv.getYunfjsl(),����ۼ۵�λΪ�ٷֱȣ�����bsv.getYunfjsl() ֱ�Ӽ���
				jiessl = bsv.getYunfjsl();
				
				db_tmp = CountZkkkdl(jiessl,bsv);
				
				bsv.setYunfjsl((CustomMaths.Round_new(CustomMaths.add(bsv.getYunfjsl(), db_tmp),Integer.parseInt(bsv.getJiesslblxs()))));
			}
			
		}else{
			
			if(Type==0){

//				��������Ǽ�Ȩƽ���ĵ�����ָ�괦��ʱʹ��
				db_tmp = CountZkkkdl(jiessl,bsv);
				bsv.setJiessl(CustomMaths.Round_new(CustomMaths.add(bsv.getJiessl(), db_tmp),Integer.parseInt(bsv.getJiesslblxs())));
				jiessl =CustomMaths.Round_new(CustomMaths.add(jiessl, db_tmp),Integer.parseInt(bsv.getJiesslblxs()));
				
			}else if(Type==1){
				
//				��������Ǽ�Ȩƽ���ĵ�����ָ�괦��ʱʹ��
				db_tmp = CountZkkkdl(jiessl,bsv);
				bsv.setYunfjsl(CustomMaths.Round_new(CustomMaths.add(bsv.getYunfjsl(), db_tmp),Integer.parseInt(bsv.getJiesslblxs())));
				jiessl =CustomMaths.Round_new(CustomMaths.add(jiessl, db_tmp),Integer.parseInt(bsv.getJiesslblxs()));
			}
		}
		
		return jiessl;
	}
	
	public static double CountZkkkdl(double jiessl,Balances_variable bsv){
//		�������ۿ�۶���
		double db_tmp = 0;
		double db_mtmp = 0;	//����ָ���������
		String str_beiz="";	//��ע�ֶ�
		double db_shul=0;
		
//		��%��ʱ������ϵ������������Ϊ׼
		if(bsv.getKoud_shulxs_yanssl()){
			jiessl = jiessl + bsv.getKoud();//������ʯ��ɽ���Ѹ���ʽ����
//			�������³���ʱ"������"ֵ�����仯��Ϊ��֤������ȷ�����������糧���㷢����ͻ����������,��"������"���¸�ֵ
			
			try{
//				bsv.setStad_zsl(Math.abs(Double.parseDouble(bsv.getStad_ht())-bsv.getStad_js())/Double.parseDouble(bsv.getStad_ht())*-10);
//				bsv.setVdaf_zsl(Math.abs(Double.parseDouble(bsv.getVdaf_ht())-bsv.getVdaf_js())/Double.parseDouble(bsv.getVdaf_ht())*-10);
		
			}catch(Exception e){
				if("".equals(bsv.getVdaf_ht())){
					bsv.getVdaf_ht();
				}
			}
		}
		if(bsv.getShul_zsl()!=0){
			
			db_mtmp = getZkzsl(jiessl, bsv.getShul_zsl(), bsv.getShul_zsldw(), bsv.getKoud_plm_ws());
			db_tmp = CustomMaths.add(db_tmp, db_mtmp);
			str_beiz = "�������ۿ��۽�����";
			
			if(bsv.getBeiz().indexOf(str_beiz)>-1){
//				˵��ǰ���Ѿ����� "�������ۿ��۽�����" �������Ӧ���ۼӿ���
//				�õ�֮ǰ�۵��������뱾�����������ۼ�
				db_shul = Double.parseDouble(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1
						,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
						(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��"))));
				
				db_mtmp = CustomMaths.add(db_mtmp, db_shul);
				bsv.setBeiz(bsv.getBeiz().replaceFirst(	
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)
								,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
								(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��")+1)),
								
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz),bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1)
						+db_mtmp+"��"					
						));
			}else{
				
				bsv.setBeiz(bsv.getBeiz()+" �������ۿ��۽����� "+db_mtmp+"��");
			}
		}
		
		if(bsv.getQnetar_zsl()!=0){
			
			db_mtmp = getZkzsl(jiessl, bsv.getQnetar_zsl(), bsv.getQnetar_zsldw(), bsv.getKoud_plm_ws());
			db_tmp = CustomMaths.add(db_tmp, db_mtmp);
			str_beiz = "Qnetar���ۿ��۽�����";
			
			if(bsv.getBeiz().indexOf(str_beiz)>-1){
//				˵��ǰ���Ѿ����� "�������ۿ��۽�����" �������Ӧ���ۼӿ���
//				�õ�֮ǰ�۵��������뱾�����������ۼ�
				db_shul = Double.parseDouble(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1
						,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
						(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��"))));
				
				db_mtmp = CustomMaths.add(db_mtmp, db_shul);
				bsv.setBeiz(bsv.getBeiz().replaceFirst(	
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)
								,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
								(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��")+1)),
								
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz),bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1)
						+db_mtmp+"��"					
						));
			}else{
				
				bsv.setBeiz(bsv.getBeiz()+" Qnetar���ۿ��۽����� "+db_mtmp+"��");
			}
		}
		
		if(bsv.getStd_zsl()!=0){
			
			db_mtmp = getZkzsl(jiessl, bsv.getStd_zsl(), bsv.getStd_zsldw(), bsv.getKoud_plm_ws());
			db_tmp = CustomMaths.add(db_tmp, db_mtmp);
			str_beiz = "Std���ۿ��۽�����";
			
			if(bsv.getBeiz().indexOf(str_beiz)>-1){
//				˵��ǰ���Ѿ����� "�������ۿ��۽�����" �������Ӧ���ۼӿ���
//				�õ�֮ǰ�۵��������뱾�����������ۼ�
				db_shul = Double.parseDouble(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1
						,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
						(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��"))));
				
				db_mtmp = CustomMaths.add(db_mtmp, db_shul);
				bsv.setBeiz(bsv.getBeiz().replaceFirst(	
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)
								,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
								(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��")+1)),
								
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz),bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1)
						+db_mtmp+"��"					
						));
			}else{
				
				bsv.setBeiz(bsv.getBeiz()+" Std���ۿ��۽����� "+db_mtmp+"��");
			}
		}
		
		if(bsv.getAd_zsl()!=0){
			
			db_mtmp = getZkzsl(jiessl, bsv.getAd_zsl(), bsv.getAd_zsldw(), bsv.getKoud_plm_ws());
			db_tmp = CustomMaths.add(db_tmp, db_mtmp);
			str_beiz = "Ad���ۿ��۽�����";
			
			if(bsv.getBeiz().indexOf(str_beiz)>-1){
//				˵��ǰ���Ѿ����� "�������ۿ��۽�����" �������Ӧ���ۼӿ���
//				�õ�֮ǰ�۵��������뱾�����������ۼ�
				db_shul = Double.parseDouble(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1
						,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
						(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��"))));
				
				db_mtmp = CustomMaths.add(db_mtmp, db_shul);
				bsv.setBeiz(bsv.getBeiz().replaceFirst(	
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)
								,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
								(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��")+1)),
								
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz),bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1)
						+db_mtmp+"��"					
						));
			}else{
				
				bsv.setBeiz(bsv.getBeiz()+" Ad���ۿ��۽����� "+db_mtmp+"��");
			}
		}
		
		if(bsv.getVdaf_zsl()!=0){
			if(jiessl==1996.56){
				jiessl = jiessl;
			}
			db_mtmp = getZkzsl(jiessl, bsv.getVdaf_zsl(), bsv.getVdaf_zsldw(), bsv.getKoud_plm_ws());
			db_tmp = CustomMaths.add(db_tmp, db_mtmp);
			str_beiz = "Vdaf���ۿ��۽�����";
			
			if(bsv.getBeiz().indexOf(str_beiz)>-1){
//				˵��ǰ���Ѿ����� "�������ۿ��۽�����" �������Ӧ���ۼӿ���
//				�õ�֮ǰ�۵��������뱾�����������ۼ�
				db_shul = Double.parseDouble(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1
						,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
						(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��"))));
				
				db_mtmp = CustomMaths.add(db_mtmp, db_shul);
				bsv.setBeiz(bsv.getBeiz().replaceFirst(	
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)
								,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
								(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��")+1)),
								
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz),bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1)
						+db_mtmp+"��"					
						));
			}else{
				
				bsv.setBeiz(bsv.getBeiz()+" Vdaf���ۿ��۽����� "+db_mtmp+"��");
			}
		}
		
		if(bsv.getMt_zsl()!=0){
			
			db_mtmp = getZkzsl(jiessl, bsv.getMt_zsl(), bsv.getMt_zsldw(), bsv.getKoud_plm_ws());
			db_tmp = CustomMaths.add(db_tmp, db_mtmp);
			str_beiz = "Mt���ۿ��۽�����";
			
			if(bsv.getBeiz().indexOf(str_beiz)>-1){
//				˵��ǰ���Ѿ����� "�������ۿ��۽�����" �������Ӧ���ۼӿ���
//				�õ�֮ǰ�۵��������뱾�����������ۼ�
				db_shul = Double.parseDouble(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1
						,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
						(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��"))));
				
				db_mtmp = CustomMaths.add(db_mtmp, db_shul);
				bsv.setBeiz(bsv.getBeiz().replaceFirst(	
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)
								,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
								(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��")+1)),
								
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz),bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1)
						+db_mtmp+"��"					
						));
			}else{
				
				bsv.setBeiz(bsv.getBeiz()+" Mt���ۿ��۽����� "+db_mtmp+"��");
			}
		}
		
		if(bsv.getQgrad_zsl()!=0){
			
			db_mtmp = getZkzsl(jiessl, bsv.getQgrad_zsl(), bsv.getQgrad_zsldw(), bsv.getKoud_plm_ws());
			db_tmp = CustomMaths.add(db_tmp, db_mtmp);
			str_beiz = "Qgrad���ۿ��۽�����";
			
			if(bsv.getBeiz().indexOf(str_beiz)>-1){
//				˵��ǰ���Ѿ����� "�������ۿ��۽�����" �������Ӧ���ۼӿ���
//				�õ�֮ǰ�۵��������뱾�����������ۼ�
				db_shul = Double.parseDouble(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1
						,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
						(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��"))));
				
				db_mtmp = CustomMaths.add(db_mtmp, db_shul);
				bsv.setBeiz(bsv.getBeiz().replaceFirst(	
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)
								,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
								(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��")+1)),
								
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz),bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1)
						+db_mtmp+"��"					
						));
			}else{
				
				bsv.setBeiz(bsv.getBeiz()+" Qgrad���ۿ��۽����� "+db_mtmp+"��");
			}
		}
		
		if(bsv.getQbad_zsl()!=0){
			
			db_mtmp = getZkzsl(jiessl, bsv.getQbad_zsl(), bsv.getQbad_zsldw(), bsv.getKoud_plm_ws());
			db_tmp = CustomMaths.add(db_tmp, db_mtmp);
			str_beiz = "Qbad���ۿ��۽�����";
			
			if(bsv.getBeiz().indexOf(str_beiz)>-1){
//				˵��ǰ���Ѿ����� "�������ۿ��۽�����" �������Ӧ���ۼӿ���
//				�õ�֮ǰ�۵��������뱾�����������ۼ�
				db_shul = Double.parseDouble(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1
						,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
						(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��"))));
				
				db_mtmp = CustomMaths.add(db_mtmp, db_shul);
				bsv.setBeiz(bsv.getBeiz().replaceFirst(	
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)
								,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
								(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��")+1)),
								
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz),bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1)
						+db_mtmp+"��"					
						));
			}else{
				
				bsv.setBeiz(bsv.getBeiz()+" Qbad���ۿ��۽����� "+db_mtmp+"��");
			}
		}
		
		if(bsv.getHad_zsl()!=0){
			
			db_mtmp = getZkzsl(jiessl, bsv.getHad_zsl(), bsv.getHad_zsldw(), bsv.getKoud_plm_ws());
			db_tmp = CustomMaths.add(db_tmp, db_mtmp);
			str_beiz = "Had���ۿ��۽�����";
			
			if(bsv.getBeiz().indexOf(str_beiz)>-1){
//				˵��ǰ���Ѿ����� "�������ۿ��۽�����" �������Ӧ���ۼӿ���
//				�õ�֮ǰ�۵��������뱾�����������ۼ�
				db_shul = Double.parseDouble(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1
						,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
						(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��"))));
				
				db_mtmp = CustomMaths.add(db_mtmp, db_shul);
				bsv.setBeiz(bsv.getBeiz().replaceFirst(	
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)
								,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
								(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��")+1)),
								
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz),bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1)
						+db_mtmp+"��"					
						));
			}else{
				
				bsv.setBeiz(bsv.getBeiz()+" Had���ۿ��۽����� "+db_mtmp+"��");
			}
		}
		
		if(bsv.getStad_zsl()!=0){
			
			db_mtmp = getZkzsl(jiessl, bsv.getStad_zsl(), bsv.getStad_zsldw(), bsv.getKoud_plm_ws());
			db_tmp = CustomMaths.add(db_tmp, db_mtmp);
			str_beiz = "Stad���ۿ��۽�����";
			
			if(bsv.getBeiz().indexOf(str_beiz)>-1){
//				˵��ǰ���Ѿ����� "�������ۿ��۽�����" �������Ӧ���ۼӿ���
//				�õ�֮ǰ�۵��������뱾�����������ۼ�
				db_shul = Double.parseDouble(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1
						,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
						(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��"))));
				
				db_mtmp = CustomMaths.add(db_mtmp, db_shul);
				bsv.setBeiz(bsv.getBeiz().replaceFirst(	
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)
								,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
								(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��")+1)),
								
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz),bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1)
						+db_mtmp+"��"					
						));
			}else{
				
				bsv.setBeiz(bsv.getBeiz()+" Stad���ۿ��۽����� "+db_mtmp+"��");
			}
		}
		
		if(bsv.getMad_zsl()!=0){
			
			db_mtmp = getZkzsl(jiessl, bsv.getMad_zsl(), bsv.getMad_zsldw(), bsv.getKoud_plm_ws());
			db_tmp = CustomMaths.add(db_tmp, db_mtmp);
			str_beiz = "Mad���ۿ��۽�����";
			
			if(bsv.getBeiz().indexOf(str_beiz)>-1){
//				˵��ǰ���Ѿ����� "�������ۿ��۽�����" �������Ӧ���ۼӿ���
//				�õ�֮ǰ�۵��������뱾�����������ۼ�
				db_shul = Double.parseDouble(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1
						,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
						(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��"))));
				
				db_mtmp = CustomMaths.add(db_mtmp, db_shul);
				bsv.setBeiz(bsv.getBeiz().replaceFirst(	
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)
								,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
								(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��")+1)),
								
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz),bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1)
						+db_mtmp+"��"					
						));
			}else{
				
				bsv.setBeiz(bsv.getBeiz()+" Mad���ۿ��۽����� "+db_mtmp+"��");
			}
		}
		
		if(bsv.getAar_zsl()!=0){
			
			db_mtmp = getZkzsl(jiessl, bsv.getAar_zsl(), bsv.getAar_zsldw(), bsv.getKoud_plm_ws());
			db_tmp = CustomMaths.add(db_tmp, db_mtmp);
			str_beiz = "Aar���ۿ��۽�����";
			
			if(bsv.getBeiz().indexOf(str_beiz)>-1){
//				˵��ǰ���Ѿ����� "�������ۿ��۽�����" �������Ӧ���ۼӿ���
//				�õ�֮ǰ�۵��������뱾�����������ۼ�
				db_shul = Double.parseDouble(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1
						,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
						(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��"))));
				
				db_mtmp = CustomMaths.add(db_mtmp, db_shul);
				bsv.setBeiz(bsv.getBeiz().replaceFirst(	
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)
								,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
								(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��")+1)),
								
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz),bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1)
						+db_mtmp+"��"					
						));
			}else{
				
				bsv.setBeiz(bsv.getBeiz()+" Aar���ۿ��۽����� "+db_mtmp+"��");
			}
		}
		
		if(bsv.getAad_zsl()!=0){
			
			db_mtmp = getZkzsl(jiessl, bsv.getAad_zsl(), bsv.getAad_zsldw(), bsv.getKoud_plm_ws());
			db_tmp = CustomMaths.add(db_tmp, db_mtmp);
			str_beiz = "Aad���ۿ��۽�����";
			
			if(bsv.getBeiz().indexOf(str_beiz)>-1){
//				˵��ǰ���Ѿ����� "�������ۿ��۽�����" �������Ӧ���ۼӿ���
//				�õ�֮ǰ�۵��������뱾�����������ۼ�
				db_shul = Double.parseDouble(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1
						,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
						(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��"))));
				
				db_mtmp = CustomMaths.add(db_mtmp, db_shul);
				bsv.setBeiz(bsv.getBeiz().replaceFirst(	
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)
								,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
								(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��")+1)),
								
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz),bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1)
						+db_mtmp+"��"					
						));
			}else{
				
				bsv.setBeiz(bsv.getBeiz()+" Aad���ۿ��۽����� "+db_mtmp+"��");
			}
		}
		
		if(bsv.getVad_zsl()!=0){
			
			db_mtmp = getZkzsl(jiessl, bsv.getVad_zsl(), bsv.getVad_zsldw(), bsv.getKoud_plm_ws());
			db_tmp = CustomMaths.add(db_tmp, db_mtmp);
			str_beiz = "Vad���ۿ��۽�����";
			
			if(bsv.getBeiz().indexOf(str_beiz)>-1){
//				˵��ǰ���Ѿ����� "�������ۿ��۽�����" �������Ӧ���ۼӿ���
//				�õ�֮ǰ�۵��������뱾�����������ۼ�
				db_shul = Double.parseDouble(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1
						,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
						(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��"))));
				
				db_mtmp = CustomMaths.add(db_mtmp, db_shul);
				bsv.setBeiz(bsv.getBeiz().replaceFirst(	
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)
								,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
								(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��")+1)),
								
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz),bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1)
						+db_mtmp+"��"					
						));
			}else{
				
				bsv.setBeiz(bsv.getBeiz()+" Vad���ۿ��۽����� "+db_mtmp+"��");
			}
		}
		
		if(bsv.getT2_zsl()!=0){
			
			db_mtmp = getZkzsl(jiessl, bsv.getT2_zsl(), bsv.getT2_zsldw(), bsv.getKoud_plm_ws());
			db_tmp = CustomMaths.add(db_tmp, db_mtmp);
			str_beiz = "T2���ۿ��۽�����";
			
			if(bsv.getBeiz().indexOf(str_beiz)>-1){
//				˵��ǰ���Ѿ����� "�������ۿ��۽�����" �������Ӧ���ۼӿ���
//				�õ�֮ǰ�۵��������뱾�����������ۼ�
				db_shul = Double.parseDouble(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1
						,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
						(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��"))));
				
				db_mtmp = CustomMaths.add(db_mtmp, db_shul);
				bsv.setBeiz(bsv.getBeiz().replaceFirst(	
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)
								,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
								(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��")+1)),
								
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz),bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1)
						+db_mtmp+"��"					
						));
			}else{
				
				bsv.setBeiz(bsv.getBeiz()+" T2���ۿ��۽����� "+db_mtmp+"��");
			}
		}
		
		if(bsv.getYunju_zsl()!=0){
			
			db_mtmp = getZkzsl(jiessl, bsv.getYunju_zsl(), bsv.getYunju_zsldw(), bsv.getKoud_plm_ws());
			db_tmp = CustomMaths.add(db_tmp, db_mtmp);
			str_beiz = "�˾����ۿ��۽�����";
			
			if(bsv.getBeiz().indexOf(str_beiz)>-1){
//				˵��ǰ���Ѿ����� "�������ۿ��۽�����" �������Ӧ���ۼӿ���
//				�õ�֮ǰ�۵��������뱾�����������ۼ�
				db_shul = Double.parseDouble(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1
						,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
						(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��"))));
				
				db_mtmp = CustomMaths.add(db_mtmp, db_shul);
				bsv.setBeiz(bsv.getBeiz().replaceFirst(	
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)
								,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
								(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��")+1)),
								
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz),bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1)
						+db_mtmp+"��"					
						));
			}else{
				
				bsv.setBeiz(bsv.getBeiz()+" �˾����ۿ��۽����� "+db_mtmp+"��");
			}
		}
		
		if(bsv.getStar_zsl()!=0){
			
			db_mtmp = getZkzsl(jiessl, bsv.getStar_zsl(), bsv.getStar_zsldw(), bsv.getKoud_plm_ws());
			db_tmp = CustomMaths.add(db_tmp, db_mtmp);
			str_beiz = "Star���ۿ��۽�����";
			
			if(bsv.getBeiz().indexOf(str_beiz)>-1){
//				˵��ǰ���Ѿ����� "�������ۿ��۽�����" �������Ӧ���ۼӿ���
//				�õ�֮ǰ�۵��������뱾�����������ۼ�
				db_shul = Double.parseDouble(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1
						,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
						(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��"))));
				
				db_mtmp = CustomMaths.add(db_mtmp, db_shul);
				bsv.setBeiz(bsv.getBeiz().replaceFirst(	
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)
								,bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1+
								(bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1).indexOf("��")+1)),
								
						bsv.getBeiz().substring(bsv.getBeiz().indexOf(str_beiz),bsv.getBeiz().indexOf(str_beiz)+str_beiz.length()+1)
						+db_mtmp+"��"					
						));
			}else{
				
				bsv.setBeiz(bsv.getBeiz()+" Star���ۿ��۽����� "+db_mtmp+"��");
			}
		}
		
		return CustomMaths.Round_new(db_tmp, bsv.getKoud_plm_ws());
	}
	
	public static double getZkzsl(double Jiessl,double Zhesl,String Zhejdw,int ws){
//		����������
//		�������ܣ�
//			Ϊ�˵õ����ۿ��������ľ���������
//		�����߼���
//				���ۿ����������ĵ�λ�С��֡�����%�֡� ����Ƕ֣���ֱ�ӷ��أ������"%��" 
//			��Ҫ������������˷��󷵻ء�
//		�����βΣ�
//			jiessl ����������	Zhesl ������,	Zhejdw �ۼ۵�λ
		
		if(Zhejdw.equals(Locale.baifbd_danw)){
//			����ۼ۵�λΪ"%��"
			
			Zhesl = CustomMaths.mul(Jiessl, CustomMaths.div(Zhesl,100));
		}
		if ("�����������".equals(MainGlobal.getXitxx_item("����", "�����������", "0", ""))) {
			return CustomMaths.Round(Zhesl, ws);
		}else{
			return CustomMaths.Round_new(Zhesl, ws);
		}
		
	}
	
	public static boolean checkZbzkksl(Balances_variable bsv,String Zhibbm){
//		�������ܣ�
//			���ĳһ��ָ������ۿ��Ƿ�Ϊ��������
//		�����߼���
//				�����ָ�����������������򷵻�true�����򷵻�false
//		�����βΣ�
//			Zhibbm ָ�����
		boolean Flag = false;
		
		if(Zhibbm.equals(Locale.jiessl_zhibb)){
			
			if(bsv.getShul_zsl()!=0){
				
				Flag = true;
			}
		}
		
		if(Zhibbm.equals(Locale.Qnetar_zhibb)){
			
			if(bsv.getQnetar_zsl()!=0){
				
				Flag = true;
			}
		}
		
		if(Zhibbm.equals(Locale.Std_zhibb)){
			
			if(bsv.getStd_zsl()!=0){
				
				Flag = true;
			}
		}
		
		if(Zhibbm.equals(Locale.Ad_zhibb)){
			
			if(bsv.getAd_zsl()!=0){
				
				Flag = true;
			}
		}
		
		if(Zhibbm.equals(Locale.Vdaf_zhibb)){
			
			if(bsv.getVdaf_zsl()!=0){
				
				Flag = true;
			}
		}
		
		if(Zhibbm.equals(Locale.Mt_zhibb)){
			
			if(bsv.getMt_zsl()!=0){
				
				Flag = true;
			}
		}
		
		if(Zhibbm.equals(Locale.Qgrad_zhibb)){
			
			if(bsv.getQgrad_zsl()!=0){
				
				Flag = true;
			}
		}
		
		if(Zhibbm.equals(Locale.Qbad_zhibb)){
			
			if(bsv.getQbad_zsl()!=0){
				
				Flag = true;
			}
		}
		
		if(Zhibbm.equals(Locale.Had_zhibb)){
			
			if(bsv.getHad_zsl()!=0){
				
				Flag = true;
			}
		}
		
		if(Zhibbm.equals(Locale.Stad_zhibb)){
			
			if(bsv.getStad_zsl()!=0){
				
				Flag = true;
			}
		}
		
		if(Zhibbm.equals(Locale.Mad_zhibb)){
			
			if(bsv.getMad_zsl()!=0){
				
				Flag = true;
			}
		}
		
		if(Zhibbm.equals(Locale.Aar_zhibb)){
			
			if(bsv.getAar_zsl()!=0){
				
				Flag = true;
			}
		}
		
		if(Zhibbm.equals(Locale.Aad_zhibb)){
			
			if(bsv.getAad_zsl()!=0){
				
				Flag = true;
			}
		}
		
		if(Zhibbm.equals(Locale.Vad_zhibb)){
			
			if(bsv.getVad_zsl()!=0){
				
				Flag = true;
			}
		}
		
		if(Zhibbm.equals(Locale.T2_zhibb)){
			
			if(bsv.getT2_zsl()!=0){
				
				Flag = true;
			}
		}
		
		if(Zhibbm.equals(Locale.Yunju_zhibb)){
			
			if(bsv.getYunju_zsl()!=0){
				
				Flag = true;
			}
		}
		
		if(Zhibbm.equals(Locale.Star_zhibb)){
			
			if(bsv.getStar_zsl()!=0){
				
				Flag = true;
			}
		}
		
		return Flag; 
	}
	
	public static void setJieszbtscl_Jqpj_Flag(Balances_variable bsv,StringBuffer sb){
		
//		�������ܣ�
//				Ϊ��Ҫ���⴦���ָ�꣨�ڵ����ν��������£���Щָ����Ҫ��Ȩƽ��������ϵͳ��ʶ����ʶ��ָ����Ҫ���⴦��
//		�����߼���
//				�Ȳ���sb�ж�Ӧ��ָ�꣬Ȼ����getjiesslzlΪ��ָ���static״̬��ֵ
//		�����βΣ�
//				sb ָ�����ϼ�
		if(sb.length()>0){
			
			String Zhibbm[] = sb.toString().split(",");
			
			for(int i=0;i<Zhibbm.length;i++){
				
				setZhib_info(bsv, Zhibbm[i], "static_status", "true");
			}
		}
	}
	
	public static void setJieszbtscl_Jqpj_value(Balances_variable bsv,StringBuffer sb){
		
//		�������ܣ�
//				Ϊ��Ҫ���⴦���ָ�꣨�ڵ����ν��������£���Щָ����Ҫ��Ȩƽ������ֵ��
//		�����߼���
//				�Ȳ���sb�ж�Ӧ��ָ�꣬Ȼ����getjiesslzlΪ��ָ���static״̬��ֵ
//		�����βΣ�
//				sb ָ�����ϼ�
		if(sb.length()>0){
			
			String Zhibbm[] = sb.toString().split(",");
			
			for(int i=0;i<Zhibbm.length;i++){
				
				setZhib_info(bsv, Zhibbm[i], "static_value", "js");
			}
		}
	}
	
	public static void setZhib_info(Balances_variable bsv,String zhibbm, String item, String value){
//		�������ܣ�
//			Ϊ Balances_variable ���������Ԫ�ظ�ֵ
//			2010-05-06	���پ��� ����Ǽ۸��ǵ����ν��㣬
//				���ۿ��е�����ָ���Ǽ�Ȩƽ��ʱ��
//					��������ָ��ġ�static_value������static_status����ֵ��
		
		if(zhibbm.equals(Locale.jiessl_zhibb)){
			
			if(item.equals("static_status")){
				
				if(value.trim().equals("true")
						||value.trim().equals("false")){
					
					bsv.setShul_static_status(Boolean.valueOf(value).booleanValue());
				}
			}else if(item.equals("static_value")){
				
				if(value.equals("js")){
					
					bsv.setShul_static_value(bsv.getJiessl());
				}
			}
			
		}else if(zhibbm.equals(Locale.Qnetar_zhibb)){
			
			if(item.equals("static_status")){
				
				if(value.trim().equals("true")
						||value.trim().equals("false")){
					
					bsv.setQnetar_static_status(Boolean.valueOf(value).booleanValue());
				}
			}else if(item.equals("static_value")){
				
				if(value.equals("js")){
					
					bsv.setQnetar_static_value(bsv.getQnetar_js());
				}
			}
			
		}else if(zhibbm.equals(Locale.Std_zhibb)){
			
			if(item.equals("static_status")){
				
				if(value.trim().equals("true")
						||value.trim().equals("false")){
					
					bsv.setStd_static_status(Boolean.valueOf(value).booleanValue());
				}
			}else if(item.equals("static_value")){
				
				if(value.equals("js")){
					
					bsv.setStd_static_value(bsv.getStd_js());
				}
			}
			
		}else if(zhibbm.equals(Locale.Ad_zhibb)){
			
			if(item.equals("static_status")){
				
				if(value.trim().equals("true")
						||value.trim().equals("false")){
					
					bsv.setAd_static_status(Boolean.valueOf(value).booleanValue());
				}
			}else if(item.equals("static_value")){
				
				if(value.equals("js")){
					
					bsv.setAd_static_value(bsv.getAd_js());
				}
			}
			
		}else if(zhibbm.equals(Locale.Vdaf_zhibb)){
			
			if(item.equals("static_status")){
				
				if(value.trim().equals("true")
						||value.trim().equals("false")){
					
					bsv.setVdaf_static_status(Boolean.valueOf(value).booleanValue());
				}
			}else if(item.equals("static_value")){
				
				if(value.equals("js")){
					
					bsv.setVdaf_static_value(bsv.getVdaf_js());
				}
			}
			
		}else if(zhibbm.equals(Locale.Mt_zhibb)){
			
			if(item.equals("static_status")){
				
				if(value.trim().equals("true")
						||value.trim().equals("false")){
					
					bsv.setMt_static_status(Boolean.valueOf(value).booleanValue());
				}
			}else if(item.equals("static_value")){
				
				if(value.equals("js")){
					
					bsv.setMt_static_value(bsv.getMt_js());
				}
			}

		}else if(zhibbm.equals(Locale.Qgrad_zhibb)){
			
			if(item.equals("static_status")){
				
				if(value.trim().equals("true")
						||value.trim().equals("false")){
					
					bsv.setQgrad_static_status(Boolean.valueOf(value).booleanValue());
				}
			}else if(item.equals("static_value")){
				
				if(value.equals("js")){
					
					bsv.setQgrad_static_value(bsv.getQgrad_js());
				}
			}
			
		}else if(zhibbm.equals(Locale.Qbad_zhibb)){
			
			if(item.equals("static_status")){
				
				if(value.trim().equals("true")
						||value.trim().equals("false")){
					
					bsv.setQbad_static_status(Boolean.valueOf(value).booleanValue());
				}
			}else if(item.equals("static_value")){
				
				if(value.equals("js")){
					
					bsv.setQbad_static_value(bsv.getQbad_js());
				}
			}
			
		}else if(zhibbm.equals(Locale.Had_zhibb)){
			
			if(item.equals("static_status")){
				
				if(value.trim().equals("true")
						||value.trim().equals("false")){
					
					bsv.setHad_static_status(Boolean.valueOf(value).booleanValue());
				}
			}else if(item.equals("static_value")){
				
				if(value.equals("js")){
					
					bsv.setHad_static_value(bsv.getHad_js());
				}
			}
			
		}else if(zhibbm.equals(Locale.Stad_zhibb)){
			
			if(item.equals("static_status")){
				
				if(value.trim().equals("true")
						||value.trim().equals("false")){
					
					bsv.setStad_static_status(Boolean.valueOf(value).booleanValue());
				}
			}else if(item.equals("static_value")){
				
				if(value.equals("js")){
					
					bsv.setStad_static_value(bsv.getStad_js());
				}
			}
			
		}else if(zhibbm.equals(Locale.Mad_zhibb)){
			
			if(item.equals("static_status")){
				
				if(value.trim().equals("true")
						||value.trim().equals("false")){
					
					bsv.setMad_static_status(Boolean.valueOf(value).booleanValue());
				}
			}else if(item.equals("static_value")){
				
				if(value.equals("js")){
					
					bsv.setMad_static_value(bsv.getMad_js());
				}
			}
			
		}else if(zhibbm.equals(Locale.Aar_zhibb)){
			
			if(item.equals("static_status")){
				
				if(value.trim().equals("true")
						||value.trim().equals("false")){
					
					bsv.setAar_static_status(Boolean.valueOf(value).booleanValue());
				}
			}else if(item.equals("static_value")){
				
				if(value.equals("js")){
					
					bsv.setAar_static_value(bsv.getAar_js());
				}
			}
			
		}else if(zhibbm.equals(Locale.Aad_zhibb)){
			
			if(item.equals("static_status")){
				
				if(value.trim().equals("true")
						||value.trim().equals("false")){
					
					bsv.setAad_static_status(Boolean.valueOf(value).booleanValue());
				}
			}else if(item.equals("static_value")){
				
				if(value.equals("js")){
					
					bsv.setAad_static_value(bsv.getAad_js());
				}
			}
			
		}else if(zhibbm.equals(Locale.Vad_zhibb)){
			
			if(item.equals("static_status")){
				
				if(value.trim().equals("true")
						||value.trim().equals("false")){
					
					bsv.setVad_static_status(Boolean.valueOf(value).booleanValue());
				}
			}else if(item.equals("static_value")){
				
				if(value.equals("js")){
					
					bsv.setVad_static_value(bsv.getVad_js());
				}
			}
			
		}else if(zhibbm.equals(Locale.T2_zhibb)){
			
			if(item.equals("static_status")){
				
				if(value.trim().equals("true")
						||value.trim().equals("false")){
					
					bsv.setT2_static_status(Boolean.valueOf(value).booleanValue());
				}
			}else if(item.equals("static_value")){
				
				if(value.equals("js")){
					
					bsv.setT2_static_value(bsv.getT2_js());
				}
			}
			
		}else if(zhibbm.equals(Locale.Yunju_zhibb)){
			
			if(item.equals("static_status")){
				
				if(value.trim().equals("true")
						||value.trim().equals("false")){
					
					bsv.setYunju_static_status(Boolean.valueOf(value).booleanValue());
				}
			}else if(item.equals("static_value")){
				
				if(value.equals("js")){
					
					bsv.setYunju_static_value(bsv.getYunju_js());
				}
			}
			
		}else if(zhibbm.equals(Locale.Star_zhibb)){
			
			if(item.equals("static_status")){
				
				if(value.trim().equals("true")
						||value.trim().equals("false")){
					
					bsv.setStar_static_status(Boolean.valueOf(value).booleanValue());
				}
			}else if(item.equals("static_value")){
				
				if(value.equals("js")){
					
					bsv.setStar_static_value(bsv.getStar_js());
				}
			}
		}
	}
	
	public static String[] getFenzzfc(String Value, String Fengf, int Buc){
		
//		�õ������ַ���������������
//		�������ܣ�
//			��sql ����е�����ʹ����in ʱ�������������������һǧoracle�ᱨ���˺������ڽ������������ֳ����ɸ��飬Ϊ��������Ĵ���
//		�����߼���
//			���ַ�������ʶ�ֳ����飬��������������
//		�����βΣ�
//			Value ����ֵ��ַ�����Fengf �ָ�����Buc ���鲽����
		
		String strTmpArray[]=null;		//�м����飬Ϊ�˴���sql ����д���1000����������⣬���������ǰ�����м�������зָ�
		StringBuffer sbTmpWhere=null;  	//�м��������
		String strFinalArray[]=null;	//���շ��ص�ֵ
		int x,i=0;						//x:����Ŀ�ﵽ����ʱ������Ŀ����
		
		strTmpArray=Value.split(Fengf);
		if(strTmpArray.length>Buc){
//			�ﵽ1000����Ŀ������
			sbTmpWhere = new StringBuffer("");
			
			for(x=1;x<=Math.ceil((double)strTmpArray.length/Buc);x++){
				
				for(i=x*Buc-Buc;i<x*Buc;i++){
					
					if(i>=strTmpArray.length){
						
						break;
					}
					sbTmpWhere.append(strTmpArray[i]).append(",");
				}
				sbTmpWhere.deleteCharAt(sbTmpWhere.length()-1);
				sbTmpWhere.append(";");
			}
			
			strFinalArray=sbTmpWhere.toString().split(";");		//�õ����շ���ֵ
		}
		return strFinalArray;
	}
	
	public static void SetZhib_het_clear(Balances_variable bsv){
		
//		�������ܣ�
//				�ڵ����ν����У���������Ϊ��Ȩƽ��ָ�����ʱ�����������еĵ��������ۿ��
//			���������Ȩ������ʱ��Ҫ�Ƚ�����ָ��� het ��׼����������ա�
//		�����߼���
//			��� Balances_variable �еĺ�ͬ����
//		�����βΣ�
//			bsv	Balances_variable�����ʵ��
		
		bsv.setShul_ht("");
		bsv.setShul_zsl(0);
		bsv.setQnetar_ht("");
		bsv.setQnetar_zsl(0);
		bsv.setStd_ht("");
		bsv.setStd_zsl(0);
		bsv.setAd_ht("");
		bsv.setAd_zsl(0);
		bsv.setVdaf_ht("");
		bsv.setVdaf_zsl(0);
		bsv.setMt_ht("");
		bsv.setMt_zsl(0);
		bsv.setQgrad_ht("");
		bsv.setQgrad_zsl(0);
		bsv.setQbad_ht("");
		bsv.setQbad_zsl(0);
		bsv.setHad_ht("");
		bsv.setHad_zsl(0);
		bsv.setStad_ht("");
		bsv.setStad_zsl(0);
		bsv.setStar_ht("");
		bsv.setStar_zsl(0);
		bsv.setMad_ht("");
		bsv.setMad_zsl(0);
		bsv.setAar_ht("");
		bsv.setAar_zsl(0);
		bsv.setAad_ht("");
		bsv.setAad_zsl(0);
		bsv.setVad_ht("");
		bsv.setVad_zsl(0);
		bsv.setT2_ht("");
		bsv.setT2_zsl(0);
		bsv.setYunju_ht("");
		bsv.setYunju_zsl(0);
	}
	
	public static void UpdateFahShuiftzl(String Sids){
		JDBCcon con = new JDBCcon();
		String [] ids=Sids.split(",");
		for(int i=0; i<ids.length; i++){
			long id = Long.parseLong(ids[i]);
			String sql ="{call setFahshuiftzl("+id+")}";
			con.UpdateCall(sql);
		}
		
		
		
		
		con.Close();
	}
	
}