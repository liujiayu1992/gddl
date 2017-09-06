//chh 2008-03-17 修改查询语句与表格格式
package com.zhiren.pub.wenjgl;


import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Tongzwj extends BasePage {
	
	String id;
	

	
	public boolean getRaw() {
		return true;
	}

	
	public String getDXMoney(double _Money ){
		Money money=new Money();
		return money.NumToRMBStr1(_Money);
	}
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
			return getJiesd();
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
	
	
	private String getJiesd() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String biaot="";
		String biaot1="";
		String neir="";
		String neir1="";
		String yuanmc="";
		String url="";
		String fujbItem="";
		String fujbItem1="";
		String lngjiesbID = visit.getString1();
		
		String fabr="";
		String fabsj="";
		
//		String sql="select wenjb.id,neir,biaot,fujb.url,yuanmc\n" +
//		"from wenjb,fujb\n" + 
//		"where wenjb.id=fujb.wenjb_id and wenjb.id='"+lngjiesbID+"'";
		
		String sql="select id,biaot,neir from wenjb where id="+lngjiesbID;
		ResultSet rs=con.getResultSet(sql);
		 try {
				while(rs.next()){
					biaot="文件标题:"+rs.getString("biaot");
					biaot1=""+rs.getString("biaot");
					neir="文件内容:"+rs.getString("neir");
					neir1="&nbsp;&nbsp;&nbsp;&nbsp;"+rs.getString("neir");
					
					String sql1="select f.yuanmc,url from fujb f where f.wenjb_id="+rs.getLong("id");
					ResultSet rs1=con.getResultSet(sql1);
					int i=0;
					while(rs1.next()){
						i++;
						yuanmc=rs1.getString("yuanmc");
						url=rs1.getString("url");
						fujbItem=fujbItem+ "附件"+i+":"+ "<a  onclick=\"window.open('"+url+"')\" href=\"#\" >"+yuanmc+"</a><br>";
					}
					rs1.close();
					String sql2="select r.mingc,f.shij from fabwjb f,renyxxb r " +
						"where f.renyxxb_id=r.id and f.diancxxb_id="+visit.getDiancxxb_id()+" and wenjb_id="+rs.getLong("id")+"";
					ResultSet rs2=con.getResultSet(sql2);
					while(rs2.next()){
						fabr=rs2.getString("mingc");
						fabsj=rs2.getString("shij");
					}
					rs2.close();
				}
				getShowHtml1(biaot1,neir1,fujbItem,fabr,fabsj);
				fujbItem+="";
				setWenjnr(biaot+neir+fujbItem);
			} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			con.Close();
		}
//	 		
 
	 	String ArrHeader[][]=new String[3][1];
		ArrHeader[0]=new String[] {biaot};
		ArrHeader[1]=new String[] {neir};
		ArrHeader[2]=new String[] {fujbItem};


		int ArrWidth[]=new int[] {1000};

		Report rt=new Report();
		rt.setBody(new Table(ArrHeader,0,0,0));
		rt.body.setWidth(ArrWidth);
		rt.body.setRowHeight(150);
		 
		return rt.getAllPagesHtml();
	}
	
	private String strhtml="";
	
	public void getShowHtml1(String title,String neir,String fuj,String fabr,String fabsj) {//返回页面数据。biaot,neir,fujbItem
		StringBuffer str=new StringBuffer();
//		str.append("function showMessage() { \n");
//		str.append(" var str=\"\"; \n");
//		str.append(" str +=\"<table cellpadding=\\\"0\\\" cellspacing=\\\"0\\\" style=\\\"font-size: 10pt\\\"  bgcolor=\\\"#FFFFFF\\\" border=\\\"0\\\" height=\\\"100%\\\" width=\\\"100%\\\">\" \n");
//		str.append(" str += \"<tr></tr><tr><td align=\\\"center\\\" style=\\\"font-size: 15pt\\\"  height=\\\"5%\\\"><b>"+title+"</b></td></tr>\" \n");
//		str.append(" str += \"<tr><td align=\\\"left\\\" valign=\\\"top\\\">"+neir+"</td></tr>\" \n");
//		str.append(" str += \"<tr><td align=\\\"left\\\" valign=\\\"bottom\\\">"+fuj+"</td></tr><tr></tr></table>\" \n");
//		str.append(" return str;} \n");
		
		str.append("<table><tr> \n");
		str.append("<td width=50></td> \n");
		str.append("<td width=90% > \n");
		str.append("<table cellpadding=\"0\" cellspacing=\"0\" style=\"font-size: 10pt\" bgcolor=\"#FFFFFF\" border=\"0\" height=\"100%\" width=\"100%\"> \n");
		str.append("<tr><td height=10></td></tr> \n");
		str.append("<tr><td align=\"center\" style=\"font-size: 15pt\" height=\"70\" ><b>"+title+"</b></td></tr> \n");
		str.append("<tr><td height=30 align=right valign=top>发布人："+fabr+"&nbsp;&nbsp;&nbsp;&nbsp;发布时间："+fabsj+"<hr></td></tr> \n");
		str.append("<tr><td  align=\"left\"  valign=top style=\"line-height: 150%\" >&nbsp;&nbsp;&nbsp;&nbsp; "+neir+"</td></tr>");
		str.append("<tr><td>&nbsp;<td></tr><tr><td>&nbsp;<td></tr><tr><td>&nbsp;<td></tr> \n");
		str.append("<tr><td align=\"left\"><hr>"+fuj+"</td></tr></table></td><td width=50></td> \n");
		str.append("</tr></table> \n");
		
		strhtml=str.toString();
	}
	
	
	public String getShowHtml() {//返回页面数据。biaot,neir,fujbItem
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		getJiesd();
	
		if(strhtml.equals("")){
			StringBuffer str=new StringBuffer();
//			str.append("function showMessage() { \n");
//			str.append(" var str=\"\"; \n");
//			str.append(" str +=\"<table cellpadding=\\\"0\\\" cellspacing=\\\"0\\\"  style=\\\"font-size: 10pt\\\"   bgcolor=\\\"#FFFFFF\\\" border=\\\"0\\\" height=\\\"100%\\\" width=\\\"100%\\\">\" \n");
//			str.append(" str += \"<tr></tr><tr><td align=\\\"center\\\" style=\\\"font-size: 15pt\\\" height=\\\"5%\\\"><b>空</b></td></tr>\" \n");
//			str.append(" str += \"<tr><td align=\\\"center\\\" valign=\\\"top\\\">空</td></tr><tr></tr>\" \n");
//			str.append(" str += \"</table>\" \n");
//			str.append(" return str;} \n");
			
			str.append(" <table cellpadding=\"0\" cellspacing=\"0\" style=\"font-size: 10pt\"  bgcolor=\"#FFFFFF\" border=\"0\" height=\"100%\" width=\"100%\"> \n");
			str.append(" <tr></tr><tr><td align=\"center\" style=\"font-size: 15pt\"  height=\"5%\"><b>空</b></td></tr> \n");
			str.append(" <tr><td align=\"left\" valign=\"top\">空</td></tr> \n");
			str.append(" <tr><td align=\"left\" valign=\"bottom\">空</td></tr><tr></tr></table> \n");
			
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
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
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
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDefaultTree(null);
		}
		if (cycle.getRequestContext().getParameter("wenj") != null&&!cycle.getRequestContext().getParameter("wenj").equals("-1")) {
			visit.setString1(cycle.getRequestContext().getParameter("wenj"));
		}else{
			blnIsBegin = false;
			return;
		}
//		getToolbars();
		blnIsBegin = true;
	}
   
}
