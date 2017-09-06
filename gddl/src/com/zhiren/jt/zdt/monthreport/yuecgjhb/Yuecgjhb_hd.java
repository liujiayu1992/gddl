package com.zhiren.jt.zdt.monthreport.yuecgjhb;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：王磊
 * 时间：2009-11-30
 * 描述：月采购计划机组类型下拉框初始化
 */
/*
 * 作者：王磊
 * 时间：2009-12-17
 * 描述：修改月采购计划供应商、煤矿、车站下拉框未初始化导致数据存储失败的问题。
 */
/*
 * 作者：夏峥
 * 时间：2012-06-18
 * 描述：修正界面显示宽度问题
 */
/*
 * 作者：夏峥
 * 时间：2012-06-29
 * 描述：优化并修正生成方法
 */
public class Yuecgjhb_hd extends BasePage implements PageValidateListener {
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
		Visit visit = (Visit) this.getPage().getVisit();
		
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("begin\n");
		
		ResultSetList rsl=visit.getExtGrid1().getDeleteResultSet(getChange());
		while (rsl.next()){
			//System.out.println("保存时进入删除");
			String cc ="delete from yuecgjhb " +
			"where id="+rsl.getString("id")+";\n";
			sb.append(cc.toString());
		}
		
		rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			setMsg("没有需要保存的记录！");
			return;
		}

		
		while (rsl.next()) {
			long diancxxb_id=(getExtGrid().getColumn("diancxxb_id").combo).getBeanId(rsl.getString("diancxxb_id"));
			if ("0".equals(rsl.getString("id"))) {
				sb.append("insert into yuecgjhb (ID,RIQ,DIANCXXB_ID,GONGYSB_ID,JIHKJB_ID,FAZ_ID,DAOZ_ID,YUEJHCGL,CHEBJG,YUNF,ZAF,REZ,JIAKK,\n");
				sb.append("JIHDDSJYSL,HUIFF,LIUF,DAOCJ,BIAOMDJ,JIZZT,YUNSFSB_ID,MEIKXXB_ID) \n");
				sb.append("values (getnewid(").append(diancxxb_id).append("),");
				sb.append(DateUtil.FormatOracleDate(rsl.getString("riq")));
				sb.append(",").append(diancxxb_id);
				sb.append(",").append(((IDropDownModel) getGongysModel()).getBeanId(rsl.getString("gongysb_id")));
				sb.append(",").append((getExtGrid().getColumn("jihkjb_id").combo).getBeanId(rsl.getString("jihkjb_id")));
				sb.append(",").append(((IDropDownModel) getChezModel()).getBeanId(rsl.getString("faz_id")));
				sb.append(",").append((getExtGrid().getColumn("daoz_id").combo).getBeanId(rsl.getString("daoz_id")));
				sb.append(",").append(rsl.getDouble("yuejhcgl"));
				sb.append(",").append(rsl.getDouble("chebjg"));
				sb.append(",").append(rsl.getDouble("yunf"));
				sb.append(",").append(rsl.getDouble("zaf"));
				sb.append(",").append(rsl.getDouble("rez"));
				sb.append(",").append(rsl.getDouble("jiakk"));
				sb.append(",").append(rsl.getDouble("jihddsjysl"));
				sb.append(",").append(rsl.getDouble("huiff"));
				sb.append(",").append(rsl.getDouble("liuf"));
				sb.append(",").append(rsl.getDouble("daocj"));
				sb.append(",").append(rsl.getDouble("biaomdj"));
				sb.append(",").append(rsl.getDouble("jizzt"));
				sb.append(",").append((getExtGrid().getColumn("yunsfsb_id").combo).getBeanId(rsl.getString("yunsfsb_id")));
				//sb.append(",").append((getExtGrid().getColumn("pinzb_id").combo).getBeanId(rsl.getString("pinzb_id")));
				sb.append(",").append(((IDropDownModel) getMeikModel()).getBeanId(rsl.getString("meikxxb_id")));
				sb.append(");\n");
				
			}else{
 				sb.append("update yuecgjhb set DIANCXXB_ID=");
 				sb.append(diancxxb_id).append(",gongysb_id=");
				sb.append(((IDropDownModel) getGongysModel()).getBeanId(rsl.getString("gongysb_id")));
				sb.append(",riq=").append(DateUtil.FormatOracleDate(rsl.getString("riq")));
				sb.append(",faz_id=").append(((IDropDownModel) getChezModel()).getBeanId(rsl.getString("faz_id")));
				sb.append(",daoz_id=").append((getExtGrid().getColumn("daoz_id").combo).getBeanId(rsl.getString("daoz_id")));
				//sb.append(",pinzb_id=").append((getExtGrid().getColumn("pinzb_id").combo).getBeanId(rsl.getString("pinzb_id")));
				sb.append(",yunsfsb_id=").append((getExtGrid().getColumn("yunsfsb_id").combo).getBeanId(rsl.getString("yunsfsb_id")));
				sb.append(",jihkjb_id=").append((getExtGrid().getColumn("jihkjb_id").combo).getBeanId(rsl.getString("jihkjb_id")));
				sb.append(",meikxxb_id=").append(((IDropDownModel) getMeikModel()).getBeanId(rsl.getString("meikxxb_id")));
				sb.append(",yuejhcgl=").append(rsl.getDouble("yuejhcgl"));
				sb.append(",chebjg=").append(rsl.getDouble("chebjg"));
				sb.append(",yunf=").append(rsl.getDouble("yunf"));
				sb.append(",zaf=").append(rsl.getDouble("zaf"));
				sb.append(",rez=").append(rsl.getDouble("rez"));
				sb.append(",jiakk=").append(rsl.getDouble("jiakk"));
				sb.append(",jihddsjysl=").append(rsl.getDouble("jihddsjysl"));
				sb.append(",huiff=").append(rsl.getDouble("huiff"));
				sb.append(",liuf=").append(rsl.getDouble("liuf"));
				sb.append(",daocj=").append(rsl.getDouble("daocj"));
				sb.append(",biaomdj=").append(rsl.getDouble("biaomdj"));
				sb.append(",jizzt=").append(rsl.getDouble("jizzt"));
				sb.append(" where id=").append(rsl.getString("id")).append(";\n");
				
			}
			
		}
		sb.append("end;");
		int flag = con.getInsert(sb.toString());
		
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
					+ sb);
			return;
		}
		con.commit();
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);
		
		con.commit();
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _CopyButton = false;
	public void CopyButton(IRequestCycle cycle) {
		_CopyButton = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData(null);
		}
		if(_CopyButton){
			_CopyButton=false;
			CoypLastYueData();
		}
	}

	public void CoypLastYueData(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
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
		
//		String StrMonth1="";
//		if(intMonth<10){
//			
//			StrMonth1="0"+intMonth;
//		}else{
//			StrMonth1=""+intMonth;
//		}
		//-----------------------------------
//		String strriq = intyear+"-"+StrMonth1+"-01";
		
		
		//当月份是1时上个月显示12
		if(intMonth-1==0){
			intMonth=12;
			intyear=intyear-1;
		}else{
			intMonth=intMonth-1;
		}
		//当月份是1的时候显示01,
		String StrMonth="";
		if(intMonth<10){
			StrMonth="0"+intMonth;
		}else{
			StrMonth=""+intMonth;
		}
	
		String str_jizzt ="";
		
		if(getLeixDropDownValue()!=null){
			str_jizzt=" and y.jizzt="+getLeixDropDownValue().getId();
		}
		
		String str = "";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and dc.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();	
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
			
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and dc.id = " + getTreeid() + "";
			
		}
	  
		String sql = 
			"INSERT INTO YUECGJHB\n" +
			"(ID,RIQ,DIANCXXB_ID,GONGYSB_ID,MEIKXXB_ID,JIHKJB_ID,FAZ_ID,DAOZ_ID,YUNSFSB_ID,YUEJHCGL,REZ,\n" + 
			"HUIFF,LIUF,CHEBJG,YUNF,ZAF,DAOCJ,BIAOMDJ,JIAKK,JIHDDSJYSL,JIZZT)\n" + 
			"  (SELECT GETNEWID("+getTreeid()+") AS ID,\n" + 
			"          ADD_MONTHS(Y.RIQ, 1) AS RIQ,\n" + 
			"          DC.ID AS DIANCXXB_ID,\n" + 
			"          Y.GONGYSB_ID AS GONGYSB_ID,\n" + 
			"          Y.MEIKXXB_ID AS MEIKXXB_ID,\n" + 
			"          Y.JIHKJB_ID AS JIHKJB_ID,\n" + 
			"          Y.FAZ_ID AS FAZ_ID,\n" + 
			"          Y.DAOZ_ID AS DAOZ_ID,\n" + 
			"          Y.YUNSFSB_ID AS YUNSFSB_ID,\n" + 
			"          Y.YUEJHCGL AS YUEJHCGL,\n" + 
			"          Y.REZ AS REZ,\n" + 
			"          Y.HUIFF AS HUIFF,\n" + 
			"          Y.LIUF AS LIUF,\n" + 
			"          Y.CHEBJG AS CHEBJG,\n" + 
			"          Y.YUNF AS YUNF,\n" + 
			"          Y.ZAF AS ZAF,\n" + 
			"          Y.DAOCJ AS DAOCJ,\n" + 
			"          Y.BIAOMDJ AS BIAOMDJ,\n" + 
			"          Y.JIAKK AS JIAKK,\n" + 
			"          Y.JIHDDSJYSL AS JIHDDSJYSL,\n" + 
			"          Y.JIZZT\n" + 
			"     FROM YUECGJHB Y, DIANCXXB DC\n" + 
			"    WHERE Y.DIANCXXB_ID = DC.ID(+)\n" + 
			"      AND TO_CHAR(Y.RIQ, 'yyyy-mm') = '" + intyear + "-" + StrMonth+ "'\n" + 
			"    "+str+"  "+ str_jizzt +"      )";

	 
		con.getInsert(sql);
		con.commit();
		getSelectData(null);
		con.Close();
	}
	
