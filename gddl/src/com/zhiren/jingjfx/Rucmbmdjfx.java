package com.zhiren.jingjfx;

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

/*
 * 修改人:tzf
 * 时间:2009-06-04
 * 内容:将从yuercbmdj取数调整成yuejsbmdj表中取数，更改日期（当年） 为上一年
 */
/**
 * @author ly
 *
 */
public class Rucmbmdjfx  extends BasePage implements PageValidateListener{

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
	
	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
			getPrintTable();
			getSelectData();
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
			visit.setString3(null);
			visit.setProSelectionModel2(null);
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
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		StringBuffer strSQL = new StringBuffer();
		String diancxxbid = " ";
		if(getDiancTreeJib()==2){
			diancxxbid = "and d.id = "+getTreeid_dc()+" or d.fuid = "+getTreeid_dc()+"\n";
		}else if(getDiancTreeJib()==3){
			diancxxbid = "and d.id = "+getTreeid_dc()+"\n";
		}
//		diancxxbid = getTreeid_dc();// 电厂信息表id
		String strDate = getNianfValue().getValue()+"-"+getYuefValue().getValue();
		String strDate1 =Integer.valueOf( getNianfValue().getValue()).intValue()-1+"-"+getYuefValue().getValue();
		
		String sql = 
			"select decode(grouping(decode(d.diancmc,null,s.diancmc,d.diancmc)),1,'公司总计',decode(d.diancmc,null,s.diancmc,d.diancmc)),\n" +
			"       decode(d.benyhlj,null,s.benyhlj,d.benyhlj),\n" + 
			"       sum(nvl(d.tianrml,0)),sum(nvl(s.tianrml,0)),sum(nvl(d.tianrml,0)-nvl(s.tianrml,0)),\n" + 
			"       sum(nvl(d.rez,0)),sum(nvl(s.rez,0)),sum(nvl(d.rez,0)-nvl(s.rez,0)),\n" + 
			"       sum(nvl(d.tianrdj,0)),sum(nvl(s.tianrdj,0)),sum(nvl(d.tianrdj,0)-nvl(s.tianrdj,0)),\n" + 
			"       sum(nvl(d.hansbmdj,0)),sum(nvl(s.hansbmdj,0)),sum(nvl(d.hansbmdj,0)-nvl(s.hansbmdj,0)),\n" + 
			"       sum(nvl(d.buhsbmdj,0)),sum(nvl(s.buhsbmdj,0)),sum(nvl(d.buhsbmdj,0)-nvl(s.buhsbmdj,0)),\n" + 
			"       sum(nvl(d.jij,0)),0,0,sum(nvl(d.zengzs,0)),sum(nvl(d.xiaoj,0)),sum(nvl(d.xiaoj,0)-nvl(s.xiaoj,0)),\n" + 
			"       sum(nvl(d.tielyf,0)),0,0,0,sum(nvl(d.shuie,0)),sum(nvl(d.yunfxj,0)),sum(nvl(d.yunfxj,0)-nvl(s.yunfxj,0)),\n" + 
			"       sum(nvl(d.daozzf,0)),sum(nvl(d.daozzf,0)-nvl(s.daozzf,0)),\n" + 
			"       sum(nvl(d.qitfy,0)),sum(nvl(d.qitfy,0)-nvl(s.qitfy,0))\n" + 
			"from\n" + 
			"((select distinct d.mingc as diancmc,\n" + 
			"        r.fenx as benyhlj\n" + 
			" from yuejsbmdj r,yuetjkjb t,diancxxb d\n" + 
			" where t.diancxxb_id = d.id\n" + 
			"      and r.yuetjkjb_id = t.id\n" + 
//			"      and y.yuetjkjb_id = t.id\n" + 
//			"      and y.fenx = r.fenx\n" + 
			diancxxbid + 
			"      and t.riq=to_date('"+strDate+"-01','yyyy-MM-dd')\n" + 
			")\n" + 
			"union\n" + 
			"(select distinct d.mingc as diancmc,\n" + 
			"        r.fenx as benyhlj\n" + 
			" from yuejsbmdj r,yuetjkjb t,diancxxb d\n" + 
			" where t.diancxxb_id = d.id\n" + 
			"      and r.yuetjkjb_id = t.id\n" + 
//			"      and y.yuetjkjb_id = t.id\n" + 
//			"      and y.fenx = r.fenx\n" + 
			diancxxbid + 
			"      and t.riq=to_date('"+strDate1+"-01','yyyy-MM-dd')\n" + 
			")) a,\n" + 
			//当年
			"(select distinct d.mingc as diancmc,--电厂名称\n" + 
			"       r.fenx as benyhlj, --本月或累计\n" + 
			"       r.jiesl as tianrml, --天然煤量\n" + 
			"       r.qnet_ar as rez,  --热值\n" + 
			"       r.meij+r.yunj+r.yunjs+r.daozzf+r.zaf+r.qit as tianrdj, --天然单价\n" + 
			"       r.biaomdj as hansbmdj, --含税标煤单价,\n" + 
			"       r.buhsbmdj as buhsbmdj, --不含税标煤单价\n" + 
			"       r.meij-r.meijs as jij, --基价\n" + 
			"       r.meijs as zengzs, --增值税\n" + 
			"       r.meij as xiaoj, --小计\n" + 
			"       r.yunj as tielyf, --铁路运费\n" + 
			"       r.yunjs as shuie, --税额\n" + 
			"       r.yunj+r.yunjs as yunfxj, --运费小计\n" + 
			"       r.daozzf as daozzf, --到站杂费\n" + 
			"       r.qit as qitfy --其他费用\n" + 
			"from yuejsbmdj r,yuetjkjb t,diancxxb d\n" + 
			"where t.diancxxb_id = d.id\n" + 
			"      and r.yuetjkjb_id = t.id\n" + 
//			"      and y.yuetjkjb_id = t.id\n" + 
//			"      and y.fenx = r.fenx\n" + 
			diancxxbid + 
			"      and t.riq=to_date('"+strDate+"-01','yyyy-MM-dd')) d,\n" + 
			//上年
			"(select distinct d.mingc as diancmc,--电厂名称\n" + 
			"       r.fenx as benyhlj, --本月或累计\n" + 
			"       r.jiesl as tianrml, --天然煤量\n" + 
			"       r.qnet_ar as rez,  --热值\n" + 
			"       r.meij+r.yunj+r.yunjs+r.daozzf+r.zaf+r.qit as tianrdj, --天然单价\n" + 
			"       r.biaomdj as hansbmdj, --含税标煤单价,\n" + 
			"       r.buhsbmdj as buhsbmdj, --不含税标煤单价\n" + 
			"       r.meij-r.meijs as jij, --基价\n" + 
			"       r.meijs as zengzs, --增值税\n" + 
			"       r.meij as xiaoj, --小计\n" + 
			"       r.yunj as tielyf, --铁路运费\n" + 
			"       r.yunjs as shuie, --税额\n" + 
			"       r.yunj+r.yunjs as yunfxj, --运费小计\n" + 
			"       r.daozzf as daozzf, --到站杂费\n" + 
			"       r.qit as qitfy --其他费用\n" + 
			"from yuejsbmdj r,yuetjkjb t,diancxxb d\n" + 
			"where t.diancxxb_id = d.id\n" + 
			"      and r.yuetjkjb_id = t.id\n" + 
//			"      and y.yuetjkjb_id = t.id\n" + 
//			"      and y.fenx = r.fenx\n" + 
			diancxxbid + 
			"      and t.riq=to_date('"+strDate1+"-01','yyyy-MM-dd')) s\n" + 
			"where a.diancmc = d.diancmc(+)\n" + 
			"      and a.diancmc = s.diancmc(+)\n" + 
			"      and a.benyhlj = d.benyhlj(+)\n" + 
			"      and a.benyhlj = s.benyhlj(+)\n" + 
			"group by grouping sets ('1',(decode(d.benyhlj,null,s.benyhlj,d.benyhlj)),(decode(d.diancmc,null,s.diancmc,d.diancmc),decode(d.benyhlj,null,s.benyhlj,d.benyhlj)))\n" + 
			"having not (grouping(decode(d.diancmc,null,s.diancmc,d.diancmc))+grouping(decode(d.benyhlj,null,s.benyhlj,d.benyhlj)))=2\n" + 
			"order by grouping(decode(d.diancmc,null,s.diancmc,d.diancmc)) desc,decode(d.diancmc,null,s.diancmc,d.diancmc),decode(d.benyhlj,null,s.benyhlj,d.benyhlj)\n" + 
			"";
		
