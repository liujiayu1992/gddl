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
 * ʱ��:2011-06-08
 * ����:���
 * ����:ChartXYLine����������x���Ƿ�����б�̶ȵ��ж�
 * 
 */
/*
 * ����:���
 * ʱ��:2012-11-23
 * ����:��״ͼ����������λ�õ����÷�����
 * 	       ������״ͼÿ�����Ӵ�С�����á�
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
	
	//���ò�����Ϣ
	private String imgPath="";//��ѯͼƬ·��
	private String chartFilePath="";//����ͼƬ·��
	private String chartZhandmc="";//ͼƬվ������
	private IPage page = null;
	private ResultSetList rsl;
//	����
	private String imgName;
//	���
	private int imgWidth;
//	�߶�
	private int imgHeight;
//	X���ֶ���
	private String xLine;
//	ͼ���ֶ���
	private String LegendName;
//	Y���ֶ���
	private String yLine;
//	��ʾ�ٷ���
	private boolean blnPercent;
//	�����ֶ���
	private String dataName;
//	��ʾͼ��
	public boolean showLegend = true;//�Ƿ���ʾͼ��
//	X����
	public String xlineName = "";//x��
//	Y����
	public String ylineName = "";//y��
//	��ʾX����
	public boolean showXvalue = true;//��ʾX������
//	��ʾY����
	public boolean showYvalue = true;//��ʾY������
	
//	����
	public String titlefontName="����";
//	����
	public int titlefont = Font.PLAIN; //������״
//	�ֺ�
	public int titlefontSize=16; //�����С
//	�����
	public boolean blnAntiAlias = true;//�Ƿ�ʹ����ʾ����ʱ�ķ���ݹ���;
//	YС��λ
	public int intDigits = 2;//����y�����ݵ�С��λ
//	X�ֺ�
	public int xfontSize = 9;//����x������������С
//	X��б
	public boolean xTiltShow = false;//����x������ֵ��б��ʾ��x������ӦΪ�ַ���
	
//	public Color chartBackgroundPaint = Color.lightGray;//ͼƬ������ɫ	
	public GradientPaint chartBackgroundPaint = new GradientPaint(0, 0,  Color.lightGray, 500, 0, Color.white);//ͼƬ������ɫ
	public GradientPaint plotBackgroundPaint = new GradientPaint(-500, 500, Color.green, 300, 0, Color.white);
//	�ϱ߾�
	public double rangeUpperMargin = 0.15;//��ߵ����ͼƬ�����ĸ߶�
//	�±߾�
	public double rangeLowerMargin = 0.1;//��͵����ͼƬ�ײ��ĸ߶�
	
//	����ͼ line �Ĳ�����Ϣ
//	��ʾ���ݵ�
	public boolean lineShapesVisiblebln = true;//���������Ƿ���ʾ���ݵ�
//	X��ʾ��ʽ
	public String lineDateFormatOverride = "MM-dd";//x��Ϊ����ʱ����ʾ��ʽ
//	X��ֱ��ʾ
	public boolean dateApeakShowbln = false;//x��������Ƿ���ֱ��ʾ
	
//	��״ͼ bar �Ĳ�����Ϣ	
//	�����
	public double MaximumBarWidth=0.035;
//	������
	public double MinimumBarLength=0.035;
//	�ұ߾�
	public double barDomUpperMargin = 0.015;//������״ͼ����ͼƬ�Ҷ˾���
//	��߾�
	public double barDomLowerMargin = 0.025;//������״ͼ����ͼƬ��˾���
//	͸����
	public float barForegroundAlpha = 1;//���ӵ�͸����
//	��ʾ�߿�
	public boolean barOutlinebln = true;//���þ��������Ƿ���ʾ�߿�
//	�߿���ɫ
	public Color barOutlineColor = Color.black;//�������ӱ߿����ɫ
//	
	public double barItemMargin = 0.1;//���ø�������������ֱ�ӵļ��(-1��0.6֮��)
//	����ʾ����
	public boolean barLabelsFontbln = true;//�����������Ƿ���ʾ����
//	������
	public String barfontName="����";	//��������ʾ������
//	������
	public int barfont = Font.PLAIN; //��������ʾ��������״
//	���ֺ�
	public int barfontSize=11; //��������ʾ�������С
	public ItemLabelAnchor barfontPlace = ItemLabelAnchor.OUTSIDE1;//OUTSIDE1\2\10\11\12:���ˣ�OUTSIDE4��8:�ײ���OUTSIDE3\9:�м�
	public ItemLabelAnchor barfontPlaceNormal= ItemLabelAnchor.OUTSIDE1;//OUTSIDE1\2\10\11\12:���ˣ�OUTSIDE4��8:�ײ���OUTSIDE3\9:�м�
//	����б
	public int outSide = 25;//�Ƿ��������ݾ������Ӷ��˵ľ���
	public double barfontTilt = -0.7;//�������ֵ���б�ȡ�0:ˮƽ
	
//	����ͼ pie �Ĳ�����Ϣ
	public final String piedatformat0 = "{0}";		//����ʾ���ֺͰٷֱ�
	public final String piedatformat1 = "{0} {1}";	//����ͼ����ʾ����
	public final String piedatformat2 = "{0} {1}%";	//��ʾ�ٷ�����
	
//	����͸����
	public float pieBackgroundAlpha = 0.6f;//	�趨����͸���ȣ�0-1.0֮�䣩
//	ǰ��͸����
	public float pieForegroundAlpha = 1;	 //	�趨ǰ��͸���ȣ�0-1.0֮�䣩
//	��ʾ����
	public Rotation pieRotation = Rotation.CLOCKWISE;//������˳ʱ����ʾ������ʱ����ʾ
//	����
	public double pieStartAngle = 90;//���û��Ƶ�һ��Բ���ĽǶ�
//	Բ��
	public boolean pieCircularbln = false;//������ʾΪ��Բ��(false)����Բ��(true)
//	����ʾ����
	public boolean pieLabFormat = true;		//ͼ���Ƿ���ʾ��������
//	����ʾ����
	public String pieLabGenerator = this.piedatformat1;//����ͼ����ʾ����
//	
	public String pieLegendLabGenerator = this.piedatformat0;//����ͼ���ϲ���ʾ����
	
//	setDateUnit
	public static final int DATEUNIT_DAY = DateTickUnit.DAY;
	public static final int DATEUNIT_MONTH = DateTickUnit.MONTH;
	public static final int DATEUNIT_YEAR = DateTickUnit.YEAR;
	public static final int DATEUNIT_HOUR = DateTickUnit.HOUR;
	public static final int DATEUNIT_MILLISECOND = DateTickUnit.MILLISECOND;
	public static final int DATEUNIT_MINUTE = DateTickUnit.MINUTE;
	public static final int DATEUNIT_SECOND = DateTickUnit.SECOND;
	
	
//	����ͼ��λ��
	public static final RectangleEdge LEGEND_POS_RIGHT = RectangleEdge.RIGHT;
	public static final RectangleEdge LEGEND_POS_LEFT = RectangleEdge.LEFT;
	public static final RectangleEdge LEGEND_POS_BOTTOM = RectangleEdge.BOTTOM;
	public static final RectangleEdge LEGEND_POS_TOP = RectangleEdge.TOP;
	private RectangleEdge legendPosition = Chart.LEGEND_POS_BOTTOM;	//ͼ����ʾλ��
	
	
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
		if("����".equals(name)) {
			imgName = v;
		}else if("���".equals(name)){
			imgWidth = Integer.parseInt(v);
		}else if("�߶�".equals(name)){
			imgHeight = Integer.parseInt(v);
		}else if("X���ֶ���".equals(name)){
			xLine = v;
		}else if("ͼ���ֶ���".equals(name)){
			LegendName = v;
		}else if("Y���ֶ���".equals(name)){
			yLine = v;
		}else if("��ʾ�ٷ���".equals(name)){
			blnPercent = Boolean.valueOf(v).booleanValue();
		}else if("�����ֶ���".equals(name)){
			dataName = v;
		}else if("��ʾͼ��".equals(name)){
			showLegend = Boolean.valueOf(v).booleanValue();
		}else if("X����".equals(name)){
			xlineName = v;
		}else if("Y����".equals(name)){
			ylineName = v;
		}else if("��ʾX����".equals(name)){
			showXvalue = Boolean.valueOf(v).booleanValue();
		}else if("��ʾY����".equals(name)){
			showYvalue = Boolean.valueOf(v).booleanValue();
		}else if("����".equals(name)){
			titlefontName = v;
		}else if("����".equals(name)){
			titlefont = Integer.parseInt(v);
		}else if("�ֺ�".equals(name)){
			titlefontSize = Integer.parseInt(v);
		}else if("YС��λ".equals(name)){
			intDigits = Integer.parseInt(v);
		}else if("X��ʾ��ʽ".equals(name)){
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
	
//	ʱ������ͼ
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
			System.out.println("/******** Session����Ϊ�� ********/");
			return "";
		}
		
		/*------------����JFreeChart,��dataset�е����ݵ��뵽JFreeChart��-------------*/
		
		JFreeChart chart = ChartFactory.createLineChart(imgchartName, // title 
				xlineName, //      x������� 
				ylineName, //      y������� 
				dataset,   // 		���ݼ� 
				PlotOrientation.VERTICAL,        // orientation
				showLegend,//   	�Ƿ����ͼ��
				false,    //  		�Ƿ������ʾ���� 
				false 	   //   	�Ƿ����url
				);

		/*------------����ͼ��(chart)����--------------*///����JFreeChart����ʾ����,��ͼ���ⲿ���ֽ��е��� 
		chart.setBackgroundPaint(chartBackgroundPaint);//��������ͼ����ɫ 
		Font font = new Font(titlefontName, titlefont, titlefontSize); //���������С����״ 
		TextTitle title = new TextTitle(imgchartName, font);
		chart.setTitle(title);
