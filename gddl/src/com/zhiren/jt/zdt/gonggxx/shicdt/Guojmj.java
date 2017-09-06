package com.zhiren.jt.zdt.gonggxx.shicdt;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Guojmj extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO �Զ����ɷ������
		super.initialize();
		setMsg("");
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
	
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
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
	

//�󶨽�������
	private String endRiq;
	public String getEndRiq() {
		return endRiq;
	}
	public void setEndRiq(String riq) {
		this.endRiq = riq;
	}

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String sql="select id,\n"
			+ "       riq,\n"
			+ "       leib,\n"
			+ "       jiag,\n"
			+ "       beiz\n"
			+ " from guojmj\n"
			+ " where riq = " + DateUtil.FormatOracleDate(getEndRiq()) 
			+ " order by riq";
//		System.out.print(sql);
		ResultSetList rsl = con
				.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("guojmj");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		egu.getColumn("riq").setCenterHeader("����");
		egu.getColumn("riq").setWidth(80);
		egu.getColumn("riq").setDefaultValue(getEndRiq());
		
		egu.getColumn("leib").setHeader("NEWC Index�۸�");
		egu.getColumn("leib").setWidth(70);
		egu.getColumn("leib").setDefaultValue("NEWC Index�۸�");
		egu.getColumn("leib").editor=null;
		egu.getColumn("leib").hidden=true;
		egu.getColumn("jiag").setCenterHeader("�۸�(��Ԫ/��)");
		egu.getColumn("jiag").setWidth(100);
		egu.getColumn("jiag").setDefaultValue("0");
		((NumberField)egu.getColumn("jiag").editor).setDecimalPrecision(3);
		egu.getColumn("beiz").setCenterHeader("��ע");
		egu.getColumn("beiz").setWidth(200);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

		egu.addTbarText("����:");
		
		DateField dEnd = new DateField();
		dEnd.Binding("ENDRIQ","");
		dEnd.setValue(getEndRiq());
		egu.addToolbarItem(dEnd.getScript());
		egu.addTbarText("-");
		
		GridButton gRefresh = new GridButton("ˢ��","function(){document.getElementById('RefreshButton').click();}");
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
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
			
			setEndRiq(DateUtil.FormatDate(new Date()));
			getSelectData();
		}
	}
}
