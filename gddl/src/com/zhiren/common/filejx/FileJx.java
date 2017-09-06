package com.zhiren.common.filejx;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import java.util.*;

public class FileJx{
	
    public FileJx(){
    	
    }

    public ArrayList TextJx(String fileName){
    	
    	File file=new File(fileName);
    	ArrayList buffer=new ArrayList();
	    try{
	    	 
		     BufferedReader input=new BufferedReader(new FileReader(file));
		     String text;
		     while((text=input.readLine())!=null)
		     buffer.add(text);
		     input.close();
//		     file.delete();
	     }catch(Exception ex){
	    	 ex.printStackTrace();
	     }
	     return buffer; 
    }
    
    public String getWenjrq(String fileName){
    	
    	File fileriq = new File(fileName);
    	String riq = getDateString(fileriq.lastModified());
    	return riq;
    }
    
    public static String getDateString(long mill){
 	   
    	if(mill < 0) return  "";
	    
	    Date date = new Date(mill);
	    Calendar rightNow = Calendar.getInstance();
	    rightNow.setTime(date);
	    int year = rightNow.get(Calendar.YEAR);
	    int month = rightNow.get(Calendar.MONTH)+1;
	    int day = rightNow.get(Calendar.DAY_OF_MONTH);
	    int hour = rightNow.get(Calendar.HOUR_OF_DAY);
	    int min = rightNow.get(Calendar.MINUTE);
	    int second = rightNow.get(Calendar.SECOND);

	    return year + "-" + (month <10 ? "0" + month : "" + month) + "-" 
	           +  (day <10 ? "0" + day : "" + day)
	           +  (hour <10 ? " 0" + hour : " " + hour)+":"
	           + (min <10 ? "0" + min : "" + min)
	           +":"+ (second <10 ? "0" + second : "" + second);
	}
    
}
