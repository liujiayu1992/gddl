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
 * 时间：2011-1-7
 * 描述：
 *   根据国电需求  增加收耗存情况汇总图
 */
public class Shouhcqk_t extends BasePage {
	
	public  static final int LEIX_SHOUML=1;
	public static final int LEIX_HAOML=2;
	public static final int LEIX_KUC=3;
	
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

	
	//结束年份
	private static IPropertySelectionModel _ENianfModel;

	public IPropertySelectionModel getENianfModel() {
		if (_ENianfModel == null) {
			getENianfModels();
		}
		return _ENianfModel;
	}
	
	public void setENianfModel(IPropertySelectionModel _value) {
		_ENianfModel = _value;
	}
	
	private IDropDownBean _ENianfValue;

	public IDropDownBean getENianfValue() {
		if (_ENianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getENianfModel().getOptionCount(); i++) {
				Object obj = getENianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_ENianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _ENianfValue;
	}

	public void setENianfValue(IDropDownBean Value) {
		if (_ENianfValue != Value) {
			_ENianfValue = Value;
		}
	}

	public IPropertySelectionModel getENianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = DateUtil.getYear(new Date())-2; i <= DateUtil.getYear(new Date()) + 2; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_ENianfModel = new IDropDownModel(listNianf);
		return _ENianfModel;
	}


	// 结束月份下拉框
	private static IPropertySelectionModel _EYuefModel;

	public IPropertySelectionModel getEYuefModel() {
		if (_EYuefModel == null) {
			getEYuefModels();
		}
		return _EYuefModel;
	}

	public void setEYuefModel(IPropertySelectionModel _value) {
		_EYuefModel = _value;
	}
	
	private IDropDownBean _EYuefValue;

