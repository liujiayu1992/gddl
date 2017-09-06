package com.zhiren.jt.jihgl.yuexqjhh;

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
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yuexqjhh extends BasePage implements PageValidateListener {
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
		int flag = 0;
		con.setAutoCommit(false);
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
		
		//
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange().replaceAll("&nbsp;", ""));//把getChange中的&nbsp;替换成"",否则ext不能识别
		
		String zidm1 = "";
		StringBuffer sb = new StringBuffer();
		sb.append("update yuexqjhh set ");
		while (rsl.next()) {
			switch(rsl.getInt("LANC")){
				case 1:
					zidm1 = "fadl";
					break;
				case 2:
					zidm1="fadmh";
					break;
				case 3:
					zidm1="fadml";
					break;
				case 4:
					zidm1="fadtrmh";
					break;
				case 5:
					zidm1="fadtrml";
					break;
				case 6:
					zidm1="gongrl";
					break;
				case 7:
					zidm1="gongrmh";
					break;
				case 8:
					zidm1="gongrml";
					break;
				case 9:
					zidm1="gongrtrmh";
					break;
				case 10:
					zidm1="gongrtrml";
					break;
				case 11:
					zidm1="sunh";
					break;
				case 12:
					zidm1="qity";
					break;
				case 13:
					zidm1="yuecbmkc";
					break;
				case 14:
					zidm1="yuembmkc";
					break;
				case 15:
					zidm1="biaomxqsl";
					break;
				case 16:
					zidm1="yuectrmkc";
					break;
				case 17:
					zidm1="yuemtrmkc";
					break;
				case 18:
					zidm1=" tianrmxqsl";
					break;
				case 19:
					zidm1="rulrz";
					break;
				default: break;
			}
			if(!zidm1.equals("")){
				sb.append(zidm1).append("=").append(rsl.getDouble("zhi")).append(",");
			}
		}
		long diancxxb_id=Long.parseLong(this.getTreeid());
		sb.deleteCharAt(sb.length()-1);
		sb.append(" where ").append("riq=to_date('"+ intyear + "-"+ StrMonth + "-01','yyyy-mm-dd')").append(" and");
		sb.append(" diancxxb_id="+diancxxb_id+" ");
		flag = con.getUpdate(sb.toString());
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
		if(visit.getRenyjb()==3){
			zhuangt="zhuangt=1"; 
		}else if(visit.getRenyjb()==2){
			zhuangt=" zhuangt=2";
		}
		
		JDBCcon con = new JDBCcon();
		String sqlcx=	"select y.* \n" + 
		" from yuexqjhh y,diancxxb dc\n" + 
		" where y.diancxxb_id=dc.id "+"\n" + 
		"       and y.riq="+CurrODate+chaxzt+" order by y.id";
		ResultSetList rsl = con.getResultSetList(sqlcx);
	
		StringBuffer sql = new StringBuffer();
		sql.append("begin \n");		
		while (rsl.next()) {
	
			sql.append("update yuexqjhh set "+zhuangt+ " where id=" + rsl.getLong("id")+";\n");
			

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
		" from yuexqjhh y,diancxxb dc\n" + 
		" where y.diancxxb_id=dc.id "+"\n" + 
		"      and y.riq="+CurrODate+chaxzt+" order by y.id";
		ResultSetList rsl = con.getResultSetList(sqlcx);
	
		StringBuffer sql = new StringBuffer();
		int result = 0;
		sql.append("begin \n");		
		while (rsl.next()) {
			result++;
			sql.append("update yuexqjhh set "+zhuangt+ " where id=" + rsl.getLong("id")+";\n");
			
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
		//删除本月和累计的数据
		long id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
		long diancxxb_id=Long.parseLong(this.getTreeid());
		String DeleteStr="delete yuexqjhh y where y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') " +
				"and y.diancxxb_id="+diancxxb_id+"";
		con.getDelete(DeleteStr);
		con.Close();
	}
	public void Insert(){
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
		
		long id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
		long diancxxb_id=Long.parseLong(this.getTreeid());
		String isHaveZhi="select * from yuexqjhh y where y.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') " +
				"and y.diancxxb_id="+diancxxb_id+"";
		ResultSetList rssy=con.getResultSetList(isHaveZhi);
		//插入时判断是否有值
		if(rssy.next()){
			con.Close();
			return;
		}else{
			con.getInsert("insert into yuexqjhh(ID,DIANCXXB_ID,RIQ,FADL,FADMH,FADML,GONGRL,GONGRMH,GONGRML," +
				"SUNH,QITY,YUECTRMKC,YUEMTRMKC,BIAOMXQSL,GONGSSHR,GONGSSHSJ,JITSHR,JITSHSJ,ZHUANGT,RULRZ,JISFDL,GONGRBML,BIAOMH) values (\n"
				+id+","+diancxxb_id+",to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd')," +
						"0,0,0,0,0,0,0,0,0" +
						",0,0,'',null,'',null,0,0,0,0,0)");
		}
		
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
		//当月份是1的时候显示01,
		String StrMonth="";
		if(intMonth<10){
			
			StrMonth="0"+intMonth;
		}else{
			StrMonth=""+intMonth;
		}
		//-----------------------------------
		String str = "";
		
		
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
//			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
//					+ getTreeid() + ")";
			str = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
		
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = " and dc.id = " + getTreeid() + "";
		
		}
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
	

//		String chaxun  =
//			"select sj.lanc,sj.mingc,sj.danw,sj.zhi,decode("+visit.getRenyjb()+",3,decode(sj.zhuangt,0,'未提交','已提交'),2,decode(sj.zhuangt,1,'未审核','已审核'),3,decode(sj.zhuangt,2,'未审核','已审核')) as zhuangt\n" +
//			"from (\n" + 
//			"select 1 as lanc,'发电量' as mingc,yx.fadl as zhi,'万千瓦时' as danw,yx.zhuangt\n" + 
//			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" +
//			"union\n" +
//			"select 2 as lanc,'发电标煤耗' as mingc,yx.fadmh as zhi,'克/千瓦时' as danw,yx.zhuangt\n" + 
//			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" +
//			"union\n" +
//			"select 3 as lanc,'发电标煤量' as mingc,yx.fadml as zhi,'吨' as danw,yx.zhuangt\n" + 
//			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
//			"union\n" +
//			"select 4 as lanc,'供热量' as mingc,yx.gongrl as zhi,'万吉焦' as danw,yx.zhuangt\n" + 
//			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
//			"union\n" +
//			"select 5 as lanc,'供热标煤耗' as mingc,yx.gongrmh as zhi,'千克/吉焦' as danw,yx.zhuangt\n" + 
//			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
//			"union\n" +
//			"select 6 as lanc,'供热标煤量' as mingc,yx.gongrml as zhi,'吨' as danw,yx.zhuangt\n" + 
//			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
//			"union\n" +
//			"select 7 as lanc,'运输损耗' as mingc,yx.sunh as zhi,'吨' as danw,yx.zhuangt\n" + 
//			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
//			"union\n" +
//			"select 8 as lanc,'其它用' as mingc,yx.qity as zhi,'吨' as danw,yx.zhuangt\n" + 
//			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
//			"union\n" +
//			"select 9 as lanc,'月初库' as mingc,yx.yuekc as zhi,'吨' as danw,yx.zhuangt\n" + 
//			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
//			"union\n" +
//			"select 10 as lanc,'月末库存' as mingc,yx.yuemkc as zhi,'吨' as danw,yx.zhuangt\n" + 
//			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd') and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
//			"union\n" +
//			"select 11 as lanc,'需求数量' as mingc,yx.xuqsl as zhi,'吨' as danw,yx.zhuangt\n" + 
//			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd')  and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
//			//******************新增加字段************************//
//			"union\n" +
//			"select 12 as lanc,'入炉热值' as mingc,yx.rulrz as zhi,'兆焦/千克' as danw,yx.zhuangt\n" + 
//			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd')  and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
//			/*"union\n" +
//			"select 13 as lanc,'计划发电' as mingc,yx.jisfdl as zhi,'吨' as danw,yx.zhuangt\n" + 
//			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd')  and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
//			"union\n" +
//			"select 14 as lanc,'供热标煤量' as mingc,yx.gongrbml as zhi,'吨' as danw,yx.zhuangt\n" + 
//			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd')  and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
//			"union\n" +
//			"select 15 as lanc,'标煤耗' as mingc,yx.biaomh as zhi,'吨' as danw,yx.zhuangt\n" + 
//			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd')  and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
//			*/
////			"select 12 as lanc,'公司审核人' as mingc,yx.gongsshr as zhi,'' as danw,yx.zhuangt\n" + 
////			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and where yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd')  and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
////			"select 13 as lanc,'公司审核时间' as mingc,to_char(yx.gongsshsj,'yyyy-mm-dd') as zhi,'' as danw,yx.zhuangt\n" + 
////			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and where yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd')  and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
////			"select 14 as lanc,'集团审核人' as mingc,yx.jitshr as zhi,'' as danw,yx.zhuangt\n" + 
////			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and where yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd')  and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" +
////			"select 15 as lanc,'集团审核时间' as mingc,to_char(yx.jitshsj,'yyyy-mm-dd') as zhi,'' as danw,yx.zhuangt\n" + 
////			"from yuexqjhh yx where yx.riq=to_date('" + intyear + "-" + StrMonth+ "-01','yyyy-mm-dd')  and yx.diancxxb_id="+this.getTreeid()+zhuangt+"\n" + 
//			
//			") sj order by sj.lanc";
		
		String chaxun = 
			"select sj.lanc,sj.mingc,sj.danw,sj.zhi,decode("+ visit.getRenyjb() +",3,decode(sj.zhuangt,0,'未提交','已提交'),2,decode(sj.zhuangt,1,'未审核','已审核'),3,decode(sj.zhuangt,2,'未审核','已审核')) as zhuangt\n" +
			"from (\n" + 
			"select 1 as lanc,'发电量' as mingc,yx.fadl as zhi,'万千瓦时' as danw,yx.zhuangt\n" + 
			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('"+ intyear +"-"+ StrMonth +"-01','yyyy-mm-dd') and yx.diancxxb_id="+ this.getTreeid()+zhuangt +"\n" + 
			"union\n" + 
			"select 2 as lanc,'发电标煤耗' as mingc,yx.fadmh as zhi,'克/千瓦时' as danw,yx.zhuangt\n" + 
			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('"+ intyear +"-"+ StrMonth +"-01','yyyy-mm-dd') and yx.diancxxb_id="+ this.getTreeid()+zhuangt +"\n" + 
			"union\n" + 
			"select 3 as lanc,'发电标煤量' as mingc,yx.fadml as zhi,'吨' as danw,yx.zhuangt\n" + 
			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('"+ intyear +"-"+ StrMonth +"-01','yyyy-mm-dd') and yx.diancxxb_id="+ this.getTreeid()+zhuangt +"\n" + 
			"union\n" + 
			"select 4 as lanc,'发电天然煤耗' as mingc,yx.fadtrmh as zhi,'克/千瓦时' as danw,yx.zhuangt\n" + 
			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('"+ intyear +"-"+ StrMonth +"-01','yyyy-mm-dd') and yx.diancxxb_id="+ this.getTreeid()+zhuangt +"\n" + 
			"union\n" + 
			"select 5 as lanc,'发电天然煤量' as mingc,yx.fadtrml as zhi,'吨' as danw,yx.zhuangt\n" + 
			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('"+ intyear +"-"+ StrMonth +"-01','yyyy-mm-dd') and yx.diancxxb_id="+ this.getTreeid()+zhuangt +"\n" + 
			"union\n" + 
			"select 6 as lanc,'供热量' as mingc,yx.gongrl as zhi,'万吉焦' as danw,yx.zhuangt\n" + 
			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('"+ intyear +"-"+ StrMonth +"-01','yyyy-mm-dd') and yx.diancxxb_id="+ this.getTreeid()+zhuangt +"\n" + 
			"union\n" + 
			"select 7 as lanc,'供热标煤耗' as mingc,yx.gongrmh as zhi,'千克/吉焦' as danw,yx.zhuangt\n" + 
			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('"+ intyear +"-"+ StrMonth +"-01','yyyy-mm-dd') and yx.diancxxb_id="+ this.getTreeid()+zhuangt +"\n" + 
			"union\n" + 
			"select 8 as lanc,'供热标煤量' as mingc,yx.gongrml as zhi,'吨' as danw,yx.zhuangt\n" + 
			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('"+ intyear +"-"+ StrMonth +"-01','yyyy-mm-dd') and yx.diancxxb_id="+ this.getTreeid()+zhuangt +"\n" + 
			"union\n" + 
			"select 9 as lanc,'供热天然煤耗' as mingc,yx.gongrtrmh as zhi,'千克/吉焦' as danw,yx.zhuangt\n" + 
			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('"+ intyear +"-"+ StrMonth +"-01','yyyy-mm-dd') and yx.diancxxb_id="+ this.getTreeid()+zhuangt +"\n" + 
			"union\n" + 
			"select 10 as lanc,'供热天然煤量' as mingc,yx.gongrtrml as zhi,'吨' as danw,yx.zhuangt\n" + 
			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('"+ intyear +"-"+ StrMonth +"-01','yyyy-mm-dd') and yx.diancxxb_id="+ this.getTreeid()+zhuangt +"\n" + 
			"union\n" + 
			"select 11 as lanc,'运输损耗' as mingc,yx.sunh as zhi,'吨' as danw,yx.zhuangt\n" + 
			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('"+ intyear +"-"+ StrMonth +"-01','yyyy-mm-dd') and yx.diancxxb_id="+ this.getTreeid()+zhuangt +"\n" + 
			"union\n" + 
			"select 12 as lanc,'其它用' as mingc,yx.qity as zhi,'吨' as danw,yx.zhuangt\n" + 
			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('"+ intyear +"-"+ StrMonth +"-01','yyyy-mm-dd') and yx.diancxxb_id="+ this.getTreeid()+zhuangt +"\n" + 
			"union\n" + 
			"select 13 as lanc,'月初标煤库存' as mingc,yx.yuecbmkc as zhi,'吨' as danw,yx.zhuangt\n" + 
			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('"+ intyear +"-"+ StrMonth +"-01','yyyy-mm-dd') and yx.diancxxb_id="+ this.getTreeid()+zhuangt +"\n" + 
			"union\n" + 
			"select 14 as lanc,'月末标煤库存' as mingc,yx.yuembmkc as zhi,'吨' as danw,yx.zhuangt\n" + 
			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('"+ intyear +"-"+ StrMonth +"-01','yyyy-mm-dd') and yx.diancxxb_id="+ this.getTreeid()+zhuangt +"\n" + 
			"union\n" + 
			"select 15 as lanc,'标煤需求数量' as mingc,yx.biaomxqsl as zhi,'吨' as danw,yx.zhuangt\n" + 
			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('"+ intyear +"-"+ StrMonth +"-01','yyyy-mm-dd') and yx.diancxxb_id="+ this.getTreeid()+zhuangt +"\n" + 
			"union\n" + 
			"select 16 as lanc,'月初天然煤库存' as mingc,yx.yuectrmkc as zhi,'吨' as danw,yx.zhuangt\n" + 
			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('"+ intyear +"-"+ StrMonth +"-01','yyyy-mm-dd') and yx.diancxxb_id="+ this.getTreeid()+zhuangt +"\n" + 
			"union\n" + 
			"select 17 as lanc,'月末天然煤库存' as mingc,yx.yuemtrmkc as zhi,'吨' as danw,yx.zhuangt\n" + 
			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('"+ intyear +"-"+ StrMonth +"-01','yyyy-mm-dd') and yx.diancxxb_id="+ this.getTreeid()+zhuangt +"\n" + 
			"union\n" + 
			"select 18 as lanc,'天然煤需求数量' as mingc,yx.tianrmxqsl as zhi,'吨' as danw,yx.zhuangt\n" + 
			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('"+ intyear +"-"+ StrMonth +"-01','yyyy-mm-dd') and yx.diancxxb_id="+ this.getTreeid()+zhuangt +"\n" + 
			"union\n" + 
			"select 19 as lanc,'入炉热值' as mingc,yx.rulrz as zhi,'兆焦/千克' as danw,yx.zhuangt\n" + 
			"from yuexqjhh yx,diancxxb dc where yx.diancxxb_id=dc.id and yx.riq=to_date('"+ intyear +"-"+ StrMonth +"-01','yyyy-mm-dd') and yx.diancxxb_id="+ this.getTreeid()+zhuangt +"\n" + 
			") sj order by sj.lanc";

	// System.out.println(chaxun);
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
   	egu.setTableName("yuexqjhh");
	egu.getColumn("lanc").setCenterHeader("栏次");
	egu.getColumn("lanc").setEditor(null);
	egu.getColumn("mingc").setCenterHeader("项目");
	egu.getColumn("mingc").setEditor(null);
	egu.getColumn("danw").setCenterHeader("单位");
	egu.getColumn("danw").setEditor(null);
	egu.getColumn("zhi").setCenterHeader("计划");

	egu.getColumn("zhuangt").setCenterHeader("状态");
	egu.getColumn("zhuangt").setHidden(true);
	//egu.getColumn("zhuangt").setEditor(null);
//	设定不可编辑列的颜色
	egu.getColumn("lanc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
	egu.getColumn("mingc").setRenderer("function(value,metadata){metadata.css='tdTextext1'; return value;}");
	egu.getColumn("danw").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
//	//设定单元格的底色
//	egu.getColumn("zhi").setRenderer("function(value,metadata,rec,rowIndex){if(rowIndex==2||rowIndex==4||rowIndex==7||rowIndex==9||rowIndex==14||rowIndex==17){metadata.css='tdTextext3';} return value;}");
	String control = 
		"function(value,metadata,record,rowIndex,columnIndedx,store){\n" +
		"    if(rowIndex==2||rowIndex==4||rowIndex==7||rowIndex==9||rowIndex==14||rowIndex==17){\n" + 
		"        metadata.attr=\"style=background-color:#FFFFCC\";\n" + 
		"    }\n" + 
		"    return value;\n" + 
		"}";
	egu.getColumn("zhi").setRenderer(control);
//	egu.getColumn("mingc2").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
//	egu.getColumn("danw2").setRenderer("function(value,metadata){metadata.css='tdTextext2'; return value;}");
//	egu.getColumn("zhi2").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
	
	egu.setDefaultsortable(false);//设定页面不自动排序
	
	//设定列初始宽度
	egu.getColumn("lanc").setWidth(70);
	egu.getColumn("lanc").setAlign("right");
	egu.getColumn("mingc").setWidth(200);
	egu.getColumn("danw").setWidth(60);
	egu.getColumn("zhi").setWidth(80);
	
	
//	设定列的小数位
	//((NumberField)egu.getColumn("zhi1").editor).setDecimalPrecision(3);
	
	egu.setGridType(ExtGridUtil.Gridstyle_Edit);//设定grid可以编辑
	egu.addPaging(100);//设置分页
 	egu.setWidth("bodyWidth");//设置页面的宽度,当超过这个宽度时显示滚动条
	
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
		
		egu.addTbarText("月份:");
		ComboBox comb2=new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");//和自动刷新绑定
		comb2.setLazyRender(true);//动态绑定
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");//设置分隔符
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
		.append(MainGlobal.getExtMessageBox("'正在刷新'+Ext.getDom('NIANF').value+'年'+Ext.getDom('YUEF').value+'月的数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
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
				}
				egu.addTbarBtn(gbIns);
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
				egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton",MainGlobal.getExtMessageShow("正在保存数据,请稍后...", "保存中...", 200));
//				审核按钮
				if(visit.isShifsh()==true){
					if(visit.getRenyjb()==2 || visit.getRenyjb()==3){
						GridButton gsh = new GridButton("审核","function (){document.getElementById('ShenhButton').click();}");
						gsh.setIcon(SysConstant.Btn_Icon_Search);
						gsh.setId("SHENH");
		//				if(yincan==true){
		//					String str1="";
		//					str1="Ext.getCmp(\"SHENH\").setDisabled(true);";
		//					egu.addOtherScript(str1);
		//				}
						egu.addTbarBtn(gsh);
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
		sb.append("if(e.record.get('LANC')=='3'||e.record.get('LANC')=='5'||e.record.get('LANC')=='8'||e.record.get('LANC')=='10'||e.record.get('LANC')=='15'||e.record.get('LANC')=='18'){e.cancel=true;}");//
		
		sb.append("});");
		
//		sb.append("gridDiv_grid.on('afteredit',function(e){");
//			//计划发电原煤量				
//			sb.append("if(e.row==0 || e.row==1){ rec = gridDiv_ds.getAt(2); recfdl = gridDiv_ds.getAt(0);recfdmh = gridDiv_ds.getAt(1);");
//			sb.append("rec.set('ZHI',Round((eval(recfdl.get('ZHI')||0)*10000)*eval(recfdmh.get('ZHI')||0)/1000/1000,0));}");
//			
//			//计算供热用原煤量		
//			sb.append("if(e.row==3 || e.row==4){ rec1 = gridDiv_ds.getAt(5); recgrl = gridDiv_ds.getAt(3);recgrmh = gridDiv_ds.getAt(4);");
//			sb.append("rec1.set('ZHI',Round((eval(recgrl.get('ZHI')||0)*10000)*eval(recgrmh.get('ZHI')||0)/1000,0));}");
//		
//			//计算供热用原煤量		
//			sb.append("if(e.row==2 || e.row==5 || e.row==6 || e.row==7 || e.row==8 || e.row==9){ rec2 = gridDiv_ds.getAt(10);");
//			sb.append("recfadml = gridDiv_ds.getAt(2);recgongrml = gridDiv_ds.getAt(5);");
//			sb.append("recsunh = gridDiv_ds.getAt(6);recqity = gridDiv_ds.getAt(7);");
//			sb.append("recyuekc = gridDiv_ds.getAt(8);recyuemkc = gridDiv_ds.getAt(9);");
//			
//			sb.append("rec2.set('ZHI',Round(eval(recfadml.get('ZHI')||0)+eval(recgongrml.get('ZHI')||0)");
//			sb.append("+eval(recsunh.get('ZHI')||0)+eval(recqity.get('ZHI')||0)");
//			sb.append("+eval(recyuemkc.get('ZHI')||0)-eval(recyuekc.get('ZHI')||0),0));}");
//		
//		sb.append("});");
		
		String handler = 
			"gridDiv_grid.on('afteredit',\n" +
			"    function(e){\n" + 
			"        if(e.row==0){\n" + 
			"\n" + 
			"            fadl = eval(gridDiv_ds.getAt(0).get('ZHI')||0);\n" + 
			"            fadbmh = eval(gridDiv_ds.getAt(1).get('ZHI')||0);\n" + 
			"            fadbml = eval(Round(fadl * 10000 * fadbmh / 1000 / 1000, 0));\n" + 
			"            fadtrmh = eval(gridDiv_ds.getAt(3).get('ZHI')||0);\n" + 
			"            fadtrml = eval(Round(fadl * 10000 * fadtrmh / 1000 / 1000, 0));\n" + 
			"\n" + 
			"            gongrbml = eval(gridDiv_ds.getAt(7).get('ZHI')||0);\n" + 
			"            rulrz = eval(gridDiv_ds.getAt(18).get('ZHI')||0);\n" + 
			"            yunssh_bm = eval(Round(eval(gridDiv_ds.getAt(10).get('ZHI')||0) * rulrz / 29.271, 0));\n" + 
			"            qity_bm = eval(Round(eval(gridDiv_ds.getAt(11).get('ZHI')||0) * rulrz / 29.271, 0));\n" + 
			"            yuecbmkc = eval(gridDiv_ds.getAt(12).get('ZHI')||0);\n" + 
			"            yuembmkc = eval(gridDiv_ds.getAt(13).get('ZHI')||0);\n" + 
			"\n" + 
			"            gongrtrml = eval(gridDiv_ds.getAt(9).get('ZHI')||0);\n" + 
			"            yunssh = eval(gridDiv_ds.getAt(10).get('ZHI')||0);\n" + 
			"            qity = eval(gridDiv_ds.getAt(11).get('ZHI')||0);\n" + 
			"            yuectrmkc = eval(gridDiv_ds.getAt(15).get('ZHI')||0);\n" + 
			"            yuemtrmkc = eval(gridDiv_ds.getAt(16).get('ZHI')||0);\n" + 
			"\n" + 
			"            fadbml_rec = gridDiv_ds.getAt(2);\n" + 
			"            fadtrml_rec = gridDiv_ds.getAt(4);\n" + 
			"            biaomxqsl_rec = gridDiv_ds.getAt(14);\n" + 
			"            tianrmxqsl_rec = gridDiv_ds.getAt(17);\n" + 
			"\n" + 
			"            fadbml_rec.set('ZHI',fadbml);\n" + 
			"            fadtrml_rec.set('ZHI',fadtrml);\n" + 
			"            biaomxqsl_rec.set('ZHI', fadbml + gongrbml + yunssh_bm + qity_bm + yuembmkc - yuecbmkc);\n" + 
			"            tianrmxqsl_rec.set('ZHI', fadtrml + gongrtrml + yunssh + qity + yuemtrmkc - yuectrmkc);\n" + 
			"\n" + 
			"        }\n" + 
			"        if(e.row==1){\n" + 
			"\n" + 
			"            fadl = eval(gridDiv_ds.getAt(0).get('ZHI')||0);\n" + 
			"            fadbmh = eval(gridDiv_ds.getAt(1).get('ZHI')||0);\n" + 
			"            fadbml = eval(Round(fadl * 10000 * fadbmh / 1000 / 1000, 0));\n" + 
			"\n" + 
			"            gongrbml = eval(gridDiv_ds.getAt(7).get('ZHI')||0);\n" + 
			"            rulrz = eval(gridDiv_ds.getAt(18).get('ZHI')||0);\n" + 
			"            yunssh_bm = eval(Round(eval(gridDiv_ds.getAt(10).get('ZHI')||0) * rulrz / 29.271, 0));\n" + 
			"            qity_bm = eval(Round(eval(gridDiv_ds.getAt(11).get('ZHI')||0) * rulrz / 29.271, 0));\n" + 
			"            yuecbmkc = eval(gridDiv_ds.getAt(12).get('ZHI')||0);\n" + 
			"            yuembmkc = eval(gridDiv_ds.getAt(13).get('ZHI')||0);\n" + 
			"\n" + 
			"            fadbml_rec = gridDiv_ds.getAt(2);\n" + 
			"            biaomxqsl_rec = gridDiv_ds.getAt(14);\n" + 
			"\n" + 
			"            fadbml_rec.set('ZHI',fadbml);\n" + 
			"            biaomxqsl_rec.set('ZHI', fadbml + gongrbml + yunssh_bm + qity_bm + yuembmkc - yuecbmkc);\n" + 
			"\n" + 
			"        }\n" + 
			"        if(e.row==3){\n" + 
			"\n" + 
			"            fadl = eval(gridDiv_ds.getAt(0).get('ZHI')||0);\n" + 
			"            fadtrmh = eval(gridDiv_ds.getAt(3).get('ZHI')||0);\n" + 
			"            fadtrml = eval(Round(fadl * 10000 * fadtrmh / 1000 / 1000, 0));\n" + 
			"\n" + 
			"            gongrtrml = eval(gridDiv_ds.getAt(9).get('ZHI')||0);\n" + 
			"            yunssh = eval(gridDiv_ds.getAt(10).get('ZHI')||0);\n" + 
			"            qity = eval(gridDiv_ds.getAt(11).get('ZHI')||0);\n" + 
			"            yuectrmkc = eval(gridDiv_ds.getAt(15).get('ZHI')||0);\n" + 
			"            yuemtrmkc = eval(gridDiv_ds.getAt(16).get('ZHI')||0);\n" + 
			"\n" + 
			"            fadtrml_rec = gridDiv_ds.getAt(4);\n" + 
			"            tianrmxqsl_rec = gridDiv_ds.getAt(17);\n" + 
			"\n" + 
			"            fadtrml_rec.set('ZHI',fadtrml);\n" + 
			"            tianrmxqsl_rec.set('ZHI', fadtrml + gongrtrml + yunssh + qity + yuemtrmkc - yuectrmkc);\n" + 
			"\n" + 
			"        }\n" + 
			"        if(e.row==5){\n" + 
			"\n" + 
			"            gongrl = eval(gridDiv_ds.getAt(5).get('ZHI')||0);\n" + 
			"            gongrbmh = eval(gridDiv_ds.getAt(6).get('ZHI')||0);\n" + 
			"            gongrbml = eval(Round(gongrl * 10000 * gongrbmh / 1000, 0));\n" + 
			"            gongrtrmh = eval(gridDiv_ds.getAt(8).get('ZHI')||0);\n" + 
			"            gongrtrml = eval(Round(gongrl * 10000 * gongrtrmh / 1000, 0));\n" + 
			"\n" + 
			"            fadbml = eval(gridDiv_ds.getAt(2).get('ZHI')||0);\n" + 
			"            rulrz = eval(gridDiv_ds.getAt(18).get('ZHI')||0);\n" + 
			"            yunssh_bm = eval(Round(eval(gridDiv_ds.getAt(10).get('ZHI')||0) * rulrz / 29.271, 0));\n" + 
			"            qity_bm = eval(Round(eval(gridDiv_ds.getAt(11).get('ZHI')||0) * rulrz / 29.271, 0));\n" + 
			"            yuecbmkc = eval(gridDiv_ds.getAt(12).get('ZHI')||0);\n" + 
			"            yuembmkc = eval(gridDiv_ds.getAt(13).get('ZHI')||0);\n" + 
			"\n" + 
			"            fadtrml = eval(gridDiv_ds.getAt(4).get('ZHI')||0);\n" + 
			"            yunssh = eval(gridDiv_ds.getAt(10).get('ZHI')||0);\n" + 
			"            qity = eval(gridDiv_ds.getAt(11).get('ZHI')||0);\n" + 
			"            yuectrmkc = eval(gridDiv_ds.getAt(15).get('ZHI')||0);\n" + 
			"            yuemtrmkc = eval(gridDiv_ds.getAt(16).get('ZHI')||0);\n" + 
			"\n" + 
			"            gongrbml_rec = gridDiv_ds.getAt(7);\n" + 
			"            gongrtrml_rec = gridDiv_ds.getAt(9);\n" + 
			"            biaomxqsl_rec = gridDiv_ds.getAt(14);\n" + 
			"            tianrmxqsl_rec = gridDiv_ds.getAt(17);\n" + 
			"\n" + 
			"            gongrbml_rec.set('ZHI',gongrbml);\n" + 
			"            gongrtrml_rec.set('ZHI',gongrtrml);\n" + 
			"            biaomxqsl_rec.set('ZHI', fadbml + gongrbml + yunssh_bm + qity_bm + yuembmkc - yuecbmkc);\n" + 
			"            tianrmxqsl_rec.set('ZHI', fadtrml + gongrtrml + yunssh + qity + yuemtrmkc - yuectrmkc);\n" + 
			"\n" + 
			"        }\n" + 
			"        if(e.row==6){\n" + 
			"\n" + 
			"            gongrl = eval(gridDiv_ds.getAt(5).get('ZHI')||0);\n" + 
			"            gongrbmh = eval(gridDiv_ds.getAt(6).get('ZHI')||0);\n" + 
			"            gongrbml = eval(Round(gongrl * 10000 * gongrbmh / 1000, 0));\n" + 
			"\n" + 
			"            fadbml = eval(gridDiv_ds.getAt(2).get('ZHI')||0);\n" + 
			"            rulrz = eval(gridDiv_ds.getAt(18).get('ZHI')||0);\n" + 
			"            yunssh_bm = eval(Round(eval(gridDiv_ds.getAt(10).get('ZHI')||0) * rulrz / 29.271, 0));\n" + 
			"            qity_bm = eval(Round(eval(gridDiv_ds.getAt(11).get('ZHI')||0) * rulrz / 29.271, 0));\n" + 
			"            yuecbmkc = eval(gridDiv_ds.getAt(12).get('ZHI')||0);\n" + 
			"            yuembmkc = eval(gridDiv_ds.getAt(13).get('ZHI')||0);\n" + 
			"\n" + 
			"            gongrbml_rec = gridDiv_ds.getAt(7);\n" + 
			"            biaomxqsl_rec = gridDiv_ds.getAt(14);\n" + 
			"\n" + 
			"            gongrbml_rec.set('ZHI',gongrbml);\n" + 
			"            biaomxqsl_rec.set('ZHI', fadbml + gongrbml + yunssh_bm + qity_bm + yuembmkc - yuecbmkc);\n" + 
			"\n" + 
			"        }\n" + 
			"        if(e.row==8){\n" + 
			"\n" + 
			"            gongrl = eval(gridDiv_ds.getAt(5).get('ZHI')||0);\n" + 
			"            gongrtrmh = eval(gridDiv_ds.getAt(8).get('ZHI')||0);\n" + 
			"            gongrtrml = eval(Round(gongrl * 10000 * gongrtrmh / 1000, 0));\n" + 
			"\n" + 
			"            fadtrml = eval(gridDiv_ds.getAt(4).get('ZHI')||0);\n" + 
			"            yunssh = eval(gridDiv_ds.getAt(10).get('ZHI')||0);\n" + 
			"            qity = eval(gridDiv_ds.getAt(11).get('ZHI')||0);\n" + 
			"            yuectrmkc = eval(gridDiv_ds.getAt(15).get('ZHI')||0);\n" + 
			"            yuemtrmkc = eval(gridDiv_ds.getAt(16).get('ZHI')||0);\n" + 
			"\n" + 
			"            gongrtrml_rec = gridDiv_ds.getAt(9);\n" + 
			"            tianrmxqsl_rec = gridDiv_ds.getAt(17);\n" + 
			"\n" + 
			"            gongrtrml_rec.set('ZHI',gongrtrml);\n" + 
			"            tianrmxqsl_rec.set('ZHI', fadtrml + gongrtrml + yunssh + qity + yuemtrmkc - yuectrmkc);\n" + 
			"\n" + 
			"        }\n" + 
			"        if(e.row==10 || e.row==11 || e.row==12 || e.row==13 || e.row==15 || e.row==16 || e.row==18){\n" + 
			"\n" + 
			"            fadbml = eval(gridDiv_ds.getAt(2).get('ZHI')||0);\n" + 
			"            gongrbml = eval(gridDiv_ds.getAt(7).get('ZHI')||0);\n" + 
			"            rulrz = eval(gridDiv_ds.getAt(18).get('ZHI')||0);\n" + 
			"            yunssh_bm = eval(Round(eval(gridDiv_ds.getAt(10).get('ZHI')||0) * rulrz / 29.271, 0));\n" + 
			"            qity_bm = eval(Round(eval(gridDiv_ds.getAt(11).get('ZHI')||0) * rulrz / 29.271, 0));\n" + 
			"            yuecbmkc = eval(gridDiv_ds.getAt(12).get('ZHI')||0);\n" + 
			"            yuembmkc = eval(gridDiv_ds.getAt(13).get('ZHI')||0);\n" + 
			"\n" + 
			"            fadtrml = eval(gridDiv_ds.getAt(4).get('ZHI')||0);\n" + 
			"            gongrtrml = eval(gridDiv_ds.getAt(9).get('ZHI')||0);\n" + 
			"            yunssh = eval(gridDiv_ds.getAt(10).get('ZHI')||0);\n" + 
			"            qity = eval(gridDiv_ds.getAt(11).get('ZHI')||0);\n" + 
			"            yuectrmkc = eval(gridDiv_ds.getAt(15).get('ZHI')||0);\n" + 
			"            yuemtrmkc = eval(gridDiv_ds.getAt(16).get('ZHI')||0);\n" + 
			"\n" + 
			"            biaomxqsl_rec = gridDiv_ds.getAt(14);\n" + 
			"            tianrmxqsl_rec = gridDiv_ds.getAt(17);\n" + 
			"\n" + 
			"            biaomxqsl_rec.set('ZHI', fadbml + gongrbml + yunssh_bm + qity_bm + yuembmkc - yuecbmkc);\n" + 
			"            tianrmxqsl_rec.set('ZHI', fadtrml + gongrtrml + yunssh + qity + yuemtrmkc - yuectrmkc);\n" + 
			"\n" + 
			"        }\n" + 
			"    }\n" + 
			");";
		sb.append(handler);
		
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
			this.setYuefValue(null);
			this.getYuefModels();
			visit.setShifsh(true);
			setTbmsg(null);
		}
			
			getSelectData();
		
		
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
		cn.Close();
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