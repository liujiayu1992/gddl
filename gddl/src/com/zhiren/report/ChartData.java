package com.zhiren.report;


import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import com.zhiren.common.DateUtil;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.CustomMaths;

public class ChartData {
	public static final int PIE_ROUND=1;
	public static final int PIE_ROUND_NEW=2;
	
	private int  PieRoundMath=PIE_ROUND_NEW;
	
	public void setPieRoundMath(int PieRoundMath ){
		this.PieRoundMath=PieRoundMath;
	}
	
//	构造各种图需要数据
	public ChartData(){
	}

//	rs 记录集构造普通柱状图	
	public CategoryDataset getRsDataChart(ResultSet rs,String xLine,String LegendName,String yLine){//xLine:x轴坐标字段名；LegendName:图例字段名；yLine轴字段名称，数据类型为double\float\int

		/*------------设定Dataset-------------*/
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		try {
			while(rs.next()){
				dataset.addValue(rs.getDouble(yLine), rs.getString(LegendName), rs.getString(xLine));
			}
			
		}catch(Exception e){
			System.out.println("/********************* 记录集构造数据失败  *************************/");
			e.printStackTrace();
		}finally{
		}
		return dataset;
	}
//	rslist 记录集构造普通柱状图	
	public CategoryDataset getRsDataChart(ResultSetList rs,String xLine,String LegendName,String yLine){//xLine:x轴坐标字段名；LegendName:图例字段名；yLine轴字段名称，数据类型为double\float\int

		/*------------设定Dataset-------------*/
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		try {
			rs.beforefirst();
			while(rs.next()){
				dataset.addValue(rs.getDouble(yLine), rs.getString(LegendName), rs.getString(xLine));
			}
			rs.close();
			
		}catch(Exception e){
			System.out.println("/********************* 记录集构造数据失败  *************************/");
			e.printStackTrace();
		}finally{
		}
		return dataset;
	}
	
//	ArrayList 构造普通柱状图	
	public CategoryDataset getArrayDataChart(ArrayList array){//非rs记录集的普通柱状图的数据处理

		/*------------设定Dataset-------------*/
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		try {
			for(int i=0;i<array.size();i++){
				dataset.addValue(((ChartBean)array.get(i)).getDb_Data(), ((ChartBean)array.get(i)).getLegendName(), ((ChartBean)array.get(i)).getX_strLine());
			}
			
		}catch(Exception e){
			System.out.println("/********************* 数据构造失败 *************************/");
			e.printStackTrace();
		}finally{
		}
		return dataset;
	}
	
//	rs记录集构造曲线图	
	public TimeSeriesCollection getRsDataTimeGraph(ResultSet rs,String lineName,String xline,String yline){//xline:横轴坐标对应的字段名称,数据类型为日期；yline:纵轴坐标对应的字段名称,数据类型为long型；lineName:曲线名称对应的字段名称

		Vector vorLine = new Vector();//曲线顶点数组
		Vector vorLineName = new Vector();//曲线名称数组
		
		/*------------设定Dataset-------------*/
		try{
			int hasit = 0;
			while(rs.next()){//曲线名称
				if(lineName!=null || !lineName.equals("")){//如果指定曲线名称,lineName为字段名称
					if(vorLineName.size()!=0){
						hasit = 0;
						for(int i=0;i<vorLineName.size();i++){
							
							if(((String)vorLineName.get(i)).equals(rs.getString(lineName))){
								hasit++;
								break;
							}
						}
						if(hasit==0){
							vorLine.add(new TimeSeries(rs.getString(lineName)));
							vorLineName.add(rs.getString(lineName));
						}
					}else{
						vorLine.add(new TimeSeries(rs.getString(lineName)));
						vorLineName.add(rs.getString(lineName));
					}
				}
			}
			rs.beforeFirst();
			while(rs.next()){//确定曲线上的顶点坐标
				if(vorLineName.size()>0){
					for(int l=0;l<vorLineName.size();l++){
						if(rs.getString(lineName).equals(((String)vorLineName.get(l)))){
							((TimeSeries)vorLine.get(l)).add(new Day(rs.getDate(xline)), rs.getDouble(yline));
						}
					}
				}else{
					((TimeSeries)vorLine.get(0)).add(new Day(rs.getDate(xline)), rs.getDouble(yline));
				}
			}
		}catch(Exception e){
			System.out.println("/********************* 记录集构造数据失败  *************************/");
			e.printStackTrace();
		}finally{
		}
		TimeSeriesCollection dataset = new TimeSeriesCollection();

		for (int i = 0; i < vorLine.size(); i++) {
			dataset.addSeries((TimeSeries) vorLine.get(i));//将曲线加到图中
		}
		return dataset;
	}
//	rslist记录集构造曲线图	
	public TimeSeriesCollection getRsDataTimeGraph(ResultSetList rs,String lineName,String xline,String yline){//xline:横轴坐标对应的字段名称,数据类型为日期；yline:纵轴坐标对应的字段名称,数据类型为long型；lineName:曲线名称对应的字段名称

		Vector vorLine = new Vector();//曲线顶点数组
		Vector vorLineName = new Vector();//曲线名称数组
		
		/*------------设定Dataset-------------*/
		try{
			int hasit = 0;
			while(rs.next()){//曲线名称
				if(lineName!=null || !lineName.equals("")){//如果指定曲线名称,lineName为字段名称
					if(vorLineName.size()!=0){
						hasit = 0;
						for(int i=0;i<vorLineName.size();i++){
							
							if(((String)vorLineName.get(i)).equals(rs.getString(lineName))){
								hasit++;
								break;
							}
						}
						if(hasit==0){
							vorLine.add(new TimeSeries(rs.getString(lineName)));
							vorLineName.add(rs.getString(lineName));
						}
					}else{
						vorLine.add(new TimeSeries(rs.getString(lineName)));
						vorLineName.add(rs.getString(lineName));
					}
				}
			}
			rs.beforefirst();
			while(rs.next()){//确定曲线上的顶点坐标
				if(vorLineName.size()>0){
					for(int l=0;l<vorLineName.size();l++){
						if(rs.getString(lineName).equals(((String)vorLineName.get(l)))){
							((TimeSeries)vorLine.get(l)).add(new Day(rs.getDate(xline)), rs.getDouble(yline));
						}
					}
				}else{
					((TimeSeries)vorLine.get(0)).add(new Day(rs.getDate(xline)), rs.getDouble(yline));
				}
			}
		}catch(Exception e){
			System.out.println("/********************* 记录集构造数据失败  *************************/");
			e.printStackTrace();
		}finally{
		}
		TimeSeriesCollection dataset = new TimeSeriesCollection();

		for (int i = 0; i < vorLine.size(); i++) {
			dataset.addSeries((TimeSeries) vorLine.get(i));//将曲线加到图中
		}
		return dataset;
	}
//	rslist记录集构造曲线图	
	public TimeSeriesCollection getRsDataTimeGraph(ResultSetList rs,String lineName,String xline,String yline, String lineDateFormatOverride,Date startTime, int count){//xline:横轴坐标对应的字段名称,数据类型为日期；yline:纵轴坐标对应的字段名称,数据类型为long型；lineName:曲线名称对应的字段名称

		Vector vorLine = new Vector();//曲线顶点数组
		Vector vorLineName = new Vector();//曲线名称数组
		
		/*------------设定Dataset-------------*/
		try{
			int hasit = 0;
			while(rs.next()){//曲线名称
				if(lineName!=null || !lineName.equals("")){//如果指定曲线名称,lineName为字段名称
					if(vorLineName.size()!=0){
						hasit = 0;
						for(int i=0;i<vorLineName.size();i++){
							
							if(((String)vorLineName.get(i)).equals(rs.getString(lineName))){
								hasit++;
								break;
							}
						}
						if(hasit==0){
							vorLine.add(new TimeSeries(rs.getString(lineName)));
							vorLineName.add(rs.getString(lineName));
						}
					}else{
						vorLine.add(new TimeSeries(rs.getString(lineName)));
						vorLineName.add(rs.getString(lineName));
					}
				}
			}
			rs.beforefirst();
			while(rs.next()){//确定曲线上的顶点坐标
				if(vorLineName.size()>0){
					for(int l=0;l<vorLineName.size();l++){
						if(rs.getString(lineName).equals(((String)vorLineName.get(l)))){
							if("MM".equals(lineDateFormatOverride)){
								for(int m = 0;m < count; m++){
									Date dd = DateUtil.AddDate(startTime, 1, DateUtil.AddType_intMonth);
									if(dd.equals(rs.getDate(xline))){
										((TimeSeries)vorLine.get(l)).add(new Day(dd), rs.getDouble(yline));
									}else{
										((TimeSeries)vorLine.get(l)).add(new Day(dd), 0.0);
									}
								}
							}else{
								((TimeSeries)vorLine.get(l)).add(new Day(rs.getDate(xline)), rs.getDouble(yline));
							}
						}
					}
				}else{
					((TimeSeries)vorLine.get(0)).add(new Day(rs.getDate(xline)), rs.getDouble(yline));
				}
			}
		}catch(Exception e){
			System.out.println("/********************* 记录集构造数据失败  *************************/");
			e.printStackTrace();
		}finally{
		}
		TimeSeriesCollection dataset = new TimeSeriesCollection();

		for (int i = 0; i < vorLine.size(); i++) {
			dataset.addSeries((TimeSeries) vorLine.get(i));//将曲线加到图中
		}
		return dataset;
	}
	
//	ArrayList构造曲线图
	public TimeSeriesCollection getArrayDataTimeGraph(ArrayList array){//非rs记录集的曲线图的数据处理
		
		Vector vorLine = new Vector();//曲线顶点数组
		Vector vorLineName = new Vector();//曲线名称数组
		
		/*------------设定Dataset-------------*/
		int hasit = 0;
		for(int l=0;l<array.size();l++){//曲线名称
			if(((ChartBean)array.get(l)).getLegendName()!=null || !((ChartBean)array.get(l)).getLegendName().equals("")){//如果指定曲线名称,lineName为字段名称
				if(vorLineName.size()!=0){
					hasit = 0;
					for(int i=0;i<vorLineName.size();i++){
						
						if(((String)vorLineName.get(i)).equals(((ChartBean)array.get(l)).getLegendName())){
							hasit++;
							break;
						}
					}
					if(hasit==0){
						vorLine.add(new TimeSeries(((ChartBean)array.get(l)).getLegendName()));
						vorLineName.add(((ChartBean)array.get(l)).getLegendName());
					}
				}else{
					vorLine.add(new TimeSeries(((ChartBean)array.get(l)).getLegendName()));
					vorLineName.add(((ChartBean)array.get(l)).getLegendName());
				}
			}
		}
		for(int j=0;j<array.size();j++){//确定曲线上的顶点坐标
			if(vorLineName.size()>0){
				for(int l=0;l<vorLineName.size();l++){
					if(((ChartBean)array.get(j)).getLegendName().equals(((String)vorLineName.get(l)))){
						((TimeSeries)vorLine.get(l)).add(new Day(((ChartBean)array.get(j)).getX_datLine()), ((ChartBean)array.get(j)).getDb_Data());
					}
				}
			}else{
				((TimeSeries)vorLine.get(0)).add(new Day(((ChartBean)array.get(j)).getX_datLine()), ((ChartBean)array.get(j)).getDb_Data());
			}
		}

		TimeSeriesCollection dataset = new TimeSeriesCollection();

		for (int i = 0; i < vorLine.size(); i++) {
			dataset.addSeries((TimeSeries) vorLine.get(i));//将曲线加到图中
		}
		return dataset;
	}
	

//	rs记录集 构造堆栈柱状图		
	public CategoryDataset getRsDataStackBar(ResultSet rs,String xLine,String LegendName,String yLine){//xLine:x轴坐标字段名；LegendName:图例字段名；yLine轴字段名称，数据类型为double\float\int

		Vector vorTmpX_LineData = new Vector();//临时存放x轴数据
		Vector vorTmpLegendNameData = new Vector();//临时存放图例数据
		
		String[] rowKeys = new String[0];//x轴数据
		String[] legendKeys = new String[0];//图例数据
		double[][] data = new double[0][];//y轴数据
		
		try {
			boolean hasfatherName = false;
			boolean hassubName = false;
			while(rs.next()){
				hasfatherName = false;
				hassubName = false;
				for(int i=0;i<vorTmpX_LineData.size();i++){
					if(((String)vorTmpX_LineData.get(i)).equals(rs.getString(xLine))){
						hasfatherName = true;
						break;
					}
				}
				if(! hasfatherName){
					vorTmpX_LineData.add(rs.getString(xLine));
				}
				for(int j=0;j<vorTmpLegendNameData.size();j++){
					if(((String)vorTmpLegendNameData.get(j)).equals(rs.getString(LegendName))){
						hassubName = true;
						break;
					}
				}
				if(! hassubName){
					vorTmpLegendNameData.add(rs.getString(LegendName));
				}
			}
			
			rowKeys = new String[vorTmpX_LineData.size()];
			legendKeys = new String[vorTmpLegendNameData.size()];
			data = new double[vorTmpX_LineData.size()][vorTmpLegendNameData.size()];
			
			for(int i=0;i<vorTmpX_LineData.size();i++){//x轴
				rowKeys[i] = (String)vorTmpX_LineData.get(i);
			}
			for(int i=0;i<vorTmpLegendNameData.size();i++){//y轴
				legendKeys[i] = (String)vorTmpLegendNameData.get(i);
			}
			
			rs.beforeFirst();
			while(rs.next()){
				for (int i=0;i<legendKeys.length;i++){
					if(legendKeys[i].equals(rs.getString(LegendName))){
						
						for (int j=0;j<rowKeys.length;j++){
							if(rowKeys[j].equals(rs.getString(xLine))){
								data[j][i]=rs.getDouble(yLine);//坐标数据
							}
						}
					}
				}
			}
			
		}catch(Exception e){
			System.out.println("/********************* 记录集构造数据失败  *************************/");
			e.printStackTrace();
		}finally{
		}
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, legendKeys, data);	
		return dataset;
	}
	