//	private String FormatDate(Date _date) {
//		if (_date == null) {
//			return "";
//		}
//		return DateUtil.Formatdate("yyyy-MM-dd", _date);
//	}

	public void getSelectData(ResultSetList rsl) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
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
		
		String str_jizzt ="";
		
		if(getLeixDropDownValue()!=null){
			str_jizzt=" and y.jizzt="+getLeixDropDownValue().getId();
		}
		
		String str = "";
		
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and dc.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
//			str = "and (d.id = " + getTreeid() + " or d.fuid = " + getTreeid()
//			+ ")";
			str = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and dc.id = " + getTreeid() + "";
			
		}
		if(rsl==null){
			String chaxun = 
				"select y.id as id,\n" +
				"       y.riq as riq,\n" + 
				"       dc.mingc as diancxxb_id,\n" + 
				"       g.mingc as gongysb_id,\n" + 
				"       m.mingc as meikxxb_id,\n" +
				"       j.mingc as jihkjb_id,\n" + 
				"       ch.mingc as faz_id,\n" + 
				"       che.mingc as daoz_id,\n" + 
				"       ys.mingc as yunsfsb_id,\n" +
				"       y.yuejhcgl as yuejhcgl,\n" + 
				"       y.rez as rez,\n" + 
				"       y.huiff as huiff,\n" +
				"       y.liuf as liuf,\n" +
				"       y.chebjg as chebjg,\n" + 
				"       y.yunf as yunf,\n" + 
				"       y.zaf as zaf,\n" + 
				"       y.daocj as daocj,\n" +
				"       y.biaomdj as biaomdj,\n" +
				"       y.jiakk as jiakk,\n" + 
				"       y.jihddsjysl as jihddsjysl\n ,y.jizzt \n" + 
				"  from yuecgjhb y, gongysb g, chezxxb ch, chezxxb che, diancxxb dc,jihkjb j,yunsfsb ys,meikxxb m\n" + 
				" where y.gongysb_id = g.id(+)\n" + 
				"   and y.faz_id = ch.id(+)\n" + 
				"   and y.daoz_id = che.id(+)\n" + 
				"   and y.diancxxb_id = dc.id(+)\n" + 
				"   and y.jihkjb_id = j.id(+)\n" +
				"   and y.yunsfsb_id = ys.id(+)\n" +
				"   and y.meikxxb_id = m.id(+)\n" +
				"   and to_char(y.riq,'yyyy-mm') ='" + intyear + "-" + StrMonth+ "'  \n"+
				"    "+str+"  "+ str_jizzt +
				"   order by y.id";
		//System.out.println(chaxun);	
		rsl = con.getResultSetList(chaxun);
	}
	boolean showBtn = false;
	if(rsl.next()){
		rsl.beforefirst();
		showBtn = false;
	}else{
		showBtn = true;
	}
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("yuecgjhb");
   	egu.setWidth("bodyWidth");
	egu.getColumn("riq").setCenterHeader("日期");
	egu.getColumn("riq").setHidden(true);
	egu.getColumn("riq").setEditor(null);
	egu.getColumn("diancxxb_id").setCenterHeader("电厂名称");
	egu.getColumn("gongysb_id").setCenterHeader("矿别名称");
	egu.getColumn("meikxxb_id").setCenterHeader("煤矿名称");
	egu.getColumn("jihkjb_id").setCenterHeader("计划口径");
	egu.getColumn("faz_id").setCenterHeader("发站");
	egu.getColumn("daoz_id").setCenterHeader("到站");
	egu.getColumn("yunsfsb_id").setCenterHeader("运输方式");
	egu.getColumn("yuejhcgl").setCenterHeader("月计划<br>采购量(吨)");
	egu.getColumn("rez").setCenterHeader("热值<br>(MJ/Kg)");
	egu.getColumn("huiff").setCenterHeader("挥发分%");
	egu.getColumn("liuf").setCenterHeader("硫分%");
	egu.getColumn("chebjg").setCenterHeader("车板价格<br>(元/吨)");
	egu.getColumn("yunf").setCenterHeader("运费<br>(元/吨)");
	egu.getColumn("zaf").setCenterHeader("杂费<br>(元/吨)");
	egu.getColumn("daocj").setCenterHeader("到厂价<br>(元/吨)");
	egu.getColumn("biaomdj").setCenterHeader("标煤单价<br>(元/吨)");
	egu.getColumn("jiakk").setCenterHeader("加扣款<br>(元/吨)");
	egu.getColumn("jiakk").setHidden(true);
	egu.getColumn("jihddsjysl").setCenterHeader("计划到达时<br>间与数量");
	egu.getColumn("jihddsjysl").setHidden(true);
	
	egu.getColumn("jizzt").setHeader("机组状态");
	egu.getColumn("jizzt").setHidden(true);
	
	//设置不可编辑的颜色
