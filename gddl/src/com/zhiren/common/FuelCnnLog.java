package com.zhiren.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FuelCnnLog {
	public static int cnnCount=0;
	public static Map dbconnect = new HashMap();
	private static boolean isWindows(){
		if (System.getProperty("os.name").indexOf("Windows")>=0){
			return true;
		}else{
			return false;
		}
	} 
	
	private static String getLogPath(){
		if (isWindows()){
			return "c:"+File.separator;
		}else{
			return File.separator+"app"+File.separator+"fueltst"+File.separator+"tomcat"+File.separator+"logs"+File.separator;
		}
	}
	
	public static void writeCnnInfo(String Log,boolean blnPrintStack){
		try{
			System.out.println(Log);
			if (blnPrintStack){
				System.out.println(getStatckInfo());
			}
			FileOutputStream out=new FileOutputStream(getLogPath()+"cpifuelcnnlog.txt",true);
	        PrintStream p=new PrintStream(out);
	        p.println(DateUtil.FormatDateTime(new Date())+":"+Log);
	        p.close();
	        out.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static void createConnection(String objectString){
		dbconnect.put(objectString, getStatckInfo());
		cnnCount=cnnCount+1;
//		System.out.println("创建-连接数:"+cnnCount+" " + objectString);
//		System.out.println(getStatckInfo());
	}
	public static void closeConnection(String objectString){
		//System.out.println(objectString);
		//System.out.println(getStatckInfo());
		dbconnect.remove(objectString);
		cnnCount=cnnCount-1;
//		System.out.println("关闭-连接数:"+cnnCount+ " "+objectString);
	}
	
	public static void printLog(){
		Iterator it = dbconnect.keySet().iterator();
		String key = "";
		while(it.hasNext()){
			key = String.valueOf(it.next());
			System.out.println("打开连接数："+cnnCount+" "+key);
			System.out.println(dbconnect.get(key));
		}
	}
	
	private static String getStatckInfo(){
		Throwable ex = new Throwable();
		boolean inCnn=false;
		int iRow=0;
		StringBuffer sb=new StringBuffer("");  
		 StackTraceElement[] trace = ex.getStackTrace();
		 for (int i=0; i < trace.length; i++){  
			 if (trace[i].getClassName().equals("com.zhiren.common.JDBCcon")){
				 inCnn=true;
			 }else{
				 if (inCnn){//从DBHelp中调用切换了输出最近的2次堆栈调用
					iRow=iRow+1;
					
					 if( iRow>3 ){
						 break;
					 }
				 }
			 }
			 if (iRow>1 ){
				sb.append(trace[i]+"\n");
			 }
		 }
		 return sb.toString();  
	}
	
	
}
