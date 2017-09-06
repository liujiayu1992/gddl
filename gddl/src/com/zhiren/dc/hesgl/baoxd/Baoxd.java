package com.zhiren.dc.hesgl.baoxd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Baoxd extends BasePage {

	private static final String REPORTNAME_BAOXD = "Baoxd";

	// ��Դ����ȷҲ�Դ�Ĭ��ȡ��

	private String check = "false";

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	// private String mstrReportName = "";
	public boolean getRaw() {
		return true;
	}

	// �����û���ʾ
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
	}

	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}

	public String getPrintTable() {
		return getHuaybgd();
	}

	// 
	private String getHuaybgd() {

		if (getBianmValue().getValue() == "") {
			return "";
		}
		Report rt = new Report();
		JDBCcon con = new JDBCcon();

		StringBuffer buf = new StringBuffer();

		int colWidth[] = new int[] { 100, 120, 40, 100, 100, 100, 100, 150 };
		String[][] bodyData = new String[17][8];

		buf.append("SELECT GONGYSMC,\n");
		buf.append("       TO_CHAR(JIESRQ,'YYYY-MM-DD') JIESRQ,\n");
		buf.append("       BIANM,\n");
		buf.append("       NVL('',\n");
		buf.append(" TO_CHAR(FAHKSRQ, 'MM.DD') || '-'");
		buf.append(" || TO_CHAR(FAHJZRQ, 'MM.DD') || '��' ||\n");
		buf.append(" ROUND_NEW(JIESSL, 2) || '/' ||");
		buf.append(" CHES || '��;���ձ��:') ZHAIY,\n");
		buf.append("      DECODE(FAZ,'��','��ú','��ú') PINGM,\n");
		buf.append(" NVL('','Q='||JIESRL||'�� S='||JIESLF||'%') REL,\n");
		buf.append("      ROUND_NEW(JIESSL, 2) JIESSL,\n");
		buf.append("      BUHSDJ DANJ,\n");
		buf.append("      MEIKJE,\n");
		buf.append("      SHUIK,\n");
		buf.append("      HANSMK,\n");
		buf.append("      BEIZ\n");
		buf.append("  FROM JIESB\n");
		buf.append(" WHERE BIANM = '" + getBianmValue().getValue() + "'");

		ResultSet rs = con.getResultSet(buf);

		try {
			if (rs.next()) {
				rt.setTitle("�żҿ��ȵ糧ȼ�ϱ�����", colWidth);
				rt.title.setCellValue(3, 1, "������λ:" + rs.getString("GONGYSMC"));
				rt.title.setCellValue(3, 5, rs.getString("JIESRQ"));
				rt.title.mergeCell(3, 1, 3, 3);
				rt.title.setCellValue(3, 7, "���:" + rs.getString("BIANM"));
				rt.title.mergeCell(3, 7, 3, 8);
				rt.title.setCellAlign(3, 7, Table.ALIGN_RIGHT);

				// ********����ֵ***********************************//
				bodyData[0][0] = "ժ&nbsp;&nbsp;Ҫ";
				bodyData[0][1] = rs.getString("ZHAIY");
				bodyData[0][7] = "��&nbsp;&nbsp;ע";
				bodyData[1][0] = "Ʒ&nbsp;&nbsp;��";
				bodyData[1][1] = "��&nbsp;&nbsp;��";
				bodyData[1][2] = "��λ";
				bodyData[1][3] = "��&nbsp;&nbsp;��";
				bodyData[1][5] = "��&nbsp;&nbsp;��";
				bodyData[2][3] = "ƾ֤��";
				bodyData[2][4] = "ʵ����";
				bodyData[2][5] = "����";
				bodyData[2][6] = "�ܼ�";
				bodyData[16][0] = "�ϼ�(��д���)";
				bodyData[3][0] = rs.getString("PINGM");
				bodyData[3][1] = rs.getString("REL");
				bodyData[3][3] = rs.getString("JIESSL");
				bodyData[3][4] = rs.getString("JIESSL");
				bodyData[3][5] = rs.getString("DANJ");
				bodyData[3][6] = rs.getString("MEIKJE");
				bodyData[4][6] = rs.getString("SHUIK");
				bodyData[15][6] = rs.getString("HANSMK");
				bodyData[1][7] = rs.getString("BEIZ");
				bodyData[4][0] = "˰��";
				Money money = new Money();
				bodyData[16][1] = money.NumToRMBStr(rs.getDouble("HANSMK"));

				for (int i = 2; i < 16; i++) {
					bodyData[i][2] = "��";
				}
				// **********����ֵ���************************//
				rt.setBody(new Table(bodyData, 0, 0, 0));
				rt.body.setWidth(colWidth);
				rt.setPageRows(23);

				// ********���úϲ�***********************************//
				rt.body.mergeCell(1, 2, 1, 7);
				rt.body.mergeCell(2, 1, 3, 1);
				rt.body.mergeCell(2, 2, 3, 2);
				rt.body.mergeCell(2, 3, 3, 3);
				rt.body.mergeCell(2, 4, 2, 5);
				rt.body.mergeCell(2, 6, 2, 7);
				rt.body.mergeCell(2, 8, 17, 8);
				rt.body.mergeCell(17, 2, 17, 7);

				// ********������úϲ�*******************************//

				// ************���ö��뷽ʽ****************************//
				rt.body.setColAlign(1, Table.ALIGN_CENTER);
				rt.body.setCellAlign(2, 2, Table.ALIGN_CENTER);
				rt.body.setCellAlign(2, 4, Table.ALIGN_CENTER);
				rt.body.setCellAlign(3, 2, Table.ALIGN_CENTER);
				rt.body.setColAlign(3, Table.ALIGN_CENTER);
				rt.body.setColAlign(4, Table.ALIGN_RIGHT);
				rt.body.setColAlign(5, Table.ALIGN_RIGHT);
				rt.body.setColAlign(6, Table.ALIGN_RIGHT);
				rt.body.setColAlign(7, Table.ALIGN_RIGHT);

				rt.body.setCellAlign(2, 4, Table.ALIGN_CENTER);
				rt.body.setCellAlign(2, 6, Table.ALIGN_CENTER);
				rt.body.setCellAlign(3, 4, Table.ALIGN_CENTER);
				rt.body.setCellAlign(3, 5, Table.ALIGN_CENTER);
				rt.body.setCellAlign(3, 6, Table.ALIGN_CENTER);
				rt.body.setCellAlign(3, 7, Table.ALIGN_CENTER);
				rt.body.setCellAlign(1, 8, Table.ALIGN_CENTER);
				rt.body.setCellAlign(4, 2, Table.ALIGN_CENTER);
				// ************������ö��뷽ʽ****************************//

				rt.createFooter(2, colWidth);
				rt.setDefautlFooter(1, 1, "�������:", Table.ALIGN_LEFT);
				rt.setDefautlFooter(2, 1, "��������", Table.ALIGN_RIGHT);
				rt.setDefautlFooter(6, 1, "���:", Table.ALIGN_LEFT);
				rt.setDefautlFooter(8, 1, "����:", Table.ALIGN_LEFT);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// ����ҳ��
		_CurrentPage = 1;
		// _AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		// rt.body.setRowHeight(25);

		return rt.getAllPagesHtml();

	}

	// ��ť�ļ����¼�
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// ����ť�ύ����
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}

	// �жϵ糧Tree����ѡ�糧ʱ�����ӵ�
	private boolean hasDianc(String id) {
		JDBCcon con = new JDBCcon();
		boolean mingc = false;
		String sql = "select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			mingc = true;
		}
		rsl.close();
		return mingc;
	}

	// -------------------------�糧Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sql = " select d.id,d.mingc from diancxxb d where d.id="
				+ visit.getDiancxxb_id() + " \n";
		sql += " union \n";
		sql += "  select d.id,d.mingc from diancxxb d where d.fuid="
				+ visit.getDiancxxb_id() + " \n";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}

	DefaultTree dc;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc = etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}

	// -------------------------�糧Tree END----------

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	// ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null,
				getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel())
				.getBeanValue(Long.parseLong(getTreeid_dc() == null
						|| "".equals(getTreeid_dc()) ? "-1" : getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);

		tb1.addText(new ToolbarText("�糧:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);

		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("��������:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("Jiesrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("������:"));
		ComboBox shij = new ComboBox();
		shij.setTransform("BianmSelect");
		shij.setWidth(180);
		shij
				.setListeners("select:function(own,rec,index){Ext.getDom('BianmSelect').selectedIndex=index}");
		tb1.addField(shij);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "��ѯ",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);
		tb1.addFill();
		setToolbar(tb1);
	}

	// ������ʹ�õķ���
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		((DateField) getToolbar().getItem("Jiesrq")).setValue(getRiq());
		return getToolbar().getRenderScript();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setString13("");
			setChangbValue(null);
			setChangbModel(null);
			setBianmValue(null);
			setBianmModel(null);
			setTreeid_dc(String.valueOf(visit.getDiancxxb_id()));
			getSelectData();
		}

		if (riqchange) {
			riqchange = false;
			setBianmValue(null);
			setBianmModel(null);
		}

		if (cycle.getRequestContext().getParameters("lx") != null) {

			if (!cycle.getRequestContext().getParameters("lx")[0].equals(this
					.getReportName())) {
				setChangbValue(null);
				setChangbModel(null);
				setBianmValue(null);
				setBianmModel(null);
				this.setRiq(DateUtil.FormatDate(new Date()));
			}
			setReportName(cycle.getRequestContext().getParameters("lx")[0]);
			// System.out.println(this.getReportName()+"----");
		} else {
			if (this.getReportName().equals("")) {
				this.setReportName("");
			}
		}
	}

	private void setReportName(String name) {
		Visit visit = (Visit) getPage().getVisit();

		if (name.equals(REPORTNAME_BAOXD)) {
			visit.setString13(REPORTNAME_BAOXD);
		}

	}

	private String getReportName() {
		Visit visit = (Visit) getPage().getVisit();
		return visit.getString13();
	}

	// �����Ƿ�仯
	private boolean riqchange = false;

	// ������
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		if (this.riq != null) {
			if (!this.riq.equals(riq))
				riqchange = true;
		}
		this.riq = riq;
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public IDropDownBean getBianmValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			if (getBianmModel().getOptionCount() > 0) {
				setBianmValue((IDropDownBean) getBianmModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setBianmValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(value);
	}

	public IPropertySelectionModel getBianmModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setBianmModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setBianmModel(IPropertySelectionModel model) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(model);
	}

	private void setBianmModels() {
		StringBuffer buf = new StringBuffer();

		String s = "";// ����ѡ��Ĳ��Ǹ��糧ʱ���������� ��Ϊ��ѯ���� ������ ���ӵ糧ʱ ȫ����ѯ
		if (!this.hasDianc(this.getTreeid_dc())) {
			s = " and f.diancxxb_id=" + this.getTreeid_dc() + " \n";

		} else {
			s = " and f.diancxxb_id in ( select distinct d.id from diancxxb d start with d.id="
					+ this.getTreeid_dc() + " connect by prior d.id=d.fuid )";
		}
		buf.append("SELECT ID,BIANM FROM JIESB F WHERE JIESRQ = ");
		buf.append(DateUtil.FormatOracleDate(getRiq()));
		buf.append(s);

		setBianmModel(new IDropDownModel(buf.toString(), "��ѡ��"));
	}

	// ����������
	public IDropDownBean getChangbValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			if (getChangbModel().getOptionCount() > 0) {
				setChangbValue((IDropDownBean) getChangbModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setChangbValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(value);
	}

	public IPropertySelectionModel getChangbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setChangbModel();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setChangbModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
	}

	public void setChangbModel() {
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if (visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="
					+ visit.getDiancxxb_id());
		} else {
			sb.append("select id,mingc from diancxxb where id="
					+ visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
}