package com.zhiren.main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.validate.Login;

/*
 * 作者：夏峥
 * 时间：2013-01-25
 * 描述：动态获得下载地址
 */

public  class Wenj extends BasePage implements PageValidateListener {
	private int msg = 1;

	public int getMsg() {
		return msg;
	}

	public void setMsg(int msg) {
		this.msg = msg;
	}

	public String getUserDanwmc(){
		Visit visit = (Visit)getPage().getVisit();
//		JDBCcon con = new JDBCcon();
		return visit.getDiancqc();
	}
//	
	
//	 判断是否是集团用户
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
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			//在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString1(null);
			visit.setString2(null);
			visit.setString3(null);
		}	
	}
	
//	得到电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(long diancmcId) {
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}
	
public String getWenj(){
		JDBCcon con = new JDBCcon();
		Date dat=new Date();//日期
		String strDate=DateUtil.FormatDate(dat);//日期字符
		Visit visit = (Visit)this.getPage().getVisit();
		String diancid = "";
		if(isJitUserShow()){
			diancid = "";
		}else if(isGongsUser()){
			diancid = " and (dc.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"or dc.shangjgsid= "+((Visit) getPage().getVisit()).getDiancxxb_id()+") and dc.ranlgs<>1 ";
		}else{//选电厂只刷新出该电厂
			diancid = " and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		int iAllLines=5;
		
		ResultSetList rs = con.getResultSetList("select zhi from xitxxb t where mingc  ='厂级系统查看通知最大记录' And zhuangt = 1");
		
		if (rs.next()){
			iAllLines = rs.getInt("zhi");
		}
		rs.close();
		
		
		String sql =
			"select w.id as id,r.mingc reny,to_char(min(f.shij),'YYYY-MM-DD HH24:MI:SS')shij,to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')-to_date(to_char(f.shij,'yyyy-mm-dd'),'yyyy-mm-dd') as tians,x.zhi as tiansbj,"
			+"to_char(min(f.youxq),'YYYY-MM-DD')youxsj,getMainHtmlAlertDC('main','"+MainGlobal.getHomeContext(this)
			+"','Tongzwj','wenj',w.id, w.biaot,to_char(f.shij,'yyyy-mm-dd'),(x.zhi-(to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')-to_date(to_char(f.shij,'yyyy-mm-dd'),'yyyy-mm-dd')))) as biaot,getJiesdws(w.id,f.shij) jiesdws\n" +
			"from fabwjb f,renyxxb r,wenjb w,xitxxb x \n" + 
			"where f.renyxxb_id=r.id and f.wenjb_id=w.id and f.diancxxb_id = "+visit.getDiancxxb_id()+" and x.mingc='新闻天数设置' \n" +
			"and f.youxq>=sysdate\n" + 
			"group by w.id,r.mingc ,w.biaot ,f.shij,x.zhi order by shij desc";

		
		rs = con.getResultSetList(sql);
		int i = 0;
		int j = 0;
		
		
		String wenj = "";
		String strbiaot = "";
//		String strShicdt=getShicdtLink();
//		if (strShicdt.length()>0) {//有市场动态的相关设置，在总行数中给市场动态留3行
//			iAllLines=iAllLines-3;
//		}
		
		for( i=0;rs.next();i++){
			if(i<iAllLines){
				if(rs.getInt("tians")<=rs.getInt("tiansbj")){
					strbiaot = "<img src='"+ MainGlobal.getHomeContext(this)+"/imgs/login/zdtgs/news.gif'" +">"+rs.getString("biaot");
				}else{
					strbiaot = rs.getString("biaot");
				}
				if(wenj.equals("")){
					wenj ="<tr><td class=\"news\">" + strbiaot+"&nbsp</td></tr>" ;
				}else{
					wenj = wenj+"<tr><td class=\"news\">" +strbiaot+"&nbsp</td></tr>" ;
				}
			}else{
				break;
			}
			j = i;
		}
//		for(int k =j;k<iAllLines;k++){
//			if(wenj.equals("")){
//				wenj="<tr><td>&nbsp;</td></tr>";
//			}else{
//				wenj=wenj+"<tr><td>&nbsp;</td></tr>";
//			}
//		}
		 IRequestCycle cycle = getPage().getRequestCycle();
		 String user = cycle.getRequestContext().getParameter("user");
		 String pwd = cycle.getRequestContext().getParameter("pwd");
		 if(user==null) user="unknown";
		 if(pwd==null) pwd="unknown";
		wenj=wenj+"<tr><td align=right class=\"news\"><a target=\"_blank\" title=\"更多...\"  href='"+MainGlobal.getHomeContext(this)+"/app?service=page/WenjAll&user="+user+"&pwd="+pwd+"'>更多...</a> &nbsp;</td></tr>";
//		wenj=wenj+getShicdtLink();
//		wenj = wenj+"<tr><td class=\"news\" align=\"right\"><a href=\"#\">更多 >></a></td></tr>";
		con.Close();
		return wenj;
	}
	
