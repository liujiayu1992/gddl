package com.zhiren.gs.bjdt.monthreport;

import java.sql.ResultSet;

import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;

public class CreateDataFormat extends BasePage {
	
	public CreateDataFormat(){
		
	}
	public String[] getData(String fileline,String leix){
		String value[] = {};
		if(leix.substring(0,1).equals("6")){//diaor16bb取数
			int changd[]={6,2,6,8,10,8,6,6,6,6,8,10,10,8,8,8,8,8,8};
			 String Data[]= new String[19];
			 
			 value = getFileData("6",fileline,changd,Data);
			 
		}else if(leix.substring(0,1).equals("1")){//diaor01bb取数
			int changd[]={6,8,10,10,10,8,8,8,8,8,8,6,6,6,6,8,12,10,4,6,4,6,10,8,6,6};
			 String Data[]= new String[26];
			 
			 value = getFileData("1",fileline,changd,Data);
		
		}else if(leix.substring(0,1).equals("3")){//diaor03bb取数
			int changd[]={6,6,10,10,6,6,6,10,10,10,10,10};
			 String Data[]= new String[12];
			 
			 value = getFileData("3",fileline,changd,Data);
		
		}else if(leix.substring(0,1).equals("4")){//diaor04bb取数
			int changd[]={6,6,10,10,6,5,4,5,5,5,5,5,4,5,5,5,5,5,6,6,6,10,10};
			 String Data[]= new String[23];
			 
			 value = getFileData("4",fileline,changd,Data);
		
		}else if(leix.substring(0,1).equals("8")){//diaor08bb取数
			int changd[]={6,2,6,10,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6};
			 String Data[]= new String[23];
			 
			 value = getFileData("8",fileline,changd,Data);
		
		}else if(leix.substring(0,1).equals("0")){//diaor08bb_new取数
			int changd[]={6,2,6,10,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6};
			 String Data[]= new String[23];
			 
			 value = getFileData("0",fileline,changd,Data);	 
			 
		}else if(leix.substring(0,1).equals("z")){//zhibwcqk取数
			int changd[]={6,2,1,12,12,12,100};
			 String Data[]= new String[7];
			 
			 value = getFileData("z",fileline,changd,Data);
		
		}else if(leix.substring(0,2).equals("HC")){//haocrbb
			int changd[]={8,6,8,8,6,7,8,2,1};
			String Data[] = new String[9];
		
			value = getFileData("HC",fileline,changd,Data);
		
		}else if(leix.substring(0,2).equals("DP")){//dap
			int changd[]={8,6,2,6,5,2,7,6,6,6,6,6,9,9,5,5};
			String Data[] = new String[16];
		
			value = getFileData("DP",fileline,changd,Data);
		
		}else if(leix.substring(0,2).equals("XC")){//xiecrbb
			int changd[]={8,6,6,4,3,4,4,2};
			String Data[] = new String[8];
		
			value = getFileData("XC",fileline,changd,Data);
		}
		return value;
	}
	
	public String[] getFileData(String strleix,String file,int index[],String value[]){

		int length=0;
		if(strleix.equals("z")){
			for(int i=0;i<index.length;i++){
				 
				 if(i!=(index.length-1)){
					 value[i]=file.substring(length,length+index[i]).replaceAll(" ", "");
				 }else{
					 value[i]=file.substring(length).replaceAll(" ", "");
				 }
				 length=length+index[i];
			 }
		}else{
			for(int i=0;i<index.length;i++){
				 
				 value[i]=file.substring(length,length+index[i]).replaceAll(" ", "");
				 
//				 if(i!=(index.length-1)){
//					 value[i]=file.substring(length,length+index[i]).replaceAll(" ", "");
//				 }else{
//					 value[i]=file.substring(length).replaceAll(" ", "");
//				 }
				 length=length+index[i];
			 }
		}
		 
		 return value;
	}
	
	public String getFilepath(String strMingc){
		JDBCcon con=new JDBCcon();
		String filepath="";
		 try{
			 String pathsql = "select zhi from xitxxb where mingc='"+strMingc+"'";
			 ResultSet rspath = con.getResultSet(pathsql);
			 if(rspath.next()){
				 filepath = rspath.getString("zhi");
			 }
			 rspath.close();
		 }catch(Exception e){
			 e.printStackTrace();
		 }finally{
			 con.Close();
		 }
		return filepath;
	}
}