//	egu.getColumn("daohldy").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
	
	
	//设定列初始宽度
	egu.getColumn("riq").setWidth(80);
	egu.getColumn("gongysb_id").setWidth(70);
	egu.getColumn("diancxxb_id").setWidth(70);
	egu.getColumn("meikxxb_id").setWidth(70);
	egu.getColumn("jihkjb_id").setWidth(70);
	egu.getColumn("faz_id").setWidth(50);
	egu.getColumn("daoz_id").setWidth(50);
	egu.getColumn("yunsfsb_id").setWidth(50);
	egu.getColumn("yuejhcgl").setWidth(50);
	egu.getColumn("yuejhcgl").setDefaultValue("0");
	egu.getColumn("rez").setWidth(50);
	egu.getColumn("rez").setDefaultValue("0");
	egu.getColumn("huiff").setWidth(50);
	egu.getColumn("huiff").setDefaultValue("0");
	egu.getColumn("liuf").setWidth(50);
	egu.getColumn("liuf").setDefaultValue("0");
	egu.getColumn("chebjg").setWidth(60);
	egu.getColumn("chebjg").setDefaultValue("0");
	egu.getColumn("yunf").setWidth(60);
	egu.getColumn("yunf").setDefaultValue("0");
	egu.getColumn("zaf").setWidth(60);
	egu.getColumn("zaf").setDefaultValue("0");
	egu.getColumn("daocj").setWidth(60);
	egu.getColumn("daocj").setDefaultValue("0");
	egu.getColumn("biaomdj").setWidth(60);
	egu.getColumn("biaomdj").setEditor(null);
	egu.getColumn("biaomdj").setDefaultValue("0");
	egu.getColumn("jiakk").setWidth(60);
	egu.getColumn("jiakk").setDefaultValue("0");
	egu.getColumn("jihddsjysl").setWidth(70);
	egu.getColumn("jihddsjysl").setDefaultValue("0");
	
	egu.getColumn("jizzt").setWidth(60);
	egu.getColumn("jizzt").setDefaultValue(""+getLeixDropDownValue().getId());
	
	
	
	egu.setGridType(ExtGridUtil.Gridstyle_Edit);//设定grid可以编辑
	egu.addPaging(0);//设置分页
