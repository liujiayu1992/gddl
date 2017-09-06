package com.zhiren.report;

import java.io.Serializable;
import java.util.Date;

public class ChartBean implements Serializable{
	
	private static final long serialVersionUID = -3933894275662736784L;
	
	private String strLegendName;//图例名
	
	private String x_strLine;//x轴坐标
	private Date x_datLine;//x轴坐标
	private double dbDate;//y轴坐标
	
	public ChartBean(){
		
	}
/*----------------  x轴数据为Date型的方法(曲线图) -------------------*/
	
	public ChartBean(String LegendName, Date xLine, double yLine){
		this.strLegendName = LegendName;
		this.x_datLine = xLine;
		this.dbDate = yLine;
	}

/*---------------  x轴数据为String型的方法(柱状图)  ------------------*/	
	
	public ChartBean(String LegendName, String xLine, double yLine){
		this.strLegendName = LegendName;
		this.x_strLine = xLine;
		this.dbDate = yLine;
	}

/*----------------------  构造饼型图的方法  --------------------------*/		
	
	public ChartBean(String LegendName, double db_Data){
		this.strLegendName = LegendName;
		this.dbDate = db_Data;
	}
	

	public String getLegendName(){
		return this.strLegendName;
	}
	public void setLegendName(String value){
		this.strLegendName = value;
	}
	
	public String getX_strLine(){
		return this.x_strLine;
	}
	public void setX_strLine(String xvalue){
		this.x_strLine = xvalue;
	}
	
	public Date getX_datLine(){
		return this.x_datLine;
	}
	public void setX_datLine(Date xvalue){
		this.x_datLine = xvalue;
	}
	
	public double getDb_Data(){
		return dbDate;
	}
	public void setDb_Data(double yvalue){
		dbDate = yvalue;
	}
	
	
}
