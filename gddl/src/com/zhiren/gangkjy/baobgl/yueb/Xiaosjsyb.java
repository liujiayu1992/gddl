package com.zhiren.gangkjy.baobgl.yueb;


import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;

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
 * 
 * @author 张琦
 *
 */
public class Xiaosjsyb  extends BasePage implements PageValidateListener{

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
	
	
	// 年份下拉框
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// 月份下拉框
	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_yuef = 12;
			} else {
				_yuef = _yuef - 1;
			}
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			_YuefValue = Value;
		}
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}
	//日期控件结束
	
	
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
			visit.setList1(null);
			setTreeid(null);
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


	

	private String getSelectData(){
		
		_CurrentPage=1;
		_AllPages=1;
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		StringBuffer sb = new StringBuffer();
//		 工具栏的年份和月份下拉框
		long intyear;
		String StrMonth = "";
		long intMonth;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else { 
			intMonth = getYuefValue().getId();
		}
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
			
		} else {
			StrMonth = "" + intMonth;
			
		}
		String date1 = intyear + "-" +StrMonth + "-01";
		String date2 = intyear + "-01-01";

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

		// 设置明细行SQL
		String sqltable = 

			"select decode(grouping(dc.mingc),1,'总计',dc.mingc) as danwmc,\n" +
			"       decode(grouping(dc.mingc)+grouping(g.mingc),1,'合计',g.mingc) as fahr,\n" + 
			"       tb2.fenx as leix,\n" + 
			"       tb.pinz as pinz,\n" + 
			"       sum(tb.jiessl) as meil,\n" + 
			"       decode(sum(tb.jiessl),0,0,round_new(sum(tb.jiessl * tb.jiesrl) / sum(tb.jiessl), 2)) as frl,\n" + 
			"       decode(sum(tb.jieslf),0,0,round_new(sum(tb.jiessl * tb.jieslf) / sum(tb.jiessl), 2)) as luf,\n" + 
			"       sum(tb.hansmk + tb.yhansyf) as hej,\n" + 
			"       sum(tb.hansmk) as hansmk,\n" + 
			"       sum(tb.buhsmk) as buhsmk,\n" + 
			"       sum(tb.yhansyf) as hansyf,\n" + 
			"       sum(tb.ybuhsyf) buhsyf,\n" + 
			"       sum(tb.yshuik) yunfs,\n" + 
			"round_new(sum(decode(tb.jiesrl,0,0,(tb.hansmk + tb.yhansyf)/tb.jiessl * 7000 / tb.jiesrl)), 2) as biaomdj,\n" +
			"round_new(sum(decode(tb.jiesrl,0,0,(tb.buhsmk + tb.ybuhsyf)/tb.jiessl * 7000 / tb.jiesrl)), 2) as buhsbmdj \n"+
