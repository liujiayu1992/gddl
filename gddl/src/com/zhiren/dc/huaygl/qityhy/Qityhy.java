package com.zhiren.dc.huaygl.qityhy;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Qityhy extends BasePage implements PageValidateListener {
	private static final String customKey = "Qityhy";
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
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String riqTiaoj = this.getRiqi();
		if (riqTiaoj == null || riqTiaoj.equals("")) {
			riqTiaoj = DateUtil.FormatDate(new Date());

		}
		String chaxun = "select id,huaybh,zhilb_id,huaysj,qnet_ar,"
				+ "aar,ad,vdaf,mt,stad," 
				+ "aad,mad,qbad,had,vad,"
				+ "fcad,std,qbrad,hdaf," 
				+ "qgrad_daf,sdaf,var,huayy,"
				+ "lury,shenhzt,banz," 
				+ "huaylb,beiz"
				+ " from zhillsb "
				+ " where huaysj >= to_date('" + getRiqi() + "','yyyy-mm-dd') "
				+ " and huaysj <= to_date('" + getRiqi1() + "','yyyy-mm-dd') "
				+ " and huaylb ='��ʱ��'";
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl,customKey);
		egu.setTableName("zhillsb");
		egu.getColumn("zhilb_id").setHeader("��������");
		egu.getColumn("zhilb_id").setDefaultValue("0");
		egu.getColumn("zhilb_id").setHidden(true);
		egu.getColumn("huaybh").setHeader("������");
		egu.getColumn("huaysj").setHeader("����ʱ��");
		egu.getColumn("huaysj").setDefaultValue(riqTiaoj);
		egu.getColumn("qnet_ar").setHeader("Qnet,ar(Mj/kg)");
		egu.getColumn("aar").setHeader("Aar(%)");
		egu.getColumn("ad").setHeader("Ad(%)");
		egu.getColumn("vdaf").setHeader("Vdaf(%)");
		egu.getColumn("mt").setHeader("Mt(%)");
		egu.getColumn("stad").setHeader("St,ad(%)");
		egu.getColumn("aad").setHeader("Aad(%)");
		egu.getColumn("mad").setHeader("Mad(%)");
		egu.getColumn("qbad").setHeader("Qb,ad(Mj/kg)");
		egu.getColumn("had").setHeader("Had(%)");
		egu.getColumn("vad").setHeader("Vad(%)");
		egu.getColumn("fcad").setHeader("FCad(%)");
		egu.getColumn("std").setHeader("St,d(%)");
		egu.getColumn("qbrad").setHeader("Qbr,ad(Mj/kg)");
		egu.getColumn("qgrad_daf").setHeader("Qgr,ad(Mj/kg)");
		egu.getColumn("hdaf").setHeader("Hdaf(%)");
		egu.getColumn("sdaf").setHeader("Sdaf(%)");
		egu.getColumn("var").setHeader("Var(%)");
		egu.getColumn("huayy").setHeader("����Ա");
		egu.getColumn("lury").setHeader("¼��Ա");
		egu.getColumn("lury").setHidden(true);
		egu.getColumn("shenhzt").setHeader("״̬");
		egu.getColumn("shenhzt").setHidden(true);
		egu.getColumn("shenhzt").setEditor(null);
		egu.getColumn("banz").setHeader("����");
		egu.getColumn("banz").setHidden(true);
		egu.getColumn("huaylb").setHeader("�������");
		egu.getColumn("huaylb").setHidden(true);
		egu.getColumn("huaylb").setEditor(null);
		egu.getColumn("huaylb").setDefaultValue("��ʱ��");
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("qnet_ar").setWidth(60);
		egu.getColumn("aar").setWidth(60);
		egu.getColumn("ad").setWidth(60);
		egu.getColumn("vdaf").setWidth(60);
		egu.getColumn("mt").setWidth(60);
		egu.getColumn("stad").setWidth(60);
		egu.getColumn("aad").setWidth(60);
		egu.getColumn("mad").setWidth(60);
		egu.getColumn("qbad").setWidth(60);
		egu.getColumn("had").setWidth(60);
		egu.getColumn("vad").setWidth(60);
		egu.getColumn("fcad").setWidth(60);
		egu.getColumn("std").setWidth(60);
		egu.getColumn("qgrad_daf").setWidth(60);
		egu.getColumn("hdaf").setWidth(60);
		egu.getColumn("sdaf").setWidth(60);
		egu.getColumn("var").setWidth(60);
		egu.getColumn("huayy").setWidth(60);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(25);// ���÷�ҳ
		egu.setWidth(1000);// ����ҳ��Ŀ��,������������ʱ��ʾ������
		egu.getColumn("lury").setDefaultValue(visit.getRenymc());
		egu.getColumn("shenhzt").setDefaultValue("0");
		egu.getColumn("");
		// ������
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df.getScript());

		egu.addTbarText("��");
		DateField df1 = new DateField();
		df1.setValue(this.getRiqi1());
		df1.Binding("RIQI1", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df1.getScript());

		// ************************************************************
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
		}
		getSelectData();

	}

	// ���ڿؼ�
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}

	boolean riqichange1 = false;

	private String riqi1;

	public String getRiqi1() {
		if (riqi1 == null || riqi1.equals("")) {
			riqi1 = DateUtil.FormatDate(new Date());
		}
		return riqi1;
	}

	public void setRiqi1(String riqi) {

		if (this.riqi1 != null && !this.riqi1.equals(riqi)) {
			this.riqi1 = riqi;
			riqichange1 = true;
		}

	}
}
