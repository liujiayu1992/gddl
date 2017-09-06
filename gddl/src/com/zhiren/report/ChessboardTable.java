package com.zhiren.report;

import com.zhiren.common.JDBCcon;
import java.sql.ResultSet;
import java.util.HashMap;
/*
 * 作者：王磊
 * 时间：2009-06-14 11：32
 * 描述：增加数据列默认右对齐的设置
 */
public class ChessboardTable {
	private String msbRowNames="" ;
	private String msbColNames="";
	private String mvRowNames[]=null ;
	private String mvColNames[]=null;
	private String mvDataName[]=null;
	
	public Table DataTable=null;
	
	HashMap mhmColPos=new HashMap();
	HashMap mhmRowPos=new HashMap();
	
	public int miDataCount=0;
	public boolean mblnRowToCol=false;
	public boolean mblnDataOnRow=true;
	public int miColTitleRows=0;
	public int miRowTitleCols=0;
	public int miRows=0;
	public int miCols=0;
	
	private JDBCcon mcnn = new JDBCcon();
	
	public void setColNames(String strColFieldName ){
		msbColNames=strColFieldName;
	}
	
	public void setRowNames(String strRowFidldName){
		msbRowNames=strRowFidldName;
	}
	
	public void setDataNames(String strDataColField){
		mvDataName=strDataColField.split(",");
	}
	
	public void setDataOnRow(boolean blnDataOnRow){
		this.mblnDataOnRow=blnDataOnRow;
	}
	
	public void setRowToCol(boolean blnRowToCol){
		this.mblnRowToCol=blnRowToCol;
	}
		