//	rsList记录集 构造堆栈柱状图	
	public CategoryDataset getRsDataStackBar(ResultSetList rs,String xLine,String LegendName,String yLine){//xLine:x轴坐标字段名；LegendName:图例字段名；yLine轴字段名称，数据类型为double\float\int

		Vector vorTmpX_LineData = new Vector();//临时存放x轴数据
		Vector vorTmpLegendNameData = new Vector();//临时存放图例数据
		
		String[] rowKeys = new String[0];//x轴数据
		String[] legendKeys = new String[0];//图例数据
		double[][] data = new double[0][];//y轴数据
		
		try {
			boolean hasfatherName = false;
			boolean hassubName = false;
			while(rs.next()){
				hasfatherName = false;
				hassubName = false;
				for(int i=0;i<vorTmpX_LineData.size();i++){
					if(((String)vorTmpX_LineData.get(i)).equals(rs.getString(xLine))){
						hasfatherName = true;
						break;
					}
				}
				if(! hasfatherName){
					vorTmpX_LineData.add(rs.getString(xLine));
				}
				for(int j=0;j<vorTmpLegendNameData.size();j++){
					if(((String)vorTmpLegendNameData.get(j)).equals(rs.getString(LegendName))){
						hassubName = true;
						break;
					}
				}
				if(! hassubName){
					vorTmpLegendNameData.add(rs.getString(LegendName));
				}
			}
			
			rowKeys = new String[vorTmpX_LineData.size()];
			legendKeys = new String[vorTmpLegendNameData.size()];
			data = new double[vorTmpX_LineData.size()][vorTmpLegendNameData.size()];
			
			for(int i=0;i<vorTmpX_LineData.size();i++){//x轴
				rowKeys[i] = (String)vorTmpX_LineData.get(i);
			}
			for(int i=0;i<vorTmpLegendNameData.size();i++){//y轴
				legendKeys[i] = (String)vorTmpLegendNameData.get(i);
			}
			
			rs.beforefirst();
			while(rs.next()){
				for (int i=0;i<legendKeys.length;i++){
					if(legendKeys[i].equals(rs.getString(LegendName))){
						
						for (int j=0;j<rowKeys.length;j++){
							if(rowKeys[j].equals(rs.getString(xLine))){
								data[j][i]=rs.getDouble(yLine);//坐标数据
							}
						}
					}
				}
			}
			
		}catch(Exception e){
			System.out.println("/********************* 记录集构造数据失败  *************************/");
			e.printStackTrace();
		}finally{
		}
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, legendKeys, data);	
		return dataset;
	}
	