//	egu.setWidth(1000);//设置页面的宽度,当超过这个宽度时显示滚动条
	
	
	
	//*****************************************设置默认值****************************
	//	电厂下拉框
	int treejib2 = this.getDiancTreeJib();

	if (treejib2 == 1) {// 选集团时刷新出所有的电厂
		//egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		ComboBox cb_diancxxb = new ComboBox();
		egu.getColumn("diancxxb_id").setEditor(cb_diancxxb);
		egu.getColumn("diancxxb_id").setDefaultValue("");
		cb_diancxxb.setEditable(true);
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
		egu.getColumn("diancxxb_id").setReturnId(true);
	} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
		//egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		ComboBox cb_diancxxb = new ComboBox();
		egu.getColumn("diancxxb_id").setEditor(cb_diancxxb);
		egu.getColumn("diancxxb_id").setDefaultValue("");
		cb_diancxxb.setEditable(true);
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id,mingc from diancxxb where (fuid="
									+ getTreeid() + " or shangjgsid ="+getTreeid()+") order by mingc"));
		egu.getColumn("diancxxb_id").setReturnId(true);
	} else if (treejib == 3) {// 选电厂只刷新出该电厂
		//egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		ComboBox cb_diancxxb = new ComboBox();
		egu.getColumn("diancxxb_id").setEditor(cb_diancxxb);
		egu.getColumn("diancxxb_id").setDefaultValue("");
		cb_diancxxb.setEditable(true);
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc"));
		ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");			
		String mingc="";
		if(r.next()){
			mingc=r.getString("mingc");
		}
		egu.getColumn("diancxxb_id").setDefaultValue(mingc);
		
	}		
	
	//设置电厂默认到站
	egu.getColumn("daoz_id").setDefaultValue(this.getDiancDaoz());
	//设置日期的默认值,
	egu.getColumn("riq").setDefaultValue(intyear+"-"+StrMonth+"-01");
	
	
	
	//*************************下拉框*****************************************88
	//设置供应商的下拉框
	//egu.getColumn("gongysb_id").setEditor(new ComboBox());
