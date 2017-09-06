package com.zhiren.dc.kaoh.caiyycyjgfx_hz;

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
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import org.apache.tapestry.IPage;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;

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
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * ���ߣ�������
 * ʱ�䣺2010-01-21 
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
public class Caiyycyjgfx_hz extends BasePage {

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
		// TODO �Զ����ɷ������
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

	// ������
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}

	// ������
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
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

	// ��ȡ��Ӧ��
	public IPropertySelectionModel getGongysDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getGongysDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public void getGongysDropDownModels() {
		String sql = "select id,mingc from vwgongysmk where diancxxb_id = "
				+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		setGongysDropDownModel(new IDropDownModel(sql, "ȫ��"));
		return;
	}

	// ˢ�ºⵥ�б�
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
		tb1.addText(new ToolbarText("��������:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setId("guohrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" �� "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setId("guohrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));

		DefaultTree dt = new DefaultTree(DefaultTree.tree_gongys_win,
				"gongysTree", "" + visit.getDiancxxb_id(), "forms[0]", null,
				getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("gongysTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getGongysDropDownModel())
				.getBeanValue(Long.parseLong(getTreeid() == null
						|| "".equals(getTreeid()) ? "-1" : getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

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
	
	private String getGongysSql() {
		Visit visit = (Visit) this.getPage().getVisit();
		int level = visit.getDefaultTree().getTree().getTreeRootNode().getNodeById(getTreeid()).getLevel();
		String ss = "";
		switch (level) {
			case 1:break;
			case 2:ss=" and f.gongysb_id ="+getTreeid();break;
			case 3:ss=" and f.meikxxb_id ="+getTreeid();break;
			default :break;
		}
		return ss;
	}

	private String REPORT_NAME_CAIYYCYJGFX_HZ = "Caiyycyjgfx_hz";// ��������

	private String REPORT_NAME_CAIYYCYJGFX_MX = "Caiyycyjgfx_mx";// ��������

	private String REPORT_NAME_JINCSJKH = "Jincsjkh";// ����ʱ������Ա��ϵͼ

//	private String mstrReportName = "";

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		Visit visit = (Visit)getPage().getVisit();
		String mstrReportName = visit.getString3();
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		if (mstrReportName.equals(REPORT_NAME_CAIYYCYJGFX_HZ)) {
			return getCaiyycyjgfx_hz();
		} else if (mstrReportName.equals(REPORT_NAME_CAIYYCYJGFX_MX)) {
			return getCaiyycyjgfx_mx();
		} else if (mstrReportName.equals(REPORT_NAME_JINCSJKH)) {
			return getJincsjkh();
		} else {
			return "�޴˱���";
		}
		
	}

	public String getImgServletPath(String name) {
		Visit visit = (Visit) getPage().getVisit();
		return ("<img width=745 height=400 src='"
				+ MainGlobal.getHomeContext(getPage()) + "/img/CaiyPicture"
				+ visit.getRenyID() + "/" + name + "' />");
	}

	// ���ܱ�
	int num = 0;// ����һ��ֵ�Զ���������ֹ��һ�η����в�ˢ��ͼ������

	private String getCaiyycyjgfx_hz() {
		num++;
		CreateChartFile(createChart(), "Caiyycyjgfx_hz" + num);

		Report rt = new Report();
		int[] colWidth = { 999 };
//		int[] colWidth = { 750 };
		String value = ((IDropDownModel) getGongysDropDownModel())
				.getBeanValue(Long.parseLong(treeid));
		value = value == null ? "" : value;
		rt.setTitle("<font size =5><b>" + getBRiq() + "��" + getERiq() + value
				+ "��������ͼ</b></font>", colWidth);

		rt.setBody(new Table(1, 1));// ����

		rt.body.setCellValue(1, 1, getImgServletPath("Caiyycyjgfx_hz" + num
				+ ".jpg"));// ���е�ͼ

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}

		return rt.getAllPagesHtml();
	}

	// ��ϸ��
	private String getCaiyycyjgfx_mx() {

		num++;
		CreateChartFile(createChart2(), "Caiyycyjgfx_mx" + num);

		Report rt = new Report();
		int[] colWidth = { 990 };
//		int[] colWidth = { 750 };
		String value = ((IDropDownModel) getGongysDropDownModel())
				.getBeanValue(Long.parseLong(treeid));
		value = value == null ? "" : value;

		rt.setTitle("<font size =5><b>" + getBRiq() +"��"+ getERiq() + value
				+ "������ϸͼ</b></font>", colWidth);// ���ñ�ͷ

		rt.setBody(new Table(1, 1));// ����

		rt.body.setCellValue(1, 1, getImgServletPath("Caiyycyjgfx_mx" + num
				+ ".jpg"));// ���е�ͼ

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}

		return rt.getAllPagesHtml();
	}

	// ����ʱ������Ա��ϵͼ
	private String getJincsjkh() {

		num++;
		CreateChartFile(createChart1(), "Jincsjkh" + num);

		Report rt = new Report();

		int[] colWidth = { 990 };
//		int[] colWidth = { 750 };

		String value = ((IDropDownModel) getGongysDropDownModel())
				.getBeanValue(Long.parseLong(treeid));
		value = value == null ? "" : value;

		rt.setTitle("<font size =5><b>" + getBRiq() +"��"+ getERiq() + value
				+ "����ʱ������Ա��ϵͼ</b></font>", colWidth);// ���ñ�ͷ

		rt.setBody(new Table(1, 1));// ����

		rt.body.setCellValue(1, 1, getImgServletPath("Jincsjkh" + num
				+ ".jpg"));// ���е�ͼ

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}

		return rt.getAllPagesHtml();
	}

	private JFreeChart createChart() {
		// create the first dataset...
		DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();

		// now create the second dataset and renderer...
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append("select y.caiyy,\n")
		.append("nvl(decode(sum(f.jingz),0,0,sum(y.qnet_ar*f.jingz)/sum(f.jingz)),0) qnet_ar,\n")
		.append("count(c.id) cs,sum(f.ches) ches from caiyb c,fahb f,\n")
		.append("(select y.caiyb_id, nvl(r.mingc, 'δ֪') caiyy,nvl(avg(z.qnet_ar), 0) qnet_ar \n")
		.append("from caiyryglb b, zhillsb z, yangpdhb y, renyxxb r where y.zhilblsb_id = z.id \n")
		.append("and b.renyxxb_id = r.id(+) and y.id = b.yangpdhb_id \n")
		.append("group by y.caiyb_id, r.mingc ) y\n")
		.append("where y.caiyb_id = c.id and c.zhilb_id = f.zhilb_id\n")
		.append(" and f.daohrq>=").append(DateUtil.FormatOracleDate(getBRiq()))
		.append(" and f.daohrq<").append(DateUtil.FormatOracleDate(getERiq()))
		.append(getGongysSql())
		.append("\n")
		.append("group by y.caiyy\n");

		ResultSet rs = con.getResultSet(sql);

		try {
			while (rs.next()) {
				dataset1.addValue(rs.getDouble(3), "��������",
						(rs.getString(1) == null ? "" : rs.getString(1)));

				dataset1.addValue(rs.getDouble(4), "��������",
						(rs.getString(1) == null ? "" : rs.getString(1)));

				dataset2.addValue(rs.getDouble(2), "������",
						(rs.getString(1) == null ? "" : rs.getString(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// create the first renderer...
		CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();// Ҳ����ͨ�������������ϵ����ֿɼ�
		CategoryItemRenderer renderer = new BarRenderer();
		renderer.setItemLabelGenerator(generator);
		renderer.setItemLabelsVisible(true);

		CategoryPlot plot = new CategoryPlot();
		plot.setDataset(dataset1);// ���������д����
		plot.setRenderer(renderer);

		plot.setDomainAxis(new CategoryAxis(""));// ����x axis
		plot.setRangeAxis(new NumberAxis(""));// ����y axis

		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.setRangeGridlinesVisible(true);
		plot.setDomainGridlinesVisible(true);
		// --------------------------------------------------------------��ǰ����������
		CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
		renderer2.setItemLabelGenerator(generator);
		renderer2.setItemLabelsVisible(true);
		plot.setDataset(1, dataset2);
		plot.setRenderer(1, renderer2);
		plot.mapDatasetToRangeAxis(1, 1);// �����ұߵ�����Ϊ1����

		// create the third dataset and renderer...
		ValueAxis rangeAxis2 = new NumberAxis("");
		plot.setRangeAxis(1, rangeAxis2);

		plot.getDomainAxis().setCategoryLabelPositions(
				CategoryLabelPositions.DOWN_45);
		plot.setNoDataMessage("�������õ�������û���������!");
		JFreeChart chart = new JFreeChart(plot);
		chart.setTitle(" ");
		return chart;
	}

	private JFreeChart createChart1() {
		// create the first dataset...
		DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();

		// now create the second dataset and renderer...
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append("select y.caiyy,\n")
		.append("nvl(decode(sum(f.jingz),0,0,sum(y.qnet_ar*f.jingz)/sum(f.jingz)),0) qnet_ar,\n")
		.append("sum(f.ches) ches from caiyb c,fahb f,\n")
		.append("(select y.caiyb_id,nvl(r.mingc,'δ֪') caiyy,nvl(avg(z.qnet_ar),0) qnet_ar \n")
		.append("from caiyryglb b, zhillsb z, yangpdhb y, renyxxb r where y.zhilblsb_id = z.id\n")
		.append("and b.renyxxb_id = r.id(+) and y.id = b.yangpdhb_id \n")
		.append("group by y.caiyb_id, r.mingc) y\n")
		.append("where y.caiyb_id = c.id and c.zhilb_id = f.zhilb_id\n")
		.append(" and f.daohrq>=").append(DateUtil.FormatOracleDate(getBRiq()))
		.append(" and f.daohrq<").append(DateUtil.FormatOracleDate(getERiq()))
		.append(getGongysSql())
		.append("\n")
		.append("group by y.caiyy\n");
		ResultSet rs = con.getResultSet(sql);

		String str = "";
		int number = 0;
		try {
			while (rs.next()) {
				if (!(rs.getString(1).compareTo(str) == 0)) {
					number = 0;
				}
				str = rs.getString(1);
				dataset1.addValue(rs.getDouble(3), "��������", rs.getString(1)
						+ (number++));

				dataset2.addValue(rs.getDouble(2), "������", rs.getString(1)
						+ number);

				// dataset1.addValue(rs.getDouble(5), "2", rs.getString(1));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// create the first renderer...
		CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();// Ҳ����ͨ�������������ϵ����ֿɼ�
		CategoryItemRenderer renderer = new BarRenderer();
		renderer.setItemLabelGenerator(generator);
		renderer.setItemLabelsVisible(true);

		CategoryPlot plot = new CategoryPlot();
		plot.setDataset(dataset1);// ���������д����
		plot.setRenderer(renderer);

		plot.setDomainAxis(new CategoryAxis(""));// ����x axis
		plot.setRangeAxis(new NumberAxis(""));// ����y axis

		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.setRangeGridlinesVisible(true);
		plot.setDomainGridlinesVisible(true);
		// --------------------------------------------------------------��ǰ����������
		CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
		renderer2.setItemLabelGenerator(generator);
		renderer2.setItemLabelsVisible(true);
		plot.setDataset(1, dataset2);
		plot.setRenderer(1, renderer2);
		plot.mapDatasetToRangeAxis(1, 1);// �����ұߵ�����Ϊ1����
		plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

		// create the third dataset and renderer...
		ValueAxis rangeAxis2 = new NumberAxis("");
		plot.setRangeAxis(1, rangeAxis2);

		plot.getDomainAxis().setCategoryLabelPositions(
				CategoryLabelPositions.DOWN_45);
		plot.setNoDataMessage("�������õ�������û���������!");
		JFreeChart chart = new JFreeChart(plot);
		chart.setTitle(" ");
		return chart;
	}

	private JFreeChart createChart2() {
		// create the first dataset...
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append("select y.caiyy|| rownum, y.qnet_ar, f.ches from caiyb c,fahb f,\n")
		.append("(select y.caiyb_id,nvl(r.mingc, 'δ֪') caiyy,nvl(avg(z.qnet_ar), 0) qnet_ar  \n")
		.append("from caiyryglb b,zhillsb z,yangpdhb y ,renyxxb r where y.zhilblsb_id = z.id \n")
		.append("and  b.renyxxb_id = r.id(+) and y.id = b.yangpdhb_id \n")
		.append(" group by y.caiyb_id,r.mingc) y\n")
		.append("where y.caiyb_id = c.id and c.zhilb_id = f.zhilb_id\n")
		.append(" and f.daohrq>=").append(DateUtil.FormatOracleDate(getBRiq()))
		.append(" and f.daohrq<").append(DateUtil.FormatOracleDate(getERiq()))
		.append(getGongysSql())
		.append("\n");
		
		ResultSet rs = con.getResultSet(sql);
		try {
			while (rs.next()) {
				String str = rs.getString(1);

				dataset.addValue(rs.getInt(2), "������", str);

				dataset.addValue(rs.getInt(3), "��������", str);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// create the first renderer...
		CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();// Ҳ����ͨ�������������ϵ����ֿɼ�
		CategoryItemRenderer renderer = new BarRenderer();
		renderer.setItemLabelGenerator(generator);
		renderer.setItemLabelsVisible(true);

		CategoryPlot plot = new CategoryPlot();
		plot.setDataset(dataset);// ���������д����
		plot.setRenderer(renderer);

		plot.setDomainAxis(new CategoryAxis(""));// ����x axis
		plot.setRangeAxis(new NumberAxis(""));// ����y axis

		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.setRangeGridlinesVisible(true);
		plot.setDomainGridlinesVisible(true);
		// --------------------------------------------------------------��ǰ����������

		JFreeChart chart = new JFreeChart(plot);

		return chart;
	}

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
//			ChartUtilities.saveChartAsJPEG(chartFile, chart, 990, 400);
			ChartUtilities.saveChartAsJPEG(chartFile, chart, 790, 400);
			return getImgServletPath(chartFile.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	// ������ʹ�õķ���
	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(0);
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField) getToolbar().getItem("gongysTree_text"))
						.setValue(((IDropDownModel) getGongysDropDownModel())
								.getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}

	public String getTreeScript() {
		// System.out.print(((Visit)
		// this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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
		return getToolbar().getRenderScript();
	}

	// ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		String reportType = cycle.getRequestContext().getParameter("lx");
		if (reportType != null) {
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			visit.setString3(reportType);
			//mstrReportName = reportType;
			num = 0;
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			setChangbValue(null);
			setChangbModel(null);
			getSelectData();
		}

		blnIsBegin = true;
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

	// ҳ���½��֤
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
}