//		����ͼ����ʾλ��
//		chart.getLegend().setPosition(getLegendPos());
		
		/*------------�趨Plot����-------------*/
		CategoryPlot plot = (CategoryPlot) chart.getPlot();//��ȡͼ�εĻ��� 
		        plot.setBackgroundPaint(plotBackgroundPaint);//�������񱳾�ɫ 
		//        plot.setDomainGridlinePaint(Color.lightGray);//������������(Domain��)��ɫ 
		//        plot.setRangeGridlinePaint(Color.lightGray);//�������������ɫ 
		//        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));//��������ͼ��xy��ľ��� 
		//        plot.setDomainCrosshairVisible(false); 
		//        plot.setRangeCrosshairVisible(false);
		//        plot.setDomainCrosshairLockedOnData(false);

		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setUpperMargin(rangeUpperMargin);//������ߵ�һ������ͼƬ���˵ľ���
		rangeAxis.setLowerMargin(rangeLowerMargin);//������͵�һ������ͼƬ�׶˵ľ���
		plot.setRangeAxis(rangeAxis);
		
		CategoryAxis domainAxis = plot.getDomainAxis();
    	domainAxis.setTickLabelFont(new Font("sans-serif",Font.PLAIN,xfontSize));//����X�������ϵ�����
//		domainAxis.setLabelFont(new Font("����",Font.PLAIN,12));          //����X��ı�������      
//		rangeAxis.setTickLabelFont(new Font("sans-serif",Font.PLAIN,12));     //����Y�������ϵ�����
//		rangeAxis.setLabelFont(new Font("����",Font.PLAIN,12));               //����Y��ı�������

    	domainAxis.setVisible(showXvalue);
    	rangeAxis.setVisible(showYvalue);
    	if(xTiltShow){
			domainAxis.setCategoryLabelPositions(//����X��������б�̶�
					CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
		}
    	
		NumberAxis numAxis = (NumberAxis) plot.getRangeAxis(); //      ����Y��
		NumberFormat numFormater = NumberFormat.getNumberInstance();
		numFormater.setMinimumFractionDigits(intDigits);
		numAxis.setNumberFormatOverride(numFormater);

		/*------------�趨Renderer����-------------*/
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
		renderer.setShapesVisible(lineShapesVisiblebln);//��ʾ���ݵ�
//        renderer.setUseFillPaint(false);//��ʾ���ݵ�Ϊ����
        renderer.setFillPaint(Color.white);

		/*------------����ͼƬ ��servlet��ʽ-------------*/
		String fileName = null;
		ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
		PrintWriter pw = null;
		try {
			ServletOutputStream out = page.getRequestCycle().getRequestContext().getResponse().getOutputStream();
			pw = new PrintWriter(out);
			
			if(getChartFilePath()!=null && !getChartFilePath().equals("")){
				String prefix = ServletUtilities.getTempFilePrefix();
				  File tempFile = File.createTempFile(prefix, ".jpg", new File(getChartFilePath()));
	
				  ChartUtilities.saveChartAsJPEG(tempFile,chart, imgWidth, imgHeight, info);//����ͼƬ
				  
				  if (page.getRequestCycle().getRequestContext().getSession() != null) 
	            {
	                  ServletUtilities.registerChartForDeletion(tempFile, page.getRequestCycle().getRequestContext().getSession());
	            }
				  setImgContextPath(tempFile.getName());
	            return getImgContextPath(tempFile.getName());
			}else{
			
				fileName = ServletUtilities.saveChartAsJPEG(chart, imgWidth, imgHeight, info, 
						page.getRequestCycle().getRequestContext().getSession());//����ͼƬ
				
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
		// ���ļ���ʽ
		/*try {
		 File file = new File(MainGlobal.getServletPath(main.getPage(),"tempImg"),"XYchart2.jpg");
		 ChartUtilities.saveChartAsJPEG(file, chart, 500, 300);
		 main.setImgContextPath(file.getName());
		 } catch (Exception e) {
		 System.out.println("����ͼ�γ����쳣.");
		 }  ******************************************************************************/
	}
	
//	ʱ������ͼ
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
			System.out.println("/******** Session����Ϊ�� ********/");
			return "";
		}
		
		/*------------����JFreeChart,��dataset�е����ݵ��뵽JFreeChart��-------------*/
		
		JFreeChart chart = ChartFactory.createTimeSeriesChart(imgchartName, // title 
				xlineName, //      x������� 
				ylineName, //      y������� 
				dataset,   // 		���ݼ� 
				showLegend,//   	�Ƿ����ͼ��
				false,    //  		�Ƿ������ʾ���� 
				false 	   //   	�Ƿ����url
				);

		/*------------����ͼ��(chart)����--------------*///����JFreeChart����ʾ����,��ͼ���ⲿ���ֽ��е��� 
		chart.setBackgroundPaint(chartBackgroundPaint);//��������ͼ����ɫ 
		Font font = new Font(titlefontName, titlefont, titlefontSize); //���������С����״ 
		TextTitle title = new TextTitle(imgchartName, font);
		chart.setTitle(title);
//		����ͼ����ʾλ��
//		chart.getLegend().setPosition(getLegendPos());
		/*------------�趨Plot����-------------*/
		XYPlot plot = (XYPlot) chart.getPlot();//��ȡͼ�εĻ��� 
		//        plot.setBackgroundPaint(Color.WHITE);//�������񱳾�ɫ 
		//        plot.setDomainGridlinePaint(Color.lightGray);//������������(Domain��)��ɫ 
		//        plot.setRangeGridlinePaint(Color.lightGray);//�������������ɫ 
		//        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));//��������ͼ��xy��ľ��� 
		//        plot.setDomainCrosshairVisible(false); 
		//        plot.setRangeCrosshairVisible(false);
		//        plot.setDomainCrosshairLockedOnData(false);

		ValueAxis rangeAxis = plot.getRangeAxis();
		
		rangeAxis.setUpperMargin(rangeUpperMargin);//������ߵ�һ������ͼƬ���˵ľ���
		rangeAxis.setLowerMargin(rangeLowerMargin);//������͵�һ������ͼƬ�׶˵ľ���
		plot.setRangeAxis(rangeAxis);
		plot.setBackgroundPaint(plotBackgroundPaint);
		
     
//		rangeAxis.setTickLabelFont(new Font("sans-serif",Font.PLAIN,12));     //����Y�������ϵ�����
//		rangeAxis.setLabelFont(new Font("����",Font.PLAIN,12));               //����Y��ı�������

		NumberAxis numAxis = (NumberAxis) plot.getRangeAxis(); //      ����Y��
		NumberFormat numFormater = NumberFormat.getNumberInstance();
		numFormater.setMinimumFractionDigits(intDigits);
		numAxis.setNumberFormatOverride(numFormater);
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat(lineDateFormatOverride));
		
		axis.setTickUnit(new DateTickUnit(dateunit,unitCount));
		
		axis.setAutoTickUnitSelection(false);
		axis.setVerticalTickLabels(dateApeakShowbln);
		axis.setTickLabelFont(new Font("sans-serif",Font.PLAIN,xfontSize));//����X�������ϵ�����
