package com.zhiren.jt.zdt.zonghcx.tupzs;


import java.sql.ResultSet;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.asset.ExternalAsset;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.main.Visit;

public class Tupzs extends BasePage  {
	private String id="";
	
	public IAsset getPio(){
		String tup="";
		if(id.equals("")){
			tup="";
		}else if(!getShebtp().equals("")){
			tup=getShebtp();
		}else if(!getMeictp().equals("")){
			tup=getMeictp();
		}
		String src = getHomeContext(getPage())+"/"+getluj()+"/"+tup;
		return new ExternalAsset(src,null); 
	}
	public String getluj(){
		Visit visit = (Visit) getPage().getVisit();
		String luj="";
		String mingc="";
		if(!getShebtp().equals("")){
			mingc="设备图片上传路径";
		}else if(!getMeictp().equals("")){
			mingc="煤场图片上传路径";
		}
		JDBCcon con=new JDBCcon();
		String sql=
			"select zhi \n" +
			"from xitxxb \n" + 
			"where mingc='"+mingc+"'";
		ResultSet rs=con.getResultSet(sql);
		try{
			if(rs.next()){
				luj=rs.getString("zhi");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return luj;
	}

	public String getShebtp(){
		Visit visit = (Visit) getPage().getVisit();
		String shebtp="";
		JDBCcon con=new JDBCcon();
		String sql=
			"select shebzp\n" +
			"from jiexsbb\n" + 
			"where id="+id+" and shebzp like '%.%'";
		ResultSet rs=con.getResultSet(sql);
		try{
			if(rs.next()){
				shebtp=rs.getString("shebzp");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return shebtp;
	}
	
	public String getMeictp(){
		Visit visit = (Visit) getPage().getVisit();
		String meictp="";
		JDBCcon con=new JDBCcon();
		String sql=
			"select meict\n" +
			"from meicb\n" + 
			"where id="+id+" and meict like '%.%'";
		ResultSet rs=con.getResultSet(sql);
		try{
			if(rs.next()){
				meictp=rs.getString("meict");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return meictp;
	}
//	public IAsset getShebtp(){
//		Visit visit = (Visit) getPage().getVisit();
//		String shebtp="";
//		JDBCcon con=new JDBCcon();
//		String sql=
//			"select shebzp\n" +
//			"from jiexsbb\n" + 
//			"where id="+id;
//		ResultSet rs=con.getResultSet(sql);
//		try{
//			if(rs.next()){
//				shebtp=rs.getString("shebzp");
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			con.Close();
//		}
//		String src = getHomeContext(getPage())+"/ftp/"+shebtp;
//		return new ExternalAsset(src,null); 
//	}
//	public IAsset getMeictp(){
//		Visit visit = (Visit) getPage().getVisit();
//		String meictp="";
//		JDBCcon con=new JDBCcon();
//		String sql=
//			"select meict\n" +
//			"from meicb\n" + 
//			"where id="+id;
//		ResultSet rs=con.getResultSet(sql);
//		try{
//			if(rs.next()){
//				meictp=rs.getString("meict");
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			con.Close();
//		}
//		String src = getHomeContext(getPage())+"/ftp/"+meictp;
//		return new ExternalAsset(src,null); 
//	}
	public static String getHomeContext(IPage page) {
		return page.getRequestCycle().getRequestContext().getRequest()
				.getScheme()
				+ "://"
				+ page.getRequestCycle().getRequestContext().getServerName()
				+ ":"
				+ page.getRequestCycle().getRequestContext().getServerPort()
				+ page.getEngine().getContextPath();
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {//显示
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			
		}
		if(cycle.getRequestContext().getParameters("jiexsbb_id") !=null) {
			visit.setString1(cycle.getRequestContext().getParameters("jiexsbb_id")[0]);
			id =visit.getString1();
        }else{
        	if(visit.getString1().equals("")) {
        		id =visit.getString1();
            }
        }
		if(cycle.getRequestContext().getParameters("meicb_id") !=null) {
			visit.setString1(cycle.getRequestContext().getParameters("meicb_id")[0]);
			id =visit.getString1();
        }else{
        	if(visit.getString1().equals("")) {
        		id =visit.getString1();
            }
        }
	}
}
