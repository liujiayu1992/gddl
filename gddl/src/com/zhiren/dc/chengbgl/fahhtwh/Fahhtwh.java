package com.zhiren.dc.chengbgl.fahhtwh;
/*
 * 作者：songy
 * 时间：2011-03-23 
 * 描述：修改下拉菜单的排序，要求按照名称进行排序
 */
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.DateUtil;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：王磊
 * 时间：2009-10-22
 * 描述：合同下拉框增长合同号字母过滤
 */
public class Fahhtwh extends BasePage implements PageValidateListener{	
//	界面用户提示
	private String msg = "";
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
//	刷新数据日期绑定
	private String riq;
	public void setRiq(String riq) {
		this.riq = riq;
	}
	public String getRiq() {
		return riq;
	}
//	页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
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
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}else if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}
	
	public void Save() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long lngId = 0;
		int flag = 0;
		if(getChange( )==null || "".equals(getChange())) {
			setTbmsg("没有做出任何更改！");
			return;
		}
		con.setAutoCommit(false);
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Fahhtwh.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			if (rsl.getString("id").equals("0")) {
				lngId = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
				String sInsert = "insert into fahhtb(id,qisrq,jiesrq,gongysb_id,diancxxb_id,meikxxb_id,faz_id,hetb_id,pinzb_id) values(" 
					+ lngId + ","
					+ DateUtil.FormatOracleDate(rsl.getString("qisrq")) + ","
					+ DateUtil.FormatOracleDate(rsl.getString("jiesrq")) + ","
					+ getExtGrid().getColumn("gongysb_id").combo.getBeanId(rsl.getString("gongysb_id")) + ","
					+ visit.getDiancxxb_id() + ","
					+ getExtGrid().getColumn("meikxxb_id").combo.getBeanId(rsl.getString("meikxxb_id")) + ","
					+ getExtGrid().getColumn("faz_id").combo.getBeanId(rsl.getString("faz_id")) + ","
					+ getExtGrid().getColumn("hetb_id").combo.getBeanId(rsl.getString("hetb_id")) + ","
					+ getExtGrid().getColumn("pinzb_id").combo.getBeanId(rsl.getString("pinzb_id"))
					+	")";
				flag = con.getInsert(sInsert);
				if (flag == -1) {
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:" + sInsert);
					setMsg("保存过程出现错误！保存未成功！");
					con.rollBack();
					con.Close();
					return;
				}
			}else {
				String sUpdate = "update fahhtb set qisrq=" + DateUtil.FormatOracleDate(rsl.getString("qisrq"))
						+ ",jiesrq=" + DateUtil.FormatOracleDate(rsl.getString("jiesrq"))
						+ ",gongysb_id=" + getExtGrid().getColumn("gongysb_id").combo.getBeanId(rsl.getString("gongysb_id"))
						+ ",meikxxb_id=" + getExtGrid().getColumn("meikxxb_id").combo.getBeanId(rsl.getString("meikxxb_id"))
						+ ",faz_id=" + getExtGrid().getColumn("faz_id").combo.getBeanId(rsl.getString("faz_id"))
						+ ",hetb_id=" + getExtGrid().getColumn("hetb_id").combo.getBeanId(rsl.getString("hetb_id"))
						+ ",pinzb_id=" + getExtGrid().getColumn("pinzb_id").combo.getBeanId(rsl.getString("pinzb_id"))
						+ " where id=" + rsl.getString("id");
				flag = con.getUpdate(sUpdate);
				if (flag == -1) {
					WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail + "SQL:" + sUpdate);
					setMsg("保存过程出现错误！保存未成功！");
					con.rollBack();
					con.Close();
					return;
				}
			}
		}
		rsl = getExtGrid().getDeleteResultSet(getChange());
		if (rsl == null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Fahhtwh.Save 中 getExtGrid().getDeleteResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while(rsl.next()) {
			String sDelete = "delete fahhtb where id=" + rsl.getString("id");
			flag = con.getDelete(sDelete);
			if (flag == -1){
				WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:" + sDelete);
				setMsg("保存过程出现错误！保存未成功！");
				con.rollBack();
				con.Close();
				return;
			}
		}
		con.commit();
		con.Close();
		setTbmsg("保存成功");
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sSql = 
			"select id,qisrq,jiesrq,\n" +
			"(select mingc from gongysb where gongysb.id=gongysb_id) as gongysb_id,\n" + 
			"nvl((select mingc from meikxxb where meikxxb.id=meikxxb_id),'-') as meikxxb_id,\n" + 
			"nvl((select mingc from chezxxb where chezxxb.id=faz_id),'-') as faz_id,\n" + 
			"nvl((select mingc from pinzb where pinzb.id=pinzb_id), '-') as pinzb_id,\n" +
			"(select hetbh from hetb where hetb.id=hetb_id) as hetb_id\n" + 
			"from fahhtb\n" + 
			"where qisrq<=" + DateUtil.FormatOracleDate(getRiq()) + "\n" + 
			"and jiesrq>=" + DateUtil.FormatOracleDate(getRiq()) + "\n" + 
			"and diancxxb_id=" + visit.getDiancxxb_id() + "\n" +
			"order by qisrq";

		ResultSetList rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(25);
		//egu.setTableName("fahhtb");
		egu.getColumn("id").setHeader("编号");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("qisrq").setHeader("起始日期");
		egu.getColumn("jiesrq").setHeader("结束日期");
		egu.getColumn("gongysb_id").setHeader("发货单位");
		egu.getColumn("meikxxb_id").setHeader("煤矿单位");
		egu.getColumn("faz_id").setHeader("发站");
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("hetb_id").setHeader("合同编号");
		egu.getColumn("hetb_id").setWidth(200);
		
		egu.getColumn("qisrq").setDefaultValue(getRiq());
		egu.getColumn("jiesrq").setDefaultValue(getRiq());
		
		String sTSql = "";
//		发货单位
		ComboBox combFahdw= new ComboBox();
		egu.getColumn("gongysb_id").setEditor(combFahdw);
		combFahdw.setEditable(true);
		sTSql = "select distinct id, mingc mingc from gongysb where leix=1 and zhuangt=1 order by mingc \n";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(sTSql));
