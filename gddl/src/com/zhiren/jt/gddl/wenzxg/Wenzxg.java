package com.zhiren.jt.gddl.wenzxg;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

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
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Wenzxg extends BasePage implements PageValidateListener{
	private String msg;        //提示信息
	public void setMsg(String msg){
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	public String getMsg(){
		return msg;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
/*	
	private boolean EditChick = false;  //保存按钮触发情况
	
	public void EditButton(IRequestCycle cycle){
		EditChick = true;
	}*/
	
	private boolean RefurbishChick = false;   //刷新按钮触发情况
	public void RefurbishButton(IRequestCycle cycle){
		RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle){
		if(RefurbishChick){
			RefurbishChick = false;
			getSelectData();
		}
	}
	
	public void edit(){
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = getExtGrid().getModifyResultSet("");
		StringBuffer sql = new StringBuffer();
		String id = "";
		String riq = null;
		String diancxxb_id = "";
		String wenz = "";
		String lursj = null;
		String lurry = "";
		while(rsl.next()){
			sql.delete(0,sql.length());
			id = rsl.getString("id");
			riq = rsl.getString("riq");
			diancxxb_id = rsl.getString("diancxxb_id");
			wenz = rsl.getString("wenz");
			lursj = rsl.getString("lursj");
			lurry = rsl.getString("lurry");
			sql.append("update wenzxxb wb set wenz = '"+wenz+"'\n" +
					"where id = "+id);
			con.getUpdate(sql.toString());
			con.Close();
		}
	}
	
	public void getSelectData(){
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(getSql());
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("diancmc").setHeader("单位");
		egu.getColumn("diancmc").setWidth(200);
		egu.getColumn("diancmc").setEditor(null);
		egu.getColumn("diancmc").setFixed(true);
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setWidth(100);
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("riq").setFixed(true);
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("wenz").setHeader("内容");
		egu.getColumn("wenz").setWidth(500);
		egu.getColumn("wenz").setEditor(null);
		egu.getColumn("wenz").setFixed(true);
		
//		 电厂树
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarText("-");// 设置分隔符
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符
		egu.addTbarText("起始日期");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "forms[0]");// 与html页中的id绑定,并自动刷新
		df.setId("riq1");
		egu.addToolbarItem(df.getScript());

		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "forms[0]");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
//		编辑按钮
		egu.addTbarText("-");
		StringBuffer editHandler = new StringBuffer();
		editHandler.append("function(){" +
				"if(gridDiv_sm.getSelected() == null){" +
				"  Ext.MessageBox.alert('提示信息','请选中需要编辑的行！');" +
				"  return;" +
				"}" +
				"var grid_rcd = gridDiv_sm.getSelected();" +
				"grid_history = grid_rcd.get('ID');" +
				"var url = 'http://'+document.location.host+document.location.pathname;var end = url.indexOf(';');" +
				"url = url.substring(0,end);" +
				"var win = window.open(url+'?service=page/Wenznrxg&id='+grid_history,'ddd','toolbar=0,resizable=0,status=1,width=600,height=430,scrollbar=1,left='+(window.screen.width-600)/2+',top='+(window.screen.height-430)/2);}");
		egu.addTbarBtn(new GridButton("编辑",editHandler.toString()));
		egu.addPaging(25);     //限制每页显示记录的条数
		setExtGrid(egu);
		con.Close();
	}
	
	public String getSql(){
		Visit visit = (Visit)getPage().getVisit();
		StringBuffer sql = new StringBuffer();
		sql.append("select wb.id,to_char(wb.riq,'yyyy-mm') riq,wb.diancxxb_id,dc.mingc diancmc,wb.wenz from wenzxxb wb,diancxxb dc\n");
		if(visit.getDiancxxb_id()==112){
			sql.append("where wb.riq>=to_date('"+this.getRiq1()+"','yyyy-mm-dd')\n" +
					 " and wb.riq <= to_date('"+this.getRiq2()+"','yyyy-mm-dd')\n" +
			 		 "and wb.diancxxb_id = "+getTreeid()+"\n");
		}else{
			sql.append("where wb.riq>=to_date('"+this.getRiq1()+"','yyyy-mm-dd')\n" +
					   " and wb.riq <= to_date('"+this.getRiq2()+"','yyyy-mm-dd')\n" +
							"and wb.diancxxb_id = "+visit.getDiancxxb_id()+"\n");
		}
		sql.append("and dc.id = wb.diancxxb_id\n");
		return sql.toString();
	}
	
/*	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}*/
	
	//页面Pane的内容
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	//装饰后的Pane的javascript
	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}

//	得到电厂树下拉框的电厂名称或者分公司,集团的名称
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
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}
	
	private String treeid;         //定义电厂树
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
	
//	 绑定日期
	public String getRiq1() {
		if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRiq1(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString5() != null && !((Visit) this.getPage().getVisit()).getString5().equals(riq1)) {
			
			((Visit) this.getPage().getVisit()).setString5(riq1);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}
	}
	

	public String getRiq2() {
		if (((Visit) this.getPage().getVisit()).getString6() == null || ((Visit) this.getPage().getVisit()).getString6().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString6(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString6();
	}

	public void setRiq2(String riq2) {

		if (((Visit) this.getPage().getVisit()).getString6() != null && !((Visit) this.getPage().getVisit()).getString6().equals(riq2)) {
			
			((Visit) this.getPage().getVisit()).setString6(riq2);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}

	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString2("");
			this.setTreeid(null);
		}
		getSelectData();
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
}
