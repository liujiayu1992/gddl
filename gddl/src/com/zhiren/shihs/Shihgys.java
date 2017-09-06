package com.zhiren.shihs;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Shihgys extends BasePage implements PageValidateListener {
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

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("select * from shihgysb  order by mingc");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("shihgysb");
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("mingc").setHeader("名称");
		egu.getColumn("mingc").setWidth(90);
		egu.getColumn("quanc").setHeader("全称");
		egu.getColumn("quanc").setWidth(90);
		egu.getColumn("piny").setHeader("拼音");
		egu.getColumn("piny").setWidth(90);
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("bianm").setWidth(70);
		egu.getColumn("danwdz").setHeader("地址");
		egu.getColumn("danwdz").setWidth(90);
		egu.getColumn("faddbr").setHeader("法人");
		egu.getColumn("faddbr").setWidth(70);
		egu.getColumn("weitdlr").setHeader("委托代理人");
		egu.getColumn("weitdlr").setWidth(70);
		egu.getColumn("kaihyh").setHeader("开户银行");
		egu.getColumn("kaihyh").setWidth(90);
		egu.getColumn("zhangh").setHeader("帐号");
		egu.getColumn("zhangh").setWidth(70);
		egu.getColumn("dianh").setHeader("电话");
		egu.getColumn("dianh").setWidth(70);
		egu.getColumn("shuih").setHeader("税号");
		egu.getColumn("shuih").setWidth(70);
		egu.getColumn("youzbm").setHeader("邮政编码");
		egu.getColumn("youzbm").setWidth(70);
		egu.getColumn("chuanz").setHeader("传真");
		egu.getColumn("chuanz").setWidth(70);
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(90);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置页面是否可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
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
