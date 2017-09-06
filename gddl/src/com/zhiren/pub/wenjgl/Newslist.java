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

public class Newslist extends BasePage {
	
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
			return getNews();
	}
	
	private String getNews() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String biaot="";
//		String neir="";
		String fujbItem="";
//		String lngjiesbID = visit.getString1();
		
		String fabr="";
		String fabsj="";
		int tians = 0;//新闻发布天数
		int tiansbj = 0;//新闻天数标准
		StringBuffer str=new StringBuffer();
		
		String sql="select wenjb.id,neir,to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')-to_date(to_char(f.shij,'yyyy-mm-dd'),'yyyy-mm-dd') as tians,x.zhi as tiansbj,"
				+ "		   getMainHtmlAlert('news','"+MainGlobal.getHomeContext(this)+"','Tongzwj','wenj',wenjb.id,wenjb.biaot,to_char(f.shij,'yyyy-mm-dd'),"
				+ "			   (x.zhi-(to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')-to_date(to_char(f.shij,'yyyy-mm-dd'),'yyyy-mm-dd')))) as biaot,to_char(f.shij,'yyyy-mm-dd') as shij \n" 
				+ "	  from wenjb,fabwjb f,renyxxb r,xitxxb x \n"
				+ "  where f.renyxxb_id=r.id and f.wenjb_id=wenjb.id and x.mingc='新闻天数设置' and f.diancxxb_id="+visit.getDiancxxb_id()+" order by shij desc ";
		ResultSet rs=con.getResultSet(sql);
		 try {
				while(rs.next()){
					tians = rs.getInt("tians");
					tiansbj = rs.getInt("tiansbj");
					biaot=rs.getString("biaot");
//					neir1=rs.getString("neir");
					fabsj=rs.getString("shij");
					
					if(tians<=tiansbj){
						str.append("<tr><td align=\"left\" width=\"70%\"  valign=top style=\"line-height: 150%\" ><img src='"+ MainGlobal.getHomeContext(this)+"/imgs/login/zdtgs/news.gif'" +">"+biaot+"</td>");
					}else{
						str.append("<tr><td align=\"left\" width=\"70%\"  valign=top style=\"line-height: 150%\" >"+biaot+"</td>");
					}
//					str.append("<td align=\"left\" width=\"30%\" valign=top style=\"line-height: 150%\" >"+fujbItem+"</td>");
//					str.append("<td align=\"left\" width=\"20%\" valign=top style=\"line-height: 150%\" >"+fabr+"</td>");
					str.append("<td align=\"left\" width=\"30%\" valign=top style=\"line-height: 150%\" >"+fabsj+"</td>");
					
				}
				strhtml=str.toString();
				fujbItem+="";
			} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			con.Close();
		}
 
	 	String ArrHeader[][]=new String[4][1];
		ArrHeader[0]=new String[] {biaot};
		ArrHeader[1]=new String[] {fujbItem};
		ArrHeader[2]=new String[] {fabr};
		ArrHeader[3]=new String[] {fabsj};

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
		
		str.append("<tr><td align=\"left\" width=\"70%\"  valign=top style=\"line-height: 150%\" >"+title+"</td>");
//		str.append("<td align=\"left\" width=\"30%\" valign=top style=\"line-height: 150%\" >"+fuj+"</td>");
//		str.append("<td align=\"left\" width=\"14%\" valign=top style=\"line-height: 150%\" >"+fabr+"</td>");
		str.append("<td align=\"left\" width=\"30%\" valign=top style=\"line-height: 150%\" >"+fabsj+"</td>");
		
		strhtml=str.toString();
	}
	
	
	public String getShowHtml() {//返回页面数据。biaot,neir,fujbItem
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		getNews();
		StringBuffer str=new StringBuffer();
		
		if(strhtml.equals("")){
			
			str.append(" <table cellpadding=\"0\" cellspacing=\"0\" style=\"font-size: 10pt\"  bgcolor=\"#FFFFFF\" border=\"0\" height=\"100%\" width=\"100%\"> \n");
			str.append(" <tr></tr><tr><td align=\"center\" style=\"font-size: 15pt\"  height=\"5%\"><b>空</b></td></tr> \n");
			str.append(" <tr><td align=\"left\" valign=\"top\">空</td></tr> \n");
			str.append(" <tr><td align=\"left\" valign=\"bottom\">空</td></tr><tr></tr></table> \n");
			
		}else{
			
			str.append("<table><tr> \n");
			str.append("<td width=50></td> \n");
			str.append("<td width=100% > \n");
			str.append("<table cellpadding=\"0\" cellspacing=\"0\" style=\"font-size: 10pt\" bgcolor=\"#FFFFFF\" border=\"0\" height=\"100%\" width=\"100%\"> \n");
			str.append("<tr><td height=10 width=60%></td><td width=40%></td></tr> \n");
//			str.append("<tr><td align=\"center\" style=\"font-size: 15pt\" height=\"70\" ><b>"+title+"</b></td></tr> \n");
			str.append("<tr><td height=30 width=\"100%\" align=left valign=top colspan=2>新闻频道<hr></td><td></td></tr> \n");
			
			str.append(strhtml);
			
			str.append("</tr></table></td> \n");
//			str.append("<tr><td  align=\"left\"  valign=top style=\"line-height: 150%\" >&nbsp;&nbsp;&nbsp;&nbsp; "+neir+"</td></tr>");
			str.append("<tr><td>&nbsp;<td></tr><tr><td>&nbsp;<td></tr><tr><td>&nbsp;<td></tr> \n");
			str.append("</tr></table> \n");
			
		}
		
		return str.toString();
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
//		if (cycle.getRequestContext().getParameter("wenj") != null&&!cycle.getRequestContext().getParameter("wenj").equals("-1")) {
//			visit.setString1(cycle.getRequestContext().getParameter("wenj"));
//		}else{
//			blnIsBegin = false;
//			return;
//		}
//		getToolbars();
		blnIsBegin = true;
	}
   
}
