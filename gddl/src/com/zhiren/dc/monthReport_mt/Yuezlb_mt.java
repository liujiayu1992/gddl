package com.zhiren.dc.monthReport_mt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者:刘雨
 * 时间:2010-07-06
 * 描述:马头质量月报维护
 */


public class Yuezlb_mt extends BasePage implements PageValidateListener {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		
		 
		
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		int flag ;
		while(rsl.next()){
			String sql = 
				"select\n" +
				"max(ljid) id,\n" +
				"sum(laimslby) laimslby,\n" + 
				"sum(laimsllj) laimsllj,\n" + 
				"sum(laimsllj - laimslby) laimsl,\n" + 
				"sum(qnet_ar_lj-qnet_ar) qnet_ar,\n" + 
				"sum(qnet_ar_kflj-qnet_ar_kf) qnet_ar_kf,\n" + 
				"sum(mt_kflj-mt_kf) mt_kf,\n" + 
				"sum(mad_kflj-mad_kf) mad_kf,\n" + 
				"sum(aar_kflj-aar_kf) aar_kf,\n" + 
				"sum(vdaf_kflj-vdaf_kf) vdaf_kf,\n" + 
				"sum(std_kflj-std_kf) std_kf,\n" + 
				"sum(had_kflj-had_kf) had_kf,\n" + 
				"sum(fcad_kflj-fcad_kf) fcad_kf,\n" + 
				"sum(zhijbfmllj-zhijbfml) zhijbfml,\n" + 
				"sum(zhijbfjelj-zhijbfje) zhijbfje,\n" + 
				
				"sum(zhijbfjemlj-zhijbfjem) zhijbfjemlj,\n" + 
				"sum(zhijbfjealj-zhijbfjea) zhijbfjealj,\n" + 
				"sum(zhijbfjevlj-zhijbfjev) zhijbfjevlj,\n" + 
				"sum(zhijbfjeqlj-zhijbfjeq) zhijbfjeqlj,\n" + 
				"sum(zhijbfjeslj-zhijbfjes) zhijbfjeslj,\n" + 
				"sum(zhijbfjetlj-zhijbfjet) zhijbfjetlj,\n" + 
				
				"sum(suopjelj-suopje) suopje,\n" + 
				"sum(lsuopsllj-lsuopsl) lsuopsl,\n" + 
				"sum(lsuopjelj-lsuopje) lsuopje\n" + 
				" from\n" + 
				"(select\n" + 
				"case when s.fenx = '累计' then z.id end ljid,\n" +
				"nvl(case when s.fenx = '本月' then s.laimsl end,0) laimslby,\n" + 
				"nvl(case when s.fenx = '累计' then s.laimsl end,0) laimsllj,\n" + 
				"nvl(case when s.fenx = '本月' then z.qnet_ar*s.laimsl end,0) qnet_ar,\n" + 
				"nvl(case when s.fenx = '本月' then z.qnet_ar_kf*s.laimsl end,0) qnet_ar_kf,\n" + 
				"nvl(case when s.fenx = '本月' then z.mt_kf*s.laimsl end,0) mt_kf,\n" + 
				"nvl(case when s.fenx = '本月' then z.mad_kf*s.laimsl end,0) mad_kf,\n" + 
				"nvl(case when s.fenx = '本月' then z.aar_kf*s.laimsl end,0) aar_kf,\n" + 
				"nvl(case when s.fenx = '本月' then z.vdaf_kf*s.laimsl end,0) vdaf_kf,\n" + 
				"nvl(case when s.fenx = '本月' then z.std_kf*s.laimsl end,0) std_kf,\n" + 
				"nvl(case when s.fenx = '本月' then z.had_kf*s.laimsl end,0) had_kf,\n" + 
				"nvl(case when s.fenx = '本月' then z.fcad_kf*s.laimsl end,0) fcad_kf,\n" + 
				"nvl(case when s.fenx = '本月' then z.zhijbfml end,0) zhijbfml,\n" + 
				"nvl(case when s.fenx = '本月' then z.zhijbfje end,0) zhijbfje,\n" + 
				
				"nvl(case when s.fenx = '本月' then z.ZHIJBFJE_M end,0) zhijbfjem,\n" + 
				"nvl(case when s.fenx = '本月' then z.ZHIJBFJE_A end,0) zhijbfjea,\n" + 
				"nvl(case when s.fenx = '本月' then z.ZHIJBFJE_V end,0) zhijbfjev,\n" + 
				"nvl(case when s.fenx = '本月' then z.ZHIJBFJE_Q end,0) zhijbfjeq,\n" + 
				"nvl(case when s.fenx = '本月' then z.ZHIJBFJE_S end,0) zhijbfjes,\n" + 
				"nvl(case when s.fenx = '本月' then z.ZHIJBFJE_T end,0) zhijbfjet,\n" + 
				
				"nvl(case when s.fenx = '本月' then z.suopje end,0) suopje,\n" + 
				"nvl(case when s.fenx = '本月' then z.lsuopsl end,0) lsuopsl,\n" + 
				"nvl(case when s.fenx = '本月' then z.lsuopje end,0) lsuopje,\n" + 
				"nvl(case when s.fenx = '累计' then z.qnet_ar*s.laimsl end,0) qnet_ar_lj,\n" + 
				"nvl(case when s.fenx = '累计' then z.qnet_ar_kf*s.laimsl end,0) qnet_ar_kflj,\n" + 
				"nvl(case when s.fenx = '累计' then z.mt_kf*s.laimsl end,0) mt_kflj,\n" + 
				"nvl(case when s.fenx = '累计' then z.mad_kf*s.laimsl end,0) mad_kflj,\n" + 
				"nvl(case when s.fenx = '累计' then z.aar_kf*s.laimsl end,0) aar_kflj,\n" + 
				"nvl(case when s.fenx = '累计' then z.vdaf_kf*s.laimsl end,0) vdaf_kflj,\n" + 
				"nvl(case when s.fenx = '累计' then z.std_kf*s.laimsl end,0) std_kflj,\n" + 
				"nvl(case when s.fenx = '累计' then z.had_kf*s.laimsl end,0) had_kflj,\n" + 
				"nvl(case when s.fenx = '累计' then z.fcad_kf*s.laimsl end,0) fcad_kflj,\n" + 
				"nvl(case when s.fenx = '累计' then z.zhijbfml end,0) zhijbfmllj,\n" + 
				"nvl(case when s.fenx = '累计' then z.zhijbfje end,0) zhijbfjelj,\n" + 
				"nvl(case when s.fenx = '累计' then z.ZHIJBFJE_M end,0) zhijbfjemlj,\n" + 
				"nvl(case when s.fenx = '累计' then z.ZHIJBFJE_A end,0) zhijbfjealj,\n" + 
				"nvl(case when s.fenx = '累计' then z.ZHIJBFJE_V end,0) zhijbfjevlj,\n" + 
				"nvl(case when s.fenx = '累计' then z.ZHIJBFJE_Q end,0) zhijbfjeqlj,\n" + 
				"nvl(case when s.fenx = '累计' then z.ZHIJBFJE_S end,0) zhijbfjeslj,\n" + 
				"nvl(case when s.fenx = '累计' then z.ZHIJBFJE_T end,0) zhijbfjetlj,\n" + 
				"nvl(case when s.fenx = '累计' then z.suopje end,0) suopjelj,\n" + 
				"nvl(case when s.fenx = '累计' then z.lsuopsl end,0) lsuopsllj,\n" + 
				"nvl(case when s.fenx = '累计' then z.lsuopje end,0) lsuopjelj\n" + 
				"from yueslb s,yuezlb z,yuetjkjb_mt t\n" + 
				"where z.yuetjkjb_id = t.id and s.yuetjkjb_id = t.id\n" + 
				"and s.fenx = z.fenx and t.id = "+rsl.getLong("yuetjkjb_id")+")";
			ResultSetList rs= con.getResultSetList(sql);
			if(rs.next() && rs.getDouble("laimsllj")!=0){
				sql = "update yuezlb set " +
				"qnet_ar = round((" + rs.getDouble("qnet_ar")+ " +" + 
				rsl.getDouble("qnet_ar") + "*" + rs.getDouble("laimslby") + 
				")/" + rs.getDouble("laimsllj") + ",2),\n" + 
				"qnet_ar_kf = round((" + rs.getDouble("qnet_ar_kf")+ " +" + 
				rsl.getDouble("qnet_ar_kf") + "*" + rs.getDouble("laimslby") + 
				")/" + rs.getDouble("laimsllj") + ",2),\n" + 
				"mt_kf = round((" + rs.getDouble("mt_kf")+ " +" + 
				rsl.getDouble("mt_kf") + "*" + rs.getDouble("laimslby") + 
				")/" + rs.getDouble("laimsllj") + ",1),\n" + 
				"mad_kf = round((" + rs.getDouble("mad_kf")+ " +" + 
				rsl.getDouble("mad_kf") + "*" + rs.getDouble("laimslby") + 
				")/" + rs.getDouble("laimsllj") + ",2),\n" + 
				"aar_kf = round((" + rs.getDouble("aar_kf")+ " +" + 
				rsl.getDouble("aar_kf") + "*" + rs.getDouble("laimslby") + 
				")/" + rs.getDouble("laimsllj") + ",2),\n" + 
				"vdaf_kf = round((" + rs.getDouble("vdaf_kf")+ " +" + 
				rsl.getDouble("vdaf_kf") + "*" + rs.getDouble("laimslby") + 
				")/" + rs.getDouble("laimsllj") + ",2),\n" + 
				"std_kf = round((" + rs.getDouble("std_kf")+ " +" + 
				rsl.getDouble("std_kf") + "*" + rs.getDouble("laimslby") + 
				")/" + rs.getDouble("laimsllj") + ",2),\n" + 
				"had_kf = round((" + rs.getDouble("had_kf")+ " +" + 
				rsl.getDouble("had_kf") + "*" + rs.getDouble("laimslby") + 
				")/" + rs.getDouble("laimsllj") + ",2),\n" + 
				"fcad_kf = round((" + rs.getDouble("fcad_kf")+ " +" + 
				rsl.getDouble("fcad_kf") + "*" + rs.getDouble("laimslby") + 
				")/" + rs.getDouble("laimsllj") + ",2),\n" + 
				"zhijbfml = round(" + rs.getDouble("zhijbfml")+ " +" + 
				rsl.getDouble("zhijbfml")  +  ",0),\n" + 
				"zhijbfje = round(" + rs.getDouble("zhijbfje")+ " +" + 
				rsl.getDouble("zhijbfje")  +  ",0),\n" ;
				
				
				sql+="ZHIJBFJE_M = round(" + rs.getDouble("zhijbfjemlj")+ " +" + 
				rsl.getDouble("ZHIJBFJE_M")  +  ",0),\n" +
				"ZHIJBFJE_A = round(" + rs.getDouble("zhijbfjealj")+ " +" + 
				rsl.getDouble("ZHIJBFJE_A")  +  ",0),\n"+
				"ZHIJBFJE_V = round(" + rs.getDouble("zhijbfjevlj")+ " +" + 
				rsl.getDouble("ZHIJBFJE_V")  +  ",0),\n"+
				"ZHIJBFJE_Q = round(" + rs.getDouble("zhijbfjeqlj")+ " +" + 
				rsl.getDouble("ZHIJBFJE_Q")  +  ",0),\n"+
				"ZHIJBFJE_S = round(" + rs.getDouble("zhijbfjeslj")+ " +" + 
				rsl.getDouble("ZHIJBFJE_S")  +  ",0),\n"+
				"ZHIJBFJE_T = round(" + rs.getDouble("zhijbfjetlj")+ " +" + 
				rsl.getDouble("ZHIJBFJE_T")  +  ",0),\n";
				
				
				sql+="suopje = round(" + rs.getDouble("suopje")+ " +" + 
					rsl.getDouble("suopje")  +  ",0),\n" ;
				
				
				
				sql+="lsuopsl = round(" + rs.getDouble("lsuopsl")+ " +" + 
				rsl.getDouble("lsuopsl")  +  ",0),\n" + 
				"lsuopje = round(" + rs.getDouble("lsuopje")+ " +" + 
				rsl.getDouble("lsuopje")  +  ",0)\n" + 
				"where id =" + rs.getLong("id");
				flag = con.getUpdate(sql);
				if(flag == -1){
					WriteLog.writeErrorLog(this.getClass().getName() + "\n"
							+ ErrorMessage.UpdateYuezlbFailed + "\nSQL:" + sql);
					setMsg(ErrorMessage.UpdateYuezlbFailed);
					con.rollBack();
					con.Close();
					return;
				}
			}
			rs.close();
			sql = "update yuezlb set " +
			"qnet_ar = round(" + rsl.getDouble("qnet_ar") + ",2),\n" +
			"qnet_ar_kf = round(" + rsl.getDouble("qnet_ar_kf") + ",2),\n" +
			"mt_kf = round(" + rsl.getDouble("mt_kf") + ",2),\n" +
			"mad_kf = round(" + rsl.getDouble("mad_kf") + ",2),\n" +
			"aar_kf = round(" + rsl.getDouble("aar_kf") + ",2),\n" +
			"vdaf_kf = round(" + rsl.getDouble("vdaf_kf") + ",2),\n" +
			"std_kf = round(" + rsl.getDouble("std_kf") + ",2),\n" +
			"had_kf = round(" + rsl.getDouble("had_kf") + ",2),\n" +
			"fcad_kf = round(" + rsl.getDouble("fcad_kf") + ",2),\n" +
			"zhijbfml = round(" + rsl.getDouble("zhijbfml") + ",0),\n" +
			"zhijbfje = round(" + rsl.getDouble("zhijbfje") + ",0),\n" +
			"suopje = round(" + rsl.getDouble("suopje") + ",0),\n" +
			"lsuopsl = round(" + rsl.getDouble("lsuopsl") + ",0),\n" +
			"lsuopje = round(" + rsl.getDouble("lsuopje") + ",0),\n" +
			
			"ZHIJBFJE_M = round(" + rsl.getDouble("ZHIJBFJE_M") + ",0),\n" +
			"ZHIJBFJE_A = round(" + rsl.getDouble("ZHIJBFJE_A") + ",0),\n" +
			"ZHIJBFJE_V = round(" + rsl.getDouble("ZHIJBFJE_V") + ",0),\n" +
			"ZHIJBFJE_Q = round(" + rsl.getDouble("ZHIJBFJE_Q") + ",0),\n" +
			"ZHIJBFJE_S = round(" + rsl.getDouble("ZHIJBFJE_S") + ",0),\n" +
			"ZHIJBFJE_T = round(" + rsl.getDouble("ZHIJBFJE_T") + ",0)\n" +
			
			" where id =" + rsl.getLong("id");
			flag = con.getUpdate(sql);
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.UpdateYuezlbFailed + "\nSQL:" + sql);
				setMsg(ErrorMessage.UpdateYuezlbFailed);
				con.rollBack();
				con.Close();
				return;
			}
		}
		rsl.close();
		con.commit();
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	
	private boolean _CreateClick = false;
	
	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	private boolean _DelClick = false;
	
	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
		}
		
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		}
		getSelectData();
	}
	public void DelData() {
		JDBCcon con = new JDBCcon();
		String CurrZnDate=getNianf()+"年"+getYuef()+"月";
		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
		String strSql=
			"delete from yuezlb where yuetjkjb_id in (select id from yuetjkjb_mt where riq="
			+CurrODate+" and diancxxb_id="+getTreeid()+")";
		int flag = con.getDelete(strSql);
		if(flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail+"SQL:"+strSql);
			setMsg("删除过程中发生错误！");
		}else {
			setMsg(CurrZnDate+"的数据被成功删除！");
		}
		con.Close();
	}
	public String InsertYuezlbSql(long id, long yuetjkjb_id, String fenx, double Qnetar,
			double Qbad, double Mt, double Aar, double Vdaf, double Std, double Had,
			double Mad, double Aad, double Ad, double Vad, double Var, double Stad,
			double Sdaf, double Hdaf, double Qgrad, double Qgrad_daf, double Fcad,
			double kQnetar, double kQbad, double kMt, double kAar, double kVdaf, 
			double kStd, double kHad, double kMad, double kAad, double kAd, double kVad, 
			double kVar, double kStad, double kSdaf, double kHdaf, double kQgrad, 
			double kQgrad_daf, double kFcad, double zhijbfml, double zhijbfje, 
			double suopje, double liuspsl, double liuspje,double ZHIJBFJE_M,double ZHIJBFJE_A,
			double ZHIJBFJE_V,double ZHIJBFJE_Q,double ZHIJBFJE_S,double ZHIJBFJE_T
			){
		String sql = "insert into yuezlb (id,yuetjkjb_id,fenx,qnet_ar,qbad,mt,aar," +
				"vdaf,std,had,mad,aad,ad,vad,var,stad,sdaf,hdaf,qbrad,qgrad_daf," +
				"fcad,qnet_ar_kf,qbad_kf,mt_kf,aar_kf,vdaf_kf,std_kf,had_kf,mad_kf," +
				"aad_kf,ad_kf,vad_kf,var_kf,stad_kf,sdaf_kf,hdaf_kf,qbrad_kf," +
				"qgrad_daf_kf,fcad_kf,zhijbfml,zhijbfje,suopje,lsuopsl,lsuopje,ZHIJBFJE_M,ZHIJBFJE_A,ZHIJBFJE_V,ZHIJBFJE_Q,ZHIJBFJE_S,ZHIJBFJE_T) values(" +
				id+ "," + yuetjkjb_id + ",'" + fenx + "'," + Qnetar + "," + Qbad +
				"," + Mt + "," + Aar + "," + Vdaf + "," + Std + "," + Had + "," +
				Mad + "," + Aad + "," + Ad + "," + Vad + "," + Var + "," + Stad +
				"," + Sdaf + "," + Hdaf + "," + Qgrad + "," + Qgrad_daf + "," +
				Fcad + "," + kQnetar + "," + kQbad + "," + kMt + "," + kAar + "," + 
				kVdaf + "," + kStd + "," + kHad + "," + kMad + "," + kAad + "," + 
				kAd + "," + kVad + "," + kVar + "," + kStad + "," + kSdaf + "," + 
				kHdaf + "," + kQgrad + "," + kQgrad_daf + "," + kFcad + "," + 
				zhijbfml + "," + zhijbfje + "," + suopje + "," + liuspsl + "," + 
				liuspje +","+ZHIJBFJE_M+","+ZHIJBFJE_A+","+ZHIJBFJE_V+","+ZHIJBFJE_Q+","+ZHIJBFJE_S+","+ZHIJBFJE_T+ ")";
		return sql;
	}
	public void CreateData() {
		String diancxxb_id = getTreeid();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String CurrZnDate=getNianf()+"年"+getYuef()+"月";
		Date cd = DateUtil.getDate(getNianf() + "-"
				+ getYuef() + "-01");
		String CurrODate = DateUtil.FormatOracleDate(cd);
		int intYuef = Integer.parseInt(getYuef());
		String LastODate = DateUtil.FormatOracleDate(DateUtil
				.AddDate(cd, -1, DateUtil.AddType_intMonth));
		int flag;
		long lngId=0;
		String date_c = MainGlobal.getXitxx_item("月报", "月报取数日期差", diancxxb_id, "0");
		String sql = "delete from yuezlb where yuetjkjb_id in "
			+ "(select id from yuetjkjb_mt where riq = " + CurrODate
			+ " and diancxxb_id = " + diancxxb_id + ")";
		flag = con.getDelete(sql);
		if (flag == -1) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.DeleteYuezlbFailed + "\nSQL:" + sql);
			setMsg(ErrorMessage.DeleteYuezlbFailed);
			con.rollBack();
			con.Close();
			return;
		}
		
		
		
		sql = "select * from yuetjkjb_mt where riq = " + CurrODate + 
		" and diancxxb_id = " + diancxxb_id;
		ResultSetList rs = con.getResultSetList(sql);
		if (rs == null) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.NullResult + "引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			con.rollBack();
			con.Close();
			return;
		}
		
		String Zhijbf = MainGlobal.getXitxx_item("月报", "计算质价不符", String.valueOf(diancxxb_id), "否");
		
		while(rs.next()){
			lngId = rs.getLong("id");
			long gongysb_id = rs.getLong("gongysb_id");
			long jihkjb_id = rs.getLong("jihkjb_id");
			long pinzb_id = rs.getLong("pinzb_id");
			long yunsfsb_id = rs.getLong("yunsfsb_id");
			double suopje=0.0,liuspsl=0.0,liuspje=0.0,zhijbfml=0.0,zhijbfje=0.0,
			qnet_ar = 0.0,aar = 0.0,ad = 0.0,vdaf = 0.0,mt = 0.0,stad = 0.0,
			aad = 0.0,mad = 0.0,qbad = 0.0,had = 0.0,vad = 0.0,fcad = 0.0,std = 0.0,
			qgrad = 0.0,hdaf = 0.0,qgrad_daf = 0.0,sdaf = 0.0,var = 0.0,kqnet_ar = 0.0,
			kaar = 0.0,kad = 0.0,kvdaf = 0.0,kmt = 0.0,kstad = 0.0,kaad = 0.0,
			kmad = 0.0,kqbad = 0.0,khad = 0.0,kvad = 0.0,kfcad = 0.0,kstd = 0.0,
			kqgrad = 0.0,khdaf = 0.0,kqgrad_daf = 0.0,ksdaf = 0.0,kvar = 0.0,
			laimslby=0.0,laimsllj;
			String type=MainGlobal.getXitxx_item("结算", "结算单所属单位", String.valueOf(diancxxb_id), "ZGDT");
			String model = MainGlobal.getXitxx_item("月报", "月报单位", String
					.valueOf(diancxxb_id), "ZGDT");
			
			
			sql = 
				"select\n" +
				"a.dqid, a.jihkjb_id, a.pinzb_id, a.yunsfsb_id,sum(a.laimsl) laimsl,\n" + 
				"sum(zhijbfml) zhijbfml,sum(zhijbfje) zhijbfje," + 
				
				"sum(relzj_zj) relzj_zj,sum(liuzj_zj) liuzj_zj,sum(huifzj_zj) huifzj_zj,\n"+
				"sum(shuifzj_zj) shuifzj_zj,sum(huiffzj_zj) huiffzj_zj,sum(huirdzj_zj) huirdzj_zj,\n"+
				
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.qnet_ar)/sum(a.laimsl),2)) as qnet_ar,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.aar)/sum(a.laimsl),2)) as aar,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.ad)/sum(a.laimsl),2)) as ad,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.vdaf)/sum(a.laimsl),2)) as vdaf,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.mt)/sum(a.laimsl),1)) as mt,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.stad)/sum(a.laimsl),2)) as stad,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.aad)/sum(a.laimsl),2)) as aad,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.mad)/sum(a.laimsl),2)) as mad,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.qbad)/sum(a.laimsl),2)) as qbad,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.had)/sum(a.laimsl),2)) as had,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.vad)/sum(a.laimsl),2)) as vad,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.fcad)/sum(a.laimsl),2)) as fcad,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.std)/sum(a.laimsl),2)) as std,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.qgrad)/sum(a.laimsl),2)) as qgrad,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.hdaf)/sum(a.laimsl),2)) as hdaf,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.qgrad_daf)/sum(a.laimsl),2)) as qgrad_daf,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.sdaf)/sum(a.laimsl),2)) as sdaf,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.var)/sum(a.laimsl),2)) as var,\n" + 
				"\n" + 
