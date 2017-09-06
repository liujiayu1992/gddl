package com.zhiren.jt.zdt.rulgl.rulmzlys;

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
import com.zhiren.common.SysConstant;
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

public class Rulmzlys extends BasePage {
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
	
	private void setReturnValue(){
		String strDiancid=getTreeid();
		Visit visit=((Visit) getPage().getVisit());
		long diancxxb_id=visit.getDiancxxb_id();
		String strleix=getLeixDropDownValue().getValue();
		
		String strOldId="";
		if (String.valueOf(diancxxb_id).equals(strDiancid)){
			if (strleix.equals("分矿")){
				if (!"-1".equals(visit.getString3())){
					visit.setString3("-1");
				}
			}
			return;
		}
		
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select fuid from diancxxb d where d.id="+ strDiancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				strOldId = rs.getString("fuid");
				if (strOldId.equals("0")){
					return;
				}
			}
			setTreeid(strOldId);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
	}
	
//	开始日期
//	private Date _BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (((Visit)getPage().getVisit()).getDate1()==null){
			((Visit)getPage().getVisit()).setDate1(DateUtil.AddDate(DateUtil.getFirstDayOfMonth(new Date()), 0, DateUtil.AddType_intDay));
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
	
	private boolean _EndriqChange=false;
	public Date getEndriqDate() {
		if (((Visit)getPage().getVisit()).getDate2()==null){
			((Visit)getPage().getVisit()).setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate2();
	}
	
	public void setEndriqDate(Date _value) {
		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate2()).equals(DateUtil.FormatDate(_value))) {
			_EndriqChange=false;
		} else {
			((Visit)getPage().getVisit()).setDate2(_value);
			_EndriqChange=true;
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
	
	public void setGongysId(String _value){
		 ((Visit) getPage().getVisit()).setString3(_value);
	}
	
	public String getGongysId(){
		Visit visit = (Visit) getPage().getVisit();
		
		return visit.getString3();
	}
	
	private String REPORT_ONEGRAPH_RUCMZLYS="rulmzlys";
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
			e.printStackTrace();
		}
		cn.Close();
		return diancmc;
	}
	private boolean isBegin=false;
	public String getPrintTable(){
		if(! isBegin){
			return "";
		}
		if(mstrReportName.equals(REPORT_ONEGRAPH_RUCMZLYS)){
			return getRulmzlys();
		}else{
			return "无此报表";
		}
	}
	
	public String getRulmzlys(){
		Date dat1=getBeginriqDate();//日期
		String strDate1=DateUtil.FormatDate(dat1);//日期字符
		
		Date dat2=getEndriqDate();//日期
		String strDate2=DateUtil.FormatDate(dat2);//日期字符
		
		Report rt=new Report();
		
		int jib=this.getDiancTreeJib();
		
		String diancid = "";
		String strgongs = "";
		String strGroupID = "";
		String strQLeix="厂";
		String strCondition="";
		
		if(jib==1){//选集团时刷新出所有的电厂
			strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc from vwdianc dc ";//取得集团下的所有分公司
			strGroupID = "dc.fgsid";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strCondition =strCondition+ " and dc.fgsid="+this.getTreeid();
			strgongs = "select distinct dc.id,dc.mingc from vwdianc dc where dc.fgsid="+this.getTreeid();//取得分公司下的所有电厂
			strGroupID = "dc.id";
		}else if (jib==3){//选电厂只刷新出该电厂
			strCondition =strCondition+ " and fh.diancxxb_id="+this.getTreeid();	
			strgongs="select distinct y.dqmc as mingc,y.dqid as id from\n" +
				"        rulmzlb fh,vwdianc dc\n" + 
				"        where dc.id=fh.diancxxb_id \n" + strCondition+
				"        and fh.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd')\n" + 
				"        and fh.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd')\n" ;
			strGroupID = " y.dqid ";
			strQLeix="矿";
		}else if (jib==-1){
			diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
			strgongs = "select distinct  id, mingc from vwdianc dc where id="+this.getTreeid();
			strGroupID = "dc.id";
		}
		
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append(" select getAlink('"+strQLeix+"',decode(grouping(dc.mingc),1,-1,max(dc.id)) ,  \n");
		sbsql.append("       decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danwmc,  \n");
		sbsql.append("       nvl(sum(sj.haoyl),0) as haoyl,  \n");
		sbsql.append("       nvl(round(decode(sum(sj.haoyl),0,0,sum(sj.mt*sj.haoyl)/sum(sj.haoyl)),2),0) as mt,  \n");
		sbsql.append("       nvl(round(decode(sum(sj.haoyl),0,0,sum(sj.ad*sj.haoyl)/sum(sj.haoyl)),2),0) as ad,  \n");
		sbsql.append("       nvl(round(decode(sum(sj.haoyl),0,0,sum(sj.vdaf*sj.haoyl)/sum(sj.haoyl)),2),0) as vdaf,  \n");
		sbsql.append("       nvl(round(decode(sum(sj.haoyl),0,0,sum(sj.stad*sj.haoyl)/sum(sj.haoyl)),2),0) as stad,  \n");
		sbsql.append("       nvl(round(decode(sum(sj.haoyl),0,0,sum(sj.qnet_ar*sj.haoyl)/sum(sj.haoyl)),2),0) as qnet_ar,  \n");
		sbsql.append("       nvl(round(decode(sum(sj.haoyl),0,0,sum((sj.qnet_ar/0.0041816)*sj.haoyl)/sum(sj.haoyl))),0) as qnet_ar1 \n");
		sbsql.append(" from ( "+strgongs+" ) dc, \n");
		sbsql.append("       (select "+strGroupID+" as id, nvl(sum(fadhy+gongrhy),0) as haoyl,\n");
		sbsql.append("              round(decode(sum(fadhy+gongrhy),0,0,sum(mt*(fadhy+gongrhy))/sum(fadhy+gongrhy)),2) as mt, \n");
		sbsql.append("              round(decode(sum(fadhy+gongrhy),0,0,sum(ad*(fadhy+gongrhy))/sum(fadhy+gongrhy)),2) as ad, \n");
		sbsql.append("              round(decode(sum(fadhy+gongrhy),0,0,sum(vdaf*(fadhy+gongrhy))/sum(fadhy+gongrhy)),2) as vdaf, \n");
		sbsql.append("              round(decode(sum(fadhy+gongrhy),0,0,sum(stad*(fadhy+gongrhy))/sum(fadhy+gongrhy)),2) as stad, \n");
		sbsql.append("              round(decode(sum(fadhy+gongrhy),0,0,sum(qnet_ar*(fadhy+gongrhy))/sum(fadhy+gongrhy)),2) as qnet_ar,");
		sbsql.append("              round(decode(sum(fadhy+gongrhy),0,0,sum((qnet_ar/0.0041816)*(fadhy+gongrhy))/sum(fadhy+gongrhy))) as qnet_ar1");
		sbsql.append("       from rulmzlb r,meihyb m,vwdianc dc  \n");
		sbsql.append("       where m.rulmzlb_id=r.id(+) \n");
		sbsql.append("         and r.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd')  \n");
		sbsql.append("         and r.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd')  \n").append(strCondition).append(diancid);
		sbsql.append("         and r.diancxxb_id=dc.id    \n");
		sbsql.append("         group by "+strGroupID+") sj  \n");
		sbsql.append(" where sj.id(+)=dc.id  \n");
		sbsql.append(" group by rollup(dc.mingc)  \n");
		sbsql.append(" order by grouping(dc.mingc),dc.mingc  \n");
		
		String ArrHeader[][]=new String[2][8];
		ArrHeader[0]=new String[] {"单位","入炉煤量<br>(吨)","水分<br>Mt(%)","灰分<br>Ad(%)","挥发分<br>Vdaf(%)","硫分<br>St,d(%)","低位发热量","低位发热量"};
		ArrHeader[1]=new String[] {"单位","入炉煤量<br>(吨)","水分<br>Mt(%)","灰分<br>Ad(%)","挥发分<br>Vdaf(%)","硫分<br>St,d(%)","MJ/kg","Kcal/Kg"};

		int ArrWidth[]=new int[] {200,80,80,80,80,80,80,80};
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		rt.setCenter(false);
		rt.setBody(new Table(rs,2,0,1));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setFontSize(12);
		
		rt.body.setUseCss(true);
		rt.body.setColHeaderClass("tab_colheader");
		rt.body.setRowHeaderClass("tab_rowheader");
		rt.body.setFirstDataRowClass("tab_data_line_one");
		rt.body.setSecondDataRowClass("tab_data_line_two");
		rt.body.setCellsClass("tab_cells");
		rt.body.setTableClass("tab_body");
		
		rt.body.setBorder(2);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.body.ShowZero = true;
		
		cn.Close();
		return rt.getHtml();
	}
	
	//比例图
	public String getChart3D(){
		Date dat1=getBeginriqDate();//日期
		String strleix="分厂";//getLeixDropDownValue().getValue();
		String strDate1=DateUtil.FormatDate(dat1);//日期字符
		
		Date dat2=getEndriqDate();//日期
		String strDate2=DateUtil.FormatDate(dat2);//日期字符
		int jib=this.getDiancTreeJib();
		
		String diancid = "";
		String strgongs = "";
		String strGroupID = "";
		String strQLeix="厂";
		String strCondition="";
		
		if (getYunsfsDropDownValue().getId()!=-1){
			strCondition ="and fh.yunsfsb_id=" +getYunsfsDropDownValue().getId() ;//选择运输方式
		}
		
		if (-1!=(getJihkjDropDownValue().getId())){
			strCondition=strCondition+" and fh.jihkjb_id=" +getJihkjDropDownValue().getId();
		}
		
		if (!"-1".equals(getGongysId())){
			strCondition=strCondition+" and y.dqid=" +getGongysId();
		}
		
		if  (strleix.equals("分厂")){
			if(jib==1){//选集团时刷新出所有的电厂
				strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc from vwdianc dc ";//取得集团下的所有分公司
				strGroupID = "dc.fgsid";
			}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
				strCondition =strCondition+ " and dc.fgsid="+this.getTreeid();
				strgongs = "select distinct dc.id,dc.mingc from vwdianc dc where dc.fgsid="+this.getTreeid();//取得分公司下的所有电厂
				strGroupID = "dc.id";
			}else if (jib==3){//选电厂只刷新出该电厂
				strCondition =strCondition+ " and fh.diancxxb_id="+this.getTreeid();	
				strgongs="select distinct dc.mingc as mingc,y.dqid as id from\n" +
					"        rulmzlb r,vwdianc dc\n" + 
					"        where dc.id=r.diancxxb_id \n" + strCondition+
					"        and r.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd')\n" + 
					"        and r.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd')\n" ;
				strGroupID = " dc.id ";
				strQLeix="矿";
			}else if (jib==-1){
				diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
				strgongs = "select distinct  id, mingc from vwdianc dc where id="+this.getTreeid();
				strGroupID = "dc.id";
			}
		}else{
			if(jib==1){//选集团时刷新出所有的电厂
				diancid = "";
				strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc from vwdianc dc";//取得集团下的所有分公司
				strGroupID = "dc.fgsid";
			}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
				strCondition =strCondition+ " and dc.fgsid="+this.getTreeid();
				strgongs = "select distinct dc.id,mingc from vwdianc dc where dc.fgsid="+this.getTreeid();//取得分公司下的所有电厂
				strGroupID = "dc.id";
			}else if (jib==3){//选电厂只刷新出该电厂
				strCondition =strCondition+ " and dc.id="+this.getTreeid();
				strGroupID="dc.id";
			}else if (jib==-1){
				strCondition =strCondition+ " and dc.id="+this.getTreeid();
				strGroupID = "dc.id";
			}
			//取得某日期段的供货单位		
			if ("-1".equals(getGongysId())){
				strgongs="select distinct dc.mingc as mingc,y.dqid as id from\n" +
				"        rulmzlb r,vwdianc dc\n" + 
				"        where dc.id=r.diancxxb_id \n" + strCondition+
				"        and r.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd')\n" + 
				"        and r.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd')\n" ;
				
				strGroupID = "dc.id";
				strQLeix="矿";
			}
		}
		
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		 
		sbsql.append(" select dc.mingc as danwmc,  \n");
		sbsql.append(" nvl((decode(sum(sj.laimzl),0,0,Round(sum(sj.farl*sj.laimzl)/sum(sj.laimzl),2))-decode(sum(sj1.rulml),0,0,Round(sum(sj1.farl*sj1.rulml)/sum(sj1.rulml),2))),0) as tiaozq_mj , \n");
		sbsql.append(" nvl(Round((decode(sum(sj.laimzl),0,0,Round(sum(sj.farl*sj.laimzl)/sum(sj.laimzl),2))-decode(sum(sj1.rulml),0,0,Round(sum(sj1.farl*sj1.rulml)/sum(sj1.rulml),2)))*1000/4.1816,0),0) as tiaozq_dk  \n");
		sbsql.append(" from ( "+strgongs+" ) dc,  \n");
		sbsql.append("       (select "+strGroupID+" as id, sum(f.laimsl) as laimsl,sum(f.laimzl) as laimzl,  \n");
		sbsql.append(" decode(sum(decode(nvl(z.qnet_ar,0),0,0,f.laimzl)),0,0,sum(z.qnet_ar*f.laimzl)/sum(decode(nvl(z.qnet_ar,0),0,0,f.laimzl))) as farl \n");
		sbsql.append(" from fahb f,zhilb z,vwdianc dc  \n");
		sbsql.append(" where f.zhilb_id=z.id(+) and f.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd') and f.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd')  \n");
		sbsql.append(" and dc.id(+)=f.diancxxb_id  \n");
		sbsql.append(" group by "+strGroupID+") sj,  \n");
		sbsql.append("       (select dc.fgsid as id, sum(h.fadhy+h.gongrhy) as rulml,  \n");
		sbsql.append(" decode(sum(decode(nvl(mz.qnet_ar,0),0,0,h.fadhy+h.gongrhy)),0,0,sum(mz.qnet_ar*(h.fadhy+h.gongrhy))/sum(decode(nvl(mz.qnet_ar,0),0,0,h.fadhy+h.gongrhy))) as farl \n");
		sbsql.append(" from rulmzlb mz,meihyb h,vwdianc dc  \n");
		sbsql.append(" where h.rulmzlb_id=mz.id(+) and h.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd') and h.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd')  \n");
		sbsql.append(" and dc.id(+)=mz.diancxxb_id  \n");
		sbsql.append(" group by "+strGroupID+") sj1  \n");
		sbsql.append(" where sj.id(+)=dc.id and sj1.id(+)=dc.id   \n");
		sbsql.append(" group by dc.mingc order by max(dc.xuh) \n");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//图片背景颜色
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		
		DefaultPieDataset dataset = cd.getRsDataPie(rs, "danwmc", "tiaozq_mj", true);
		
		ct.showLegend=false;
		ct.chartBackgroundPaint=gp;
		ct.pieLabFormat=false;//不显示数据内容
		ct.setImgEvents(" onmouseleave=\"MM_showHideLayers('tubiaoLayer','','hide')\"   onmouseenter=\"MM_showHideLayers('tubiaoLayer','imgChart3D','show')\"");
		String charImg=ct.ChartPie3D(getPage(),dataset, "", 200, 120);
		ct.setID("imgChart3D");
		ct.showLegend=true;
		ct.pieLabFormat=true;//不显示数据内容
		ct.pieLabGenerator=ct.piedatformat2;//显示百分比
		ct.setImgEvents("");
		((Visit) getPage().getVisit()).setString7(ct.ChartPie3D(getPage(),dataset, "", 600, 300));
		
		return charImg;
	}
	
	public String getChart3DBig(){
		return ((Visit) getPage().getVisit()).getString7();
	}
	
