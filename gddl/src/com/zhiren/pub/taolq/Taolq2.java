package com.zhiren.pub.taolq;


import java.io.File;
import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Taolq2 extends BasePage  {
//	private boolean xiansnr = false;
//    public void xiansnr(IRequestCycle cycle) {
//    	xiansnr = true;
//    }
    private boolean shuax = false;
    public void shuax(IRequestCycle cycle) {
    	shuax = true;
    }
	public void submit(IRequestCycle cycle) {
		if (shuax) {
//			shuax = false;
			loadDate(((Visit) getPage().getVisit()).getString1());
        }
//		if (xiansnr) {
//			xiansnr = false;
//        }
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if(shuax){
			shuax = false;
		}else if(cycle.getRequestContext().getParameter("shanc")!=null){//如果是删除操作
			String cuncm="";//实际存储名称(相对)
			String shanc_id=cycle.getRequestContext().getParameter("shanc");
			JDBCcon con = new JDBCcon();
			String sql=
				"select mingc\n" +
				"from fujb\n" + 
				"where fujb.wenjb_id ="+shanc_id;
			ResultSet rs=con.getResultSet(sql);
			try{
				while(rs.next()){
					cuncm=Xiewj.home+rs.getString(1);
//					先删除文件系统中的文件file
					File file=new File(cuncm);
					if(file.exists()){
						file.delete();
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			//删除附件表

			sql="delete\n" +
			"from fujb\n" + 
			"where fujb.wenjb_id= "+shanc_id;
			con.getDelete(sql);
			//删除主文件
			sql=
				"delete from tiezb where id="+shanc_id;
			con.getDelete(sql);
			loadDate(visit.getString1());
		}else{//连接调用
			visit.setString1(cycle.getRequestContext().getParameter("tiezb_id"));
			visit.setString2(cycle.getRequestContext().getParameter("lx"));//权限2为管理组
			loadDate(visit.getString1());//获得参数
			//查看计数器加一
			JDBCcon con=new JDBCcon();
			String sql="update tiezb\n" +
			"set jisq=jisq+1\n" + 
			"where id="+visit.getString1();
			con.getUpdate(sql);
			con.Close();
		}
	}
	public String getfuid(){
		return ((Visit) getPage().getVisit()).getString1();
	}
	private String getPageUrl(String page){
		String url="";
		url=this.getPage().getRequestCycle().getRequestContext().getRequest().getRequestURL().toString();
		url+="?service=page/"+page;
		return url;
	}
	private void loadDate(String tiezb_id){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String sql="select tiezb.id,biaot,neir,diancxxb.mingc ,renyxxb.quanc,to_char(shij,'yyyy-mm-dd HH24:MI:SS')shij\n" +
		"from tiezb,renyxxb,diancxxb\n" + 
		"where tiezb.diancxxb_id=diancxxb.id and  tiezb.renyxxb_id=renyxxb.id and (tiezb.id="+tiezb_id+" or tiezb.fuid="+tiezb_id+")\n" + 
		"order by tiezb.fuid,tiezb.shij desc\n" 
		;
		ResultSet rs=con.getResultSet(sql);
		int i=1;
		html="'";
		String tmp="";
		
		JDBCcon con1=new JDBCcon();
		try{
			while(rs.next()){
				String biaot=rs.getString("biaot");
				String neir=rs.getString("neir");
				String diancmc=rs.getString("mingc");
				String quanc=rs.getString("quanc");
				String shij=rs.getString("shij");
				String id=rs.getString("id");
				//附件
				String sql_fuj="select yuanmc,url\n" +
				"from fujb\n" + 
				"where wenjb_id="+id;
				ResultSet rs1=con1.getResultSet(sql_fuj);
				String html_fuj="";
				while(rs1.next()){
					html_fuj+="<a href="+rs1.getString("url")+">"+rs1.getString("yuanmc")+"</a>";
				}
				html_fuj+="<br><br>";
				
				//html
				if(i==1){//主题
					tmp+="<b>"+i+"，"+biaot+"</b><br><br>";
					tmp+=neir+"<br><br>";
					tmp+=html_fuj;
					tmp+=diancmc+quanc+"  "+shij;
					tmp+="<hr>";
				}else{
					if(visit.getString2().equals("2")){//管理组
						tmp+="<b>"+i+"，"+biaot+"</b><br><br>";
						tmp+=neir+"<br><br>";
						tmp+=html_fuj;
						tmp+=diancmc+"  "+quanc+"  "+shij+" <a href="+getPageUrl(this.getPageName())+"&shanc="+id+">删除</a>";//删除功能没写可以参照主文件删除
						tmp+="<hr>";
					}else{
						tmp+="<b>"+i+"，"+biaot+"</b><br><br>";
						tmp+=neir+"<br><br>";
						tmp+=html_fuj;
						tmp+=diancmc+"  "+quanc+"  "+shij;//删除功能没写可以参照主文件删除
						tmp+="<hr>";
					}
					
				}
				i++;
			}
			tmp=tmp.replaceAll("'", "\\\\'");
			html+=tmp+"'";
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con1.Close();
			con.Close();
		}
	}
	private String html;
	public String gethtml(){
		return html;
	}
	public void sethtml(String value){
		this.html=value;
	}
////  页面变化记录
//    private String Change;
//    public String getChange() {
//    	return Change;
//    }
//    public void setChange(String change) {
//    	Change = change;
//    }
//    private String wenjnr;
//	public String getWenjnr() {
//		if(wenjnr==null||wenjnr.equals("")){
//			wenjnr="var wenjnr='';";
//		}
//		return wenjnr;
//	}
//	public void setWenjnr(String wenjnr) {
//		this.wenjnr = wenjnr;
//	}
    
}
