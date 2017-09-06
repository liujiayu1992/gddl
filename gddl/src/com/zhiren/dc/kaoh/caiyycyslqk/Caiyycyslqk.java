package com.zhiren.dc.kaoh.caiyycyslqk;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

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

public class Caiyycyslqk extends BasePage {

	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		
	}

	protected void initialize() {
		// TODO 自动生成方法存根
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

	// 绑定日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}

	// 绑定日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}

	// 页面变化记录
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

	// 厂别下拉框
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

	// 获取供应商
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
		setGongysDropDownModel(new IDropDownModel(sql, "全部"));
		return;
	}

	// 刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		if (visit.isFencb()) {
			tb1.addText(new ToolbarText("厂别:"));
			ComboBox changbcb = new ComboBox();
			changbcb.setTransform("ChangbSelect");
			changbcb.setWidth(130);
			changbcb
					.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
			tb1.addField(changbcb);
			tb1.addText(new ToolbarText("-"));
		}
		tb1.addText(new ToolbarText("到货日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("guohrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
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

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

		ToolbarButton rbtn = new ToolbarButton(null, "查询",
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


	// 通过ChartFactory创建JFreeChart的实例

	public JFreeChart createJFreeChart() {
		DefaultPieDataset defaultpiedataset = new DefaultPieDataset();

		JDBCcon con = new JDBCcon();

		StringBuffer sql = new StringBuffer();
		sql.append("select y.caiyy,\n")
		.append("sum(f.ches) ches from caiyb c,fahb f,\n")
		.append("(select y.caiyb_id,nvl(r.mingc,'未知') caiyy,nvl(avg(z.qnet_ar),0) qnet_ar \n")
		.append("from caiyryglb b, zhillsb z, yangpdhb y, renyxxb r where y.zhilblsb_id = z.id \n")
		.append("and b.renyxxb_id = r.id(+) and y.id = b.yangpdhb_id \n")
		.append("group by y.caiyb_id, r.mingc ) y\n")
		.append("where y.caiyb_id = c.id and c.zhilb_id = f.zhilb_id\n")
		.append(" and f.daohrq>=").append(DateUtil.FormatOracleDate(getBRiq()))
		.append(" and f.daohrq<").append(DateUtil.FormatOracleDate(getERiq()))
		.append(getGongysSql())
		.append("\n")
		.append("group by y.caiyy\n");
		System.out.println(sql);
		ResultSet rs = con.getResultSet(sql.toString());
		
		int number = 0;
		try {
			while (rs.next()) {
				defaultpiedataset.setValue(rs.getString(1), rs.getInt(2));
				number += rs.getInt(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		JFreeChart pieChart = ChartFactory.createPieChart("",
				defaultpiedataset, true, true, true);
		TextTitle ts = new TextTitle();
		ts.setText("采样车数：" + number);
		ts.setFont(new Font("SansSerif", Font.BOLD, 12));

		pieChart.setTitle(ts);

		PiePlot pie = (PiePlot) pieChart.getPlot();

		pie.setLabelFont(new Font("SansSerif", Font.BOLD, 12));

		pie.setNoDataMessage("No data available");

		pie.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{1}车,{2},{0} ", new DecimalFormat("0"), new DecimalFormat(
						"0.00%")));

		pie.setLabelBackgroundPaint(Color.YELLOW);

		pie.setCircular(true);

		pie.setLabelGap(0.01D);// 间距

		return pieChart;

	}

	private JFreeChart createJFreeChart1() {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		StringBuffer sql = new StringBuffer();
		sql.append("select y.caiyy,\n")
		.append("nvl(decode(sum(f.jingz),0,0,sum(y.qnet_ar*f.jingz)/sum(f.jingz)),0) qnet_ar,\n")
		.append("sum(y.cs) cs from caiyb c,fahb f,\n")
		.append("(select y.caiyb_id,nvl(r.mingc,'未知') caiyy,count(y.id) cs,nvl(avg(z.qnet_ar),0) qnet_ar \n")
		.append("from yangpdhb y,zhillsb z,renyxxb r\n")
		.append("where y.zhilblsb_id = z.id and y.caiyry_id = r.id(+) \n")
		.append("group by y.caiyb_id,r.mingc) y\n")
		.append("where y.caiyb_id = c.id and c.zhilb_id = f.zhilb_id\n")
		.append(" and f.daohrq>=").append(DateUtil.FormatOracleDate(getBRiq()))
		.append(" and f.daohrq<").append(DateUtil.FormatOracleDate(getERiq()))
		.append(getGongysSql())
		.append("\n")
		.append("group by y.caiyy\n");
		JDBCcon con = new JDBCcon();
		ResultSet rs = con.getResultSet(sql);

		try {
			while (rs.next()) {
				dataset.addValue(rs.getInt(3), "制样次数", rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();// 也可以通过这样设置柱上的数字可见
		CategoryItemRenderer renderer = new BarRenderer();

		renderer.setItemLabelGenerator(generator);
		renderer.setItemLabelsVisible(true);

		renderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());

		GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.red, 0.0f,
				0.0f, Color.YELLOW);
		renderer.setBasePaint(gp0);
		renderer.setSeriesPaint(0, gp0);

		CategoryPlot plot = new CategoryPlot();

		plot.setDataset(dataset);
		plot.setRenderer(renderer);

		plot.setDomainAxis(new CategoryAxis("制样人员"));
		plot.setRangeAxis(new NumberAxis("制样次数"));

		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinesVisible(true);

		plot.getDomainAxis().setCategoryLabelPositions(
				CategoryLabelPositions.DOWN_45);

		JFreeChart chart = new JFreeChart(plot);

		return chart;
	}

	private JFreeChart createJFreeChart2() {// 化验审核工作情况
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		String sql = "select SUBSTR(sysdate, 9, 2) AS HUAYSJ,7 as yij,9 as erj from dual\n"
				+ "union\n"
				+ "select '9' AS HUAYSJ,10 as yij,11 as erj from dual";

		JDBCcon con = new JDBCcon();
		
		ResultSet rs = con.getResultSet(sql);

		try {
			while (rs.next()) {
				dataset.addValue(rs.getInt(2), "一审合格线", rs.getString(1));
				dataset.addValue(rs.getInt(3), "二审合格线", rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// create the chart...
		JFreeChart chart = ChartFactory.createLineChart("", // chart
				// title
				"审核时间", // domain axis label
				"", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
				);

		chart.setBackgroundPaint(Color.white);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinesVisible(false);
		plot.setDomainGridlineStroke(new BasicStroke(0.5f));
		plot.setDomainGridlinePaint(Color.DARK_GRAY);

		// // add a category marker纵向参考线加法
		// CategoryMarker marker = new CategoryMarker("02", Color.red,
		// new BasicStroke(1.0f));
		// marker.setDrawAsLine(true);
		// marker.setLabel("Marker Label");
		// marker.setLabelFont(new Font("Dialog", Font.PLAIN, 11));
		// marker.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
		// marker.setLabelOffset(new RectangleInsets(2, 5, 2, 5));
		// plot.addDomainMarker(marker, Layer.BACKGROUND);
		// 横向参考线加法
		IntervalMarker target = new IntervalMarker(5.975, 6.025);
		target.setLabel("一审合格线");
		target.setLabelFont(new Font("宋体", Font.BOLD, 11));
		target.setLabelAnchor(RectangleAnchor.BOTTOM_LEFT);
		target.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);
		target.setPaint(Color.darkGray);
		plot.addRangeMarker(target, Layer.BACKGROUND);

		target = new IntervalMarker(7.975, 8.025);
		target.setLabel("二审合格线");
		target.setLabelFont(new Font("宋体", Font.BOLD, 11));
		target.setLabelAnchor(RectangleAnchor.LEFT);
		target.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);
		target.setPaint(Color.darkGray);
		plot.addRangeMarker(target, Layer.BACKGROUND);

		// CategoryMarker mark = new CategoryMarker(dataset.getRowKey(1));

		// customise the range axis...
		SymbolAxis rangeAxis = new SymbolAxis("", new String[] { "", "", "",
				"", "", "", "", "", "", "", "", "", "", "", "" });
		rangeAxis.setAxisLineStroke(new BasicStroke(0.0f));
		plot.setRangeAxis(rangeAxis);

		// customise the renderer...
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot
				.getRenderer();
		renderer.setSeriesShapesVisible(0, true);
		renderer.setSeriesShapesVisible(1, true);
		renderer.setDrawOutlines(true);
		// renderer.setUseFillPaint(true);
		// renderer.setFillPaint(Color.yellow);

		return chart;
	}

	private JFreeChart createJFreeChart3() {// 检斤员检斤数量情况图
		// create the first dataset...
//		Visit visit = (Visit)getPage().getVisit();
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		int allChes = 0;
		int allLies = 0;
		JDBCcon con = new JDBCcon();
		String sql ="select sum(f.ches) ches,count(distinct f.id) lies, nvl(c.lury,'') diaodry from fahb f,chepb c ,diancxxb d\n" +
			"       where d.id=f.diancxxb_id\n" + 
//			"   and d.id=" + visit.getDiancxxb_id() +   //加入电厂ID选择
			"       and c.fahb_id=f.id and to_char(c.lursj,'yyyy-mm-dd')>='"+getBRiq()+"'\n" + 
			"       and to_char(c.lursj,'yyyy-mm-dd')<='"+getERiq()+"'\n" + 
			"       group by c.lury";



		ResultSet rs = con.getResultSet(sql);
		try {
			while (rs.next()) {

				allLies += rs.getInt(2);
				allChes += rs.getInt(1);
				dataset.addValue(rs.getInt(1), "车数", rs.getString(3));

				dataset.addValue(rs.getInt(2), "列数", rs.getString(3));
				// dataset1.addValue(rs.getDouble(5), "2", rs.getString(1));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// create the first renderer...
		CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();// 也可以通过这样设置柱上的数字可见
		CategoryItemRenderer renderer = new BarRenderer();
		renderer.setItemLabelGenerator(generator);
		renderer.setItemLabelsVisible(true);

		CategoryPlot plot = new CategoryPlot();
		plot.setDataset(dataset);// 这里把数据写入中
		plot.setRenderer(renderer);

		plot.setDomainAxis(new CategoryAxis(""));// 设置x axis
		plot.setRangeAxis(new NumberAxis(""));// 设置y axis

		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.setRangeGridlinesVisible(true);
		plot.setDomainGridlinesVisible(true);
		// --------------------------------------------------------------以前是柱形设置

		plot.getDomainAxis().setCategoryLabelPositions(
				CategoryLabelPositions.DOWN_45);
		JFreeChart chart = new JFreeChart(plot);
		TextTitle title = new TextTitle();
		title.setFont(new Font("宋体", Font.BOLD, 11));
		title.setText("总车数:" + allChes + "                         总列数:"
				+ allLies);
		chart.setTitle(title);

		return chart;
	}

	private JFreeChart createJFreeChart4() {// 计量审核工作情况

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		String sql = "select cheh,min(shenhx),9 kaohx  from (\n"
				+ "select nvl('','01号') cheh,\t10 as Shenhx\t,9 Kaohx from dual union\n"
				+ "select nvl('','02号'),\t10\t,9 from dual union\n"
				+ "select nvl('','02号'),\t10\t,9 from dual union\n"
				+ "select nvl('','03号'),\t10\t,9 from dual union\n"
				+ "select nvl('','03号'),\t10\t,9 from dual union\n"
				+ "select nvl('','04号'),\t10\t,9 from dual union\n"
				+ "select nvl('','04号'),\t10\t,9 from dual union\n"
				+ "select nvl('','05号'),\t10\t,9 from dual union\n"
				+ "select nvl('','05号'),\t10\t,9 from dual union\n"
				+ "select nvl('','05号'),\t9\t,9 from dual union\n"
				+ "select nvl('','07号'),\t10\t,9 from dual union\n"
				+ "select nvl('','07号'),\t10\t,9 from dual union\n"
				+ "select nvl('','09号'),\t10\t,9 from dual union\n"
				+ "select nvl('','11号'),\t10\t,9 from dual union\n"
				+ "select nvl('','12号'),\t10\t,9 from dual union\n"
				+ "select nvl('','14号'),\t10\t,9 from dual union\n"
				+ "select nvl('','14号'),\t10\t,9 from dual union\n"
				+ "select nvl('','15号'),\t10\t,9 from dual union\n"
				+ "select nvl('','15号'),\t10\t,9 from dual union\n"
				+ "select nvl('','16号'),\t10\t,9 from dual union\n"
				+ "select nvl('','17号'),\t10\t,9 from dual union\n"
				+ "select nvl('','17号'),\t10\t,9 from dual union\n"
				+ "select nvl('','18号'),\t10\t,9 from dual union\n"
				+ "select nvl('','19号'),\t9\t,9 from dual union\n"
				+ "select nvl('','19号'),\t10\t,9 from dual union\n"
				+ "select nvl('','19号'),\t10\t,9 from dual union\n"
				+ "select nvl('','21号'),\t10\t,9 from dual union\n"
				+ "select nvl('','22号'),\t10\t,9 from dual union\n"
				+ "select nvl('','22号'),\t10\t,9 from dual union\n"
				+ "select nvl('','23号'),\t10\t,9 from dual union\n"
				+ "select nvl('','23号'),\t10\t,9 from dual union\n"
				+ "select nvl('','24号'),\t10\t,9 from dual union\n"
				+ "select nvl('','25号'),\t10\t,9 from dual union\n"
				+ "select nvl('','26号'),\t10\t,9 from dual union\n"
				+ "select nvl('','26号'),\t10\t,9 from dual union\n"
				+ "select nvl('','27号'),\t10\t,9 from dual union\n"
				+ "select nvl('','28号'),\t10\t,9 from dual union\n"
				+ "select nvl('','29号'),\t10\t,9 from dual union\n"
				+ "select nvl('','30号'),\t10\t,9 from dual union\n"
				+ "select nvl('','30号'),\t10\t,9 from dual )\n"
				+ "group by cheh order by cheh";

		JDBCcon con = new JDBCcon();
		System.out.println(sql);
		ResultSet rs = con.getResultSet(sql);

		try {
			while (rs.next()) {
				dataset.addValue(rs.getInt(2), "审合时间", rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// create the chart...
		JFreeChart chart = ChartFactory.createLineChart("", // chart
				// title
				"审核日期", // domain axis label
				"", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
				);

		chart.setBackgroundPaint(Color.white);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinesVisible(false);
		plot.setDomainGridlineStroke(new BasicStroke(0.2f));
		plot.setDomainGridlinePaint(Color.BLACK);

		IntervalMarker target = new IntervalMarker(8.975, 9.025);
		target.setLabel("计量审核时间考核线");
		target.setLabelPaint(Color.DARK_GRAY);
		target.setLabelFont(new Font("宋体", Font.BOLD, 11));
		target.setLabelAnchor(RectangleAnchor.BOTTOM_LEFT);
		target.setLabelTextAnchor(TextAnchor.BOTTOM_LEFT);
		target.setPaint(Color.darkGray);
		plot.addRangeMarker(target, Layer.BACKGROUND);

		// CategoryMarker mark = new CategoryMarker(dataset.getRowKey(1));

		// customise the range axis...
		SymbolAxis rangeAxis = new SymbolAxis("审合时间", new String[] { "", "",
				"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
				"", "", "" });
		rangeAxis.setAxisLineStroke(new BasicStroke(0.0f));
		plot.setRangeAxis(rangeAxis);

		// customise the renderer...
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot
				.getRenderer();
		CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();
		renderer.setSeriesShapesVisible(0, true);
		renderer.setSeriesShapesVisible(1, true);
		renderer.setDrawOutlines(true);
		renderer.setItemLabelGenerator(generator);
		renderer.setItemLabelsVisible(true);
		renderer.setItemLabelPaint(Color.blue);

		plot.getDomainAxis().setCategoryLabelPositions(
				CategoryLabelPositions.DOWN_45);
		plot.setNoDataMessage("您设置的时间段内没有数据!");
		plot.setNoDataMessageFont(new Font("", 10, 20));
		// renderer.setUseFillPaint(true);
		// renderer.setFillPaint(Color.yellow);

		return chart;
	}

	public String CreateChartFile(JFreeChart chart, String filebassname) {
		try {
			Visit visit = (Visit) getPage().getVisit();
			String FilePath = MainGlobal.getServletPath(getPage())
					+ "/img/CaiyPicture" + visit.getRenyID();
			File dir = new File(FilePath);
			if (dir.exists()) {
				deleteFile(dir);// 册除文件及文件夹下所在的文件
				dir.mkdir();
			} else {
				dir.mkdir();
			}
			File chartFile = new File(dir, filebassname + ".jpg");
			ChartUtilities.saveChartAsJPEG(chartFile, chart, 790, 400);
			return getImgServletPath(chartFile.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getImgServletPath(String name) {
		Visit visit = (Visit) getPage().getVisit();
		return ("<img width=740 height=400 src='"
				+ MainGlobal.getHomeContext(getPage()) + "/img/CaiyPicture"
				+ visit.getRenyID() + "/" + name + "' />");
	}

	private String REPORT_NAME_CAIYYCYSLQK = "Caiyycyslqk";// 采样汇总

	private String REPORT_NAME_CAIYYZYSLQK = "Caiyyzyslqk";// 制样数量情况图

	private String REPORT_NAME_HUAYSHSJKH = "Huayshsjkh";// 化验审核工作情况

	private String REPORT_NAME_JIANJYJJQK = "Jianjyjjqk";// 检斤员检斤数量情况图

	private String REPORT_NAME_JILSHSJKH = "Jilshsjkh";// 检斤员检斤数量情况图

//	private String mstrReportName = "";

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		Visit visit = (Visit)getPage().getVisit();
		if (!blnIsBegin) {
			return "";
		}
		String mstrReportName = visit.getString3(); 
		blnIsBegin = false;
		if (mstrReportName.equals(REPORT_NAME_CAIYYCYSLQK)) {
			return getCaiyycyslqk();
		} else if (mstrReportName.equals(REPORT_NAME_CAIYYZYSLQK)) {
			return getCaiyyzyslqk();
		} else if (mstrReportName.equals(REPORT_NAME_HUAYSHSJKH)) {
			return getHuayshsjkh();
		} else if (mstrReportName.equals(REPORT_NAME_JIANJYJJQK)) {
			return getJianjyjjqk();
		} else if (mstrReportName.equals(REPORT_NAME_JILSHSJKH)) {
			return getJilshsjkh();
		} else {
			return "无此报表";
		}
	}

	// 采样汇总
	int num = 0;// 设置一个值自动曾长，防止在一次访问中不刷新图形设置

	private String getCaiyycyslqk() {

		num++;
		CreateChartFile(createJFreeChart(), "Caiyycyslqk" + num);

		Report rt = new Report();
//		int[] colWidth = { 999 };
		int[] colWidth = { 790 };
		rt.setTitle("<font size =5><b>" + getBRiq() + "至" + getERiq()
				+ "采样员采样数量情况图</b></font>", colWidth);

		rt.setBody(new Table(1, 1));// 表身

		rt.body.setCellValue(1, 1, getImgServletPath("Caiyycyslqk" + num
				+ ".jpg"));// 表中的图

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}

		return rt.getAllPagesHtml();
	}

	// 制样数量情况图
	private String getCaiyyzyslqk() {

		num++;
		CreateChartFile(createJFreeChart1(), "Caiyyzyslqk" + num);

		Report rt = new Report();

//		int[] colWidth = { 999 };
		int[] colWidth = { 790 };
		rt.setTitle("<font size =5><b>" + getBRiq() + "至" + getERiq()
				+ "制样数量情况图</b></font>", colWidth);

		rt.setBody(new Table(1, 1));// 表身

		rt.body.setCellValue(1, 1, getImgServletPath("Caiyyzyslqk" + num
				+ ".jpg"));// 表中的图

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}

		return rt.getAllPagesHtml();
	}

	// 化验审核工作情况
	private String getHuayshsjkh() {

		num++;
		CreateChartFile(createJFreeChart2(), "Huayshsjkh" + num);

		Report rt = new Report();

//		int[] colWidth = { 999 };
		int[] colWidth = { 790 };
		rt.setTitle("<font size =5><b>" + getBRiq() + "至" + getERiq()
				+ "化验审核工作情况</b></font>", colWidth);

		rt.setBody(new Table(1, 1));// 表身

		rt.body.setCellValue(1, 1, getImgServletPath("Huayshsjkh" + num
				+ ".jpg"));// 表中的图

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}

		return rt.getAllPagesHtml();
	}

	// 检斤员检斤数量情况图
	private String getJianjyjjqk() {

		num++;
		CreateChartFile(createJFreeChart3(), "Jianjyjjqk" + num);

		Report rt = new Report();
//		int[] colWidth = { 999 }
		int[] colWidth = { 840 };;
		rt.setTitle("<font size =5><b>" + getBRiq() + "至" + getERiq()
				+ "检斤员检斤数量情况图</b></font>", colWidth);

		rt.setBody(new Table(1, 1));// 表身

		rt.body.setCellValue(1, 1, getImgServletPath("Jianjyjjqk" + num
				+ ".jpg"));// 表中的图

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}

		return rt.getAllPagesHtml();
	}

	private String getJilshsjkh() {// 检斤员检斤数量情况图

		num++;
		CreateChartFile(createJFreeChart4(), "Jilshsjkh" + num);

		Report rt = new Report();
//		int[] colWidth = { 999 };
		int[] colWidth = { 840 };
		rt.setTitle("<font size =5><b>" + getBRiq() + "至" + getERiq()
				+ "计量提交审核时间情况统计</b></font>", colWidth);

		rt.setBody(new Table(1, 1));// 表身

		rt.body.setCellValue(1, 1,
				getImgServletPath("Jilshsjkh" + num + ".jpg"));// 表中的图

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}

		return rt.getAllPagesHtml();
	}

	// 工具栏使用的方法
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

	// 页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		String reportType = cycle.getRequestContext().getParameter("lx");
		if (reportType != null) {
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			visit.setString3(reportType);
//			mstrReportName = reportType;
			num = 0;
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			setChangbValue(null);
			setChangbModel(null);
			getSelectData();
		}

		blnIsBegin = true;
	}

	// 按钮的监听事件
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}

	// 页面登陆验证
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

	private static void deleteFile(File dir) throws IOException {// 册除文件夹及文件夹下所有内容
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
