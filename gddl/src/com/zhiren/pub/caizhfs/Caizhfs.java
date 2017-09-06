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

	// ҳ��仯��¼
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
		// ���ñ��������ڱ���
		egu.setTableName("caizhfsb");
		// ������ʾ������
		egu.getColumn("mingc").setHeader("����");
		// �����п��
		egu.getColumn("mingc").setWidth(200);
		// ���õ�ǰgrid�Ƿ�ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// ���÷�ҳ������ȱʡ25�пɲ��裩
		egu.addPaging(25);
		// ֻ��ѡ�е���
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
//		
		egu.getColumn("diancxxb_id").hidden = true;
		egu.getColumn("diancxxb_id").setDefaultValue(String.valueOf(v.getDiancxxb_id()));

		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addToolbarItem("{"+new GridButton("��ϸ����","function(){" +
				"if(gridDiv_sm.getSelections().length <= 0 || gridDiv_sm.getSelections().length > 1){" +
				"Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ�������ʽ��');" +
				"return;}" +
				"grid1_rcd = gridDiv_sm.getSelections()[0];" +
				"if(grid1_rcd.get('ID') == '0'){" +
				"Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ�������ʽ֮ǰ���ȱ���!');" +
				"return;}" +
				"grid1_history = grid1_rcd.get('ID');" +
				"var Cobj = document.getElementById('CHANGE');" +
				"Cobj.value = grid1_history;" +
				"document.getElementById('ShezButton').click();" +
				"}").getScript()+"}");
		GridButton gbp = new GridButton("��ӡ", "function (){"
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			getSelectData();
		}
	}
}
