package com.zhiren.jt.jihgl.nianxqjhh;

import java.sql.ResultSet;
import java.sql.SQLException;
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

public class Nianxqjhh extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO 自动生成方法存根
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
		int flag =0;
//		 工具栏的年份和月份下拉框
		long intyear;
		long lastyear;
		long nextyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
			lastyear=intyear-1;
			nextyear=intyear+1;
		} else {
			intyear = getNianfValue().getId();
			lastyear=intyear-1;
			nextyear=intyear+1;
		}
		long intMonth;
		if (getYuefenValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefenValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {
 
			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		
		//
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange().replaceAll("&nbsp;", ""));//把getChange中的&nbsp;替换成"",否则ext不能识别
		
		String zhi_sn="";
		String zhi_jn="";
		String zhi_nn="";
		
		String zhi_sn1="";
		String zhi_jn1="";
		String zhi_nn1="";
		
		StringBuffer snsb = new StringBuffer();
		StringBuffer jnsb = new StringBuffer();
		StringBuffer nnsb = new StringBuffer();
		
		StringBuffer snsb1 = new StringBuffer();
		StringBuffer jnsb1 = new StringBuffer();
		StringBuffer nnsb1 = new StringBuffer();
		
		
		StringBuffer snsj = new StringBuffer();
		StringBuffer jnsj = new StringBuffer();
		StringBuffer nnsj = new StringBuffer();
		
		StringBuffer snsj1 = new StringBuffer();
		StringBuffer jnsj1 = new StringBuffer();
		StringBuffer nnsj1 = new StringBuffer();
		
		
		snsb.append("update nianxqjhh set ");
		jnsb.append("update nianxqjhh set ");
		nnsb.append("update nianxqjhh set ");
		
		snsb1.append("update nianxqjhh set ");
		jnsb1.append("update nianxqjhh set ");
		nnsb1.append("update nianxqjhh set ");
		
		while (rsl.next()) {
			if (rsl.getString("JIZLX").equals("新增机组")){
				
				switch(rsl.getInt("LANC")){
					case 1:
						zhi_sn1 = "fadl="+rsl.getDouble("lastyear");
						zhi_jn1 = "fadl="+rsl.getDouble("year");
						zhi_nn1 = "fadl="+rsl.getDouble("nextyear");
						break;
					case 2:
						zhi_sn1 = "fadbhm="+rsl.getDouble("lastyear");
						zhi_jn1 = "fadbhm="+rsl.getDouble("year");
						zhi_nn1 = "fadbhm="+rsl.getDouble("nextyear");
						break;
					case 3:
						zhi_sn1 = "zonghcydl="+rsl.getDouble("lastyear");
						zhi_jn1 = "zonghcydl="+rsl.getDouble("year");
						zhi_nn1 = "zonghcydl="+rsl.getDouble("nextyear");
						break;
					case 4:
						zhi_sn1 = "gongrl="+rsl.getDouble("lastyear");
						zhi_jn1 = "gongrl="+rsl.getDouble("year");
						zhi_nn1 = "gongrl="+rsl.getDouble("nextyear");
						break;
					case 5:
						zhi_sn1 = "gongrbzmh="+rsl.getDouble("lastyear");
						zhi_jn1 = "gongrbzmh="+rsl.getDouble("year");
						zhi_nn1 = "gongrbzmh="+rsl.getDouble("nextyear");
						break;
					case 6:
						zhi_sn1 = "rulrz="+rsl.getDouble("lastyear");
						zhi_jn1 = "rulrz="+rsl.getDouble("year");
						zhi_nn1 = "rulrz="+rsl.getDouble("nextyear");
						break;
					case 7:
						zhi_sn1 = "fadxybml="+rsl.getDouble("lastyear");
						zhi_jn1 = "fadxybml="+rsl.getDouble("year");
						zhi_nn1 = "fadxybml="+rsl.getDouble("nextyear");
						break;
					case 8:
						zhi_sn1 = "gongrxybml="+rsl.getDouble("lastyear");
						zhi_jn1 = "gongrxybml="+rsl.getDouble("year");
						zhi_nn1 = "gongrxybml="+rsl.getDouble("nextyear");
						break;
					case 9:
						zhi_sn1 = "xuybml="+rsl.getDouble("lastyear");
						zhi_jn1 = "xuybml="+rsl.getDouble("year");
						zhi_nn1 = "xuybml="+rsl.getDouble("nextyear");
						break;
					case 10:
						zhi_sn1 = "fadxyml="+rsl.getDouble("lastyear");
						zhi_jn1 = "fadxyml="+rsl.getDouble("year");
						zhi_nn1 = "fadxyml="+rsl.getDouble("nextyear");
						break;
					case 11:
						zhi_sn1 = "gongrxyml="+rsl.getDouble("lastyear");
						zhi_jn1 = "gongrxyml="+rsl.getDouble("year");
						zhi_nn1 = "gongrxyml="+rsl.getDouble("nextyear");
						break;
					case 12:
						zhi_sn1 = "xuyyml="+rsl.getDouble("lastyear");
						zhi_jn1 = "xuyyml="+rsl.getDouble("year");
						zhi_nn1 = "xuyyml="+rsl.getDouble("nextyear");
						break;
					case 13:
						zhi_sn1 = "dianhzryl="+rsl.getDouble("lastyear");
						zhi_jn1 = "dianhzryl="+rsl.getDouble("year");
						zhi_nn1 = "dianhzryl="+rsl.getDouble("nextyear");
						break;
					case 14:
						zhi_sn1 = "jisfdl="+rsl.getDouble("lastyear");
						zhi_jn1 = "jisfdl="+rsl.getDouble("year");
						zhi_nn1 = "jisfdl="+rsl.getDouble("nextyear");
						break;
					case 15:
						zhi_sn1 = "gongrbml="+rsl.getDouble("lastyear");
						zhi_jn1 = "gongrbml="+rsl.getDouble("year");
						zhi_nn1 = "gongrbml="+rsl.getDouble("nextyear");
						break;
					case 16:
						zhi_sn1 = "biaomh="+rsl.getDouble("lastyear");
						zhi_jn1 = "biaomh="+rsl.getDouble("year");
						zhi_nn1 = "biaomh="+rsl.getDouble("nextyear");
						break;
					default: break;
				}
				if(!zhi_sn1.equals("")){
					snsj1.append(zhi_sn1).append(",");
				}
				if(!zhi_jn1.equals("")){
					jnsj1.append(zhi_jn1).append(",");
				}
				if(!zhi_nn1.equals("")){
					nnsj1.append(zhi_nn1).append(",");
				}
				
			}
			if (rsl.getString("JIZLX").equals("现存机组")){
				switch(rsl.getInt("LANC")){
					case 1:
						zhi_sn = "fadl="+rsl.getDouble("lastyear");
						zhi_jn = "fadl="+rsl.getDouble("year");
						zhi_nn = "fadl="+rsl.getDouble("nextyear");
						break;
					case 2:
						zhi_sn = "fadbhm="+rsl.getDouble("lastyear");
						zhi_jn = "fadbhm="+rsl.getDouble("year");
						zhi_nn = "fadbhm="+rsl.getDouble("nextyear");
						break;
					case 3:
						zhi_sn = "zonghcydl="+rsl.getDouble("lastyear");
						zhi_jn = "zonghcydl="+rsl.getDouble("year");
						zhi_nn = "zonghcydl="+rsl.getDouble("nextyear");
						break;
					case 4:
						zhi_sn = "gongrl="+rsl.getDouble("lastyear");
						zhi_jn = "gongrl="+rsl.getDouble("year");
						zhi_nn = "gongrl="+rsl.getDouble("nextyear");
						break;
					case 5:
						zhi_sn = "gongrbzmh="+rsl.getDouble("lastyear");
						zhi_jn = "gongrbzmh="+rsl.getDouble("year");
						zhi_nn = "gongrbzmh="+rsl.getDouble("nextyear");
						break;
					case 6:
						zhi_sn = "rulrz="+rsl.getDouble("lastyear");
						zhi_jn = "rulrz="+rsl.getDouble("year");
						zhi_nn = "rulrz="+rsl.getDouble("nextyear");
						break;
					case 7:
						zhi_sn = "fadxybml="+rsl.getDouble("lastyear");
						zhi_jn = "fadxybml="+rsl.getDouble("year");
						zhi_nn = "fadxybml="+rsl.getDouble("nextyear");
						break;
					case 8:
						zhi_sn = "gongrxybml="+rsl.getDouble("lastyear");
						zhi_jn = "gongrxybml="+rsl.getDouble("year");
						zhi_nn = "gongrxybml="+rsl.getDouble("nextyear");
						break;
					case 9:
						zhi_sn = "xuybml="+rsl.getDouble("lastyear");
						zhi_jn = "xuybml="+rsl.getDouble("year");
						zhi_nn = "xuybml="+rsl.getDouble("nextyear");
						break;
					case 10:
						zhi_sn = "fadxyml="+rsl.getDouble("lastyear");
						zhi_jn = "fadxyml="+rsl.getDouble("year");
						zhi_nn = "fadxyml="+rsl.getDouble("nextyear");
						break;
					case 11:
						zhi_sn = "gongrxyml="+rsl.getDouble("lastyear");
						zhi_jn = "gongrxyml="+rsl.getDouble("year");
						zhi_nn = "gongrxyml="+rsl.getDouble("nextyear");
						break;
					case 12:
						zhi_sn = "xuyyml="+rsl.getDouble("lastyear");
						zhi_jn = "xuyyml="+rsl.getDouble("year");
						zhi_nn = "xuyyml="+rsl.getDouble("nextyear");
						break;
					case 13:
						zhi_sn = "dianhzryl="+rsl.getDouble("lastyear");
						zhi_jn = "dianhzryl="+rsl.getDouble("year");
						zhi_nn = "dianhzryl="+rsl.getDouble("nextyear");
						break;
					case 14:
						zhi_sn = "jisfdl="+rsl.getDouble("lastyear");
						zhi_jn = "jisfdl="+rsl.getDouble("year");
						zhi_nn = "jisfdl="+rsl.getDouble("nextyear");
						break;
					case 15:
						zhi_sn = "gongrbml="+rsl.getDouble("lastyear");
						zhi_jn = "gongrbml="+rsl.getDouble("year");
						zhi_nn = "gongrbml="+rsl.getDouble("nextyear");
						break;
					case 16:
						zhi_sn = "biaomh="+rsl.getDouble("lastyear");
						zhi_jn = "biaomh="+rsl.getDouble("year");
						zhi_nn = "biaomh="+rsl.getDouble("nextyear");
						break;
					default: break;
				}
				if(!zhi_sn.equals("")){
					snsj.append(zhi_sn).append(",");
				}
				if(!zhi_jn.equals("")){
					jnsj.append(zhi_jn).append(",");
				}
				if(!zhi_nn.equals("")){
					nnsj.append(zhi_nn).append(",");
				}
			}
			
		}
		long diancxxb_id=Long.parseLong(this.getTreeid());
		
		
		if(snsj.length()!=0){
			snsj.deleteCharAt(snsj.length()-1);
			snsb.append(snsj);
			snsb.append(" where ").append("nianf="+intyear+"").append(" and ");
			snsb.append(" diancxxb_id="+diancxxb_id+" and jizzt=0 and shujzt=").append(lastyear);
			flag=con.getUpdate(snsb.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
						+ snsb.toString());
				setMsg(ErrorMessage.InsertDatabaseFail);
				return;
			}
		}
		if(jnsj.length()!=0){
			jnsj.deleteCharAt(jnsj.length()-1);
			jnsb.append(jnsj);
			jnsb.append(" where ").append("nianf="+intyear+"").append(" and ");
			jnsb.append(" diancxxb_id="+diancxxb_id+" and jizzt=0 and shujzt=").append(intyear);
			flag=con.getUpdate(jnsb.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
						+ jnsb.toString());
				setMsg(ErrorMessage.InsertDatabaseFail);
				return;
			}
		}
		if(nnsj.length()!=0){
			nnsj.deleteCharAt(nnsj.length()-1);
			nnsb.append(nnsj);
			nnsb.append(" where ").append("nianf="+intyear+"").append(" and ");
			nnsb.append(" diancxxb_id="+diancxxb_id+" and jizzt=0 and shujzt=").append(nextyear);
			flag=con.getUpdate(nnsb.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
						+ nnsb.toString());
				setMsg(ErrorMessage.InsertDatabaseFail);
				return;
			}
		}
		if(snsj1.length()!=0){
			snsj1.deleteCharAt(snsj1.length()-1);
			snsb1.append(snsj1);
			snsb1.append(" where ").append("nianf="+intyear+"").append(" and ");
			snsb1.append(" diancxxb_id="+diancxxb_id+" and jizzt=1 and shujzt=").append(lastyear);
			flag=con.getUpdate(snsb1.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
						+ snsb1.toString());
				setMsg(ErrorMessage.InsertDatabaseFail);
				return;
			}
		}
		if(jnsj1.length()!=0){
			jnsj1.deleteCharAt(jnsj1.length()-1);
			jnsb1.append(jnsj1);
			jnsb1.append(" where ").append("nianf="+intyear+"").append(" and ");
			jnsb1.append(" diancxxb_id="+diancxxb_id+" and jizzt=1 and shujzt=").append(intyear);
			flag=con.getUpdate(jnsb1.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
						+ jnsb1.toString());
				setMsg(ErrorMessage.InsertDatabaseFail);
				return;
			}
		}
		if(nnsj1.length()!=0){
			nnsj1.deleteCharAt(nnsj1.length()-1);
			nnsb1.append(nnsj1);
			nnsb1.append(" where ").append("nianf="+intyear+"").append(" and ");
			nnsb1.append(" diancxxb_id="+diancxxb_id+" and jizzt=1 and shujzt=").append(nextyear);
			flag=con.getUpdate(nnsb1.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
						+ nnsb1.toString());
				setMsg(ErrorMessage.InsertDatabaseFail);
				return;
			}
		}
		if(flag!=-1){
			setMsg(ErrorMessage.SaveSuccessMessage);
		}
		con.commit();
		//---------------------
		con.Close();
		
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	private boolean _InsertButton = false;
	public void InsertButton(IRequestCycle cycle) {
		_InsertButton = true;
	}
	private boolean _DeleteButton = false;
	public void DeleteButton(IRequestCycle cycle) {
		_DeleteButton = true;
	}
	
	private boolean _ShenhClick = false;
	
	public void ShenhButton(IRequestCycle cycle) {
		_ShenhClick = true;
	}
	private boolean _ReturnClick = false;
	
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			
			getSelectData();
		}
		if(_InsertButton){
			_InsertButton=false;
			Insert();
			getSelectData();
		}
		if(_DeleteButton){
			_DeleteButton = false;
			Delete();
			getSelectData();
		}
		if (_ShenhClick) {
			_ShenhClick = false;
			Shenh();
			getSelectData();
		}
		if (_ReturnClick) {
			_ReturnClick = false;
			Return();
			getSelectData();
		}

	}
	public void Shenh(){				//	审核按钮
//		 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefenValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefenValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		//--------------------------------
		String CurrODate = DateUtil.FormatOracleDate(intyear+"-"+StrMonth+"-01");
		String chaxzt="";
		String zhuangt="";
		Visit visit = (Visit) getPage().getVisit();

		// ----------电厂树--------------
		String str = "";
			int treejib = this.getDiancTreeJib();
			if (treejib == 1) {// 选集团时刷新出所有的电厂
				str = "";
			} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else if (treejib == 3) {// 选电厂只刷新出该电厂
				str = "and dc.id = " + getTreeid() + "";
			}
		//查询数据时的状态
		if(visit.getRenyjb()==3){

		}else if(visit.getRenyjb()==2){
			chaxzt=" and y.zhuangt=1";
		}else if(visit.getRenyjb()==1){
			chaxzt=" and y.zhuangt=2";
		}
		//点击审核改变的状态
		if(visit.getRenyjb()==3){
			zhuangt="zhuangt=1"; 
		}else if(visit.getRenyjb()==2){
			zhuangt=" zhuangt=2";
		}
		
		JDBCcon con = new JDBCcon();
		String sqlcx=	"select y.* \n" + 
		" from nianxqjhh y,diancxxb dc\n" + 
		" where y.diancxxb_id=dc.id \n" + 
		"       and y.nianf="+intyear+chaxzt+" order by y.id";
		ResultSetList rsl = con.getResultSetList(sqlcx);
	
		StringBuffer sql = new StringBuffer();
		sql.append("begin \n");		
		while (rsl.next()) {
	
			sql.append("update nianxqjhh set "+zhuangt+ " where id=" + rsl.getLong("id")+";\n");
			

//			String tongjkj_id = "select distinct y.id as id from yuezbb y,diancxxb dc" +
//								" where  y.fenx='累计' and y.diancxxb_id=dc.id "+" and y.riq="+CurrODate;
//			ResultSet rs1 = con.getResultSet(tongjkj_id);
//		
//			StringBuffer sqllj =new StringBuffer();
//			try {
//				while (rs1.next()) {
//					 sqllj.append("begin \n");
//					 sqllj.append("update yuezbb set "+zhuangt+ " where id=" + rs1.getLong("id")+";\n");
//						 sqllj.append("end;");
//				 }
//			} catch (SQLException e) {
//				// TODO 自动生成 catch 块
//				e.printStackTrace();
//			}
//			 con.getUpdate(sqllj.toString());
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
		con.Close();
		setMsg("数据以上报上级单位，不可再对数据进行修改!");
	}

	public void Return(){
//		 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		//--------------------------------
		String CurrODate = DateUtil.FormatOracleDate(intyear+"-"+StrMonth+"-01");
		String chaxzt="";
		String zhuangt="";
		Visit visit = (Visit) getPage().getVisit();
		// ----------电厂树--------------
		String str = "";
			int treejib = this.getDiancTreeJib();
			if (treejib == 1) {// 选集团时刷新出所有的电厂
				str = "";
			} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else if (treejib == 3) {// 选电厂只刷新出该电厂
				str = "and dc.id = " + getTreeid() + "";
			}
		//查询数据时的状态
		if(visit.getRenyjb()==3){

		}else if(visit.getRenyjb()==2){
			chaxzt=" and y.zhuangt=1";
		}else if(visit.getRenyjb()==1){
			chaxzt=" and y.zhuangt=2";
		}
		//点击审核改变的状态
		if(visit.getRenyjb()==1){
			zhuangt="zhuangt=1";
		}else if(visit.getRenyjb()==2){
			zhuangt=" zhuangt=0";
		}

		JDBCcon con = new JDBCcon();
		String sqlcx=	"select y.* \n" + 
		" from nianxqjhh y,diancxxb dc\n" + 
		" where y.diancxxb_id=dc.id \n" + 
		"      and y.riq="+CurrODate+chaxzt+" order by y.id";
		ResultSetList rsl = con.getResultSetList(sqlcx);
	
		StringBuffer sql = new StringBuffer();
		int result = 0;
		sql.append("begin \n");		
		while (rsl.next()) {
			result++;
			sql.append("update nianxqjhh set "+zhuangt+ " where id=" + rsl.getLong("id")+";\n");
			
		}	
		sql.append("end;");
		if(result>0){
			con.getUpdate(sql.toString());
		}
//		con.getUpdate(sql.toString());
		
		con.Close();
	}
	public void Delete(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
//		 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefenValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefenValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		//删除本月和累计的数据
		//long id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
		long diancxxb_id=Long.parseLong(this.getTreeid());
		String DeleteStr="delete nianxqjhh y where y.nianf=" + intyear + " " +
				"and y.diancxxb_id="+diancxxb_id+"";
		con.getDelete(DeleteStr);
		con.Close();
	}
	public void Insert(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
//		 工具栏的年份和月份下拉框
		long intyear;
		long lastyear;
		long nextyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
			lastyear=intyear-1;
			nextyear=intyear+1;
		} else {
			intyear = getNianfValue().getId();
			lastyear=intyear-1;
			nextyear=intyear+1;
		}
		long intyuefen;
		if (getYuefenValue() == null) {
			intyuefen = DateUtil.getMonth(new Date());
		} else {
			intyuefen = getYuefenValue().getId();
		}
		//当月份是1的时候显示01,
		String Stryuefen="";
		if(intyuefen<10){
			
			Stryuefen="0"+intyuefen;
		}else{
			Stryuefen=""+intyuefen;
		}
		
		
		long id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
		long diancxxb_id=Long.parseLong(this.getTreeid());
		
		//上一年数据
		double fadl_sn=0;//上年发电量
		double fadbzmh_sn=0;//上年发电标准煤耗
		double gongrl_sn=0;//上年供热量
		double gongrbzmh_sn=0;//上年供热标准煤耗
		double rulmrz_sn=0;//上年入炉热值
		
		double jisfdl_sn=0;//计划发电
		double gongrbml_sn=0;//供热标煤量
		double biaomh_sn=0;//标煤耗
		
		//计算值
		double fadxybml_sn=0;//上年发电需用标煤量＝上年发电量×上年发电标准煤耗
		double gongrxybml_sn=0;//上年供热需用标煤量＝上年供热量×上年供热标准煤耗
		double xuybml_sn=0;//上年需用标煤量＝上年发电需用标煤量＋上年供热需用标煤量
		double fadxyml_sn=0;//上年发电需原煤量＝上年发电需用标煤量*29.271/上年入炉热值
		double gongrxyml_sn=0;//上年供热需原煤量＝上年供热需用标煤量*29.271/上年入炉热值
		double xuyyml_sn=0;//上年需用原煤量＝上年发电需原煤量＋上年供热需原煤量
		double dianhzryl_sn=0;//上年点火助燃油量
		
		//今年数据
		double fadl_jn=0;//今年发电量
		double gongrl_jn=0;//今年供热量
		double fadbzmh_jn=0;//今年发电标准煤耗
		double gongrbzmh_jn=0;//今年供热标准煤耗
		double rulmrz_jn=0;//今年入炉热值
		
		double jisfdl_jn=0;//计划发电
		double gongrbml_jn=0;//供热标煤量
		double biaomh_jn=0;//标煤耗
		
		//计算值－方式同上年计算方法
		double fadxybml_jn=0;//今年发电需用标煤量
		double gongrxybml_jn=0;//今年供热需用标煤量
		double xuybml_jn=0;//今年需用标煤量
		double fadxyml_jn=0;//今年发电需原煤量
		double gongrxyml_jn=0;//今年供热需原煤量
		double xuyyml_jn=0;//今年需用原煤量
		double dianhzryl_jn=0;//今年点火助燃油量

		String yuefen="12";
		if(getRbvalue()!=null && !getRbvalue().equals("")){
			if (Long.parseLong(getRbvalue())<10){
				yuefen="0"+Long.parseLong(getRbvalue());
			}else{
				yuefen=getRbvalue();
			}
			
		}
		//取得上年数据
		String sql_sn = "select yz.diancxxb_id,yz.fenx,yz.fadl,yz.gongrl,yz.fadbzmh,yz.gongrbzmh,yz.rultrmpjfrl from yuezbb yz \n" +//,yz.jisfdl,yz.gongrbml,yz.biaomh
				"where yz.riq=to_date('" + intyear + "-"+yuefen+"-01','yyyy-mm-dd') and yz.fenx='累计' \n" +
				"and yz.diancxxb_id="+diancxxb_id+"";
		ResultSetList rs_sn=con.getResultSetList(sql_sn);
		
		if(rs_sn.next()){
			fadl_sn=rs_sn.getDouble("fadl");
			fadbzmh_sn=rs_sn.getDouble("fadbzmh");
			gongrl_sn=rs_sn.getDouble("gongrl");
			gongrbzmh_sn=rs_sn.getDouble("gongrbzmh");
			rulmrz_sn=rs_sn.getDouble("rultrmpjfrl");
			
//			jisfdl_sn=rs_sn.getDouble("jisfdl");
//			gongrbml_sn=rs_sn.getDouble("gongrbml");
//			biaomh_sn=rs_sn.getDouble("biaomh");
			
			fadxybml_sn=CustomMaths.mul(fadl_sn, fadbzmh_sn);//上年发电需用标煤量＝上年发电量×上年发电标准煤耗
			gongrxybml_sn=CustomMaths.mul(gongrl_sn, gongrbzmh_sn);//上年供热需用标煤量＝上年供热量×上年供热标准煤耗
			xuybml_sn=CustomMaths.add(fadxybml_sn, gongrxybml_sn);//上年需用标煤量＝上年发电需用标煤量＋上年供热需用标煤量
			fadxyml_sn=CustomMaths.div(CustomMaths.mul(fadxybml_sn, 29.271), rulmrz_sn);//上年发电需原煤量＝上年发电需用标煤量*29.271/上年入炉热值
			gongrxyml_sn=CustomMaths.div(CustomMaths.mul(gongrxybml_sn, 29.271), rulmrz_sn);//上年供热需原煤量＝上年供热需用标煤量*29.271/上年入炉热值
			xuyyml_sn=CustomMaths.add(fadxyml_sn, gongrxyml_sn);//上年需用原煤量＝上年发电需原煤量＋上年供热需原煤量
			dianhzryl_sn=0;//上年点火助燃油量
			
		}
		//取得今年数据
		String sql_jn = "select yz.diancxxb_id,yz.fenx,yz.fadl,yz.gongrl,yz.fadbzmh,yz.gongrbzmh,yz.rultrmpjfrl from yuezbb yz \n" +//,yz.jisfdl,yz.gongrbml,yz.biaomh
				"where yz.riq=to_date('" + intyear + "-"+Stryuefen+"-01','yyyy-mm-dd') and yz.fenx='累计' \n" +
				"and yz.diancxxb_id="+diancxxb_id+"";
		ResultSetList rs_jn=con.getResultSetList(sql_jn);
		
		if(rs_jn.next()){
			fadl_jn=rs_jn.getDouble("fadl");
			gongrl_jn=rs_jn.getDouble("gongrl");
			fadbzmh_jn=rs_jn.getDouble("fadbzmh");
			gongrbzmh_jn=rs_jn.getDouble("gongrbzmh");
			rulmrz_jn=rs_jn.getDouble("rultrmpjfrl");
			
//			jisfdl_jn=rs_jn.getDouble("jisfdl");
//			gongrbml_jn=rs_jn.getDouble("gongrbml");
//			biaomh_jn=rs_jn.getDouble("biaomh");
			
			fadxybml_jn=CustomMaths.mul(fadl_jn,fadbzmh_jn);//上年发电需用标煤量＝今年发电量×今年发电标准煤耗
			gongrxybml_jn=CustomMaths.mul(gongrl_jn, gongrbzmh_jn);//今年供热需用标煤量＝今年供热量×今年供热标准煤耗
			xuybml_jn=CustomMaths.add(fadxybml_jn, gongrxybml_jn);//今年需用标煤量＝今年发电需用标煤量＋今年供热需用标煤量
			fadxyml_jn=CustomMaths.div(CustomMaths.mul(fadxybml_jn, 29.271), rulmrz_jn);//今年发电需原煤量＝今年发电需用标煤量*29.271/今年入炉热值
			gongrxyml_jn=CustomMaths.div(CustomMaths.mul(gongrxybml_jn, 29.271), rulmrz_jn);//今年供热需原煤量＝今年供热需用标煤量*29.271/今年入炉热值
			xuyyml_jn=CustomMaths.add(fadxyml_jn, gongrxyml_jn);//今年需用原煤量＝今年发电需原煤量＋今年供热需原煤量
			dianhzryl_jn=0;//今年点火助燃油量
		}
		
		//***********插入上年数据***************//		
		//现存机－机组状态为0
		String insert_sql="insert into nianxqjhh(ID,DIANCXXB_ID,NIANF,FADL,FADBHM,ZONGHCYDL," +
				"GONGRL,GONGRBZMH,RULRZ,FADXYBML,GONGRXYBML,XUYBML,FADXYML,GONGRXYML,XUYYML," +
				"DIANHZRYL,ZHUANGT,JIZZT,SHUJZT,GONGSSHR,GONGSSHSJ,JITSHR,JITSHSJ,JISFDL,GONGRBML,BIAOMH ) values (" +
					id+","+diancxxb_id+","+intyear+","+fadl_sn+","+fadbzmh_sn+",0,"+
					gongrl_sn+","+gongrbzmh_sn+","+rulmrz_sn+","+fadxybml_sn+","+gongrxybml_sn+","+
					xuybml_sn+","+fadxyml_sn+","+gongrxyml_sn+","+xuyyml_sn+","+dianhzryl_sn+
					",0,0,"+lastyear+",null,null,null,null,"+jisfdl_sn+","+gongrbml_sn+","+biaomh_sn+")";//审核状态，机组状态，数据状态，公司审核人，公司审核时间，集团审核人，集团审核时间
		
		con.getInsert(insert_sql);
		
		//新增机－机组状态为1
		String insert_sql1="insert into nianxqjhh(ID,DIANCXXB_ID,NIANF,FADL,FADBHM,ZONGHCYDL," +
				"GONGRL,GONGRBZMH,RULRZ,FADXYBML,GONGRXYBML,XUYBML,FADXYML,GONGRXYML,XUYYML," +
				"DIANHZRYL,ZHUANGT,JIZZT,SHUJZT,GONGSSHR,GONGSSHSJ,JITSHR,JITSHSJ,JISFDL,GONGRBML,BIAOMH ) values (" +
					id+","+diancxxb_id+","+intyear+",0,0,0,0,0,0,0,0,0,0,0,0,0," +
					"0,1,"+lastyear+",null,null,null,null,0,0,0)";//审核状态，机组状态，数据状态，公司审核人，公司审核时间，集团审核人，集团审核时间
		
		con.getInsert(insert_sql1);
		
		//***********插入今年数据***************//		
		//现存机－机组状态为0
		String insert_sql2="insert into nianxqjhh(ID,DIANCXXB_ID,NIANF,FADL,FADBHM,ZONGHCYDL," +
				"GONGRL,GONGRBZMH,RULRZ,FADXYBML,GONGRXYBML,XUYBML,FADXYML,GONGRXYML,XUYYML," +
				"DIANHZRYL,ZHUANGT,JIZZT,SHUJZT,GONGSSHR,GONGSSHSJ,JITSHR,JITSHSJ,JISFDL,GONGRBML,BIAOMH ) values (" +
					id+","+diancxxb_id+","+intyear+","+fadl_jn+","+fadbzmh_jn+",0,"+
					gongrl_jn+","+gongrbzmh_jn+","+rulmrz_jn+","+fadxybml_jn+","+gongrxybml_jn+","+
					xuybml_jn+","+fadxyml_jn+","+gongrxyml_jn+","+xuyyml_jn+","+dianhzryl_jn+
					",0,0,"+intyear+",null,null,null,null,"+jisfdl_jn+","+gongrbml_jn+","+biaomh_jn+")";//审核状态，机组状态，数据状态，公司审核人，公司审核时间，集团审核人，集团审核时间
		
		con.getInsert(insert_sql2);
		
		//新增机－机组状态为1
		String insert_sql3="insert into nianxqjhh(ID,DIANCXXB_ID,NIANF,FADL,FADBHM,ZONGHCYDL," +
		"GONGRL,GONGRBZMH,RULRZ,FADXYBML,GONGRXYBML,XUYBML,FADXYML,GONGRXYML,XUYYML," +
		"DIANHZRYL,ZHUANGT,JIZZT,SHUJZT,GONGSSHR,GONGSSHSJ,JITSHR,JITSHSJ,JISFDL,GONGRBML,BIAOMH ) values (" +
			id+","+diancxxb_id+","+intyear+",0,0,0,0,0,0,0,0,0,0,0,0,0," +
			"0,1,"+intyear+",null,null,null,null,0,0,0)";//审核状态，机组状态，数据状态，公司审核人，公司审核时间，集团审核人，集团审核时间

		con.getInsert(insert_sql3);
		
		//***********插入明年数据***************//	
		//现存机－机组状态为0
		String insert_sql4="insert into nianxqjhh(ID,DIANCXXB_ID,NIANF,FADL,FADBHM,ZONGHCYDL," +
		"GONGRL,GONGRBZMH,RULRZ,FADXYBML,GONGRXYBML,XUYBML,FADXYML,GONGRXYML,XUYYML," +
		"DIANHZRYL,ZHUANGT,JIZZT,SHUJZT,GONGSSHR,GONGSSHSJ,JITSHR,JITSHSJ,JISFDL,GONGRBML,BIAOMH ) values (" +
			id+","+diancxxb_id+","+intyear+",0,0,0,0,0,0,0,0,0,0,0,0,0," +
			"0,0,"+nextyear+",null,null,null,null,0,0,0)";//审核状态，机组状态，数据状态，公司审核人，公司审核时间，集团审核人，集团审核时间

		con.getInsert(insert_sql4);
		
		//新增机－机组状态为1
		String insert_sql5="insert into nianxqjhh(ID,DIANCXXB_ID,NIANF,FADL,FADBHM,ZONGHCYDL," +
		"GONGRL,GONGRBZMH,RULRZ,FADXYBML,GONGRXYBML,XUYBML,FADXYML,GONGRXYML,XUYYML," +
		"DIANHZRYL,ZHUANGT,JIZZT,SHUJZT,GONGSSHR,GONGSSHSJ,JITSHR,JITSHSJ,JISFDL,GONGRBML,BIAOMH ) values (" +
			id+","+diancxxb_id+","+intyear+",0,0,0,0,0,0,0,0,0,0,0,0,0," +
			"0,1,"+nextyear+",null,null,null,null,0,0,0)";//审核状态，机组状态，数据状态，公司审核人，公司审核时间，集团审核人，集团审核时间

		con.getInsert(insert_sql5);
		
		con.Close();
	}
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String zhuangt="";
		if(visit.isShifsh()==true){
			if(visit.getRenyjb()==3){
				zhuangt="";
			}else if(visit.getRenyjb()==2){
				zhuangt=" and (yx.zhuangt=1 or yx.zhuangt=2)";
			}else if(visit.getRenyjb()==1){
				zhuangt=" and yx.zhuangt=2";
			}
		}		
		//工具栏的年份和月份下拉框
		long intyear;
		long lastyear;
		long nextyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
			lastyear=intyear-1;
			nextyear=intyear+1;
		} else {
			intyear = getNianfValue().getId();
			lastyear=intyear-1;
			nextyear=intyear+1;
		}
		long intyuefen;
		if (getYuefenValue() == null) {
			intyuefen = DateUtil.getMonth(new Date());
		} else {
			intyuefen = getYuefenValue().getId();
		}
		//当月份是1的时候显示01,
		String Stryuefen="";
		if(intyuefen<10){
			
			Stryuefen="0"+intyuefen;
		}else{
			Stryuefen=""+intyuefen;
		}
		
		
		//-----------------------------------
