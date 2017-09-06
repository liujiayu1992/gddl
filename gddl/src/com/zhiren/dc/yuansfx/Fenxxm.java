package com.zhiren.dc.yuansfx;

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
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Fenxxm extends BasePage implements PageValidateListener {
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
				.getResultSetList("select id,xuh,mingc,leib from fenxxmb");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("fenxxmb");
		// /设置显示列名称
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("mingc").setHeader("分析项目名称");
		egu.getColumn("leib").setHeader("类别");
		// // //设置列宽度
		// egu.getColumn("mingc").setWidth(70);
		// // //设置当前列是否编辑
		// egu.getColumn("piny").setEditor(null);
		// /设置当前grid是否可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// //设置分页行数（缺省25行可不设）
		egu.addPaging(25);
		// /动态下拉框
		// egu.getColumn("lujxxb_id").setEditor(new ComboBox());
		// egu.getColumn("lujxxb_id").setComboEditor(egu.gridId,
		// new IDropDownModel("select id, mingc from lujxxb"));
		// // /静态下拉框
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "灰熔点"));
		l.add(new IDropDownBean(1, "元素分析"));
		egu.getColumn("leib").setEditor(new ComboBox());
		egu.getColumn("leib").setComboEditor(egu.gridId, new IDropDownModel(l));
		// /是否返回下拉框的值或ID
		egu.getColumn("leib").setReturnId(false);
		// /设置下拉框默认值
		egu.getColumn("leib").setDefaultValue("元素分析");
		// /设置按钮
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String str = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/' + 'Fenxxmreport&lx=rezc';"
				+ " window.open(url,'newWin');";
		egu.addToolbarItem("{"
				+ new GridButton("打印", "function (){" + str + "}").getScript()
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
