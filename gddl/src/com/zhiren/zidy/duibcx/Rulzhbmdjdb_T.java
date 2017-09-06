package com.zhiren.zidy.duibcx;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.jfree.data.time.TimeSeriesCollection;

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

public class Rulzhbmdjdb_T extends BasePage{
	
	
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
		
		tb1.addText(new ToolbarText("请选择年份:"));
		ComboBox bnianf = new ComboBox();
		bnianf.setTransform("Bnianf");
		bnianf.setWidth(60);
		tb1.addField(bnianf);
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
		String nianf=getBNianfValue().getValue();
		String diancList=this.getDiancList(getKoujValue().getStrId());
		String sql=

			"select to_date('"+getBNianfValue().getValue()+"-' || bt.shuz || '-01', 'yyyy-MM-dd') riq,\n" +
			"       bt.leix,\n" + 
			"       nvl(sj.rulzhbmdj, 0) shuj\n" + 
			"  from (select a.leix, y.mvalue shuz\n" + 
			"          from yuefb y,\n" + 
			"               (select '本期' leix\n" + 
			"                  from dual\n" + 
			"                union\n" + 
			"                select '同期' leix from dual) a) bt,\n" + 
			"       (select '本期' danw,\n" + 
			"               to_char(y.riq, 'MM') riq,\n" + 
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
			"           and to_char(y.riq, 'yyyy') = '"+nianf+"'\n" + 
			"           and y.fenx = '当月'\n" + 
			"           and d.id in ("+diancList+")\n" + 
			"         group by y.riq\n" + 
			"        union\n" + 
			"        select '同期' danw,\n" + 
			"               to_char(y.riq, 'MM') riq,\n" + 
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
			"           and to_char(add_months(y.riq, -12), 'yyyy') = '"+nianf+"'\n" + 
			"           and y.fenx = '当月'\n" + 
			"           and d.id in ("+diancList+")\n" + 
			"         group by y.riq) sj\n" + 
			" where bt.leix = sj.danw(+)\n" + 
			"   and bt.shuz = sj.riq(+)\n" + 
			" order by bt.leix, bt.shuz";


		return getZhilChart(sql)+"<div>&nbsp;</div>"+getZhil(sql);
	}
	public String getZhil(String sql){
		JDBCcon con=new JDBCcon();
	   	Report rt=new Report();
	   	String[][] ArrHeader=new String[3][13];
		int[] ArrWidth=new int[13];
		for (int i = 0; i < 13; i++) {
			ArrWidth[i]=50;
			if(i==0){
				ArrHeader[0][i]="项目";
			}else{
				ArrHeader[0][i]=i+"月份";
			}
		}
		ArrHeader[1][0]="本期";
		ArrHeader[2][0]="同期";
		
		ResultSetList rs=con.getResultSetList(sql);
		for (int i = 1; i < 3; i++) {
			for (int j = 1; j < 13; j++) {
				if(rs.next()){
					ArrHeader[i][j]=rs.getString("shuj");
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
		
		
		String picDate=getBNianfValue().getValue()+"公司标煤单价对比";
		

//		线形图
		
		ResultSetList rs=cn.getResultSetList(sql);
		
		TimeSeriesCollection dataset = cd.getRsDataTimeGraph(rs, "leix", "riq", "shuj");//rs记录集构造生成图片需要的数据
		
		/*--------------设置图片参数开始-------------------*/
		
		ct.intDigits=0;			//	显示小数位数
		ct.xfontSize=9;		//	x轴字体大小
		ct.dateApeakShowbln = false;//竖直显示x轴的日期
		ct.lineDateFormatOverride="MM月份";//日期显示格式
		ct.setDateUnit(Chart.DATEUNIT_MONTH, 1);
		//ct.xTiltShow = true;//倾斜显示X轴的文字
		//ct.barLabelsFontbln=false;
		
		
		
		/*--------------设置图片参数结束-------------------*/		
		
		//生成柱状图并显示到页面
		
		String charStr=ct.ChartTimeGraph(getPage(), dataset,picDate, 600, 295);
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
