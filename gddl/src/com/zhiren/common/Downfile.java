package com.zhiren.common;
import javax.servlet.http.HttpServletResponse;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

/*
 * 作者：ww
 * 时间：2010-01-21
 * 描述：处理文件附件名称中文乱码无法下载问题
 */
public class Downfile extends BasePage{
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
//		String   filename   = cycle.getRequestContext().getParameter("filename");
//		 String   filepath   = cycle.getRequestContext().getParameter("filepath");

		
		 try{
			 String   filename   =  new String(cycle.getRequestContext().getParameter("filename").getBytes("iso8859-1"), "gb2312");
			 String filepath = new String(cycle.getRequestContext().getParameter("filepath").getBytes("iso8859-1"), "gb2312");
			 String filenameutf =  java.net.URLEncoder.encode(filename,"UTF-8");
			 HttpServletResponse response=cycle.getRequestContext().getResponse();
			 java.io.File file = new java.io.File(filepath+"/"+filename);
			    //System.out.println(filepath);
			    String   CONTENT_TYPE   =   "text/html;   charset=GB2312";
			    response.setContentType(CONTENT_TYPE);
			    response.setContentType("APPLICATION/OCTET-STREAM");
			    //System.out.println(filepath+filename);
			    response.setHeader("Content-Disposition","attachment;   filename=\""   +   filenameutf   +   "\"");
			    java.io.FileInputStream   fileInputStream   =
			      new   java.io.FileInputStream(file);

			    int   i;
			    while   ((i=fileInputStream.read())   !=   -1)   {
			      response.getOutputStream().write(i);
			    }
			    fileInputStream.close();
			    response.getOutputStream().close();
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		
	}
}
