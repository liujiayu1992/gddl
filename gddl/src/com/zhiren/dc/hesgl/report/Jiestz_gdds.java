package com.zhiren.dc.hesgl.report;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;

import org.apache.tapestry.contrib.palette.SortMode;

/*
 * 作者：张立东
 * 时间：2009-10-30
 * 描述：结算台帐中增加按入账日期的查询复选框
 */

/*
 * 作者：  王伟
 * 时间：  2009-10-31
 * 描述：  在硫折价中添加了Star的折价，即硫折价=Stad的折价+Star的折价
 */
public class Jiestz_gdds extends BasePage implements PageValidateListener {

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

	private String check = "false"; 

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	boolean riqchange = false;

	private String riq;

	public String getBeginriqDateSelect() {
		if (riq == null || riq.equals("")) {
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			riq = DateUtil.FormatDate(stra.getTime());
		}
		return riq;
	}

	public void setBeginriqDateSelect(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

	}

	boolean afterchange = false;

	private String after;

	public String getEndriqDateSelect() {
		if (after == null || after.equals("")) {
			after = DateUtil.FormatDate(new Date());
		}
		return after;
	}

	public void setEndriqDateSelect(String after) {

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

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			setGongysValue(null);
			setGongysModel(null);
			getGongysModels();
		}
	}

	// ******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			// _BeginriqValue = DateUtil.AddDate(new Date(), -1,
			// DateUtil.AddType_intDay);
			visit.setList1(null);
			setGongysValue(null);
			setGongysModel(null);
			getGongysModels();
			this.getSelectData();
		}
		isBegin = true;
		getToolBars();

	}

	public String getPrintTable() {
		if (!isBegin) {
			return "";
		}
		isBegin = false;

		return getSelectData();

	}

	private boolean isBegin = false;

	/**
	 * 发电集团电煤信息日报表
	 * 
	 * @author xzy
	 */
	private String getSelectData() {
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		// String riq=OraDate(_BeginriqValue);//当前日期
		String riq_sql = "";
		if (this.getCheck().equals("true")) {
			riq_sql = " and j.ruzrq >= to_date('" + getBeginriqDateSelect()
					+ "','yyyy-MM-dd') \n" + " and j.ruzrq <= to_date('"
					+ getEndriqDateSelect() + "','yyyy-MM-dd') \n";
		} else {
			riq_sql = " and j.jiesrq >= to_date('" + getBeginriqDateSelect()
					+ "','yyyy-MM-dd') \n" + " and j.jiesrq <= to_date('"
					+ getEndriqDateSelect() + "','yyyy-MM-dd') \n";
		}

		String gys_sql = "";
		if (getGongysValue().getValue() == "全部") {
			gys_sql = " ";
		} else {
			gys_sql = "and g.id =" + getGongysValue().getId() + " \n";
		}
		
		String yunfxx = // 运费信息
		  "       sum(nvl(y.guotyf,0) + nvl(y.kuangqyf,0)) as yunf,\n"
		+ "       sum(nvl(y.guotzf,0) + nvl(y.kuangqzf,0)) as zaf,\n"
		+ "       sum(nvl(y.buhsyf,0)) as buhsyf,\n"
		+ "       sum(nvl(y.shuik,0)) as yunfsk,\n"
		+ "       sum(nvl(y.hansyf,0)) as hansyf,\n"
		+ "       sum(nvl(j.hansmk,0) + nvl(y.hansyf,0)) as zongje,\n";
		
		if (MainGlobal.getXitxx_item("数量", "是否重新关联煤款结算单对应的运费信息", 
			String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()), "否").equals("是")) {
//			由于国电青铝电厂的煤款和运费是分开结算的，并且可能一张煤款结算单对应多个运输单位，那么还会有多个运费结算单，
//			这种情况比较特殊，所以需要在此重新关联出运费信息
			yunfxx = 
				"sum(getYunfxx4Jiesbid(j.id, 'guotyf') + getYunfxx4Jiesbid(j.id, 'kuangqyf')) as yunf,\n" +
				"sum(getYunfxx4Jiesbid(j.id, 'guotzf') + getYunfxx4Jiesbid(j.id, 'kuangqzf')) as zaf,\n" + 
				"sum(getYunfxx4Jiesbid(j.id, 'buhsyf')) as buhsyf,\n" + 
				"sum(getYunfxx4Jiesbid(j.id, 'shuik')) as yunfsk,\n" + 
				"sum(getYunfxx4Jiesbid(j.id, 'hansyf')) as hansyf,\n" + 
				"sum(nvl(j.hansmk, 0) + getYunfxx4Jiesbid(j.id, 'hansyf')) as zongje,";
		}

		StringBuffer strSQL = new StringBuffer();
		String sql = "select\n"
				+ "GONGYS,\n"
				+ "JIESRQ,\n"
				+ "RUZRQ,\n"
				
				+"decode(BIANM,null,null,'<a target=_blank href="+MainGlobal.getHomeContext(this)+"/app?service=page/Jiesmx_gdds&bianm='||BIANM||'>'||BIANM||'</a>'),\n" 
				+ "PIAOZ,\n"
				+ "JINGZ,\n"
				+ "JIESSL,\n"
				+ "YANSRL\n"
				+ " from (select decode(grouping(g.mingc),1,'总计',g.mingc) as gongys,\n"
				+ "       decode(grouping(g.mingc)+grouping(j.jiesrq),1,'小计',to_char(j.jiesrq,'yyyy-MM-dd')) as jiesrq,\n"
				+ "       to_char(j.ruzrq,'yyyy-MM-dd') as ruzrq,\n"
				+ "       j.bianm,\n"
				+ "       decode(grouping(g.mingc), null, '', sum(getjiesdzb('jiesb',j.id,'结算数量','gongf'))) as piaoz,\n"
				+ "       decode(grouping(g.mingc),null,'',sum(j.guohl)) as jingz,\n"
				+ "       decode(grouping(g.mingc),null,'',sum(j.jiessl)) as jiessl,\n"
				+ "       round_new(sum(decode(j.jiessl,0,0,getjiesdzb('jiesb',j.id,'Qnetar','changf')*j.jiessl))/sum(decode(j.jiessl,0,1,j.jiessl)),0) as yansrl \n"
				+ " from jiesb j,jiesyfb y,gongysb g\n"
				+ " where y.diancjsmkb_id(+) = j.id\n"
				+ "   and j.gongysb_id = g.id\n"
				+ riq_sql
				+ "\n"
				+ gys_sql
				+ "\n"
				+ " group by rollup(g.mingc,j.jiesrq,j.ruzrq,j.bianm)\n"
				+ " having not (grouping(j.bianm)=1 and grouping(j.jiesrq)=0)\n"
				+ " order by grouping(g.mingc) desc,g.mingc,grouping(j.jiesrq) desc,j.jiesrq)";

		strSQL.append(sql);
		String ArrHeader[][] = new String[1][8];
		ArrHeader[0] = new String[] { "供应商", "结算日期", "入账日期", "结算编号",
				"票重<br>(吨)", "净重<br>(吨)", "结算量<br>(吨)", "验收热量<br>(kcal/kg)"};

		int ArrWidth[] = new int[] { 180, 90, 90, 170, 80, 80, 80, 80,  };

		ResultSet rs = cn.getResultSet(strSQL.toString());

		// 数据
		Table tb = new Table(rs, 1, 0, 1);
		rt.setBody(tb);

		rt.setTitle("结算统计台帐", ArrWidth);
		// rt.setDefaultTitle(1, 2, "单位：美元/吨", Table.ALIGN_LEFT);
		rt.body.setRowHeight(1, 40);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.mergeFixedRowCol();

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
		//		
	}

	// ******************************************************************************

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}

	// ***************************报表初始设置***************************//
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

	public Date getYesterday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	public Date getMonthFirstday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}

	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}

	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getBeginriqDateSelect());
		df.Binding("riq", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("至:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getEndriqDateSelect());
		df1.Binding("after", "");// 与html页中的id绑定,并自动刷新
		df1.setId("after");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("供应商:"));
		ComboBox gys = new ComboBox();
		gys.setTransform("GongysDropDown");
		gys.setEditable(true);
		gys.setWidth(200);
		gys.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(gys);

		Checkbox chk = new Checkbox();
		chk.setId("CHECKED");
		if (this.getCheck().equals("true")) {
			chk.setChecked(true);
		} else {
			chk.setChecked(false);
		}
		chk
				.setListeners("check:function(own,checked){if(checked){document.all.CHECKED.value='true'}else{document.all.CHECKED.value='false'}}");
		tb1.addField(chk);
		tb1.addText(new ToolbarText("入账日期"));

		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.getElementById('RefurbishButton').click();}");
		tb1.addItem(tb);

		setToolbar(tb1);
	}

	// 供应商下拉框
	public boolean _Gongyschange = false;

	public IDropDownBean getGongysValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setGongysValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean5()) {

			((Visit) getPage().getVisit()).setboolean3(true);

		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getGongysModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getGongysModels() {
		JDBCcon con = new JDBCcon();
		List List = new ArrayList();

		String sql = "select g.id,g.mingc from gongysb g,jiesb j \n"
				+ " where j.gongysb_id = g.id \n"
				+ " 	and j.jiesrq >= to_date('" + getBeginriqDateSelect()
				+ "','yyyy-MM-dd') \n" + " 	and j.jiesrq <= to_date('"
				+ getEndriqDateSelect() + "','yyyy-MM-dd') \n";

		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql, "全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
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

}