	public void setData(String strRow,String strCol,String strData ){
		if (mblnRowToCol){
			String strTmp="";
			strTmp=msbColNames;
			msbColNames=msbRowNames;
			msbRowNames=strTmp;
			
			strTmp=strRow;
			strRow=strCol;
			strCol=strTmp;
		}
		
		mvColNames=msbColNames.split(",");
		mvRowNames=msbRowNames.split(",");
		
		miRowTitleCols=mvRowNames.length;
		miColTitleRows=mvColNames.length;
		miDataCount=mvDataName.length;
		
		ResultSet rsRow=mcnn.getResultSet(strRow);
		ResultSet rsCol=mcnn.getResultSet(strCol);
		ResultSet rsData=mcnn.getResultSet(strData);
	
		try{
			rsRow.last();
			rsCol.last();
	
			if  (mblnDataOnRow){
				if (miDataCount>1){
					miRowTitleCols=miRowTitleCols+1;
				}
				miRows=rsRow.getRow()*miDataCount;
				miCols=rsCol.getRow();
				miRows=miRows+miColTitleRows;
				miCols=miCols+miRowTitleCols;
			}else{
				if (miDataCount>1){
					miColTitleRows=miColTitleRows+1;
				}
				miRows=rsRow.getRow();
				miCols=rsCol.getRow()*miDataCount;
				miRows=miRows+miColTitleRows;
				miCols=miCols+miRowTitleCols;
			}
			
			DataTable=new Table(miRows,miCols);
			DataTable.setFixedRows(miColTitleRows);
			DataTable.setFixedCols(miRowTitleCols);
			
			fillRowKey(rsRow);
			fillColKey(rsCol);
			fillBodyData(rsData);
			
			DataTable.setFixedRowsAlign(Table.ALIGN_CENTER );
			rsRow.close();
			rsCol.close();
			rsData.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private int getRow(String strKey){
		int  iRow=0;
		String strRow="";
		try{
			strRow=mhmRowPos.get(strKey).toString();
		}catch(Exception e){
			return -1;
		}
		if (strRow==null){
			   System.out.println("RowKey:" +strKey);
		  }else{
			   iRow =Integer.parseInt(strRow);
		   }
		   return iRow;
	}
	
	private int getCol(String strKey){
		int iCol=0;
		String strCol="";
		
		try{
		 strCol=mhmColPos.get(strKey).toString();
		}catch(Exception e){
			return -1;
		}
		 if (strCol==null){
			   System.out.println("ColKey:" +strKey);
		   }else{
			   iCol =Integer.parseInt(strCol);
		   }
		   return iCol;
	}
	
	private void fillBodyData(ResultSet rs ){
		int iCol =-1;
		int iRow=-1;
		int iCurRow=0;
		int iCurCol=0;
		
		try{
		    //填充数据
		   while (rs.next()){
			   StringBuffer strRowKey =new StringBuffer();
			   StringBuffer strColKey =new StringBuffer();
			   
			   iRow=0;
			   iCol=0;
			   
			   for (int i=0;i<mvRowNames.length ;i++){
				   strRowKey.append(rs.getString(mvRowNames[i]));
			   }
			   
			   for (int i=0;i<mvColNames.length ;i++){
				   strColKey.append(rs.getString(mvColNames[i]));
			   }

			   iRow=getRow(strRowKey.toString());
			   iCol=getCol(strColKey.toString());
			   
	           if ((iRow>-1) && (iCol>-1)){
	        	   for(int i = 0;i< miDataCount;i++){
	        		   if  (mblnDataOnRow){
	        			   iCurRow=iRow+miColTitleRows+i;
	        			   iCurCol=iCol+miRowTitleCols;
	        			}else{
	        				iCurRow=iRow+miColTitleRows;
	        				iCurCol=iCol+miRowTitleCols+i;
	        			}
	        		   DataTable.setCellValue(iCurRow, iCurCol, rs.getString(mvDataName[i]));
	        		   DataTable.setCellAlign(iCurRow, iCurCol, Table.ALIGN_RIGHT);
	        		   
	        	   }
	           }
		   }
       	}catch(Exception e){
			e.printStackTrace();
		}
	}
	
//	填充行标题到数组中，并生成记录行位置的key和位置
	private void fillRowKey(ResultSet rsRow){
		try{
			int iRow=0;
			int iPos=0;
			rsRow.beforeFirst();
			iPos=1;
		    while (rsRow.next()){
		    	//计算行位置的key
		    	StringBuffer strKey=new StringBuffer();
	            for(int i = 0;i<mvRowNames.length;i++){
	            	strKey.append(rsRow.getString(mvRowNames[i]));
	            }
	            
	            //把行位置的key和行值存入HashMap
	            mhmRowPos.put(strKey.toString(),String.valueOf(iPos) );
	            
	            //存列标题到数组中
	            if (mblnDataOnRow){//数据在行标题上显示
	            	iRow=miColTitleRows+iPos;
	                for (int i=0;i<miDataCount;i++){
	                	for (int j=0;j<mvRowNames.length;j++){
	                		DataTable.setCellValue(iRow+i, j+1,rsRow.getString(mvRowNames[j]));
	                	}
	                	if  (miDataCount>1){
	                		DataTable.setCellValue(iRow+i, miRowTitleCols, mvDataName[i]);
	                	}
	                }
	                iPos=iPos+miDataCount;
	            }else{
	            	//数据在列标题上显示
	            	iRow=miColTitleRows+iPos;
	            	
	            	for (int i=0;i<mvRowNames.length;i++){
		            	 DataTable.setCellValue(iRow, i+1, rsRow.getString(mvRowNames[i]));
		            }
	            	iPos=iPos+1;
	            }
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
//	填充列标题到数组中，并生成记录列位置的key和位置
	private void fillColKey(ResultSet rsCol){
		try{
			rsCol.beforeFirst();
			
			int iPos=0;
			int iCol=0;
			
			iPos=1;
			
			while (rsCol.next()){
				//计算列位置的key
				StringBuffer strKey=new StringBuffer();
 	            for(int i = 0;i<mvColNames.length;i++){
	            	strKey.append(rsCol.getString(mvColNames[i]));
	            }
	            
// 		          把列位置的key和列值存入HashMap
	            mhmColPos.put(strKey.toString(), String.valueOf(iPos));
	                        
	            //存列标题到数组中
	            if (mblnDataOnRow){//数据在行标题上显示
	            	iCol=miRowTitleCols+iPos;
	            	for (int i=0;i<mvColNames.length;i++){
		            	 DataTable.setCellValue(i+1,iCol, rsCol.getString(mvColNames[i]));
		            }
	            	iPos=iPos+1;
	            }else{
	            	iCol=miRowTitleCols+iPos;
	                for (int i=0;i<miDataCount;i++){
	                	for (int j=0;j<mvColNames.length;j++){
	                		DataTable.setCellValue(j+1, iCol+i,rsCol.getString(mvColNames[j]));
	                	}
	                	
	                	if  (miDataCount>1){
	                		DataTable.setCellValue(miColTitleRows, iCol+i, mvDataName[i]);
	                	}
	                }
	                iPos=iPos+miDataCount;
	            }

	            	
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