		ResultSet rs = cn.getResultSet(sql);
		// 定义表头数据
		 String ArrHeader[][]=new String[4][34];
		 ArrHeader[0]=new String[] {"电厂名称","本月<br>或<br>累计","天然煤量(吨)","天然煤量(吨)","天然煤量(吨)","热值(MJ/Kg)","热值(MJ/Kg)","热值(MJ/Kg)","天然单价(含税)","天然单价(含税)","天然单价(含税)","含税标煤单价","含税标煤单价","含税标煤单价","不含税标煤单价","不含税标煤单价","不含税标煤单价","天然煤单价的组成","天然煤单价的组成","天然煤单价的组成","天然煤单价的组成","天然煤单价的组成","天然煤单价的组成","天然煤单价的组成","天然煤单价的组成","天然煤单价的组成","天然煤单价的组成","天然煤单价的组成","天然煤单价的组成","天然煤单价的组成","天然煤单价的组成","天然煤单价的组成","天然煤单价的组成","天然煤单价的组成"};
		 ArrHeader[1]=new String[] {"电厂名称","本月<br>或<br>累计","天然煤量(吨)","天然煤量(吨)","天然煤量(吨)","热值(MJ/Kg)","热值(MJ/Kg)","热值(MJ/Kg)","天然单价(含税)","天然单价(含税)","天然单价(含税)","含税标煤单价","含税标煤单价","含税标煤单价","不含税标煤单价","不含税标煤单价","不含税标煤单价","煤炭费用","煤炭费用","煤炭费用","煤炭费用","煤炭费用","煤炭费用","运输费用","运输费用","运输费用","运输费用","运输费用","运输费用","运输费用","到站杂费","到站杂费","其它费用","其它费用"};
		 ArrHeader[2]=new String[] {"电厂名称","本月<br>或<br>累计","当年","上年","同比","当年","上年","同比","当年","上年","同比","当年","上年","同比","当年","上年","同比","基价","矿区杂费","加价","增值税","小计","同比","铁路运费","铁路杂费","专线费","汽运费","税额","小计","同比","当年","同比","当年","同比"};
		 ArrHeader[3]=new String[] {"电厂名称","本月<br>或<br>累计","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32"};