//		String str = "";
		
		int treejib = this.getDiancTreeJib();
//		if (treejib == 1) {// 选集团时刷新出所有的电厂
//			str = "";
//		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
////			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
////					+ getTreeid() + ")";
//			str = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
//		
//		} else if (treejib == 3) {// 选电厂只刷新出该电厂
//			str = " and dc.id = " + getTreeid() + "";
//		
//		}
//		---------------------------------------------------------------------	
		boolean isReture = false ;
		if(visit.getRenyjb()==1){
			if(treejib==1){
				isReture=true;
			}
		}else if(visit.getRenyjb()==2){
			if(treejib==2){
				isReture=true;
			}
		}

//		if(isReture) {
//			setMsg("如果有需要回退信息时，请选择所要回退的公司或电厂名称！");
//		}
//	----------------------------------------------------------------------	
	

		String chaxun  =
			"select decode(grouping(jizlx),1,'总计',jizlx) as jizlx,lanc,\n" +
			"decode(mingc,null,decode(lanc,1,'发电量',2,'发电标准煤耗',3,'综合厂用电率',4,'供热量',5,'供热标准煤耗',\n" +
			"6,'入炉热值',7,'发电需用标煤量',8,'供热需用标煤量',9,'需用标煤量',10,'发电需原煤量',11,'供热需原煤量', \n" +
			"12,'需用原煤量',13,'点火助燃油量',14,'计划发电',15,'供热标煤量',16,'标煤耗',mingc),mingc) as mingc, \n" +
			"sum(lastyear) as lastyear,sum(year) as year,sum(nextyear) as nextyear,\n" + 
			"decode(grouping(zhuangt),1,'',decode(3,3,decode(zz.zhuangt,0,'未提交','已提交'),2,decode(zz.zhuangt,1,'未审核','已审核'),3,decode(zz.zhuangt,2,'未审核','已审核'))) as zhuangt\n" +
			"from ( \n"+
			//"--现存机组\n" + 
			"select '现存机组' as jizlx,1 as lanc,'发电量' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'万千瓦时' as danw,z1.zhuangt from\n" + 
			"(select yx.fadl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z1,\n" + 
			"(select yx.fadl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z2,\n" + 
			"(select yx.fadl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '现存机组' as jizlx,2 as lanc,'发电标准煤耗' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'克/千瓦时' as danw,z1.zhuangt from\n" + 
			"(select yx.fadbhm as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z1,\n" + 
			"(select yx.fadbhm as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z2,\n" + 
			"(select yx.fadbhm as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '现存机组' as jizlx,3 as lanc,'综合厂用电率' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'克/千瓦时' as danw,z1.zhuangt from\n" + 
			"(select yx.zonghcydl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z1,\n" + 
			"(select yx.zonghcydl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z2,\n" + 
			"(select yx.zonghcydl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '现存机组' as jizlx,4 as lanc,'供热量' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'万吉焦' as danw,z1.zhuangt from\n" + 
			"(select yx.gongrl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z1,\n" + 
			"(select yx.gongrl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z2,\n" + 
			"(select yx.gongrl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '现存机组' as jizlx,5 as lanc,'供热标准煤耗' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'千克/吉焦' as danw,z1.zhuangt from\n" + 
			"(select yx.gongrbzmh as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z1,\n" + 
			"(select yx.gongrbzmh as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z2,\n" + 
			"(select yx.gongrbzmh as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '现存机组' as jizlx,6 as lanc,'入炉热值' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'Mj/kg' as danw,z1.zhuangt from\n" + 
			"(select yx.rulrz as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z1,\n" + 
			"(select yx.rulrz as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z2,\n" + 
			"(select yx.rulrz as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '现存机组' as jizlx,7 as lanc,'发电需用标煤量' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from\n" + 
			"(select yx.fadxybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z1,\n" + 
			"(select yx.fadxybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z2,\n" + 
			"(select yx.fadxybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '现存机组' as jizlx,8 as lanc,'供热需用标煤量' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from\n" + 
			"(select yx.gongrxybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z1,\n" + 
			"(select yx.gongrxybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z2,\n" + 
			"(select yx.gongrxybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '现存机组' as jizlx,9 as lanc,'需用标煤量' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from\n" + 
			"(select yx.xuybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z1,\n" + 
			"(select yx.xuybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z2,\n" + 
			"(select yx.xuybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '现存机组' as jizlx,10 as lanc,'发电需原煤量' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from\n" + 
			"(select yx.fadxyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z1,\n" + 
			"(select yx.fadxyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z2,\n" + 
			"(select yx.fadxyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '现存机组' as jizlx,11 as lanc,'供热需原煤量' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from\n" + 
			"(select yx.gongrxyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z1,\n" + 
			"(select yx.gongrxyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z2,\n" + 
			"(select yx.gongrxyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '现存机组' as jizlx,12 as lanc,'需用原煤量' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from\n" + 
			"(select yx.xuyyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z1,\n" + 
			"(select yx.xuyyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z2,\n" + 
			"(select yx.xuyyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '现存机组' as jizlx,13 as lanc,'点火助燃油量' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from\n" + 
			"(select yx.dianhzryl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z1,\n" + 
			"(select yx.dianhzryl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z2,\n" + 
			"(select yx.dianhzryl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z3\n" +
			"union\n" + 
			"select '现存机组' as jizlx,14 as lanc,'计划发电' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from\n" + 
			"(select yx.jisfdl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z1,\n" + 
			"(select yx.jisfdl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z2,\n" + 
			"(select yx.jisfdl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '现存机组' as jizlx,15 as lanc,'供热标煤量' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from\n" + 
			"(select yx.gongrbml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z1,\n" + 
			"(select yx.gongrbml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z2,\n" + 
			"(select yx.gongrbml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '现存机组' as jizlx,16 as lanc,'标煤耗' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from\n" + 
			"(select yx.biaomh as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z1,\n" + 
			"(select yx.biaomh as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z2,\n" + 
			"(select yx.biaomh as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=0 "+zhuangt+") z3\n" +
			//"--新增机组\n" + 
			"union\n" + 
			"select '新增机组' as jizlx,1 as lanc,'发电量' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'万千瓦时' as danw,z1.zhuangt from\n" + 
			"(select yx.fadl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z1,\n" + 
			"(select yx.fadl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z2,\n" + 
			"(select yx.fadl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '新增机组' as jizlx,2 as lanc,'发电标准煤耗' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'克/千瓦时' as danw,z1.zhuangt from\n" + 
			"(select yx.fadbhm as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z1,\n" + 
			"(select yx.fadbhm as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z2,\n" + 
			"(select yx.fadbhm as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '新增机组' as jizlx,3 as lanc,'综合厂用电率' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'克/千瓦时' as danw,z1.zhuangt from\n" + 
			"(select yx.zonghcydl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z1,\n" + 
			"(select yx.zonghcydl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z2,\n" + 
			"(select yx.zonghcydl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '新增机组' as jizlx,4 as lanc,'供热量' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'万吉焦' as danw,z1.zhuangt from\n" + 
			"(select yx.gongrl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z1,\n" + 
			"(select yx.gongrl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z2,\n" + 
			"(select yx.gongrl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '新增机组' as jizlx,5 as lanc,'供热标准煤耗' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'千克/吉焦' as danw,z1.zhuangt from\n" + 
			"(select yx.gongrbzmh as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z1,\n" + 
			"(select yx.gongrbzmh as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z2,\n" + 
			"(select yx.gongrbzmh as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '新增机组' as jizlx,6 as lanc,'入炉热值' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'Mj/kg' as danw,z1.zhuangt from\n" + 
			"(select yx.rulrz as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z1,\n" + 
			"(select yx.rulrz as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z2,\n" + 
			"(select yx.rulrz as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '新增机组' as jizlx,7 as lanc,'发电需用标煤量' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from\n" + 
			"(select yx.fadxybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z1,\n" + 
			"(select yx.fadxybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z2,\n" + 
			"(select yx.fadxybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '新增机组' as jizlx,8 as lanc,'供热需用标煤量' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from\n" + 
			"(select yx.gongrxybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z1,\n" + 
			"(select yx.gongrxybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z2,\n" + 
			"(select yx.gongrxybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '新增机组' as jizlx,9 as lanc,'需用标煤量' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from\n" + 
			"(select yx.xuybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z1,\n" + 
			"(select yx.xuybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z2,\n" + 
			"(select yx.xuybml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '新增机组' as jizlx,10 as lanc,'发电需原煤量' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from\n" + 
			"(select yx.fadxyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z1,\n" + 
			"(select yx.fadxyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z2,\n" + 
			"(select yx.fadxyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '新增机组' as jizlx,11 as lanc,'供热需原煤量' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from\n" + 
			"(select yx.gongrxyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z1,\n" + 
			"(select yx.gongrxyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z2,\n" + 
			"(select yx.gongrxyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '新增机组' as jizlx,12 as lanc,'需用原煤量' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from\n" + 
			"(select yx.xuyyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z1,\n" + 
			"(select yx.xuyyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z2,\n" + 
			"(select yx.xuyyml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '新增机组' as jizlx,13 as lanc,'点火助燃油量' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from\n" + 
			"(select yx.dianhzryl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z1,\n" + 
			"(select yx.dianhzryl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z2,\n" + 
			"(select yx.dianhzryl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '新增机组' as jizlx,14 as lanc,'计划发电' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from\n" + 
			"(select yx.jisfdl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z1,\n" + 
			"(select yx.jisfdl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z2,\n" + 
			"(select yx.jisfdl as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '新增机组' as jizlx,15 as lanc,'供热标煤量' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from\n" + 
			"(select yx.gongrbml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z1,\n" + 
			"(select yx.gongrbml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z2,\n" + 
			"(select yx.gongrbml as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z3\n" + 
			"union\n" + 
			"select '新增机组' as jizlx,16 as lanc,'标煤耗' as mingc,z1.zhi as lastyear,z2.zhi as year,z3.zhi as nextyear,'吨' as danw,z1.zhuangt from\n" + 
			"(select yx.biaomh as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z1,\n" + 
			"(select yx.biaomh as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z2,\n" + 
			"(select yx.biaomh as zhi,yx.zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid() +" and yx.nianf="+intyear+" and yx.jizzt=1 "+zhuangt+") z3\n" + 
			") zz\n" + 
			"group by rollup (lanc,mingc,jizlx,zhuangt)\n" + 
			"having not (grouping(mingc) || grouping(zhuangt))=1 and grouping(lanc)=0\n" + 
			"order by jizlx desc,lanc asc";

		  
