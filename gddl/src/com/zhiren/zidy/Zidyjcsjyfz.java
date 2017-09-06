package com.zhiren.zidy;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.*;



public class Zidyjcsjyfz extends BasePage implements PageValidateListener {
	public List gridColumns;
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
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
		Visit visit = (Visit) this.getPage().getVisit();
		getExtGrid().Save(getChange(), visit);
	}


	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private void Return(IRequestCycle cycle){
		cycle.activate("Zidyjcsjy");
	}
	
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}


	public void submit(IRequestCycle cycle) {
		Visit visit = (Visit) this.getPage().getVisit();
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData(visit.getString10());
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			Return(cycle);
		}
	}

	public void getSelectData(String tt) {
		Visit visit = (Visit) getPage().getVisit();
		String sql1 = "";
	
		JDBCcon con = new JDBCcon();
			 sql1=" select id,zidyjcsjy_id,groupname,selectsql,groupbysql,orderbysql,selectsql_jc,colname_cn,colwidth,colbm,GroupDescription"+
                  " from zidyjcsjyfz where zidyjcsjy_id="+tt;		
		ResultSetList rsl = con.getResultSetList(sql1);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("zidyjcsjyfz");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("zidyjcsjy_id").setHidden(true);
		egu.getColumn("zidyjcsjy_id").setDefaultValue(""+tt);
		egu.getColumn("groupname").setHeader("分组名");
		egu.getColumn("groupname").setWidth(60);
		egu.getColumn("selectsql").setHeader("分组列的查询部分");
		egu.getColumn("selectsql").setWidth(120);
		egu.getColumn("groupbysql").setHeader("分组列的分组部分");
		egu.getColumn("groupbysql").setWidth(120);
		egu.getColumn("orderbysql").setHeader("分组列的排序部分");
		egu.getColumn("orderbysql").setWidth(120);
		egu.getColumn("selectsql_jc").setHeader("分组列的字段基础部分");
		egu.getColumn("selectsql_jc").setWidth(120);
		egu.getColumn("colname_cn").setHeader("分组列的中文字段名");
		egu.getColumn("colname_cn").setWidth(120);
		egu.getColumn("colwidth").setHeader("分组列的宽度");
		egu.getColumn("colwidth").setWidth(80);
		egu.getColumn("colbm").setHeader("分组列的别名");
		egu.getColumn("colbm").setWidth(80);
		egu.getColumn("GroupDescription").setHeader("分组描述");
		egu.getColumn("GroupDescription").setWidth(80);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		GridButton btnreturn = new GridButton("返回",
		"function (){document.getElementById('ReturnButton').click()}");
		btnreturn.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(btnreturn);
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
			visit.setDropDownBean10(null);
			visit.setProSelectionModel10(null);
			getSelectData(visit.getString10());
		}
		getSelectData(visit.getString10());
	}

}