	public IDropDownBean getEYuefValue() {
		if (_EYuefValue == null) {
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_yuef = 12;
			} else {
				_yuef = _yuef - 1;
			}
			for (int i = 0; i < getEYuefModel().getOptionCount(); i++) {
				Object obj = getEYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_EYuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _EYuefValue;
	}

	public void setEYuefValue(IDropDownBean Value) {
		if (_EYuefValue != Value) {
			_EYuefValue = Value;
		}
	}

	public IPropertySelectionModel getEYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_EYuefModel = new IDropDownModel(listYuef);
		return _EYuefModel;
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
//类型下拉框
	private IPropertySelectionModel _leixModel;
	public void setLeixModel(IPropertySelectionModel value){
		_leixModel=value;
	}
	public IPropertySelectionModel getLeixModel(){
		if(_leixModel==null){
			setLeixModels();
		}
		return _leixModel;
	}
	private IDropDownBean _leixValue;
	public void setLeixValue(IDropDownBean value){
		_leixValue=value;
	}
	public IDropDownBean getLeixValue(){
		if(_leixValue==null){
			_leixValue=(IDropDownBean)getLeixModel().getOption(0);
		}
		return _leixValue;
	}
	public void setLeixModels(){
		List list=new ArrayList();
		list.add(new IDropDownBean(LEIX_SHOUML,"来煤量"));
		list.add(new IDropDownBean(LEIX_HAOML,"耗煤量"));
		list.add(new IDropDownBean(LEIX_KUC,"库存"));
		setLeixModel(new IDropDownModel(list));
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
		
		tb1.addText(new ToolbarText("开始年份:"));
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
		
		tb1.addText(new ToolbarText("结束年份:"));
		ComboBox enianf = new ComboBox();
		enianf.setTransform("Enianf");
		enianf.setWidth(60);
		tb1.addField(enianf);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox eyuef = new ComboBox();
		eyuef.setTransform("Eyuef");
		eyuef.setWidth(60);
		tb1.addField(eyuef);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("电厂口径:"));
		ComboBox kouj = new ComboBox();
		kouj.setTransform("KoujDropDown");
		kouj.setWidth(100);
		tb1.addField(kouj);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("类型:"));
		ComboBox leix = new ComboBox();
		leix.setTransform("Leix");
		leix.setWidth(100);
		tb1.addField(leix);
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
		String endsj=getENianfValue().getValue()+"-"+getEYuefValue().getValue()+"-01";
		String diancList=getDiancList(getKoujValue().getStrId());
		String rs="<div align='center'><h2>无此图表</h2></div>";
		switch ((int)getLeixValue().getId()) {
		case LEIX_SHOUML:
			rs=getShouml(beginsj, endsj, diancList);
			break;
		case LEIX_HAOML:
			rs=getHaoml(beginsj, endsj, diancList);
			break;
		case LEIX_KUC:
			rs=getKuc(beginsj, endsj, diancList);
			break;
		default:
			break;
		}
		return rs;
	}
	//收煤量
	public String getShouml(String beginsj,String endsj,String diancList){
		String charSql=
			"select biaot.mingc, biaot.leix, nvl(shuj.shouml / 10000, 0) shouml\n" +
			"  from (select a.id, a.mingc, b.leix\n" + 
			"          from (select id, mingc\n" + 
			"                  from diancxxb\n" + 
			"                 where id in ("+diancList+")) a,\n" + 
			"               (select '本期' leix\n" + 
			"                  from dual\n" + 
			"                union\n" + 
			"                select '同期' leix from dual) b) biaot,\n" + 
			"       (select shc.diancxxb_id, sum(nvl(shc.shouml, 0)) shouml, '本期' leix\n" + 
			"          from yueshchjb shc\n" + 
			"         where shc.fenx = '本月'\n" + 
			"           and shc.riq >= date '"+beginsj+"'\n" + 
			"           and shc.riq <= date '"+endsj+"'\n" + 
			"         group by shc.diancxxb_id\n" + 
			"        union\n" + 
			"        select shc.diancxxb_id, sum(nvl(shc.shouml, 0)) shouml, '同期' leix\n" + 
			"          from yueshchjb shc\n" + 
			"         where  shc.fenx = '本月'\n" + 
			"           and shc.riq >= add_months(date '"+beginsj+"', -12)\n" + 
			"           and shc.riq <= add_months(date '"+endsj+"', -12)\n" + 
			"         group by shc.diancxxb_id) shuj\n" + 
			" where biaot.id = shuj.diancxxb_id(+)\n" + 
			"   and biaot.leix = shuj.leix(+)\n" + 
			" order by biaot.id, biaot.leix";

		
		String shujSql=
			"select biaot.mingc,\n" +
			"       nvl(benq.shouml / 10000, 0) benqsml,\n" + 
			"       nvl(tongq.shouml / 10000, 0) tqsml\n" + 
			"  from (select id, mingc from diancxxb where id in ("+diancList+")) biaot,\n" + 
			"       (select shc.diancxxb_id, sum(nvl(shc.shouml, 0)) shouml\n" + 
			"          from yueshchjb shc\n" + 
			"         where shc.fenx = '本月'\n" + 
			"           and shc.riq >= date '"+beginsj+"'\n" + 
			"           and shc.riq <= date '"+endsj+"'\n" + 
			"         group by shc.diancxxb_id) benq,\n" + 
			"       (select shc.diancxxb_id, sum(nvl(shc.shouml, 0)) shouml\n" + 
			"          from yueshchjb shc\n" + 
			"         where shc.fenx = '本月'\n" + 
			"           and shc.riq >= add_months(date '"+beginsj+"', -12)\n" + 
			"           and shc.riq <= add_months(date '"+endsj+"', -12)\n" + 
			"         group by shc.diancxxb_id) tongq\n" + 
			" where biaot.id = benq.diancxxb_id(+)\n" + 
			"   and biaot.id = tongq.diancxxb_id(+)\n" + 
			" order by biaot.id";
		return getBaseChart(charSql, "mingc", "leix", "shouml")+"<div>&nbsp;</div>"+getBaseShuj(shujSql);
	}
	//耗煤量
	public String getHaoml(String beginsj,String endsj,String diancList){
		String charSql=
			"select biaot.mingc, biaot.leix, nvl(shuj.haoml / 10000, 0) haoml\n" +
			"  from (select a.id, a.mingc, b.leix\n" + 
			"          from (select id, mingc\n" + 
			"                  from diancxxb\n" + 
			"                 where id in ("+diancList+")) a,\n" + 
			"               (select '本期' leix\n" + 
			"                  from dual\n" + 
			"                union\n" + 
			"                select '同期' leix from dual) b) biaot,\n" + 
			"       (select shc.diancxxb_id, sum(nvl(shc.fady+shc.gongry+shc.qith, 0)) haoml, '本期' leix\n" + 
			"          from yueshchjb shc\n" + 
			"         where shc.fenx = '本月'\n" + 
			"           and shc.riq >= date '"+beginsj+"'\n" + 
			"           and shc.riq <= date '"+endsj+"'\n" + 
			"         group by shc.diancxxb_id\n" + 
			"        union\n" + 
			"        select shc.diancxxb_id, sum(nvl(shc.fady+shc.gongry+shc.qith, 0)) haoml, '同期' leix\n" + 
			"          from yueshchjb shc\n" + 
			"         where  shc.fenx = '本月'\n" + 
			"           and shc.riq >= add_months(date '"+beginsj+"', -12)\n" + 
			"           and shc.riq <= add_months(date '"+endsj+"', -12)\n" + 
			"         group by shc.diancxxb_id) shuj\n" + 
			" where biaot.id = shuj.diancxxb_id(+)\n" + 
			"   and biaot.leix = shuj.leix(+)\n" + 
			" order by biaot.id, biaot.leix";

		
		String shujSql=
			"select biaot.mingc,\n" +
			"       nvl(benq.haoml / 10000, 0) benqhml,\n" + 
			"       nvl(tongq.haoml / 10000, 0) tqhml\n" + 
			"  from (select id, mingc from diancxxb where id in ("+diancList+")) biaot,\n" + 
			"       (select shc.diancxxb_id, sum(nvl(shc.fady+shc.gongry+shc.qith, 0)) haoml\n" + 
			"          from yueshchjb shc\n" + 
			"         where shc.fenx = '本月'\n" + 
			"           and shc.riq >= date '"+beginsj+"'\n" + 
			"           and shc.riq <= date '"+endsj+"'\n" + 
			"         group by shc.diancxxb_id) benq,\n" + 
			"       (select shc.diancxxb_id, sum(nvl(shc.fady+shc.gongry+shc.qith, 0)) haoml\n" + 
			"          from yueshchjb shc\n" + 
			"         where shc.fenx = '本月'\n" + 
			"           and shc.riq >= add_months(date '"+beginsj+"', -12)\n" + 
			"           and shc.riq <= add_months(date '"+endsj+"', -12)\n" + 
			"         group by shc.diancxxb_id) tongq\n" + 
			" where biaot.id = benq.diancxxb_id(+)\n" + 
			"   and biaot.id = tongq.diancxxb_id(+)\n" + 
			" order by biaot.id";
		return getBaseChart(charSql, "mingc", "leix", "haoml")+"<div>&nbsp;</div>"+getBaseShuj(shujSql);
	}
	//库存
	public String getKuc(String beginsj,String endsj,String diancList){
		String charSql=
			"select biaot.mingc, biaot.leix, nvl(shuj.kuc / 10000, 0) kuc\n" +
			"  from (select a.id, a.mingc, b.leix\n" + 
			"          from (select id, mingc\n" + 
			"                  from diancxxb\n" + 
			"                 where id in ("+diancList+")) a,\n" + 
			"               (select '本期' leix\n" + 
			"                  from dual\n" + 
			"                union\n" + 
			"                select '同期' leix from dual) b) biaot,\n" + 
			"       (select shc.diancxxb_id, sum(nvl(shc.kuc, 0)) kuc, '本期' leix\n" + 
			"          from yueshchjb shc\n" + 
			"         where shc.fenx = '本月'\n" + 
			"           and shc.riq >= date '"+beginsj+"'\n" + 
			"           and shc.riq <= date '"+endsj+"'\n" + 
			"         group by shc.diancxxb_id\n" + 
			"        union\n" + 
			"        select shc.diancxxb_id, sum(nvl(shc.kuc, 0)) kuc, '同期' leix\n" + 
			"          from yueshchjb shc\n" + 
			"         where  shc.fenx = '本月'\n" + 
			"           and shc.riq >= add_months(date '"+beginsj+"', -12)\n" + 
			"           and shc.riq <= add_months(date '"+endsj+"', -12)\n" + 
			"         group by shc.diancxxb_id) shuj\n" + 
			" where biaot.id = shuj.diancxxb_id(+)\n" + 
			"   and biaot.leix = shuj.leix(+)\n" + 
			" order by biaot.id, biaot.leix";

		
		String shujSql=
			"select biaot.mingc,\n" +
			"       nvl(benq.kuc / 10000, 0) benqkc,\n" + 
			"       nvl(tongq.kuc / 10000, 0) tqkc\n" + 
			"  from (select id, mingc from diancxxb where id in ("+diancList+")) biaot,\n" + 
			"       (select shc.diancxxb_id, sum(nvl(shc.kuc, 0)) kuc\n" + 
			"          from yueshchjb shc\n" + 
			"         where shc.fenx = '本月'\n" + 
			"           and shc.riq >= date '"+beginsj+"'\n" + 
			"           and shc.riq <= date '"+endsj+"'\n" + 
			"         group by shc.diancxxb_id) benq,\n" + 
			"       (select shc.diancxxb_id, sum(nvl(shc.kuc, 0)) kuc\n" + 
			"          from yueshchjb shc\n" + 
			"         where shc.fenx = '本月'\n" + 
			"           and shc.riq >= add_months(date '"+beginsj+"', -12)\n" + 
			"           and shc.riq <= add_months(date '"+endsj+"', -12)\n" + 
			"         group by shc.diancxxb_id) tongq\n" + 
			" where biaot.id = benq.diancxxb_id(+)\n" + 
			"   and biaot.id = tongq.diancxxb_id(+)\n" + 
			" order by biaot.id";
		return getBaseChart(charSql, "mingc", "leix", "kuc")+"<div>&nbsp;</div>"+getBaseShuj(shujSql);
	}
	/**
	 * 基础数据段
	 * @param dateSql  数据SQL
	 * @return
	 */
	public String getBaseShuj(String dateSql){
		JDBCcon con=new JDBCcon();
	   	Report rt=new Report();
	   	String[][] ArrHeader=new String[1][3];
		int[] ArrWidth=new int[]{100,250,250};
		ArrHeader[0]=new String[]{"单位","本期","同期"};
		
		ResultSetList rs=con.getResultSetList(dateSql);
		
		rt.setBody(new Table(rs,1,0,0));
		rt.body.setHeaderData(ArrHeader);
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
	/**
	 * 图基础
	 * @param charSql   图片SQL
	 * @param xLine		X轴字段名
	 * @param legendName  图例字段名
	 * @param yLine		Y轴字段名
	 * @return
	 */
	public String getBaseChart(String charSql,String xLine,String legendName,String yLine){
		JDBCcon cn = new JDBCcon();
		Report rt=new Report();
		ChartData cd = new ChartData();
		Chart ct = new Chart();
		
		int ArrWidth[]=new int[] {805};
		String ArrHeader[][]=new String[1][1];
		
		String picDate="";
		if(getBNianfValue().getValue().equals(getENianfValue().getValue())){
			if(getBYuefValue().getValue().equals(getEYuefValue().getValue())){
				picDate=getBNianfValue().getValue()+"年"+getBYuefValue().getValue()+"月份"+getLeixValue().getValue();
			}else{
				picDate=getBNianfValue().getValue()+"年"+getBYuefValue().getValue()+"-"+getEYuefValue().getValue()+"月份"+getLeixValue().getValue();
			}
		}else{
			picDate=getBNianfValue().getValue()+"年"+getBYuefValue().getValue()+"月-"+getENianfValue().getValue()+"年"+getEYuefValue().getValue()+"月份"+getLeixValue().getValue();
		}
		

//		柱状图
		
		ResultSet rs=cn.getResultSet(charSql);
		
		CategoryDataset dataset = cd.getRsDataChart(rs, xLine, legendName, yLine);//rs记录集构造生成图片需要的数据
		
		/*--------------设置图片参数开始-------------------*/
		
		ct.intDigits=0;			//	显示小数位数
		ct.barItemMargin=-0.05;
		ct.xTiltShow = true;//倾斜显示X轴的文字
		ct.barLabelsFontbln=false;
		
		
		/*--------------设置图片参数结束-------------------*/		
		
		//生成柱状图并显示到页面
		
		String charStr=ct.ChartBar3D(getPage(), dataset,picDate, 800, 395);
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
	public static String getDiancList(String baobkjId){
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