//		axis.setLabelFont(new Font("����",Font.PLAIN,12));          //����X��ı������� 
		
		axis.setVisible(showXvalue);
    	rangeAxis.setVisible(showYvalue);
		
		/*------------�趨Renderer����-------------*/
		XYItemRenderer r = plot.getRenderer();
		if (r instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			renderer.setBaseShapesVisible(true);
			renderer.setBaseShapesFilled(true);
			renderer.setShapesVisible(lineShapesVisiblebln);//���������Ƿ���ʾ���ݵ� 
			
		}
//		StandardXYToolTipGenerator tipGenerator = new StandardXYToolTipGenerator(//������ʾ��Ϣ 
//        		"��ʷ��Ϣ:({1} 16:00,{2})", new SimpleDateFormat("MM-dd"),numFormater); 
//        r.setToolTipGenerator(tipGenerator);

		/*------------����ͼƬ ��servlet��ʽ-------------*/
		String fileName = null;
		ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
		PrintWriter pw = null;
		
		try {
			ServletOutputStream out = page.getRequestCycle().getRequestContext().getResponse().getOutputStream();
			pw = new PrintWriter(out);
			
			if(getChartFilePath()!=null && !getChartFilePath().equals("")){
				  String prefix = ServletUtilities.getTempFilePrefix();
				  File tempFile = File.createTempFile(prefix, ".jpg", new File(getChartFilePath()));
	
				  ChartUtilities.saveChartAsJPEG(tempFile,chart, imgWidth, imgHeight, info);//����ͼƬ
				  
				  if (page.getRequestCycle().getRequestContext().getSession() != null) 
	              {
	                    ServletUtilities.registerChartForDeletion(tempFile, page.getRequestCycle().getRequestContext().getSession());
	              }
				  setImgContextPath(tempFile.getName());
	              return getImgContextPath(tempFile.getName());
			}else{
              
				fileName = ServletUtilities.saveChartAsJPEG(chart, imgWidth, imgHeight, info, 
						page.getRequestCycle().getRequestContext().getSession());//����ͼƬ
				
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
	
//	��ͨ��״ͼ
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
			System.out.println("/******** Session����Ϊ�� ********/");
			return "";
		}
		
		/*------------����JFreeChart,��dataset�е����ݵ��뵽JFreeChart��-------------*/
		
		JFreeChart chart = ChartFactory.createBarChart(
				imgchartName,			  // ͼ�����
				xlineName,				  // Ŀ¼�����ʾ��ǩ
				ylineName,				  // ��ֵ�����ʾ��ǩ
				dataset, 				  // ���ݼ�
				PlotOrientation.VERTICAL, // ͼ����ˮƽ����ֱ
				showLegend,  			  // �Ƿ���ʾͼ��(���ڼ򵥵���״ͼ������false)
				false,  				  // �Ƿ����ɹ���
				false  					  // �Ƿ�����URL����
		);
		Font font = new Font(titlefontName, titlefont, titlefontSize); //���������С����״ 
		TextTitle title = new TextTitle(imgchartName, font);
		chart.setTitle(title);
//		����ͼ����ʾλ��
//		chart.getLegend().setPosition(getLegendPos());
		
		/*------------�趨Plot����-------------*/
		CategoryPlot plot = chart.getCategoryPlot();
		chart.setBackgroundPaint(chartBackgroundPaint);
		CategoryAxis domainAxis = plot.getDomainAxis();
//		domainAxis.setLabelAngle(0.0);//����columnKey �Ƿ�ֱ��ʾ
		domainAxis.setUpperMargin(barDomUpperMargin);//���þ���ͼƬ�Ҷ˾���
    	domainAxis.setLowerMargin(barDomLowerMargin);//���þ���ͼƬ��˾���
		plot.setDomainAxis(domainAxis); //���� columnKey �Ƿ�����ʾ
		plot.setBackgroundPaint(plotBackgroundPaint);
		
		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setUpperMargin(rangeUpperMargin);//������ߵ�һ������ͼƬ���˵ľ���
		rangeAxis.setLowerMargin(rangeLowerMargin);//������͵�һ������ͼƬ�׶˵ľ���
		
		domainAxis.setVisible(showXvalue);
		rangeAxis.setVisible(showYvalue);
		if(xTiltShow){
			domainAxis.setCategoryLabelPositions(//����X��������б�̶�
					CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
		}
		
		plot.setRangeAxis(rangeAxis); 
		
//		domainAxis.setTickLabelFont(new Font("sans-serif",Font.PLAIN,20));//����X�������ϵ�����
//		domainAxis.setLabelFont(new Font("����",Font.PLAIN,12));          //����X��ı�������      
//		rangeAxis.setTickLabelFont(new Font("sans-serif",Font.PLAIN,12));     //����Y�������ϵ�����
//		rangeAxis.setLabelFont(new Font("����",Font.PLAIN,12));               //����Y��ı�������
		
		plot.setForegroundAlpha(barForegroundAlpha); //����ÿ������͸����
//		plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);//���õ�������������ʾλ��
//		plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

		/*------------�趨Renderer����-------------*/
		BarRenderer renderer = new BarRenderer();
		
		renderer.setDrawBarOutline(barOutlinebln);//�����Ƿ���ʾ���ӱ߿�
		renderer.setOutlinePaint(barOutlineColor);//�������ӱ߿����ɫ
		
//		renderer.setWallPaint(Color.gray);//���� Wall ����ɫ
//		renderer.setSeriesPaint(0, Color.GREEN);//	����ÿ�����ӵ���ɫ	

		
		renderer.setItemMargin(barItemMargin);//����ÿ��������������ƽ������֮�����
		
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMinimumFractionDigits(intDigits);
		renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}",format));
		ItemLabelPosition itemlabelposition = new ItemLabelPosition(barfontPlace, 
                TextAnchor.CENTER, 
                TextAnchor.BASELINE_CENTER, 
                barfontTilt);
		renderer.setPositiveItemLabelPositionFallback(itemlabelposition);
		renderer.setNegativeItemLabelPositionFallback(itemlabelposition);
		
//		������������BAR����ʾ���ݵ���ʾλ��
		ItemLabelPosition itemlabelpositionN = new ItemLabelPosition(barfontPlaceNormal, 
                TextAnchor.CENTER, 
                TextAnchor.BASELINE_CENTER, 
                barfontTilt);
		renderer.setPositiveItemLabelPosition(itemlabelpositionN);
		renderer.setNegativeItemLabelPosition(itemlabelpositionN);
		
		renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());//��ʾÿ��������ֵ�����޸ĸ���ֵ����������
		renderer.setItemLabelFont(new Font(barfontName,barfont,barfontSize));
		renderer.setItemLabelAnchorOffset(outSide);
		renderer.setItemLabelsVisible(barLabelsFontbln);
		plot.setRenderer(renderer);
		
		/*------------����ͼƬ ���ļ���ʽ-------------*/
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
	
				  ChartUtilities.saveChartAsJPEG(tempFile,chart, imgWidth, imgHeight, info);//����ͼƬ
				  
				  if (page.getRequestCycle().getRequestContext().getSession() != null) 
	              {
	                   ServletUtilities.registerChartForDeletion(tempFile, page.getRequestCycle().getRequestContext().getSession());
	              }
				  setImgContextPath(tempFile.getName());
	              return getImgContextPath(tempFile.getName());
	             
			 }else{
	        	fileName = ServletUtilities.saveChartAsJPEG(chart, imgWidth, imgHeight, info,
	        	page.getRequestCycle().getRequestContext().getSession());//����ͼƬ
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
	
//	��ͨ��״ͼ	
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
			System.out.println("/******** Session����Ϊ�� ********/");
			return "";
		}
		
		/*------------����JFreeChart,��dataset�е����ݵ��뵽JFreeChart��-------------*/
		
		JFreeChart chart = ChartFactory.createBarChart3D(
				imgchartName,			  // ͼ�����
				xlineName,				  // Ŀ¼�����ʾ��ǩ
				ylineName,				  // ��ֵ�����ʾ��ǩ
				dataset, 				  // ���ݼ�
				PlotOrientation.VERTICAL, // ͼ����ˮƽ����ֱ
				showLegend,  			  // �Ƿ���ʾͼ��(���ڼ򵥵���״ͼ������false)
				false,  				  // �Ƿ����ɹ���
				false  					  // �Ƿ�����URL����
		);
		chart.setBackgroundPaint(chartBackgroundPaint);
		Font font = new Font(titlefontName, titlefont, titlefontSize); //���������С����״ 
		TextTitle title = new TextTitle(imgchartName, font);
		chart.setTitle(title);
