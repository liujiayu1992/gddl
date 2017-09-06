package com.zhiren.pub.chezxx;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Chez extends BasePage implements PageValidateListener {
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
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("select cz.id,\n"
						+ "       cz.xuh,\n"
						+ "       cz.bianm,\n"
						+ "       cz.mingc,\n"
						+ "       cz.quanc,\n"
						+ "       cz.piny,\n"
						+ "       lj.mingc as lujxxb_id,\n"
						+ "       cz.beiz,\n"
						+ "       cz.leib\n"
						+ "  from chezxxb cz, lujxxb lj\n"
						+ " where lj.id(+) = cz.lujxxb_id and cz.leib='车站' order by cz.xuh,cz.quanc");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("chezxxb");
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("mingc").setHeader("简称");
//		egu.getColumn("mingc").setHidden(true);
		
		egu.getColumn("quanc").setHeader("全称");
		egu.getColumn("piny").setHeader("拼音");
		egu.getColumn("lujxxb_id").setHeader("路局");
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("leib").setHeader("类别");
		egu.getColumn("leib").setDefaultValue("车站");
		egu.getColumn("leib").setHidden(true);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.getColumn("leib").setEditor(null);

		ComboBox luj = new ComboBox();
		luj.setEditable(true);
		egu.getColumn("lujxxb_id").setEditor(luj);
		egu.getColumn("lujxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from lujxxb"));
		egu.getColumn("lujxxb_id").setDefaultValue("京局");
		// List l = new ArrayList();
		// l.add(new IDropDownBean(0, "车站"));
		// l.add(new IDropDownBean(1, "港口"));
		// egu.getColumn("leib").setEditor(new ComboBox());
		// egu.getColumn("leib").setComboEditor(egu.gridId, new
		// IDropDownModel(l));
		// egu.getColumn("leib").setReturnId(false);
		// egu.getColumn("leib").setDefaultValue("车站");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String str = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/' + 'Chezreport&lx=rezc';"
				+ " window.open(url,'newWin');";
		egu.addToolbarItem("{"
				+ new GridButton("打印", "function (){" + str + "}",SysConstant.Btn_Icon_Print).getScript()
				+ "}");
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
