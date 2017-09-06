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
	
//	�������ͼ��Ҫ����
	public ChartData(){
	}

//	rs ��¼��������ͨ��״ͼ	
	public CategoryDataset getRsDataChart(ResultSet rs,String xLine,String LegendName,String yLine){//xLine:x�������ֶ�����LegendName:ͼ���ֶ�����yLine���ֶ����ƣ���������Ϊdouble\float\int

		/*------------�趨Dataset-------------*/
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		try {
			while(rs.next()){
				dataset.addValue(rs.getDouble(yLine), rs.getString(LegendName), rs.getString(xLine));
			}
			
		}catch(Exception e){
			System.out.println("/********************* ��¼����������ʧ��  *************************/");
			e.printStackTrace();
		}finally{
		}
		return dataset;
	}
//	rslist ��¼��������ͨ��״ͼ	
	public CategoryDataset getRsDataChart(ResultSetList rs,String xLine,String LegendName,String yLine){//xLine:x�������ֶ�����LegendName:ͼ���ֶ�����yLine���ֶ����ƣ���������Ϊdouble\float\int

		/*------------�趨Dataset-------------*/
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		try {
			rs.beforefirst();
			while(rs.next()){
				dataset.addValue(rs.getDouble(yLine), rs.getString(LegendName), rs.getString(xLine));
			}
			rs.close();
			
		}catch(Exception e){
			System.out.println("/********************* ��¼����������ʧ��  *************************/");
			e.printStackTrace();
		}finally{
		}
		return dataset;
	}
	
//	ArrayList ������ͨ��״ͼ	
	public CategoryDataset getArrayDataChart(ArrayList array){//��rs��¼������ͨ��״ͼ�����ݴ���

		/*------------�趨Dataset-------------*/
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		try {
			for(int i=0;i<array.size();i++){
				dataset.addValue(((ChartBean)array.get(i)).getDb_Data(), ((ChartBean)array.get(i)).getLegendName(), ((ChartBean)array.get(i)).getX_strLine());
			}
			
		}catch(Exception e){
			System.out.println("/********************* ���ݹ���ʧ�� *************************/");
			e.printStackTrace();
		}finally{
		}
		return dataset;
	}
	
