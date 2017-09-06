package com.zhiren.report;

import java.sql.*;
import java.util.HashMap;

import com.zhiren.common.JDBCcon;

public class ChaxData {
	private String msbRowNames="" ;
	private String msbColNames="";
	private String mstrTitle="";
	private String mvRowNames[] =new String[1];
	private String mvColNames[]=new String[1];
	private String mvDataName[]=null;
	private String mvDataDisplayName[]=null;
	private String mvRowDisplayName[]=null;
	private int mvDataWidth[]=null;
	private int mvRowNamsWidth[]=null;
	private int mintSum=1;
	private boolean mblnIsTrue=false;
	private JDBCcon mcnn = new JDBCcon();
	
	HashMap mhmColPos=new HashMap();
	HashMap mhmRowPos=new HashMap();
	
	public String ArrColTitle[][]=null;
	public String ArrData[][]=null;
	public String ArrRowTitle[][]=null;
	
	public int miDataCount=0;
	public int miColTitleRows=0;
	public int miRowTitleCols=0;
	public int mintRows =0;
	public int mintCols=0;
	
	public void setColField(String strColFieldName ){
		msbColNames=strColFieldName;
	}
	
	public void setRowField(String strRowFidldName){
		msbRowNames=strRowFidldName;
	}
	
	public void setDataField(String strDataColField){
		mvDataName=strDataColField.split(",");
		miDataCount=mvDataName.length;
	}
	
	public void IniData(String lngid){
		miRowTitleCols=mvRowNames.length;
		miColTitleRows=mvColNames.length;
		
		for (int i=0;i<miColTitleRows;i++){
			mvColNames[i]=mvColNames[i].trim();
		}
		
		miDataCount=mvDataName.length;
		mvDataDisplayName=new String[miDataCount];
		mvRowDisplayName=new String[miRowTitleCols];
	}
	
