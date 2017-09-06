package com.zhiren.report;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletOutputStream;

import org.apache.tapestry.IPage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;
import org.jfree.util.Rotation;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;

/*
 * 时间:2011-06-08
 * 作者:夏峥
 * 描述:ChartXYLine方法中增加x轴是否有倾斜程度的判读
 * 
 */
/*
 * 作者:夏峥
 * 时间:2012-11-23
 * 描述:柱状图中增加数字位置的设置方法。
 * 	       增加柱状图每个柱子大小的设置。
 */

public class Chart extends ServletUtilities  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2638720536505996921L;
	
	public static final int ChartType_XYLine=0;
	public static final int ChartType_TimeGraph=1;
	public static final int ChartType_Bar=2;
	public static final int ChartType_Bar3D=3;
	public static final int ChartType_StackBar3D=4;
	public static final int ChartType_Pie=5;
	public static final int ChartType_Pie3D=6;
	
	//公用参数信息
	private String imgPath="";//查询图片路径
	private String chartFilePath="";//生成图片路径
	private String chartZhandmc="";//图片站点名称
	private IPage page = null;
	private ResultSetList rsl;
//	名称
	private String imgName;
//	宽度
	private int imgWidth;
//	高度
	private int imgHeight;
//	X轴字段名
	private String xLine;
//	图例字段名
	private String LegendName;
//	Y轴字段名
	private String yLine;
//	显示百分率
	private boolean blnPercent;
//	数据字段名
	private String dataName;
//	显示图例
	public boolean showLegend = true;//是否显示图例
//	X轴名
	public String xlineName = "";//x轴
//	Y轴名
	public String ylineName = "";//y轴
//	显示X坐标
	public boolean showXvalue = true;//显示X轴坐标
//	显示Y坐标
	public boolean showYvalue = true;//显示Y轴坐标
	
//	字体
	public String titlefontName="宋体";
//	字形
	public int titlefont = Font.PLAIN; //字体形状
//	字号
	public int titlefontSize=16; //字体大小
//	反锯齿
	public boolean blnAntiAlias = true;//是否使用显示字体时的反锯齿功能;
//	Y小数位
	public int intDigits = 2;//设置y轴数据的小数位
//	X字号
	public int xfontSize = 9;//设置x轴坐标的字体大小
//	X倾斜
	public boolean xTiltShow = false;//设置x轴坐标值倾斜显示，x轴数据应为字符串
	
//	public Color chartBackgroundPaint = Color.lightGray;//图片背景颜色	
	public GradientPaint chartBackgroundPaint = new GradientPaint(0, 0,  Color.lightGray, 500, 0, Color.white);//图片背景颜色
	public GradientPaint plotBackgroundPaint = new GradientPaint(-500, 500, Color.green, 300, 0, Color.white);
//	上边距
	public double rangeUpperMargin = 0.15;//最高点距离图片顶部的高度
//	下边距
	public double rangeLowerMargin = 0.1;//最低点距离图片底部的高度
	
//	曲线图 line 的参数信息
//	显示数据点
	public boolean lineShapesVisiblebln = true;//设置曲线是否显示数据点
//	X显示格式
	public String lineDateFormatOverride = "MM-dd";//x轴为日期时的显示格式
//	X竖直显示
	public boolean dateApeakShowbln = false;//x轴的日期是否竖直显示
	
//	柱状图 bar 的参数信息	
//	柱宽度
	public double MaximumBarWidth=0.035;
//	柱长度
	public double MinimumBarLength=0.035;
//	右边距
	public double barDomUpperMargin = 0.015;//设置柱状图距离图片右端距离
//	左边距
	public double barDomLowerMargin = 0.025;//设置柱状图距离图片左端距离
//	透明度
	public float barForegroundAlpha = 1;//柱子的透明度
//	显示边框
	public boolean barOutlinebln = true;//设置矩形柱子是否显示边框
//	边框颜色
	public Color barOutlineColor = Color.black;//设置柱子边框的颜色
//	
	public double barItemMargin = 0.1;//设置父类中子类柱子直接的间距(-1到0.6之间)
//	柱显示数据
	public boolean barLabelsFontbln = true;//设置柱子上是否显示数据
//	柱字体
	public String barfontName="宋体";	//柱子上显示的字体
//	柱字形
	public int barfont = Font.PLAIN; //柱子上显示的字体形状
//	柱字号
	public int barfontSize=11; //柱子上显示的字体大小
	public ItemLabelAnchor barfontPlace = ItemLabelAnchor.OUTSIDE1;//OUTSIDE1\2\10\11\12:顶端；OUTSIDE4～8:底部；OUTSIDE3\9:中间
	public ItemLabelAnchor barfontPlaceNormal= ItemLabelAnchor.OUTSIDE1;//OUTSIDE1\2\10\11\12:顶端；OUTSIDE4～8:底部；OUTSIDE3\9:中间
//	柱倾斜
	public int outSide = 25;//是否设置数据距离柱子顶端的距离
	public double barfontTilt = -0.7;//柱子上字的倾斜度。0:水平
	
//	饼型图 pie 的参数信息
	public final String piedatformat0 = "{0}";		//不显示数字和百分比
	public final String piedatformat1 = "{0} {1}";	//设置图上显示数字
	public final String piedatformat2 = "{0} {1}%";	//显示百分数；
	
//	背景透明度
	public float pieBackgroundAlpha = 0.6f;//	设定背景透明度（0-1.0之间）
//	前景透明度
	public float pieForegroundAlpha = 1;	 //	设定前景透明度（0-1.0之间）
//	显示方向
	public Rotation pieRotation = Rotation.CLOCKWISE;//设置是顺时针显示还是逆时针显示
//	弧度
	public double pieStartAngle = 90;//设置绘制第一段圆弧的角度
//	圆形
	public boolean pieCircularbln = false;//设置显示为椭圆形(false)还是圆形(true)
//	饼显示数据
	public boolean pieLabFormat = true;		//图上是否显示数据内容
//	饼显示数字
	public String pieLabGenerator = this.piedatformat1;//设置图上显示数字
//	
	public String pieLegendLabGenerator = this.piedatformat0;//设置图例上不显示数字
	
//	setDateUnit
	public static final int DATEUNIT_DAY = DateTickUnit.DAY;
	public static final int DATEUNIT_MONTH = DateTickUnit.MONTH;
	public static final int DATEUNIT_YEAR = DateTickUnit.YEAR;
	public static final int DATEUNIT_HOUR = DateTickUnit.HOUR;
	public static final int DATEUNIT_MILLISECOND = DateTickUnit.MILLISECOND;
	public static final int DATEUNIT_MINUTE = DateTickUnit.MINUTE;
	public static final int DATEUNIT_SECOND = DateTickUnit.SECOND;
	
	
//	设置图例位置
	public static final RectangleEdge LEGEND_POS_RIGHT = RectangleEdge.RIGHT;
	public static final RectangleEdge LEGEND_POS_LEFT = RectangleEdge.LEFT;
	public static final RectangleEdge LEGEND_POS_BOTTOM = RectangleEdge.BOTTOM;
	public static final RectangleEdge LEGEND_POS_TOP = RectangleEdge.TOP;
	private RectangleEdge legendPosition = Chart.LEGEND_POS_BOTTOM;	//图例显示位置
	
	
	public static final int PIE_ROUND=1;
	public static final int PIE_ROUND_NEW=2;
	
	private int  PieRoundMath=PIE_ROUND_NEW;
	public void setPieRoundMath(int PieRoundMath ){
		this.PieRoundMath=PieRoundMath;
	}
