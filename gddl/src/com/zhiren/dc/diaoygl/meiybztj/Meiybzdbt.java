package com.zhiren.dc.diaoygl.meiybztj;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Meiybzdbt extends BasePage {

	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
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

	// 年份
	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean2((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();

	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() != Value) {
			nianfchanged = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(listNianf));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	// 月份
	public boolean Changeyuef = false;

	public IPropertySelectionModel getYuefModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getYuefModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public IDropDownBean getYuefValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean3((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit) getPage().getVisit()).getDropDownBean3() != null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);

	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(listYuef));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(_value);
	}
	
    // 煤矿下拉框
	public IDropDownBean getMeikValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			if (getMeikModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getMeikModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setMeikValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setMeikModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getIMeikModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public void getIMeikModels() {		
		String sql = "select id, mingc from meikxxb";
		((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql));
	}
	
    // 电厂名称
	public boolean _diancmcchange = false;

	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if (_DiancmcValue == null) {
			_DiancmcValue = (IDropDownBean) getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		String sql = "";
		sql = "select d.id,d.mingc from diancxxb d order by d.mingc desc";

		_IDiancmcModel = new IDropDownModel(sql);
	}

	public String getNianf() {
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		return intyear + "";
	}

	public String getYuef() {
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {
			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		return StrMonth;
	}
	
    // 记录煤矿ID
	public long getMeikxxb_id() {
		return getMeikValue().getId();
	}
	
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("煤矿:"));
		ComboBox meik = new ComboBox();
		meik.setTransform("MEIKXXB_ID");
		meik.setWidth(120);
		meik.setListeners("select:function(){document.Form0.submit();}");
		meik.setLazyRender(true);
		meik.setEditable(false);
		tb1.addField(meik);
		tb1.addText(new ToolbarText("-"));
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),
				"-1".equals(getTreeid()) ? null : getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(120);
		tf.setValue(((IDropDownModel) getIDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "刷新",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText(
				"<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}

	private String REPORT_NAME_BIZFELDBT = "Bizfrldbt";// 比重发热量对比图

	private String REPORT_NAME_BIZHFDBT = "Bizhfdbt";// 比重灰分对比图

	private String REPORT_NAME_BIZSFDBT = "Bizsfdbt";// 比重水分对比图

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		Visit visit = (Visit)getPage().getVisit();
		String mstrReportName = visit.getString3();
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		if (mstrReportName.equals(REPORT_NAME_BIZFELDBT)) {
			return getBizfrldbt();
		} else if (mstrReportName.equals(REPORT_NAME_BIZHFDBT)) {
			return getBizhfdbt();
		} else if (mstrReportName.equals(REPORT_NAME_BIZSFDBT)) {
			return getBizsfdbt();
		} else {
			return "无此报表";
		}		
	}

	public String getImgServletPath(String name) {
		Visit visit = (Visit) getPage().getVisit();
		return ("<img width=945 height=600 src='"
				+ MainGlobal.getHomeContext(getPage()) + "/img/BizdbPicture"
				+ visit.getRenyID() + "/" + name + "' />");
	}

	// 比重发热量对比图
	int num = 0;// 设置一个值自动增长，防止在一次访问中不刷新图形设置
	String meikmc;
	
	private String getBizfrldbt() {
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select mingc from meikxxb where id = " + getMeikxxb_id());
		if (rsl.next()) {
			meikmc = rsl.getString("mingc");
		}
		
		num++;
		CreateChartFile(createChart(), "Bizfrldbt" + num);

		Report rt = new Report();
		int[] colWidth = { 999 };
		
		rt.setTitle("<font size =5><b>" + getNianf() + "年" + getYuef() + "月" + meikmc
				+ "比重发热量对比图</b></font>", colWidth);

		rt.setBody(new Table(1, 1));// 表身

		rt.body.setCellValue(1, 1, getImgServletPath("Bizfrldbt" + num
				+ ".jpg"));// 表中的图

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		rsl.close();
		con.Close();
		return rt.getAllPagesHtml();
	}

	// 比重灰分对比图
	private String getBizhfdbt() {
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select mingc from meikxxb where id = " + getMeikxxb_id());
		if (rsl.next()) {
			meikmc = rsl.getString("mingc");
		}
		
		num++;
		CreateChartFile(createChart1(), "Bizhfdbt" + num);

		Report rt = new Report();
		int[] colWidth = { 990 };

		rt.setTitle("<font size =5><b>" + getNianf() + "年" + getYuef() + "月" + meikmc
				+ "比重灰分对比图</b></font>", colWidth);// 设置表头

		rt.setBody(new Table(1, 1));// 表身

		rt.body.setCellValue(1, 1, getImgServletPath("Bizhfdbt" + num
				+ ".jpg"));// 表中的图

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		rsl.close();
		con.Close();
		return rt.getAllPagesHtml();
	}

	// 比重水分对比图
	private String getBizsfdbt() {
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select mingc from meikxxb where id = " + getMeikxxb_id());
		if (rsl.next()) {
			meikmc = rsl.getString("mingc");
		}
		
		num++;
		CreateChartFile(createChart2(), "Bizsfdbt" + num);

		Report rt = new Report();
		int[] colWidth = { 990 };

		rt.setTitle("<font size =5><b>" + getNianf() + "年" + getYuef() + "月" + meikmc
				+ "比重水分对比图</b></font>", colWidth);// 设置表头

		rt.setBody(new Table(1, 1));// 表身

		rt.body.setCellValue(1, 1, getImgServletPath("Bizsfdbt" + num
				+ ".jpg"));// 表中的图

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		rsl.close();
		con.Close();
		return rt.getAllPagesHtml();
	}

    // 比重发热量对比图
	private JFreeChart createChart() {
		// create the first dataset...
		DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();

		// now create the second dataset and renderer...
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select sj.day, nvl(qnet_ar, 0) as frl, nvl(proportion, 0) as biz\n" +
				"  from (select to_char(caiysj, 'dd') as day, qnet_ar, proportion\n" + 
				"          from meiybztjb\n" + 
				"         where diancxxb_id = "+getTreeid()+"\n" + 
				"           and meikxxb_id = "+getMeikxxb_id()+"\n" + 
				"           and to_char(caiysj, 'yyyy') = '"+getNianf()+"'\n" + 
				"           and to_char(caiysj, 'mm') = '"+getYuef()+"') tj,\n" + 
				"       (select to_char(s_date, 'dd') as day\n" + 
				"          from (select to_date('"+getNianf()+getYuef()+"', 'yyyymm') + (rownum - 1) s_date\n" + 
				"                  from dual\n" + 
				"                connect by rownum <= last_day(to_date('"+getNianf()+getYuef()+"', 'yyyymm')) -\n" + 
				"                           to_date('"+getNianf()+getYuef()+"', 'yyyymm') + 1)) sj\n" + 
				" where tj.day(+) = sj.day\n" + 
				" order by sj.day");
		ResultSet rs = con.getResultSet(sql);

		try {
			while (rs.next()) {
				if(rs.getDouble(3) == 0){
					dataset1.addValue(null, "比重",
							(rs.getString(1) == null ? "" : rs.getString(1)));
				}else{
					dataset1.addValue(rs.getDouble(3), "比重",
							(rs.getString(1) == null ? "" : rs.getString(1)));
				}
				if(rs.getDouble(2) == 0){
					dataset2.addValue(null, "低位发热量(K/g)",
							(rs.getString(1) == null ? "" : rs.getString(1)));
				}else{
					dataset2.addValue(rs.getDouble(2), "低位发热量(K/g)",
							(rs.getString(1) == null ? "" : rs.getString(1)));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// create the first renderer...
		CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();// 也可以通过这样设置柱上的数字可见

		CategoryPlot plot = new CategoryPlot();	
		
		CategoryItemRenderer renderer1 = new LineAndShapeRenderer();
		renderer1.setItemLabelGenerator(generator);
		renderer1.setItemLabelsVisible(true);
		plot.setDataset(dataset1);
		plot.setRenderer(renderer1);
		
		NumberAxis Number = new NumberAxis("");
		Number.setAutoTickUnitSelection(false);
		Number.setTickUnit(new NumberTickUnit(0.05));
		Number.setRangeWithMargins(1.4, 1.8);
		 
		plot.setDomainAxis(new CategoryAxis(""));// 设置x axis
//		plot.setRangeAxis(new NumberAxis(""));// 设置y axis
		plot.setRangeAxis(Number);// 设置y axis
		
		CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
		renderer2.setItemLabelGenerator(generator);
		renderer2.setItemLabelsVisible(true);
		plot.setDataset(1, dataset2);
		plot.setRenderer(1, renderer2);
		plot.mapDatasetToRangeAxis(1, 1);// 设置右边的纵轴为1的轴

		// create the third dataset and renderer...
//		ValueAxis rangeAxis2 = new NumberAxis("");
		NumberAxis rangeAxis2 = new NumberAxis("");
		rangeAxis2.setAutoTickUnitSelection(false);
		rangeAxis2.setTickUnit(new NumberTickUnit(500));
		rangeAxis2.setRangeWithMargins(3000, 6500);
		plot.setRangeAxis(1, rangeAxis2);

		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.setRangeGridlinesVisible(true);
		plot.setDomainGridlinesVisible(true);
		
		plot.getDomainAxis().setCategoryLabelPositions(
				CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
		plot.setNoDataMessage("在您设置的条件中没有相关数据!");
		JFreeChart chart = new JFreeChart(plot);
		chart.setTitle(" ");
		return chart;
	}

    // 比重灰分对比图
	private JFreeChart createChart1() {
        // create the first dataset...
		DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();

		// now create the second dataset and renderer...
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select sj.day, nvl(ad, 0) as huif, nvl(proportion, 0) as biz\n" +
				"  from (select to_char(caiysj, 'dd') as day, ad, proportion\n" + 
				"          from meiybztjb\n" + 
				"         where diancxxb_id = "+getTreeid()+"\n" + 
				"           and meikxxb_id = "+getMeikxxb_id()+"\n" + 
				"           and to_char(caiysj, 'yyyy') = '"+getNianf()+"'\n" + 
				"           and to_char(caiysj, 'mm') = '"+getYuef()+"') tj,\n" + 
				"       (select to_char(s_date, 'dd') as day\n" + 
				"          from (select to_date('"+getNianf()+getYuef()+"', 'yyyymm') + (rownum - 1) s_date\n" + 
				"                  from dual\n" + 
				"                connect by rownum <= last_day(to_date('"+getNianf()+getYuef()+"', 'yyyymm')) -\n" + 
				"                           to_date('"+getNianf()+getYuef()+"', 'yyyymm') + 1)) sj\n" + 
				" where tj.day(+) = sj.day\n" + 
				" order by sj.day");
		ResultSet rs = con.getResultSet(sql);

		try {
			while (rs.next()) {
				if (rs.getDouble(3) == 0) {
					dataset1.addValue(null, "比重",
							(rs.getString(1) == null ? "" : rs.getString(1)));
				} else {
					dataset1.addValue(rs.getDouble(3), "比重",
							(rs.getString(1) == null ? "" : rs.getString(1)));
				}
				if (rs.getDouble(2) == 0) {
					dataset2.addValue(null, "灰分",
							(rs.getString(1) == null ? "" : rs.getString(1)));
				} else {
					dataset2.addValue(rs.getDouble(2), "灰分",
							(rs.getString(1) == null ? "" : rs.getString(1)));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// create the first renderer...
		CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();// 也可以通过这样设置柱上的数字可见

		CategoryPlot plot = new CategoryPlot();	
		
		CategoryItemRenderer renderer1 = new LineAndShapeRenderer();
		renderer1.setItemLabelGenerator(generator);
		renderer1.setItemLabelsVisible(true);
		plot.setDataset(dataset1);
		plot.setRenderer(renderer1);
		
		NumberAxis Number = new NumberAxis("");
		Number.setAutoTickUnitSelection(false);
		Number.setTickUnit(new NumberTickUnit(0.05));
		Number.setRangeWithMargins(1.4, 1.8);
		
		plot.setDomainAxis(new CategoryAxis(""));// 设置x axis
		plot.setRangeAxis(Number);// 设置y axis
		
		CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
		renderer2.setItemLabelGenerator(generator);
		renderer2.setItemLabelsVisible(true);
		plot.setDataset(1, dataset2);
		plot.setRenderer(1, renderer2);
		plot.mapDatasetToRangeAxis(1, 1);// 设置右边的纵轴为1的轴

		// create the third dataset and renderer...
		NumberAxis rangeAxis2 = new NumberAxis("");
		rangeAxis2.setAutoTickUnitSelection(false);
		rangeAxis2.setTickUnit(new NumberTickUnit(5));
		rangeAxis2.setRangeWithMargins(20, 50);
		plot.setRangeAxis(1, rangeAxis2);

		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.setRangeGridlinesVisible(true);
		plot.setDomainGridlinesVisible(true);
		
		plot.getDomainAxis().setCategoryLabelPositions(
				CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
		plot.setNoDataMessage("在您设置的条件中没有相关数据!");
		JFreeChart chart = new JFreeChart(plot);
		chart.setTitle(" ");
		return chart;
	}

    // 比重水分对比图
	private JFreeChart createChart2() {
        // create the first dataset...
		DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();

		// now create the second dataset and renderer...
		DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select sj.day, nvl(mt, 0) as mt, nvl(proportion, 0) as biz\n" +
				"  from (select to_char(caiysj, 'dd') as day, mt, proportion\n" + 
				"          from meiybztjb\n" + 
				"         where diancxxb_id = "+getTreeid()+"\n" + 
				"           and meikxxb_id = "+getMeikxxb_id()+"\n" + 
				"           and to_char(caiysj, 'yyyy') = '"+getNianf()+"'\n" + 
				"           and to_char(caiysj, 'mm') = '"+getYuef()+"') tj,\n" + 
				"       (select to_char(s_date, 'dd') as day\n" + 
				"          from (select to_date('"+getNianf()+getYuef()+"', 'yyyymm') + (rownum - 1) s_date\n" + 
				"                  from dual\n" + 
				"                connect by rownum <= last_day(to_date('"+getNianf()+getYuef()+"', 'yyyymm')) -\n" + 
				"                           to_date('"+getNianf()+getYuef()+"', 'yyyymm') + 1)) sj\n" + 
				" where tj.day(+) = sj.day\n" + 
				" order by sj.day");
		ResultSet rs = con.getResultSet(sql);

		try {
			while (rs.next()) {
				if (rs.getDouble(3) == 0) {
					dataset1.addValue(null, "比重",
							(rs.getString(1) == null ? "" : rs.getString(1)));
				} else {
					dataset1.addValue(rs.getDouble(3), "比重",
							(rs.getString(1) == null ? "" : rs.getString(1)));
				}
				if (rs.getDouble(2) == 0) {
					dataset2.addValue(null, "水分",
							(rs.getString(1) == null ? "" : rs.getString(1)));
				} else {
					dataset2.addValue(rs.getDouble(2), "水分",
							(rs.getString(1) == null ? "" : rs.getString(1)));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// create the first renderer...
		CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();// 也可以通过这样设置柱上的数字可见

		CategoryPlot plot = new CategoryPlot();	
		
		CategoryItemRenderer renderer1 = new LineAndShapeRenderer();
		renderer1.setItemLabelGenerator(generator);
		renderer1.setItemLabelsVisible(true);
		plot.setDataset(dataset1);
		plot.setRenderer(renderer1);
		
		NumberAxis Number = new NumberAxis("");
		Number.setAutoTickUnitSelection(false);
		Number.setTickUnit(new NumberTickUnit(0.05));
		Number.setRangeWithMargins(1.4, 1.8);
		
		plot.setDomainAxis(new CategoryAxis(""));// 设置x axis
		plot.setRangeAxis(Number);// 设置y axis
		
		CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
		renderer2.setItemLabelGenerator(generator);
		renderer2.setItemLabelsVisible(true);
		plot.setDataset(1, dataset2);
		plot.setRenderer(1, renderer2);
		plot.mapDatasetToRangeAxis(1, 1);// 设置右边的纵轴为1的轴

		// create the third dataset and renderer...
		NumberAxis rangeAxis2 = new NumberAxis("");
		rangeAxis2.setAutoTickUnitSelection(false);
		rangeAxis2.setTickUnit(new NumberTickUnit(5));
		rangeAxis2.setRangeWithMargins(0, 30);
		plot.setRangeAxis(1, rangeAxis2);

		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.setRangeGridlinesVisible(true);
		plot.setDomainGridlinesVisible(true);
		
		plot.getDomainAxis().setCategoryLabelPositions(
				CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
		plot.setNoDataMessage("在您设置的条件中没有相关数据!");
		JFreeChart chart = new JFreeChart(plot);
		chart.setTitle(" ");
		return chart;
	}

	public String CreateChartFile(JFreeChart chart, String filebassname) {
		try {
			Visit visit = (Visit) getPage().getVisit();
			String FilePath = MainGlobal.getServletPath(getPage())
					+ "/img/BizdbPicture" + visit.getRenyID();
			File dir = new File(FilePath.replaceAll("\\\\", "/"));
			if (dir.exists()) {
				deleteFile(dir);// 册除文件及文件夹下所在的文件
				dir.mkdir();
			} else {
				dir.mkdirs();
			}
			File chartFile = new File(dir, filebassname + ".jpg");
			ChartUtilities.saveChartAsJPEG(chartFile, chart, 790, 400);
			return getImgServletPath(chartFile.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	// 工具栏使用的方法
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
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
			
			visit.setString3(reportType);
			num = 0;
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());

			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			setMeikModel(null);
			setMeikValue(null);
			
			visit.setDefaultTree(null);
			setTreeid(null);
						
		}
		getSelectData();
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
