package com.zhiren.jt.diaoygl.shouhcrb.shouhcchart;


import org.apache.tapestry.html.BasePage;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletOutputStream;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Cell;
import com.zhiren.report.Chart;
import com.zhiren.report.ChartBean;
import com.zhiren.report.ChartData;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.jfree.util.Rotation;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;

public class ChartShouhc extends BasePage {
//	 判断是否是集团用户
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团
	}
	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
	}
	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
	}
	 //	页面判定方法
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
	private String _msg;

	protected void initialize() {
		_msg = "";
	}

	public void setMsg(String _value) {
		_msg = _value;
	}
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
//	开始日期v
//	private Date _BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (((Visit)getPage().getVisit()).getDate1()==null){
			((Visit)getPage().getVisit()).setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate1();
	}
	
	public void setBeginriqDate(Date _value) {
		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate1()).equals(DateUtil.FormatDate(_value))) {
			_BeginriqChange=false;
		} else {
			((Visit)getPage().getVisit()).setDate1(_value);
			_BeginriqChange=true;
		}
	}
	
	public boolean getRaw() {
		return true;
	}
	
	private int _CurrentPage = -1;
	
	public int getCurrentPage() {
		return _CurrentPage;
	}
	
	public void setCurrentPage(int _value) {
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	
	public int getAllPages() {
		return _AllPages;
	}
	
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
	private String REPORT_ONEGRAPH_KUCZS="shouhcchart";
	private String mstrReportName="";
	
	//得到登陆人员所属电厂或分公司的名称
	public String getDiancmc(){
		String diancmc="";
		JDBCcon cn = new JDBCcon();
		long diancid=((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc="select d.quanc from diancxxb d where d.id="+diancid;
		ResultSet rs=cn.getResultSet(sql_diancmc);
		try {
			while(rs.next()){
				 diancmc=rs.getString("quanc");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		cn.Close();	
		return diancmc;
		
	}
	private boolean isBegin=false;
	public String getPrintTable(){
		if(mstrReportName.equals(REPORT_ONEGRAPH_KUCZS)){
			return getShouhcChart();
		}else{
			return "无此报表";
		}
	}
	
	private String getGongsID(){
		String strGongsID = "";
//		String danwmc="";//汇总名称
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";
			
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = " and dc.fuid="+this.getTreeid();
			
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID = " and dc.id="+this.getTreeid();
			 
		}else if (jib==-1){
			strGongsID= " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
//		danwmc=getTreeDiancmc(this.getTreeid());
		return strGongsID;
	}
	
//	库存走势图
	public String getShouhcChart(){
		
		Date dat=getBeginriqDate();//日期
		String strDate=DateUtil.FormatDate(dat);//日期字符
		Report rt=new Report();
		
		int jib=this.getDiancTreeJib();
		String diancid = "";
		String groupid = "";
		if(jib==1){//选集团时刷新出所有的电厂
			diancid = " dc.fuid="+this.getTreeid();
			groupid = "dc.fuid";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			diancid = " dc.fuid="+this.getTreeid();
			groupid = "dc.id";
			
		}else if (jib==3){//选电厂只刷新出该电厂
			diancid = " dc.id="+this.getTreeid();
			groupid = "dc.id";
			
		}else if (jib==-1){
			diancid = " dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
			groupid = "dc.id";
		}

		JDBCcon cn = new JDBCcon();
		long lngDays=getDayValue().getId();//显示库存的天数
		String where = getGongsID();
		String danwmc=getTreeDiancmc(this.getTreeid());
		
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("select danw,fenx,dangrgm,haoyqkdr,kuc,jihl,decode(jihl,0,0,round((dangrgm/jihl)*100,2)) as daohl from  \n");
		sbsql.append("(select danw,fenx,dangrgm,haoyqkdr,kuc,decode(fenx,'当日',dangrjhl,'累计',jihl,0) as jihl from \n");
		sbsql.append("(select '"+danwmc+"' as danw,decode(1,1,'当日','') as fenx, \n");
		sbsql.append("       sum(nvl(rb.dangrgm,0)) as dangrgm,sum(nvl(rb.haoyqkdr,0)) as haoyqkdr,sum(nvl(rb.kuc,0)) as kuc \n");
		sbsql.append("  from  (select diancxxb_id,rb.dangrgm,rb.haoyqkdr,rb.kuc from shouhcrbb rb \n");
		sbsql.append("          where  rb.riq=to_date('"+strDate+"','yyyy-mm-dd')) rb,diancxxb dc \n");
		sbsql.append(" where rb.diancxxb_id(+)=dc.id  "+where+" \n");
		sbsql.append("union \n");
		sbsql.append("select '"+danwmc+"' as danw,decode(1,1,'累计','') as fenx, \n");
		sbsql.append("       sum(nvl(rb.dangrgm,0)) as dangrgm,sum(nvl(rb.haoyqkdr,0)) as haoyqkdr,sum(nvl(rb.kuc,0)) as kuc \n");
		sbsql.append("  from  (select diancxxb_id,sum(nvl(rb.dangrgm,0)) as dangrgm,sum(nvl(rb.haoyqkdr,0)) as haoyqkdr,sum(nvl(rb.kuc,0)) as kuc from shouhcrbb rb \n");
		sbsql.append("          where rb.riq >= First_day(to_date('"+strDate+"', 'yyyy-mm-dd')) and rb.riq <= to_date('"+strDate+"', 'yyyy-mm-dd')  \n");
		sbsql.append("          group by rollup(diancxxb_id)) rb,diancxxb dc \n");
		sbsql.append(" where rb.diancxxb_id(+)=dc.id  "+where+" ) sj, \n");
		sbsql.append("(select sum(nvl(sl.hetl,0)) as jihl,round(sum(nvl(sl.hetl,0))/daycount(to_date('"+strDate+"','yyyy-mm-dd'))) as dangrjhl  \n");
		sbsql.append("  from hetslb sl,diancxxb dc,hetb ht   \n");
		sbsql.append(" where sl.hetb_id=ht.id and sl.diancxxb_id=dc.id and dc.jib=3 "+where+" and sl.zhuangt=1  \n");
		sbsql.append("   and sl.riq=to_date('"+strDate+"','yyyy-mm-dd') ) jh ) \n");
		
		String ArrHeader[][]=new String[1][7];
		ArrHeader[0]=new String[] {"单位","单位","来煤量","耗煤量","库存量","计划量","到货率(%)"};

		int ArrWidth[]=new int[] {230,70,100,100,100,100,100};
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		rt.setTitle("收耗存日报",ArrWidth);
		
		rt.setBody(new Table(rs,1,2,2));
		rt.body.setHeaderData(ArrHeader);
		
		rt.body.setWidth(ArrWidth);
		
		int iRows=rt.body.getRows();
		rt.body.setRowCells(iRows-1, Table.PER_FONTSIZE , 1);
		rt.body.setRowHeight(iRows-1, 30);
		rt.body.setRowHeight(iRows, 300);
		rt.body.ShowZero = true;
		rt.body.mergeFixedRow();
		rt.body.setCellValue(2,1, danwmc);
		rt.body.setCellValue(3,1, danwmc);
		rt.body.mergeCell(iRows-1,1,iRows-1,7);
		rt.body.mergeCell(iRows,1,iRows,3);
		rt.body.mergeCell(iRows,4,iRows,7);
		
		rt.body.setBorder(0, 0, 2, 0);
		rt.body.setCells(1, 1, 3, 1, Table.PER_BORDER_LEFT, 2);
		rt.body.setCells(1, 7, 3, 7, Table.PER_BORDER_RIGHT, 2);
		rt.body.setCells(3, 1, 3, 7, Table.PER_BORDER_BOTTOM, 2);
		
		rt.body.setCellBorderLeft(iRows, 1, 1);
		rt.body.setCellBorderRight(iRows-1, 1, 0);
		
		sbsql.setLength(0);
//		柱状图
		sbsql.append("select fx.mingc,fx.fenx,nvl(sj.shul,0) as shuj from   \n");
		sbsql.append("(select dc.mingc as danw,fenx,shul from   \n");
		sbsql.append("(select "+groupid+" as danwid,decode(1,1,'供应','') as fenx,sum(nvl(dangrgm,0)) as shul from shouhcrbb rb,diancxxb dc   \n");
		sbsql.append("where rb.riq=to_date('"+strDate+"','yyyy-mm-dd') and rb.diancxxb_id=dc.id "+where+" group by ("+groupid+")  \n");
		sbsql.append("union  \n");
		sbsql.append("select "+groupid+" as danwid,decode(1,1,'耗用','') as fenx,sum(nvl(haoyqkdr,0)) as shul from shouhcrbb rb,diancxxb dc  \n");
		sbsql.append("where rb.riq=to_date('"+strDate+"','yyyy-mm-dd') and rb.diancxxb_id=dc.id  "+where+" group by ("+groupid+") ) s,diancxxb dc where s.danwid=dc.id  \n");
		sbsql.append("order by fenx )sj,  \n");
		sbsql.append("(select dc.mingc,fx.fenx,fx.xuh from  \n");
		sbsql.append("(select mingc from diancxxb dc where "+diancid+") dc, \n");
		sbsql.append("(select decode(1,1,'供应','') as fenx,1 as xuh from dual union select decode(1,1,'耗用','') as fenx,2 as xuh from dual)  fx ) fx \n");
		sbsql.append("where sj.danw(+)=fx.mingc and sj.fenx(+)=fx.fenx  \n");
		sbsql.append("order by mingc,fx.xuh \n");
		
		rs=cn.getResultSet(sbsql.toString());
		
		ChartData cd = new ChartData();
		Chart ct = new Chart();
		
		CategoryDataset dataset = cd.getRsDataChart(rs, "mingc", "fenx", "shuj");//rs记录集构造生成图片需要的数据
		
		/*--------------设置图片参数开始-------------------*/
		
		ct.intDigits=0;			//	显示小数位数
		ct.barItemMargin=-0.05;
		ct.xTiltShow = true;//倾斜显示X轴的文字
		
		/*--------------设置图片参数结束-------------------*/		
		
		//生成柱状图并显示到页面
		rt.body.setCellValue(iRows,1,ct.ChartBar3D(getPage(), dataset, "煤炭收、耗图", 400, 300));
		
//		时序图
		sbsql.setLength(0);
		sbsql.append("select * from (select * from  \n");
		sbsql.append(" (select bqrq.riq as riq,decode(1,1,'本期库存','') as fenx,nvl(kuc,0) as kuc from   \n");
		sbsql.append(" (select riq,sum(nvl(kuc,0)) as kuc  from shouhcrbb s,diancxxb dc   \n");
		sbsql.append("      where riq > to_date('"+strDate+"','yyyy-mm-dd')-"+lngDays+"   \n");
		sbsql.append("      and  riq <= to_date('"+strDate+"','yyyy-mm-dd') and s.diancxxb_id=dc.id "+where+" group by riq) bqsj,  \n");
		sbsql.append(" (select to_date('"+strDate+"','yyyy-mm-dd')-rownum+1 as riq from diancxxb where rownum<="+lngDays+") bqrq  \n");
		sbsql.append("   where bqrq.riq=bqsj.riq(+)   \n");
		sbsql.append(" union \n");
		sbsql.append("  select add_months(tqrq.riq,12) as riq,decode(1,1,'同期库存','') as fenx,nvl(kuc,0) as kuc from          \n");
		sbsql.append(" (select riq,sum(nvl(kuc,0)) as kuc from shouhcrbb s,diancxxb dc   \n");
		sbsql.append("      where riq > add_months(to_date('"+strDate+"','yyyy-mm-dd')-"+lngDays+",-12)  \n");
		sbsql.append("      and  riq <= add_months(to_date('"+strDate+"','yyyy-mm-dd'),-12) and s.diancxxb_id=dc.id "+where+" group by riq) tqsj,  \n");
		sbsql.append(" (select add_months(to_date('"+strDate+"','yyyy-mm-dd')-rownum+1,-12) as riq from diancxxb dc where rownum<="+lngDays+") tqrq  \n");
		sbsql.append("  where tqrq.riq=tqsj.riq(+)   \n");
		
		if(getDiancTreeJib()==3){//选择电厂显示警戒库存和限负荷库存
			
			sbsql.append("  union  \n");
			sbsql.append("  select rq.riq as riq,decode(1,1,'警戒库存','') as fenx,dc.jingjcml as kuc from diancxxb dc, \n");
			sbsql.append("  (select to_date('"+strDate+"','yyyy-mm-dd')-rownum+1 as riq from diancxxb dc where rownum<="+lngDays+") rq where dc.id="+this.getTreeid()+" \n");
			sbsql.append("   union \n");
			sbsql.append("   select rq.riq as riq,decode(1,1,'限负荷库存','') as fenx,dc.xianfhkc as kuc from diancxxb dc, \n");
			sbsql.append("  (select to_date('"+strDate+"','yyyy-mm-dd')-rownum+1 as riq from diancxxb dc where rownum<="+lngDays+") rq where dc.id="+this.getTreeid()+"  \n");
		}
		sbsql.append("  ) order by riq ) \n");
		
		rs=cn.getResultSet(sbsql.toString());
		
		
		TimeSeriesCollection data2 = cd.getRsDataTimeGraph(rs,  "fenx","riq", "kuc");//rs记录集构造生成图片需要的数据
		
		/*--------------设置图片参数开始-------------------*/
		
		ct.intDigits=0;			//	显示小数位数
		ct.xfontSize=9;		//	x轴字体大小
		ct.dateApeakShowbln = true;//竖直显示x轴的日期
//		ct.lineDateFormatOverride = "dd";//x轴的日期只显示天
		/*--------------设置图片参数结束-------------------*/		
		
		rt.body.setCellValue(iRows,4,ct.ChartTimeGraph(getPage(), data2, "煤炭库存走势图", 400, 300));
		cn.Close();

		return rt.getHtml();
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _LinkChick = false;

	public void LinkButton(IRequestCycle cycle) {
		_LinkChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
		if (_LinkChick) {
			_LinkChick = false;
			PageLink(cycle);
		}
	}

	public void PageLink(IRequestCycle cycle){
		
		cycle.activate("Shouhcreport");
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			this.setTreeid(null);
//			getDayValue();
		}
		
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			
			if(!visit.getString1().equals("")){
				if(!visit.getString1().equals(cycle.getRequestContext().getParameters("lx")[0])){
					visit.setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
					visit.setDropDownBean1(null);
					visit.setProSelectionModel1(null);
					visit.setDropDownBean4(null);
					visit.setProSelectionModel4(null);
					this.setTreeid(null);
				}
			}
			visit.setString1((cycle.getRequestContext().getParameters("lx")[0]));
			mstrReportName = visit.getString1();
        }else{
        	if(!visit.getString1().equals("")) {
        		mstrReportName = visit.getString1();
            }
        }
		getToolBars();
		isBegin=true;
	}

//	 分公司下拉框
	private boolean _fengschange = false;

	public IDropDownBean getFengsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getFengsModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setFengsValue(IDropDownBean Value) {
		if (getFengsValue().getId() != Value.getId()) {
			_fengschange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public IPropertySelectionModel getFengsModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getFengsModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setDiancxxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getFengsModels() {
		String sql;
		sql = "select id ,mingc from diancxxb where jib=2 order by id";
		setDiancxxModel(new IDropDownModel(sql,"中电投"));
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
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;
	}
	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
		df.Binding("riqDateSelect","forms[0]");// 与html页中的id绑定,并自动刷新
		df.setWidth(100);
		//df.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("天数:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("TIANS");
		cb.setWidth(60);
//		cb.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb);
		tb1.addText(new ToolbarText("-"));
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		ToolbarButton tbLink = new ToolbarButton(null,"查询数据","function(){document.getElementById('LinkButton').click();}");
		
		tb1.addItem(tb);
		tb1.addItem(tbLink);
		setToolbar(tb1);
	}
	
	
//	电厂名称
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
			_DiancmcValue=(IDropDownBean)getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		String sql="";
		sql = "select d.id,d.mingc from diancxxb d order by d.mingc desc";
		
		_IDiancmcModel = new IDropDownModel(sql);
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

	private String treeid;

	/*public String getTreeid() {
		if (treeid == null || "".equals(treeid)) {
			return "-1";
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}
	
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
	
//  Day下拉框
	private boolean _Day = false;
	private IDropDownBean _DayValue;
	private IPropertySelectionModel _DayModel;

	public IDropDownBean getDayValue() {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean) getDayModel().getOption(14));
    	}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}
	public void setDayValue(IDropDownBean Value) {
//		if(((Visit)getPage().getVisit()).getDropDownBean1()!=null){
			if(((Visit)getPage().getVisit()).getDropDownBean1().getId()!=Value.getId()){
				_Day=true;
			}
//		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setDayModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
    }
    public IPropertySelectionModel getDayModel() {
        if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
            getDayModels();
        }
        return ((Visit)getPage().getVisit()).getProSelectionModel1();
    }
    public void getDayModels() {
        List listDay = new ArrayList();
//        listDay.add(new IDropDownBean(-1, "请选择"));
    	for (int i = 1; i < 32; i++) {
            listDay.add(new IDropDownBean(i, String.valueOf(i)+"天"));
        }
    	((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(listDay));
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
	
	
}
