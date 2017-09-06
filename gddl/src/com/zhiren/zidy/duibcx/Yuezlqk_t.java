package com.zhiren.zidy.duibcx;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.jfree.data.category.CategoryDataset;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.report.Chart;
import com.zhiren.report.ChartData;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者：李琛基
 * 时间：2011-1-11
 * 描述：
 *   根据国电需求  增加月质量情况图
 */
public class Yuezlqk_t extends BasePage {
	
	
	private String _msg;
	
	public String getMsg() {
		return _msg;
	}

	public void setMsg(String msg) {
		_msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	
	public boolean getRaw() {
		return true;
	}
	
	// 报表初始设置
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
	
	private int paperStyle;
	
	private void paperStyle() {
		JDBCcon con = new JDBCcon();
		int paperStyle = Report.PAPER_A4_WIDTH;
		ResultSetList rsl = con
				.getResultSetList("select zhi from xitxxb  where zhuangt=1 and mingc='纸张类型' and diancxxb_id="
						+ ((Visit) this.getPage().getVisit()).getDiancxxb_id());
		if (rsl.next()) {
			paperStyle = rsl.getInt("zhi");
		}

		this.paperStyle = paperStyle;
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
	
	private boolean reportShowZero(){
		return ((Visit) getPage().getVisit()).isReportShowZero();
	}

//	 开始年份下拉框
	private static IPropertySelectionModel _BNianfModel;

	public IPropertySelectionModel getBNianfModel() {
		if (_BNianfModel == null) {
			getBNianfModels();
		}
		return _BNianfModel;
	}
	
	public void setBNianfModel(IPropertySelectionModel _value) {
		_BNianfModel = _value;
	}
	
	private IDropDownBean _BNianfValue;

	public IDropDownBean getBNianfValue() {
		if (_BNianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getBNianfModel().getOptionCount(); i++) {
				Object obj = getBNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_BNianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _BNianfValue;
	}

	public void setBNianfValue(IDropDownBean Value) {
		if (_BNianfValue != Value) {
			_BNianfValue = Value;
		}
	}

	public IPropertySelectionModel getBNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = DateUtil.getYear(new Date())-2; i <= DateUtil.getYear(new Date()) + 2; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_BNianfModel = new IDropDownModel(listNianf);
		return _BNianfModel;
	}

	

	// 开始月份下拉框
	private static IPropertySelectionModel _BYuefModel;

	public IPropertySelectionModel getBYuefModel() {
		if (_BYuefModel == null) {
			getBYuefModels();
		}
		return _BYuefModel;
	}
	
	public void setBYuefModel(IPropertySelectionModel _value) {
		_BYuefModel = _value;
	}
	
	private IDropDownBean _BYuefValue;

	public IDropDownBean getBYuefValue() {
		if (_BYuefValue == null) {
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_yuef = 12;
			} else {
				_yuef = _yuef - 1;
			}
			for (int i = 0; i < getBYuefModel().getOptionCount(); i++) {
				Object obj = getBYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_BYuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _BYuefValue;
	}

	public void setBYuefValue(IDropDownBean Value) {
		if (_BYuefValue != Value) {
			_BYuefValue = Value;
		}
	}

	public IPropertySelectionModel getBYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_BYuefModel = new IDropDownModel(listYuef);
		return _BYuefModel;
	}
	
//电厂口径
	private IPropertySelectionModel _koujModel;
	public void setKoujModel(IPropertySelectionModel value){
		_koujModel=value;
	}
	public IPropertySelectionModel getKoujModel(){
		if(_koujModel==null){
			setKoujModels();
		}
		return _koujModel;
	}
	private IDropDownBean _koujValue;
	public void setKoujValue(IDropDownBean value){
		_koujValue=value;
	}
	public IDropDownBean getKoujValue(){
		if(_koujValue==null){
			_koujValue=(IDropDownBean)getKoujModel().getOption(0);
		}
		return _koujValue;
	}
	public void setKoujModels(){
		JDBCcon con=new JDBCcon();
		String sql="select id,mingc from dianckjb order by id";
		if(con.getHasIt(sql)){
			setKoujModel(new IDropDownModel(sql));
		}else{
			setKoujModel(new IDropDownModel(sql,"请配置电厂口径！"));
		}
	}
	
	public String getReportType() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	

	// 页面初始设置
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			init();
		}
	}
	
