package com.zhiren.gangkjy.taiz;

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


public class Kuctz  extends BasePage implements PageValidateListener{

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
			setTreeid(null);
		}
		isBegin=true;
		getToolBars();
		getSelectData();
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
		String str="";
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = " and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = " and dc.id = " + getTreeid() + "";
		}

		StringBuffer strSQL = new StringBuffer();
		/*String sql = 
			"select to_char(d.riq,'yyyy-MM-dd') as riq,\n" +
			"       dw.mingc as duowgs, v.mingc as duow,\n" + 
			"       decode(d.leix,1,d.fahr,null) as fahr,\n" + 
			"       decode(d.leix,1,d.chec,null) as chec,\n" + 
			"       decode(d.leix,1,d.pinz,null) as pinz_rk,\n" + 
			"       decode(d.leix,1,d.biaoz,null) as piaoz,\n" + 
			"       decode(d.leix,-1,d.fahr,null) as chuanm,\n" + 
			"       decode(d.leix,-1,d.chec,null) as hangc,\n" + 
			"       decode(d.leix,-1,d.pinz,null) as pinz_ck,\n" + 
			"       decode(d.leix,1,d.panyk,null) as yingk,\n" + 
			"       decode(d.leix,1,d.ruksl,-1,d.chuksl,null) as jingz,\n" + 
			"       d.kucl as kuc\n" + 
			"     from duowkcb d,meicb v, diancxxb dc, duowgsb dw,fayslb fy\n" + 
			"     where d.meicb_id = v.id and d.diancxxb_id=dc.id and dw.id=v.duowgsb_id \n" + 
			str+
			"      and d.leix in (-1,1)\n" + 
			"      and d.riq>="+DateUtil.FormatOracleDate(getBRiq()) +
			"	   and d.riq<"+DateUtil.FormatOracleDate(getBRiq()) + "+1 \n" +
			"      order by riq,duowgs,duow,shij";*/
		
		    String sql="  select decode(grouping( to_char(d.shij,'yyyy-mm-dd'))+grouping(dw.mingc),0,to_char(d.shij,'yyyy-mm-dd hh24:mi'),2,'汇总',to_char(d.shij,'yyyy-mm-dd')||'汇总') as riq,\n"+
      " dw.mingc as duowgs, v.mingc as duow,\n"+
      " decode(d.leix,1,d.fahr,null) as fahr,\n"+
      " decode(d.leix,1,d.chec,null) as chec,\n"+
      " decode(d.leix,1,d.pinz,null) as pinz_rk,\n"+
      "  sum(nvl(d.biaoz,0)) as piaoz,\n"+
      "  sum(nvl(d.panyk,0)) as yingk,\n"+
      "   sum(nvl(d.ruksl,0)) as jingz,\n"+
      "   decode(d.leix,-1,l.mingc,null) as chuanm,\n"+
      "   decode(d.leix,-1,d.chec,null) as hangc,\n"+
      "           sum(nvl(d.chuksl,0)) as meil,\n"+
      "    d.kucl as kuc\n"+
      "  from duowkcb d,meicb v, diancxxb dc, duowgsb dw,fayslb fy,qumxxb q,luncxxb l\n"+
      "  where d.meicb_id = v.id and d.diancxxb_id=dc.id and dw.id=v.duowgsb_id \n"+
      str+"\n"+  
      "   and d.leix in (-1,1)\n"+
      "   and d.duiqm_id=q.id(+)\n"+
      "   and  fy.id(+)=q.zhuangcb_id\n"+
      "   and l.id(+)=fy.luncxxb_id\n"+
      "   and d.shij>=to_date('"+getBRiq()+"','yyyy-mm-dd')	   and d.shij<to_date('"+getERiq()+"','yyyy-mm-dd')+1 \n"+
      "  group by rollup(to_char(d.shij,'yyyy-mm-dd'),(l.mingc,d.shij,dw.mingc,v.mingc,d.fahr,d.chec,d.pinz,d.biaoz,d.fahr,d.chec,d.pinz,d.panyk,d.leix,d.kucl)) \n"+
      "  order by  grouping( to_char(d.shij,'yyyy-mm-dd'))desc,to_char(d.shij,'yyyy-mm-dd'),grouping(dw.mingc)desc ,to_char(d.shij,'hh24') ,duowgs desc,duow";
		
		
		strSQL.append(sql);
		
		String ArrHeader[][]=new String[2][13];
		ArrHeader[0]=new String[] {Local.riq,"垛位公司",Local.duow,Local.fahr,Local.ruk,Local.ruk,Local.ruk,Local.ruk,Local.ruk,Local.chuk,Local.chuk,Local.chuk,Local.kuc};
		ArrHeader[1]=new String[] {Local.riq,"垛位公司",Local.duow,Local.fahr,Local.chec,Local.pinz,"票重",Local.yingk,Local.jingz,Local.chuanm_zhuangcb,Local.chuanc_duic,Local.meil_jies,Local.kuc};

		int ArrWidth[]=new int[] {120,60,50,70,50,40,50,50,50,50,50,50,65};
          int r=0;
//		for(int i=0;i<ArrWidth.length;i++){
//			r+=ArrWidth[i];
//		}
//		System.out.println(r);
		ResultSet rs = cn.getResultSet(strSQL.toString());

		// 数据
		Table tb=new Table(rs, 2, 0, 2);
		rt.setBody(tb);
		
		rt.setTitle("库存台帐", ArrWidth);
		rt.setDefaultTitle(5, 4, getBRiq() + " 至 " + getERiq(), Table.ALIGN_CENTER);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(22);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRowCol();
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_LEFT);
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.setColAlign(4, Table.ALIGN_LEFT);
		rt.body.setColAlign(5, Table.ALIGN_LEFT);
		rt.body.setColAlign(6, Table.ALIGN_LEFT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_LEFT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		
//		int iStartRow=0;
//		int iEndRow=0;
//		for (int i=rt.body.getFixedRows()+1;i<=rt.body.getRows(); i++){
//			if (rt.body.getCell(i, 3).value.equals("小计") ){
//				if (iStartRow==0){
//					iStartRow=i;
//				}
//			}else{
//				if (iStartRow>0){
//					iEndRow=i-1;
//					rt.body.mergeCell(iStartRow, 3, iEndRow, 3);
//					iStartRow=0;
//					iEndRow=0;
//				}
//			}
//		}
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
		Visit visit = (Visit) this.getPage().getVisit();
		tb1.addText(new ToolbarText("日期:"));
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
		
		tb1.addText(new ToolbarText("单位名称:"));
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		ToolbarButton tb = new ToolbarButton(null,"查询","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Print);
		tb1.addItem(tb);
		setToolbar(tb1);
	}
	
//	添加电厂树
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	电厂名称
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
//	得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
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