/*
*huochaoyuan
*2009-10-22修改如果矿方化验值为空，则值取自厂方化验值
*/
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kqnet_ar,null,a.qnet_ar,a.kqnet_ar))/sum(a.laimsl),2)) as kqnet_ar,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kaar,null,a.aar,a.kaar))/sum(a.laimsl),2)) as kaar,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kad,null,a.ad,a.kad))/sum(a.laimsl),2)) as kad,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kvdaf,null,a.vdaf,a.kvdaf))/sum(a.laimsl),2)) as kvdaf,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kmt,null,a.mt,a.kmt))/sum(a.laimsl),1)) as kmt,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kstad,null,a.stad,a.kstad))/sum(a.laimsl),2)) as kstad,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kaad,null,a.aad,a.kaad))/sum(a.laimsl),2)) as kaad,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kmad,null,a.mad,a.kmad))/sum(a.laimsl),2)) as kmad,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kqbad,null,a.qbad,a.kqbad))/sum(a.laimsl),2)) as kqbad,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.khad,null,a.had,a.khad))/sum(a.laimsl),2)) as khad,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kvad,null,a.vad,a.kvad))/sum(a.laimsl),2)) as kvad,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kfcad,null,a.fcad,a.kfcad))/sum(a.laimsl),2)) as kfcad,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kstd,null,a.std,a.kstd))/sum(a.laimsl),2)) as kstd,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kqgrad,null,a.qgrad,a.kqgrad))/sum(a.laimsl),2)) as kqgrad,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.khdaf,null,a.hdaf,a.khdaf))/sum(a.laimsl),2)) as khdaf,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kqgrad_daf,null,a.qgrad_daf,a.kqgrad_daf))/sum(a.laimsl),2)) as kqgrad_daf,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.ksdaf,null,a.sdaf,a.ksdaf))/sum(a.laimsl),2)) as ksdaf,\n" + 
				"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kvar,null,a.var,a.kvar))/sum(a.laimsl),2)) as kvar\n" + 
