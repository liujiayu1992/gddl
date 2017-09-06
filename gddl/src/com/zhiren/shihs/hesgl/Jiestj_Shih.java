package com.zhiren.shihs.hesgl;

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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;

import org.apache.tapestry.contrib.palette.SortMode;


public class Jiestj_Shih  extends BasePage implements PageValidateListener{

	public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}
	
	boolean riqchange = false;

	private String riq;

	public String getBeginriqDateSelect() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
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
//	private String OraDate(Date _date){
//		if (_date == null) {
//			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
//		}
//		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
//	}
	
	
//	//开始日期v
//	private Date _BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
//	private boolean _BeginriqChange=false;
//	public Date getBeginriqDate() {
//		if (_BeginriqValue==null){
//			_BeginriqValue =DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
//		}
//		return _BeginriqValue;
//	}
//	
//	public void setBeginriqDate(Date _value) {
//		if (FormatDate(_BeginriqValue).equals(FormatDate(_value))) {
//			_BeginriqChange=false;
//		} else {
//			_BeginriqValue = _value;
//			_BeginriqChange=true;
//		}
//	}
	
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
		}
	}



//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
//			_BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
			visit.setList1(null);
			setGongysValue(null);
			setGongysModel(null);
			getGongysModels();
			this.getSelectData();
		}
		isBegin=true;
		getToolBars();
		
	}

	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
	
		return getSelectData();
	
	}

	private boolean isBegin=false;

	/**
	 * 发电集团电煤信息日报表
	 * @author xzy
	 */
	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
//		String riq=OraDate(_BeginriqValue);//当前日期
		String riq_sql = " and js.jiesrq >= to_date('" + getBeginriqDateSelect() + "','yyyy-MM-dd') \n"
						+ " and js.jiesrq <= to_date('" + getEndriqDateSelect() + "','yyyy-MM-dd') \n";
		String gys_sql = "";
		if(getGongysValue().getValue()=="全部"){
			gys_sql = " ";
		}else{
			gys_sql = "and gys.id =" + getGongysValue().getId() + " \n";
		}
		
		StringBuffer strSQL = new StringBuffer();
		String sql = 

			"select decode(grouping(gys.mingc)+grouping(js.jiesrq),2,'总计',1,'小计',gys.mingc) as mingc,\n" +
			"       to_char(js.jiesrq,'yyyy/MM/dd') as jiesrq,\n" + 
			"       sum(js.jiessl) as jiessl,\n" + 
			"       sum(js.ches) as ches,\n" + 
			"       decode(gys.mingc,null,'',decode(sum(c.jiessl),0,0,sum(c.jiessl*c.jies)/sum(c.jiessl))) as caco3,\n" + 
			"       decode(gys.mingc,null,'',decode(sum(x.jiessl),0,0,sum(x.jiessl*x.jies)/sum(x.jiessl))) as xid,\n" + 
			"       decode(gys.mingc,null,'',decode(sum(m.jiessl),0,0,sum(m.jiessl*m.jies)/sum(m.jiessl))) as mgco3,\n" + 
			"       decode(gys.mingc,null,'',decode(sum(js.jiessl),0,0,sum(js.jiessl*js.hansdj)/sum(js.jiessl))) as hansdj,\n" + 
			"       decode(gys.mingc,null,'',decode(sum(js.jiessl),0,0,sum(js.jiessl*js.buhsdj)/sum(js.jiessl))) as buhsdj,\n" + 
			"       sum(js.hansje) as hansje,\n" + 
			"       sum(js.buhsje) as buhsje\n" + 
			"from shihgysb gys,shihjsb js,zhibb z,shihjszbsjb zb,\n" + 
			"     (select z.id,z.bianm,zb.jies,js.jiessl\n" + 
			"     from shihjszbsjb zb,zhibb z,shihjsb js\n" + 
			"     where zb.zhibb_id = z.id\n" + 
			"           and zb.jiesdid = js.id\n" + 
			riq_sql +
			"           and z.bianm = 'CaO') c,\n" + 
			"     (select z.id,z.bianm,zb.jies,js.jiessl\n" + 
			"     from shihjszbsjb zb,zhibb z,shihjsb js\n" + 
			"     where zb.zhibb_id = z.id\n" + 
			"           and zb.jiesdid = js.id\n" + 
			riq_sql +
			"           and z.bianm = 'MgO') m,\n" + 
			"     (select z.id,z.bianm,zb.jies,js.jiessl\n" + 
			"     from shihjszbsjb zb,zhibb z,shihjsb js\n" + 
			"     where zb.zhibb_id = z.id\n" + 
			"           and zb.jiesdid = js.id\n" + 
			riq_sql +
			"           and z.bianm = '细度') x\n" + 
			"where gys.mingc = js.gongysmc\n" + 
			"      and zb.jiesdid = js.id\n" + 
			"      and zb.zhibb_id = z.id\n" + 
			riq_sql +
			gys_sql +
			"group by rollup (gys.mingc,js.jiesrq)\n" + 
			"having not (grouping(js.jiesrq)=0 and grouping(gys.mingc)=1)\n" + 
			"order by grouping(gys.mingc) desc,gys.mingc desc,grouping(js.jiesrq) desc,js.jiesrq";

		strSQL.append(sql);
			
		 String ArrHeader[][]=new String[1][11];
		 ArrHeader[0]=new String[] {"供应商","结算日期","结算数量<br>（吨）","车数<br>（车）","CaO<br>（%）","细度<br>（%）","MgO<br>（%）","含税单价<br>（元/吨）","不含税单价<br>（元/吨）","结算含税总金额<br>（元）","结算不含税总金额<br>（元）"};
		 //ArrHeader[1]=new String[] {"供应商","结算日期","结算数量<br>（吨）","车数<br>（车）","CaO<br>（%）","细度（%）","MgO（%）","含税单价（元/吨）","不含税单价（元/吨）","结算含税总金额（元）","结算不含税总金额（元）"};
		 
		 int ArrWidth[]=new int[] {120,100,58,58,58,58,58,58,90,90,100};

		
		ResultSet rs = cn.getResultSet(strSQL.toString());

		// 数据
		
		Table tb=new Table(rs, 1, 0, 1);
		rt.setBody(tb);
		
		rt.setTitle("石灰石结算统计", ArrWidth);