	//填充行标题到数组中，并生成记录行位置的key和位置
	private void fillRowKey(ResultSet rsRow){
		try{
			int iOrder=0;
			int iPos=0;
			rsRow.beforeFirst();
			
		    while (rsRow.next()){
		    	//计算行位置的key
		    	StringBuffer strKey=new StringBuffer();
	            for(int i = 0;i<mvRowNames.length;i++){
	            	strKey.append(rsRow.getString(mvRowNames[i]));
	            }
	            
	            //把行位置的key和行值存入HashMap
	            mhmRowPos.put(strKey.toString(),String.valueOf(iPos) );
	             
	            //存列标题到数组中
	             for (int i=0;i<mvRowNames.length;i++){
	            	 ArrRowTitle[iPos][i]= rsRow.getString(mvRowNames[i]);
	             }
	             
	             iPos=iPos+1;
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//填充列标题到数组中，并生成记录列位置的key和位置
	private void fillColKey(ResultSet rsCol){
		try{
			rsCol.beforeFirst();
			
			int iOrder=0;
			int iPos=0;
			int iCurrentCol=0;
			
			while (rsCol.next()){
				//计算列位置的key
				StringBuffer strKey=new StringBuffer();
 	            for(int i = 0;i<miColTitleRows;i++){
	            	strKey.append(rsCol.getString(mvColNames[i]));
	            }
	            
	            //把列位置的key和列值存入HashMap
	            mhmColPos.put(strKey.toString(), String.valueOf(iPos));
	             
				//存列标题到数组中
				for (int j=0;j<miDataCount;j++){
					iCurrentCol = iPos * miDataCount + j;//miDataCount是数据项的个数
					for (int i=0;i<miColTitleRows;i++){
						ArrColTitle[i][iCurrentCol]=rsCol.getString(mvColNames[i]);
					}
					ArrColTitle[miColTitleRows][iCurrentCol] =getColDisplayName(j);
				}
				iPos=iPos+1;//列位置
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setData(String strRow,String strCol,String strRowData ,String strColData){
		mvRowNames[0]=msbRowNames;
		miRowTitleCols=mvRowNames.length;
		
		mvColNames[0]=msbColNames;
		miColTitleRows=mvColNames.length;
		
		ResultSet rsRow=mcnn.getResultSet(strRow);
		ResultSet rsCol=mcnn.getResultSet(strCol);
		ResultSet rsRowData=mcnn.getResultSet(strRowData);
		ResultSet rsColData=mcnn.getResultSet(strColData);
		
			try{
				rsRow.last();
				mintRows=rsRow.getRow();
				
				rsCol.last();
				mintCols=rsCol.getRow();//+miRowTitleCols;
				
				ArrData=new String[mintRows][mintCols*miDataCount];
				ArrRowTitle=new String[mintRows][miRowTitleCols];
				ArrColTitle=new String[miColTitleRows+1][mintCols*miDataCount];//有计算数据数据占一行
			
				fillRowKey(rsRow);
				fillColKey(rsCol);
				fillBodyData(rsRowData);
				fillBodyData(rsColData);
				
				if (miRowTitleCols>1) {
		//			computeHejOnRow();
				}
				
				rsRow.close();
				rsCol.close();
				rsRowData.close();
				rsColData.close();

			}
			catch(Exception e){
				e.printStackTrace();
			}
	}
	private void fillBodyData(ResultSet rs ){
		int iCol =-1;
		int iRow=-1;
		String strRow="";
		String strCol="";
		
		try{
		    //填充数据
		   while (rs.next()){
			   StringBuffer strRowKey =new StringBuffer();
			   StringBuffer strColKey =new StringBuffer();
			   
			   for (int i=0;i<mvRowNames.length ;i++){
				   strRowKey.append(rs.getString(mvRowNames[i]));
			   }
			   
			   for (int i=0;i<mvColNames.length ;i++){
				   strColKey.append(rs.getString(mvColNames[i]));
			   }
			   
			   strCol=mhmColPos.get(strColKey.toString()).toString();
			   strRow=mhmRowPos.get(strRowKey.toString()).toString();
			   
			   if (strCol==null){
				   iCol=-1;
				   System.out.println("RowKey:" +strRowKey.toString());
			   }else{
				   iCol =Integer.parseInt(strCol);
			   }
			   
			   if (strRow==null){
				   iRow=-1;
				   System.out.println("colKey:" +strColKey.toString());
			   }else{
				   iRow= Integer.parseInt(strRow);
			   }
			   
	           if ((iRow>-1) && (iCol>-1)){
	        	   for(int i = 0;i< miDataCount;i++){
	        		   ArrData[iRow][iCol * miDataCount + i] = rs.getString(mvDataName[i]);
	        	   }
	           }
		   }
       	}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private String getColDisplayName(int iIndex){
//		if (mvDataDisplayName.length>=iIndex){
//			if (mvDataDisplayName[iIndex]==null){
//				return "";
//			}else{
//				return mvDataDisplayName[iIndex];
//			}
//		}else{
			return "";
//		}
	}
		
	private void fillRowData(ResultSet rsRowData){
		if (mblnIsTrue){
			try{
				rsRowData.last();
				mintRows=rsRowData.getRow();
				if (mintRows==0){
					mintRows=1;
				}
				mintCols=1;
				
				ArrData=new String[mintRows][mintCols*miDataCount];
				ArrRowTitle=new String[mintRows][miRowTitleCols];
				ArrColTitle=new String[1][mintCols*miDataCount];//有计算数据数据占一行
				
				int iPos =0;
				rsRowData.beforeFirst();
				while (rsRowData.next()){
					for (int i=0;i<miRowTitleCols;i++){
						ArrRowTitle[iPos][i]=rsRowData.getString(mvRowNames[i]);
					}
					for (int i=0;i<miDataCount;i++){
						ArrData[iPos][i]=rsRowData.getString(mvDataName[i]);
						
					}
					iPos=iPos+1;
				}
				for (int i=0;i<miDataCount;i++){
					ArrColTitle[0][i]=getColDisplayName(i);
				}
				if (mintSum==1){
					changeXiaojRowTitle();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	//改变是小计的标题
	private void changeXiaojRowTitle(){
//	    int i =0;
//	    int j =0;
//	    
//	    for (j=miRowTitleCols-1;j>0;j--){
//	    	for (i=mintRows-1;i>=0;i--){
//	    		if (ArrRowTitle[i][j].equals("小计")){
//	    			if (i==0){
//	    				ArrRowTitle[i][j]="";
//	    			}else{
//	    				if (ArrRowTitle[i][j-1].equals("小计")){
//	    					ArrRowTitle[i][j]="";	
//	    				}else{
//	    					ArrRowTitle[i][j]=ArrRowTitle[i][j-1]+"小计";
//	    				}
//	    			}
//
//	    		}
//	    	}
//	    }
//	    ArrRowTitle[0][0]="总计";
	}
	
	private void changeXiaojColTitle(){
//	    int i =0;
//	    int j =0;
//	    
//	    for (i=miColTitleRows-1;i>0;i--){
//	    	for (j=mintCols*miDataCount-1;j>=0;j--){
//	    		if (ArrColTitle[i][j].equals("小计")){
//	    			if (j==0){
//	    				ArrColTitle[i][j]="";
//	    			}else{
//	    				if (ArrColTitle[i-1][j].equals("小计")){
//	    					ArrColTitle[i][j]="";	
//	    				}else{
//	    					ArrColTitle[i][j]=ArrColTitle[i-1][j]+"小计";
//	    				}
//	    			}
//
//	    		}
//	    	}
//	    }
//	    for(i=0;i< miDataCount;i++){
//	    	ArrColTitle[0][i]="总计";
//	    }
	}
	
	private String getSapce(int iCount){
		StringBuffer sb=new StringBuffer();
		for (int i=0;i<iCount;i++){
			sb.append(".");
		}
		return sb.toString();
	}
	
	private int XiaojCeng(int iRow) {
	    int i =0;
	    int iCount =0;
	    
	    for( i = miRowTitleCols - 1;i>0;i--){
	        if  (ArrRowTitle[iRow][i].equals( "小计")) {
	        	iCount = iCount + 1;
	        }else{
	           break;
	        }
	    }
        return iCount;
	}
	

	//计算每行的分组小计
	private void computeHejOnRow(){
	    int i =0;
	    int j =0;
	    int iBodyCols=mintCols*miDataCount;
	    String v[][]=new String[miRowTitleCols][iBodyCols];
	    int iCeng=0;

	    for (i=mintRows-1;i>0;i--){
	    	if (ArrRowTitle[i][miRowTitleCols-1].equals("小计")){
	    		 iCeng = XiaojCeng(i)-1;
	    		 //如果是最后一层的合计
	    		 if (iCeng==miRowTitleCols-1){
	    			 for (j=0;j< iBodyCols;j++){
	    				 ArrData[i][j]= v[iCeng][j]; 
	    			 }
	    		 }else{
	    			 //把当前层的合计加到下一层的合计，清零当前层的数据
	    			 for (j=0;j< iBodyCols;j++){
	    				 ArrData[i][j]= v[iCeng][j]; 
	    				 v[iCeng + 1][ j] = v[iCeng + 1][j] + v[iCeng][j];
	    				 v[iCeng][j] = "0";
	    			 }
	    		 }
	    	}else{
				 for (j=0;j<iBodyCols;j++){
					v[0][j]=v[0][j]+ArrData[i][j]; //累加原始数据到第一层小计
				 }
	    	}
	    }
	}
	
	public String getRowTilteCell(int i ,int j){
		if (ArrRowTitle[i][j]==null){
			return "";
		}
		return ArrRowTitle[i][j];
	}
	
	public String getColTitleCell(int i,int j){
		if (ArrColTitle[i][j]==null){
			return "";
		}
		return ArrColTitle[i][j];
	}
	
	public String getData(int i,int j){
		return ArrData[i][j];
	}
	
	public String getQueryTitle(){
		return mstrTitle;
	}
	
	public int getDataWidth(int i){
		return mvDataWidth[i];
	}
	
	public int getRowNamsWidth(int i){
		return  mvRowNamsWidth[i];
	}
	
	public String getRowDisplay(int i){
		return mvRowDisplayName[i];
	}
	
}