//				end
				"from (select g.meikdq_id dqid, f.jihkjb_id, f.pinzb_id, f.yunsfsb_id,\n" + 
				"  f.laimsl,z.*,k.qnet_ar kqnet_ar, k.aar kaar, k.ad kad,\n" + 
				"  k.vdaf kvdaf, k.mt kmt, k.stad kstad, k.aad kaad,\n" + 
				"  k.mad kmad, k.qbad kqbad, k.had khad, k.vad kvad,\n" + 
				"  k.fcad kfcad, k.std kstd, k.qgrad kqgrad, k.hdaf khdaf,\n" + 
				"  k.qgrad_daf kqgrad_daf, k.sdaf ksdaf, k.var kvar,\n"; 
				
				if(Zhijbf.equals("否")){
					
					sql+= "  0 as	zhijbfml,	\n"	+
						"  0 as	zhijbfje, 	\n" +
						"  0 as relzj_zj	\n";
				}else if(Zhijbf.equals("是")){
					
					sql+="  nvl(case when (getgusxx(f.id,'hetzk') < 0) then f.biaoz end,0) zhijbfml,\n" + 
						"  abs(case when getgusxx(f.id,'hetzk') < 0 then getgusxx(f.id,'hetzk') * f.biaoz end) zhijbfje	\n" + 
						", abs(case when getgusxx(f.id,'hetzk') < 0 then getgusxx(f.id,'hetzk') * f.biaoz end) relzj_zj	\n";
				}
				
				sql+=",0 liuzj_zj\n"+
					",0 huifzj_zj\n"+
					",0 shuifzj_zj\n"+
					",0 huiffzj_zj\n"+
					",0 huirdzj_zj\n"+
					
					"from fahb f, zhilb z,kuangfzlb k, meikxxb g\n" + 
					"where f.zhilb_id = z.id \n" + 
					"and f.kuangfzlb_id = k.id(+) and f.meikxxb_id = g.id\n" + 
					"and f.diancxxb_id = " + diancxxb_id + " and g.meikdq_id =" + gongysb_id +
					" and f.jihkjb_id = " + jihkjb_id + " and f.pinzb_id =" + pinzb_id +
					" and f.daohrq >= " +CurrODate + "-" + date_c + "\n" +
					" and f.daohrq < add_months("+CurrODate+"-" + date_c + ",1)\n" +
					" and f.yunsfsb_id =" + yunsfsb_id +
					") a\n" + 
					"group by a.dqid, a.jihkjb_id, a.pinzb_id, a.yunsfsb_id\n" + 
					"";
			
			ResultSetList datars = con.getResultSetList(sql);
			
			double relzj_zj=0.0;double liuzj_zj=0.0;double huifzj_zj=0.0;
			double shuifzj_zj=0.0;double huiffzj_zj=0.0;double huirdzj_zj=0.0;
			
			if(datars.next()){
				zhijbfml = datars.getDouble("zhijbfml"); zhijbfje = datars.getDouble("zhijbfje");
				qnet_ar = datars.getDouble("qnet_ar"); kqnet_ar = datars.getDouble("kqnet_ar");
				aar = datars.getDouble("aar"); kaar = datars.getDouble("kaar");
				ad = datars.getDouble("ad"); kad = datars.getDouble("kad");
				vdaf = datars.getDouble("vdaf"); kvdaf = datars.getDouble("kvdaf");
				mt = datars.getDouble("mt"); kmt = datars.getDouble("kmt");
				stad = datars.getDouble("stad"); kstad = datars.getDouble("kstad");
				aad = datars.getDouble("aad"); kaad = datars.getDouble("kaad");
				mad = datars.getDouble("mad"); kmad = datars.getDouble("kmad");
				qbad = datars.getDouble("qbad"); kqbad = datars.getDouble("kqbad");
				had = datars.getDouble("had"); khad = datars.getDouble("khad");
				vad = datars.getDouble("vad"); kvad = datars.getDouble("kvad");
				fcad = datars.getDouble("fcad"); kfcad = datars.getDouble("kfcad");
				std = datars.getDouble("std"); kstd = datars.getDouble("kstd");
				qgrad = datars.getDouble("qgrad"); kqgrad = datars.getDouble("kqgrad");
				hdaf = datars.getDouble("hdaf"); khdaf = datars.getDouble("khdaf");
				qgrad_daf = datars.getDouble("qgrad_daf"); kqgrad_daf = datars.getDouble("kqgrad_daf");
				sdaf = datars.getDouble("sdaf"); ksdaf = datars.getDouble("ksdaf");
				var = datars.getDouble("var"); kvar = datars.getDouble("kvar");
				laimslby = datars.getDouble("laimsl");
				
				relzj_zj=datars.getDouble("relzj_zj");
				liuzj_zj=datars.getDouble("liuzj_zj");
				huifzj_zj=datars.getDouble("huifzj_zj");
				shuifzj_zj=datars.getDouble("shuifzj_zj");
				huiffzj_zj=datars.getDouble("huiffzj_zj");
				huirdzj_zj=datars.getDouble("huirdzj_zj");
				
			}
			datars.close();
			sql =
				"select nvl(sum(relzjje + quanszjje + huifzjje + huiffzjje),0) zhejje,\n" +
				"round(nvl(sum(liuspsl),0),0) liuspsl,nvl(sum(liuspje),0) liuspje\n" + 
				"from (select j.id,\n" + 
				"nvl(sum(case when z.bianm = 'Qnetar' and s.zhejje <0 then abs(s.zhejje) end),0) relzjje,\n" + 
				"nvl(sum(case when z.bianm = 'Mt' and s.zhejje <0 then abs(s.zhejje) end),0) quanszjje,\n" + 
				"nvl(sum(case when z.bianm = 'Aar' and s.zhejje <0 then abs(s.zhejje) end),0) huifzjje,\n" + 
				"nvl(sum(case when z.bianm = 'Vdaf' and s.zhejje <0 then abs(s.zhejje) end),0) huiffzjje,\n" + 
				"nvl(sum(case when z.bianm = 'Std' and s.zhejje <0 then abs(j.jiessl) end),0) liuspsl,\n" + 
				"nvl(sum(case when z.bianm = 'Std' and s.zhejje <0 then abs(s.zhejje) end),0) liuspje\n" + 
				"from jiesb j, jieszbsjb s, zhibb z\n" + 
				"where j.id = s.jiesdid and s.zhibb_id = z.id\n" + 
				"and j.ruzrq >= " +CurrODate + "-"+date_c+"\n" + 
				"and j.ruzrq < add_months("+CurrODate+"-"+date_c+",1)\n" + 
				"group by j.id) js, (select distinct f.jiesb_id from fahb f, meikxxb g\n" + 
				"where f.meikxxb_id = g.id\n" + 
				" and f.diancxxb_id =" + diancxxb_id +
				" and g.meikdq_id = " + gongysb_id +
				" and f.jihkjb_id = " + jihkjb_id +
				" and f.yunsfsb_id = " + yunsfsb_id +
				" and f.pinzb_id = " + pinzb_id +
				" ) fh where fh.jiesb_id = js.id";
			datars = con.getResultSetList(sql);
			if(datars.next()){
				suopje = datars.getDouble("zhejje");
				liuspsl = datars.getDouble("liuspsl");
				liuspje = datars.getDouble("liuspje");
			}
			datars.close();
			sql = InsertYuezlbSql(Long.parseLong(MainGlobal.getNewID(Long.parseLong(diancxxb_id)
			)), lngId, SysConstant.Fenx_Beny,qnet_ar,qbad,mt,aar,vdaf,std,had,mad,
			aad,ad,vad,var,stad,sdaf,hdaf,qgrad,qgrad_daf,fcad,kqnet_ar,kqbad,kmt,
			kaar,kvdaf,kstd,khad,kmad,kaad,kad,kvad,kvar,kstad,ksdaf,khdaf,kqgrad,
			kqgrad_daf,kfcad,zhijbfml,zhijbfje,suopje,liuspsl,liuspje,shuifzj_zj,huifzj_zj,
			huiffzj_zj,relzj_zj,liuzj_zj,huirdzj_zj);
			flag = con.getInsert(sql);
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.InsertYuezlbFailed + "\nSQL:" + sql);
				setMsg(ErrorMessage.InsertYuezlbFailed);
				con.rollBack();
				con.Close();
				return;
			}
			
			sql = "select s.laimsl,zl.* from yuetjkjb_mt tj,yuezlb zl,yueslb s  " +
					"where s.yuetjkjb_id=tj.id and s.fenx = zl.fenx " +
					" and zl.yuetjkjb_id=tj.id and tj.riq="
				+ LastODate
				+ " and tj.diancxxb_id="
				+ diancxxb_id
				+ "\n"
				+ " and tj.gongysb_id="
				+ gongysb_id
				+ " and tj.pinzb_id="
				+ pinzb_id
				+ " and tj.jihkjb_id="
				+ jihkjb_id
				+ " and tj.yunsfsb_id="
				+ yunsfsb_id
				+ " and zl.fenx='累计'";
			datars = con.getResultSetList(sql);
			if (datars == null) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.NullResult + "\n引发错误SQL:" + sql);
				setMsg(ErrorMessage.NullResult);
				con.rollBack();
				con.Close();
				return;
			}
			if(datars.next() && intYuef != 1){
				zhijbfml = zhijbfml + datars.getDouble("zhijbfml"); 
				zhijbfje = zhijbfje + datars.getDouble("zhijbfje");
				
				shuifzj_zj = shuifzj_zj + datars.getDouble("ZHIJBFJE_M");
				huifzj_zj = huifzj_zj + datars.getDouble("ZHIJBFJE_A");
				huiffzj_zj = huiffzj_zj + datars.getDouble("ZHIJBFJE_V");
				relzj_zj = relzj_zj + datars.getDouble("ZHIJBFJE_Q");
				liuzj_zj = liuzj_zj + datars.getDouble("ZHIJBFJE_S");
				huirdzj_zj = huirdzj_zj + datars.getDouble("ZHIJBFJE_T");
				
				
				
				laimsllj = datars.getDouble("laimsl");
				if(laimslby + laimsllj != 0){
					qnet_ar = CustomMaths.Round_new(CustomMaths.div(qnet_ar*laimslby 
							+ datars.getDouble("qnet_ar")*laimsllj,laimslby+laimsllj), 2); 
					kqnet_ar = CustomMaths.Round_new(CustomMaths.div(kqnet_ar*laimslby 
							+ datars.getDouble("qnet_ar_kf")*laimsllj,laimslby+laimsllj), 2); 
					aar = CustomMaths.Round_new(CustomMaths.div(aar*laimslby 
							+ datars.getDouble("aar")*laimsllj,laimslby+laimsllj), 2); 
					kaar = CustomMaths.Round_new(CustomMaths.div(kaar*laimslby 
							+ datars.getDouble("aar_kf")*laimsllj,laimslby+laimsllj), 2); 
					ad = CustomMaths.Round_new(CustomMaths.div(ad*laimslby 
							+ datars.getDouble("ad")*laimsllj,laimslby+laimsllj), 2); 
					kad = CustomMaths.Round_new(CustomMaths.div(kad*laimslby 
							+ datars.getDouble("ad_kf")*laimsllj,laimslby+laimsllj), 2); 
					vdaf = CustomMaths.Round_new(CustomMaths.div(vdaf*laimslby 
							+ datars.getDouble("vdaf")*laimsllj,laimslby+laimsllj), 2); 
/**
*huochaoyuan
*2009-10-11 修改取数错误的bug，原来为"vad_kf",改为"vdaf_kf"
*/
						
					kvdaf = CustomMaths.Round_new(CustomMaths.div(kvdaf*laimslby 
							+ datars.getDouble("vdaf_kf")*laimsllj,laimslby+laimsllj), 2); 
//end					
					mt = CustomMaths.Round_new(CustomMaths.div(mt*laimslby 
							+ datars.getDouble("mt")*laimsllj,laimslby+laimsllj), 1); 
					kmt = CustomMaths.Round_new(CustomMaths.div(kmt*laimslby 
							+ datars.getDouble("mt_kf")*laimsllj,laimslby+laimsllj), 1); 
					stad = CustomMaths.Round_new(CustomMaths.div(stad*laimslby 
							+ datars.getDouble("stad")*laimsllj,laimslby+laimsllj), 2); 
					kstad = CustomMaths.Round_new(CustomMaths.div(kstad*laimslby 
							+ datars.getDouble("stad_kf")*laimsllj,laimslby+laimsllj), 2); 
					aad = CustomMaths.Round_new(CustomMaths.div(aad*laimslby 
							+ datars.getDouble("aad")*laimsllj,laimslby+laimsllj), 2); 
					kaad = CustomMaths.Round_new(CustomMaths.div(kaad*laimslby 
							+ datars.getDouble("aad_kf")*laimsllj,laimslby+laimsllj), 2); 
					mad = CustomMaths.Round_new(CustomMaths.div(mad*laimslby 
							+ datars.getDouble("mad")*laimsllj,laimslby+laimsllj), 2); 
					kmad = CustomMaths.Round_new(CustomMaths.div(kmad*laimslby 
							+ datars.getDouble("mad_kf")*laimsllj,laimslby+laimsllj), 2); 
					qbad = CustomMaths.Round_new(CustomMaths.div(qbad*laimslby 
							+ datars.getDouble("qbad")*laimsllj,laimslby+laimsllj), 2); 
					kqbad = CustomMaths.Round_new(CustomMaths.div(kqbad*laimslby 
							+ datars.getDouble("qbad_kf")*laimsllj,laimslby+laimsllj), 2); 
					had = CustomMaths.Round_new(CustomMaths.div(had*laimslby 
							+ datars.getDouble("had")*laimsllj,laimslby+laimsllj), 2); 
					khad = CustomMaths.Round_new(CustomMaths.div(khad*laimslby 
							+ datars.getDouble("had_kf")*laimsllj,laimslby+laimsllj), 2); 
					vad = CustomMaths.Round_new(CustomMaths.div(vad*laimslby 
							+ datars.getDouble("vad")*laimsllj,laimslby+laimsllj), 2); 
					kvad = CustomMaths.Round_new(CustomMaths.div(kvad*laimslby 
							+ datars.getDouble("vad_kf")*laimsllj,laimslby+laimsllj), 2); 
					fcad = CustomMaths.Round_new(CustomMaths.div(fcad*laimslby 
							+ datars.getDouble("fcad")*laimsllj,laimslby+laimsllj), 2);
					kfcad = CustomMaths.Round_new(CustomMaths.div(kfcad*laimslby 
							+ datars.getDouble("fcad_kf")*laimsllj,laimslby+laimsllj), 2);
					std = CustomMaths.Round_new(CustomMaths.div(std*laimslby 
							+ datars.getDouble("std")*laimsllj,laimslby+laimsllj), 2);
					kstd = CustomMaths.Round_new(CustomMaths.div(kstd*laimslby 
							+ datars.getDouble("std_kf")*laimsllj,laimslby+laimsllj), 2);
					qgrad = CustomMaths.Round_new(CustomMaths.div(qgrad*laimslby 
							+ datars.getDouble("qgrad")*laimsllj,laimslby+laimsllj), 2);
					kqgrad = CustomMaths.Round_new(CustomMaths.div(kqgrad*laimslby 
							+ datars.getDouble("qgrad_kf")*laimsllj,laimslby+laimsllj), 2);
					hdaf = CustomMaths.Round_new(CustomMaths.div(hdaf*laimslby 
							+ datars.getDouble("hdaf")*laimsllj,laimslby+laimsllj), 2);
					khdaf = CustomMaths.Round_new(CustomMaths.div(khdaf*laimslby 
							+ datars.getDouble("hdaf_kf")*laimsllj,laimslby+laimsllj), 2);
					qgrad_daf = CustomMaths.Round_new(CustomMaths.div(qgrad_daf*laimslby 
							+ datars.getDouble("qgrad_daf")*laimsllj,laimslby+laimsllj), 2);
					kqgrad_daf = CustomMaths.Round_new(CustomMaths.div(kqgrad_daf*laimslby 
							+ datars.getDouble("qgrad_daf_kf")*laimsllj,laimslby+laimsllj), 2);
					sdaf = CustomMaths.Round_new(CustomMaths.div(sdaf*laimslby 
							+ datars.getDouble("sdaf")*laimsllj,laimslby+laimsllj), 2);
					ksdaf = CustomMaths.Round_new(CustomMaths.div(ksdaf*laimslby 
							+ datars.getDouble("sdaf_kf")*laimsllj,laimslby+laimsllj), 2);
					var = CustomMaths.Round_new(CustomMaths.div(var*laimslby 
							+ datars.getDouble("var")*laimsllj,laimslby+laimsllj), 2);
					kvar = CustomMaths.Round_new(CustomMaths.div(kvar*laimslby 
							+ datars.getDouble("var_kf")*laimsllj,laimslby+laimsllj), 2);
					
					
						suopje = suopje + datars.getDouble("suopje");//suppje
					
					
					liuspsl = liuspsl + datars.getDouble("lsuopsl");
					liuspje = liuspje + datars.getDouble("lsuopje");
				}
			}
			sql = InsertYuezlbSql(Long.parseLong(MainGlobal.getNewID(Long.parseLong(diancxxb_id)
			)), lngId, SysConstant.Fenx_Leij,qnet_ar,qbad,mt,aar,vdaf,std,had,mad,
			aad,ad,vad,var,stad,sdaf,hdaf,qgrad,qgrad_daf,fcad,kqnet_ar,kqbad,kmt,
			kaar,kvdaf,kstd,khad,kmad,kaad,kad,kvad,kvar,kstad,ksdaf,khdaf,kqgrad,
			kqgrad_daf,kfcad,zhijbfml,zhijbfje,suopje,liuspsl,liuspje,shuifzj_zj,huifzj_zj,
			huiffzj_zj,relzj_zj,liuzj_zj,huirdzj_zj);
			flag = con.getInsert(sql);
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.InsertYuezlbFailed + "\nSQL:" + sql);
				setMsg(ErrorMessage.InsertYuezlbFailed);
				con.rollBack();
				con.Close();
				return;
			}
		}
		rs.close();
		
		if (intYuef!=1){//取上个月有这个月没有的数据，本月值为0，累计值等与上个月累计
			sql = "select * from (select distinct y.id,y.gongysb_id,y.jihkjb_id,y.pinzb_id,\n"
				+ "y.yunsfsb_id,nvl(z.yuetjkjb_id,-1) yuetjkj\n"
				+ "from yuetjkjb_mt y, yuezlb z\n"
				+ "where y.id = z.yuetjkjb_id(+)\n"
				+ "and z.fenx(+) = '"
				+ SysConstant.Fenx_Beny
				+ "'\n"
				+ "and y.riq = "
				+ CurrODate
				+ "\n"
				+ "and y.diancxxb_id = "
				+ diancxxb_id
				+ ")\n" + "where yuetjkj = -1";
			
			ResultSetList rssylj=con.getResultSetList(sql);
			if(rssylj == null) {
				WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sql);
				setMsg(ErrorMessage.NullResult);
				con.rollBack();
				con.Close();
				return;
			}
			
			while (rssylj.next()){
				sql = "select y.id yid,z.* from yuetjkjb_mt y,yuezlb z where y.id = z.yuetjkjb_id(+) "
					+ " and z.fenx(+) = '"
					+ SysConstant.Fenx_Leij
					+ "'"
					+ " and y.riq="
					+ LastODate
					+ " and y.diancxxb_id="
					+ diancxxb_id
					+ "\n"
					+ " and y.gongysb_id="
					+ rssylj.getLong("gongysb_id")
					+ " and y.pinzb_id="
					+ rssylj.getLong("pinzb_id")
					+ " and y.jihkjb_id="
					+ rssylj.getLong("jihkjb_id")
					+ " and y.yunsfsb_id="
					+ rssylj.getLong("yunsfsb_id");
				ResultSetList recby = con.getResultSetList(sql);
				if (recby == null) {
					WriteLog.writeErrorLog(this.getClass().getName() + "\n"
							+ ErrorMessage.NullResult + "\n引发错误SQL:" + sql);
					setMsg(ErrorMessage.NullResult);
					con.rollBack();
					con.Close();
					return;
				}
				if (recby.next()) {
					sql = InsertYuezlbSql(Long.parseLong(MainGlobal.getNewID(Long.parseLong(diancxxb_id)
					)), lngId, SysConstant.Fenx_Beny,0,0,0,0,0,0,0,0,
					0,0,0,0,0,0,0,0,0,0,0,0,0,
					0,0,0,0,0,0,0,0,0,0,0,0,0,
					0,0,0,0,0,0,0,0,0,0,0,0,0);
					flag = con.getInsert(sql);
					if (flag == -1) {
						WriteLog.writeErrorLog(this.getClass().getName() + "\n"
								+ ErrorMessage.InsertYuezlbFailed + "\nSQL:" + sql);
						setMsg(ErrorMessage.InsertYuezlbFailed);
						con.rollBack();
						con.Close();
						return;
					}
					
				
					sql = InsertYuezlbSql(Long.parseLong(MainGlobal.getNewID(Long.parseLong(diancxxb_id)
					)), lngId, SysConstant.Fenx_Leij,recby.getDouble("qnet_ar"),
					recby.getDouble("qbad"),recby.getDouble("mt"),recby.getDouble("aar"),
					recby.getDouble("vdaf"),recby.getDouble("std"),recby.getDouble("had"),
					recby.getDouble("mad"),recby.getDouble("aad"),recby.getDouble("ad"),
					recby.getDouble("vad"),recby.getDouble("var"),recby.getDouble("stad"),
					recby.getDouble("sdaf"),recby.getDouble("hdaf"),recby.getDouble("qgrad"),
					recby.getDouble("qgrad_daf"),recby.getDouble("fcad"),recby.getDouble("kqnet_ar"),
					recby.getDouble("kqbad"),recby.getDouble("kmt"),recby.getDouble("kaar"),
					recby.getDouble("kvdaf"),recby.getDouble("kstd"),recby.getDouble("khad"),
					recby.getDouble("kmad"),recby.getDouble("kaad"),recby.getDouble("kad"),
					recby.getDouble("kvad"),recby.getDouble("kvar"),recby.getDouble("kstad"),
					recby.getDouble("ksdaf"),recby.getDouble("khdaf"),recby.getDouble("kqgrad"),
					recby.getDouble("kqgrad_daf"),recby.getDouble("kfcad"),recby.getDouble("zhijbfml"),
					recby.getDouble("zhijbfje"),recby.getDouble("suopje"),recby.getDouble("lsuopsl"),
					recby.getDouble("lsuopje"),
					recby.getDouble("ZHIJBFJE_M"),recby.getDouble("ZHIJBFJE_A"),recby.getDouble("ZHIJBFJE_V"),
					recby.getDouble("ZHIJBFJE_Q"),recby.getDouble("ZHIJBFJE_S"),recby.getDouble("ZHIJBFJE_T"));
					flag = con.getInsert(sql);
					if (flag == -1) {
						WriteLog.writeErrorLog(this.getClass().getName() + "\n"
								+ ErrorMessage.InsertYuezlbFailed + "\nSQL:"
								+ sql);
						setMsg(ErrorMessage.InsertYuezlbFailed);
						con.rollBack();
						con.Close();
						return;
					}
				}
				recby.close();
			}
			rssylj.close();
		}
		con.commit();
		con.Close();
		setMsg(CurrZnDate+"的数据成功生成！");
	}

	public void getSelectData() {
		String diancxxb_id = getTreeid();
		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
		String sql = 

			"select z.id, t.id yuetjkjb_id, g.mingc gongysb_id, m.mingc meikxxb_id, j.mingc jihkjb_id\n" +
			", p.mingc pinzb_id,y.mingc yunsfsb_id, fenx, qnet_ar, qbad, mt, mad, aar," +
			"		vdaf, std, had, fcad, qnet_ar_kf, qbad_kf, mt_kf, mad_kf," + 
			"		aar_kf, vdaf_kf, std_kf, had_kf, fcad_kf, zhijbfml,zhijbfje," +
			"		ZHIJBFJE_M,ZHIJBFJE_A,ZHIJBFJE_V,ZHIJBFJE_Q,ZHIJBFJE_S,ZHIJBFJE_T,"+
			"		suopje, lsuopsl, lsuopje\n" + 
			"from yuezlb z, yuetjkjb_mt t, gongysb g, pinzb p, yunsfsb y, jihkjb j,meikxxb m\n" + 
			"where t.id = z.yuetjkjb_id and t.gongysb_id = g.id and t.meikxxb_id = m.id\n" + 
			"and t.pinzb_id = p.id and t.jihkjb_id = j.id\n" + 
			"and t.yunsfsb_id = y.id and z.fenx = '"+SysConstant.Fenx_Beny+"'\n" + 
			"and t.diancxxb_id = "+diancxxb_id+" and t.riq =" + CurrODate ;

		JDBCcon con = new JDBCcon();
//		System.out.println(strSql);
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.NullResult + "引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		// //设置表名称用于保存
		egu.setTableName("yuezlb");
		// /设置显示列名称
		egu.setWidth("bodyWidth");
//		egu.setHeight("bodyHeight");
		egu.addPaging(0);
		//egu.getColumn("xuh").setHeader("序号");
		//egu.getColumn("xuh").setWidth(50);
		egu.getColumn("yuetjkjb_id").setHidden(true);
		egu.getColumn("gongysb_id").setCenterHeader("供货单位");
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("meikxxb_id").setCenterHeader("煤矿单位");
		egu.getColumn("meikxxb_id").setWidth(120);
		egu.getColumn("jihkjb_id").setCenterHeader("计划口径");
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("pinzb_id").setCenterHeader("品种");
		egu.getColumn("pinzb_id").setWidth(80);
		egu.getColumn("yunsfsb_id").setCenterHeader("运输方式");
		egu.getColumn("yunsfsb_id").setWidth(80);
		egu.getColumn("fenx").setCenterHeader("分项");
		egu.getColumn("fenx").setWidth(60);
		//*******************本月**********************//
//		egu.getColumn("jingz").setCenterHeader("净重");
//		egu.getColumn("jingz").setWidth(60);
		egu.getColumn("qnet_ar").setCenterHeader("收到基低位热(Qnet,ar(Mj/kg))",6,2);
		egu.getColumn("qnet_ar").setWidth(90);
		egu.getColumn("qbad").setCenterHeader("弹筒热(Qbad)",3,2);
		egu.getColumn("qbad").setWidth(60);
		egu.getColumn("mt").setCenterHeader("全水(Mt)",2,2);
		egu.getColumn("mt").setWidth(60);
		egu.getColumn("mad").setCenterHeader("空干基水(Mad)",4,2);
		egu.getColumn("mad").setWidth(60);
		egu.getColumn("aar").setCenterHeader("收到基灰分(Aar)",5,2);
		egu.getColumn("aar").setWidth(60);
		egu.getColumn("vdaf").setCenterHeader("干燥无灰基挥发分(Vdaf)",8,2);
		egu.getColumn("vdaf").setWidth(60);
		egu.getColumn("std").setCenterHeader("干基硫(Std)",3,2);
		egu.getColumn("std").setWidth(60);
		egu.getColumn("had").setCenterHeader("空干基氢(Had)",4,2);
		egu.getColumn("had").setWidth(60);
		egu.getColumn("fcad").setCenterHeader("固定碳(Fcad)",3,2);
		egu.getColumn("fcad").setWidth(60);
		
		
		egu.getColumn("qnet_ar_KF").setCenterHeader("矿方低位热(Qnet,ar(Mj/kg))",5,2);
		egu.getColumn("qnet_ar_KF").setWidth(90);
		egu.getColumn("qbad_KF").setCenterHeader("矿方弹筒热(Qbad)",5,2);
		egu.getColumn("qbad_KF").setWidth(60);
		egu.getColumn("mt_KF").setCenterHeader("矿方全水(Mt)",4,2);
		egu.getColumn("mt_KF").setWidth(60);
		egu.getColumn("mad_KF").setCenterHeader("矿方空干基水(Mad)",6,2);
		egu.getColumn("mad_KF").setWidth(60);
		egu.getColumn("aar_KF").setCenterHeader("矿方收到基灰分(Aar)",7,2);
		egu.getColumn("aar_KF").setWidth(60);
		egu.getColumn("vdaf_KF").setCenterHeader("矿方挥发分(Vdaf)",5,2);
		egu.getColumn("vdaf_KF").setWidth(60);
		egu.getColumn("std_KF").setCenterHeader("矿方干基硫(Std)",5,2);
		egu.getColumn("std_KF").setWidth(60);
		egu.getColumn("had_KF").setCenterHeader("矿方空干基氢(Had)",6,2);
		egu.getColumn("had_KF").setWidth(60);
		egu.getColumn("fcad_KF").setCenterHeader("矿方固定碳(Fcad)",5,2);
		egu.getColumn("fcad_KF").setWidth(60);
		
		egu.getColumn("zhijbfml").setCenterHeader("质价不符煤量(吨)",4,2);
		egu.getColumn("zhijbfml").setWidth(60);
		egu.getColumn("zhijbfje").setCenterHeader("质价不符金额(元)",4,2);
		egu.getColumn("zhijbfje").setWidth(60);
		egu.getColumn("suopje").setCenterHeader("索赔金额(元)",2,2);
		egu.getColumn("suopje").setWidth(60);
		egu.getColumn("lsuopsl").setCenterHeader("硫索赔数量(吨)",3,2);
		egu.getColumn("lsuopsl").setWidth(60);
		egu.getColumn("lsuopje").setCenterHeader("硫索赔金额(元)",3,2);
		egu.getColumn("lsuopje").setWidth(60);
		
		
		egu.getColumn("ZHIJBFJE_M").setCenterHeader("质价不符水分(元)", 4, 2);
		egu.getColumn("ZHIJBFJE_A").setCenterHeader("质价不符灰分(元)", 4, 2);
		egu.getColumn("ZHIJBFJE_V").setCenterHeader("质价不符挥发分(元)", 4, 2);
		egu.getColumn("ZHIJBFJE_Q").setCenterHeader("质价不符热值(元)", 4, 2);
		egu.getColumn("ZHIJBFJE_S").setCenterHeader("质价不符硫分(元)", 4, 2);
		egu.getColumn("ZHIJBFJE_T").setCenterHeader("质价不符灰熔点(元)", 4, 2);
		
		egu.getColumn("ZHIJBFJE_M").setWidth(60);
		egu.getColumn("ZHIJBFJE_A").setWidth(60);
		egu.getColumn("ZHIJBFJE_V").setWidth(60);
		egu.getColumn("ZHIJBFJE_Q").setWidth(60);
		egu.getColumn("ZHIJBFJE_S").setWidth(60);
		egu.getColumn("ZHIJBFJE_T").setWidth(60);
		
//		egu.getColumn("ZHIJBFJE_M").setHidden(true);
//		egu.getColumn("ZHIJBFJE_A").setHidden(true);
//		egu.getColumn("ZHIJBFJE_V").setHidden(true);
//		egu.getColumn("ZHIJBFJE_M").setHidden(true);
//		egu.getColumn("ZHIJBFJE_S").setHidden(true);
//		egu.getColumn("ZHIJBFJE_T").setHidden(true);
		
		
//		egu.getColumn("ZHIJBFJE_M").setEditor(null);
//		egu.getColumn("ZHIJBFJE_A").setEditor(null);
//		egu.getColumn("ZHIJBFJE_V").setEditor(null);
//		egu.getColumn("ZHIJBFJE_Q").setEditor(null);
//		egu.getColumn("ZHIJBFJE_S").setEditor(null);
//		egu.getColumn("ZHIJBFJE_T").setEditor(null);
		
		
//		设定不可编辑列的颜色
		egu.getColumn("gongysb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("meikxxb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("jihkjb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("pinzb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("yunsfsb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("fenx").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("qnet_ar").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("qbad").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("mt").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("mad").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("aar").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("vdaf").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("std").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("had").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("fcad").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("jihkjb_id").setEditor(null);
		egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("yunsfsb_id").setEditor(null);
		egu.getColumn("fenx").setEditor(null);
		String sql1="select zhi from xitxxb where mingc='质量月报热值是否可编辑' and zhuangt=1";
		ResultSetList rs1 = con.getResultSetList(sql1);
		if(rs1.next()){
			
		}else{
			egu.getColumn("qnet_ar").setEditor(null);
		}
		egu.getColumn("qbad").setEditor(null);
		egu.getColumn("mt").setEditor(null);
		egu.getColumn("mad").setEditor(null);
		egu.getColumn("aar").setEditor(null);
		egu.getColumn("vdaf").setEditor(null);
		egu.getColumn("std").setEditor(null);
		egu.getColumn("had").setEditor(null);
		egu.getColumn("fcad").setEditor(null);
		
//		隐藏列设置
		egu.getColumn("qbad").setHidden(true);
		egu.getColumn("qbad_kf").setHidden(true);
		String Sql="select zhi from xitxxb x where x.leib='月报' and x.danw='质量' and beiz='使用'";
		ResultSetList rs = con.getResultSetList(Sql);
		if(rs != null) {
			while (rs.next()){
				String zhi = rs.getString("zhi");
				if(egu.getColByHeader(zhi)!=null){
					egu.getColByHeader(zhi).hidden=true;
				}
			}
		}
		egu.setDefaultsortable(false);   
		egu.getColumn("gongysb_id").update=false;
		egu.getColumn("meikxxb_id").update=false;
		egu.getColumn("jihkjb_id").update=false;
		egu.getColumn("pinzb_id").update=false;
		egu.getColumn("yunsfsb_id").update=false;
		egu.getColumn("fenx").update=false;
		
		// /设置按钮
		egu.addTbarText("年份");
		ComboBox comb1=new ComboBox();
		comb1.setWidth(50);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");//和自动刷新绑定
		comb1.setLazyRender(true);//动态绑定
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("月份");
		ComboBox comb2=new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");//和自动刷新绑定
		comb2.setLazyRender(true);//动态绑定
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());
		
		egu.addTbarText("-");// 设置分隔符
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符
		
//		判断数据是否被锁定
		boolean isLocked = isLocked(con);
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新'+Ext.getDom('NianfDropDown').value+'年'+Ext.getDom('YuefDropDown').value+'月的数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
//		生成按钮
		GridButton gbc = new GridButton("生成",getBtnHandlerScript("CreateButton"));
		if(isLocked) {
			gbc.setHandler("function (){"+MainGlobal.getExtMessageBox(ErrorMessage.DataLocked_Yueslb,false)+"return;}");
		}
		gbc.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbc);
//		删除按钮
		GridButton gbd = new GridButton("删除",getBtnHandlerScript("DelButton"));
		if(isLocked) {
			gbd.setHandler("function (){"+MainGlobal.getExtMessageBox(ErrorMessage.DataLocked_Yueslb,false)+"return;}");
		}
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbd);
//		保存按钮
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
//		打印按钮
		GridButton gbp = new GridButton("打印","function (){"+MainGlobal.getOpenWinScript("MonthReport&lx=yuezlb")+"}");
		gbp.setIcon(SysConstant.Btn_Icon_Print);
		egu.addTbarBtn(gbp);
		
		
		String type=MainGlobal.getXitxx_item("结算", "结算单所属单位", String.valueOf(diancxxb_id), "ZGDT");
		String model = MainGlobal.getXitxx_item("月报", "月报单位", String
				.valueOf(diancxxb_id), "ZGDT");
		 if(!type.equals("JZRD") && !model.equals("DTGJ")){
//			质价不符 不允许编辑
				egu.addOtherScript(" gridDiv_grid.addListener('beforeedit',function(e){" +
						"if(e.field=='ZHIJBFJE'){ e.cancel=true;}\n" +
						"\n} );\n");
				
				egu.addOtherScript(" gridDiv_grid.addListener('afteredit',function(e){" +
						
						"if(e.field=='ZHIJBFJE_M' || e.field=='ZHIJBFJE_A' ||e.field=='ZHIJBFJE_V' ||" +
						"e.field=='ZHIJBFJE_Q' ||e.field=='ZHIJBFJE_S' ||e.field=='ZHIJBFJE_T' )" +
						"{ var _val=parseFloat(e.record.get('ZHIJBFJE_M'))+parseFloat(e.record.get('ZHIJBFJE_A'))+parseFloat(e.record.get('ZHIJBFJE_V'))" +
						" +parseFloat(e.record.get('ZHIJBFJE_Q'))+parseFloat(e.record.get('ZHIJBFJE_S'))+parseFloat(e.record.get('ZHIJBFJE_T'));\n" +
						" e.record.set('ZHIJBFJE',_val);}\n" +
						
						"\n} );\n");
				
		 }else{
				egu.getColumn("ZHIJBFJE_M").setHidden(true);
				egu.getColumn("ZHIJBFJE_A").setHidden(true);
				egu.getColumn("ZHIJBFJE_V").setHidden(true);
				egu.getColumn("ZHIJBFJE_M").setHidden(true);
				egu.getColumn("ZHIJBFJE_S").setHidden(true);
				egu.getColumn("ZHIJBFJE_T").setHidden(true);
		 }
		
		
		
		
		
		
		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
//		按钮的script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianf()+"年"+getYuef()+"月";
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if(btnName.endsWith("CreateButton")) {
			btnsb.append("新生成数据将覆盖")
			.append(cnDate).append("的已存数据，是否继续？");
		}else {
			btnsb.append("是否删除").append(cnDate).append("的数据？");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){")
		.append("document.getElementById('").append(btnName).append("').click()")
		.append("}; // end if \n").append("});}");
		return btnsb.toString();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	public void pageValidate(PageEvent arg0) {
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setRiq();
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			getSelectData();
		}
	}
	
	public boolean isLocked(JDBCcon con) {
//		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
		return false;
	}
	
	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	
	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit()).getString2());
		if (intYuef<10){
			return "0"+intYuef;
		}else{
			return ((Visit) getPage().getVisit()).getString2();
		}
	}
	public void setYuef(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}
	
	public void setRiq() {
		setNianf(getNianfValue().getValue());
		setYuef(getYuefValue().getValue());
	}
	
	 // 年份下拉框
    private static IPropertySelectionModel _NianfModel;
    public IPropertySelectionModel getNianfModel() { 
        if (_NianfModel == null) {
            getNianfModels();
        }
        return _NianfModel;
    }
    
	private IDropDownBean _NianfValue;
	
    public IDropDownBean getNianfValue() {
        if (_NianfValue == null) {
            int _nianf = DateUtil.getYear(new Date());
            int _yuef = DateUtil.getMonth(new Date());
            if (_yuef == 1) {
                _nianf = _nianf - 1;
            }
            for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
                Object obj = getNianfModel().getOption(i);
                if (_nianf == ((IDropDownBean) obj).getId()) {
                    _NianfValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _NianfValue;
    }
	
    public void setNianfValue(IDropDownBean Value) {
    	if  (_NianfValue!=Value){
    		_NianfValue = Value;
    	}
    }

    public IPropertySelectionModel getNianfModels() {
        List listNianf = new ArrayList();
        int i;
        for (i = 2004; i <= DateUtil.getYear(new Date())+1; i++) {
            listNianf.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _NianfModel = new IDropDownModel(listNianf);
        return _NianfModel;
    }

    public void setNianfModel(IPropertySelectionModel _value) {
        _NianfModel = _value;
    }

	// 月份下拉框
	private static IPropertySelectionModel _YuefModel;
	
	public IPropertySelectionModel getYuefModel() {
	    if (_YuefModel == null) {
	        getYuefModels();
	    }
	    return _YuefModel;
	}
	
	private IDropDownBean _YuefValue;
	
	public IDropDownBean getYuefValue() {
	    if (_YuefValue == null) {
	        int _yuef = DateUtil.getMonth(new Date());
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
	        for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
	            Object obj = getYuefModel().getOption(i);
	            if (_yuef == ((IDropDownBean) obj).getId()) {
	                _YuefValue = (IDropDownBean) obj;
	                break;
	            }
	        }
	    }
	    return _YuefValue;
	}
	
	public void setYuefValue(IDropDownBean Value) {
    	if  (_YuefValue!=Value){
    		_YuefValue = Value;
    	}
	}

    public IPropertySelectionModel getYuefModels() {
        List listYuef = new ArrayList();
        for (int i = 1; i < 13; i++) {
            listYuef.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _YuefModel = new IDropDownModel(listYuef);
        return _YuefModel;
    }

    public void setYuefModel(IPropertySelectionModel _value) {
        _YuefModel = _value;
    }
    
    public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	public String getTreedcScript() {
		return getTree().getWindowTreeScript();
	}
}
