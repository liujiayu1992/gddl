package com.zhiren.gangkjy.jiexdy.diaoygl;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Luncxx extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefreshChick =false;
	
	public void RefreshButton(IRequestCycle cycle){
		_RefreshChick =true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("select * from luncxxb ");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setTableName("luncxxb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("mingc").setHeader(Local.mingc);
		egu.getColumn("mingc").setWidth(100);
		egu.getColumn("dunw").setHeader(Local.dunw);
		egu.getColumn("dunw").setWidth(60);
		egu.getColumn("dunw").setDefaultValue("0");
		egu.getColumn("suosgs").setHeader(Local.suosgs);
		egu.getColumn("suosgs").setWidth(100);
		egu.getColumn("zongc").setHeader(Local.zongc);
		egu.getColumn("zongc").setWidth(60);
		egu.getColumn("zongc").setDefaultValue("0");
		egu.getColumn("shejcs").setHeader(Local.shejcs);
		egu.getColumn("shejcs").setWidth(80);
		egu.getColumn("shejcs").setDefaultValue("0");
		egu.getColumn("xuhnl").setHeader(Local.xuhnl);
		egu.getColumn("xuhnl").setWidth(95);
		egu.getColumn("xuhnl").setDefaultValue("0");
		egu.getColumn("shejhs").setHeader(Local.shejhs);
		egu.getColumn("shejhs").setWidth(120);
		egu.getColumn("shejhs").setDefaultValue("0");
		egu.getColumn("shiynx").setHeader(Local.shiynx);
		egu.getColumn("shiynx").setWidth(70);
		egu.getColumn("shiynx").setDefaultValue("0");
		egu.getColumn("leib").setHeader(Local.leib);
		egu.getColumn("leib").setWidth(50);
		egu.getColumn("leib").setDefaultValue("0");
		egu.getColumn("leib").setHidden(true);
		egu.getColumn("zhuangt").setHeader(Local.zhuangt);
		egu.getColumn("zhuangt").setWidth(50);
		egu.getColumn("zhuangt").setDefaultValue("0");
		egu.getColumn("zhuangt").setHidden(true);
		egu.getColumn("beiz").setHeader(Local.beiz);
		egu.getColumn("beiz").setWidth(80);
		
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, "deletebutton");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
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
			getSelectData();
		}
	}
}
