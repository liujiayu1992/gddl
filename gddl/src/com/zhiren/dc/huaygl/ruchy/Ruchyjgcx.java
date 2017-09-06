package com.zhiren.dc.huaygl.ruchy;

/*
 * 作者：Qiuzw
 * 时间：2013年4月26日
 * 描述：查询入厂化验数据
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JdbcConSqlServer;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Ruchyjgcx extends BasePage {

	public Date getMonthFirstday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}

	boolean riqchange = false;

	private String riq;

	public String getRiq() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setRiq(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}

	boolean afterchange = false;

	private String after;

	public String getAfter() {
		if (after == null || after.equals("")) {
			after = DateUtil.FormatDate(new Date());
		}
		return after;
	}

	public void setAfter(String after) {

		if (this.after != null && !this.after.equals(after)) {
			this.after = after;
			afterchange = true;
		}

	}

	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	public boolean getRaw() {
		return true;
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

	public static final String REPORT_NAME_RUCHYJGCX = "Ruchyjgcx";// 入厂煤化验结果查询

	private String mstrReportName = "";

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		if (mstrReportName == null || mstrReportName.equals(""))
			mstrReportName = ((Visit) getPage().getVisit()).getString1();
		if (mstrReportName.equals(REPORT_NAME_RUCHYJGCX)) {
			return getRuchyjgcx();
		} else {
			return "无此报表";
		}
	}

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public String ConvertToChinese(String s) {
		String strChinese = "";
		if (s.equalsIgnoreCase("Mt")) {
			strChinese = "全水分";
		}else if(s.equalsIgnoreCase("Mad")) {
			strChinese = "空干基水分" ;
		}else if(s.equalsIgnoreCase("Aad")) {
			strChinese = "空干基灰分" ;
		}else if(s.equalsIgnoreCase("Vad")) {
			strChinese = "空干基挥发分";
		}else if(s.equalsIgnoreCase("St,ad")) {
			strChinese = "空干基硫分" ;
		}else if(s.equalsIgnoreCase("Qbad")) {
			strChinese = "弹筒热量" ;
		}
		return strChinese + "("+s+")";
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	private String getRuchyjgcx() {
		Report rt = new Report();
		JdbcConSqlServer con = new JdbcConSqlServer();
		ResultSet rs;
		String strFirstDate, strLastDate;
		strFirstDate = this.getRiq();
		strLastDate = this.getAfter();
		String Sql = "SELECT COUNT(ID) AS ID FROM (\n"
				+ "SELECT ID FROM LIUFB WHERE CONVERT(VARCHAR(100), SHIY_RQ, 23) BETWEEN '"
				+ strFirstDate
				+ "' AND '"
				+ strLastDate
				+ "'\n"
				+ "UNION ALL\n"
				+ "SELECT ID FROM RELB WHERE CONVERT(VARCHAR(100), SHIY_RQ, 23) BETWEEN '"
				+ strFirstDate
				+ "' AND '"
				+ strLastDate
				+ "'\n"
				+ "UNION ALL\n"
				+ "SELECT ID FROM SHUIFB WHERE CONVERT(VARCHAR(100), SHIY_RQ, 23) BETWEEN '"
				+ strFirstDate
				+ "' AND '"
				+ strLastDate
				+ "'\n"
				+ "UNION ALL\n"
				+ "SELECT ID FROM GONGYFXB WHERE CONVERT(VARCHAR(100), SHIY_RQ, 23) BETWEEN '"
				+ strFirstDate + "' AND '" + strLastDate + "'\n" + ") T";// 默认“全部”
		String sql = "SELECT SHIY_RQ,SHIY_BH,SHIY_SERAIL,SHIY_XM,SHIY_ZHI,REPORTBZ\n"
				+ "FROM (\n"
				+ "SELECT CONVERT(VARCHAR(100), SHIY_RQ, 23) AS SHIY_RQ,SHIY_BH,SHIY_SERAIL,'St,ad' AS SHIY_XM,RTRIM(LTRIM(STR(SHIY_STAD,5,2))) AS SHIY_ZHI,REPORTBZ FROM LIUFB WHERE CONVERT(VARCHAR(100), SHIY_RQ, 23) BETWEEN '"
				+ strFirstDate
				+ "' AND '"
				+ strLastDate
				+ "' \n"
				+ "UNION ALL\n"
				+ "SELECT CONVERT(VARCHAR(100), SHIY_RQ, 23) AS SHIY_RQ,SHIY_BH,SHIY_SERAIL,'Qbad' AS SHIY_XM,RTRIM(LTRIM(STR(SHIY_FRL,5,3))) AS SHIY_ZHI,REPORTBZ FROM RELB WHERE CONVERT(VARCHAR(100), SHIY_RQ, 23) BETWEEN '"
				+ strFirstDate
				+ "' AND '"
				+ strLastDate
				+ "' \n"
				+ "UNION ALL\n"
				+ "SELECT CONVERT(VARCHAR(100), SHIY_RQ, 23) AS SHIY_RQ,SHIY_BH,SHIY_SERAIL,'Mt' AS SHIY_XM,RTRIM(LTRIM(STR(SHIY_ZHI,5,2))) AS SHIY_ZHI,REPORTBZ FROM SHUIFB WHERE CONVERT(VARCHAR(100), SHIY_RQ, 23) BETWEEN '"
				+ strFirstDate
				+ "' AND '"
				+ strLastDate
				+ "' \n"
				+ "UNION ALL\n"
				+ "SELECT CONVERT(VARCHAR(100), SHIY_RQ, 23) AS SHIY_RQ,SHIY_BH,SHIY_SERAIL,SHIY_XM AS HUAYXM,RTRIM(LTRIM(STR(SHIY_ZHI,5,3))) AS SHIY_ZHI,REPORTBZ FROM GONGYFXB WHERE CONVERT(VARCHAR(100), SHIY_RQ, 23) BETWEEN '"
				+ strFirstDate
				+ "' AND '"
				+ strLastDate
				+ "' \n"
				+ ") T\n"
				+ "ORDER BY SHIY_RQ,SHIY_BH,SHIY_XM,SHIY_SERAIL";
		long lngHuayxmId = getHuayxmValue().getId();
		if (lngHuayxmId == 1) { //
			// Mt
			Sql = "SELECT COUNT(ID) AS ID FROM SHUIFB WHERE CONVERT(VARCHAR(100), SHIY_RQ, 23) BETWEEN '"
					+ strFirstDate + "' AND '" + strLastDate + "'\n";
			sql = "SELECT CONVERT(VARCHAR(100), SHIY_RQ, 23) AS SHIY_RQ,SHIY_BH,SHIY_SERAIL,'Mt' AS SHIY_XM,RTRIM(LTRIM(STR(SHIY_ZHI,5,2))) AS SHIY_ZHI ,REPORTBZ FROM SHUIFB WHERE CONVERT(VARCHAR(100), SHIY_RQ, 23) BETWEEN '"
					+ strFirstDate + "' AND '" + strLastDate + "' \n";
		} else if (lngHuayxmId == 2) { //
			// Mad
			Sql = "SELECT COUNT(ID) AS ID FROM GONGYFXB WHERE CONVERT(VARCHAR(100), SHIY_RQ, 23) BETWEEN '"
					+ strFirstDate + "' AND '" + strLastDate + "'";
			sql = "SELECT CONVERT(VARCHAR(100), SHIY_RQ, 23) AS SHIY_RQ,SHIY_BH,SHIY_SERAIL,SHIY_XM,RTRIM(LTRIM(STR(SHIY_ZHI,5,2))) AS SHIY_ZHI,REPORTBZ FROM GONGYFXB WHERE SHIY_XM = 'Mad' AND CONVERT(VARCHAR(100), SHIY_RQ, 23) BETWEEN '"
					+ strFirstDate + "' AND '" + strLastDate + "' \n";
		} else if (lngHuayxmId == 3) { //
			// Vad
			Sql = "SELECT COUNT(ID) AS ID FROM GONGYFXB WHERE CONVERT(VARCHAR(100), SHIY_RQ, 23) BETWEEN '"
					+ strFirstDate + "' AND '" + strLastDate + "'";
			sql = "SELECT CONVERT(VARCHAR(100), SHIY_RQ, 23) AS SHIY_RQ,SHIY_BH,SHIY_SERAIL,SHIY_XM,RTRIM(LTRIM(STR(SHIY_ZHI,5,2))) AS SHIY_ZHI,REPORTBZ FROM GONGYFXB WHERE SHIY_XM = 'Vad' AND CONVERT(VARCHAR(100), SHIY_RQ, 23) BETWEEN '"
					+ strFirstDate + "' AND '" + strLastDate + "' \n";
		} else if (lngHuayxmId == 4) { //
			// St，ad
			Sql = "SELECT COUNT(ID) AS ID FROM LIUFB WHERE CONVERT(VARCHAR(100), SHIY_RQ, 23) BETWEEN '"
					+ strFirstDate + "' AND '" + strLastDate + "'\n";
			sql = "SELECT CONVERT(VARCHAR(100), SHIY_RQ, 23) AS SHIY_RQ,SHIY_BH,SHIY_SERAIL,'St,ad' AS SHIY_XM,RTRIM(LTRIM(STR(SHIY_STAD,5,2))) AS SHIY_ZHI,REPORTBZ FROM LIUFB WHERE CONVERT(VARCHAR(100), SHIY_RQ, 23) BETWEEN '"
					+ strFirstDate + "' AND '" + strLastDate + "' \n";
		} else if (lngHuayxmId == 5) { //
			// Aad
			Sql = "SELECT COUNT(ID) AS ID FROM GONGYFXB WHERE CONVERT(VARCHAR(100), SHIY_RQ, 23) BETWEEN '"
					+ strFirstDate + "' AND '" + strLastDate + "'";
			sql = "SELECT CONVERT(VARCHAR(100), SHIY_RQ, 23) AS SHIY_RQ,SHIY_BH,SHIY_SERAIL,SHIY_XM, RTRIM(LTRIM(STR(SHIY_ZHI,5,2))) AS SHIY_ZHI,REPORTBZ FROM GONGYFXB WHERE SHIY_XM = 'Aad' AND CONVERT(VARCHAR(100), SHIY_RQ, 23) BETWEEN '"
					+ strFirstDate + "' AND '" + strLastDate + "' \n";
		} else if (lngHuayxmId == 6) { //
			// Qbad
			Sql = "SELECT COUNT(ID) AS ID FROM RELB WHERE CONVERT(VARCHAR(100), SHIY_RQ, 23) BETWEEN '"
					+ strFirstDate + "' AND '" + strLastDate + "'\n";
			sql = "SELECT CONVERT(VARCHAR(100), SHIY_RQ, 23) AS SHIY_RQ,SHIY_BH,SHIY_SERAIL,'Qbad' AS SHIY_XM,RTRIM(LTRIM(STR(SHIY_FRL,5,3))) AS SHIY_ZHI,REPORTBZ FROM RELB WHERE CONVERT(VARCHAR(100), SHIY_RQ, 23) BETWEEN '"
					+ strFirstDate + "' AND '" + strLastDate + "' \n";
		}
		rs = con.getResultSet(Sql);
		int intRows = 0;
		try {
			if (rs.next()) {
				intRows = rs.getInt("ID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "无法获得正确的查询结果。";
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs = null;
			con.closeRs();
		}
		String[][] data = new String[intRows][7];
		rs = con.getResultSet(sql);
		int i = 0;
		try {
			while (rs.next()) {
//				System.out.println("i = " + i);
				data[i][0] = i + 1 + "";
				data[i][1] = rs.getString("SHIY_RQ");
				data[i][2] = rs.getString("SHIY_BH");
				data[i][3] = rs.getString("SHIY_SERAIL");
				data[i][4] = ConvertToChinese(rs.getString("SHIY_XM"));
				data[i][5] = rs.getString("SHIY_ZHI");
				data[i][6] = rs.getInt("REPORTBZ") == 1 ? "已上报" : "未上报";
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs = null;
			con.closeRs();
			con.Close();
		}
		if (intRows == 0) {
			return "报表无数据";
		}
		String[][] ArrHeader = null;
		String[] strFormat = null;
		int[] ArrWidth = null;
		int aw = 0;
		// -------------------------------------------------------
		ArrHeader = new String[1][7];

		ArrHeader[0] = new String[] { "序号", "日期", "编号", "自动编号", "化验项目", "化验结果",
				"上报标识" };
		ArrWidth = new int[7];

		ArrWidth = new int[] { 60, 90, 100, 120, 120, 80, 100 };
		aw = rt.paperStyle(
				((Visit) this.getPage().getVisit()).getDiancxxb_id(),
				((Visit) this.getPage().getVisit()).getString4());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
		rt.setTitle("入 厂 煤 化 验 结 果 查 询", ArrWidth);

		strFormat = new String[] { "", "", "", "", "", "", "" };
		// }

		rt.title.setRowHeight(2, 25);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 24);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 5, "化验日期:" + getRiq() + "至" + getAfter(),
				Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(data, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(rt.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedCols();
		rt.body.setColFormat(strFormat);
		for (i = 1; i <= 7; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(21);

		return rt.getAllPagesHtml();

	}

	// -------------------------电厂Tree-----------------------------------------------------------------
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

	// -------------------------电厂Tree END----------

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		Visit visit = (Visit) this.getPage().getVisit();

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

		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);

		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("化验日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq());
		df.Binding("riq", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("至:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getAfter());
		df1.Binding("after", "");// 与html页中的id绑定,并自动刷新
		df1.setId("after");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("化验项目:"));
		ComboBox meik = new ComboBox();
		meik.setTransform("HuayxmSelect");
		meik.setEditable(true);
		meik.setWidth(150);
		// meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(meik);

		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);

		setToolbar(tb1);

	}

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	public String getcontext() {
		return "";
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_pageLink = "";
	}

	// 化验项目下拉框
	private IDropDownBean HuayxmValue;

	public IDropDownBean getHuayxmValue() {
		if (HuayxmValue == null) {
			HuayxmValue = (IDropDownBean) getHuayxmModel().getOption(0);
		}
		return HuayxmValue;
	}

	public void setHuayxmValue(IDropDownBean Value) {
		if (!(HuayxmValue == Value)) {
			HuayxmValue = Value;
		}
	}

	private IPropertySelectionModel HuayxmModel;

	public void setHuayxmModel(IPropertySelectionModel value) {
		HuayxmModel = value;
	}

	public IPropertySelectionModel getHuayxmModel() {
		if (HuayxmModel == null) {
			getHuayxmModels();
		}
		return HuayxmModel;
	}

	public IPropertySelectionModel getHuayxmModels() {
		List lstHuayxms = new ArrayList();
		lstHuayxms.add(new IDropDownBean(0, "全部"));
		lstHuayxms.add(new IDropDownBean(1, "全水分(Mt)"));
		lstHuayxms.add(new IDropDownBean(2, "空干基水分(Mad)"));
		lstHuayxms.add(new IDropDownBean(3, "空干基灰分(Vad)"));
		lstHuayxms.add(new IDropDownBean(4, "空干基硫分(St,ad)"));
		lstHuayxms.add(new IDropDownBean(5, "空干基灰分Aad"));
		lstHuayxms.add(new IDropDownBean(6, "弹筒热量(Qbad)"));
		HuayxmModel = new IDropDownModel(lstHuayxms);
		return HuayxmModel;
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");

			// begin方法里进行初始化设置
			visit.setString4(null);
			visit.setString3(null);
			getSelectData();
		}
		if (cycle.getRequestContext().getParameters("lx") != null) {
			if (!visit.getString1().equals(
					cycle.getRequestContext().getParameters("lx")[0])) {

				visit.setProSelectionModel10(null);
				visit.setDropDownBean10(null);
				visit.setProSelectionModel1(null);
				visit.setDropDownBean1(null);
				visit.setProSelectionModel5(null);
				visit.setDropDownBean5(null);
				visit.setString3(null);
				setTreeid_dc(String.valueOf(visit.getDiancxxb_id()));
			}
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName = visit.getString1();
		} else {
			visit.setString3(null);
			if (visit.getString1().equals("")) {
				mstrReportName = visit.getString1();
			}
		}
		blnIsBegin = true;
		getSelectData();

	}

}
