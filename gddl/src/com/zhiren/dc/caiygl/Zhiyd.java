package com.zhiren.dc.caiygl;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * 
 * @author �������̵糧����������
 * 
 */
public class Zhiyd extends BasePage {
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
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
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

	public boolean getRaw() {
		return true;
	}

	// �ⵥ������
	public IDropDownBean getHengdValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			if (getHengdModel().getOptionCount() > 0) {
				setHengdValue((IDropDownBean) getHengdModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setHengdValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(value);
	}

	public IPropertySelectionModel getHengdModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setHengdModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setHengdModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(value);
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
			setChangbModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setChangbModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
	}

	public void setChangbModels() {
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

	public void setHengdModels() {
		StringBuffer sb = new StringBuffer();
		sb
				.append("select rownum as id, j.*\n"
						+ "  from (select distinct to_char(cp.lursj, 'hh24:mi:ss') lursj\n"
						+ "          from chepb cp, fahb f\n"
						+ "         where f.id = cp.fahb_id\n"
						+ "           and to_char(cp.lursj, 'yyyy-mm-dd') = '"
						+ getRiq() + "'\n" + "         order by lursj) j");
		setHengdModel(new IDropDownModel(sb.toString(), "��ѡ��"));
	}

	public String getPrintTable() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer("");
		sb.setLength(0);

		sb
				.append("select distinct z.zhillsb_id as id,\n"
						+ "       z.bianm as bianm,\n"
						+ "       zz.mingc as mingc,\n"
						+ "       decode((select mingc from zhuanmlb where jib = 2),\n"
						+ "              null,\n"
						+ "              zz.mingc,\n"
						+ "              (select mingc from zhuanmlb where jib = 2)) as mingc1\n"
						+ "  from (select f.zhilb_id\n"
						+ "          from chepb cp, fahb f\n"
						+ "         where f.id = cp.fahb_id\n"
						+ "           and to_char(cp.lursj, 'yyyy-mm-dd hh24:mi:ss') =\n"
						+ "               '" + getRiq() + "' || ' ' || '"
						+ getHengdValue().getValue() + "') j,\n"
						+ "       caiyb c,\n" + "       yangpdhb y,\n"
						+ "       zhuanmb z,\n" + "       zhuanmlb zz\n"
						+ " where j.zhilb_id = c.zhilb_id\n"
						+ "   and y.caiyb_id = c.id\n"
						+ "   and z.zhillsb_id = y.zhilblsb_id\n"
						+ "   and z.zhuanmlb_id = zz.id\n"
						+ "   and zz.jib = 2");

		ResultSet rs = con.getResultSet(sb, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();

		String[][] dataList = null;
		int rowNum = 0;
		String imgname = "";
		String SQL = "SELECT   zhi\n" + "  FROM   xitxxb\n"
				+ " WHERE   mingc = '������ͼƬ����' AND diancxxb_id = "
				+ visit.getDiancxxb_id();
		try {
			ResultSet result = con.getResultSet(SQL);
			while (result.next()) {
				imgname = result.getString("zhi");
			}
			result.close();
			int i = 0;
			rs.last();
			rowNum = rs.getRow() * 9;
			if (rowNum <= 0) {
				return null;
			}
			dataList = new String[rowNum][14];
			rs.beforeFirst();
			while (rs.next()) {
				dataList[i++] = new String[] {
						getImgServletPath(imgname + ".gif"),
						getImgServletPath(imgname + ".gif"), "|",
						getImgServletPath(imgname + ".gif"),
						getImgServletPath(imgname + ".gif"), "|",
						getImgServletPath(imgname + ".gif"),
						getImgServletPath(imgname + ".gif"), "|",
						getImgServletPath(imgname + ".gif"),
						getImgServletPath(imgname + ".gif"), "|",
						getImgServletPath(imgname + ".gif"),
						getImgServletPath(imgname + ".gif") };
				dataList[i++] = new String[] { "ȼ�Ϲ���������", "ȼ�Ϲ���������", "|",
						"ȼ�Ϲ���������", "ȼ�Ϲ���������", "|", "ȼ�Ϲ���������", "ȼ�Ϲ���������",
						"|", "ȼ�Ϲ���������", "ȼ�Ϲ���������", "|", "ȼ�Ϲ���������",
						"ȼ�Ϲ���������" };

				dataList[i++] = new String[] { "ú������", "������", "|", "ú������",
						"������", "|", "ú������", "�����", "|", "ú������", "�����", "|",
						"ú������", "ȫˮ��" };
				dataList[i++] = new String[] { "ú������", rs.getString("bianm"),
						"|", "ú������", rs.getString("bianm"), "|", "ú������",
						rs.getString("bianm"), "|", "ú������",
						rs.getString("bianm"), "|", "ú������",
						rs.getString("bianm") };

				dataList[i++] = new String[] { "������Ա", "", "|", "������Ա", "",
						"|", "������Ա", "", "|", "������Ա", "", "|", "������Ա", "" };
				dataList[i++] = new String[] { "������Ա", "", "|", "������Ա", "",
						"|", "������Ա", "", "|", "������Ա", "", "|", "������Ա", "" };
				dataList[i++] = new String[] { "��������", "", "|", "��������", "",
						"|", "��������", "", "|", "��������", "", "|", "��������", "" };
				dataList[i++] = new String[] { "��Ʒ����", "", "|", "��Ʒ����", "",
						"|", "��Ʒ����", "", "|", "��Ʒ����", "", "|", "��Ʒ����", "" };
				dataList[i++] = new String[] { "----", "----", "|", "----",
						"----", "|", "----", "----", "|", "----", "----", "|",
						"----", "----" };

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		int[] ArrWidth = new int[] { 60, 100, 40, 60, 100, 40, 60, 100, 40, 60,
				100, 40, 60, 100 };

		rt.setBody(new Table(rowNum, 14));
		rt.body.setWidth(ArrWidth);
		for (int i = 0; i < dataList.length; i++) {
			for (int j = 0; j < 14; j++) {
				rt.body.setCellValue(i + 1, j + 1, dataList[i][j]);
			}
		}
		rt.body.setPageRows(45);

		rt.body.setBorder(0, 0, 0, 2);

		for (int i = 1; i <= 14; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		for (int i = 1; i <= 14; i++) {
			if (i % 3 == 0) {
				rt.body.setColCells(i, Table.PER_BORDER_RIGHT, 2);
				rt.body.setColCells(i, Table.PER_BORDER_LEFT, 1);
				rt.body.setColCells(i, Table.PER_BORDER_BOTTOM, 0);
				rt.body.setColCells(i, Table.PER_BORDER_TOP, 0);
			}
		}

		rt.body.setColCells(1, Table.PER_BORDER_LEFT, 2);
		rt.body.setColCells(14, Table.PER_BORDER_RIGHT, 2);
		for (int i = 1; i <= dataList.length; i++) {
			rt.body.setRowCells(i, Table.PER_BORDER_TOP, 0);
			rt.body.setRowCells(i, Table.PER_BORDER_BOTTOM, 0);
			rt.body.setRowCells(i, Table.PER_BORDER_LEFT, 0);
			rt.body.setRowCells(i, Table.PER_BORDER_RIGHT, 0);
			rt.body.setRowCells(i + 1, Table.PER_BORDER_TOP, 0);
			rt.body.setRowCells(i + 1, Table.PER_BORDER_BOTTOM, 2);
			rt.body.setRowCells(i + 1, Table.PER_BORDER_LEFT, 0);
			rt.body.setRowCells(i + 1, Table.PER_BORDER_RIGHT, 0);
			i = i + 8;
		}
		for (int i = 9; i <= dataList.length; i++) {
			rt.body.setRowCells(i, Table.PER_BORDER_RIGHT, 0);
			rt.body.setRowCells(i, Table.PER_BORDER_LEFT, 0);
			rt.body.setRowCells(i, Table.PER_BORDER_TOP, 1);
			rt.body.setRowCells(i, Table.PER_BORDER_BOTTOM, 0);
			i = i + 8;

		}
		for (int i = 1; i <= 14; i++) {
			if (i % 3 == 0) {
				rt.body.setColCells(i, Table.PER_BORDER_BOTTOM, 0);
				rt.body.setColCells(i, Table.PER_BORDER_TOP, 0);
			}
		}

		for (int i = 1; i <= dataList.length; i++) {
			rt.body.mergeRow(i);
			rt.body.mergeRow(i + 1);
			i = i + 8;

		}
		for (int i = 5; i <= dataList.length; i++) {
			for (int j = 1; j < 14; j++) {
				rt.body.mergeCell(i, j, i + 1, j);
				j = j + 2;
			}
			i = i + 8;
		}
		con.Close();
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		rt.body.setFontSize(9);
		rt.body.setRowHeight(23);
		for (int i = 9; i <= dataList.length; i++) {
			rt.body.setRowHeight(i, 34);
			i = i + 8;
		}
		return rt.getAllPagesHtml();

	}

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

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		if (visit.isFencb()) {
			tb1.addText(new ToolbarText("����:"));
			ComboBox changbcb = new ComboBox();
			changbcb.setTransform("ChangbSelect");
			changbcb.setWidth(130);
			changbcb
					.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
			tb1.addField(changbcb);
			tb1.addText(new ToolbarText("-"));
		}
		tb1.addText(new ToolbarText("ʱ��:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("Riq", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("lursj");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		ComboBox hengdcb = new ComboBox();
		hengdcb.setTransform("HengdSelect");
		hengdcb.setWidth(130);
		hengdcb
				.setListeners("select:function(own,rec,index){Ext.getDom('HengdSelect').selectedIndex=index}");
		tb1.addField(hengdcb);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "��ѯ",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText(
				"<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		if (getTbmsg() != null) {
			getToolbar().deleteItem();
			getToolbar().addText(
					new ToolbarText("<marquee width=300 scrollamount=2>"
							+ getTbmsg() + "</marquee>"));
		}
		((DateField) getToolbar().getItem("lursj")).setValue(getRiq());
		return getToolbar().getRenderScript();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setboolean1(false);
			visit.setString1(null);
			setRiq(DateUtil.FormatDate(new Date()));
			setHengdValue(null);
			setHengdModel(null);
			getSelectData();
		}
		if (riqchange) {
			riqchange = false;
			setHengdValue(null);
			setHengdModel(null);
			setTbmsg(null);
		}
	}

	// ����

	public String CreateChartFile(JFreeChart chart, String filebassname) {
		try {
			Visit visit = (Visit) getPage().getVisit();
			String FilePath = MainGlobal.getServletPath(getPage())
					+ "/img/CaiyPicture" + visit.getRenyID();
			File dir = new File(FilePath.replaceAll("\\\\", "/"));
			if (dir.exists()) {
				deleteFile(dir);// ����ļ����ļ��������ڵ��ļ�
				dir.mkdir();
			} else {
				dir.mkdirs();
			}
			File chartFile = new File(dir, filebassname + ".jpg");
			ChartUtilities.saveChartAsJPEG(chartFile, chart, 990, 400);
			return getImgServletPath(chartFile.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	private static void deleteFile(File dir) throws IOException {// ����ļ��м��ļ�������������
		if ((dir == null) || !dir.isDirectory()) {
			throw new IllegalArgumentException("Argument " + dir
					+ " is not a directory. ");
		}
		File[] entries = dir.listFiles();
		int sz = entries.length;
		for (int i = 0; i < sz; i++) {
			if (entries[i].isDirectory()) {
				deleteFile(entries[i]);
			} else {
				entries[i].delete();
			}
		}
		dir.delete();
	}

	public String getImgServletPath(String name) {
		// Visit visit = (Visit) getPage().getVisit();
		String a = "<img width=160 height=40 src='"
				+ MainGlobal.getHomeContext(getPage()) + "/imgs/report" + "/"
				+ name + "' />";
		return a;
	}
}