//	ArrayList 构造堆栈柱状图			
	public CategoryDataset getArrayDataStackBar(ArrayList array){

		Vector vorTmpX_LineData = new Vector();//临时存放x轴数据
		Vector vorTmpLegendNameData = new Vector();//临时存放图例数据
		
		String[] rowKeys = new String[0];//x轴数据
		String[] legendKeys = new String[0];//图例数据
		double[][] data = new double[0][];//y轴数据
		
		try {
			boolean hasfatherName = false;
			boolean hassubName = false;
			for(int a=0;a<array.size();a++){
				hasfatherName = false;
				hassubName = false;
				for(int i=0;i<vorTmpX_LineData.size();i++){
					if(((String)vorTmpX_LineData.get(i)).equals(((ChartBean)array.get(a)).getX_strLine())){
						hasfatherName = true;
						break;
					}
				}
				if(! hasfatherName){
					vorTmpX_LineData.add(((ChartBean)array.get(a)).getX_strLine());
				}
				for(int j=0;j<vorTmpLegendNameData.size();j++){
					if(((String)vorTmpLegendNameData.get(j)).equals(((ChartBean)array.get(a)).getLegendName())){
						hassubName = true;
						break;
					}
				}
				if(! hassubName){
					vorTmpLegendNameData.add(((ChartBean)array.get(a)).getLegendName());
				}
			}
			
			rowKeys = new String[vorTmpX_LineData.size()];
			legendKeys = new String[vorTmpLegendNameData.size()];
			data = new double[vorTmpX_LineData.size()][vorTmpLegendNameData.size()];
			
			for(int i=0;i<vorTmpX_LineData.size();i++){//x轴
				rowKeys[i] = (String)vorTmpX_LineData.get(i);
			}
			for(int i=0;i<vorTmpLegendNameData.size();i++){//图例
				legendKeys[i] = (String)vorTmpLegendNameData.get(i);
			}
			
			for(int a=0;a<array.size();a++){
				for (int i=0;i<legendKeys.length;i++){
					if(legendKeys[i].equals(((ChartBean)array.get(a)).getLegendName())){
						
						for (int j=0;j<rowKeys.length;j++){
							if(rowKeys[j].equals(((ChartBean)array.get(a)).getX_strLine())){
								data[j][i]=((ChartBean)array.get(a)).getDb_Data();//坐标数据
							}
						}
					}
				}
			}
			
		}catch(Exception e){
			System.out.println("/********************* 数据构造失败 *************************/");
			e.printStackTrace();
		}finally{
		}
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, legendKeys, data);	
		return dataset;
	}
	
