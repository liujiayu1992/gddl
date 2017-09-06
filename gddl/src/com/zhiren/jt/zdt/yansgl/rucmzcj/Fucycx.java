package com.zhiren.jt.zdt.yansgl.rucmzcj;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
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
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.Locale;

public class Fucycx extends BasePage implements PageValidateListener {

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
//	页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
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
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setRiq1(DateUtil.FormatDate(new Date()));
			setRiq2(DateUtil.FormatDate(new Date()));
			visit.setList1(null);
			
			visit.setProSelectionModel1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel12(null);
			visit.setDropDownBean12(null);
			
			
			getFazModels();
			setFazValue(null);
			getFazModel();
			
			getMeikModels();
			setMeikValue(null);
			getMeikModel();
			
			
			
			getFucy();
		}
		isBegin = true;
		getToolBars();
		
	}
	private boolean isBegin = false;
	
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
	
		return getFucy();
	
	}
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}



	
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	private boolean riqchange1 = false; //开始日期是否变化
//	绑定日期
	private String riq1;

	public String getRiq1() {
		return riq1;
	}

	public void setRiq1(String riq1) {
		if(this.riq1 != null){
			if(!this.riq1.equals(riq1))
				riqchange1 = true;
		}
		this.riq1 = riq1;
	}
	
	
	private boolean riqchange2 = false;//结束日期是否变化
	
	private String riq2;
	public String getRiq2(){
		return riq2;
	}
	public void setRiq2(String riq2){
		if(this.riq2 != null){
			if(!this.riq2.equals(riq2))
				riqchange2 = true;
		}
		this.riq2 = riq2;
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
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	public String getFucy(){
		
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		
//		得到年、月、日
		
		String[] a = getRiq1().split("-");
		String[] b = getRiq2().split("-");
		String title_bt = b[0]+"年"+b[1]+"月份("+a[1]+"月"+a[2]+"-"+b[1]+"月"+b[2]+"日)";
		
		StringBuffer strSQL = new StringBuffer();
		String lx = "";
		String fs = "";
		String gongys = "";
		
		String briq = this.getRiq1();
		String eriq = this.getRiq2();
//		煤矿下拉框条件
		if(getMeikValue().getValue().equals("全部")){
			lx = " ";
		}else{
			lx = " and m.id=  '" + getMeikValue().getId() + "'\n";
		}
		//发站下拉框条件
		if(getFazValue().getValue().equals("全部")){
			fs = " ";
		}else{
			fs = "and c.id=  '" + getFazValue().getId() + "'\n";
		}
		
		String diancxxb_id = Long.toString(v.getDiancxxb_id());

		String sql = "select  m.mingc,c.mingc,f.yuanid ,to_char(f.daohrq,'yyyy-mm-dd'),zi.huaylb ,decode(zi.shifsy, 0,'未采用',1,'采用') shifsy,zh.bianm,zi.qnet_ar,zi.mt,zi.mad,zi.vdaf,zi.aad,zi.stad from fahb f,meikxxb m,chezxxb c,zhuanmb zh,zhuanmlb zl,zhillsb zi\n" +
			"        where f.diancxxb_id = "+ diancxxb_id +" and f.meikxxb_id=m.id and f.faz_id=c.id and zi.zhilb_id=f.zhilb_id\n" + 
			"        and zh.zhillsb_id=zi.id and zh.zhuanmlb_id = zl.id and zi.huaylb in('正常样','复化样') and\n" + 
			"        zl.jib=3 and c.leib='车站' and  (select count(fa.id)\n" + 
			"                     from fahb fa,zhillsb z where z.zhilb_id=fa.zhilb_id and fa.yuanid = f.yuanid) >1\n" + 
			"         and f.daohrq>=to_date('"+ briq +"','yyyy-mm-dd') and f.daohrq<=to_date('"+ eriq +"','yyyy-mm-dd') \n" +
			lx + fs+"  order by f.daohrq,f.yuanid,zi.huaylb desc ";
			
		ResultSetList rs = con.getResultSetList(sql);
		Report rt = new Report();
		String[][] ArrHeader = new String[1][13];
		ArrHeader[0] = new String[]{"煤矿","发站","进厂批次号","过衡时间","样品类别","结算采用","化验编码","Qnet,ar","Mt","Mad","Vdaf","Aad","Stad"};
		int[] ArrWidth = new int[13];
		ArrWidth = new int[] { 100, 60, 70, 80, 80, 50,60,50,50,50, 50, 50, 50 };
		Table tb = new Table(rs,1,0,1);
		rt.setBody(tb);
		tb.setColFormat(9,"0.000");
		tb.setColFormat(10,"0.000");
		tb.setColFormat(11,"0.000");
		tb.setColFormat(8,"0.000");
		tb.setColFormat(12,"0.000");
		tb.setColFormat(13,"0.000");
		rt.setTitle("复化样查询",ArrWidth);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(25);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_CENTER);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.body.mergeCol(2);
		rt.body.mergeCol(3);
		rt.body.mergeCol(4);
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		con.Close();
		return rt.getAllPagesHtml();

	}
	public void getToolBars() {
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("过衡时间"));
		DateField df = new DateField();
		df.setValue(getRiq1());
		df.Binding("Riq1", "Form0");// 与html页中的id绑定,并自动刷新
		df.setId("riq1");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		DateField df1 = new DateField();
		df1.setValue(getRiq2());
		df1.Binding("Riq2", "Form0");//
		df1.setId("riq2");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		ComboBox meik = new ComboBox();
		meik.setTransform("MeikSelect");
		meik.setWidth(130);
		meik.setListeners("select:function(own,rec,index){Ext.getDom('MeikSelect').selectedIndex=index}");
		tb1.addField(meik);
		tb1.addText(new ToolbarText("-"));
		ComboBox faz = new ComboBox();
		faz.setTransform("FazSelect");
		faz.setWidth(130);
		faz.setListeners("select:function(own,rec,index){Ext.getDom('FazSelect').selectedIndex=index}");
		tb1.addField(faz);
		tb1.addText(new ToolbarText("-"));
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
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
	public boolean getRaw() {
		return true;
	}