//	public String getShicdtLink(){
//		JDBCcon cn=new JDBCcon();
//		String strSz ="";
//		ResultSetList rsmc = cn.getResultSetList( "select distinct x.zhi from xitxxb x where x.mingc='首页显示市场动态链接'");
//		if(rsmc.next()){
//			strSz= rsmc.getString("zhi");
//		}
//		rsmc.close();
//		cn.Close();
//		
//		String wenj="";
//		if ("是".equals(strSz)){//如果设置了在“首页显示市场动态链接”显示，则生成相关链接
//			wenj ="<tr><td class=\"news\" id=\"GangkjgReport\" onclick=\"ShownewPage(this);\"><a href=\"#\">秦皇港岛煤炭市场行情价格周报</a></td></tr>" ;
//			wenj =wenj+"\n<tr><td class=\"news\" id=\"GuojmjReport\" onclick=\"ShownewPage(this);\"><a href=\"#\">环球煤炭电子交易平台NEWC指数</a></td></tr>" ;
//			wenj =wenj+"\n<tr><td class=\"news\" id=\"GuojyjReport\" onclick=\"ShownewPage(this);\"><a href=\"#\">国际原油价格速览</a></td></tr>" ;
//		}
//		return wenj;
//	}
	public String getOneTongz(String strWenjID ){
		StringBuffer str=new StringBuffer();	
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String biaot="";
		String neir="";
		String yuanmc="";
		String url="";
		String fujbItem="";
		String fabr="";
		String fabsj="";
		try {
			String sql="select id,biaot,neir from wenjb where id="+strWenjID;
			ResultSet rs=con.getResultSet(sql);
			while(rs.next()){
				biaot=""+rs.getString("biaot");
				neir="&nbsp;&nbsp;&nbsp;&nbsp;"+rs.getString("neir");
				
				String newurl=MainGlobal.getHomeContext(this);
				String sql1="select f.yuanmc,NVL(SUBSTR(URL,0,INSTR('"+newurl+"','service=page')-1),'"+newurl+"')||SUBSTR(URL,INSTR(URL,'service=page')-1) newurl from fujb f where f.wenjb_id="+rs.getLong("id");
				ResultSet rs1=con.getResultSet(sql1);
				int i=0;
				while(rs1.next()){
					i++;
					yuanmc=rs1.getString("yuanmc");
					url=rs1.getString("url");
					System.out.println("url:"+url);
					fujbItem=fujbItem+ "附件"+i+":"+ "<a  onclick=\"window.open('"+url+"')\" href=\"#\" >"+yuanmc+"</a><br>";
				}
				rs1.close();
				String sql2="select r.mingc,f.shij from fabwjb f,renyxxb r where f.renyxxb_id=r.id and f.diancxxb_id="+visit.getDiancxxb_id()+" and wenjb_id="+rs.getLong("id")+"";
				ResultSet rs2=con.getResultSet(sql2);
				while(rs2.next()){
					fabr=rs2.getString("mingc");
					fabsj=rs2.getString("shij");
				}
				rs2.close();
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		str.append("<table ><tr> \n");
		str.append("<td width=20></td> \n");
		str.append("<td width=90% > \n");
		str.append("<table cellpadding=\"0\" cellspacing=\"0\" style=\"font-size: 10pt\" border=\"0\" height=\"100%\" width=\"100%\"> \n");
		str.append("<tr><td height=5></td></tr> \n");
		str.append("<tr><td align=\"center\" style=\"font-size: 15pt;color: #FF0000\" height=\"50\" ><b>"+biaot+"</b></td></tr> \n");
		str.append("<tr><td height=30 align=right valign=top >发布人："+fabr+"&nbsp;&nbsp;&nbsp;&nbsp;发布时间："+fabsj+"<hr></td></tr> \n");
		str.append("<tr><td  align=\"left\"  valign=top style=\"line-height: 150%\" >&nbsp;&nbsp;&nbsp;&nbsp; "+neir+"</td></tr>");
		str.append("<tr><td>&nbsp;<td></tr><tr><td>&nbsp;<td></tr> \n");
		str.append("<tr><td align=\"left\"><hr>"+fujbItem+"</td></tr></table></td><td width=20></td> \n");
		str.append("</tr></table> \n");
		return str.toString();
	}
	public String getTongz() {//返回页面数据。biaot,neir,fujbItem
		StringBuffer str=new StringBuffer();	
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		try {
			 //得到置顶的个文件
			 String strsql="select w.id as id \n" + 
				"      from fabwjb f,wenjb w ,renyxxb ry\n" + 
				"      where f.wenjb_id=w.id and f.diancxxb_id ="+visit.getDiancxxb_id()+"\n" + 
				"      and ry.id=f.renyxxb_id\n" + 
				"      and f.zhid=1\n" + 
				"      and f.youxq>=sysdate order by f.shij desc\n"  ;
			 ResultSet rs=con.getResultSet(strsql);
			 while (rs.next()){
				 if (str.length()>0){
					str.append("\n<br>\n"); 
				 }
				 str.append(getOneTongz(rs.getString("id")));
			 }
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return str.toString();
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
