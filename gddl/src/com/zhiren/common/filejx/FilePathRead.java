package com.zhiren.common.filejx;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import com.zhiren.common.DateUtil;

public class FilePathRead {

	private ArrayList _txtFileList;
	private ArrayList _txtFileNameList;
	private String _filepath;
	private String _leix;
	
	public FilePathRead(){
		_txtFileList=new ArrayList();
		_txtFileNameList=new ArrayList();
		_filepath="";
		_leix="";
	}
	
	public ArrayList getTxtFileList(){
		
		return _txtFileList;
	}
	
	public void setTxtFileList(ArrayList value){
		
		_txtFileList=value;
	}
	
	public ArrayList getTxtFileNameList(){
		
		return _txtFileNameList;
	}
	
	public void setTxtFileNameList(ArrayList value){
		
		_txtFileNameList=value;
	}
	
	public String getFilepath(){
		
		return _filepath;
	}
	
	public void setFilepath(String value){
		
		_filepath=value;
	}
	
	public String getLeix(){
		
		return _leix;
	}
	
	public void setLeix(String value){
		
		_leix=value;
	}
	
	public FilePathRead(String leix,String FilePath){
		_txtFileList = new ArrayList();
		_txtFileNameList = new ArrayList();
		
		_filepath=FilePath;
		_leix=leix;
		//FilePathRead t = new FilePathRead();
		findCurrentDirectory(new File(_filepath));
	}

	public void find(File f){
		
		if(f.isDirectory()){
			File[] fileList = f.listFiles();
			for(int i = 0; i < fileList.length; i++){
				find(fileList[i]);
			}
		}else{
			if(matchResult(f.getName())){ // 匹配
				_txtFileNameList.add(f.getName());
				_txtFileList.add(f.getAbsolutePath());
			}
		}
	}
	
	public FilePathRead(String leix,String FilePath,boolean blfindCurrentDirectory){
		_txtFileList = new ArrayList();
		_txtFileNameList = new ArrayList();
		
		_filepath=FilePath;
		_leix=leix;
		
		if(blfindCurrentDirectory){//只查找当前目录下的文件
			findCurrentDirectory(new File(_filepath));
		
		}else{//查找当前目录下及更深层目录下的文件
			find(new File(_filepath));
		}
	}
	
	public void findCurrentDirectory(File f){
		FileJx wjjx=new FileJx();
		File[] fileList = f.listFiles();
		String filedate = "";
		for(int i = 0; i < fileList.length; i++){
			
			if(matchResult(fileList[i].getName())){ // 匹配
				filedate = wjjx.getWenjrq(fileList[i].toString());
				if(DateUtil.getYear(DateUtil.getDateTime(filedate))!=DateUtil.getYear(new Date())){
					continue;
				}
				_txtFileNameList.add(fileList[i].getName());
				_txtFileList.add(fileList[i].getAbsolutePath());
			}
		}
	}
	
	public boolean matchResult(String str){

		if(_leix.substring(0,1).equals("1") || _leix.substring(0,1).equals("3") 
				  || _leix.substring(0,1).equals("4") || _leix.substring(0,1).equals("8") || _leix.substring(0,1).equals("0") 
				    || _leix.substring(0,1).equals("6") || _leix.substring(0,1).equals("z")){
			
			if(str.indexOf(_leix)==0 && (str.indexOf("M")!=-1 || str.indexOf("m")!=-1)){
				
				return true;
			}else{
				return false;
			}

		}else if(str.indexOf(_leix)==0){
			return true;

		}else{
			
			return false;
		}
	}	
}