//	rs��¼����������ͼ	
	public TimeSeriesCollection getRsDataTimeGraph(ResultSet rs,String lineName,String xline,String yline){//xline:���������Ӧ���ֶ�����,��������Ϊ���ڣ�yline:���������Ӧ���ֶ�����,��������Ϊlong�ͣ�lineName:�������ƶ�Ӧ���ֶ�����

		Vector vorLine = new Vector();//���߶�������
		Vector vorLineName = new Vector();//������������
		
		/*------------�趨Dataset-------------*/
		try{
			int hasit = 0;
			while(rs.next()){//��������
				if(lineName!=null || !lineName.equals("")){//���ָ����������,lineNameΪ�ֶ�����
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
			while(rs.next()){//ȷ�������ϵĶ�������
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
			System.out.println("/********************* ��¼����������ʧ��  *************************/");
			e.printStackTrace();
		}finally{
		}
		TimeSeriesCollection dataset = new TimeSeriesCollection();

		for (int i = 0; i < vorLine.size(); i++) {
			dataset.addSeries((TimeSeries) vorLine.get(i));//�����߼ӵ�ͼ��
		}
		return dataset;
	}
//	rslist��¼����������ͼ	
	public TimeSeriesCollection getRsDataTimeGraph(ResultSetList rs,String lineName,String xline,String yline){//xline:���������Ӧ���ֶ�����,��������Ϊ���ڣ�yline:���������Ӧ���ֶ�����,��������Ϊlong�ͣ�lineName:�������ƶ�Ӧ���ֶ�����

		Vector vorLine = new Vector();//���߶�������
		Vector vorLineName = new Vector();//������������
		
		/*------------�趨Dataset-------------*/
		try{
			int hasit = 0;
			while(rs.next()){//��������
				if(lineName!=null || !lineName.equals("")){//���ָ����������,lineNameΪ�ֶ�����
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
			while(rs.next()){//ȷ�������ϵĶ�������
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
			System.out.println("/********************* ��¼����������ʧ��  *************************/");
			e.printStackTrace();
		}finally{
		}
		TimeSeriesCollection dataset = new TimeSeriesCollection();

		for (int i = 0; i < vorLine.size(); i++) {
			dataset.addSeries((TimeSeries) vorLine.get(i));//�����߼ӵ�ͼ��
		}
		return dataset;
	}
//	rslist��¼����������ͼ	
	public TimeSeriesCollection getRsDataTimeGraph(ResultSetList rs,String lineName,String xline,String yline, String lineDateFormatOverride,Date startTime, int count){//xline:���������Ӧ���ֶ�����,��������Ϊ���ڣ�yline:���������Ӧ���ֶ�����,��������Ϊlong�ͣ�lineName:�������ƶ�Ӧ���ֶ�����

		Vector vorLine = new Vector();//���߶�������
		Vector vorLineName = new Vector();//������������
		
		/*------------�趨Dataset-------------*/
		try{
			int hasit = 0;
			while(rs.next()){//��������
				if(lineName!=null || !lineName.equals("")){//���ָ����������,lineNameΪ�ֶ�����
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
			while(rs.next()){//ȷ�������ϵĶ�������
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
			System.out.println("/********************* ��¼����������ʧ��  *************************/");
			e.printStackTrace();
		}finally{
		}
		TimeSeriesCollection dataset = new TimeSeriesCollection();

		for (int i = 0; i < vorLine.size(); i++) {
			dataset.addSeries((TimeSeries) vorLine.get(i));//�����߼ӵ�ͼ��
		}
		return dataset;
	}
	
//	ArrayList��������ͼ
	public TimeSeriesCollection getArrayDataTimeGraph(ArrayList array){//��rs��¼��������ͼ�����ݴ���
		
		Vector vorLine = new Vector();//���߶�������
		Vector vorLineName = new Vector();//������������
		
		/*------------�趨Dataset-------------*/
		int hasit = 0;
		for(int l=0;l<array.size();l++){//��������
			if(((ChartBean)array.get(l)).getLegendName()!=null || !((ChartBean)array.get(l)).getLegendName().equals("")){//���ָ����������,lineNameΪ�ֶ�����
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
		for(int j=0;j<array.size();j++){//ȷ�������ϵĶ�������
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
			dataset.addSeries((TimeSeries) vorLine.get(i));//�����߼ӵ�ͼ��
		}
		return dataset;
	}
	

//	rs��¼�� �����ջ��״ͼ		
	public CategoryDataset getRsDataStackBar(ResultSet rs,String xLine,String LegendName,String yLine){//xLine:x�������ֶ�����LegendName:ͼ���ֶ�����yLine���ֶ����ƣ���������Ϊdouble\float\int

		Vector vorTmpX_LineData = new Vector();//��ʱ���x������
		Vector vorTmpLegendNameData = new Vector();//��ʱ���ͼ������
		
		String[] rowKeys = new String[0];//x������
		String[] legendKeys = new String[0];//ͼ������
		double[][] data = new double[0][];//y������
		
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
			
			for(int i=0;i<vorTmpX_LineData.size();i++){//x��
				rowKeys[i] = (String)vorTmpX_LineData.get(i);
			}
			for(int i=0;i<vorTmpLegendNameData.size();i++){//y��
				legendKeys[i] = (String)vorTmpLegendNameData.get(i);
			}
			
			rs.beforeFirst();
			while(rs.next()){
				for (int i=0;i<legendKeys.length;i++){
					if(legendKeys[i].equals(rs.getString(LegendName))){
						
						for (int j=0;j<rowKeys.length;j++){
							if(rowKeys[j].equals(rs.getString(xLine))){
								data[j][i]=rs.getDouble(yLine);//��������
							}
						}
					}
				}
			}
			
		}catch(Exception e){
			System.out.println("/********************* ��¼����������ʧ��  *************************/");
			e.printStackTrace();
		}finally{
		}
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, legendKeys, data);	
		return dataset;
	}
	
//	rsList��¼�� �����ջ��״ͼ	
	public CategoryDataset getRsDataStackBar(ResultSetList rs,String xLine,String LegendName,String yLine){//xLine:x�������ֶ�����LegendName:ͼ���ֶ�����yLine���ֶ����ƣ���������Ϊdouble\float\int

		Vector vorTmpX_LineData = new Vector();//��ʱ���x������
		Vector vorTmpLegendNameData = new Vector();//��ʱ���ͼ������
		
		String[] rowKeys = new String[0];//x������
		String[] legendKeys = new String[0];//ͼ������
		double[][] data = new double[0][];//y������
		
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
			
			for(int i=0;i<vorTmpX_LineData.size();i++){//x��
				rowKeys[i] = (String)vorTmpX_LineData.get(i);
			}
			for(int i=0;i<vorTmpLegendNameData.size();i++){//y��
				legendKeys[i] = (String)vorTmpLegendNameData.get(i);
			}
			
			rs.beforefirst();
			while(rs.next()){
				for (int i=0;i<legendKeys.length;i++){
					if(legendKeys[i].equals(rs.getString(LegendName))){
						
						for (int j=0;j<rowKeys.length;j++){
							if(rowKeys[j].equals(rs.getString(xLine))){
								data[j][i]=rs.getDouble(yLine);//��������
							}
						}
					}
				}
			}
			
		}catch(Exception e){
			System.out.println("/********************* ��¼����������ʧ��  *************************/");
			e.printStackTrace();
		}finally{
		}
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, legendKeys, data);	
		return dataset;
	}
	
//	ArrayList �����ջ��״ͼ			
	public CategoryDataset getArrayDataStackBar(ArrayList array){

		Vector vorTmpX_LineData = new Vector();//��ʱ���x������
		Vector vorTmpLegendNameData = new Vector();//��ʱ���ͼ������
		
		String[] rowKeys = new String[0];//x������
		String[] legendKeys = new String[0];//ͼ������
		double[][] data = new double[0][];//y������
		
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
			
			for(int i=0;i<vorTmpX_LineData.size();i++){//x��
				rowKeys[i] = (String)vorTmpX_LineData.get(i);
			}
			for(int i=0;i<vorTmpLegendNameData.size();i++){//ͼ��
				legendKeys[i] = (String)vorTmpLegendNameData.get(i);
			}
			
			for(int a=0;a<array.size();a++){
				for (int i=0;i<legendKeys.length;i++){
					if(legendKeys[i].equals(((ChartBean)array.get(a)).getLegendName())){
						
						for (int j=0;j<rowKeys.length;j++){
							if(rowKeys[j].equals(((ChartBean)array.get(a)).getX_strLine())){
								data[j][i]=((ChartBean)array.get(a)).getDb_Data();//��������
							}
						}
					}
				}
			}
			
		}catch(Exception e){
			System.out.println("/********************* ���ݹ���ʧ�� *************************/");
			e.printStackTrace();
		}finally{
		}
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, legendKeys, data);	
		return dataset;
	}
	
//	rs ��¼��������ͨ����ͼ	
	public DefaultPieDataset getRsDataPie(ResultSet rs,String LegendName,String datName, boolean blnPercent){//xLine:x�������ֶ�����LegendName:ͼ���ֶ�����datName�����ֶ����ƣ�blnPercent�Ƿ���ʾ�ٷֱ��� 

		/*------------�趨Dataset-------------*/
		DefaultPieDataset dataset = new DefaultPieDataset();
		double sumData = 0;
		double percentData = 0;
		try {
			while(rs.next()){//��������ʾ
				sumData = sumData+rs.getDouble(datName);
				dataset.setValue(rs.getString(LegendName), rs.getDouble(datName));
			}
			if(blnPercent){//�԰ٷֱ�����ʾ
				dataset.clear();
				rs.beforeFirst();
				
				while(rs.next()){
					if(sumData!=0){
						if (this.PieRoundMath==PIE_ROUND_NEW){
							//�����round_new����round_new����
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
			System.out.println("/********************* ��¼����������ʧ��  *************************/");
			e.printStackTrace();
		}finally{
		}
		return dataset;
	}
//	rsl ��¼��������ͨ����ͼ	
	public DefaultPieDataset getRsDataPie(ResultSetList rs,String LegendName,String datName, boolean blnPercent){//xLine:x�������ֶ�����LegendName:ͼ���ֶ�����datName�����ֶ����ƣ�blnPercent�Ƿ���ʾ�ٷֱ��� 

		/*------------�趨Dataset-------------*/
		DefaultPieDataset dataset = new DefaultPieDataset();
		double sumData = 0;
		double percentData = 0;
		try {
			while(rs.next()){//��������ʾ
				sumData = sumData+rs.getDouble(datName);
				dataset.setValue(rs.getString(LegendName), rs.getDouble(datName));
			}
			if(blnPercent){//�԰ٷֱ�����ʾ
				dataset.clear();
				rs.beforefirst();
				
				while(rs.next()){
					if(sumData!=0){
						if (this.PieRoundMath==PIE_ROUND_NEW){
//							�����round_new����round_new����
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
			System.out.println("/********************* ��¼����������ʧ��  *************************/");
			e.printStackTrace();
		}finally{
		}
		return dataset;
	}
	
//	ArrayList ������ͨ����ͼ
	public DefaultPieDataset getArrayDataPie(ArrayList array, boolean blnPercent){//��rs��¼������ͨ��״ͼ�����ݴ���

		/*------------�趨Dataset-------------*/
		DefaultPieDataset dataset = new DefaultPieDataset();
		double sumData = 0;
		double percentData = 0;
		try {
			for(int i=0;i<array.size();i++){//��������ʾ
				sumData = sumData+((ChartBean)array.get(i)).getDb_Data();
				dataset.setValue(((ChartBean)array.get(i)).getLegendName(), ((ChartBean)array.get(i)).getDb_Data());
			}
			if(blnPercent){//�԰ٷֱ�����ʾ
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
			System.out.println("/********************* ���ݹ���ʧ�� *************************/");
			e.printStackTrace();
		}finally{
		}
		return dataset;
	}
}