//	rs 记录集构造普通饼形图	
	public DefaultPieDataset getRsDataPie(ResultSet rs,String LegendName,String datName, boolean blnPercent){//xLine:x轴坐标字段名；LegendName:图例字段名；datName数据字段名称，blnPercent是否显示百分比数 

		/*------------设定Dataset-------------*/
		DefaultPieDataset dataset = new DefaultPieDataset();
		double sumData = 0;
		double percentData = 0;
		try {
			while(rs.next()){//以数据显示
				sumData = sumData+rs.getDouble(datName);
				dataset.setValue(rs.getString(LegendName), rs.getDouble(datName));
			}
			if(blnPercent){//以百分比数显示
				dataset.clear();
				rs.beforeFirst();
				
				while(rs.next()){
					if(sumData!=0){
						if (this.PieRoundMath==PIE_ROUND_NEW){
							//如果是round_new则用round_new方法
							percentData =CustomMaths.Round_new(rs.getDouble(datName)/sumData*100,2);// Math.floor((rs.getDouble(datName)/sumData)*10000)/100;
						}else{
							percentData =CustomMaths.round(rs.getDouble(datName)/sumData*100,2);
						}
					}else{
						percentData = 0.00;
					}
					dataset.setValue(rs.getString(LegendName), percentData);
				}
			}
			
		}catch(Exception e){
			System.out.println("/********************* 记录集构造数据失败  *************************/");
			e.printStackTrace();
		}finally{
		}
		return dataset;
	}
