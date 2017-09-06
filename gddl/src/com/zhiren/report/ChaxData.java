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
	
	//����б��⵽�����У������ɼ�¼��λ�õ�key��λ��
	private void fillRowKey(ResultSet rsRow){
		try{
			int iOrder=0;
			int iPos=0;
			rsRow.beforeFirst();
			
		    while (rsRow.next()){
		    	//������λ�õ�key
		    	StringBuffer strKey=new StringBuffer();
	            for(int i = 0;i<mvRowNames.length;i++){
	            	strKey.append(rsRow.getString(mvRowNames[i]));
	            }
	            
	            //����λ�õ�key����ֵ����HashMap
	            mhmRowPos.put(strKey.toString(),String.valueOf(iPos) );
	             
	            //���б��⵽������
	             for (int i=0;i<mvRowNames.length;i++){
	            	 ArrRowTitle[iPos][i]= rsRow.getString(mvRowNames[i]);
	             }
	             
	             iPos=iPos+1;
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//����б��⵽�����У������ɼ�¼��λ�õ�key��λ��
	private void fillColKey(ResultSet rsCol){
		try{
			rsCol.beforeFirst();
			
			int iOrder=0;
			int iPos=0;
			int iCurrentCol=0;
			
			while (rsCol.next()){
				//������λ�õ�key
				StringBuffer strKey=new StringBuffer();
 	            for(int i = 0;i<miColTitleRows;i++){
	            	strKey.append(rsCol.getString(mvColNames[i]));
	            }
	            
	            //����λ�õ�key����ֵ����HashMap
	            mhmColPos.put(strKey.toString(), String.valueOf(iPos));
	             
				//���б��⵽������
				for (int j=0;j<miDataCount;j++){
					iCurrentCol = iPos * miDataCount + j;//miDataCount��������ĸ���
					for (int i=0;i<miColTitleRows;i++){
						ArrColTitle[i][iCurrentCol]=rsCol.getString(mvColNames[i]);
					}
					ArrColTitle[miColTitleRows][iCurrentCol] =getColDisplayName(j);
				}
				iPos=iPos+1;//��λ��
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
				ArrColTitle=new String[miColTitleRows+1][mintCols*miDataCount];//�м�����������ռһ��
			
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
		    //�������
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
				ArrColTitle=new String[1][mintCols*miDataCount];//�м�����������ռһ��
				
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

	//�ı���С�Ƶı���
	private void changeXiaojRowTitle(){
//	    int i =0;
//	    int j =0;
//	    
//	    for (j=miRowTitleCols-1;j>0;j--){
//	    	for (i=mintRows-1;i>=0;i--){
//	    		if (ArrRowTitle[i][j].equals("С��")){
//	    			if (i==0){
//	    				ArrRowTitle[i][j]="";
//	    			}else{
//	    				if (ArrRowTitle[i][j-1].equals("С��")){
//	    					ArrRowTitle[i][j]="";	
//	    				}else{
//	    					ArrRowTitle[i][j]=ArrRowTitle[i][j-1]+"С��";
//	    				}
//	    			}
//
//	    		}
//	    	}
//	    }
//	    ArrRowTitle[0][0]="�ܼ�";
	}
	
	private void changeXiaojColTitle(){
//	    int i =0;
//	    int j =0;
//	    
//	    for (i=miColTitleRows-1;i>0;i--){
//	    	for (j=mintCols*miDataCount-1;j>=0;j--){
//	    		if (ArrColTitle[i][j].equals("С��")){
//	    			if (j==0){
//	    				ArrColTitle[i][j]="";
//	    			}else{
//	    				if (ArrColTitle[i-1][j].equals("С��")){
//	    					ArrColTitle[i][j]="";	
//	    				}else{
//	    					ArrColTitle[i][j]=ArrColTitle[i-1][j]+"С��";
//	    				}
//	    			}
//
//	    		}
//	    	}
//	    }
//	    for(i=0;i< miDataCount;i++){
//	    	ArrColTitle[0][i]="�ܼ�";
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
	        if  (ArrRowTitle[iRow][i].equals( "С��")) {
	        	iCount = iCount + 1;
	        }else{
	           break;
	        }
	    }
        return iCount;
	}
	

	//����ÿ�еķ���С��
	private void computeHejOnRow(){
	    int i =0;
	    int j =0;
	    int iBodyCols=mintCols*miDataCount;
	    String v[][]=new String[miRowTitleCols][iBodyCols];
	    int iCeng=0;

	    for (i=mintRows-1;i>0;i--){
	    	if (ArrRowTitle[i][miRowTitleCols-1].equals("С��")){
	    		 iCeng = XiaojCeng(i)-1;
	    		 //��������һ��ĺϼ�
	    		 if (iCeng==miRowTitleCols-1){
	    			 for (j=0;j< iBodyCols;j++){
	    				 ArrData[i][j]= v[iCeng][j]; 
	    			 }
	    		 }else{
	    			 //�ѵ�ǰ��ĺϼƼӵ���һ��ĺϼƣ����㵱ǰ�������
	    			 for (j=0;j< iBodyCols;j++){
	    				 ArrData[i][j]= v[iCeng][j]; 
	    				 v[iCeng + 1][ j] = v[iCeng + 1][j] + v[iCeng][j];
	    				 v[iCeng][j] = "0";
	    			 }
	    		 }
	    	}else{
				 for (j=0;j<iBodyCols;j++){
					v[0][j]=v[0][j]+ArrData[i][j]; //�ۼ�ԭʼ���ݵ���һ��С��
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
