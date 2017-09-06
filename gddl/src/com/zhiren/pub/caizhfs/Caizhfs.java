package com.zhiren.pub.caizhfs;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Caizhfs extends BasePage implements PageValidateListener {
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
		getExtGrid().Save(getChange(), (Visit) this.getPage().getVisit());
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private void Shez(IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(getChange());
		cycle.activate("Caizhdy");
	}

	private boolean _ShezChick = false;

	public void ShezButton(IRequestCycle cycle) {
		_ShezChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_ShezChick) {
			_ShezChick = false;
			Shez(cycle);
		}
	}

	public void getSelectData() {
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("select id,diancxxb_id,mingc from caizhfsb where diancxxb_id="
						+ v.getDiancxxb_id() + " order by id");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// 设置表名称用于保存
		egu.setTableName("caizhfsb");
		// 设置显示列名称
		egu.getColumn("mingc").setHeader("名称");
		// 设置列宽度
		egu.getColumn("mingc").setWidth(200);
		// 设置当前grid是否可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// 设置分页行数（缺省25行可不设）
		egu.addPaging(25);
		// 只能选中单行
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
//		
		egu.getColumn("diancxxb_id").hidden = true;
		egu.getColumn("diancxxb_id").setDefaultValue(String.valueOf(v.getDiancxxb_id()));

		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addToolbarItem("{"+new GridButton("详细设置","function(){" +
				"if(gridDiv_sm.getSelections().length <= 0 || gridDiv_sm.getSelections().length > 1){" +
				"Ext.MessageBox.alert('提示信息','请选择采样方式！');" +
				"return;}" +
				"grid1_rcd = gridDiv_sm.getSelections()[0];" +
				"if(grid1_rcd.get('ID') == '0'){" +
				"Ext.MessageBox.alert('提示信息','在选择采样方式之前请先保存!');" +
				"return;}" +
				"grid1_history = grid1_rcd.get('ID');" +
				"var Cobj = document.getElementById('CHANGE');" +
				"Cobj.value = grid1_history;" +
				"document.getElementById('ShezButton').click();" +
				"}").getScript()+"}");
		GridButton gbp = new GridButton("打印", "function (){"
				+ MainGlobal.getOpenWinScript("Caizhfsreport") + "}");
		gbp.setIcon(SysConstant.Btn_Icon_Print);
		egu.addTbarBtn(gbp);
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