//	 System.out.println(chaxun);
	ResultSetList rsl = con.getResultSetList(chaxun);
	boolean yincan=false;
	boolean falge=false;
	while(rsl.next()){
		falge = true;
		if(visit.getRenyjb()==3){//电厂用户
			if(rsl.getString("zhuangt").equals("已提交")){
				yincan = true;
			}else{
				yincan = false;
			}
		}else if(visit.getRenyjb()==2){//集团用户
			if(rsl.getString("zhuangt").equals("已审核")){
				yincan = true;
			}else{
				yincan = false;
			}
		}
	}
	rsl.beforefirst();
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("nianxqjhh");
   	egu.setWidth("bodyWidth");
   	egu.getColumn("jizlx").setCenterHeader("机组类型");
	egu.getColumn("jizlx").setEditor(null);
	egu.getColumn("lanc").setCenterHeader("栏次");
	egu.getColumn("lanc").setEditor(null);
	egu.getColumn("mingc").setCenterHeader("项目");
	egu.getColumn("mingc").setEditor(null);
	if (getRbvalue()==null || getRbvalue().equals("")){
		egu.getColumn("year").setCenterHeader(intyear+"年已完成");
	}else{
		egu.getColumn("year").setCenterHeader(intyear+"年"+getRbvalue()+"月已完成");
	}
	egu.getColumn("lastyear").setCenterHeader(lastyear+"完成");
	
	egu.getColumn("nextyear").setCenterHeader(nextyear+"年预计");

	egu.getColumn("zhuangt").setCenterHeader("状态");
	egu.getColumn("zhuangt").setHidden(true);
	//egu.getColumn("zhuangt").setEditor(null);
	
	
