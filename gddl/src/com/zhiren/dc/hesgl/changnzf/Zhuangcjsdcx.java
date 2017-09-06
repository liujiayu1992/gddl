package com.zhiren.dc.hesgl.changnzf;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.gangkjy.Local;

import org.apache.tapestry.contrib.palette.SortMode;

/**
 * @author ly
 * 日期：2010-01-30
 * 描述：装车结算单查询
 */
public class Zhuangcjsdcx  extends BasePage implements PageValidateListener{

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
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
	private boolean isChange = false;
//	绑定日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		if(this.briq != briq){
			isChange = true;
		}
		this.briq = briq;
	}
	
//	绑定日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		if(this.eriq != eriq){
			isChange = true;
		}
		this.eriq = eriq;
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
		}
	}



//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			visit.setString3(null);
			visit.setProSelectionModel2(null);
			visit.setList1(null);
			setJiesValue(null);
			setJiesModel(null);
			this.getSelectData();
		}
		
		if(isChange==true){
			setJiesModels();
			isChange=false;
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

	private String getSelectData(){
		Report rt = new Report();
		JDBCcon con = new JDBCcon();	
		String shoumsj = "";
		String jiesdh = "";
		
		String sql =
			"select to_char(z.riq,'yyyy')||'-'||to_char(z.riq,'MM') as jiesyf,\n" +
			"       z.koud,\n" + 
			"       z.beiz as kuangb,\n" + 
			"       s.mingc as yunsdw,\n" + 
			"       z.danj as jiesdj,\n" + 
			"       z.hej as jiesje,\n" + 
			"       z.ches,\n" + 
			"       z.jiessl as duns,\n" + 
			"       to_char(z.qiszyfw,'yyyy-MM-dd')||'至'||to_char(z.jiezzyfw,'yyyy-MM-dd') as shoumsj,\n" + 
			"       z.bianm as jiesdh\n" + 
			"from zafjsb z,shoukdw s\n" + 
			"where z.shoukdw_id=s.id\n" + 
			"      and z.id='"+getJiesValue().getId()+"'\n";

		ResultSet rs = con.getResultSet(sql);
		String[][] ArrHeader = new String[6][4];
		try {
			if (rs.next()) {
				String jiesyf = rs.getString("jiesyf");
				String koud = rs.getString("koud");
				String kuangb = rs.getString("kuangb");
				String yunsdw = rs.getString("yunsdw");
				String jiesdj = rs.getString("jiesdj");
				String jiesje = rs.getString("jiesje");
				String ches = rs.getString("ches");
				String duns = rs.getString("duns");
					   shoumsj = rs.getString("shoumsj");
					   jiesdh = rs.getString("jiesdh");
				
				ArrHeader[0] = new String[] { "结算月份",jiesyf,"扣吨",koud};

				ArrHeader[1] = new String[] { "矿别", kuangb, "运输单位",yunsdw};
				
				ArrHeader[2] = new String[] { "结算单价",jiesdj,"结算金额",jiesje};
				
				ArrHeader[3] = new String[] { "应结车数",ches,"应结吨数",duns};

				ArrHeader[4] = new String[] { "实结车数",ches,"实结吨数",duns};
				
				ArrHeader[5] = new String[] { "备注","","",""};
				
			} else
				return "";
		} catch (Exception e) {
			System.out.println(e);
		}
		int[] ArrWidth = new int[] { 100, 250,100, 200};

		rt.setTitle("海电 结算验收", ArrWidth);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 20);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setBody(new Table(6, 4));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		
		rt.setDefaultTitle(1, 2,"收煤时间："+shoumsj ,Table.ALIGN_RIGHT);
		rt.setDefaultTitle(3, 2,"结算单号："+jiesdh ,Table.ALIGN_RIGHT);

		rt.body.merge(6, 2, 6, 4);
		rt.body.setFontSize(12);
		rt.body.setRowHeight(43);
		rt.body.setRowHeight(6, 100);
		rt.body.ShowZero = true;

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();	
		return rt.getAllPagesHtml();
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


	
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	
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
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("结算日期："));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "Form0");// 与html页中的id绑定,并自动刷新
		dfb.setId("xiemrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "Form0");// 与html页中的id绑定,并自动刷新
		dfe.setId("xiemrqe");
		tb1.addField(dfe);

		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("结算单号:"));
		ComboBox fh = new ComboBox();
		fh.setTransform("JiesSelect");
		fh.setWidth(130);
		fh.setListeners("select:function(own,rec,index){Ext.getDom('JiesSelect').selectedIndex=index}");
		tb1.addField(fh);
		
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"查询","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
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
	
	
//	结算单号
	public IDropDownBean getJiesValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getJiesModel().getOptionCount()>0) {
				setJiesValue((IDropDownBean)getJiesModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	public void setJiesValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getJiesModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			setJiesModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	public void setJiesModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
	public IPropertySelectionModel setJiesModels() {
		String sql = 
			"select id,bianm from zafjsb\n" +
			"where riq>to_date('"+getBRiq()+"','yyyy-MM-dd')\n" + 
			"      and riq<to_date('"+getERiq()+"','yyyy-MM-dd')\n" + 
			"order by bianm";

		((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql, "全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
	
}