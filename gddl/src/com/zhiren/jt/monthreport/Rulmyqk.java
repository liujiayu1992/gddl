package com.zhiren.jt.monthreport;

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

public class Rulmyqk extends BasePage implements PageValidateListener {
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
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
		int flag = visit.getExtGrid1().Save(getChange(), visit);
		if (flag!=-1){
			setMsg(ErrorMessage.SaveSuccessMessage);
		}
	}
	
	private void GotoRucmtqk(IRequestCycle cycle){
		cycle.activate("Rucmtqk");
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ReturnButton = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnButton = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData(null);
		}
		
		if (_ReturnButton) {
			_ReturnButton = false;
			GotoRucmtqk(cycle);
		}
	}
	

	public void getSelectData(ResultSetList rsl) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// 工具栏的年份和月份下拉框
		// -----------------------------------
		
		String sql = 

			"select r.id,\n" +
			"       r.diancxxb_id,\n" + 
			"       r.riq,\n" + 
			"       r.fenx,\n" + 
			"       r.rez,\n" + 
			"       r.laiy,\n" + 
			"       r.haoy,\n" + 
			"       r.youkc\n" + 
			"from rucmtqkzb r\n" + 
			"where r.diancxxb_id = "+visit.getString3()+"\n" + 
			"      and r.riq = to_date('"+visit.getString4()+"','yyyy-MM-dd')\n" + 
			"order by decode(r.fenx,'当月',1,'本期',2,'同期',3,'差值',4,5)";

		rsl = con.getResultSetList(sql);	
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("rucmtqkzb");
		egu.setWidth("bodyWidth");
		egu.getColumn("diancxxb_id").setHeader("电厂名称");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("fenx").setHeader("分项");
		egu.getColumn("fenx").setEditor(null);
		egu.getColumn("rez").setHeader("入炉热值");
		egu.getColumn("laiy").setHeader("来油");
		egu.getColumn("haoy").setHeader("耗油");
		egu.getColumn("youkc").setHeader("油库存");

		// 设定列初始宽度
//		egu.getColumn("riq").setWidth(80);
//		egu.getColumn("rez").setWidth(100);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(30);// 设置分页

		// *****************************************设置默认值****************************
		
//		// 设置日期的默认值,
//		egu.getColumn("riq").setDefaultValue(intyear + "-" + StrMonth + "-01");

		// ********************工具栏************************************************
		GridButton ght = new GridButton(GridButton.ButtonType_Save, egu.gridId,
				egu.getGridColumns(), "SaveButton");
				ght.setId("SAVE");
				egu.addTbarBtn(ght);
				
		StringBuffer ret = new StringBuffer();
		ret.append("function(){").append("document.getElementById('ReturnButton').click();}");
		GridButton fh = new GridButton("返回", ret.toString());
		fh.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(fh);
		
//		 ---------------页面js的计算开始------------------------------------------
		
		
		
		String sb_str=
			"gridDiv_grid.on('beforeedit',function(e){" +
			" 	if(e.row==3){ e.cancel=true;}" +//第4行差值行不允许编辑
			"});" +
		
			"gridDiv_grid.on('afteredit',function(e){\n" +
			"   if(e.row==1||e.row==2){\n" +
			"		var rec1=gridDiv_ds.getAt(1);\n" +
			"		var rec2=gridDiv_ds.getAt(2);\n" +
			"		var rec3=gridDiv_ds.getAt(3);\n" +
			"		rec3.set(e.field,rec1.get(e.field)-rec2.get(e.field));\n" +
			"	}\n" +
			"});\n";

		StringBuffer sb = new StringBuffer(sb_str);
		
		egu.addOtherScript(sb.toString());
		// ---------------页面js计算结束--------------------------
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
		}
		getSelectData(null);

	}


//	// 得到登陆用户所在电厂或者分公司的名称
//	public String getDiancmc() {
//		String diancmc = "";
//		JDBCcon cn = new JDBCcon();
//		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
//		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
//				+ diancid;
//		ResultSet rs = cn.getResultSet(sql_diancmc);
//		try {
//			while (rs.next()) {
//				diancmc = rs.getString("quanc");
//			}
//			rs.close();
//		} catch (SQLException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		} finally {
//			cn.Close();
//		}
//
//		return diancmc;
//
//	}
//
//	// 得到电厂树下拉框的电厂名称或者分公司,集团的名称
//	public String getIDropDownDiancmc(String diancmcId) {
//		if (diancmcId == null || diancmcId.equals("")) {
//			diancmcId = "1";
//		}
//		String IDropDownDiancmc = "";
//		JDBCcon cn = new JDBCcon();
//
//		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
//				+ diancmcId;
//		ResultSet rs = cn.getResultSet(sql_diancmc);
//		try {
//			while (rs.next()) {
//				IDropDownDiancmc = rs.getString("mingc");
//			}
//			rs.close();
//		} catch (SQLException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		} finally {
//			cn.Close();
//		}
//
//		return IDropDownDiancmc;
//
//	}
//
//	private String treeid;
//
//	/*
//	 * public String getTreeid() { return treeid; } public void setTreeid(String
//	 * treeid) { this.treeid = treeid; }
//	 */
//	public String getTreeid() {
//		String treeid = ((Visit) getPage().getVisit()).getString2();
//		if (treeid == null || treeid.equals("")) {
//			((Visit) getPage().getVisit()).setString2(String
//					.valueOf(((Visit) this.getPage().getVisit())
//							.getDiancxxb_id()));
//		}
//		return ((Visit) getPage().getVisit()).getString2();
//	}
//	public boolean dctree = false;
//	public void setTreeid(String treeid) {
//		if(!((Visit) getPage().getVisit()).getString2().equals(treeid)){
//			dctree = true;
//		}
//		((Visit) getPage().getVisit()).setString2(treeid);
//	}
//
//	public ExtTreeUtil getTree() {
//		return ((Visit) this.getPage().getVisit()).getExtTree1();
//	}
//
//	public void setTree(ExtTreeUtil etu) {
//		((Visit) this.getPage().getVisit()).setExtTree1(etu);
//	}
//
//	public String getTreeHtml() {
//		return getTree().getWindowTreeHtml(this);
//	}
//
//	public String getTreeScript() {
//		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
//	}
//	
//	public String getTreedcScript() {
//		return getTree().getWindowTreeScript();
//	}
//
//	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
//	public int getDiancTreeJib() {
//		JDBCcon con = new JDBCcon();
//		int jib = -1;
//		String DiancTreeJib = this.getTreeid();
//		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
//			DiancTreeJib = "0";
//		}
//		String sqlJib = "select d.jib from diancxxb d where d.id="
//				+ DiancTreeJib;
//		ResultSet rs = con.getResultSet(sqlJib.toString());
//
//		try {
//			while (rs.next()) {
//				jib = rs.getInt("jib");
//			}
//			rs.close();
//		} catch (SQLException e) {
//
//			e.printStackTrace();
//		} finally {
//			con.Close();
//		}
//
//		return jib;
//	}
//	
}