//	public int imgHtmlWidth=0;
//	public int imgHtmlHeight=0;
	
	public String ID="";
	
	private String imgEvents="";
	
	public void setID(String ID){
		this.ID=ID;
	}
	public void setImgEvents(String strEvents){
		imgEvents=strEvents;
	}

	public boolean displayPictrue=true;
	
	public void setDisplayPicture(boolean displayPictrue){
		this.displayPictrue=displayPictrue;
	}
	
	public int dateunit = Chart.DATEUNIT_DAY;
	public int unitCount = 1;
	
	public void setDateUnit(int dateUnit, int count){
		dateunit = dateUnit;
		unitCount = count;
	}
	
	public Chart() {
		
	}
	public Chart(IPage page){
		setPage(page);
	}
	
	public void setAttribute(String name,String v) {
		if("名称".equals(name)) {
			imgName = v;
		}else if("宽度".equals(name)){
			imgWidth = Integer.parseInt(v);
		}else if("高度".equals(name)){
			imgHeight = Integer.parseInt(v);
		}else if("X轴字段名".equals(name)){
			xLine = v;
		}else if("图例字段名".equals(name)){
			LegendName = v;
		}else if("Y轴字段名".equals(name)){
			yLine = v;
		}else if("显示百分率".equals(name)){
			blnPercent = Boolean.valueOf(v).booleanValue();
		}else if("数据字段名".equals(name)){
			dataName = v;
		}else if("显示图例".equals(name)){
			showLegend = Boolean.valueOf(v).booleanValue();
		}else if("X轴名".equals(name)){
			xlineName = v;
		}else if("Y轴名".equals(name)){
			ylineName = v;
		}else if("显示X坐标".equals(name)){
			showXvalue = Boolean.valueOf(v).booleanValue();
		}else if("显示Y坐标".equals(name)){
			showYvalue = Boolean.valueOf(v).booleanValue();
		}else if("字体".equals(name)){
			titlefontName = v;
		}else if("字形".equals(name)){
			titlefont = Integer.parseInt(v);
		}else if("字号".equals(name)){
			titlefontSize = Integer.parseInt(v);
		}else if("Y小数位".equals(name)){
			intDigits = Integer.parseInt(v);
		}else if("X显示格式".equals(name)){
			lineDateFormatOverride = v;
		}
		
	}
	
	public Chart(ResultSetList rsl,String legendName,String xLine,
			String yLine, boolean blnPercent, String dataName,IPage page,String imgName,
			int imgHeight,int imgWidth) {
		setBlnPercent(blnPercent);
		setDataName(dataName);
		setLegendName(legendName);
		setRsl(rsl);
		setXLine(xLine);
		setYLine(yLine);
		setPage(page);
		setImgName(imgName);
		setImgHeight(imgHeight);
		setImgWidth(imgWidth);
	}
	
	public int getImgHeight() {
		return imgHeight;
	}
	public void setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	public int getImgWidth() {
		return imgWidth;
	}
	public void setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
	}
	public IPage getPage() {
		return page;
	}
	public void setPage(IPage page) {
		this.page = page;
	}
	public boolean isBlnPercent() {
		return blnPercent;
	}
	public void setBlnPercent(boolean blnPercent) {
		this.blnPercent = blnPercent;
	}
	public String getDataName() {
		return dataName;
	}
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}
	public String getLegendName() {
		return LegendName;
	}
	public void setLegendName(String legendName) {
		LegendName = legendName;
	}
	public ResultSetList getRsl() {
		return rsl;
	}
	public void setRsl(ResultSetList rsl) {
		this.rsl = rsl;
	}
	public String getXLine() {
		return xLine;
	}
	public void setXLine(String line) {
		xLine = line;
	}
	public String getYLine() {
		return yLine;
	}
	public void setYLine(String line) {
		yLine = line;
	}
	
	public RectangleEdge getLegendPos() {
		return legendPosition;
	}
	public void setLegendPos(RectangleEdge leg_Position) {
		legendPosition = leg_Position;
	}
	
	public String getStringChart(int ChartType) {
		String StrChart=null;
		switch(ChartType) {
			case ChartType_XYLine:
				StrChart = ChartXYLine();
				break;
			case ChartType_TimeGraph:
				StrChart = ChartTimeGraph();
				break;
			case ChartType_Bar:
				StrChart = ChartBar();
				break;
			case ChartType_Bar3D:
				StrChart = ChartBar3D();
				break;
			case ChartType_StackBar3D:
				StrChart = ChartStackBar3D();
				break;
			case ChartType_Pie:
				StrChart = ChartPie();
				break;
			case ChartType_Pie3D:
				StrChart = ChartPie3D();
				break;
			default:
				
				break;
		}
		return StrChart;
	}
	