	private void init(){
		Visit visit = (Visit) getPage().getVisit();
		visit.setToolbar(null);
		visit.setString1(null);
		paperStyle();
		getSelectData();
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox bnianf = new ComboBox();
		bnianf.setTransform("Bnianf");
		bnianf.setWidth(60);
		tb1.addField(bnianf);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox byuef = new ComboBox();
		byuef.setTransform("Byuef");
		byuef.setWidth(60);
		tb1.addField(byuef);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("电厂口径:"));
		ComboBox kouj = new ComboBox();
		kouj.setTransform("KoujDropDown");
		kouj.setWidth(100);
		tb1.addField(kouj);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "查询",
			"function(){document.getElementById('QueryButton').click();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(tb);
		setToolbar(tb1);
	}
	
	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	// submit
	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
			getPrintTable();
		}
		this.getSelectData();
	}

	// 报表主体
	public String getPrintTable() {	
		String beginsj=getBNianfValue().getValue()+"-"+getBYuefValue().getValue()+"-01";
		String diancList=this.getDiancList(getKoujValue().getStrId());
		String sql="select biaot.mingc,biaot.leix,t.shuj\n" +
		"  from (select a.mingc, b.leix,a.xuh mingcxh,b.xuh leixxh\n" + 
		"  from (select '入厂煤热值' mingc,1 xuh\n" + 
		"          from dual\n" + 
		"        union\n" + 
		"        select '入炉煤热值' mingc,2 xuh\n" + 
		"          from dual\n" + 
		"        union\n" + 
		"        select '热值差' mingc,3 xuh from dual) a,\n" + 
		"       (select '本期' leix,1 xuh\n" + 
		"          from dual\n" + 
		"        union\n" + 
		"        select '同期' leix,2 xuh\n" + 
		"          from dual\n" + 
		"        union\n" + 
		"        select '同比差' leix,3 xuh from dual)b\n" + 
		"        order by b.xuh,a.xuh\n" + 
		") biaot, (select '入厂煤热值' mingc,\n" + 
		"       '本期' leix,\n" + 
		"       round_new(nvl(decode(sum(s.laimsl),\n" + 
		"                            0,\n" + 
		"                            0,\n" + 
		"                            sum(z.qnet_ar * s.laimsl) / sum(s.laimsl)),\n" + 
		"                     0),\n" + 
		"                 2) shuj\n" + 
		"  from yuezlb z, yueslb s, yuetjkjb k\n" + 
		" where k.id = z.yuetjkjb_id\n" + 
		"   and k.id = s.yuetjkjb_id\n" + 
		"   and z.fenx = '累计'\n" + 
		"   and s.fenx = '累计'\n" + 
		"   and k.riq = date '"+beginsj+"'\n" + 
		"   and k.diancxxb_id in ("+diancList+")\n" + 
		"union\n" + 
		"select '入厂煤热值' mingc,\n" + 
		"       '同期' leix,\n" + 
		"       round_new(nvl(decode(sum(s.laimsl),\n" + 
		"                            0,\n" + 
		"                            0,\n" + 
		"                            sum(z.qnet_ar * s.laimsl) / sum(s.laimsl)),\n" + 
		"                     0),\n" + 
		"                 2) shuj\n" + 
		"  from yuezlb z, yueslb s, yuetjkjb k\n" + 
		" where k.id = z.yuetjkjb_id\n" + 
		"   and k.id = s.yuetjkjb_id\n" + 
		"   and z.fenx = '累计'\n" + 
		"   and s.fenx = '累计'\n" + 
		"   and k.riq = add_months(date '"+beginsj+"', -12)\n" + 
		"   and k.diancxxb_id in ("+diancList+")\n" + 
		"union\n" + 
		"select '入厂煤热值' mingc,\n" + 
		"       '同比差' leix,\n" + 
		"       round_new(nvl(bq.shuj, 0), 2) - round_new(nvl(tq.shuj, 0), 2) shuj\n" + 
		"  from (select '入厂煤热值' mingc,\n" + 
		"               decode(sum(s.laimsl),\n" + 
		"                      0,\n" + 
		"                      0,\n" + 
		"                      sum(z.qnet_ar * s.laimsl) / sum(s.laimsl)) shuj\n" + 
		"          from yuezlb z, yueslb s, yuetjkjb k\n" + 
		"         where k.id = z.yuetjkjb_id\n" + 
		"           and k.id = s.yuetjkjb_id\n" + 
		"           and z.fenx = '累计'\n" + 
		"           and s.fenx = '累计'\n" + 
		"           and k.riq = date\n" + 
		"         '"+beginsj+"'\n" + 
		"           and k.diancxxb_id in ("+diancList+")) bq,\n" + 
		"       (select '入厂煤热值' mingc,\n" + 
		"               nvl(decode(sum(s.laimsl),\n" + 
		"                          0,\n" + 
		"                          0,\n" + 
		"                          sum(z.qnet_ar * s.laimsl) / sum(s.laimsl)),\n" + 
		"                   0) shuj\n" + 
		"          from yuezlb z, yueslb s, yuetjkjb k\n" + 
		"         where k.id = z.yuetjkjb_id\n" + 
		"           and k.id = s.yuetjkjb_id\n" + 
		"           and z.fenx = '累计'\n" + 
		"           and s.fenx = '累计'\n" + 
		"           and k.riq = add_months(date '"+beginsj+"', -12)\n" + 
		"           and k.diancxxb_id in ("+diancList+")) tq\n" + 
		" where bq.mingc = tq.mingc\n" + 
		"union\n" + 
		"select '入炉煤热值' mingc,\n" + 
		"       '本期' leix,\n" + 
		"       round_new(nvl(decode(sum(z.fadgrytrml),\n" + 
		"                            0,\n" + 
		"                            0,\n" + 
		"                            sum(z.rultrmpjfrl / 1000 * z.fadgrytrml) /\n" + 
		"                            sum(z.fadgrytrml)),\n" + 
		"                     0),\n" + 
		"                 2) shuj\n" + 
		"  from yuezbb z\n" + 
		" where z.fenx = '累计'\n" + 
		"   and z.riq = date '"+beginsj+"'\n" + 
		"   and z.diancxxb_id in ("+diancList+")\n" + 
		"union\n" + 
		"select '入炉煤热值' mingc,\n" + 
		"       '同期' leix,\n" + 
		"       round_new(nvl(decode(sum(z.fadgrytrml),\n" + 
		"                            0,\n" + 
		"                            0,\n" + 
		"                            sum(z.rultrmpjfrl / 1000 * z.fadgrytrml) /\n" + 
		"                            sum(z.fadgrytrml)),\n" + 
		"                     0),\n" + 
		"                 2) shuj\n" + 
		"  from yuezbb z\n" + 
		" where z.fenx = '累计'\n" + 
		"   and z.riq = add_months(date '"+beginsj+"', -12)\n" + 
		"   and z.diancxxb_id in ("+diancList+")\n" + 
		"union\n" + 
		"select '入炉煤热值' mingc,\n" + 
		"       '同比差' leix,\n" + 
		"       round_new(nvl(bq.shuj, 0), 2) - round_new(nvl(tq.shuj, 0), 2) shuj\n" + 
		"  from (select '入炉煤热值' mingc,\n" + 
		"               '本期' leix,\n" + 
		"               decode(sum(z.fadgrytrml),\n" + 
		"                      0,\n" + 
		"                      0,\n" + 
		"                      sum(z.rultrmpjfrl / 1000 * z.fadgrytrml) /\n" + 
		"                      sum(z.fadgrytrml)) shuj\n" + 
		"          from yuezbb z\n" + 
		"         where z.fenx = '累计'\n" + 
		"           and z.riq = date\n" + 
		"         '"+beginsj+"'\n" + 
		"           and z.diancxxb_id in ("+diancList+")) bq,\n" + 
		"       (select '入炉煤热值' mingc,\n" + 
		"               '同期' leix,\n" + 
		"               decode(sum(z.fadgrytrml),\n" + 
		"                      0,\n" + 
		"                      0,\n" + 
		"                      sum(z.rultrmpjfrl / 1000 * z.fadgrytrml) /\n" + 
		"                      sum(z.fadgrytrml)) shuj\n" + 
		"          from yuezbb z\n" + 
		"         where z.fenx = '累计'\n" + 
		"           and z.riq = add_months(date '"+beginsj+"', -12)\n" + 
		"           and z.diancxxb_id in ("+diancList+")) tq\n" + 
		" where bq.mingc = tq.mingc\n" + 
		"union\n" + 
		"select '热值差' mingc,\n" + 
		"       '本期' leix,\n" + 
		"       round_new(nvl(ruc.shuj, 0), 2) - round_new(nvl(rul.shuj, 0), 2) shuj\n" + 
		"  from (select '入厂煤热值' mingc,\n" + 
		"               '本期' leix,\n" + 
		"               nvl(decode(sum(s.laimsl),\n" + 
		"                          0,\n" + 
		"                          0,\n" + 
		"                          sum(z.qnet_ar * s.laimsl) / sum(s.laimsl)),\n" + 
		"                   0) shuj\n" + 
		"          from yuezlb z, yueslb s, yuetjkjb k\n" + 
		"         where k.id = z.yuetjkjb_id\n" + 
		"           and k.id = s.yuetjkjb_id\n" + 
		"           and z.fenx = '累计'\n" + 
		"           and s.fenx = '累计'\n" + 
		"           and k.riq = date\n" + 
		"         '"+beginsj+"'\n" + 
		"           and k.diancxxb_id in ("+diancList+")) ruc,\n" + 
		"       (select '入炉煤热值' mingc,\n" + 
		"               '本期' leix,\n" + 
		"               nvl(decode(sum(z.fadgrytrml),\n" + 
		"                          0,\n" + 
		"                          0,\n" + 
		"                          sum(z.rultrmpjfrl / 1000 * z.fadgrytrml) /\n" + 
		"                          sum(z.fadgrytrml)),\n" + 
		"                   0) shuj\n" + 
		"          from yuezbb z\n" + 
		"         where z.fenx = '累计'\n" + 
		"           and z.riq = date\n" + 
		"         '"+beginsj+"'\n" + 
		"           and z.diancxxb_id in ("+diancList+")) rul\n" + 
		" where ruc.leix = rul.leix\n" + 
		"union\n" + 
		"select '热值差' mingc,\n" + 
		"       '同期' leix,\n" + 
		"       round_new(nvl(ruc.shuj, 0), 2) - round_new(nvl(rul.shuj, 0), 2) shuj\n" + 
		"  from (select '入厂煤热值' mingc,\n" + 
		"               '本期' leix,\n" + 
		"               nvl(decode(sum(s.laimsl),\n" + 
		"                          0,\n" + 
		"                          0,\n" + 
		"                          sum(z.qnet_ar * s.laimsl) / sum(s.laimsl)),\n" + 
		"                   0) shuj\n" + 
		"          from yuezlb z, yueslb s, yuetjkjb k\n" + 
		"         where k.id = z.yuetjkjb_id\n" + 
		"           and k.id = s.yuetjkjb_id\n" + 
		"           and z.fenx = '累计'\n" + 
		"           and s.fenx = '累计'\n" + 
		"           and k.riq = add_months(date '"+beginsj+"', -12)\n" + 
		"           and k.diancxxb_id in ("+diancList+")) ruc,\n" + 
		"       (select '入炉煤热值' mingc,\n" + 
		"               '本期' leix,\n" + 
		"               nvl(decode(sum(z.fadgrytrml),\n" + 
		"                          0,\n" + 
		"                          0,\n" + 
		"                          sum(z.rultrmpjfrl / 1000 * z.fadgrytrml) /\n" + 
		"                          sum(z.fadgrytrml)),\n" + 
		"                   0) shuj\n" + 
		"          from yuezbb z\n" + 
		"         where z.fenx = '累计'\n" + 
		"           and z.riq = add_months(date '"+beginsj+"', -12)\n" + 
		"           and z.diancxxb_id in ("+diancList+")) rul\n" + 
		" where ruc.leix = rul.leix\n" + 
		"union\n" + 
		"select '热值差' mingc,\n" + 
		"       '同比差' leix,\n" + 
		"       nvl(bq.shuj, 0) - nvl(tq.shuj, 0) shuj\n" + 
		"  from (select '热值差' mingc,\n" + 
		"               '本期' leix,\n" + 
		"               round_new(nvl(ruc.shuj, 0), 2) -\n" + 
		"               round_new(nvl(rul.shuj, 0), 2) shuj\n" + 
		"          from (select '入厂煤热值' mingc,\n" + 
		"                       '本期' leix,\n" + 
		"                       nvl(decode(sum(s.laimsl),\n" + 
		"                                  0,\n" + 
		"                                  0,\n" + 
		"                                  sum(z.qnet_ar * s.laimsl) / sum(s.laimsl)),\n" + 
		"                           0) shuj\n" + 
		"                  from yuezlb z, yueslb s, yuetjkjb k\n" + 
		"                 where k.id = z.yuetjkjb_id\n" + 
		"                   and k.id = s.yuetjkjb_id\n" + 
		"                   and z.fenx = '累计'\n" + 
		"                   and s.fenx = '累计'\n" + 
		"                   and k.riq = date\n" + 
		"                 '"+beginsj+"'\n" + 
		"                   and k.diancxxb_id in ("+diancList+")) ruc,\n" + 
		"               (select '入炉煤热值' mingc,\n" + 
		"                       '本期' leix,\n" + 
		"                       nvl(decode(sum(z.fadgrytrml),\n" + 
		"                                  0,\n" + 
		"                                  0,\n" + 
		"                                  sum(z.rultrmpjfrl / 1000 * z.fadgrytrml) /\n" + 
		"                                  sum(z.fadgrytrml)),\n" + 
		"                           0) shuj\n" + 
		"                  from yuezbb z\n" + 
		"                 where z.fenx = '累计'\n" + 
		"                   and z.riq = date\n" + 
		"                 '"+beginsj+"'\n" + 
		"                   and z.diancxxb_id in ("+diancList+")) rul\n" + 
		"         where ruc.leix = rul.leix) bq,\n" + 
		"       (select '热值差' mingc,\n" + 
		"               '同期' leix,\n" + 
		"               round_new(nvl(ruc.shuj, 0), 2) -\n" + 
		"               round_new(nvl(rul.shuj, 0), 2) shuj\n" + 
		"          from (select '入厂煤热值' mingc,\n" + 
		"                       '本期' leix,\n" + 
		"                       nvl(decode(sum(s.laimsl),\n" + 
		"                                  0,\n" + 
		"                                  0,\n" + 
		"                                  sum(z.qnet_ar * s.laimsl) / sum(s.laimsl)),\n" + 
		"                           0) shuj\n" + 
		"                  from yuezlb z, yueslb s, yuetjkjb k\n" + 
		"                 where k.id = z.yuetjkjb_id\n" + 
		"                   and k.id = s.yuetjkjb_id\n" + 
		"                   and z.fenx = '累计'\n" + 
		"                   and s.fenx = '累计'\n" + 
		"                   and k.riq = add_months(date '"+beginsj+"', -12)\n" + 
		"                   and k.diancxxb_id in ("+diancList+")) ruc,\n" + 
		"               (select '入炉煤热值' mingc,\n" + 
		"                       '本期' leix,\n" + 
		"                       nvl(decode(sum(z.fadgrytrml),\n" + 
		"                                  0,\n" + 
		"                                  0,\n" + 
		"                                  sum(z.rultrmpjfrl / 1000 * z.fadgrytrml) /\n" + 
		"                                  sum(z.fadgrytrml)),\n" + 
		"                           0) shuj\n" + 
		"                  from yuezbb z\n" + 
		"                 where z.fenx = '累计'\n" + 
		"                   and z.riq = add_months(date '"+beginsj+"', -12)\n" + 
		"                   and z.diancxxb_id in ("+diancList+")) rul\n" + 
		"         where ruc.leix = rul.leix) tq\n" + 
		" where bq.mingc = tq.mingc\n" + 
		") t\n" + 
		" where biaot.mingc = t.mingc(+)\n" + 
		"   and biaot.leix = t.leix(+)\n" + 
		" order by biaot.leixxh, biaot.mingcxh";
		return getZhilChart(sql)+"<div>&nbsp;</div>"+getZhil(sql);
	}
	public String getZhil(String sql){
		JDBCcon con=new JDBCcon();
	   	Report rt=new Report();
	   	String[][] ArrHeader=new String[4][4];
		int[] ArrWidth=new int[]{100,170,170,170};
		
		ArrHeader[0]=new String[]{"项目","本期","同期","同比差"};
		ArrHeader[1][0]="入厂煤热值";
		ArrHeader[2][0]="入炉煤热值";
		ArrHeader[3][0]="热值差";
		
		ResultSetList rs=con.getResultSetList(sql);
		for (int i = 1; i < 4; i++) {
			for (int j = 1; j < 4; j++) {
				if(rs.next()){
					ArrHeader[j][i]=rs.getString("shuj");
				}else{
					break;
				}
			}
		}
		rt.setBody(new Table(ArrHeader,0,0,0));
		rt.body.setWidth(ArrWidth);
		for (int i = 0; i < rt.body.getRows(); i++) {
			if(i==0){
				rt.body.setRowCells(i+1, Table.PER_FONTBOLD, true);
			}
			rt.body.setRowCells(i+1, Table.PER_ALIGN, Table.ALIGN_CENTER);
		}
		con.Close();
		return rt.getHtml();
	}
	public String getZhilChart(String sql){
		JDBCcon cn = new JDBCcon();
		Report rt=new Report();
		ChartData cd = new ChartData();
		Chart ct = new Chart();
		
		int ArrWidth[]=new int[] {605};
		String ArrHeader[][]=new String[1][1];
		
		
		String beginsj=getBNianfValue().getValue()+"-"+getBYuefValue().getValue()+"-01";
		
		String picDate="公司"+getBNianfValue().getValue()+"年"+getBYuefValue().getValue()+"月份累计煤炭质量情况";
		

//		柱状图
		
		ResultSet rs=cn.getResultSet(sql);
		
		CategoryDataset dataset = cd.getRsDataChart(rs, "leix", "mingc", "shuj");//rs记录集构造生成图片需要的数据
		
		/*--------------设置图片参数开始-------------------*/
		
		ct.intDigits=0;			//	显示小数位数
		ct.barItemMargin=-0.05;
		//ct.xTiltShow = true;//倾斜显示X轴的文字
		ct.barLabelsFontbln=true;
		
		
		/*--------------设置图片参数结束-------------------*/		
		
		//生成柱状图并显示到页面
		
		String charStr=ct.ChartBar3D(getPage(), dataset,picDate, 600, 295);
		ArrHeader[0]=new String[] {charStr};
		rt.setBody(new Table(ArrHeader,0,0,0));
		rt.body.setWidth(ArrWidth);
		
		_CurrentPage = 1;
		_AllPages = rt.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		
		cn.Close();

		return rt.getHtml();
	}
	public String getDiancList(String baobkjId){
		JDBCcon con=new JDBCcon();
		String sql="select diancxxb_id from dianckjmx where dianckjb_id="+baobkjId;
		StringBuffer str=new StringBuffer();
		if(!con.getHasIt(sql)){
			return null;
		}
		ResultSetList rs=con.getResultSetList(sql);
		while(rs.next()){
			str.append(rs.getString("diancxxb_id")).append(",");
		}
		return str.toString().substring(0,str.length()-1);
	}
}