//	柱型图
	public String getChartBar(){
		Date dat1=getBeginriqDate();//日期
		String strleix="分厂";//getLeixDropDownValue().getValue();
		String strDate1=DateUtil.FormatDate(dat1);//日期字符
		
		Date dat2=getEndriqDate();//日期
		String strDate2=DateUtil.FormatDate(dat2);//日期字符
		int jib=this.getDiancTreeJib();
		
		String diancid = "";
		String strgongs = "";
		String strGroupID = "";
		String strQLeix="厂";
		String strCondition="";
		String strDataField=getDataField();
		
		/*if (getYunsfsDropDownValue().getId()!=-1){
			strCondition ="and fh.yunsfsb_id=" +getYunsfsDropDownValue().getId() ;//选择运输方式
		}
		
		if (-1!=(getJihkjDropDownValue().getId())){
			strCondition=strCondition+" and fh.jihkjb_id=" +getJihkjDropDownValue().getId();
		}
		
		if (!"-1".equals(getGongysId())){
			strCondition=strCondition+" and y.dqid=" +getGongysId();
		}*/
		
		if  (strleix.equals("分厂")){
			if(jib==1){//选集团时刷新出所有的电厂
				strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc from vwdianc dc ";//取得集团下的所有分公司
				strGroupID = "dc.fgsid";
			}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
				strCondition =strCondition+ " and dc.fgsid="+this.getTreeid();
				strgongs = "select distinct dc.id,dc.mingc from vwdianc dc where dc.fgsid="+this.getTreeid();//取得分公司下的所有电厂
				strGroupID = "dc.id";
			}else if (jib==3){//选电厂只刷新出该电厂
				strCondition =strCondition+ " and fh.diancxxb_id="+this.getTreeid();	
				strgongs="select distinct dc.mingc as mingc,dc.id as id from\n" +
					"        rulmzlb r,vwdianc dc\n" + 
					"        where dc.id=r.diancxxb_id \n" + strCondition+
					"        and r.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd')\n" + 
					"        and r.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd')\n" ;
				strGroupID = " dc.id ";
				strQLeix="矿";
			}else if (jib==-1){
				diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
				strgongs = "select distinct  id, mingc from vwdianc dc where id="+this.getTreeid();
				strGroupID = "dc.id";
			}
		}else{
			if(jib==1){//选集团时刷新出所有的电厂
				diancid = "";
				strgongs = "select distinct dc.fgsid as id,dc.fgsmc as mingc from vwdianc dc";//取得集团下的所有分公司
				strGroupID = "dc.fgsid";
			}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
				strCondition =strCondition+ " and dc.fgsid="+this.getTreeid();
				strgongs = "select distinct dc.id,mingc from vwdianc dc where dc.fgsid="+this.getTreeid();//取得分公司下的所有电厂
				strGroupID = "dc.id";
			}else if (jib==3){//选电厂只刷新出该电厂
				strCondition =strCondition+ " and dc.id="+this.getTreeid();
				strGroupID="dc.id";
			}else if (jib==-1){
				strCondition =strCondition+ " and dc.id="+this.getTreeid();
				strGroupID = "dc.id";
			}
			//取得某日期段的供货单位		
			if ("-1".equals(getGongysId())){
				strgongs="select distinct dc.mingc as mingc,dc.id as id from\n" +
				"        rulmzlb r,vwdianc dc\n" + 
				"        where dc.id=r.diancxxb_id \n" + strCondition+
				"        and r.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd')\n" + 
				"        and r.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd')\n" ;
				
				strGroupID = "dc.id";
				strQLeix="矿";
			}
		}
		
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" select dc.mingc as danwmc, decode(1,1,'本期','本期') as fenx,nvl(haoyl,0) as haoyl  \n");
		sbsql.append(" from ( "+strgongs+" ) dc, \n");
		sbsql.append("       (select "+strGroupID+" as id,");
		sbsql.append(strDataField);
		sbsql.append("       from rulmzlb r,meihyb m,vwdianc dc  \n");
		sbsql.append("       where m.rulmzlb_id=r.id  \n");
		sbsql.append("         and r.rulrq>=to_date('"+strDate1+"','yyyy-mm-dd')  \n");
		sbsql.append("         and r.rulrq<=to_date('"+strDate2+"','yyyy-mm-dd')  \n").append(strCondition).append(diancid);
		sbsql.append("         and r.diancxxb_id=dc.id    \n");
		sbsql.append("         group by "+strGroupID+") sj  \n");
		sbsql.append(" where sj.id(+)=dc.id  \n");
		sbsql.append(" union all  \n");
		sbsql.append(" select dc.mingc as danwmc, decode(1,1,'同期','同期') as fenx,nvl(haoyl,0) haoyl  \n");
		sbsql.append(" from ( "+strgongs+" ) dc, \n");
		sbsql.append("       (select "+strGroupID+" as id,");
		sbsql.append(strDataField);
		sbsql.append("       from rulmzlb r,meihyb m,vwdianc dc  \n");
		sbsql.append("       where m.rulmzlb_id=r.id  \n");
		sbsql.append("         and r.rulrq>=add_months(to_date('"+strDate1+"','yyyy-mm-dd'),-12)  \n");
		sbsql.append("         and r.rulrq<=add_months(to_date('"+strDate2+"','yyyy-mm-dd'),-12)  \n").append(strCondition).append(diancid);
		sbsql.append("         and r.diancxxb_id=dc.id    \n");
		sbsql.append("         group by "+strGroupID+") sj  \n");
		sbsql.append(" where sj.id(+)=dc.id  \n");
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//图片背景颜色
		CategoryDataset dataset = cd.getRsDataChart(rs, "danwmc", "fenx", "haoyl");
		ct.intDigits=getDigts();				//	显示小数位数
		ct.barItemMargin=-0.05;
		ct.barLabelsFontbln = false;
		ct.showXvalue = false;
		ct.showYvalue = false;
		ct.showLegend = false;
		ct.chartBackgroundPaint=gp;
		//return ct.ChartBar3D(getPage(), dataset, "", 200, 120);
		
		ct.setImgEvents(" onmouseleave=\"MM_showHideLayers('tubiaoLayer','','hide')\"   onmouseenter=\"MM_showHideLayers('tubiaoLayer','imgChartBar','show')\" ");
		String charImg=ct.ChartBar3D(getPage(),dataset, "", 200, 120);
		ct.setID("imgChartBar");
		ct.barLabelsFontbln = true;
		ct.showXvalue = true;
		ct.showYvalue = true;
		ct.xTiltShow = true;		//倾斜显示X轴的文字
		ct.showLegend = true;
		ct.setImgEvents("");
		
		((Visit) getPage().getVisit()).setString5(ct.ChartBar3D(getPage(),dataset, "", 600, 300));
		
		return charImg;
	}
	
	public String getChartBarBig(){
		return ((Visit) getPage().getVisit()).getString5();
	}
	
	
