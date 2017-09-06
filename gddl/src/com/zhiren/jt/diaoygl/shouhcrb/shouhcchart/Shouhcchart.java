package com.zhiren.jt.diaoygl.shouhcrb.shouhcchart;


import org.apache.tapestry.html.BasePage;

import java.awt.Color;
import java.awt.Font;
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
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;

public class Shouhcchart extends BasePage {
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
	
	private String REPORT_NAME_KUCZS="kuczst";
	private String REPORT_NAME_KEYTS="shouhzst";
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
			
		return diancmc;
		
	}
	private boolean isBegin=false;
	public String getPrintTable(){
		if(mstrReportName.equals(REPORT_NAME_KUCZS)){
			return getKuczst();
		}else if(mstrReportName.equals(REPORT_NAME_KEYTS)){
			return getShouhzst();
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
			strGongsID = " and d.fuid="+this.getTreeid();
			
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID = " and d.id="+this.getTreeid();
			 
		}else if (jib==-1){
			strGongsID= " and d.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
//		danwmc=getTreeDiancmc(this.getTreeid());
		return strGongsID;
	}
	
//	库存走势图
	public String getKuczst(){
		
		Date dat=getBeginriqDate();//日期
		String strDate=DateUtil.FormatDate(dat);//日期字符
		Report rt=new Report();
		
		int ArrWidth[]=new int[] {800};
		rt.setTitle("<font style='font-size:11pt'>燃料煤炭库存最近("+getDayValue().getValue()+")走势图分析<br>"+strDate+"</font>",ArrWidth);
		rt.setBody(new Table(1,1));
		rt.body.setWidth(ArrWidth);
		rt.body.setRowHeight(1,500);
		rt.body.setCellValue(1,1,getKuczsChartImage());

		return rt.getHtml();
	}
//	收耗走势图
	public String getShouhzst(){
		Date dat=getBeginriqDate();//日期
		String strDate=DateUtil.FormatDate(dat);//日期字符
		Report rt=new Report();
		
		int ArrWidth[]=new int[] {800};
		rt.setTitle("<font style='font-size:11pt'>燃料煤炭(最近"+getDayValue().getValue()+")供应耗用走势分析<br>"+strDate+"</font>",ArrWidth);
		rt.setBody(new Table(1,1));
		rt.body.setWidth(ArrWidth);
		rt.body.setRowHeight(1,450);
		rt.body.setCellValue(1,1,getShouhzsChartImage());

		return rt.getHtml();
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
		setDiancxxModel(new IDropDownModel(sql,"中国大唐集团"));
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
		tb1.addItem(tb);
		
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

	public String getKuczsChartImage(){
		JDBCcon cn = new JDBCcon();
		
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		Vector vorQux = new Vector();//曲线数组
		Vector lineName = new Vector();//曲线名称数组
		
		Date dat=getBeginriqDate();//日期
		long lngDays=getDayValue().getId();//显示库存的天数
		String strDate=DateUtil.FormatDate(dat);//日期字符
		String where = getGongsID();
		
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append(" select rq.riq,fenx,kuc as kuc from  \n");
		sbsql.append(" (select decode(1,1,'库存','') as fenx,riq,sum(kuc) as kuc  \n");
		sbsql.append("      from shouhcrbb s,diancxxb d  \n");
		sbsql.append("      where riq > to_date('"+strDate+"','yyyy-mm-dd')-"+lngDays+"  \n");
		sbsql.append("      and  riq <= to_date('"+strDate+"','yyyy-mm-dd') and s.diancxxb_id=d.id "+where+" group by riq) sj, \n");
		sbsql.append(" (select to_date('"+strDate+"','yyyy-mm-dd')-rownum+1 as riq from diancxxb  \n");
		sbsql.append("        where rownum<="+lngDays+") rq \n");
		sbsql.append("  where rq.riq=sj.riq(+) \n");
		sbsql.append("  order by rq.riq \n");
		
		ResultSet rs=cn.getResultSet(sbsql.toString());
		/*------------设定Dataset-------------*/
		
		try{
			//定义曲线并存放到向量中
			vorQux.add(new TimeSeries("库存"));
			lineName.add("库存");
			
			for(int x=0;rs.next();x++){
				//曲线确定顶点坐标		
				//if(rs.getString("fenx").equals( ((String)lineName.get(0)) )){
					((TimeSeries)vorQux.get(0)).add(new Day(rs.getDate("riq")), rs.getLong("kuc"));
				//}
			}
			for(int j=0;j<vorQux.size();j++){
				dataset.addSeries((TimeSeries)vorQux.get(j));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		
		/*------------建立JFreeChart,将dataset中的数据导入到JFreeChart中-------------*/
        JFreeChart chart = ChartFactory.createTimeSeriesChart( 
                "煤炭库存走势图", // title 
                "", // x-axis label 
                "", // y-axis label 
                dataset, // data 
                true, // create legend? 
                false, // generate tooltips? 
                false // generate URLs? 
                ); 
        
        /*------------配置图表(chart)属性--------------*/  //设置JFreeChart的显示属性,对图形外部部分进行调整 
        
        chart.setBackgroundPaint(Color.WHITE);//设置曲线图背景色 
        Font font = new Font("宋体", Font.BOLD, 16);  //设置字体大小，形状 
        TextTitle title = new TextTitle("煤炭库存走势图", font); 
        chart.setTitle(title); 
 
        /*------------设定Plot参数-------------*/
        XYPlot plot = (XYPlot) chart.getPlot();//获取图形的画布 
//        plot.setBackgroundPaint(Color.WHITE);//设置网格背景色 
//        plot.setDomainGridlinePaint(Color.lightGray);//设置网格竖线(Domain轴)颜色 
//        plot.setRangeGridlinePaint(Color.lightGray);//设置网格横线颜色 
//        //plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));//设置曲线图与xy轴的距离 
//        plot.setDomainCrosshairVisible(false); 
//        plot.setRangeCrosshairVisible(false);
//        plot.setDomainCrosshairLockedOnData(false);
        
        ValueAxis rangeAxis = plot.getRangeAxis();
    	rangeAxis.setUpperMargin(0.15);//设置最高的一个柱与图片顶端的距离
    	rangeAxis.setLowerMargin(0.1);//设置最低的一个柱与图片底端的距离
    	plot.setRangeAxis(rangeAxis);
//    	plot.getDomainAxis().set
    	
        NumberAxis numAxis = (NumberAxis) plot.getRangeAxis(); //      设置Y轴
        NumberFormat numFormater = NumberFormat.getNumberInstance(); 
        numFormater.setMinimumFractionDigits(0); 
        numAxis.setNumberFormatOverride(numFormater); 
//        StandardXYToolTipGenerator tipGenerator = new StandardXYToolTipGenerator(//设置提示信息 
//        		"历史信息:({1} 16:00,{2})", new SimpleDateFormat("MM-dd"),numFormater); 
//               设置X轴（日期轴）;
        DateAxis axis = (DateAxis) plot.getDomainAxis(); 
        axis.setDateFormatOverride(new SimpleDateFormat("MM-dd")); 
        axis.setAutoTickUnitSelection(false);
        
        /*------------设定Renderer参数-------------*/
        XYItemRenderer r = plot.getRenderer(); 
        if (r instanceof XYLineAndShapeRenderer) 
        { 
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r; 
            renderer.setBaseShapesVisible(true); 
            renderer.setBaseShapesFilled(true); 
            renderer.setShapesVisible(true);//设置曲线是否显示数据点 
            
            renderer.setSeriesPaint(0, new Color(0, 100, 255));//	设置每种水果代表的柱的颜色
//			renderer.setSeriesPaint(1, new Color(255, 0, 0));
//			renderer.setSeriesPaint(2, new Color(255, 0, 255));
//			renderer.setSeriesPaint(3, new Color(200, 150, 0));
        } 
//        r.setToolTipGenerator(tipGenerator);
        
        /*------------生成图片 用servlet方式-------------*/
        String fileName = null; 
        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection()); 
        PrintWriter pw = null;
        try 
        { 
        	fileName = ServletUtilities.saveChartAsJPEG(chart, 800, 450, info,
        	getPage().getRequestCycle().getRequestContext().getSession());//生成图片
            ServletOutputStream out  = getPage().getRequestCycle().getRequestContext().getResponse().getOutputStream();
            pw = new PrintWriter(out);
            ChartUtilities.writeImageMap(new PrintWriter(out), fileName, info, false);// Write the image map to the PrintWriter 
            setImgServletPath(fileName);
            return getImgServletPath(fileName);
        } 
        catch (IOException e) 
        { 
            e.printStackTrace(); 
        }finally{
        	pw.flush(); 
        }
        return "";
	}
	
	
	public String getShouhzsChartImage(){
		JDBCcon cn = new JDBCcon();
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		Vector vorQux = new Vector();//曲线数组
		Vector lineName = new Vector();//曲线名称数组
		
		Date dat=getBeginriqDate();//日期
		String strDate=DateUtil.FormatDate(dat);//日期字符
		long lngDays=getDayValue().getId();//显示数据的天数
		String where = getGongsID();
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("select fx.riq,fx.fenx,sj.shul from  \n");
		sbsql.append("(select fenx,riq,shul from  \n");
		sbsql.append("(select decode(1,1,'供应','') as fenx,riq,sum(dangrgm) as shul from shouhcrbb rb,diancxxb d  \n");
		sbsql.append("where rb.riq>to_date('"+strDate+"','yyyy-mm-dd')-"+lngDays+" and rb.riq<=to_date('"+strDate+"','yyyy-mm-dd') and rb.diancxxb_id=d.id "+where+" group by riq) \n");
		sbsql.append("union \n");
		sbsql.append("(select decode(1,1,'耗用','') as fenx,riq,sum(haoyqkdr) as shul from shouhcrbb rb,diancxxb d  \n");
		sbsql.append("where rb.riq>=to_date('"+strDate+"','yyyy-mm-dd')-"+lngDays+" and rb.riq<=to_date('"+strDate+"','yyyy-mm-dd') and rb.diancxxb_id=d.id "+where+" group by riq) \n");
		sbsql.append("order by fenx,riq )sj, \n");
		sbsql.append("(select rq.riq,f.fenx from  \n");
		sbsql.append("(select to_date('"+strDate+"','yyyy-mm-dd')-rownum+1 as riq from diancxxb   \n");
		sbsql.append("  where rownum<="+lngDays+") rq, \n");
		sbsql.append("  (select decode(1,1,'供应','') as fenx from dual union select decode(1,1,'耗用','') as fenx from dual) f) fx \n");
		sbsql.append("  where fx.riq=sj.riq(+) and fx.fenx=sj.fenx(+) order by fx.riq \n");
		
		
		ResultSet rs=cn.getResultSet(sbsql.toString());
		
		/*------------设定Dataset-------------*/
		
		try{
//			根据电厂名称定义曲线并存放到向量中
			vorQux.add(new TimeSeries("供应"));
			lineName.add("供应");
			vorQux.add(new TimeSeries("耗用"));
			lineName.add("耗用");
			for(int x=0;rs.next();x++){
				for(int y=0;y<lineName.size();y++){//根据曲线名称为曲线确定顶点坐标		
					if(rs.getString("fenx").equals( ((String)lineName.get(y)) )){
						((TimeSeries)vorQux.get(y)).add(new Day(rs.getDate("riq")), rs.getLong("shul"));
					}
				}
			}
			for(int j=0;j<vorQux.size();j++){
				dataset.addSeries((TimeSeries)vorQux.get(j));
			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		
		/*------------建立JFreeChart,将dataset中的数据导入到JFreeChart中-------------*/
        JFreeChart chart = ChartFactory.createTimeSeriesChart( 
                "煤炭可用天数走势图", // title 
                "", // x-axis label 
                "", // y-axis label 
                dataset, // data 
                true, // create legend? 
                false, // generate tooltips? 
                false // generate URLs? 
                ); 
        
        /*------------配置图表(chart)属性--------------*/  //设置JFreeChart的显示属性,对图形外部部分进行调整 
        
        chart.setBackgroundPaint(Color.WHITE);//设置曲线图背景色 
        Font font = new Font("宋体", Font.BOLD, 16);  //设置字体大小，形状 
        TextTitle title = new TextTitle("煤炭可用天数走势图", font); 
        chart.setTitle(title); 
 
        /*------------设定Plot参数-------------*/
        XYPlot plot = (XYPlot) chart.getPlot();//获取图形的画布 
//        plot.setBackgroundPaint(Color.WHITE);//设置网格背景色 
//        plot.setDomainGridlinePaint(Color.lightGray);//设置网格竖线(Domain轴)颜色 
//        plot.setRangeGridlinePaint(Color.lightGray);//设置网格横线颜色 
//        //plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));//设置曲线图与xy轴的距离 
//        plot.setDomainCrosshairVisible(false); 
//        plot.setRangeCrosshairVisible(false);
//        plot.setDomainCrosshairLockedOnData(false);
        
        ValueAxis rangeAxis = plot.getRangeAxis();
    	rangeAxis.setUpperMargin(0.15);//设置最高的一个柱与图片顶端的距离
    	rangeAxis.setLowerMargin(0.1);//设置最低的一个柱与图片底端的距离
    	plot.setRangeAxis(rangeAxis);
//    	plot.getDomainAxis().set
    	
        NumberAxis numAxis = (NumberAxis) plot.getRangeAxis(); //      设置Y轴
        NumberFormat numFormater = NumberFormat.getNumberInstance(); 
        numFormater.setMinimumFractionDigits(0); 
        numAxis.setNumberFormatOverride(numFormater); 
//        StandardXYToolTipGenerator tipGenerator = new StandardXYToolTipGenerator(//设置提示信息 
//        		"历史信息:({1} 16:00,{2})", new SimpleDateFormat("MM-dd"),numFormater); 
//               设置X轴（日期轴）;
        DateAxis axis = (DateAxis) plot.getDomainAxis(); 
        axis.setDateFormatOverride(new SimpleDateFormat("MM-dd")); 
        axis.setAutoTickUnitSelection(false);
        
        /*------------设定Renderer参数-------------*/
        XYItemRenderer r = plot.getRenderer(); 
        if (r instanceof XYLineAndShapeRenderer) 
        { 
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r; 
            renderer.setBaseShapesVisible(true); 
            renderer.setBaseShapesFilled(true); 
            renderer.setShapesVisible(true);//设置曲线是否显示数据点 
            
            renderer.setSeriesPaint(0, new Color(0, 100, 255));//	设置每种水果代表的柱的颜色
			renderer.setSeriesPaint(1, new Color(255, 0, 0));
//			renderer.setSeriesPaint(2, new Color(255, 0, 255));
//			renderer.setSeriesPaint(3, new Color(200, 150, 0));
        } 
//        r.setToolTipGenerator(tipGenerator);
        
        /*------------生成图片 用servlet方式-------------*/
        String fileName = null; 
        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection()); 
        PrintWriter pw = null;
        try 
        { 
        	fileName = ServletUtilities.saveChartAsJPEG(chart, 800, 450, info,
        	getPage().getRequestCycle().getRequestContext().getSession());//生成图片
            ServletOutputStream out  = getPage().getRequestCycle().getRequestContext().getResponse().getOutputStream();
            pw = new PrintWriter(out);
            ChartUtilities.writeImageMap(new PrintWriter(out), fileName, info, false);// Write the image map to the PrintWriter 
            setImgServletPath(fileName);
            return getImgServletPath(fileName);
        } 
        catch (IOException e) 
        { 
            e.printStackTrace(); 
        }finally{
        	pw.flush(); 
        }
        return "";
        // 用文件方式
        /*try {
        	File file = new File(MainGlobal.getServletPath(main.getPage(),"tempImg"),"XYchart2.jpg");
            ChartUtilities.saveChartAsJPEG(file, chart, 500, 300);
           main.setImgContextPath(file.getName());
        } catch (Exception e) {
            System.out.println("创建图形出现异常.");
        }  ******************************************************************************/ 
	}
	
	private String imgPath;
	public String getImgPath(){
		return imgPath;
	}
	public void setImgPath(String path){
		imgPath = path;
	}
	public void setImgServletPath(String name){
		StringBuffer sb = new StringBuffer();
		sb.append("<tr><td><p><img name='Picture' "
					+ "src='" + MainGlobal.getHomeContext(getPage())+"/servlet/DisplayChart?filename="+name
					+  "' usemap=' " 
					+ name
				    + "'/></p></td></tr>");
		setImgPath(sb.toString());
	}
	public String getImgContextPath(String name){
		return ("<tr><td><p><img name='Picture' "
					+ "src='" + MainGlobal.getHomeContext(getPage())+"/tempImg/"+name
					+  "' /></p></td></tr>");
	}
	public String getImgServletPath(String name){
		return ("<img name='Picture' "
					+ "src='" + MainGlobal.getHomeContext(getPage())+"/servlet/DisplayChart?filename="+name
					+  "' usemap='" 
					+ name
				    + "'/>");
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
