package com.zhiren.common.tools;

//import java.io.File;
import java.io.FileWriter; 
import java.io.PrintWriter; 
import java.io.IOException; 

public class FtpCreatTxt { 
//	private File result =new File("result.txt"); 
	private FileWriter writer; 

	private PrintWriter pw; 

	public void CreatTxt(String FileName) { //生成文件
		try { 

			writer = new FileWriter(FileName); //生成文件名

			pw=new PrintWriter(writer); 

		} 
		catch (IOException iox){ 
			System.err.println(iox); 
		} 

	} 

	public void aLine(String in) { //写入一行 
		pw.println(in); 

	} 
	public void finish() { //关闭输入流，将文字从缓存写入文件 

		try{ 
			pw.flush(); 
			writer.close(); 
		}catch (IOException iox){ 
			System.err.println(iox); 
		} 

	} 

} 