//	曲线图
	public String getChartLine(){
		String strDateBegin=DateUtil.FormatDate(DateUtil.getFirstDayOfYear(getBeginriqDate()));//日期字符
		String strDateEnd=DateUtil.FormatDate(DateUtil.getLastDayOfYear(getBeginriqDate()));//日期字符
		
		int jib=this.getDiancTreeJib();
		String strCondition="";
		String strPictureFile="";
		String strDataField=getDataField();
		
	/*	if (getYunsfsDropDownValue().getId()!=-1){
			strCondition ="and fh.yunsfsb_id=" +getYunsfsDropDownValue().getId() ;//选择运输方式
		}
		
		if (-1!=(getJihkjDropDownValue().getId())){
			strCondition=strCondition+" and fh.jihkjb_id=" +getJihkjDropDownValue().getId();
		}
		
		if (!"-1".equals(getGongysId())){
			strCondition=strCondition+" and y.dqid=" +getGongysId();
		}*/
		
		if(jib==1){//选集团时刷新出所有的电厂
			
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strCondition =strCondition+ " and dc.fgsid="+this.getTreeid();
		}else if (jib==3){//选电厂只刷新出该电厂
			strCondition =strCondition+ " and dc.id="+this.getTreeid();
		}else if (jib==-1){
			strCondition =strCondition+ " and dc.id="+this.getTreeid();
		}
		
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select to_date('2008-'||to_char(bq.yuef)||'-01','yyyy-mm-dd') as yuef,bq.fenx,nvl(haoyl,0) as haoyl  \n");
		sbsql.append("  from (select to_char(r.rulrq,'mm') as yuef, \n");
		sbsql.append(strDataField);
		sbsql.append("           from rulmzlb r,meihyb m,vwdianc dc  \n");
		sbsql.append("        where m.rulmzlb_id=r.id   \n");
		sbsql.append("              and r.rulrq>=to_date('"+strDateBegin+"','yyyy-mm-dd')   \n");
		sbsql.append("              and r.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
		sbsql.append("              and r.diancxxb_id=dc.id     \n").append(strCondition);
		sbsql.append("         group by to_char(r.rulrq,'mm')) bqsj, \n");
		sbsql.append("         (select trim(to_char(rownum,'00')) as yuef,rownum as yf,decode(1,1,'本期') as fenx \n");
		sbsql.append("           from xitxxb  where rownum<=12) bq \n");
		sbsql.append(" where bq.yuef=bqsj.yuef(+) \n");
		sbsql.append(" union all \n");
		sbsql.append(" select  to_date('2008-'||to_char(bq.yuef)||'-01','yyyy-mm-dd') as yuef,bq.fenx,nvl(haoyl,0) as haoyl  \n");
		sbsql.append("  from (select to_char(r.rulrq,'mm') as yuef, \n");
		sbsql.append(strDataField);
		sbsql.append("               from rulmzlb r,meihyb m,vwdianc dc \n");
		sbsql.append("        where m.rulmzlb_id=r.id   \n");
		sbsql.append("              and r.rulrq>=to_date('"+strDateBegin+"','yyyy-mm-dd')   \n");
		sbsql.append("              and r.rulrq<=to_date('"+strDateEnd+"','yyyy-mm-dd')   \n");
		sbsql.append("              and r.diancxxb_id=dc.id     \n").append(strCondition);
		sbsql.append("         group by to_char(r.rulrq,'mm')) bqsj  , \n");
		sbsql.append("         (select trim(to_char(rownum,'00')) as yuef,rownum as yf,decode(1,1,'同期') as fenx \n");
		sbsql.append("           from xitxxb  where rownum<=12) bq \n");
		sbsql.append("  where bq.yuef=bqsj.yuef(+) \n");
		sbsql.append(" order by yuef,fenx \n");
		
		ResultSet rs = cn.getResultSet(sbsql.toString());
		
		Chart ct = new Chart();
		ChartData cd = new ChartData();
		GradientPaint gp=new GradientPaint(0, 0,  Color.white, 500, 0, Color.white);//图片背景颜色
		TimeSeriesCollection data2 = cd.getRsDataTimeGraph(rs,  "fenx","yuef", "haoyl");//rs记录集构造生成图片需要的数据
		ct.intDigits=getDigts();
		ct.lineDateFormatOverride="MM";
		ct.setDateUnit(Chart.DATEUNIT_MONTH, 1);
		ct.chartBackgroundPaint=gp;
		ct.showXvalue = false;
		ct.showYvalue = false;
		ct.showLegend = false;
		ct.setImgEvents(" onmouseleave=\"MM_showHideLayers('tubiaoLayer','','hide')\"   onmouseenter=\"MM_showHideLayers('tubiaoLayer','imgChartLine','show')\"");
		String charImg=ct.ChartTimeGraph(getPage(),data2, "", 200, 120);
		ct.setID("imgChartLine");
		ct.showXvalue = true;
		ct.showYvalue = true;
		ct.xTiltShow = true;		//倾斜显示X轴的文字
		ct.showLegend = true;
		ct.setImgEvents("");
		((Visit) getPage().getVisit()).setString6(ct.ChartTimeGraph(getPage(),data2, "", 600, 300));
		
		cn.Close();
		return charImg;
	}
	
	public String getChartLineBig(){
		return ((Visit) getPage().getVisit()).getString6();
	}
	
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _ReturnClick = false;
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			return;
		}
		if (_ReturnClick){
			_ReturnClick=false;
			setReturnValue();
			return;
		}
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setString3("-1");
			
			visit.setDate1(DateUtil.AddDate(DateUtil.getFirstDayOfMonth(new Date()), 0, DateUtil.AddType_intDay));
			visit.setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
			visit.setDropDownBean1(null);
			visit.setDropDownBean2(null);
			visit.setDropDownBean3(null);
			visit.setDropDownBean4(null);
			visit.setDropDownBean5(null);
			
			visit.setProSelectionModel1(null);
			visit.setProSelectionModel2(null);
			visit.setProSelectionModel3(null);
			visit.setProSelectionModel4(null);
			visit.setProSelectionModel5(null);
			this.setTreeid(null);