//	ComboBox cb_gongysb = new ComboBox();
//	egu.getColumn("gongysb_id").setEditor(cb_gongysb);
//	egu.getColumn("gongysb_id").setDefaultValue("");
//	cb_gongysb.setEditable(true);
	/*
	 //与电厂相关联的供应商
	String GongysSql = "select g.id,g.mingc from diancxxb d,gongysdcglb gd,gongysb  g\n"
				+ "where gd.diancxxb_id=d.id\n"
				+ "and gd.gongysb_id=g.id\n"
				+ "and d.id="+visit.getDiancxxb_id();
	*/
//	String GongysSql="select id,mingc from gongysb order by mingc";
//	egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(GongysSql));
	//设置计划口径的下拉框
	egu.getColumn("jihkjb_id").setEditor(new ComboBox());
	String JihkjSql="select id,mingc from jihkjb order by mingc ";
	egu.getColumn("jihkjb_id").setComboEditor(egu.gridId, new IDropDownModel(JihkjSql));
	//设置发站下拉框
//	ComboBox cb_faz=new ComboBox();
//	egu.getColumn("faz_id").setEditor(cb_faz);
//	cb_faz.setEditable(true);
//	String fazSql="select id ,mingc from chezxxb c where c.leib='车站' order by c.mingc";
//	egu.getColumn("faz_id").setComboEditor(egu.gridId, new IDropDownModel(fazSql));
	//设置到站下拉框
	ComboBox cb_daoz=new ComboBox();
	egu.getColumn("daoz_id").setEditor(cb_daoz);
	cb_daoz.setEditable(true);

	String daozSql="select id,mingc from chezxxb order by mingc";
	egu.getColumn("daoz_id").setComboEditor(egu.gridId, new IDropDownModel(daozSql));
	
	//设置运输方式下拉框
	ComboBox cb_yunsfs=new ComboBox();
	egu.getColumn("yunsfsb_id").setEditor(cb_yunsfs);
	cb_daoz.setEditable(true);

	String yunsfsSql="select id,mingc from yunsfsb order by mingc";
	egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId, new IDropDownModel(yunsfsSql));
	
	
	
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
//		egu.addTbarText("单位:");
//		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
//		setTree(etu);
//		egu.addTbarTreeBtn("diancTree"); 
		
		egu.addTbarText("单位:");