		 int ArrWidth[]=new int[] {80,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50};
		// 设置页标题
		rt.setTitle("大唐甘肃公司入厂煤标煤单价分析表", ArrWidth);
		rt.setDefaultTitle(1, 5, "报送单位:", Table.ALIGN_LEFT);
		rt.setDefaultTitle(6, 24, getNianfValue().getValue()+"年"+getYuefValue().getValue()+"月", Table.ALIGN_CENTER);

		// 数据
		rt.setBody(new Table(rs, 4, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(32);
		rt.body.setHeaderData(ArrHeader);// 表头数据
//		rt.body.mergeFixedRow();
//		rt.body.mergeFixedCol(4);
		rt.body.mergeFixedRowCol();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);

		 //页脚
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(11,4,"审核:",Table.ALIGN_LEFT);
		 rt.setDefautlFooter(17,3,"制表:",Table.ALIGN_LEFT);
		 rt.setDefautlFooter(20,9,"报送日期:",Table.ALIGN_LEFT);
		
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
		
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
						: getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "查询",
				"function(){document.getElementById('QueryButton').click();}");
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

//	-------------------------电厂Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		Visit visit=(Visit)this.getPage().getVisit();
		String sql = " select d.id,d.mingc from diancxxb d where d.id="+visit.getDiancxxb_id()+" \n";
		sql+=" union \n";
		sql+="  select d.id,d.mingc from diancxxb d where d.fuid="+visit.getDiancxxb_id()+" \n";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	DefaultTree dc ;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc= etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	
//	-------------------------电厂Tree END----------
	
//得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid_dc();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		return jib;
	}

}