//	时间曲线图
	public String ChartXYLine() {
		ChartData cd = new ChartData();
		CategoryDataset dataset = cd.getRsDataChart(getRsl(), 
				getXLine(), getLegendName(),
				getYLine());
		return ChartXYLine(getPage(),dataset,getImgName(),getImgWidth(),getImgHeight());
	}
	public String ChartXYLine(IPage ipage,CategoryDataset dataset, String imgchartName, int imgWidth, int imgHeight) {
		this.imgWidth=imgWidth;
		this.imgHeight=imgHeight;
		
		page = ipage;
		if(page==null){
			System.out.println("/******** Session对象为空 ********/");
			return "";
		}
		
		/*------------建立JFreeChart,将dataset中的数据导入到JFreeChart中-------------*/
		
		JFreeChart chart = ChartFactory.createLineChart(imgchartName, // title 
				xlineName, //      x轴的名字 
				ylineName, //      y轴的名字 
				dataset,   // 		数据集 
				PlotOrientation.VERTICAL,        // orientation
				showLegend,//   	是否包含图例
				false,    //  		是否包含提示工具 
				false 	   //   	是否包含url
				);

		/*------------配置图表(chart)属性--------------*///设置JFreeChart的显示属性,对图形外部部分进行调整 
		chart.setBackgroundPaint(chartBackgroundPaint);//设置曲线图背景色 
		Font font = new Font(titlefontName, titlefont, titlefontSize); //设置字体大小，形状 
		TextTitle title = new TextTitle(imgchartName, font);
		chart.setTitle(title);
//		设置图例显示位置
//		chart.getLegend().setPosition(getLegendPos());
		
		/*------------设定Plot参数-------------*/
		CategoryPlot plot = (CategoryPlot) chart.getPlot();//获取图形的画布 
		        plot.setBackgroundPaint(plotBackgroundPaint);//设置网格背景色 
		//        plot.setDomainGridlinePaint(Color.lightGray);//设置网格竖线(Domain轴)颜色 
		//        plot.setRangeGridlinePaint(Color.lightGray);//设置网格横线颜色 
		//        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));//设置曲线图与xy轴的距离 
		//        plot.setDomainCrosshairVisible(false); 
		//        plot.setRangeCrosshairVisible(false);
		//        plot.setDomainCrosshairLockedOnData(false);

		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setUpperMargin(rangeUpperMargin);//设置最高的一个柱与图片顶端的距离
		rangeAxis.setLowerMargin(rangeLowerMargin);//设置最低的一个柱与图片底端的距离
		plot.setRangeAxis(rangeAxis);
		
		CategoryAxis domainAxis = plot.getDomainAxis();
    	domainAxis.setTickLabelFont(new Font("sans-serif",Font.PLAIN,xfontSize));//设置X轴坐标上的文字
//		domainAxis.setLabelFont(new Font("宋体",Font.PLAIN,12));          //设置X轴的标题文字      
//		rangeAxis.setTickLabelFont(new Font("sans-serif",Font.PLAIN,12));     //设置Y轴坐标上的文字
//		rangeAxis.setLabelFont(new Font("黑体",Font.PLAIN,12));               //设置Y轴的标题文字

    	domainAxis.setVisible(showXvalue);
    	rangeAxis.setVisible(showYvalue);
    	if(xTiltShow){
			domainAxis.setCategoryLabelPositions(//设置X轴标题的倾斜程度
					CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
		}
    	
		NumberAxis numAxis = (NumberAxis) plot.getRangeAxis(); //      设置Y轴
		NumberFormat numFormater = NumberFormat.getNumberInstance();
		numFormater.setMinimumFractionDigits(intDigits);
		numAxis.setNumberFormatOverride(numFormater);

		/*------------设定Renderer参数-------------*/
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
		renderer.setShapesVisible(lineShapesVisiblebln);//显示数据点
//        renderer.setUseFillPaint(false);//显示数据点为空心
        renderer.setFillPaint(Color.white);

		/*------------生成图片 用servlet方式-------------*/
		String fileName = null;
		ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
		PrintWriter pw = null;
		try {
			ServletOutputStream out = page.getRequestCycle().getRequestContext().getResponse().getOutputStream();
			pw = new PrintWriter(out);
			
			if(getChartFilePath()!=null && !getChartFilePath().equals("")){
				String prefix = ServletUtilities.getTempFilePrefix();
				  File tempFile = File.createTempFile(prefix, ".jpg", new File(getChartFilePath()));
	
				  ChartUtilities.saveChartAsJPEG(tempFile,chart, imgWidth, imgHeight, info);//生成图片
				  
				  if (page.getRequestCycle().getRequestContext().getSession() != null) 
	            {
	                  ServletUtilities.registerChartForDeletion(tempFile, page.getRequestCycle().getRequestContext().getSession());
	            }
				  setImgContextPath(tempFile.getName());
	            return getImgContextPath(tempFile.getName());
			}else{
			
				fileName = ServletUtilities.saveChartAsJPEG(chart, imgWidth, imgHeight, info, 
						page.getRequestCycle().getRequestContext().getSession());//生成图片
				
//				ServletOutputStream out = page.getRequestCycle().getRequestContext().getResponse().getOutputStream();
//				pw = new PrintWriter(out);
				//ChartUtilities.writeImageMap(pw, fileName, info, false);// Write the image map to the PrintWriter 
				setImgServletPath(fileName);
				return getImgServletPath(fileName);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
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
	
//	时间曲线图
	public String ChartTimeGraph() {
		ChartData cd = new ChartData();
		TimeSeriesCollection dataset = cd.getRsDataTimeGraph(
				getRsl(), 
				getLegendName(), 
				getXLine(),
				getYLine());
		return ChartTimeGraph(getPage(),dataset,getImgName(),getImgWidth(),getImgHeight());
	}
	public String ChartTimeGraph(IPage ipage,TimeSeriesCollection dataset, String imgchartName, int imgWidth, int imgHeight) {
		this.imgWidth=imgWidth;
		this.imgHeight=imgHeight;

		page = ipage;
		if(page==null){
			System.out.println("/******** Session对象为空 ********/");
			return "";
		}
		
		/*------------建立JFreeChart,将dataset中的数据导入到JFreeChart中-------------*/
		
		JFreeChart chart = ChartFactory.createTimeSeriesChart(imgchartName, // title 
				xlineName, //      x轴的名字 
				ylineName, //      y轴的名字 
				dataset,   // 		数据集 
				showLegend,//   	是否包含图例
				false,    //  		是否包含提示工具 
				false 	   //   	是否包含url
				);

		/*------------配置图表(chart)属性--------------*///设置JFreeChart的显示属性,对图形外部部分进行调整 
		chart.setBackgroundPaint(chartBackgroundPaint);//设置曲线图背景色 
		Font font = new Font(titlefontName, titlefont, titlefontSize); //设置字体大小，形状 
		TextTitle title = new TextTitle(imgchartName, font);
		chart.setTitle(title);
//		设置图例显示位置
//		chart.getLegend().setPosition(getLegendPos());
		/*------------设定Plot参数-------------*/
		XYPlot plot = (XYPlot) chart.getPlot();//获取图形的画布 
		//        plot.setBackgroundPaint(Color.WHITE);//设置网格背景色 
		//        plot.setDomainGridlinePaint(Color.lightGray);//设置网格竖线(Domain轴)颜色 
		//        plot.setRangeGridlinePaint(Color.lightGray);//设置网格横线颜色 
		//        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));//设置曲线图与xy轴的距离 
		//        plot.setDomainCrosshairVisible(false); 
		//        plot.setRangeCrosshairVisible(false);
		//        plot.setDomainCrosshairLockedOnData(false);

		ValueAxis rangeAxis = plot.getRangeAxis();
		
		rangeAxis.setUpperMargin(rangeUpperMargin);//设置最高的一个柱与图片顶端的距离
		rangeAxis.setLowerMargin(rangeLowerMargin);//设置最低的一个柱与图片底端的距离
		plot.setRangeAxis(rangeAxis);
		plot.setBackgroundPaint(plotBackgroundPaint);
		
     
//		rangeAxis.setTickLabelFont(new Font("sans-serif",Font.PLAIN,12));     //设置Y轴坐标上的文字
//		rangeAxis.setLabelFont(new Font("黑体",Font.PLAIN,12));               //设置Y轴的标题文字

		NumberAxis numAxis = (NumberAxis) plot.getRangeAxis(); //      设置Y轴
		NumberFormat numFormater = NumberFormat.getNumberInstance();
		numFormater.setMinimumFractionDigits(intDigits);
		numAxis.setNumberFormatOverride(numFormater);
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat(lineDateFormatOverride));
		
		axis.setTickUnit(new DateTickUnit(dateunit,unitCount));
		
		axis.setAutoTickUnitSelection(false);
		axis.setVerticalTickLabels(dateApeakShowbln);
		axis.setTickLabelFont(new Font("sans-serif",Font.PLAIN,xfontSize));//设置X轴坐标上的文字
//		axis.setLabelFont(new Font("宋体",Font.PLAIN,12));          //设置X轴的标题文字 
		
		axis.setVisible(showXvalue);
    	rangeAxis.setVisible(showYvalue);
		
		/*------------设定Renderer参数-------------*/
		XYItemRenderer r = plot.getRenderer();
		if (r instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			renderer.setBaseShapesVisible(true);
			renderer.setBaseShapesFilled(true);
			renderer.setShapesVisible(lineShapesVisiblebln);//设置曲线是否显示数据点 
			
		}
//		StandardXYToolTipGenerator tipGenerator = new StandardXYToolTipGenerator(//设置提示信息 
//        		"历史信息:({1} 16:00,{2})", new SimpleDateFormat("MM-dd"),numFormater); 
//        r.setToolTipGenerator(tipGenerator);

		/*------------生成图片 用servlet方式-------------*/
		String fileName = null;
		ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
		PrintWriter pw = null;
		
		try {
			ServletOutputStream out = page.getRequestCycle().getRequestContext().getResponse().getOutputStream();
			pw = new PrintWriter(out);
			
			if(getChartFilePath()!=null && !getChartFilePath().equals("")){
				  String prefix = ServletUtilities.getTempFilePrefix();
				  File tempFile = File.createTempFile(prefix, ".jpg", new File(getChartFilePath()));
	
				  ChartUtilities.saveChartAsJPEG(tempFile,chart, imgWidth, imgHeight, info);//生成图片
				  
				  if (page.getRequestCycle().getRequestContext().getSession() != null) 
	              {
	                    ServletUtilities.registerChartForDeletion(tempFile, page.getRequestCycle().getRequestContext().getSession());
	              }
				  setImgContextPath(tempFile.getName());
	              return getImgContextPath(tempFile.getName());
			}else{
              
				fileName = ServletUtilities.saveChartAsJPEG(chart, imgWidth, imgHeight, info, 
						page.getRequestCycle().getRequestContext().getSession());//生成图片
				
//				ServletOutputStream out = page.getRequestCycle().getRequestContext().getResponse().getOutputStream();
//				pw = new PrintWriter(out);
				//ChartUtilities.writeImageMap(pw, fileName, info, false);// Write the image map to the PrintWriter 
				setImgServletPath(fileName);
	            return getImgServletPath(fileName);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			pw.flush();
		}
		return "";
	}
	
//	普通柱状图
	public String ChartBar(){
		ChartData cd = new ChartData();
		CategoryDataset dataset = cd.getRsDataChart(
				getRsl(), 
				getXLine(), 
				getLegendName(),
				getYLine());
		return ChartBar(getPage(),dataset,getImgName(),getImgWidth(),getImgHeight());
	}
	public String ChartBar(IPage ipage,CategoryDataset dataset, String imgchartName, int imgWidth, int imgHeight){
		this.imgWidth=imgWidth;
		this.imgHeight=imgHeight;

		page = ipage;
		if(page==null){
			System.out.println("/******** Session对象为空 ********/");
			return "";
		}
		
		/*------------建立JFreeChart,将dataset中的数据导入到JFreeChart中-------------*/
		
		JFreeChart chart = ChartFactory.createBarChart(
				imgchartName,			  // 图表标题
				xlineName,				  // 目录轴的显示标签
				ylineName,				  // 数值轴的显示标签
				dataset, 				  // 数据集
				PlotOrientation.VERTICAL, // 图表方向：水平、垂直
				showLegend,  			  // 是否显示图例(对于简单的柱状图必须是false)
				false,  				  // 是否生成工具
				false  					  // 是否生成URL链接
		);
		Font font = new Font(titlefontName, titlefont, titlefontSize); //设置字体大小，形状 
		TextTitle title = new TextTitle(imgchartName, font);
		chart.setTitle(title);
//		设置图例显示位置
//		chart.getLegend().setPosition(getLegendPos());
		
		/*------------设定Plot参数-------------*/
		CategoryPlot plot = chart.getCategoryPlot();
		chart.setBackgroundPaint(chartBackgroundPaint);
		CategoryAxis domainAxis = plot.getDomainAxis();
//		domainAxis.setLabelAngle(0.0);//设置columnKey 是否垂直显示
		domainAxis.setUpperMargin(barDomUpperMargin);//设置距离图片右端距离
    	domainAxis.setLowerMargin(barDomLowerMargin);//设置距离图片左端距离
		plot.setDomainAxis(domainAxis); //设置 columnKey 是否间隔显示
		plot.setBackgroundPaint(plotBackgroundPaint);
		
		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setUpperMargin(rangeUpperMargin);//设置最高的一个柱与图片顶端的距离
		rangeAxis.setLowerMargin(rangeLowerMargin);//设置最低的一个柱与图片底端的距离
		
		domainAxis.setVisible(showXvalue);
		rangeAxis.setVisible(showYvalue);
		if(xTiltShow){
			domainAxis.setCategoryLabelPositions(//设置X轴标题的倾斜程度
					CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
		}
		
		plot.setRangeAxis(rangeAxis); 
		
//		domainAxis.setTickLabelFont(new Font("sans-serif",Font.PLAIN,20));//设置X轴坐标上的文字
//		domainAxis.setLabelFont(new Font("宋体",Font.PLAIN,12));          //设置X轴的标题文字      
//		rangeAxis.setTickLabelFont(new Font("sans-serif",Font.PLAIN,12));     //设置Y轴坐标上的文字
//		rangeAxis.setLabelFont(new Font("黑体",Font.PLAIN,12));               //设置Y轴的标题文字
		
		plot.setForegroundAlpha(barForegroundAlpha); //设置每个柱的透明度
//		plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);//设置地区、销量的显示位置
//		plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

		/*------------设定Renderer参数-------------*/
		BarRenderer renderer = new BarRenderer();
		
		renderer.setDrawBarOutline(barOutlinebln);//设置是否显示柱子边框
		renderer.setOutlinePaint(barOutlineColor);//设置柱子边框的颜色
		
//		renderer.setWallPaint(Color.gray);//设置 Wall 的颜色
//		renderer.setSeriesPaint(0, Color.GREEN);//	设置每个柱子的颜色	

		
		renderer.setItemMargin(barItemMargin);//设置每个地区所包含的平行柱的之间距离
		
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMinimumFractionDigits(intDigits);
		renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}",format));
		ItemLabelPosition itemlabelposition = new ItemLabelPosition(barfontPlace, 
                TextAnchor.CENTER, 
                TextAnchor.BASELINE_CENTER, 
                barfontTilt);
		renderer.setPositiveItemLabelPositionFallback(itemlabelposition);
		renderer.setNegativeItemLabelPositionFallback(itemlabelposition);
		
//		设置能正常在BAR内显示数据的显示位置
		ItemLabelPosition itemlabelpositionN = new ItemLabelPosition(barfontPlaceNormal, 
                TextAnchor.CENTER, 
                TextAnchor.BASELINE_CENTER, 
                barfontTilt);
		renderer.setPositiveItemLabelPosition(itemlabelpositionN);
		renderer.setNegativeItemLabelPosition(itemlabelpositionN);
		
		renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());//显示每个柱的数值，并修改该数值的字体属性
		renderer.setItemLabelFont(new Font(barfontName,barfont,barfontSize));
		renderer.setItemLabelAnchorOffset(outSide);
		renderer.setItemLabelsVisible(barLabelsFontbln);
		plot.setRenderer(renderer);
		
		/*------------生成图片 用文件方式-------------*/
		String fileName="";
        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection()); 
        PrintWriter pw = null;
		 try 
	        { 
			 ServletOutputStream out = page.getRequestCycle().getRequestContext().getResponse().getOutputStream();
			 pw = new PrintWriter(out);
				
			 if(getChartFilePath()!=null && !getChartFilePath().equals("")){
				  String prefix = ServletUtilities.getTempFilePrefix();
				  File tempFile = File.createTempFile(prefix, ".jpg", new File(getChartFilePath()));
	
				  ChartUtilities.saveChartAsJPEG(tempFile,chart, imgWidth, imgHeight, info);//生成图片
				  
				  if (page.getRequestCycle().getRequestContext().getSession() != null) 
	              {
	                   ServletUtilities.registerChartForDeletion(tempFile, page.getRequestCycle().getRequestContext().getSession());
	              }
				  setImgContextPath(tempFile.getName());
	              return getImgContextPath(tempFile.getName());
	             
			 }else{
	        	fileName = ServletUtilities.saveChartAsJPEG(chart, imgWidth, imgHeight, info,
	        	page.getRequestCycle().getRequestContext().getSession());//生成图片
//	            ServletOutputStream out  = page.getRequestCycle().getRequestContext().getResponse().getOutputStream();
//	            pw = new PrintWriter(out);
	            //ChartUtilities.writeImageMap(pw, fileName, info, false);// Write the image map to the PrintWriter 
	            setImgServletPath(fileName);
	            return getImgServletPath(fileName);
			 }
        } 
        catch (IOException e) 
        { 
            e.printStackTrace(); 
        }finally{
        	pw.flush(); 
        }
        return "";
	}
	
//	普通柱状图	
	public String ChartBar3D(){
		ChartData cd = new ChartData();
		CategoryDataset dataset = cd.getRsDataChart(
				getRsl(), 
				getXLine(), 
				getLegendName(),
				getYLine());
		return ChartBar3D(getPage(),dataset,getImgName(),getImgWidth(),getImgHeight());
	}
	public String ChartBar3D(IPage ipage,CategoryDataset dataset, String imgchartName, int imgWidth, int imgHeight){
		this.imgWidth=imgWidth;
		this.imgHeight=imgHeight;

		page = ipage;
		if(page==null){
			System.out.println("/******** Session对象为空 ********/");
			return "";
		}
		
		/*------------建立JFreeChart,将dataset中的数据导入到JFreeChart中-------------*/
		
		JFreeChart chart = ChartFactory.createBarChart3D(
				imgchartName,			  // 图表标题
				xlineName,				  // 目录轴的显示标签
				ylineName,				  // 数值轴的显示标签
				dataset, 				  // 数据集
				PlotOrientation.VERTICAL, // 图表方向：水平、垂直
				showLegend,  			  // 是否显示图例(对于简单的柱状图必须是false)
				false,  				  // 是否生成工具
				false  					  // 是否生成URL链接
		);
		chart.setBackgroundPaint(chartBackgroundPaint);
		Font font = new Font(titlefontName, titlefont, titlefontSize); //设置字体大小，形状 
		TextTitle title = new TextTitle(imgchartName, font);
		chart.setTitle(title);
//		设置图例显示位置
//		chart.getLegend().setPosition(getLegendPos());
		
		/*------------设定Plot参数-------------*/
		CategoryPlot plot = chart.getCategoryPlot();
		CategoryAxis domainAxis = plot.getDomainAxis();
//		domainAxis.setLabelAngle(0.0);//设置columnKey 是否垂直显示
		domainAxis.setUpperMargin(barDomUpperMargin);//设置距离图片右端距离
    	domainAxis.setLowerMargin(barDomLowerMargin);//设置距离图片左端距离
		plot.setDomainAxis(domainAxis); //设置 columnKey 是否间隔显示
		
		domainAxis.setVisible(showXvalue);
		
		
		if(xTiltShow){
			domainAxis.setCategoryLabelPositions(//设置X轴标题的倾斜程度
					CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
		}
		plot.setBackgroundPaint(plotBackgroundPaint);
		
		ValueAxis rangeAxis = plot.getRangeAxis();
		
		rangeAxis.setVisible(showYvalue);
		
		rangeAxis.setUpperMargin(rangeUpperMargin);//设置最高的一个柱与图片顶端的距离
		rangeAxis.setLowerMargin(rangeLowerMargin);//设置最低的一个柱与图片底端的距离
		plot.setRangeAxis(rangeAxis); 
		
//		domainAxis.setTickLabelFont(new Font("sans-serif",Font.PLAIN,20));//设置X轴坐标上的文字
//		domainAxis.setLabelFont(new Font("宋体",Font.PLAIN,12));          //设置X轴的标题文字      
//		rangeAxis.setTickLabelFont(new Font("sans-serif",Font.PLAIN,12));     //设置Y轴坐标上的文字
//		rangeAxis.setLabelFont(new Font("黑体",Font.PLAIN,12));               //设置Y轴的标题文字
		
		plot.setForegroundAlpha(barForegroundAlpha); //设置每个柱的透明度
//		plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);//设置地区、销量的显示位置
//		plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

		/*------------设定Renderer参数-------------*/
		BarRenderer3D renderer = new BarRenderer3D();
		
		renderer.setDrawBarOutline(barOutlinebln);//设置是否显示柱子边框
//		renderer.setWallPaint(Color.gray);//设置 Wall 的颜色
//		renderer.setSeriesPaint(0, Color.GREEN);//	设置每个柱子的颜色	

		renderer.setOutlinePaint(barOutlineColor);//设置柱子边框的颜色
		
		renderer.setItemMargin(barItemMargin);//设置每个地区所包含的平行柱的之间距离
		
		renderer.setMaximumBarWidth(MaximumBarWidth);
    	renderer.setMinimumBarLength(MinimumBarLength);
    	
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMinimumFractionDigits(intDigits);
		renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}",format));
		
		ItemLabelPosition itemlabelposition = new ItemLabelPosition(barfontPlace, 
                TextAnchor.CENTER, 
                TextAnchor.BASELINE_CENTER, 
                barfontTilt);
		renderer.setPositiveItemLabelPositionFallback(itemlabelposition);
		renderer.setNegativeItemLabelPositionFallback(itemlabelposition);
		
//		设置能正常在BAR内显示数据的显示位置
		ItemLabelPosition itemlabelpositionN = new ItemLabelPosition(barfontPlaceNormal, 
                TextAnchor.CENTER, 
                TextAnchor.BASELINE_CENTER, 
                barfontTilt);
		renderer.setPositiveItemLabelPosition(itemlabelpositionN);
		renderer.setNegativeItemLabelPosition(itemlabelpositionN);
		
		renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());//显示每个柱的数值，并修改该数值的字体属性
		renderer.setItemLabelFont(new Font(barfontName,barfont,barfontSize));
		renderer.setItemLabelAnchorOffset(outSide);//设置数据与柱子的顶端距离
		renderer.setItemLabelsVisible(barLabelsFontbln);
		renderer.setMaximumBarWidth(MaximumBarWidth);
		plot.setRenderer(renderer);
		
		/*------------生成图片 用文件方式-------------*/
		String fileName="";
        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection()); 
        PrintWriter pw = null;
		 try 
	        { 
			 ServletOutputStream out = page.getRequestCycle().getRequestContext().getResponse().getOutputStream();
			 pw = new PrintWriter(out);
			 if(getChartFilePath()!=null && !getChartFilePath().equals("")){
				 String prefix = ServletUtilities.getTempFilePrefix();
				  File tempFile = File.createTempFile(prefix, ".jpg", new File(getChartFilePath()));
	
				  ChartUtilities.saveChartAsJPEG(tempFile,chart, imgWidth, imgHeight, info);//生成图片
				  
				  if (page.getRequestCycle().getRequestContext().getSession() != null) 
	             {
	                   ServletUtilities.registerChartForDeletion(tempFile, page.getRequestCycle().getRequestContext().getSession());
	             }
				  setImgContextPath(tempFile.getName());
	             return getImgContextPath(tempFile.getName());
			 }else{
	        	fileName = ServletUtilities.saveChartAsJPEG(chart, imgWidth, imgHeight, info,
	        	page.getRequestCycle().getRequestContext().getSession());//生成图片
//	            ServletOutputStream out  = page.getRequestCycle().getRequestContext().getResponse().getOutputStream();
//	            pw = new PrintWriter(out);
	            //ChartUtilities.writeImageMap(pw, fileName, info, false);// Write the image map to the PrintWriter 
	            setImgServletPath(fileName);
	            return getImgServletPath(fileName);
			 }
        } 
        catch (IOException e) 
        { 
            e.printStackTrace(); 
        }finally{
        	pw.flush(); 
        }
        return "";
	}
	
