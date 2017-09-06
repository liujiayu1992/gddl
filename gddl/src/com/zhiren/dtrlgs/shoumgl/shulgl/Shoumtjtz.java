package com.zhiren.dtrlgs.shoumgl.shulgl;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;

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


public class Shoumtjtz  extends BasePage implements PageValidateListener{

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
	
	
	//绑定供应商下拉框
	private IDropDownBean _Gongys;
	public IDropDownBean getGongysValue() {
		if (_Gongys == null) {
			_Gongys = (IDropDownBean) getGongysModel().getOption(0);
		}
		return _Gongys;
	}
	public void setGongysValue(IDropDownBean Value) {
		_Gongys = Value;
	}

	private IPropertySelectionModel _GongysModel;
	public void setGongysModel(IPropertySelectionModel value) {
		_GongysModel = value;
	}
	public IPropertySelectionModel getGongysModel() {
		if (_GongysModel == null) {
			getGongysModels();
		}
		return _GongysModel;
	}

	public void getGongysModels() {
		String zhuangtaiSql =
			"select 0 xuh, '全部' mingc from dual\n" +
			"union\n" + 
			"select xuh,mingc from vwgongys order by xuh";
		_GongysModel = new IDropDownModel(zhuangtaiSql);

	}
//	绑定业务类型
	private IDropDownBean _Yewlx;
	public IDropDownBean getYewlxValue() {
		if (_Yewlx == null) {
			_Yewlx = (IDropDownBean) getYewlxModel().getOption(0);
		}
		return _Yewlx;
	}
	public void setYewlxValue(IDropDownBean Value) {
		_Yewlx = Value;
	}

	private IPropertySelectionModel _YewlxModel;
	public void setYewlxModel(IPropertySelectionModel value) {
		_YewlxModel = value;
	}
	public IPropertySelectionModel getYewlxModel() {
		if (_YewlxModel == null) {
			getYewlxModels();
		}
		return _YewlxModel;
	}

	public void getYewlxModels() {
		String YewlxSql =
			"select 0 xuh, '全部' mingc from dual\n" +
			"union\n" + 
			"select xuh,mingc from yewlxb where leib=1 or leib=3 order by xuh";
		_YewlxModel = new IDropDownModel(YewlxSql);

	}
	
//	绑定日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
//	绑定日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
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
			visit.setList1(null);
			setGongysValue(null);
			setGongysModel(null);
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
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		String Where="";
		if(getGongysValue().getStrId().equals("0")){
			Where="and f.gongysb_id = g.id\n";
		}else{
			Where+="and f.gongysb_id = g.id and g.id=(select id from vwgongys where mingc='"+getGongysValue().getValue()+"')\n";
			
		}
		if( getYewlxValue().getStrId().equals("0")){
			Where+=" and (y.leib=1 or y.leib=3)\n";
		}else{
			Where+=" and y.leib="+getYewlxValue().toString()+"\n";
		}
		
		String strDiancID = "";
		if(visit.isJTUser()){
			strDiancID = "";
		}else if(visit.isGSUser()){
			strDiancID = " and (dc.id="+visit.getDiancxxb_id()+" or dc.fuid="+visit.getDiancxxb_id()+")";
		}else{
			strDiancID = " and dc.id="+visit.getDiancxxb_id();
		}
		