//		煤矿单位
		ComboBox combMeikdw= new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(combMeikdw);
		combMeikdw.setEditable(true);
		sTSql = "select -1 as id, '无' as mingc from dual union select distinct id, mingc from meikxxb where zhuangt=1 order by mingc ";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, new IDropDownModel(sTSql));
		egu.getColumn("meikxxb_id").editor.allowBlank = true;
//		车站
		ComboBox combChez= new ComboBox();
		egu.getColumn("faz_id").setEditor(combChez);
		combChez.setEditable(true);
		sTSql = "select -1 as id, '无' as mingc from dual union select distinct id, mingc from chezxxb   order by mingc ";
		egu.getColumn("faz_id").setComboEditor(egu.gridId, new IDropDownModel(sTSql));
		egu.getColumn("faz_id").editor.allowBlank = true;
//		品种
		ComboBox combPinz= new ComboBox();
		egu.getColumn("pinzb_id").setEditor(combPinz);
		combPinz.setEditable(true);
		sTSql = "select -1 as id, '无' as mingc from dual union select distinct id, mingc from pinzb where pinzb.leib = '煤'   order by mingc ";
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId, new IDropDownModel(sTSql));
		egu.getColumn("pinzb_id").editor.allowBlank = true;
//		合同
		ComboBox combHet= new ComboBox();
		egu.getColumn("hetb_id").setEditor(combHet);
		combHet.setEditable(true);
		sTSql = 
			"select id,hetbh from hetb where qisrq<=" + DateUtil.FormatOracleDate(getRiq()) + "\n" + 
			"and guoqrq>=" + DateUtil.FormatOracleDate(getRiq());
		egu.getColumn("hetb_id").setComboEditor(egu.gridId, new IDropDownModel(sTSql));
		
		egu.addTbarText("日期:");
		DateField dfRIQ = new DateField();
		dfRIQ.Binding("RIQ", "");
		dfRIQ.setValue(getRiq());
		egu.addToolbarItem(dfRIQ.getScript());
		egu.addTbarText("-");
		String sRefreshHandler = 
				"function(){var grid_Mrcd = gridDiv_ds.getModifiedRecords();" +
				"if (grid_Mrcd.length>0) {" +
				"Ext.MessageBox.confirm('消息提示','刷新界面后您所做的更改将不被保存,是否继续?',function(btn){" +
				"if (btn == 'yes') {" +
				"document.getElementById('RefreshButton').click();" +
				"}" +
				"})" +
				"}else {document.getElementById('RefreshButton').click();}" +
				"}";

		GridButton gRefresh = new GridButton("刷新",sRefreshHandler);
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		setExtGrid(egu);
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
	
	public void beginResponse(IMarkupWriter writer,IRequestCycle cycle){
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			init();
		}
	}
	
	private void init() {
		setExtGrid(null);
		setRiq(DateUtil.FormatDate( new Date()));
		getSelectData();
	}
}
