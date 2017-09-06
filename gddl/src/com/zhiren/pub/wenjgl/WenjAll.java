//chh 2008-03-17 修改查询语句与表格格式
package com.zhiren.pub.wenjgl;


import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.main.validate.Login;

public class WenjAll extends BasePage implements PageValidateListener{
	
	String id;
	

	
	public String getDXMoney(double _Money ){
		Money money=new Money();
		return money.NumToRMBStr1(_Money);
	}
	
    private String wenjnr;
	public String getWenjnr() {
		if(wenjnr==null||wenjnr.equals("")){
			wenjnr="var wenjnr='';";
		}
		return wenjnr;
	}
	public void setWenjnr(String wenjnr) {
		this.wenjnr = wenjnr;
	}
	
	
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团
	}
	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
	}
	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
	}
	
	public boolean getRaw() {
		return true;
	}
	
	public String getWenj(){
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit)this.getPage().getVisit();
		
		String sql =
			"select rownum,s.* from ( "+
			"select w.id as id,r.mingc reny,to_char(min(f.shij),'yy-mm-dd hh:mi:ss')shij,to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')-to_date(to_char(f.shij,'yyyy-mm-dd'),'yyyy-mm-dd') as tians,x.zhi as tiansbj,"
			+"to_char(min(f.youxq),'YYYY-MM-DD')youxsj,getMainHtmlAlertAll('main','"+MainGlobal.getHomeContext(this)
			+"','Tongzwj','wenj',w.id, w.biaot,'',(x.zhi-(to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')-to_date(to_char(f.shij,'yyyy-mm-dd'),'yyyy-mm-dd')))) as biaot,getJiesdws(w.id,f.shij) jiesdws\n" +
			"from fabwjb f,renyxxb r,wenjb w,xitxxb x \n" + 
			"where f.renyxxb_id=r.id and f.wenjb_id=w.id and f.diancxxb_id = "+visit.getDiancxxb_id()+" and x.mingc='新闻天数设置' \n" +
			"and f.youxq>=sysdate\n" + 
			"group by w.id,r.mingc ,w.biaot ,f.shij,x.zhi order by shij desc ) s ";

		ResultSetList rs = con.getResultSetList(sql);
		
		
		int i = 0;
		
		String xuh = "";
		String reny = "";
		String strbiaot = "";
		String neir = "";
		String date ="";
		
		StringBuffer htmlstr = new StringBuffer();
		htmlstr.append("<tr>"+
		"<td class=\"news\" align=\"center\" style=\" border-bottom-style: solid; border-bottom-width: 1px; padding-left: 4px; padding-right: 4px; padding-top: 1px; padding-bottom: 1px\">"+
		"<b>"+
		"<font size=\"2\">序号</font></b></td>"+
		"<td class=\"news\" align=\"center\" style=\" border-bottom-style: solid; border-bottom-width: 1px; padding-left: 4px; padding-right: 4px; padding-top: 1px; padding-bottom: 1px\" width=\"78\">"+
		"<b>"+
		"<font size=\"2\">发布人员</font></b></td>"+
		"<td class=\"news\" align=\"center\" style=\" border-bottom-style: solid; border-bottom-width: 1px; padding-left: 4px; padding-right: 4px; padding-top: 1px; padding-bottom: 1px\" width=\"240\">"+
		"<b>"+
		"<font size=\"2\">标题</font></b></td>"+
		"<td class=\"news\" align=\"center\" style=\" border-bottom-style: solid; border-bottom-width: 1px; padding-left: 4px; padding-right: 4px; padding-top: 1px; padding-bottom: 1px\">"+
		"<b>"+
		"<font size=\"2\">发布时间</font></b></td>"+
		"</tr>");
		
		for( i=0;rs.next();i++){
				htmlstr.append("<tr>");
				if(rs.getInt("tians")<=rs.getInt("tiansbj")){
					strbiaot = "<img src='"+ MainGlobal.getHomeContext(this)+"/imgs/login/zdtgs/news.gif'" +">"+rs.getString("biaot");
				}else{
					strbiaot = rs.getString("biaot");
				}
//				if(wenj.equals("")){
//					xuh = "<td class=\"news\" align=\"center\" style=\" border-bottom-style: solid; border-bottom-width: 1px; padding-left: 4px; padding-right: 4px; padding-top: 1px; padding-bottom: 1px\">"+
//							"<b><font size=\"2\">无</font></b></td>";
//
//					reny ="<td class=\"news\" align=\"left\" style=\" border-bottom-style: solid; border-bottom-width: 1px; padding-left: 4px; padding-right: 4px; padding-top: 1px; padding-bottom: 1px\" width=\"78\">"+
//							"<font size=\"2\">无</font></td>";
//					
//					neir = "<td class=\"news\" align=\"left\" style=\" border-bottom-style: solid; border-bottom-width: 1px; padding-left: 4px; padding-right: 4px; padding-top: 1px; padding-bottom: 1px\" width=\"255\">"+
//							"无</td>";
//					
//					date = "<td class=\"news\" align=\"left\" style=\" border-bottom-style: solid; border-bottom-width: 1px; padding-left: 4px; padding-right: 4px; padding-top: 1px; padding-bottom: 1px\">" +
//							
//							"<font size=\"2\">无</font></td>";
//					
//				}else{
					xuh = "<td class=\"news\" align=\"center\" style=\" border-bottom-style: solid; border-bottom-width: 1px; padding-left: 4px; padding-right: 4px; padding-top: 1px; padding-bottom: 1px\">"+
							"<b><font size=\"2\">"+rs.getInt("rownum")+"</font></b></td>";

					reny ="<td class=\"news\" align=\"left\" style=\" border-bottom-style: solid; border-bottom-width: 1px; padding-left: 4px; padding-right: 4px; padding-top: 1px; padding-bottom: 1px\" width=\"78\">"+
							"<font size=\"2\">"+rs.getString("reny")+"</font></td>";
					
					neir = "<td class=\"news\" align=\"left\" style=\" border-bottom-style: solid; border-bottom-width: 1px; padding-left: 4px; padding-right: 4px; padding-top: 1px; padding-bottom: 1px\" width=\"240\">"+
							"<font size=\"2\">"+strbiaot+"</font></td>";
					
					date = "<td class=\"news\" align=\"left\" style=\" border-bottom-style: solid; border-bottom-width: 1px; padding-left: 4px; padding-right: 4px; padding-top: 1px; padding-bottom: 1px\">" +
							"<font size=\"2\">"+rs.getString("shij")+"</font></td>";
//				}
				htmlstr.append(xuh).append(reny).append(neir).append(date);
				htmlstr.append("</tr>");
		
		}

		con.Close();
		return htmlstr.toString();
	}
	
	private String strhtml="";
	
	public void getShowHtml1() {//返回页面数据。biaot,neir,fujbItem
		String title="文件通知";
		if (getWenj().equals("")){
			strhtml="";
		}else{
			StringBuffer str=new StringBuffer();
			/*String neir="<table><tr><hr></tr>"+getWenj()+"</table>";
			StringBuffer str=new StringBuffer();
			str.append("<table><tr> \n");
			str.append("<td width=50></td> \n");
			str.append("<td width=90% > \n");
			str.append("<table cellpadding=\"0\" cellspacing=\"0\" style=\"font-size: 10pt\" bgcolor=\"#FFFFFF\" border=\"0\" height=\"100%\" width=\"100%\"> \n");
			str.append("<tr><td height=10></td></tr> \n");
			str.append("<tr><td align=\"center\" style=\"font-size: 15pt\" height=\"70\" ><b>"+title+"</b></td></tr> \n");
			str.append("<tr><td  align=\"center\"  valign=top style=\"line-height: 150%\" >&nbsp;&nbsp;&nbsp;&nbsp; "+neir+"</td></tr>");
			str.append("<tr><td>&nbsp;<td></tr><tr><td>&nbsp;<td></tr><tr><td>&nbsp;<td></tr> \n");
			str.append("</table></td><td width=50></td> \n");
			str.append("</tr></table> \n");
			*/
			

			str.append("<table bgcolor='#95ABC3' height='99%' >\n" +
				"\t\t<tr>\n" + 
				"\t\t\t<td width=\"230\"></td>\n" + 
				"\t\t\t<td width=\"58%\">\n" + 
				"\t\t\t<table cellpadding=\"0\" cellspacing=\"0\" style=\"font-size: 10pt\"  border=\"0\" height=\"100%\" width=\"102%\">\n" + 
				"\t\t\t\t<tr>\n" + 
				"\t\t\t\t\t<td height=\"10\" width=\"584\"></td>\n" + 
				"\t\t\t\t</tr>\n" + 
				"\t\t\t\t<tr>\n" + 
				"\t\t\t\t\t<td align=\"center\" style=\"font-size: 15pt\" height=\"33\" width=\"584\"><b>文件通知</b></td>\n" + 
				"\t\t\t\t</tr>\n" + 
				"\t\t\t\t<tr>\n" + 
				"\t\t\t\t\t<td align=\"center\" valign=\"top\" style=\"line-height: 150%\" width=\"584\" height=\"35\"><hr>　</td>\n" + 
				"\t\t\t\t</tr>\n" + 
				"\t\t\t\t<tr>\n" + 
				"\t\t\t\t\t<td align=\"center\" valign=\"top\" style=\"line-height: 150%\" width=\"584\">\n" + 
				"\t\t\t\t\t<table style=\"padding-left: 4px; padding-right: 4px; padding-top: 1px; padding-bottom: 1px\" width=\"513\">");
			str.append(getWenj());
			str.append(
				"</table>\n" +
				"\t\t\t\t\t</td>\n" + 
				"\t\t\t\t</tr>\n" + 
				"\t\t\t\t<tr>\n" + 
				"\t\t\t\t\t<td width=\"584\">　</td>\n" + 
				"\t\t\t\t\t<td></td>\n" + 
				"\t\t\t\t</tr>\n" + 
				"\t\t\t\t<tr>\n" + 
				"\t\t\t\t\t<td width=\"584\">　</td>\n" + 
				"\t\t\t\t\t<td></td>\n" + 
				"\t\t\t\t</tr>\n" + 
				"\t\t\t\t<tr>\n" + 
				"\t\t\t\t\t<td width=\"584\">　</td>\n" + 
				"\t\t\t\t\t<td></td>\n" + 
				"\t\t\t\t</tr>\n" + 
				"\t\t\t</table>\n" + 
				"\t\t\t</td>\n" + 
				"\t\t\t<td width=\"135\"></td>\n" + 
				"\t\t</tr>\n" + 
				"\t</table>");

			strhtml=str.toString();
		}
	}
	
	
	public String getShowHtml() {//返回页面数据。
		getShowHtml1();
		if(strhtml.equals("")){
			StringBuffer str=new StringBuffer();
			str.append(" <table cellpadding=\"0\" cellspacing=\"0\" style=\"font-size: 10pt\"  border=\"0\" height=\"100%\" width=\"100%\"> \n");
			str.append(" <tr></tr><tr><td align=\"center\" style=\"font-size: 15pt\"  height=\"5%\"><b>空</b></td></tr> \n");
			str.append(" <tr><td align=\"left\" valign=\"top\">空</td></tr> \n");
			str.append(" <tr></tr></table> \n");
			
			return str.toString();
		}else{
			return strhtml;
		}
	}
//	________________________________________________________

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		} 
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString1("");
		}
		
	}
	public void pageValidate(PageEvent arg0) {
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
		
	}
   
}