		StringBuffer strSQL = new StringBuffer();
		String sql ="select decode(grouping(g.mingc),1,'总计',g.mingc) as fahr,\n" +
			"         decode(grouping(g.mingc)+grouping(y.mingc),1,'合计',y.mingc) as yewlx,\n" + 
			"         decode(grouping(y.mingc)+grouping(f.daohrq),1,'小计',to_char(f.daohrq,'yyyy-MM-dd')) as daohrq,\n" + 
			"         to_char(f.fahrq,'yyyy-MM-dd') as fahrq,\n" + 
			"         h.hetbh as hetbh,c.mingc as faz,f.chec,p.mingc as pinz,\n" + 
			"         sum(round_new(f.ches,0)) as ches,\n" + 
			"         sum(round_new(f.biaoz,0)) as biaoz,\n" + 
			"         sum(round_new(f.maoz,0)) as maoz,\n" + 
			"         sum(round_new(f.piz,0)) as piz,\n" + 
			"         sum(round_new(f.jingz,0)) as jingz,\n" + 
			"         sum(round_new(f.yingk,0)) as yingk,\n" + 
			"         sum(round_new(f.yuns,0)) as yuns,\n" + 
			"         getduow(f.id) as duow,\n" + 
			"         f.beiz\n" + 
			"from fahb f ,vwchez c,vwpinz p,vwgongys g ,yewlxb y,hetb h,diancxxb dc \n" + 
			"where f.faz_id = c.id\n" + 
			"      and f.hetb_id=h.id(+)\n" + 
			"      and f.pinzb_id = p.id\n" + 
			Where+
			"      and  f.yewlxb_id=y.id\n" + 
			"      and f.daohrq>="+DateUtil.FormatOracleDate(getBRiq())+"\n" + 
			"      and f.daohrq<"+DateUtil.FormatOracleDate(getERiq())+"+1\n" + 
			"      and f.diancxxb_id = dc.id "+strDiancID+"\n" + 
			"		group by rollup (g.mingc, y.mingc ,f.daohrq,f.fahrq,h.hetbh,c.mingc,f.chec,p.mingc,f.guohsj,getrudsj(f.id),getduow(f.id),f.beiz)"+ 
			"		having grouping(g.mingc)=1 or grouping(g.mingc)+grouping(y.mingc)=1 or grouping(y.mingc)+grouping(f.daohrq)=1 or grouping(f.beiz)=0"+ 
      		"		order by grouping(g.mingc) desc,g.mingc,grouping(y.mingc) desc,min(y.xuh),grouping(f.daohrq) desc,daohrq";

		strSQL.append(sql);
//		System.out.println(getYewlxValue().getValue().toString());
		 String ArrHeader[][]=new String[1][17];
		 ArrHeader[0]=new String[] {Local.gongysb_id_fahb,"业务类型",Local.daohrq_id_fahb,Local.fahrq,"合同编号",Local.faz,Local.chec,Local.pinz,Local.ches,Local.biaoz,Local.maoz,Local.piz,Local.jingz,Local.yingk,Local.yuns,Local.duow,Local.beiz};

		 int ArrWidth[]=new int[] {120,60,70,70,80,50,50,50,40,50,50,50,50,40,40,80,70};
		
		ResultSet rs = cn.getResultSet(strSQL.toString());

		// 数据
		
		Table tb=new Table(rs, 1, 0, 1);
		rt.setBody(tb);
		
		rt.setTitle("收煤统计台帐", ArrWidth);
		rt.setDefaultTitle(1, 4, "制表单位: "+visit.getDiancqc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 7, "到货日期："+getBRiq() + " 至 " + getERiq(), Table.ALIGN_CENTER);
		rt.body.mergeFixedCol(1);
		rt.body.mergeFixedCol(2);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(18);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.setColAlign(1, Table.ALIGN_LEFT);
		rt.body.setColAlign(2, Table.ALIGN_LEFT);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_LEFT);
		rt.body.setColAlign(6, Table.ALIGN_LEFT);
		rt.body.setColAlign(7, Table.ALIGN_LEFT);
		rt.body.setColAlign(8, Table.ALIGN_LEFT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		rt.body.setColAlign(14, Table.ALIGN_RIGHT);
		rt.body.setColAlign(15, Table.ALIGN_RIGHT);
		rt.body.setColAlign(16, Table.ALIGN_LEFT);
		rt.body.setColAlign(17, Table.ALIGN_CENTER);
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 4, "打印日期：" + DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(16,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT);
		
		
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
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("到货日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("xiemrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
		dfe.setId("xiemrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("供应商"));
		ComboBox comb = new ComboBox();
		comb.setTransform("GONGYS");
		comb.setEditable(false);
		comb.setLazyRender(true);// 动态绑定
		comb.setWidth(100);
		comb.setReadOnly(true);
		comb.setId("Gongysmc");
		tb1.addField(comb);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("业务类型"));
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("YEWLX");
		comb1.setEditable(false);
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(70);
		comb1.setReadOnly(true);
		comb1.setId("Yewlxmc");
		tb1.addField(comb1);
		
		ToolbarButton tb = new ToolbarButton(null,"查询","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Print);
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


}