//		����ͼ����ʾλ��
//		chart.getLegend().setPosition(getLegendPos());
		
		/*------------�趨Plot����-------------*/
		CategoryPlot plot = chart.getCategoryPlot();
		CategoryAxis domainAxis = plot.getDomainAxis();
//		domainAxis.setLabelAngle(0.0);//����columnKey �Ƿ�ֱ��ʾ
		domainAxis.setUpperMargin(barDomUpperMargin);//���þ���ͼƬ�Ҷ˾���
    	domainAxis.setLowerMargin(barDomLowerMargin);//���þ���ͼƬ��˾���
		plot.setDomainAxis(domainAxis); //���� columnKey �Ƿ�����ʾ
		
		domainAxis.setVisible(showXvalue);
		
		
		if(xTiltShow){
			domainAxis.setCategoryLabelPositions(//����X��������б�̶�
					CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
		}
		plot.setBackgroundPaint(plotBackgroundPaint);
		
		ValueAxis rangeAxis = plot.getRangeAxis();
		
		rangeAxis.setVisible(showYvalue);
		
		rangeAxis.setUpperMargin(rangeUpperMargin);//������ߵ�һ������ͼƬ���˵ľ���
		rangeAxis.setLowerMargin(rangeLowerMargin);//������͵�һ������ͼƬ�׶˵ľ���
		plot.setRangeAxis(rangeAxis); 
		
//		domainAxis.setTickLabelFont(new Font("sans-serif",Font.PLAIN,20));//����X�������ϵ�����
//		domainAxis.setLabelFont(new Font("����",Font.PLAIN,12));          //����X��ı�������      
//		rangeAxis.setTickLabelFont(new Font("sans-serif",Font.PLAIN,12));     //����Y�������ϵ�����
//		rangeAxis.setLabelFont(new Font("����",Font.PLAIN,12));               //����Y��ı�������
		
		plot.setForegroundAlpha(barForegroundAlpha); //����ÿ������͸����
//		plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);//���õ�������������ʾλ��
//		plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

		/*------------�趨Renderer����-------------*/
		BarRenderer3D renderer = new BarRenderer3D();
		
		renderer.setDrawBarOutline(barOutlinebln);//�����Ƿ���ʾ���ӱ߿�
//		renderer.setWallPaint(Color.gray);//���� Wall ����ɫ
//		renderer.setSeriesPaint(0, Color.GREEN);//	����ÿ�����ӵ���ɫ	

		renderer.setOutlinePaint(barOutlineColor);//�������ӱ߿����ɫ
		
		renderer.setItemMargin(barItemMargin);//����ÿ��������������ƽ������֮�����
		
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
		
//		������������BAR����ʾ���ݵ���ʾλ��
		ItemLabelPosition itemlabelpositionN = new ItemLabelPosition(barfontPlaceNormal, 
                TextAnchor.CENTER, 
                TextAnchor.BASELINE_CENTER, 
                barfontTilt);
		renderer.setPositiveItemLabelPosition(itemlabelpositionN);
		renderer.setNegativeItemLabelPosition(itemlabelpositionN);
		
		renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());//��ʾÿ��������ֵ�����޸ĸ���ֵ����������
		renderer.setItemLabelFont(new Font(barfontName,barfont,barfontSize));
		renderer.setItemLabelAnchorOffset(outSide);//�������������ӵĶ��˾���
		renderer.setItemLabelsVisible(barLabelsFontbln);
		renderer.setMaximumBarWidth(MaximumBarWidth);
		plot.setRenderer(renderer);
		
		/*------------����ͼƬ ���ļ���ʽ-------------*/
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
	
				  ChartUtilities.saveChartAsJPEG(tempFile,chart, imgWidth, imgHeight, info);//����ͼƬ
				  
				  if (page.getRequestCycle().getRequestContext().getSession() != null) 
	             {
	                   ServletUtilities.registerChartForDeletion(tempFile, page.getRequestCycle().getRequestContext().getSession());
	             }
				  setImgContextPath(tempFile.getName());
	             return getImgContextPath(tempFile.getName());
			 }else{
	        	fileName = ServletUtilities.saveChartAsJPEG(chart, imgWidth, imgHeight, info,
	        	page.getRequestCycle().getRequestContext().getSession());//����ͼƬ
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
	
//	������״ͼ	
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
			System.out.println("/******** Session����Ϊ�� ********/");
			return "";
		}
		
		/*------------����JFreeChart,��dataset�е����ݵ��뵽JFreeChart��-------------*/
		
    	JFreeChart chart = ChartFactory.createStackedBarChart3D(imgchartName,
		xlineName,				    // Ŀ¼�����ʾ��ǩ
		ylineName,				    // ��ֵ�����ʾ��ǩ
		dataset, 					// ���ݼ� 
    	PlotOrientation.VERTICAL,   // ͼ����ˮƽ����ֱ
    	showLegend,					// �Ƿ���ʾͼ��(���ڼ򵥵���״ͼ������false)
    	false,						// �Ƿ����ɹ���
    	false);						// �Ƿ�����URL����
		
		/*------------����ͼ��(chart)����--------------*/
		
		Font font = new Font(titlefontName, titlefont, titlefontSize); //���������С����״ 
		TextTitle title = new TextTitle(imgchartName, font);
		chart.setTitle(title);