//			"       round_new(sum((tb.hansmk + tb.yhansyf) * 29.271 / tb.jiesrl), 2) as biaomdj,\n" + 
//			"       round_new(sum((tb.buhsmk + tb.ybuhsyf) * 29.271 / tb.jiesrl), 2) as buhsbmdj\n" + 
			"from\n" + 
			"(select k.diancxxb_id as diancxxb_id, k.gongysb_id as gongysb_id, k.meiz as pinz,decode(1,1,'本月','本月') as leix,\n" + 
			"       sum(k.jiessl) jiessl, decode(sum(k.jiessl),0,0,round_new(sum(k.jiessl * k.jiesrl) / sum(k.jiessl), 2)) jiesrl,\n" + 
			"       decode(sum(k.jieslf),0,0,round_new(sum(k.jiessl * k.jieslf) / sum(k.jiessl), 2)) as jieslf,\n" + 
			"       sum(k.hansmk) hansmk, sum(y.hansyf) as yhansyf, sum(k.buhsmk) as buhsmk, sum(y.buhsyf) as ybuhsyf, sum(y.shuik) as yshuik\n" + 
			"  from diancjsmkb k, diancjsyfb y ,diancxxb dc\n" + 
			" where dc.id=k.diancxxb_id\n" + "	and k.id = y.diancjsmkb_id\n" + 
			str+
			"		and k.ruzrq>=to_date('"+date1+"','yyyy-MM-dd')\n" + 
			"       and k.ruzrq<last_day(to_date('"+date1+"','yyyy-MM-dd'))+1 \n"+
			" 		group by (k.diancxxb_id, k.gongysb_id, k.meiz, decode(1,1,'本月','本月'))\n"+
			"union\n" + 
			" select k.diancxxb_id as diancxxb_id, k.gongysb_id as gongysb_id, k.meiz as pinz,decode(1,1,'累计','累计') as leix,\n" + 
			"       sum(k.jiessl) jiessl, decode(sum(k.jiessl),0,0,round_new(sum(k.jiessl * k.jiesrl) / sum(k.jiessl), 2)) jiesrl,\n" + 
			"       decode(sum(k.jieslf),0,0,round_new(sum(k.jiessl * k.jieslf) / sum(k.jiessl), 2)) as jieslf,\n" + 
			"       sum(k.hansmk) hansmk, sum(y.hansyf) as yhansyf, sum(k.buhsmk) as buhsmk, sum(y.buhsyf) as ybuhsyf, sum(y.shuik) as yshuik\n" + 
			"  from diancjsmkb k, diancjsyfb y ,diancxxb dc\n" + 
			" where dc.id=k.diancxxb_id\n" + 
			str+
			"		and k.ruzrq>=to_date('"+date2+"','yyyy-MM-dd')\n" + 
			"       and k.ruzrq<last_day(to_date('"+date1+"','yyyy-MM-dd'))+1"+
			"	and k.id = y.diancjsmkb_id\n" + 
			" group by (k.diancxxb_id, k.gongysb_id, k.meiz, decode(1,1,'累计','累计')) ) tb,\n" + 
			"(select * from (select distinct k.diancxxb_id, k.gongysb_id\n" + 
			"  from diancjsmkb k, diancxxb dc, diancjsyfb y\n" + 
			" where dc.id=k.diancxxb_id\n" + 
			str+
			"	and k.ruzrq>=to_date('"+date2+"','yyyy-MM-dd')\n" + 
			"       and k.ruzrq<last_day(to_date('"+date1+"','yyyy-MM-dd'))+1 \n"+
			"   and k.id = y.diancjsmkb_id group by(k.diancxxb_id,k.gongysb_id)) sj,\n" + 
			"(select decode(1,1,'本月','本月') fenx from dual union select decode(2,2,'累计','累计') fenx from dual ) fx) tb2, diancxxb dc, gongysb g\n" + 
			"where dc.id=tb2.diancxxb_id and g.id=tb2.gongysb_id\n" + 
			"and tb.diancxxb_id(+)=tb2.diancxxb_id\n" + 
			"and tb2.gongysb_id=tb.gongysb_id(+)\n" + 
			"and tb.leix(+)=tb2.fenx\n" + 
			" group by rollup (tb2.fenx,dc.mingc, (g.mingc, tb.pinz))\n" + 
			" having not grouping(tb2.fenx)=1\n" + 
			" order by grouping(dc.mingc) desc,danwmc,grouping(g.mingc) desc, fahr,grouping(tb2.fenx) desc,tb2.fenx";
			
		sb.append(sqltable);
		

			ResultSet rs = con.getResultSet(sb,
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			String[][] ArrHeader = new String[1][15];
			ArrHeader[0] = new String[] {"单位名称",Local.fahr,Local.dangyue_leij_yueb,Local.pinz,Local.meil_jies,
							Local.farl_jies,Local.liuf_jies,Local.hej_jies,Local.meik_jies,Local.buhsmk_jies,
							Local.yunf_jies,Local.buhsyf_jies,Local.yunfs_jies,Local.biaomdj_jies,Local.buhsbmdj_jies};

			int[] ArrWidth = new int[15];
			ArrWidth = new int[] { 80, 70, 60, 60, 80, 80, 50,50,80, 40, 40, 40, 40, 40, 40 };
			


// 数据
			
			Table tb=new Table(rs, 1, 0, 4);
			rt.setBody(tb);
			String []strFormat=new String[]{"","","","","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00"};
			rt.setTitle("销售结算月报", ArrWidth);
			rt.title.setRowHeight(2, 40);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 5, "制表单位:"
					+ visit.getDiancqc(),
					Table.ALIGN_LEFT);
			rt.setDefaultTitle(6,3, "月报日期:"
					+ date1,
					Table.ALIGN_CENTER);
			rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(25);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.mergeFixedCols();
			rt.body.mergeFixedRow();
			rt.body.setColFormat(strFormat);

			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 3, "制表日期:"
					+ DateUtil.FormatDate(new Date()),
					Table.ALIGN_LEFT);
			rt.setDefautlFooter(4, 2, "主管:", Table.ALIGN_LEFT);
			rt.setDefautlFooter(6, 2, "审核:", Table.ALIGN_LEFT);
			rt.setDefautlFooter(8, 3, "制表:", Table.ALIGN_LEFT);
			rt.footer.setRowCells(1, Table.PER_FONTSIZE, 10);
//			rt.createFooter(1, ArrWidth);
//			rt.setDefautlFooter(1, 2, "总计:", Table.ALIGN_LEFT);

				_CurrentPage=1;
				_AllPages=rt.body.getPages();
				if (_AllPages==0){
					_CurrentPage=0;
				}
			

			con.Close();
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
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		// nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);

		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		// yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);

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
	
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
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
	
//	页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}


}