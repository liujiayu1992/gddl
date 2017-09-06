package com.zhiren.dc.chengbgl.fahhtpp;
/* 
* 时间：2009-03-14
* 作者： 曹林
* 修改内容： 
* 1，按时间段查询发货合同匹配
* 2，条件改为hedbz>=2
*/ 
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.Locale;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Fahhtpp extends BasePage implements PageValidateListener {
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
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
	
//	绑定日期
	private String riq;
	public String getRiq() {
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
	private String riq1;
	public String getRiq1() {
		return riq1;
	}
	public void setRiq1(String riq) {
		this.riq1 = riq;
	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		if(getChange()==null || "".equals(getChange())) {
			setMsg("没有做出任何更改！");
			return;
		}
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "ShujshH.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
		}
		List fhlist = new ArrayList();
		while(rsl.next()) {
			Jilcz.addFahid(fhlist,rsl.getString("id"));
		}
		con.commit();
		con.Close();
		Chengbjs.CountChengb(visit.getDiancxxb_id(), fhlist, false);
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			init();
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		String strdhrq = DateUtil.FormatOracleDate(getRiq());
		String strdhrq1 = DateUtil.FormatOracleDate(getRiq1());
		StringBuffer sb = new StringBuffer();
		sb.append("select f.id, g.mingc gongysb_id, m.mingc meikxxb_id, \n")
		.append("p.mingc pinzb_id, h.hetbh hetb_id, f.daohrq, \n")
		.append("(select fc.mingc from chezxxb fc where fc.id = f.faz_id) faz_id, \n")
		.append("(select dc.mingc from chezxxb dc where dc.id = f.daoz_id) daoz_id, \n")
		.append("f.jingz, f.biaoz, f.ches, f.chec, j.mingc jihkjb_id \n")
		.append("from fahb f, gongysb g, meikxxb m, pinzb p, jihkjb j, hetb h \n")
		.append("where f.gongysb_id = g.id and f.meikxxb_id = m.id \n")
		.append("and f.pinzb_id = p.id and f.jihkjb_id = j.id \n")
		.append("and f.hetb_id = h.id(+) and f.liucztb_id=").append("1").append(" \n")
//		煤款未结算
		.append("and (f.jiesb_id=0 or f.jiesb_id is null)	\n")	
//		运费未结算
		.append("and getYunfjsbid(f.id)=0	\n")
//		.append("and f.hetb_id = h.id(+) and f.hedbz>=").append(SysConstant.HEDBZ_YJJ).append("\n")
		.append("and f.daohrq >=").append(strdhrq).append("and f.daohrq <=").append(strdhrq1).append("\n")
		.append(" order by f.daohrq");
//		原来的逻辑结算只要入账就不再能重算
//		.append("and f.id in (select f.id from fahb f,jiesb j ")
//		.append("where f.hedbz >= ").append(SysConstant.HEDBZ_YJJ)
//		.append(" and and f.jiesb_id = j.id(+) ").append("and f.daohrq >=")
//		.append(strdhrq).append(" and f.daohrq <= ").append(strdhrq1)
//		.append(" minus select fahb_id from danjcpb dj,chepb c, fahb f, jiesyfb jy ")
//		.append(" where dj.chepb_id = c.id and f.id = c.fahb_id")
//		.append(" and f.hedbz < ").append(SysConstant.HEDBZ_YJJ)
//		.append(" and dj.yunfjsb_id=jy.id ").append("and f.daohrq >=")
//		.append(strdhrq).append(" and f.daohrq <= ").append(strdhrq1)
//		.append(" and jy.ruzrq is not null group by fahb_id)");
	
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl == null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyHeight");
		egu.addPaging(0);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		//设置多选框
		//egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		//设置每页显示行数
		egu.addPaging(25);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor=null;

		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(90);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(90);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(50);
		egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("hetb_id").setHeader(Locale.hetb_id_fahb);
		egu.getColumn("hetb_id").setWidth(100);
		egu.getColumn("hetb_id").setEditor(null);
		egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
		egu.getColumn("daohrq").setWidth(77);
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("faz_id").setHeader(Locale.faz_id_fahb);
		egu.getColumn("faz_id").setWidth(65);
		egu.getColumn("faz_id").setEditor(null);
		egu.getColumn("daoz_id").setHeader(Locale.daoz_id_fahb);
		egu.getColumn("daoz_id").setWidth(65);
		egu.getColumn("daoz_id").setEditor(null);
		egu.getColumn("jingz").setHeader(Locale.jingz_fahb);
		egu.getColumn("jingz").setWidth(60);
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("biaoz").setHeader(Locale.biaoz_fahb);
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("ches").setHeader(Locale.ches_fahb);
		egu.getColumn("ches").setWidth(60);
		egu.getColumn("ches").setEditor(null);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("chec").setEditor(null);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(65);
		egu.getColumn("jihkjb_id").setEditor(null);

		egu.addTbarText("到货日期:");
		DateField df = new DateField();
		df.Binding("RIQ","");
		df.setValue(getRiq());
		egu.addToolbarItem(df.getScript());
		DateField df1 = new DateField();
		df1.Binding("RIQ1","");
		df1.setValue(getRiq1());
		egu.addToolbarItem(df1.getScript());
		GridButton refurbish = new GridButton("刷新","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton("匹配", GridButton.ButtonType_SaveAll, "SaveButton");
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
		if(getExtGrid()==null){
			return "";
		}
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid()==null){
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
			setRiq(DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(new Date())));
			setRiq1(DateUtil.FormatDate(new Date()));
			init();
		}
	} 
	
	private void init() {
		setExtGrid(null);
		getSelectData();
	}
}
