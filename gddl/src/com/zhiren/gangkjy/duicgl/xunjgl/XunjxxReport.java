package com.zhiren.gangkjy.duicgl.xunjgl;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
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
import com.zhiren.common.ext.form.DateField;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.gangkjy.Local;

import org.apache.tapestry.contrib.palette.SortMode;


public class XunjxxReport  extends BasePage implements PageValidateListener{

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
	
//	private String OraDate(Date _date){
//		if (_date == null) {
//			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
//		}
//		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
//	}
//	
//	
//	//开始日期v
//	private Date _BeginriqValue = new Date();
//	private boolean _BeginriqChange=false;
//	public Date getBeginriqDate() {
//		if (_BeginriqValue==null){
//			_BeginriqValue =new Date();
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
	
//	private boolean _RefurbishChick = false;
//
//	public void RefurbishButton(IRequestCycle cycle) {
//		_RefurbishChick = true;
//	}
//
//
//	public void submit(IRequestCycle cycle) {
//		if (_RefurbishChick) {
//			_RefurbishChick = false;
//		}
//	}



//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
//			_BeginriqValue = new Date();
//			visit.setList1(null);
			
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
		String riq = "";
		String banc = "";
		String tp = "";
		String xunjr = "";
		String xunjsj = "";
		String beiz = "";
		
		_CurrentPage=1;
		_AllPages=1;
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
//		String riq=OraDate(_BeginriqValue);//当前日期
//		String riq2=OraDate(DateUtil.AddDate(_BeginriqValue, -15, DateUtil.AddType_intDay));
//		StringBuffer strSQL = new StringBuffer();
		String sql = "select to_char(riq,'yyyy-MM-dd') as riq,banc,xunjr,to_char(xunjsj,'yyyy-MM-dd hh24:mi') as xunjsj,beiz " +
				" from xunjb where id = " + v.getString10();
		ResultSet rs = cn.getResultSet(sql);
		try {
			if(rs.next()){
				riq = rs.getString("riq");
				banc = rs.getString("banc");
				xunjr = rs.getString("xunjr");
				xunjsj = rs.getString("xunjsj");
				beiz = rs.getString("beiz");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		tp = "无图片";
//		strSQL.append(sql);
			
		 String ArrHeader[][]=new String[7][4];
		 ArrHeader[0]=new String[] {Local.riq,riq,Local.duowtp,Local.duowtp};
		 ArrHeader[1]=new String[] {Local.banc,banc,tp,tp};
		 ArrHeader[2]=new String[] {Local.xunjr,xunjr,tp,tp};
		 ArrHeader[3]=new String[] {Local.xunjsj,Local.xunjsj,tp,tp};
		 ArrHeader[4]=new String[] {xunjsj,xunjsj,tp,tp};
		 ArrHeader[5]=new String[] {Local.beiz,beiz,beiz,beiz};
		 ArrHeader[6]=new String[] {Local.beiz,beiz,beiz,beiz};

		 int ArrWidth[]=new int[] {100,130,180,180};

		 rt.setTitle("巡检信息查询", ArrWidth);
		 rt.setDefaultTitle(1, 2, "制表单位："+getZhibdwmc(), Table.ALIGN_LEFT);
		 rt.setDefaultTitle(3, 2, "制表时间："+FormatDate(new Date()), Table.ALIGN_LEFT);
		 rt.setBody(new Table(ArrHeader,0,0,0));
		 rt.body.setWidth(ArrWidth);

		 //合并单元格
		 rt.body.mergeCell(1,3,1,4);
		 rt.body.mergeCell(4,1,4,2);
		 rt.body.mergeCell(5,1,5,2);
		 rt.body.mergeCell(6,1,7,1);
		 rt.body.mergeCell(2,3,5,4);
		 rt.body.mergeCell(6,2,7,4);
		 
		 rt.body.setCellAlign(1, 1, Table.ALIGN_CENTER);
		 rt.body.setCellAlign(1, 2, Table.ALIGN_CENTER);
		 rt.body.setCellAlign(2, 1, Table.ALIGN_CENTER);
		 rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		 rt.body.setCellAlign(3, 2, Table.ALIGN_CENTER);
		 rt.body.setCellAlign(1, 3, Table.ALIGN_CENTER);
		 rt.body.setCellAlign(2, 3, Table.ALIGN_CENTER);
		 rt.body.setCellAlign(4, 1, Table.ALIGN_CENTER);
		 rt.body.setCellAlign(5, 1, Table.ALIGN_CENTER);
		 rt.body.setCellAlign(6, 1, Table.ALIGN_CENTER);
		 
		 //设置行高
		 rt.body.setRowHeight(70);
		 rt.body.setRowHeight(6, 150);
		 
		 rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 2, "制表:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 2, "审核:", Table.ALIGN_LEFT);
		
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);
		
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
//		Toolbar tb1 = new Toolbar("tbdiv");
//		
//		tb1.addText(new ToolbarText("日期:"));
//		DateField df = new DateField();
//		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
//		df.Binding("riqDateSelect","forms[0]");// 与html页中的id绑定,并自动刷新
//		df.setWidth(100);
//		//df.setListeners("select:function(){document.Form0.submit();}");
//		tb1.addField(df);
//		tb1.addText(new ToolbarText("-"));
//		
//		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
//		tb1.addItem(tb);
//		
//		setToolbar(tb1);
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