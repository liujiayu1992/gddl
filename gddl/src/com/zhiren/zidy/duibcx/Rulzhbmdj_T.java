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

public class Rulzhbmdj_T extends BasePage{
	
	
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

//	 年份下拉框
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

	

	// 月份下拉框
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
		
		tb1.addText(new ToolbarText("查询年份:"));
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
		return getShouhChart()+"<div>&nbsp;</div>"+getShouhc();
	}
	public String getShouhc(){
		JDBCcon con=new JDBCcon();
	   	Report rt=new Report();
	   	String[][] ArrHeader=new String[1][4];
		int[] ArrWidth=new int[]{50,180,180,180};
		String sql="";
		String beginsj=getBNianfValue().getValue()+"-"+getBYuefValue().getValue()+"-01";
		
		
		ArrHeader[0]=new String[]{"项目","本期","同期"};
		sql=
			"select bt.mingc,a.rulzhbmdj as benq,b.rulzhbmdj as tongq from\n" +
			"\n" + 
			"(select decode(grouping(d.mingc), 1, '公司合计', d.mingc) as mingc from diancxxb d group by rollup (d.mingc)) bt,\n" + 
			"(select decode(grouping(d.mingc), 1, '公司合计', d.mingc) as mingc,\n" + 
			"               round_new(decode(sum(nvl(y.rulmzbzml, 0) +\n" + 
			"                                    nvl(y.rulyzbzml, 0) +\n" + 
			"                                    nvl(y.rulqzbzml, 0)),\n" + 
			"                                0,\n" + 
			"                                0,\n" + 
			"                                sum(nvl(y.rulzhbmdj, 0) *\n" + 
			"                                    (nvl(y.rulmzbzml, 0) +\n" + 
			"                                     nvl(y.rulyzbzml, 0) +\n" + 
			"                                     nvl(y.rulqzbzml, 0))) /\n" + 
			"                                sum(nvl(y.rulmzbzml, 0) + nvl(y.rulyzbzml, 0) +\n" + 
			"                                    nvl(y.rulqzbzml, 0))),\n" + 
			"                         2) as rulzhbmdj\n" + 
			"          from yuezbb y, diancxxb d\n" + 
			"         where y.diancxxb_id = d.id\n" + 
			"           and y.riq  = add_months(date '"+beginsj+"', -0)\n" + 
			"           and y.fenx = '累计'\n" + 
			"           and d.id in ("+getDiancList(getKoujValue().getStrId())+")\n" + 
			"         group by rollup(d.mingc)) a,\n" + 
			" (select decode(grouping(d.mingc), 1, '公司合计', d.mingc) as mingc,\n" + 
			"               round_new(decode(sum(nvl(y.rulmzbzml, 0) +\n" + 
			"                                    nvl(y.rulyzbzml, 0) +\n" + 
			"                                    nvl(y.rulqzbzml, 0)),\n" + 
			"                                0,\n" + 
			"                                0,\n" + 
			"                                sum(nvl(y.rulzhbmdj, 0) *\n" + 
			"                                    (nvl(y.rulmzbzml, 0) +\n" + 
			"                                     nvl(y.rulyzbzml, 0) +\n" + 
			"                                     nvl(y.rulqzbzml, 0))) /\n" + 
			"                                sum(nvl(y.rulmzbzml, 0) + nvl(y.rulyzbzml, 0) +\n" + 
			"                                    nvl(y.rulqzbzml, 0))),\n" + 
			"                         2) as rulzhbmdj\n" + 
			"          from yuezbb y, diancxxb d\n" + 
			"         where y.diancxxb_id = d.id\n" + 
			"           and y.riq  = add_months(date '"+beginsj+"', -12)\n" + 
			"           and y.fenx = '累计'\n" + 
			"           and d.id in ("+getDiancList(getKoujValue().getStrId())+")\n" + 
			"         group by rollup(d.mingc)) b\n" + 
			" where a.mingc(+) = BT.MINGC\n" + 
			" and b.mingc(+) = bt.mingc\n" + 
			"";

		ResultSetList rs=con.getResultSetList(sql);
		
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
	public String getShouhChart(){
		JDBCcon cn = new JDBCcon();
		Report rt=new Report();
		ChartData cd = new ChartData();
		Chart ct = new Chart();
		
		String sbsql = "";
		int ArrWidth[]=new int[] {800};
		String ArrHeader[][]=new String[1][1];
		
		
		String beginsj=getBNianfValue().getValue()+"-"+getBYuefValue().getValue()+"-01";
//		String endsj=getENianfValue().getValue()+"-"+getEYuefValue().getValue()+"-01";
//		
    	String picDate=getBNianfValue().getValue()+"年"+getBYuefValue().getValue()+"累计各厂入炉综合标煤单价";;
//		if(getBNianfValue().getValue().equals(getENianfValue().getValue())){
//			if(getBYuefValue().getValue().equals(getEYuefValue().getValue())){
//				picDate="公司"+getBNianfValue().getValue()+"年"+getBYuefValue().getValue()+"月份来耗存情况";
//			}else{
//				picDate="公司"+getBNianfValue().getValue()+"年"+getBYuefValue().getValue()+"-"+getEYuefValue().getValue()+"月份来耗存情况";
//			}
//		}else{
//			picDate="公司"+getBNianfValue().getValue()+"年"+getBYuefValue().getValue()+"月-"+getENianfValue().getValue()+"年"+getEYuefValue().getValue()+"月份来耗存情况";
//		}
		
		sbsql=
			"select bt.danw, bt.leib, sj.rulzhbmdj\n" +
			"  from (select a.danw, b.leib, b.xuh leixxh,a.xuh danwxh\n" + 
			"          from (select decode(grouping(d.mingc), 1, '公司合计', mingc) danw,\n" + 
			"          decode(grouping(d.mingc), 1, 0, 1) xuh\n"+
			"                  from diancxxb d\n" + 
			"                 group by rollup(d.mingc)) a,\n" + 
			"               (select '本期' leib, 1 xuh\n" + 
			"                  from dual\n" + 
			"                union\n" + 
			"                select '同期' leib, 2 xuh from dual) b) bt,\n" + 
			"\n" + 
			"       (select '本期' danw,\n" + 
			"               decode(grouping(d.mingc), 1, '公司合计', d.mingc) as mingc,\n" + 
			"               round_new(decode(sum(nvl(y.rulmzbzml, 0) +\n" + 
			"                                    nvl(y.rulyzbzml, 0) +\n" + 
			"                                    nvl(y.rulqzbzml, 0)),\n" + 
			"                                0,\n" + 
			"                                0,\n" + 
			"                                sum(nvl(y.rulzhbmdj, 0) *\n" + 
			"                                    (nvl(y.rulmzbzml, 0) +\n" + 
			"                                     nvl(y.rulyzbzml, 0) +\n" + 
			"                                     nvl(y.rulqzbzml, 0))) /\n" + 
			"                                sum(nvl(y.rulmzbzml, 0) + nvl(y.rulyzbzml, 0) +\n" + 
			"                                    nvl(y.rulqzbzml, 0))),\n" + 
			"                         2) as rulzhbmdj\n" + 
			"          from yuezbb y, diancxxb d\n" + 
			"         where y.diancxxb_id = d.id\n" + 
			"           and y.riq  = add_months(date '"+beginsj+"', -0)\n" + 
			"           and y.fenx = '累计'\n" + 
			"           and d.id in ("+getDiancList(getKoujValue().getStrId())+")\n" + 
			"         group by rollup(d.mingc)\n" + 
			"        union\n" + 
			"        select '同期' danw,\n" + 
			"               decode(grouping(d.mingc), 1, '公司合计', d.mingc) as mingc,\n" + 
			"               round_new(decode(sum(nvl(y.rulmzbzml, 0) +\n" + 
			"                                    nvl(y.rulyzbzml, 0) +\n" + 
			"                                    nvl(y.rulqzbzml, 0)),\n" + 
			"                                0,\n" + 
			"                                0,\n" + 
			"                                sum(nvl(y.rulzhbmdj, 0) *\n" + 
			"                                    (nvl(y.rulmzbzml, 0) +\n" + 
			"                                     nvl(y.rulyzbzml, 0) +\n" + 
			"                                     nvl(y.rulqzbzml, 0))) /\n" + 
			"                                sum(nvl(y.rulmzbzml, 0) + nvl(y.rulyzbzml, 0) +\n" + 
			"                                    nvl(y.rulqzbzml, 0))),\n" + 
			"                         2) as rulzhbmdj\n" + 
			"          from yuezbb y, diancxxb d\n" + 
			"         where y.diancxxb_id = d.id\n" + 
			"           and y.riq = add_months(date '"+beginsj+"', -12)\n" + 
			"           and y.fenx = '累计'\n" + 
			"           and d.id in ("+getDiancList(getKoujValue().getStrId())+")\n" + 
			"         group by rollup(d.mingc)) sj\n" + 
			" where bt.danw = sj.mingc(+)\n" + 
			"   and bt.leib = sj.danw(+)\n" + 
			" order by bt.leixxh,bt.danwxh desc";


//		柱状图
		
		ResultSet rs=cn.getResultSet(sbsql);
		
		CategoryDataset dataset = cd.getRsDataChart(rs, "danw", "leib", "rulzhbmdj");//rs记录集构造生成图片需要的数据
		
		/*--------------设置图片参数开始-------------------*/
		
		ct.intDigits=0;			//	显示小数位数
		ct.barItemMargin=-0.05;
		ct.xTiltShow = true;//倾斜显示X轴的文字
		ct.barLabelsFontbln=false;
		
		
		/*--------------设置图片参数结束-------------------*/		
		
		//生成柱状图并显示到页面
		/*
		String dateStr=
			"select biaot.mingc,\n" +
			"       round_new(nvl(shuj.shouml, 0), 2) shouml,\n" + 
			"       round_new(nvl(shuj.haoml, 0), 2) haoml,\n" + 
			"       round_new(nvl(shuj.kuc, 0), 2) kuc\n" + 
			"  from (select '本期' mingc\n" + 
			"          from dual\n" + 
			"        union\n" + 
			"        select '同期' mingc from dual) biaot,\n" + 
			"       (select '本期' mingc,\n" + 
			"               sum(shc.shouml) / 10000 shouml,\n" + 
			"               sum(shc.fady + shc.gongry + shc.qith) / 10000 haoml,\n" + 
			"               sum(shc.kuc) / 10000 kuc\n" + 
			"          from yueshchjb shc\n" + 
			"         where shc.diancxxb_id in ("+getDiancList(getKoujValue().getStrId())+")\n" + 
			"           and shc.riq >= date '"+beginsj+"'\n" + 
			"           and shc.riq <= date '"+endsj+"' and shc.fenx='本月'\n" + 
			"        union\n" + 
			"        select '同期' mingc,\n" + 
			"               sum(shc.shouml) / 10000 shouml,\n" + 
			"               sum(shc.fady + shc.gongry + shc.qith) / 10000 haoml,\n" + 
			"               sum(shc.kuc) / 10000 kuc\n" + 
			"          from yueshchjb shc\n" + 
			"         where shc.diancxxb_id in ("+getDiancList(getKoujValue().getStrId())+")\n" + 
			"           and shc.riq >= add_months(date '"+beginsj+"', -12)\n" + 
			"           and shc.riq <= add_months(date '"+endsj+"', -12) and shc.fenx='本月') shuj\n" + 
			" where biaot.mingc = shuj.mingc(+)";

		ResultSetList date=cn.getResultSetList(dateStr);*/
		String charStr=ct.ChartBar3D(getPage(), dataset,picDate, 800, 350);
		ArrHeader[0]=new String[] {charStr};
		rt.setBody(new Table(ArrHeader,0,0,0));
		/*rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		rt.body.setRowCells(2,Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setRowCells(3,Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setBorder(0);
		rt.body.setCellBorderLeft(1, 1, 0);
		rt.body.setCellBorderTop(1, 1, 0);
		rt.body.mergeRow(1);*/
		
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
