package com.zhiren.gangkjy.duicgl.xunjgl;
/*
 * 作者:杨宏杰
 * 时间:2009-3-26
 * 内容:巡检台帐的查询报表
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
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Xunjtz extends BasePage {

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

	private String REPORT_NAME_RUCMZJYYB = "Rihcdyxxreport";// 入厂煤质检验报表

//	private String REPORT_NAME_RUCMZJYYB_A = "Rucmzjyyb_A";// 入厂煤质检验报表
//
//	private String REPORT_NAME_RUCMZJYYB_B = "Rucmzjyyb_B";// 入厂煤质检验报表

	private String REPORT_NAME_REZC = "rihcdyxx";

	private String mstrReportName = "";

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		// if (mstrReportName.equals(REPORT_NAME_RUCMZJYYB)) {
		return getRihcdyxxreport();
		// } else if (mstrReportName.equals(REPORT_NAME_RUCMZJYYB_A)) {
		// return getRucmzjyyb_A();
		// } else if (mstrReportName.equals(REPORT_NAME_RUCMZJYYB_B)) {
		// return getRucmzjyyb_B();
		// } else {
		// return "无此报表";
		// }
	}

	private String getRihcdyxxreport() {
		Visit visit = (Visit) getPage().getVisit();
		
		JDBCcon con = new JDBCcon();

		String sql=
//			
			"select to_char(x.riq, 'yyyy-mm-dd') as riq,\n" +
			"       x.banc,\n" + 
			"       x.xunjr,\n" + 
			"       to_char(x.xunjsj,'yyyy-mm-dd hh24:mi:ss') as xunjsj,\n" + 
			"       x.xunjtp,\n" + 
			"       x.beiz\n" + 
			"  from xunjb x\n" + 
			
			"     where  x.diancxxb_id="+ visit.getDiancxxb_id()+"\n" + 
			"      and  x.riq >= to_date('"+ getRiq() +"','yyyy-mm-dd')\n" + 
			"      and  x.riq <=  to_date('"+ getAfter() +"','yyyy-mm-dd')\n" + 
			"      order by x.riq";

		
		ResultSet rs = con.getResultSet(sql);
//		System.out.println(sql);

		Report rt = new Report();
		
		String ArrHeader[][]=new String[1][6];
		ArrHeader[0]=new String[] {Local.riq,Local.banc,Local.xunjr,Local.xunjsj,Local.xunjtp,Local.beiz};
		

		int ArrWidth[]=new int[] {100,80,80,160,80,100};

		rt.setTitle("巡 检 台 帐", ArrWidth);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(3, 2, "查询日期:" + getRiq() + "至" + getAfter(),
				Table.ALIGN_CENTER);
		rt.setDefaultTitle(1, 2, "制表单位:"+getZhibdwmc(),Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

//		String[] strFormat = new String[] {  "", "", "", "", "", "",
//				"0.00", "", "", "0.00", "", "", "0.00", "", "",
//				"0.00", "", "" , "0.00","", "", "0.00", ""};

		rt.setBody(new Table(rs, 1, 0, 1));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.mergeFixedCols();
//		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 6; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 5, "打印日期:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
//		rt.setDefautlFooter(7, 2, "主管:", Table.ALIGN_LEFT);
//		rt.setDefautlFooter(11, 3, "审核:", Table.ALIGN_LEFT);
//		rt.setDefautlFooter(15, 4, "制表:", Table.ALIGN_LEFT);
//		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

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

	

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("查询日期:"));
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

//		tb1.addText(new ToolbarText("运输方式:"));
//		ComboBox meik = new ComboBox();
//		meik.setTransform("YUNSFSSelect");
//		meik.setEditable(true);
//		meik.setWidth(100);
//		meik.setListeners("select:function(){document.Form0.submit();}");
//		tb1.addField(meik);

		ToolbarButton tb = new ToolbarButton(null, "查询",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);
		tb.setIcon(SysConstant.Btn_Icon_Print);
		setToolbar(tb1);

	}

	private String getZhibdwmc(){
		Visit visit = (Visit)getPage().getVisit();
		String danwmc = "";
		String sql = "select quanc from diancxxb where id="+visit.getDiancxxb_id();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			danwmc = rsl.getString("quanc");
		}
		return danwmc;
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
		// return "var context='http://"
		// + this.getRequestCycle().getRequestContext().getServerName()
		// + ":"
		// + this.getRequestCycle().getRequestContext().getServerPort()
		// + ((Visit) getPage().getVisit()).get() + "';";
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

	// 运输方式下拉框
//	private boolean falg1 = false;
//
//	private IDropDownBean YunsfsValue;
//
//	public IDropDownBean getYunsfsValue() {
//		if (YunsfsValue == null) {
//			YunsfsValue = (IDropDownBean) YunsfsModel.getOption(0);
//		}
//		return YunsfsValue;
//	}
//
//	public void setYunsfsValue(IDropDownBean Value) {
//		if (!(YunsfsValue == Value)) {
//			YunsfsValue = Value;
//			falg1 = true;
//		}
//	}
//
//	private IPropertySelectionModel YunsfsModel;
//
//	public void setYunsfsModel(IPropertySelectionModel value) {
//		YunsfsModel = value;
//	}
//
//	public IPropertySelectionModel getYunsfsModel() {
//		if (YunsfsModel == null) {
//			getYunsfsModels();
//		}
//		return YunsfsModel;
//	}
//
//	public IPropertySelectionModel getYunsfsModels() {
//		String sql = "select id,mingc from yunsfsb";
//		YunsfsModel = new IDropDownModel(sql);
//		return YunsfsModel;
//	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
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

			}
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName = visit.getString1();
		} else {
			if (visit.getString1().equals("")) {
				mstrReportName = visit.getString1();
			}
		}
		blnIsBegin = true;
		// mstrReportName="diaor04bb";
		getSelectData();

	}

}
