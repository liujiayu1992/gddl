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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yunfwh extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
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
				.getResultSetList(
						"select y.id,m.mingc MEIKXXB_ID,yunf,zaf,fazzf,ditf,yunfs,y.beiz,d.mingc diancxxb_id\n" +
						"from yunfwhb y,meikxxb m ,diancxxb d where y.diancxxb_id=d.id(+) and y.meikxxb_id=m.id");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("yunfwhb");
		egu.getColumn("MEIKXXB_ID").setHeader("ú��");
		egu.getColumn("yunf").setHeader("�˷�");
		egu.getColumn("zaf").setHeader("�ӷ�");
		egu.getColumn("fazzf").setHeader("��վ�ӷ�");
		egu.getColumn("ditf").setHeader("�����ӷ�");
		egu.getColumn("yunfs").setHeader("�˷�˰");
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("diancxxb_id").setHeader("�糧");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		ComboBox meikxx = new ComboBox();
		meikxx.setEditable(true);
		egu.getColumn("MEIKXXB_ID").setEditor(meikxx);
		egu.getColumn("MEIKXXB_ID").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from meikxxb"));
		// List l = new ArrayList();
		// l.add(new IDropDownBean(0, "��վ"));
		// l.add(new IDropDownBean(1, "�ۿ�"));
		 egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		 egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new
		 IDropDownModel("select id, mingc from diancxxb"));

		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			getSelectData();
		}
	}
}
