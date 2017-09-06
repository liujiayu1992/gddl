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
public class Caigjsyb  extends BasePage implements PageValidateListener{

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
	
	private boolean _FindChick = false;

	public void FindButton(IRequestCycle cycle) {
		_FindChick = true;
	}
	public void submit(IRequestCycle cycle) {
				if (_FindChick) {
			_FindChick = false;
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
			visit.setList1(null);
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean5(null);
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
//		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		StringBuffer sb = new StringBuffer();
		
		String strDianc = "";
		if(this.getDiancTreeJib()==1){//集团
			strDianc = "";
		}else if(this.getDiancTreeJib()==2){
			strDianc = " and (dc.id="+this.getTreeid()+" or dc.fuid="+this.getTreeid()+" ) ";
		}else{
			strDianc = " and dc.id="+this.getTreeid();
		}
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
		//报表时间
//		DateUtil.FormatDate(new Date());
		

		// 设置明细行SQL
		String sqltable = 
			"select /*grouping(tb.fahr),\n" +
			"       grouping(tb.pinz),\n" + 
			"       grouping(tb.leix), */\n" + 
			"      decode(grouping(tb.fahr),1,'总计',tb.fahr) as fahr,\n" + 
			"      decode(grouping(tb.fahr)+grouping(tb.pinz),1,'合计',tb.pinz) as pinz,\n" + 
			"      tb.leix,\n" + 
			"      sum(s.meil) as meil,\n" + 
			"      decode(sum(s.meil),0,0,round_new(sum(s.frl) / sum(s.meil), 2)) as frl,\n" + 
			"      decode(sum(s.meil),0,0,round_new(sum(s.luf)/  sum(s.meil),2))  as luf,\n" + 
			"       sum(s.hej) as hej,\n" + 
			"       sum(s.hansmk) as hansmk,\n" + 
			"       sum(s.buhsmk) as buhsmk,\n" + 
			"       sum(s.hansyf) as hansyf,\n" + 
			"       sum(s.buhsyf) as buhsyf,\n" + 
			"       sum(s.yunfs)  as yunfs,\n" + 
			"     decode(sum(s.meil),0,0,round_new(sum(biaomdj) / sum(s.meil), 2) )as biaomdj,\n" + 
			"     decode(sum(s.meil),0,0,round_new(sum( buhsbmdj) / sum(s.meil), 2) )as buhsbmdj\n" + 
			"from (select f.mingc as fahr,\n" + 
			"       k.meiz as pinz,\n" + 
			"       decode(0,0,'本月','本月') as leix,\n" + 
			"       sum(k.jiessl) as meil,\n" + 
			"       sum(k.jiessl * k.jiesrl)  as frl,\n" + 
			"       sum(k.jiessl * k.jieslf)  as luf,\n" + 
			"       sum(k.hansmk + y.hansyf)  as hej,\n" + 
			"       sum(k.hansmk) as hansmk,\n" + 
			"       sum(k.buhsmk) as buhsmk,\n" + 
			"       sum(y.hansyf) as hansyf,\n" + 
			"       sum(y.buhsyf) buhsyf,\n" + 
			"       sum(y.shuik) yunfs,\n" + 
			"       sum((k.hansmk + y.hansyf) * 29.271 )  as biaomdj,\n" + 
			"       sum((k.buhsmk + y.buhsyf) * 29.271 )  as buhsbmdj\n" + 
			"  from kuangfjsmkb k, kuangfjsyfb y ,vwfahr f,diancxxb dc\n" + 
			" where k.id = y.kuangfjsmkb_id (+)\n" + 
			"   and f.id=k.gongysb_id\n" + 
			"  and k.ruzrq>=to_date('"+date2+"','yyyy-MM-dd')\n" + 
			"  and k.ruzrq<last_day(to_date('"+date1+"','yyyy-MM-dd'))+1\n" + 
			"  and k.diancxxb_id= dc.id"+ strDianc+"\n" + 
			"  group by (f.mingc,k.meiz,decode(0,0,'本月','本月'))\n" + 
			"union\n" + 
			"select\n" + 
			"       f.mingc as fahr,\n" + 
			"       k.meiz as pinz,\n" + 
			"       decode(0,0,'累计','累计') as leix,\n" + 
			"       sum(k.jiessl) as meil,\n" + 
			"   sum(k.jiessl * k.jiesrl) as frl,\n" + 
			"   sum(k.jiessl * k.jieslf) as luf,\n" + 
			"       sum(k.hansmk + y.hansyf) as hej,\n" + 
			"       sum(k.hansmk) as hansmk,\n" + 
			"       sum(k.buhsmk) as buhsmk,\n" + 
			"       sum(y.hansyf) as hansyf,\n" + 
			"       sum(y.buhsyf) buhsyf,\n" + 
			"       sum(y.shuik) yunfs,\n" + 
			"      sum((k.hansmk + y.hansyf) * 29.271) as biaomdj,\n" + 
			"      sum((k.buhsmk + y.buhsyf) * 29.271) as buhsbmdj\n" + 
			"  from kuangfjsmkb k, kuangfjsyfb y ,vwfahr f,diancxxb dc\n" + 
			" where k.id = y.kuangfjsmkb_id(+)\n" + 
			"      and f.id=k.gongysb_id\n" + 
			"       and k.ruzrq>=to_date('"+date2+"','yyyy-MM-dd')\n" + 
			"       and k.ruzrq<last_day(to_date('"+date1+"','yyyy-MM-dd'))+1\n" + 
			"       and k.diancxxb_id= dc.id "+ strDianc+"\n" + 
			"       group by (f.mingc,k.meiz,decode(0,0,'累计','累计'))  ) s ,\n" + 
			"\n" + 
			"(select * from(select\n" + 
			"       f.mingc as fahr,\n" + 
			"       k.meiz as pinz\n" + 
			"  from kuangfjsmkb k, kuangfjsyfb y ,vwfahr f,diancxxb dc\n" + 
			" where k.id = y.kuangfjsmkb_id(+)\n" + 
			"      and f.id=k.gongysb_id\n" + 
			"       and k.ruzrq>=to_date('"+date2+"','yyyy-MM-dd')\n" + 
			"       and k.ruzrq<last_day(to_date('"+date1+"','yyyy-MM-dd'))+1\n" + 
			"       and k.diancxxb_id= dc.id ) t1,\n" + 
			"    (select decode(1,1,'本月',' 本月') as leix from dual union select decode(1,1,'累计','累计') as leix from dual )t2 ) tb\n" + 
			"\n" + 
			"    where tb.fahr=s.fahr(+) and tb.pinz=s.pinz(+) and tb.leix=s.leix(+)\n" + 
			"\n" + 
			"    group by rollup (tb.leix,tb.fahr,tb.pinz)\n" + 
			"    having not grouping(tb.leix)=1\n" + 
			"order by grouping(tb.fahr) desc,fahr,grouping(tb.pinz) desc,tb.pinz, leix";

			
		sb.append(sqltable);
		
		//System.out.print(sqltable);

			ResultSet rs = con.getResultSet(sb,
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			

			
				
			
			String[][] ArrHeader = new String[1][14];
			ArrHeader[0] = new String[] {Local.fahr,Local.pinz,Local.dangyue_leij_yueb,Local.meil_jies,
							Local.farl_jies,Local.liuf_jies,Local.hej_jies,Local.meik_jies,Local.buhsmk_jies,
							Local.yunf_jies,Local.buhsyf_jies,Local.yunfs_jies,Local.biaomdj_jies,Local.buhsbmdj_jies};

			int[] ArrWidth = new int[14];
			ArrWidth = new int[] { 80, 60, 60, 80, 80, 50,50,80, 40, 40, 40, 40, 40, 40 };
			


			// 数据
			
			Table tb=new Table(rs, 1, 0, 1);
			rt.setBody(tb);
			
			tb.setColFormat(4,"0.00");
			tb.setColFormat(5,"0.00");
			tb.setColFormat(6,"0.00");
			tb.setColFormat(7,"0.00");
			tb.setColFormat(8,"0.00");
			tb.setColFormat(9,"0.00");
			tb.setColFormat(10,"0.00");
			tb.setColFormat(11,"0.00");
			tb.setColFormat(13,"0.00");
			tb.setColFormat(14,"0.00");
			tb.setColFormat(12,"0.00");
			
			
			rt.setTitle("结算月报(采购)", ArrWidth);
			rt.setDefaultTitle(1, 9, "制表单位:"+getZhibdwmc(),Table.ALIGN_LEFT);
			rt.setDefaultTitle(12, 3, "制表时间:"+getNianfValue().getValue()+"年"+getYuefValue().getValue()+"月", Table.ALIGN_RIGHT);
			
			
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 5, "打印日期:"
					+ DateUtil.FormatDate(new Date()),
					Table.ALIGN_LEFT);
			rt.setDefautlFooter(6, 3, "审核:", Table.ALIGN_LEFT);
			rt.setDefautlFooter(12, 3, "制表:", Table.ALIGN_LEFT);
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(25);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			rt.body.setColAlign(3, Table.ALIGN_CENTER);
			rt.body.setColAlign(4, Table.ALIGN_RIGHT);
			rt.body.setColAlign(5, Table.ALIGN_RIGHT);
			rt.body.setColAlign(6, Table.ALIGN_RIGHT);
			rt.body.setColAlign(7, Table.ALIGN_RIGHT);
			rt.body.setColAlign(8, Table.ALIGN_RIGHT);
			rt.body.setColAlign(9, Table.ALIGN_RIGHT);
			rt.body.setColAlign(10, Table.ALIGN_RIGHT);
			rt.body.setColAlign(11, Table.ALIGN_RIGHT);
			rt.body.setColAlign(12, Table.ALIGN_RIGHT);
			rt.body.setColAlign(13, Table.ALIGN_RIGHT);
			rt.body.setColAlign(14, Table.ALIGN_RIGHT);
			
			
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
	Toolbar tb1 = new Toolbar("tbdiv");
	
	tb1.addText(new ToolbarText("单位名称:"));
	DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+((Visit)getPage().getVisit()).getDiancxxb_id(),"",null,getTreeid());
	((Visit)getPage().getVisit()).setDefaultTree(dt);
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
	tb1.addText(new ToolbarText("-"));
	
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
	
	ToolbarButton tb = new ToolbarButton(null,"查询","function(){document.Form0.submit();}");
	tb.setIcon(SysConstant.Btn_Icon_Print);
	tb1.addItem(tb);
	
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
	
//	 年份下拉框
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
				for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
					Object obj = getNianfModel().getOption(i);
					if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
							.getId()) {
						_NianfValue = (IDropDownBean) obj;
						break;
					}
				}
			}
			return _NianfValue;
		}

		public boolean nianfchanged;

		public void setNianfValue(IDropDownBean Value) {
			if (_NianfValue != Value) {
				nianfchanged = true;
			}
			_NianfValue = Value;
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

		// 月份
		public boolean Changeyuef = false;

		private static IPropertySelectionModel _YuefModel;

		public IPropertySelectionModel getYuefModel() {
			if (_YuefModel == null) {
				getYuefModels();
			}
			return _YuefModel;
		}

		private IDropDownBean _YuefValue;


		public IDropDownBean getYuefValue(){
			if (_YuefValue == null) {
				for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
					Object obj = getYuefModel().getOption(i);
					if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
							.getId()) {
						_YuefValue = (IDropDownBean) obj;
						break;
					}
				}
			}
			return _YuefValue;
		}

		public void setYuefValue(IDropDownBean Value) {
			long id = -2;
			if (_YuefValue!= null) {
				id = getYuefValue().getId();
			}
			if (Value != null) {
				if (Value.getId() != id) {
					Changeyuef = true;
				} else {
					Changeyuef = false;
				}
			}
			_YuefValue = Value;
			
		}

		public IPropertySelectionModel getYuefModels() {
			List listYuef = new ArrayList();
			// listYuef.add(new IDropDownBean(-1,"请选择"));
			for (int i = 1; i < 13; i++) {
				listYuef.add(new IDropDownBean(i, String.valueOf(i)));
			}
			_YuefModel = new IDropDownModel(listYuef);
			return _YuefModel;
		}

		public void setYuefModel(IPropertySelectionModel _value) {
			_YuefModel = _value;
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

	
//	页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}


}