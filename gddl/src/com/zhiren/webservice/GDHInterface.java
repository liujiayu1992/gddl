package com.zhiren.webservice;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import com.zhiren.common.MainGlobal;

public class GDHInterface {
	public static String Gdhdata(String diancid, String fileName, byte[] bytFile) {
		try {
			String strFile = new String(bytFile,"GB2312");
			String filepath = MainGlobal.getXitsz("系统文件夹位置", diancid, "D:/zhiren/")+diancid+"/shul/jianjwj";
			File newDir = new File(filepath);
			if(!newDir.exists()) {
				if(!newDir.mkdirs()) {
					return "目录创建失败";
				}
			}
			File newFile = new File(filepath,fileName);
		    if(newFile.exists()) {
		    	if(!newFile.delete()) {
		    		return "文件删除失败";
		    	}
		    }
		    if(!newFile.createNewFile()) {
		    	return "文件创建失败";
		    }
			FileWriter fw=new FileWriter(newFile,true);
		    BufferedWriter bw=new BufferedWriter(fw);
		    PrintWriter pw=new PrintWriter(bw);
		    pw.println(strFile);
		    pw.close();
		    bw.close();
		    fw.close();
		}catch(Exception e) {
			e.printStackTrace();
			return "错误";
		}
		return "";
	}
}