//		����ͼ����ʾλ��
//		chart.getLegend().setPosition(getLegendPos());
		
		CategoryPlot plot = chart.getCategoryPlot();
    	CategoryAxis domainAxis = plot.getDomainAxis();
    	//domainAxis.setVerticalCategoryLabels(false);
    	plot.setDomainAxis(domainAxis);
    	ValueAxis rangeAxis = plot.getRangeAxis();
    	
    	chart.setBackgroundPaint(chartBackgroundPaint);//  ��������ͼ������ɫ
		plot.setBackgroundPaint(plotBackgroundPaint);
    	
    	domainAxis.setUpperMargin(barDomUpperMargin);//���þ���ͼƬ�Ҷ˾���
    	domainAxis.setLowerMargin(barDomLowerMargin);//���þ���ͼƬ��˾���
		
    	rangeAxis.setUpperMargin(rangeUpperMargin);//������ߵ�һ������ͼƬ���˵ľ���
    	rangeAxis.setLowerMargin(rangeLowerMargin);//������͵�һ������ͼƬ�׶˵ľ���
    	
    	domainAxis.setVisible(showXvalue);
    	rangeAxis.setVisible(showYvalue);
    	
    	if(xTiltShow){
			domainAxis.setCategoryLabelPositions(//����X��������б�̶�
					CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
		}
    	
    	plot.setRangeAxis(rangeAxis);
    	
//    	domainAxis.setTickLabelFont(new Font("sans-serif",Font.PLAIN,20));//����X�������ϵ�����
//		domainAxis.setLabelFont(new Font("����",Font.PLAIN,12));          //����X��ı�������      
//		rangeAxis.setTickLabelFont(new Font("sans-serif",Font.PLAIN,12));     //����Y�������ϵ�����
//		rangeAxis.setLabelFont(new Font("����",Font.PLAIN,12));               //����Y��ı�������
    	
//	    ��������͸����
    	plot.setForegroundAlpha(barForegroundAlpha);
//    	plot.setAxisOffset(new RectangleInsets(0.0, 0.0, 0.0, 0.0));
//	    	���õ�������������ʾλ��
    	//plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
    	//plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

    	StackedBarRenderer3D renderer = new StackedBarRenderer3D();
    	renderer.setDrawBarOutline(barOutlinebln);//�����Ƿ���ʾ���ӱ߿�
    	
//    	renderer.setWallPaint(Color.LIGHT_GRAY); //���� Wall ����ɫ

    	renderer.setOutlinePaint(barOutlineColor);//�������ӱ߿����ɫ
    	
    	renderer.setItemMargin(barItemMargin);//����ÿ��������������ƽ����֮��ľ���
    	
//	    	��ʾÿ��������ֵ�����޸ĸ���ֵ����������
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

    	/*------------����ͼƬ ��servlet��ʽ-------------*/
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
	
				  ChartUtilities.saveChartAsJPEG(tempFile,chart, imgWidth, imgHeight, info);//����ͼƬ
				  
				  if (page.getRequestCycle().getRequestContext().getSession() != null) 
	            {
	                  ServletUtilities.registerChartForDeletion(tempFile, page.getRequestCycle().getRequestContext().getSession());
	            }
				  setImgContextPath(tempFile.getName());
	            return getImgContextPath(tempFile.getName());
        	}else{
	        	fileName = ServletUtilities.saveChartAsJPEG(chart, imgWidth, imgHeight, info,
	        						page.getRequestCycle().getRequestContext().getSession());//����ͼƬ
	        	
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
	
//	��ͨ����ͼ	
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
			System.out.println("/******** Session����Ϊ�� ********/");
			return "";
		}
		/*------------����JFreeChart,��dataset�е����ݵ��뵽JFreeChart��-------------*/
		
		JFreeChart chart =         					//����JFreeChart����ʹ��ChartFactory������JFreeChart,�ܱ�׼�Ĺ������ģʽ
				ChartFactory.createPieChart(imgchartName, piedata, showLegend, true, true);