//			getDayValue();
		}
		
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			if(!visit.getString1().equals("")){
				if(!visit.getString1().equals(cycle.getRequestContext().getParameters("lx")[0])){
					visit.setDate1(DateUtil.AddDate(DateUtil.getFirstDayOfMonth(new Date()),0, DateUtil.AddType_intDay));
					visit.setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
					visit.setDropDownBean1(null);
					visit.setDropDownBean2(null);
					visit.setDropDownBean3(null);
					visit.setDropDownBean4(null);
					visit.setDropDownBean5(null);
					
					visit.setProSelectionModel1(null);
					visit.setProSelectionModel2(null);
					visit.setProSelectionModel3(null);
					visit.setProSelectionModel4(null);
					visit.setProSelectionModel5(null);
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
		df.setWidth(60);
 		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("至:"));
		DateField edf = new DateField();
		edf.setValue(DateUtil.FormatDate(this.getEndriqDate()));
		edf.Binding("EndriqDateSelect","forms[0]");// 与html页中的id绑定,并自动刷新
		edf.setWidth(60);
		tb1.addField(edf);
		tb1.addText(new ToolbarText("-"));
		
//		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
//		setTree(etu);
//		
//		TextField tf = new TextField();
//		tf.setId("diancTree_text");
//		tf.setWidth(100);
//		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
//		
//		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
//		tb2.setIcon("ext/resources/images/list-items.gif");
//		tb2.setCls("x-btn-icon");
//		tb2.setMinWidth(20);
		
		/*tb1.addText(new ToolbarText("类型:"));
		ComboBox tblx = new ComboBox();
		tblx.setTransform("LeixDropDown");
		tblx.setWidth(60);
		tb1.addField(tblx);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("计划口径:"));
		ComboBox tbjhkj = new ComboBox();
		tbjhkj.setTransform("JihkjDropDown");
		tbjhkj.setWidth(80);
		tb1.addField(tbjhkj);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("运输方式:"));
		ComboBox tbysfs = new ComboBox();
		tbysfs.setTransform("YunsfsDropDown");
		tbysfs.setWidth(60);
		tb1.addField(tbysfs);
		tb1.addText(new ToolbarText("-"));*/
		
		
		tb1.addText(new ToolbarText("图表数据:"));
		ComboBox tbChart = new ComboBox();
		tbChart.setTransform("ChartDropDown");
		tbChart.setWidth(90);
		tb1.addField(tbChart);
		tb1.addText(new ToolbarText("-"));
		
//		tb1.addText(new ToolbarText("单位:"));
//		tb1.addField(tf);
//		tb1.addItem(tb2);
//		tb1.addText(new ToolbarText("-"));
	

		ToolbarButton tbok = new ToolbarButton(null,"确定","function(){document.Form0.submit();}");
		tbok.setIcon(SysConstant.Btn_Icon_SelSubmit);
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Refurbish);
		
		tb1.addItem(tbok);
		tb1.addItem(tb);
		
		String strDiancid=getTreeid();
		Visit visit=((Visit) getPage().getVisit());
		long diancxxb_id=visit.getDiancxxb_id();
		String diancxxbid=""+diancxxb_id;
		System.out.println("树="+strDiancid+"，电厂="+diancxxbid);
		
		if(!strDiancid.equals(diancxxbid)){
			ToolbarButton tbfh = new ToolbarButton(null,"返回上级","function(){document.getElementById('ReturnButton').click();}");
			tbfh.setIcon(SysConstant.Btn_Icon_Return);
			tb1.addItem(tbfh);
		}
		
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
		Visit visit =((Visit) getPage().getVisit());
		visit.setString2(treeid);
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
	