//		System.out.println(getTreeid());
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
	
		//设定工具栏下拉框自动刷新
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});YUEF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// 设置分隔符
		
		egu.addTbarText("机组状态:");
		ComboBox comb3=new ComboBox();
		comb3.setTransform("LeixDropDown");
		comb3.setId("LeixDropDown");//和自动刷新绑定
		comb3.setLazyRender(true);//动态绑定
		comb3.setWidth(80);
		egu.addToolbarItem(comb3.getScript());
		
		egu.addOtherScript("LeixDropDown.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// 设置分隔符
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		
		if(showBtn){
			//egu.addToolbarItem("{"+new GridButton("复制同期计划","function(){document.getElementById('CopyButton').click();}").getScript()+"}");
			StringBuffer cpb = new StringBuffer();
			cpb.append("function(){").append(
					"document.getElementById('CopyButton').click();}");
				GridButton cpr = new GridButton("复制前月数据", cpb.toString());
				cpr.setIcon(SysConstant.Btn_Icon_Copy);
				egu.addTbarBtn(cpr);
			}
			//egu.addTbarText("->");
			//egu.addTbarText("<font color=\"#EE0000\">单位:吨、元/吨、千卡/千克</font>");
	

		
		
//		---------------页面js的计算开始------------------------------------------
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){");
			//sb.append("e.record.set('DAOHLDY',parseFloat(e.record.get('SHIDLDY')==''?0:e.record.get('SHIDLDY'))/parseFloat(e.record.get('JIHLDY')==''?0:e.record.get('JIHLDY'))*100);");
			sb.append("e.record.set('DAOCJ',eval(e.record.get('CHEBJG')||0)+eval(e.record.get('YUNF')||0)+eval(e.record.get('ZAF')||0));");
			sb.append("if (e.record.get('REZ')!=0){ e.record.set('BIAOMDJ',eval(Round(eval(e.record.get('DAOCJ')||0)*29.271/eval(e.record.get('REZ')||0),2)||0));}");
		sb.append("});");
		
		
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("if(e.record.get('DIANCXXB_ID')=='合计'){e.cancel=true;}");//当电厂列的值是"合计"时,这一行不允许编辑
		sb.append("});");
		
		
       //设定合计列不保存
		sb.append("function gridDiv_save(record){if(record.get('diancxxb_id')=='合计') return 'continue';}");
		
		
		
		
		egu.addOtherScript(sb.toString());
		//---------------页面js计算结束--------------------------
		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
				+ "row = irow; \n"
				+ "if('GONGYSB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
				+ "gongysTree_window.show();}});\n");
		// egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){alert();if(e.Field=='MEIKXXB_ID'){e.record.set('YUANMKDW',e.value)}});\n");
		egu.setDefaultsortable(false);
		setExtGrid(egu);
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk_cz_kj,
				"gongysTree", "" + visit.getDiancxxb_id(), null, null, null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		StringBuffer handler = new StringBuffer();
		handler
				.append("function() { \n")
				.append("var cks = gongysTree_treePanel.getSelectionModel().getSelectedNode(); \n")
				.append("if(cks==null){gongysTree_window.hide();return;} \n")
				.append("rec = gridDiv_grid.getSelectionModel().getSelected(); \n")
				.append("if(cks.getDepth() == 4){ \n")
				.append("rec.set('GONGYSB_ID', cks.parentNode.parentNode.parentNode.text);\n")
				.append("rec.set('MEIKXXB_ID', cks.parentNode.parentNode.text);\n")
				//.append("rec.set('YUANMKDW', cks.parentNode.parentNode.text);\n")
				.append("rec.set('FAZ_ID', cks.parentNode.text);rec.set('JIHKJB_ID', cks.text);\n")
				.append("}else if(cks.getDepth() == 3){\n")
				.append("rec.set('GONGYSB_ID', cks.parentNode.parentNode.text);\n")
				.append("rec.set('MEIKXXB_ID', cks.parentNode.text);\n")
				//.append("rec.set('YUANMKDW', cks.parentNode.text);\n")
				.append("rec.set('FAZ_ID', cks.text);\n")
				.append("}else if(cks.getDepth() == 2){\n")
				.append("rec.set('GONGYSB_ID', cks.parentNode.text);\n")
				.append("rec.set('MEIKXXB_ID', cks.text);\n")
				//.append("rec.set('YUANMKDW', cks.text);\n")
				.append("}else if(cks.getDepth() == 1){\n")
				.append("rec.set('GONGYSB_ID', cks.text); }\n")
				.append("gongysTree_window.hide();\n")
				.append("return;")
				.append("}");
		ToolbarButton btn = new ToolbarButton(null, "确认", handler.toString());
		bbar.addItem(btn);
		visit.setDefaultTree(dt);
		
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
			this.setMsg(null);
			this.getYuefModels();
			setLeixDropDownValue(null);
			setLeixDropDownModel(null);
			setMeikModel(null);
			setGongysModel(null);
			setChezModel(null);