//		rt.setDefaultTitle(1, 2, "单位：美元/吨", Table.ALIGN_LEFT);
		rt.body.setRowHeight(1, 40);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.mergeFixedRow();
			
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rt.getAllPagesHtml();
//		
	}
//	******************************************************************************
	
	
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


	
//	private String FormatDate(Date _date) {
//		if (_date == null) {
//			return "";
//		}
//		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
//	}
	
//	***************************报表初始设置***************************//
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
		
	public Date getYesterday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE,-1);
		return cal.getTime();
	}
	
	public Date getMonthFirstday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
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
		
		tb1.addText(new ToolbarText("结算日期:"));
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
		gys.setWidth(100);
		gys.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(gys);
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	
//	 供应商下拉框
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
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean5()){
			
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

		
			String sql ="select id,mingc from shihgysb";
			
			((Visit) getPage().getVisit())
			.setProSelectionModel5(new IDropDownModel(sql, "全部"));
	return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}
//	private boolean falg1 = false;
//
//	private IDropDownBean GongysValue;
//
//	public IDropDownBean getGongysValue() {
//		if (GongysValue == null) {
//			GongysValue = (IDropDownBean) GongysModel.getOption(0);
//		}
//		return GongysValue;
//	}
//
//	public void setGongysValue(IDropDownBean Value) {
//		if (!(GongysValue == Value)) {
//			GongysValue = Value;
//			falg1 = true;
//		}
//	}
//
//	private IPropertySelectionModel GongysModel;
//
//	public void setGongysModel(IPropertySelectionModel value) {
//		GongysModel = value;
//	}
//
//	public IPropertySelectionModel getGongysModel() {
//		if (GongysModel == null) { 
//			getGongysModels();
//		}
//		return GongysModel;
//	}
//
//	public IPropertySelectionModel getGongysModels() {
//		String sql = "select id,mingc from shihgysb";
//		GongysModel = new IDropDownModel(sql);
//		return GongysModel;
//	}
	
	
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