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
		if(leix.equals("HC")){//�պĴ��ձ����ݽ���
			value = new String [rows][10];
			int changd[]={8,6,8,8,8,8,8,8,8,8};//�ļ��и����ֶ���ռ�ĳ���
			value = getData(file,value,changd);
			
		}else if(leix.equals("1")){//��ȼ01�����ݽ���
			value = new String [rows][26];
			int changd[]={6,8,10,10,10,8,8,8,8,8,8,8,6,6,6,8,12,10,4,6,4,6,10,8,6,6};
			value = getData(file,value,changd);
		
		}else if(leix.equals("3")){//��ȼ03�����ݽ���
			value = new String [rows][12];
			int changd[]={6,6,10,10,6,6,6,10,10,10,10,10};
			value = getData(file,value,changd);
		
		}else if(leix.equals("4")){//��ȼ04�����ݽ���
			value = new String [rows][23];
			int changd[]={6,6,10,10,6,5,4,5,5,5,5,5,4,5,5,5,5,5,6,6,6,10,10};
			value = getData(file,value,changd);
		
		}else if(leix.equals("6")){//��ȼ06�����ݽ���
			value = new String [rows][19];
			int changd[]={6,2,6,8,10,8,6,6,6,6,8,10,10,8,8,8,8,8,8};
			value = getData(file,value,changd);
		
		}else if(leix.equals("8")){//��ȼ08�����ݽ���
			value = new String [rows][23];
			int changd[]={6,2,6,10,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6};
			value = getData(file,value,changd);
		}
		return value;
	}
	
	public String[][] getData(ArrayList file,String data[][],int length[]){

		for (int i=0;i<file.size();i++){//����Ϊ��λ��ȡ�ļ�����
			int changd=0;
			for(int j=0;j<length.length;j++){//����ȡ���ֶδ浽������
				 if(j!=(length.length-1)){
					 data[i][j] = (file.get(i).toString()).substring(changd,changd+length[j]).replaceAll(" ", "");
				 }else{
					 data[i][j] = (file.get(i).toString()).substring(changd).replaceAll(" ", "");
				 }
				 
				 if(j==0){//��ʽ������
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