//	叠加柱状图	
	public String ChartStackBar3D(){
		ChartData cd = new ChartData();
		CategoryDataset dataset = cd.getRsDataChart(
				getRsl(), 
				getXLine(), 
				getLegendName(),
				getYLine());
		return ChartStackBar3D(getPage(),dataset,getImgName(),getImgWidth(),getImgHeight());
	}
	public String ChartStackBar3D(IPage ipage,CategoryDataset dataset, String imgchartName, int imgWidth, int imgHeight){
		this.imgWidth=imgWidth;
		this.imgHeight=imgHeight;

		page = ipage;
		if(page==null){
			System.out.println("/******** Session对象为空 ********/");
			return "";
		}
		
		/*------------建立JFreeChart,将dataset中的数据导入到JFreeChart中-------------*/
		
    	JFreeChart chart = ChartFactory.createStackedBarChart3D(imgchartName,
		xlineName,				    // 目录轴的显示标签
		ylineName,				    // 数值轴的显示标签
		dataset, 					// 数据集 
    	PlotOrientation.VERTICAL,   // 图表方向：水平、垂直
    	showLegend,					// 是否显示图例(对于简单的柱状图必须是false)
    	false,						// 是否生成工具
    	false);						// 是否生成URL链接
		
		/*------------配置图表(chart)属性--------------*/
		
		Font font = new Font(titlefontName, titlefont, titlefontSize); //设置字体大小，形状 
		TextTitle title = new TextTitle(imgchartName, font);
		chart.setTitle(title);
//		设置图例显示位置
//		chart.getLegend().setPosition(getLegendPos());
		
		CategoryPlot plot = chart.getCategoryPlot();
    	CategoryAxis domainAxis = plot.getDomainAxis();
    	//domainAxis.setVerticalCategoryLabels(false);
    	plot.setDomainAxis(domainAxis);
    	ValueAxis rangeAxis = plot.getRangeAxis();
    	
    	chart.setBackgroundPaint(chartBackgroundPaint);//  设置整个图表背景颜色
		plot.setBackgroundPaint(plotBackgroundPaint);
    	
    	domainAxis.setUpperMargin(barDomUpperMargin);//设置距离图片右端距离
    	domainAxis.setLowerMargin(barDomLowerMargin);//设置距离图片左端距离
		
    	rangeAxis.setUpperMargin(rangeUpperMargin);//设置最高的一个柱与图片顶端的距离
    	rangeAxis.setLowerMargin(rangeLowerMargin);//设置最低的一个柱与图片底端的距离
    	
    	domainAxis.setVisible(showXvalue);
    	rangeAxis.setVisible(showYvalue);
    	
    	if(xTiltShow){
			domainAxis.setCategoryLabelPositions(//设置X轴标题的倾斜程度
					CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
		}
    	
    	plot.setRangeAxis(rangeAxis);
    	
//    	domainAxis.setTickLabelFont(new Font("sans-serif",Font.PLAIN,20));//设置X轴坐标上的文字
//		domainAxis.setLabelFont(new Font("宋体",Font.PLAIN,12));          //设置X轴的标题文字      
//		rangeAxis.setTickLabelFont(new Font("sans-serif",Font.PLAIN,12));     //设置Y轴坐标上的文字
//		rangeAxis.setLabelFont(new Font("黑体",Font.PLAIN,12));               //设置Y轴的标题文字
    	
//	    设置柱的透明度
    	plot.setForegroundAlpha(barForegroundAlpha);
//    	plot.setAxisOffset(new RectangleInsets(0.0, 0.0, 0.0, 0.0));
//	    	设置地区、销量的显示位置
    	//plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
    	//plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

    	StackedBarRenderer3D renderer = new StackedBarRenderer3D();
    	renderer.setDrawBarOutline(barOutlinebln);//设置是否显示柱子边框
    	
//    	renderer.setWallPaint(Color.LIGHT_GRAY); //设置 Wall 的颜色

    	renderer.setOutlinePaint(barOutlineColor);//设置柱子边框的颜色
    	
    	renderer.setItemMargin(barItemMargin);//设置每个地区所包含的平行柱之间的距离
    	
//	    	显示每个柱的数值，并修改该数值的字体属性
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMinimumFractionDigits(intDigits);
		renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}",format));
		ItemLabelPosition itemlabelposition = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, 
                TextAnchor.TOP_CENTER, 
                TextAnchor.CENTER, 
                barfontTilt);
		renderer.setPositiveItemLabelPositionFallback(itemlabelposition);	
		renderer.setNegativeItemLabelPositionFallback(itemlabelposition);
    	renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
    	renderer.setItemLabelFont(new Font(barfontName,barfont,barfontSize));
    	renderer.setItemLabelPaint(Color.BLACK);
    	renderer.setItemLabelsVisible(barLabelsFontbln);
    	renderer.setMaximumBarWidth(MaximumBarWidth);
    	renderer.setMinimumBarLength(MinimumBarLength);
    	plot.setRenderer(renderer);

    	/*------------生成图片 用servlet方式-------------*/
        String fileName="";
        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection()); 
        PrintWriter pw = null;
        try 
        { 
        	ServletOutputStream out = page.getRequestCycle().getRequestContext().getResponse().getOutputStream();
			pw = new PrintWriter(out);
        	if(getChartFilePath()!=null && !getChartFilePath().equals("")){
	        	String prefix = ServletUtilities.getTempFilePrefix();
				  File tempFile = File.createTempFile(prefix, ".jpg", new File(getChartFilePath()));
	
				  ChartUtilities.saveChartAsJPEG(tempFile,chart, imgWidth, imgHeight, info);//生成图片
				  
				  if (page.getRequestCycle().getRequestContext().getSession() != null) 
	            {
	                  ServletUtilities.registerChartForDeletion(tempFile, page.getRequestCycle().getRequestContext().getSession());
	            }
				  setImgContextPath(tempFile.getName());
	            return getImgContextPath(tempFile.getName());
        	}else{
	        	fileName = ServletUtilities.saveChartAsJPEG(chart, imgWidth, imgHeight, info,
	        						page.getRequestCycle().getRequestContext().getSession());//生成图片
	        	
//	            ServletOutputStream out  = page.getRequestCycle().getRequestContext().getResponse().getOutputStream();
//	            pw = new PrintWriter(out);
	           // ChartUtilities.writeImageMap(pw, fileName, info, false);// Write the image map to the PrintWriter 
	            setImgServletPath(fileName);
	            return getImgServletPath(fileName);
	       }
        } 
        catch (IOException e) 
        { 
            e.printStackTrace(); 
        }finally{
        	pw.flush(); 
        }
	   
	    return "";
	}
	
