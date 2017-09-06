package com.zhiren.jt.shous;

import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;

import java.sql.ResultSet;
import java.util.ArrayList;

public class CreateData extends BasePage {
	
	public CreateData(){
		
	}
	public String [][] getFileData(ArrayList file,String leix){
		
		String value[][] = new String [][]{};
		int rows = file.size();
		if(leix.equals("HC")){//收耗存日报数据接收
			value = new String [rows][10];
			int changd[]={8,6,8,8,8,8,8,8,8,8};//文件中各个字段所占的长度
			value = getData(file,value,changd);
			
		}else if(leix.equals("1")){//调燃01表数据接收
			value = new String [rows][26];
			int changd[]={6,8,10,10,10,8,8,8,8,8,8,8,6,6,6,8,12,10,4,6,4,6,10,8,6,6};
			value = getData(file,value,changd);
		
		}else if(leix.equals("3")){//调燃03表数据接收
			value = new String [rows][12];
			int changd[]={6,6,10,10,6,6,6,10,10,10,10,10};
			value = getData(file,value,changd);
		
		}else if(leix.equals("4")){//调燃04表数据接收
			value = new String [rows][23];
			int changd[]={6,6,10,10,6,5,4,5,5,5,5,5,4,5,5,5,5,5,6,6,6,10,10};
			value = getData(file,value,changd);
		
		}else if(leix.equals("6")){//调燃06表数据接收
			value = new String [rows][19];
			int changd[]={6,2,6,8,10,8,6,6,6,6,8,10,10,8,8,8,8,8,8};
			value = getData(file,value,changd);
		
		}else if(leix.equals("8")){//调燃08表数据接收
			value = new String [rows][23];
			int changd[]={6,2,6,10,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6};
			value = getData(file,value,changd);
		}
		return value;
	}
	
	public String[][] getData(ArrayList file,String data[][],int length[]){

		for (int i=0;i<file.size();i++){//以行为单位读取文件内容
			int changd=0;
			for(int j=0;j<length.length;j++){//将读取的字段存到数组中
				 if(j!=(length.length-1)){
					 data[i][j] = (file.get(i).toString()).substring(changd,changd+length[j]).replaceAll(" ", "");
				 }else{
					 data[i][j] = (file.get(i).toString()).substring(changd).replaceAll(" ", "");
				 }
				 
				 if(j==0){//格式化日期
					 data[i][j] = FormatDate(data[i][j]);
				 }
				 changd=changd+length[j];
			 }
		}
		 return data;
	}
	
	public String FormatDate(String filedata){
		String date = filedata;
		if(filedata.length()==8){
			date = filedata.substring(0,4)+"-"+filedata.substring(4,6)+"-"+filedata.substring(6);
		}
		return date;
	}
	public String getFilepath(String str){
		JDBCcon con=new JDBCcon();
		String filepath="";
		 try{
			 String pathsql = "select zhi from xitxxb where mingc='"+str+"'";
			 ResultSet rspath = con.getResultSet(pathsql);
			 if(rspath.next()){
				 filepath = rspath.getString("zhi");
			 }
			 rspath.close();
			 con.closeRs();
		 }catch(Exception e){
			 e.printStackTrace();
		 }finally{
			 con.Close();
			 
		 }
		return filepath;
	}
	
}
