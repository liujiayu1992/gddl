package com.zhiren.common;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.zhiren.common.SysConstant;

public class WriteLog {	
	public static void writeErrorLog(String err){
//		
		String filepath = MainGlobal.getXitsz("系统文件夹位置", "0", "C:/zhiren/")+"/logs";
		File errLogPath = new File(filepath);
		File errLog = new File(errLogPath , SysConstant.WS_errLogFileName);
		try{
			if(!errLogPath.exists()){
				errLogPath.mkdirs();
			}
			if(errLog.exists()){
				if((new Date().getTime() - errLog.lastModified())/86400000 > 60) {
					errLog.delete();
					errLog.createNewFile();
				}
			}else {
				errLog.createNewFile();
			}
			FileWriter fwn = new FileWriter(errLog,true);
			PrintWriter pwn = new PrintWriter(fwn);
			SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒:");
			pwn.println(f.format(new Date())+"  "+err);
			pwn.close();
			fwn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void writeInfoLog(String log){
		String filepath = MainGlobal.getXitsz("系统文件夹位置", "0", "C:/zhiren/")+"/logs";
		File outLogPath = new File(filepath);
		File outLog = new File(outLogPath , SysConstant.WS_infoLogFileName);
		try{
			if(!outLogPath.exists()){
				outLogPath.mkdirs();
			}
			if(!outLog.exists()){
				outLog.createNewFile();
			}else {
				if((new Date().getTime() - outLog.lastModified())/86400000 >15);
				outLog.delete();
				outLog.createNewFile();
			}
//			System.out.println(Math.abs(outLog.lastModified() - new Date().getTime()));
			FileWriter fwn = new FileWriter(outLog,true);
			PrintWriter pwn = new PrintWriter(fwn);
			SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒:");
			pwn.println(f.format(new Date())+"  "+log);
			pwn.close();
			fwn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