//	MSG
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
//	煤矿下拉框
	public IDropDownBean getMeikValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null) {
			if(getMeikModel().getOptionCount()>0) {
				setMeikValue((IDropDownBean)getMeikModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	public void setMeikValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(value);
	}
	
	public IPropertySelectionModel getMeikModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1()==null) {
			getMeikModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
	}
	public void setMeikModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel1(value);
	}

	
	public IPropertySelectionModel getMeikModels() {

		//StringBuffer sb = new StringBuffer();
		String sql = "select id ,mingc from meikxxb order by mingc";
		//sb.append("select id,to_char(guohsj,'hh24:mi:ss') guohsj from fahb where to_char(guohsj,'yyyy-mm-dd') = '").append(getRiq()).append("'");
		((Visit)this.getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql,"全部"));
		
		return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
	}
//发站下拉框
	public IPropertySelectionModel getFazModel(){
		if(((Visit)this.getPage().getVisit()).getProSelectionModel12()==null){
			getFazModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel12();
	}
	public IPropertySelectionModel getFazModels(){
		String sql = " select id ,mingc from chezxxb where leib='车站' order by mingc";
		
		((Visit)getPage().getVisit()).setProSelectionModel12(new IDropDownModel(sql,"全部"));
		
		return ((Visit)this.getPage().getVisit()).getProSelectionModel12();
	}
	public void setFazModel(IPropertySelectionModel value){
		((Visit)this.getPage().getVisit()).setProSelectionModel12(value);
	}
	public IDropDownBean getFazValue(){
		if(((Visit)this.getPage().getVisit()).getDropDownBean12()==null) {
			if(getFazModel().getOptionCount()>0) {
				setFazValue((IDropDownBean)getFazModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean12();
	}
	public void setFazValue(IDropDownBean value){
		((Visit)this.getPage().getVisit()).setDropDownBean12(value);
		
	}
	
}