//	类型
	public IDropDownBean getLeixDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean) getLeixDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setLeixDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setLeixDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getLeixDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getLeixDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getLeixDropDownModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "分矿"));
		list.add(new IDropDownBean(2, "分厂"));
		//list.add(new IDropDownBean(3, "棋盘表"));
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(list));
		return;
	}
	
//	计划口径
	public IDropDownBean getJihkjDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean) getJihkjDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setJihkjDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setJihkjfsDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getJihkjDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getJihkjDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getJihkjDropDownModels() {
		String sql = "select id,mingc\n" + "from jihkjb \n";
		((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql, "全部"));
		return;
	}	
	
//	运输方式
	public IDropDownBean getYunsfsDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit()).setDropDownBean3((IDropDownBean) getYunsfsDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setYunsfsDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setYunsfsDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getYunsfsDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getYunsfsDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getYunsfsDropDownModels() {
		String sql = "select id,mingc\n" + "from yunsfsb \n";
		((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql, "全部"));
		return;
	}
	
//	 供应商
	public IDropDownBean getGongysDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getGongysDropDownModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setGongysDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setGongysDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getGongysDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getGongysDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getGongysDropDownModels() {
		String sql = "select id,mingc\n" + "from gongysb\n";
		((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql, "全部"));
		return;
	}
	
//	类型
	public IDropDownBean getChartDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit()).setDropDownBean6((IDropDownBean) getChartDropDownModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setChartDropDownValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean6(Value);
	}

	public void setChartDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getChartDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getChartDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void getChartDropDownModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "低位发热量"));
		list.add(new IDropDownBean(2, "全水分mt(%)"));
		list.add(new IDropDownBean(3, "挥发份vdaf(%)"));
		list.add(new IDropDownBean(4, "灰份ad(%)"));
		list.add(new IDropDownBean(5, "硫份std(%)"));
		
		((Visit) getPage().getVisit()).setProSelectionModel6(new IDropDownModel(list));
		return;
	}
	
	private String getDataField(){
		String strDataField=getChartDropDownValue().getValue();
		if (strDataField.equals("低位发热量")){
			return " round(decode(sum(fadhy+gongrhy),0,0,sum((qnet_ar/0.0041816)*(fadhy+gongrhy))/sum(fadhy+gongrhy))) as haoyl \n";
		}else if(strDataField.equals("全水分mt(%)")){
			return "  round(decode(sum(fadhy+gongrhy),0,0,sum(mt*(fadhy+gongrhy))/sum(fadhy+gongrhy)),2) as haoyl \n";
		}else if(strDataField.equals("挥发份vdaf(%)")){
			return " round(decode(sum(fadhy+gongrhy),0,0,sum(vdaf*(fadhy+gongrhy))/sum(fadhy+gongrhy)),2) as haoyl \n";
		}else if(strDataField.equals("灰份ad(%)")){
			return " round(decode(sum(fadhy+gongrhy),0,0,sum(ad*(fadhy+gongrhy))/sum(fadhy+gongrhy)),2) as haoyl \n";
		}else if(strDataField.equals("硫份std(%)")){
			return " round(decode(sum(fadhy+gongrhy),0,0,sum(stad*(fadhy+gongrhy))/sum(fadhy+gongrhy)),2) as haoyl \n";
		}
		return " round(decode(sum(fadhy+gongrhy),0,0,sum((qnet_ar/0.0041816)*(fadhy+gongrhy))/sum(fadhy+gongrhy))) as haoyl \n";
	}
	
	private int getDigts(){
		String strDataField=getChartDropDownValue().getValue();
		if (strDataField.equals("低位发热量")){
			return 0;
		}else if(strDataField.equals("全水分mt(%)")){
			return 1;
		}else if(strDataField.equals("挥发份vdaf(%)")){
			return 2;
		}else if(strDataField.equals("灰份ad(%)")){
			return 3;
		}else if(strDataField.equals("硫份std(%)")){
			return 4;
		}
		
		return 0;
	}
}