package com.zhiren.common.error;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;

public class ErrorPage  extends BasePage{
	String msg="";
	String value="";
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle){
		int intErrCode = ((Visit)getPage().getVisit()).getErrcode();
		if(cycle.getRequestContext().getRequest().getParameter("msg")!=null){
			intErrCode = Integer.parseInt(cycle.getRequestContext().getRequest().getParameter("msg"));
		}
		msg = MainGlobal.getErrMsg(intErrCode);
	} 
	public String getMsg(){
		 return msg;
	}
	public void setMsg(String value){
		msg = value;
	}
}