//	普通饼型图	
	public String ChartPie(){
		ChartData cd = new ChartData();
		DefaultPieDataset dataset = cd.getRsDataPie(
				getRsl(),  
				getLegendName(),
				getDataName(),
				isBlnPercent());
		return ChartPie(getPage(),dataset,getImgName(),getImgWidth(),getImgHeight());
	}
	public String ChartPie(IPage ipage,DefaultPieDataset piedata, String imgchartName, int imgWidth, int imgHeight){
		this.imgWidth=imgWidth;
		this.imgHeight=imgHeight;

		page = ipage;
		if(page==null){
			System.out.println("/******** Session对象为空 ********/");
			return "";
		}
		/*------------建立JFreeChart,将dataset中的数据导入到JFreeChart中-------------*/
		
		JFreeChart chart =         					//创建JFreeChart，都使用ChartFactory来创建JFreeChart,很标准的工厂设计模式
				ChartFactory.createPieChart(imgchartName, piedata, showLegend, true, true);

//		设置图例显示位置
//		chart.getLegend().setPosition(getLegendPos());
		
		/*------------配置图表(chart)属性--------------*/
		Font font = new Font(titlefontName, titlefont, titlefontSize); //设置字体大小，形状 
		TextTitle title = new TextTitle(imgchartName, font);
		chart.setTitle(title);
		chart.setBackgroundPaint(chartBackgroundPaint);//  设置整个图表背景颜色

		chart.setAntiAlias(false);//是否使用显示字体的反锯齿功能;
		
		/*------------设定Plot参数-------------*/
		PiePlot pie = (PiePlot)chart.getPlot();              //饼图使用一个PiePlot
		
		pie.setBackgroundPaint(plotBackgroundPaint);
		/*pie.setSectionLabelType(PiePlot.NAME_AND_PERCENT_LABELS);
		pie.setSectionLabelType(PiePlot.NAME_AND_VALUE_LABELS);*/
		pie.setLabelGenerator(new StandardPieSectionLabelGenerator());
		pie.setLabelFont(new Font("黑体", Font.TRUETYPE_FONT, 12));
//		
		//pie.setPercentFormatString("#,###0.0#%");//设定显示格式(名称加百分比或数值)
//		
		pie.setBackgroundPaint(Color.white);//
		
		pie.setCircular(pieCircularbln);//false为椭圆；true为圆形
		
		//pie.setSectionLabelFont(new Font("黑体", Font.TRUETYPE_FONT, 12));
//		设定背景透明度（0-1.0之间）
		pie.setBackgroundAlpha(pieBackgroundAlpha);
//		设定前景透明度（0-1.0之间）
		pie.setForegroundAlpha(pieForegroundAlpha);

		pie.setDirection(pieRotation);//设置是顺时针还是逆时针

		pie.setStartAngle(pieStartAngle);//设置显示第一个圆弧的角度

		pie.setLabelGenerator(new StandardPieSectionLabelGenerator(pieLabGenerator));//图上的名称后面显示数据
		pie.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(pieLegendLabGenerator)); //图例的名称后面显示百分比%
		
		/*------------生成图片 用servlet方式-------------*/
		String fileName="";
		ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection()); 
		PrintWriter pw = null;
		try 
		{ 
			ServletOutputStream out = page.getRequestCycle().getRequestContext().getResponse().getOutputStream();
			pw = new PrintWriter(out);
			
			if(getChartFilePath()!=null && !getChartFilePath().equals("")){
				String prefix = ServletUtilities.getTempFilePrefix();
				  File tempFile = File.createTempFile(prefix, ".jpg", new File(getChartFilePath()));
	
				  ChartUtilities.saveChartAsJPEG(tempFile,chart, imgWidth, imgHeight, info);//生成图片
				  
				  if (page.getRequestCycle().getRequestContext().getSession() != null) 
	            {
	                  ServletUtilities.registerChartForDeletion(tempFile, page.getRequestCycle().getRequestContext().getSession());
	            }
				  setImgContextPath(tempFile.getName());
	            return getImgContextPath(tempFile.getName());
			}else{
				fileName = ServletUtilities.saveChartAsJPEG(chart, 800, 400, info,
						page.getRequestCycle().getRequestContext().getSession());//生成图片
//				ServletOutputStream out  = page.getRequestCycle().getRequestContext().getResponse().getOutputStream();
//				pw = new PrintWriter(out);
				//ChartUtilities.writeImageMap(pw, fileName, info, false);// Write the image map to the PrintWriter 
				setImgServletPath(fileName);
	            return getImgServletPath(fileName);
			}
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace(); 
		}finally{
			pw.flush(); 
		}
		return "";
	}
	
	