//		����ͼ����ʾλ��
//		chart.getLegend().setPosition(getLegendPos());
		
		/*------------����ͼ��(chart)����--------------*/
		Font font = new Font(titlefontName, titlefont, titlefontSize); //���������С����״ 
		TextTitle title = new TextTitle(imgchartName, font);
		chart.setTitle(title);
		chart.setBackgroundPaint(chartBackgroundPaint);//  ��������ͼ������ɫ

		chart.setAntiAlias(false);//�Ƿ�ʹ����ʾ����ķ���ݹ���;
		
		/*------------�趨Plot����-------------*/
		PiePlot pie = (PiePlot)chart.getPlot();              //��ͼʹ��һ��PiePlot
		
		pie.setBackgroundPaint(plotBackgroundPaint);
		/*pie.setSectionLabelType(PiePlot.NAME_AND_PERCENT_LABELS);
		pie.setSectionLabelType(PiePlot.NAME_AND_VALUE_LABELS);*/
		pie.setLabelGenerator(new StandardPieSectionLabelGenerator());
		pie.setLabelFont(new Font("����", Font.TRUETYPE_FONT, 12));
//		
		//pie.setPercentFormatString("#,###0.0#%");//�趨��ʾ��ʽ(���ƼӰٷֱȻ���ֵ)
