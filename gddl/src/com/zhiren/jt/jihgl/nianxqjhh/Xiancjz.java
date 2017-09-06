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
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Xiancjz extends BasePage implements PageValidateListener {
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
		
//		 工具栏的年份和月份下拉框
		
		long intyear;
		long lastyear;
		long nextyear;
		if (getNianfValue().getId() == 0) {
			intyear = DateUtil.getYear(new Date())-1;
			lastyear=intyear-1;
			nextyear=DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId()-1;
			lastyear=intyear-1;
			nextyear=getNianfValue().getId();
		}
		
		long diancxxb_id=Long.parseLong(this.getTreeid());
		//判断是否有数据，如果没有就添加
		
		String sbsql="select * from nianxqjhh where nianf="+nextyear+" and jizzt=0 and diancxxb_id="+diancxxb_id;
		
		if (!con.getHasIt(sbsql)){//如果没有数据添加一行新的数据。
			Insert();
		}
		
		con.setAutoCommit(false);
		int flag =0;

		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange().replaceAll("&nbsp;", ""));//把getChange中的&nbsp;替换成"",否则ext不能识别
		
		String zhi_sn1="";
		String zhi_jn1="";
		String zhi_nn1="";
		
		//定义SQL
		StringBuffer snsb1 = new StringBuffer();
		StringBuffer jnsb1 = new StringBuffer();
		StringBuffer nnsb1 = new StringBuffer();
	
		//定义数据
		StringBuffer snsj1 = new StringBuffer();
		StringBuffer jnsj1 = new StringBuffer();
		StringBuffer nnsj1 = new StringBuffer();
		
		snsb1.append("update nianxqjhh set ");
		jnsb1.append("update nianxqjhh set ");
		nnsb1.append("update nianxqjhh set ");
		
		while (rsl.next()) {
				
			switch(rsl.getInt("LANC")){
				case 1:
					zhi_sn1 = "FADL="+rsl.getString("lastyear");
					zhi_jn1 = "FADL="+rsl.getString("intyear");
					zhi_nn1 = "FADL="+rsl.getString("nextyear");
					break;
				case 2:
					zhi_sn1 = "GONGDBZMH="+rsl.getString("lastyear");
					zhi_jn1 = "GONGDBZMH="+rsl.getString("intyear");
					zhi_nn1 = "GONGDBZMH="+rsl.getString("nextyear");
					break;
				case 3:
					zhi_sn1 = "ZONGHCYDL="+rsl.getString("lastyear");
					zhi_jn1 = "ZONGHCYDL="+rsl.getString("intyear");
					zhi_nn1 = "ZONGHCYDL="+rsl.getString("nextyear");
					break;
				case 4:
					zhi_sn1 = "FADBHM="+rsl.getString("lastyear");
					zhi_jn1 = "FADBHM="+rsl.getString("intyear");
					zhi_nn1 = "FADBHM="+rsl.getString("nextyear");
					break;
				case 5:
					zhi_sn1 = "GONGRL="+rsl.getString("lastyear");
					zhi_jn1 = "GONGRL="+rsl.getString("intyear");
					zhi_nn1 = "GONGRL="+rsl.getString("nextyear");
					break;
				case 6:
					zhi_sn1 = "GONGRBZMH="+rsl.getString("lastyear");
					zhi_jn1 = "GONGRBZMH="+rsl.getString("intyear");
					zhi_nn1 = "GONGRBZMH="+rsl.getString("nextyear");
					break;
				case 7:
					zhi_sn1 = "FADXYBML="+rsl.getString("lastyear");
					zhi_jn1 = "FADXYBML="+rsl.getString("intyear");
					zhi_nn1 = "FADXYBML="+rsl.getString("nextyear");
					break;
				case 8:
					zhi_sn1 = "GONGRXYBML="+rsl.getString("lastyear");
					zhi_jn1 = "GONGRXYBML="+rsl.getString("intyear");
					zhi_nn1 = "GONGRXYBML="+rsl.getString("nextyear");
					break;
				case 9:
					zhi_sn1 = "XUYBML="+rsl.getString("lastyear");
					zhi_jn1 = "XUYBML="+rsl.getString("intyear");
					zhi_nn1 = "XUYBML="+rsl.getString("nextyear");
					break;
				case 10:
					zhi_sn1 = "DIANHZRYL="+rsl.getString("lastyear");
					zhi_jn1 = "DIANHZRYL="+rsl.getString("intyear");
					zhi_nn1 = "DIANHZRYL="+rsl.getString("nextyear");
					break;
				case 11:
					zhi_sn1 = "YOUFRL="+rsl.getString("lastyear");
					zhi_jn1 = "YOUFRL="+rsl.getString("intyear");
					zhi_nn1 = "YOUFRL="+rsl.getString("nextyear");
					break;
				case 12: 
					zhi_sn1 = "";
					zhi_jn1 = "";
					zhi_nn1 = "";
					continue;
				case 13:
					zhi_sn1 = "RULRZ="+rsl.getString("lastyear");
					zhi_jn1 = "RULRZ="+rsl.getString("intyear");
					zhi_nn1 = "RULRZ="+rsl.getString("nextyear");
					break;	
				case 14:
					zhi_sn1 = "FADXYML="+rsl.getString("lastyear");
					zhi_jn1 = "FADXYML="+rsl.getString("intyear");
					zhi_nn1 = "FADXYML="+rsl.getString("nextyear");
					break;	
				case 15:
					zhi_sn1 = "GONGRXYML="+rsl.getString("lastyear");
					zhi_jn1 = "GONGRXYML="+rsl.getString("intyear");
					zhi_nn1 = "GONGRXYML="+rsl.getString("nextyear");
					break;	
				case 16:
					zhi_sn1 = "XUYYML="+rsl.getString("lastyear");
					zhi_jn1 = "XUYYML="+rsl.getString("intyear");
					zhi_nn1 = "XUYYML="+rsl.getString("nextyear");
					break;	
				case 17:
					zhi_sn1 = "QITY="+rsl.getString("lastyear");
					zhi_jn1 = "QITY="+rsl.getString("intyear");
					zhi_nn1 = "QITY="+rsl.getString("nextyear");
					break;
				case 18:
					zhi_sn1 = "YUNS="+rsl.getString("lastyear");
					zhi_jn1 = "YUNS="+rsl.getString("intyear");
					zhi_nn1 = "YUNS="+rsl.getString("nextyear");
					break;
				case 19:
					zhi_sn1 = "QICKC="+rsl.getString("lastyear");
					zhi_jn1 = "QICKC="+rsl.getString("intyear");
					zhi_nn1 = "QICKC="+rsl.getString("nextyear");
					break;
				case 20:
					zhi_sn1 = "QIMKC="+rsl.getString("lastyear");
					zhi_jn1 = "QIMKC="+rsl.getString("intyear");
					zhi_nn1 = "QIMKC="+rsl.getString("nextyear");
					break;
				case 21:
					zhi_sn1 = "ZONGXQL="+rsl.getString("lastyear");
					zhi_jn1 = "ZONGXQL="+rsl.getString("intyear");
					zhi_nn1 = "ZONGXQL="+rsl.getString("nextyear");
					break;	
				case 22:
					zhi_sn1 = "ZHUANGJRL="+rsl.getString("lastyear");
					zhi_jn1 = "ZHUANGJRL="+rsl.getString("intyear");
					zhi_nn1 = "ZHUANGJRL="+rsl.getString("nextyear");
					break;
				case 23:
					zhi_sn1 = "MIAOS='"+rsl.getString("lastyear")+"'";
					zhi_jn1 = "MIAOS='"+rsl.getString("intyear")+"'";
					zhi_nn1 = "MIAOS='"+rsl.getString("nextyear")+"'";
					break;
				case 24:
					zhi_sn1 = "TOUCRQ='"+rsl.getString("lastyear")+"'";
					zhi_jn1 = "TOUCRQ='"+rsl.getString("intyear")+"'";
					zhi_nn1 = "TOUCRQ='"+rsl.getString("nextyear")+"'";
					break;
				case 25:
					zhi_sn1 = "SHEJMZ='"+rsl.getString("lastyear")+"'";
					zhi_jn1 = "SHEJMZ='"+rsl.getString("intyear")+"'";
					zhi_nn1 = "SHEJMZ='"+rsl.getString("nextyear")+"'";
					break;
				case 26:
					zhi_sn1 = "BEIZ='"+rsl.getString("lastyear")+"'";
					zhi_jn1 = "BEIZ='"+rsl.getString("intyear")+"'";
					zhi_nn1 = "BEIZ='"+rsl.getString("nextyear")+"'";
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
		
		if(snsj1.length()!=0){
			snsj1.deleteCharAt(snsj1.length()-1);
			snsb1.append(snsj1);
			snsb1.append(" where ").append("nianf="+nextyear+"").append(" and ");
			snsb1.append(" diancxxb_id="+diancxxb_id+" and jizzt=0 and shujzt=").append(lastyear);
			System.out.println(snsb1.toString());
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
			jnsb1.append(" where ").append("nianf="+nextyear+"").append(" and ");
			jnsb1.append(" diancxxb_id="+diancxxb_id+" and jizzt=0 and shujzt=").append(intyear);
			System.out.println(jnsb1.toString());
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
			nnsb1.append(" where ").append("nianf="+nextyear+"").append(" and ");
			nnsb1.append(" diancxxb_id="+diancxxb_id+" and jizzt=0 and shujzt=").append(nextyear);
			System.out.println(nnsb1.toString());
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
		
		//---------------------
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
	public void Shenh(){//	审核按钮
		//工具栏的年份和月份下拉框
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
			str = "and (dc.shangjgsid = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and dc.id = " + getTreeid() + "";
		}
		//查询数据时的状态
		if(visit.getRenyjb()==3){
			chaxzt="";
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
		"       and y.nianf="+intyear+chaxzt+str+" order by y.id";
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
			str = "and (dc.shangjgsid = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and dc.id = " + getTreeid() + "";
		}
		//查询数据时的状态
		if(visit.getRenyjb()==3){
			chaxzt="";
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
		"      and y.riq="+CurrODate+chaxzt+str+" order by y.id";
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
		JDBCcon con = new JDBCcon();
		//工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}

		//删除本月和累计的数据
		//long id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
		long diancxxb_id=Long.parseLong(this.getTreeid());
		String DeleteStr="delete nianxqjhh y where y.nianf=" + intyear + " and y.diancxxb_id="+diancxxb_id+"";
		con.getDelete(DeleteStr);
		con.Close();
	}
	
//	定义两维数组-存储去年和今年的数据
	private String shuj[][]= new String[2][26];
	
	/**
	 * 得到上年和去年的数据
	 * @param (参数有JDBCcon,年份，电厂ID,行)
	 */
	public void getQus(JDBCcon con2,long year,long shujlx,String yuefen,long diancxxb_id,int row){//得到上年和去年的数据(参数有年份，电厂ID)
		String sql_sn="select to_char(n.gongdbzmh) as gongdbzmh,to_char(n.zonghcydl) as zonghcydl," +
		  " to_char(n.fadbhm) as fadbhm,to_char(n.gongrl) as gongrl,to_char(n.gongrbzmh) as gongrbzmh, " +
		  " to_char(n.fadxybml) as fadxybml,to_char(n.gongrxybml) as gongrxybml,to_char(n.xuybml) as xuybml," +
		  " to_char(n.dianhzryl) as dianhzryl,to_char(n.youfrl) as youfrl, " +
		  " to_char(n.zongxql-n.youfrl) xuymzbml,to_char(n.rulrz) as rulrz,to_char(n.fadxyml) as fadxyml," +
		  " to_char(n.gongrxyml) as gongrxyml, " +
		  " to_char(n.xuyyml) as xuyyml,to_char(n.qity) as qity,to_char(n.yuns) as yuns," +
		  " to_char(n.qickc) as qickc,to_char(n.qimkc) as qimkc,to_char(n.zongxql) as zongxql " +
		  " from nianxqjhh n where n.nianf="+(year-1)+" and n.jizzt=0 and n.shujzt='"+(shujlx-1)+"' " +
		" and n.diancxxb_id="+diancxxb_id;

		if(!con2.getHasIt(sql_sn)){//判断去年是否有数据，如果没有从月报里取
		sql_sn = 
			"select \n" +// yz.diancxxb_id,yz.fenx,
			"to_char(round_new(yz.fadl,2)) as fadl,\n" + //万千瓦时
			"to_char(round_new(yz.gongdbzmh,2)) as gongdbzmh,\n" + //克/千瓦时
			"to_char(round_new(yz.FADZHCGDL,2)) as zonghcydl,\n" + //克/千瓦时
			"to_char(round_new(yz.FADBZMH,2)) as fadbhm,\n" + //克/千瓦时
			"to_char(round_new(yz.gongrl,2)) as gongrl,\n" + //吉焦
			"to_char(round_new(yz.gongrbzmh,2)) as gongrbzmh,\n" + //千克/吉焦
			"to_char(round_new((yz.fadl*10000*yz.gongdbzmh)/1000000,2)) as fadxybml,\n" + //万吨(万千瓦时*10000*克/千瓦时)/1000000
			"to_char(round_new((yz.gongrl*yz.gongrbzmh)/1000,2)) as gongrxybml,\n" + //万吨 (吉焦*千克/吉焦)/1000
			"to_char(round_new(((yz.fadl*10000*yz.gongdbzmh)/1000000+(yz.gongrl*yz.gongrbzmh)/1000),2)) as xuybml,\n" + //万吨
			"'0' as dianhzryl,\n" + //吨
			"to_char(round_new(yz.RULTRYPJFRL,2)) as youfrl,\n" + //MJ/KG
			"to_char(round_new(((decode(yz.RULTRMPJFRL,0,0,(yz.fadl*10000*yz.gongdbzmh)/1000*29.271/yz.RULTRMPJFRL/1000) +\n" + 
			"            decode(yz.RULTRMPJFRL,0,0,(yz.gongrl*yz.gongrbzmh)*29.271/yz.RULTRMPJFRL/1000)+yz.qitfy)-yz.RULTRYPJFRL),2)) as xuymzbml,\n" + //MJ/KG
			"to_char(round_new(yz.RULTRMPJFRL,2)) as rulrz,\n" + //MJ/KG
			"to_char(round_new(decode(yz.RULTRMPJFRL,0,0,(yz.fadl*10000*yz.gongdbzmh)/1000*29.271/yz.RULTRMPJFRL/1000),2)) as fadxyml,\n" + //万吨
			"to_char(round_new(decode(yz.RULTRMPJFRL,0,0,(yz.gongrl*yz.gongrbzmh)*29.271/yz.RULTRMPJFRL/1000),2)) as gongrxyml,\n" + //万吨	
			"to_char(round_new(decode(yz.RULTRMPJFRL,0,0,(yz.fadl*10000*yz.gongdbzmh)/1000*29.271/yz.RULTRMPJFRL/1000) +\n" + 
			"          decode(yz.RULTRMPJFRL,0,0,(yz.gongrl*yz.gongrbzmh)*29.271/yz.RULTRMPJFRL/1000),2)) as xuyyml,\n" + //万吨
			"to_char(round_new(yz.qitfy,2)) as qity,'0' as yuns,'0' as qickc,'0' as qimkc,\n" + //万吨
			"to_char(round_new((decode(yz.RULTRMPJFRL,0,0,(yz.fadl*10000*yz.gongdbzmh)/1000*29.271/yz.RULTRMPJFRL/1000) +\n" + 
			"    decode(yz.RULTRMPJFRL,0,0,(yz.gongrl*yz.gongrbzmh)*29.271/yz.RULTRMPJFRL/1000)+yz.qitfy),2)) as zongxql\n" + //万吨
			"from yuezbb yz\n" + 
			" where yz.riq=to_date('" + (year-1) + "-"+yuefen+"-01','yyyy-mm-dd') and yz.fenx='累计' \n" +
			"   and yz.diancxxb_id="+diancxxb_id+"";
		}
		
		ResultSetList rs_sn=con2.getResultSetList(sql_sn);
		
		if(rs_sn.next()){
		for (int j=0;j<20;j++){
			shuj[row][j]=""+rs_sn.getString(j);
		}
		}
		con2.Close();
	}
	
	public void Insert(){
		JDBCcon con1 = new JDBCcon();
		//工具栏的年份和月份下拉框
		long intyear;
		long lastyear;
		long nextyear;
		if (getNianfValue().getId() == 0) {
			intyear = DateUtil.getYear(new Date())-1;
			lastyear=intyear-1;
			nextyear=DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId()-1;
			lastyear=intyear-1;
			nextyear=getNianfValue().getId();
		}
		
		/**
		 * 数据字段：去年(从去年的nianxqjhh表里取，如果没有从去年的月报里取)，今年（从今年的月报里取）。
		 * 0 fadl_xj 发电量－录入
		 * 1 gongdbzmh_xj 供电标准煤耗－录入
		 * 2 zonghcydl_xj 综合厂用电率－录入
		 * 3 fadbhm_xj 发电标准煤耗－录入
		 * 4 gongrl_xj 热量－录入
		 * 5 gongrbzmh_xj 供热标准煤耗－录入
		 * 6 fadxybml_xj 发电需用标煤量--计算（发电量*供电标准煤耗）
		 * 7 gongrxybml_xj 供热需用标煤量--计算（供热量*供热标准煤耗）
		 * 8 xuybml_xj 需用标煤量--计算（发电需用标煤量+供热需用标煤量）
		 * 9 dianhzryl_xj 点火助燃油量－录入
		 * 10 youfrl_xj 油发热量－录入
		 * 11 xuymzbml_xj 需用油折标煤量--计算（总需求量-油发热量）
		 * 12 rulrz_xj 入炉热值－录入
		 * 13 fadxyml_xj 发电需原煤量--计算（发电需用标煤量*29.271/入炉热值）
		 * 14 gongrxyml_xj 供热需原煤量--计算（供热需用标煤量*29.271/入炉热值）
		 * 15 xuyyml_xj 需用原煤量--计算（发电需原煤量+供热需原煤量）
		 * 16 qity_xj 其他用－录入
		 * 17 yuns_xj 运损－录入
		 * 18 qickc_xj 期初库存－录入
		 * 19 qimkc_xj 期未库存－录入
		 * 20 zongxql_xj 总需求量--计算（需用原煤量+其他用+运损+期未库存-期初库存）
		 * 21 ZHUANGJRL 装机容量
		 * 22 MIAOS 装机容量描述
		 * 23 TOUCRQ 投产日期
		 * 24 SHEJMZ 设计煤种
		 * 25 BEIZ 备注
		 */
		
		for (int i=0;i<shuj.length;i++){
			for (int j=0;j<shuj[0].length;j++){
				shuj[i][j]="0";
			}
		}
		
		
		//判断状态
		long zhuangt =0;//数据状态
		String gongsshr="";//公司审核人
		String gongsshsj=DateUtil.FormatDate(new Date());//公司审核时间
		String jitshr="";//集团审核人
		String jitshsj=DateUtil.FormatDate(new Date());//集团审核时间
		long id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));//得到一个新的ID
		long diancxxb_id=Long.parseLong(this.getTreeid());//得到电厂信息表ID
		
		//取得去年的数据(从去年的nianxqjhh表里取，如果没有从去年的月报里取)
		getQus(con1,intyear,lastyear,"12",diancxxb_id,0);
		//取得今年的数据(从今年的nianxqjhh表里取，如果没有从今年的月报里取)
		String yuefen="12";
		if(getRbvalue()!=null && !getRbvalue().equals("")){
			if (Long.parseLong(getRbvalue())<10){
				yuefen="0"+Long.parseLong(getRbvalue());
			}else{
				yuefen=getRbvalue();
			}
		}
		getQus(con1,nextyear,intyear,yuefen,diancxxb_id,1);
		
		shuj[1][10]="41.816";//油发热量
	
		//***********插入上年数据***************//		
		//新增机－机组状态为1
		String insert_sql1="insert into nianxqjhh(ID,DIANCXXB_ID,NIANF,FADL,FADBHM,ZONGHCYDL," +
				"GONGRL,GONGRBZMH,RULRZ,FADXYBML,GONGRXYBML,XUYBML,FADXYML,GONGRXYML,XUYYML," +
				"DIANHZRYL,ZHUANGT,JIZZT,SHUJZT,GONGSSHR,GONGSSHSJ,JITSHR,JITSHSJ,JISFDL," +
				"GONGRBML,BIAOMH,ZHUANGJRL,MIAOS,TOUCRQ,SHEJMZ,BEIZ,QITY,YUNS,QICKC,QIMKC,ZONGXQL,GONGDBZMH,YOUFRL) values (" +
					id+","+diancxxb_id+","+nextyear+","+shuj[0][0]+","+shuj[0][3]+","+shuj[0][2]+","+
					shuj[0][4]+","+shuj[0][5]+","+shuj[0][12]+","+shuj[0][6]+","+shuj[0][7]+","+
					shuj[0][8]+","+shuj[0][13]+","+shuj[0][14]+","+shuj[0][15]+","+
					shuj[0][9]+","+zhuangt+",0,"+lastyear+",'"+gongsshr+"',to_date('"+gongsshsj+"','yyyy-mm-dd'),'"+
					jitshr+"',to_date('"+jitshsj+"','yyyy-mm-dd'),0,0,0,"+shuj[0][21]+",'"+shuj[0][22]+"','"+
					shuj[0][23]+"','"+shuj[0][24]+"','"+shuj[0][25]+"',"+shuj[0][16]+","+shuj[0][17]+","+
					shuj[0][18]+","+shuj[0][19]+","+shuj[0][20]+","+shuj[0][1]+","+shuj[0][10]+")";
		con1.getInsert(insert_sql1);
		
		//***********插入今年数据***************//		
		//新增机－机组状态为1
		String insert_sql2="insert into nianxqjhh(ID,DIANCXXB_ID,NIANF,FADL,FADBHM,ZONGHCYDL," +
				"GONGRL,GONGRBZMH,RULRZ,FADXYBML,GONGRXYBML,XUYBML,FADXYML,GONGRXYML,XUYYML," +
				"DIANHZRYL,ZHUANGT,JIZZT,SHUJZT,GONGSSHR,GONGSSHSJ,JITSHR,JITSHSJ,JISFDL," +
				"GONGRBML,BIAOMH,ZHUANGJRL,MIAOS,TOUCRQ,SHEJMZ,BEIZ,QITY,YUNS,QICKC,QIMKC,ZONGXQL,GONGDBZMH,YOUFRL) values (" +
					id+","+diancxxb_id+","+nextyear+","+shuj[1][0]+","+shuj[1][3]+","+shuj[1][2]+","+
					shuj[1][4]+","+shuj[1][5]+","+shuj[1][12]+","+shuj[1][6]+","+shuj[1][7]+","+
					shuj[1][8]+","+shuj[1][13]+","+shuj[1][14]+","+shuj[1][15]+","+
					shuj[1][9]+","+zhuangt+",0,"+intyear+",'"+gongsshr+"',to_date('"+gongsshsj+"','yyyy-mm-dd'),'"+
					jitshr+"',to_date('"+jitshsj+"','yyyy-mm-dd'),0,0,0,"+shuj[1][21]+",'"+shuj[1][22]+"','"+
					shuj[1][23]+"','"+shuj[1][24]+"','"+shuj[1][25]+"',"+shuj[1][16]+","+shuj[1][17]+","+
					shuj[1][18]+","+shuj[1][19]+","+shuj[1][20]+","+shuj[1][1]+","+shuj[1][10]+")";
		con1.getInsert(insert_sql2);
		
		//***********插入明年数据***************//	
		//新增机－机组状态为1
		String insert_sql3="insert into nianxqjhh(ID,DIANCXXB_ID,NIANF,FADL,FADBHM,ZONGHCYDL," +
		"GONGRL,GONGRBZMH,RULRZ,FADXYBML,GONGRXYBML,XUYBML,FADXYML,GONGRXYML,XUYYML," +
		"DIANHZRYL,ZHUANGT,JIZZT,SHUJZT,GONGSSHR,GONGSSHSJ,JITSHR,JITSHSJ,JISFDL," +
		"GONGRBML,BIAOMH,ZHUANGJRL,MIAOS,TOUCRQ,SHEJMZ,BEIZ,QITY,YUNS,QICKC,QIMKC,ZONGXQL  ) values (" +
			id+","+diancxxb_id+","+nextyear+",0,0,0,0,0,0,0,0,0,0,0,0,0," +
			"0,0,"+nextyear+",'"+gongsshr+"',to_date('"+gongsshsj+"','yyyy-mm-dd'),'"+
					jitshr+"',to_date('"+jitshsj+"','yyyy-mm-dd'),0,0,0,0,'0','0','0','0',0,0,0,0,0)";
		con1.getInsert(insert_sql3);
		
		con1.Close();
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
		if (getNianfValue().getId() == 0) {
			intyear = DateUtil.getYear(new Date())-1;
			lastyear=intyear-1;
			nextyear=DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId()-1;
			lastyear=intyear-1;
			nextyear=getNianfValue().getId();
		}
		
		int treejib = this.getDiancTreeJib();

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
		
		String jizzt= "and yx.jizzt=0";

//		if(isReture) {
//			setMsg("如果有需要回退信息时，请选择所要回退的公司或电厂名称！");
//		}
//	----------------------------------------------------------------------	
		
		String sbsql1="select * from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and  nianf="+nextyear+" and jizzt=0 "+zhuangt+" and diancxxb_id="+getTreeid();
		
		if (!con.getHasIt(sbsql1)){//如果没有数据添加一行新的数据。
			Insert();
		}

		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("select lanc, \n");//decode(jizlx,null,'现存机组',jizlx) as jizlx,
		sbsql.append("decode(mingc,null,decode(lanc,1,'发电量',2,'供电标准煤耗',3,'综合厂用电率',4,'发电标准煤耗',5,'供热量',6,'供热标准煤耗', \n");
		sbsql.append("7,'发电需用标煤量',8,'供热需用标煤量',9,'需用标煤量',10,'点火助燃油量',11,'油发热量',  \n");
		sbsql.append("12,'需用油折标煤量',13,'入炉热值',14,'发电需原煤量',15,'供热需用标煤量',16,'需用原煤量',17,'其他用',18,'运损',19,'期初库存',\n");
		sbsql.append("20,'期未库存',21,'总需求量',22,'装机容量',23,'装机容量描述',24,'投产日期',25,'设计煤种',26,'备注',mingc),mingc) as mingc,zz.danw,\n");
		//sbsql.append("to_char(sum(nvl(lastyear,0))) as lastyear,to_char(sum(nvl(intyear,0))) as intyear,to_char(sum(nvl(nextyear,0))) as nextyear, \n");
		sbsql.append("to_char(max(nvl(lastyear,0))) as lastyear,to_char(max(nvl(intyear,0))) as intyear,to_char(max(nvl(nextyear,0))) as nextyear, \n");
		sbsql.append("decode("+visit.getRenyjb()+",3,decode(zz.zhuangt,0,'未提交','已提交'),2,decode(zz.zhuangt,1,'未审核','已审核'),3,decode(zz.zhuangt,2,'未审核','已审核')) as zhuangt \n");
		sbsql.append("from (  \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'万千瓦时' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 1 as lanc,'发电量' as mingc,'万千瓦时' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 1 as lanc,decode(to_char(yx.fadl,'FM99999990.00'),0,'0',to_char(yx.fadl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"  ) z1, \n");
		sbsql.append("(select 1 as lanc,decode(to_char(yx.fadl,'FM99999990.00'),0,'0',to_char(yx.fadl,'FM99999990.00'))  as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 1 as lanc,decode(to_char(yx.fadl,'FM99999990.00'),0,'0',to_char(yx.fadl,'FM99999990.00'))  as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'克/千瓦时' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 2 as lanc,'供电标准煤耗' as mingc,'克/千瓦时' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 2 as lanc,decode(to_char(yx.gongdbzmh,'FM99999990.00'),0,'0',to_char(yx.gongdbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 2 as lanc,decode(to_char(yx.gongdbzmh,'FM99999990.00'),0,'0',to_char(yx.gongdbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 2 as lanc,decode(to_char(yx.gongdbzmh,'FM99999990.00'),0,'0',to_char(yx.gongdbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'克/千瓦时' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 3 as lanc,'综合厂用电率' as mingc,'克/千瓦时' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 3 as lanc,decode(to_char(yx.zonghcydl,'FM99999990.00'),0,'0',to_char(yx.zonghcydl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 3 as lanc,decode(to_char(yx.zonghcydl,'FM99999990.00'),0,'0',to_char(yx.zonghcydl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 3 as lanc,decode(to_char(yx.zonghcydl,'FM99999990.00'),0,'0',to_char(yx.zonghcydl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'克/千瓦时' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 4 as lanc,'发电标准煤耗' as mingc,'克/千瓦时' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 4 as lanc,decode(to_char(yx.fadbhm,'FM99999990.00'),0,'0',to_char(yx.fadbhm,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 4 as lanc,decode(to_char(yx.fadbhm,'FM99999990.00'),0,'0',to_char(yx.fadbhm,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 4 as lanc,decode(to_char(yx.fadbhm,'FM99999990.00'),0,'0',to_char(yx.fadbhm,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'吉焦' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 5 as lanc,'供热量' as mingc,'吉焦' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 5 as lanc,decode(to_char(yx.gongrl,'FM99999990.00'),0,'0',to_char(yx.gongrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 5 as lanc,decode(to_char(yx.gongrl,'FM99999990.00'),0,'0',to_char(yx.gongrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 5 as lanc,decode(to_char(yx.gongrl,'FM99999990.00'),0,'0',to_char(yx.gongrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'千克/吉焦' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 6 as lanc,'供热标准煤耗' as mingc,'千克/吉焦' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 6 as lanc,decode(to_char(yx.gongrbzmh,'FM99999990.00'),0,'0',to_char(yx.gongrbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 6 as lanc,decode(to_char(yx.gongrbzmh,'FM99999990.00'),0,'0',to_char(yx.gongrbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 6 as lanc,decode(to_char(yx.gongrbzmh,'FM99999990.00'),0,'0',to_char(yx.gongrbzmh,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 7 as lanc,'发电需用标煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 7 as lanc,decode(to_char(yx.fadxybml,'FM99999990.00'),0,'0',to_char(yx.fadxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 7 as lanc,decode(to_char(yx.fadxybml,'FM99999990.00'),0,'0',to_char(yx.fadxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 7 as lanc,decode(to_char(yx.fadxybml,'FM99999990.00'),0,'0',to_char(yx.fadxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 8 as lanc,'供热需用标煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 8 as lanc,decode(to_char(yx.gongrxybml,'FM99999990.00'),0,'0',to_char(yx.gongrxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 8 as lanc,decode(to_char(yx.gongrxybml,'FM99999990.00'),0,'0',to_char(yx.gongrxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 8 as lanc,decode(to_char(yx.gongrxybml,'FM99999990.00'),0,'0',to_char(yx.gongrxybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 9 as lanc,'需用标煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 9 as lanc,decode(to_char(yx.xuybml,'FM99999990.00'),0,'0',to_char(yx.xuybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 9 as lanc,decode(to_char(yx.xuybml,'FM99999990.00'),0,'0',to_char(yx.xuybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 9 as lanc,decode(to_char(yx.xuybml,'FM99999990.00'),0,'0',to_char(yx.xuybml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 10 as lanc,'点火助燃油量' as mingc,'吨' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 10 as lanc,decode(to_char(yx.dianhzryl,'FM99999990.00'),0,'0',to_char(yx.dianhzryl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 10 as lanc,decode(to_char(yx.dianhzryl,'FM99999990.00'),0,'0',to_char(yx.dianhzryl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 10 as lanc,decode(to_char(yx.dianhzryl,'FM99999990.00'),0,'0',to_char(yx.dianhzryl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'Mj/kg' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 11 as lanc,'油发热量' as mingc,'Mj/kg' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 11 as lanc,decode(to_char(yx.youfrl,'FM99999990.00'),0,'0',to_char(yx.youfrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 11 as lanc,decode(to_char(yx.youfrl,'FM99999990.00'),0,'0',to_char(yx.youfrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 11 as lanc,decode(to_char(yx.youfrl,'FM99999990.00'),0,'0',to_char(yx.youfrl,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'Mj/kg' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 12 as lanc,'需用油折标煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 12 as lanc,decode(to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00'),0,'0',to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 12 as lanc,decode(to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00'),0,'0',to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 12 as lanc,decode(to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00'),0,'0',to_char((yx.dianhzryl*nvl(yx.youfrl,41.816)/29.271),'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,'Mj/kg' as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 13 as lanc,'入炉热值' as mingc,'Mj/kg' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 13 as lanc,decode(to_char(yx.rulrz,'FM99999990.00'),0,'0',to_char(yx.rulrz,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 13 as lanc,decode(to_char(yx.rulrz,'FM99999990.00'),0,'0',to_char(yx.rulrz,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 13 as lanc,decode(to_char(yx.rulrz,'FM99999990.00'),0,'0',to_char(yx.rulrz,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 14 as lanc,'发电需原煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 14 as lanc,decode(to_char(yx.fadxyml,'FM99999990.00'),0,'0',to_char(yx.fadxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 14 as lanc,decode(to_char(yx.fadxyml,'FM99999990.00'),0,'0',to_char(yx.fadxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 14 as lanc,decode(to_char(yx.fadxyml,'FM99999990.00'),0,'0',to_char(yx.fadxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 15 as lanc,'供热需原煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 15 as lanc,decode(to_char(yx.gongrxyml,'FM99999990.00'),0,'0',to_char(yx.gongrxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 15 as lanc,decode(to_char(yx.gongrxyml,'FM99999990.00'),0,'0',to_char(yx.gongrxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 15 as lanc,decode(to_char(yx.gongrxyml,'FM99999990.00'),0,'0',to_char(yx.gongrxyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 16 as lanc,'需用原煤量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 16 as lanc,decode(to_char(yx.xuyyml,'FM99999990.00'),0,'0',to_char(yx.xuyyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 16 as lanc,decode(to_char(yx.xuyyml,'FM99999990.00'),0,'0',to_char(yx.xuyyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 16 as lanc,decode(to_char(yx.xuyyml,'FM99999990.00'),0,'0',to_char(yx.xuyyml,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
//		新增：其他用，运损，期初库存，期未库存
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 17 as lanc,'其他用' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 17 as lanc,decode(to_char(yx.qity,'FM99999990.00'),0,'0',to_char(yx.qity,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 17 as lanc,decode(to_char(yx.qity,'FM99999990.00'),0,'0',to_char(yx.qity,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 17 as lanc,decode(to_char(yx.qity,'FM99999990.00'),0,'0',to_char(yx.qity,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 18 as lanc,'运损' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 18 as lanc,decode(to_char(yx.yuns,'FM99999990.00'),0,'0',to_char(yx.yuns,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 18 as lanc,decode(to_char(yx.yuns,'FM99999990.00'),0,'0',to_char(yx.yuns,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 18 as lanc,decode(to_char(yx.yuns,'FM99999990.00'),0,'0',to_char(yx.yuns,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 19 as lanc,'期初库存' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 19 as lanc,decode(to_char(yx.qickc,'FM99999990.00'),0,'0',to_char(yx.qickc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 19 as lanc,decode(to_char(yx.qickc,'FM99999990.00'),0,'0',to_char(yx.qickc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 19 as lanc,decode(to_char(yx.qickc,'FM99999990.00'),0,'0',to_char(yx.qickc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 20 as lanc,'期未库存' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 20 as lanc,decode(to_char(yx.qimkc,'FM99999990.00'),0,'0',to_char(yx.qimkc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 20 as lanc,decode(to_char(yx.qimkc,'FM99999990.00'),0,'0',to_char(yx.qimkc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 20 as lanc,decode(to_char(yx.qimkc,'FM99999990.00'),0,'0',to_char(yx.qimkc,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 21 as lanc,'总需求量' as mingc,'万吨' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 21 as lanc,decode(to_char(yx.zongxql,'FM99999990.00'),0,'0',to_char(yx.zongxql,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 21 as lanc,decode(to_char(yx.zongxql,'FM99999990.00'),0,'0',to_char(yx.zongxql,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 21 as lanc,decode(to_char(yx.zongxql,'FM99999990.00'),0,'0',to_char(yx.zongxql,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 22 as lanc,'装机容量' as mingc,'万千瓦' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 22 as lanc,decode(to_char(yx.ZHUANGJRL,'FM99999990.00'),0,'0',to_char(yx.ZHUANGJRL,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 22 as lanc,decode(to_char(yx.ZHUANGJRL,'FM99999990.00'),0,'0',to_char(yx.ZHUANGJRL,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 22 as lanc,decode(to_char(yx.ZHUANGJRL,'FM99999990.00'),0,'0',to_char(yx.ZHUANGJRL,'FM99999990.00')) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		/*sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 22 as lanc,'装机容量' as mingc,'万千瓦' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 22 as lanc,to_char(yx.ZHUANGJRL,'FM99999990.00') as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 22 as lanc,to_char(yx.ZHUANGJRL,'FM99999990.00') as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 22 as lanc,to_char(yx.ZHUANGJRL,'FM99999990.00') as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 23 as lanc,'装机容量描述' as mingc,'' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 23 as lanc,to_char(yx.MIAOS) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 23 as lanc,to_char(yx.MIAOS) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 23 as lanc,to_char(yx.MIAOS) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 24 as lanc,'投产日期' as mingc,'' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 24 as lanc,to_char(yx.TOUCRQ) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 24 as lanc,to_char(yx.TOUCRQ) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 24 as lanc,to_char(yx.TOUCRQ) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 25 as lanc,'设计煤种' as mingc,'' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 25 as lanc,to_char(yx.SHEJMZ) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 25 as lanc,to_char(yx.SHEJMZ) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 25 as lanc,to_char(yx.SHEJMZ) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");
		sbsql.append("union \n");
		sbsql.append("select t.lanc as lanc,t.mingc as mingc,z1.zhi as lastyear,z2.zhi as intyear,z3.zhi as nextyear,t.dun as danw,nvl(z1.zhuangt,0) as zhuangt from \n");
		sbsql.append("(select 26 as lanc,'备注' as mingc,'' dun,0 as zhuangt from dual) t,\n");
		sbsql.append("(select 26 as lanc,to_char(yx.BEIZ) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+lastyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z1, \n");
		sbsql.append("(select 26 as lanc,to_char(yx.BEIZ) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+intyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z2, \n");
		sbsql.append("(select 26 as lanc,to_char(yx.BEIZ) as zhi,nvl(yx.zhuangt,0) as zhuangt from nianxqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.shujzt="+nextyear+" and yx.diancxxb_id="+getTreeid()+" and yx.nianf="+nextyear+" "+jizzt+" "+zhuangt+"   ) z3 \n");
		sbsql.append(" where t.lanc=z1.lanc(+) and t.lanc=z2.lanc(+) and t.lanc=z3.lanc(+)  ");*/
		
		sbsql.append(") zz \n");
		sbsql.append("group by (lanc,mingc,zz.danw,zz.zhuangt) \n");
		sbsql.append("order by lanc asc \n");

		  
	    //System.out.println(sbsql.toString());
	    
		ResultSetList rsl = con.getResultSetList(sbsql.toString());
		boolean yincan = false;
		boolean falge = false;
		while (rsl.next()) {
			falge = true;
			if (visit.getRenyjb() == 3) {// 电厂用户
				if (rsl.getString("zhuangt").equals("已提交")) {
					yincan = true;
				} else {
					yincan = false;
				}
			} else if (visit.getRenyjb() == 2) {// 集团用户
				if (rsl.getString("zhuangt").equals("已审核")) {
					yincan = true;
				} else {
					yincan = false;
				}
			}
		}
		rsl.beforefirst();
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("nianxqjhh");

		// egu.getColumn("jizlx").setCenterHeader("机组类型");
		// egu.getColumn("jizlx").setEditor(null);
		egu.getColumn("lanc").setCenterHeader("栏次");
		egu.getColumn("lanc").setEditor(null);
		egu.getColumn("mingc").setCenterHeader("项目");
		egu.getColumn("mingc").setEditor(null);
		if (getRbvalue() == null || getRbvalue().equals("")) {
			egu.getColumn("intyear").setCenterHeader(intyear + "年已完成");
		} else {
			egu.getColumn("intyear").setCenterHeader(
					intyear + "年" + getRbvalue() + "月已完成");
		}
		egu.getColumn("lastyear").setCenterHeader(lastyear + "完成");
		egu.getColumn("nextyear").setCenterHeader(nextyear + "年预计");
		egu.getColumn("danw").setCenterHeader("单位");
		egu.getColumn("danw").setEditor(null);
		egu.getColumn("zhuangt").setCenterHeader("状态");
		//egu.getColumn("zhuangt").setHidden(true);
		egu.getColumn("zhuangt").setEditor(null);
		egu.getColumn("zhuangt").setDefaultValue("未提交");

		// 设定不可编辑列的颜色
		egu.getColumn("lanc").setRenderer("function(value,metadata){metadata.css='tdTextext4'; return value;}");
		egu.getColumn("mingc").setRenderer("function(value,metadata){metadata.css='tdTextext4'; return value;}");
		egu.getColumn("danw").setRenderer("function(value,metadata){metadata.css='tdTextext4'; return value;}");
		egu.getColumn("zhuangt").setRenderer("function(value,metadata){metadata.css='tdTextext4'; return value;}");
		// //设定单元格的底色
		egu.getColumn("lastyear").setRenderer("function(value,metadata,rec,rowIndex){if((rowIndex>5 && rowIndex<9) || rowIndex==11 || (rowIndex>12 && rowIndex<16) || rowIndex==20){metadata.css='tdTextext4';} return value;}");
		egu.getColumn("intyear").setRenderer("function(value,metadata,rec,rowIndex){if((rowIndex>5 && rowIndex<9) || rowIndex==11 || (rowIndex>12 && rowIndex<16) || rowIndex==20){metadata.css='tdTextext4';} return value;}");
		egu.getColumn("nextyear").setRenderer("function(value,metadata,rec,rowIndex){if((rowIndex>5 && rowIndex<9) || rowIndex==11 || (rowIndex>12 && rowIndex<16) || rowIndex==20){metadata.css='tdTextext4';} return value;}");

		egu.setDefaultsortable(false);// 设定页面不自动排序

		// 设定列初始宽度
		// egu.getColumn("jizlx").setWidth(70);
		// egu.getColumn("jizlx").setAlign("right");
		egu.getColumn("lanc").setWidth(70);
		egu.getColumn("lanc").setAlign("right");
		egu.getColumn("mingc").setWidth(120);
		egu.getColumn("lastyear").setWidth(80);
		egu.getColumn("intyear").setWidth(120);
		egu.getColumn("nextyear").setWidth(80);
		egu.getColumn("danw").setWidth(70);
		egu.getColumn("zhuangt").setWidth(70);

		// 设定列的小数位
		// ((NumberField)egu.getColumn("zhi1").editor).setDecimalPrecision(3);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(100);// 设置分页
		egu.setWidth(1000);// 设置页面的宽度,当超过这个宽度时显示滚动条

		// 审核标志
		
		/*ComboBox cb_ys = new ComboBox();
		egu.getColumn("zhuangt").setEditor(cb_ys);
		cb_ys.setEditable(true);
		// String ysSql = "select id,mingc from yunsfsb order by mingc";

		List list = new ArrayList();

		if (visit.getRenyjb() == 3) {// 电厂
			list.add(new IDropDownBean(0, "未提交"));
			list.add(new IDropDownBean(1, "已提交"));
		} else if (visit.getRenyjb() == 2) {// 分公司
			list.add(new IDropDownBean(1, "未审核"));
			list.add(new IDropDownBean(2, "已审核"));
		} else {// 集团
			list.add(new IDropDownBean(2, "未审核"));
			list.add(new IDropDownBean(3, "已审核"));
		}

		egu.getColumn("zhuangt").setComboEditor(egu.gridId,
				new IDropDownModel(list));
		egu.getColumn("zhuangt").setReturnId(true);*/
		
		// *************************下拉框*****************************************88
		// ********************工具栏************************************************
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("-");// 设置分隔符

		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		

		// 设定工具栏下拉框自动刷新
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// 设置分隔符

		// 刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append(
				MainGlobal.getExtMessageBox(
						"'正在刷新'+Ext.getDom('NIANF').value+'年的数据,请稍候！'", true))
				.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);

		String Strtmpfunction = "var form = new Ext.FormPanel({ \n"
				+ "		labelAlign : 'right', \n"
				+ "		frame : true, \n"
				+ "		labelAlign : 'messageform', \n"
				+ "		labelWidth : 160, \n"
				+ "		items : [{ \n"
				+ "		layout : 'column',\n"
				+ "		items : [{ \n"
				+ "		layout:'form',\n"
				+ "		columnWidth:3,\n"
				+ "			items : [new Ext.form.ComboBox({ \n"
				+ "			fieldLabel:'请选择今年完成截止的月份', \n"
				+ "          id: 'yuefenname',\n"
				+ "			width:100, \n"
				+ "			selectOnFocus:true, \n"
				+ "			transform:'YUEFEN', \n"
				+ "			lazyRender:true, \n"
				+ "			triggerAction:'all', \n"
				+ "			typeAhead:true, \n"
				+ "			forceSelection:true\n"
				+ "			//editable:false \n"
				+ "			})] \n"
				+ "		}]\n"
				+ "	}] \n"
				+ "}); \n"
				+ "	win = new Ext.Window({ \n"
				+ "		layout : 'fit', \n"
				+ "		width : 300, \n"
				+ "		//height : 110, \n"
				+ "		closeAction : 'hide', \n"
				+ "		plain : true, \n"
				+ "		title : '条件', \n"
				+ "		items : [form],  \n"
				+ "		buttons : [{ \n"
				+ "			text : '确定', \n"
				+ "			handler : function() { \n"
				+ "				win.hide(); \n"
				+ "				document.getElementById('TEXT_RADIO_SELECT_VALUE').value = \n"
				+ "						Ext.getCmp(\"yuefenname\").value; \n"
				+ "				//document.getElementById('RefurbishButton').click(); \n"
				+ "			} \n" + "		}, { \n" + "			text : '取消', \n"
				+ "			handler : function() { \n" + "				win.hide(); \n"
				+ "			} \n" + "		}] \n" + "	});\n";

		if (visit.getRenyjb() == 3) {
//			 设置按钮
			StringBuffer insz = new StringBuffer();
			insz.append("function(){ if(!win){");
			insz.append(Strtmpfunction);
			insz.append("} win.show(this);	\n");
			insz.append("}");
			GridButton gbSz = new GridButton("条件设置", insz.toString());
			gbSz.setIcon(SysConstant.Btn_Icon_Search);
			gbSz.setId("SEARCH");
//			if (falge) {
//				String str1 = "";
//				str1 = "Ext.getCmp(\"SEARCH\").setDisabled(true);";
//				egu.addOtherScript(str1);
//			} else {
//				String str1 = "";
//				str1 = "Ext.getCmp(\"SEARCH\").setDisabled(false);";
//				egu.addOtherScript(str1);
//			}

			egu.addTbarBtn(gbSz);
			
			if (yincan == false) {
				// 添加按钮
//				StringBuffer ins = new StringBuffer();
//				ins.append("function (){").append(
//						"document.getElementById('InsertButton').click();}");
//				GridButton gbIns = new GridButton("添加", ins.toString());
//				gbIns.setIcon(SysConstant.Btn_Icon_Insert);
//				gbIns.setId("INSERT");
//				if (falge) {
//					String str1 = "";
//					str1 = "Ext.getCmp(\"INSERT\").setDisabled(true);";
//					egu.addOtherScript(str1);
//				} else {
//					String str1 = "";
//					str1 = "Ext.getCmp(\"INSERT\").setDisabled(false);";
//					egu.addOtherScript(str1);
//				}
//
//				egu.addTbarBtn(gbIns);
				
				// 删除按钮
				if (falge == true) {
					String ss = "Ext.MessageBox.confirm('警告', '你确定要删除本月所有的数据吗？', function(btn) { if(btn=='yes'){document.getElementById('DeleteButton').click();}})";
					StringBuffer del = new StringBuffer();
					del.append("function (){").append("" + ss + "}");
					GridButton gbDel = new GridButton("删除", del.toString());
					gbDel.setIcon(SysConstant.Btn_Icon_Delete);
					gbDel.setId("DEL");
					// if(falge){
					// String str1="";
					// str1="Ext.getCmp(\"DEL\").setDisabled(true);";
					// egu.addOtherScript(str1);
					// }
					egu.addTbarBtn(gbDel);
				}
			}
		}
		// 保存按钮
		if (falge == true) {
			if (yincan == false) {
				egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton",
						MainGlobal.getExtMessageShow("正在保存数据,请稍后...", "保存中...",
								200));
				// 审核按钮
				if (visit.isShifsh() == true) {
					if (visit.getRenyjb() == 2 || visit.getRenyjb() == 3) {
						// GridButton gsh = new GridButton("审核","function
						// (){document.getElementById('ShenhButton').click();}");

						String ss = "Ext.MessageBox.confirm('警告', '你确定要将数据提交上级单位吗？', function(btn) { if(btn=='yes'){document.getElementById('ShenhButton').click();}})";
						StringBuffer shenh = new StringBuffer();
						shenh.append("function (){").append("" + ss + "}");

						GridButton gbShen = new GridButton("审核", shenh
								.toString());
						gbShen.setIcon(SysConstant.Btn_Icon_Search);
						gbShen.setId("SHENH");
						// if(yincan==true){
						// String str1="";
						// str1="Ext.getCmp(\"SHENH\").setDisabled(true);";
						// egu.addOtherScript(str1);
						// }
						egu.addTbarBtn(gbShen);
					}
				}
			}

			// 回退按钮
			if (visit.isShifsh() == true) {
				// egu.getColumn("zhuangt").setEditor(null);
				if (visit.getRenyjb() == 2 || visit.getRenyjb() == 1) {
					GridButton ght = new GridButton("回退",
							"function (){document.getElementById('ReturnButton').click();}");
					ght.setDisabled(isReture);
					ght.setIcon(SysConstant.Btn_Icon_Return);
					ght.setId("RETURN");
					if (falge) {
						String str1 = "";
						str1 = "Ext.getCmp(\"RETURN\").setDisabled(false);";
						egu.addOtherScript(str1);
					}
					egu.addTbarBtn(ght);
				}
			}
		}

		egu.addTbarText("->");

		// ---------------页面js的计算开始------------------------------------------
		StringBuffer sb = new StringBuffer();

		sb.append("gridDiv_grid.on('beforeedit',function(e){");

		// 设定某一行不能编辑
		// sb.append("if(e.record.get('JIZLX')=='总计'){e.cancel=true;}");//
		sb
				.append("if(e.record.get('LANC')=='7' || e.record.get('LANC')=='8' || e.record.get('LANC')=='9' || e.record.get('LANC')=='12' ||e.record.get('LANC')=='14' || e.record.get('LANC')=='15' || e.record.get('LANC')=='16' || e.record.get('LANC')=='21'){e.cancel=true;}");

		sb.append("});");

		sb.append("gridDiv_grid.on('afteredit',function(e){\n");

		// ************************新增机组**************************//
		sb.append("fadl_xj = gridDiv_ds.getAt(0);\n");// 发电量－录入
		sb.append("gongdbzmh_xj = gridDiv_ds.getAt(1);\n");// 供电标准煤耗－录入

		sb.append("zonghcydl_xj = gridDiv_ds.getAt(2);\n");// 综合厂用电率－录入
		sb.append("fadbhm_xj = gridDiv_ds.getAt(3);\n");// 发电标准煤耗－录入

		sb.append("gongrl_xj = gridDiv_ds.getAt(4);\n");// 供热量－录入
		sb.append("gongrbzmh_xj = gridDiv_ds.getAt(5);\n");// 供热标准煤耗－录入

		sb.append("fadxybml_xj = gridDiv_ds.getAt(6);\n");// 发电需用标煤量--计算（发电量*发电标准煤耗）
		sb.append("gongrxybml_xj = gridDiv_ds.getAt(7);\n");// 供热需用标煤量--计算（供热量*供热标准煤耗）
		
		sb.append("xuybml_xj = gridDiv_ds.getAt(8);\n");// 需用标煤量--计算（发电需用标煤量+供热需用标煤量）
		sb.append("dianhzryl_xj = gridDiv_ds.getAt(9);\n");// 点火助燃油量－录入
		sb.append("youfrl_xj = gridDiv_ds.getAt(10);\n");// 油发热量－录入

		sb.append("xuymzbml_xj = gridDiv_ds.getAt(11);\n");// 需用油折标煤量－计算（总需求量-油发热量）
		sb.append("rulrz_xj = gridDiv_ds.getAt(12);\n");// 入炉热值－录入
		sb.append("fadxyml_xj = gridDiv_ds.getAt(13);\n");// 发电需原煤量--计算（发电需用标煤量*29.271/入炉热值）
		sb.append("gongrxyml_xj = gridDiv_ds.getAt(14);\n");// 供热需原煤量--计算（供热需用标煤量*29.271/入炉热值）
		sb.append("xuyyml_xj = gridDiv_ds.getAt(15);\n");// 需用原煤量--计算（发电需原煤量+供热需原煤量）
		sb.append("qity_xj = gridDiv_ds.getAt(16);\n");// 其他用－录入
		sb.append("yuns_xj = gridDiv_ds.getAt(17);\n");// 运损－录入
		sb.append("qickc_xj = gridDiv_ds.getAt(18);\n");// 期初库存－录入
		sb.append("qimkc_xj = gridDiv_ds.getAt(19);\n");// 期未库存－录入
		sb.append("zongxql_xj = gridDiv_ds.getAt(20);\n");// 总需求量--计算（需用原煤量+其他用+运损+期未库存-期初库存）

		//计算发电需用标煤量-新增机组 （发电量*发电标准煤耗）/10000
		sb.append("fadxybml_xj.set('LASTYEAR',Round((eval(fadl_xj.get('LASTYEAR')||0))*eval(fadbhm_xj.get('LASTYEAR')||0)/1000000,2));\n");
		sb.append("fadxybml_xj.set('INTYEAR',Round((eval(fadl_xj.get('INTYEAR')||0))*eval(fadbhm_xj.get('INTYEAR')||0)/1000000,2));\n");
		sb.append("fadxybml_xj.set('NEXTYEAR',Round((eval(fadl_xj.get('NEXTYEAR')||0))*eval(fadbhm_xj.get('NEXTYEAR')||0)/1000000,2));\n");

		// 计算供热需用标煤量-新增机组 （供热量*供热标准煤耗）/10000
		sb.append("gongrxybml_xj.set('LASTYEAR',Round(eval(gongrl_xj.get('LASTYEAR')||0)*eval(gongrbzmh_xj.get('LASTYEAR')||0)/10000000,2));\n");
		sb.append("gongrxybml_xj.set('INTYEAR',Round(eval(gongrl_xj.get('INTYEAR')||0)*eval(gongrbzmh_xj.get('INTYEAR')||0)/10000000,2));\n");
		sb.append("gongrxybml_xj.set('NEXTYEAR',Round(eval(gongrl_xj.get('NEXTYEAR')||0)*eval(gongrbzmh_xj.get('NEXTYEAR')||0)/10000000,2));\n");
		// 计算需用标煤量-新增机组 （发电需用标煤量+供热需用标煤量）
		sb.append("xuybml_xj.set('LASTYEAR',Round(eval(fadxybml_xj.get('LASTYEAR')||0)+eval(gongrxybml_xj.get('LASTYEAR')||0),2));\n");
		sb.append("xuybml_xj.set('INTYEAR',Round(eval(fadxybml_xj.get('INTYEAR')||0)+eval(gongrxybml_xj.get('INTYEAR')||0),2));\n");
		sb.append("xuybml_xj.set('NEXTYEAR',Round(eval(fadxybml_xj.get('NEXTYEAR')||0)+eval(gongrxybml_xj.get('NEXTYEAR')||0),2));\n");
		// 计算需用油折标煤量 点火助燃油量*41.816/29.271  油发热量=41.816 --（总需求量-油发热量）
		sb.append("xuymzbml_xj.set('LASTYEAR',Round(eval(dianhzryl_xj.get('LASTYEAR')||0)*eval(youfrl_xj.get('LASTYEAR')||0)/29.271,2));\n");
		sb.append("xuymzbml_xj.set('INTYEAR',Round(eval(dianhzryl_xj.get('INTYEAR')||0)*eval(youfrl_xj.get('INTYEAR')||0)/29.271,2));\n");
		sb.append("xuymzbml_xj.set('NEXTYEAR',Round(eval(dianhzryl_xj.get('NEXTYEAR')||0)*eval(youfrl_xj.get('NEXTYEAR')||0)/29.271,2));\n");
		// 计算发电需原煤量--新增机组 （发电需用标煤量*29.271/入炉热值）
		sb.append("if(eval(rulrz_xj.get('LASTYEAR')||0)==0){ \n");
		sb.append("		fadxyml_xj.set('LASTYEAR',0);\n");
		sb.append("}else{ \n");
		sb.append("		fadxyml_xj.set('LASTYEAR',Round((eval(fadxybml_xj.get('LASTYEAR')||0)*1000*29.271)/eval(rulrz_xj.get('LASTYEAR')||0)/1000,2));\n");
		sb.append("}\n");
		sb.append("if(eval(rulrz_xj.get('INTYEAR')||0)==0){ \n");
		sb.append("		fadxyml_xj.set('INTYEAR',0);\n ");
		sb.append("}else{\n ");
		sb.append("		fadxyml_xj.set('INTYEAR',Round((eval(fadxybml_xj.get('INTYEAR')||0)*1000*29.271)/eval(rulrz_xj.get('INTYEAR')||0)/1000,2));\n");
		sb.append("}\n ");
		sb.append("if(eval(rulrz_xj.get('NEXTYEAR')||0)==0){ \n");
		sb.append("		fadxyml_xj.set('NEXTYEAR',0);\n ");
		sb.append("}else{\n ");
		sb.append("		fadxyml_xj.set('NEXTYEAR',Round((eval(fadxybml_xj.get('NEXTYEAR')||0)*1000*29.271)/eval(rulrz_xj.get('NEXTYEAR')||0)/1000,2));\n");
		sb.append("}\n ");
		// 计算供热需原煤量--新增机组 （供热需用标煤量*29.271/入炉热值）
		sb.append("if(eval(rulrz_xj.get('LASTYEAR')||0)==0){ \n");
		sb.append("gongrxyml_xj.set('LASTYEAR',0);\n ");
		sb.append("}else{\n ");
		sb.append("gongrxyml_xj.set('LASTYEAR',Round((eval(gongrxybml_xj.get('LASTYEAR')||0)*1000*29.271)/eval(rulrz_xj.get('LASTYEAR')||0)/1000,2));\n");
		sb.append("}\n ");
		sb.append("if(eval(rulrz_xj.get('INTYEAR')||0)==0){ \n");
		sb.append("gongrxyml_xj.set('INTYEAR',0);\n ");
		sb.append("}else{\n ");
		sb.append("gongrxyml_xj.set('INTYEAR',Round((eval(gongrxybml_xj.get('INTYEAR')||0)*1000*29.271)/eval(rulrz_xj.get('INTYEAR')||0)/1000,2));\n");
		sb.append("}\n ");
		sb.append("if(eval(rulrz_xj.get('NEXTYEAR')||0)==0){ \n");
		sb.append("gongrxyml_xj.set('NEXTYEAR',0);\n ");
		sb.append("}else{\n ");
		sb.append("gongrxyml_xj.set('NEXTYEAR',Round((eval(gongrxybml_xj.get('NEXTYEAR')||0)*1000*29.271)/eval(rulrz_xj.get('NEXTYEAR')||0)/1000,2));\n");
		sb.append("}\n ");
		// 计算需用原煤量--新增机组 （发电需原煤量+供热需原煤量）
		sb.append("var xuyyml1=Round(eval(fadxyml_xj.get('LASTYEAR')||0)+eval(gongrxyml_xj.get('LASTYEAR')||0),2);\n");
		sb.append("if (xuyyml1=='0.00'){\n");
		sb.append(" 	xuyyml1='0';\n");
		sb.append("}\n");
		sb.append("var xuyyml2=Round(eval(fadxyml_xj.get('INTYEAR')||0)+eval(gongrxyml_xj.get('INTYEAR')||0),2);\n");
		sb.append("if (xuyyml2=='0.00'){\n");
		sb.append(" 	xuyyml2='0';\n");
		sb.append("}\n");
		sb.append("var xuyyml3=Round(eval(fadxyml_xj.get('NEXTYEAR')||0)+eval(gongrxyml_xj.get('NEXTYEAR')||0),2);\n");
		sb.append("if (xuyyml3=='0.00'){\n");
		sb.append(" 	xuyyml3='0';\n");
		sb.append("}\n");
		sb.append("xuyyml_xj.set('LASTYEAR',xuyyml1);\n");
		sb.append("xuyyml_xj.set('INTYEAR',xuyyml2);\n");
		sb.append("xuyyml_xj.set('NEXTYEAR',xuyyml3);\n");
		// 计算总需求量--新增机组（需用原煤量+其他用+运损+期未库存-期初库存）
		sb.append("var zongxql1=Round(eval(xuyyml_xj.get('LASTYEAR')||0)+eval(qity_xj.get('LASTYEAR')||0)+eval(yuns_xj.get('LASTYEAR')||0)+eval(qimkc_xj.get('LASTYEAR')||0)-eval(qickc_xj.get('LASTYEAR')||0),2);\n");
		sb.append("if (zongxql1=='0.00'){\n");
		sb.append(" 	zongxql1='0';\n");
		sb.append("}\n");
		sb.append("var zongxql2=Round(eval(xuyyml_xj.get('INTYEAR')||0)+eval(qity_xj.get('INTYEAR')||0)+eval(yuns_xj.get('INTYEAR')||0)+eval(qimkc_xj.get('INTYEAR')||0)-eval(qickc_xj.get('INTYEAR')||0),2);\n");
		sb.append("if (zongxql2=='0.00'){\n");
		sb.append(" 	zongxql2='0';\n");
		sb.append("}\n");
		sb.append("var zongxql3=Round(eval(xuyyml_xj.get('NEXTYEAR')||0)+eval(qity_xj.get('NEXTYEAR')||0)+eval(yuns_xj.get('NEXTYEAR')||0)+eval(qimkc_xj.get('NEXTYEAR')||0)-eval(qickc_xj.get('NEXTYEAR')||0),2);\n");
		sb.append("if (zongxql3=='0.00'){\n");
		sb.append(" 	zongxql3='0';\n");
		sb.append("}\n");
		sb.append("zongxql_xj.set('LASTYEAR',zongxql1);\n");
		sb.append("zongxql_xj.set('INTYEAR',zongxql2);\n");
		sb.append("zongxql_xj.set('NEXTYEAR',zongxql3);\n");
		
		sb.append("});\n");

		//设定合计列不保存
		//sb.append("function gridDiv_save(record){if(record.get('JIZLX')=='总计') return 'continue';}");

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
			getSelectData();
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