//	3D饼型图	
	public String ChartPie3D(){
		ChartData cd = new ChartData();
		DefaultPieDataset dataset = cd.getRsDataPie(
				getRsl(),  
				getLegendName(),
				getDataName(),
				isBlnPercent());
		return ChartPie3D(getPage(),dataset,getImgName(),getImgWidth(),getImgHeight());
	}
	public String ChartPie3D(IPage ipage,DefaultPieDataset piedata, String imgchartName, int imgWidth, int imgHeight){
		this.imgWidth=imgWidth;
		this.imgHeight=imgHeight;
		page = ipage;
		if(page==null){
			System.out.println("/******** Session对象为空 ********/");
			return "";
		}
		/*------------建立JFreeChart,将dataset中的数据导入到JFreeChart中-------------*/
		
		JFreeChart chart = ChartFactory.createPieChart3D(imgchartName,      // 图表标题
					piedata,      	// 绘图数据集
					showLegend,     // 设定是否显示图例
					true,     	  	// 设定是否显示图例名称
					false);     	// 设定是否生成链接

		/*------------配置图表(chart)属性--------------*/
		Font font = new Font(titlefontName, titlefont, titlefontSize); //设置字体大小，形状 
		TextTitle title = new TextTitle(imgchartName, font);
		chart.setTitle(title);
		chart.setBackgroundPaint(chartBackgroundPaint);//  设置整个图表背景颜色
//		设置图例显示位置
//		chart.getLegend().setPosition(getLegendPos());
		
		chart.setAntiAlias(false);//是否使用显示字体的反锯齿功能;
		
		/*------------设定Plot参数-------------*/
		PiePlot pie = (PiePlot)chart.getPlot();              //饼图使用一个PiePlot
		
		/*pie.setSectionLabelType(PiePlot.NAME_AND_PERCENT_LABELS);
		pie.setSectionLabelType(PiePlot.NAME_AND_VALUE_LABELS);*/
		pie.setLabelFont(new Font("黑体", Font.TRUETYPE_FONT, 12));
//		
		//pie.setPercentFormatString("#,###0.0#%");//设定显示格式(名称加百分比或数值)
		
		pie.setBackgroundPaint(plotBackgroundPaint);//
		
		pie.setCircular(pieCircularbln);//false为椭圆；true为圆形
		
		//pie.setSectionLabelFont(new Font("黑体", Font.TRUETYPE_FONT, 12));
//		设定背景透明度（0-1.0之间）
		pie.setBackgroundAlpha(pieBackgroundAlpha);
//		设定前景透明度（0-1.0之间）
		pie.setForegroundAlpha(pieForegroundAlpha);

		pie.setDirection(pieRotation);//设置是顺时针还是逆时针

		pie.setStartAngle(pieStartAngle);//设置显示第一个圆弧的角度

		if(pieLabFormat){
			pie.setLabelGenerator(new StandardPieSectionLabelGenerator(pieLabGenerator));//图上的名称后面显示数据
		}else{
			pie.setLabelGenerator(null);
		}
		pie.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(pieLegendLabGenerator)); //图例的名称后面显示数据%
		
		/*------------生成图片 用servlet方式-------------*/
		String fileName="";
		ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection()); 
		PrintWriter pw = null;
		try 
		{ 
			ServletOutputStream out = page.getRequestCycle().getRequestContext().getResponse().getOutputStream();
			pw = new PrintWriter(out);
			if(getChartFilePath()!=null && !getChartFilePath().equals("")){
				String prefix = ServletUtilities.getTempFilePrefix();
				  File tempFile = File.createTempFile(prefix, ".jpg", new File(getChartFilePath()));
	
				  ChartUtilities.saveChartAsJPEG(tempFile,chart, imgWidth, imgHeight, info);//生成图片
				  
				  if (page.getRequestCycle().getRequestContext().getSession() != null) 
	            {
	                  ServletUtilities.registerChartForDeletion(tempFile, page.getRequestCycle().getRequestContext().getSession());
	            }
				  setImgContextPath(tempFile.getName());
	            return getImgContextPath(tempFile.getName());
			}else{
				fileName = ServletUtilities.saveChartAsJPEG(chart, imgWidth, imgHeight, info,
						page.getRequestCycle().getRequestContext().getSession());//生成图片
//				ServletOutputStream out  = page.getRequestCycle().getRequestContext().getResponse().getOutputStream();
//				pw = new PrintWriter(out);
				//ChartUtilities.writeImageMap(pw, fileName, info, false);// Write the image map to the PrintWriter 
				setImgServletPath(fileName);
	            return getImgServletPath(fileName);
			}
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace(); 
		}finally{
			pw.flush(); 
		}
		return "";
	}
	
	public String getImgPath(){
		return imgPath;
	}
	public void setImgPath(String path){
		imgPath = path;
	}
	
	public String getChartFilePath(){
		if(this.chartFilePath.equals("")){
			getChartPath();
		}
		return chartFilePath;
	}
	public void setChartFilePath(String path){
		chartFilePath = path;
	}
	
	public String getZhandmc(){
		if(this.chartZhandmc.equals("")){
			getChartPath();
		}
		return chartZhandmc;
	}
	public void setZhandmc(String zhandmc){
		chartZhandmc = zhandmc;
	}
	
	public void setImgServletPath(String name){
		StringBuffer sb = new StringBuffer();
		sb.append("<tr><td><p><img name='Picture' "
					+ "src='" + MainGlobal.getHomeContext(page)+"/servlet/DisplayChart?filename="+name
					+  "' usemap=' " 
					+ name
				    + "'/></p></td></tr>");

		setImgPath(sb.toString());
	}
	
	public void setImgContextPath(String name){
		StringBuffer sb = new StringBuffer();
		sb.append("<tr><td><p><img name='Picture' "
				+ " src='" +getContext()+"/"+ name
				+  "'  usemap=' " 
				+ name
			    + "'/></p></td></tr>");
		
		setImgPath(sb.toString());
	}
	
	public String getImgContextPath(String name){
		String strID="";
		String strDisplay="";
		if (!displayPictrue){
			strDisplay=" style=\"display:none\" ";
		}
			
		if (!ID.equals("")){
			strID=" id='" +ID +"'";
		}
		return ("<img name='"+name+"' " +strID +" " +strDisplay+ imgEvents
					+ " src='" +getContext()+"/"+name +"' usemap='" + name
				    + "' width="+getImgWidth()+" height="+getImgHeight()
					+  " />");
	}
	
	public String getImgServletPath(String name){
		String strID="";
		String strDisplay="";
		if (!displayPictrue){
			strDisplay=" style=\"display:none\" ";
		}
			
		if (!ID.equals("")){
			strID=" id='" +ID +"'";
		}
		
		return ("<img  name='"+name+"'" +strID +" " +strDisplay+ imgEvents
					+ " src='" + MainGlobal.getHomeContext(page)+"/servlet/DisplayChart?filename="+name
//					+ "src='" +getContext()+"/"+name +"'usemap='" + name
					+  "' usemap='" 
					+ name
				    + "' width="+getImgWidth()+" height="+getImgHeight()+" />");
	}
	
	public String getContext() {
		
		String contextPath = "http://"
			+ page.getRequestCycle().getRequestContext().getServerName()
			+ ":"
			+ page.getRequestCycle().getRequestContext().getServerPort();
		
		if(getZhandmc()!=null && !getZhandmc().equals("")){
			contextPath = contextPath+"/"+getZhandmc()+"/chart";
		}
		return contextPath;
	}
	
	private void getChartPath(){
		JDBCcon con = new JDBCcon();
		String sql = "select zhi,leib from xitxxb where mingc='jfreechart图片生成路径'";
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			if((new File(rsl.getString("zhi")).isDirectory())){
				if(!(new File(rsl.getString("zhi")+"\\chart").isDirectory())){//判断文件夹是否存在
					new File(rsl.getString("zhi")+"\\chart").mkdir(); //新建文件夹
				}
				setChartFilePath(rsl.getString("zhi")+"\\chart");
				setZhandmc(rsl.getString("leib"));
				deleteChart(rsl.getString("zhi")+"\\chart");
			}
		}
		rsl.close();
		con.Close();
	}
	private void deleteChart(String filePath){
		File[] fileList = new File(filePath).listFiles();
		if(fileList != null){
			for(int i=0;i<fileList.length;i++){
				boolean delete = getDeleteFile(fileList[i].lastModified());
				if(delete){
					fileList[i].delete();
				}
			}
		}
	}
	public  boolean getDeleteFile(long mill){
	 	   
    	boolean deleteFile = false;
	    
    	Date date1 = new Date();
    	Date date2 = new Date(mill);
	    
	    long shijc = (date1.getTime()-date2.getTime())/(1000*60);//时间差
	    if(shijc>10){
	    	deleteFile = true;
	    }else{
	    	deleteFile = false;
	    }
	    return deleteFile;
	}
	
}