//		
		pie.setBackgroundPaint(Color.white);//
		
		pie.setCircular(pieCircularbln);//falseΪ��Բ��trueΪԲ��
		
		//pie.setSectionLabelFont(new Font("����", Font.TRUETYPE_FONT, 12));
//		�趨����͸���ȣ�0-1.0֮�䣩
		pie.setBackgroundAlpha(pieBackgroundAlpha);
//		�趨ǰ��͸���ȣ�0-1.0֮�䣩
		pie.setForegroundAlpha(pieForegroundAlpha);

		pie.setDirection(pieRotation);//������˳ʱ�뻹����ʱ��

		pie.setStartAngle(pieStartAngle);//������ʾ��һ��Բ���ĽǶ�

		pie.setLabelGenerator(new StandardPieSectionLabelGenerator(pieLabGenerator));//ͼ�ϵ����ƺ�����ʾ����
		pie.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(pieLegendLabGenerator)); //ͼ�������ƺ�����ʾ�ٷֱ�%
		
		/*------------����ͼƬ ��servlet��ʽ-------------*/
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
	
				  ChartUtilities.saveChartAsJPEG(tempFile,chart, imgWidth, imgHeight, info);//����ͼƬ
				  
				  if (page.getRequestCycle().getRequestContext().getSession() != null) 
	            {
	                  ServletUtilities.registerChartForDeletion(tempFile, page.getRequestCycle().getRequestContext().getSession());
	            }
				  setImgContextPath(tempFile.getName());
	            return getImgContextPath(tempFile.getName());
			}else{
				fileName = ServletUtilities.saveChartAsJPEG(chart, 800, 400, info,
						page.getRequestCycle().getRequestContext().getSession());//����ͼƬ
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
	
	
//	3D����ͼ	
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
			System.out.println("/******** Session����Ϊ�� ********/");
			return "";
		}
		/*------------����JFreeChart,��dataset�е����ݵ��뵽JFreeChart��-------------*/
		
		JFreeChart chart = ChartFactory.createPieChart3D(imgchartName,      // ͼ�����
					piedata,      	// ��ͼ���ݼ�
					showLegend,     // �趨�Ƿ���ʾͼ��
					true,     	  	// �趨�Ƿ���ʾͼ������
					false);     	// �趨�Ƿ���������

		/*------------����ͼ��(chart)����--------------*/
		Font font = new Font(titlefontName, titlefont, titlefontSize); //���������С����״ 
		TextTitle title = new TextTitle(imgchartName, font);
		chart.setTitle(title);
		chart.setBackgroundPaint(chartBackgroundPaint);//  ��������ͼ������ɫ