//	rsl 记录集构造普通饼形图	
	public DefaultPieDataset getRsDataPie(ResultSetList rs,String LegendName,String datName, boolean blnPercent){//xLine:x轴坐标字段名；LegendName:图例字段名；datName数据字段名称，blnPercent是否显示百分比数 

		/*------------设定Dataset-------------*/
		DefaultPieDataset dataset = new DefaultPieDataset();
		double sumData = 0;
		double percentData = 0;
		try {
			while(rs.next()){//以数据显示
				sumData = sumData+rs.getDouble(datName);
				dataset.setValue(rs.getString(LegendName), rs.getDouble(datName));
			}
			if(blnPercent){//以百分比数显示
				dataset.clear();
				rs.beforefirst();
				
				while(rs.next()){
					if(sumData!=0){
						if (this.PieRoundMath==PIE_ROUND_NEW){
//							如果是round_new则用round_new方法
							percentData =CustomMaths.Round_new(rs.getDouble(datName)/sumData*100,2);// Math.floor((rs.getDouble(datName)/sumData)*10000)/100;
						}else{
							percentData =CustomMaths.round(rs.getDouble(datName)/sumData*100,2);
						}
					}else{
						percentData = 0.00;
					}
					dataset.setValue(rs.getString(LegendName), percentData);
				}
			}
			
		}catch(Exception e){
			System.out.println("/********************* 记录集构造数据失败  *************************/");
			e.printStackTrace();
		}finally{
		}
		return dataset;
	}
	
//	ArrayList 构造普通饼形图
	public DefaultPieDataset getArrayDataPie(ArrayList array, boolean blnPercent){//非rs记录集的普通柱状图的数据处理

		/*------------设定Dataset-------------*/
		DefaultPieDataset dataset = new DefaultPieDataset();
		double sumData = 0;
		double percentData = 0;
		try {
			for(int i=0;i<array.size();i++){//以数据显示
				sumData = sumData+((ChartBean)array.get(i)).getDb_Data();
				dataset.setValue(((ChartBean)array.get(i)).getLegendName(), ((ChartBean)array.get(i)).getDb_Data());
			}
			if(blnPercent){//以百分比数显示
				dataset.clear();
				
				for(int i=0;i<array.size();i++){
					if(sumData!=0){
						percentData = Math.floor((((ChartBean)array.get(i)).getDb_Data()/sumData)*10000)/100;
					}else{
						percentData = 0.00;
					}
					dataset.setValue(((ChartBean)array.get(i)).getLegendName(), percentData);
				}
			}
			
		}catch(Exception e){
			System.out.println("/********************* 数据构造失败 *************************/");
			e.printStackTrace();
		}finally{
		}
		return dataset;
	}
}