//			getSelectData(null);
			
		}
//		if(nianfchanged){
//			nianfchanged = false;
//			getSelectData(null);
//		}
//		if(Changeyuef){
//			Changeyuef = false;
//			getSelectData(null);
//		}
		getSelectData(null);
		
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
		if (_NianfValue == null) {
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
	
	
//	private String treeid;
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
//	public String getTreeScript() {
	//	return getTree().getWindowTreeScript();
	//}
	
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
	public String getTreedcScript() {
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
	
//	类型
	public IDropDownBean getLeixDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean) getLeixDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setLeixDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setLeixDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getLeixDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getLeixDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getLeixDropDownModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, "现役机组"));
		list.add(new IDropDownBean(1, "新增机组"));
		//list.add(new IDropDownBean(3, "棋盘表"));
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(list));
		return;
	}
	
	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setMeikModels() {
		String sql = "select id,mingc from meikxxb order by xuh,mingc";
		setMeikModel(new IDropDownModel(sql));
	}
	
	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb where leix=1 order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
	}
	
	public IPropertySelectionModel getChezModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			setChezModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setChezModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(_value);
	}

	public void setChezModels() {
		String sql = "select id,mingc from chezxxb order by xuh,mingc";
		setChezModel(new IDropDownModel(sql));
	}
}