//		����ͼ����ʾλ��
//		chart.getLegend().setPosition(getLegendPos());
		
		chart.setAntiAlias(false);//�Ƿ�ʹ����ʾ����ķ���ݹ���;
		
		/*------------�趨Plot����-------------*/
		PiePlot pie = (PiePlot)chart.getPlot();              //��ͼʹ��һ��PiePlot
		
		/*pie.setSectionLabelType(PiePlot.NAME_AND_PERCENT_LABELS);
		pie.setSectionLabelType(PiePlot.NAME_AND_VALUE_LABELS);*/
		pie.setLabelFont(new Font("����", Font.TRUETYPE_FONT, 12));
//		
		//pie.setPercentFormatString("#,###0.0#%");//�趨��ʾ��ʽ(���ƼӰٷֱȻ���ֵ)
		
		pie.setBackgroundPaint(plotBackgroundPaint);//
		
		pie.setCircular(pieCircularbln);//falseΪ��Բ��trueΪԲ��
		
		//pie.setSectionLabelFont(new Font("����", Font.TRUETYPE_FONT, 12));
//		�趨����͸���ȣ�0-1.0֮�䣩
		pie.setBackgroundAlpha(pieBackgroundAlpha);
//		�趨ǰ��͸���ȣ�0-1.0֮�䣩
		pie.setForegroundAlpha(pieForegroundAlpha);

		pie.setDirection(pieRotation);//������˳ʱ�뻹����ʱ��

		pie.setStartAngle(pieStartAngle);//������ʾ��һ��Բ���ĽǶ�

		if(pieLabFormat){
			pie.setLabelGenerator(new StandardPieSectionLabelGenerator(pieLabGenerator));//ͼ�ϵ����ƺ�����ʾ����
		}else{
			pie.setLabelGenerator(null);
		}
		pie.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(pieLegendLabGenerator)); //ͼ�������ƺ�����ʾ����%
		
		/*------------����ͼƬ ��servlet��ʽ-------------*/
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
	
				  ChartUtilities.saveChartAsJPEG(tempFile,chart, imgWidth, imgHeight, info);//����ͼƬ
				  
				  if (page.getRequestCycle().getRequestContext().getSession() != null) 
	            {
	                  ServletUtilities.registerChartForDeletion(tempFile, page.getRequestCycle().getRequestContext().getSession());
	            }
				  setImgContextPath(tempFile.getName());
	            return getImgContextPath(tempFile.getName());
			}else{
				fileName = ServletUtilities.saveChartAsJPEG(chart, imgWidth, imgHeight, info,
						page.getRequestCycle().getRequestContext().getSession());//����ͼƬ
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
		String sql = "select zhi,leib from xitxxb where mingc='jfreechartͼƬ����·��'";
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			if((new File(rsl.getString("zhi")).isDirectory())){
				if(!(new File(rsl.getString("zhi")+"\\chart").isDirectory())){//�ж��ļ����Ƿ����
					new File(rsl.getString("zhi")+"\\chart").mkdir(); //�½��ļ���
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
	    
	    long shijc = (date1.getTime()-date2.getTime())/(1000*60);//ʱ���
	    if(shijc>10){
	    	deleteFile = true;
	    }else{
	    	deleteFile = false;
	    }
	    return deleteFile;
	}
	
}