package com.zhiren.dc.gdxw.zhiyxxwh;

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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.DateUtil;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*作者:王总兵
 *日期:2010-6-18 14:08:08
 *描述:增加制样人默认登录人就为制样人,增加多行替换功能.
 * 
 * 
 */
public class Zhiyxxwh extends BasePage implements PageValidateListener{	
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
	
//	刷新数据日期绑定
	private String riq1;
	public void setRiq1(String riq1) {
		this.riq1 = riq1;
	}
	public String getRiq1() {
		return riq1;
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
	
	private boolean _ShowChick = false;
	public void ShowButton(IRequestCycle cycle) {
		_ShowChick = true;
	}
	

	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			
		}else if (_SaveChick) {
			_SaveChick = false;
			Save();
			
		}
		if (_ShowChick) {
			_ShowChick = false;
			DeleteRq();
			
		}
		getSelectData();
	}
	
	
	private void DeleteRq() {
		
		Visit visit=(Visit)this.getPage().getVisit();
		if(getChange()==null || "".equals(getChange())) {
			setMsg("请选择数据进行删除！");
			return;
		}
		int flag=0;
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Zhiyxxwh.Delete 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("begin\n");
		while(rsl.next()) {
			sbsql.append("delete zhiyryb where zhillsb_id= ").append(rsl.getString("zhillsb_id")).append(";\n");
		}	
		
		sbsql.append("end;");
		
		
		flag=con.getUpdate(sbsql.toString());
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail + "SQL:" + sbsql.toString());
			setMsg("保存过程出现错误！保存未成功！");
			con.rollBack();
			con.Close();
			return;
		}
		
		con.Close();
		this.setMsg("删除成功!");
		
	}
	public void Save() {

		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long lngId = 0;
		
		if(getChange( )==null || "".equals(getChange())) {
			setTbmsg("没有做出任何更改！");
			return;
		}
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		// 保存和更新
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Fahhtwh.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			if (rsl.getString("id").equals("0")) {
				//lngId = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
				sbsql.append("insert into zhiyryb(id,zhilb_id,renyxxb_id,zhiyrq,zhiybh,beiz,zhillsb_id) values(268||xl_xul_id.nextval," 
					+rsl.getString("zhilb_id")+","
					+ getExtGrid().getColumn("renyxxb_id").combo.getBeanId(rsl.getString("renyxxb_id")) + ","
					+ DateUtil.FormatOracleDate(rsl.getString("zhiyrq")) + ","
					+"'"+rsl.getString("bianm")+"',"
					+"'"+rsl.getString("beiz")+"'"
					+","+rsl.getString("zhillsb_id")+""
					+	");\n");
				
			}else {
				sbsql.append( "update zhiyryb set zhiyrq=" + DateUtil.FormatOracleDate(rsl.getString("zhiyrq"))
						+ ",renyxxb_id=" + getExtGrid().getColumn("renyxxb_id").combo.getBeanId(rsl.getString("renyxxb_id"))
						+ ",beiz='" + rsl.getString("beiz")
						+ "' where id=" + rsl.getString("id")+";\n");
				
			}
		}
		
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		con.Close();
		setTbmsg("保存成功");
	
		
		
		
	}
	
	public void getSelectData() {
		

		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String riq=this.getRiq();
		if (riq==null){
			riq=DateUtil.FormatDate(new Date());
		}
		String riq1=this.getRiq1();
		if(riq1==null){
			riq1=DateUtil.FormatDate(new Date());
		}
		String denglurenyuan=visit.getRenymc();//登陆人员姓名
		String sSql=


		"select decode(ry.id, null, 0, ry.id) as id,\n" +
		"       cy.mingc,\n" + 
		//"       gd.jingz,\n" + 
		"       zm.bianm,\n" + 
		"       ls.zhilb_id,\n" + 
		"       zm.zhillsb_id,\n" + 
		"       nvl(ren.quanc,'"+denglurenyuan+"') as renyxxb_id,\n" + 
		"       ry.zhiyrq,\n" + 
		"       ry.beiz\n" + 
		" from zhuanmb zm,zhillsb ls,zhilb zl,caiyb cai,\n" + 
		" cunywzb cy,gdxw_cy gd,zhiyryb ry,renyxxb ren\n" + 
		"where zm.zhillsb_id=ls.id\n" + 
		"and ls.zhilb_id=zl.id(+)\n" + 
		"and ls.zhilb_id=cai.zhilb_id\n" + 
		"and cai.cunywzb_id=cy.id\n" + 
		"and ls.zhilb_id=gd.zhilb_id\n" + 
		"and zm.zhuanmlb_id=100663\n" + 
		"and ls.id=ry.zhillsb_id(+)\n" + 
		"and ry.renyxxb_id=ren.id(+)\n" + 
		"and ls.id in (\n" + 
		"    select ls.id from zhillsb ls where ls.zhilb_id in (\n" + 
		"         select c.zhilb_id from gdxw_cy c\n" + 
		"         where  c.zhuangt = 1\n" + 
		"        and c.zhiyrq >= to_date('"+riq+"', 'yyyy-mm-dd')\n" + 
		"        and c.zhiyrq < to_date('"+riq1+"', 'yyyy-mm-dd') + 1\n" + 
		")\n" + 
		") order by cy.xuh";


		ResultSetList rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
//		设置多选框
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		
		egu.addPaging(0);
		egu.setTableName("zhiyryb");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("mingc").setCenterHeader("桶号");
		egu.getColumn("mingc").setEditor(null);
		
//		egu.getColumn("jingz").setHeader("煤量");
//		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("zhilb_id").setHidden(true);
		egu.getColumn("zhilb_id").setEditor(null);
		egu.getColumn("zhillsb_id").setHidden(true);
		egu.getColumn("zhillsb_id").setEditor(null);
		egu.getColumn("bianm").setHeader("制样编号");
		egu.getColumn("bianm").setEditor(null);
		egu.getColumn("zhiyrq").setHeader("制样日期");
		egu.getColumn("renyxxb_id").setHeader("制样员");
		egu.getColumn("beiz").setHeader("备注");
		
		
		
		
		String sTSql = "";
//		发货单位
		ComboBox combFahdw= new ComboBox();
		egu.getColumn("renyxxb_id").setEditor(combFahdw);
		combFahdw.setEditable(false);
		sTSql = "select r.id,r.quanc from renyxxb r where r.bum='制样员' order by r.quanc";
		egu.getColumn("renyxxb_id").setComboEditor(egu.gridId, new IDropDownModel(sTSql));

		
		egu.addTbarText("来煤日期:");
		DateField dfRIQ = new DateField();
		dfRIQ.Binding("RIQ", "");
		dfRIQ.setValue(getRiq());
		egu.addToolbarItem(dfRIQ.getScript());
		egu.addTbarText("-");
		
		egu.addTbarText("至:");
		DateField Endriq = new DateField();
		Endriq.Binding("RIQ1", "");
		Endriq.setValue(getRiq1());
		egu.addToolbarItem(Endriq.getScript());
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
		egu.addTbarText("-");
		egu.addTbarText("-");
		egu.addTbarText("-");
		egu.addToolbarButton("删除",GridButton.ButtonType_Sel, "ShowButton", null, SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarText("-");
		egu.addTbarText("-");
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		

		
//		多行替换
		egu.addTbarText("-");
		egu.addTbarText("-");
		egu.addTbarText("-");
		Checkbox cbselectlike=new Checkbox();
		cbselectlike.setId("SelectLike");
		egu.addToolbarItem(cbselectlike.getScript());
		egu.addTbarText("多行替换");
		egu.addOtherScript(" gridDiv_grid.on('afteredit',function(e){\n" +
				
				" if(SelectLike.checked) { \n" +
				
				"for(var i=e.row;i<gridDiv_ds.getCount();i++){\n" +
				"var rec=gridDiv_ds.getAt(i);\n" +
				" rec.set(e.field+'',e.value);\n" +
				"}\n" +
				
				"}\n" +
				
				"" +
				"  }); ");
		
		
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
		setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
		setRiq1(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
		getSelectData();
	}
}
