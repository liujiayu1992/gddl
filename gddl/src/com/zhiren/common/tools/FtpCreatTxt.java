package com.zhiren.common.tools;

//import java.io.File;
import java.io.FileWriter; 
import java.io.PrintWriter; 
import java.io.IOException; 

public class FtpCreatTxt { 
//	private File result =new File("result.txt"); 
	private FileWriter writer; 

	private PrintWriter pw; 

	public void CreatTxt(String FileName) { //�����ļ�
		try { 

			writer = new FileWriter(FileName); //�����ļ���

			pw=new PrintWriter(writer); 

		} 
		catch (IOException iox){ 
			System.err.println(iox); 
		} 

	} 

	public void aLine(String in) { //д��һ�� 
		pw.println(in); 

	} 
	public void finish() { //�ر��������������ִӻ���д���ļ� 

		try{ 
			pw.flush(); 
			writer.close(); 
		}catch (IOException iox){ 
			System.err.println(iox); 
		} 

	} 

} 