//	设定不可编辑列的颜色
	egu.getColumn("jizlx").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
	egu.getColumn("lanc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
	egu.getColumn("mingc").setRenderer("function(value,metadata){metadata.css='tdTextext1'; return value;}");
//	//设定单元格的底色
	egu.getColumn("lastyear").setRenderer("function(value,metadata,rec,rowIndex){if(rowIndex<=12 || (rowIndex>=19 && rowIndex<25) || (rowIndex>=32 && rowIndex<38)){metadata.css='tdTextext3';} return value;}");
	egu.getColumn("year").setRenderer("function(value,metadata,rec,rowIndex){if(rowIndex<=12 || (rowIndex>=19 && rowIndex<25) || (rowIndex>=32 && rowIndex<38)){metadata.css='tdTextext3';} return value;}");
	egu.getColumn("nextyear").setRenderer("function(value,metadata,rec,rowIndex){if(rowIndex<=12 || (rowIndex>=19 && rowIndex<25) ||(rowIndex>=32 && rowIndex<38)){metadata.css='tdTextext3';} return value;}");
	
//	egu.getColumn("zhi").setRenderer("function(value,metadata,rec,rowIndex){if(rowIndex==2||rowIndex==5||rowIndex==10){metadata.css='tdTextext3';} return value;}");
//	egu.getColumn("mingc2").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
//	egu.getColumn("danw2").setRenderer("function(value,metadata){metadata.css='tdTextext2'; return value;}");
//	egu.getColumn("zhi2").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
	
	egu.setDefaultsortable(false);//设定页面不自动排序
	
	//设定列初始宽度
	egu.getColumn("jizlx").setWidth(70);
	egu.getColumn("jizlx").setAlign("right");
	egu.getColumn("lanc").setWidth(70);
	egu.getColumn("lanc").setAlign("right");
	egu.getColumn("mingc").setWidth(200);
	egu.getColumn("lastyear").setWidth(80);
	egu.getColumn("year").setWidth(120);
	egu.getColumn("nextyear").setWidth(80);
	
//	设定列的小数位
	//((NumberField)egu.getColumn("zhi1").editor).setDecimalPrecision(3);
	
	egu.setGridType(ExtGridUtil.Gridstyle_Edit);//设定grid可以编辑
	egu.addPaging(30);//设置分页
	
	//审核标志
	ComboBox cb_ys = new ComboBox();
	egu.getColumn("zhuangt").setEditor(cb_ys);
	cb_ys.setEditable(true);
	//String ysSql = "select id,mingc from yunsfsb order by mingc";
	
	List list = new ArrayList();
	if (visit.getRenyjb()==3) {//电厂
		list.add(new IDropDownBean(0, "未提交"));
		list.add(new IDropDownBean(1, "已提交"));
	}else if(visit.getRenyjb()==2){//分公司
		list.add(new IDropDownBean(1, "未审核"));
		list.add(new IDropDownBean(2, "已审核"));
	}else{//集团
		list.add(new IDropDownBean(2, "未审核"));
		list.add(new IDropDownBean(3, "已审核"));
	}
	
	egu.getColumn("zhuangt").setComboEditor(egu.gridId,new IDropDownModel(list));
	egu.getColumn("zhuangt").setReturnId(true);
	

	
	
	//*************************下拉框*****************************************88
	
	//********************工具栏************************************************
		egu.addTbarText("年份:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");//和自动刷新绑定
		comb1.setLazyRender(true);//动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("-");// 设置分隔符
		
		//设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		
		//设定工具栏下拉框自动刷新
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// 设置分隔符
		//egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		//egu.addToolbarButton(GridButton.ButtonType_Delete, null);
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新'+Ext.getDom('NIANF').value+'年的数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
		 String Strtmpfunction=
			 "var form = new Ext.FormPanel({ \n"+
			 "		labelAlign : 'right', \n"+
			 "		frame : true, \n"+
			 "		labelAlign : 'messageform', \n"+
			 "		labelWidth : 160, \n"+
			 "		items : [{ \n"+
			 "		layout : 'column',\n"+
			 "		items : [{ \n"+
			 "		layout:'form',\n"+
			 "		columnWidth:3,\n"+
			 "			items : [new Ext.form.ComboBox({ \n"+
			 "			fieldLabel:'请选择今年完成截止的月份', \n"+
			 "          id: 'yuefenname',\n"+
			 "			width:100, \n"+
			 "			selectOnFocus:true, \n"+
			 "			transform:'YUEFEN', \n"+
			 "			lazyRender:true, \n"+
			 "			triggerAction:'all', \n"+
			 "			typeAhead:true, \n"+
			 "			forceSelection:true\n"+
			 "			//editable:false \n"+
			 "			})] \n"+
			 "		}]\n"+
			 "	}] \n"+
			 "}); \n"+
			 "	win = new Ext.Window({ \n"+
			 "		layout : 'fit', \n"+
			 "		width : 300, \n"+
			 "		//height : 110, \n"+
			 "		closeAction : 'hide', \n"+
			 "		plain : true, \n"+
			 "		title : '条件', \n"+
			 "		items : [form],  \n"+
			 "		buttons : [{ \n"+
			 "			text : '确定', \n"+
			 "			handler : function() { \n"+
			 "				win.hide(); \n"+
			 "				document.getElementById('TEXT_RADIO_SELECT_VALUE').value = \n"+
			 "						Ext.getCmp(\"yuefenname\").value; \n"+
			 "				//document.getElementById('RefurbishButton').click(); \n"+
			 "			} \n"+
			 "		}, { \n"+
			 "			text : '取消', \n"+
			 "			handler : function() { \n"+
			 "				win.hide(); \n"+
			 "			} \n"+
			 "		}] \n"+
			 "	});\n";
		 
		if(visit.getRenyjb()==3){
			if(yincan ==false){
		//		添加按钮
				StringBuffer ins = new StringBuffer();
				ins.append("function (){")
				.append("document.getElementById('InsertButton').click();}");
				GridButton gbIns = new GridButton("添加",ins.toString());
				gbIns.setIcon(SysConstant.Btn_Icon_Insert);
				gbIns.setId("INSERT");
				if(falge){
					String str1="";
					str1="Ext.getCmp(\"INSERT\").setDisabled(true);";
					egu.addOtherScript(str1);
				}else{
					String str1="";
					str1="Ext.getCmp(\"INSERT\").setDisabled(false);";
					egu.addOtherScript(str1);
				}
				
				egu.addTbarBtn(gbIns);
				//设置按钮
				StringBuffer insz = new StringBuffer();
				insz.append("function(){ if(!win){");
				insz.append(Strtmpfunction);
				insz.append("} win.show(this);	\n");
				insz.append("}");
				GridButton gbSz = new GridButton("条件设置",insz.toString());
				gbSz.setIcon(SysConstant.Btn_Icon_Search);
				gbSz.setId("SEARCH");
				if(falge){
					String str1="";
					str1="Ext.getCmp(\"SEARCH\").setDisabled(true);";
					egu.addOtherScript(str1);
				}else{
					String str1="";
					str1="Ext.getCmp(\"SEARCH\").setDisabled(false);";
					egu.addOtherScript(str1);
				}
				
				egu.addTbarBtn(gbSz);
		//		删除按钮
				if (falge==true){
				String ss="Ext.MessageBox.confirm('警告', '你确定要删除本月所有的数据吗？', function(btn) { if(btn=='yes'){document.getElementById('DeleteButton').click();}})";
				StringBuffer del = new StringBuffer();
				del.append("function (){")
				.append(""+ss+"}");
				GridButton gbDel = new GridButton("删除",del.toString());
				gbDel.setIcon(SysConstant.Btn_Icon_Delete);
				gbDel.setId("DEL");
//				if(falge){
//					String str1="";
//					str1="Ext.getCmp(\"DEL\").setDisabled(true);";
//					egu.addOtherScript(str1);
//				}
				egu.addTbarBtn(gbDel);
				}
			}
		}
//		保存按钮
		if(falge==true){
			if(yincan ==false){
				egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton",MainGlobal.getExtMessageShow("正在保存数据,请稍后...", "保存中...", 200));
//				审核按钮
				if(visit.isShifsh()==true){
					if(visit.getRenyjb()==2 || visit.getRenyjb()==3){
//						GridButton gsh = new GridButton("审核","function (){document.getElementById('ShenhButton').click();}");
						
						String ss="Ext.MessageBox.confirm('警告', '你确定要将数据提交上级单位吗？', function(btn) { if(btn=='yes'){document.getElementById('ShenhButton').click();}})";
						StringBuffer shenh = new StringBuffer();
						shenh.append("function (){")
						.append(""+ss+"}");
						
						GridButton gbShen = new GridButton("审核",shenh.toString());
						gbShen.setIcon(SysConstant.Btn_Icon_Search);
						gbShen.setId("SHENH");
		//				if(yincan==true){
		//					String str1="";
		//					str1="Ext.getCmp(\"SHENH\").setDisabled(true);";
		//					egu.addOtherScript(str1);
		//				}
						egu.addTbarBtn(gbShen);
					}
				}
			}
			
	//		回退按钮
			if(visit.isShifsh()==true){
				//egu.getColumn("zhuangt").setEditor(null);
				if(visit.getRenyjb()==2 || visit.getRenyjb()==1){
					GridButton ght = new GridButton("回退","function (){document.getElementById('ReturnButton').click();}");
					ght.setDisabled(isReture);
					ght.setIcon(SysConstant.Btn_Icon_Return);
					ght.setId("RETURN");
					if(falge){
						String str1="";
						str1="Ext.getCmp(\"RETURN\").setDisabled(false);";
						egu.addOtherScript(str1);
					}
					egu.addTbarBtn(ght);
				}
			} 
		}
			
		egu.addTbarText("->");
		
		//---------------页面js的计算开始------------------------------------------
		StringBuffer sb = new StringBuffer();
		
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		
		//设定某一行不能编辑
		sb.append("if(e.record.get('JIZLX')=='总计'){e.cancel=true;}");//
		sb.append("if(e.record.get('LANC')=='7' || e.record.get('LANC')=='8' || e.record.get('LANC')=='9' || e.record.get('LANC')=='10' || e.record.get('LANC')=='11' || e.record.get('LANC')=='12'){e.cancel=true;}");
		
		sb.append("});");
		
		sb.append("gridDiv_grid.on('afteredit',function(e){\n");
			
			//************************总计**************************//
			sb.append("fadl_zj = gridDiv_ds.getAt(0);\n");//发电量
			sb.append("fadbhm_zj = gridDiv_ds.getAt(1);\n");//发电标准煤耗
			
			sb.append("zonghcydl_zj = gridDiv_ds.getAt(2);\n");//综合厂用电率－录入
			
			sb.append("gongrl_zj = gridDiv_ds.getAt(3);\n");//供热量
			sb.append("gongrbzmh_zj = gridDiv_ds.getAt(4);\n");//供热标准煤耗
			
			sb.append("rulrz_zj = gridDiv_ds.getAt(5);\n");//入炉热值－录入
			
			sb.append("fadxybml_zj = gridDiv_ds.getAt(6);\n");//发电需用标煤量
			sb.append("gongrxybml_zj = gridDiv_ds.getAt(7);\n");//供热需用标煤量
			sb.append("xuybml_zj = gridDiv_ds.getAt(8);\n");//需用标煤量
			sb.append("fadxyml_zj = gridDiv_ds.getAt(9);\n");//发电需原煤量
			sb.append("gongrxyml_zj = gridDiv_ds.getAt(10);\n");//供热需原煤量
			sb.append("xuyyml_zj = gridDiv_ds.getAt(11);\n");//需用原煤量
			
			sb.append("dianhzryl_zj = gridDiv_ds.getAt(12);\n");//点火助燃油量－录入
			
			sb.append("jisdl_zj = gridDiv_ds.getAt(13);\n");//计划发电－录入
			sb.append("gongrbml_zj = gridDiv_ds.getAt(14);\n");//供热标煤量－录入
			sb.append("biaomh_zj = gridDiv_ds.getAt(15);\n");//标煤耗－录入
			
			
			//************************新增机组**************************//
			sb.append("fadl_xj = gridDiv_ds.getAt(16);\n");//发电量
			sb.append("fadbhm_xj = gridDiv_ds.getAt(17);\n");//发电标准煤耗
			
			sb.append("zonghcydl_xj = gridDiv_ds.getAt(18);\n");//综合厂用电率－录入
			
			sb.append("gongrl_xj = gridDiv_ds.getAt(19);\n");//供热量
			sb.append("gongrbzmh_xj = gridDiv_ds.getAt(20);\n");//供热标准煤耗
			
			sb.append("rulrz_xj = gridDiv_ds.getAt(21);\n");//入炉热值－录入
			
			sb.append("fadxybml_xj = gridDiv_ds.getAt(22);\n");//发电需用标煤量
			sb.append("gongrxybml_xj = gridDiv_ds.getAt(23);\n");//供热需用标煤量
			sb.append("xuybml_xj = gridDiv_ds.getAt(24);\n");//需用标煤量
			sb.append("fadxyml_xj = gridDiv_ds.getAt(25);\n");//发电需原煤量
			sb.append("gongrxyml_xj = gridDiv_ds.getAt(26);\n");//供热需原煤量
			sb.append("xuyyml_xj = gridDiv_ds.getAt(27);\n");//需用原煤量
			
			sb.append("dianhzryl_xj = gridDiv_ds.getAt(28);\n");//点火助燃油量－录入
			
			sb.append("jisdl_xj = gridDiv_ds.getAt(29);\n");//计划发电－录入
			sb.append("gongrbml_xj = gridDiv_ds.getAt(30);\n");//供热标煤量－录入
			sb.append("biaomh_xj = gridDiv_ds.getAt(31);\n");//标煤耗－录入
			
			//现存机组
			sb.append("fadl_xc = gridDiv_ds.getAt(32);\n");//发电量
			sb.append("fadbhm_xc = gridDiv_ds.getAt(33);\n");//发电标准煤耗
			
			sb.append("zonghcydl_xc = gridDiv_ds.getAt(34);\n");//综合厂用电率－录入
			
			sb.append("gongrl_xc = gridDiv_ds.getAt(35);\n");//供热量
			sb.append("gongrbzmh_xc = gridDiv_ds.getAt(36);\n");//供热标准煤耗
			
			sb.append("rulrz_xc = gridDiv_ds.getAt(37);\n");//入炉热值－录入
			
			sb.append("fadxybml_xc = gridDiv_ds.getAt(38);\n");//发电需用标煤量
			sb.append("gongrxybml_xc = gridDiv_ds.getAt(39);\n");//供热需用标煤量
			sb.append("xuybml_xc = gridDiv_ds.getAt(40);\n");//需用标煤量
			sb.append("fadxyml_xc = gridDiv_ds.getAt(41);\n");//发电需原煤量
			sb.append("gongrxyml_xc = gridDiv_ds.getAt(42);\n");//供热需原煤量
			sb.append("xuyyml_xc = gridDiv_ds.getAt(43);\n");//需用原煤量
			
			sb.append("dianhzryl_xc = gridDiv_ds.getAt(44);\n");//点火助燃油量－录入
			
			sb.append("jisdl_xc = gridDiv_ds.getAt(45);\n");//计划发电－录入
			sb.append("gongrbml_xc = gridDiv_ds.getAt(46);\n");//供热标煤量－录入
			sb.append("biaomh_xc = gridDiv_ds.getAt(47);\n");//标煤耗－录入
			
			//sb.append("if((e.row>13 && e.row<18) || (e.row>26 && e.row<32)){ \n");
			
			//计划发电需用标煤量-新增机组	
			sb.append("fadxybml_xj.set('LASTYEAR',Round((eval(fadl_xj.get('LASTYEAR')||0)*1000)*eval(fadbhm_xj.get('LASTYEAR')||0)/1000/1000,0));\n");
			sb.append("fadxybml_xj.set('YEAR',Round((eval(fadl_xj.get('YEAR')||0)*1000)*eval(fadbhm_xj.get('YEAR')||0)/1000/1000,0));\n");
			sb.append("fadxybml_xj.set('NEXTYEAR',Round((eval(fadl_xj.get('NEXTYEAR')||0)*1000)*eval(fadbhm_xj.get('NEXTYEAR')||0)/1000/1000,0));\n");
			
			//计算供热需用标煤量-新增机组	
			sb.append("gongrxybml_xj.set('LASTYEAR',Round((eval(gongrl_xj.get('LASTYEAR')||0)*1000)*eval(gongrbzmh_xj.get('LASTYEAR')||0)/1000,0));\n");
			sb.append("gongrxybml_xj.set('YEAR',Round((eval(gongrl_xj.get('YEAR')||0)*1000)*eval(gongrbzmh_xj.get('YEAR')||0)/1000,0));\n");
			sb.append("gongrxybml_xj.set('NEXTYEAR',Round((eval(gongrl_xj.get('NEXTYEAR')||0)*1000)*eval(gongrbzmh_xj.get('NEXTYEAR')||0)/1000,0));\n");
			
			sb.append("xuybml_xj.set('LASTYEAR',eval(fadxybml_xj.get('LASTYEAR')||0)+eval(gongrxybml_xj.get('LASTYEAR')||0));\n");
			sb.append("xuybml_xj.set('YEAR',eval(fadxybml_xj.get('YEAR')||0)+eval(gongrxybml_xj.get('YEAR')||0));\n");
			sb.append("xuybml_xj.set('NEXTYEAR',eval(fadxybml_xj.get('NEXTYEAR')||0)+eval(gongrxybml_xj.get('NEXTYEAR')||0));\n");
			
			sb.append("if(eval(rulrz_xj.get('LASTYEAR')||0)==0){ \n");
			sb.append("fadxyml_xj.set('LASTYEAR',0);");
			sb.append("}else{");
			sb.append("fadxyml_xj.set('LASTYEAR',Round((eval(fadxybml_xj.get('LASTYEAR')||0)*29.271)/eval(rulrz_xj.get('LASTYEAR')||0),2));\n");
			sb.append("}");
			sb.append("if(eval(rulrz_xj.get('YEAR')||0)==0){ \n");
			sb.append("fadxyml_xj.set('YEAR',0);\n ");
			sb.append("}else{\n ");
			sb.append("fadxyml_xj.set('YEAR',Round((eval(fadxybml_xj.get('YEAR')||0)*29.271)/eval(rulrz_xj.get('YEAR')||0),2));\n");
			sb.append("}\n ");
			sb.append("if(eval(rulrz_xj.get('NEXTYEAR')||0)==0){ \n");
			sb.append("fadxyml_xj.set('NEXTYEAR',0);\n ");
			sb.append("}else{\n ");
			sb.append("fadxyml_xj.set('NEXTYEAR',Round((eval(fadxybml_xj.get('NEXTYEAR')||0)*29.271)/eval(rulrz_xj.get('NEXTYEAR')||0),2));\n");
			sb.append("}\n ");
			
			sb.append("if(eval(rulrz_xj.get('LASTYEAR')||0)==0){ \n");
			sb.append("gongrxyml_xj.set('LASTYEAR',0);\n ");
			sb.append("}else{\n ");
			sb.append("gongrxyml_xj.set('LASTYEAR',Round((eval(gongrxybml_xj.get('LASTYEAR')||0)*29.271)/eval(rulrz_xj.get('LASTYEAR')||0),2));\n");
			sb.append("}\n ");
			sb.append("if(eval(rulrz_xj.get('YEAR')||0)==0){ \n");
			sb.append("gongrxyml_xj.set('YEAR',0);\n ");
			sb.append("}else{\n ");
			sb.append("gongrxyml_xj.set('YEAR',Round((eval(gongrxybml_xj.get('YEAR')||0)*29.271)/eval(rulrz_xj.get('YEAR')||0),2));\n");
			sb.append("}\n ");
			sb.append("if(eval(rulrz_xj.get('NEXTYEAR')||0)==0){ \n");
			sb.append("gongrxyml_xj.set('NEXTYEAR',0);\n ");
			sb.append("}else{\n ");
			sb.append("gongrxyml_xj.set('NEXTYEAR',Round((eval(gongrxybml_xj.get('NEXTYEAR')||0)*29.271)/eval(rulrz_xj.get('NEXTYEAR')||0),2));\n");
			sb.append("}\n ");
			
			sb.append("xuyyml_xj.set('LASTYEAR',Round(eval(fadxyml_xj.get('LASTYEAR')||0)+eval(gongrxyml_xj.get('LASTYEAR')||0),2));\n");
			sb.append("xuyyml_xj.set('YEAR',Round(eval(fadxyml_xj.get('YEAR')||0)+eval(gongrxyml_xj.get('YEAR')||0),2));\n");
			sb.append("xuyyml_xj.set('NEXTYEAR',Round(eval(fadxyml_xj.get('NEXTYEAR')||0)+eval(gongrxyml_xj.get('NEXTYEAR')||0),2));\n");
		
			//*****************现存机组*********************************//
			//计划发电需用标煤量-新增机组	
			sb.append("fadxybml_xc.set('LASTYEAR',Round((eval(fadl_xc.get('LASTYEAR')||0)*1000)*eval(fadbhm_xc.get('LASTYEAR')||0)/1000/1000,0));\n");
			sb.append("fadxybml_xc.set('YEAR',Round((eval(fadl_xc.get('YEAR')||0)*1000)*eval(fadbhm_xc.get('YEAR')||0)/1000/1000,0));\n");
			sb.append("fadxybml_xc.set('NEXTYEAR',Round((eval(fadl_xc.get('NEXTYEAR')||0)*1000)*eval(fadbhm_xc.get('NEXTYEAR')||0)/1000/1000,0));\n");
			
			//计算供热需用标煤量-新增机组	
			sb.append("gongrxybml_xc.set('LASTYEAR',Round((eval(gongrl_xc.get('LASTYEAR')||0)*1000)*eval(gongrbzmh_xc.get('LASTYEAR')||0)/1000,0));\n");
			sb.append("gongrxybml_xc.set('YEAR',Round((eval(gongrl_xc.get('YEAR')||0)*1000)*eval(gongrbzmh_xc.get('YEAR')||0)/1000,0));\n");
			sb.append("gongrxybml_xc.set('NEXTYEAR',Round((eval(gongrl_xc.get('NEXTYEAR')||0)*1000)*eval(gongrbzmh_xc.get('NEXTYEAR')||0)/1000,0));\n");
			
			sb.append("xuybml_xc.set('LASTYEAR',eval(fadxybml_xc.get('LASTYEAR')||0)+eval(gongrxybml_xc.get('LASTYEAR')||0));\n");
			sb.append("xuybml_xc.set('YEAR',eval(fadxybml_xc.get('YEAR')||0)+eval(gongrxybml_xc.get('YEAR')||0));\n");
			sb.append("xuybml_xc.set('NEXTYEAR',eval(fadxybml_xc.get('NEXTYEAR')||0)+eval(gongrxybml_xc.get('NEXTYEAR')||0));\n");
			
			sb.append("if(eval(rulrz_xc.get('LASTYEAR')||0)==0){ \n");
			sb.append("fadxyml_xc.set('LASTYEAR',0);\n ");
			sb.append("}else{\n ");
			sb.append("fadxyml_xc.set('LASTYEAR',Round((eval(fadxybml_xc.get('LASTYEAR')||0)*29.271)/eval(rulrz_xc.get('LASTYEAR')||0),2));\n");
			sb.append("}\n ");
			sb.append("if(eval(rulrz_xc.get('YEAR')||0)==0){ \n");
			sb.append("fadxyml_xc.set('YEAR',0);\n ");
			sb.append("}else{\n ");
			sb.append("fadxyml_xc.set('YEAR',Round((eval(fadxybml_xc.get('YEAR')||0)*29.271)/eval(rulrz_xc.get('YEAR')||0),2));\n");
			sb.append("}\n ");
			sb.append("if(eval(rulrz_xc.get('NEXTYEAR')||0)==0){ \n");
			sb.append("fadxyml_xc.set('NEXTYEAR',0);\n ");
			sb.append("}else{\n ");
			sb.append("fadxyml_xc.set('NEXTYEAR',Round((eval(fadxybml_xc.get('NEXTYEAR')||0)*29.271)/eval(rulrz_xc.get('NEXTYEAR')||0),2));\n");
			sb.append("}\n ");
			
			sb.append("if(eval(rulrz_xc.get('LASTYEAR')||0)==0){ \n");
			sb.append("gongrxyml_xc.set('LASTYEAR',0);\n ");
			sb.append("}else{\n ");
			sb.append("gongrxyml_xc.set('LASTYEAR',Round((eval(gongrxybml_xc.get('LASTYEAR')||0)*29.271)/eval(rulrz_xc.get('LASTYEAR')||0),2));\n");
			sb.append("}\n ");
			sb.append("if(eval(rulrz_xc.get('YEAR')||0)==0){ \n");
			sb.append("gongrxyml_xc.set('YEAR',0);\n ");
			sb.append("}else{\n ");
			sb.append("gongrxyml_xc.set('YEAR',Round((eval(gongrxybml_xc.get('YEAR')||0)*29.271)/eval(rulrz_xc.get('YEAR')||0),2));\n");
			sb.append("}\n ");
			sb.append("if(eval(rulrz_xc.get('NEXTYEAR')||0)==0){ \n");
			sb.append("gongrxyml_xc.set('NEXTYEAR',0);\n ");
			sb.append("}else{\n ");
			sb.append("gongrxyml_xc.set('NEXTYEAR',Round((eval(gongrxybml_xc.get('NEXTYEAR')||0)*29.271)/eval(rulrz_xc.get('NEXTYEAR')||0),2));\n");
			sb.append("}\n ");
			
			sb.append("xuyyml_xc.set('LASTYEAR',Round(eval(fadxyml_xc.get('LASTYEAR')||0)+eval(gongrxyml_xc.get('LASTYEAR')||0),2));\n");
			sb.append("xuyyml_xc.set('YEAR',Round(eval(fadxyml_xc.get('YEAR')||0)+eval(gongrxyml_xc.get('YEAR')||0),2));\n");
			sb.append("xuyyml_xc.set('NEXTYEAR',Round(eval(fadxyml_xc.get('NEXTYEAR')||0)+eval(gongrxyml_xc.get('NEXTYEAR')||0),2));\n");
		
			
			//*****************总计*********************************//
			sb.append("fadl_zj.set('LASTYEAR',eval(fadl_xj.get('LASTYEAR')||0)+eval(fadl_xc.get('LASTYEAR')||0));\n");
			sb.append("fadl_zj.set('YEAR',eval(fadl_xj.get('YEAR')||0)+eval(fadl_xc.get('YEAR')||0));\n");
			sb.append("fadl_zj.set('NEXTYEAR',eval(fadl_xj.get('NEXTYEAR')||0)+eval(fadl_xc.get('NEXTYEAR')||0));\n");
			
			sb.append("fadbhm_zj.set('LASTYEAR',eval(fadbhm_xj.get('LASTYEAR')||0)+eval(fadbhm_xc.get('LASTYEAR')||0));\n");
			sb.append("fadbhm_zj.set('YEAR',eval(fadbhm_xj.get('YEAR')||0)+eval(fadbhm_xc.get('YEAR')||0));\n");
			sb.append("fadbhm_zj.set('NEXTYEAR',eval(fadbhm_xj.get('NEXTYEAR')||0)+eval(fadbhm_xc.get('NEXTYEAR')||0));\n");
			
			sb.append("zonghcydl_zj.set('LASTYEAR',eval(zonghcydl_xj.get('LASTYEAR')||0)+eval(zonghcydl_xc.get('LASTYEAR')||0));\n");
			sb.append("zonghcydl_zj.set('YEAR',eval(zonghcydl_xj.get('YEAR')||0)+eval(zonghcydl_xc.get('YEAR')||0));\n");
			sb.append("zonghcydl_zj.set('NEXTYEAR',eval(zonghcydl_xj.get('NEXTYEAR')||0)+eval(zonghcydl_xc.get('NEXTYEAR')||0));\n");
			
			sb.append("gongrl_zj.set('LASTYEAR',eval(gongrl_xj.get('LASTYEAR')||0)+eval(gongrl_xc.get('LASTYEAR')||0));\n");
			sb.append("gongrl_zj.set('YEAR',eval(gongrl_xj.get('YEAR')||0)+eval(gongrl_xc.get('YEAR')||0));\n");
			sb.append("gongrl_zj.set('NEXTYEAR',eval(gongrl_xj.get('NEXTYEAR')||0)+eval(gongrl_xc.get('NEXTYEAR')||0));\n");
			
			sb.append("gongrbzmh_zj.set('LASTYEAR',eval(gongrbzmh_xj.get('LASTYEAR')||0)+eval(gongrbzmh_xc.get('LASTYEAR')||0));\n");
			sb.append("gongrbzmh_zj.set('YEAR',eval(gongrbzmh_xj.get('YEAR')||0)+eval(gongrbzmh_xc.get('YEAR')||0));\n");
			sb.append("gongrbzmh_zj.set('NEXTYEAR',eval(gongrbzmh_xj.get('NEXTYEAR')||0)+eval(gongrbzmh_xc.get('NEXTYEAR')||0));\n");
			
			sb.append("rulrz_zj.set('LASTYEAR',eval(rulrz_xj.get('LASTYEAR')||0)+eval(rulrz_xc.get('LASTYEAR')||0));\n");
			sb.append("rulrz_zj.set('YEAR',eval(rulrz_xj.get('YEAR')||0)+eval(rulrz_xc.get('YEAR')||0));\n");
			sb.append("rulrz_zj.set('NEXTYEAR',eval(rulrz_xj.get('NEXTYEAR')||0)+eval(rulrz_xc.get('NEXTYEAR')||0));\n");
			
			sb.append("dianhzryl_zj.set('LASTYEAR',eval(dianhzryl_xj.get('LASTYEAR')||0)+eval(dianhzryl_xc.get('LASTYEAR')||0));\n");
			sb.append("dianhzryl_zj.set('YEAR',eval(dianhzryl_xj.get('YEAR')||0)+eval(dianhzryl_xc.get('YEAR')||0));\n");
			sb.append("dianhzryl_zj.set('NEXTYEAR',eval(dianhzryl_xj.get('NEXTYEAR')||0)+eval(dianhzryl_xc.get('NEXTYEAR')||0));\n");
			
			sb.append("jisdl_zj.set('LASTYEAR',eval(jisdl_xj.get('LASTYEAR')||0)+eval(jisdl_xc.get('LASTYEAR')||0));\n");
			sb.append("jisdl_zj.set('YEAR',eval(jisdl_xj.get('YEAR')||0)+eval(jisdl_xc.get('YEAR')||0));\n");
			sb.append("jisdl_zj.set('NEXTYEAR',eval(jisdl_xj.get('NEXTYEAR')||0)+eval(jisdl_xc.get('NEXTYEAR')||0));\n");
			
			sb.append("gongrbml_zj.set('LASTYEAR',eval(gongrbml_xj.get('LASTYEAR')||0)+eval(gongrbml_xc.get('LASTYEAR')||0));\n");
			sb.append("gongrbml_zj.set('YEAR',eval(gongrbml_xj.get('YEAR')||0)+eval(gongrbml_xc.get('YEAR')||0));\n");
			sb.append("gongrbml_zj.set('NEXTYEAR',eval(gongrbml_xj.get('NEXTYEAR')||0)+eval(gongrbml_xc.get('NEXTYEAR')||0));\n");
			
			sb.append("biaomh_zj.set('LASTYEAR',eval(biaomh_xj.get('LASTYEAR')||0)+eval(biaomh_xc.get('LASTYEAR')||0));\n");
			sb.append("biaomh_zj.set('YEAR',eval(biaomh_xj.get('YEAR')||0)+eval(biaomh_xc.get('YEAR')||0));\n");
			sb.append("biaomh_zj.set('NEXTYEAR',eval(biaomh_xj.get('NEXTYEAR')||0)+eval(biaomh_xc.get('NEXTYEAR')||0));\n");
			
			
			//计算总计变量
			sb.append("fadxybml_zj.set('LASTYEAR',Round((eval(fadl_zj.get('LASTYEAR')||0)*1000)*eval(fadbhm_zj.get('LASTYEAR')||0)/1000/1000,0));\n");
			sb.append("fadxybml_zj.set('YEAR',Round((eval(fadl_zj.get('YEAR')||0)*1000)*eval(fadbhm_zj.get('YEAR')||0)/1000/1000,0));\n");
			sb.append("fadxybml_zj.set('NEXTYEAR',Round((eval(fadl_zj.get('NEXTYEAR')||0)*1000)*eval(fadbhm_zj.get('NEXTYEAR')||0)/1000/1000,0));\n");
			
			sb.append("gongrxybml_zj.set('LASTYEAR',Round((eval(gongrl_zj.get('LASTYEAR')||0)*1000)*eval(gongrbzmh_zj.get('LASTYEAR')||0)/1000,0));\n");
			sb.append("gongrxybml_zj.set('YEAR',Round((eval(gongrl_zj.get('YEAR')||0)*1000)*eval(gongrbzmh_zj.get('YEAR')||0)/1000,0));\n");
			sb.append("gongrxybml_zj.set('NEXTYEAR',Round((eval(gongrl_zj.get('NEXTYEAR')||0)*1000)*eval(gongrbzmh_zj.get('NEXTYEAR')||0)/1000,0));\n");
			
			sb.append("xuybml_zj.set('LASTYEAR',eval(fadxybml_zj.get('LASTYEAR')||0)+eval(gongrxybml_zj.get('LASTYEAR')||0));\n");
			sb.append("xuybml_zj.set('YEAR',eval(fadxybml_zj.get('YEAR')||0)+eval(gongrxybml_zj.get('YEAR')||0));\n");
			sb.append("xuybml_zj.set('NEXTYEAR',eval(fadxybml_zj.get('NEXTYEAR')||0)+eval(gongrxybml_zj.get('NEXTYEAR')||0));\n");
			
			sb.append("if(eval(rulrz_zj.get('LASTYEAR')||0)==0){ \n");
			sb.append("fadxyml_zj.set('LASTYEAR',0);\n ");
			sb.append("}else{\n ");
			sb.append("fadxyml_zj.set('LASTYEAR',Round((eval(fadxybml_zj.get('LASTYEAR')||0)*29.271)/eval(rulrz_zj.get('LASTYEAR')||0),2));\n");
			sb.append("}\n ");
			sb.append("if(eval(rulrz_zj.get('YEAR')||0)==0){ \n");
			sb.append("fadxyml_zj.set('YEAR',0);\n ");
			sb.append("}else{\n ");
			sb.append("fadxyml_zj.set('YEAR',Round((eval(fadxybml_zj.get('YEAR')||0)*29.271)/eval(rulrz_zj.get('YEAR')||0),2));\n");
			sb.append("}\n ");
			sb.append("if(eval(rulrz_zj.get('NEXTYEAR')||0)==0){ \n");
			sb.append("fadxyml_zj.set('NEXTYEAR',0);\n ");
			sb.append("}else{\n ");
			sb.append("fadxyml_zj.set('NEXTYEAR',Round((eval(fadxybml_zj.get('NEXTYEAR')||0)*29.271)/eval(rulrz_zj.get('NEXTYEAR')||0),2));\n");
			sb.append("}\n ");
			
			sb.append("if(eval(rulrz_zj.get('LASTYEAR')||0)==0){ \n");
			sb.append("gongrxyml_zj.set('LASTYEAR',0);\n ");
			sb.append("}else{\n ");
			sb.append("gongrxyml_zj.set('LASTYEAR',Round((eval(gongrxybml_zj.get('LASTYEAR')||0)*29.271)/eval(rulrz_zj.get('LASTYEAR')||0),2));\n");
			sb.append("}\n ");
			sb.append("if(eval(rulrz_zj.get('YEAR')||0)==0){ \n");
			sb.append("gongrxyml_zj.set('YEAR',0);\n ");
			sb.append("}else{\n ");
			sb.append("gongrxyml_zj.set('YEAR',Round((eval(gongrxybml_zj.get('YEAR')||0)*29.271)/eval(rulrz_zj.get('YEAR')||0),2));\n");
			sb.append("}\n ");
			sb.append("if(eval(rulrz_zj.get('NEXTYEAR')||0)==0){ \n");
			sb.append("gongrxyml_zj.set('NEXTYEAR',0);");
			sb.append("}else{\n ");
			sb.append("gongrxyml_zj.set('NEXTYEAR',Round((eval(gongrxybml_zj.get('NEXTYEAR')||0)*29.271)/eval(rulrz_zj.get('NEXTYEAR')||0),2));\n");
			sb.append("}\n ");
			
			sb.append("xuyyml_zj.set('LASTYEAR',Round(eval(fadxyml_zj.get('LASTYEAR')||0)+eval(gongrxyml_zj.get('LASTYEAR')||0),2));\n");
			sb.append("xuyyml_zj.set('YEAR',Round(eval(fadxyml_zj.get('YEAR')||0)+eval(gongrxyml_zj.get('YEAR')||0),2));\n");
			sb.append("xuyyml_zj.set('NEXTYEAR',Round(eval(fadxyml_zj.get('NEXTYEAR')||0)+eval(gongrxyml_zj.get('NEXTYEAR')||0),2));\n");
		
//		sb.append("}");
		sb.append("});\n");
		
		 //设定合计列不保存
		sb.append("function gridDiv_save(record){if(record.get('JIZLX')=='总计') return 'continue';}");
		
		egu.addOtherScript(sb.toString());
		//---------------页面js计算结束--------------------------
		
		setExtGrid(egu);
		con.Close();
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
		if (getTbmsg() != null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem(
					"'<marquee width=200 scrollamount=2>" + getTbmsg()
							+ "</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
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
			visit.setList1(null);
			this.setNianfValue(null);
			this.getNianfModels();
			this.setTreeid(null);
			this.setYuefenValue(null);
			this.getYuefenModels();
			visit.setShifsh(true);
			setTbmsg(null);
			setRbvalue(null);
		}
			
			getSelectData();
		
		
	}
	
	public String getRbvalue() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRbvalue(String rbvalue) {
		((Visit) this.getPage().getVisit()).setString1(rbvalue);
	}
	
	public String getNianfen() {
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setNianfen(String value) {
		((Visit) getPage().getVisit()).setString4(value);
	}
	
	public String getYuefen() {
		int intYuefen = Integer.parseInt(((Visit) getPage().getVisit()).getString5());
		if (intYuefen<10){
			return "0"+intYuefen;
		}else{
			return ((Visit) getPage().getVisit()).getString5();
		}
	}
	public void setYuefen(String value) {
		((Visit) getPage().getVisit()).setString5(value);
	}
	
	public void setRiq1() {
		setNianfen(getNianfenValue().getValue());
		setYuefen(getYuefenValue().getValue());
	}	
	private static IPropertySelectionModel _NianfenModel;
    public IPropertySelectionModel getNianfenModel() {
        if (_NianfenModel == null) {
            getNianfenModels();
        }
        return _NianfenModel;
    }
    
	private IDropDownBean _NianfenValue;
	
    public IDropDownBean getNianfenValue() {
        if (_NianfenValue == null) {
            int _nianfen = DateUtil.getYear(new Date());
            int _yuefen = DateUtil.getMonth(new Date());
            if (_yuefen == 1) {
                _nianfen = _nianfen - 1;
            }
            for (int i = 0; i < getNianfenModel().getOptionCount(); i++) {
                Object obj = getNianfenModel().getOption(i);
                if (_nianfen == ((IDropDownBean) obj).getId()) {
                    _NianfenValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _NianfenValue;
    }
	
    public void setNianfenValue(IDropDownBean Value) {
    	if  (_NianfenValue!=Value){
    		_NianfenValue = Value;
    	}
    }

    public IPropertySelectionModel getNianfenModels() {
        List listNianfen = new ArrayList();
        int i;
        for (i = 2004; i <= DateUtil.getYear(new Date())+1; i++) {
            listNianfen.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _NianfenModel = new IDropDownModel(listNianfen);
        return _NianfenModel;
    }

    public void setNianfenModel(IPropertySelectionModel _value) {
        _NianfenModel = _value;
    }

	// 月份下拉框
	private static IPropertySelectionModel _YuefenModel;
	
	public IPropertySelectionModel getYuefenModel() {
	    if (_YuefenModel == null) {
	        getYuefenModels();
	    }
	    return _YuefenModel;
	}
	
	private IDropDownBean _YuefenValue;
	
	public IDropDownBean getYuefenValue() {
	    if (_YuefenValue == null) {
	        int _yuefen = DateUtil.getMonth(new Date());
	        if (_yuefen == 1) {
	            _yuefen = 12;
	        } else {
	            _yuefen = _yuefen - 1;
	        }
	        for (int i = 0; i < getYuefenModel().getOptionCount(); i++) {
	            Object obj = getYuefenModel().getOption(i);
	            if (_yuefen == ((IDropDownBean) obj).getId()) {
	                _YuefenValue = (IDropDownBean) obj;
	                break;
	            }
	        }
	    }
	    return _YuefenValue;
	}
	
	public void setYuefenValue(IDropDownBean Value) {
    	if  (_YuefenValue!=Value){
    		_YuefenValue = Value;
    	}
	}

    public IPropertySelectionModel getYuefenModels() {
        List listYuefen = new ArrayList();
        for (int i = 1; i < 13; i++) {
            listYuefen.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _YuefenModel = new IDropDownModel(listYuefen);
        return _YuefenModel;
    }

    public void setYuefenModel(IPropertySelectionModel _value) {
        _YuefenModel = _value;
    }	
//	 年份
	private static IPropertySelectionModel _NianfModel;
	
	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null||_NianfValue.equals("")) {
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// 月份
	public boolean Changeyuef = false;

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
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue!= null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;
		
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}
	


//	 得到登陆用户所在电厂或者分公司的名称
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return diancmc;

	}
	//得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getIDropDownDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}
	
	//得到电厂的默认到站
	public String getDiancDaoz(){
		String daoz = "";
		String treeid=this.getTreeid();
		if(treeid==null||treeid.equals("")){
			treeid="1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = "+treeid+"");

			ResultSet rs = con.getResultSet(sql.toString());
			
			while (rs.next()) {
				 daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		return daoz;
	}
	
	
	private String treeid;
	/*public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
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
		return getTree().getWindowTreeScript();
	}
	
	
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
}